package com.example.datagreenmovil;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

public class DataGreenApp extends Application {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean darkTheme;

    public void InicializarTema() {
        sharedPreferences = this.getSharedPreferences("objConfLocal", MODE_PRIVATE);
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