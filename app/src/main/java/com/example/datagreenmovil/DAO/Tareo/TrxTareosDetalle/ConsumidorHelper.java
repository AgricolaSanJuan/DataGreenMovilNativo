package com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle;

import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.datagreenmovil.DataGreenApp;

public class ConsumidorHelper {
    String idConsumidor;
    TareoDetallesDAO tareoDetallesDAO;


    public ConsumidorHelper(String idConsumidor) {
        this.idConsumidor = idConsumidor;
        tareoDetallesDAO = DataGreenApp.getAppDatabase().tareoDetallesDAO();
    }

    public String obtenerDecripcionConsumidor(){
        String query = "SELECT Dex FROM mst_Consumidores WHERE TRIM(Id) = '"+idConsumidor+"'";
        SupportSQLiteQuery supportSQLiteQuery = new SimpleSQLiteQuery(query);

        return tareoDetallesDAO.obtenerDecripcionConsumidor(supportSQLiteQuery);
    }
}
