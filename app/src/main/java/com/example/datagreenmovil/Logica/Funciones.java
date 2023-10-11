package com.example.datagreenmovil.Logica;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AlertDialog;

import android.os.CountDownTimer;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Entidades.AdapatadorSpinner;
import com.example.datagreenmovil.Entidades.ClaveValor;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.Tabla;
import com.example.datagreenmovil.Entidades.Tareo;
import com.example.datagreenmovil.Entidades.TareoDetalle;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.cls_01000000_Commutador;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigInteger;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Funciones {

  //private MenuItem item;
/*
    public static void cargarSpinner(Context ctx, Spinner spi, Cursor c, int iC, int iV) throws Exception { //iC, iV indexClave, indexValor
        try{
            ClaveValor[] cv = ClaveValor.getArrayClaveValor(c, null, iC, iV); //null;
            //ClaveValor fff = new ClaveValor();
            //cv = ClaveValor.getArrayClaveValor(c, cv, iC, iV);
            AdapatadorSpinner adaptador =new AdapatadorSpinner(ctx, android.R.layout.simple_spinner_dropdown_item, cv);
            spi.setAdapter(adaptador);
            //spi.setPrompt("Seleccionar");
            //spi.setSelection(-1);
            //ArrayAdapter<String> spiAA = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,s);
            //spiAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //spiCultivos.setAdapter(spiAA);


            //AdapterView.OnItemSelectedListener onItemSelectedListener =
                    new AdapterView.OnItemSelectedListener(){
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            ClaveValor obj = (ClaveValor)(parent.getItemAtPosition(position));
                            //textView2.setText(String.valueOf(obj.getValor())); --> con esto se obtiene la clave del elemento seleccionado
                            //Toast.makeText(cls_05010000_Edicion.super.getBaseContext() ,String.valueOf(obj.getValor()),Toast.LENGTH_LONG).show();
                            /*switch (view.getId()) {
                                case R.id.spiCultivo_v:
                                    td.setIdCultivo(obj.getValor());
                                    td.setCultivo(obj.getClave());
                                    break;
                                case R.id.spiVariedad_v:
                                    td.setIdVariedad(obj.getValor());
                                    td.setVariedad(obj.getClave());
                                    break;
                                case R.id.spiActividad_v:
                                    td.setIdActividad(obj.getValor());
                                    td.setActividad(obj.getClave());
                                    break;
                                case R.id.spiLabor_v:
                                    td.setIdLabor(obj.getValor());
                                    td.setLabor(obj.getClave());
                                    break;
                                case R.id.spiConsumidor_v:
                                    td.setIdConsumidor(obj.getValor());
                                    td.setConsumidor(obj.getClave());
                                    break;

                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    };

            //spi.setOnItemSelectedListener(onItemSelectedListener);
        }catch(Exception ex) {
            throw  ex;
        }
    }
    */
/*
    public static void cargarSpinner(Context ctx, Spinner spi, ResultSet rs, int iC, int iV) throws Exception { //iC, iV indexClave, indexValor
        try{
            ClaveValor[] cv = ClaveValor.getArrayClaveValor(rs,  iC, iV); //null;
            if(cv==null || cv.length==0){
                spi.setAdapter(null);
            }else{
                AdapatadorSpinner adaptador =new AdapatadorSpinner(ctx, android.R.layout.simple_spinner_dropdown_item, cv);
                spi.setAdapter(adaptador);
            }
        }catch(Exception ex) {
            throw  ex;
        }
    }

 */
/*
    public static void cargarSpinner(Context ctx, Spinner spi, MiData rs, int iC, int iV) throws Exception { //iC, iV indexClave, indexValor
        try{
            ClaveValor[] cv = ClaveValor.getArrayClaveValor(rs,  iC, iV); //null;
            if(cv==null || cv.length==0){
                spi.setAdapter(null);
            }else{
                AdapatadorSpinner adaptador =new AdapatadorSpinner(ctx, android.R.layout.simple_spinner_dropdown_item, cv);
//                AdapatadorSpinner adaptador =new AdapatadorSpinner(ctx, R.drawable.textview_spinner, cv);
                spi.setAdapter(adaptador);
            }
        }catch(Exception ex) {
            throw  ex;
        }
    }
*/
  public static void cargarSpinner(Context ctx, Spinner spi, Tabla t, int iC, int iV) throws Exception { //iC, iV indexClave, indexValor
    try {
      if (t.Filas == null) {
        spi.setAdapter(null);
      } else {
        ClaveValor[] cvAux = ClaveValor.getArrayClaveValor(t, iC, iV);
        AdapatadorSpinner adaptador = new AdapatadorSpinner(ctx, android.R.layout.simple_spinner_dropdown_item, cvAux, null, 1);
        spi.setAdapter(adaptador);
      }
    } catch (Exception ex) {
      throw ex;
    }
  }

  public static void cargarSpinner(Context ctx, Spinner spi, Tabla t, int iC, int iV, ConfiguracionLocal cl, int v) throws Exception { //iC, iV indexClave, indexValor
    try {
      if (t.Filas == null) {
        spi.setAdapter(null);
      } else {
        ClaveValor[] cvAux = ClaveValor.getArrayClaveValor(t, iC, iV);
        AdapatadorSpinner adaptador = new AdapatadorSpinner(ctx, android.R.layout.simple_spinner_dropdown_item, cvAux, cl, v);
        spi.setAdapter(adaptador);
      }
    } catch (Exception ex) {
      throw ex;
    }
  }

  /*
      public static void cargarSpinner(Context ctx, Spinner spi, ClaveValor[] cv) throws Exception { //iC, iV indexClave, indexValor
          try{
              AdapatadorSpinner adaptador =new AdapatadorSpinner(ctx, android.R.layout.simple_spinner_dropdown_item, cv);
              spi.setAdapter(adaptador);

              //spi.setPrompt("Seleccionar");
              //spi.setSelection(2);
              /*new AdapterView.OnItemSelectedListener(){
                  @Override
                  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                      ClaveValor obj = (ClaveValor)(parent.getItemAtPosition(position));
                  }
                  @Override
                  public void onNothingSelected(AdapterView<?> parent) {spi.setPrompt("Debe Sel");}
              };
          }catch(Exception ex) {
              throw  ex;
              //Toast.makeText(super.getClass(), ex.getMessage(), Toast.LENGTH_LONG).show();;
          }
      }
  */
  public static void cargarAutoCompleteTextView(Context ctx, AutoCompleteTextView atv, String[] valores) {
    ArrayAdapter<String> adaptador = new ArrayAdapter<String>(ctx, android.R.layout.simple_list_item_1, valores);
    atv.setAdapter(adaptador);
  }

  /*
      public static void cargarAutoCompleteTextView(Context ctx, AutoCompleteTextView atv, String[] valores) {
          ArrayAdapter<String> adaptador = new ArrayAdapter<String>(ctx, android.R.layout.simple_list_item_1,valores);
          atv.setAdapter(adaptador);
      }
  */
  public static void setearThriggerAutoCompleteTextView(AutoCompleteTextView atv, TextView txv, ClaveValor[] cv) {
    atv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
        String dni = atv.getText().toString();
        String nombres = ClaveValor.obtenerClaveDesdeValor(dni, cv);
        txv.setText(nombres);
        //td.setDni(dni);
        //td.setNombres(nombres);
        //txv.setText(cv[pos].getValor());
      }
    });
  }

  public static void setearEventos(AutoCompleteTextView atv, TextView txv, ClaveValor[] cv, TareoDetalle td) {
    atv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
        String dni = atv.getText().toString();
        String nombres = ClaveValor.obtenerClaveDesdeValor(dni, cv);
        txv.setText(nombres);
        td.setDni(dni);
        td.setNombres(nombres);
        //txv.setText(cv[pos].getValor());
      }
    });
  }

  public static String siguienteCorrelativo(String ultimoId, char tipo) {
    String parteDelCorrelativo = ultimoId.substring(0, ultimoId.length() - 1);
    char ultimoCaracterDelCorrelativo = ultimoId.charAt(ultimoId.length() - 1), nuevoCaracter = ' ';
    int posEnSecuencia = 0;
    String sec = "";
    if (tipo == 'N') {
      sec = "0123456789";
    } else if (tipo == 'L') {
      sec = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    } else if (tipo == 'A') {
      sec = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    }
    posEnSecuencia = sec.indexOf(ultimoCaracterDelCorrelativo);
    if (posEnSecuencia < sec.length() - 1) {
      nuevoCaracter = sec.charAt(posEnSecuencia + 1);
    } else {
      sec.length();
      parteDelCorrelativo = siguienteCorrelativo(parteDelCorrelativo, tipo);
      nuevoCaracter = sec.charAt(0);
    }
    return parteDelCorrelativo + nuevoCaracter;
  }

  public static String siguienteCorrelativo(String ultimoId, char tipo, int longitud) {
    if (ultimoId == null || ultimoId.length() == 0 || ultimoId.length() < longitud) {
      String parteIdDispostivo = ultimoId;
      String parteCorrelativa = "";
      parteIdDispostivo = "";
      for (int i = 0; i < longitud - 1; i++) {
        parteCorrelativa = parteCorrelativa + "0";
      }
      parteCorrelativa = parteCorrelativa + "1";
      return parteIdDispostivo + parteCorrelativa;
    }
    String parteDelCorrelativo = ultimoId.substring(0, ultimoId.length() - 1);
    char ultimoCaracterDelCorrelativo = ultimoId.charAt(ultimoId.length() - 1), nuevoCaracter = ' ';
    int posEnSecuencia = 0;
    String sec = "";
    if (tipo == 'N') {
      sec = "0123456789";
    } else if (tipo == 'L') {
      sec = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    } else if (tipo == 'A') {
      sec = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    }
    posEnSecuencia = sec.indexOf(ultimoCaracterDelCorrelativo);
    if (posEnSecuencia < sec.length() - 1) {
      nuevoCaracter = sec.charAt(posEnSecuencia + 1);
    } else {
      sec.length();
      parteDelCorrelativo = siguienteCorrelativo(parteDelCorrelativo, tipo);
      nuevoCaracter = sec.charAt(0);
    }
    return parteDelCorrelativo + nuevoCaracter;
  }

  public static String intACorrelativo(int x, int l) {
    String r = String.valueOf(x);
    if (l > 0 && r.length() <= l) {
      return hileraChar('0', l - r.length()).concat(r);
    }
    return "";
  }

  public static String hileraChar(char c, int n) {
    String r = "";
    //int i=0;
    for (int i = 0; i < n; i++) {
      r = r + c;
    }
    return r;
  }

  public static ArrayList<String> recolectarParametros(Tareo t) {
    //EL objeto tareo debe hacer uso de la clase conexionsqlite para poder grabar su informacion
    ArrayList<String> r = new ArrayList<>();
    r.add("01");
    r.add(t.getId());
    r.add(t.getFecha().toString());
    r.add(t.getIdTurno());
    r.add("PE");
    r.add(t.getIdUsuario());
    r.add(t.getIdUsuario());
    r.add(Double.toString(t.getTotalHoras()));
    r.add(Double.toString(t.getTotalRdtos()));
    r.add(Double.toString(t.getTotalDetalles()));
    r.add(t.getObservaciones());
    return r;
/*                  (?,-- IdEmpresa              VARCHAR (2),
                       ?,-- Id                     VARCHAR (12),
                       ?,-- Fecha                  DATE           NOT NULL,
                       ?,-- Turno                  CHAR (1)       NOT NULL,
                       ?,-- IdEstado               VARCHAR(3)    NOT NULL,
                       ?,-- IdUsuarioCrea          VARCHAR (50),
                       DATETIME('now','localtime'),-- FechaHoraCreacion      DATETIME,
                       ?,-- IdUsuarioActualiza     VARCHAR (50),
                       DATETIME('now','localtime'),-- FechaHoraActualizacion DATETIME,
                       ?,-- FechaHoraTransferencia DATETIME,
                       ?,-- TotalHoras             NUMERIC (                   2),
                       ?,-- TotalRendimientos      NUMERIC (6,                 2),
                       ?,-- TotalDetalles          INT,
                       ?);-- Observaciones          VARCHAR(500)*/
  }

  public static ArrayList<String> recolectarParametros(TareoDetalle d) {
    ArrayList<String> r = new ArrayList<>();
    r.add("01");
    r.add(d.getIdTareo());
    r.add(String.valueOf(d.getItem()));
    r.add(d.getDni());
    r.add(d.getIdPlanilla());
    r.add(d.getIdConsumidor());
    r.add(d.getIdCultivo());
    r.add(d.getIdVariedad());
    r.add(d.getIdActividad());
    r.add(d.getIdLabor());
    r.add(d.getHoras().toString());
    r.add(d.getRdtos().toString());
        /*
        IdEmpresa           VARCHAR (2),
        Idtareo             VARCHAR (12)   NOT NULL,
        Item                SMALLINT       NOT NULL,
        Dni                 VARCHAR (8),
        IdPlanilla          VARCHAR (3),
        IdConsumidor        VARCHAR (30),
        IdCultivo           VARCHAR (4)    NOT NULL,
        IdVariedad          VARCHAR (3)    NOT NULL,
        IdActividad         VARCHAR (3),
        IdLabor             VARCHAR (6),
        SubTotalHoras       NUMERIC (6,2),
        SubTotalRendimiento NUMERIC (6,2)
         */
    return r;
  }

  public static String obtenerIdUnico(Activity act) {
    //Hacemos la validación de métodos, ya que el método getDeviceId() ya no se admite para android Oreo en adelante, debemos usar el método getImei()
    return Build.VERSION.SDK_INT >= 29 ? obtenerIdAndroid(act) : obtenerImei(act);
  }

  private static String obtenerImei(Activity act) {
    try {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions
//                (this, new String[]{Manifest.permission.READ_PHONE_STATE}, 100);
//            }
      TelephonyManager telephonyManager = (TelephonyManager) act.getSystemService(Context.TELEPHONY_SERVICE);
      String r = telephonyManager.getDeviceId();
      return r != null ? r.toUpperCase() : "";
    } catch (Exception ex) {
      Toast.makeText(act, ex.getMessage(), Toast.LENGTH_SHORT).show();
      return null;
    }
  }

  private static String obtenerIdAndroid(Activity act) {
    try {
      TelephonyManager tm = (TelephonyManager) act.getSystemService(Context.TELEPHONY_SERVICE);
      String deviceID = null;
      int readIMEI = ContextCompat.checkSelfPermission(act, Manifest.permission.READ_PHONE_STATE);
      if (deviceID == null) {
        if (readIMEI != PackageManager.PERMISSION_GRANTED) {
          return deviceID.toUpperCase();
        }
        deviceID = android.provider.Settings.Secure.getString(act.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
      }
      return deviceID.toUpperCase();
    } catch (Exception ex) {
      throw ex;
    }
  }

  public static String obtenerMac() {
    try {
      List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
      for (NetworkInterface nif : all) {
        if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

        byte[] macBytes = nif.getHardwareAddress();
        if (macBytes == null) {
          return "";
        }

        StringBuilder res1 = new StringBuilder();
        for (byte b : macBytes) {
          String hex = Integer.toHexString(b & 0xFF);
          if (hex.length() == 1)
            hex = "0".concat(hex);
          res1.append(hex.concat(":"));
        }

        if (res1.length() > 0) {
          res1.deleteCharAt(res1.length() - 1);
        }
        return res1.toString().toUpperCase();
      }
    } catch (Exception ex) {
    }
    return "";
  }

//    public static AlertDialog.Builder setearAlertDialogParaCerrarSesion_(ConfiguracionLocal objConfLocal, ConexionSqlite objSqlite,Activity actividad) {
//        AlertDialog.Builder builderDialogoCerrarSesion;
//        builderDialogoCerrarSesion = new AlertDialog.Builder(actividad);
//        builderDialogoCerrarSesion.setCancelable(true);
//        builderDialogoCerrarSesion.setTitle("Cerrar Sesion");
//        builderDialogoCerrarSesion.setMessage("Seguro que desea cerrar su sesion?");
//        builderDialogoCerrarSesion.setPositiveButton("Si.",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        try {
//                            cerrarSesion(objConfLocal,objSqlite,actividad);
//                            //mostrarValoresEnPantalla();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//        builderDialogoCerrarSesion.setNegativeButton("No.", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        });
//        return builderDialogoCerrarSesion;
//    }

  public static Dialog obtenerDialogParaCerrarSesion(Context context, ConfiguracionLocal objConfLocal, ConexionSqlite objSqlite, Activity actividad) {
    //Context context, String titulo, boolean btnCerrar, String mensaje, boolean btnCancelar, boolean btnAceptar){
    try {
      Dialog popUp = new Dialog(context);
      popUp.setContentView(R.layout.v_popup_generico_017);
      popUp.getWindow().setBackgroundDrawableResource(R.drawable.bg_popup_prueba);

      //MANEJO DE CONTROLES INTERNOR DEL POPUP
      TextView c017_txv_Titulo = popUp.findViewById(R.id.c017_txv_Titulo_v);
      c017_txv_Titulo.setText("Cerrar Sesion");

      FloatingActionButton c017_fab_Cerrar = popUp.findViewById(R.id.c017_fab_Cerrar_v);
      c017_fab_Cerrar.hide();

      TextView c017_txv_Mensaje = popUp.findViewById(R.id.c017_txv_Mensaje_v);
      c017_txv_Mensaje.setText("Desea Cerrar la sesion?");

      Button c017_btn_Cancelar = popUp.findViewById(R.id.c017_btn_Cancelar_v);
      c017_btn_Cancelar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          popUp.dismiss();
        }
      });

      Button c017_btn_Aceptar = popUp.findViewById(R.id.c017_btn_Aceptar_v);
      c017_btn_Aceptar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          //popUp.dismiss();
          try {
            cerrarSesion(objConfLocal, objSqlite, actividad);
            popUp.dismiss();
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
      });

      Double x, y;
      x = context.getResources().getDisplayMetrics().widthPixels * 0.90;
      y = context.getResources().getDisplayMetrics().heightPixels * 0.40;
      popUp.getWindow().setLayout(x.intValue(), y.intValue());
      popUp.getWindow().setBackgroundDrawableResource(R.drawable.bg_popup);
      return popUp;
    } catch (Exception ex) {
      Funciones.mostrarError(context, ex);
      return null;
    }
  }

  private static void cerrarSesion(ConfiguracionLocal objConfLocal, ConexionSqlite objSqlite, Activity actividad) throws Exception {
//    objConfLocal.set("ID_USUARIO_ACTUAL", "");
//    objConfLocal.set("NOMBRE_USUARIO_ACTUAL", "");
//    objConfLocal.set("TOKEN_EXPIRA", "");
//    objConfLocal.set("MODULOS_PERMITIDOS", "");
    objSqlite.guardarConfiguracionLocal(objConfLocal);
    Intent intent = new Intent(actividad, cls_01000000_Commutador.class);
    actividad.startActivity(intent);
    actividad.finishAffinity();
  }

  public static void preguntarSiCierraSesion(AlertDialog.Builder builderDialogoCerrarSesion) throws Exception {
    AlertDialog dialogoCerrarSesion = builderDialogoCerrarSesion.create();
    dialogoCerrarSesion.show();
  }

  public static Dialog obtenerDialogParaCambiarClave(Context context, ConfiguracionLocal objConfLocal, ConexionSqlite objSqlite, Activity actividad) {
    //Context context, String titulo, boolean btnCerrar, String mensaje, boolean btnCancelar, boolean btnAceptar){
    SharedPreferences sharedPreferences = context.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
    try {

      Dialog popUp = new Dialog(context);
      popUp.setContentView(R.layout.popup_cambiar_clave_018);
      popUp.getWindow().setBackgroundDrawableResource(R.drawable.bg_popup_prueba);

      //MANEJO DE CONTROLES INTERNOR DEL POPUP
      ImageView c018_imv_Cerrar;
      TextView c018_txv_IdUsuario;
      EditText c018_etx_ContraseniaActual, c018_etx_ContraseniaNueva, c018_etx_ConfirmacionContraseniaNueva;
      FloatingActionButton c018_fab_Ok;
      TextInputEditText etxContraseniaActual;
      Animation rotateAnimation;
      Animation scaleAnimation;
      TextInputEditText etxUsuarioCondicional;

      rotateAnimation = AnimationUtils.loadAnimation(popUp.getContext(), R.anim.animation_button_save);
      scaleAnimation = AnimationUtils.loadAnimation(popUp.getContext(), R.anim.scale_button_save);

      c018_imv_Cerrar = popUp.findViewById(R.id.c018_imv_Cerrar_v);
      c018_txv_IdUsuario = popUp.findViewById(R.id.c018_txv_IdUsuario_v);
      c018_etx_ContraseniaActual = popUp.findViewById(R.id.c018_etxh_ContrasenaActual_v);
      c018_etx_ContraseniaNueva = popUp.findViewById(R.id.c018_etx_NuevaContrasena_v);
      c018_etx_ConfirmacionContraseniaNueva = popUp.findViewById(R.id.c018_etx_ConfirmarNuevaContrasena_v);
      c018_fab_Ok = popUp.findViewById(R.id.c018_fab_Ok_v);
      etxUsuarioCondicional = popUp.findViewById(R.id.etxUsuarioCondicional);


      c018_txv_IdUsuario.setText(sharedPreferences.getString("ID_USUARIO_ACTUAL","!ID_USUARIO_ACTUAL"));
      c018_imv_Cerrar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          popUp.dismiss();
        }
      });
      c018_fab_Ok.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          c018_fab_Ok.startAnimation(rotateAnimation);
          c018_fab_Ok.setImageResource(R.drawable.ic_check);
          c018_fab_Ok.startAnimation(scaleAnimation);

          String IdUsuarioActual = sharedPreferences.getString("ID_USUARIO_ACTUAL","!ID_USUARIO_ACTUAL"), nuevaContrasenia, confirmacionNuevaContrasenia;
          nuevaContrasenia = c018_etx_ContraseniaNueva.getText().toString();
          confirmacionNuevaContrasenia = c018_etx_ConfirmacionContraseniaNueva.getText().toString();
          if (validarNuevaContrasenia(nuevaContrasenia, confirmacionNuevaContrasenia)) {
            try {
              if (objSqlite.cambiarContrasenia(IdUsuarioActual, generarMD5(IdUsuarioActual + nuevaContrasenia))) {
//                notificar(actividad, "Contraseña guardada con exito.");
//                Swal.success(context, "Correcto","La contraseña se ha guardado con éxito.",1500);
                Toast.makeText(context, "La contraseña se ha guardado con éxito.", Toast.LENGTH_SHORT).show();
              }


            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          } else {
            Funciones.notificar(actividad, "Las contraseñas no coinciden. La contraseña debe tener al menos 6 digitos.");
          }
        }
      });
      Double x, y;
      x = context.getResources().getDisplayMetrics().widthPixels * 0.90;
      y = context.getResources().getDisplayMetrics().heightPixels * 0.40;
      popUp.getWindow().setLayout(x.intValue(), y.intValue());
      popUp.getWindow().setBackgroundDrawableResource(R.drawable.bg_popup);
      return popUp;
    } catch (Exception ex) {
      Funciones.mostrarError(context, ex);
      return null;
    }

  }

  //  SOBRECARGA PARA ABRIR POPUP CAMBIAR CLAVE
  public static Dialog obtenerDialogParaCambiarClave(Context context, ConfiguracionLocal objConfLocal, ConexionSqlite objSqlite, Activity actividad, Boolean isForgetPassword) {
    //Context context, String titulo, boolean btnCerrar, String mensaje, boolean btnCancelar, boolean btnAceptar){
    SharedPreferences sharedPreferences = context.getSharedPreferences("objConfLocal",Context.MODE_PRIVATE);
    try {
      Dialog popUp = new Dialog(context);
      popUp.setContentView(R.layout.popup_cambiar_clave_018);
      popUp.getWindow().setBackgroundDrawableResource(R.drawable.bg_popup_prueba);

      //MANEJO DE CONTROLES INTERNOR DEL POPUP
      ImageView c018_imv_Cerrar;
      TextView c018_txv_IdUsuario;
      EditText c018_etx_ContraseniaActual, c018_etx_ContraseniaNueva, c018_etx_ConfirmacionContraseniaNueva;
      FloatingActionButton c018_fab_Ok;
      TextInputEditText etxContraseniaActual;
      Animation rotateAnimation;
      Animation scaleAnimation;
      TextInputEditText etxUsuarioCondicional;
      TextView txtUsuarioTitle;
      TextInputLayout tillyUsuario;

      rotateAnimation = AnimationUtils.loadAnimation(popUp.getContext(), R.anim.animation_button_save);
      scaleAnimation = AnimationUtils.loadAnimation(popUp.getContext(), R.anim.scale_button_save);

      c018_imv_Cerrar = popUp.findViewById(R.id.c018_imv_Cerrar_v);
      c018_txv_IdUsuario = popUp.findViewById(R.id.c018_txv_IdUsuario_v);
      c018_etx_ContraseniaActual = popUp.findViewById(R.id.c018_etxh_ContrasenaActual_v);
      c018_etx_ContraseniaNueva = popUp.findViewById(R.id.c018_etx_NuevaContrasena_v);
      c018_etx_ConfirmacionContraseniaNueva = popUp.findViewById(R.id.c018_etx_ConfirmarNuevaContrasena_v);
      c018_fab_Ok = popUp.findViewById(R.id.c018_fab_Ok_v);
      etxUsuarioCondicional = popUp.findViewById(R.id.etxUsuarioCondicional);

      txtUsuarioTitle = popUp.findViewById(R.id.txtUsuarioTitle);

      tillyUsuario = popUp.findViewById(R.id.tillyUsuario);


      if (!!isForgetPassword) {
        txtUsuarioTitle.setVisibility(View.GONE);
        c018_txv_IdUsuario.setVisibility(View.GONE);
        tillyUsuario.setVisibility(View.VISIBLE);
      } else {
        txtUsuarioTitle.setVisibility(View.VISIBLE);
        c018_txv_IdUsuario.setVisibility(View.VISIBLE);
        tillyUsuario.setVisibility(View.GONE);
      }
      c018_txv_IdUsuario.setText(sharedPreferences.getString("ID_USUARIO_ACTUAL","!ID_USUARIO_ACTUAL"));
      c018_imv_Cerrar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          popUp.dismiss();
        }
      });
      c018_fab_Ok.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          c018_fab_Ok.startAnimation(rotateAnimation);
          c018_fab_Ok.setImageResource(R.drawable.ic_check);
          c018_fab_Ok.startAnimation(scaleAnimation);

          String IdUsuarioActual = etxUsuarioCondicional.getText().toString(), nuevaContrasenia, confirmacionNuevaContrasenia;
          nuevaContrasenia = c018_etx_ContraseniaNueva.getText().toString();
          confirmacionNuevaContrasenia = c018_etx_ConfirmacionContraseniaNueva.getText().toString();
          if (validarNuevaContrasenia(nuevaContrasenia, confirmacionNuevaContrasenia)) {
            try {
              if (objSqlite.cambiarContrasenia(IdUsuarioActual, generarMD5(IdUsuarioActual + nuevaContrasenia))) {
                Toast.makeText(context, "La contraseña se ha guardado con éxito.", Toast.LENGTH_SHORT).show();
//                Swal.success(actividad.getBaseContext(), "Correcto","",1500);
                popUp.hide();
              }

            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          } else {
            Funciones.notificar(actividad, "Las contraseñas no coinciden. La contraseña debe tener al menos 6 digitos.");
          }
        }
      });
      Double x, y;
      x = context.getResources().

          getDisplayMetrics().widthPixels * 0.90;
      y = context.getResources().

          getDisplayMetrics().heightPixels * 0.40;
      popUp.getWindow().

          setLayout(x.intValue(), y.

              intValue());
      popUp.getWindow().

          setBackgroundDrawableResource(R.drawable.bg_popup);
      return popUp;
    } catch (Exception ex) {
      Funciones.mostrarError(context, ex);
    }

    return null;

  }
