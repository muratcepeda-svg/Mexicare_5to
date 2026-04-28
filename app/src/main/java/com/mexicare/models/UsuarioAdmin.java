package com.mexicare.models;

import java.util.Date;

public class UsuarioAdmin {
    private int id;
    private String usuario;
    private String clave;
    private boolean activo;
    private Date createdAt;
    
    // Constructores
    public UsuarioAdmin() {}
    
    public UsuarioAdmin(String usuario, String clave) {
        this.usuario = usuario;
        this.clave = clave;
        this.activo = true;
    }
    
    public UsuarioAdmin(int id, String usuario, String clave, boolean activo, Date createdAt) {
        this.id = id;
        this.usuario = usuario;
        this.clave = clave;
        this.activo = activo;
        this.createdAt = createdAt;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    
    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return usuario;
    }
}