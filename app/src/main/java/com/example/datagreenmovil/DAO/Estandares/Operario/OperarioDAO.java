package com.example.datagreenmovil.DAO.Estandares.Operario;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.datagreenmovil.DAO.Estandares.TrxDParteMaquinaria.TrxDParteMaquinaria;

import java.util.List;

@Dao
public interface OperarioDAO {

    @Insert
    void insertarOperario(Operario Operario);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void sincronizarOperario(List<Operario> OperarioList);

    @Query("SELECT * from Operario")
    List<Operario> obtenerOperario();

    @Delete
    void eliminarOperario(List<Operario> Operario);

    @Update
    void actualizarOperario(List<Operario> Operario);
}