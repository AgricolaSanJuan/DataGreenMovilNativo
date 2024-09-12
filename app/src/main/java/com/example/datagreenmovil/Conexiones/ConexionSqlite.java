package com.example.datagreenmovil.Conexiones;

import static okio.HashingSink.md5;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.MiFila;
import com.example.datagreenmovil.Entidades.PopUpBuscarEnLista_Item;
import com.example.datagreenmovil.Entidades.Querys;
import com.example.datagreenmovil.Entidades.Registro;
import com.example.datagreenmovil.Entidades.Rex;
import com.example.datagreenmovil.Entidades.Status;
import com.example.datagreenmovil.Entidades.Tabla;
import com.example.datagreenmovil.Entidades.Tareo;
import com.example.datagreenmovil.Entidades.TareoDetalle;
import com.example.datagreenmovil.Logica.Funciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("serial")
public class ConexionSqlite extends SQLiteOpenHelper implements Serializable {

  //    public SQLiteDatabase SqliteDB;
  private static int DATABASE_VERSION = 1;
  SharedPreferences sharedPreferences;
  private static final String DATABASE_NOMBRE = "DataGreenMovil.db"; //="Prueba.db";
  //private static  final String DATABASE_TABLA="Tareos";
//  private static ConfiguracionLocal objConfLocal;
  //    private static Context objContext;

  public ConexionSqlite(@Nullable Context context, Integer dbVersion) {
    super(context, DATABASE_NOMBRE, null, dbVersion);
    sharedPreferences = context.getSharedPreferences("objConfLocal", context.MODE_PRIVATE);
  }


  //NECESARIA POR CLASE ABSTRACTA
  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    //        sqLiteDatabase.execSQL("CREATE TABLE TablaPrueba(Id text, Nombre text, Apellido text)");
    //        SqliteDB = sqLiteDatabase;
  }

  //NECESARIA POR CLASE ABSTRACTA
  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    //        sqLiteDatabase.execSQL("DROP TABLE TablaPrueba");
    //        onCreate(sqLiteDatabase);
  }

  public static boolean crearBaseDatos() {
    try (Connection conn = DriverManager.getConnection(DATABASE_NOMBRE)) {
      if (conn != null) {
        return true;
      }
      return false;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return false;
    }
  }


  //    public void crearTabla(String nombreTabla){
  //        String q = "CREATE TABLE " + nombreTabla + "(Id text, Nombre text, Apellido text";
  //        SqlDB.execSQL("CREATE TABLE TablaPrueba(Id text, Nombre text, Apellido text)");
  //    }{
  //    public String rapidin(String q){
  //        try{
  //            SqliteDB.execSQL(q);
  //            return "true";
  //        }catch(Exception ex){
  //            return ex.getMessage();
  //        }
  //    }

  public String obtenerVersionSQLITE(){
    SQLiteDatabase SqliteDB = getReadableDatabase();
    int versionDB = SqliteDB.getVersion();
    return String.valueOf(versionDB);
  }
  //PENDIENTE:MEJORAR ESTE PROCEDIMIENTO PARA QUE RECONOZCA CORRECTAMENTE EL TIPO DE EJECUCION Y TIPO DE DATO RETORNADO
  public Cursor doItBaby(String q, List<String> p, String crud) throws Exception {
    try {
      SQLiteDatabase SqliteDB;/*
            if(q.contains("?") && p == null){
                throw new Exception("La ejecucion requiere parametros que no fueron proveidos.");
            }*/
      Cursor c;
      if (crud.equals("READ")) {
        SqliteDB = getReadableDatabase();
        if (p != null) {
          //c = SqliteDB.rawQuery(q,p.toArray(new String[0]));
          c = SqliteDB.rawQuery(q, p.toArray(new String[0]));
        } else {
          c = SqliteDB.rawQuery(q, null);
        }
      } else {
        SqliteDB = getWritableDatabase();
        //SqliteDB.execSQL("CREATE TABLE TablaPrueba(Id text, Nombre text, Apellido text)");
        //Cursor c;
        if (p != null) {
          SqliteDB.execSQL(q, p.toArray());
        } else {
          SqliteDB.execSQL(q);
        }

        //                if(p!=null){
        //                    c = SqliteDB.rawQuery(q,p.toArray(new String[0]));
        //                }else{
        //                    c = SqliteDB.rawQuery(q,null);
        //                }
        c = null;
      }
      return c;
    } catch (Exception ex) {
      throw ex;
//            return null;
    }finally{
    }
  }

  public String doItBaby(String q, List<String> p, String crud, String s) throws Exception {
    try {
      SQLiteDatabase SqliteDB;
      if (q.contains("?") && p == null) {
        throw new Exception("La ejecucion requiere parametros que no fueron proveidos.");
      }
      Cursor c;
      if (crud.equals("READ")) {
        SqliteDB = getReadableDatabase();
        if (p != null) {
          c = SqliteDB.rawQuery(q, p.toArray(new String[0]));
        } else {
          c = SqliteDB.rawQuery(q, null);
        }
      } else {
        SqliteDB = getWritableDatabase();
        SqliteDB.execSQL(q);
        if (p != null) {
          c = SqliteDB.rawQuery(q, p.toArray(new String[0]));
        } else {
          c = SqliteDB.rawQuery(q, null);
        }
      }
      if (c.getCount() > 0) {
        c.moveToFirst();
        return c.getString(0);
      }
      return "";
    } catch (Exception ex) {
      throw ex;
    }
  }

    /*public int doItBaby(String q, List<String> p, int t) throws Exception{
        try{
//            q = prepararQuery(q,p);
            //SQLiteDatabase SqliteDB = getReadableDatabase();
            Cursor c = doItBaby(q,p);
            if(c.moveToFirst() && c.getString(0).length() > 0){
                return Integer.parseInt(c.getString(0));
            }
            return 0;
        }catch(Exception ex){
            throw ex;
        }
    }*/

  //    public ResultSet rapidin(String q, TiposQuerys tQ){
  //
  //        return null;
  //    }

  //    public String calquierCosa(){
  //        //Toast.makeText(cls_03000000_Login.this, "Si!", Toast.LENGTH_LONG).show();
  //        return "okis";
  //    }

  public List<String> obtenerModulos(String usuario) {
    List<String> r = new ArrayList<>();
    SQLiteDatabase bd = getReadableDatabase();
        /*
        Cursor c = bd.rawQuery("SELECT * FROM TablaPrueba",null);
        if (c.moveToFirst()){
            do{
                //r.add(c.getString(0));
            }while (c.moveToNext());
        }*/
    r.add("Tareos");
    r.add("Evaluaciones");
    r.add("Transportes");
    //Toast.makeText()
    //Toast.makeText(,"",Toast.LENGTH_LONG).show();
    return r;
  }

  public Cursor obtenerModulos() throws Exception {
    Cursor r;
    List<String> p = new ArrayList<String>();
//    SE DEJO DE USAR objConfLocal Y SE EMPEZO A USAR SHARED PREFERENCES
//    p.add(sharedPreferences.getString("ID_EMPRESA"));
    p.add(sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"));
    //SHARED PREFERENCES
    r = doItBaby(obtQuery("OBTENER MODULOS X EMPRESA"), p, "READ");
    return r;
  }

  ////////////////////////////////////////
  // VALIDACIONES;

  public Status validacionesIniciales() throws Exception {
    try {
      if (!existeBDLocal()) {
        return Status.SIN_BASE_SQLITE;
      }
      if (!existeConfiguracionLocal()) {
        return Status.CONFIGURACION_LOCAL_NO_EXISTE;
      }
      if (!equipoRegistrado()) {
        return Status.EQUIPO_NO_REGISTRADO;
      }
      return Status.OK;
    } catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }

  }

  public boolean equipoRegistrado() {
    try {
      //027 -> EQUIPO_REGISTRADO;
            /*String q = "SELECT Valor FROM trx_ConfiguracionesLocales WHERE IdEmpresa='"+objConfLocal.IDEMPRESA+"' AND MacDispositivoMovil='"+objConfLocal.MAC+"' AND IdOpcionConfiguracion='027';";
            Cursor c = doItBaby(q,null,"READ");
            return c.moveToFirst() && c.getString(0) == "SI";*/
      return true;
    } catch (Exception ex) {
      return false;
      //throw ex;
    }
  }

  public boolean equipoHabilitado() {
    try {
      //012 -> EQUIPO_HABILITADO;
            /*
            String q = "SELECT Valor FROM trx_ConfiguracionesLocales WHERE IdEmpresa='"+objConfLocal.IDEMPRESA+"' AND MacDIspositivoMovil='"+objConfLocal.MAC+"' AND IdOpcionConfiguracion='012';";
            Cursor c = doItBaby(q,null,"READ");
            return c.moveToFirst() && c.getString(0) == "SI";*/
      return true;
    } catch (Exception ex) {
      return false;
      //throw ex;
    }
  }

  public boolean existeConfiguracionLocal() throws Exception {
    try {
      //021 -> EQUIPO_CONFIGURADO;
      //String q = "SELECT Valor FROM trx_ConfiguracionesDispositivosMoviles WHERE IdEmpresa='" + sharedPreferences.getString("ID_EMPRESA") + "' AND MacDIspositivoMovil='" + sharedPreferences.getString("MAC") + "' AND ImeiDIspositivoMovil='" + sharedPreferences.getString("IMEI") + "' AND IdOpcionConfiguracion='021';";
      //PENDIENTE: SE COLOCAN VALORES EN DURO, MEJORAR!
      String q = "SELECT Valor FROM trx_ConfiguracionesDispositivosMoviles WHERE IdEmpresa='" + sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA") + "' AND MacDIspositivoMovil='" + sharedPreferences.getString("MAC","!MAC") + "' AND ImeiDIspositivoMovil='" + sharedPreferences.getString("IMEI","!IMEI") + "' AND IdOpcionConfiguracion='021';";
      Cursor c = doItBaby(q, null, "READ");
      return c.moveToFirst() && c.getString(0).equals("TRUE");
    } catch (Exception ex) {
      return false;
      //throw ex;
    }
  }

  public boolean existeBDLocal() {
    SQLiteDatabase s = getWritableDatabase();
    //        File a = new File(DATABASE_NOMBRE);
    //        return a.exists();
    //        if(a.exists()){
    //            return true;
    //        }
    //        return false;
    if (s == null) {
      return false;
    } else {
      return true;
    }
  }

