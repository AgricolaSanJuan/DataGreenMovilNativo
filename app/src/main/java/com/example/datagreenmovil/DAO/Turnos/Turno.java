package com.example.datagreenmovil.DAO.Turnos;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(tableName = "mst_Turnos", primaryKeys = {"IdEmpresa", "Id"}, indices = {@Index(value = {"Id"}, unique = true),
        @Index(name = "index_mst_Turnos_IdEmpresa", value = {"IdEmpresa"}),
        @Index(name = "index_mst_Turnos_IdEstado", value = {"IdEstado"})}
)
public class Turno {

    @ColumnInfo(name = "IdEmpresa")
    @NonNull
    private String idEmpresa;

    @ColumnInfo(name = "Id")
    @NonNull
    private String id;

    @ColumnInfo(name = "Dex")
    private String dex;

    @ColumnInfo(name = "IdEstado")
    private String idEstado;

    @ColumnInfo(name = "IdUsuarioCrea")
    private String idUsuarioCrea;

    @ColumnInfo(name = "FechaHoraCreacion")
    private String fechaHoraCreacion; // Usar String o Long si es un timestamp

    @ColumnInfo(name = "IdUsuarioActualiza")
    private String idUsuarioActualiza;

    @ColumnInfo(name = "FechaHoraActualizacion")
    private String fechaHoraActualizacion; // Usar String o Long si es un timestamp

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

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

    public String getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(String idEstado) {
        this.idEstado = idEstado;
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
