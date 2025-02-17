package com.example.datagreenmovil.DAO.Evaluaciones.MstAlas;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Upsert;

import com.example.datagreenmovil.DAO.Estandares.TrxEstandares.TrxEstandaresNew;
import com.example.datagreenmovil.ui.estandares.EstandaresMain.Dialogs.DaoItem;

import java.util.List;

@Dao
public interface MstAlasDAO {
    @Insert
    void insertarMstAlas(MstAlas mstAlas);

    @Upsert
        void sincronizarAlas(List<MstAlas> mstAlasList);

    @Query("SELECT * FROM mst_Alas")
    List<MstAlas>obtenerAlas ();

    @Query("SELECT ID id, Dex descripcion from mst_Alas")
    List<DaoItem> obtenerAlasParaLista();

    @Delete
    void eliminarAlas(List<MstAlas> trxEstandaresNew);

}
