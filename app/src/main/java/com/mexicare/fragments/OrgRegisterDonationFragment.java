package com.mexicare.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.mexicare.R;
import com.mexicare.api.ApiClient;
import com.mexicare.api.AuthAPI;
import com.mexicare.models.CatalogoItem;
import com.mexicare.models.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.json.JSONObject;

public class OrgRegisterDonationFragment extends Fragment {
    
    private static final String TAG = "OrgRegisterDonation";
    private Spinner spinnerMedicina;
    private EditText etCantidad, etCurpDonante, etFechaCaducidad;
    private Button btnRegistrarDonacion;
    private List<CatalogoItem> catalogoMedicinas;
    private ArrayAdapter<CatalogoItem> spinnerAdapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_org_register_donation, container, false);
        
        initViews(view);
        setupClickListeners();
        cargarCatalogo();
        
        return view;
    }
    
    private void initViews(View view) {
        spinnerMedicina = view.findViewById(R.id.spinnerMedicina);
        etCantidad = view.findViewById(R.id.etCantidad);
        etCurpDonante = view.findViewById(R.id.etCurpDonante);
        etFechaCaducidad = view.findViewById(R.id.etFechaCaducidad);
        btnRegistrarDonacion = view.findViewById(R.id.btnRegistrarDonacion);
        
        catalogoMedicinas = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, catalogoMedicinas);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedicina.setAdapter(spinnerAdapter);
    }
    
    private void setupClickListeners() {
        etFechaCaducidad.setOnClickListener(v -> showDatePicker());
        
        btnRegistrarDonacion.setOnClickListener(v -> registrarDonacion());
    }
    
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            getActivity(),
            (view, selectedYear, selectedMonth, selectedDay) -> {
                String fecha = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                etFechaCaducidad.setText(fecha + " 📅");
            },
            year, month, day
        );
        
        datePickerDialog.show();
    }
    
    private void registrarDonacion() {
        String cantidad = etCantidad.getText().toString().trim();
        String curp = etCurpDonante.getText().toString().trim();
        String fecha = etFechaCaducidad.getText().toString().trim();
        
        if (cantidad.isEmpty() || curp.isEmpty() || fecha.isEmpty()) {
            Toast.makeText(getActivity(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (spinnerMedicina.getSelectedItem() == null) {
            Toast.makeText(getActivity(), "Por favor selecciona una medicina", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Obtener ID de organización desde SharedPreferences
        int organizacionId = getActivity().getSharedPreferences("MexicarePrefs", 0)
            .getInt("organizacion_id", -1);
        
        if (organizacionId == -1) {
            Toast.makeText(getActivity(), "Error: No se encontró ID de organización", Toast.LENGTH_SHORT).show();
            return;
        }
        
        CatalogoItem medicinaSeleccionada = (CatalogoItem) spinnerMedicina.getSelectedItem();
        
        // Convertir fecha para enviar al servidor
        String fechaCaducidadLimpia = fecha.replace(" 📅", "");
        
        Log.d(TAG, "=== DEBUG DONACION ===");
        Log.d(TAG, "Cantidad: " + cantidad);
        Log.d(TAG, "CURP: " + curp);
        Log.d(TAG, "Fecha original: " + fecha);
        Log.d(TAG, "Fecha limpia: " + fechaCaducidadLimpia);
        Log.d(TAG, "Org ID: " + organizacionId);
        Log.d(TAG, "Medicina ID: " + medicinaSeleccionada.getId());
        
        // Crear objeto donación con fecha
        com.mexicare.models.DonacionRequest donacionRequest = new com.mexicare.models.DonacionRequest(
            organizacionId,
            medicinaSeleccionada.getId(),
            curp,
            Integer.parseInt(cantidad),
            fechaCaducidadLimpia
        );
        
        Log.d(TAG, "DonacionRequest creado correctamente");
        
        // Enviar a servidor
        AuthAPI api = ApiClient.getClient().create(AuthAPI.class);
        Call<LoginResponse> call = api.registrarDonacion(donacionRequest);
        
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse result = response.body();
                    if (result.isSuccess()) {
                        Toast.makeText(getActivity(), "✅ Donación registrada exitosamente", Toast.LENGTH_SHORT).show();
                        // Limpiar formulario
                        etCantidad.setText("");
                        etCurpDonante.setText("");
                        etFechaCaducidad.setText("");
                        spinnerMedicina.setSelection(0);
                    } else {
                        String errorMsg = result.getError();
                        if (errorMsg == null || errorMsg.isEmpty()) {
                            errorMsg = result.getMessage();
                        }
                        if (errorMsg == null || errorMsg.isEmpty()) {
                            errorMsg = "Error desconocido";
                        }
                        Toast.makeText(getActivity(), "Error: " + errorMsg, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Error del servidor: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "Error al registrar donación", t);
                Toast.makeText(getActivity(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
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