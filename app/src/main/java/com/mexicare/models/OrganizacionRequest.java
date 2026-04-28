package com.mexicare.models;

public class OrganizacionRequest {
    private double latitud;
    private double longitud;
    private String nombre;
    private String usuario;
    private String clave;

    public OrganizacionRequest(double latitud, double longitud, String nombre, String usuario, String clave) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
        this.usuario = usuario;
        this.clave = clave;
    }

    public double getLatitud() { return latitud; }
    public double getLongitud() { return longitud; }
    public String getNombre() { return nombre; }
    public String getUsuario() { return usuario; }
    public String getClave() { return clave; }
}