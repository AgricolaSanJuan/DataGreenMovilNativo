package com.example.datagreenmovil.ui.SettingsLocal;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.datagreenmovil.R;
import com.example.datagreenmovil.Sync.SyncDBSQLToSQLite;
import com.example.datagreenmovil.databinding.FragmentSettingsLocalBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
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
    Bundle args;
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

        if (getActivity().getIntent().getExtras() != null) {
            objConfLocal = (ConfiguracionLocal) getActivity().getIntent().getSerializableExtra("ConfiguracionLocal");
        }

        Log.i("LOCAL", objConfLocal.get("RED_HOST"));
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
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("objConfLocal", ctx.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                objSqlite = new ConexionSqlite(ctx, objConfLocal);
                editor.putString("EQUIPO_CONFIGURADO", "TRUE").apply();
                objConfLocal.set("EQUIPO_CONFIGURADO", "TRUE");
                try {
                    editor.putString("RED_HOST",objConfLocal.get("RED_HOST")).apply();
                    editor.putString("RED_INSTANCIA",objConfLocal.get("RED_INSTANCIA")).apply();
                    editor.putString("RED_NOMBRE_DB",objConfLocal.get("RED_NOMBRE_DB")).apply();
                    editor.putString("RED_USUARIO",objConfLocal.get("RED_USUARIO")).apply();
                    editor.putString("RED_PASSWORD",objConfLocal.get("RED_PASSWORD")).apply();
                    editor.putString("RED_PUERTO_CONEXION",objConfLocal.get("RED_PUERTO_CONEXION")).apply();
                    editor.putString("RED_IMEI",objConfLocal.get("IMEI")).apply();
                    editor.putString("RED_MAC",objConfLocal.get("MAC")).apply();
                    editor.putString("NRO_TELEFONICO",objConfLocal.get("NRO_TELEFONICO")).apply();
                    editor.putString("PROPIETARIO",objConfLocal.get("PROPIETARIO")).apply();

                    objSqlite.guardarConfiguracionLocal(objConfLocal);
                    NavController navController = Navigation.findNavController(requireView());
                    navController.navigate(R.id.nav_settings_sync);
                    Swal.info(ctx, "Vamos allá!", "Ahora debes sincronizar la información.",5000);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
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

        SharedPreferences sharedPreferences = ctx.getSharedPreferences("objConfLocal", ctx.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
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

            // MIGRAR PROGRESIVAMENTE A SHARED PREFERENCES
            editor.clear();
            editor.putString("RED_HOST", etxHost.getText().toString()).apply();
            editor.putString("RED_INSTANCIA", etxInstancia.getText().toString()).apply();
            editor.putString("RED_NOMBRE_DB", etxNombreBD.getText().toString()).apply();
            editor.putString("RED_PUERTO_CONEXION", etxPuerto.getText().toString()).apply();
            editor.putString("RED_USUARIO", etxUsuario.getText().toString()).apply();
            editor.putString("RED_PASSWORD", etx_Password.getText().toString()).apply();
            editor.putString("ID_EMPRESA", "01").apply();
            editor.putString("IMEI", etxImei.getText().toString()).apply();
            editor.putString("MAC", etxMac.getText().toString()).apply();
            editor.putString("NRO_TELEFONICO", etxNroTelefono.getText().toString()).apply();
            editor.putString("PROPIETARIO", etxPropietario.getText().toString()).apply();

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

    public void sincronizarBD() throws Exception {
        // Lanzar la tarea en segundo plano

        Executor executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {

                clAux = new ConfiguracionLocal();
                objConfLocal = clAux;
                objSql = new ConexionBD(objConfLocal);
                pbSync.post(() -> pbSync.setVisibility(View.VISIBLE));  // Mostrar ProgressBar en el hilo principal

                actualizarValoresConfiguracionAuxiliar(ctx);

                pbSync.post(() -> pbSync.setProgress(5));
//                Cursor c;
//                c = objSqlite.doItBaby(objSqlite.obtQuery("EXISTE DATA PENDIENTE"), null, "READ");
//                c.moveToFirst();
//                if (objSqlite.existeConfiguracionLocal() && c.getString(0).equals("1")) {
//                    Funciones.notificar(ctx,"Existe data pendiente de transferir. Imposible sincronizar.");
//                }else{
//                objSqlite.close();
//                }
                pbSync.post(() -> pbSync.setProgress(15));
                ctx.deleteDatabase("DataGreenMovil.db");
                objSqlite = new ConexionSqlite(ctx, objConfLocal);
                objQuerys = new Querys(objSql.obtenerQuerys());
                objSqlite.crearTablas(objQuerys);

                probarNuevaConexion();

                SyncDBSQLToSQLite syncDBSQLToSQLite = new SyncDBSQLToSQLite();
                getActivity().runOnUiThread(()->{
                    try {
                        Log.i("ObjConfLocal", objConfLocal.toString());
                        syncDBSQLToSQLite.sincronizar(ctx, objConfLocal, "mst_QuerysSqlite", "mst_QuerysSqlite");
                        syncDBSQLToSQLite.sincronizar(ctx, objConfLocal, "mst_OpcionesConfiguracion", "mst_OpcionesConfiguracion");
                        syncDBSQLToSQLite.sincronizar(ctx, objConfLocal,"trx_ConfiguracionesDispositivosMoviles", "trx_ConfiguracionesDispositivosMoviles");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                });


                pbSync.post(() -> pbSync.setProgress(30));
//                descargarData();
                pbSync.post(() -> pbSync.setProgress(90));
                if (clAux.get("RED_CONFIGURADA").equals("TRUE")) {
                    try {
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
                                Toast.makeText(ctx, "REGISTRADO", Toast.LENGTH_SHORT).show();
                                mostrarValoresDocumentoActual();
                            }
                        }catch (Exception e){
                            Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                pbSync.post(() -> pbSync.setProgress(90));

                pbSync.post(() -> pbSync.setProgress(100));
            } catch (Exception e) {
                Log.d("ERROR", e.toString());
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