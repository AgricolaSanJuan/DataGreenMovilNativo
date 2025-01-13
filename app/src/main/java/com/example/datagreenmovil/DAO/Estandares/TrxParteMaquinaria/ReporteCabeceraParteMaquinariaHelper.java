package com.example.datagreenmovil.DAO.Estandares.TrxParteMaquinaria;

import android.content.Context;

import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.datagreenmovil.Conexiones.AppDatabase;

import java.util.List;

public class ReporteCabeceraParteMaquinariaHelper {

    private TrxParteMaquinariaDAO trxParteMaquinariaDAO;
    public ReporteCabeceraParteMaquinariaHelper() {
        AppDatabase db = AppDatabase.getDatabase();
        trxParteMaquinariaDAO = db.trxParteMaquinariaDAO();
    }

    public List<ReporteCabeceraParteMaquinaria> getReporteCabeceraParteMaquinaria() {
        // Define la consulta SQL
        String queryObtenerReporte = "select" +
                "pm.Fecha Fecha," +
                "maq.Descripcion NombreMaquinaria," +
                "ope.NombreCompleto NombreOperario," +
                "CASE pm.IdTurno WHEN '01' THEN 'MAÑANA' WHEN '02' THEN 'TARDE' WHEN '03' THEN 'NOCHE' END Turno," +
                "SUM(dpm.TotalHoras) TotalHoras," +
                "pm.Observaciones Observacion" +
                "from trx_partemaquinaria pm" +
                "inner join trx_dpartemaquinaria dpm on dpm.IdParteMaquinaria = pm.IdParteMaquinaria" +
                "inner join `mst_consumidores_taller `  maq on maq.IdConsumidor = pm.IdMaquinaria and maq.Tipo = 'MAQ'" +
                "inner join operario ope on ope.IdOperario = pm.IdOperario";

        // Crear el objeto SupportSQLiteQuery
        SupportSQLiteQuery query = new SimpleSQLiteQuery(queryObtenerReporte);

        // Ejecutar la consulta
        return trxParteMaquinariaDAO.obtenerReporteCabeceraParteMaquinaria(query);
    }
}
