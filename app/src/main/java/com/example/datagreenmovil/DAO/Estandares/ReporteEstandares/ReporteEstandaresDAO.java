package com.example.datagreenmovil.DAO.Estandares.ReporteEstandares;

import androidx.room.Dao;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface ReporteEstandaresDAO {
    @RawQuery
    List<ReporteEstandares> obtenerReporteEstandares(SupportSQLiteQuery query);
}