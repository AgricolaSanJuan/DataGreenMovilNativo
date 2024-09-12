package com.example.datagreenmovil.DAO.Estandares.TrxEstandares;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TrxEstandaresNewDAO {

    @Insert
    void insertarEstandares(TrxEstandaresNew trxEstandaresNew);

    @Insert
    void sincronizarEstandares(List<TrxEstandaresNew> trxEstandaresNew);

    @Query("SELECT * from trx_estandares_new")
    List<TrxEstandaresNew> obtenerEstandares();

    @Delete
    void eliminarEstandares(List<TrxEstandaresNew> trxEstandaresNew);
}