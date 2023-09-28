package com.example.datagreenmovil;

import android.Manifest;
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
import androidx.core.content.ContextCompat;

import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.Querys;
import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.Logica.Swal;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import android.content.SharedPreferences;
import com.google.gson.Gson;

public class cls_01000000_Commutador extends AppCompatActivity {
    ConexionSqlite objSqlite; //= new ConexionSqlite(this, null);
    ConfiguracionLocal objConfLocal; // = new ConfiguracionLocal();
    //ConexionSqlite objSqlite = new ConexionSqlite(this, objConfLocal);
    ConexionBD objSql; // = new ConexionBD(objConfLocal);
    Querys objQuerys;
    //ResultSet rsQuerys;
    Dialog dlg_PopUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        INICIAMOS EL SERVICIO QUE NOS INDICA SI LA APLICACION TIENE UNA ACTUALIZACIÓN
        Intent dataGreenUpdateService = new Intent(this, DataGreenUpdateService.class);
        startService(dataGreenUpdateService);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_01000000_conmutador_001);
        validarPermisosAndroid();
        //CONTINUAR AQUI: EL PROCESO ENTRA A TOKEN NO EXISTE;

        try {
//            int w = Resources.getSystem().getDisplayMetrics().widthPixels;
//            int h = Resources.getSystem().getDisplayMetrics().heightPixels;
//            Toast.makeText(cls_01000000_Commutador.this.getBaseContext(), String.valueOf(w) + 'x' + String.valueOf(h), Toast.LENGTH_LONG).show();;

            objSqlite = new ConexionSqlite(this, null);
            objConfLocal = new ConfiguracionLocal(objSqlite.obtenerConfiguracionLocal());
            objSqlite = new ConexionSqlite(this, objConfLocal); //getApplicationContext()
            //PENDIENTE: REDEFINIR CONCEPTO DE QUERYS -> LIST<QUERY>
            //SE ESPERA PODER USAR objQuerys.Query("NOMBRE DE QUERY") -> RETORNA STRING
            objQuerys = new Querys(objSqlite.obtenerQuerys());

            //        LLEVA DIRECTAMENTE SI EXISTE LA SESION

//            String t=objConfLocal.get("TOKEN_EXPIRA");
//            try{
//                LocalDateTime ldt_TokenExpira = LocalDateTime.parse(t,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//                if(objConfLocal != null &&  ldt_TokenExpira.isAfter(LocalDateTime.now())){
//                    Toast.makeText(this, "ULTIMA Actividad: " , Toast.LENGTH_LONG).show();
//                    abrirUltimaActividad();
//                    finish();
//                }
//            }catch (Exception ex) {
//                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
//            }


            if (check()) {
                Intent i;
                i = new Intent(this, cls_03000000_Login.class);
//                i=new Intent(this, cls_00000000_Plantilla_Base.class);

//                OBTENER RESOLUCION PANTALLA
                int valor = Resources.getSystem().getDisplayMetrics().widthPixels;
                objConfLocal.set("ANCHO_PANTALLA", String.valueOf(valor));
                valor = Resources.getSystem().getDisplayMetrics().heightPixels;
                objConfLocal.set("ALTO_PANTALLA", String.valueOf(valor));

                //OBTENER VERSION APP
                //int versionCode = BuildConfig.VERSION_CODE;
                String versionApp = BuildConfig.VERSION_NAME;
                objConfLocal.set("VERSION_APP", versionApp);

//                objSqlite.guardarConfiguracionLocal(objConfLocal);
                i.putExtra("ConfiguracionLocal", objConfLocal);
//        if (existeSesion()) {
//          abrirMenuModulos();
//        } else {
                startActivity(i);
//        }

                finish();
            } else {
                abrirConfiguraciones();
            }
            /*
            Intent i;
            i=new Intent(this,cls_03000000_Login.class);
            i.putExtra("ConfiguracionLocal",objConfLocal);
            startActivity(i);
            finish();*/
        } catch (Exception ex) {
//            StackTraceElement z = new Exception().getStackTrace()[0];
//            Toast.makeText(this,z.getFileName()+"\n"+z.getMethodName()+"\n"+ex.getMessage(), Toast.LENGTH_LONG).show();
            Funciones.mostrarError(this, ex);
            finish();
        }
        //        objSql = new ConexionBD();
//        objSqlite = new ConexionSqlite(this);

