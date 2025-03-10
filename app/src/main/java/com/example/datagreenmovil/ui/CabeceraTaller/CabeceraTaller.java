package com.example.datagreenmovil.ui.CabeceraTaller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.datagreenmovil.databinding.FragmentMaquinariaBinding;
import com.example.datagreenmovil.viewModel.SharedViewModel;

public class CabeceraTaller extends Fragment {
    private FragmentMaquinariaBinding binding;
    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Obtener instancia del SharedViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        binding = FragmentMaquinariaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Ejemplo: Observar cambios en la maquinaria seleccionada
        sharedViewModel.getMaquinariaSeleccionada().observe(getViewLifecycleOwner(), maquinaria -> {
            // Aquí puedes actualizar la UI con la maquinaria seleccionada
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
