package com.example.datagreenmovil.Conexiones;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.datagreenmovil.DAO.Estandares.ConsumidoresTaller.ConsumidoresTaller;
import com.example.datagreenmovil.DAO.Estandares.ConsumidoresTaller.ConsumidoresTallerDAO;
import com.example.datagreenmovil.DAO.Estandares.Implemento.Implemento;
import com.example.datagreenmovil.DAO.Estandares.Implemento.ImplementoDAO;
import com.example.datagreenmovil.DAO.Estandares.Maquinaria.Maquinaria;
import com.example.datagreenmovil.DAO.Estandares.Maquinaria.MaquinariaDAO;
import com.example.datagreenmovil.DAO.Estandares.MstMedidasEstandares.MstMedidasEstandares;
import com.example.datagreenmovil.DAO.Estandares.MstMedidasEstandares.MstMedidasEstandaresDAO;
import com.example.datagreenmovil.DAO.Estandares.MstTiposBonoEstandar.MstTiposBonoEstandar;
import com.example.datagreenmovil.DAO.Estandares.MstTiposBonoEstandar.MstTiposBonoEstandarDAO;
import com.example.datagreenmovil.DAO.Estandares.MstTiposCostoEstandar.MstTiposCostoEstandar;
import com.example.datagreenmovil.DAO.Estandares.MstTiposCostoEstandar.MstTiposCostoEstandarDAO;
import com.example.datagreenmovil.DAO.Estandares.MstTiposEstandar.MstTiposEstandar;
import com.example.datagreenmovil.DAO.Estandares.MstTiposEstandar.MstTiposEstandarDAO;
import com.example.datagreenmovil.DAO.Estandares.Operario.Operario;
import com.example.datagreenmovil.DAO.Estandares.Operario.OperarioDAO;
import com.example.datagreenmovil.DAO.Estandares.ReporteEstandares.ReporteEstandaresDAO;
import com.example.datagreenmovil.DAO.Estandares.TrxDParteMaquinaria.TrxDParteMaquinaria;
import com.example.datagreenmovil.DAO.Estandares.TrxDParteMaquinaria.TrxDParteMaquinariaDAO;
import com.example.datagreenmovil.DAO.Estandares.TrxEstandares.TrxEstandaresNew;
import com.example.datagreenmovil.DAO.Estandares.TrxEstandares.TrxEstandaresNewDAO;
import com.example.datagreenmovil.DAO.Estandares.TrxParteMaquinaria.TrxParteMaquinaria;
import com.example.datagreenmovil.DAO.Estandares.TrxParteMaquinaria.TrxParteMaquinariaDAO;
import com.example.datagreenmovil.DataGreenApp;
import com.example.datagreenmovil.Migrations.Migrations;

@Database(entities =
        {
            MstTiposEstandar.class,
            MstTiposCostoEstandar.class,
            MstTiposBonoEstandar.class,
            MstMedidasEstandares.class,
            TrxEstandaresNew.class,
                TrxParteMaquinaria.class,
                TrxDParteMaquinaria.class,
                Maquinaria.class,
                Implemento.class,
                Operario.class,
                ConsumidoresTaller.class
        }, version = 3, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {

    // Método para crear la instancia de la base de datos
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase() {
        synchronized (AppDatabase.class) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(DataGreenApp.getAppContext(), AppDatabase.class,"DataGreenMovil.db")
                        .createFromAsset("DataGreenMovil.db")
                        .addMigrations(Migrations.MIGRATION_1_2
                                , Migrations.MIGRATION_2_3
                ).allowMainThreadQueries().build();
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

    public abstract TrxParteMaquinariaDAO trxParteMaquinariaDAO();

    public abstract TrxDParteMaquinariaDAO trxDParteMaquinariaDAO();

    public abstract MaquinariaDAO maquinariaDAO();

    public abstract ImplementoDAO implementoDAO();

    public abstract OperarioDAO operarioDAO();

    public abstract ConsumidoresTallerDAO consumidoresTallerDAO();

}
