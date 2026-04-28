package com.mexicare.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.mexicare.R;
import com.mexicare.models.Organizacion;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

public class OrganizationMapActivity extends AppCompatActivity {
    
    private static final String TAG = "OrganizationMapActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    
    private MapView mapView;
    private List<Organizacion> organizaciones;
    private Marker selectedMarker;
    
    private TextView tvSelectedOrg;
    private TextView tvCoordinates;
    private Button btnDonarAqui;
    private Button btnComoLlegar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        
        setContentView(R.layout.activity_organizations_map);
        
        Log.d(TAG, "Iniciando OrganizationMapActivity con OpenStreetMap");
        
        initViews();
        setupMap();
        initOrganizaciones();
        addOrganizationMarkers();
        setupButtons();
    }
    
    private void initViews() {
        mapView = findViewById(R.id.mapView);
        tvSelectedOrg = findViewById(R.id.tvSelectedOrg);
        tvCoordinates = findViewById(R.id.tvCoordinates);
        btnDonarAqui = findViewById(R.id.btnDonarAqui);
        btnComoLlegar = findViewById(R.id.btnComoLlegar);
        
        tvSelectedOrg.setText("📍 Selecciona una organización en el mapa");
        tvCoordinates.setText("Toca un marcador para ver coordenadas");
    }
    
    private void setupMap() {
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        
        IMapController mapController = mapView.getController();
        mapController.setZoom(12.0);
        
        // Centrar en Ciudad de México
        GeoPoint cdmx = new GeoPoint(19.4326, -99.1332);
        mapController.setCenter(cdmx);
        
        // Habilitar rotación
        RotationGestureOverlay rotationGestureOverlay = new RotationGestureOverlay(mapView);
        rotationGestureOverlay.setEnabled(true);
        mapView.getOverlays().add(rotationGestureOverlay);
        
        // Agregar overlay de ubicación
        MyLocationNewOverlay locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
        locationOverlay.enableMyLocation();
        mapView.getOverlays().add(locationOverlay);
        
        // Listener para clicks largos (agregar organización)
        mapView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Se implementaría aquí la funcionalidad de agregar organización
                Toast.makeText(OrganizationMapActivity.this, 
                    "Mantén presionado para agregar organización (función en desarrollo)", 
                    Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
    
    private void initOrganizaciones() {
        organizaciones = new ArrayList<>();
        organizaciones.add(new Organizacion(1, 19.4326, -99.1332, "Cruz Roja CDMX", "cruzroja", "password123", true));
        organizaciones.add(new Organizacion(2, 19.4285, -99.1277, "DIF Central", "difcentral", "password123", true));
        organizaciones.add(new Organizacion(3, 19.4200, -99.1500, "Cáritas Diocesana", "caritas", "password123", true));
        
        Log.d(TAG, "Inicializadas " + organizaciones.size() + " organizaciones");
    }
    
    private void addOrganizationMarkers() {
        for (Organizacion org : organizaciones) {
            GeoPoint point = new GeoPoint(org.getLatitud(), org.getLongitud());
            
            Marker marker = new Marker(mapView);
            marker.setPosition(point);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setTitle(org.getNombre());
            marker.setSnippet("Lat: " + String.format("%.6f", org.getLatitud()) + 
                            ", Lng: " + String.format("%.6f", org.getLongitud()));
            
            marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker clickedMarker, MapView mapView) {
                    selectedMarker = clickedMarker;
                    updateSelectedOrganizationInfo(clickedMarker);
                    return true;
                }
            });
            
            mapView.getOverlays().add(marker);
        }
        
        mapView.invalidate();
        Log.d(TAG, "Agregados " + organizaciones.size() + " marcadores al mapa");
    }
    
    private void updateSelectedOrganizationInfo(Marker marker) {
        tvSelectedOrg.setText("📍 " + marker.getTitle());
        GeoPoint position = marker.getPosition();
        tvCoordinates.setText("Lat: " + String.format("%.6f", position.getLatitude()) + 
                            ", Lng: " + String.format("%.6f", position.getLongitude()));
        
        Toast.makeText(this, "Organización seleccionada: " + marker.getTitle(), Toast.LENGTH_SHORT).show();
    }
    
    private void setupButtons() {
        if (btnDonarAqui != null) {
            btnDonarAqui.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedMarker != null) {
                        Toast.makeText(OrganizationMapActivity.this, 
                            "Redirigiendo a formulario de donación para: " + selectedMarker.getTitle(), 
                            Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(OrganizationMapActivity.this, 
                            "Selecciona una organización en el mapa", 
                            Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        
        if (btnComoLlegar != null) {
            btnComoLlegar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedMarker != null) {
                        GeoPoint position = selectedMarker.getPosition();
                        Toast.makeText(OrganizationMapActivity.this, 
                            "Abriendo navegación a: " + selectedMarker.getTitle() + "\n" +
                            "Coordenadas: " + String.format("%.6f, %.6f", 
                                position.getLatitude(), position.getLongitude()), 
                            Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(OrganizationMapActivity.this, 
                            "Selecciona una organización en el mapa", 
                            Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de ubicación concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}