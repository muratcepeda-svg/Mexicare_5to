package com.mexicare;

import android.app.Application;
import android.util.Log;


public class MexicareApplication extends Application {
    
    private static final String TAG = "MexicareApplication";
    
    @Override
    public void onCreate() {
        Log.d(TAG, "=== APPLICATION onCreate ===");
        
        // Capturar excepciones no manejadas
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                Log.e(TAG, "EXCEPCIÓN NO MANEJADA en hilo: " + thread.getName(), ex);
                Log.e(TAG, "Mensaje: " + ex.getMessage());
                Log.e(TAG, "Causa: " + ex.getCause());
                
                // Llamar al handler por defecto para que la app se cierre normalmente
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(thread, ex);
            }
        });
        
        try {
            super.onCreate();
            
            Log.d(TAG, "Application inicializada correctamente");
        } catch (Exception e) {
            Log.e(TAG, "ERROR en Application.onCreate", e);
        }
    }
}