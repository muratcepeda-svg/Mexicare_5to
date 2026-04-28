package com.mexicare.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mexicare.models.Donacion;
import java.util.List;

public class DonacionesAdapter extends RecyclerView.Adapter<DonacionesAdapter.ViewHolder> {
    
    private List<Donacion> donaciones;
    
    public DonacionesAdapter(List<Donacion> donaciones) {
        this.donaciones = donaciones;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Donacion donacion = donaciones.get(position);
        holder.textPrincipal.setText("💊 " + donacion.getMedicamento() + " - " + donacion.getOrganizacion());
        holder.textSecundario.setText("Cantidad: " + donacion.getCantidad() + " | " + donacion.getFechaDonacion());
    }
    
    @Override
    public int getItemCount() {
        return donaciones.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textPrincipal;
        TextView textSecundario;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textPrincipal = itemView.findViewById(android.R.id.text1);
            textSecundario = itemView.findViewById(android.R.id.text2);
        }
    }
}
