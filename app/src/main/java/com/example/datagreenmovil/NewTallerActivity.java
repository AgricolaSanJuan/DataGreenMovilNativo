package com.example.datagreenmovil;

import android.os.Bundle;

import com.example.datagreenmovil.DAO.Estandares.Maquinaria.Maquinaria;
import com.example.datagreenmovil.DAO.Estandares.TrxDParteMaquinaria.TrxDParteMaquinaria;
import com.example.datagreenmovil.DAO.Estandares.TrxParteMaquinaria.TrxParteMaquinaria;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.datagreenmovil.databinding.ActivityNewTallerBinding;

import java.util.ArrayList;
import java.util.List;

public class NewTallerActivity extends AppCompatActivity {

    public TrxParteMaquinaria maquinaria = new TrxParteMaquinaria();
    public List<TrxDParteMaquinaria>  listaDParteMaquinaria = new ArrayList<>();



    private ActivityNewTallerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewTallerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home
                , R.id.navigation_dashboard, R.id.navigation_notifications
        )
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_new_taller);
//        NavigationUI.setupActionBarWithNavController(this, navController);
////        NavigationUI.setupWithNavController(binding.navView, navController);
//        NavigationUI.setupWithNavController(navView, navController);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public String getIdParteMaquinaria(){
        return maquinaria.getIdMaquinaria();
    }

    public int getCantidadDParteMaquinaria(){
        return listaDParteMaquinaria.size();
    }

}