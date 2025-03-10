package com.example.datagreenmovil.DAO.Estandares.ConsumidoresTaller;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mst_consumidores_taller ")
public class ConsumidoresTaller {
    @PrimaryKey @NonNull String IdConsumidor;
    String Descripcion;
    String Tipo;

    // Constructor vacío (requerido por Room)
    public ConsumidoresTaller() {}

    // Constructor completo
    public ConsumidoresTaller(@NonNull String idConsumidor, String descripcion, String tipo) {
        this.IdConsumidor = idConsumidor;
        this.Descripcion = descripcion;
        this.Tipo = tipo;
    }

    @NonNull
    public String getIdConsumidor() {
        return IdConsumidor;
    }

    public void setIdConsumidor(@NonNull String idConsumidor) {
        this.IdConsumidor = idConsumidor;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.Descripcion = descripcion;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        this.Tipo = tipo;
    }

}
