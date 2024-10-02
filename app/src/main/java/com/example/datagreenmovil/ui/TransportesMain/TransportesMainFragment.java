package com.example.datagreenmovil.ui.TransportesMain;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RadioButton;
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
import com.example.datagreenmovil.TransportesActivity;
import com.example.datagreenmovil.cls_08000100_RecyclerViewAdapter;
import com.example.datagreenmovil.cls_08010000_Edicion;
import com.example.datagreenmovil.databinding.FragmentTransportesMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TransportesMainFragment extends Fragment {

    //@Jota:2023-05-27 -> INICIO DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    ConexionSqlite objSqlite;
    ConexionBD objSql;
    ConfiguracionLocal objConfLocal;
    TextView txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador;
    //    AlertDialog.Builder builderDialogoCerrarSesion;
    Dialog dlg_PopUp;
    Context ctx;
    SharedPreferences sharedPreferences;
    ArrayList<String> idsSeleccionados;
    //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
    //...

    TextView c022_txv_DesdeFecha, c022_txv_HastaFecha;
    RadioButton c022_rad_Todos, c022_rad_Pendiente;
    RadioGroup c022_rgr_Estado;
    RecyclerView c022_rcv_Reciclador;
    FloatingActionButton c022_fab_NuevoRegistro;

    Cursor c_Registros;

    Rex objRex = null;
    ArrayList<String> al_RegistrosSeleccionados = new ArrayList<>();
    String s_ListarDesde = LocalDate.now().plusDays(-0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    String s_ListarHasta = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //--desde, hasta, estado;
    String s_ListarIdEstado = "PE";
    String s_IdRex = "";
    private FragmentTransportesMainBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TransportesMainViewModel transportesMainViewModel = new ViewModelProvider(this).get(TransportesMainViewModel.class);

        binding = FragmentTransportesMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        try {
            if (getActivity().getIntent().getExtras() != null) {
                objConfLocal = (ConfiguracionLocal) getActivity().getIntent().getSerializableExtra("ConfiguracionLocal");
            }
            objSql = new ConexionBD(ctx);
            objSqlite = new ConexionSqlite(ctx, DataGreenApp.DB_VERSION());
//      objConfLocal.set("ULTIMA_ACTIVIDAD", "PlantillaBase");
            objRex = new Rex(objSqlite, "trx_ServiciosTransporte");
            referenciarControles();

            setearControles();
            Funciones.mostrarEstatusGeneral(ctx, objConfLocal, txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador);
            //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
            //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
            //...
//            c022_txv_DesdeFecha.setText(s_ListarDesde);
//            c022_txv_HastaFecha.setText(s_ListarHasta);
        } catch (Exception ex) {
            Funciones.mostrarError(ctx, ex);
        }

        listarRegistros();

        binding.c022TxvPushRedV.setOnClickListener(view -> {
            try {
                objSql.probarConexion(objConfLocal);
                Funciones.mostrarEstatusGeneral(ctx, objConfLocal, binding.c022TxvPushTituloVentanaV, binding.c022TxvPushRedV, binding.c022TxvNombreAppV, binding.c022TxvPushVersionAppV, binding.c022TxvPushVersionDataBaseV, binding.c022TxvPushIdentificadorV);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        binding.c022TxvPushTituloVentanaV.setOnClickListener(view -> {
            Funciones.popUpTablasPendientesDeEnviar(ctx);
        });
        binding.c022TxtHastaFechaV.setOnClickListener(view -> {
            PopUpCalendario d = new PopUpCalendario(ctx, binding.c022TxtHastaFechaV);
            d.show();
        });
        binding.c022TxtDesdeFechaV.setOnClickListener(view -> {
            PopUpCalendario d = new PopUpCalendario(ctx, binding.c022TxtDesdeFechaV);
            d.show();
        });
        binding.c022TxvPushVersionAppV.setOnClickListener(view -> {
            Funciones.popUpStatusVersiones(ctx);
        });
        binding.c022TxvPushVersionDataBaseV.setOnClickListener(view -> {
            Funciones.popUpStatusVersiones(ctx);
        });
        binding.c022TxvPushIdentificadorV.setOnClickListener(view -> {
            mostrarMenuUsuario(this.txv_PushIdentificador);
        });
        binding.c022FabNuevoRegistroV.setOnClickListener(view -> {
            Funciones.abrirActividad(ctx, cls_08010000_Edicion.class, objConfLocal, s_IdRex);
        });
        binding.c022FabOpcionesV.setOnClickListener(view -> {
            TransportesActivity transportesActivity = (TransportesActivity) getActivity();
            if (transportesActivity.obtenerDrawer() != null) {
                DrawerLayout dl = transportesActivity.obtenerDrawer();
                dl.openDrawer(GravityCompat.START);
            }
        });


        return root;
    }

    private void setearControles() {
        //        builderDialogoCerrarSesion= Funciones.setearAlertDialogParaCerrarSesion_(objConfLocal,objSqlite,this);

        //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
        //...

        c022_txv_DesdeFecha.setText(Funciones.malograrFecha(s_ListarDesde));
        c022_txv_HastaFecha.setText(Funciones.malograrFecha(s_ListarHasta));
        c022_txv_DesdeFecha.addTextChangedListener(new TextWatcher() {
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
                s_ListarDesde = Funciones.arreglarFecha(c022_txv_DesdeFecha.getText().toString());
//                s_ListarDesde = Funciones.arreglarFecha(s_Valor);
                try {
                    listarRegistros();
                } catch (Exception ex) {
                    //throw new RuntimeException(e);
                    Funciones.mostrarError(ctx, ex);
                }
            }
        });
        c022_txv_HastaFecha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                s_ListarHasta = Funciones.arreglarFecha(c022_txv_HastaFecha.getText().toString());
                try {
                    listarRegistros();
                } catch (Exception ex) {
                    //throw new RuntimeException(e);
                    Funciones.mostrarError(ctx, ex);
                }
            }
        });
        c022_rgr_Estado.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.c022_rad_Todos_v) {
                    //Toast.makeText(getBaseContext(),"todos",Toast.LENGTH_LONG).show();
                    s_ListarIdEstado = "**";
                } else if (i == R.id.c022_rad_Pendiente_v) {
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
            p.add(sharedPreferences.getString("ID_EMPRESA", "!ID_EMPRESA"));
            p.add(s_ListarIdEstado);
            p.add(s_ListarDesde);
            p.add(s_ListarHasta);
            c_Registros = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER trx_ServiciosTransporte X ESTADO Y RANGO FECHA"), p, "READ");
            Log.i("REGISTROS", c_Registros.toString());
            if (c_Registros.moveToFirst()) {
                idsSeleccionados = new ArrayList<String>();
                cls_08000100_RecyclerViewAdapter miAdaptador = new cls_08000100_RecyclerViewAdapter(ctx, c_Registros, objConfLocal, al_RegistrosSeleccionados);
                c022_rcv_Reciclador.setAdapter(miAdaptador);
                c022_rcv_Reciclador.setLayoutManager(new LinearLayoutManager(ctx));
                miAdaptador.setOnItemClickListener(new cls_08000100_RecyclerViewAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(TextView txtIdServicio, String accion) {
                        if (accion == "transferir") {
                            Swal.confirm(ctx, "Confirmar", "Estás seguro que deseas transferir el registro?").setConfirmClickListener(sweetAlertDialog -> {
                                transferirRegistros(txtIdServicio.getText().toString());
                                sweetAlertDialog.dismissWithAnimation();
                            }).setCancelClickListener(sweetAlertDialog -> sweetAlertDialog.dismissWithAnimation());
                        } else if (accion == "eliminar") {
                            Swal.confirm(ctx, "Confirmar", "Estás seguro que deseas eliminar el registro?").setConfirmClickListener(sweetAlertDialog -> {
                                if (eliminarRegistros(txtIdServicio.getText().toString())) {
                                    Swal.success(ctx, "Correcto!", "Se ha eliminado el registro con éxito", 1000);
                                }
                                sweetAlertDialog.dismissWithAnimation();
                            }).setCancelClickListener(sweetAlertDialog -> sweetAlertDialog.dismissWithAnimation());
                        }
                    }
                });
            } else {
                c022_rcv_Reciclador.setAdapter(null);
                c022_rcv_Reciclador.setLayoutManager(new LinearLayoutManager(ctx));
            }
            //reciclador.setAdapter(miAdaptador);
        } catch (Exception ex) {
            Funciones.mostrarError(ctx, ex);
        }
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
            pSqlite.add(sharedPreferences.getString("ID_EMPRESA", "!ID_EMPRESA"));
            pSqlite.add(idEliminar);
            //objRex = objSqlite.CursorARex(objSqlite.doItBaby(objSqlite.obtQuery("OBTENER REGISTRO trx_ServiciosTransporte"),pSqlite,"READ"));
            objRex = objSqlite.CursorARex(objSqlite.doItBaby(objSqlite.obtQuery("OBTENER trx_ServiciosTransporte"), pSqlite, "READ"));
            if (objRex.Existe("IdEstado") && !objRex.Get("IdEstado").equals("TR")) {
                pSqlite.clear();
                pSqlite.add(sharedPreferences.getString("ID_EMPRESA", "!ID_EMPRESA"));
                pSqlite.add(idEliminar);
                objSqlite.doItBaby(objSqlite.obtQuery("ELIMINAR trx_ServiciosTransporte_Detalle PENDIENTES X ID"), pSqlite, "WRITE");
                objSqlite.doItBaby(objSqlite.obtQuery("ELIMINAR trx_ServiciosTransporte PENDIENTES X ID"), pSqlite, "WRITE");
            } else {
//          Swal.info(ctx, "Exito!","El registro se ha eliminado con éxito",1000);
                Funciones.notificar(ctx, "El registro [" + idEliminar + "] se encuentra transferido, imposible eliminar.");
                return false;
            }

            objSqlite.ActualizarDataPendiente(objConfLocal);
            Funciones.mostrarEstatusGeneral(ctx, objConfLocal, txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador);
            listarRegistros();
            return true;
        } catch (Exception ex) {
//             Funciones.mostrarError(this,ex);
            return false;
        }
    }

    private boolean transferirRegistros(String idTransferir) {
        try {
            SQLiteDatabase database = SQLiteDatabase.openDatabase(ctx.getDatabasePath("DataGreenMovil.db").toString(), null, SQLiteDatabase.OPEN_READWRITE);

            Cursor resultsUnidad = database.rawQuery("select * from trx_ServiciosTransporte where id='" + idTransferir + "'", null);
            resultsUnidad.moveToFirst();
            String unidad = "";
            for (int i = 0; i < resultsUnidad.getColumnCount(); i++) {
                unidad += "'" + resultsUnidad.getString(i) + (i == resultsUnidad.getColumnCount() - 1 ? "'" : "', ");
            }
            Cursor results = database.rawQuery("select IdEmpresa, IdServicioTransporte, NroDocumento, Item, FechaHora, coordenadas_marca from trx_ServiciosTransporte_Detalle where idserviciotransporte='" + idTransferir + "'", null);
            JSONArray pasajeros = new JSONArray();
            String servicioTransporte = idTransferir;
            while (results.moveToNext()) {
                servicioTransporte = results.getString(1);
                JSONObject elemento = new JSONObject();
                elemento.put("IdServicioTransporte", servicioTransporte);
                elemento.put(results.getColumnName(0), results.getString(0));
                elemento.put(results.getColumnName(1), results.getString(1));
                elemento.put(results.getColumnName(2), results.getString(2));
                elemento.put(results.getColumnName(3), results.getString(3));
                elemento.put(results.getColumnName(4), results.getString(4));
                elemento.put(results.getColumnName(5), results.getString(5));
                pasajeros.put(elemento);
            }
            RequestQueue requestQueue = Volley.newRequestQueue(ctx);
//      URL DE LA API EN LARAVEL
            SharedPreferences sharedPreferences = ctx.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
            String ServerIP = sharedPreferences.getString("API_SERVER", "");
            String url = "http://" + ServerIP + "/api/get-users";
            String mac = sharedPreferences.getString("MAC", "!MAC");
//            if(mac.length() > 12){
//                mac = mac.substring(0, 12);
//            }
            JSONObject params = new JSONObject();
            try {
                params.put("unidad", unidad);
                params.put("pasajeros", pasajeros);
                params.put("idServicioTransporte", servicioTransporte);
                params.put("idDispositivo", sharedPreferences.getString("ID_DISPOSITIVO", "!ID_DISPOSITIVO"));
                params.put("idEmpresa", sharedPreferences.getString("ID_EMPRESA", "!ID_EMPRESA"));
                params.put("userLogin", sharedPreferences.getString("NOMBRE_USUARIO_ACTUAL", "!NOMBRE_USUARIO_ACTUAL"));
                params.put("app", "MiniGreen");
                params.put("mac", mac);
                params.put("usuario", sharedPreferences.getString("ID_USUARIO_ACTUAL", "!ID_USUARIO_ACTUAL"));
//                Log.i("PARAMS", params.toString());
                // Agrega otros campos según las expectativas del servidor
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("params", params.toString());

            String idServicioTransporte = servicioTransporte;
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, params, response -> {
                try {
                    boolean success = response.getBoolean("success");
                    String message = response.getString("message");
//                    int code = response.getInt("code");
                    List<String> pSqlite = new ArrayList<String>();

                    if (success) {
                        // Manejo de la respuesta exitosa
                        pSqlite.clear();
                        pSqlite.add(sharedPreferences.getString("ID_USUARIO_ACTUAL", "!ID_USUARIO_ACTUAL"));
                        pSqlite.add(sharedPreferences.getString("ID_EMPRESA", "!ID_EMPRESA"));
                        pSqlite.add(idServicioTransporte);
                        objSqlite.doItBaby(objSqlite.obtQuery("MARCAR trx_ServiciosTransporte COMO TRANSFERIDO"), pSqlite, "WRITE", "");
                        listarRegistros();
                        Swal.success(ctx, "Correcto!", message, 3000);
                        objSqlite.ActualizarCorrelativos(objConfLocal, "trx_ServiciosTransporte", idServicioTransporte);
                    } else {
                        // Manejo de errores desde la API
                        String errorMessage = response.getString("error");
                        if (response.has("newId")) {
                            String nuevoId = response.getString("newId");
                            String query = "UPDATE trx_serviciostransporte SET ID='" + nuevoId + "' WHERE ID = '" + idServicioTransporte + "'";
                            database.execSQL(query);
                            Swal.warning(ctx, "Importante!", "No se ha transferido el registro pero se ha actualizado el identificador: " + nuevoId + ". Por favor, vuelva a transferirlo.", 6000);
                        } else {
                            Swal.error(ctx, "Error!", errorMessage, 5000);
                        }
                    }

                } catch (JSONException e) {
                    Swal.error(ctx, "Oops!", "Error en la respuesta JSON: " + e.getMessage(), 5000);
                } catch (Exception e) {
//                    throw new RuntimeException(e);
                    Swal.error(ctx, "Oops!", "Error en SQLITE: " + e.getMessage(), 5000);
                }
            },error -> {
                // Manejo de errores de red o de respuesta
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    String errorMessage = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    Swal.error(ctx, "Error de red", errorMessage, 15000);
                } else {
                    // Error de red sin respuesta
                    Log.i("ERROR API", error.toString());
                    Swal.error(ctx, "Oops!", error.toString(), 15000);
                }
            });

// Agregar la solicitud a la cola de solicitudes
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Swal.error(ctx, "Error al transferir", e.toString(), 15000);
        }
        return true;
    }

    private void referenciarControles() {
        //    REFERENCIAR CONTROLES
        txv_PushTituloVentana = binding.c022TxvPushTituloVentanaV;
        txv_PushRed = binding.c022TxvPushRedV;
        txv_NombreApp = binding.c022TxvNombreAppV;
        txv_PushVersionApp = binding.c022TxvPushVersionAppV;
        txv_PushVersionDataBase = binding.c022TxvPushVersionDataBaseV;
        txv_PushIdentificador = binding.c022TxvPushIdentificadorV;

        //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
        //...

        c022_txv_DesdeFecha = binding.c022TxtDesdeFechaV;
        c022_txv_HastaFecha = binding.c022TxtHastaFechaV;
        c022_rad_Todos = binding.c022RadTodosV;
        c022_rad_Pendiente = binding.c022RadPendienteV;
        c022_rgr_Estado = binding.c022RgrEstadoV;
        c022_rcv_Reciclador = binding.c022RcvRecicladorV;
        c022_fab_NuevoRegistro = binding.c022FabNuevoRegistroV;
        idsSeleccionados = new ArrayList<>();
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
        sharedPreferences = ctx.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}