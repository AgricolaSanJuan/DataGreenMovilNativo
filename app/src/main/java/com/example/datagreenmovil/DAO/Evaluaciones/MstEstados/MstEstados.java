package com.example.datagreenmovil.DAO.Evaluaciones.MstEstados;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mst_Estados")
public class MstEstados {
    @PrimaryKey
    @NonNull
    private String Id;
    @NonNull
    private String Dex;
    @NonNull
    private String IdUsuarioRegistra;
    @NonNull
    private String Registro;
    @NonNull
    private String IdUsuarioActualiza;
    @NonNull
    private String Actualizacion;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDex() {
        return Dex;
    }

    public void setDex(String dex) {
        Dex = dex;
    }

    public String getIdUsuarioRegistra() {
        return IdUsuarioRegistra;
    }

    public void setIdUsuarioRegistra(String idUsuarioRegistra) {
        IdUsuarioRegistra = idUsuarioRegistra;
    }

    public String getRegistro() {
        return Registro;
    }

    public void setRegistro(String registro) {
        Registro = registro;
    }

    public String getIdUsuarioActualiza() {
        return IdUsuarioActualiza;
    }

    public void setIdUsuarioActualiza(String idUsuarioActualiza) {
        IdUsuarioActualiza = idUsuarioActualiza;
    }

    public String getActualizacion() {
        return Actualizacion;
    }

    public void setActualizacion(String actualizacion) {
        Actualizacion = actualizacion;
    }
}
