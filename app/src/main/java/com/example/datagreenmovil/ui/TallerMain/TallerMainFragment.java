package com.example.datagreenmovil.ui.TallerMain;

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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
import com.example.datagreenmovil.DataGreenApp;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.PopUpCalendario;
import com.example.datagreenmovil.Entidades.Rex;
import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.TallerActivity;
import com.example.datagreenmovil.TransportesActivity;
import com.example.datagreenmovil.cls_08000100_RecyclerViewAdapter;
import com.example.datagreenmovil.cls_08010000_Edicion;
import com.example.datagreenmovil.databinding.FragmentTallerMainBinding;
import com.example.datagreenmovil.ui.TallerMain.Maquinaria.CRUD.CrearRegistroOperadoresFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.NavigationMenuItemView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TallerMainFragment extends Fragment {

    //@Jota:2023-05-27 -> INICIO DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    ConexionSqlite objSqlite;
    ConexionBD objSql;
    ConfiguracionLocal objConfLocal;
    //    AlertDialog.Builder builderDialogoCerrarSesion;
    Dialog dlg_PopUp;
    Context ctx;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<String> idsSeleccionados;
    //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
    //...
    Cursor c_Registros;
    Rex objRex = null;
    ArrayList<String> al_RegistrosSeleccionados = new ArrayList<>();
    String s_ListarDesde = LocalDate.now().plusDays(-0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    String s_ListarHasta = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //--desde, hasta, estado;
    String s_ListarIdEstado = "PE";
    String s_IdRex = "";
    NavController navController;
    boolean newThemeValue;

    private FragmentTallerMainBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TallerMainViewModel tallerMainViewModel =
                new ViewModelProvider(this).get(TallerMainViewModel.class);

//        newThemeValue = sharedPreferences.getBoolean("THEME_LIGTH", false);
//
//        if(newThemeValue){
//            getActivity().setTheme(R.style.Theme_DataGreenMovilDark);
//        }else{
//            getActivity().setTheme(R.style.Theme_DataGreenMovilLight);
//        }

        ((DataGreenApp) getActivity().getApplication()).InicializarTema();

        binding = FragmentTallerMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        try {
            if (getActivity().getIntent().getExtras() != null) {
                objConfLocal = (ConfiguracionLocal) getActivity().getIntent().getSerializableExtra("ConfiguracionLocal");
            }
            objSql = new ConexionBD(ctx);
            objSqlite = new ConexionSqlite(ctx, objConfLocal);
//      objConfLocal.set("ULTIMA_ACTIVIDAD", "PlantillaBase");
            objRex = new Rex(objSqlite, "trx_ServiciosTransporte");

            setearControles();
            Funciones.mostrarEstatusGeneral(ctx,
                    objConfLocal,
                    binding.talmaiTxvPushTituloVentanaV,
                    binding.talmaiTxvPushRedV,
                    binding.talmaiTxvNombreAppV,
                    binding.talmaiTxvPushVersionAppV,
                    binding.talmaiTxvPushVersionDataBaseV,
                    binding.talmaiTxvPushIdentificadorV
            );
            //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
            //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
            //...
//            talmai_txv_DesdeFecha.setText(s_ListarDesde);
//            talmai_txv_HastaFecha.setText(s_ListarHasta);
        } catch (Exception ex) {
            Funciones.mostrarError(ctx, ex);
        }

//        listarRegistros();

//        controles comunes

        binding.talmaiTxvPushRedV.setOnClickListener(view -> {
            try {
                objSql.probarConexion(objConfLocal);
                Funciones.mostrarEstatusGeneral(ctx,
                        objConfLocal,
                        binding.talmaiTxvPushTituloVentanaV,
                        binding.talmaiTxvPushRedV,
                        binding.talmaiTxvNombreAppV,
                        binding.talmaiTxvPushVersionAppV,
                        binding.talmaiTxvPushVersionDataBaseV,
                        binding.talmaiTxvPushIdentificadorV);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        binding.talmaiTxvPushTituloVentanaV.setOnClickListener(view -> {
            Funciones.popUpTablasPendientesDeEnviar(ctx);
        });
        binding.talmaiTxtHastaFechaV.setOnClickListener(view -> {
            PopUpCalendario d = new PopUpCalendario(ctx, binding.talmaiTxtHastaFechaV);
            d.show();
        });
        binding.talmaiTxtDesdeFechaV.setOnClickListener(view -> {
            PopUpCalendario d = new PopUpCalendario(ctx, binding.talmaiTxtDesdeFechaV);
            d.show();
        });
        binding.talmaiTxvPushVersionAppV.setOnClickListener(view -> {
            Funciones.popUpStatusVersiones(ctx);
        });
        binding.talmaiTxvPushVersionDataBaseV.setOnClickListener(view -> {
            Funciones.popUpStatusVersiones(ctx);
        });
        binding.talmaiTxvPushIdentificadorV.setOnClickListener(view -> {
            mostrarMenuUsuario(binding.talmaiTxvPushIdentificadorV);
        });
        binding.talmaiFabNuevoRegistroV.setOnClickListener(view -> {
//            Funciones.abrirActividad(ctx, CrearRegistroOperadoresFragment.class, objConfLocal, s_IdRex);
            // Navega utilizando la acción definida en tu gráfico de navegación
            navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_nav_taller_main_to_nav_taller_maquinaria_crear);
        });
        binding.talmaiFabOpcionesV.setOnClickListener(view -> {
            TallerActivity tallerActivity = (TallerActivity) getActivity();
            if(tallerActivity.obtenerDrawer() != null){
                DrawerLayout dl = tallerActivity.obtenerDrawer();
                dl.openDrawer(GravityCompat.START);
                NavigationMenuItemView itemSync = dl.findViewById(R.id.nav_item_transferir);
            }
//            editor.putBoolean("THEME_LIGTH", !newThemeValue).apply();
//            ((DataGreenApp) getActivity().getApplication()).setDarkTheme(getActivity());
//            getActivity().recreate();



        });
        
        return root;
    }


    private void setearControles() {
        //        builderDialogoCerrarSesion= Funciones.setearAlertDialogParaCerrarSesion_(objConfLocal,objSqlite,this);

        //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
        //...

        binding.talmaiTxtDesdeFechaV.setText(Funciones.malograrFecha(s_ListarDesde));
        binding.talmaiTxtHastaFechaV.setText(Funciones.malograrFecha(s_ListarHasta));

        binding.talmaiTxtDesdeFechaV.addTextChangedListener(new TextWatcher() {
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
                s_ListarDesde = Funciones.arreglarFecha(binding.talmaiTxtDesdeFechaV.getText().toString());
//                s_ListarDesde = Funciones.arreglarFecha(s_Valor);
                try {
                    listarRegistros();
                } catch (Exception ex) {
                    //throw new RuntimeException(e);
                    Funciones.mostrarError(ctx, ex);
                }
            }
        });
        binding.talmaiTxtHastaFechaV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                s_ListarHasta = Funciones.arreglarFecha(binding.talmaiTxtHastaFechaV.getText().toString());
                try {
                    listarRegistros();
                } catch (Exception ex) {
                    //throw new RuntimeException(e);
                    Funciones.mostrarError(ctx, ex);
                }
            }
        });
        binding.talmaiRgrEstadoV.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == binding.talmaiRadTodosV.getId()) {
                    //Toast.makeText(getBaseContext(),"todos",Toast.LENGTH_LONG).show();
                    s_ListarIdEstado = "**";
                } else if (i == binding.talmaiRadPendienteV.getId()) {
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
    }

    private void listarRegistros() {
        try {
            //BINGO! METODO PARA LISTAR EN RECYCLERVIEW DESDE CURSOR
            List<String> p = new ArrayList<String>();
            p.add(sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"));
            p.add(s_ListarIdEstado);
            p.add(s_ListarDesde);
            p.add(s_ListarHasta);
            c_Registros = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER trx_ServiciosTransporte X ESTADO Y RANGO FECHA"), p, "READ");
            Log.i("REGISTROS", c_Registros.toString());
            if (c_Registros.moveToFirst()) {
                idsSeleccionados = new ArrayList<String>();
                cls_08000100_RecyclerViewAdapter miAdaptador = new cls_08000100_RecyclerViewAdapter(ctx, c_Registros, objConfLocal, al_RegistrosSeleccionados);
                binding.talmaiRcvRecicladorV.setAdapter(miAdaptador);
                binding.talmaiRcvRecicladorV.setLayoutManager(new LinearLayoutManager(ctx));
                miAdaptador.setOnItemClickListener(new cls_08000100_RecyclerViewAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(TextView txtIdServicio, String accion) {
                        if(accion=="transferir"){
                            Swal.confirm(ctx, "Confirmar","Estás seguro que deseas transferir el registro?")
                                    .setConfirmClickListener(sweetAlertDialog -> {
                                        transferirRegistros(txtIdServicio.getText().toString());
                                        sweetAlertDialog.dismissWithAnimation();
                                    }).setCancelClickListener(sweetAlertDialog -> sweetAlertDialog.dismissWithAnimation());
                        }else if(accion=="eliminar"){
                            Swal.confirm(ctx, "Confirmar","Estás seguro que deseas eliminar el registro?")
                                    .setConfirmClickListener(sweetAlertDialog -> {
                                        if(eliminarRegistros(txtIdServicio.getText().toString())){
                                            Swal.success(ctx, "Correcto!","Se ha eliminado el registro con éxito", 1000);
                                        }
                                        sweetAlertDialog.dismissWithAnimation();
                                    })
                                    .setCancelClickListener(sweetAlertDialog -> sweetAlertDialog.dismissWithAnimation());
                        }
                    }
                });
            } else {
                binding.talmaiRcvRecicladorV.setAdapter(null);
                binding.talmaiRcvRecicladorV.setLayoutManager(new LinearLayoutManager(ctx));
            }
            //reciclador.setAdapter(miAdaptador);
        } catch (Exception ex) {
            Funciones.mostrarError(ctx, ex);
        }
    }

    private boolean transferirRegistros(String idTransferir) {
        try{
            SQLiteDatabase database = SQLiteDatabase.openDatabase(ctx.getDatabasePath("DataGreenMovil.db").toString(), null, SQLiteDatabase.OPEN_READWRITE);

            Cursor resultsUnidad = database.rawQuery("select * from trx_ServiciosTransporte where id='"+ idTransferir +"'", null);
            resultsUnidad.moveToFirst();
            String unidad = "";
            for(int i = 0; i<resultsUnidad.getColumnCount();i++){
                unidad += "'"+resultsUnidad.getString(i)+(i == resultsUnidad.getColumnCount() -1 ? "'" : "', ");
            }
            Cursor results = database.rawQuery("select IdEmpresa, IdServicioTransporte, NroDocumento, Item, FechaHora from trx_ServiciosTransporte_Detalle where idserviciotransporte='"+ idTransferir +"'",null);
            JSONArray pasajeros = new JSONArray();
            String servicioTransporte = idTransferir;
            while(results.moveToNext()){
                servicioTransporte = results.getString(1);
                JSONObject elemento = new JSONObject();
                elemento.put("IdServicioTransporte",servicioTransporte);
                elemento.put(results.getColumnName(0), results.getString(0));
                elemento.put(results.getColumnName(1), results.getString(1));
                elemento.put(results.getColumnName(2), results.getString(2));
                elemento.put(results.getColumnName(3), results.getString(3));
                elemento.put(results.getColumnName(4), results.getString(4));
                pasajeros.put(elemento);
            }
            RequestQueue requestQueue = Volley.newRequestQueue(ctx);
//      URL DE LA API EN LARAVEL
            SharedPreferences sharedPreferences = ctx.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
            String ServerIP = sharedPreferences.getString("API_SERVER", "");
            String url = "http://"+ServerIP+"/api/get-users";

            JSONObject params = new JSONObject();
            try {
                params.put("unidad", unidad);
                params.put("pasajeros", pasajeros);
                params.put("idServicioTransporte",servicioTransporte);
                params.put("idDispositivo",sharedPreferences.getString("ID_DISPOSITIVO","!ID_DISPOSITIVO"));
                params.put("idEmpresa", sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"));
                // Agrega otros campos según las expectativas del servidor
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("params", params.toString());

            String idServicioTransporte = servicioTransporte;
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int code = response.getInt("code");
                                List<String> pSqlite = new ArrayList<String>();

                                if(code == 200){
                                    pSqlite.clear();
                                    pSqlite.add(sharedPreferences.getString("ID_USUARIO_ACTUAL","!ID_USUARIO_ACTUAL"));
                                    pSqlite.add(sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"));
                                    pSqlite.add(idServicioTransporte);
                                    objSqlite.doItBaby(objSqlite.obtQuery("MARCAR trx_ServiciosTransporte COMO TRANSFERIDO"), pSqlite, "WRITE", "");
                                    listarRegistros();
                                    Swal.success(ctx, "Correcto!","El registro se ha guardado correctamente!",3000);
                                    objSqlite.ActualizarCorrelativos(objConfLocal, "trx_ServiciosTransporte", idServicioTransporte);
                                } else if (code == 500) {
//                      Log.i("FINOS",response.toString());
                                    String nuevoId = response.getString("newId");
                                    database.execSQL("PRAGMA FOREIGN_KEYS=1;");
                                    String query = "UPDATE trx_serviciostransporte SET ID='"+ nuevoId +"' where ID = '"+idServicioTransporte+"'";
                                    database.execSQL(query);
                                    Swal.warning(ctx, "Importante!","No se ha transferido el registro pero se ha actualizado el identificador, por favor, vuelva a transferirlo",6000);
                                    listarRegistros();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                String errorMessage = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                                Swal.error(ctx,"Ha ocurrido un error al insertar el registro",errorMessage,15000);
                            } else {
                                Log.i("ERROR API", error.toString());
                                Swal.error(ctx,"Oops!","Ha ocurrido un error al insertar el registro",15000);
                            }
                        }
                    });
// Agregar la solicitud a la cola de solicitudes
            requestQueue.add(stringRequest);
        }catch (Exception e){
            Swal.error(ctx, "Error al transferir",e.toString(),15000);
        }
        return true;
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

    private boolean eliminarRegistros(String idEliminar) {
        try {
            List<String> pSqlite = new ArrayList<String>();
            pSqlite.add(sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"));
            pSqlite.add(idEliminar);
            //objRex = objSqlite.CursorARex(objSqlite.doItBaby(objSqlite.obtQuery("OBTENER REGISTRO trx_ServiciosTransporte"),pSqlite,"READ"));
            objRex = objSqlite.CursorARex(objSqlite.doItBaby(objSqlite.obtQuery("OBTENER trx_ServiciosTransporte"), pSqlite, "READ"));
            if (objRex.Existe("IdEstado") && !objRex.Get("IdEstado").equals("TR")) {
                pSqlite.clear();
                pSqlite.add(sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"));
                pSqlite.add(idEliminar);
                objSqlite.doItBaby(objSqlite.obtQuery("ELIMINAR trx_ServiciosTransporte_Detalle PENDIENTES X ID"), pSqlite, "WRITE");
                objSqlite.doItBaby(objSqlite.obtQuery("ELIMINAR trx_ServiciosTransporte PENDIENTES X ID"), pSqlite, "WRITE");
            } else {
//          Swal.info(ctx, "Exito!","El registro se ha eliminado con éxito",1000);
                Funciones.notificar(ctx, "El registro [" + idEliminar + "] se encuentra transferido, imposible eliminar.");
                return false;
            }

            objSqlite.ActualizarDataPendiente(objConfLocal);
            Funciones.mostrarEstatusGeneral(ctx,
                    objConfLocal,
                    binding.talmaiTxvPushTituloVentanaV,
                    binding.talmaiTxvPushRedV,
                    binding.talmaiTxvNombreAppV,
                    binding.talmaiTxvPushVersionAppV,
                    binding.talmaiTxvPushVersionDataBaseV,
                    binding.talmaiTxvPushIdentificadorV
            );
            listarRegistros();
            return true;
        } catch (Exception ex) {
//             Funciones.mostrarError(this,ex);
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        listarRegistros();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ctx = context;
        sharedPreferences = ctx.getSharedPreferences("objConfLocal", ctx.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}