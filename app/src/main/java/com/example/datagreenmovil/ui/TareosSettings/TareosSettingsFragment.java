package com.example.datagreenmovil.ui.TareosSettings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.datagreenmovil.databinding.FragmentTareosSettingsBinding;

public class TareosSettingsFragment extends Fragment {

    private FragmentTareosSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TareosSettingsViewModel tareosSettingsViewModel =
                new ViewModelProvider(this).get(TareosSettingsViewModel.class);

        binding = FragmentTareosSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}