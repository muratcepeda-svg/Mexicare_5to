package com.mexicare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.mexicare.R;
import com.mexicare.api.ApiClient;
import com.mexicare.api.AuthAPI;
import com.mexicare.models.CatalogoItem;
import com.mexicare.models.InventarioItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class RequesterFormActivity extends AppCompatActivity {
    
    private static final String TAG = "RequesterFormActivity";
    private EditText etCantidad;
    private Spinner spinnerMedicina;
    private Button btnBuscarDisponibilidad;
    private TextView tvResultados;
    private List<CatalogoItem> catalogoMedicinas;
    private ArrayAdapter<CatalogoItem> spinnerAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "=== INICIO RequesterFormActivity onCreate ===");
        
        try {
            Log.d(TAG, "1. Llamando super.onCreate");
            super.onCreate(savedInstanceState);
            
            Log.d(TAG, "2. Estableciendo layout activity_requester_form");
            setContentView(R.layout.activity_requester_form);
            
            initViews();
            setupClickListeners();
            cargarCatalogo();
            
            Log.d(TAG, "=== RequesterFormActivity iniciada exitosamente ===");
            
        } catch (Exception e) {
            Log.e(TAG, "ERROR en RequesterFormActivity onCreate", e);
        }
    }
    
    private void initViews() {
        etCantidad = findViewById(R.id.etCantidad);
        spinnerMedicina = findViewById(R.id.spinnerMedicina);
        btnBuscarDisponibilidad = findViewById(R.id.btnBuscarDisponibilidad);
        tvResultados = findViewById(R.id.tvResultados);
        
        catalogoMedicinas = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, catalogoMedicinas);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedicina.setAdapter(spinnerAdapter);
    }
    
    private void setupClickListeners() {
        if (btnBuscarDisponibilidad != null) {
            btnBuscarDisponibilidad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "=== CLICK EN BUSCAR DISPONIBILIDAD ===");
                    buscarDisponibilidad();
                }
            });
        }
        
        Button btnVolver = findViewById(R.id.btnVolver);
        if (btnVolver != null) {
            btnVolver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "=== CLICK EN VOLVER AL INICIO - REQUESTER ===");
                    try {
                        Intent intent = new Intent(RequesterFormActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        Log.e(TAG, "ERROR al navegar a MainActivity desde RequesterForm", e);
                    }
                }
            });
        }
    }
    
    private void buscarDisponibilidad() {
        if (spinnerMedicina.getSelectedItem() == null) {
            Toast.makeText(this, "Por favor selecciona una medicina", Toast.LENGTH_SHORT).show();
            return;
        }
        
        CatalogoItem medicinaSeleccionada = (CatalogoItem) spinnerMedicina.getSelectedItem();
        int medicinaId = medicinaSeleccionada.getId();
        
        Log.d(TAG, "Buscando disponibilidad para medicina ID: " + medicinaId);
        tvResultados.setText("Buscando...");
        
        AuthAPI api = ApiClient.getClient().create(AuthAPI.class);
        Call<List<InventarioItem>> call = api.getInventario(null);
        
        call.enqueue(new Callback<List<InventarioItem>>() {
            @Override
            public void onResponse(Call<List<InventarioItem>> call, Response<List<InventarioItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<InventarioItem> inventario = response.body();
                    StringBuilder resultado = new StringBuilder();
                    int count = 0;
                    
                    for (InventarioItem item : inventario) {
                        if (item.getId() == medicinaId || 
                            (item.getNombreComercial() != null && 
                             medicinaSeleccionada.getNombreComercial() != null &&
                             item.getNombreComercial().equals(medicinaSeleccionada.getNombreComercial()))) {
                            resultado.append("• ").append(item.getOrganizacion())
                                    .append(": ").append(item.getCantidad())
                                    .append(" unidades\n");
                            count++;
                        }
                    }
                    
                    if (count == 0) {
                        tvResultados.setText("❌ No hay organizaciones con esta medicina en stock");
                    } else {
                        tvResultados.setText(resultado.toString());
                    }
                } else {
                    tvResultados.setText("Error al consultar disponibilidad");
                }
            }
            
            @Override
            public void onFailure(Call<List<InventarioItem>> call, Throwable t) {
                Log.e(TAG, "Error al buscar disponibilidad", t);
                tvResultados.setText("Error de conexión");
            }
        });
    }
    
    private void cargarCatalogo() {
        AuthAPI api = ApiClient.getClient().create(AuthAPI.class);
        Call<List<CatalogoItem>> call = api.getCatalogo();
        
        call.enqueue(new Callback<List<CatalogoItem>>() {
            @Override
            public void onResponse(Call<List<CatalogoItem>> call, Response<List<CatalogoItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    catalogoMedicinas.clear();
                    catalogoMedicinas.addAll(response.body());
                    spinnerAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Catálogo cargado: " + catalogoMedicinas.size() + " medicinas");
                } else {
                    Log.e(TAG, "Error al cargar catálogo: " + response.code());
                    Toast.makeText(RequesterFormActivity.this, "Error al cargar medicinas", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<List<CatalogoItem>> call, Throwable t) {
                Log.e(TAG, "Error de conexión al cargar catálogo", t);
                Toast.makeText(RequesterFormActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}