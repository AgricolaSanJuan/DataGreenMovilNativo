package com.example.datagreenmovil.Conexiones;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.datagreenmovil.DAO.Dias.Dia;
import com.example.datagreenmovil.DAO.Empresas.Empresa;
import com.example.datagreenmovil.DAO.Estados.Estado;
import com.example.datagreenmovil.DAO.Estados.EstadoDAO;
import com.example.datagreenmovil.DAO.Estandares.MstMedidasEstandares.MstMedidasEstandares;
import com.example.datagreenmovil.DAO.Estandares.MstMedidasEstandares.MstMedidasEstandaresDAO;
import com.example.datagreenmovil.DAO.Estandares.MstTiposBonoEstandar.MstTiposBonoEstandar;
import com.example.datagreenmovil.DAO.Estandares.MstTiposBonoEstandar.MstTiposBonoEstandarDAO;
import com.example.datagreenmovil.DAO.Estandares.MstTiposCostoEstandar.MstTiposCostoEstandar;
import com.example.datagreenmovil.DAO.Estandares.MstTiposCostoEstandar.MstTiposCostoEstandarDAO;
import com.example.datagreenmovil.DAO.Estandares.MstTiposEstandar.MstTiposEstandar;
import com.example.datagreenmovil.DAO.Estandares.MstTiposEstandar.MstTiposEstandarDAO;
import com.example.datagreenmovil.DAO.Estandares.ReporteEstandares.ReporteEstandaresDAO;
import com.example.datagreenmovil.DAO.Estandares.TrxEstandares.TrxEstandaresNew;
import com.example.datagreenmovil.DAO.Estandares.TrxEstandares.TrxEstandaresNewDAO;
import com.example.datagreenmovil.DAO.Tareo.TrxTareo.Tareo;
import com.example.datagreenmovil.DAO.Tareo.TrxTareo.TareoDAO;
import com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle.TareoDetalles;
import com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle.TareoDetallesDAO;
import com.example.datagreenmovil.DAO.Turnos.Turno;
import com.example.datagreenmovil.DAO.Usuarios.Usuario;
import com.example.datagreenmovil.DataGreenApp;
import com.example.datagreenmovil.Migrations.Migrations;

@Database(entities =
        {
                MstTiposEstandar.class,
                MstTiposCostoEstandar.class,
                MstTiposBonoEstandar.class,
                MstMedidasEstandares.class,
                TrxEstandaresNew.class,
                Estado.class,
                Dia.class,
                Empresa.class,
                Usuario.class,
                Turno.class,
                Tareo.class,
                TareoDetalles.class
        }, version = 5, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {

    // Método para crear la instancia de la base de datos
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase() {
        synchronized (AppDatabase.class) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(DataGreenApp.getAppContext(), AppDatabase.class, "DataGreenMovil.db")
                        .createFromAsset("DataGreenMovil.db")
                        .addMigrations(Migrations.MIGRATION_1_2)
                        .addMigrations(Migrations.MIGRATION_2_3)
                        .addMigrations(Migrations.MIGRATION_3_4)
                        .addMigrations(Migrations.MIGRATION_4_5)
                        .addCallback(new RoomDatabase.Callback() {
                            @Override
                            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                super.onOpen(db);
                                Log.i("RoomDatabase", "Base de datos abierta. Versión: " + db.getVersion());
                            }
                        })
                        .allowMainThreadQueries().build();
            }
        }
        return INSTANCE;
    }

    public abstract TrxEstandaresNewDAO estandaresNewDAO();

    public abstract MstTiposEstandarDAO mstTiposEstandarDAO();

    public abstract MstTiposCostoEstandarDAO mstTiposCostoEstandarDAO();

    public abstract MstTiposBonoEstandarDAO mstTiposBonoEstandarDAO();

    public abstract MstMedidasEstandaresDAO mstMedidasEstandaresDAO();

    public abstract ReporteEstandaresDAO reporteEstandaresDAO();

    public abstract EstadoDAO estadoDAO();

    public abstract TareoDAO tareoDAO();

    public abstract TareoDetallesDAO tareoDetallesDAO();
//    Dia.class,
//    Empresa.class,
//    Usuario.class,
//    Turno.class,
//    Tareo.class,
//    TareoDetalles.class

}
