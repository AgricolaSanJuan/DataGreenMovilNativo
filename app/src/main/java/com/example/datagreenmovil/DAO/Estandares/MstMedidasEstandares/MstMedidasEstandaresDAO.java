package com.example.datagreenmovil.DAO.Estandares.MstMedidasEstandares;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface MstMedidasEstandaresDAO {
    @Insert
    void insertarMedidasEstandares(MstMedidasEstandares tiposEstandar);

    @Query("SELECT * from mst_medidas_estandares")
    List<MstMedidasEstandares> obtenerMedidasEstandares();

    @Delete
    void eliminarMedidasEstandares(List<MstMedidasEstandares> trxEstandaresNew);
}