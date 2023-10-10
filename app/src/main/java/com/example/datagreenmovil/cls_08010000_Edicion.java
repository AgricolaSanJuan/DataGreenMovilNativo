package com.example.datagreenmovil;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.PopUpBuscarEnLista;
import com.example.datagreenmovil.Entidades.PopUpBuscarEnLista_Item;
import com.example.datagreenmovil.Entidades.PopUpCalendario;
import com.example.datagreenmovil.Entidades.PopUpObservacion;
import com.example.datagreenmovil.Entidades.Rex;
import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.Logica.Swal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

//@SuppressWarnings("unchecked")
public class cls_08010000_Edicion extends AppCompatActivity {
  //@Jota:2023-05-27 -> INICIO DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
  ConexionSqlite objSqlite;
  ConexionBD objSql;
  ConfiguracionLocal objConfLocal;
  TextView txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador;
  //    AlertDialog.Builder builderDialogoCerrarSesion;
  Dialog dlg_PopUp;

  //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
  //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
  //...


  Rex objRex = null;
  String s_IdRex;
  TextView c024_txv_Id, c024_txt_Fecha, c024_txt_Proveedor_Ruc, c024_txt_Proveedor_RazonSocial,
      c024_txt_Vehiculo_Placa, c024_txt_Vehiculo_NroPasajeros, c024_txt_Ruta_Id, c024_txt_Ruta_Descripcion,
      c024_txt_Conductor_NroBrevete, c024_txt_Conductor_Nombre, c024_txt_Observacion;
  LinearLayout c024_lly_Proveedor, c024_lly_Vehiculo, c024_lly_Ruta, c024_lly_Conductor;
  FloatingActionButton c024_fab_Volver, c024_fab_Guardar, c024_fab_AgregarPersonas;
  ArrayList<PopUpBuscarEnLista_Item> arl_Proveedores, arl_Vehiculos, arl_Rutas, arl_Conductores;

  ListView lvPasajeros;
  boolean registroModificado = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.v_08010000_edicion_024);
    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    //@Jota:2023-05-27 -> INICIO DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    try {
      if (getIntent().getExtras() != null) {
        objConfLocal = (ConfiguracionLocal) getIntent().getSerializableExtra("ConfiguracionLocal");
        //objRex = (Rex) getIntent().getParcelableExtra("objRex");
        s_IdRex = (String) getIntent().getSerializableExtra("s_IdRex");
      }
      objSql = new ConexionBD(this);
      objSqlite = new ConexionSqlite(this, objConfLocal);
//      objConfLocal.set("ULTIMA_ACTIVIDAD", "ServicioTransporteEdicion");
      referenciarControles();
      obtenerDataParaControles();
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

      obtenerRexActual();
      setearControles();
      if (objRex.Get("Id") != null && !objRex.Get("Id").equals("")) {
        mostrarValoresRexActual();
      }
    } catch (Exception ex) {
      Funciones.mostrarError(this, ex);
    }
  }

  private void getPasajeros(){

  }
  //@Jota:2023-05-27 -> LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
  private void setearControles() {
//        builderDialogoCerrarSesion= Funciones.setearAlertDialogParaCerrarSesion_(objConfLocal,objSqlite,this);
    //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
    //...
//        setearSelectorFecha();
    c024_txt_Fecha.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void afterTextChanged(Editable editable) {
        String f;
        try {
          f = Funciones.arreglarFecha(c024_txt_Fecha.getText().toString());
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
        objRex.Set("Fecha", f);
        registroModificado = true;
      }
    });
    c024_txt_Fecha.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))); //--desde, hasta, estado;);
    c024_txt_Proveedor_Ruc.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void afterTextChanged(Editable editable) {
        objRex.Set("IdProveedor", c024_txt_Proveedor_Ruc.getText().toString());
        registroModificado = true;
      }
    });
    c024_txt_Vehiculo_Placa.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void afterTextChanged(Editable editable) {
        objRex.Set("IdVehiculo", c024_txt_Vehiculo_Placa.getText().toString());
        //objRex.Set("Pasajeros",c024_txt_Vehiculo_Placa.getText().toString());
        registroModificado = true;
      }
    });
    c024_txt_Ruta_Id.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void afterTextChanged(Editable editable) {
        objRex.Set("IdRuta", c024_txt_Ruta_Id.getText().toString());
        registroModificado = true;
      }
    });
    c024_txt_Conductor_NroBrevete.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void afterTextChanged(Editable editable) {
        objRex.Set("IdConductor", c024_txt_Conductor_NroBrevete.getText().toString());
        registroModificado = true;
      }
    });
