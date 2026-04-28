package com.mexicare.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.mexicare.adapters.AdminInventarioAdapter;
import com.mexicare.api.ApiClient;
import com.mexicare.api.AuthAPI;
import com.mexicare.models.InventarioItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class AdminInventoryFragment extends Fragment {
    
    private static final String TAG = "AdminInventoryFragment";
    private EditText etBuscarMedicina;
    private RecyclerView recyclerView;
    private AdminInventarioAdapter adapter;
    private List<InventarioItem> inventarioCompleto;
    private List<InventarioItem> inventarioFiltrado;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_inventory, container, false);
        
        inventarioCompleto = new ArrayList<>();
        inventarioFiltrado = new ArrayList<>();
        
        initViews(view);
        setupRecyclerView();
        setupSearchListener();
        cargarInventarioGlobal();
        
        return view;
    }
    
    private void initViews(View view) {
        etBuscarMedicina = view.findViewById(R.id.etBuscarMedicina);
        recyclerView = view.findViewById(R.id.recyclerViewInventario);
    }
    
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdminInventarioAdapter(inventarioFiltrado);
        recyclerView.setAdapter(adapter);
    }
    
    private void setupSearchListener() {
        etBuscarMedicina.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarInventario(s.toString());
            }
            
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    
    private void cargarInventarioGlobal() {
        AuthAPI api = ApiClient.getClient().create(AuthAPI.class);
        Call<List<InventarioItem>> call = api.getInventario(null);
        
        call.enqueue(new Callback<List<InventarioItem>>() {
            @Override
            public void onResponse(Call<List<InventarioItem>> call, Response<List<InventarioItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    inventarioCompleto.clear();
                    inventarioCompleto.addAll(response.body());
                    inventarioFiltrado.clear();
                    inventarioFiltrado.addAll(inventarioCompleto);
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "Inventario global cargado: " + inventarioCompleto.size() + " items");
                } else {
                    Toast.makeText(getContext(), "Error al cargar inventario", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<List<InventarioItem>> call, Throwable t) {
                Log.e(TAG, "Error de conexión", t);
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void filtrarInventario(String query) {
        inventarioFiltrado.clear();
        
        if (query.isEmpty()) {
            inventarioFiltrado.addAll(inventarioCompleto);
        } else {
            String queryLower = query.toLowerCase();
            for (InventarioItem item : inventarioCompleto) {
                String nombreComercial = item.getNombreComercial() != null ? item.getNombreComercial().toLowerCase() : "";
                String sustanciaActiva = item.getSustanciaActiva() != null ? item.getSustanciaActiva().toLowerCase() : "";
                
                if (nombreComercial.contains(queryLower) || sustanciaActiva.contains(queryLower)) {
                    inventarioFiltrado.add(item);
                }
            }
        }
        
        adapter.notifyDataSetChanged();
    }
}