package com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Upsert;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.datagreenmovil.DAO.Estandares.TrxEstandares.TrxEstandaresNew;

import java.util.List;

@Dao
public interface TareoDetallesDAO {
    @Query("SELECT * from trx_Tareos_Detalle WHERE IdTareo = :id")
    List<TareoDetalles> obtenerDetalles(String id);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void sincronizarEstandares(List<TrxEstandaresNew> trxEstandaresNew);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insertarDetalle(TareoDetalles tareoDetalles);

    @Upsert
    void insertarDetalleMasivo(List<TareoDetalles> tareoDetallesList);

    @Query("SELECT COUNT(*) FROM trx_Tareos_Detalle WHERE IdTareo = :id")
    double obtenerCantidadDetalles(String id);

    @Query("SELECT COALESCE(MAX(Item) + 1, 1) FROM trx_tareos_detalle WHERE IdTareo = :id")
    int obtenerSiguienteItem(String id);

    @Query("SELECT SUM(subTotalRendimiento) FROM trx_Tareos_Detalle WHERE IdTareo = :id")
    double obtenerCantidadRendimientos(String id);

    @Query("SELECT SUM(subTotalHoras) FROM trx_Tareos_Detalle WHERE IdTareo = :id")
    double obtenerCantidadHoras(String id);

    @Query("SELECT COUNT(*) FROM trx_Tareos_Detalle WHERE IdTareo = :id AND idActividad = :idActividad AND idLabor = :idLabor AND idConsumidor = :idConsumidor")
    int countByTareoIdAndMore(String id, String idActividad, String idLabor, String idConsumidor);

    default boolean verificarExistenciaTrabajador(String id, String idActividad, String idLabor, String idConsumidor) {
        return countByTareoIdAndMore(id, idActividad, idLabor, idConsumidor) > 0;
    }

    @RawQuery
    String obtenerPlanillaTrabajador(SupportSQLiteQuery query);

    @RawQuery
    String obtenerNombresTrabajador(SupportSQLiteQuery query);

    @RawQuery
    String obtenerDescripcionActividad(SupportSQLiteQuery query);

    @RawQuery
    String obtenerDescripcionLabor(SupportSQLiteQuery supportSQLiteQuery);

    @RawQuery
    String obtenerDecripcionConsumidor(SupportSQLiteQuery supportSQLiteQuery);
}
