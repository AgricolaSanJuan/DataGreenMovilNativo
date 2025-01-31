package com.example.datagreenmovil.ui.Evaluaciones.EvaluacionesMain;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.cls_08010000_Edicion;
import com.example.datagreenmovil.databinding.FragmentEvaluacionesBinding;
import com.example.datagreenmovil.databinding.FragmentEvaluacionesMainBinding;
import com.example.datagreenmovil.ui.Evaluaciones.EvaluacionesFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EvaluacionesMainFragment extends Fragment {
    Context ctx;
    FloatingActionButton Evaluacion_fab_NuevoRegistro;

    private EvaluacionesMainViewModel mViewModel;
    private FragmentEvaluacionesMainBinding binding;

    public static EvaluacionesMainFragment newInstance() {
        return new EvaluacionesMainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentEvaluacionesMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.etfabNuevoRegistroEvaluacion.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_navigation_home_main_to_navigation_home);
        });


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EvaluacionesMainViewModel.class);
        // TODO: Use the ViewModel
    }

}