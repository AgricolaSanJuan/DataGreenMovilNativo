package com.example.datagreenmovil.DAO.Estandares.ConsumidoresTaller;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mst_consumidores_taller ")
public class ConsumidoresTaller {
    @PrimaryKey @NonNull String IdConsumidor;
    String Descripcion;
    String Tipo;

    @NonNull
    public String getIdConsumidor() {
        return IdConsumidor;
    }

    public void setIdConsumidor(@NonNull String idConsumidor) {
        IdConsumidor = idConsumidor;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }
}
