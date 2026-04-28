package com.mexicare.api;

import com.mexicare.models.LoginRequest;
import com.mexicare.models.LoginResponse;
import com.mexicare.models.OrganizacionRequest;
import com.mexicare.models.InventarioItem;
import com.mexicare.models.CatalogoItem;
import com.mexicare.models.DonacionRequest;
import com.mexicare.models.Donacion;
import com.mexicare.models.Entrega;
import com.mexicare.models.EntregaRequest;
import com.mexicare.models.Organizacion;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import java.util.List;

public interface AuthAPI {
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);
    
    @POST("organizaciones")
    Call<Void> registrarOrganizacion(@Body OrganizacionRequest request);
    
    @GET("organizaciones")
    Call<List<Organizacion>> getOrganizaciones(@Query("medicina_id") Integer medicinaId);
    
    @GET("inventario")
    Call<List<InventarioItem>> getInventario(@Query("organizacion_id") Integer organizacionId);
    
    @GET("catalogo")
    Call<List<CatalogoItem>> getCatalogo();
    
    @POST("donaciones")
    Call<LoginResponse> registrarDonacion(@Body DonacionRequest donacion);
    
    @POST("entregas")
    Call<LoginResponse> registrarEntrega(@Body EntregaRequest entrega);
    
    @GET("donaciones")
    Call<List<Donacion>> getDonaciones();
    
    @GET("entregas")
    Call<List<Entrega>> getEntregas();
}