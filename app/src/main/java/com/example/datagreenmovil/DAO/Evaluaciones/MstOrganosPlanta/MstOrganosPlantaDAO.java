package com.example.datagreenmovil.DAO.Evaluaciones.MstOrganosPlanta;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.datagreenmovil.ui.estandares.EstandaresMain.Dialogs.DaoItem;

import java.util.List;

@Dao
public interface MstOrganosPlantaDAO {  // 💡 DAO debe ser una interfaz

    @Insert
    void insertarMstOrganosPlanta(MstOrganosPlanta mstOrganosPlanta);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void sincronizarOrganosPlanta(List<MstOrganosPlanta> mstOrganosPlantaList);

    @Query("SELECT * FROM mst_OrganosPlanta")
    List<MstOrganosPlanta> obtenerOrganosPlanta();

    @Query("SELECT Id AS id, Dex AS descripcion FROM mst_OrganosPlanta")
    List<DaoItem> obtenerOrganosPlantaParaLista();

    @Query("DELETE FROM mst_OrganosPlanta WHERE id IN (:ids)")
    void eliminarOrganosPlanta(List<Integer> ids);
}
