package com.example.datagreenmovil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.Querys;
import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.Logica.Swal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class cls_02000000_Configuracion extends AppCompatActivity implements View.OnTouchListener {
    //@Jota:2023-05-27 -> INICIO DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    ConexionSqlite objSqlite;
    ConexionBD objSql;
    ConfiguracionLocal objConfLocal;
    TextView txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador;
    Dialog dlg_PopUp;
    //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
    //...

    int flagR = 0;
    ConexionBD cnAux;
    ConfiguracionLocal clAux;
    Querys objQuerys;
    private EditText etxHost;
    private EditText etxInstancia;
    private EditText etxNombreBD;
    private EditText etxUsuario;
    private EditText etx_Password;
    private EditText etxPuerto;
    private EditText etxImei;
    private EditText etxMac;
    private EditText etxNroTelefono;
    private EditText etxPropietario;
    private GestureDetector gestureDetector;
    private ProgressBar pbSync;
    private int touchCounter;
    private Context ctx;
    private EditText txtPasswordUnlock;
    public Boolean mostrarBotones = false;

    private LinearLayout lly_main_config_buttons;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector = new GestureDetector(this, new GestureListener());
        setContentView(R.layout.v_02000000_configuracion_002);
        ctx = this;
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //@Jota:2023-05-27 -> INICIO DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES

        View view = findViewById(R.id.settingsScroll); // Reemplaza con la ID de tu vista
        view.setOnTouchListener(this);

        if (getIntent().getExtras() != null) {
            objConfLocal = (ConfiguracionLocal) getIntent().getSerializableExtra("ConfiguracionLocal");
        }
        objSql = new ConexionBD(this);
        objSqlite = new ConexionSqlite(this, DataGreenApp.DB_VERSION());
//        objConfLocal.set("ULTIMA_ACTIVIDAD", "Configuracion");


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
        mostrarValoresDocumentoActual();
    }

    @SuppressLint("ResourceAsColor")
    public void sincronizar(View view) throws Exception {
        // Lanzar la tarea en segundo plano

        Executor executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {
                pbSync.post(() -> pbSync.setVisibility(View.VISIBLE));  // Mostrar ProgressBar en el hilo principal

                actualizarValoresDeConfiguracionAuxiliar();
                Boolean resultConexion = probarNuevaConexion();

                pbSync.post(() -> pbSync.setProgress(5));
                Cursor c = null;

                if (objSqlite.existeConfiguracionLocal() && c.getString(0).equals("1")) {
                    c = objSqlite.doItBaby(objSqlite.obtQuery("EXISTE DATA PENDIENTE"), null, "READ");
                    c.moveToFirst();
                }
                objSqlite.close();
                pbSync.post(() -> pbSync.setProgress(15));
                this.deleteDatabase("DataGreenMovil.db");
                objSqlite = new ConexionSqlite(getBaseContext(), DataGreenApp.DB_VERSION());
                objQuerys = new Querys(objSql.obtenerQuerys());
                objSqlite.crearTablas(objQuerys);
                pbSync.post(() -> pbSync.setProgress(30));
//                descargarData();
                pbSync.post(() -> pbSync.setProgress(90));
                if (objSqlite.existeConfiguracionLocal() && c.getString(0).equals("1")) {
                    runOnUiThread(() -> Funciones.notificar(getBaseContext(), "Existe data pendiente de transferir. Imposible sincronizar."));
                } else {
                    if (clAux.get("RED_CONFIGURADA").equals("TRUE")) {
                        if (registrarDispositivo()) {
                            objConfLocal = clAux;
                            objSql = cnAux;
                            runOnUiThread(() -> Funciones.mostrarEstatusGeneral(getBaseContext(),
                                    objConfLocal,
                                    txv_PushTituloVentana,
                                    txv_PushRed,
                                    txv_NombreApp,
                                    txv_PushVersionApp,
                                    txv_PushVersionDataBase,
                                    txv_PushIdentificador
                            ));
                            mostrarValoresDocumentoActual();
                        }
                    }
                }
                pbSync.post(() -> pbSync.setProgress(100));
            } catch (Exception e) {
                runOnUiThread(() -> Funciones.notificar(getBaseContext(), e.getMessage()));
                pbSync.post(() -> pbSync.setBackgroundColor(R.color.AuxiliarFuerte));
                pbSync.post(() -> pbSync.setProgress(100));
            } finally {
                pbSync.post(() -> pbSync.setProgress(0));
                pbSync.post(() -> pbSync.setVisibility(View.INVISIBLE));
            }
        });
    }

    //@Jota:2023-05-27 -> LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    private void setearControles() {
