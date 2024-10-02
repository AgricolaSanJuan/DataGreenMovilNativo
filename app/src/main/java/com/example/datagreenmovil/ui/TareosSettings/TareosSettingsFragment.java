package com.example.datagreenmovil.ui.TareosSettings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.datagreenmovil.DataGreenApp;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.Sync.SyncDBSQLToSQLite;
import com.example.datagreenmovil.TareosActivity;
import com.example.datagreenmovil.databinding.FragmentTareosSettingsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class TareosSettingsFragment extends Fragment {
    Boolean stateTareos = false;
    List<String> tablasSeleccionadas = new ArrayList<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    private FragmentTareosSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TareosSettingsViewModel tareosSettingsViewModel =
                new ViewModelProvider(this).get(TareosSettingsViewModel.class);

        sharedPreferences = this.getContext().getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        binding = FragmentTareosSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.switchPermitirSinTareo.setChecked(sharedPreferences.getBoolean("PERMITIR_SIN_TAREO", true));
        binding.switchModoPacking.setChecked(sharedPreferences.getBoolean("MODO_PACKING", false));
        binding.txvTareos2.setOnClickListener(view -> {
            stateTareos = !stateTareos;
            checkAllTareos(stateTareos);
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

        binding.btnSincronizarTareos.setOnClickListener(view -> {
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


        binding.switchPermitirSinTareo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    editor.putBoolean("PERMITIR_SIN_TAREO", true).apply();
            }
        });
        AtomicBoolean manualPush = new AtomicBoolean(false);

        binding.switchModoPacking.setOnClickListener(view -> {
            boolean b = sharedPreferences.getBoolean("MODO_PACKING", false);
            if(manualPush.get()){
                Swal.settingsPassword(getContext(), (isValid, sweetAlertDialog) -> {
                    binding.switchModoPacking.setChecked(!b);
                    if (isValid) {
                        sweetAlertDialog.dismissWithAnimation();
                        DataGreenApp.passPassed = true;
                        editor.putBoolean("MODO_PACKING", !b).apply();
                        if(manualPush.get()){
                            binding.switchModoPacking.setChecked(!b);
                        }
//                        binding.switchModoPacking.setChecked(!b);
                    } else {
                        sweetAlertDialog.dismissWithAnimation();
                        Swal.warning(getContext(), "PROHIBIDO", "La contraseña no es correcta", 2000);
//                        binding.switchModoPacking.setChecked(!b);
                    }
                    manualPush.set(false);
                    boolean cambiao = sharedPreferences.getBoolean("MODO_PACKING", false);
                    binding.switchModoPacking.setChecked(cambiao);
                });
            }else {
                manualPush.set(false);
                boolean cambiao = sharedPreferences.getBoolean("MODO_PACKING", false);
                binding.switchModoPacking.setChecked(sharedPreferences.getBoolean("MODO_PACKING", false));
            }

//            binding.switchModoPacking.setSelected(true);
//            binding.switchModoPacking.setChecked(true);
            boolean modoPacking = sharedPreferences.getBoolean("MODO_PACKING", false);
            binding.switchModoPacking.setChecked(modoPacking);
        });

        binding.switchModoPacking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                compoundButton.setChecked(!b);
                manualPush.set(true);
            }
        });

        binding.fabAbrirDrawer.setOnClickListener(view -> {
            TareosActivity tareosActivity = (TareosActivity) getActivity();
            if(tareosActivity.obtenerDrawer() != null){
                DrawerLayout dl = tareosActivity.obtenerDrawer();
//                OCULTAR BOTONES DE ACCIONES PARA TAREOS
                dl.findViewById(R.id.nav_item_transferir).setVisibility(View.GONE);
                dl.findViewById(R.id.nav_item_borrar).setVisibility(View.GONE);
                dl.findViewById(R.id.nav_item_extorno).setVisibility(View.GONE);
//                ABRIR DRAWER
                dl.openDrawer(GravityCompat.START);
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

        binding.switchQuerysSqlite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!!b){
                    tablasSeleccionadas.add("mst_querysSqlite");
                }else {
                    tablasSeleccionadas.remove("mst_querysSqlite");
                }
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void checkAllTareos(Boolean state){
        for(int i=0; i < binding.chipGroupTareos.getChildCount(); i++){
            Switch switchGeneral = (Switch) binding.chipGroupTareos.getChildAt(i);
            switchGeneral.setChecked(state);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}