package com.example.datagreenmovil.ui.TareosMain;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.TareosActivity;
import com.example.datagreenmovil.cls_05010000_Edicion;
import com.example.datagreenmovil.databinding.FragmentTareosMainBinding;

public class TareosMainFragment extends Fragment {

    private FragmentTareosMainBinding binding;
    Context ctx;
    private ConfiguracionLocal objConfLocal;
    private ConexionSqlite objSqlite;
    private ConexionBD objSql;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ctx = context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TareosMainViewModel tareosMainViewModel =
                new ViewModelProvider(this).get(TareosMainViewModel.class);

        binding = FragmentTareosMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        objSql = new ConexionBD(ctx);
        objSqlite = new ConexionSqlite(ctx,objConfLocal);

        Funciones.mostrarEstatusGeneral(ctx,
                objConfLocal,
                binding.c005TxvPushTituloVentanaV,
                binding.c005TxvPushRedV,
                binding.c005TxvNombreAppV,
                binding.c005TxvPushVersionAppV,
                binding.c005TxvPushVersionDataBaseV,
                binding.c005TxvPushIdentificadorV
        );

//        binding.c005FabMainTareosNuevoTareoV.setOnClickListener(view -> {
//            abrirDocumento(null);
//        });


        return root;
    }

    private void abrirDocumento(String id) {
        Intent nuevaActividad = new Intent(ctx, cls_05010000_Edicion.class);
        nuevaActividad.putExtra("ConfiguracionLocal",objConfLocal);
        nuevaActividad.putExtra("IdDocumentoActual",id);
        startActivity(nuevaActividad);
//        Swal.info(ctx, "FINO","asd",15000);
    }

}