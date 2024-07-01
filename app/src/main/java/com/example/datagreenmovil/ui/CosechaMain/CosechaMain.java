package com.example.datagreenmovil.ui.CosechaMain;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;

import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.datagreenmovil.CosechaActivity;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.TransportesActivity;
import com.example.datagreenmovil.databinding.FragmentCosechaMainBinding;
import com.google.android.material.navigation.NavigationView;

public class CosechaMain extends Fragment {

    private FragmentCosechaMainBinding binding;
    private Context ctx;
    private Animation animScale;
    public NavController navController;
    private NavController nc;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCosechaMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        animScale = AnimationUtils.loadAnimation(ctx, R.anim.scale_animation_card);

        binding.cardCosechador.setOnClickListener(view -> {
            navigateToCosechadorScreen();
        });

        binding.cardConductor.setOnClickListener(view -> {
            navigateToConductorScreen();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void navigateToCosechadorScreen() {
        // Navegar al destino correspondiente para la pantalla de Cosechador
        binding.cardCosechador.startAnimation(animScale);
        CosechaActivity cosechaActivity = (CosechaActivity) getActivity();
        if(cosechaActivity.obtenerNavigationController() != null){
            nc = cosechaActivity.obtenerNavigationController();
            nc.navigate(R.id.nav_cosecha_transferencia,
                    null,
                    new NavOptions.Builder().setPopUpTo(R.id.nav_cosecha_submodulos, true).build());
        }
    }

    private void navigateToConductorScreen() {
        // Navegar al destino correspondiente para la pantalla de Conductor
        binding.cardCosechador.startAnimation(animScale);
        CosechaActivity cosechaActivity = (CosechaActivity) getActivity();
        if(cosechaActivity.obtenerNavigationController() != null){
            nc = cosechaActivity.obtenerNavigationController();
            nc.navigate(R.id.nav_cosecha_conductor,
                    null,
                    new NavOptions.Builder().setPopUpTo(R.id.nav_cosecha_submodulos, true).build());
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ctx = context;
    }
}