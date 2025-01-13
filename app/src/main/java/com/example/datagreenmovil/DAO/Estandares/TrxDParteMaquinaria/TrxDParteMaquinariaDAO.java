package com.example.datagreenmovil.DAO.Estandares.TrxDParteMaquinaria;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TrxDParteMaquinariaDAO {

    @Insert
    void insertar(TrxDParteMaquinaria trxDParteMaquinaria);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void sincronizarDParteMaquinaria(List<TrxDParteMaquinaria> trxParteMaquinariaList);

    @Query("SELECT * from trx_DParteMaquinaria")
    List<TrxDParteMaquinaria> obtenerDParteMaquinaria();

    @Delete
    void eliminarDParteMaquinaria(List<TrxDParteMaquinaria> trxParteMaquinariaList);

    @Update
    void actualizarDParteMaquinaria(List<TrxDParteMaquinaria> trxParteMaquinariaList);
}