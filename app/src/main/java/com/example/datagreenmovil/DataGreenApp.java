package com.example.datagreenmovil;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.datagreenmovil.Conexiones.AppDatabase;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class DataGreenApp extends Application {
    static SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean darkTheme;

    public static boolean isPassPassed() {
        return passPassed;
    }

    public void setPassPassed(boolean passPassed) {
        this.passPassed = passPassed;
    }

    public static boolean passPassed = false;
    public static ConfiguracionLocal configuracionLocal;

    public static ConfiguracionLocal getConfiguracionLocal() {
        return configuracionLocal;
    }

    public static void setConfiguracionLocal(ConfiguracionLocal cl) {
        configuracionLocal = cl;
    }

    public static Context appContext;

    public static Integer DB_VERSION(){
        return Integer.valueOf(sharedPreferences.getString("VERSION_DB_DISPONIBLE", "1"));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = this.getSharedPreferences("objConfLocal", MODE_PRIVATE);
        //        INICIAMOS EL SERVICIO QUE NOS INDICA SI LA APLICACION TIENE UNA ACTUALIZACIÓN
//        startService(new Intent(this, DataGreenUpdateService.class).putExtra("activity_context", this.getClass().getName()));
        AndroidThreeTen.init(this);
        appContext = getApplicationContext();




    }

    public static Context getAppContext(){
        return appContext;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // Detener el servicio cuando la aplicación se termina
        stopService(new Intent(this, DataGreenUpdateService.class));
    }

    public static AppDatabase getAppDatabase(){
        return AppDatabase.getDatabase();
    }

    public void InicializarTema() {
        editor = sharedPreferences.edit();
//        darkTheme = sharedPreferences.getBoolean("THEME_LIGTH", true);
//        if(darkTheme){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor.putBoolean("THEME_LIGTH", true).apply();// Para modo claro
//        }else{
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); // Para modo oscuro
//            editor.putBoolean("THEME_LIGTH", true).apply();// Para modo claro
//        }
//        return darkTheme;
        //activity.recreate();
    }

    public void setMainTheme(Activity activity,View view) {
        sharedPreferences = this.getSharedPreferences("objConfLocal", this.MODE_PRIVATE);
        darkTheme = sharedPreferences.getBoolean("THEME_LIGTH", true);
        editor = sharedPreferences.edit();
        Toast.makeText(this, String.valueOf(darkTheme), Toast.LENGTH_SHORT).show();
        if(darkTheme){
            editor.putBoolean("THEME_LIGTH", true).apply();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Para modo claro
        }else{
            editor.putBoolean("THEME_LIGTH", false).apply();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); // Para modo oscuro
        }
    }
}