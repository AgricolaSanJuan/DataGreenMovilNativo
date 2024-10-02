package com.example.datagreenmovil.Conexiones;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.MiFila;
import com.example.datagreenmovil.Entidades.PopUpBuscarEnLista_Item;
import com.example.datagreenmovil.Entidades.Status;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.Duration;

import java.io.Serializable;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import org.threeten.bp.Duration;
//import org.threeten.bp.LocalDateTime;
//import org.threeten.bp.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Result;

public class ConexionBD implements Serializable {
    ConfiguracionLocal objConfLocal;
    Connection objConexion;
    SharedPreferences confLocal;
    SharedPreferences.Editor editor;
    Context ctx;



    //////////////
//    String Host="192.168.30.99";
//    String inst="MSSQLSERVER17";
//    String BD="DataGreenMovil";
//    String Usuario="sa";
//    String Pass="A20200211sj";

    public ConexionBD(ConfiguracionLocal cl) {
        objConfLocal = cl;
    }
    public ConexionBD(Context context) {
//        objConfLocal = cl;
        ctx = context;
        confLocal = ctx.getSharedPreferences("objConfLocal", ctx.MODE_PRIVATE);
        editor = confLocal.edit();
    }
    public Status validarConexionBasePrincipal() throws Exception  {
        try{
            if (!hayConexion()){
                return Status.SIN_CONEXION_PRINCIPAL;
            }
            if (!horaActualizada()){
                return Status.TIEMPO_DESFASADO;
            }
            return Status.OK;
        }catch(Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public boolean horaActualizada() throws Exception {
        try {
            String horaString = "";
            LocalDateTime horaServidor;

            // Llama a tu método para obtener la hora del servidor
            horaString = doItBaby("sp_ObtenerFechaHoraActual", null, horaString);

            // Parsea la cadena a LocalDateTime
            horaServidor = LocalDateTime.parse(horaString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // Obtiene la hora actual del dispositivo
            LocalDateTime horaDispositivo = LocalDateTime.now();

            // Calcula la diferencia entre las dos horas
            Duration diferencia = Duration.between(horaServidor, horaDispositivo);

            // Comprueba si la diferencia está dentro del rango de -5 a 5 minutos
            return diferencia.toMinutes() >= -5 && diferencia.toMinutes() <= 5;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean hayConexion() throws Exception{
        try{
            return abrirConexion() != null;
        }catch (Exception ex){
            return false; //throw ex;
        }
    }

    /*public Status validarActualizaciones() throws Exception{
        try{
            //String r ="";
            ResultSet r;
            List<String> p = new ArrayList<>();
            p.add("01");
            p.add("DataGreen Movil");
            r = doItBaby("sp_Gen_ObtenerUltimaVersionSoftware",p);
            if (hayActualizacionSoftware(r)){
                return Status.ACTUALIZACION_SOFTWARE_DISPONIBLE;
            }
            if (hayActualizacionBDLocal(r)){
                return Status.ACTUALIZACION_BD_DISPONIBLE;
            }
            if (hayActualizacionDataLocal(r)){
                return Status.ACTUALIZACION_DATA_DISPONIBLE;
            }
            return Status.OK;
        }catch(Exception ex){
            throw new Exception(ex.getMessage());
        }
    }*/

    public boolean hayActualizacionSoftware(ConfiguracionLocal cl) throws Exception {
        return !cl.get("VERSION_APP").equals(cl.get("VERSION_APP_DISPONIBLE"));
    }

    public ConfiguracionLocal obtenerVersionesDisponibles(ConfiguracionLocal cl) throws Exception {
        try{
            List<String> p = new ArrayList<String>();
            p.add(cl.get("ID_EMPRESA"));
            p.add(cl.get("NOMBRE_APP"));
            ResultSet rS;
            rS = doItBaby("sp_Dgm_Gen_ObtenerVersionSoftware",p);
//            rS.next();
//            cl.set("VERSION_APP_DISPONIBLE",rS.getString(3));
//            rS.next();
//            cl.set("VERSION_DATA_DISPONIBLE",rS.getString(3));
//            rS.next();
//            cl.set("VERSION_DB_DISPONIBLE",rS.getString(3));
            while (rS.next()){
                if(rS.getString(2).equals("App")){
                    cl.set("VERSION_APP_DISPONIBLE",rS.getString(3));
                } else if (rS.getString(2).equals("Data")){
                    cl.set("VERSION_DATA_DISPONIBLE",rS.getString(3));
                }else if (rS.getString(2).equals("DataBase")){
                    cl.set("VERSION_DB_DISPONIBLE",rS.getString(3));
                }
            }
            return cl;
        }catch (Exception ex){
            throw ex;
        }
    }

    public boolean hayActualizacionBDLocal(ConfiguracionLocal cl) throws Exception {
        return !cl.get("VERSION_DB_SQLITE").equals(cl.get("VERSION_DB_DISPONIBLE"));
    }

    /*private boolean hayActualizacionSoftware() throws Exception {
        try{
            String r ="";
            List<String> p = new ArrayList<>();
            p.add("DataGreen Movil");
            r = doItBaby("spObtenerUltimaVersion",p,r);
            if(!objConfLocal.VERSION_APP.equals("0") && r.compareTo(objConfLocal.VERSION_APP) > 0 ){
                return true;
            }
            return false;
        }catch (Exception ex){
         //return false;
            throw new Exception(ex.getMessage());
        }
    }*/

    public ConfiguracionLocal validacionesRemotas(ConfiguracionLocal cl) {
//        if (!existenTablas()){
//            return false;
//        }
//        if (!existeDataLocal()){
//            return false;
//        }
//        if (!equipoHabilitado()){
//            return false;
//        }
        return cl;
    }

    public boolean hayActualizacionDataLocal(ConfiguracionLocal cl) throws Exception {
        return !cl.get("VERSION_DATA_SQLITE").equals(cl.get("VERSION_DATA_DISPONIBLE"));
    }

    //CONTINUAR AQUI: CREAR SP CORRESPONDIENTE;
    public String registrarEquipo(ConfiguracionLocal cl) throws Exception{
        try{
            List<String> p = new ArrayList<String>();
//            p.add(cl.IMEI);
            p.add(cl.get("ID_EMPRESA"));
            p.add(cl.get("MAC"));
            p.add(cl.get("IMEI"));
            p.add(cl.get("NRO_TELEFONICO"));
            p.add(cl.get("PROPIETARIO"));
            p.add("DGM00000"); //PENDIENTE: EN DURO TEMPORALMENTE EL VALOR ES EL ID DE LA TABLA DE USUARIOS
            ResultSet rS;
            rS = doItBaby("sp_Dgm_Gen_RegistrarDispositivoMovil",p);
            rS.next();
            if(Integer.parseInt(rS.getString(1))==1){
                //cl.ID_EQUIPO=r.getString(1);
                return rS.getString(2);
            }else{
                throw new Exception(rS.getString(2));
            }
            //return cl;
        }catch (Exception ex){
            throw ex;
        }
    }

    public ConfiguracionLocal activarModoOffline(ConfiguracionLocal cl){
        cl.set("ESTADO_CONEXION","OFFLINE");/*
        cl.set("ONLINE","TRUE");
        cl.set("OFFLINE","TRUE");
        cl.ONLINE=false;
        cl.OFFLINE=true;*/
        return cl;
    }

    public ConfiguracionLocal activarModoOnline(ConfiguracionLocal cl){
        cl.set("ESTADO_CONEXION","ONLINE");/*
        cl.ONLINE=true;
        cl.OFFLINE=false;*/
        return cl;
    }

    public ResultSet obtenerConfiguracionesDispositivoMovil() throws Exception{
        try{
            List<String> p = new ArrayList<String>();
            p.add(objConfLocal.get("ID_EMPRESA"));
            p.add(objConfLocal.get("MAC"));
            ResultSet rS;
            rS = doItBaby("sp_Dgm_Gen_ObtenerConfiguracionesDispositivoMovil",p);
            return rS;
        }catch (Exception ex){
            throw ex;
        }
    }

    /*
        public ConfiguracionLocal obtenerConfiguracionGeneral(ConfiguracionLocal cl) throws Exception{
            try{
                List<String> p = new ArrayList<String>();
                p.add("002"); //ID DE DISPOSITIVO COMO PARAMETRO;
                ResultSet rS;
                rS = doItBaby("sp_Dgm_Gen_ObtenerConfiguracionDispositivoMovil",p);
                Map<String,String> conf = new HashMap<>();
                //KW: RECORRER RESULTSET
                while(rS.next()){
                    conf.put(rS.getString("Clave"),rS.getString("Valor"));
                }
                return agregarValoresConfiguracion(cl,conf);
    //            if(Integer.parseInt(rS.getString(1))==1){
    //                //cl.ID_EQUIPO=r.getString(1);
    //                return rS.getString(2);
    //            }else{
    //                throw new Exception(rS.getString(2));
    //            }
                //return cl;
            }catch (Exception ex){
                throw ex;
            }
        }

        private ConfiguracionLocal agregarValoresConfiguracion(ConfiguracionLocal cl, Map<String, String> conf) {
            cl.TOKEN = conf.containsKey("TOKEN") ? Boolean.parseBoolean(conf.get("TOKEN")) :  cl.TOKEN;
            cl.ULTIMA_SESION_ABIERTA = conf.containsKey("ULTIMA_SESION_ABIERTA") ? LocalDateTime.parse(conf.get("ULTIMA_SESION_ABIERTA"),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) :  cl.ULTIMA_SESION_ABIERTA;
            cl.TOKEN_EXPIRA = conf.containsKey("TOKEN_EXPIRA") ? LocalDateTime.parse(conf.get("TOKEN_EXPIRA"),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) :  cl.TOKEN_EXPIRA;
            cl.ACTIVIDAD_ACTUAL = conf.containsKey("ACTIVIDAD_ACTUAL") ? conf.get("ACTIVIDAD_ACTUAL") :  cl.ACTIVIDAD_ACTUAL;
            cl.EQUIPO_HABILITADO = conf.containsKey("EQUIPO_HABILITADO") ? Boolean.parseBoolean(conf.get("EQUIPO_HABILITADO")) :  cl.EQUIPO_HABILITADO;
            cl.RED_CONFIGURADA = conf.containsKey("RED_CONFIGURADA") ? Boolean.parseBoolean(conf.get("RED_CONFIGURADA")) :  cl.RED_CONFIGURADA;
            cl.RECORDAR_USUARIO = conf.containsKey("RECORDAR_USUARIO") ? Boolean.parseBoolean(conf.get("RECORDAR_USUARIO")) :  cl.RECORDAR_USUARIO;
            return cl;
        }
    */
    public Connection abrirConexion() throws Exception{
        Connection cn = null;
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            String cc = "jdbc:jtds:sqlserver://" + confLocal.getString("RED_HOST","!RED_HOST")
                    + ";instance=" + confLocal.getString("RED_INSTANCIA","!RED_INSTANCIA")
                    + ";databaseName=" + confLocal.getString("RED_NOMBRE_DB","!RED_NOMBRE_DB")
                    + ";user=" + confLocal.getString("RED_USUARIO","!RED_USUARIO")
                    + ";password=" + confLocal.getString("RED_PASSWORD","!RED_PASSWORD") +";";
            //String c2="jdbc:jtds:sqlserver://192.168.30.99;instance=MSSQLSERVER17;databaseName=DataGreen;user=sa;password=A20200211sj";
            //Host+";instanceName="+inst + ;trustServerCertificate=false;integratedSecurity=true;authentication=SqlPassword;
            //Toast.makeText(getApplicationContext(),cc,Toast.LENGTH_LONG).show();
            //policy.wait(3);
            DriverManager.setLoginTimeout(1);
            cn = DriverManager.getConnection(cc);
            //cn.close();
        }catch(Exception ex){
//            logger.log(ex);
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            throw ex;
        }
        return cn;
    }

    //DOITBABY BASICO, DEVUELVE RESULTSET;
//    public ResultSet doItBaby(String q) throws Exception {
//        try{
//            objConexion = abrirConexion();
//            Statement sentencia = objConexion.createStatement(); //conexionBD(objConfLocal).createStatement();
//            sentencia.setQueryTimeout(2);
//            ResultSet r = sentencia.executeQuery(q);
//            return r;
//        }catch(Exception ex){
//            throw ex;
//        }finally {
////            objConexion.close();
//        }
//    }

    @SafeVarargs
    public final ResultSet doItBaby(String q, List<String>... p) throws Exception {
        try{
            if(p!=null){
                q = prepararQuery(q,p[0]);
            }
            objConexion = abrirConexion();
            Statement sentencia = objConexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); //conexionBD(objConfLocal).createStatement();
            sentencia.setQueryTimeout(1);
            return sentencia.executeQuery(q);
        }catch(Exception ex) {
            throw ex;
        }
//        }finally {
//            objConexion.close();
//        }
    }

    //DOITBABY DEVUELVE STRING;
    public String doItBaby(String q, List<String> p, String s) throws Exception {
        try{
            ResultSet r = doItBaby(q,p);
            r.next();
//        r.getRow();
            return r.getString(1);
        }catch(Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    public boolean resulSetVacio(ResultSet rs) throws Exception{
        return (!rs.isBeforeFirst() && rs.getRow()==0);
    }

    public String prepararQuery(String q, List<String> p){
        //q = "Exec " + q;
        if(p!=null){
            for (String item : p) {
                q = encadenar(q, item);
            }
        }
        return q;
    }

    private String encadenar(String s, String i) {
        if(s.length()==0){
            s = "'" + i + "'";
        }else if(s.charAt(s.length()-1)=='\''){
            s = s + ", '" + i + "'";
        }else{
            s = s + " '" + i + "'";
        }
        return s;
    }

    public ResultSet obtenerQuerys() throws Exception {
        try{
            //String q="EXEC sp_Dgm_ObtenerQuerys '" + objConfLocal.IDEMPRESA+"'";
            //q = prepararQuery(q,p);
            String q="EXEC sp_Dgm_Gen_ListarQuerys";
            objConexion = abrirConexion();
            Statement sentencia = objConexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); //conexionBD(objConfLocal).createStatement();
            sentencia.setQueryTimeout(1);
            ResultSet r = sentencia.executeQuery(q);
            return r;
        }catch(Exception ex){
            throw ex;
        }
    }

    public void guardarConfiguracionLocalEnServidor(ConfiguracionLocal cl) throws Exception {
        try{
            String IdEmpresa="01";
            for(MiFila f: cl.Filas().Filas){
                List<String> p = new ArrayList<String>();
                p.add(objConfLocal.get("ID_EMPRESA"));
                p.add(objConfLocal.get("MAC"));
                p.add(f.Item(0));
                p.add(f.Item(1));
                ResultSet rs= doItBaby("sp_Dgm_Gen_GuardarConfiguracionDispositivoMovil",p);
                p.clear();
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    public void probarConexion(ConfiguracionLocal objCL) throws Exception {
        try {
            if (this.hayConexion()) {
//                objCL.set("RED_CONFIGURADA","TRUE");
//                objCL.set("ESTADO_RED","ONLINE");
                editor.putString("RED_CONFIGURADA","TRUE").apply();
                editor.putString("ESTADO_RED","ONLINE").apply();
            }else{
//                objCL.set("RED_CONFIGURADA","FALSE");
//                objCL.set("ESTADO_RED","OFFLINE");
                editor.putString("RED_CONFIGURADA","FALSE").apply();
                editor.putString("ESTADO_RED","OFFLINE").apply();
            }
        } catch (Exception e) {
            //Toast.makeText(this.getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            throw e;
            //e.printStackTrace();
        }
    }

    public boolean existeId(String nombreTabla, String idEmpresa, String id) throws Exception {
        try{
            List<String> p = new ArrayList<String>();
            p.add(nombreTabla);
            p.add(idEmpresa);
            p.add(id);
            ResultSet rS;
            rS = doItBaby("sp_Dgm_Gen_ExisteId",p);
            rS.next();
            if(Integer.parseInt(rS.getString(1))==1){
                //cl.ID_EQUIPO=r.getString(1);
                return Integer.parseInt(rS.getString(2))==1;
            }else{
                throw new Exception(rS.getString(2));
            }
            //return cl;
        }catch (Exception ex){
            throw ex;
        }
    }

    public String obtenerNuevoId(String nombreTabla, String idEmpresa, String indice) throws Exception {
        try{
            List<String> p = new ArrayList<String>();
            p.add(nombreTabla);
            p.add(idEmpresa);
            p.add(indice);
            ResultSet rS;
            rS = doItBaby("sp_Dgm_Gen_obtenerNuevoId",p);
            rS.next();
            if(Integer.parseInt(rS.getString(1))==1){
                //cl.ID_EQUIPO=r.getString(1);
                return rS.getString(2);
            }else{
                throw new Exception(rS.getString(2));
            }
            //return cl;
        }catch (Exception ex){
            throw ex;
        }
    }



    public ArrayList<PopUpBuscarEnLista_Item> arrayParaXaPopUpBuscarEnLista(ResultSet rS) throws SQLException {
        ArrayList<PopUpBuscarEnLista_Item> r = new ArrayList<>();
        PopUpBuscarEnLista_Item item = null;
//        rS.beforeFirst();
        while (rS.next()){
            item = new PopUpBuscarEnLista_Item();
            item.setIcono(0);
            item.setId(rS.getString(1));
            item.setDescripcion(rS.getString(2));
            r.add(item);
        }
        rS.close();

//        if (rS.moveToFirst()){
//            do{
//                item = new PopUpBuscarEnLista_Item();
//                item.setIcono(0);
//                item.setId(rS.getString(0));
//                item.setDescripcion(rS.getString(1));
//                r.add(item);
//            }while (rS.moveToNext());
//        }
//        rS.close();
        return r;
    }
//    public String obtenerMac(){
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
//        }
//        return  "0";
//    }

//    public String obtenerImei(Activity a){
//        TelephonyManager tm = (TelephonyManager) a.getSystemService(Context.TELEPHONY_SERVICE);
//        return tm.getDeviceId();
//    }


}