//    private static void cambiarContrasenia(String idUsuarioActual, String nuevaContrasenia) {
//    }

  private static boolean validarNuevaContrasenia(String nuevaContrasenia, String
      confirmacionNuevaContrasenia) {
    return nuevaContrasenia.equals(confirmacionNuevaContrasenia) && nuevaContrasenia.length() >= 6;
  }

  public static void mostrarEstatusGeneral(Context context,
                                           ConfiguracionLocal cl,
                                           TextView txv_TituloVentana,
                                           TextView txv_PushRed,
                                           TextView txv_NombreApp,
                                           TextView txv_PushVersionApp,
                                           TextView txv_PushVersionDataBase,
                                           TextView txv_PushIdentificador) {
    SharedPreferences confLocal = context.getSharedPreferences("objConfLocal",Context.MODE_PRIVATE);

    //PUSH TITULO VENTANA
    if ( confLocal.getString("EXISTE_DATA_PENDIENTE","FALSE").equals("TRUE")) {
      txv_TituloVentana.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.black, null));
      txv_TituloVentana.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.blanco, null));
    } else {
      txv_TituloVentana.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.verdeClaro, null));
      txv_TituloVentana.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.grisOscuro, null));
    }
    //PUSH ESTADO RED
    txv_PushRed.setText(confLocal.getString("ESTADO_RED","OFFLINE"));
    if (confLocal.getString("ESTADO_RED","OFFLINE").equals("ONLINE")) {
      txv_PushRed.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.verdeClaro, null));
      txv_PushRed.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.grisOscuro, null));
    } else {
      txv_PushRed.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.alerta, null));
      txv_PushRed.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.blanco, null));
    }
    //NOMBRE APP
    txv_NombreApp.setText(confLocal.getString("NOMBRE_APP","!NOMBRE_APP"));
    //VERSION APP
    String versionApp = " v: " + confLocal.getString("VERSION_APP","!VERSION_APP");
    txv_PushVersionApp.setText(versionApp);
    if (versionApp.equals(confLocal.getString("VERSION_APP_DISPONIBLE","!VERSION_APP_DISPONIBLE"))) {
      txv_PushVersionApp.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.blanco, null));
      txv_PushVersionApp.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.grisOscuro, null));
    } else {
      txv_PushVersionApp.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.verde, null));
      txv_PushVersionApp.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.blanco, null));
    }
    //VERSION DATABASE
    String versionDB = " v: " +  confLocal.getString("VERSION_DB_SQLITE","");
    txv_PushVersionDataBase.setText(versionDB);
    if (versionDB.equals(confLocal.getString("VERSION_DB_DISPONIBLE","!VERSION_DB_DISPONIBLE"))) {
      txv_PushVersionDataBase.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.blanco, null));
      txv_PushVersionDataBase.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.grisOscuro, null));
    } else {
      txv_PushVersionDataBase.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.verde, null));
      txv_PushVersionDataBase.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.blanco, null));
    }
    //IDENTIFICADOR
    String identificador = confLocal.getString("ID_DISPOSITIVO","!ID_DISPOSITIVO") + " " + confLocal.getString("NOMBRE_USUARIO_ACTUAL","!NOMBRE_USUARIO_ACTUAL");
    txv_PushIdentificador.setText(identificador);
  }

  public static void mostrarEstatusGeneral(Context
                                               context, //USADO SOLAMENTE EN ACTIVIDAD DE LECTURA DE EFICIENCIAS -> DEPURAR Y ELIMINAR ESTE METODO
                                           ConfiguracionLocal cl,
                                           TextView txv_PushIdentificador,
                                           TextView txv_TituloVentana,
                                           TextView txv_PushRed
  ) {
    //PUSH TITULO VENTANA
    if (("EXISTE_DATA_PENDIENTE").equals("TRUE")) {
      txv_TituloVentana.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.black, null));
      txv_TituloVentana.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.blanco, null));
    } else {
      txv_TituloVentana.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.verdeClaro, null));
      txv_TituloVentana.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.grisOscuro, null));
    }
    //PUSH ESTADO RED
    txv_PushRed.setText(cl.get("ESTADO_RED"));
    if (cl.get("ESTADO_RED").equals("ONLINE")) {
      txv_PushRed.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.verdeClaro, null));
      txv_PushRed.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.grisOscuro, null));
    } else {
      txv_PushRed.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.alerta, null));
      txv_PushRed.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.blanco, null));
    }
    //NOMBRE APP
