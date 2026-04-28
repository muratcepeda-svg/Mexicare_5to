package com.mexicare.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mexicare.R;
import com.mexicare.adapters.DonacionesAdapter;
import com.mexicare.adapters.EntregasAdapter;
import com.mexicare.api.ApiClient;
import com.mexicare.api.AuthAPI;
import com.mexicare.models.Donacion;
import com.mexicare.models.Entrega;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdminHistoryFragment extends Fragment {
    
    private static final String TAG = "AdminHistoryFragment";
    private EditText etFiltrarFecha;
    private RecyclerView recyclerViewDonaciones, recyclerViewEntregas;
    private DonacionesAdapter donacionesAdapter;
    private EntregasAdapter entregasAdapter;
    private List<Donacion> donaciones;
    private List<Entrega> entregas;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_history, container, false);
        
        initViews(view);
        setupRecyclerViews();
        setupDatePicker();
        cargarHistorico();
        
        return view;
    }
    
    private void initViews(View view) {
        etFiltrarFecha = view.findViewById(R.id.etFiltrarFecha);
        recyclerViewDonaciones = view.findViewById(R.id.recyclerViewDonaciones);
        recyclerViewEntregas = view.findViewById(R.id.recyclerViewEntregas);
        donaciones = new ArrayList<>();
        entregas = new ArrayList<>();
    }
    
    private void setupRecyclerViews() {
        recyclerViewDonaciones.setLayoutManager(new LinearLayoutManager(getContext()));
        donacionesAdapter = new DonacionesAdapter(donaciones);
        recyclerViewDonaciones.setAdapter(donacionesAdapter);
        
        recyclerViewEntregas.setLayoutManager(new LinearLayoutManager(getContext()));
        entregasAdapter = new EntregasAdapter(entregas);
        recyclerViewEntregas.setAdapter(entregasAdapter);
    }
    
    private void setupDatePicker() {
        etFiltrarFecha.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String fecha = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    etFiltrarFecha.setText(fecha);
                    filtrarPorFecha(fecha);
                },
                year, month, day
            );
            
            datePickerDialog.show();
        });
    }
    
    private void cargarHistorico() {
        cargarDonaciones();
        cargarEntregas();
    }
    
    private void cargarDonaciones() {
        AuthAPI api = ApiClient.getClient().create(AuthAPI.class);
        Call<List<Donacion>> call = api.getDonaciones();
        
        call.enqueue(new Callback<List<Donacion>>() {
            @Override
            public void onResponse(Call<List<Donacion>> call, Response<List<Donacion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    donaciones.clear();
                    donaciones.addAll(response.body());
                    donacionesAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Donaciones cargadas: " + donaciones.size());
                } else {
                    Toast.makeText(getContext(), "Error al cargar donaciones", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<List<Donacion>> call, Throwable t) {
                Log.e(TAG, "Error de conexión donaciones", t);
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void cargarEntregas() {
        AuthAPI api = ApiClient.getClient().create(AuthAPI.class);
        Call<List<Entrega>> call = api.getEntregas();
        
        call.enqueue(new Callback<List<Entrega>>() {
            @Override
            public void onResponse(Call<List<Entrega>> call, Response<List<Entrega>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    entregas.clear();
                    entregas.addAll(response.body());
                    entregasAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Entregas cargadas: " + entregas.size());
                } else {
                    Toast.makeText(getContext(), "Error al cargar entregas", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<List<Entrega>> call, Throwable t) {
                Log.e(TAG, "Error de conexión entregas", t);
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void filtrarPorFecha(String fecha) {
        Toast.makeText(getContext(), "Filtrando por fecha: " + fecha, Toast.LENGTH_SHORT).show();
        // TODO: Implementar filtrado por fecha en el servidor
    }
}