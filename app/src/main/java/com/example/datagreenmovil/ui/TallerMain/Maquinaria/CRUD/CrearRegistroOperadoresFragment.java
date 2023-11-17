package com.example.datagreenmovil.ui.TallerMain.Maquinaria.CRUD;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.datagreenmovil.R;

public class CrearRegistroOperadoresFragment extends Fragment {

    private CrearRegistroOperadoresViewModel mViewModel;

    public static CrearRegistroOperadoresFragment newInstance() {
        return new CrearRegistroOperadoresFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_taller_maquinaria_crear_registro_operadores, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CrearRegistroOperadoresViewModel.class);
        // TODO: Use the ViewModel
    }

}