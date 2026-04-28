package com.mexicare.models;

public class DonacionRequest {
    private int idOrganizacion;
    private int idMedicina;
    private String curpDonatario;
    private int cantidad;
    private String fechaCaducidad;
    
    public DonacionRequest() {}
    
    public DonacionRequest(int idOrganizacion, int idMedicina, String curpDonatario, int cantidad, String fechaCaducidad) {
        this.idOrganizacion = idOrganizacion;
        this.idMedicina = idMedicina;
        this.curpDonatario = curpDonatario;
        this.cantidad = cantidad;
        this.fechaCaducidad = fechaCaducidad;
    }
    
    public int getIdOrganizacion() { return idOrganizacion; }
    public void setIdOrganizacion(int idOrganizacion) { this.idOrganizacion = idOrganizacion; }
    
    public int getIdMedicina() { return idMedicina; }
    public void setIdMedicina(int idMedicina) { this.idMedicina = idMedicina; }
    
    public String getCurpDonatario() { return curpDonatario; }
    public void setCurpDonatario(String curpDonatario) { this.curpDonatario = curpDonatario; }
    
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    
    public String getFechaCaducidad() { return fechaCaducidad; }
    public void setFechaCaducidad(String fechaCaducidad) { this.fechaCaducidad = fechaCaducidad; }
}