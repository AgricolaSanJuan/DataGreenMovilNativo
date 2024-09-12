package com.example.datagreenmovil.DAO.Estandares.MstTiposBonoEstandar;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mst_tipos_bono_estandar")
public class MstTiposBonoEstandar {
    @PrimaryKey private int id;
    private String idNisira;
    private String descripcion;
    private String nombreCorto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdNisira() {
        return idNisira;
    }

    public void setIdNisira(String idNisira) {
        this.idNisira = idNisira;
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
