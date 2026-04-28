package com.mexicare.dao;

import com.mexicare.models.Medicina;
import com.mexicare.utils.DatabaseHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class MedicinaDAO {
    private static final String TAG = "MedicinaDAO";
    private DatabaseHelper dbHelper;
    
    public MedicinaDAO() {
        dbHelper = DatabaseHelper.getInstance();
    }
    
    // Obtener todas las medicinas
    public List<Medicina> getAllMedicinas() {
        List<Medicina> medicinas = new ArrayList<>();
        String query = "SELECT * FROM catalogo ORDER BY nombre_comercial";
        
        try {
            ResultSet rs = dbHelper.executeQuery(query);
            while (rs.next()) {
                Medicina medicina = new Medicina(
                    rs.getInt("id"),
                    rs.getString("sustancia_activa"),
                    rs.getString("nombre_comercial"),
                    rs.getString("presentacion"),
                    rs.getString("descripcion")
                );
                medicinas.add(medicina);
            }
            rs.close();
        } catch (SQLException e) {
            Log.e(TAG, "Error getting all medicinas", e);
        }
        
        return medicinas;
    }
    
    // Buscar medicinas por nombre o sustancia activa
    public List<Medicina> buscarMedicinas(String query) {
        List<Medicina> medicinas = new ArrayList<>();
        String sql = "SELECT * FROM catalogo WHERE " +
                    "nombre_comercial LIKE ? OR sustancia_activa LIKE ? " +
                    "ORDER BY nombre_comercial";
        
        try {
            String searchPattern = "%" + query + "%";
            ResultSet rs = dbHelper.executeQuery(sql, searchPattern, searchPattern);
            
            while (rs.next()) {
                Medicina medicina = new Medicina(
                    rs.getInt("id"),
                    rs.getString("sustancia_activa"),
                    rs.getString("nombre_comercial"),
                    rs.getString("presentacion"),
                    rs.getString("descripcion")
                );
                medicinas.add(medicina);
            }
            rs.close();
        } catch (SQLException e) {
            Log.e(TAG, "Error searching medicinas", e);
        }
        
        return medicinas;
    }
    
    // Obtener medicina por ID
    public Medicina getMedicinaById(int id) {
        String query = "SELECT * FROM catalogo WHERE id = ?";
        
        try {
            ResultSet rs = dbHelper.executeQuery(query, id);
            if (rs.next()) {
                Medicina medicina = new Medicina(
                    rs.getInt("id"),
                    rs.getString("sustancia_activa"),
                    rs.getString("nombre_comercial"),
                    rs.getString("presentacion"),
                    rs.getString("descripcion")
                );
                rs.close();
                return medicina;
            }
            rs.close();
        } catch (SQLException e) {
            Log.e(TAG, "Error getting medicina by id", e);
        }
        
        return null;
    }
}