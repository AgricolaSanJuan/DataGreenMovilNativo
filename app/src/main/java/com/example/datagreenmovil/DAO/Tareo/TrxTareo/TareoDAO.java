package com.example.datagreenmovil.DAO.Tareo.TrxTareo;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Upsert;

@Dao
public interface TareoDAO {

    @Upsert
    void guardarTareo(Tareo tareo);

    @Query("SELECT * FROM trx_tareos WHERE Id = :id")
    Tareo obtenerTareoPorId(String id);

    @Query("SELECT IdEmpresa FROM trx_tareos WHERE Id = :id")
    String getIdEmpresa(String id);

    @Query("SELECT COALESCE((SELECT MAX(Id) Id FROM trx_Tareos), '000000000')")
    String getLastId();
}