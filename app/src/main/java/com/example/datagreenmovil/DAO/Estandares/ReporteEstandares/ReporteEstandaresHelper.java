package com.example.datagreenmovil.DAO.Estandares.ReporteEstandares;

import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import android.content.Context;

import com.example.datagreenmovil.Conexiones.AppDatabase;

import java.util.List;

public class ReporteEstandaresHelper {
    private ReporteEstandaresDAO reporteEstandaresDAO;

    public ReporteEstandaresHelper(Context context) {
        AppDatabase db = AppDatabase.getDatabase();
        reporteEstandaresDAO = db.reporteEstandaresDAO();
    }

    public List<ReporteEstandares> getReporteEstandares() {
        // Define la consulta SQL
        String queryObtenerReporte = "SELECT " +
                "    TRIM(en.id) AS id, " +
                "    TRIM(en.idEmpresa) AS idEmpresa, " +
                "    TRIM(te.descripcion) AS tipoEstandar, " +
                "    TRIM(a.Id) AS idActividad, " +
                "    TRIM(a.Dex) AS descripcionActividad, " +
                "    TRIM(l.Id) AS idLabor, " +
                "    TRIM(l.Dex) AS descripcionLabor, " +
                "    TRIM(en.periodo) AS periodo, " +
                "    TRIM(en.fechaInicio) AS fechaInicio, " +
                "    TRIM(me.descripcion) AS medida, " +
                "    TRIM(en.cantidad) AS cantidad, " +
                "    TRIM(en.precio) AS precio, " +
                "    TRIM(en.base) AS base, " +
                "    TRIM(tbe.descripcion) AS tipoBono, " +
                "    TRIM(en.horas) AS horas, " +
                "    TRIM(tce.descripcion) AS tipoCosto, " +
                "    TRIM(en.idConsumidor) AS idConsumidor " +
                "FROM trx_estandares_new en " +
                "INNER JOIN mst_Actividades a ON a.Id = en.idActividad " +
                "INNER JOIN mst_Labores l ON l.Id = en.idLabor AND l.IdActividad = en.idActividad " +
                "INNER JOIN mst_tipos_bono_estandar tbe ON tbe.id = en.idTipoBonoEstandar " +
                "INNER JOIN mst_tipos_costo_estandar tce ON tce.id = en.idTipoCostoEstandar " +
                "INNER JOIN mst_tipos_estandar te ON te.id = en.idTipoEstandar " +
                "INNER JOIN mst_medidas_estandares me ON me.id = en.idMedidaEstandar";

        // Crear el objeto SupportSQLiteQuery
        SupportSQLiteQuery query = new SimpleSQLiteQuery(queryObtenerReporte);

        // Ejecutar la consulta
        return reporteEstandaresDAO.obtenerReporteEstandares(query);
    }
}