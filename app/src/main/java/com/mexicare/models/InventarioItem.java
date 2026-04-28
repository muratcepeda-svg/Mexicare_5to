package com.mexicare.models;

import com.google.gson.annotations.SerializedName;

public class InventarioItem {
    @SerializedName("id")
    private int id;
    
    @SerializedName("nombre_comercial")
    private String nombre_comercial;
    
    @SerializedName("sustancia_activa")
    private String sustancia_activa;
    
    @SerializedName("cantidad")
    private int cantidad;
    
    @SerializedName("fecha_caducidad")
    private String fecha_caducidad;
    
    @SerializedName("organizacion")
    private String organizacion;

    public int getId() { return id; }
    public String getNombreComercial() { return nombre_comercial; }
    public String getSustanciaActiva() { return sustancia_activa; }
    public int getCantidad() { return cantidad; }
    public String getFechaCaducidad() { return fecha_caducidad; }
    public String getOrganizacion() { return organizacion; }
}