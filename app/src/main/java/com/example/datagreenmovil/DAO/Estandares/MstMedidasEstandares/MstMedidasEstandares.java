package com.example.datagreenmovil.DAO.Estandares.MstMedidasEstandares;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mst_medidas_estandares")
public class MstMedidasEstandares {
    @PrimaryKey private int id;
    private String descripcion;
    private String nombreNISIRA;

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

    public String getNombreNISIRA() {
        return nombreNISIRA;
    }

    public void setNombreNISIRA(String nombreNISIRA) {
        this.nombreNISIRA = nombreNISIRA;
    }
}