//        txv_NombreApp.setText(cl.get("NOMBRE_APP"));
//        //VERSION APP
//        String versionApp = " v: " + cl.get("VERSION_APP");
//        txv_PushVersionApp.setText(versionApp);
//        if (cl.get("VERSION_APP").equals(cl.get("VERSION_APP_DISPONIBLE"))){
//            txv_NombreApp.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.blanco, null));
//            txv_NombreApp.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.grisOscuro, null));
//        }else {
//            txv_NombreApp.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.verde, null));
//            txv_NombreApp.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.blanco, null));
//        }
//        //VERSION DATABASE
//        String versionDB = " v: " + cl.get("VERSION_APP");
//        txv_PushVersionDataBase.setText(versionDB);
//        if (cl.get("VERSION_APP").equals(cl.get("VERSION_APP_DISPONIBLE"))){
//            txv_PushVersionDataBase.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.blanco, null));
//            txv_PushVersionDataBase.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.grisOscuro, null));
//        }else {
//            txv_PushVersionDataBase.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.verde, null));
//            txv_PushVersionDataBase.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.blanco, null));
//        }
    //IDENTIFICADOR
    String identificador = cl.get("ID_DISPOSITIVO") + " " + cl.get("NOMBRE_USUARIO_ACTUAL");
    txv_PushIdentificador.setText(identificador);
  }

  public static String generarMD5(String input) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] messageDigest = md.digest(input.getBytes());
      BigInteger no = new BigInteger(1, messageDigest);
      String hashtext = no.toString(16);
      while (hashtext.length() < 32) {
        hashtext = "0" + hashtext;
      }
      return hashtext.toUpperCase();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

