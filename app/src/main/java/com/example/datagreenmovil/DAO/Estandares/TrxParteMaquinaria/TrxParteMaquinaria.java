package com.example.datagreenmovil.DAO.Estandares.TrxParteMaquinaria;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Entity(tableName = "trx_ParteMaquinaria")
public class TrxParteMaquinaria {
    @PrimaryKey @NonNull private String IdParteMaquinaria;
    private String Fecha;
    private String IdEmpresa;
    private String IdSucursal;
    private String IdMaquinaria;
    private String IdOperario;
    private String IdTurno;
    private String Observaciones;

    @NonNull
    public String getIdParteMaquinaria() {
        return IdParteMaquinaria;
    }

    public void setIdParteMaquinaria(@NonNull String idParteMaquinaria) {
        IdParteMaquinaria = idParteMaquinaria;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getIdEmpresa() {
        return IdEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        IdEmpresa = idEmpresa;
    }

    public String getIdSucursal() {
        return IdSucursal;
    }

    public void setIdSucursal(String idSucursal) {
        IdSucursal = idSucursal;
    }

    public String getIdMaquinaria() {
        return IdMaquinaria;
    }

    public void setIdMaquinaria(String idMaquinaria) {
        IdMaquinaria = idMaquinaria;
    }

    public String getIdOperario() {
        return IdOperario;
    }

    public void setIdOperario(String idOperario) {
        IdOperario = idOperario;
    }

    public String getIdTurno() {
        return IdTurno;
    }

    public void setIdTurno(String idTurno) {
        IdTurno = idTurno;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String observaciones) {
        Observaciones = observaciones;
    }
}
