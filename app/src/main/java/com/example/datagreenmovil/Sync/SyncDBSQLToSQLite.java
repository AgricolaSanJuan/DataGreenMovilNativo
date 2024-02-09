package com.example.datagreenmovil.Sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Logica.Swal;

import org.json.JSONArray;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SyncDBSQLToSQLite {
    public void sincronizar(Context ctx, ConfiguracionLocal objConfLocal, String tableName, String tableOriginName) throws SQLException {

        //                OPENSQL CONNECTION

        SharedPreferences sharedPreferences = ctx.getSharedPreferences("objConfLocal", ctx.MODE_PRIVATE);
        SQLiteDatabase database = SQLiteDatabase.openDatabase(ctx.getDatabasePath("DataGreenMovil.db").toString(), null, SQLiteDatabase.OPEN_READWRITE);
        String results = database.rawQuery("select * from mst_usuarios",null).toString();
        Log.i("venimos finos",results);

        String redHost = sharedPreferences.getString("RED_HOST", "!RED_HOST");
        String redInstancia = sharedPreferences.getString("RED_INSTANCIA", "!RED_INSTANCIA");
        String redNombreDB = sharedPreferences.getString("RED_NOMBRE_DB", "RED_NOMBRE_DB");
        String redUsuario = sharedPreferences.getString("RED_USUARIO", "RED_USUARIO");
        String redPassword = sharedPreferences.getString("RED_PASSWORD", "RED_PASSWORD");

        String StringConnection = "jdbc:jtds:sqlserver://"+redHost+";instance="+redInstancia+";databaseName="+redNombreDB+";user="+redUsuario+";password="+redPassword+";";
        Connection connection = DriverManager.getConnection(StringConnection);

        String query = "select * from "+tableOriginName+"";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        ConexionSqlite sqliteConn = new ConexionSqlite(ctx, objConfLocal);
//        OPEN SQLITE CONNECTION

//                sqLiteDatabase.execSQL("select * from mst_usuarios;");
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        List<String> columns = new ArrayList<>();

        //POR AQUI NOS QUEDAMOS
        List<String> registers = new ArrayList<>();
        while (resultSet.next()) {
            String regis = "";
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                regis += "'"+resultSet.getString(columnName)+(i == columnCount ? "'" : "', ");
            }
            registers.add(regis);
        }

        try {
            sqliteConn.syncData(ctx, columns, registers, tableName);
            Log.i("SQLITE ACCEPT", "INSERTADO");
        }catch (SQLiteException e){
            Log.e("SQLITE EXCEPTION",e.getMessage());
        }

        // Cierra la conexión
        resultSet.close();
        statement.close();
        connection.close();

//        return tableName;
    }

    public static void sincronizarDatosUsuario(Context ctx) throws SQLException {
        try {
            // Crear un ExecutorService con un tiempo de espera de 10 segundos
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<Connection> future = executor.submit(() -> {
                SharedPreferences sharedPreferences = ctx.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
                String redHost = sharedPreferences.getString("RED_HOST", "!RED_HOST");
                String redInstancia = sharedPreferences.getString("RED_INSTANCIA", "!RED_INSTANCIA");
                String redNombreDB = sharedPreferences.getString("RED_NOMBRE_DB", "RED_NOMBRE_DB");
                String redUsuario = sharedPreferences.getString("RED_USUARIO", "RED_USUARIO");
                String redPassword = sharedPreferences.getString("RED_PASSWORD", "RED_PASSWORD");
                String StringConnection = "jdbc:jtds:sqlserver://" + redHost + ";instance=" + redInstancia + ";databaseName=" + redNombreDB + ";user=" + redUsuario + ";password=" + redPassword + ";";

                return DriverManager.getConnection(StringConnection);
            });

            // Esperar hasta 10 segundos para la conexión
            Connection connection = future.get(2, TimeUnit.SECONDS);

            // Verificar si la conexión es exitosa
            if (connection != null) {
                Statement statement = connection.createStatement();

                SQLiteDatabase database = SQLiteDatabase.openDatabase(ctx.getDatabasePath("DataGreenMovil.db").toString(), null, SQLiteDatabase.OPEN_READWRITE);
                Cursor c;
                c = database.rawQuery("SELECT * from mst_usuarios where suma != '';", null);
                while (c.moveToNext()) {
                    statement.execute("UPDATE DataGreenMovil..mst_usuarios SET Suma = '" + c.getString(8) + "' WHERE Id = '" + c.getString(1) + "'");
                }
            } else {
                // Manejar caso de conexión fallida
            }

        } catch (Exception e) {
            // Manejar excepciones
        }
    }
    public void sincronizarData(Context ctx, String tableName) throws SQLException {

        LocalDateTime now = LocalDateTime.now();

        // Formatear la fecha como una cadena
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateTime = now.format(formatter);

        try {
            sincronizarDatosUsuario(ctx);
        }catch (Exception e){

        }

        try{
            SharedPreferences sharedPreferences = ctx.getSharedPreferences("objConfLocal", ctx.MODE_PRIVATE);
            SQLiteDatabase database = SQLiteDatabase.openDatabase(ctx.getDatabasePath("DataGreenMovil.db").toString(), null, SQLiteDatabase.OPEN_READWRITE);
            String sqliteQuery = "select QuerySqlite, tablaObjetivo from mst_queryssqlite where NombreQuery LIKE '%descargar%' AND tablaObjetivo LIKE '"+tableName+"'";
            String query = "";

            String redHost = sharedPreferences.getString("RED_HOST", "!RED_HOST");
            String redInstancia = sharedPreferences.getString("RED_INSTANCIA", "!RED_INSTANCIA");
            String redNombreDB = sharedPreferences.getString("RED_NOMBRE_DB", "RED_NOMBRE_DB");
            String redUsuario = sharedPreferences.getString("RED_USUARIO", "RED_USUARIO");
            String redPassword = sharedPreferences.getString("RED_PASSWORD", "RED_PASSWORD");

            String StringConnection = "jdbc:jtds:sqlserver://" + redHost + ";instance=" + redInstancia + ";databaseName=" + redNombreDB + ";user=" + redUsuario + ";password=" + redPassword + ";";
//            Log.i("Conection", StringConnection);
            Connection connection = DriverManager.getConnection(StringConnection);
//            Log.i("SQLITEQuery", sqliteQuery);

            if(tableName.equals("mst_marcasTareo")){
                database.execSQL("CREATE TABLE IF NOT EXISTS mst_marcasTareo( id INTEGER PRIMARY KEY AUTOINCREMENT, fecha DATE, dni CHAR(8))");
                query = "EXEC DatagreenMovil..sp_obtener_marcaciones_para_tareo_diario '"+formattedDateTime+"'";
                Log.i("QUERY MARCAS", query);
                database.execSQL("DELETE FROM "+tableName+";");
            }else{
                Cursor c = database.rawQuery(sqliteQuery, null);
                c.moveToNext();
                query = c.getString(0);
                database.execSQL("DELETE FROM  "+ tableName +";");
            }

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                String regis = "";
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    if(resultSet.getString(columnName) == null && !tableName.equals("mst_marcasTareo")) {
                        regis += "' ', ";
                    }else if(resultSet.getString(columnName) == null){
                        regis += "null , ";
                    }else {
                        regis += "'" + resultSet.getString(columnName) + (i == columnCount ? "'" : "', ");
                    }
                }
                Log.i("QUERY REEMPLAZO", "INSERT OR REPLACE INTO " + tableName + " VALUES (" + regis + ")");
                database.execSQL("INSERT OR REPLACE INTO " + tableName + " VALUES (" + regis + ")");
            }
            database.close();
        }catch (Exception e){
            throw e;
        }

//        });
    }
}
