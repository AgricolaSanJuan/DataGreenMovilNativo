package com.example.datagreenmovil.DAO.Estandares.ConsumidoresTaller;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.datagreenmovil.DAO.Estandares.Implemento.Implemento;

import java.util.List;

@Dao
public interface ConsumidoresTallerDAO {

    @Insert
    void insertarConsumidoresTaller(com.example.datagreenmovil.DAO.Estandares.ConsumidoresTaller.ConsumidoresTaller ConsumidoresTaller);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void sincronizarMaquinariasTaller(List<ConsumidoresTaller> ConsumidoresTallerList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void sincronizarImplementosTaller(List<ConsumidoresTaller> ConsumidoresTallerList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void sincronizarCombustiblesTaller(List<ConsumidoresTaller> ConsumidoresTallerList);

    @Query("SELECT * FROM `MST_CONSUMIDORES_TALLER ` WHERE TIPO = 'MAQ'")
    List<ConsumidoresTaller> obtenerMaquinarias();

    @Query("SELECT * FROM `MST_CONSUMIDORES_TALLER ` WHERE TIPO = 'IMP'")
    List<ConsumidoresTaller> obtenerImplementos();

    @Query("SELECT * FROM `MST_CONSUMIDORES_TALLER ` WHERE TIPO = 'COM'")
    List<ConsumidoresTaller> obtenerCombustibles();

    @Query("SELECT * from `mst_consumidores_taller `")
    List<com.example.datagreenmovil.DAO.Estandares.ConsumidoresTaller.ConsumidoresTaller> obtenerConsumidoresTaller();

    @Delete
    void eliminarConsumidoresTaller(List<com.example.datagreenmovil.DAO.Estandares.ConsumidoresTaller.ConsumidoresTaller> ConsumidoresTaller);

    @Update
    void actualizarConsumidoresTaller(List<com.example.datagreenmovil.DAO.Estandares.ConsumidoresTaller.ConsumidoresTaller> ConsumidoresTaller);

}
