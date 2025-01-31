package com.example.datagreenmovil;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.datagreenmovil.databinding.ActivityEvaluacionesBinding;

public class EvaluacionesActivity extends AppCompatActivity {

    private ActivityEvaluacionesBinding binding;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEvaluacionesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configuración de navegación
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_evaluaciones);

        // Sin necesidad de AppBarConfiguration si no vas a usar ActionBar
        mAppBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

        // Comentar o eliminar esta línea ya que no usarás el ActionBar
        // NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        // Si no deseas el ActionBar, esta línea puede ser omitida también
        // NavigationUI.setupWithNavController(this, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Navegar hacia arriba sin ActionBar
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_evaluaciones);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
