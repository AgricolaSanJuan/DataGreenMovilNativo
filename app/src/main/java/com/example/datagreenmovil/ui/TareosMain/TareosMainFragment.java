package com.example.datagreenmovil.ui.TareosMain;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.PopUpCalendario;
import com.example.datagreenmovil.Entidades.Tareo;
import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.TareosActivity;
import com.example.datagreenmovil.cls_05000100_Item_RecyclerView;
import com.example.datagreenmovil.cls_05010000_Edicion;
import com.example.datagreenmovil.databinding.FragmentTareosMainBinding;
import com.google.android.material.internal.NavigationMenuItemView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
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
    Dialog dlg_PopUp;
    String s_DesdeFecha = LocalDate.now().plusDays(-7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    String s_HastaFecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //--desde, hasta, estado;
    String s_ListarDesde = LocalDate.now().plusDays(-7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    String s_ListarHasta = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //--desde, hasta, estado;
    String s_ListarIdEstado ="PE";
    Cursor c_Registros;
    RecyclerView c005_rcv_Reciclador;
    ArrayList<String> al_RegistrosSeleccionados=new ArrayList<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<String> idsSeleccionados;
    cls_05000100_Item_RecyclerView miAdaptador;
    int anio, mes, dia;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ctx = context;
        sharedPreferences = ctx.getSharedPreferences("objConfLocal",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
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
        idsSeleccionados = new ArrayList<>();

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
//                s_Valor = talmai_txv_DesdeFecha.getText().toString();
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
        binding.c005FabMainTareosOpcionesV.setOnClickListener(view -> {
            TareosActivity tareosActivity = (TareosActivity) getActivity();
            if(tareosActivity.obtenerDrawer() != null){
                DrawerLayout dl = tareosActivity.obtenerDrawer();
                dl.openDrawer(GravityCompat.START);
                NavigationMenuItemView itemSync = dl.findViewById(R.id.nav_item_sync);
                if(!idsSeleccionados.isEmpty()){
                    itemSync.setEnabled(true);
                    itemSync.setOnClickListener(view1 -> {
                        if(transferirTareos()){
                            idsSeleccionados = null;
                            itemSync.setEnabled(false);
                            dl.closeDrawer(GravityCompat.START);
                            Swal.info(ctx, "Correcto!","Se han transferido los tareos seleccionados",2000);
                        }
                    });
                }else{
                    itemSync.setOnClickListener(view1 -> {
                            Swal.warning(ctx, "Alerta!","No has seleccionado ningún tareo para transferir",1000);
                    });
                    itemSync.setEnabled(false);
                }
            }
        });
        binding.c005FabMainTareosNuevoTareoV.setOnClickListener(view -> {
            abrirDocumento(null);
        });

        binding.c005TxvPushTituloVentanaV.setOnClickListener(view -> {
            Funciones.popUpTablasPendientesDeEnviar(ctx);
        });
        binding.c005TxvHastaFechaV.setOnClickListener(view -> {
            PopUpCalendario d = new PopUpCalendario(ctx, binding.c005TxvHastaFechaV);
            d.show();
        });
        binding.c005TxvDesdeFechaV.setOnClickListener(view -> {
            PopUpCalendario d = new PopUpCalendario(ctx, binding.c005TxvDesdeFechaV);
            d.show();
        });
        binding.c005TxvPushVersionAppV.setOnClickListener(view -> {
            Funciones.popUpStatusVersiones(ctx);
        });
        binding.c005TxvPushVersionDataBaseV.setOnClickListener(view -> {
            Funciones.popUpStatusVersiones(ctx);
        });
        binding.c005TxvPushIdentificadorV.setOnClickListener(view -> {
            mostrarMenuUsuario(binding.c005TxvPushIdentificadorV);
        });

        binding.c005TxvPushRedV.setOnClickListener(view -> {
            try {
                objSql.probarConexion(objConfLocal);
                Funciones.mostrarEstatusGeneral(ctx,
                        objConfLocal,
                        binding.c005TxvPushTituloVentanaV,
                        binding.c005TxvPushRedV,
                        binding.c005TxvNombreAppV,
                        binding.c005TxvPushVersionAppV,
                        binding.c005TxvPushVersionDataBaseV,
                        binding.c005TxvPushIdentificadorV);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return root;
    }
    public void mostrarMenuUsuario(View v) {
        PopupMenu popup = new PopupMenu(ctx, v);
        popup.setOnMenuItemClickListener(this::onMenuItemClick);
        popup.inflate(R.menu.mnu_00000001_menu_usuario);
        popup.show();
    }
    public boolean onMenuItemClick(MenuItem item) {
        try {
            int idControlClickeado = item.getItemId();
            if (idControlClickeado == R.id.opc_00000001_cambiar_clave_usuario_v) {
                dlg_PopUp = Funciones.obtenerDialogParaCambiarClave(ctx, objConfLocal, objSqlite, getActivity());
                dlg_PopUp.show();
            } else if (idControlClickeado == R.id.opc_00000001_cerrar_sesion_v) {
                dlg_PopUp = Funciones.obtenerDialogParaCerrarSesion(ctx, objConfLocal, objSqlite, getActivity());
                dlg_PopUp.show();
            } else return false;
        } catch (Exception ex) {
            Funciones.mostrarError(ctx, ex);
            return false;
        }
        return true;
    }
    private boolean transferirTareos(){
        try{
            List<String> tareos = new ArrayList<String>();
            tareos = miAdaptador.retornarTareos();
            List<String> pSqlite = new ArrayList<String>();
            String pSql = "";
            ResultSet rS;

            for(String id: idsSeleccionados){
                Tareo aux = new Tareo(id,objSqlite,objConfLocal, ctx);
                if (aux.getIdEstado().equals("PE")){
                    if (objSql.existeId("trx_Tareos",sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"), id)){
                        String nuevoId = objSql.obtenerNuevoId("trx_Tareos",sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"), sharedPreferences.getString("ID_DISPOSITIVO","!ID_DISPOSITIVO")  );
                        objSqlite.ActualizarId("trx_Tareos",sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"), id,nuevoId); //PROBAR
                        id=nuevoId;
                        objSqlite.ActualizarCorrelativos(objConfLocal,"trx_Tareos",id); //PROBAR
                    }
                    pSql = obtenerParametrosDeExecPara(id,"TRANSFERIR trx_Tareos");
                    rS = objSql.doItBaby(objSqlite.obtQuery("TRANSFERIR trx_Tareos") + pSql, null);
                    rS.next();
                    if(rS.getString("Resultado").equals("1")){
                        if(transferirTareosDetalle(id)){
                            pSqlite.clear();
                            pSqlite.add(rS.getString("Detalle"));
                            pSqlite.add(sharedPreferences.getString("ID_USUARIO_ACTUAL","!ID_USUARIO_ACTUAL"));
                            pSqlite.add(sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"));
                            pSqlite.add(id);
                            objSqlite.doItBaby(objSqlite.obtQuery("MARCAR TAREO COMO TRANSFERIDO") , pSqlite, "WRITE","");
                            //return true;
                        }
                        else{
                            List<String> p = new ArrayList<>();
                            p.add(id);
                            objSql.doItBaby(objSqlite.obtQuery("ROLLBACK trx_Tareos"), p); //PROBAR
                            return false;
                        }
//                    if (!id.equals(rS.getString("IdTrx")) && rS.getString("IdTrx").length()>0){
//                        objSqlite.ActualizarId("trx_Tareos",objConfLocal.get("ID_EMPRESA"), id,rS.getString("IdTrx")); //PROBAR
//                    }

                    }else{
                        //Toast.makeText(this,rS.getString("Detalle"),Toast.LENGTH_SHORT).show();
                        editor.putString("MENSAJE",rS.getString("Detalle")).apply();
                        return false;
                    }
                }else {
                    Funciones.notificar(ctx,"El tareo ["+id+"] no se puede transferir porque ya fue transferido anteriormente.");
                }
            }
            objConfLocal=objSqlite.ActualizarDataPendiente(objConfLocal,true);
            listarRegistros();


//            Funciones.mostrarEstatusGeneral(ctx,
//                    objConfLocal,
//                    c005_txv_PushTituloVentana,
//                    c005_txv_PushRed,
//                    c005_txv_NombreApp,
//                    c005_txv_PushVersionApp,
//                    c005_txv_PushVersionDataBase,
//                    c005_txv_PushIdentificador);
            return true;
        }catch (Exception ex){
            Swal.error(ctx, "Error al transferir",ex.toString(),6000);
            return false;
        }
    }
    private boolean transferirTareosDetalle(String id) {
        try{
            List<String> pSqlite = new ArrayList<String>();
            String pSql = "";
            ResultSet rS;
            pSqlite.add(sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"));
            pSqlite.add(id);
            Cursor c = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER trx_Tareos_Detalle XA TRANSFERIR"),pSqlite,"READ");
            c.moveToFirst();
            for(int i = 0; i<c.getCount(); i++){
                pSql = concatenarParametros(c,i);
                rS = objSql.doItBaby(objSqlite.obtQuery("TRANSFERIR trx_Tareos_Detalle") + pSql,null);
                rS.next();
                if(!rS.getString("Resultado").equals("1")){
                    editor.putString("MENSAJE",rS.getString("Detalle"));
                    return false;
                }
            }
            return true;
        }catch(Exception ex){
            Funciones.mostrarError(ctx,ex);
            return false;
        }
    }
    private String concatenarParametros(Cursor c, int index) {
        c.moveToPosition(index);
        String r=" '";
        for(int i=0; i<c.getColumnCount(); i++){
            r = r + c.getString(i) + "|";
        }
        r = r + "'";
        return r;
    }
    private String obtenerParametrosDeExecPara(String id, String proceso) {
        try{
            String r=" '";
            Cursor c;
            List<String> p = new ArrayList<String>();
            p.add(sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"));
            p.add(id);
            switch (proceso){
                case "TRANSFERIR trx_Tareos":
                    c = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER trx_Tareos XA TRANSFERIR"),p, "READ");
                    c.moveToFirst();
                    for(int i=0; i<c.getColumnCount(); i++){
                        r = r + c.getString(i) + "|";
                    }
                    //r = r + "'";
                    r = r + sharedPreferences.getString("MAC","!MAC") + "|" + sharedPreferences.getString("IMEI","!IMEI");
                    r = r + "'";
                    return r;
                case "EXTORNAR":

                    return r;
                case "ELIMINAR":

                    return r;
                default: return null;
            }
        }catch(Exception ex){
            Funciones.mostrarError(ctx,ex);
            return null;
        }
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

                miAdaptador = new cls_05000100_Item_RecyclerView(ctx, c_Registros, objConfLocal, al_RegistrosSeleccionados);
                binding.c005RcvRecicladorV.setAdapter(miAdaptador);
                binding.c005RcvRecicladorV.setLayoutManager(new LinearLayoutManager(ctx));
                miAdaptador.setOnItemClickListener(new cls_05000100_Item_RecyclerView.OnItemClickListener() {
                    @Override
                    public void onItemClick(CheckBox cbxSeleccionado, TextView txtId) {
                        if (cbxSeleccionado.isChecked()) {
                            idsSeleccionados.add(txtId.getText().toString());
                        } else {
                            idsSeleccionados.remove(txtId.getText().toString());
                        }
                        Log.i("IDS", idsSeleccionados.toString());
                    }
                });
            }else{
                binding.c005RcvRecicladorV.setAdapter(null);
                binding.c005RcvRecicladorV.setLayoutManager(new LinearLayoutManager(ctx));
            }
            //reciclador.setAdapter(miAdaptador);
        } catch (Exception ex) {
            Funciones.mostrarError(ctx, ex);
        }
    }
    private void abrirDocumento(String id) {
        ConfiguracionLocal objConfLocal = null;
        Intent nuevaActividad = new Intent(ctx, cls_05010000_Edicion.class);
        nuevaActividad.putExtra("ConfiguracionLocal",objConfLocal);
        nuevaActividad.putExtra("IdDocumentoActual",id);
        startActivity(nuevaActividad);
    }
}