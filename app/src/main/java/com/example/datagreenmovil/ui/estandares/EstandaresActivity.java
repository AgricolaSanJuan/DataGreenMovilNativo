package com.example.datagreenmovil.ui.estandares;

import android.os.Bundle;
import android.view.Menu;

import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.Utilidades.Filtros;
import com.example.datagreenmovil.databinding.ActivityEstandaresBinding;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

public class EstandaresActivity extends AppCompatActivity {
//public class EstandaresActivity extends AppCompatActivity implements Filtros.GetFilterData {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityEstandaresBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEstandaresBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_estandares_main, R.id.nav_estandares_registro, R.id.nav_estandares_settings)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_estandares);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.estandares, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_estandares);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

//    @Override
//    public void onChangeFilterData(Swal.DialogResult filterData) {
//        Swal.info(this, "hola", filterData.getMensaje(), 5000);
//    }
}