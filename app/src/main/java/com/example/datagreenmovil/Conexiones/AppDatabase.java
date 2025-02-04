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
import com.example.datagreenmovil.DAO.Evaluaciones.MstAlas.MstAlas;
import com.example.datagreenmovil.DAO.Evaluaciones.MstAlas.MstAlasDAO;
import com.example.datagreenmovil.DAO.Evaluaciones.MstLote.MstLote;
import com.example.datagreenmovil.DAO.Evaluaciones.MstLote.MstLoteDAO;
import com.example.datagreenmovil.DAO.Evaluaciones.MstObjetivosEvaluables.MstObjetivosEvaluables;
import com.example.datagreenmovil.DAO.Evaluaciones.MstObjetivosEvaluables.MstObjetivosEvaluablesDAO;
import com.example.datagreenmovil.DAO.Evaluaciones.MstOrganosPlanta.MstOrganosPlanta;
//import com.example.datagreenmovil.DAO.Evaluaciones.MstOrganosPlanta.MstOrganosPlantaDAO;
import com.example.datagreenmovil.DAO.Evaluaciones.MstOrganosPlanta.MstOrganosPlantaDAO;
import com.example.datagreenmovil.DAO.Evaluaciones.MstTipoTrampa.MstTipoTrampa;
import com.example.datagreenmovil.DAO.Evaluaciones.MstTipoTrampa.MstTipoTrampaDAO;
import com.example.datagreenmovil.DAO.Evaluaciones.TrxEvaluaciones.TrxEvaluaciones;
import com.example.datagreenmovil.DAO.Evaluaciones.TrxEvaluacionesDetalle.TrxEvaluacionesDetalle;
import com.example.datagreenmovil.DAO.Evaluaciones.TrxEvaluacionesDetalle.TrxEvaluacionesDetalleDAO;
import com.example.datagreenmovil.DAO.Tareo.TrxTareo.Tareo;
import com.example.datagreenmovil.DAO.Tareo.TrxTareo.TareoDAO;
import com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle.TareoDetalles;
import com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle.TareoDetallesDAO;
import com.example.datagreenmovil.DAO.Turnos.Turno;
import com.example.datagreenmovil.DAO.Usuarios.Usuario;
import com.example.datagreenmovil.DataGreenApp;
import com.example.datagreenmovil.EvaluacionesActivity;
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
                TareoDetalles.class,
                MstAlas.class,
                MstLote.class,
                MstObjetivosEvaluables.class,
                MstOrganosPlanta.class,
                MstTipoTrampa.class,
                TrxEvaluaciones.class,
                TrxEvaluacionesDetalle.class,
        }, version = 4, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {

    // Método para crear la instancia de la base de datos
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase() {
        synchronized (AppDatabase.class) {
            if (INSTANCE == null) {
                try {
                    INSTANCE = Room.databaseBuilder(DataGreenApp.getAppContext(), AppDatabase.class, "DataGreenMovil.db")
                            .createFromAsset("DataGreenMovil.db")
                            .addMigrations(Migrations.MIGRATION_1_2)
                            .addMigrations(Migrations.MIGRATION_2_3)
                            .addMigrations(Migrations.MIGRATION_3_4)
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                    super.onOpen(db);
                                    Log.i("RoomDatabase", "Base de datos abierta. Versión: " + db.getVersion());
                                }
                            })
                            .allowMainThreadQueries().build();
                } catch (Exception e) {
                    INSTANCE = null;
                }
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

    public abstract MstAlasDAO mstAlasDAO();

    public abstract MstLoteDAO mstLoteDAO();

    public  abstract MstObjetivosEvaluablesDAO mstObjetivosEvaluablesDAO();

    public abstract MstOrganosPlantaDAO mstOrganosPlantaDAO();

    public abstract MstTipoTrampaDAO mstTipoTrampaDAO();

    public abstract TrxEvaluacionesDetalleDAO trxEvaluacionesDetalleDAO();



//    Dia.class,
//    Empresa.class,
//    Usuario.class,
//    Turno.class,
//    Tareo.class,
//    TareoDetalles.class
}
