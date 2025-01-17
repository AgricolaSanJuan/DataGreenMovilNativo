package com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle;

import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.datagreenmovil.DataGreenApp;

public class ActividadHelper {
    String idActividad;
    TareoDetallesDAO tareoDetallesDAO;


    public ActividadHelper(String idActividad) {
        this.idActividad = idActividad;
        tareoDetallesDAO = DataGreenApp.getAppDatabase().tareoDetallesDAO();
    }

    public String obtenerDescripcionActividad(){
        String query = "SELECT Dex FROM mst_Actividades WHERE TRIM(Id) = '"+idActividad+"'";
        SupportSQLiteQuery supportSQLiteQuery = new SimpleSQLiteQuery(query);

        return tareoDetallesDAO.obtenerDescripcionActividad(supportSQLiteQuery);
    }
}
