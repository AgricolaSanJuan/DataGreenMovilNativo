package com.example.datagreenmovil.ui.estandares.EstandaresRegistro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.datagreenmovil.databinding.FragmentEstandaresMainBinding;

public class EstandaresRegistroFragment extends Fragment {

    private FragmentEstandaresMainBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        EstandaresRegistroViewModel galleryViewModel =
                new ViewModelProvider(this).get(EstandaresRegistroViewModel.class);

        binding = FragmentEstandaresMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}