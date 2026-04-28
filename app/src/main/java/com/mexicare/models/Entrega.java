package com.mexicare.models;

import com.google.gson.annotations.SerializedName;

public class Entrega {
    private int id;
    
    @SerializedName("medicamento")
    private String medicamento;
    
    @SerializedName("cantidad")
    private int cantidad;
    
    @SerializedName("fecha_entrega")
    private String fechaEntrega;
    
    @SerializedName("curp_receptor")
    private String curpReceptor;
    
    @SerializedName("organizacion")
    private String organizacion;
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getMedicamento() { return medicamento; }
    public void setMedicamento(String medicamento) { this.medicamento = medicamento; }
    
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    
    public String getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(String fechaEntrega) { this.fechaEntrega = fechaEntrega; }
    
    public String getCurpReceptor() { return curpReceptor; }
    public void setCurpReceptor(String curpReceptor) { this.curpReceptor = curpReceptor; }
    
    public String getOrganizacion() { return organizacion; }
    public void setOrganizacion(String organizacion) { this.organizacion = organizacion; }
}