//    public ConfiguracionLocal obtenerConfiguracionLocal(ConfiguracionLocal cl) {
//        try {
//            //            Map<String,String> v = new HashMap<>();
//            //            Cursor c = doItBaby("SELECT * FROM cls_02000000_Configuracion",null);
//            //            if (c.moveToFirst()){
//            //                do{
//            //                    v.put(c.getString(0),c.getString(1));
//            //                }while (c.moveToNext());
//            //            }
//            //            cl = new ConfiguracionLocal(v);
//
//            return cl;
//        } catch (Exception ex) {
//            return cl;
//        }
//    }
/*
    public ConfiguracionLocal obtenerConfiguracionLocal(){
        try{
//            Map<String,String> v = new HashMap<>();
            Cursor c = doItBaby("SELECT OC.Dex Clave, CL.Valor FROM trx_ConfiguracionesLocales CL INNER JOIN mst_OpcionesConfiguracion OC ON CL.IdOpcionConfiguracion=OC.Id WHERE OC.IdEmpresa='01'",null,"READ");
//            if (c.moveToFirst()){
//                do{
//                    v.put(c.getString(0),c.getString(1));
//                }while (c.moveToNext());
//            }
//            cl = new ConfiguracionLocal(v);
            ConfiguracionLocal cl = new ConfiguracionLocal(c);
            return cl;
        }catch (Exception ex){
            //return cl;
        }
        return null;
    }*/

  public Cursor obtenerConfiguracionLocal(ConfiguracionLocal ocl) {
    try {
      Cursor c;
      if (ocl != null) {
        String IdEmpresa = ocl.get("ID_EMPRESA");
        String Mac = ocl.get("MAC");
        String Imei = ocl.get("IMEI");
        c = doItBaby("SELECT OC.Dex Clave, CL.Valor FROM trx_ConfiguracionesDispositivosMoviles CL " +
            "INNER JOIN mst_OpcionesConfiguracion OC ON CL.IdOpcionConfiguracion=OC.Id " +
            "WHERE CL.IdEmpresa='" + IdEmpresa + "' AND MacDispositivoMovil='" + Mac + "' AND ImeiDispositivoMovil='" + Mac + "';", null, "READ");
      } else {
        c = doItBaby("SELECT OC.Dex Clave, CL.Valor FROM trx_ConfiguracionesDispositivosMoviles CL " +
            "INNER JOIN mst_OpcionesConfiguracion OC ON CL.IdOpcionConfiguracion=OC.Id " +
            "WHERE CL.IdEmpresa='01';", null, "READ");
      }
      return c;
    } catch (Exception ex) {
      //return cl;
      return null;
    }
//        return null;
  }

  public Cursor obtenerConfiguracionLocal() {
    //PENDIENTE DECLARAR AQUI METODO PARA QUE LA SELECCION DE LA CONFIGURACION SEA CON LA EMPRESA CORRESPONDIENTE
    try {
      //            Map<String,String> v = new HashMap<>();
      //Cursor c = doItBaby("SELECT OC.Dex Clave, CL.Valor FROM trx_ConfiguracionesDispositivosMoviles CL INNER JOIN mst_OpcionesConfiguracion OC ON CL.IdOpcionConfiguracion=OC.Id WHERE CL.IdEmpresa='" + sharedPreferences.getString("ID_EMPRESA") + "' AND CL.MacDIspositivoMovil='" + sharedPreferences.getString("MAC") + "' AND CL.ImeiDIspositivoMovil='" + sharedPreferences.getString("IMEI") + "'", null, "READ");

      Cursor c = doItBaby("SELECT OC.Dex Clave, CL.Valor FROM trx_ConfiguracionesDispositivosMoviles CL INNER JOIN mst_OpcionesConfiguracion OC ON CL.IdOpcionConfiguracion=OC.Id WHERE CL.IdEmpresa='01'", null, "READ");
      //            if (c.moveToFirst()){
      //                do{
      //                    v.put(c.getString(0),c.getString(1));
      //                }while (c.moveToNext());
      //            }
      //            cl = new ConfiguracionLocal(v);
      return c;
    } catch (Exception ex) {
      //return cl;
      return null;
    }
//        return null;
  }

  //    public ConfiguracionLocal procesoCarga1(ConfiguracionLocal cl){
  //        try{
  //            if (!existeBDLocal()){
  //                crearBaseSqlite();
  //            }
  //            if (!existeConfiguracionLocal()){
  //                realizarConfiguracionLocal()
  //            }
  //            cl=obtenerConfiguracionLocal(cl);
  //        }catch (Exception ex){
  //            cl.MENSAJE=ex.getMessage();
  //        }finally {
  //            return cl;
  //        }
  //    }

  public String validarUsario(String u, String s) throws Exception { //u=Usuario, s=Suma md5;
    try {
      //PENDIENTE:
      //String q = "SELECT Permisos FROM mst_Usuarios WHERE IdEmpresa='"+objConfLocal.IDEMPRESA+"' AND Id='"+u+"' AND Suma='"+s+"';";
      String q = "SELECT Permisos FROM mst_Usuarios WHERE IdEmpresa='" + sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA") + "' AND Id='" + u + "' AND Suma='" + s + "';";
      Cursor c = doItBaby(q, null, "READ");
      if (c.moveToFirst()) {
        return c.getString(0);
      } else {
        return "";
      }
      //return c.moveToFirst() && c.getString(0) == "SI";
    } catch (Exception ex) {
      //return false;
      throw ex;
    }
    //        int i= Arrays.asList(usuariosValidos).indexOf(u);
    //        if (i>=0 && paswords[i].equals(p)) {
    //            return true;
    //        }/*
        /*if(u.equals("JMERA") && s.equals("44363337")){
            return true;
        }else{
            return false;
        }*/

    //        return u.equals("JMERA")? true: false;
    //        return false;
  }

  public LocalDateTime obtenerToken() {
    try {
      //SQLiteDatabase bd = getReadableDatabase();
      SQLiteDatabase SqliteDB;
      SqliteDB = getReadableDatabase();
      //PENDIENTE: MODIFICAR A CONSULTA DINAMICA DE LA BASE SQLITE;
      Cursor c = SqliteDB.rawQuery("SELECT Valor FROM ConfiguracionLocal WHERE Clave='TOKEN_EXPIRA'", null);
      if (c.moveToFirst()) {
        return LocalDateTime.parse(c.getString(0));//, DateTimeFormatter.ofPattern("yyyy-MM-DD HH:mm:ss.SSS"));
      }
      return LocalDateTime.now();
    } catch (Exception ex) {
      //return LocalDateTime.now();
      return LocalDateTime.now().plusHours(-1); //HORAS DE DURACION DEL TOKEN;
    }
  }

  public void destruirBaseSqlite() {
    this.close();

  }