//        builderDialogoCerrarSesion= Funciones.setearAlertDialogParaCerrarSesion_(objConfLocal,objSqlite,this);

        //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
        //...
    }

    private void referenciarControles() {
        txv_PushTituloVentana = findViewById(R.id.c002_txv_PushTituloVentana_v);
        txv_PushRed = findViewById(R.id.c002_txv_PushRed_v);
        txv_NombreApp = findViewById(R.id.c002_txv_NombreApp_v);
        txv_PushVersionApp = findViewById(R.id.c002_txv_PushVersionApp_v);
        txv_PushVersionDataBase = findViewById(R.id.c002_txv_PushVersionDataBase_v);
        txv_PushIdentificador = findViewById(R.id.c002_txv_PushIdentificador_v);

        //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
        //...
        etxHost = findViewById(R.id.c002_etxHost_v);
        etxInstancia = findViewById(R.id.c002_etxInstancia_v);
        etxNombreBD = findViewById(R.id.c002_etxNombreBD_v);
        etxUsuario = findViewById(R.id.c002_etxUsuario_v);
        etx_Password = findViewById(R.id.c002_etx_Password_v);
        etxPuerto = findViewById(R.id.c002_etxPuerto_v);
        etxImei = findViewById(R.id.c002_etxImei_v);
        etxMac = findViewById(R.id.c002_etxMac_v);
        etxNroTelefono = findViewById(R.id.c002_etxNroTelefono_v);
        etxPropietario = findViewById(R.id.c002_etxPropietario_v);

//    UI
        pbSync = findViewById(R.id.progress_bar_horizontal);
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
                assert dlg_PopUp != null;
                dlg_PopUp.show();
            } else if (idControlClickeado == R.id.opc_00000001_cerrar_sesion_v) {
                dlg_PopUp = Funciones.obtenerDialogParaCerrarSesion(this, objConfLocal, objSqlite, this);
                assert dlg_PopUp != null;
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
            if (idControlClickeado == R.id.c002_txv_PushTituloVentana_v) {
                Funciones.popUpTablasPendientesDeEnviar(this);
            } else if (idControlClickeado == R.id.c002_txv_PushRed_v) {
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
            } else if (idControlClickeado == R.id.c002_txv_PushVersionApp_v || idControlClickeado == R.id.c002_txv_PushVersionDataBase_v) {
                Funciones.popUpStatusVersiones(this);
            } else if (idControlClickeado == R.id.c002_txv_PushIdentificador_v) {
                mostrarMenuUsuario(this.txv_PushIdentificador);
            }
            //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
            //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
            //...
            else if (idControlClickeado == R.id.c002_btnProbarConexion_v) {
                actualizarValoresDeConfiguracionAuxiliar();
                Boolean statusConexion = probarNuevaConexion();
                if (statusConexion == true) {
                    Swal.success(ctx, "Exito!", "Conexion realizada con écito!", 1500);
                } else {
                    Swal.error(ctx, "Oops!", "Algo falló en la conexion, comunicate con soporte técnico.", 2500);
                }
                Toast.makeText(this, statusConexion.toString(), Toast.LENGTH_SHORT).show();
            } else if (idControlClickeado == R.id.c002_btnRegistrar_v) {
                if (clAux.get("RED_CONFIGURADA").equals("TRUE")) {
                    if (registrarDispositivo()) {
                        objConfLocal = clAux;
                        objSql = cnAux;
                        Funciones.mostrarEstatusGeneral(this.getBaseContext(),
                                objConfLocal,
                                txv_PushTituloVentana,
                                txv_PushRed,
                                txv_NombreApp,
                                txv_PushVersionApp,
                                txv_PushVersionDataBase,
                                txv_PushIdentificador
                        );
                        mostrarValoresDocumentoActual();
                        Funciones.notificar(this, "Dispositivo Registrado Correctamente.");
                    }
                } else {
                    Funciones.notificar(this, "No se puede registrar el dispositivo con los valores proporcionados.");
                }
            } else if (idControlClickeado == R.id.c002_bntSincronizar_v) {
            } else if (idControlClickeado == R.id.c002_btnMas_v) {

                flagR++;
                if (flagR == 10) {
                    Funciones.notificar(this, "Estas a punto de borrar la base de datos.");
                }
                if (flagR >= 20) {
                    flagR = 0;
                    objSqlite.close();
                    this.deleteDatabase("DataGreenMovil.db");
                    objConfLocal = null;
                    clAux = null;
                    finish();
//                    Swal.info(this, "aea", "asdasd", 1500);

                }
//                if (flagR == 25) {
//                    objSqlite.close();
//                    this.deleteDatabase("DataGreenMovil.db");
//                    objSqlite = new ConexionSqlite(this, DataGreenApp.DB_VERSION());
////                    flagR = 0;
//                    objConfLocal.set("ID_DISPOSITIVO", "");
//                    objConfLocal.get("ID_DISPOSITIVO");
//                    Swal.warning(this, "Importante", "Base de datos borrada correctamente.", 1500);
//                }


            } else if (idControlClickeado == R.id.c002_btnGuardar_v) {
//                objConfLocal.set("EQUIPO_CONFIGURADO", "TRUE");
                objSqlite.guardarConfiguracionLocal(objConfLocal);
                Funciones.notificar(this, "Configuracion guardada correctamente.");
            } else throw new IllegalStateException("Click sin programacion: " + view.getId());
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
        }
    }

    //@Jota:
    //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
    //...


    private void mostrarValoresDocumentoActual() {
        etxHost.setText(objConfLocal.get("RED_HOST"));
        etxInstancia.setText(objConfLocal.get("RED_INSTANCIA"));
        etxNombreBD.setText(objConfLocal.get("RED_NOMBRE_DB"));
        etxUsuario.setText(objConfLocal.get("RED_USUARIO"));
        etx_Password.setText(objConfLocal.get("RED_PASSWORD"));
        etxPuerto.setText(objConfLocal.get("RED_PUERTO_CONEXION"));
        etxImei.setText(Funciones.obtenerIdUnico(this));
//        etxMac.setText(Funciones.obtenerMac());
//        if (etxMac.getText().length() == 0 && etxImei.getText().length() > 0) {
//            etxMac.setText(etxImei.getText());
//        } else if (etxMac.getText().length() > 0 && etxImei.getText().length() == 0) {
//            etxImei.setText(etxMac.getText());
//        }
        if (etxImei.getText().length() == 0) {
            etxImei.setEnabled(true);
        }
        if (etxMac.getText().length() == 0) {
            etxMac.setEnabled(true);
        }
        etxNroTelefono.setText(objConfLocal.get("NRO_TELEFONICO"));
        etxPropietario.setText(objConfLocal.get("PROPIETARIO"));
    }

    private boolean registrarDispositivo() {
        try {
            clAux.set("ID_DISPOSITIVO", cnAux.registrarEquipo(objConfLocal));
            return true;
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
            return false;
        }
    }

    private void actualizarValoresDeConfiguracionAuxiliar() {
        try {
            clAux = new ConfiguracionLocal();
            clAux.set("RED_HOST", etxHost.getText().toString());
            clAux.set("RED_INSTANCIA", etxInstancia.getText().toString());
            clAux.set("RED_NOMBRE_DB", etxNombreBD.getText().toString());
            clAux.set("RED_PUERTO_CONEXION", etxPuerto.getText().toString());
            clAux.set("RED_USUARIO", etxUsuario.getText().toString());
            clAux.set("RED_PASSWORD", etx_Password.getText().toString());
            clAux.set("ID_EMPRESA", "01");
            //clAux.set("IMEI",obtenerImei());
            clAux.set("IMEI", etxImei.getText().toString());
            //clAux.set("MAC",obtenerMac());
            clAux.set("MAC", etxMac.getText().toString());
            clAux.set("NRO_TELEFONICO", etxNroTelefono.getText().toString());
            clAux.set("PROPIETARIO", etxPropietario.getText().toString());
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
        }
    }

    private Boolean probarNuevaConexion() {
        Boolean status = false;
        try {
            cnAux = new ConexionBD(this);

            if (cnAux.hayConexion()) {
                clAux.set("RED_CONFIGURADA", "TRUE");
                clAux.set("ESTADO_RED", "ONLINE");
                objConfLocal = clAux;
                objSql = cnAux;
                status = true;
            } else {
                clAux.set("RED_CONFIGURADA", "FALSE");
                clAux.set("ESTADO_RED", "OFFLINE");

//        Swal.error(this,"Oops!","Imposible conectar con servidor.", 1500);

                status = false;
            }
        } catch (Exception ex) {

            Funciones.mostrarError(this, ex);
        }
        return status;

    }

    private void descargarData() throws Exception {
        String tablasSincronizadas = "Sincronizacion finalizada correctamente en:";
        try {
            objQuerys = new Querys(objSql.obtenerQuerys());
            HashMap<String, String> hmQuerys = objQuerys.obtQuerysParaDescarga("01"); //PENDIENTE OBTENER DINAMICAMENTE;
            Iterator<Map.Entry<String, String>> it = hmQuerys.entrySet().iterator();
            ResultSet rsAux;
            ResultSetMetaData m;
            String q;
            while (it.hasNext()) {
                Map.Entry<String, String> set = (Map.Entry<String, String>) it.next();
                tablasSincronizadas = tablasSincronizadas + "\n" + set.getKey();
                rsAux = objSql.doItBaby(set.getValue(), null);
                m = rsAux.getMetaData();
                while (rsAux.next()) {
                    q = "INSERT INTO " + set.getKey() + " VALUES(";
                    for (int i = 0; i < m.getColumnCount(); i++) {
                        if (i > 0) {
                            q = q + ",";
                        }
                        q = q + "'" + rsAux.getString(i + 1) + "'";
                    }
                    q = q + ")";
                    objSqlite.doItBaby(q, null, "INSERT");
                }
            }
        } catch (Exception ex) {
            runOnUiThread(() -> Funciones.mostrarError(this, ex));
        } finally {
            String finalTablasSincronizadas = tablasSincronizadas;
            runOnUiThread(() -> Funciones.notificar(this, finalTablasSincronizadas));
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Pasa el evento táctil al GestureDetector
        gestureDetector.onTouchEvent(event);
        return true;
    }

    public void desbloquearConfiguraciones(View view) {
        if(txtPasswordUnlock.getText().toString() == "114422"){
            mostrarControles(true);
        }else{
            mostrarControles(false);
        }
    }

    public void mostrarControles(Boolean mostrar){
        if(mostrar){
            lly_main_config_buttons.setVisibility(View.VISIBLE);
        }else{
            lly_main_config_buttons.setVisibility(View.INVISIBLE);
        }
    }
    // Define la clase GestureListener para manejar el gesto
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            touchCounter++;
            Log.i("Presionado", String.valueOf(touchCounter));
            if (touchCounter == 3) {
                etxHost.setText("192.168.30.99");
                etxInstancia.setText("");
                etxNombreBD.setText("DataGreenMovil");
                etxUsuario.setText("sa");
                etx_Password.setText("A20200211sj");
                touchCounter = 0;
            }
            return true;
        }

    }

    public void shareDatabase() {
        // Ruta en duro para el archivo DataGreenMovil.db
        String filePath = "/data/data/com.example.datagreenmovil/databases/DataGreenMovil.db";

        // Verifica si el archivo existe en la ruta en duro
        File sourceFile = new File(filePath);

        if (sourceFile.exists()) {
            // Ruta de destino (ubicación pública)
            File destFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "DataGreenMovil.db");

            try {
                // Copiar el archivo desde la ubicación privada a la ubicación pública
                copyFile(sourceFile, destFile);

                // Crear una URI para la ubicación pública del archivo
                Uri uri = Uri.fromFile(destFile);

                // Crea una instancia de DownloadManager
                DownloadManager downloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);

                // Crea una solicitud de descarga
                DownloadManager.Request request = new DownloadManager.Request(uri);

                // Configura el título y descripción de la descarga (opcional)
                request.setTitle("Descargando base de datos");
                request.setDescription("Descarga en progreso");

                // Permite que otras aplicaciones accedan al archivo descargado
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                // Inicia la descarga
                downloadManager.enqueue(request);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void copyFile(File sourceFile, File destFile) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;

        try {
            sourceChannel = new FileInputStream(sourceFile).getChannel();
            destChannel = new FileOutputStream(destFile).getChannel();
            sourceChannel.transferTo(0, sourceChannel.size(), destChannel);
        } finally {
            if (sourceChannel != null) {
                sourceChannel.close();
            }
            if (destChannel != null) {
                destChannel.close();
            }
        }
    }
}