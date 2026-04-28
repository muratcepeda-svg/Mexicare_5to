package com.mexicare.models;

public class Catalogo {
    private int id;
    private String sustancia_activa;
    private String nombre_comercial;
    private String presentacion;
    private String descripcion;
    private String created_at;
    
    // Constructores
    public Catalogo() {}
    
    public Catalogo(int id, String sustancia_activa, String nombre_comercial, 
                   String presentacion, String descripcion) {
        this.id = id;
        this.sustancia_activa = sustancia_activa;
        this.nombre_comercial = nombre_comercial;
        this.presentacion = presentacion;
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getSustancia_activa() { return sustancia_activa; }
    public void setSustancia_activa(String sustancia_activa) { this.sustancia_activa = sustancia_activa; }
    
    public String getNombre_comercial() { return nombre_comercial; }
    public void setNombre_comercial(String nombre_comercial) { this.nombre_comercial = nombre_comercial; }
    
    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }
    
    @Override
    public String toString() {
        return nombre_comercial + " (" + sustancia_activa + ")";
    }
}