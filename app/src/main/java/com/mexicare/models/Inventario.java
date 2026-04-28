package com.mexicare.models;

public class Inventario {
    private int id;
    private int id_organizacion;
    private int id_medicina;
    private int cantidad;
    private String fecha_caducidad;
    private String nombre_medicina; // Campo adicional para búsquedas
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getId_organizacion() { return id_organizacion; }
    public void setId_organizacion(int id_organizacion) { this.id_organizacion = id_organizacion; }
    
    public int getId_medicina() { return id_medicina; }
    public void setId_medicina(int id_medicina) { this.id_medicina = id_medicina; }
    
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    
    public String getFecha_caducidad() { return fecha_caducidad; }
    public void setFecha_caducidad(String fecha_caducidad) { this.fecha_caducidad = fecha_caducidad; }
    
    public String getNombre_medicina() { return nombre_medicina; }
    public void setNombre_medicina(String nombre_medicina) { this.nombre_medicina = nombre_medicina; }
}