package com.example.datagreenmovil.ui.TareosMain;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.PopUpCalendario;
import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.TareosActivity;
import com.example.datagreenmovil.cls_05000100_Item_RecyclerView;
import com.example.datagreenmovil.cls_05010000_Edicion;
import com.example.datagreenmovil.databinding.FragmentTareosMainBinding;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TareosMainFragment extends Fragment {

    private FragmentTareosMainBinding binding;
    Context ctx;
    private ConfiguracionLocal objConfLocal;
    private ConexionSqlite objSqlite;
    private ConexionBD objSql;
    String s_DesdeFecha = LocalDate.now().plusDays(-7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    String s_HastaFecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //--desde, hasta, estado;
    String s_ListarDesde = LocalDate.now().plusDays(-7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    String s_ListarHasta = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //--desde, hasta, estado;
    String s_ListarIdEstado ="PE";
    Cursor c_Registros;
    RecyclerView c005_rcv_Reciclador;
    ArrayList<String> al_RegistrosSeleccionados=new ArrayList<>();
    SharedPreferences sharedPreferences;
    int anio, mes, dia;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ctx = context;
        sharedPreferences = ctx.getSharedPreferences("objConfLocal",Context.MODE_PRIVATE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onResume() {
        super.onResume();
        try {
            objSqlite.ActualizarDataPendiente(objConfLocal);
            Funciones.mostrarEstatusGeneral(ctx,
                    objConfLocal,
                    binding.c005TxvPushTituloVentanaV,
                    binding.c005TxvPushRedV,
                    binding.c005TxvNombreAppV,
                    binding.c005TxvPushVersionAppV,
                    binding.c005TxvPushVersionDataBaseV,
                    binding.c005TxvPushIdentificadorV
            );
            listarRegistros();
        } catch (Exception ex) {
            Funciones.mostrarError(ctx,ex);
        }
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TareosMainViewModel tareosMainViewModel =
                new ViewModelProvider(this).get(TareosMainViewModel.class);

        binding = FragmentTareosMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        objSql = new ConexionBD(ctx);
        objSqlite = new ConexionSqlite(ctx,objConfLocal);

//        setearSelectorFechaDesde();
//        setearSelectorFechaHasta();
        binding.c005TxvDesdeFechaV.setText(Funciones.malograrFecha(s_ListarDesde));
        binding.c005TxvHastaFechaV.setText(Funciones.malograrFecha(s_ListarHasta));
        Funciones.mostrarEstatusGeneral(ctx,
                objConfLocal,
                binding.c005TxvPushTituloVentanaV,
                binding.c005TxvPushRedV,
                binding.c005TxvNombreAppV,
                binding.c005TxvPushVersionAppV,
                binding.c005TxvPushVersionDataBaseV,
                binding.c005TxvPushIdentificadorV
        );
        try {
            listarRegistros();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        binding.c005TxvHastaFechaV.setOnClickListener(view -> {
            PopUpCalendario d = new PopUpCalendario(ctx, binding.c005TxvHastaFechaV);
            d.show();
        });

        binding.c005TxvDesdeFechaV.setOnClickListener(view -> {
            PopUpCalendario d = new PopUpCalendario(ctx, binding.c005TxvDesdeFechaV);
            d.show();
        });
        binding.c005TxvDesdeFechaV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                String s_Valor = "";
//                s_Valor = c022_txv_DesdeFecha.getText().toString();
                s_ListarDesde = Funciones.arreglarFecha(binding.c005TxvDesdeFechaV.getText().toString());
//                s_ListarDesde = Funciones.arreglarFecha(s_Valor);
                try {
                    listarRegistros();
                } catch (Exception ex) {
                    //throw new RuntimeException(e);
                    Funciones.mostrarError(ctx, ex);
                }
            }
        });

        binding.c005TxvHastaFechaV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                s_ListarHasta = Funciones.arreglarFecha(binding.c005TxvHastaFechaV.getText().toString());
                try {
                    listarRegistros();
                } catch (Exception ex) {
                    //throw new RuntimeException(e);
                    Funciones.mostrarError(ctx, ex);
                }
            }
        });

        binding.c005RgrEstadoV.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.c005_rad_Todos_v) {
                    //Toast.makeText(getBaseContext(),"todos",Toast.LENGTH_LONG).show();
                    s_ListarIdEstado = "**";
                } else if (i == R.id.c005_rad_Pendientes_v) {
                    //Toast.makeText(getBaseContext(),"pendientes",Toast.LENGTH_LONG).show();
                    s_ListarIdEstado = "PE";
                }
                try {
                    listarRegistros();
                } catch (Exception ex) {
                    //throw new RuntimeException(e);
                    Funciones.mostrarError(ctx, ex);
                }
            }
        });

        return root;
    }

        public void listarRegistros() throws Exception {
//        try{
//            List<String> p = new ArrayList<String>();
//            p.add(objConfLocal.get("ID_EMPRESA"));
//            p.add(idEstado);
//            p.add(d);
//            p.add(h);
//            c_Registros = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER trx_Tareos X ESTADO Y RANGO FECHA"),p,"READ");
//            cls_05000100_Item_RecyclerView miAdaptador = new cls_05000100_Item_RecyclerView(this, c_Registros,objConfLocal,tareosSeleccionados);
//            c005_rcv_Reciclador.setAdapter(miAdaptador);
//            c005_rcv_Reciclador.setLayoutManager(new LinearLayoutManager(this));
//            //reciclador.setAdapter(miAdaptador);
//        }catch (Exception ex){
//             Funciones.mostrarError(this,ex);
//        }
        /////////////////////////////////////////////////////////
        try {
            //BINGO! METODO PARA LISTAR EN RECYCLERVIEW DESDE CURSOR
            List<String> p = new ArrayList<String>();
            p.add(sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"));
            p.add(s_ListarIdEstado);
            p.add(s_ListarDesde);
            p.add(s_ListarHasta);
            Log.i("PARAMS",p.toString());
            c_Registros = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER trx_Tareos X ESTADO Y RANGO FECHA"), p, "READ");
            if (c_Registros.moveToFirst()){

                cls_05000100_Item_RecyclerView miAdaptador = new cls_05000100_Item_RecyclerView(ctx, c_Registros, objConfLocal, al_RegistrosSeleccionados);
                binding.c005RcvRecicladorV.setAdapter(miAdaptador);
                binding.c005RcvRecicladorV.setLayoutManager(new LinearLayoutManager(ctx));
            }else{
                binding.c005RcvRecicladorV.setAdapter(null);
                binding.c005RcvRecicladorV.setLayoutManager(new LinearLayoutManager(ctx));
            }
            //reciclador.setAdapter(miAdaptador);
        } catch (Exception ex) {
            Funciones.mostrarError(ctx, ex);
        }
    }
}