package com.example.datagreenmovil.DAO.Estandares.Operario;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Operario")
public class Operario {
    @PrimaryKey @NonNull
    private String idOperario;
    private String nombreCompleto;
    private String nroDocumento;

    @NonNull
    public String getIdOperario() {
        return idOperario;
    }

    public void setIdOperario(@NonNull String idOperario) {
        this.idOperario = idOperario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }
}
