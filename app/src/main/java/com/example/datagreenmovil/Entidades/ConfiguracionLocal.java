package com.example.datagreenmovil.Entidades;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//PENDIENTE: TODA ESTA CLASE COMPLETA DEBE DE MODIFICARSE PARA ACEPTAR EL VALOR ID_EMPRESA COMO NUEVA COLUMNA CLAVE ADICIONAL AL NOMBRE DE LA OPCION DE CONFIGURACION;
//POR AHORA SOLO ADMITE (CLAVE, VALOR) DEBERIA DE SER (ID_EMPRESA, CLAVE, VALOR);
public class ConfiguracionLocal implements Serializable {
    public MiData data;
    /*
    public Map<String, String> CREDENCIALES_BD;
    public String USUARIO_ACTUAL;
    public String NOMBRE_APP;
    public String VERSION_APP;
    public String VERSION_DB_SQLITE;
    public String VERSION_DATA;
    public String VERSION_DISPONIBLE;
    public boolean TOKEN;
    public LocalDateTime ULTIMA_SESION_ABIERTA;
    public LocalDateTime TOKEN_EXPIRA;
    public String ACTIVIDAD_ACTUAL;
    //public String NOMBRE_BDSQLITE;
    public String IMEI;
    public String ID_EQUIPO; //CHAR(3)
    public boolean ONLINE;
    public boolean OFFLINE;
    public boolean EQUIPO_HABILITADO;
    public boolean RED_CONFIGURADA;
    public boolean RECORDAR_USUARIO;
    public String MAC;
    public String NRO_TELEFONICO;
    public String PROPIETARIO;
    public String IDEMPRESA;
    public Status STATUS;
    public String MENSAJE;
*/
    public ConfiguracionLocal(Cursor c){
        if(c!=null){
            this.data=new MiData(c);
        }else{
         this.data = new MiData();
        }
    }

    public ConfiguracionLocal(ResultSet rs){
        if(rs!=null){
            this.data=new MiData(rs);
        }else{
            this.data = new MiData();
        }
    }

