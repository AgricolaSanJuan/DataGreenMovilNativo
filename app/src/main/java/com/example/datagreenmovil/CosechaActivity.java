package com.example.datagreenmovil;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datagreenmovil.databinding.ActivityCosechaBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CosechaActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityCosechaBinding binding;
    NavigationView navigationView;
    private NavController navController;
    Snackbar snackbar;
    String trabajadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCosechaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        setSupportActionBar(binding.appBarCosecha.toolbar);
        binding.appBarCosecha.fabGenerarQRBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar = Snackbar.make(view, "Se está generando el código QR, por favor, espere...", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Action", null);
                snackbar.show();
                if(snackbar.isShown()){
                    navigateToQR();
                }
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_cosecha_conductor, R.id.nav_cosecha_transferencia, R.id.nav_cosecha_submodulos)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_cosecha);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    public void navigateToQR() {
        // Obtener los trabajadores de la actividad implementando la interfaz
        // Crear Bundle y pasar los trabajadores como String
        Bundle parametros = new Bundle();
        parametros.putString("trabajadores", trabajadores.toString());

        // Navegar con los parámetros
        NavController nc = obtenerNavigationController();
        if (nc != null) {
            nc.navigate(R.id.nav_cosecha_transferencia, parametros, new NavOptions.Builder().setPopUpTo(R.id.nav_cosecha_transferencia, true).build());
            // Configurar el contador para despedir el snackbar
            CountDownTimer countDownTimer = new CountDownTimer(1500, 1500) {
                @Override
                public void onTick(long l) {}

                @Override
                public void onFinish() { snackbar.dismiss(); }
            };
            countDownTimer.start();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cosecha, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_cosecha);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public NavController obtenerNavigationController() { return navController; }

    public void setTrabajadores(String trabajadores){
        this.trabajadores = trabajadores;
    }

    public void setQuantityTrabajador(int cantidad, int position) throws JSONException {
        JSONObject jsonObject = new JSONObject(this.trabajadores);
        JSONArray jsonArray = jsonObject.getJSONArray("detalles");
        jsonArray.getJSONObject(position).put("cantidad", cantidad);
        Log.i("INFO DEBUG", jsonObject.toString());
    }

}