package com.mexicare.models;

public class Organizacion {
    private int id;
    private double latitud;
    private double longitud;
    private String nombre;
    private String usuario;
    private String clave;
    private boolean activa;
    private double distancia; // Para cálculos de proximidad
    
    // Constructores
    public Organizacion() {}
    
    public Organizacion(int id, double latitud, double longitud, String nombre, 
                       String usuario, String clave, boolean activa) {
        this.id = id;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
        this.usuario = usuario;
        this.clave = clave;
        this.activa = activa;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }
    
    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    
    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }
    
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
    
    public double getDistancia() { return distancia; }
    public void setDistancia(double distancia) { this.distancia = distancia; }
    
    // Método para calcular distancia desde una ubicación
    public void calcularDistancia(double latOrigen, double lonOrigen) {
        final int R = 6371; // Radio de la Tierra en km
        
        double latDistance = Math.toRadians(this.latitud - latOrigen);
        double lonDistance = Math.toRadians(this.longitud - lonOrigen);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(latOrigen)) * Math.cos(Math.toRadians(this.latitud))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        this.distancia = R * c;
    }
    
    public String getDistanciaFormateada() {
        if (distancia < 1) {
            return String.format("%.0f m", distancia * 1000);
        } else {
            return String.format("%.1f km", distancia);
        }
    }
    
    @Override
    public String toString() {
        return nombre;
    }
}