package com.mexicare.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.mexicare.R;
import com.mexicare.api.ApiClient;
import com.mexicare.api.AuthAPI;
import com.mexicare.models.CatalogoItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DonorFormActivity extends AppCompatActivity {
    
    private static final String TAG = "DonorFormActivity";
    private EditText etCantidad, etFechaCaducidad;
    private Spinner spinnerMedicina;
    private Button btnVerMapa;
    private List<CatalogoItem> catalogoMedicinas;
    private ArrayAdapter<CatalogoItem> spinnerAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "=== INICIO DonorFormActivity onCreate ===");
        
        try {
            Log.d(TAG, "1. Llamando super.onCreate");
            super.onCreate(savedInstanceState);
            
            Log.d(TAG, "2. Estableciendo layout activity_donor_form");
            setContentView(R.layout.activity_donor_form);
            
            initViews();
            setupClickListeners();
            cargarCatalogo();
            
            Log.d(TAG, "=== DonorFormActivity iniciada exitosamente ===");
            
        } catch (Exception e) {
            Log.e(TAG, "ERROR en DonorFormActivity onCreate", e);
        }
    }
    
    private void initViews() {
        etCantidad = findViewById(R.id.etCantidad);
        etFechaCaducidad = findViewById(R.id.etFechaCaducidad);
        spinnerMedicina = findViewById(R.id.spinnerMedicina);
        btnVerMapa = findViewById(R.id.btnVerMapa);
        
        catalogoMedicinas = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, catalogoMedicinas);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedicina.setAdapter(spinnerAdapter);
    }
    
    private void setupClickListeners() {
        // DatePicker para fecha de caducidad
        if (etFechaCaducidad != null) {
            etFechaCaducidad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mostrarDatePicker();
                }
            });
        }
        
        if (btnVerMapa != null) {
            btnVerMapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "=== CLICK EN VER MAPA ===");
                    try {
                        Intent intent = new Intent(DonorFormActivity.this, LocationPickerActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e(TAG, "ERROR al navegar al mapa", e);
                    }
                }
            });
        }
        
        Button btnVolver = findViewById(R.id.btnVolver);
        if (btnVolver != null) {
            Log.d(TAG, "Botón VOLVER encontrado y configurado");
            btnVolver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "=== CLICK EN VOLVER AL INICIO ===");
                    try {
                        Intent intent = new Intent(DonorFormActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        Log.d(TAG, "Navegación a MainActivity exitosa");
                    } catch (Exception e) {
                        Log.e(TAG, "ERROR al navegar a MainActivity", e);
                    }
                }
            });
        } else {
            Log.e(TAG, "ERROR: Botón VOLVER no encontrado en el layout");
        }
    }
    
    private void mostrarDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String fechaTexto = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                    etFechaCaducidad.setText(fechaTexto);
                }
            },
            año, mes, dia
        );
        
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    
    @Override
    protected void onStart() {
        Log.d(TAG, "=== DonorFormActivity onStart ===");
        super.onStart();
    }
    
    @Override
    protected void onResume() {
        Log.d(TAG, "=== DonorFormActivity onResume ===");
        super.onResume();
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
                    Toast.makeText(DonorFormActivity.this, "Error al cargar medicinas", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<List<CatalogoItem>> call, Throwable t) {
                Log.e(TAG, "Error de conexión al cargar catálogo", t);
                Toast.makeText(DonorFormActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}