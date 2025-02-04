package com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.room.Upsert;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.datagreenmovil.DAO.Estandares.TrxEstandares.TrxEstandaresNew;
import com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle.DTO.ReporteDTO;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TareoDetallesDAO {
    @Query("SELECT * from trx_Tareos_Detalle WHERE IdTareo = :id")
    List<TareoDetalles> obtenerDetalles(String id);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void sincronizarEstandares(List<TrxEstandaresNew> trxEstandaresNew);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insertarDetalle(TareoDetalles tareoDetalles);

    @Delete
    void eliminarDetalle(TareoDetalles tareoDetalle);

    @Upsert
    void insertarDetalleMasivo(List<TareoDetalles> tareoDetallesList);

    //    INSERCIÓN CON ELIMINADOS
    @Transaction
    default void sincronizarDetalles(List<TareoDetalles> nuevosDetalles, String idTareo) {
        // Obtener los detalles actuales
        List<TareoDetalles> detallesActuales = obtenerDetalles(idTareo);

        // Filtrar los que no están en la nueva lista
        List<TareoDetalles> detallesAEliminar = new ArrayList<>(detallesActuales);
        detallesAEliminar.removeIf(detalleActual ->
                nuevosDetalles.stream().anyMatch(nuevo -> nuevo.getItem() == detalleActual.getItem())
        );

        // Eliminar los detalles obsoletos
        for (TareoDetalles detalle : detallesAEliminar) {
            eliminarDetalle(detalle);
        }

        // Insertar o actualizar los nuevos detalles
        for (int i = 0; i < nuevosDetalles.size(); i++) {
            TareoDetalles detalle = nuevosDetalles.get(i);
            detalle.setItem(i + 1); // Reasigna los items empezando desde 1
        }
        insertarDetalleMasivo(nuevosDetalles);
    }
//    INSERCIÓN CON ELIMINADOS

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

    //    REPORTERÍA REPORTE 1
    @RawQuery
    List<ReporteDTO> obtenerReporte(SupportSQLiteQuery supportSQLiteQuery);
//    REPORTERÍA REPORTE 1
}
