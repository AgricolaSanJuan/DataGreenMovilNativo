package com.example.datagreenmovil.DAO.Estandares.Maquinaria;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.datagreenmovil.DAO.Estandares.Operario.Operario;

import java.util.List;

@Dao
public interface MaquinariaDAO {

    @Insert
    void insertarMaquinaria(Maquinaria Maquinaria);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void sincronizarMaquinaria(List<Maquinaria> MaquinariaList);

    @Query("SELECT * from Maquinaria")
    List<Maquinaria> obtenerMaquinaria();

    @Delete
    void eliminarMaquinaria(List<Maquinaria> Maquinaria);

    @Update
    void actualizarMaquinaria(List<Maquinaria> Maquinaria);
}