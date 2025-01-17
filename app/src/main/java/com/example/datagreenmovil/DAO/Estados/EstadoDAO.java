package com.example.datagreenmovil.DAO.Estados;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EstadoDAO {
    @Query("SELECT * FROM mst_Estados")
    List<Estado> obtenerEstados();
}
