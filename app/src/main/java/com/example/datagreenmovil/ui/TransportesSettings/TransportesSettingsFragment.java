package com.example.datagreenmovil.ui.TransportesSettings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.Sync.SyncDBSQLToSQLite;
import com.example.datagreenmovil.databinding.FragmentTransportesSettingsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TransportesSettingsFragment extends Fragment {
    Context ctx;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean permitirTrabajadoresDesconocidos;
    List<String> tablasSeleccionadas = new ArrayList<>();
    private FragmentTransportesSettingsBinding binding;
    private boolean stateTareos;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TransportesSettingsViewModel transportesSettingsViewModel = new ViewModelProvider(this).get(TransportesSettingsViewModel.class);
        permitirTrabajadoresDesconocidos = sharedPreferences.getBoolean("PERMITIR_TRABAJADORES_DESCONOCIDOS", true);


        binding = FragmentTransportesSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.switchTrabajadoresDesconocidos.setEnabled(false);
        binding.switchTrabajadoresDesconocidos.setChecked(permitirTrabajadoresDesconocidos);

        binding.txvTransportes.setOnClickListener(v -> {
            stateTareos = !stateTareos;
            checkAllTransportes(stateTareos);
        });

        binding.switchVehiculos.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tablasSeleccionadas.add("mst_Vehiculos");
            } else {
                tablasSeleccionadas.remove("mst_Vehiculos");
            }
        });

        binding.switchConductores.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tablasSeleccionadas.add("mst_Conductores");
            } else {
                tablasSeleccionadas.remove("mst_Conductores");
            }
        });

        binding.switchRutas.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tablasSeleccionadas.add("mst_Rutas");
            } else {
                tablasSeleccionadas.remove("mst_Rutas");
            }
        });

        binding.switchProveedores.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tablasSeleccionadas.add("mst_Proveedores");
            } else {
                tablasSeleccionadas.remove("mst_Proveedores");
            }
        });

        binding.switchPersonas.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tablasSeleccionadas.add("mst_Personas");
            } else {
                tablasSeleccionadas.remove("mst_Personas");
            }
        });

        binding.switchUsuarios.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tablasSeleccionadas.add("mst_usuarios");
            } else {
                tablasSeleccionadas.remove("mst_usuarios");
            }
        });

        binding.switchQuerysSqlite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tablasSeleccionadas.add("mst_querysSqlite");
            } else {
                tablasSeleccionadas.remove("mst_querysSqlite");
            }
        });


        binding.switchTrabajadoresDesconocidos.setOnCheckedChangeListener((compoundButton, b) -> {
            editor.putBoolean("PERMITIR_TRABAJADORES_DESCONOCIDOS", b).apply();
            if (b) {
                Swal.success(ctx, "CAMBIO CORRECTO!", "Ahora se permitirán los trabajadores que no están registrados en la base de datos local.", 3000);
            } else {
                Swal.warning(ctx, "CAMBIO CORRECTO!", "Ahora no se permitirán los trabajadores que no están registrados en la base de datos local.", 3000);
            }
        });

        binding.btnSincronizarTransportes.setOnClickListener(view -> {
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                getActivity().runOnUiThread(()->{
                    binding.progressBar2.setVisibility(View.VISIBLE);
                });
                SyncDBSQLToSQLite syncDBSQLToSQLite = new SyncDBSQLToSQLite();
                try{
                    for(int i = 0; i < tablasSeleccionadas.size(); i ++){
                        syncDBSQLToSQLite.sincronizarData(this.getContext(),tablasSeleccionadas.get(i).toString());
                    }
                    getActivity().runOnUiThread(()->{
                        Swal.success(this.getContext(), "Sincronización completada!","Se realizo la sincronización en las tablas seleccionadas", 8000);
                    });
                }catch (Exception e){
                    getActivity().runOnUiThread(()->{
                        Log.e("ERROR", e.toString());
                        Swal.error(this.getContext(), "Oops!","Hubo un error al sincronizar las tablas: "+e.toString(), 8000);
                    });
                }finally {
                    getActivity().runOnUiThread(()->{
                        binding.progressBar2.setVisibility(View.GONE);
                    });
                }
            });
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

    public void checkAllTransportes(Boolean state){
        for(int i=0; i < binding.chipGroupTransportes.getChildCount(); i++){
            Switch switchGeneral = (Switch) binding.chipGroupTransportes.getChildAt(i);
            switchGeneral.setChecked(state);
        }
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