//    public static void mostrarError(Context context, Exception ex){
//        StackTraceElement z = new Exception().getStackTrace()[0];
//        String detalleError = z.getFileName() + "\n" + z.getMethodName() + "\n" + z.getLineNumber() + ": \n" + ex.getMessage();
//        ex.printStackTrace();
//        //Toast.makeText(context,detalleError, Toast.LENGTH_LONG).show();
//        notificar(context, detalleError);
//    }

  public static void mostrarError(Context context, Exception ex) {
//        StackTraceElement[] z = ex.getStackTrace();
//        String detalleError = z.getFileName() + "\n" + z.getMethodName() + "\n" + z.getLineNumber() + ": \n" + ex.getMessage();
    Log.e("ERROR LOGIN", ex.toString());
    ex.printStackTrace();
    //Toast.makeText(context,detalleError, Toast.LENGTH_LONG).show();
//        notificar(context, detalleError);
  }

  public static void mostrarPopUp(Context context, String titulo, boolean btnCerrar, String
      mensaje, boolean btnCancelar, boolean btnAceptar) {
    try {
      Dialog popUp = new Dialog(context);
//      popUp.setContentView(R.layout.v_popup_generico_017);
      popUp.getWindow().setBackgroundDrawableResource(R.drawable.bg_popup_prueba);


      //MANEJO DE CONTROLES INTERNOR DEL POPUP
      TextView c017_txv_Titulo = popUp.findViewById(R.id.c017_txv_Titulo_v);
      c017_txv_Titulo.setText(titulo);

      FloatingActionButton c017_fab_Cerrar = popUp.findViewById(R.id.c017_fab_Cerrar_v);
      c017_fab_Cerrar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          popUp.dismiss();
        }
      });
      if (btnCerrar) {
        c017_fab_Cerrar.hide();
      }

      TextView c017_txv_Mensaje = popUp.findViewById(R.id.c017_txv_Mensaje_v);
      c017_txv_Mensaje.setText(mensaje);

      Button c017_btn_Cancelar = popUp.findViewById(R.id.c017_btn_Cancelar_v);
      c017_btn_Cancelar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          popUp.dismiss();
        }
      });
      if (!btnCancelar) {
        c017_btn_Cancelar.setVisibility(View.GONE);
      }

      Button c017_btn_Aceptar = popUp.findViewById(R.id.c017_btn_Aceptar_v);
      c017_btn_Aceptar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          popUp.dismiss();
        }
      });
      if (!btnAceptar) {
        c017_btn_Aceptar.setVisibility(View.GONE);
      }

      Window window = popUp.getWindow();

      window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      popUp.show();
    } catch (Exception ex) {
      Funciones.mostrarError(context, ex);
    }
  }

  public static void popUpTablasPendientesDeEnviar(Context context) {
    Funciones.mostrarPopUp(context,
        "Data Pendiente",
        false,
        "Existe data pendiente de enviar en algunas tablas, revisar.",
        true,
        true);
  }


  public static void popUpStatusVersiones(Context context) {
    Funciones.mostrarPopUp(context,
        "Informacion De Software",
        false,
        "Actualizacion Disponible.",
        true,
        true);
  }

  //    public static void popUpActualizarDetalleTareos(Context context, ConexionSqlite objSqlite, TareoDetalle detalle){
