package com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle;

import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.datagreenmovil.Conexiones.AppDatabase;
import com.example.datagreenmovil.DataGreenApp;

public class PersonaHelper {
    private final String dni;
    TareoDetallesDAO tareoDetallesDAO;

    public PersonaHelper(String dni) {
        this.dni = dni;
        tareoDetallesDAO = DataGreenApp.getAppDatabase().tareoDetallesDAO();
//        tareoDetallesDAO = AppDatabase.getDatabase().tareoDetallesDAO();;
    }

    public String obtenerPlanilla(){
        String query = "SELECT IdPlanilla FROM mst_personas WHERE NroDocumento = '"+dni+"'";
        SupportSQLiteQuery supportSQLiteQuery = new SimpleSQLiteQuery(query);

        return tareoDetallesDAO.obtenerPlanillaTrabajador(supportSQLiteQuery);
    }

    public String obtenerNombreTrabajador(){
        String query = "SELECT COALESCE(Paterno || ' ' || Materno || ' ' || Nombres, 'no existe') " +
                "FROM mst_Personas " +
                "WHERE NroDocumento = '" + dni + "' " +
                "LIMIT 1";

        SupportSQLiteQuery supportSQLiteQuery = new SimpleSQLiteQuery(query);

        return tareoDetallesDAO.obtenerNombresTrabajador(supportSQLiteQuery);
    }
}
