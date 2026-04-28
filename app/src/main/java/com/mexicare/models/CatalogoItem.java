package com.mexicare.models;

public class CatalogoItem {
    private int id;
    private String sustancia_activa;
    private String nombre_comercial;
    private String presentacion;
    private String descripcion;

    public int getId() { return id; }
    public String getSustanciaActiva() { return sustancia_activa; }
    public String getNombreComercial() { return nombre_comercial; }
    public String getPresentacion() { return presentacion; }
    public String getDescripcion() { return descripcion; }
    
    @Override
    public String toString() {
        return nombre_comercial + " (" + sustancia_activa + ")";
    }
}