//        try{
//            Dialog popUp = new Dialog(context);
//            popUp.setContentView(R.layout.v_popup_actualizar_detalle_tareo_021);
//            popUp.getWindow().setBackgroundDrawableResource(R.drawable.bg_popup);
//
//            //MANEJO DE CONTROLES INTERNOR DEL POPUP
//            EditText c021_etx_Horas = popUp.findViewById(R.id.c021_etx_Horas_v);
//            EditText c021_etx_Rdtos = popUp.findViewById(R.id.c021_etx_Rdtos_v);
//            EditText c021_etx_Observacion = popUp.findViewById(R.id.c021_etx_Observacion_v);
//
//            TextView c021_txv_IdTareo = popUp.findViewById(R.id.c021_txv_IdTareo_v);
//            c021_txv_IdTareo.setText(detalle.getIdTareo());
//
//            TextView c021_txv_Item = popUp.findViewById(R.id.c021_txv_Item_v);
//            c021_txv_Item.setText(String.valueOf(detalle.getItem()));
//
//            TextView c021_txv_Dni = popUp.findViewById(R.id.c021_txv_Dni_v);
//            c021_txv_Dni.setText(detalle.getDni());
//
//            TextView c021_txv_NombreTrabajador = popUp.findViewById(R.id.c021_txv_NombreTrabajador_v);
//            c021_txv_NombreTrabajador.setText(detalle.getNombres());
//
//            Button c021_btn_Aceptar = popUp.findViewById(R.id.c021_btn_Aceptar_v);
//            c021_btn_Aceptar.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View view){
//                    detalle.setHoras(Double.parseDouble(c021_etx_Horas.getText().toString()));
//                    detalle.setRdtos(Double.parseDouble(c021_etx_Rdtos.getText().toString()));
//                    detalle.setObservacion(c021_etx_Observacion.getText().toString());
//                    try {
//                        detalle.guardar(objSqlite);
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//
//                    popUp.dismiss();
//                }
//            });
//
//            Button c021_btn_Cancelar = popUp.findViewById(R.id.c021_btn_Cancelar_v);
//            c021_btn_Cancelar.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View view){
//                    popUp.dismiss();
//                }
//            });
//
//            popUp.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                @Override
//                public void onDismiss(DialogInterface dialog) {
//                    cls_05010000_Edicion edicion = new cls_05010000_Edicion();
//                    edicion.mostrarValoresDocumentoActual();
////                    super.getClass().getMethods()
////                    context.getClass().mostrarValoresDocumentoActual();
//
////                    if (mIsSettingsDirty)
////                        refreshRecyclerView();
//                }
//            });
//
//            popUp.show();
//        }catch(Exception ex){
//            Funciones.mostrarError(context,ex);
//        }
//    }
  public static void abrirActividadCambiarClave() {
  }

