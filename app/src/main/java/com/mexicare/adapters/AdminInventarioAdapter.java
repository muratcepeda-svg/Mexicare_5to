package com.mexicare.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.mexicare.R;
import com.mexicare.models.InventarioItem;
import java.util.List;

public class AdminInventarioAdapter extends RecyclerView.Adapter<AdminInventarioAdapter.ViewHolder> {
    
    private List<InventarioItem> inventario;
    
    public AdminInventarioAdapter(List<InventarioItem> inventario) {
        this.inventario = inventario;
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_inventario, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        InventarioItem item = inventario.get(position);
        
        String nombreComercial = item.getNombreComercial() != null ? item.getNombreComercial() : "Sin nombre";
        String sustanciaActiva = item.getSustanciaActiva() != null ? item.getSustanciaActiva() : "Sin sustancia";
        String organizacion = item.getOrganizacion() != null ? item.getOrganizacion() : "Sin organización";
        String fechaCaducidad = item.getFechaCaducidad() != null ? item.getFechaCaducidad() : "Sin fecha";
        
        holder.tvNombreComercial.setText("💊 " + nombreComercial);
        holder.tvSustanciaActiva.setText(sustanciaActiva);
        holder.tvOrganizacion.setText("🏥 " + organizacion);
        holder.tvCantidad.setText(item.getCantidad() + " unidades");
        holder.tvFechaCaducidad.setText("📅 " + fechaCaducidad);
    }
    
    @Override
    public int getItemCount() {
        return inventario.size();
    }
    
    public void updateInventario(List<InventarioItem> nuevoInventario) {
        this.inventario = nuevoInventario;
        notifyDataSetChanged();
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreComercial, tvSustanciaActiva, tvOrganizacion, tvCantidad, tvFechaCaducidad;
        
        ViewHolder(View view) {
            super(view);
            tvNombreComercial = view.findViewById(R.id.tvNombreComercial);
            tvSustanciaActiva = view.findViewById(R.id.tvSustanciaActiva);
            tvOrganizacion = view.findViewById(R.id.tvOrganizacion);
            tvCantidad = view.findViewById(R.id.tvCantidad);
            tvFechaCaducidad = view.findViewById(R.id.tvFechaCaducidad);
        }
    }
}