    public ConfiguracionLocal() {
        this.data = new MiData();
//        //this.CREDENCIALES_BD = CREDENCIALES_BD;
//        Map<String,String> m = new HashMap<>();
//        m.put("HOST","");
//        m.put("INSTANCIA","");
//        m.put("NOMBRE_BASE","");
//        m.put("PUERTO","");
//        m.put("USUARIO_BD","");
//        m.put("PASSWORD_BD","");
//        this.setCREDENCIALES_BD(m);
////        this.VERSION_APP = "";
////        this.VERSION_DISPONIBLE = "";
////        this.TOKEN = true;
////        this.ULTIMA_SESION_ABIERTA = LocalDateTime.now();
//        this.setVERSION_APP("");
//        this.setUSUARIO_ACTUAL("");
//        this.setTOKEN(true);
//        //this.setULTIMA_SESION_ABIERTA(LocalDateTime.now());
//        this.EQUIPO_HABILITADO=true;
    }
/*
    public ConfiguracionLocal(Cursor c) {
        while(c.moveToNext()){
            switch (c.getString(0)){
                case "ID_EMPRESA":
                    this.IDEMPRESA=c.getString(1);
                    break;
                case "DURACION_TOKEN_HORAS":
                    this.DURACION_TOKEN_HORAS=c.getString(1);
                    break;
                case "EQUIPO_CONFIGURADO":
                    this.EQUIPO_CONFIGURADO=c.getString(1);
                    break;
                case "EQUIPO_HABILITADO":
                    this.EQUIPO_HABILITADO=c.getString(1);
                    break;
                case "EXISTEN_TABLAS":
                    this.EXISTEN_TABLAS=c.getString(1);
                    break;
                case "EXISTE_CONFIGURACION_LOCAL":
                    this.EXISTE_CONFIGURACION_LOCAL=c.getString(1);
                    break;
                case "EXISTE_DATA_PENDIENTE":
                    this.EXISTE_DATA_PENDIENTE=c.getString(1);
                    break;
                case "HOST_DB":
                    this.IDEMPRESA=c.getString(1);
                    break;
                case "ID_DISPOSITIVO":
                    this.IDEMPRESA=c.getString(1);
                    break;
                case "IMEI":
                    this.IDEMPRESA=c.getString(1);
                    break;
                case "INSTANCIA_DB":
                    this.IDEMPRESA=c.getString(1);
                    break;
                case "MAC":
                    this.IDEMPRESA=c.getString(1);
                    break;
                case "NOMBRE_DB":
                    this.IDEMPRESA=c.getString(1);
                    break;
                case "PASSWORD_DB":
                    this.IDEMPRESA=c.getString(1);
                    break;
                case "RECORDAR_USUARIO":
                    this.IDEMPRESA=c.getString(1);
                    break;
                case "RED_CONFIGURADA":
                    this.IDEMPRESA=c.getString(1);
                    break;
                case "TOKEN_EXPIRA":
                    this.IDEMPRESA=c.getString(1);
                    break;
                case "ULTIMA_ACTIVIDAD":
                    this.IDEMPRESA=c.getString(1);
                    break;
                case "ULTIMA_CONEXION":
                    this.IDEMPRESA=c.getString(1);
                    break;
                case "ULTIMA_SESION":
                    this.IDEMPRESA=c.getString(1);
                    break;
                case "ULTIMO_CORRELATIVO":
                    this.IDEMPRESA=c.getString(1);
                    break;
                case "ULTIMO_USUARIO":
                    this.IDEMPRESA=c.getString(1);
                    break;
                case "USUARIO_DB":
                    this.IDEMPRESA=c.getString(1);
                    break;
                    case "VERSION_APP":
                        this.IDEMPRESA=c.getString(1);
                        break;
                case "VERSION_DATA":
                    this.IDEMPRESA=c.getString(1);
                    break;
                case "VERSION_DATA_SQLITE":
                    this.IDEMPRESA=c.getString(1);
                    break;
                case "VERSION_DB":
                    this.IDEMPRESA=c.getString(1);
                    break;
                case "VERSION_DB_SQLITE":
                    this.IDEMPRESA=c.getString(1);
                    break;
            }
            if(c.getString(0).equals("")){
                this.IDEMPRESA=c.getString(1);
            }
        }*/

//        //this.CREDENCIALES_BD = CREDENCIALES_BD;
//        Map<String,String> m = new HashMap<>();
//        m.put("HOST","");
//        m.put("INSTANCIA","");
//        m.put("NOMBRE_BASE","");
//        m.put("PUERTO","");
//        m.put("USUARIO_BD","");
//        m.put("PASSWORD_BD","");
//        this.setCREDENCIALES_BD(m);
////        this.VERSION_APP = "";
////        this.VERSION_DISPONIBLE = "";
////        this.TOKEN = true;
////        this.ULTIMA_SESION_ABIERTA = LocalDateTime.now();
//        this.setVERSION_APP("");
//        this.setUSUARIO_ACTUAL("");
//        this.setTOKEN(true);
//        //this.setULTIMA_SESION_ABIERTA(LocalDateTime.now());
//        this.EQUIPO_HABILITADO=true;
    //}
    /*
    public ConfiguracionLocal(Map<String,String> v) {
        //PENDIENTE: INTENTAR HACER ESTO DINAMICAMENTE;
//        for(Field f : this.getClass().getDeclaredFields()){
//            if(v.contains(f.getName())){
//                this.
//            }
//        }
        //MIENTRAS TANTO SE HACE MANUAL;
        this.IDEMPRESA=v.get("IDEMPRESA");
        this.VERSION_APP = v.get("VERSION_APP");
        this.VERSION_DB_SQLITE = v.get("VERSION_DB_SQLITE");
        this.VERSION_DATA = v.get("VERSION_DATA");
        this.TOKEN = Boolean.parseBoolean(v.get("TOKEN"));
        String aux = v.get("ULTIMA_SESION_ABIERTA");
        this.ULTIMA_SESION_ABIERTA = aux!=null && aux.length()>0?LocalDateTime.parse(aux):LocalDateTime.now(); //v.get("ULTIMA_SESION_ABIERTA");
        aux = v.get("TOKEN_EXPIRA");
        this.TOKEN_EXPIRA = aux!=null && aux.length()>0?LocalDateTime.parse(aux):LocalDateTime.now();
        this.IMEI = v.get("IMEI");
        this.MAC = v.get("MAC");
        this.ID_EQUIPO = v.get("ID_EQUIPO");
        this.EQUIPO_HABILITADO = Boolean.parseBoolean(v.get("EQUIPO_HABILITADO"));
        this.CREDENCIALES_BD = new HashMap<>();
        if(v.get("RED_CONFIGURADA")=="Si"){
            this.CREDENCIALES_BD.put("Host",v.get("HOST"));
            this.CREDENCIALES_BD.put("inst",v.get("INSTANCIA"));
            this.CREDENCIALES_BD.put("BD",v.get("NOMBRE_BASE_DATOS_PRINCIPAL"));
            this.CREDENCIALES_BD.put("Puerto",v.get("PUERTO_BD"));
            this.CREDENCIALES_BD.put("Usuario",v.get("USUARIO_BD"));
            this.CREDENCIALES_BD.put("Password",v.get("PASSWORD_BD"));
        }else{
            this.CREDENCIALES_BD.put("Host","192.168.30.99");
            this.CREDENCIALES_BD.put("inst","MSSQLSERVER17");
            this.CREDENCIALES_BD.put("BD","DataGreen");
            this.CREDENCIALES_BD.put("Puerto","");
            this.CREDENCIALES_BD.put("Usuario","sa");
            this.CREDENCIALES_BD.put("Password","A20200211sj");
        }
    }

    public Map<String, String> getCREDENCIALES_BD() {
        return CREDENCIALES_BD;
    }

    public void setCREDENCIALES_BD(Map<String, String> CREDENCIALES_BD) {
        this.CREDENCIALES_BD = CREDENCIALES_BD;
    }

    /*public String getUSUARIO_ACTUAL() {
        return USUARIO_ACTUAL;
    }

    public void setUSUARIO_ACTUAL(String USUARIO_ACTUAL) {
        this.USUARIO_ACTUAL = USUARIO_ACTUAL;
    }
*/
    /*
    public String getVERSION_APP() {
        return VERSION_APP;
    }

    public void setVERSION_APP(String VERSION_APP) {
        this.VERSION_APP = VERSION_APP;
    }

    public String getVERSION_DISPONIBLE() {
        return VERSION_DISPONIBLE;
    }

    public void setVERSION_DISPONIBLE(String VERSION_DISPONIBLE) {
        this.VERSION_DISPONIBLE = VERSION_DISPONIBLE;
    }

    public boolean isTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(boolean TOKEN) {
        this.TOKEN = TOKEN;
    }

    public LocalDateTime getULTIMA_SESION_ABIERTA() {
        return ULTIMA_SESION_ABIERTA;
    }

    public void setULTIMA_SESION_ABIERTA(LocalDateTime ULTIMA_SESION_ABIERTA) {
        this.ULTIMA_SESION_ABIERTA = ULTIMA_SESION_ABIERTA;
    }*/

