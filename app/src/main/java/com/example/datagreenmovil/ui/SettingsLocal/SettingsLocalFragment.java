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
import com.example.datagreenmovil.DataGreenApp;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.Querys;
import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.Logica.Swal;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.datagreenmovil.R;
import com.example.datagreenmovil.SettingsActivity;
import com.example.datagreenmovil.Sync.SyncDBSQLToSQLite;
import com.example.datagreenmovil.TareosActivity;
import com.example.datagreenmovil.databinding.FragmentSettingsLocalBinding;
import com.google.android.material.internal.NavigationMenuItemView;

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
    EditText etxHost, etxApiServer, etxInstancia, etxNombreBD, etxPuerto, etxUsuario, etx_Password, etxImei, etxMac, etxNroTelefono, etxPropietario;
    TextView txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador, txv_PushTituloVentana, txv_PushRed, txv_NombreApp;
    ProgressBar pbSync;
    Button btnProbarConexion, btnGenerarBD, btnGuardar;
    Integer touchCounter = 0;
    Integer flingCounter = 0;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private GestureDetector gestureDetector;
    private FragmentSettingsLocalBinding binding;
    public Context context;

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
        etxApiServer = binding.c002EtxApiServerV;
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
                objSqlite = new ConexionSqlite(context, DataGreenApp.DB_VERSION());
                objConfLocal = DataGreenApp.getConfiguracionLocal();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        if (getActivity().getIntent().getExtras() != null) {
            objConfLocal = DataGreenApp.getConfiguracionLocal();
        }
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objConfLocal = DataGreenApp.getConfiguracionLocal();
                editor.putString("EQUIPO_CONFIGURADO", "TRUE").apply();
//                objConfLocal.set("EQUIPO_CONFIGURADO", "TRUE");
                try {
                    editor.putString("RED_HOST",binding.c002EtxHostV.getText().toString()).apply();
                    editor.putString("API_SERVER",binding.c002EtxApiServerV.getText().toString()).apply();
                    editor.putString("RED_INSTANCIA",binding.c002EtxInstanciaV.getText().toString()).apply();
                    editor.putString("RED_NOMBRE_DB",binding.c002EtxNombreBDV.getText().toString()).apply();
                    editor.putString("RED_USUARIO",binding.c002EtxUsuarioV.getText().toString()).apply();
                    editor.putString("RED_PASSWORD",binding.c002EtxPasswordV.getText().toString()).apply();
                    editor.putString("RED_PUERTO_CONEXION",binding.c002EtxPuertoV.getText().toString()).apply();
                    editor.putString("RED_IMEI",binding.c002EtxImeiV.getText().toString()).apply();
                    editor.putString("ID_EMPRESA", "01").apply();
                    editor.putString("RED_MAC",binding.c002EtxMacV.getText().toString()).apply();
                    editor.putString("NRO_TELEFONICO",binding.c002EtxNroTelefonoV.getText().toString()).apply();
                    editor.putString("PROPIETARIO",binding.c002EtxPropietarioV.getText().toString()).apply();

                    objSqlite.guardarConfiguracionLocal(objConfLocal);
                    NavController navController = Navigation.findNavController(requireView());
                    navController.navigate(R.id.nav_settings_sync);
                    Swal.info(context, "Vamos allá!", "Ahora debes sincronizar la información.",5000);
                } catch (Exception e) {
//                    throw new RuntimeException(e);
                    Swal.error(context, "Oops!", "Error al guardar la información, reintente.", 5000);
                }
            }
        });

        txv_PushVersionApp.setOnClickListener(view -> Funciones.popUpStatusVersiones(context));

        txv_PushVersionDataBase.setOnClickListener(view -> Funciones.popUpStatusVersiones(context));

