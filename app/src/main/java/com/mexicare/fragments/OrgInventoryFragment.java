package com.mexicare.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mexicare.R;
import com.mexicare.adapters.InventarioAdapter;
import com.mexicare.api.ApiClient;
import com.mexicare.api.AuthAPI;
import com.mexicare.models.InventarioItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class OrgInventoryFragment extends Fragment {
    
    private static final String TAG = "OrgInventoryFragment";
    private RecyclerView recyclerView;
    private InventarioAdapter adapter;
    private int organizacionId;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_org_inventory, container, false);
        
        initViews(view);
        obtenerOrganizacionId();
        cargarInventario();
        
        return view;
    }
    
    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewInventario);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        adapter = new InventarioAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }
    
    private void obtenerOrganizacionId() {
        SharedPreferences prefs = getActivity().getSharedPreferences("MexicarePrefs", 0);
        organizacionId = prefs.getInt("organizacion_id", -1);
        Log.d(TAG, "ID de organización: " + organizacionId);
    }
    
    private void cargarInventario() {
        if (organizacionId == -1) {
            Toast.makeText(getContext(), "Error: ID de organización no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }
        
        AuthAPI api = ApiClient.getClient().create(AuthAPI.class);
        Call<List<InventarioItem>> call = api.getInventario(organizacionId);
        
        call.enqueue(new Callback<List<InventarioItem>>() {
            @Override
            public void onResponse(Call<List<InventarioItem>> call, Response<List<InventarioItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<InventarioItem> inventario = response.body();
                    Log.d(TAG, "Inventario cargado: " + inventario.size() + " items");
                    
                    if (inventario.isEmpty()) {
                        Toast.makeText(getContext(), "No hay medicamentos en inventario", Toast.LENGTH_SHORT).show();
                    }
                    
                    adapter.updateInventario(inventario);
                } else {
                    Log.e(TAG, "Error en respuesta: " + response.code());
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
}