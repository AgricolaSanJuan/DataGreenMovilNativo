package com.example.datagreenmovil.DAO.Evaluaciones.TrxEvaluacionesDetalle;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.datagreenmovil.DAO.Evaluaciones.MstAlas.MstAlas;
import com.example.datagreenmovil.DAO.Evaluaciones.MstObjetivosEvaluables.MstObjetivosEvaluables;
import com.example.datagreenmovil.DAO.Evaluaciones.MstTipoTrampa.MstTipoTrampa;
import com.example.datagreenmovil.DAO.Evaluaciones.TrxEvaluaciones.TrxEvaluaciones;

@Entity(tableName = "trx_Evaluaciones_Detalle",
        primaryKeys = {
                "IdEvaluacion",
                "Item"
        },
        foreignKeys = {
                @ForeignKey(entity = TrxEvaluaciones.class, parentColumns = "Id", childColumns = "IdEvaluacion"),
                @ForeignKey(entity = MstObjetivosEvaluables.class, parentColumns = "Id", childColumns = "IdObjetivoEvaluable"),
                @ForeignKey(entity = MstAlas.class, parentColumns = "Id", childColumns = "IdAla"),
                @ForeignKey(entity = MstTipoTrampa.class, parentColumns = "Id", childColumns = "IdTipoTrampa")
        })

public class TrxEvaluacionesDetalle {
    @PrimaryKey
    @NonNull
    private String IdEvaluacion;
    @NonNull
    private int Item;
    private String IdObjetivoEvaluable;
    @NonNull
    private String IdAla;
    @NonNull
    private int NroMuestra;
    private String IdTipoTrampa;

    @NonNull
    public String getIdEvaluacion() {
        return IdEvaluacion;
    }

    public void setIdEvaluacion(@NonNull String idEvaluacion) {
        IdEvaluacion = idEvaluacion;
    }

    public int getItem() {
        return Item;
    }

    public void setItem(int item) {
        Item = item;
    }

    public String getIdObjetivoEvaluable() {
        return IdObjetivoEvaluable;
    }

    public void setIdObjetivoEvaluable(String idObjetivoEvaluable) {
        IdObjetivoEvaluable = idObjetivoEvaluable;
    }

    @NonNull
    public String getIdAla() {
        return IdAla;
    }

    public void setIdAla(@NonNull String idAla) {
        IdAla = idAla;
    }

    public int getNroMuestra() {
        return NroMuestra;
    }

    public void setNroMuestra(int nroMuestra) {
        NroMuestra = nroMuestra;
    }

    public String getIdTipoTrampa() {
        return IdTipoTrampa;
    }

    public void setIdTipoTrampa(String idTipoTrampa) {
        IdTipoTrampa = idTipoTrampa;
    }
}
