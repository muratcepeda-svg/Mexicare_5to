package com.mexicare.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mexicare.models.Entrega;
import java.util.List;

public class EntregasAdapter extends RecyclerView.Adapter<EntregasAdapter.ViewHolder> {
    
    private List<Entrega> entregas;
    
    public EntregasAdapter(List<Entrega> entregas) {
        this.entregas = entregas;
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
        Entrega entrega = entregas.get(position);
        holder.textPrincipal.setText("📦 " + entrega.getMedicamento() + " - " + entrega.getOrganizacion());
        holder.textSecundario.setText("Cantidad: " + entrega.getCantidad() + " | " + entrega.getFechaEntrega());
    }
    
    @Override
    public int getItemCount() {
        return entregas.size();
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
