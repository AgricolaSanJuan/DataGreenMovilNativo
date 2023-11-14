package com.example.datagreenmovil.ui.SettingsAbout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.databinding.FragmentSettingsAboutBinding;

public class SettingsAboutFragment extends Fragment {
    Context ctx;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private FragmentSettingsAboutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsAboutViewModel settingsAboutViewModel =
                new ViewModelProvider(this).get(SettingsAboutViewModel.class);

        binding = FragmentSettingsAboutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.switchTrabajadoresDesconocidos.setOnCheckedChangeListener((compoundButton, b) -> {
            editor.putBoolean("PERMITIR_TRABAJADORES_DESCONOCIDOS", b).apply();
            if(b){
                Swal.success(ctx, "CAMBIO CORRECTO!", "Ahora se permitirán los trabajadores que no están registrados en la base de datos local.", 3000);
            }else{
                Swal.warning(ctx, "CAMBIO CORRECTO!", "Ahora no se permitirán los trabajadores que no están registrados en la base de datos local.", 3000);
            }
        });

//        final TextView textView = binding.textGallery;
//        settingsAboutViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ctx = context;
        sharedPreferences = ctx.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
}