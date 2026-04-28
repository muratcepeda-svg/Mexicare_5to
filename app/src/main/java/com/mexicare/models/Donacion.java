package com.mexicare.models;

import com.google.gson.annotations.SerializedName;

public class Donacion {
    private int id;
    
    @SerializedName("medicamento")
    private String medicamento;
    
    @SerializedName("cantidad")
    private int cantidad;
    
    @SerializedName("fecha_donacion")
    private String fechaDonacion;
    
    @SerializedName("curp_donatario")
    private String curpDonatario;
    
    @SerializedName("organizacion")
    private String organizacion;
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getMedicamento() { return medicamento; }
    public void setMedicamento(String medicamento) { this.medicamento = medicamento; }
    
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    
    public String getFechaDonacion() { return fechaDonacion; }
    public void setFechaDonacion(String fechaDonacion) { this.fechaDonacion = fechaDonacion; }
    
    public String getCurpDonatario() { return curpDonatario; }
    public void setCurpDonatario(String curpDonatario) { this.curpDonatario = curpDonatario; }
    
    public String getOrganizacion() { return organizacion; }
    public void setOrganizacion(String organizacion) { this.organizacion = organizacion; }
}