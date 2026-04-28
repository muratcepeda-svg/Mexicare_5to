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

public class LoginActivityFixed extends AppCompatActivity {
    
    private static final String TAG = "LoginActivity";
    private EditText etUsuario, etPassword;
    private Button btnLogin;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "=== INICIO LoginActivity onCreate ===");
        
        try {
            super.onCreate(savedInstanceState);
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
            btnLogin.setOnClickListener(v -> {
                Log.d(TAG, "=== CLICK EN LOGIN ===");
                performLogin();
            });
        }
        
        Button btnVolver = findViewById(R.id.btnVolver);
        if (btnVolver != null) {
            btnVolver.setOnClickListener(v -> {
                Intent intent = new Intent(LoginActivityFixed.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }
    }
    
    private void performLogin() {
        String usuario = etUsuario != null ? etUsuario.getText().toString().trim() : "";
        String password = etPassword != null ? etPassword.getText().toString().trim() : "";
        
        if (usuario.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // LOGIN HARDCODEADO TEMPORAL (sin JDBC)
        loginHardcoded(usuario, password);
    }
    
    private void loginHardcoded(String usuario, String password) {
        Log.d(TAG, "=== LOGIN HARDCODEADO ===");
        
        // Credenciales hardcodeadas para pruebas
        if ("admin".equals(usuario) && "admin123".equals(password)) {
            Toast.makeText(this, "Login exitoso como Admin", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AdminDashboardActivity.class);
            startActivity(intent);
            finish();
        } else if ("org1".equals(usuario) && "org123".equals(password)) {
            Toast.makeText(this, "Login exitoso como Organización", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, OrganizationDashboardActivity.class);
            intent.putExtra("organizacion_usuario", usuario);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
        }
    }
}