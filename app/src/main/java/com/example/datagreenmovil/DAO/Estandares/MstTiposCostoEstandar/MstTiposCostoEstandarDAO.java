package com.example.datagreenmovil.DAO.Estandares.MstTiposCostoEstandar;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.datagreenmovil.ui.estandares.EstandaresMain.Dialogs.DaoItem;

import java.util.List;

@Dao
public interface MstTiposCostoEstandarDAO {
    @Insert
    void insertarTiposCostoEstandar(MstTiposCostoEstandar tiposEstandar);

    @Query("SELECT * from mst_tipos_costo_estandar")
    List<MstTiposCostoEstandar> obtenerTiposCostoEstandar();

    @Query("SELECT id, descripcion from mst_tipos_costo_estandar")
    List<DaoItem> obtenerTiposCostoEstandarParaLista();

    @Delete
    void eliminarTiposCostoEstandar(List<MstTiposCostoEstandar> trxEstandaresNew);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void sincronizarTiposCostoEstandar(List<MstTiposCostoEstandar> tiposCostoEstandarList);
}