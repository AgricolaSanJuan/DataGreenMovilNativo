package com.example.datagreenmovil.DAO.Tareo.TrxTareo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.datagreenmovil.Conexiones.AppDatabase;
import com.example.datagreenmovil.DAO.Dias.Dia;
import com.example.datagreenmovil.DAO.Empresas.Empresa;
import com.example.datagreenmovil.DAO.Estados.Estado;
import com.example.datagreenmovil.DAO.Turnos.Turno;
import com.example.datagreenmovil.DAO.Usuarios.Usuario;
import com.example.datagreenmovil.DataGreenApp;

@Entity(
        tableName = "trx_Tareos",
        foreignKeys = {
                @ForeignKey(
                        entity = Empresa.class,
                        parentColumns = "Id",
                        childColumns = "IdEmpresa"
                ),
                @ForeignKey(
                        entity = Dia.class,
                        parentColumns = "Dia",
                        childColumns = "Fecha"
                ),
                @ForeignKey(
                        entity = Turno.class,
                        parentColumns = "Id",
                        childColumns = "IdTurno"
                ),
                @ForeignKey(
                        entity = Estado.class,
                        parentColumns = "Id",
                        childColumns = "IdEstado"
                ),
                @ForeignKey(
                        entity = Usuario.class,
                        parentColumns = "Id",
                        childColumns = "IdUsuarioCrea"
                ),
                @ForeignKey(
                        entity = Usuario.class,
                        parentColumns = "Id",
                        childColumns = "IdUsuarioActualiza"
                )
        },
        indices = {
                @Index(value = {"IdEmpresa"}),
                @Index(value = {"Fecha"}),
                @Index(value = {"IdTurno"}),
                @Index(value = {"IdEstado"}),
                @Index(value = {"IdUsuarioCrea"}),
                @Index(value = {"IdUsuarioActualiza"})
        }
)
public class Tareo {

    @ColumnInfo(name = "IdEmpresa")
    private String idEmpresa;

    @PrimaryKey
    @ColumnInfo(name = "Id")
    @NonNull
    private String id;

    @ColumnInfo(name = "Fecha")
    private String fecha; // Puede ser String o Long si usas timestamp

    @ColumnInfo(name = "IdTurno")
    private String idTurno;

    @ColumnInfo(name = "IdEstado")
    private String idEstado;

    @ColumnInfo(name = "IdUsuarioCrea")
    private String idUsuarioCrea;

    @ColumnInfo(name = "FechaHoraCreacion")
    private String fechaHoraCreacion; // Puede ser String o Long si usas timestamp

    @ColumnInfo(name = "IdUsuarioActualiza")
    private String idUsuarioActualiza;

    @ColumnInfo(name = "FechaHoraActualizacion")
    private String fechaHoraActualizacion; // Puede ser String o Long si usas timestamp

    @ColumnInfo(name = "FechaHoraTransferencia")
    private String fechaHoraTransferencia; // Puede ser String o Long si usas timestamp

    @ColumnInfo(name = "TotalHoras")
    private Double totalHoras;

    @ColumnInfo(name = "TotalRendimientos")
    private Double totalRendimientos;

    @ColumnInfo(name = "TotalDetalles")
    private Integer totalDetalles;

    @ColumnInfo(name = "Observaciones")
    private String observaciones;

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        AppDatabase db = DataGreenApp.getAppDatabase();
        this.id = id;
//        this.id = Funciones.siguienteCorrelativo(db.tareoDAO().getLastId(),'A');
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(String idTurno) {
        this.idTurno = idTurno;
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

    public String getFechaHoraTransferencia() {
        return fechaHoraTransferencia;
    }

    public void setFechaHoraTransferencia(String fechaHoraTransferencia) {
        this.fechaHoraTransferencia = fechaHoraTransferencia;
    }

    public Double getTotalHoras() {
        return totalHoras;
    }

    public void setTotalHoras(Double totalHoras) {
        this.totalHoras = totalHoras;
    }

    public Double getTotalRendimientos() {
        return totalRendimientos;
    }

    public void setTotalRendimientos(Double totalRendimientos) {
        this.totalRendimientos = totalRendimientos;
    }

    public Integer getTotalDetalles() {
        return totalDetalles;
    }

    public void setTotalDetalles(Integer totalDetalles) {
        this.totalDetalles = totalDetalles;
    }

    public String getObservaciones() {
        return observaciones != null ? observaciones : "";
    }


    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
