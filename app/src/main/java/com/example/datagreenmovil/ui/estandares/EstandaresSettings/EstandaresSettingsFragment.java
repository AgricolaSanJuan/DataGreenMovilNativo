package com.example.datagreenmovil.ui.estandares.EstandaresSettings;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.datagreenmovil.Conexiones.AppDatabase;
import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.DAO.Estandares.MstMedidasEstandares.MstMedidasEstandares;
import com.example.datagreenmovil.DAO.Estandares.MstMedidasEstandares.MstMedidasEstandaresDAO;
import com.example.datagreenmovil.DAO.Estandares.MstTiposBonoEstandar.MstTiposBonoEstandar;
import com.example.datagreenmovil.DAO.Estandares.MstTiposBonoEstandar.MstTiposBonoEstandarDAO;
import com.example.datagreenmovil.DAO.Estandares.MstTiposCostoEstandar.MstTiposCostoEstandar;
import com.example.datagreenmovil.DAO.Estandares.MstTiposCostoEstandar.MstTiposCostoEstandarDAO;
import com.example.datagreenmovil.DAO.Estandares.MstTiposEstandar.MstTiposEstandar;
import com.example.datagreenmovil.DAO.Estandares.MstTiposEstandar.MstTiposEstandarDAO;
import com.example.datagreenmovil.DAO.Estandares.TrxEstandares.TrxEstandaresNew;
import com.example.datagreenmovil.DAO.Estandares.TrxEstandares.TrxEstandaresNewDAO;
import com.example.datagreenmovil.DataGreenApp;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.databinding.FragmentEstandaresSettingsBinding;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EstandaresSettingsFragment extends Fragment {

    private List<String> tablasSeleccionadas = new ArrayList<>();
    private FragmentEstandaresSettingsBinding binding;
    private Context ctx;
    private TrxEstandaresNewDAO estandaresNewDAO;
    private MstTiposEstandarDAO tiposEstandarDAO;
    private MstMedidasEstandaresDAO medidasEstandaresDao;
    private MstTiposBonoEstandarDAO tiposBonoEstandarDao;
    private MstTiposCostoEstandarDAO tiposCostoEstandarDao;
    private int tablasRestantesSync = 0;
    private boolean stateAllSwitch = false;
    private boolean syncEstandares = false, syncTiposCostoEstandares = false, syncTiposEstandar = false, syncTiposBonoEstandar = false, syncMedidasEstandar = false;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEstandaresSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inicializarBase();

        binding.btnSincronizarEstandares.setOnClickListener(view -> {
            tablasRestantesSync = tablasSeleccionadas.size();
            Log.i("TABLAS ESTANDARES SELECCIONADAS", tablasSeleccionadas.toString());
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                getActivity().runOnUiThread(() -> {
                    binding.pbSync.setVisibility(View.VISIBLE);
                });
                sincronizarEstandares();
                sincronizarTiposEstandar();
                sincronizarTiposBonoEstandar();
                sincronizarTiposCostoEstandar();
                sincronizarMstMedidasEstandares();
                getActivity().runOnUiThread(this::procesarAlerta);
            });
        });

        binding.txvEstandares.setOnClickListener(v -> {
            toggleAllSwitch(!stateAllSwitch);
            stateAllSwitch = !stateAllSwitch;
        });

        binding.switchEstandares.setOnCheckedChangeListener((buttonView, isChecked) -> {
            syncEstandares = isChecked;
            if (isChecked) {
                tablasSeleccionadas.add("trx_estandares_new");
            } else {
                tablasSeleccionadas.remove("trx_estandares_new");
            }
        });

        binding.switchMedidasEstandar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            syncMedidasEstandar = true;
            if (isChecked) {
                tablasSeleccionadas.add("mst_medidas_estandares");
            } else {
                tablasSeleccionadas.remove("mst_medidas_estandares");
            }
        });

        binding.switchTiposBonoEstandar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            syncTiposBonoEstandar = true;
            if (isChecked) {
                tablasSeleccionadas.add("mst_tipos_bono_estandar");
            } else {
                tablasSeleccionadas.remove("mst_tipos_bono_estandar");
            }
        });

        binding.switchTiposEstandar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            syncTiposEstandar = true;
            if (isChecked) {
                tablasSeleccionadas.add("mst_tipos_estandar");
            } else {
                tablasSeleccionadas.remove("mst_tipos_estandar");
            }
        });

        binding.switchTiposCostoEstandar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            syncTiposCostoEstandares = true;
            if (isChecked) {
                tablasSeleccionadas.add("mst_tipos_costo_estandar");
            } else {
                tablasSeleccionadas.remove("mst_tipos_costo_estandar");
            }
        });

        return root;
    }

    private void procesarAlerta() {
        tablasSeleccionadas = new ArrayList<>();
        toggleAllSwitch(false);
        stateAllSwitch = false;
        binding.pbSync.setVisibility(View.INVISIBLE);
        Swal.success(ctx, "Bien!", "Se han sincronizado las tablas seleccionadas", 5000);
    }

    private void toggleAllSwitch(boolean b) {
        for (int i = 0; i < binding.chipGroupEstandares.getChildCount(); i++) {
            View child = binding.chipGroupEstandares.getChildAt(i);
            if (child instanceof Switch) {
                Switch switchControl = (Switch) child;
                switchControl.setChecked(b);
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //    TIPOS ESTANDAR
    public void sincronizarTiposEstandar() {
        if (syncTiposEstandar) {
            try {
                ConexionBD sql = new ConexionBD(ctx);
                ResultSet rsTiposEstandar = sql.doItBaby("SELECT x.* FROM DataGreenMovil..mst_Tipos_Estandar x", null);
                List<MstTiposEstandar> tiposEstandarList = new ArrayList<>();
                while (rsTiposEstandar.next()) {
                    MstTiposEstandar tiposEstandar = new MstTiposEstandar();
                    tiposEstandar.setId(rsTiposEstandar.getInt("Id"));
                    tiposEstandar.setDescripcion(rsTiposEstandar.getString("Descripcion"));
                    tiposEstandar.setNombreCorto(rsTiposEstandar.getString("NombreCorto"));
                    tiposEstandarList.add(tiposEstandar);
                }
                tiposEstandarDAO.sincronizarTiposEstandar(tiposEstandarList);
            } catch (Exception e) {
                Log.e("SQLERROR", e.toString());
            }
        }
    }

    //    TIPOS COSTO ESTANDAR
    public void sincronizarTiposCostoEstandar() {
        if (syncTiposCostoEstandares) {
            try {
                ConexionBD sql = new ConexionBD(ctx);
                ResultSet rsTiposEstandar = sql.doItBaby("SELECT x.* FROM DataGreenMovil..mst_Tipos_Costo_Estandar x", null);
                List<MstTiposCostoEstandar> tiposCostoEstandarList = new ArrayList<>();
                while (rsTiposEstandar.next()) {
                    MstTiposCostoEstandar tiposCostoEstandar = new MstTiposCostoEstandar();
                    tiposCostoEstandar.setId(rsTiposEstandar.getInt("Id"));
                    tiposCostoEstandar.setIdNisira(rsTiposEstandar.getInt("IdNISIRA"));
                    tiposCostoEstandar.setDescripcion(rsTiposEstandar.getString("Descripcion"));
                    tiposCostoEstandar.setNombreCorto(rsTiposEstandar.getString("NombreCorto"));
                    tiposCostoEstandarList.add(tiposCostoEstandar);
                }
                tiposCostoEstandarDao.sincronizarTiposCostoEstandar(tiposCostoEstandarList);
            } catch (Exception e) {
                Log.e("SQLERROR", e.toString());
            }
        }
    }

    //    TIPOS BONO ESTANDAR
    public void sincronizarTiposBonoEstandar() {
        if (syncTiposBonoEstandar) {
            try {
                ConexionBD sql = new ConexionBD(ctx);
                ResultSet rsTiposEstandar = sql.doItBaby("SELECT x.* FROM DataGreenMovil..mst_Tipos_Bono_Estandar x", null);
                List<MstTiposBonoEstandar> tiposBonoEstandarList = new ArrayList<>();
                while (rsTiposEstandar.next()) {
                    MstTiposBonoEstandar tiposBonoEstandar = new MstTiposBonoEstandar();
                    tiposBonoEstandar.setId(rsTiposEstandar.getInt("Id"));
                    tiposBonoEstandar.setIdNisira(rsTiposEstandar.getString("IdNISIRA"));
                    tiposBonoEstandar.setDescripcion(rsTiposEstandar.getString("Descripcion"));
                    tiposBonoEstandar.setNombreCorto(rsTiposEstandar.getString("NombreCorto"));
                    tiposBonoEstandarList.add(tiposBonoEstandar);
                }
                tiposBonoEstandarDao.sincronizarTiposBonoEstandar(tiposBonoEstandarList);
            } catch (Exception e) {
                Log.e("SQLERROR", e.toString());
            }
        }
    }

    //    TIPOS COSTO ESTANDAR
    public void sincronizarMstMedidasEstandares() {
        if (syncMedidasEstandar) {
            try {
                ConexionBD sql = new ConexionBD(ctx);
                ResultSet rsTiposEstandar = sql.doItBaby("SELECT x.* FROM DataGreenMovil..mst_Medidas_Estandares x", null);
                List<MstMedidasEstandares> medidasEstandaresList = new ArrayList<>();
                while (rsTiposEstandar.next()) {
                    MstMedidasEstandares medidasEstandar = new MstMedidasEstandares();
                    medidasEstandar.setId(rsTiposEstandar.getInt("Id"));
                    medidasEstandar.setDescripcion(rsTiposEstandar.getString("Descripcion"));
                    medidasEstandar.setNombreNISIRA(rsTiposEstandar.getString("NombreNISIRA"));
                    medidasEstandaresList.add(medidasEstandar);
                }
                medidasEstandaresDao.sincronizarMedidas(medidasEstandaresList);
            } catch (Exception e) {
                Log.e("SQLERROR", e.toString());
            }
        }
    }

    public void sincronizarEstandares() {
        if (syncEstandares) {
            try {
                ConexionBD sql = new ConexionBD(ctx);
                ResultSet rsEstandares = sql.doItBaby("SELECT x.* FROM DataGreenMovil..trx_estandares_new x", null);
                List<TrxEstandaresNew> estandaresNewList = new ArrayList<>();
                while (rsEstandares.next()) {
                    //                    DEFINIMOS UN NUEVO MODELO DE ESTANDAR
                    TrxEstandaresNew estandaresNew = new TrxEstandaresNew();

                    estandaresNew.setId(rsEstandares.getInt("ID"));
                    estandaresNew.setIdEmpresa(rsEstandares.getString("idempresa"));
                    estandaresNew.setIdTipoEstandar(rsEstandares.getString("IdTipoEstandar"));
                    estandaresNew.setIdActividad(rsEstandares.getString("idactividad"));
                    estandaresNew.setIdLabor(rsEstandares.getString("idlabor"));
                    estandaresNew.setPeriodo(rsEstandares.getString("periodo"));
                    estandaresNew.setFechaInicio(rsEstandares.getString("fecha_inicio"));
                    estandaresNew.setFechaFinal(rsEstandares.getString("fecha_final"));
                    estandaresNew.setIdMedidaEstandar(rsEstandares.getInt("IdMedidaEstandar"));
                    estandaresNew.setCantidad(rsEstandares.getDouble("cantidad"));
                    estandaresNew.setPrecio(rsEstandares.getDouble("precio"));
                    estandaresNew.setBase(rsEstandares.getDouble("base"));
                    estandaresNew.setPrecioBase(rsEstandares.getDouble("precio_base"));
                    estandaresNew.setIdTipoBonoEstandar(rsEstandares.getInt("IdTipoBonoEstandar"));
                    estandaresNew.setValMinExcedente(rsEstandares.getDouble("valmin_excedente"));
                    estandaresNew.setHoras(rsEstandares.getDouble("HORAS"));
                    estandaresNew.setIdTipoCostoEstandar(rsEstandares.getInt("IdTipoCostoEstandar"));
                    estandaresNew.setIdConsumidor(rsEstandares.getString("IDCONSUMIDOR"));
                    estandaresNew.setPorcentajeValidExcedente(rsEstandares.getDouble("porcentajevalid_excedente"));
                    estandaresNew.setFactorPrecio(rsEstandares.getDouble("factor_precio"));
                    estandaresNew.setDniUsuarioCrea(rsEstandares.getString("DniUsuarioCrea"));
                    estandaresNew.setFechaHoraCrea(rsEstandares.getString("FechaHoraCrea"));
                    estandaresNew.setDniUsuarioActualiza(rsEstandares.getString("DniUsuarioActualiza"));
                    estandaresNew.setFechaHoraActualiza(rsEstandares.getString("FechaHoraActualiza"));

                    estandaresNewList.add(estandaresNew);
                }

                estandaresNewDAO.sincronizarEstandares(estandaresNewList);
//                Swal.info(ctx, "Bien!", "Se han insertado " + estandaresNewList.size() + " nuevos registros.", 5000);
            } catch (Exception e) {
                Log.e("SQLERROR", e.toString());
            }
        }
    }

    private void inicializarBase() {
        AppDatabase db = DataGreenApp.getAppDatabase();
        estandaresNewDAO = db.estandaresNewDAO();
        tiposEstandarDAO = db.mstTiposEstandarDAO();
        medidasEstandaresDao = db.mstMedidasEstandaresDAO();
        tiposBonoEstandarDao = db.mstTiposBonoEstandarDAO();
        tiposCostoEstandarDao = db.mstTiposCostoEstandarDAO();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ctx = context;
    }
}