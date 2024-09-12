package com.example.datagreenmovil.Conexiones;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.datagreenmovil.DAO.Estandares.MstMedidasEstandares.MstMedidasEstandares;
import com.example.datagreenmovil.DAO.Estandares.MstMedidasEstandares.MstMedidasEstandaresDAO;
import com.example.datagreenmovil.DAO.Estandares.MstTiposBonoEstandar.MstTiposBonoEstandar;
import com.example.datagreenmovil.DAO.Estandares.MstTiposBonoEstandar.MstTiposBonoEstandarDAO;
import com.example.datagreenmovil.DAO.Estandares.MstTiposCostoEstandar.MstTiposCostoEstandar;
import com.example.datagreenmovil.DAO.Estandares.MstTiposCostoEstandar.MstTiposCostoEstandarDAO;
import com.example.datagreenmovil.DAO.Estandares.MstTiposEstandar.MstTiposEstandar;
import com.example.datagreenmovil.DAO.Estandares.MstTiposEstandar.MstTiposEstandarDAO;
import com.example.datagreenmovil.DAO.Estandares.TrxEstandares.TrxEstandaresNew;
import com.example.datagreenmovil.DAO.Estandares.TrxEstandares.TrxEstandaresNewDAO;
import com.example.datagreenmovil.Migrations.Migrations;

@Database(entities = {
        MstTiposEstandar.class,
        MstTiposCostoEstandar.class,
        MstTiposBonoEstandar.class,
        MstMedidasEstandares.class,
        TrxEstandaresNew.class
}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    public abstract MstTiposEstandarDAO mstTiposEstandarDAO();
    public abstract MstTiposCostoEstandarDAO mstTiposCostoEstandarDAO();
    public abstract MstTiposBonoEstandarDAO mstTiposBonoEstandarDAO();
    public abstract MstMedidasEstandaresDAO mstMedidasEstandaresDAO();
    public abstract TrxEstandaresNewDAO estandaresNewDAO();

    // Método para crear la instancia de la base de datos
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "DataGreenMovil.db")
                            // Agrega la migración aquí
                            .addMigrations(Migrations.MIGRATION_1_3)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