    //////////////////////////////////////////////
    //METODOS COMUNES;
    public void setToken(LocalDateTime token){
        this.data.Filas(data.encontrar("TOKEN_EXPIRA",0)).Item("Clave").Valor(token.toString());
    }

    public void set(String Clave, String Valor){
        int i =this.data.encontrar(Clave,0);
        if(i>=0){
            //this.data.Filas(this.data.encontrar(Clave,0)).Item("Valor").Valor(Valor);
            this.data.Filas(i).Item("Valor").Valor(Valor);
        }else{
            String[] nombresColumnas= {"Clave","Valor"};
            String[] valores=new String[2];
            valores[0]=Clave;
            valores[1]=Valor;
            this.data.insertar(new MiFila(this.data.Filas.size(), nombresColumnas,valores));
        }

    }

    public String get(String Clave){
        int i =this.data.encontrar(Clave,0);
        if(i>=0){
            return this.data.Filas(i).Item("Valor").Valor();
        }else{
            return "!" + Clave;
        }
    }

    public MiData Filas(){
        return this.data;
    }

    public void actualizarConfiguraciones(Cursor c) {
        String clave, valor;
        while (c.moveToNext()){
            clave=c.getString(0);
            valor=c.getString(1);
            set(clave,valor);
            /*
            if(this.data.encontrar(clave,0)>0){
                set(clave,valor);
            }else{
                //PENDIENTE: ESTE BLOQUE DE CODIGO SE DEBE OPTIMIZAR CREANDO EL METOD "OBTENER NUEVA FILA" EN LA CLASE MiFila QUE RECIBE COMO PARAMETRO UN CURSO Y DEVUELVE UNA NUEVA FILA
                String[] nombresColumnas=new String[c.getColumnCount()];
                String[] valores=new String[c.getColumnCount()];
                for(int i=0; i<c.getColumnCount()-1; i++){
                    nombresColumnas[i]=c.getColumnName(i);
                    valores[i]=c.getString(i);
                }
                this.data.insertar(new MiFila(this.data.Filas.size(),nombresColumnas,valores));
            }*/
        }
    }

