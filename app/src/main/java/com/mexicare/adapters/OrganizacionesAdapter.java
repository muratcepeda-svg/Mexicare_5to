package com.mexicare.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mexicare.R;
import com.mexicare.models.Organizacion;
import java.util.List;

public class OrganizacionesAdapter extends RecyclerView.Adapter<OrganizacionesAdapter.ViewHolder> {
    
    private List<Organizacion> organizaciones;
    
    public OrganizacionesAdapter(List<Organizacion> organizaciones) {
        this.organizaciones = organizaciones;
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
        Organizacion org = organizaciones.get(position);
        holder.textNombre.setText("🏥 " + org.getNombre());
        holder.textDetalle.setText("Organización activa");
    }
    
    @Override
    public int getItemCount() {
        return organizaciones.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textNombre;
        TextView textDetalle;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombre = itemView.findViewById(android.R.id.text1);
            textDetalle = itemView.findViewById(android.R.id.text2);
        }
    }
}