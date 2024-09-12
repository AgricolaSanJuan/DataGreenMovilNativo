package com.example.datagreenmovil.Migrations;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.migration.Migration;

import com.example.datagreenmovil.DataGreenApp;

public class Migrations {
    public static final Migration MIGRATION_1_3 = new Migration(1, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            try{

                database.execSQL("CREATE TABLE IF NOT EXISTS mst_tipos_estandar (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "descripcion TEXT, " +
                        "nombreCorto TEXT" +
                        ")");

                SharedPreferences sharedPreferences = DataGreenApp.getAppContext().getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("VERSION_DB_DISPONIBLE", "3").apply();
            }catch (Exception e){
                Log.i("ERRORSQLITE", e.toString());
            }
        }
    };
}
