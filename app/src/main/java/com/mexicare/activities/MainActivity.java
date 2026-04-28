package com.mexicare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.mexicare.R;
import com.mexicare.utils.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    
    private static final String TAG = "MainActivity";
    private CardView cardDonar, cardSolicitar, cardOrganizaciones;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "=== INICIO onCreate ===");
        
        try {
            Log.d(TAG, "1. Llamando super.onCreate");
            super.onCreate(savedInstanceState);
            
            Log.d(TAG, "2. Estableciendo layout");
            setContentView(R.layout.activity_main);
            
            Log.d(TAG, "3. Inicializando vistas");
            initViews();
            
            Log.d(TAG, "4. Configurando listeners");
            setupClickListeners();
            
            Log.d(TAG, "5. Probando conexión BD");
            testDatabaseConnection();
            
            Log.d(TAG, "=== FIN onCreate EXITOSO ===");
            
        } catch (Exception e) {
            Log.e(TAG, "ERROR en onCreate: " + e.getMessage(), e);
        }
    }
    
    private void initViews() {
        Log.d(TAG, "initViews - Buscando cardDonar");
        cardDonar = findViewById(R.id.cardDonar);
        if (cardDonar == null) {
            Log.e(TAG, "ERROR: cardDonar es null");
        } else {
            Log.d(TAG, "cardDonar encontrado OK");
        }
        
        Log.d(TAG, "initViews - Buscando cardSolicitar");
        cardSolicitar = findViewById(R.id.cardSolicitar);
        if (cardSolicitar == null) {
            Log.e(TAG, "ERROR: cardSolicitar es null");
        } else {
            Log.d(TAG, "cardSolicitar encontrado OK");
        }
        
        Log.d(TAG, "initViews - Buscando cardOrganizaciones");
        cardOrganizaciones = findViewById(R.id.cardOrganizaciones);
        if (cardOrganizaciones == null) {
            Log.e(TAG, "ERROR: cardOrganizaciones es null");
        } else {
            Log.d(TAG, "cardOrganizaciones encontrado OK");
        }
        
        Log.d(TAG, "initViews - COMPLETADO");
    }
    
    private void setupClickListeners() {
        Log.d(TAG, "setupClickListeners - INICIO");
        
        try {
            if (cardDonar != null) {
                Log.d(TAG, "Configurando listener para cardDonar");
                cardDonar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "=== CLICK EN DONAR - MAIN ACTIVITY ===");
                        try {
                            Log.d(TAG, "Creando Intent para DonorFormActivity");
                            Intent intent = new Intent(MainActivity.this, DonorFormActivity.class);
                            Log.d(TAG, "Intent creado, iniciando DonorFormActivity");
                            startActivity(intent);
                            Log.d(TAG, "DonorFormActivity iniciada exitosamente desde MainActivity");
                        } catch (Exception e) {
                            Log.e(TAG, "ERROR al navegar a DonorFormActivity desde MainActivity", e);
                        }
                    }
                });
            } else {
                Log.e(TAG, "ERROR: cardDonar es null en MainActivity");
            }
            
            if (cardSolicitar != null) {
                Log.d(TAG, "Configurando listener para cardSolicitar");
                cardSolicitar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "=== CLICK EN SOLICITAR ===");
                        try {
                            Log.d(TAG, "Creando Intent para RequesterFormActivity");
                            Intent intent = new Intent(MainActivity.this, RequesterFormActivity.class);
                            Log.d(TAG, "Intent creado, iniciando actividad");
                            startActivity(intent);
                            Log.d(TAG, "Actividad iniciada exitosamente");
                        } catch (Exception e) {
                            Log.e(TAG, "ERROR al navegar a RequesterFormActivity", e);
                        }
                    }
                });
            }
            
            if (cardOrganizaciones != null) {
                Log.d(TAG, "Configurando listener para cardOrganizaciones");
                cardOrganizaciones.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "=== CLICK EN ORGANIZACIONES ===");
                        try {
                            Log.d(TAG, "Creando Intent para LoginActivity");
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            Log.d(TAG, "Intent creado, iniciando actividad");
                            startActivity(intent);
                            Log.d(TAG, "Actividad iniciada exitosamente");
                        } catch (Exception e) {
                            Log.e(TAG, "ERROR al navegar a LoginActivity", e);
                        }
                    }
                });
            }
            
            Log.d(TAG, "setupClickListeners - COMPLETADO");
            
        } catch (Exception e) {
            Log.e(TAG, "ERROR en setupClickListeners: " + e.getMessage(), e);
        }
    }
    
    private void testDatabaseConnection() {
        Log.d(TAG, "testDatabaseConnection - INICIO");
        
        try {
            // Comentamos la conexión BD por ahora para evitar crashes
            Log.d(TAG, "Conexión BD deshabilitada temporalmente");
            Log.i(TAG, "App iniciada correctamente");
            
            Log.d(TAG, "testDatabaseConnection - COMPLETADO");
            
        } catch (Exception e) {
            Log.e(TAG, "ERROR en testDatabaseConnection: " + e.getMessage(), e);
        }
    }
    
    @Override
    protected void onStart() {
        Log.d(TAG, "=== onStart ===");
        super.onStart();
    }
    
    @Override
    protected void onResume() {
        Log.d(TAG, "=== onResume ===");
        super.onResume();
    }
    
    @Override
    protected void onPause() {
        Log.d(TAG, "=== onPause ===");
        super.onPause();
    }
    
    @Override
    protected void onDestroy() {
        Log.d(TAG, "=== onDestroy ===");
        super.onDestroy();
    }
}