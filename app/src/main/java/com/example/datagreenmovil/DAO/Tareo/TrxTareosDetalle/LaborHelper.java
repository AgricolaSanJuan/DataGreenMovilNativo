package com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle;

import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.datagreenmovil.DataGreenApp;

public class LaborHelper {
    String idLabor;
    String idActividad;
    TareoDetallesDAO tareoDetallesDAO;


    public LaborHelper(String idLabor, String idActividad) {
        this.idLabor = idLabor;
        this.idActividad = idActividad;
        tareoDetallesDAO = DataGreenApp.getAppDatabase().tareoDetallesDAO();
    }

    public String obtenerDescripcionLabor(){
        String query = "SELECT Dex FROM mst_Labores WHERE TRIM(Id) = '"+idLabor+"' AND TRIM(IdActividad) = '"+idActividad+"'";
        SupportSQLiteQuery supportSQLiteQuery = new SimpleSQLiteQuery(query);

        return tareoDetallesDAO.obtenerDescripcionLabor(supportSQLiteQuery);
    }
}
