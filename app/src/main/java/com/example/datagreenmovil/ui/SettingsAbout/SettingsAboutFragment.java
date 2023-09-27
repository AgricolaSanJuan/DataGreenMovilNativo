package com.example.datagreenmovil.ui.SettingsAbout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.datagreenmovil.databinding.FragmentSettingsAboutBinding;

public class SettingsAboutFragment extends Fragment {

    private FragmentSettingsAboutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsAboutViewModel settingsAboutViewModel =
                new ViewModelProvider(this).get(SettingsAboutViewModel.class);

        binding = FragmentSettingsAboutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textGallery;
//        settingsAboutViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}