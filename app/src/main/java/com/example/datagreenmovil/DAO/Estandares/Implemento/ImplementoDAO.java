package com.example.datagreenmovil.DAO.Estandares.Implemento;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ImplementoDAO {

    @Insert
    void insertarImplemento(com.example.datagreenmovil.DAO.Estandares.Implemento.Implemento Implemento);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void sincronizarImplemento(List<com.example.datagreenmovil.DAO.Estandares.Implemento.Implemento> ImplementoList);

    @Query("SELECT * from Implemento")
    List<com.example.datagreenmovil.DAO.Estandares.Implemento.Implemento> obtenerImplemento();

    @Delete
    void eliminarImplemento(List<com.example.datagreenmovil.DAO.Estandares.Implemento.Implemento> Implemento);

    @Update
    void actualizarImplemento(List<com.example.datagreenmovil.DAO.Estandares.Implemento.Implemento> Implemento);
}