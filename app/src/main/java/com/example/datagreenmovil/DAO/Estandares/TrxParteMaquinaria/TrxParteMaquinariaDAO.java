package com.example.datagreenmovil.DAO.Estandares.TrxParteMaquinaria;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.datagreenmovil.DAO.Estandares.TrxEstandares.TrxEstandaresNew;

import java.util.List;

@Dao
public interface TrxParteMaquinariaDAO {

    @Insert
    void insertarParteMaquinaria(TrxParteMaquinaria trxDParteMaquinaria);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void sincronizarParteMaquinaria(List<TrxParteMaquinaria> trxParteMaquinariaList);

    @Query("SELECT * from  trx_ParteMaquinaria")
    List<TrxParteMaquinaria> obtenerParteMaquinaria();

    @Delete
    void eliminarParteMaquinaria(List<TrxParteMaquinaria> trxParteMaquinariaList);

    @Update
    void actualizarParteMaquinaria(List<TrxParteMaquinaria> trxParteMaquinariaList);

    @RawQuery
    List<ReporteCabeceraParteMaquinaria> obtenerReporteCabeceraParteMaquinaria(SupportSQLiteQuery query);


}