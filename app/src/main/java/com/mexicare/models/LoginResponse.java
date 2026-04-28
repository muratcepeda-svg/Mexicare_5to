package com.mexicare.models;

public class LoginResponse {
    private boolean success;
    private String tipo;
    private String type;
    private int id;
    private String message;
    private String error;
    
    public boolean isSuccess() { return success; }
    public String getType() { return tipo != null ? tipo : type; }
    public int getId() { return id; }
    public String getMessage() { return message != null ? message : error; }
    public String getError() { return error; }
    
    public static class User {
        private int id;
        private String usuario;
        private String nombre;
        
        public int getId() { return id; }
        public String getUsuario() { return usuario; }
        public String getNombre() { return nombre; }
    }
}