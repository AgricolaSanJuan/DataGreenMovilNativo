package com.example.datagreenmovil.DAO.Estados;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mst_Estados")
public class Estado {

    @PrimaryKey
    @ColumnInfo(name = "Id")
    @NonNull
    private String id;

    @ColumnInfo(name = "Dex")
    private String dex;

    @ColumnInfo(name = "IdUsuarioCrea")
    private String idUsuarioCrea;

    @ColumnInfo(name = "FechaHoraCreacion")
    private String fechaHoraCreacion; // Usar String o Long si es un timestamp

    @ColumnInfo(name = "IdUsuarioActualiza")
    private String idUsuarioActualiza;

    @ColumnInfo(name = "FechaHoraActualizacion")
    private String fechaHoraActualizacion; // Usar String o Long si es un timestamp

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDex() {
        return dex;
    }

    public void setDex(String dex) {
        this.dex = dex;
    }

    public String getIdUsuarioCrea() {
        return idUsuarioCrea;
    }

    public void setIdUsuarioCrea(String idUsuarioCrea) {
        this.idUsuarioCrea = idUsuarioCrea;
    }

    public String getFechaHoraCreacion() {
        return fechaHoraCreacion;
    }

    public void setFechaHoraCreacion(String fechaHoraCreacion) {
        this.fechaHoraCreacion = fechaHoraCreacion;
    }

    public String getIdUsuarioActualiza() {
        return idUsuarioActualiza;
    }

    public void setIdUsuarioActualiza(String idUsuarioActualiza) {
        this.idUsuarioActualiza = idUsuarioActualiza;
    }

    public String getFechaHoraActualizacion() {
        return fechaHoraActualizacion;
    }

    public void setFechaHoraActualizacion(String fechaHoraActualizacion) {
        this.fechaHoraActualizacion = fechaHoraActualizacion;
    }
}