//        objSqlite.SqliteDB= objSqlite.getWritableDatabase();
//        objSqlite.rapidin("DROP TABLE TablaPrueba");
//        String r = objSqlite.rapidin("CREATE TABLE TablaPrueba(Id text, Nombre text, Apellido text)");
//        if (r=="true"){
//            Toast.makeText(this,"Base OK.", Toast.LENGTH_LONG).show();
//        }else{
//            Toast.makeText(this,"Base con ERRORES." + r, Toast.LENGTH_LONG).show();
//        }
//        realizarValidaciones();

    }

    public void abrirMenuModulos() {
        try {
//            objSqlite.guardarConfiguracionLocal(objConfLocal);
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
//        Intent i;
//        switch (objConfLocal.ACTIVIDAD_ACTUAL){
//            case "cls_04000000_Modulos":
//                i = new Intent(this, cls_04000000_Modulos.class);
//                break;
//            default:
//                i = new Intent(this, cls_03000000_Login.class);
//        }
//        i.putExtra("ConfiguracionLocal",objConfLocal);
//        startActivity(i);
//        finish();
        //Toast.makeText(this,ex.getMessage(), Toast.LENGTH_LONG).show();
    }

    private boolean existeSesion() {
        return objConfLocal.get("ID_USUARIO_ACTUAL") != "!ID_USUARIO_ACTUAL" ? true : false;
    }

    private boolean check() {

        try {
//            objConfLocal= new ConfiguracionLocal(objSqlite.obtenerConfiguracionLocal());
            objSql = new ConexionBD(objConfLocal);
            objSqlite = new ConexionSqlite(this, objConfLocal);
            if (!objSqlite.existeBDLocal()) {
//                Funciones.notificar(this,"No existe base de datos local.");
//                abrirConfiguraciones();
                //objSqlite.crearBaseSqlite();
                return false;
            }
            if (!objSqlite.existenTablas()) {
//                Funciones.notificar(this,"No existe tablas en base de datos local.");
//                abrirConfiguraciones();
                return false;
                //objQuerys = new Querys(objSql.obtenerQuerys());
                //objSqlite.crearTablas(objQuerys);
            }
            if (!objSqlite.existeDataLocal()) {
//                Funciones.notificar(this,"No existe data local.");
//                abrirConfiguraciones();
                //descargarData();
                //objSqlite.guardarConfiguracionLocal(objConfLocal);
                return false;
            }

            //objConfLocal.actualizarConfiguraciones(objSql.obtenerConfiguracionesDispositivoMovil());
            if (!objSqlite.existeConfiguracionLocal()) {
                Funciones.notificar(this, "No existe configuracion local.");
//                abrirConfiguraciones();
                //realizarConfiguracionLocal();
                return false;
            }

            if (objSql.hayConexion()) {
                objConfLocal.set("ESTADO_RED", "ONLINE");
                procesoCargaOnline1();
                procesoCargaOnline2();
            } else {
                objConfLocal.set("ESTADO_RED", "OFFLINE");
                procesoCargaOffline1();
            }
            return true;
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
            return false;
        }

        ////////////////////////////////////////
        /*
        if(!objSqlite.equipoRegistrado()){
            objConfLocal.set("ID_DISPOSITIVO",objSql.registrarEquipo(objConfLocal));
        }
        if(objSql.hayActualizacionBDLocal()){
            actualizarBDLocal();
        }
        if(!objSqlite.existeDataLocal()){
            descargarData();
            objSqlite.guardarConfiguracionLocal(objConfLocal);
        }else{
            if(objSql.hayActualizacionDataLocal()){
                actualizarDataLocal();
            }
        }
        cargarQuerys();
        if (!objSqlite.existeConfiguracionLocal()){
            realizarConfiguracionLocal();
        }else {
        }*/
    }

    private void abrirConfiguraciones() {
        try {

            SharedPreferences.Editor editor = getSharedPreferences("MiPreferencia", MODE_PRIVATE).edit();
            Gson gson = new Gson();
            String objConfLocalJson = gson.toJson(objConfLocal);
            editor.putString ("objConfLocal", objConfLocalJson);
            editor.apply();

            Intent intent = new Intent(this, SettingsActivity.class);
//            objSqlite.guardarConfiguracionLocal(objConfLocal);
            intent.putExtra("ConfiguracionLocal", objConfLocal);
            //intent.putExtra("Conexion",objSql);
            //intent.putExtra("Sqlite",objSqlite);
            startActivity(intent);
            finish();
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
        }
    }

//    private void realizarValidaciones() {
//        try {
//            //objConfLocal.IMEI = obtenerIMEI();
//            //objConfLocal.MAC = obtenerMac();
//            if (objConfLocal.get("EQUIPO_CONFIGURADO").equals("TRUE")){
////                procesoCarga1();
//                if (objSql.hayConexion()){
//                    objConfLocal.set("ESTADO_CONEXION","ONLINE");
//                    procesoCargaOnline1();
//                    procesoCargaOnline2();
//                }else{
//                    objConfLocal.set("ESTADO_CONEXION","OFFLINE");
//                    procesoCargaOffline1();
//                }
//            }
//
//
//            /*if (objConfLocal.STATUS==Status.OK){
//
//            }
//
//            Status s = objSqlite.validacionesIniciales();
//            switch (s) {
//                case SIN_BASE_SQLITE:
//                    objSqlite = new ConexionSqlite(this,objConfLocal);
////                    objSqlite.crearBaseSqlite();
//                    //objSqlite.SqliteDB = objSqlite.crearConfiguracionLocal(objSqlite.SqliteDB);
//                    objSqlite.crearConfiguracionLocal();
//                    objConfLocal = objSqlite.obtenerConfiguracionLocal(objConfLocal);
//                    objSql = new ConexionBD(objConfLocal);
//                    if (objSql.hayConexion()) {
//                        objConfLocal.ID_EQUIPO = objSql.registrarEquipo(objConfLocal);
//                    } else {
//                        Toast.makeText(this, "No hay conexion a la base de datos principal, imposible registrar dispositivo.", Toast.LENGTH_LONG).show();
//                    }
//                    break;
//                case CONFIGURACION_LOCAL_NO_EXISTE:
//                    //objSqlite.SqliteDB = objSqlite.crearConfiguracionLocal(objSqlite.SqliteDB);
//                    objSqlite.crearConfiguracionLocal();
//                    objConfLocal = objSqlite.obtenerConfiguracionLocal(objConfLocal);
//                    objSql = new ConexionBD(objConfLocal);
//                    if (objSql.hayConexion()) {
//                        objConfLocal.ID_EQUIPO = objSql.registrarEquipo(objConfLocal);
//                    } else {
//                        Toast.makeText(this, "No hay conexion a la base de datos principal, imposible registrar dispositivo.", Toast.LENGTH_LONG).show();
//                    }
//                    break;
//                case EQUIPO_NO_REGISTRADO:
//                    if (objSql.hayConexion()) {
//                        objConfLocal.ID_EQUIPO = objSql.registrarEquipo(objConfLocal);
//                    } else {
//                        Toast.makeText(this, "No hay conexion a la base de datos principal, imposible registrar dispositivo.", Toast.LENGTH_LONG).show();
//                    }
//                    break;
//            }
////            objConfLocal = objSqlite.obtenerConfiguracionLocal(objConfLocal);
//
//            //MISMO PROCEDIMIENTO QUE ARRIBA;
//            s = objSql.validarConexionBasePrincipal();
//            objConfLocal = objSql.activarModoOnline(objConfLocal);
//            switch (s) {
//                case SIN_CONEXION_PRINCIPAL:
//                    objConfLocal = objSql.activarModoOffline(objConfLocal);
//                    Toast.makeText(this, "No hay conexion a la base de datos principal, modo OFFLINE activado.", Toast.LENGTH_LONG).show();
//                    objConfLocal = objSqlite.obtenerConfiguracionLocal(objConfLocal);
//                    desplegarModoOffline();
//                    break;
//                case TIEMPO_DESFASADO:
//                    Toast.makeText(this, "Tiempo desfasado, corregir para poder iniciar el aplicativo", Toast.LENGTH_LONG).show();
//                    break;
//                case OK:
//                    Toast.makeText(this, "Conectado a la base de datos.", Toast.LENGTH_LONG).show();
//                    Status s2 = objSql.validarActualizaciones();
//                    switch (s2){
//                        case ACTUALIZACION_SOFTWARE_DISPONIBLE:
//                        case ACTUALIZACION_BD_DISPONIBLE:
//                        case ACTUALIZACION_DATA_DISPONIBLE:
//                            if(objSqlite.hayInfoPendiente()){
//                                Toast.makeText(this, "Hay actualizaciones disponibles, transferir registros pendientes para poder actualizar el sistema.", Toast.LENGTH_LONG).show();
//                            }else{
//                                desplegarModuloActualizacion();
//                            }
//                            break;
//                        case OK:
//                            Status s3 = objSqlite.validarEquipo();
//                            switch(s3){
//                                case EQUIPO_NO_HABILITADO:
//                                    objConfLocal.EQUIPO_HABILITADO = false;
//                                    break;
//                                case OK:
//                                    objConfLocal.EQUIPO_HABILITADO = true;
//                                    objConfLocal = objSql.obtenerConfiguracionGeneral(objConfLocal);
//                                    break;
//                            }
//                            break;
//                    }
//            }
//
//            //FIN DE TODAS LAS VALIDACIONES;
//            if(objConfLocal.EQUIPO_HABILITADO){
//                todoOk();
//            }else{
//                Toast.makeText(this, "Equipo restringido, acerquese al area de sistemas.", Toast.LENGTH_LONG).show();
//            }*/
//
//
//        }catch (Exception ex){
//             Funciones.mostrarError(this,ex);
//        }
//    }

//    private void procesoCarga1() {
//        try{
//            if (!objSqlite.existeBDLocal()){
//                objSqlite.crearBaseSqlite();
//            }
//            if (!objSqlite.existeConfiguracionLocal()){
////                realizarConfiguracionLocal();
//                abrirConfiguraciones();
//            }else{
//                //PENDIENTE: MEJORAR ESTA LINEA, NO ES INDISPENSABLE
//                //objConfLocal=objSqlite.obtenerConfiguracionLocal(objConfLocal);
//                objConfLocal.actualizarConfiguraciones(objSqlite.obtenerConfiguracionLocal());
//            }
//        }catch (Exception ex){
//            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
//            objConfLocal.set("MENSAJE",ex.getMessage());
//        }
//        //objConfLocal= objSqlite.procesoCarga1(objConfLocal);
//    }

    private void procesoCargaOnline1() {
        try {
//            if(!objSqlite.existenTablas()){
//                //rsQuerys=objSql.obtenerQuerys();
//                objQuerys = new Querys(objSql.obtenerQuerys());
//                objSqlite.crearTablas(objQuerys);
//            }
            if (!objSqlite.equipoRegistrado()) {
                objConfLocal.set("ID_DISPOSITIVO", objSql.registrarEquipo(objConfLocal));
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
//            objConfLocal.set("MENSAJE",ex.getMessage());
            //PENDIENTE: REVISAR SI ES UTIL LA CLASE STATUS;
            //objConfLocal.STATUS=Status.ERROR;
        }
    }

    private void procesoCargaOnline2() throws Exception {
        try {
            //rsQuerys=objSqlite.obtenerQuerys();
            if (!objSqlite.equipoHabilitado()) {
                finalizarAplicacion();
            }
            if (!objSql.horaActualizada()) {
//                dlg_PopUp = Funciones.obtenerDialogParaCambiarClave(this,objConfLocal,objSqlite,this);
//                dlg_PopUp.show();
//                Funciones.mostrarPopUp(this,
//                        "Tiempo con desface",
//                        false,
//                        "El equipo tiene configurada una fecha u hora distinta al servidor, corregir.",
//                        false,
//                        true);
                //finalizarAplicacion();
                Funciones.notificar(this, "El dispositivo móvil tiene configurada una fecha u hora distinta al servidor, corregir.");
            }
            if (objSql.hayActualizacionSoftware(objConfLocal)) {
                actualizarApp();
            }
            if (objSqlite.existeDataPendiente()) {
                eviarDataPendiente();
            }
            //objConfLocal.STATUS=Status.OK;
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
            //rsQuerys=objSql.obtenerQuerys();
            objQuerys = new Querys(objSql.obtenerQuerys());
            HashMap<String, String> hmQuerys = objQuerys.obtQuerysParaDescarga("01"); //PENDIENTE OBTENER DINAMICAMENTE;
            Iterator<Map.Entry<String, String>> it = hmQuerys.entrySet().iterator();
            ResultSet rsAux;
            ResultSetMetaData m;
            String q;
            while (it.hasNext()) {
                Map.Entry<String, String> set = (Map.Entry<String, String>) it.next();
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
                    //m.getColumnCount()
                    //rsQuerys.getString("Valor")
                    objSqlite.doItBaby(q, null, "INSERT");
                }
            }
            /*
            ResultSet rsAux;
            ResultSetMetaData m;
            String q;
            //rsQuerys.beforeFirst();
            while(rsQuerys.next()){
                //q = "INSERT INTO ";
                if(rsQuerys.getString("NombreQuery").contains("DESCARGAR DATA") && rsQuerys.getInt("nParametros")==0){
                    rsAux = objSql.doItBaby(rsQuerys.getString("QuerySqlite"),null);
                    m=rsAux.getMetaData();
                    //q = "INSERT INTO " + rsQuerys.getString("TablaObjetivo") + " VALUES(";
                    while (rsAux.next()){
                        q = "INSERT INTO " + rsQuerys.getString("TablaObjetivo") + " VALUES(";
                        for (Integer i=0; i<m.getColumnCount(); i++){
                            if(i>0){
                                q = q +",";
                            }
                            q = q + "'" + rsAux.getString(i+1) + "'";
                        }
                        q = q + ")";
                        //m.getColumnCount()
                        //rsQuerys.getString("Valor")
                        objSqlite.doItBaby(q,null,"INSERT");
                    }
                }
            }*/
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
        }
    }

    private void actualizarBDLocal() {
        //DESTRUIR BD LOCAL
        //CONSTRUIR BD LOCAL
        if (!objSqlite.existeBDLocal()) {
            objSqlite.destruirBaseSqlite();
//            objSqlite.crearBaseSqlite();
        }
    }

    private void eviarDataPendiente() {
    }

    private void actualizarApp() {
    }

    private void finalizarAplicacion() {
//        finish();
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
            //objConfLocal.STATUS=Status.OK;
            Funciones.notificar(this, "Modo OffLine Activado.");
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
//            objConfLocal.set("MENSAJE",ex.getMessage());
            //objConfLocal.STATUS=Status.ERROR;
        }
    }

    private void activarModoOffline() {
    }

