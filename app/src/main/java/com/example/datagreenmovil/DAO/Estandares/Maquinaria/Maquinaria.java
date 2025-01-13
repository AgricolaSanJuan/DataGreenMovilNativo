package com.example.datagreenmovil.DAO.Estandares.Maquinaria;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Maquinaria")
public class Maquinaria {
    @PrimaryKey @NonNull private String IdMaquinaria;
    private String Descripcion;

    public String getIdMaquinaria() {
        return IdMaquinaria;
    }

    public void setIdMaquinaria(String idMaquinaria) {
        IdMaquinaria = idMaquinaria;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }
}
