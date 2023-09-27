package com.example.datagreenmovil.ui.SettingsLocal;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.Querys;
import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.Logica.Swal;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.datagreenmovil.R;
import com.example.datagreenmovil.Sync.SyncDBSQLToSQLite;
import com.example.datagreenmovil.databinding.FragmentSettingsLocalBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SettingsLocalFragment extends Fragment implements View.OnTouchListener {

    ConexionBD cnAux;
    ConfiguracionLocal clAux;
    ConfiguracionLocal objConfLocal;
    ConexionBD objSql;
    Querys objQuerys;
    EditText etxHost, etxInstancia, etxNombreBD, etxPuerto, etxUsuario, etx_Password, etxImei, etxMac, etxNroTelefono, etxPropietario;
    TextView txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador, txv_PushTituloVentana, txv_PushRed, txv_NombreApp;
    ProgressBar pbSync;
    Button btnProbarConexion, btnGenerarBD, btnGuardar;
    Integer touchCounter = 0;
    private GestureDetector gestureDetector;
    private FragmentSettingsLocalBinding binding;
    public Context ctx;

    ConexionSqlite objSqlite;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        SettingsLocalViewModel settingsLocalViewModel =
                new ViewModelProvider(this).get(SettingsLocalViewModel.class);

        binding = FragmentSettingsLocalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        View settingsScroll = binding.settingsScroll;

        gestureDetector = new GestureDetector(root.getContext(), new GestureListener());

        btnProbarConexion = binding.c002BtnProbarConexionV;
        btnGenerarBD = binding.c002BtnGenerarDBV;
        btnGuardar = binding.c002BtnGuardarV;

        pbSync = binding.progressBarHorizontal;

        etxHost = binding.c002EtxHostV;
        etxInstancia = binding.c002EtxInstanciaV;
        etxNombreBD = binding.c002EtxNombreBDV;
        etxPuerto = binding.c002EtxPuertoV;
        etxUsuario = binding.c002EtxUsuarioV;
        etx_Password = binding.c002EtxPasswordV;
        etxImei = binding.c002EtxImeiV;
        etxMac = binding.c002EtxMacV;
        etxNroTelefono = binding.c002EtxNroTelefonoV;
        etxPropietario = binding.c002EtxPropietarioV;

        txv_PushVersionApp = binding.c002TxvPushVersionAppV;
        txv_PushVersionDataBase = binding.c002TxvPushVersionDataBaseV;
        txv_PushIdentificador = binding.c002TxvPushIdentificadorV;
        txv_PushTituloVentana = binding.c002TxvPushTituloVentanaV;
        txv_PushRed = binding.c002TxvPushRedV;
        txv_NombreApp = binding.c002TxvNombreAppV;

        Funciones.mostrarEstatusGeneral(root.getContext(),
                objConfLocal,
                txv_PushTituloVentana,
                txv_PushRed,
                txv_NombreApp,
                txv_PushVersionApp,
                txv_PushVersionDataBase,
                txv_PushIdentificador
        );

        mostrarValoresDocumentoActual();
        btnProbarConexion.setOnClickListener(view -> {
                probarConexion();
        });

        btnGenerarBD.setOnClickListener(view -> {
            try {
                sincronizarBD();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objSqlite = new ConexionSqlite(ctx, objConfLocal);
                objConfLocal.set("EQUIPO_CONFIGURADO", "TRUE");
                try {
                    objSqlite.guardarConfiguracionLocal(objConfLocal);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                Funciones.notificar(ctx, "Configuracion guardada correctamente.");
            }
        });

        txv_PushVersionApp.setOnClickListener(view -> Funciones.popUpStatusVersiones(ctx));

        txv_PushVersionDataBase.setOnClickListener(view -> Funciones.popUpStatusVersiones(ctx));

        settingsScroll.setOnTouchListener(this::onTouch);
        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ctx = context;
        clAux = new ConfiguracionLocal();
        objConfLocal = clAux;
        objSql = new ConexionBD(objConfLocal);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

//    FUNCIONES DE CONFIGURACION LOCAL

    public AsyncTask<Void, Void, String> probarConexion() {

        actualizarValoresConfiguracionAuxiliar(ctx);
        Boolean statusConexion = probarNuevaConexion();
        if (statusConexion == true) {
            Swal.success(ctx, "Exito!", "Conexion realizada con éxito!", 1500);
        } else {
            Swal.error(ctx, "Oops!", "Algo falló en la conexion, comunicate con soporte técnico.", 2500);
        }
        return null;
    }

    public void actualizarValoresConfiguracionAuxiliar(Context ctx) {

//        String redHost, String redInstancia, String redNombreDB,
//                String redPuertoConexion, String redUsuario, String redPassword, String IdEmpresa,
//                String imei, String mac, String nroTelefono, String propietario
        try {
            clAux.set("RED_HOST", etxHost.getText().toString());
            clAux.set("RED_INSTANCIA", etxInstancia.getText().toString());
            clAux.set("RED_NOMBRE_DB", etxNombreBD.getText().toString());
            clAux.set("RED_PUERTO_CONEXION", etxPuerto.getText().toString());
            clAux.set("RED_USUARIO", etxUsuario.getText().toString());
            clAux.set("RED_PASSWORD", etx_Password.getText().toString());
            clAux.set("ID_EMPRESA", "01");
            clAux.set("IMEI", etxImei.getText().toString());
            clAux.set("MAC", etxMac.getText().toString());
            clAux.set("NRO_TELEFONICO", etxNroTelefono.getText().toString());
            clAux.set("PROPIETARIO", etxPropietario.getText().toString());
        } catch (Exception ex) {
            Swal.error(ctx, "Oops!", "ex", 3000);
//            Funciones.mostrarError(ctx, ex);
        }
    }

    private Boolean probarNuevaConexion() {
        Boolean status = false;
        try {
            cnAux = new ConexionBD(clAux);

            if (cnAux.hayConexion()) {
                clAux.set("RED_CONFIGURADA", "TRUE");
                clAux.set("ESTADO_RED", "ONLINE");
                objConfLocal = clAux;
                objSql = cnAux;
                status = true;
            } else {
                clAux.set("RED_CONFIGURADA", "FALSE");
                clAux.set("ESTADO_RED", "OFFLINE");

                status = false;
            }
        } catch (Exception ex) {

            Funciones.mostrarError(ctx, ex);
        }
        return status;

    }

    private void mostrarValoresDocumentoActual() {
        etxHost.setText(objConfLocal.get("RED_HOST"));
        etxInstancia.setText(objConfLocal.get("RED_INSTANCIA"));
        etxNombreBD.setText(objConfLocal.get("RED_NOMBRE_DB"));
        etxUsuario.setText(objConfLocal.get("RED_USUARIO"));
        etx_Password.setText(objConfLocal.get("RED_PASSWORD"));
        etxPuerto.setText(objConfLocal.get("RED_PUERTO_CONEXION"));
        etxImei.setText(obtenerIMEI().toString());
        etxMac.setText(obtenerMAC());
        if (etxMac.getText().length() == 0 && etxImei.getText().length() > 0) {
            etxMac.setText(etxImei.getText());
        } else if (etxMac.getText().length() > 0 && etxImei.getText().length() == 0) {
            etxImei.setText(etxMac.getText());
        }
        if (etxImei.getText().length() == 0) {
            etxImei.setEnabled(true);
        }
        if (etxMac.getText().length() == 0) {
            etxMac.setEnabled(true);
        }
        etxNroTelefono.setText(objConfLocal.get("NRO_TELEFONICO"));
        etxPropietario.setText(objConfLocal.get("PROPIETARIO"));
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Pasa el evento táctil al GestureDetector
        gestureDetector.onTouchEvent(event);
        return true;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            touchCounter++;
            Log.i("Presionado", String.valueOf(touchCounter));
            if (touchCounter == 3) {
                etxHost.setText("192.168.30.99");
                etxInstancia.setText("MSSQLSERVER17");
                etxNombreBD.setText("DataGreenMovil");
                etxUsuario.setText("sa");
                etx_Password.setText("A20200211sj");
                touchCounter = 0;
            }
            return true;
        }

    }

    private String obtenerIMEI() {
        if (Build.VERSION.SDK_INT >= 29) {
            try {
                TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
                String deviceID = null;
                int readIMEI = ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE);
                if (deviceID == null) {
                    if (readIMEI != PackageManager.PERMISSION_GRANTED) {
                        return deviceID.toUpperCase();
                    }
                    deviceID = android.provider.Settings.Secure.getString(ctx.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                }
                return deviceID.toUpperCase();
            } catch (Exception ex) {
                throw ex;
            }
        } else {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
                String r = telephonyManager.getDeviceId();
                return r != null ? r.toUpperCase() : "";
            } catch (Exception ex) {
                Toast.makeText(ctx, ex.getMessage(), Toast.LENGTH_SHORT).show();
                return null;
            }
        }
    }

    private String obtenerMAC() {
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return wifiInfo.getMacAddress();
                }
            }
        }
        return "MAC no disponible";
    }

    public void sincronizarBD() throws Exception {
        // Lanzar la tarea en segundo plano

        Executor executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {
                pbSync.post(() -> pbSync.setVisibility(View.VISIBLE));  // Mostrar ProgressBar en el hilo principal

                actualizarValoresConfiguracionAuxiliar(ctx);
                Boolean resultConexion = probarNuevaConexion();

                pbSync.post(() -> pbSync.setProgress(5));
                Cursor c = null;

                if (objSqlite.existeConfiguracionLocal() && c.getString(0).equals("1")) {
                    c = objSqlite.doItBaby(objSqlite.obtQuery("EXISTE DATA PENDIENTE"), null, "READ");
                    c.moveToFirst();
                }
                objSqlite.close();
                pbSync.post(() -> pbSync.setProgress(15));
//                ctx.deleteDatabase("DataGreenMovil.db");
                objSqlite = new ConexionSqlite(ctx, objConfLocal);
                objQuerys = new Querys(objSql.obtenerQuerys());
                objSqlite.crearTablas(objQuerys);

                pbSync.post(() -> pbSync.setProgress(30));
//                descargarData();
                pbSync.post(() -> pbSync.setProgress(90));
                if (objSqlite.existeConfiguracionLocal() && c.getString(0).equals("1")) {
                    getActivity().runOnUiThread(() -> Funciones.notificar(ctx, "Existe data pendiente de transferir. Imposible sincronizar."));
                } else {
                    if (clAux.get("RED_CONFIGURADA").equals("TRUE")) {
                        if (registrarDispositivo()) {
                            objConfLocal = clAux;
                            objSql = cnAux;
                            getActivity().runOnUiThread(() -> Funciones.mostrarEstatusGeneral(ctx,
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
                pbSync.post(() -> pbSync.setProgress(90));



                pbSync.post(() -> pbSync.setProgress(100));
            } catch (Exception e) {
                getActivity().runOnUiThread(() -> Funciones.notificar(ctx, e.getMessage()));
                pbSync.post(() -> pbSync.setProgress(100));
            } finally {
                pbSync.post(() -> pbSync.setProgress(0));
                pbSync.post(() -> pbSync.setVisibility(View.INVISIBLE));
            }
        });
    }

    private boolean registrarDispositivo() {
        try {
            clAux.set("ID_DISPOSITIVO", cnAux.registrarEquipo(objConfLocal));
            //clAux.actualizarConfiguraciones(cnAux.obtenerConfiguracionesDispositivoMovil());
            return true;
        } catch (Exception ex) {
            Funciones.mostrarError(ctx, ex);
            return false;
        }
    }
}