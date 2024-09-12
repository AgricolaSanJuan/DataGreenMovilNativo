package com.example.datagreenmovil.ui.estandares.EstandaresSettings;

import android.content.Context;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.datagreenmovil.Conexiones.AppDatabase;
import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.DAO.Estandares.MstTiposEstandar.MstTiposEstandarDAO;
import com.example.datagreenmovil.DAO.Estandares.TrxEstandares.TrxEstandaresNew;
import com.example.datagreenmovil.DAO.Estandares.TrxEstandares.TrxEstandaresNewDAO;
import com.example.datagreenmovil.DataGreenApp;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.databinding.FragmentEstandaresSettingsBinding;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EstandaresSettingsFragment extends Fragment {

    private FragmentEstandaresSettingsBinding binding;
    private Context ctx;
    private TrxEstandaresNewDAO estandaresNewDAO;
    private MstTiposEstandarDAO tiposEstandarDAO;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEstandaresSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inicializarBase();

        binding.btnSincronizarEstandares.setOnClickListener(view -> {


        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void sincronizarEstandares(){
        try {
            ConexionBD sql = new ConexionBD(ctx);
            ResultSet rsEstandares = sql.doItBaby("SELECT x.* FROM DataGreenMovil..trx_estandares_new x", null);
            List<TrxEstandaresNew> estandaresNewList = new ArrayList<>();
            while (rsEstandares.next()){
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
                estandaresNew.setDniUsuarioCrea(rsEstandares.getString("DniUsuarioActualiza"));
                estandaresNew.setFechaHoraActualiza(rsEstandares.getString("FechaHoraActualiza"));

                estandaresNewList.add(estandaresNew);
            }



            estandaresNewDAO.sincronizarEstandares(estandaresNewList);

            Swal.info(ctx, "Bien!", "Se han insertado "+estandaresNewList.size()+" nuevos registros.", 5000);
        } catch (Exception e) {
            Log.e("SQLERROR", e.toString());
        }
    }

    private void inicializarBase(){
        AppDatabase db = DataGreenApp.getAppDatabase();
        estandaresNewDAO = db.estandaresNewDAO();
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        ctx = context;
    }
}