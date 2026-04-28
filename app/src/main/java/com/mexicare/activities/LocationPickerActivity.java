package com.mexicare.activities;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mexicare.R;
import com.mexicare.adapters.OrganizacionesAdapter;
import com.mexicare.api.ApiClient;
import com.mexicare.api.AuthAPI;
import com.mexicare.models.Organizacion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

public class LocationPickerActivity extends AppCompatActivity {
    
    private static final String TAG = "LocationPickerActivity";
    private RecyclerView recyclerViewOrganizaciones;
    private Button btnVolverMapa;
    private MapView mapView;
    private List<Organizacion> organizaciones;
    private OrganizacionesAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "=== INICIO LocationPickerActivity onCreate ===");
        
        try {
            Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
            
            Log.d(TAG, "1. Llamando super.onCreate");
            super.onCreate(savedInstanceState);
            
            Log.d(TAG, "2. Estableciendo layout activity_location_picker");
            setContentView(R.layout.activity_location_picker);
            
            initViews();
            setupMap();
            cargarOrganizaciones();
            setupClickListeners();
            
            Log.d(TAG, "=== LocationPickerActivity iniciada exitosamente ===");
            
        } catch (Exception e) {
            Log.e(TAG, "ERROR en LocationPickerActivity onCreate", e);
        }
    }
    
    private void initViews() {
        recyclerViewOrganizaciones = findViewById(R.id.recyclerViewOrganizaciones);
        btnVolverMapa = findViewById(R.id.btnVolverMapa);
        mapView = findViewById(R.id.mapView);
        organizaciones = new ArrayList<>();
    }
    
    private void setupMap() {
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        
        IMapController mapController = mapView.getController();
        mapController.setZoom(10.0);
        
        GeoPoint cdmx = new GeoPoint(19.4326, -99.1332);
        mapController.setCenter(cdmx);
        
        MyLocationNewOverlay locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
        locationOverlay.enableMyLocation();
        mapView.getOverlays().add(locationOverlay);
    }
    
    private void cargarOrganizaciones() {
        int medicinaId = getIntent().getIntExtra("medicina_id", -1);
        Log.d(TAG, "=== CARGANDO ORGANIZACIONES ===");
        Log.d(TAG, "Medicina ID recibido: " + medicinaId);
        
        AuthAPI api = ApiClient.getClient().create(AuthAPI.class);
        Integer filtroMedicina = medicinaId > 0 ? medicinaId : null;
        Log.d(TAG, "Filtro a enviar al servidor: " + filtroMedicina);
        
        Call<List<Organizacion>> call = api.getOrganizaciones(filtroMedicina);
        
        call.enqueue(new Callback<List<Organizacion>>() {
            @Override
            public void onResponse(Call<List<Organizacion>> call, Response<List<Organizacion>> response) {
                Log.d(TAG, "Respuesta recibida del servidor");
                if (response.isSuccessful() && response.body() != null) {
                    organizaciones.clear();
                    organizaciones.addAll(response.body());
                    
                    Log.d(TAG, "Organizaciones cargadas: " + organizaciones.size());
                    
                    if (organizaciones.isEmpty()) {
                        Toast.makeText(LocationPickerActivity.this, "No hay organizaciones con esta medicina en inventario", Toast.LENGTH_LONG).show();
                    }
                    
                    addOrganizationMarkers();
                    setupRecyclerView();
                } else {
                    Log.e(TAG, "Error al cargar organizaciones: " + response.code());
                    Toast.makeText(LocationPickerActivity.this, "Error al cargar organizaciones", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<List<Organizacion>> call, Throwable t) {
                Log.e(TAG, "Error de conexión al cargar organizaciones", t);
                Toast.makeText(LocationPickerActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    
    private void addOrganizationMarkers() {
        mapView.getOverlays().clear();
        
        MyLocationNewOverlay locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
        locationOverlay.enableMyLocation();
        mapView.getOverlays().add(locationOverlay);
        
        for (Organizacion org : organizaciones) {
            GeoPoint point = new GeoPoint(org.getLatitud(), org.getLongitud());
            
            Marker marker = new Marker(mapView);
            marker.setPosition(point);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setTitle(org.getNombre());
            marker.setSnippet("🏥 Organización activa");
            
            mapView.getOverlays().add(marker);
        }
        mapView.invalidate();
    }
    
    private void setupRecyclerView() {
        if (recyclerViewOrganizaciones != null) {
            recyclerViewOrganizaciones.setLayoutManager(new LinearLayoutManager(this));
            
            adapter = new OrganizacionesAdapter(organizaciones);
            recyclerViewOrganizaciones.setAdapter(adapter);
            
            Log.d(TAG, "RecyclerView configurado con " + organizaciones.size() + " organizaciones");
        }
    }
    
    private void setupClickListeners() {
        if (btnVolverMapa != null) {
            btnVolverMapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "=== CLICK EN ACTUALIZAR MAPA ===");
                    cargarOrganizaciones();
                    Toast.makeText(LocationPickerActivity.this, "Actualizando organizaciones...", Toast.LENGTH_SHORT).show();
                }
            });
        }
        
        Button btnVolver = findViewById(R.id.btnVolver);
        Log.d(TAG, "Buscando botón btnVolver en LocationPicker...");
        if (btnVolver != null) {
            Log.d(TAG, "Botón VOLVER encontrado en LocationPicker");
            btnVolver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "=== CLICK EN VOLVER - LOCATION PICKER ===");
                    try {
                        finish();
                        Log.d(TAG, "LocationPicker cerrado exitosamente");
                    } catch (Exception e) {
                        Log.e(TAG, "ERROR al cerrar LocationPicker", e);
                    }
                }
            });
        } else {
            Log.e(TAG, "ERROR: Botón VOLVER no encontrado en LocationPicker");
        }
    }
    
    @Override
    protected void onStart() {
        Log.d(TAG, "=== LocationPickerActivity onStart ===");
        super.onStart();
    }
    
    @Override
    protected void onResume() {
        Log.d(TAG, "=== LocationPickerActivity onResume ===");
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }
    
    @Override
    public void onBackPressed() {
        Log.d(TAG, "=== onBackPressed ===");
        super.onBackPressed();
    }
}