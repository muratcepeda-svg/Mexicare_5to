package com.mexicare.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import android.util.Log;

public class DatabaseHelper {
    private static final String TAG = "DatabaseHelper";
    private static DatabaseHelper instance;
    
    // Configuración para Docker Compose - CAMBIAR SEGÚN TU CONFIGURACIÓN
    private static final String HOST = "localhost"; // Cambiar por IP del servidor Docker
    private static final String PORT = "3306";
    private static final String DATABASE = "mexicare_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "rootpassword"; // Cambiar según docker-compose.yml
    
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + 
                                     "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    
    private Connection connection;
    
    private DatabaseHelper() {
        Log.d(TAG, "Constructor DatabaseHelper - INICIO");
        try {
            Log.d(TAG, "Cargando driver MySQL");
            Class.forName("com.mysql.cj.jdbc.Driver");
            Log.d(TAG, "Driver MySQL cargado exitosamente");
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "ERROR cargando driver MySQL", e);
        }
        Log.d(TAG, "Constructor DatabaseHelper - FIN");
    }
    
    public static synchronized DatabaseHelper getInstance() {
        Log.d(TAG, "getInstance - INICIO");
        try {
            if (instance == null) {
                Log.d(TAG, "Creando nueva instancia DatabaseHelper");
                instance = new DatabaseHelper();
                Log.d(TAG, "Instancia creada exitosamente");
            } else {
                Log.d(TAG, "Retornando instancia existente");
            }
            return instance;
        } catch (Exception e) {
            Log.e(TAG, "ERROR en getInstance", e);
            throw e;
        }
    }
    
    public Connection getConnection() throws SQLException {
        Log.d(TAG, "getConnection - INICIO");
        try {
            Log.d(TAG, "URL: " + URL);
            Log.d(TAG, "Usuario: " + USERNAME);
            
            if (connection == null || connection.isClosed()) {
                Log.d(TAG, "Creando nueva conexión");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                Log.d(TAG, "Conexión establecida exitosamente");
            } else {
                Log.d(TAG, "Reutilizando conexión existente");
            }
            return connection;
        } catch (SQLException e) {
            Log.e(TAG, "ERROR SQL en getConnection", e);
            throw e;
        } catch (Exception e) {
            Log.e(TAG, "ERROR INESPERADO en getConnection", e);
            throw new SQLException("Error inesperado", e);
        }
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                Log.d(TAG, "Database connection closed");
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error closing connection", e);
        }
    }
    
    public ResultSet executeQuery(String query, Object... params) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        
        return stmt.executeQuery();
    }
    
    public int executeUpdate(String query, Object... params) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        
        int result = stmt.executeUpdate();
        stmt.close();
        return result;
    }
    
    public long executeInsert(String query, Object... params) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        
        stmt.executeUpdate();
        ResultSet rs = stmt.getGeneratedKeys();
        long id = 0;
        if (rs.next()) {
            id = rs.getLong(1);
        }
        
        rs.close();
        stmt.close();
        return id;
    }
    
    public boolean testConnection() {
        Log.d(TAG, "testConnection - INICIO");
        try {
            Log.d(TAG, "Intentando obtener conexión");
            Connection conn = getConnection();
            
            if (conn == null) {
                Log.e(TAG, "Conexión es null");
                return false;
            }
            
            boolean isClosed = conn.isClosed();
            Log.d(TAG, "Conexión cerrada: " + isClosed);
            
            boolean result = !isClosed;
            Log.d(TAG, "testConnection resultado: " + result);
            return result;
            
        } catch (SQLException e) {
            Log.e(TAG, "ERROR en testConnection", e);
            return false;
        } catch (Exception e) {
            Log.e(TAG, "ERROR INESPERADO en testConnection", e);
            return false;
        }
    }
}