//        GUARDAR EL API SERVER EN SHARED PREFERENCES
        binding.btnConnectApiServer.setOnClickListener(view -> editor.putString("API_SERVER", binding.c002EtxApiServerV.getText().toString()).apply());

        settingsScroll.setOnTouchListener(this::onTouch);

        return root;
    }

    @Override
    public void onAttach(@NonNull Context ctxOrigin) {
        super.onAttach(context);
        context = ctxOrigin;
        clAux = new ConfiguracionLocal();
        objConfLocal = clAux;
        objSql = new ConexionBD(context);
        sharedPreferences = ctxOrigin.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

//    FUNCIONES DE CONFIGURACION LOCAL

    public AsyncTask<Void, Void, String> probarConexion() {

        actualizarValoresConfiguracionAuxiliar(context);
        Boolean statusConexion = probarNuevaConexion();
        if (statusConexion == true) {
            Swal.success(context, "Exito!", "Conexion realizada con éxito!", 1500);
        } else {
            Swal.error(context, "Oops!", "Algo falló en la conexion, comunicate con soporte técnico.", 2500);
        }
        return null;
    }

    public void actualizarValoresConfiguracionAuxiliar(Context ctx) {

//        String redHost, String redInstancia, String redNombreDB,
//                String redPuertoConexion, String redUsuario, String redPassword, String IdEmpresa,
//                String imei, String mac, String nroTelefono, String propietario
        try {
            clAux.set("RED_HOST", etxHost.getText().toString());
            clAux.set("API_SERVER", etxApiServer.getText().toString());
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
            editor.putString("RED_HOST", etxHost.getText().toString()).apply();
            editor.putString("API_SERVER", etxApiServer.getText().toString()).apply();
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
            Swal.error(ctx, "Oops!", ex.toString(), 3000);
//            Funciones.mostrarError(ctx, ex);
        }
    }

    private Boolean probarNuevaConexion() {
        Boolean status = false;
        try {
            cnAux = new ConexionBD(context);

            if (cnAux.hayConexion()) {
                clAux.set("RED_CONFIGURADA", "TRUE");
                clAux.set("ESTADO_RED", "ONLINE");
//                migrar progresivamente a sharedPreferences
                editor.putString("RED_CONFIGURADA","TRUE").apply();
                editor.putString("ESTADO_RED","ONLINE").apply();
                objConfLocal = clAux;
                objSql = cnAux;
                status = true;
            } else {
                clAux.set("RED_CONFIGURADA", "FALSE");
                clAux.set("ESTADO_RED", "OFFLINE");
//                migrar progresivamente a sharedPreferences
                editor.putString("RED_CONFIGURADA","FALSE").apply();
                editor.putString("ESTADO_RED","OFFLINE").apply();
                editor.apply();
                status = false;
            }
        } catch (Exception ex) {

            Funciones.mostrarError(context, ex);
        }
        return status;

    }

    private void mostrarValoresDocumentoActual() {
        etxHost.setText(sharedPreferences.getString("RED_HOST","!RED_HOST"));
        etxApiServer.setText(sharedPreferences.getString("API_SERVER","!API_SERVER"));
        etxInstancia.setText(sharedPreferences.getString("RED_INSTANCIA", "!RED_INSTANCIAA"));
        etxNombreBD.setText(sharedPreferences.getString("RED_NOMBRE_DB","!RED_NOMBRE_DB"));
        etxUsuario.setText(sharedPreferences.getString("RED_USUARIO","!RED_USUARIO"));
        etx_Password.setText(sharedPreferences.getString("RED_PASSWORD","!RED_PASSWORD"));
        etxPuerto.setText(sharedPreferences.getString("RED_PUERTO_CONEXION","!RED_PUERTO_CONEXION"));
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
        etxNroTelefono.setText(sharedPreferences.getString("NRO_TELEFONICO","!NRO_TELEFONICO"));
        etxPropietario.setText(sharedPreferences.getString("PROPIETARIO","!PROPIETARIO"));
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
                etxApiServer.setText("192.168.30.94:8080");
                etxInstancia.setText("");
                etxNombreBD.setText("DataGreenMovil");
                etxUsuario.setText("sa");
                etx_Password.setText("A20200211sj");
                touchCounter = 0;
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            flingCounter += 1;

            if(flingCounter == 2){
                SettingsActivity settingsActivity = (SettingsActivity) getActivity();
                if(settingsActivity.obtenerDrawer() != null){
                    DrawerLayout dl = settingsActivity.obtenerDrawer();
//                    NavigationMenuItemView syncBtn = dl.findViewById(R.id.nav_item_sync);
//                    syncBtn.setVisibility(View.INVISIBLE);
                    dl.openDrawer(GravityCompat.START);
                }
                flingCounter = 0;
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    private String obtenerIMEI() {
        if (Build.VERSION.SDK_INT >= 29) {
            try {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String deviceID = null;
                int readIMEI = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
                if (deviceID == null) {
                    if (readIMEI != PackageManager.PERMISSION_GRANTED) {
                        return deviceID.toUpperCase();
                    }
                    deviceID = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                }
                return deviceID.toUpperCase();
            } catch (Exception ex) {
                throw ex;
            }
        } else {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String r = telephonyManager.getDeviceId();
                return r != null ? r.toUpperCase() : "";
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
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
                objSql = new ConexionBD(context);
                pbSync.post(() -> pbSync.setVisibility(View.VISIBLE));  // Mostrar ProgressBar en el hilo principal

                getActivity().runOnUiThread(()->{
                    actualizarValoresConfiguracionAuxiliar(context);
                    pbSync.post(() -> pbSync.setProgress(15));
                });

//                Cursor c;
//                c = objSqlite.doItBaby(objSqlite.obtQuery("EXISTE DATA PENDIENTE"), null, "READ");
//                c.moveToFirst();
//                if (objSqlite.existeConfiguracionLocal() && c.getString(0).equals("1")) {
//                    Funciones.notificar(ctx,"Existe data pendiente de transferir. Imposible sincronizar.");
//                }else{
//                objSqlite.close();
//                }
                context.deleteDatabase("DataGreenMovil.db");
                objSqlite = new ConexionSqlite(context, DataGreenApp.DB_VERSION());
                objQuerys = new Querys(objSql.obtenerQuerys());
                objSqlite.crearTablas(objQuerys);

                probarNuevaConexion();

                SyncDBSQLToSQLite syncDBSQLToSQLite = new SyncDBSQLToSQLite();
                getActivity().runOnUiThread(()->{
                    try {
                        Log.i("ObjConfLocal", objConfLocal.toString());
                        syncDBSQLToSQLite.sincronizar(context, objConfLocal, "mst_QuerysSqlite", "mst_QuerysSqlite");
                        syncDBSQLToSQLite.sincronizar(context, objConfLocal, "mst_OpcionesConfiguracion", "mst_OpcionesConfiguracion");
                        syncDBSQLToSQLite.sincronizar(context, objConfLocal,"trx_ConfiguracionesDispositivosMoviles", "trx_ConfiguracionesDispositivosMoviles");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                });


                pbSync.post(() -> pbSync.setProgress(30));
//                descargarData();
                pbSync.post(() -> pbSync.setProgress(90));
                if (sharedPreferences.getString("RED_CONFIGURADA","FALSE").equals("TRUE")) {
                    try {
                        if (registrarDispositivo()) {
                            objConfLocal = clAux;
                                objSql = cnAux;
                                getActivity().runOnUiThread(() -> {
                                    Funciones.mostrarEstatusGeneral(context,
                                            objConfLocal,
                                            txv_PushTituloVentana,
                                            txv_PushRed,
                                            txv_NombreApp,
                                            txv_PushVersionApp,
                                            txv_PushVersionDataBase,
                                            txv_PushIdentificador
                                    );
                                    Toast.makeText(context, "REGISTRADO", Toast.LENGTH_SHORT).show();
                                    mostrarValoresDocumentoActual();
                                });
                            }
                        }catch (Exception e){
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
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
            editor.putString("ID_DISPOSITIVO",cnAux.registrarEquipo(objConfLocal)).apply();
            Log.i("ENTRAO","ASDASD");
            //clAux.actualizarConfiguraciones(cnAux.obtenerConfiguracionesDispositivoMovil());
            return true;
        } catch (Exception ex) {
            Funciones.mostrarError(context, ex);
            return false;
        }
    }
}