    public void actualizarConfiguraciones(ResultSet rs) {
        try {
            String clave, valor;
            ResultSetMetaData m;
            while (rs.next()) {
                clave = rs.getString(2);
                valor = rs.getString(3);
                set(clave, valor);
                //m=rs.getMetaData();
                //int nCol=m.getColumnCount();

                /*
                String[] nombresColumnas = {"Clave", "Valor"};
                String[] valores = new String[2];
                if (this.data.encontrar(clave, 0) >= 0) {
                    set(clave, valor);
                } else {
                    //PENDIENTE: ESTE BLOQUE DE CODIGO SE DEBE OPTIMIZAR CREANDO EL METOD "OBTENER NUEVA FILA" EN LA CLASE MiFila QUE RECIBE COMO PARAMETRO UN RESULTSET Y DEVUELVE UNA NUEVA FILA
                    //for(int i=0; i<nCol-1; i++){
                    //nombresColumnas[i]=m.getColumnName(i);
                    //  valores[i]=rs.getString(i);
                    //}
                    valores[0] = rs.getString(2);
                    valores[1] = rs.getString(3);
                    this.data.insertar(new MiFila(this.data.Filas.size(), nombresColumnas, valores));
                }*/
            }
        }catch(Exception ex){
            ex.getMessage();
        }
    }
    //////////////////////////////////////////////
/*
    //LOS SIGUIENTES METODOS SIRVEN PARA HACER A LA CLASE PARCELABLE Y PODER MANDAR SUS OBJETOS COMO PARAMETROS A OTRAS ACTIVIDADES;
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.data, i);
    }

    protected ConfiguracionLocal(Parcel in) {
        this.data=in.readParcelable(MiData.class.getClassLoader());
    }


    public static final Parcelable.Creator<ConfiguracionLocal> CREATOR = new Parcelable.Creator<ConfiguracionLocal>(){
        @Override
        public ConfiguracionLocal createFromParcel(Parcel source){
            return new ConfiguracionLocal(source);
        }

        @Override
        public ConfiguracionLocal[] newArray(int size){
            return new ConfiguracionLocal[size];
        }
    };*/
}
