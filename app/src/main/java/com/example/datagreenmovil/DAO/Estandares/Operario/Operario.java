package com.example.datagreenmovil.DAO.Estandares.Operario;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Operario")
public class Operario {
    @PrimaryKey @NonNull private String IdOperario;
    private String NombreCompleto;
    private String NroDocumento;

    @NonNull
    public String getIdOperario() {
        return IdOperario;
    }

    public void setIdOperario(@NonNull String idOperario) {
        IdOperario = idOperario;
    }

    public String getNombreCompleto() {
        return NombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        NombreCompleto = nombreCompleto;
    }

    public String getNroDocumento() {
        return NroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        NroDocumento = nroDocumento;
    }
}
