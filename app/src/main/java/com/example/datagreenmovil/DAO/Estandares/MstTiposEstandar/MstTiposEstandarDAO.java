package com.example.datagreenmovil.DAO.Estandares.MstTiposEstandar;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MstTiposEstandarDAO {
    @Insert
    void insertarTiposEstandar(MstTiposEstandar tiposEstandar);

    @Query("SELECT * from mst_tipos_estandar")
    List<MstTiposEstandar> obtenerTiposEstandar();

    @Delete
    void eliminarTiposEstandar(List<MstTiposEstandar> trxEstandaresNew);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void sincronizarTiposEstandar(List<MstTiposEstandar> trxEstandaresNew);
}