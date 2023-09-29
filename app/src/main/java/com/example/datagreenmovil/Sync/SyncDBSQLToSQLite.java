package com.example.datagreenmovil.Sync;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;

import org.json.JSONArray;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SyncDBSQLToSQLite {
    public String sincronizar(Context ctx, ConfiguracionLocal objConfLocal, String tableName, String tableOriginName) throws SQLException {

        //                OPENSQL CONNECTION

        String StringConnection = "jdbc:jtds:sqlserver://192.168.30.99;instance=MSSQLSERVER17;databaseName=DataGreenMovil;user=sa;password=A20200211sj;";
        Connection connection = DriverManager.getConnection(StringConnection);

        String query = "select * from "+tableOriginName+"";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

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
        ConexionSqlite sqliteConn = new ConexionSqlite(ctx, objConfLocal);

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

        return tableName;
    }
}
