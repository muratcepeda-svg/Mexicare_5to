package com.mexicare.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.mexicare.R;
import com.mexicare.api.ApiClient;
import com.mexicare.api.AuthAPI;
import com.mexicare.models.CatalogoItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class OrgRegisterDeliveryFragment extends Fragment {
    
    private static final String TAG = "OrgRegisterDelivery";
    private Spinner spinnerMedicina;
    private TextView tvCantidadDisponible, tvNuevoStock;
    private EditText etCantidadEntregar, etCurpReceptor;
    private Button btnRegistrarEntrega;
    private List<CatalogoItem> catalogoMedicinas;
    private ArrayAdapter<CatalogoItem> spinnerAdapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_org_register_delivery, container, false);
        
        initViews(view);
        setupClickListeners();
        cargarCatalogo();
        
        return view;
    }
    
    private void initViews(View view) {
        spinnerMedicina = view.findViewById(R.id.spinnerMedicina);
        tvCantidadDisponible = view.findViewById(R.id.tvCantidadDisponible);
        tvNuevoStock = view.findViewById(R.id.tvNuevoStock);
        etCantidadEntregar = view.findViewById(R.id.etCantidadEntregar);
        etCurpReceptor = view.findViewById(R.id.etCurpReceptor);
        btnRegistrarEntrega = view.findViewById(R.id.btnRegistrarEntrega);
        
        catalogoMedicinas = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, catalogoMedicinas);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedicina.setAdapter(spinnerAdapter);
    }
    
    private void setupClickListeners() {
        btnRegistrarEntrega.setOnClickListener(v -> registrarEntrega());
    }
    
    private void registrarEntrega() {
        String cantidadStr = etCantidadEntregar.getText().toString().trim();
        String curp = etCurpReceptor.getText().toString().trim();
        
        if (cantidadStr.isEmpty() || curp.isEmpty()) {
            Toast.makeText(getActivity(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (spinnerMedicina.getSelectedItem() == null) {
            Toast.makeText(getActivity(), "Por favor selecciona una medicina", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            int cantidad = Integer.parseInt(cantidadStr);
            
            // Obtener ID de organización desde SharedPreferences
            int organizacionId = getActivity().getSharedPreferences("MexicarePrefs", 0)
                .getInt("organizacion_id", -1);
            
            if (organizacionId == -1) {
                Toast.makeText(getActivity(), "Error: No se encontró ID de organización", Toast.LENGTH_SHORT).show();
                return;
            }
            
            CatalogoItem medicinaSeleccionada = (CatalogoItem) spinnerMedicina.getSelectedItem();
            
            // Crear objeto entrega
            com.mexicare.models.EntregaRequest entrega = new com.mexicare.models.EntregaRequest();
            entrega.setIdOrganizacion(organizacionId);
            entrega.setIdMedicina(medicinaSeleccionada.getId());
            entrega.setCurpReceptor(curp);
            entrega.setCantidad(cantidad);
            
            // Enviar a servidor
            AuthAPI api = ApiClient.getClient().create(AuthAPI.class);
            Call<com.mexicare.models.LoginResponse> call = api.registrarEntrega(entrega);
            
            call.enqueue(new Callback<com.mexicare.models.LoginResponse>() {
                @Override
                public void onResponse(Call<com.mexicare.models.LoginResponse> call, Response<com.mexicare.models.LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        com.mexicare.models.LoginResponse result = response.body();
                        if (result.isSuccess()) {
                            Toast.makeText(getActivity(), "✅ Entrega registrada exitosamente", Toast.LENGTH_SHORT).show();
                            // Limpiar formulario
                            etCantidadEntregar.setText("");
                            etCurpReceptor.setText("");
                            spinnerMedicina.setSelection(0);
                            tvNuevoStock.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(getActivity(), "Error: " + result.getError(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Error del servidor: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
                
                @Override
                public void onFailure(Call<com.mexicare.models.LoginResponse> call, Throwable t) {
                    Log.e(TAG, "Error al registrar entrega", t);
                    Toast.makeText(getActivity(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Cantidad inválida", Toast.LENGTH_SHORT).show();
        }
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
                    Toast.makeText(getContext(), "Error al cargar medicinas", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<List<CatalogoItem>> call, Throwable t) {
                Log.e(TAG, "Error de conexión al cargar catálogo", t);
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}