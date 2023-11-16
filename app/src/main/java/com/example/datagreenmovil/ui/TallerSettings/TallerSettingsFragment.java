package com.example.datagreenmovil.ui.TallerSettings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.datagreenmovil.databinding.FragmentTallerSettingsBinding;

public class TallerSettingsFragment extends Fragment {

    private FragmentTallerSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TallerSettingsViewModel tallerSettingsViewModel =
                new ViewModelProvider(this).get(TallerSettingsViewModel.class);

        binding = FragmentTallerSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}