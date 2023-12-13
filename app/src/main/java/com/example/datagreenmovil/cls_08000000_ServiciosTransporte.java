package com.example.datagreenmovil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.PopUpCalendario;
import com.example.datagreenmovil.Entidades.Rex;
import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.Logica.Swal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class cls_08000000_ServiciosTransporte extends AppCompatActivity {
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
  FloatingActionButton c022_fab_Opciones, c022_fab_NuevoRegistro;

  Cursor c_Registros;

  Rex objRex = null;
  ArrayList<String> al_RegistrosSeleccionados = new ArrayList<>();
  String s_ListarDesde = LocalDate.now().plusDays(-0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
  String s_ListarHasta = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //--desde, hasta, estado;
  String s_ListarIdEstado = "PE";
  String s_IdRex = "";


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.v_08000000_servicios_transporte_022);
    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    //@Jota:2023-05-27 -> INICIO DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    ctx = this;
    idsSeleccionados = new ArrayList<>();
    sharedPreferences = this.getSharedPreferences("objConfLocal", MODE_PRIVATE);
    try {
      if (getIntent().getExtras() != null) {
        objConfLocal = (ConfiguracionLocal) getIntent().getSerializableExtra("ConfiguracionLocal");
      }
      objSql = new ConexionBD(this);
      objSqlite = new ConexionSqlite(this, objConfLocal);
//      objConfLocal.set("ULTIMA_ACTIVIDAD", "PlantillaBase");
      objRex = new Rex(objSqlite, "trx_ServiciosTransporte");
      referenciarControles();

      setearControles();
      Funciones.mostrarEstatusGeneral(this.getBaseContext(),
          objConfLocal,
          txv_PushTituloVentana,
          txv_PushRed,
          txv_NombreApp,
          txv_PushVersionApp,
          txv_PushVersionDataBase,
          txv_PushIdentificador
      );
      //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
      //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
      //...
//            c022_txv_DesdeFecha.setText(s_ListarDesde);
//            c022_txv_HastaFecha.setText(s_ListarHasta);
    } catch (Exception ex) {
      Funciones.mostrarError(this, ex);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    try {
      listarRegistros();
    } catch (Exception ex) {
      //throw new RuntimeException(e);
      Funciones.mostrarError(getBaseContext(), ex);
    }
//        Toast.makeText(this, "onResume()", Toast.LENGTH_LONG).show();
  }

  //@Jota:2023-05-27 -> LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
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
          Funciones.mostrarError(getBaseContext(), ex);
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
          Funciones.mostrarError(getBaseContext(), ex);
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
          Funciones.mostrarError(getBaseContext(), ex);
        }
      }
    });
  }
  private void referenciarControles() {
    txv_PushTituloVentana = findViewById(R.id.c022_txv_PushTituloVentana_v);
    txv_PushRed = findViewById(R.id.c022_txv_PushRed_v);
    txv_NombreApp = findViewById(R.id.c022_txv_NombreApp_v);
    txv_PushVersionApp = findViewById(R.id.c022_txv_PushVersionApp_v);
    txv_PushVersionDataBase = findViewById(R.id.c022_txv_PushVersionDataBase_v);
    txv_PushIdentificador = findViewById(R.id.c022_txv_PushIdentificador_v);

    //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
    //...

    c022_txv_DesdeFecha = findViewById(R.id.c022_txt_DesdeFecha_v);
    c022_txv_HastaFecha = findViewById(R.id.c022_txt_HastaFecha_v);
    c022_rad_Todos = findViewById(R.id.c022_rad_Todos_v);
    c022_rad_Pendiente = findViewById(R.id.c022_rad_Pendiente_v);
    c022_rgr_Estado = findViewById(R.id.c022_rgr_Estado_v);
    c022_rcv_Reciclador = findViewById(R.id.c022_rcv_Reciclador_v);
    c022_fab_Opciones = findViewById(R.id.c022_fab_Opciones_v);
    c022_fab_NuevoRegistro = findViewById(R.id.c022_fab_NuevoRegistro_v);
  }

  public void mostrarMenuUsuario(View v) {
    PopupMenu popup = new PopupMenu(this, v);
    popup.setOnMenuItemClickListener(this::onMenuItemClick);
    popup.inflate(R.menu.mnu_00000001_menu_usuario);
    popup.show();
  }

  public boolean onMenuItemClick(MenuItem item) {
    try {
      int idControlClickeado = item.getItemId();
      if (idControlClickeado == R.id.opc_00000001_cambiar_clave_usuario_v) {
        dlg_PopUp = Funciones.obtenerDialogParaCambiarClave(this, objConfLocal, objSqlite, this);
        dlg_PopUp.show();
      } else if (idControlClickeado == R.id.opc_00000001_cerrar_sesion_v) {
        dlg_PopUp = Funciones.obtenerDialogParaCerrarSesion(this, objConfLocal, objSqlite, this);
        dlg_PopUp.show();
      } else return false;
    } catch (Exception ex) {
      Funciones.mostrarError(this, ex);
      return false;
    }
    return true;
  }

  public void mostrarMenuOpciones(View v) {
    PopupMenu popup = new PopupMenu(this, v);
    popup.setOnMenuItemClickListener(this::onMenuItemClickPrincipal);
    popup.inflate(R.menu.mnu_05000000_menu_principal);
    popup.show();
  }

  //@Override
  public boolean onMenuItemClickPrincipal(MenuItem item) {
    try {
      int idControlClickeado = item.getItemId();
      if (idControlClickeado == R.id.opc_05000000_transferir) {
//        if (transferirRegistros()) {
////          al_RegistrosSeleccionados.clear();
////          listarRegistros();
////          Funciones.notificar(this, "Proceso finalizado correctamente.");
////          return true;
//        }
//        Funciones.notificar(this, objConfLocal.get("MENSAJE"));
        return false;
      } else if (idControlClickeado == R.id.opc_05000000_extornar) {
        if (extornarRegistros()) {
          Funciones.notificar(this, "Registros extronados correctamente.");
        }
        return true;
      } else if (idControlClickeado == R.id.opc_05000000_eliminar) {
//        if (eliminarRegistros()) {
//          listarRegistros();
//          Funciones.notificar(this, "Registros eliminados correctamente.");
//        }
        return true;
      } else if (idControlClickeado == R.id.opc_05000000_reporte) {
        abrirActividadReportes();
        finish();
        return true;
      }
//      else if (idControlClickeado == R.id.opc_05000000_configuraciones) {
//        Intent settingsTransporte = new Intent(this, SettingsTransporte.class);
//        startActivity(settingsTransporte);
//        return true;
//      }
    } catch (Exception ex) {
      Funciones.mostrarError(this, ex);
      return false;
    }
    return true;
  }

  public void openAgregarDNI(View view) {
//    Funciones.abrirActividad(this, cls_08020000_AgregarDni.class, objConfLocal, objRex.Get("Id"));


//    Intent nuevaActividad = new Intent(this, cls_08020000_AgregarDni.class);
//    nuevaActividad.putExtra("ConfiguracionLocal", objConfLocal);
//    nuevaActividad.putExtra("IdRegistro", objRex.Get("Id"));
//    startActivity(nuevaActividad);


//    servicioTransporte.moveToPosition(holder.getAdapterPosition());
//          if (servicioTransporte.getString(servicioTransporte.getColumnIndex("Id"))) {
//    Intent nuevaActividad = new Intent(contextoLocal, cls_08020000_AgregarDni.class);
//    nuevaActividad.putExtra("ConfiguracionLocal", objConfLocal);

//    contextoLocal.startActivity(nuevaActividad);
//          } else
//            Funciones.notificar(this, "Antes de agregar Dni debe de guardar el registro actual.");
  }

  public void onClick(View view) {
    try {
      int idControlClickeado = view.getId();
      if (idControlClickeado == R.id.c022_txv_PushTituloVentana_v) {
        Funciones.popUpTablasPendientesDeEnviar(this);
      } else if (idControlClickeado == R.id.c022_txv_PushRed_v) {
        objSql.probarConexion(objConfLocal);
        Funciones.mostrarEstatusGeneral(this.getBaseContext(),
            objConfLocal,
            txv_PushTituloVentana,
            txv_PushRed,
            txv_NombreApp,
            txv_PushVersionApp,
            txv_PushVersionDataBase,
            txv_PushIdentificador
        );
      } else if (idControlClickeado == R.id.c022_txv_PushVersionApp_v || idControlClickeado == R.id.c022_txv_PushVersionDataBase_v) {
        Funciones.popUpStatusVersiones(this);
      } else if (idControlClickeado == R.id.c022_txv_PushIdentificador_v) {
        mostrarMenuUsuario(this.txv_PushIdentificador);
      }
      //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
      //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
      //...
//            else if (idControlClickeado == R.id.c022_rad_Todos_v) {
//                s_ListarIdEstado ="";
////                listarRegistros();
//            }else if (idControlClickeado == R.id.c022_rad_Pendiente_v) {
//                throw new IllegalStateException("Click sin programacion: " + view.getId());
      else if (idControlClickeado == R.id.c022_txt_DesdeFecha_v) {
        PopUpCalendario d = new PopUpCalendario(this, c022_txv_DesdeFecha);
        d.show();
      } else if (idControlClickeado == R.id.c022_txt_HastaFecha_v) {
        PopUpCalendario d = new PopUpCalendario(this, c022_txv_HastaFecha);
        d.show();
      } else if (idControlClickeado == R.id.c022_fab_Volver_v) {
        finish();
//            } else if (idControlClickeado == R.id.c022_fab_Opciones_v) {
//                throw new IllegalStateException("Click sin programacion: " + view.getId());
      } else if (idControlClickeado == R.id.c022_fab_NuevoRegistro_v) {
        //s_IdRex = null;
        Funciones.abrirActividad(this, cls_08010000_Edicion.class, objConfLocal, s_IdRex);
//        Toast.makeText(this, "Esta accion se movió al boton de la lista.", Toast.LENGTH_LONG).show();
      } else throw new IllegalStateException("Click sin programacion: " + view.getId());
    } catch (Exception ex) {
      Funciones.mostrarError(this, ex);
    }
  }
  //@Jota:


  //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
  //...

  public void listarRegistros() throws Exception {
    try {
      //BINGO! METODO PARA LISTAR EN RECYCLERVIEW DESDE CURSOR
      List<String> p = new ArrayList<String>();
      p.add(sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"));
      p.add(s_ListarIdEstado);
      p.add(s_ListarDesde);
      p.add(s_ListarHasta);
      c_Registros = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER trx_ServiciosTransporte X ESTADO Y RANGO FECHA"), p, "READ");
      if (c_Registros.moveToFirst()) {
        idsSeleccionados = new ArrayList<String>();
        cls_08000100_RecyclerViewAdapter miAdaptador = new cls_08000100_RecyclerViewAdapter(this, c_Registros, objConfLocal, al_RegistrosSeleccionados);
        c022_rcv_Reciclador.setAdapter(miAdaptador);
        c022_rcv_Reciclador.setLayoutManager(new LinearLayoutManager(this));
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

//            if(cbxIdServicio.isChecked()){
//              SweetAlertDialog sd = Swal.confirm(ctx, "Estás seguro?","El registro seleccionado se transferirá.");
//              sd.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sDialog) {
//                          if(transferirRegistros()){
//                            Swal.success(ctx, "Transferido","Transferido con éxito",1000);
//                          }
//                          sDialog.dismissWithAnimation();
//                        }
//                      }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener(){
//                @Override
//                public void onClick(SweetAlertDialog sDialog){
//                  sDialog.dismissWithAnimation();
//                }
//              });

//              Log.i("INFO",String.valueOf(sd));

//            }else {
//              idsSeleccionados.remove(txtIdServicio.getText().toString());
//            }
            // Manejar el evento de clic aquí
            // Puedes acceder al elemento en la posición "position" si es necesario
          }
        });
      } else {
        c022_rcv_Reciclador.setAdapter(null);
        c022_rcv_Reciclador.setLayoutManager(new LinearLayoutManager(this));
      }
      //reciclador.setAdapter(miAdaptador);
    } catch (Exception ex) {
      Funciones.mostrarError(this, ex);
    }
  }
  private void abrirActividadReportes() {
//        Intent nuevaActividad = new Intent(this, cls_05020000_Reportes.class);
//        nuevaActividad.putExtra("ConfiguracionLocal",objConfLocal);
//        startActivity(nuevaActividad);
    Funciones.notificar(this, "NO SE HA CREADO UNA ACTIVIDAD DE REPORTES.");
  }
  private boolean extornarRegistros() {
    try {
      throw new Exception();
    } catch (Exception ex) {
      Funciones.mostrarError(this, ex);
      return false;
    }
  }
  private boolean eliminarRegistros(String idEliminar) {
    try {
      List<String> pSqlite = new ArrayList<String>();
        Log.i("IDELIMINADO",idEliminar);
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
          Funciones.notificar(this, "El registro [" + idEliminar + "] se encuentra transferido, imposible eliminar.");
          return false;
        }

      objSqlite.ActualizarDataPendiente(objConfLocal);
      Funciones.mostrarEstatusGeneral(this.getBaseContext(),
          objConfLocal,
          txv_PushTituloVentana,
          txv_PushRed,
          txv_NombreApp,
          txv_PushVersionApp,
          txv_PushVersionDataBase,
          txv_PushIdentificador
      );
      listarRegistros();
      return true;
    } catch (Exception ex) {
//             Funciones.mostrarError(this,ex);
      return false;
    }
  }
  private boolean transferirRegistros(String idTransferir) {
    try{
      SQLiteDatabase database = SQLiteDatabase.openDatabase(this.getDatabasePath("DataGreenMovil.db").toString(), null, SQLiteDatabase.OPEN_READWRITE);

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
      RequestQueue requestQueue = Volley.newRequestQueue(this);
//      URL DE LA API EN LARAVEL
      String url = "http://192.168.30.99:8000/api/insertar_servicios_transporte";

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
                    Log.i("ERROR API 2", error.toString());
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

  private boolean transferirDetalle(String id) {
    try {
      List<String> pSqlite = new ArrayList<String>();
      String pSql = "";
      ResultSet rS;
      pSqlite.add(objConfLocal.get("ID_EMPRESA"));
      pSqlite.add(id);
      Cursor c = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER trx_ServiciosTransporte_Detalle XA TRANSFERIR"), pSqlite, "READ");
      c.moveToFirst();
      for (int i = 0; i < c.getCount(); i++) {
        pSql = obtenerParametrosDeDetalle(c, i);
        rS = objSql.doItBaby(objSqlite.obtQuery("TRANSFERIR trx_ServiciosTransporte_Detalle") + pSql, null);
        rS.next();
        if (!rS.getString("Resultado").equals("1")) {
//          objConfLocal.set("MENSAJE", rS.getString("Detalle"));
          return false;
        }
      }
      return true;
    } catch (Exception ex) {
      Funciones.mostrarError(this, ex);
      return false;
    }
  }

  private String obtenerParametrosDeDetalle(Cursor c, int index) {
    c.moveToPosition(index);
    String r = " '";
    for (int i = 0; i < c.getColumnCount(); i++) {
      r = r + c.getString(i) + "|";
    }
    r = r + "'";
    return r;
  }

  private String obtenerParametrosDeCabecera(String id, String proceso) {
    try {
      String r = " '";
      Cursor c;
      List<String> p = new ArrayList<String>();
      p.add(objConfLocal.get("ID_EMPRESA"));
      p.add(id);
      switch (proceso) {
        case "TRANSFERIR trx_ServiciosTransporte":
          c = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER trx_ServiciosTransporte XA TRANSFERIR"), p, "READ");
          c.moveToFirst();
          for (int i = 0; i < c.getColumnCount(); i++) {
            r = r + c.getString(i) + "|";
          }
          //r = r + "'";
          r = r + objConfLocal.get("MAC") + "|" + objConfLocal.get("IMEI");
          r = r + "'";
          return r;
        case "EXTORNAR":

          return r;
        case "ELIMINAR":

          return r;
        default:
          return null;
      }
    } catch (Exception ex) {
      Funciones.mostrarError(this, ex);
      return null;
    }
  }
}