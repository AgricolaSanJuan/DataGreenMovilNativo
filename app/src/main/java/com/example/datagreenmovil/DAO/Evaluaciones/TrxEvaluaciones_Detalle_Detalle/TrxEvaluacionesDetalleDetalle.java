package com.example.datagreenmovil.DAO.Evaluaciones.TrxEvaluaciones_Detalle_Detalle;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.datagreenmovil.DAO.Evaluaciones.MstMedible.MstMedible;
import com.example.datagreenmovil.DAO.Evaluaciones.TrxEvaluaciones.TrxEvaluaciones;
import com.example.datagreenmovil.DAO.Evaluaciones.TrxEvaluacionesDetalle.TrxEvaluacionesDetalle;

@Entity(tableName = "trxEvaluaciondesDetalleDetalle",
    primaryKeys = {
        "IdEvaluacion",
        "Item",
            "ItemDetalle"
    },
        foreignKeys = {
            @ForeignKey(entity = MstMedible.class,parentColumns = "Id", childColumns = "IdMedible"),
                @ForeignKey(entity = TrxEvaluacionesDetalle.class,parentColumns = "IdEvaluacion", childColumns = "IdEvaluacion"),
                @ForeignKey(entity = TrxEvaluacionesDetalle.class,parentColumns = "Item", childColumns = "ItemDetalle"),
        })

public class TrxEvaluacionesDetalleDetalle {
    @PrimaryKey
    @NonNull
    private String IdEvaluacion;
    @NonNull
    private Integer ItemDetalle;
    @NonNull
    private Integer Item;
    @NonNull
    private String IdMedible;
    @NonNull
    private Integer CantidadMedida;

    @NonNull
    public String getIdEvaluacion() {
        return IdEvaluacion;
    }

    public void setIdEvaluacion(@NonNull String idEvaluacion) {
        IdEvaluacion = idEvaluacion;
    }

    @NonNull
    public Integer getItemDetalle() {
        return ItemDetalle;
    }

    public void setItemDetalle(@NonNull Integer itemDetalle) {
        ItemDetalle = itemDetalle;
    }

    @NonNull
    public Integer getItem() {
        return Item;
    }

    public void setItem(@NonNull Integer item) {
        Item = item;
    }

    @NonNull
    public String getIdMedible() {
        return IdMedible;
    }

    public void setIdMedible(@NonNull String idMedible) {
        IdMedible = idMedible;
    }

    @NonNull
    public Integer getCantidadMedida() {
        return CantidadMedida;
    }

    public void setCantidadMedida(@NonNull Integer cantidadMedida) {
        CantidadMedida = cantidadMedida;
    }
}
