package com.example.datagreenmovil.DAO.Estandares.MstTiposBonoEstandar;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.datagreenmovil.ui.estandares.EstandaresMain.Dialogs.DaoItem;

import java.util.List;
@Dao
public interface MstTiposBonoEstandarDAO {
    @Insert
    void insertarTiposBonoEstandar(MstTiposBonoEstandar tiposEstandar);

    @Query("SELECT * from mst_tipos_bono_estandar")
    List<MstTiposBonoEstandar> obtenerTiposBonoEstandar();

    @Query("SELECT id, descripcion from mst_tipos_bono_estandar")
    List<DaoItem> obtenerTiposBonoEstandarParaLista();

    @Delete
    void eliminarTiposBonoEstandar(List<MstTiposBonoEstandar> trxEstandaresNew);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void sincronizarTiposBonoEstandar(List<MstTiposBonoEstandar> tiposBonoEstandarList);
}