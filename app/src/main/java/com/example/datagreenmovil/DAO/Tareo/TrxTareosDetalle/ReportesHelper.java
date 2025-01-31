package com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle;

import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.datagreenmovil.Conexiones.AppDatabase;
import com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle.DTO.ReporteDTO;
import com.example.datagreenmovil.DataGreenApp;

import java.util.List;

public class ReportesHelper {
    String idEmpresa;
    String idUsuario;
    String fecha;
    AppDatabase database;

    public ReportesHelper(String idEmpresa, String idUsuario, String fecha) {
        this.idEmpresa = idEmpresa;
        this.idUsuario = idUsuario;
        this.fecha = fecha;
        database = DataGreenApp.getAppDatabase();
    }

    public List<ReporteDTO> obtenerReporteTareos(String queryString) {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(queryString, new Object[]{idEmpresa, fecha, idUsuario});
        return database.tareoDetallesDAO().obtenerReporte(query);
    }
}
