package com.example.datagreenmovil.ui.estandares.EstandaresSettings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.datagreenmovil.databinding.FragmentEstandaresSettingsBinding;

public class EstandaresSettingsFragment extends Fragment {

    private FragmentEstandaresSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        EstandaresSettingsViewModel slideshowViewModel =
                new ViewModelProvider(this).get(EstandaresSettingsViewModel.class);

        binding = FragmentEstandaresSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textSlideshow;
//        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}