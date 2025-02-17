package com.example.datagreenmovil.DAO.Tareo.TrxTareo;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.room.Upsert;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

@Dao
public interface TareoDAO {

    @Query("SELECT COUNT(*) FROM trx_Tareos WHERE id = :id")
    int existeTareo(String id);

    @Upsert
    void guardarTareo(Tareo tareo);

    @Query("SELECT * FROM trx_tareos WHERE Id = :id")
    Tareo obtenerTareoPorId(String id);

    @Query("SELECT IdEmpresa FROM trx_tareos WHERE Id = :id")
    String getIdEmpresa(String id);

    @Query("SELECT COALESCE((SELECT MAX(Id) Id FROM trx_Tareos), '000000000')")
    String getLastId();

    @Query("UPDATE trx_tareos SET Id = :nuevoId WHERE Id = :id")
    void actualizarIdTareos(String id, String nuevoId);

    @Query("UPDATE trx_tareos_detalle SET IdTareo = :nuevoId WHERE IdTareo = :id")
    void actualizarIdTareosDetalle(String id, String nuevoId);

    @RawQuery
    String getLasIdFromCorrelativos(SupportSQLiteQuery query);

    @Transaction
    default void actualizarIdTareo(String id, String nuevoId) {
        actualizarIdTareos(id, nuevoId);
        actualizarIdTareosDetalle(id, nuevoId);
    }
}