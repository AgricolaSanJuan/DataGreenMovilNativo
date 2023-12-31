package com.example.datagreenmovil.ui.TareosSettings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.Sync.SyncDBSQLToSQLite;
import com.example.datagreenmovil.TareosActivity;
import com.example.datagreenmovil.databinding.FragmentTareosSettingsBinding;
import com.google.android.material.internal.NavigationMenuItemView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TareosSettingsFragment extends Fragment {
    Boolean stateTareos = false;
    List<String> tablasSeleccionadas = new ArrayList<>();
    private FragmentTareosSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TareosSettingsViewModel tareosSettingsViewModel =
                new ViewModelProvider(this).get(TareosSettingsViewModel.class);

        binding = FragmentTareosSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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
                        Swal.error(this.getContext(), "Oops!","Hubo un error al sincronizar las tablas: "+e.toString(), 8000);
                    });
                }finally {
                    getActivity().runOnUiThread(()->{
                        binding.progressBar2.setVisibility(View.GONE);
                    });
                }
            });
        });

        binding.fabAbrirDrawer.setOnClickListener(view -> {
            TareosActivity tareosActivity = (TareosActivity) getActivity();
            if(tareosActivity.obtenerDrawer() != null){
                DrawerLayout dl = tareosActivity.obtenerDrawer();
                NavigationMenuItemView syncBtn = dl.findViewById(R.id.nav_item_sync);
                syncBtn.setVisibility(View.INVISIBLE);
                dl.openDrawer(GravityCompat.START);
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
}