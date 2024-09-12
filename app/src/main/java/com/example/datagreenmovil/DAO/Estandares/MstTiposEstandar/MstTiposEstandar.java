package com.example.datagreenmovil.DAO.Estandares.MstTiposEstandar;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "mst_tipos_estandar")
public class MstTiposEstandar {
    @PrimaryKey private int id;
    private String descripcion;
    private String nombreCorto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreCorto() {
        return nombreCorto;
    }

    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }
}
