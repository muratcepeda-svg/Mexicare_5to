package com.mexicare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.mexicare.R;
import com.mexicare.api.ApiClient;
import com.mexicare.api.AuthAPI;
import com.mexicare.models.LoginRequest;
import com.mexicare.models.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    
    private static final String TAG = "LoginActivity";
    private EditText etUsuario, etPassword;
    private Button btnLogin;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "=== INICIO LoginActivity onCreate ===");
        
        try {
            Log.d(TAG, "1. Llamando super.onCreate");
            super.onCreate(savedInstanceState);
            
            Log.d(TAG, "2. Estableciendo layout activity_login");
            setContentView(R.layout.activity_login);
            
            initViews();
            setupClickListeners();
            
            Log.d(TAG, "=== LoginActivity iniciada exitosamente ===");
            
        } catch (Exception e) {
            Log.e(TAG, "ERROR en LoginActivity onCreate", e);
        }
    }
    

    
    private void initViews() {
        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }
    
    private void setupClickListeners() {
        if (btnLogin != null) {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "=== CLICK EN LOGIN ===");
                    performLogin();
                }
            });
        }
        
        Button btnVolver = findViewById(R.id.btnVolver);
        Log.d(TAG, "Buscando botón btnVolver en Login...");
        if (btnVolver != null) {
            Log.d(TAG, "Botón VOLVER encontrado y configurado en Login");
            btnVolver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "=== CLICK EN VOLVER AL INICIO - LOGIN ===");
                    try {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        Log.d(TAG, "Intent creado, iniciando MainActivity desde Login...");
                        startActivity(intent);
                        finish();
                        Log.d(TAG, "Navegación a MainActivity exitosa desde Login");
                    } catch (Exception e) {
                        Log.e(TAG, "ERROR al navegar a MainActivity desde Login", e);
                    }
                }
            });
        } else {
            Log.e(TAG, "ERROR: Botón VOLVER no encontrado en Login layout");
        }
    }
    
    private void performLogin() {
        String usuario = etUsuario != null ? etUsuario.getText().toString().trim() : "";
        String password = etPassword != null ? etPassword.getText().toString().trim() : "";
        
        if (usuario.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Login optimizado con una sola consulta
        loginOptimizado(usuario, password);
    }
    
    private void loginOptimizado(String usuario, String password) {
        Log.d(TAG, "=== LOGIN CON MYSQL API ===");
        Log.d(TAG, "Usuario: " + usuario + ", Password: " + password);
        
        try {
            AuthAPI api = ApiClient.getClient().create(AuthAPI.class);
            LoginRequest request = new LoginRequest(usuario, password);
            
            Log.d(TAG, "Enviando request a servidor Python...");
            Log.d(TAG, "URL base: " + ApiClient.getClient().baseUrl());
            
            Call<LoginResponse> call = api.login(request);
            Log.d(TAG, "Call creado, ejecutando...");
            
            call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d(TAG, "=== RESPUESTA RECIBIDA ===");
                Log.d(TAG, "Response code: " + response.code());
                Log.d(TAG, "Response successful: " + response.isSuccessful());
                Log.d(TAG, "Response body: " + response.body());
                
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    Log.d(TAG, "Login success: " + loginResponse.isSuccess());
                    Log.d(TAG, "Login type: " + loginResponse.getType());
                    
                    if (loginResponse.isSuccess()) {
                        String type = loginResponse.getType();
                        
                        if ("admin".equals(type)) {
                            Log.d(TAG, "Navegando a AdminDashboard...");
                            Toast.makeText(LoginActivity.this, "Login exitoso como Admin", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        } else if ("organizacion".equals(type)) {
                            Log.d(TAG, "Navegando a OrganizationDashboard...");
                            
                            // Guardar ID de organización en SharedPreferences
                            getSharedPreferences("MexicarePrefs", 0)
                                .edit()
                                .putInt("organizacion_id", loginResponse.getId())
                                .putString("organizacion_usuario", usuario)
                                .apply();
                            
                            Toast.makeText(LoginActivity.this, "Login exitoso como Organización", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, OrganizationDashboardActivity.class);
                            intent.putExtra("organizacion_usuario", usuario);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Log.d(TAG, "Login fallido: " + loginResponse.getError());
                        Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Error del servidor - Code: " + response.code());
                    Toast.makeText(LoginActivity.this, "Error del servidor: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "=== ERROR DE CONEXIÓN ===");
                Log.e(TAG, "Error type: " + t.getClass().getSimpleName());
                Log.e(TAG, "Error message: " + t.getMessage(), t);
                Toast.makeText(LoginActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        
        } catch (Exception e) {
            Log.e(TAG, "ERROR al crear request", e);
            Toast.makeText(this, "Error interno: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    // Método fallback removido - usar solo login hardcodeado por ahora
    
    @Override
    protected void onStart() {
        Log.d(TAG, "=== LoginActivity onStart ===");
        super.onStart();
    }
    
    @Override
    protected void onResume() {
        Log.d(TAG, "=== LoginActivity onResume ===");
        super.onResume();
    }
}