package com.example.datagreenmovil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.Querys;
import com.example.datagreenmovil.Logica.Funciones;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class cls_01000000_Commutador extends AppCompatActivity {
    ConexionSqlite objSqlite; //= new ConexionSqlite(this, DataGreenApp.DB_VERSION());
    ConfiguracionLocal objConfLocal; // = new ConfiguracionLocal();
    ConexionBD objSql; // = new ConexionBD(objConfLocal);
    Querys objQuerys;
    Dialog dlg_PopUp;
    SharedPreferences sharedPreferences;

    public static boolean tienePermisos(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_01000000_conmutador_001);
        sharedPreferences = getSharedPreferences("objConfLocal", MODE_PRIVATE);
        validarPermisosAndroid();
        //CONTINUAR AQUI: EL PROCESO ENTRA A TOKEN NO EXISTE;
        Context appContext = getApplicationContext();
        String nombreApp = appContext.getString(R.string.app_name);
        sharedPreferences.edit().putString("API_SERVER", "192.168.30.94:8080").apply();
        sharedPreferences.edit().putString("NOMBRE_APP", nombreApp).apply();

        try {

            objSqlite = new ConexionSqlite(this, DataGreenApp.DB_VERSION());
            objConfLocal = new ConfiguracionLocal(objSqlite.obtenerConfiguracionLocal());
//            AQUI SE SETEA LA VERSION DE SQLITE
            sharedPreferences.edit().putString("VERSION_DB_SQLITE", objSqlite.obtenerVersionSQLITE()).apply();
            sharedPreferences.edit().putString("VERSION_DB_DISPONIBLE", objSqlite.obtenerVersionSQLITE()).apply();
            //SE ESPERA PODER USAR objQuerys.Query("NOMBRE DE QUERY") -> RETORNA STRING
            objQuerys = new Querys(objSqlite.obtenerQuerys());
            //OBTENER VERSION APP
            String versionApp = BuildConfig.VERSION_NAME;
            sharedPreferences.edit().putString("VERSION_APP", versionApp).apply();

            if (check()) {
                Intent i;
                i = new Intent(this, cls_03000000_Login.class);
                int valor = Resources.getSystem().getDisplayMetrics().widthPixels;
//                objConfLocal.set("ANCHO_PANTALLA", String.valueOf(valor));
                sharedPreferences.edit().putString("ANCHO_PANTALLA", String.valueOf(valor)).apply();
                valor = Resources.getSystem().getDisplayMetrics().heightPixels;
//                objConfLocal.set("ALTO_PANTALLA", String.valueOf(valor));
                sharedPreferences.edit().putString("ALTO_PANTALLA", String.valueOf(valor)).apply();
                DataGreenApp.setConfiguracionLocal(objConfLocal);
                i.putExtra("ConfiguracionLocal", objConfLocal);
                startActivity(i);

                finish();
            } else {
                DataGreenApp.setConfiguracionLocal(objConfLocal);
                abrirConfiguraciones();
            }
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
            finish();
        }
    }

    public void abrirMenuModulos() {
        try {
            Intent intent = new Intent(this, cls_04000000_Modulos.class);
            intent.putExtra("ConfiguracionLocal", objConfLocal);
            startActivity(intent);
            finish();
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
        }
    }

    private void abrirUltimaActividad() {
        Toast.makeText(this, "UltimaActividadAbierta: ", Toast.LENGTH_LONG).show();
    }

    private boolean existeSesion() {
        return objConfLocal.get("ID_USUARIO_ACTUAL") != "!ID_USUARIO_ACTUAL";
    }

    private boolean check() {

        try {
            objSql = new ConexionBD(this);
            objSqlite = new ConexionSqlite(this, DataGreenApp.DB_VERSION());
            if (!objSqlite.existeBDLocal()) {
                return false;
            }
            if (!objSqlite.existenTablas()) {
                return false;
            }

            if (objSql.hayConexion()) {
                sharedPreferences.edit().putString("ESTADO_RED", "ONLINE").apply();
                procesoCargaOnline1();
                procesoCargaOnline2();
            } else {
                sharedPreferences.edit().putString("ESTADO_RED", "OFFLINE").apply();
                procesoCargaOffline1();
            }
            return true;
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
            return false;
        }
    }

    private void abrirConfiguraciones() {
        try {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra("ConfiguracionLocal", objConfLocal);
            startActivity(intent);
            finish();
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
        }
    }

    private void procesoCargaOnline1() {
        try {
            if (!objSqlite.equipoRegistrado()) {
//                objConfLocal.set("ID_DISPOSITIVO", objSql.registrarEquipo(objConfLocal));
                sharedPreferences.edit().putString("ID_DISPOSITIVO", objSql.registrarEquipo(objConfLocal)).apply();
            }
            objConfLocal = objSql.obtenerVersionesDisponibles(objConfLocal);
            if (objSql.hayActualizacionBDLocal(objConfLocal)) {
                actualizarBDLocal();
            }
            if (!objSqlite.existeDataLocal()) {
                descargarData();
                objSqlite.guardarConfiguracionLocal(objConfLocal);
            } else {
                if (objSql.hayActualizacionDataLocal(objConfLocal)) {
                    actualizarDataLocal();
                }
            }
            cargarQuerys();
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
        }
    }

    private void procesoCargaOnline2() throws Exception {
        try {
            if (!objSqlite.equipoHabilitado()) {
                finalizarAplicacion();
            }
            if (!objSql.horaActualizada()) {
                Funciones.notificar(this, "El dispositivo móvil tiene configurada una fecha u hora distinta al servidor, corregir.");
            }
            if (objSql.hayActualizacionSoftware(objConfLocal)) {
                actualizarApp();
            }
            if (objSqlite.existeDataPendiente()) {
                eviarDataPendiente();
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void cargarQuerys() {

    }

    private void actualizarDataLocal() {
    }

    private void descargarData() throws Exception {
        try {
            Funciones.notificar(this, "Descargando Data.");
            objQuerys = new Querys(objSql.obtenerQuerys());
            HashMap<String, String> hmQuerys = objQuerys.obtQuerysParaDescarga("01"); //PENDIENTE OBTENER DINAMICAMENTE;
            Iterator<Map.Entry<String, String>> it = hmQuerys.entrySet().iterator();
            ResultSet rsAux;
            ResultSetMetaData m;
            String q;
            while (it.hasNext()) {
                Map.Entry<String, String> set = it.next();
                Toast.makeText(this, set.getKey(), Toast.LENGTH_LONG).show();
                rsAux = objSql.doItBaby(set.getValue(), null);
                m = rsAux.getMetaData();
                //q = "INSERT INTO " + rsQuerys.getString("TablaObjetivo") + " VALUES(";
                while (rsAux.next()) {
                    q = "INSERT INTO " + set.getKey() + " VALUES(";
                    for (Integer i = 0; i < m.getColumnCount(); i++) {
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
            Funciones.mostrarError(this, ex);
        }
    }

    private void actualizarBDLocal() {
        //DESTRUIR BD LOCAL
        //CONSTRUIR BD LOCAL
        if (!objSqlite.existeBDLocal()) {
            objSqlite.destruirBaseSqlite();
        }
    }

    private void eviarDataPendiente() {
    }

    private void actualizarApp() {
    }

    private void finalizarAplicacion() {
        Funciones.notificar(this, "FINnnn.");
    }

    private void procesoCargaOffline1() {
        try {
            if (!objSqlite.equipoRegistrado()) {
                finalizarAplicacion();
            }
            if (!objSqlite.existenTablas()) {
                finalizarAplicacion();
            }
            if (!objSqlite.existeDataLocal()) {
                finalizarAplicacion();
            }
            activarModoOffline();
            cargarQuerys();
            if (!objSqlite.equipoHabilitado()) {
                finalizarAplicacion();
            }
            Funciones.notificar(this, "Modo OffLine Activado.");
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
        }
    }

    private void activarModoOffline() {
    }

    private void validarPermisosAndroid() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.MANAGE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA

        };
        if (!tienePermisos(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }
}