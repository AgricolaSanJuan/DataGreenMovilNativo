package com.example.datagreenmovil.DAO.Estandares.MstTiposCostoEstandar;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mst_tipos_costo_estandar")
public class MstTiposCostoEstandar {
    @PrimaryKey private int id;
    private int idNisira;
    private String descripcion;
    private String nombreCorto;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdNisira() {
        return idNisira;
    }

    public void setIdNisira(int idNisira) {
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
