package com.mexicare.models;

import com.google.gson.annotations.SerializedName;

public class EntregaRequest {
    @SerializedName("id_organizacion")
    private int idOrganizacion;
    
    @SerializedName("id_medicina")
    private int idMedicina;
    
    @SerializedName("curp_receptor")
    private String curpReceptor;
    
    @SerializedName("cantidad")
    private int cantidad;

    public int getIdOrganizacion() { return idOrganizacion; }
    public void setIdOrganizacion(int idOrganizacion) { this.idOrganizacion = idOrganizacion; }
    
    public int getIdMedicina() { return idMedicina; }
    public void setIdMedicina(int idMedicina) { this.idMedicina = idMedicina; }
    
    public String getCurpReceptor() { return curpReceptor; }
    public void setCurpReceptor(String curpReceptor) { this.curpReceptor = curpReceptor; }
    
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
