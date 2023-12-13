package com.example.datagreenmovil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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