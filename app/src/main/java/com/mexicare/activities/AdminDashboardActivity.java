package com.mexicare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.tabs.TabLayout;
import com.mexicare.R;
import com.mexicare.fragments.AdminAddOrgFragment;
import com.mexicare.fragments.AdminInventoryFragment;
import com.mexicare.fragments.AdminHistoryFragment;

public class AdminDashboardActivity extends AppCompatActivity {
    
    private TabLayout tabLayout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        
        setupToolbar();
        initViews();
        setupTabs();
        
        // Mostrar el primer fragment por defecto
        showFragment(new AdminAddOrgFragment());
    }
    
    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("👨💼 Panel Administrador");
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    
    private void initViews() {
        tabLayout = findViewById(R.id.tabLayout);
        
        Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        Log.d("AdminDashboard", "Buscando botón btnCerrarSesion...");
        if (btnCerrarSesion != null) {
            Log.d("AdminDashboard", "Botón CERRAR SESIÓN encontrado y configurado");
            btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("AdminDashboard", "=== CLICK EN CERRAR SESIÓN - ADMIN ===");
                    try {
                        Intent intent = new Intent(AdminDashboardActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        Log.d("AdminDashboard", "Intent creado, navegando a MainActivity...");
                        startActivity(intent);
                        finish();
                        Log.d("AdminDashboard", "Navegación exitosa desde AdminDashboard");
                    } catch (Exception e) {
                        Log.e("AdminDashboard", "ERROR al navegar desde AdminDashboard", e);
                    }
                }
            });
        } else {
            Log.e("AdminDashboard", "ERROR: Botón CERRAR SESIÓN no encontrado");
        }
    }
    
    private void setupTabs() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new AdminAddOrgFragment();
                        break;
                    case 1:
                        fragment = new AdminInventoryFragment();
                        break;
                    case 2:
                        fragment = new AdminHistoryFragment();
                        break;
                    default:
                        fragment = new AdminAddOrgFragment();
                        break;
                }
                showFragment(fragment);
            }
            
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
    
    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}