//    public void crearBaseSqlite(@Nullable Context context, ConfiguracionLocal cl) {
//        this.close();
//        this.
//        this.finalize();
//        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
//        objConfLocal = cl;
//    }

  public void LcrearConfiguracionLocal() {
    try {
      SQLiteDatabase SqliteDB = getWritableDatabase();
      //            sqLiteDatabase.execSQL("CREATE TABLE TablaPrueba(Id text, Nombre text, Apellido text)");
      SqliteDB.execSQL("CREATE TABLE IF NOT EXISTS Configuraciones(Clave text, Valor text, TipoValor text)");

      SqliteDB.execSQL("DELETE FROM Configuraciones");

      SqliteDB.execSQL("INSERT INTO Configuraciones VALUES('IMEI','Default','HARDWARE')");
      SqliteDB.execSQL("INSERT INTO Configuraciones VALUES('MACADDRESS','Default','HARDWARE')");
      SqliteDB.execSQL("INSERT INTO Configuraciones VALUES('MARCA','Default','HARDWARE')");
      SqliteDB.execSQL("INSERT INTO Configuraciones VALUES('MODELO','Default','HARDWARE')");
      SqliteDB.execSQL("INSERT INTO Configuraciones VALUES('SERIE','Default','HARDWARE')");

      SqliteDB.execSQL("INSERT INTO Configuraciones VALUES('EQUIPO_HABILITADO','true','DATAGREEN')");
      SqliteDB.execSQL("INSERT INTO Configuraciones VALUES('VERSION_APP','0','DATAGREEN')");
      SqliteDB.execSQL("INSERT INTO Configuraciones VALUES('VERSION_DB_SQLITE','0','DATAGREEN')");
      SqliteDB.execSQL("INSERT INTO Configuraciones VALUES('VERSION_DATA','0','DATAGREEN')");
      SqliteDB.execSQL("INSERT INTO Configuraciones VALUES('TOKEN','true','DATAGREEN')");
      SqliteDB.execSQL("INSERT INTO Configuraciones VALUES('TOKEN_EXPIRA','','DATAGREEN')");
      SqliteDB.execSQL("INSERT INTO Configuraciones VALUES('ID_EQUIPO','','DATAGREEN')");
      SqliteDB.execSQL("INSERT INTO Configuraciones VALUES('ULTIMA_SESION_ABIERTA','','DATAGREEN')");
      SqliteDB.execSQL("INSERT INTO Configuraciones VALUES('ULTIMO_USUARIO_ACTIVO','','DATAGREEN')");
      SqliteDB.execSQL("INSERT INTO Configuraciones VALUES('RED_CONFIGURADA','false','DATAGREEN')");
      SqliteDB.execSQL("INSERT INTO Configuraciones VALUES('RECORDAR_USUARIO','false','DATAGREEN')");
      //            return sdb;
    } catch (Exception ex) {
      throw ex;
    }

  }

  public String obtQuery(ResultSet rs, String nombreQuery) throws Exception {
    if (nombreQuery.length() == 0) {
      throw new Exception("Error! el nombre del query buscado esta vacio");
    }
    while (rs.next()) {
      if (nombreQuery.equals(rs.getString("NombreQuery"))) {
        return rs.getString("QuerySqlite");
      }
    }
    throw new Exception("Error! no existe el query de nombre: " + nombreQuery);
    //return "";
  }

  public String obtQuery(String nombreQuery) throws Exception {
    try {
      if (nombreQuery.length() == 0) {
        throw new Exception("Error! el nombre del query buscado esta vacio");
      }
      Cursor c = doItBaby("SELECT QuerySqlite FROM mst_QuerysSqlite WHERE NombreQuery='" + nombreQuery + "';", null, "READ");
      if (c.moveToFirst()) {
        String q = c.getString(0);
        return q;
      } else {
        throw new Exception("No se encuentra el query: " + nombreQuery);
      }
    } catch (Exception ex) {
      throw ex;
    }
  }

  public String obtQueryXId(String IdQuery) throws Exception {
    try {
      if (IdQuery.length() == 0) {
        throw new Exception("Error! el Id del query buscado esta vacio");
      }
      Cursor c = doItBaby("SELECT QuerySqlite FROM mst_QuerysSqlite WHERE Id='" + IdQuery + "';", null, "READ");
      c.moveToFirst();
      String q = c.getString(0);
      return q;
    } catch (Exception ex) {
      throw ex;
    }
  }

  //    public Status validacionesIniciales() {
  //        if (!existenTablas()){
  //            return Status.TABLAS_LOCALES_NO_EXISTEN;
  //        }
  //        if (!existeDataLocal()){
  //            return Status.DATA_LOCAL_NO_EXISTE;
  //        }
  //        if (!equipoHabilitado()){
  //            return Status.EQUIPO_NO_HABILITADO;
  //        }
  //        return Status.OK;
  //    }
  //
  //    private boolean equipoHabilitado() {
  //        return true;
  //    }
  //
  //    private boolean existeDataLocal() {
  //        return true;
  //    }
  //
  //    private boolean existenTablas() {
  //        return true;
  //    }

  public boolean existeDataLocal() {
    try {
      String q = "SELECT count(*) nFilas FROM mst_OpcionesConfiguracion;";
      Cursor c = doItBaby(q, null, "READ");
      c.moveToFirst();
      if (Integer.parseInt(c.getString(0)) > 0) {
        return true;
      } else {
        return false;
      }
            /*if(c.moveToFirst()){
                String r = c.getString(0);
                if(r.equals("SI")){
                    return true;
                }else{
                    return false;
                }
            }else {
                return false;
            }*/
    } catch (Exception ex) {
      //throw ex;
      return false;
    }
  }

  public boolean existenTablas() throws Exception {
    try {
      //026 -> EXISTEN_TABLAS;
      //String q = "SELECT Valor FROM trx_ConfiguracionesLocales WHERE IdEmpresa='"+objConfLocal.IDEMPRESA+"' AND MacDIspositivoMovil='"+objConfLocal.MAC+"' AND IdOpcionConfiguracion='026';";
      String q = "SELECT count(*) nTablas FROM sqlite_master WHERE type='table';";
      //String q ="SELECT * FROM mst_Estados;";
      Cursor c = doItBaby(q, null, "READ");
      //Integer v = c.getInt(0);
      c.moveToFirst();
      if (Integer.parseInt(c.getString(0)) > 1) {
        return true;
      } else {
        return false;
      }
            /*if(c.moveToFirst()){
                String r = c.getString(0);
                if(r.equals("SI")){
                    return true;
                }else{
                    return false;
                }
            }else {
                return false;
            }*/
    } catch (Exception ex) {
      //throw ex;
      return false;
    }
  }

  public Status validarEquipo() throws Exception {
    try {
      String q = "SELECT Valor FROM cls_02000000_Configuracion WHERE Clave='EquipoHabilitado'";
      Cursor c = doItBaby(q, null, "READ");
      if (c.moveToFirst()) {
        String r = c.getString(0);
        if (r.equals("SI")) {
          return Status.OK;
        } else {
          return Status.EQUIPO_NO_HABILITADO;
        }
      } else {
        return Status.OK;
      }
      //c.moveToFirst();
      //            return  r.length() > 0 && r.equals("SI") ? Status.EQUIPO_HABILITADO : Status.EQUIPO_NO_HABILITADO ;
    } catch (Exception ex) {
      //return Status.EQUIPO_NO_HABILITADO;
      throw ex;
    }
  }

  public boolean existeDataPendiente() throws Exception {
    try {
      //028 -> EXISTE_DATA_PENDIENTE;
      //LA SIGUIENTE LINEA PODRÍA PERMITIR QUE SE BORRE DATA DE OTRAS EMPRESAS EN UNA ACTUALIZACION DE SQLITE
      //String q = "SELECT Valor FROM trx_ConfiguracionesDispositivosMoviles WHERE IdEmpresa='" + sharedPreferences.getString("ID_EMPRESA") + "' AND MacDIspositivoMovil='" + sharedPreferences.getString("MAC") + "' AND ImeiDIspositivoMovil='" + sharedPreferences.getString("IMEI") + "' AND IdOpcionConfiguracion='028';";
      //POR TAL MOTIVO SE REEMPLAZA POR ESTA LINEA QUE YA NO TIENE EL FILTRO DE EMPRESA
      String q = "SELECT Valor FROM trx_ConfiguracionesDispositivosMoviles WHERE MacDIspositivoMovil='" + sharedPreferences.getString("MAC","!MAC") + "' AND ImeiDIspositivoMovil='" + sharedPreferences.getString("IMEI", "!IMEI") + "' AND IdOpcionConfiguracion='028';";
      Cursor c = doItBaby(q, null, "READ");
      return c.moveToFirst() && c.getString(0) == "TRUE";
    } catch (Exception ex) {
      return false;
      //throw ex;
    }
  }

  public String prepararQuery(String q, List<String> p) {
    //q = "Exec " + q;
    for (String item : p) {
      q = encadenar(q, item);
    }
    return q;
  }

  private String encadenar(String s, String i) {
    if (s.length() == 0) {
      s = "'" + i + "'";
    } else if (s.charAt(s.length() - 1) == '\'') {
      s = s + ", '" + i + "'";
    } else {
      s = s + " '" + i + "'";
    }
    return s;
  }



  public void crearTablas(ResultSet rsQuerys) throws Exception {
    try {
      //doItBaby("create table if not exists testTable(id varchar(8));",null);
      while (rsQuerys.next()) {
        if (rsQuerys.getString("Crud").equals("CREATE TABLE")) {
          doItBaby(rsQuerys.getString("QuerySqlite"), null, "CREATE TABLE");
        }
      }
    } catch (Exception ex) {

    }
  }

  public void crearTablas(Querys objQuerys) throws Exception {
    try {
      for (MiFila f : objQuerys.data.Filas) {
        String q = f.Item("QuerySqlite").Valor();
        if (f.Item("Crud").Valor().equals("CREATE TABLE")) {
          doItBaby(q, null, "CREATE TABLE");
        }
      }
    } catch (Exception ex) {
      throw new Exception(ex);
    }
  }

  public Cursor obtenerQuerys() throws Exception {
    try {
      //String q="EXEC sp_Dgm_ObtenerQuerys '" + objConfLocal.IDEMPRESA+"'";
      //q = prepararQuery(q,p);
      String q = "SELECT * FROM mst_QuerysSqlite;";
      Cursor c = doItBaby(q, null, "READ");
      //ResultSet r = sentencia.executeQuery(q);
      return c;
    } catch (Exception ex) {
      return null;
    }
  }

  public void guardarConfiguracionLocal(ConfiguracionLocal cl) throws Exception { //PENDIENTE: GRABAR LOCALMENTE EN SQLITE
    try {
      String IdEmpresa = sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA");
      for (MiFila f : cl.Filas().Filas) {
        List<String> p = new ArrayList<String>();
        p.add(IdEmpresa);
        p.add(sharedPreferences.getString("MAC","!MAC"));
        p.add(sharedPreferences.getString("IMEI","!IMEI"));
        p.add(IdEmpresa);
        p.add(f.Item(0));
        Cursor c = doItBaby(obtQuery("EXISTE VALOR trx_ConfiguracionesDispositivosMoviles"), p, "READ");
        c.moveToNext();
        String evaluador = c.getString(0);
        if (evaluador.equals("TRUE")) {
          p.clear();
          p.add(f.Item(1)); // valor
          p.add("AC"); //idestado
          p.add(sharedPreferences.getString("ID_USUARIO_ACTUAL","!ID_USUARIO_ACTUAL"));
          p.add(IdEmpresa);
          p.add(sharedPreferences.getString("MAC","!MAC"));
          p.add(sharedPreferences.getString("IMEI","!IMEI"));
          p.add(f.Item(0));
          doItBaby(obtQuery("ACTUALIZAR trx_ConfiguracionesDispositivosMoviles"), p, "WRITE");
        } else {
          p.clear();
          p.add(IdEmpresa);
          p.add(sharedPreferences.getString("MAC","!MAC"));
          p.add(sharedPreferences.getString("IMEI","!IMEI"));
          p.add(IdEmpresa);
          p.add(f.Item(0));
          p.add(f.Item(1));
          p.add("DGM00000");
          p.add("DGM00000");
          doItBaby(obtQuery("INSERTAR VALOR trx_ConfiguracionesDispositivosMoviles"), p, "WRITE");
        }
        p.clear();
      }
    } catch (Exception ex) {
      Log.e("ERROR AL GUARDAR", ex.toString());
      throw ex;
    }
  }

  public void GuardarTareo(Tareo t) throws Exception {
    try {
      String q = obtQuery("EXISTE ID").replace("#", "trx_Tareos");
      q = "SELECT CASE WHEN COUNT( * ) = 1 THEN 'TRUE' ELSE 'FALSE' END Existe\n" + "  FROM trx_Tareos\n" + " WHERE IdEmpresa = ? AND \n" + "       Id = ?;";
      List<String> p = new ArrayList<String>();
      p.add(t.getIdEmpresa());
      p.add(t.getId());
      Cursor c = doItBaby(q, p, "READ");
      c.moveToFirst();
      if (c.getString(0).equals("TRUE")) {
        q = obtQuery("ACTUALIZAR trx_Tareos");
        p.clear();
        p.add(t.getFecha().toString());
        p.add(t.getIdTurno());
        p.add(t.getIdEstado());
        p.add(sharedPreferences.getString("ID_USUARIO_ACTUAL","!ID_USUARIO_ACTUAL"));
        p.add("");
        p.add(Double.toString(t.getTotalHoras()));
        p.add(Double.toString(t.getTotalRdtos()));
        p.add(Double.toString(t.getTotalDetalles()));
        p.add(t.getObservaciones());
        p.add(t.getIdEmpresa());
        p.add(t.getId());
        doItBaby(q, p, "READ");
        //c.moveToFirst();
      } else {
        q = obtQuery("INSERTAR trx_Tareos");
        p = Funciones.recolectarParametros(t);
        doItBaby(q, p, "WRITE");
        //c.moveToFirst();
      }
      q = obtQuery("ELIMINAR trx_Tareos_Detalle EN BLOQUE");
      p = new ArrayList<String>();
      p.add(t.getIdEmpresa());
      p.add(t.getId());
      doItBaby(q, p, "WRITE");
      for (TareoDetalle d : t.getDetalle()) {
        GuardarTareoDetalle(d);
      }
    } catch (Exception ex) {
      throw new Exception(ex);
      //ex.getMessage();
    }

  }
  public void GuardarTareoDetalle(TareoDetalle d) throws Exception {
    try {
      String q = obtQuery("INSERTAR trx_Tareos_Detalle");
      List<String> p = new ArrayList<String>();
      //Cursor c;
      p = Funciones.recolectarParametros(d);
      doItBaby(q, p, "WRITE");
    } catch (Exception ex) {
      throw new Exception(ex);
    }
  }

  public JSONObject obtenerTareos(String whereIn) throws Exception {
    JSONObject mainJSON = new JSONObject();
    JSONArray tareosMainArray = new JSONArray();

    Cursor cMain;
    String qMain;

    Cursor cDetalle;
    String qDetalle;

    qMain = "SELECT * FROM trx_tareos where Id IN "+whereIn+";";
    cMain = doItBaby(qMain, null, "READ");
    while (cMain.moveToNext()){
      JSONObject tareoObject = new JSONObject();
      JSONArray detallesTareoMainArray = new JSONArray();
      qDetalle = "SELECT * FROM trx_tareos_detalle where IdTareo = '"+cMain.getString(1)+"';";
      cDetalle = doItBaby(qDetalle, null, "READ");
      for(int i = 0 ; i < cMain.getColumnCount(); i++){
        tareoObject.put(cMain.getColumnName(i), cMain.getString(i));
//        INSERTAR DETALLES
        while (cDetalle.moveToNext()){
          JSONObject detalleTareoObject = new JSONObject();
          for(int j = 0 ; j < cDetalle.getColumnCount(); j++){
            detalleTareoObject.put(cDetalle.getColumnName(j), cDetalle.getString(j));
          }
          detallesTareoMainArray.put(detalleTareoObject);
        }
      }
      Log.i("RECORRIENDO",detallesTareoMainArray.toString());
      tareoObject.put("detalles", detallesTareoMainArray);

//      SE ELIMINA EL ELEMENTO FechaHoraActualizacion YA QUE VARIA DE ACUERDO A LA FECHA DE TRANSFERENCIA Y PUEDE TRAER PROBLEMAS, YA QUE ES UN CAMPO QUE VARÍA DE ACUERDO A LA FECHA ACTUAL HASTA POR MILISEGUNDOS
      String FechaHoraActualizacion = tareoObject.optString("FechaHoraActualizacion", "");

      String FechaHoraTransferencia = tareoObject.optString("FechaHoraTransferencia", "");

      tareoObject.remove("FechaHoraActualizacion");
      tareoObject.remove("FechaHoraTransferencia");
      String md5String = Funciones.generarMD5(tareoObject.toString()).toLowerCase();
      tareoObject.put("FechaHoraActualizacion", FechaHoraActualizacion);
      tareoObject.put("FechaHoraTransferencia", FechaHoraTransferencia);
      tareoObject.put("MD5String", md5String);

      tareosMainArray.put(tareoObject);
      Log.i("TAREOSMAINARRAY", tareosMainArray.toString());
    }
    mainJSON.put("tareos", tareosMainArray);
    Log.i("RESULTADO TAREOS", mainJSON.toString());
    return mainJSON;
  }

  public boolean actualizarIdTareo(String newId,String oldId){
    try {
      doItBaby("PRAGMA FOREIGN_KEYS=1;", null, "WRITE");

      String query = "UPDATE trx_tareos SET ID='"+ newId +"' where ID = '"+oldId+"'";

      doItBaby(query, null, "WRITE");

      doItBaby("PRAGMA FOREIGN_KEYS=0;", null, "WRITE");
      return true;
    }catch (Exception e){
      return false;
    }
  }
  public JSONObject eliminarTareos(ArrayList<String> idSeleccionados) throws JSONException {
    JSONObject jsonObject = new JSONObject();
    Cursor c;
    String q;
    try{
      String whereIn = "(";
      for(int i = 0; i < idSeleccionados.size(); i++){
        if(i == idSeleccionados.size() - 1){
          whereIn += "'"+idSeleccionados.get(i)+"')";
          Log.i("CANTIDAD - 1", String.valueOf(i));
        }else{
          whereIn += "'"+idSeleccionados.get(i)+"',";
        }
      }
      q = "SELECT COUNT(*) FROM trx_tareos where Id IN "+whereIn+" AND IdEstado = 'TR'";
      c = doItBaby(q, null, "READ");
      c.moveToFirst();
      if(c.getInt(0) > 0){
        jsonObject.put("message", "Hay ");
        jsonObject.put("message_code", "transferido_seleccionado");
        jsonObject.put("state", true);
        return jsonObject;
      }

      q = "DELETE FROM trx_tareos_detalle WHERE IdTareo IN "+whereIn;
      c = doItBaby(q, null, "READ");
      c.moveToFirst();

      q = "DELETE FROM trx_tareos WHERE Id IN "+whereIn;
      c = doItBaby(q, null, "READ");
      c.moveToFirst();
      jsonObject.put("message", "error");
      jsonObject.put("message_code", "eliminado");
      jsonObject.put("state", true);
    }catch (Exception e){
      jsonObject.put("message", "error");
      jsonObject.put("message_code", "error");
      jsonObject.put("state", false);
    }
    return jsonObject;
  }

  public boolean GuardarRegistro(String nombreTabla, List<String> valores) throws Exception {
    //--PASO 1: OBTENER LLAVES PRIMARIAS DE LA TABLA
    String q = "SELECT P.pk PK, P.name Columna FROM sqlite_master M LEFT OUTER JOIN pragma_table_info( (M.name) ) P ON M.name <> P.name WHERE P.pk > 0 AND M.name = '@NombreTabla' ORDER BY P.cid;";
    q = q.replace("@NombreTabla", nombreTabla);
    Tabla tLLaves = new Tabla(doItBaby(q, null, "READ"));
    //--PASO 2: OBTENER NUMERO DE REGISTROS COINCIDENTES CON LAS LLAVES PRIMARIAS, SI =0 ENTONCES EL REGISTRO ES NUEVO Y SE HACE INSERT, SI = 1 EL REGISTRO YA EXISTE Y SE HACE UPDATE
    //q = "SELECT COUNT(*) Registros FROM @NombreTabla WHERE " + concatenarColumnas(tLLaves, valores, "A");
    q = "SELECT COUNT(*) Registros FROM @NombreTabla WHERE " + concatenarColumnas(tLLaves.toList(1), valores, "AND");
    q = q.replace("@NombreTabla", nombreTabla);
    Tabla tRegistros = new Tabla(doItBaby(q, null, "READ"));
    //--PASO 3: OBTENER ESTRUCTURA DE TABLA PARA CONCATENAR LA CONSULTA INSERT / UPDATE
    q = "SELECT P.cid [Index], P.name Columna, P.type Tipo FROM sqlite_master M LEFT OUTER JOIN pragma_table_info( (M.name) ) P ON M.name <> P.name WHERE M.name = '@NombreTabla' ORDER BY P.cid;";
    q = q.replace("@NombreTabla", nombreTabla);
    Tabla tEstructura = new Tabla(doItBaby(q, null, "READ"));
    boolean esNuevo = tRegistros.Filas.get(0).Item.get(0).toString().equals("0");
    //--PASO 4: GUARDAR
    if (esNuevo) {
      //--PASO 4: --SI RESGISTROS=0 => CONCATENAR INSERT
      //q = "INSERT INTO @NombreTabla VALUES(" + concatenarColumnas(tLLaves, valores, "L") + ");";
      q = "INSERT INTO @NombreTabla VALUES(" + concatenarColumnas(null, valores, ",") + ");";
      q = q.replace("@NombreTabla", nombreTabla);
      doItBaby(q, null, "WRITE");
    } else {
      //PASO 4: --SI REGISTROS=1 => CONCATENAR UPDATE
      q = "UPDATE @NombreTabla SET " + concatenarColumnas(tEstructura.toList(1), valores, ",") + " WHERE " + concatenarColumnas(tLLaves.toList(1), valores, "AND");
      q = q.replace("@NombreTabla", nombreTabla);
      doItBaby(q, null, "WRITE");
    }
    return true;
  }

  public boolean GuardarRex(ConfiguracionLocal objCl, String nombreTabla, Rex valores) throws Exception {
    try {
      //--PASO 1: OBTENER LLAVES PRIMARIAS DE LA TABLA
      String q = "SELECT P.pk PK, P.name Columna FROM sqlite_master M LEFT OUTER JOIN pragma_table_info( (M.name) ) P ON M.name <> P.name WHERE P.pk > 0 AND M.name = '@NombreTabla' ORDER BY P.cid;";
      q = q.replace("@NombreTabla", nombreTabla);
      Tabla tLLaves = new Tabla(doItBaby(q, null, "READ"));
      //--PASO 2: OBTENER NUMERO DE REGISTROS COINCIDENTES CON LAS LLAVES PRIMARIAS, SI =0 ENTONCES EL REGISTRO ES NUEVO Y SE HACE INSERT, SI = 1 EL REGISTRO YA EXISTE Y SE HACE UPDATE
      //q = "SELECT COUNT(*) Registros FROM @NombreTabla WHERE " + concatenarColumnas(tLLaves, valores, "A");
      q = "SELECT COUNT(*) Registros FROM @NombreTabla WHERE " + concatenarColumnas(tLLaves.toList(1), valores, "AND", false);
      q = q.replace("@NombreTabla", nombreTabla);
      Tabla tRegistros = new Tabla(doItBaby(q, null, "READ"));
      //--PASO 3: OBTENER ESTRUCTURA DE TABLA PARA CONCATENAR LA CONSULTA INSERT / UPDATE
      q = "SELECT P.cid [Index], P.name Columna, P.type Tipo FROM sqlite_master M LEFT OUTER JOIN pragma_table_info( (M.name) ) P ON M.name <> P.name WHERE M.name = '@NombreTabla' ORDER BY P.cid;";
      q = q.replace("@NombreTabla", nombreTabla);
      Tabla tEstructura = new Tabla(doItBaby(q, null, "READ"));
      boolean esNuevo = tRegistros.Filas.get(0).Item.get(0).toString().equals("0");
      //--PASO 4: GUARDAR
      if (esNuevo) {
        //--PASO 0: VALIDAR SI ES REGISTRO NUEVO
//                if (valores.Get("Id").length() == 0 || valores.Get("Id") == null ){
        if (!nombreTabla.contains("_Detalle")) {
          valores.Set("Id", siguienteCorrelativo(ultimoCorrelativo(nombreTabla, sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA")), 'A'));
        }
//                }
        //--PASO 4: --SI RESGISTROS=0 => CONCATENAR INSERT
        //q = "INSERT INTO @NombreTabla VALUES(" + concatenarColumnas(tLLaves, valores, "L") + ");";
        q = "INSERT INTO @NombreTabla VALUES(" + concatenarColumnas(tEstructura.toList(1), valores, ",", true) + ");";
        q = q.replace("@NombreTabla", nombreTabla);
        doItBaby(q, null, "WRITE");
      } else {
        //PASO 4: --SI REGISTROS=1 => CONCATENAR UPDATE
        q = "UPDATE @NombreTabla SET " + concatenarColumnas(tEstructura.toList(1), valores, ",", false) + " WHERE " + concatenarColumnas(tLLaves.toList(1), valores, "AND", false);
        q = q.replace("@NombreTabla", nombreTabla);
        doItBaby(q, null, "WRITE");
      }
//      ActualizarDataPendiente(objCl, false);
      if (!nombreTabla.contains("_Detalle")) {
        ActualizarCorrelativos(objCl, nombreTabla, valores.Get("Id"));
      }
      return true;
    } catch (Exception ex) {
      throw ex;
    }
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

  private String ultimoCorrelativo(String nombreTabla, String idEmpresa) {
    int longitud = 9; //PENDIENTE MEJORAR ESTE VALOR, DEBERIA DE SALIR DE LA MISMA DEFINICION DE TABLAS GUARDADA EN LA BASE DE DATOS mst_Tablas
    //NO TODAS LAS TABLAS DEBERIA TENER LA MISMA LONGITUD DE CADENA EN EL ID
    String q;
    q = "SELECT Correlativo FROM trx_Correlativos WHERE IdEmpresa = '@IdEmpresa' AND MacDispositivoMovil = '@MacDispositivoMovil' AND ImeiDispositivoMovil = '@ImeiDispositivoMovil' AND IdTabla = '@IdTabla';";
    q = q.replace("@IdEmpresa", sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"));
    q = q.replace("@MacDispositivoMovil", sharedPreferences.getString("MAC","!MAC"));
    q = q.replace("@ImeiDispositivoMovil", sharedPreferences.getString("IMEI","!IMEI"));

    try {
      q = q.replace("@IdTabla", Objects.requireNonNull(obtenerIdTabla(nombreTabla)));
      Cursor c = doItBaby(q, null, "READ");
      if (c.moveToFirst()) {
        return c.getString(0);
      } else {
        String parteIdDispostivo = sharedPreferences.getString("ID_DISPOSITIVO","!ID_DISPOSITIVO");
        StringBuilder parteCorrelativa = new StringBuilder();
        for (int i = 0; i < longitud - 1; i++) {
          parteCorrelativa.append("0");
        }
        parteCorrelativa.append("0");
        return parteIdDispostivo + parteCorrelativa;
      }
    } catch (Exception EX) {
      String parteIdDispostivo = sharedPreferences.getString("ID_DISPOSITIVO","!ID_DISPOSITIVO");
      StringBuilder parteCorrelativa = new StringBuilder();
      for (int i = 0; i < longitud; i++) {
        parteCorrelativa.append("0");
      }
      return parteIdDispostivo + parteCorrelativa;
    }
  }

  private CharSequence obtenerIdTabla(String nombreTabla) throws Exception {
    try {
      List<String> p = new ArrayList<String>();
      p.add(nombreTabla);
      Cursor c = doItBaby(obtQuery("OBTENER ID mst_Tablas"), p, "READ"); //PENDIENTE: CREAR ESTA CONSULTA SQLITE
      if (c.moveToFirst()) {
        return c.getString(0);
      }
      return null;
    } catch (Exception ex) {
      throw ex;
    }
  }

  public boolean EliminarRegistro(String nombreTabla, List<String> valores) throws Exception {
    String q;
    //--PASO 1: OBTENER ESTRUCTURA DE TABLA PARA CONCATENAR LA CONSULTA INSERT / UPDATE
    q = "SELECT P.cid [Index], P.name Columna, P.type Tipo FROM sqlite_master M LEFT OUTER JOIN pragma_table_info( (M.name) ) P ON M.name <> P.name WHERE M.name = '@NombreTabla' ORDER BY P.cid;";
    q = q.replace("@NombreTabla", nombreTabla);
    Tabla tEstructura = new Tabla(doItBaby(q, null, "READ"));
    q = "DELETE FROM @NombreTabla WHERE " + concatenarColumnas(tEstructura.toList(1), valores, "AND");
    //+ " WHERE " + concatenarColumnas(tLLaves.toList(1),valores,"AND")
    q = q.replace("@NombreTabla", nombreTabla);
    doItBaby(q, null, "WRITE");
    return true;
  }

//    public String concatenarColumnas(Tabla t, List<String> valores, String m) { //m -> MODO, DONDE 'A' = 'ASIGNAR' Y 'L' = 'LISTAR'
//        //@Jota 2022-11-11: RECORDARTORIO, IMPLEMENTAR LA ENTIDAD CELDA CON ATRIBUTOS DE COLUMNA PARA PODER ACCEDER POR EJEMPLO ASI: FILA.CELDAS("NOMBRE_DE_CELDA")
//        StringBuilder r = new StringBuilder();
//        Fila f;
//        for (int i = 0; i < t.Filas.size(); i++) {
//            f = t.Filas.get(i);
//            if (i > 0) {
//                r.append(",");
//            }
//            if (m.equals("A")) { //ASIGNAR CONCATENA ASI: COLUMNA1='VALOR1', COLUMNA1='VALOR2'...
//                r.append(f.Item.get(1).toString()).append(" ='").append(valores.get(Integer.parseInt(f.Item.get(1).toString()) - 1)).append("'");
//            } else if (m.equals("L")) { //LISTAR CONCATENA ASI: 'VALOR1','VALOR2'...
//                r.append(" '").append(valores.get(Integer.parseInt(f.Item.get(1).toString()) - 1)).append("'");
//            }
//
//        }
//        return r.toString();
//    }

  public String concatenarColumnas(List<String> Cols, List<String> Vals, String separador) {
    StringBuilder r = new StringBuilder();
    try {
//            if (Cols != null && (Cols.size() != Vals.size())) {
//                throw new Exception("Las lista de columnas y valores son de diferente tamaño");
//            }
      int hasta = Math.min(Cols == null ? Vals.size() : Cols.size(), Vals.size());
      for (int i = 0; i < hasta; i++) {
        if (i > 0) {
          r.append(" ").append(separador).append(" ");
        }
        if (Cols != null) { //ASIGNAR CONCATENA ASI: COLUMNA1='VALOR1', COLUMNA1='VALOR2'...
          r.append(Cols.get(i)).append(" ='").append(Vals.get(i)).append("'");
        } else { //LISTAR CONCATENA ASI: 'VALOR1','VALOR2'...
          r.append(" '").append(Vals.get(i)).append("'");
        }
      }
      return r.toString().replace(" 'null' ,", " null ,");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return r.toString();
  }

  public String concatenarColumnas(List<String> Cols, Rex Vals, String separador, boolean inserta) {
    StringBuilder r = new StringBuilder();
    try {
//            if (Cols != null && (Cols.size() != Vals.size())) {
//                throw new Exception("Las lista de columnas y valores son de diferente tamaño");
//            }
      int hasta = Cols.size(); //Math.min(Cols == null ? Vals.size() : Cols.size(), Vals.size());
      for (int i = 0; i < hasta; i++) {
        if (i > 0) {
          r.append(" ").append(separador).append(" ");
        }
        if (inserta) { //LISTAR CONCATENA ASI: 'VALOR1','VALOR2'...
          //r.append(" '").append(Vals.Get(i)).append("'");
          r.append(" '").append(Vals.Get(Cols.get(i).toString())).append("'");
        } else {//ASIGNAR CONCATENA ASI: COLUMNA1='VALOR1', COLUMNA1='VALOR2'...
          r.append(Cols.get(i)).append(" ='").append(Vals.Get(Cols.get(i).toString())).append("'");
        }
      }
      return r.toString().replace(" 'null' ,", " null ,");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return r.toString();
  }

  public void ActualizarId(String tabla, String idEmpresa, String id, String idTrx) throws Exception {
    try {
      List<String> p = new ArrayList<String>();
      p.add(idTrx);
      p.add(idEmpresa);
      p.add(id);
      //update tabla set id =? where idempresa=? and id=?
      //p.add(f.Item(1));
      if (tabla.equals("trx_Tareos")) {
         doItBaby(obtQuery("ACTUALIZAR ID trx_Tareos"), p, "WRITE");
      } else if (tabla.equals("trx_ServiciosTransporte")) {
        doItBaby(obtQuery("ACTUALIZAR ID trx_ServiciosTransporte"), p, "WRITE");
      }
    } catch (Exception ex) {
      throw ex;
    }
  }

  public void ActualizarCorrelativos(ConfiguracionLocal objConfLocal_l, String NombreTabla, String idCorrelativo) throws Exception {
    try {
      List<String> p = new ArrayList<String>();
      p.add(NombreTabla);
      Cursor c = doItBaby(obtQuery("OBTENER ID mst_Tablas"), p, "READ");
      c.moveToFirst();
      String idTabla = c.getString(0);
      String fechaHora;// = dateFormat.format(date);
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      LocalDateTime now = LocalDateTime.now();
      fechaHora = dtf.format(now);
      List<String> l = new ArrayList<>();
      l.add(sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"));
      l.add(sharedPreferences.getString("MAC","!MAC"));
      l.add(sharedPreferences.getString("IMEI","!IMEI"));
      l.add(idTabla);
      l.add(idCorrelativo);
      l.add(sharedPreferences.getString("ID_USUARIO_ACTUAL","!ID_USUARIO_ACTUAL"));
      l.add(fechaHora);
      l.add(sharedPreferences.getString("ID_USUARIO_ACTUAL","!ID_USUARIO_ACTUAL"));
      l.add(fechaHora);
      GuardarRegistro("trx_Correlativos", l);
    } catch (Exception ex) {
      throw ex;
    }
  }

  public void ActualizarDataPendiente(ConfiguracionLocal objConfLocal) throws Exception {
    try {
      Cursor c = doItBaby(obtQuery("EXISTE DATA PENDIENTE"), null, "READ");
      c.moveToFirst();
      if (c.getString(0).equals("1")) {
//        objConfLocal.set("EXISTE_DATA_PENDIENTE", "TRUE");

        sharedPreferences.edit().putString("EXISTE_DATA_PENDIENTE","TRUE").apply();
      } else {
//        objConfLocal.set("EXISTE_DATA_PENDIENTE", "FALSE");
        sharedPreferences.edit().putString("EXISTE_DATA_PENDIENTE","FALSE").apply();
      }
//      guardarConfiguracionLocal(objConfLocal);
    } catch (Exception ex) {
      throw ex;
    }
  }

  public ConfiguracionLocal ActualizarDataPendiente(ConfiguracionLocal objConfLocal, boolean retorno) throws Exception {
    try {
      Cursor c = doItBaby(obtQuery("EXISTE DATA PENDIENTE"), null, "READ");
      c.moveToFirst();
      if (c.getString(0).equals("1")) {
//        objConfLocal.set("EXISTE_DATA_PENDIENTE", "TRUE");
        sharedPreferences.edit().putString("EXISTE_DATA_PENDIENTE","TRUE").apply();
      } else {
//        objConfLocal.set("EXISTE_DATA_PENDIENTE", "FALSE");
        sharedPreferences.edit().putString("EXISTE_DATA_PENDIENTE","FALSE").apply();
      }
//      guardarConfiguracionLocal(objConfLocal);
      return new ConfiguracionLocal(obtenerConfiguracionLocal());
    } catch (Exception ex) {
      throw ex;
    }
  }

  public ArrayList<PopUpBuscarEnLista_Item> arrayParaXaPopUpBuscarEnLista(Cursor cursor) {
    ArrayList<PopUpBuscarEnLista_Item> r = new ArrayList<>();
    PopUpBuscarEnLista_Item item = null;
    if (cursor.moveToFirst()) {
      do {
        item = new PopUpBuscarEnLista_Item();
        item.setIcono(0);
        item.setId(cursor.getString(0));
        item.setDescripcion(cursor.getString(1));
        r.add(item);
      } while (cursor.moveToNext());
    }
    cursor.close();
    return r;
  }

  public Registro CursorARegistro(Cursor cursor) {
    Registro r = new Registro();
    String[] columnas = cursor.getColumnNames();
    if (cursor.moveToFirst()) {
      for (int i = 0; i < columnas.length; i++) {
        r.put(columnas[i], cursor.getString(i));
      }
    }
    cursor.close();
    return r;
  }

  public Rex CursorARex(Cursor cursor) throws Exception {
    Rex r = new Rex();
    if (cursor != null) {
      boolean existeData = cursor.moveToFirst();
      String[] columnas = cursor.getColumnNames();
      for (int i = 0; i < columnas.length; i++) {
        r.Set(columnas[i], existeData ? cursor.getString(i) : "");
      }
      cursor.close();
    }
        /*else {
            r = new Rex(this,"trx_ServiciosTransporte");
        }*/
    return r;
        /*Rex r = new Rex();
        if(cursor!=null){
            String[] columnas = cursor.getColumnNames();
            if (cursor.moveToFirst()){
                for (int i=0; i<columnas.length;i++){
                    r.Set(columnas[i],cursor.getString(i));
                }
            }else {
                for (int i=0; i<columnas.length;i++){
                    r.Set(columnas[i],"");
                }
            }
            cursor.close();
        }*//*else {
            r = new Rex(this,"trx_ServiciosTransporte");
        }*//*
        return r;*/
  }

  public boolean cambiarContrasenia(String idUsuarioActual, String nuevaContrasenia) throws Exception {
    try {
      List<String> p = new ArrayList<String>();
      p.add(nuevaContrasenia);
      p.add(idUsuarioActual);
      Cursor c = doItBaby(obtQuery("CAMBIAR CONTRASENIA"), p, "WRITE");
      return true;
    } catch (Exception ex) {
      throw ex;
    }
  }

  public void syncData(Context ctx, List<String> listaColumnas, List<String> listaRegistros, String tableName) {
    SQLiteDatabase SqliteDB;
    SqliteDB = getWritableDatabase();
    try{
      for (int i = 0; i < listaRegistros.size(); i ++){
      SqliteDB.execSQL("INSERT OR REPLACE INTO "+tableName+" VALUES ("+listaRegistros.get(i)+");");
      }
    }catch (SQLiteException e){
    }

  }

  public void insertInto(Context ctx, HashMap<String, String> paramsBus, String tableName) {
    List<String> keys = new ArrayList<>(paramsBus.keySet());

//    Object[] valuesTable = paramsBus.entrySet();
    List<Object> values = new ArrayList<>(paramsBus.values());

    Toast.makeText(ctx, keys.get(0), Toast.LENGTH_SHORT).show();
    SQLiteDatabase SqliteDB;

    SqliteDB = getReadableDatabase();
    Cursor c;
    String query, columnInsert, valuesInsert;
    columnInsert = "";
    valuesInsert = "";
    int counterKeys = keys.size();
    int runnerKeys = 0;
    String keystring = "";

    for (String element : keys
    ) {
      runnerKeys++;
      if (runnerKeys < counterKeys) {
        columnInsert += element + ",";
        valuesInsert += "?,";

      } else {
        columnInsert += element;
        Log.i("key: ", element);
        valuesInsert += "?";
      }
    }

    for (String columnName:keys
         ) {
      Log.i("column: ",columnName);
      query = "SELECT CASE WHEN EXISTS (SELECT 1 FROM sqlite_master WHERE type='table' AND name='" + tableName + "' AND sql LIKE '%"+columnName+"%') THEN 1 ELSE 0 END;";
      c = SqliteDB.rawQuery(query, null);
      c.moveToFirst();
      if (c.getString(0) == "1") {
        SqliteDB.execSQL("ALTER TABLE mst_Vehiculos ADD COLUMN Placa Varchar(255);");
        Log.i(columnName, c.getString(0));
        c.close();
      }
      keystring += columnName;

    }

//    SqliteDB.execSQL("INSERT INTO mst_Vehiculos ("+columnInsert+") values ("+valuesInsert+");",values.toArray());
//
//
//
////        VERIFY PROVEEDOR
//    query = "SELECT CASE WHEN EXISTS (SELECT 1 FROM sqlite_master WHERE type='table' AND name='mst_Vehiculos' AND sql LIKE '%IdProveedor%') THEN 1 ELSE 0 END;";
//    c = SqliteDB.rawQuery(query, null);
//    c.moveToFirst();
//    if (c.getString(0) == "1") {
//      SqliteDB.execSQL("ALTER TABLE mst_Vehiculos ADD COLUMN IdProveedor Varchar(255);");
//    }
//
////        VERIFY MARCA
//    query = "SELECT CASE WHEN EXISTS (SELECT 1 FROM sqlite_master WHERE type='table' AND name='mst_Vehiculos' AND sql LIKE '%Marca%') THEN 1 ELSE 0 END;";
//    c = SqliteDB.rawQuery(query, null);
//    c.moveToFirst();
//    if (c.getString(0) == "1") {
//      SqliteDB.execSQL("ALTER TABLE mst_Vehiculos ADD COLUMN Marca Varchar(255);");
//    }
//
////        VERIFY MODELO
//    query = "SELECT CASE WHEN EXISTS (SELECT 1 FROM sqlite_master WHERE type='table' AND name='mst_Vehiculos' AND sql LIKE '%Modelo%') THEN 1 ELSE 0 END;";
//    c = SqliteDB.rawQuery(query, null);
//    c.moveToFirst();
//    if (c.getString(0) == "1") {
//      SqliteDB.execSQL("ALTER TABLE mst_Vehiculos ADD COLUMN Modelo Varchar(255);");
//    }
//
////        VERIFY COLOR
//    query = "SELECT CASE WHEN EXISTS (SELECT 1 FROM sqlite_master WHERE type='table' AND name='mst_Vehiculos' AND sql LIKE '%Color%') THEN 1 ELSE 0 END;";
//    c = SqliteDB.rawQuery(query, null);
//    c.moveToFirst();
//    if (c.getString(0) == "1") {
//      SqliteDB.execSQL("ALTER TABLE mst_Vehiculos ADD COLUMN Color Varchar(255);");
//    }
//
////        VERIFY CAPACIDAD
//    query = "SELECT CASE WHEN EXISTS (SELECT 1 FROM sqlite_master WHERE type='table' AND name='mst_Vehiculos' AND sql LIKE '%Capacidad%') THEN 1 ELSE 0 END;";
//    c = SqliteDB.rawQuery(query, null);
//    c.moveToFirst();
//    if (c.getString(0) == "1") {
//      SqliteDB.execSQL("ALTER TABLE mst_Vehiculos ADD COLUMN Capacidad Varchar(255);");
//    }


  }

    public boolean verificarExistenciaMarca(String dni, boolean validarMarca) {
      if(validarMarca){
        return true;
      }
        SQLiteDatabase SqliteDB;
        SqliteDB = getReadableDatabase();
        Cursor c;

        c = SqliteDB.rawQuery("SELECT COUNT(*) FROM mst_marcasTareo WHERE Dni = '"+dni+"'", null);
        c.moveToFirst();
        if(c.getInt(0) > 0){
          return true;
        }else {
          return false;
      }
    }

    public boolean verificarExistenciaPersona(String dni){
      SQLiteDatabase sqLiteDatabase;
      sqLiteDatabase = getReadableDatabase();
      Cursor c;

      c = sqLiteDatabase.rawQuery("SELECT COUNT(*) FROM mst_personas WHERE Nrodocumento = '"+dni+"'", null);
      c.moveToFirst();
      if(c.getInt(0) > 0){
        return true;
      }else{
        return false;
      }
    }

    public void alterarColumnasEnServicioTransporte() {
      SQLiteDatabase db = this.getWritableDatabase();

      String column_name = "coordenadas_marca";

      // Verificar si la columna existe
      Cursor cursor = db.rawQuery("PRAGMA table_info(trx_ServiciosTransporte_Detalle)", null);
      boolean columnExists = false;

      while (cursor.moveToNext()) {
        String currentColumn = cursor.getString(cursor.getColumnIndex("name"));
        if (column_name.equals(currentColumn)) {
          columnExists = true;
          break;
        }
      }
      cursor.close();

      // Si la columna no existe, agregarla
      if (!columnExists) {
        String alterTableSQL = "ALTER TABLE trx_ServiciosTransporte_Detalle ADD COLUMN coordenadas_marca VARCHAR(100)";
        db.execSQL(alterTableSQL);
      }
    }
}