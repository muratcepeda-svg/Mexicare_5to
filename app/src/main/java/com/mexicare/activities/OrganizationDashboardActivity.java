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
import com.mexicare.fragments.OrgInventoryFragment;
import com.mexicare.fragments.OrgRegisterDonationFragment;
import com.mexicare.fragments.OrgRegisterDeliveryFragment;

public class OrganizationDashboardActivity extends AppCompatActivity {
    
    private TabLayout tabLayout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_dashboard);
        
        setupToolbar();
        initViews();
        setupTabs();
        
        // Mostrar el primer fragment por defecto
        showFragment(new OrgInventoryFragment());
    }
    
    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("🏢 Panel Organización");
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
        Log.d("OrgDashboard", "Buscando botón btnCerrarSesion...");
        if (btnCerrarSesion != null) {
            Log.d("OrgDashboard", "Botón CERRAR SESIÓN encontrado y configurado");
            btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("OrgDashboard", "=== CLICK EN CERRAR SESIÓN - ORG ===");
                    try {
                        Intent intent = new Intent(OrganizationDashboardActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        Log.d("OrgDashboard", "Intent creado, navegando a MainActivity...");
                        startActivity(intent);
                        finish();
                        Log.d("OrgDashboard", "Navegación exitosa desde OrgDashboard");
                    } catch (Exception e) {
                        Log.e("OrgDashboard", "ERROR al navegar desde OrgDashboard", e);
                    }
                }
            });
        } else {
            Log.e("OrgDashboard", "ERROR: Botón CERRAR SESIÓN no encontrado");
        }
    }
    
    private void setupTabs() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new OrgInventoryFragment();
                        break;
                    case 1:
                        fragment = new OrgRegisterDonationFragment();
                        break;
                    case 2:
                        fragment = new OrgRegisterDeliveryFragment();
                        break;
                    default:
                        fragment = new OrgInventoryFragment();
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