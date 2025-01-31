package com.example.datagreenmovil.DAO.Usuarios;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(tableName = "mst_Usuarios", primaryKeys = {"IdEmpresa", "Id"},
        indices = {
            @Index(value = {"Id"}, unique = true),
            @Index(value = {"IdEmpresa"}),
            @Index(value = {"NroDocumento"})
        }
)
public class Usuario {

    @ColumnInfo(name = "IdEmpresa")
    @NonNull
    private String idEmpresa;

    @ColumnInfo(name = "Id")
    @NonNull
    private String id;

    @ColumnInfo(name = "NombreUsuario")
    private String nombreUsuario;

    @ColumnInfo(name = "NroDocumento")
    private String nroDocumento;

    @ColumnInfo(name = "Nombres")
    private String nombres;

    @ColumnInfo(name = "Paterno")
    private String paterno;

    @ColumnInfo(name = "Materno")
    private String materno;

    @ColumnInfo(name = "FechaNacimiento")
    private String fechaNacimiento; // Usar String o Long si es un timestamp

    @ColumnInfo(name = "Suma")
    private String suma;

    @ColumnInfo(name = "Permisos")
    private String permisos;

    @ColumnInfo(name = "IdEstado")
    private String idEstado;

    @ColumnInfo(name = "IdUsuarioCrea")
    private String idUsuarioCrea;

    @ColumnInfo(name = "FechaHoraCreacion")
    private String fechaHoraCreacion; // Usar String o Long si es un timestamp

    @ColumnInfo(name = "IdUsuarioActualiza")
    private String idUsuarioActualiza;

    @ColumnInfo(name = "FechaHoraActualizacion")
    private String fechaHoraActualizacion; // Usar String o Long si es un timestamp

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getPaterno() {
        return paterno;
    }

    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    public String getMaterno() {
        return materno;
    }

    public void setMaterno(String materno) {
        this.materno = materno;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSuma() {
        return suma;
    }

    public void setSuma(String suma) {
        this.suma = suma;
    }

    public String getPermisos() {
        return permisos;
    }

    public void setPermisos(String permisos) {
        this.permisos = permisos;
    }

    public String getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(String idEstado) {
        this.idEstado = idEstado;
    }

    public String getIdUsuarioCrea() {
        return idUsuarioCrea;
    }

    public void setIdUsuarioCrea(String idUsuarioCrea) {
        this.idUsuarioCrea = idUsuarioCrea;
    }

    public String getFechaHoraCreacion() {
        return fechaHoraCreacion;
    }

    public void setFechaHoraCreacion(String fechaHoraCreacion) {
        this.fechaHoraCreacion = fechaHoraCreacion;
    }

    public String getIdUsuarioActualiza() {
        return idUsuarioActualiza;
    }

    public void setIdUsuarioActualiza(String idUsuarioActualiza) {
        this.idUsuarioActualiza = idUsuarioActualiza;
    }

    public String getFechaHoraActualizacion() {
        return fechaHoraActualizacion;
    }

    public void setFechaHoraActualizacion(String fechaHoraActualizacion) {
        this.fechaHoraActualizacion = fechaHoraActualizacion;
    }
}
