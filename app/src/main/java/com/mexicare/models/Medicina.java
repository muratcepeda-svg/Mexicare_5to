package com.mexicare.models;

public class Medicina {
    private int id;
    private String sustanciaActiva;
    private String nombreComercial;
    private String presentacion;
    private String descripcion;
    
    // Constructores
    public Medicina() {}
    
    public Medicina(int id, String sustanciaActiva, String nombreComercial, 
                   String presentacion, String descripcion) {
        this.id = id;
        this.sustanciaActiva = sustanciaActiva;
        this.nombreComercial = nombreComercial;
        this.presentacion = presentacion;
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getSustanciaActiva() { return sustanciaActiva; }
    public void setSustanciaActiva(String sustanciaActiva) { this.sustanciaActiva = sustanciaActiva; }
    
    public String getNombreComercial() { return nombreComercial; }
    public void setNombreComercial(String nombreComercial) { this.nombreComercial = nombreComercial; }
    
    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    // Método toString para AutoCompleteTextView
    @Override
    public String toString() {
        return nombreComercial + " (" + sustanciaActiva + ") - " + presentacion;
    }
    
    // Método para búsqueda
    public boolean matches(String query) {
        String lowerQuery = query.toLowerCase();
        return nombreComercial.toLowerCase().contains(lowerQuery) ||
               sustanciaActiva.toLowerCase().contains(lowerQuery) ||
               presentacion.toLowerCase().contains(lowerQuery);
    }
}