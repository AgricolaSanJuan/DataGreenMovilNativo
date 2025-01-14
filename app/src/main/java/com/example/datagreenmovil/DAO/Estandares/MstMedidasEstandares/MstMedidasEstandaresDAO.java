package com.example.datagreenmovil.DAO.Estandares.MstMedidasEstandares;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.datagreenmovil.ui.estandares.EstandaresMain.Dialogs.DaoItem;

import java.util.List;
@Dao
public interface MstMedidasEstandaresDAO {
    @Insert
    void insertarMedidasEstandares(MstMedidasEstandares tiposEstandar);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void sincronizarMedidas(List<MstMedidasEstandares> medidasEstandaresList);

    @Query("SELECT * from mst_medidas_estandares")
    List<MstMedidasEstandares> obtenerMedidasEstandares();

    @Query("SELECT id, descripcion from mst_medidas_estandares")
    List<DaoItem> obtenerMedidasEstandaresParaLista();

    @Delete
    void eliminarMedidasEstandares(List<MstMedidasEstandares> trxEstandaresNew);
}