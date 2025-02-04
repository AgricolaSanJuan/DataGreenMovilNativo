package com.example.datagreenmovil;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datagreenmovil.databinding.ActivityTareosBinding;

import java.util.ArrayList;

public class TareosActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityTareosBinding binding;
    DrawerLayout drawer;
    private SQLiteDatabase database;
    NavigationView navigationView;
    Context ctx;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<String> idsSeleccionados;
    ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataGreenApp dataGreenApp = (DataGreenApp) getApplication();
        super.onCreate(savedInstanceState);
        idsSeleccionados = new ArrayList<>();
        sharedPreferences = this.getSharedPreferences("objConfLocal",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        ctx = this;



//        editor.putBoolean("MODO_PACKING", true).apply();


//        PACKINGINTENSIVO
        database = SQLiteDatabase.openDatabase(ctx.getDatabasePath("DataGreenMovil.db").toString(), null, SQLiteDatabase.OPEN_READWRITE);



        boolean columnaIngresoExiste = false;
        boolean columnaSalidaExiste = false;
        boolean columnaHomologarExiste = false;



        Cursor cursorText = database.rawQuery("PRAGMA table_info(trx_tareos_detalle)", null);

        int posicionColumna = 0;

        for(int i = 0; i < cursorText.getCount(); i++){
            cursorText.moveToNext();
            posicionColumna = cursorText.getColumnIndex("name");
            String columnName = cursorText.getString(posicionColumna);
//            Log.i("COLUMNA", columnName);
            if ("ingreso".equals(columnName)) {
                columnaIngresoExiste = true;
            } else if ("salida".equals(columnName)) {
                columnaSalidaExiste = true;
            } else if("homologar".equals(columnName)){
                columnaHomologarExiste = true;
            }

        }

        if (!columnaIngresoExiste) {
            database.execSQL("ALTER TABLE trx_tareos_detalle ADD COLUMN ingreso TEXT;");
            database.execSQL("UPDATE trx_tareos_detalle set ingreso = ''");
        }

        if (!columnaSalidaExiste) {
            database.execSQL("ALTER TABLE trx_tareos_detalle ADD COLUMN salida TEXT;");
            database.execSQL("UPDATE trx_tareos_detalle set salida = ''");
        }

        if (!columnaHomologarExiste) {
            database.execSQL("ALTER TABLE trx_tareos_detalle ADD COLUMN homologar INTEGER;");
            database.execSQL("UPDATE trx_tareos_detalle set homologar = 0");
        }

        database.close();


//        END PACKINGINTENSIVO



        binding = ActivityTareosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        drawer = binding.drawerLayoutTareos;
        navigationView = binding.navViewTareos;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_tareos_main,
                R.id.nav_tareos_settings)
                .setOpenableLayout(drawer)
                .build();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            imageButton = findViewById(R.id.nav_header_tareos).findViewById(R.id.btnStyle);
            imageButton.setImageDrawable(getDrawable(R.drawable.add_person));
        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_tareos);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    public void setDarkTheme(View view) {
//        DataGreenApp dataGreenApp = (DataGreenApp) getApplication();
//        dataGreenApp.setMainTheme(this,view);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_tareos);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public DrawerLayout obtenerDrawer() {
        return drawer;
    }

    public NavigationView obtenerNavigationView() { return navigationView; }
}