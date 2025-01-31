package com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle;

import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.datagreenmovil.DAO.Tareo.TrxTareo.TareoDAO;
import com.example.datagreenmovil.DataGreenApp;
import com.example.datagreenmovil.ui.estandares.EstandaresMain.Dialogs.DaoItem;

import java.util.List;

public class ActividadHelper {
    String idActividad;
    TareoDetallesDAO tareoDetallesDAO;
    TareoDAO tareoDAO;


    public ActividadHelper(String idActividad) {
        this.idActividad = idActividad;
        tareoDetallesDAO = DataGreenApp.getAppDatabase().tareoDetallesDAO();
        tareoDAO = DataGreenApp.getAppDatabase().tareoDAO();

    }

    public ActividadHelper(){
        tareoDetallesDAO = DataGreenApp.getAppDatabase().tareoDetallesDAO();
        tareoDAO = DataGreenApp.getAppDatabase().tareoDAO();
    }

    public String obtenerDescripcionActividad(){
        String query = "SELECT Dex FROM mst_Actividades WHERE TRIM(Id) = '"+idActividad+"'";
        SupportSQLiteQuery supportSQLiteQuery = new SimpleSQLiteQuery(query);

        return tareoDetallesDAO.obtenerDescripcionActividad(supportSQLiteQuery);
    }

    public List<DaoItem> obtenerActivdadesParaLista(){
        String query ="SELECT Id id, Dex descripcion FROM MST_ACTIVIDADES " ;
        SupportSQLiteQuery supportSQLiteQuery= new SimpleSQLiteQuery(query);

        return tareoDAO.obtenerActividadLista(supportSQLiteQuery);
    }

//    public List<DaoItem> obtenerLoteParaLista(){
//        String query="";
//        SupportSQLiteQuery supportSQLiteQuery= new SimpleSQLiteQuery();
////
////
//    }

}
