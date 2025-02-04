package com.example.datagreenmovil.DAO.Evaluaciones.TrxEvaluaciones;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.datagreenmovil.DAO.Estados.Estado;
import com.example.datagreenmovil.DAO.Evaluaciones.MstLote.MstLote;
import com.example.datagreenmovil.DAO.Usuarios.Usuario;

@Entity(tableName = "trx_Evaluaciones",
    foreignKeys = {
        @ForeignKey(entity = Usuario.class, parentColumns = "Id", childColumns = "IdUsuarioRegistra"),
        @ForeignKey(entity = Usuario.class, parentColumns = "Id", childColumns = "IdUsuarioActualiza"),
        @ForeignKey(entity = Estado.class, parentColumns = "Id", childColumns = "IdEstado"),
        @ForeignKey(entity = MstLote.class, parentColumns = "Id", childColumns = "IdLote")
    })
public class TrxEvaluaciones {
    @PrimaryKey
    @NonNull
    public String Id;

    private String IdLote;
    private String IdTipoEvaluacion;
    private String Fecha;
    private String Linea;
    private String Planta;
    private String Cuartel;
    private String Inicio;
    private String Fin;
    private String Transferencia;
    private String IdEstado;
    private String IdUsuarioRegistra;
    private String Registro;
    private String IdUsuarioActualiza;
    private String Actualizacion;

    @NonNull
    public String getId() {
        return Id;
    }

    public void setId(@NonNull String id) {
        Id = id;
    }

    public String getIdLote() {
        return IdLote;
    }

    public void setIdLote(String idLote) {
        IdLote = idLote;
    }

    public String getIdTipoEvaluacion() {
        return IdTipoEvaluacion;
    }

    public void setIdTipoEvaluacion(String idTipoEvaluacion) {
        IdTipoEvaluacion = idTipoEvaluacion;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getLinea() {
        return Linea;
    }

    public void setLinea(String linea) {
        Linea = linea;
    }

    public String getPlanta() {
        return Planta;
    }

    public void setPlanta(String planta) {
        Planta = planta;
    }

    public String getCuartel() {
        return Cuartel;
    }

    public void setCuartel(String cuartel) {
        Cuartel = cuartel;
    }

    public String getInicio() {
        return Inicio;
    }

    public void setInicio(String inicio) {
        Inicio = inicio;
    }

    public String getFin() {
        return Fin;
    }

    public void setFin(String fin) {
        Fin = fin;
    }

    public String getTransferencia() {
        return Transferencia;
    }

    public void setTransferencia(String transferencia) {
        Transferencia = transferencia;
    }

    public String getIdEstado() {
        return IdEstado;
    }

    public void setIdEstado(String idEstado) {
        IdEstado = idEstado;
    }

    public String getIdUsuarioRegistra() {
        return IdUsuarioRegistra;
    }

    public void setIdUsuarioRegistra(String idUsuarioRegistra) {
        IdUsuarioRegistra = idUsuarioRegistra;
    }

    public String getRegistro() {
        return Registro;
    }

    public void setRegistro(String registro) {
        Registro = registro;
    }

    public String getIdUsuarioActualiza() {
        return IdUsuarioActualiza;
    }

    public void setIdUsuarioActualiza(String idUsuarioActualiza) {
        IdUsuarioActualiza = idUsuarioActualiza;
    }

    public String getActualizacion() {
        return Actualizacion;
    }

    public void setActualizacion(String actualizacion) {
        Actualizacion = actualizacion;
    }
}
