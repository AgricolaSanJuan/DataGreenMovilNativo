package com.example.datagreenmovil.ui.DetalleTaller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.datagreenmovil.databinding.FragmentParronBinding;
import com.example.datagreenmovil.viewModel.SharedViewModel;

public class DetalleTaller extends Fragment {
    private FragmentParronBinding binding;
    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Obtener instancia del SharedViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        binding = FragmentParronBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Observar cambios en la lista de detalles de maquinaria
        sharedViewModel.getListaParteMaquinaria().observe(getViewLifecycleOwner(), detalles -> {
            // Aquí puedes actualizar un RecyclerView con los detalles
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