//    public static void abrirActividad(Context c, Class<?> clase,ConfiguracionLocal objConfLocal, Rex objRex) {
//            Intent nuevaActividad = new Intent(c,clase);
//            nuevaActividad.putExtra("ConfiguracionLocal",objConfLocal);
//            nuevaActividad.putParcelableArrayListExtra("objRex",objRex);
//            c.startActivity(nuevaActividad);
//    }

  public static void abrirActividad(Context c, Class<?> clase, ConfiguracionLocal
      objConfLocal, String s_IdRex) {
    Intent nuevaActividad = new Intent(c, clase);
    nuevaActividad.putExtra("ConfiguracionLocal", objConfLocal);
    nuevaActividad.putExtra("s_IdRex", s_IdRex);
    c.startActivity(nuevaActividad);
  }

  public static void notificar(Context context, String mensaje) {

//        // Create layout inflator object to inflate toast.xml file
//        LayoutInflater inflater = getLayoutInflater();
//
//        // Call toast.xml file for toast layout
//        View toastView = inflater.inflate(R.layout.toast, null);
//
//        Toast toastt = new Toast(context);
//        toastView.setView(toast);
//
//        // Set layout to toast
//        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
//                0, 0);
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast.show();
    ////////////////
//        Toast toast = Toast.makeText(context, mensaje, Toast.LENGTH_LONG);
//        View view = toast.getView();
//
//        //To change the Background of Toast
////        view.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.verdeClaro, null));
//        view.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.bg_notificacion,null));
//        TextView text = (TextView) view.findViewById(android.R.id.message);
//
//        //Shadow of the Of the Text Color
////        text.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
//        text.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.blanco, null));
//        text.setTextSize(18);
//        Typeface typeface = ResourcesCompat.getFont(context, R.font.auxiliar);
//        text.setTypeface(typeface);
//        toast.show();
////////////////////////////////////////////////////
//
//        Toast t = Toast.makeText(context, mensaje, Toast.LENGTH_LONG);
//        if (t.getView() == null) {
//            int layoutRes = context.getResources().getIdentifier("transient_notification", "layout", "android");
//            int tvRes = context.getResources().getIdentifier("message", "id", "android");
//            View layout = LayoutInflater.from(context).inflate(layoutRes, null);
//            layout.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.bg_notificacion,null));
//            TextView textView = layout.findViewById(tvRes);
//            textView.setGravity(Gravity.LEFT);
//            textView.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.blanco, null));
//            textView.setTextSize(18);
//            Typeface typeface = ResourcesCompat.getFont(context, R.font.auxiliar);
//            textView.setTypeface(typeface);
//            textView.setText(mensaje);
//            t.setView(layout);
//        }
//        t.show();
    //-----------------------------------------
//        Typeface fuenteToast = ResourcesCompat.getFont(context, R.font.auxiliar);
//        Toast toast = Toast.makeText(context, mensaje, Toast.LENGTH_LONG);
//        View view; // = toast.getView();
//        TextView text;
//        if (toast.getView() == null) {
//            int layoutRes = context.getResources().getIdentifier("transient_notification", "layout", "android");
////            int tvRes = context.getResources().getIdentifier("message", "id", "android");
//            view = LayoutInflater.from(context).inflate(layoutRes, null);
//
//        }else{
//            view = toast.getView();
//
//        }
//        if (toast.getView() == null) {
//            int layoutRes = context.getResources().getIdentifier("transient_notification", "layout", "android");
//            int tvRes = context.getResources().getIdentifier("message", "id", "android");
//            text = view.findViewById(tvRes);
//        }else {
//            text = (TextView) view.findViewById(android.R.id.message);
//        }
//        view.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.bg_notificacion,null));
//        //TextView text = (TextView) view.findViewById(android.R.id.message);
//        text.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.blanco, null));
//        text.setTextSize(18);
//        text.setTypeface(fuenteToast);
////        if (toast.getView() == null) {
////            int layoutRes = context.getResources().getIdentifier("transient_notification", "layout", "android");
////            int tvRes = context.getResources().getIdentifier("message", "id", "android");
////            View layout = LayoutInflater.from(context).inflate(layoutRes, null);
////            layout.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.bg_notificacion,null));
////            TextView textView = layout.findViewById(tvRes);
////            textView.setGravity(Gravity.LEFT);
////            textView.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.blanco, null));
////            textView.setTextSize(18);
////            textView.setTypeface(fuenteToast);
////            textView.setText(mensaje);
////            toast.setView(layout);
////        }
//        toast.show();

//         SI FUNCIONA:
    Toast t = Toast.makeText(context, mensaje, Toast.LENGTH_LONG);

    if (t.getView() == null) { //ESTE TOAST ES PARA LOS EQUIPOS XIAOMI, POR ALGUNA RAZON NO LEVANTAN EL MISMO CODIGO
      int layoutRes = context.getResources().getIdentifier("transient_notification", "layout", "android");
//            int layoutRes = context.getResources().getIdentifier(t.getView().getAccessibilityClassName().toString(), "layout", "android");
      int tvRes = context.getResources().getIdentifier("message", "id", "android");
      View layout = LayoutInflater.from(context).inflate(layoutRes, null);
      layout.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.bg_notificacion, null));
      TextView textView = layout.findViewById(tvRes);
      //layout.set
      //layout.setLayoutParams(new LinearLayout.LayoutParams(10, LinearLayout.LayoutParams.WRAP_CONTENT));
      textView.setGravity(Gravity.START);
      //textView.setPadding(15,-5,15,-5);
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
      //params.setMargins(5,-5,5,-10);
      params.setMargins(20, 0, 20, 0);
      textView.setLayoutParams(params);
      textView.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.blanco, null));
      textView.setTextSize(16);
      Typeface typeface = ResourcesCompat.getFont(context, R.font.monoespaciada_principal);
      textView.setTypeface(typeface);
      textView.setText(mensaje);
      textView.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
      t.setView(layout);
    } else {
      View view = t.getView();
      view.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.bg_notificacion, null));
      TextView text = (TextView) view.findViewById(android.R.id.message);
      text.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.blanco, null));
      text.setTextSize(18);
      Typeface typeface = ResourcesCompat.getFont(context, R.font.monoespaciada_principal);
      text.setTypeface(typeface);
      //text.setPadding(15,-5,15,-5);
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
      params.setMargins(20, 10, 20, 10);
      text.setLayoutParams(params);
      text.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
    }
    t.show();

//        Toast.makeText(context,mensaje,Toast.LENGTH_LONG).show();
  }

  public static String malograrFecha(String fecha) {
    if (fecha != null && fecha.length() == 10) {
      String anio, mes, dia;
      anio = fecha.substring(0, 4);
      mes = fecha.substring(5, 7);
      dia = fecha.substring(8, 10);
      return dia + "/" + mes + '/' + anio;
    } else {
      return "99/99/9999";
    }
  }

  public static String arreglarFecha(String fecha) {
    if (fecha != null && fecha.length() == 10) {
      String anio, mes, dia;
      dia = fecha.substring(0, 2);
      mes = fecha.substring(3, 5);
      anio = fecha.substring(6, 10);
      return anio + "-" + mes + '-' + dia;
    } else {
      return "9999-99-99";
    }
  }
}