//        c024_txt_Observacion.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//            @Override
//            public void afterTextChanged(Editable editable) {
//                objRex.Set("Observacion",c024_txt_Observacion.getText().toString());
//                registroModificado = true;
//            }
//        });
    c024_txt_Observacion.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void afterTextChanged(Editable editable) {
        String obs = c024_txt_Observacion.getText().toString();
        if (!obs.equals("...")) {
          objRex.Set("Observacion", obs);
          c024_txt_Observacion.setText("...");
          registroModificado = true;
        }
      }
    });
  }

  private void referenciarControles() {
    txv_PushTituloVentana = findViewById(R.id.c024_txv_PushTituloVentana_v);
    txv_PushRed = findViewById(R.id.c024_txv_PushRed_v);
    txv_NombreApp = findViewById(R.id.c024_txv_NombreApp_v);
    txv_PushVersionApp = findViewById(R.id.c024_txv_PushVersionApp_v);
    txv_PushVersionDataBase = findViewById(R.id.c024_txv_PushVersionDataBase_v);
    txv_PushIdentificador = findViewById(R.id.c024_txv_PushIdentificador_v);

    //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
    //...

    c024_txv_Id = findViewById(R.id.c024_txv_Id_v);
    c024_txt_Fecha = findViewById(R.id.c024_txt_Fecha_v);
    c024_txt_Proveedor_Ruc = findViewById(R.id.c024_txt_Proveedor_Ruc_v);
    c024_txt_Proveedor_RazonSocial = findViewById(R.id.c024_txt_Proveedor_RazonSocial_v);
    c024_txt_Vehiculo_Placa = findViewById(R.id.c024_txt_Vehiculo_Placa_v);
    c024_txt_Vehiculo_NroPasajeros = findViewById(R.id.c024_txt_Vehiculo_NroPasajeros_v);
    c024_txt_Ruta_Id = findViewById(R.id.c024_txt_Ruta_Id_v);
    c024_txt_Ruta_Descripcion = findViewById(R.id.c024_txt_Ruta_Descripcion_v);
    c024_txt_Conductor_NroBrevete = findViewById(R.id.c024_txt_Conductor_NroBrevete_v);
    c024_txt_Conductor_Nombre = findViewById(R.id.c024_txt_Conductor_Nombre_v);
    c024_txt_Observacion = findViewById(R.id.c024_txt_Observacion_v);

    c024_lly_Proveedor = findViewById(R.id.c024_lly_Proveedor_v);
    c024_lly_Vehiculo = findViewById(R.id.c024_lly_Vehiculo_v);
    c024_lly_Ruta = findViewById(R.id.c024_lly_Ruta_v);
    c024_lly_Conductor = findViewById(R.id.c024_lly_Conductor_v);

    c024_fab_Volver = findViewById(R.id.c024_fab_Volver_v);
    c024_fab_Guardar = findViewById(R.id.c024_fab_Guardar_v);
    c024_fab_AgregarPersonas = findViewById(R.id.c024_fab_AgregarPersonas_v);

//    LIST VIEW
    lvPasajeros = findViewById(R.id.lvPasajeros);
  }

  private void obtenerDataParaControles() throws Exception {
    try {
      List<String> p = new ArrayList<>();
      p.add(objConfLocal.get("ID_EMPRESA"));
      //PENDIENTE: CREAR ESTAS CONSULTAS EN QUERYS SQLITE
      arl_Proveedores = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Proveedores"), p, "READ"));
      arl_Rutas = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Rutas"), p, "READ"));
      arl_Vehiculos = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Vehiculos"), p, "READ"));
      arl_Conductores = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Conductores"), p, "READ"));
    } catch (Exception ex) {
      Funciones.mostrarError(this, ex);
    }
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

  public void onClick(View view) {
    try {
      int idControlClickeado = view.getId();
      if (idControlClickeado == R.id.c024_txv_PushTituloVentana_v) {
        Funciones.popUpTablasPendientesDeEnviar(this);
      } else if (idControlClickeado == R.id.c024_txv_PushRed_v) {
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
      } else if (idControlClickeado == R.id.c024_txv_PushVersionApp_v || idControlClickeado == R.id.c024_txv_PushVersionDataBase_v) {
        Funciones.popUpStatusVersiones(this);
      } else if (idControlClickeado == R.id.c024_txv_PushIdentificador_v) {
        mostrarMenuUsuario(this.txv_PushIdentificador);
      }
      //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
      //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
      //...
      else if (idControlClickeado == R.id.c024_txt_Fecha_v) {
        //c024_txt_Fecha = findViewById(R.id.c024_txt_Fecha_v);
//                month = month - 1;
//                DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo, setListener, year, month, day);
//                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                datePickerDialog.show();
        //PENDIENTE: EVALUAR LA POSIBILIDAD DE PASARLE AL POPUP NO SOLO EL CONTROL QUE MUESTRA EL VALOR SINO TAMBIEN EL REGISTRO MISMO
        PopUpCalendario d = new PopUpCalendario(this, c024_txt_Fecha);
        d.show();
      } else if (idControlClickeado == R.id.c024_lly_Proveedor_v) {
        //c024_lly_Proveedor_v = findViewById(R.id.c024_lly_Proveedor_v);
        PopUpBuscarEnLista d = new PopUpBuscarEnLista(this, arl_Proveedores, c024_txt_Proveedor_Ruc, c024_txt_Proveedor_RazonSocial);
        d.show();
      } else if (idControlClickeado == R.id.c024_lly_Vehiculo_v) {
        //c024_lly_Vehiculo = findViewById(R.id.c024_lly_Vehiculo_v);
        PopUpBuscarEnLista d = new PopUpBuscarEnLista(this, arl_Vehiculos, c024_txt_Vehiculo_Placa, c024_txt_Vehiculo_NroPasajeros);
        d.show();
      } else if (idControlClickeado == R.id.c024_lly_Ruta_v) {
        //c024_lly_Ruta = findViewById(R.id.c024_lly_Ruta_v);
        PopUpBuscarEnLista d = new PopUpBuscarEnLista(this, arl_Rutas, c024_txt_Ruta_Id, c024_txt_Ruta_Descripcion);
        d.show();
      } else if (idControlClickeado == R.id.c024_lly_Conductor_v) {
        //c024_lly_Conductor = findViewById(R.id.c024_lly_Conductor_v);
        PopUpBuscarEnLista d = new PopUpBuscarEnLista(this, arl_Conductores, c024_txt_Conductor_NroBrevete, c024_txt_Conductor_Nombre);
        d.show();
      } else if (idControlClickeado == R.id.c024_lly_Observacion_v) {
        PopUpObservacion d = new PopUpObservacion(this, objRex.Get("Observacion"), c024_txt_Observacion);
        d.show();
      } else if (idControlClickeado == R.id.c024_fab_Volver_v) {
        finish();
      } else if (idControlClickeado == R.id.c024_fab_Guardar_v) {
        guardarRegistro();
        registroModificado = false;
      } else if (idControlClickeado == R.id.c024_fab_AgregarPersonas_v) {
        //c024_fab_AgregarPersonas = findViewById(R.id.c024_fab_AgregarPersonas_v);
        //throw new IllegalStateException("Click sin programacion: " + view.getId());
        if (registroModificado && guardarRegistro()) {
          abrirActividad();
        }
      }

/*
            c024_txv_Id = findViewById(R.id.c024_txv_Id_v);
            c024_txt_Proveedor_Ruc = findViewById(R.id.c024_txt_Proveedor_Ruc_v);
            c024_txt_Proveedor_RazonSocial = findViewById(R.id.c024_txt_Proveedor_RazonSocial_v);
            c024_txt_Vehiculo_Placa = findViewById(R.id.c024_txt_Vehiculo_Placa_v);
            c024_txt_Vehiculo_NroPasajeros = findViewById(R.id.c024_txt_Vehiculo_NroPasajeros_v);
            c024_txt_Ruta_Id = findViewById(R.id.c024_txt_Ruta_Id_v);
            c024_txt_Ruta_Descripcion = findViewById(R.id.c024_txt_Ruta_Descripcion_v);
            c024_txt_Conductor_NroBrevete = findViewById(R.id.c024_txt_Conductor_NroBrevete_v);
            c024_txt_Conductor_Nombre = findViewById(R.id.c024_txt_Conductor_Nombre_v);
            c024_txt_Observacion = findViewById(R.id.c024_txt_Observacion_v);
*/
      else throw new IllegalStateException("Click sin programacion: " + view.getId());
    } catch (Exception ex) {
      Funciones.mostrarError(this, ex);
    }
  }

  private boolean guardarRegistro() {
    try {
      if (((objRex.Existe("IdEstado") && !objRex.Get("IdEstado").equals("TR")) || !objRex.Existe("IdEstado")) &&
          objSqlite.GuardarRex(objConfLocal, "trx_ServiciosTransporte", objRex)) {
        //if (s_IdRex==null || s_IdRex.length()==0){}
        s_IdRex = (s_IdRex == null || s_IdRex.length() == 0) ? objRex.Get("Id") : s_IdRex;
        Funciones.mostrarEstatusGeneral(this.getBaseContext(),
            objConfLocal,
            txv_PushTituloVentana,
            txv_PushRed,
            txv_NombreApp,
            txv_PushVersionApp,
            txv_PushVersionDataBase,
            txv_PushIdentificador
        );
        obtenerRexActual();
        mostrarValoresRexActual();

        Swal.success(this, "Correcto","El registro se ha guardado correctamente", 1500);
        finish();
//                Funciones.notificar(this, "Registro guardado correctamente.");
        return true;
      } else {
        Funciones.notificar(this, "El Registro no cuenta con el estado PENDIENTE, imposible actualizar.");
        return false;
      }
    } catch (Exception ex) {
      Funciones.mostrarError(this, ex);
      return false;
    }
  }

  //@Jota:
  //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
  //...

  //PENDIENTE: CREAR CLASE TIEMPO PARA USAR FUNCIONES PROPIAS
  public void mostrarValoresRexActual() {
//        DateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
//        Date date = new Date();
    //System.out.println(formato.format(date));
    c024_txv_Id.setText(objRex.Get("Id"));
    //c024_txt_Fecha.setText(objRex.Existe("Fecha") ? objRex.Get("Fecha") : formato.format(date));
    c024_txt_Fecha.setText(Funciones.malograrFecha(objRex.Get("Fecha")));
    c024_txt_Proveedor_Ruc.setText(objRex.Get("IdProveedor"));
    c024_txt_Proveedor_RazonSocial.setText(objRex.Get("Proveedor"));
    c024_txt_Vehiculo_Placa.setText(objRex.Get("IdVehiculo"));
    c024_txt_Vehiculo_NroPasajeros.setText(objRex.Get("Capacidad"));
    c024_txt_Ruta_Id.setText(objRex.Get("IdRuta"));
    c024_txt_Ruta_Descripcion.setText(objRex.Get("Ruta"));
    c024_txt_Conductor_NroBrevete.setText(objRex.Get("IdConductor"));
    c024_txt_Conductor_Nombre.setText(objRex.Get("Conductor"));
    c024_txt_Observacion.setText(objRex.Get("Observacion"));
  }

  private void abrirActividad() {
    if (objRex.Existe("Id")) {
      Intent nuevaActividad = new Intent(this, cls_08020000_AgregarDni.class);
      nuevaActividad.putExtra("ConfiguracionLocal", objConfLocal);
      nuevaActividad.putExtra("IdRegistro", objRex.Get("Id"));
      startActivity(nuevaActividad);
    } else Funciones.notificar(this, "Antes de agregar Dni debe de guardar el registro actual.");
  }

//    private void setearSelectorFecha() {
////        c024_txt_Fecha.setOnClickListener(new View.OnClickListener() {
////                                        @Override
////                                        public void onClick(View view) {
////                                            //Locale.setDefault(Spani);
////                                            month = month - 1;
////                                            DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo, setListener, year, month, day);
////                                            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
////                                            datePickerDialog.show();
////                                        }
////                                    }
////        );
//        setListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int anio, int mes, int dia) {
//                mes = mes + 1;
//                day =dia;
//                month =mes;
//                year =anio;
//                String fec =  (dia  < 10 ? "0" + dia : dia) + "/" + (mes < 10 ? "0" + mes : mes) + "/" + anio; //  day + "/" + month + "/" + year;
//                c024_txt_Fecha.setText(fec);
//                registroActual.s("Fecha",fec);
//                //tareoActual.setFecha(LocalDate.of(year,month,day));
//            }
//        };
//    }


  private void obtenerRexActual() throws Exception {
    List<String> p = new ArrayList<>();
    p.add(objConfLocal.get("ID_EMPRESA"));
    p.add(s_IdRex);
    objRex = objSqlite.CursorARex(objSqlite.doItBaby(objSqlite.obtQuery("OBTENER trx_ServiciosTransporte X ID"), p, "READ"));
    //PENDIENTE 1 DE 2: CREAR 2 OBJETOS UNO PARA IMPRESION VISUAL Y OTRO PARA GRABAR A SQLITE O MODIFICAR PROCEDIMIENTO DE GUARDADO EN SQLITE
    if (objRex.Get("Id").length() == 0) {
      String fechaHoraActual;// = dateFormat.format(date);
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      LocalDateTime now = LocalDateTime.now();
      fechaHoraActual = dtf.format(now);
      objRex.Set("IdEmpresa", objConfLocal.get("ID_EMPRESA"));
      objRex.Set("IdEstado", "PE");
      objRex.Set("IdUsuarioCrea", objConfLocal.get("ID_USUARIO_ACTUAL"));
      objRex.Set("FechaHoraCreacion", fechaHoraActual);
      objRex.Set("IdUsuarioActualiza", objConfLocal.get("ID_USUARIO_ACTUAL"));
    }
  }

}