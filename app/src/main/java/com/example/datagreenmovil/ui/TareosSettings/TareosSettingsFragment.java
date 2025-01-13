package com.example.datagreenmovil.ui.TareosSettings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    Boolean stateLock = true;
    List<String> tablasSeleccionadas = new ArrayList<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    private FragmentTareosSettingsBinding binding;
    private boolean manualPush = false;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TareosSettingsViewModel tareosSettingsViewModel = new ViewModelProvider(this).get(TareosSettingsViewModel.class);

        sharedPreferences = this.getContext().getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        binding = FragmentTareosSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        <!--            ITEMS_PER_PAGE = sharedPreferences.getInt("ITEMS_PER_PAGE_TAREOS", 20);-->
//            <!--            MOSTRAR_CONTROLES_HORAS_TAREOS = sharedPreferences.getBoolean("MOSTRAR_CONTROLES_HORAS_TAREOS", false);-->
//            <!--            AGREGAR_SOLO_CAMARA_TAREOS = sharedPreferences.getBoolean("AGREGAR_SOLO_CAMARA_TAREOS", false);-->
//            <!--            REPRODUCIR_SONIDO_TAREOS = sharedPreferences.getBoolean("REPRODUCIR_SONIDO_TAREOS", false);-->

        binding.switchMostrarControlesHoras.setChecked(sharedPreferences.getBoolean("MOSTRAR_CONTROLES_HORAS_TAREOS", false));
        binding.switchMostrarControlesRdtos.setChecked(sharedPreferences.getBoolean("MOSTRAR_CONTROLES_RDTOS_TAREOS", false));
        binding.switchReproducirSonido.setChecked(sharedPreferences.getBoolean("REPRODUCIR_SONIDO_TAREOS", false));
        binding.switchAgregarSoloCamara.setChecked(sharedPreferences.getBoolean("AGREGAR_SOLO_CAMARA_TAREOS", false));
        binding.etItemsPerPage.setText(String.valueOf(sharedPreferences.getInt("ITEMS_PER_PAGE_TAREOS", 20)));
        binding.switchValidarTrabajadorRepetido.setChecked(sharedPreferences.getBoolean("VALIDAR_TRABAJADOR_REPETIDO", false));
        binding.switchActivarPermiso.setChecked(sharedPreferences.getBoolean("ACTIVAR_PERMISO", false));
        binding.switchAutoSalida.setChecked(sharedPreferences.getBoolean("SALIDA_AUTOMATICA", false));
        binding.switchMostrarSwitchEntradaSalida.setChecked(sharedPreferences.getBoolean("MOSTRAR_ENTRADA_SALIDA", false));

        activarControles(false);

        binding.switchMostrarSwitchEntradaSalida.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("MOSTRAR_ENTRADA_SALIDA", isChecked).apply();
        });

        binding.switchAutoSalida.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("SALIDA_AUTOMATICA", isChecked).apply();
        });

        binding.switchMostrarControlesHoras.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("MOSTRAR_CONTROLES_HORAS_TAREOS", isChecked).apply();
        });

        binding.switchMostrarControlesRdtos.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("MOSTRAR_CONTROLES_RDTOS_TAREOS", isChecked).apply();
        });

        binding.switchActivarPermiso.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("ACTIVAR_PERMISO", isChecked).apply();
        });

        binding.switchReproducirSonido.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("REPRODUCIR_SONIDO_TAREOS", isChecked).apply();
        });

        binding.switchAgregarSoloCamara.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("AGREGAR_SOLO_CAMARA_TAREOS", isChecked).apply();
        });

        binding.switchValidarTrabajadorRepetido.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("VALIDAR_TRABAJADOR_REPETIDO", isChecked).apply();
        });

        binding.fabLockOptions.setOnClickListener(v -> {
            if(stateLock){
                Swal.settingsPassword(getContext(), (isValid, sweetAlertDialog) -> {
                    stateLock = !stateLock;
                    if (isValid) {
                        sweetAlertDialog.dismissWithAnimation();
                        DataGreenApp.passPassed = true;
//                    editor.putBoolean("MODO_PACKING", !stateLock).apply();
                        activarControles(!stateLock);
//                    if (manualPush.get()) {
//                        binding.switchModoPacking.setChecked(!b);
//                    }
//                        binding.switchModoPacking.setChecked(!b);
                    } else {
                        sweetAlertDialog.dismissWithAnimation();
                        Swal.warning(getContext(), "PROHIBIDO", "La contraseña no es correcta", 2000);
//                        binding.switchModoPacking.setChecked(!b);
                    }
//                manualPush.set(false);
                    boolean cambiao = sharedPreferences.getBoolean("MODO_PACKING", false);
                    binding.switchModoPacking.setChecked(cambiao);
                });
            }else {
                stateLock = !stateLock;
                activarControles(!stateLock);
            }


//            stateLock = !stateLock;
//            activarControles(!stateLock);
        });

        binding.etItemsPerPage.addTextChangedListener(new TextWatcher() {
            String oldText = "";
            String newText = "";
            boolean returnDefault = false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().isEmpty()){
//                    binding.etItemsPerPage.setText(oldText);
                    returnDefault = true;
                }
                newText = binding.etItemsPerPage.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(returnDefault){
                    editor.putInt("ITEMS_PER_PAGE_TAREOS", 20).apply();
                }else {
                    editor.putInt("ITEMS_PER_PAGE_TAREOS", Integer.parseInt(newText)).apply();
                }
                returnDefault = false;
            }
        });

        binding.switchPermitirSinTareo.setChecked(sharedPreferences.getBoolean("PERMITIR_SIN_TAREO", true));
        binding.switchModoPacking.setChecked(sharedPreferences.getBoolean("MODO_PACKING", false));
        binding.txvTareos2.setOnClickListener(view -> {
            stateTareos = !stateTareos;
            checkAllTareos(stateTareos);
        });
        binding.switchLabores.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tablasSeleccionadas.add("mst_labores");
                } else {
                    tablasSeleccionadas.remove("mst_labores");
                }
            }
        });
        binding.switchConsumidores.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tablasSeleccionadas.add("mst_consumidores");
                } else {
                    tablasSeleccionadas.remove("mst_consumidores");
                }
            }
        });
        binding.switchCultivos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tablasSeleccionadas.add("mst_cultivos");
                } else {
                    tablasSeleccionadas.remove("mst_cultivos");
                }
            }
        });
        binding.switchDias.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tablasSeleccionadas.add("mst_dias");
                } else {
                    tablasSeleccionadas.remove("mst_dias");
                }
            }
        });
        binding.switchVariedades.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tablasSeleccionadas.add("mst_variedades");
                } else {
                    tablasSeleccionadas.remove("mst_variedades");
                }
            }
        });
        binding.switchTurnos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tablasSeleccionadas.add("mst_turnos");
                } else {
                    tablasSeleccionadas.remove("mst_turnos");
                }
            }
        });
        binding.switchActividades.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tablasSeleccionadas.add("mst_actividades");
                } else {
                    tablasSeleccionadas.remove("mst_actividades");
                }
            }
        });
        binding.switchPersonas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tablasSeleccionadas.add("mst_personas");
                } else {
                    tablasSeleccionadas.remove("mst_personas");
                }
            }
        });

        binding.btnSincronizarTareos.setOnClickListener(view -> {
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                getActivity().runOnUiThread(() -> {
                    binding.progressBar2.setVisibility(View.VISIBLE);
                });
                SyncDBSQLToSQLite syncDBSQLToSQLite = new SyncDBSQLToSQLite();
                try {
                    for (int i = 0; i < tablasSeleccionadas.size(); i++) {
                        syncDBSQLToSQLite.sincronizarData(this.getContext(), tablasSeleccionadas.get(i));
                    }
                    getActivity().runOnUiThread(() -> {
                        Swal.success(this.getContext(), "Sincronización completada!", "Se realizo la sincronización en las tablas seleccionadas", 8000);
                    });
                } catch (Exception e) {
                    getActivity().runOnUiThread(() -> {
                        Log.e("ERROR", e.toString());
                        Swal.error(this.getContext(), "Oops!", "Hubo un error al sincronizar las tablas: " + e, 8000);
                    });
                } finally {
                    getActivity().runOnUiThread(() -> {
                        binding.progressBar2.setVisibility(View.GONE);
                    });
                }
            });
        });


        binding.switchPermitirSinTareo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean("PERMITIR_SIN_TAREO", true).apply();
            }
        });

        binding.switchModoPacking.setOnClickListener(view -> {
            manualPush = true;
            boolean b = sharedPreferences.getBoolean("MODO_PACKING", false);
            if (manualPush) {
                binding.switchModoPacking.setChecked(!b);
                editor.putBoolean("MODO_PACKING", !b).apply();
            }
                manualPush = false;
        });

        binding.switchModoPacking.setOnCheckedChangeListener((compoundButton, b) -> {
            manualPush = false;
        });

        binding.fabAbrirDrawer.setOnClickListener(view -> {
            TareosActivity tareosActivity = (TareosActivity) getActivity();
            if (tareosActivity.obtenerDrawer() != null) {
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
                if (b) {
                    tablasSeleccionadas.add("mst_usuarios");
                } else {
                    tablasSeleccionadas.remove("mst_usuarios");
                }
            }
        });

        binding.switchQuerysSqlite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tablasSeleccionadas.add("mst_querysSqlite");
                } else {
                    tablasSeleccionadas.remove("mst_querysSqlite");
                }
            }
        });
        return root;
    }

    private void activarControles(boolean b) {
//        binding.switchModoPacking.setEnabled(b);
//        binding.switchMostrarControlesHoras.setEnabled(b);
//        binding.switchMostrarControlesRdtos.setEnabled(b);
//        binding.switchAgregarSoloCamara.setEnabled(b);
//        binding.switchReproducirSonido.setEnabled(b);
//        binding.etItemsPerPage.setEnabled(b);
//        binding.switchValidarTrabajadorRepetido.setEnabled(b);
//        binding.switchMostrarSwitchEntradaSalida.setEnabled(b);
//        binding.switchActivarPermiso.setEnabled(b);
//        binding.switchAutoSalida.setEnabled(b);
        if(b){
            binding.fabLockOptions.setImageDrawable(getResources().getDrawable(R.drawable.ic_unlock));
        }else{
            binding.fabLockOptions.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void checkAllTareos(Boolean state) {
        for (int i = 0; i < binding.chipGroupTareos.getChildCount(); i++) {
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