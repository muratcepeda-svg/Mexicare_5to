package com.mexicare.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.mexicare.R;
import com.mexicare.api.ApiClient;
import com.mexicare.api.AuthAPI;
import com.mexicare.models.OrganizacionRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAddOrgFragment extends Fragment {
    
    private EditText etNombreOrganizacion, etUsuario, etContrasena, etLatitud, etLongitud;
    private Button btnRegistrarOrganizacion;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_add_org, container, false);
        
        initViews(view);
        setupClickListeners();
        
        return view;
    }
    
    private void initViews(View view) {
        etNombreOrganizacion = view.findViewById(R.id.etNombreOrganizacion);
        etUsuario = view.findViewById(R.id.etUsuario);
        etContrasena = view.findViewById(R.id.etContrasena);
        etLatitud = view.findViewById(R.id.etLatitud);
        etLongitud = view.findViewById(R.id.etLongitud);
        btnRegistrarOrganizacion = view.findViewById(R.id.btnRegistrarOrganizacion);
    }
    
    private void setupClickListeners() {
        btnRegistrarOrganizacion.setOnClickListener(v -> {
            registrarOrganizacion();
        });
    }
    
    private void registrarOrganizacion() {
        String nombre = etNombreOrganizacion.getText().toString().trim();
        String usuario = etUsuario.getText().toString().trim();
        String contrasena = etContrasena.getText().toString().trim();
        String latitudStr = etLatitud.getText().toString().trim();
        String longitudStr = etLongitud.getText().toString().trim();
        
        if (nombre.isEmpty() || usuario.isEmpty() || contrasena.isEmpty() || 
            latitudStr.isEmpty() || longitudStr.isEmpty()) {
            Toast.makeText(getActivity(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            double latitud = Double.parseDouble(latitudStr);
            double longitud = Double.parseDouble(longitudStr);
            
            // Validar rangos de coordenadas
            if (latitud < -90 || latitud > 90 || longitud < -180 || longitud > 180) {
                Toast.makeText(getActivity(), "Coordenadas inválidas. Lat: -90 a 90, Lng: -180 a 180", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Registrar via API REST
            OrganizacionRequest request = new OrganizacionRequest(latitud, longitud, nombre, usuario, contrasena);
            
            AuthAPI api = ApiClient.getClient().create(AuthAPI.class);
            Call<Void> call = api.registrarOrganizacion(request);
            
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getActivity(), "✅ Organización registrada\n" +
                            "Nombre: " + nombre + "\n" +
                            "Usuario: " + usuario, Toast.LENGTH_LONG).show();
                        
                        // Limpiar formulario
                        etNombreOrganizacion.setText("");
                        etUsuario.setText("");
                        etContrasena.setText("");
                        etLatitud.setText("");
                        etLongitud.setText("");
                    } else {
                        Toast.makeText(getActivity(), "❌ Error al registrar", Toast.LENGTH_SHORT).show();
                    }
                }
                
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getActivity(), "❌ Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Por favor ingresa coordenadas válidas", Toast.LENGTH_SHORT).show();
        }
    }
    
}