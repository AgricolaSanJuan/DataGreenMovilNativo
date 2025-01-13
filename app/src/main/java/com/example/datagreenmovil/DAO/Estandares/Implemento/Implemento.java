package com.example.datagreenmovil.DAO.Estandares.Implemento;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Implemento")
public class Implemento {
    @PrimaryKey @NonNull private String IdImplemento;
    private String Descripcion;

    @NonNull
    public String getIdImplemento() {
        return IdImplemento;
    }

    public void setIdImplemento(@NonNull String idImplemento) {
        IdImplemento = idImplemento;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }
}
