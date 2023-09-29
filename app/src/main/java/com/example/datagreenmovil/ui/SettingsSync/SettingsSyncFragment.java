package com.example.datagreenmovil.ui.SettingsSync;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.Sync.SyncDBSQLToSQLite;
import com.example.datagreenmovil.SyncDBService;
import com.example.datagreenmovil.databinding.FragmentSettingsSyncBinding;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SettingsSyncFragment extends Fragment {

    private FragmentSettingsSyncBinding binding;
    Boolean stateGeneral = false;
    Boolean stateTareos = false;
    Boolean stateTransportes = false;
    ConfiguracionLocal objConfLocal;
    Context ctx;
    List<String> tablasSeleccionadas = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsSyncViewModel settingsLocalViewModel =
                new ViewModelProvider(this).get(SettingsSyncViewModel.class);

        binding = FragmentSettingsSyncBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.switchTodas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    checkAll(b, true);
            }
        });

        binding.txvGeneral.setOnClickListener(view -> {
            stateGeneral = !stateGeneral;
            checkAllGeneral(stateGeneral);
        });

        binding.txvTareos.setOnClickListener(view -> {
            stateTareos = !stateTareos;
            checkAllTareos(stateTareos);
        });

        binding.txvTransportes.setOnClickListener(view -> {
            stateTransportes = !stateTransportes;
            checkAllTransportes(stateTransportes);
        });

        binding.fabSyncInfo.setOnClickListener(view -> {
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                getActivity().runOnUiThread(()->{
                    binding.progressBar.setVisibility(View.VISIBLE);
                });
                SyncDBSQLToSQLite syncDBSQLToSQLite = new SyncDBSQLToSQLite();
                Boolean sync;
                for(int i = 0; i < tablasSeleccionadas.size(); i ++){
                    try {
                        syncDBSQLToSQLite.sincronizar(ctx, objConfLocal, tablasSeleccionadas.get(i).toString(), tablasSeleccionadas.get(i).toString());
                    } catch (Exception e) {
                        Log.e("ERROR",e.toString());
                    }
                }
                getActivity().runOnUiThread(()->{
                    binding.progressBar.setVisibility(View.GONE);
                    Swal.success(ctx, "Sincronización completada!","Se realizo la sincronización en las tablas seleccionadas", 8000);
                });
            });

        });
        binding.switchEmpresasVsModulos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!!b) {
                    tablasSeleccionadas.add("crs_empresasvsmodulos");
                } else {
                    tablasSeleccionadas.remove("crs_empresasvsmodulos");
                }
            }
        });
        binding.switchDispositivosMoviles.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!!b){
                    tablasSeleccionadas.add("mst_dispositivosmoviles");
                }else {
                    tablasSeleccionadas.remove("mst_dispositivosmoviles");
                }
            }
        });
        binding.switchEmpresas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!!b){
                    tablasSeleccionadas.add("mst_empresas");
                }else {
                    tablasSeleccionadas.remove("mst_empresas");
                }
            }
        });
        binding.switchEstados.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!!b){
                    tablasSeleccionadas.add("mst_estados");
                }else {
                    tablasSeleccionadas.remove("mst_estados");
                }
            }
        });
        binding.switchOpcionesConfiguracion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!!b){
                    tablasSeleccionadas.add("mst_opcionesconfiguracion");
                }else {
                    tablasSeleccionadas.remove("mst_opcionesconfiguracion");
                }
            }
        });
        binding.switchQuerysSqlite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!!b){
                    tablasSeleccionadas.add("mst_queryssqlite");
                }else {
                    tablasSeleccionadas.remove("mst_queryssqlite");
                }
            }
        });
        binding.switchTablas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!!b){
                    tablasSeleccionadas.add("mst_tablas");
                }else {
                    tablasSeleccionadas.remove("mst_tablas");
                }
            }
        });
        binding.switchUsuarios.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!!b){
                    tablasSeleccionadas.add("mst_usuarios");
                }else {
                    tablasSeleccionadas.remove("mst_usuarios");
                }
            }
        });
        binding.switchOpcionesConfiguracion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!!b){
                    tablasSeleccionadas.add("mst_opcionesconfiguracion");
                }else {
                    tablasSeleccionadas.remove("mst_opcionesconfiguracion");
                }
            }
        });
        binding.switchCorrelativos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!!b){
                    tablasSeleccionadas.add("trx_correlativos");
                }else {
                    tablasSeleccionadas.remove("trx_correlativos");
                }
            }
        });
        binding.switchPersonas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!!b){
                    tablasSeleccionadas.add("mst_personas");
                }else {
                    tablasSeleccionadas.remove("mst_personas");
                }
            }
        });
        binding.switchLabores.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!!b){
                    tablasSeleccionadas.add("mst_labores");
                }else {
                    tablasSeleccionadas.remove("mst_labores");
                }
            }
        });
        binding.switchModulos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!!b){
                    tablasSeleccionadas.add("mst_modulos");
                }else {
                    tablasSeleccionadas.remove("mst_modulos");
                }
            }
        });
        binding.switchConsumidores.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!!b){
                    tablasSeleccionadas.add("mst_consumidores");
                }else {
                    tablasSeleccionadas.remove("mst_consumidores");
                }
            }
        });
        binding.switchCultivos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!!b){
                    tablasSeleccionadas.add("mst_cultivos");
                }else {
                    tablasSeleccionadas.remove("mst_cultivos");
                }
            }
        });
        binding.switchDias.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!!b){
                    tablasSeleccionadas.add("mst_dias");
                }else {
                    tablasSeleccionadas.remove("mst_dias");
                }
            }
        });
        binding.switchVariedades.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!!b){
                    tablasSeleccionadas.add("mst_variedades");
                }else {
                    tablasSeleccionadas.remove("mst_variedades");
                }
            }
        });
        binding.switchTurnos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!!b){
                    tablasSeleccionadas.add("mst_turnos");
                }else {
                    tablasSeleccionadas.remove("mst_turnos");
                }
            }
        });
        binding.switchActividades.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!!b){
                    tablasSeleccionadas.add("mst_actividades");
                }else {
                    tablasSeleccionadas.remove("mst_actividades");
                }
            }
        });
        binding.switchVehiculos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!!b){
                    tablasSeleccionadas.add("mst_vehiculos");
                }else {
                    tablasSeleccionadas.remove("mst_vehiculos");
                }
            }
        });
        binding.switchConductores.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!!b){
                    tablasSeleccionadas.add("mst_conductores");
                }else {
                    tablasSeleccionadas.remove("mst_conductores");
                }
            }
        });
        binding.switchRutas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!!b){
                    tablasSeleccionadas.add("mst_rutas");
                }else {
                    tablasSeleccionadas.remove("mst_rutas");
                }
            }
        });
        binding.switchProveedores.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!!b){
                    tablasSeleccionadas.add("mst_proveedores");
                }else {
                    tablasSeleccionadas.remove("mst_proveedores");
                }
            }
        });
        return root;
    }

    public void checkAll(Boolean state, Boolean unableAll){
        if(!!unableAll){
            checkAllGeneral(state);
            checkAllTareos(state);
            checkAllTransportes(state);
        }
    }
    public void checkAllGeneral(Boolean state){
        if(!state){
            binding.switchTodas.setChecked(false);
            checkAll(state, false);
        }
        for(int i=0; i < binding.chipGroupGeneral.getChildCount(); i++){
            Switch switchGeneral = (Switch) binding.chipGroupGeneral.getChildAt(i);
            switchGeneral.setChecked(state);
        }
    }
    public void checkAllTareos(Boolean state){
        if(!state){
            binding.switchTodas.setChecked(false);
            checkAll(state, false);
        }
        for(int i=0; i < binding.chipGroupTareos.getChildCount(); i++){
            Switch switchGeneral = (Switch) binding.chipGroupTareos.getChildAt(i);
            switchGeneral.setChecked(state);
        }
    }
    public void checkAllTransportes(Boolean state){
        if(!state){
            binding.switchTodas.setChecked(false);
            checkAll(state, false);
        }
        for(int i=0; i < binding.chipGroupTransportes.getChildCount(); i++){
            Switch switchGeneral = (Switch) binding.chipGroupTransportes.getChildAt(i);
            switchGeneral.setChecked(state);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ctx = context;
        objConfLocal = (ConfiguracionLocal) getActivity().getIntent().getSerializableExtra("ConfiguracionLocal");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}