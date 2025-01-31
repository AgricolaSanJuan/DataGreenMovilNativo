package com.example.datagreenmovil.DAO.Evaluaciones.MstOrganosPlanta;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.datagreenmovil.DAO.Estados.Estado;
import com.example.datagreenmovil.DAO.Usuarios.Usuario;

@Entity(
        tableName = "mst_OrganosPlanta",
        foreignKeys = {
                @ForeignKey(entity = Estado.class, parentColumns = "Id", childColumns = "IdEstado"),
                @ForeignKey(entity = Usuario.class, parentColumns = "Id", childColumns = "IdUsuarioRegistra"),
                @ForeignKey(entity = Usuario.class, parentColumns = "Id", childColumns = "IdUsuarioActualiza")
        }
)
public class MstOrganosPlanta {
    @PrimaryKey
    @NonNull
    private String Id;
    @NonNull
    private String Dex;
    @NonNull
    private String IdEstado;
    @NonNull
    private String IdUsuarioRegistra;
    @NonNull
    private String Registro;
    @NonNull
    private String IdUsuarioActualiza;
    @NonNull
    private String Actualizacion;

    @NonNull
    public String getId() {
        return Id;
    }

    public void setId(@NonNull String id) {
        Id = id;
    }

    @NonNull
    public String getDex() {
        return Dex;
    }

    public void setDex(@NonNull String dex) {
        Dex = dex;
    }

    @NonNull
    public String getIdEstado() {
        return IdEstado;
    }

    public void setIdEstado(@NonNull String idEstado) {
        IdEstado = idEstado;
    }

    @NonNull
    public String getIdUsuarioRegistra() {
        return IdUsuarioRegistra;
    }

    public void setIdUsuarioRegistra(@NonNull String idUsuarioRegistra) {
        IdUsuarioRegistra = idUsuarioRegistra;
    }

    @NonNull
    public String getRegistro() {
        return Registro;
    }

    public void setRegistro(@NonNull String registro) {
        Registro = registro;
    }

    @NonNull
    public String getIdUsuarioActualiza() {
        return IdUsuarioActualiza;
    }

    public void setIdUsuarioActualiza(@NonNull String idUsuarioActualiza) {
        IdUsuarioActualiza = idUsuarioActualiza;
    }

    @NonNull
    public String getActualizacion() {
        return Actualizacion;
    }

    public void setActualizacion(@NonNull String actualizacion) {
        Actualizacion = actualizacion;
    }
}
