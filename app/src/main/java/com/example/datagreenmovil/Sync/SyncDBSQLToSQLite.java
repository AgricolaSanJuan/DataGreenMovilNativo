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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

    public void sincronizarData(Context ctx, String tableName) throws SQLException {

        try{
            SharedPreferences sharedPreferences = ctx.getSharedPreferences("objConfLocal", ctx.MODE_PRIVATE);
            SQLiteDatabase database = SQLiteDatabase.openDatabase(ctx.getDatabasePath("DataGreenMovil.db").toString(), null, SQLiteDatabase.OPEN_READWRITE);
            String sqliteQuery = "select QuerySqlite, tablaObjetivo from mst_queryssqlite where NombreQuery LIKE '%descargar%' AND tablaObjetivo LIKE '"+tableName+"'";

            String redHost = sharedPreferences.getString("RED_HOST", "!RED_HOST");
            String redInstancia = sharedPreferences.getString("RED_INSTANCIA", "!RED_INSTANCIA");
            String redNombreDB = sharedPreferences.getString("RED_NOMBRE_DB", "RED_NOMBRE_DB");
            String redUsuario = sharedPreferences.getString("RED_USUARIO", "RED_USUARIO");
            String redPassword = sharedPreferences.getString("RED_PASSWORD", "RED_PASSWORD");

            String StringConnection = "jdbc:jtds:sqlserver://" + redHost + ";instance=" + redInstancia + ";databaseName=" + redNombreDB + ";user=" + redUsuario + ";password=" + redPassword + ";";
//            Log.i("Conection", StringConnection);
            Connection connection = DriverManager.getConnection(StringConnection);
//            Log.i("SQLITEQuery", sqliteQuery);
            Cursor c = database.rawQuery(sqliteQuery, null);

            c.moveToNext();
            String query = c.getString(0);
//            Log.i("result", query);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            //        resultSet.next();
            //        Log.i("EJECUTAO:", resultSet.getString(0));
            while (resultSet.next()) {
                String regis = "";
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    regis += "'" + resultSet.getString(columnName) + (i == columnCount ? "'" : "', ");
                }

                database.execSQL("INSERT OR IGNORE INTO " + c.getString(1) + " VALUES (" + regis + ")");
//                Log.i("REGISTER: " + c.getString(1), regis);
                //            registers.add(regis);
            }
            database.close();
        }catch (Exception e){
            throw e;
        }

//        });
    }
}
