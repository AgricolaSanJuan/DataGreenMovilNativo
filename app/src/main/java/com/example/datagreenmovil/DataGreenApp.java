package com.example.datagreenmovil;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

public class DataGreenApp extends Application {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean darkTheme;
    public void setDarkTheme(Activity activity) {
        sharedPreferences = this.getSharedPreferences("objConfLocal", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("THEME_LIGTH", !darkTheme).apply();
        if(darkTheme){
            activity.setTheme(R.style.Theme_DataGreenMovilDark);
        }else{
            activity.setTheme(R.style.Theme_DataGreenMovilLight);
        }
        //activity.recreate();
    }
    public void InicializarTema(Activity activity) {
        sharedPreferences = this.getSharedPreferences("objConfLocal", MODE_PRIVATE);
        darkTheme = sharedPreferences.getBoolean("THEME_LIGTH", true);
        if(darkTheme){
            activity.setTheme(R.style.Theme_DataGreenMovilLight);
        }else{
            activity.setTheme(R.style.Theme_DataGreenMovilDark);
        }
        //activity.recreate();
    }
}