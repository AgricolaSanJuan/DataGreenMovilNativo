package com.example.datagreenmovil.DAO.Empresas;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.datagreenmovil.DAO.Estados.Estado;

@Entity(
        tableName = "mst_Empresas",
        foreignKeys = @ForeignKey(
                entity = Estado.class,
                parentColumns = "Id",
                childColumns = "IdEstado"
        ),
        indices = {
        @Index(name = "index_mst_Empresas_IdEstado", value = {"IdEstado"})
}
)
public class Empresa {

    @PrimaryKey
    @ColumnInfo(name = "Id")
    @NonNull
    private String id;

    @ColumnInfo(name = "NombreCorto")
    private String nombreCorto;

    @ColumnInfo(name = "RazonSocial")
    private String razonSocial;

    @ColumnInfo(name = "Ruc")
    private String ruc;

    @ColumnInfo(name = "Direccion")
    private String direccion;

    @ColumnInfo(name = "Email")
    private String email;

    @ColumnInfo(name = "Telefono")
    private String telefono;

    @ColumnInfo(name = "IdEstado")
    private String idEstado;

    @ColumnInfo(name = "FechaHoraCreacion")
    private String fechaHoraCreacion; // Puede ser String o Long si usas timestamps

    @ColumnInfo(name = "FechaHoraActualizacion")
    private String fechaHoraActualizacion; // Puede ser String o Long si usas timestamps

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreCorto() {
        return nombreCorto;
    }

    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(String idEstado) {
        this.idEstado = idEstado;
    }

    public String getFechaHoraCreacion() {
        return fechaHoraCreacion;
    }

    public void setFechaHoraCreacion(String fechaHoraCreacion) {
        this.fechaHoraCreacion = fechaHoraCreacion;
    }

    public String getFechaHoraActualizacion() {
        return fechaHoraActualizacion;
    }

    public void setFechaHoraActualizacion(String fechaHoraActualizacion) {
        this.fechaHoraActualizacion = fechaHoraActualizacion;
    }
}

