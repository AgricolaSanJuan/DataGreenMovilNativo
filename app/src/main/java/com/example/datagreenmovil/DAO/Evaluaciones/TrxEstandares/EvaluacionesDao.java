package com.example.datagreenmovil.DAO.Evaluaciones.TrxEvaluaciones;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Upsert;

@Dao

public interface EvaluacionesDao {
    @Upsert
    void guardarEvaluacion(Evaluaciones evaluaciones);

    @Query("SELECT * FROM trx_Evaluaciones WHERE Id = :id")
    Evaluaciones obtenerEvaluaciones(String id);

    @Query("SELECT Lote FROM trx_Evaluaciones WHERE Id = :id")
    Evaluaciones obtenerEvaluaciones(String id);

    @Query("SELECT nromuestra FROM trx_Evaluaciones WHERE ID = :id")
    Evaluaciones obtenerEvaluaciones(String id);

    @Query("SELECT ala FROM trx_Evaluaciones WHERE ID = :id")
    Evaluaciones obtenerEvaluaciones(String id);

    @Query("SELECT tipo_evaluacion FROM trx_Evaluaciones WHERE ID = :id")
    Evaluaciones obtenerEvaluaciones(String id);

    @Query("SELECT linea FROM trx_Evaluaciones WHERE ID = :id")
    Evaluaciones obtenerEvaluaciones(String id);

    @Query("SELECT planta FROM trx_Evaluaciones WHERE ID = :id")
    Evaluaciones obtenerEvaluaciones(String id);

    @Query("SELECT estado FROM trx_Evaluaciones WHERE ID = :id")
    Evaluaciones obtenerEvaluaciones(String id);

    @Query("SELECT item FROM trx_Evaluaciones WHERE ID = :id")
    Evaluaciones obtenerEvaluaciones(String id);

    @Query("SELECT objetoevaluable FROM trx_Evaluaciones WHERE ID = :id")
    Evaluaciones obtenerEvaluaciones(String id);

    @Query("SELECT total FROM trx_Evaluaciones WHERE ID = :id")
    Evaluaciones obtenerEvaluaciones(String id);

    @Query("SELECT dañadas FROM trx_Evaluaciones WHERE ID = :id")
    Evaluaciones obtenerEvaluaciones(String id);

    @Query("SELECT ratio FROM trx_Evaluaciones WHERE ID = :id")
    Evaluaciones obtenerEvaluaciones(String id);

    @Query("SELECT medida FROM trx_Evaluaciones WHERE ID = :id")
    Evaluaciones obtenerEvaluaciones(String id);

    @Query("SELECT muestra FROM trx_Evaluaciones WHERE ID = :id")
    Evaluaciones obtenerEvaluaciones(String id);

    @Query("SELECT lectura FROM trx_Evaluaciones WHERE ID = :id")
    Evaluaciones obtenerEvaluaciones(String id);

    @Query("SELECT clasificacion FROM trx_Evaluaciones WHERE ID = :id")
    Evaluaciones obtenerEvaluaciones(String id);


}
