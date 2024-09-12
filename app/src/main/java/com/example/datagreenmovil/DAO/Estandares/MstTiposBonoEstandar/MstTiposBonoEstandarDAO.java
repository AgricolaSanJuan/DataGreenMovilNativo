package com.example.datagreenmovil.DAO.Estandares.MstTiposBonoEstandar;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface MstTiposBonoEstandarDAO {
    @Insert
    void insertarTiposBonoEstandar(MstTiposBonoEstandar tiposEstandar);

    @Query("SELECT * from mst_tipos_bono_estandar")
    List<MstTiposBonoEstandar> obtenerTiposBonoEstandar();

    @Delete
    void eliminarTiposBonoEstandar(List<MstTiposBonoEstandar> trxEstandaresNew);
}