//    private void realizarConfiguracionLocal() {
//        //AQUI DEBERIAN DESPLEGARSE EL ACTIVITY DE CONFIGURACION BASICA //aparece nombre final de valores de configuraicon
//        try {
//            objConfLocal.set("RED_HOST","192.168.30.99");
//            objConfLocal.set("RED_INSTANCIA","MSSQLSERVER17");
//            objConfLocal.set("RED_NOMBRE_DB","DataGreenMovil");
//            objConfLocal.set("RED_USUARIO","sa");
//            objConfLocal.set("RED_PASSWORD","A20200211sj");
//            //objConfLocal.IDEMPRESA="01";
//            objConfLocal.set("ID_EMPRESA","01");
//            //objConfLocal.IMEI="NUEVOIMEI";
//            objConfLocal.set("IMEI", Funciones.obtenerIdUnico(this));
//            //objConfLocal.ID_EQUIPO="001"; //CHAR(3)
//            //objConfLocal.set("MENSAJE",EEE);
//            //objConfLocal.ONLINE=true;
//            //objConfLocal.set("ESTADO_CONEXION","ONLINE");
//            //objConfLocal.OFFLINE=false;
//            //objConfLocal.set("MENSAJE",EEE);
//            //objConfLocal.EQUIPO_HABILITADO=true;
//            objConfLocal.set("EQUIPO_HABILITADO","TRUE");
//            //objConfLocal.RED_CONFIGURADA=true;
//            objConfLocal.set("RED_CONFIGURADA","TRUE");
//            //objConfLocal.RECORDAR_USUARIO=true;
//            //objConfLocal.MAC="00:11:22:33";
//            //objConfLocal.set("MAC",obtenerMac());
//            objConfLocal.set("MAC",Funciones.obtenerMac());
//            //objConfLocal.NRO_TELEFONICO="999999999";
//            objConfLocal.set("NRO_TELEFONICO","999 999 999");
//            //objConfLocal.PROPIETARIO="SU DUEÑO";
//            objConfLocal.set("PROPIETARIO","SU DUEÑO");
//            //objConfLocal.USUARIO_ACTUAL="JMERA";
//            //objConfLocal.set("MENSAJE",EEE);
//            //objConfLocal.set("MENSAJE",EEE);
//            //Toast.makeText(this, "CONFIGURACDNES AUTOMATICAS", Toast.LENGTH_LONG).show();
//            objSql=new ConexionBD(objConfLocal);
//            objConfLocal.set("ID_DISPOSITIVO",objSql.registrarEquipo(objConfLocal));
//            objConfLocal.actualizarConfiguraciones(objSql.obtenerConfiguracionesDispositivoMovil());
//            objSql.guardarConfiguracionLocalEnServidor(objConfLocal);
//            objSqlite.guardarConfiguracionLocal(objConfLocal);
//        }catch (Exception ex){
//             Funciones.mostrarError(this,ex);
//        }
//    }

