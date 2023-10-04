package com.example.datagreenmovil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;

import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Logica.Swal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datagreenmovil.databinding.ActivityTareosBinding;

public class TareosActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityTareosBinding binding;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTareosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        setSupportActionBar(binding.appBarTareos.toolbar);

        drawer = binding.drawerLayoutTareos;

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

        NavigationView navigationView = binding.navViewTareos;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_tareos_main, R.id.nav_tareos_settings)
                .setOpenableLayout(drawer)
                .build();

        FloatingActionButton nuevoTareo = (FloatingActionButton) binding.getRoot().findViewById(R.id.c005_fab_MainTareos_NuevoTareo_v);
        nuevoTareo.setOnClickListener(view -> {
            abrirDocumento(null);
        });

        FloatingActionButton opciones = (FloatingActionButton) binding.getRoot().findViewById(R.id.c005_fab_MainTareos_Opciones_v);
        opciones.setOnClickListener(view -> {
            drawer.openDrawer(GravityCompat.START);
        });

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_tareos);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.tareos, menu);
//        return true;
//    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_tareos);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void abrirDocumento(String id) {
        ConfiguracionLocal objConfLocal = null;
        Intent nuevaActividad = new Intent(this, cls_05010000_Edicion.class);
        nuevaActividad.putExtra("ConfiguracionLocal",objConfLocal);
        nuevaActividad.putExtra("IdDocumentoActual",id);
        startActivity(nuevaActividad);
    }
}