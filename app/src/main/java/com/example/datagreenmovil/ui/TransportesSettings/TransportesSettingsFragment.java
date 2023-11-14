package com.example.datagreenmovil.ui.TransportesSettings;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.Rex;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.databinding.FragmentTransportesSettingsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TransportesSettingsFragment extends Fragment {
    Context ctx;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean permitirTrabajadoresDesconocidos;
    private FragmentTransportesSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        TransportesSettingsViewModel transportesSettingsViewModel =
                new ViewModelProvider(this).get(TransportesSettingsViewModel.class);
        permitirTrabajadoresDesconocidos = sharedPreferences.getBoolean("PERMITIR_TRABAJADORES_DESCONOCIDOS", false);




    binding = FragmentTransportesSettingsBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

    binding.switchTrabajadoresDesconocidos.setChecked(permitirTrabajadoresDesconocidos);

    binding.switchTrabajadoresDesconocidos.setOnCheckedChangeListener((compoundButton, b) -> {
        editor.putBoolean("PERMITIR_TRABAJADORES_DESCONOCIDOS", b).apply();
        if(b){
            Swal.success(ctx, "CAMBIO CORRECTO!", "Ahora se permitirán los trabajadores que no están registrados en la base de datos local.", 3000);
        }else{
            Swal.warning(ctx, "CAMBIO CORRECTO!", "Ahora no se permitirán los trabajadores que no están registrados en la base de datos local.", 3000);
        }
    });

//    binding.switchReproducirSonidoAlerta.setOnCheckedChangeListener((compoundButton, b) -> {
//        editor.putBoolean("REPRODUCIR_SONIDO_ALERTA", b).apply();
//        if(b){
//            Swal.success(ctx, "CAMBIO CORRECTO!", "Ahora se reproducirá un sonido de alerta cuando se detece un trabajador desconocido", 3000);
//        }else{
//            Swal.warning(ctx, "CAMBIO CORRECTO!", "Se dejará de reproducir un sonido de alerta cuando se detece un trabajador desconocido", 3000);
//        }
//    });

//        final TextView textView = binding.textSlideshow;
//        transportesSettingsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
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