//    private void todoOk() {
//        Toast.makeText(this, "Todo OK", Toast.LENGTH_LONG).show();
//    }
//
//    private void desplegarModuloActualizacion() {
//        Toast.makeText(this, "Modulo de actualizaciones desplegadas", Toast.LENGTH_LONG).show();
//    }
//
//    private void desplegarModoOffline() {
//        Toast.makeText(this, "Modo OFFLINE desplegado.", Toast.LENGTH_LONG).show();
//    }

//    public String obtenerMac(){
//        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        return wm.getConnectionInfo().getMacAddress();
////        return wi.getMacAddress();
//    }

//    public String obtenerMac(){/*
//        try{
//            List<NetworkInterface> networkInterfaceList = Collections.list(NetworkInterface.getNetworkInterfaces());
//            String stringMac = "";
//            for(NetworkInterface networkInterface : networkInterfaceList){
//
//                if(networkInterface.getName().equalsIgnoreCase("wlon0")){
//                    for(int i = 0 ;i <networkInterface.getHardwareAddress().length; i++){
//                        String stringMacByte = Integer.toHexString(networkInterface.getHardwareAddress()[i]& 0xFF);
//                        if(stringMacByte.length() == 1){
//                            stringMacByte = "0" +stringMacByte;
//                        }
//                        stringMac = stringMac + stringMacByte.toUpperCase() + ":";
//                    }
//                    break;
//                }
//            }
//            return stringMac;
//        }catch (SocketException e){
//            e.printStackTrace();
//        }*/
//        return "00:00:00:00:00:01";
//    }
//
//    private String obtenerIMEI() {
//        /*
//        final TelephonyManager telephonyManager= (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            //Hacemos la validación de métodos, ya que el método getDeviceId() ya no se admite para android Oreo en adelante, debemos usar el método getImei()
//            return telephonyManager.getImei();
//        }else{
//            return "Sin Imei.";//telephonyManager.getDeviceId();
//        }*/
//        return "0000000000000";
//    }

    //PENDIENTE: REALIZAR VALIDACIONES SEGUIDAS, ACTUALMENTE SOLO VALIDA 1 Y LUEGO SE CIERRA
    private void validarPermisosAndroid() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.MANAGE_EXTERNAL_STORAGE

        };
        if (!tienePermisos(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
//            ActivityCompat.requestPermissions(this, new String[]{
//                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    android.Manifest.permission.READ_PHONE_STATE,
//                    android.Manifest.permission.ACCESS_FINE_LOCATION,
//                    android.Manifest.permission.MANAGE_EXTERNAL_STORAGE}, PERMISSION_ALL);
        }
//        Toast.makeText(this,"Permisos Ok.", Toast.LENGTH_SHORT).show();
    }

    public static boolean tienePermisos(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                {
                    return false;
                }
            }
        }
        return true;
    }
    /*
    private String obtenerIdUnico() {
        //Hacemos la validación de métodos, ya que el método getDeviceId() ya no se admite para android Oreo en adelante, debemos usar el método getImei()
        return Build.VERSION.SDK_INT >= 29 ? obtenerIdAndroid() : obtenerImei();
    }

    private String obtenerImei() {
        try{
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 100);
            }
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String r = telephonyManager.getDeviceId();
            return r != null ? r.toUpperCase() : "";
        }catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_SHORT).show();
            return  null;
        }
    }

    private String obtenerIdAndroid(){
        try{
            TelephonyManager tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            String deviceID = null;
            int readIMEI= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            if(deviceID == null) {
                if (readIMEI != PackageManager.PERMISSION_GRANTED) {
                    return deviceID.toUpperCase() ;
                }
                deviceID = android.provider.Settings.Secure.getString(this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            }
            return deviceID.toUpperCase();
        }catch(Exception ex){
            throw  ex;
        }
    }

    private String obtenerMac() {
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
*/
}