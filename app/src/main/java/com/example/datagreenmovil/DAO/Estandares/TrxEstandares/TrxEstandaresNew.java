package com.example.datagreenmovil.DAO.Estandares.TrxEstandares;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "trx_estandares_new")
public class TrxEstandaresNew {
    @PrimaryKey private int id;
    private String idEmpresa;
    private String idTipoEstandar;
    private String idActividad;
    private String idLabor;
    private String periodo;
    private String fechaInicio;
    private String fechaFinal;
    private int idMedidaEstandar;
    private Double cantidad;
    private Double precio;
    private Double base;
    private Double precioBase;
    private int idTipoBonoEstandar;
    private Double valMinExcedente;
    private Double horas;
    private int idTipoCostoEstandar;
    private String idConsumidor;
    private Double porcentajeValidExcedente;
    private Double factorPrecio;
    private String dniUsuarioCrea;
    private String fechaHoraCrea;
    private String dniUsuarioActualiza;
    private String fechaHoraActualiza;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getIdTipoEstandar() {
        return idTipoEstandar;
    }

    public void setIdTipoEstandar(String idTipoEstandar) {
        this.idTipoEstandar = idTipoEstandar;
    }

    public String getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(String idActividad) {
        this.idActividad = idActividad;
    }

    public String getIdLabor() {
        return idLabor;
    }

    public void setIdLabor(String idLabor) {
        this.idLabor = idLabor;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public int getIdMedidaEstandar() {
        return idMedidaEstandar;
    }

    public void setIdMedidaEstandar(int idMedidaEstandar) {
        this.idMedidaEstandar = idMedidaEstandar;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getBase() {
        return base;
    }

    public void setBase(Double base) {
        this.base = base;
    }

    public Double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(Double precioBase) {
        this.precioBase = precioBase;
    }

    public int getIdTipoBonoEstandar() {
        return idTipoBonoEstandar;
    }

    public void setIdTipoBonoEstandar(int idTipoBonoEstandar) {
        this.idTipoBonoEstandar = idTipoBonoEstandar;
    }

    public Double getValMinExcedente() {
        return valMinExcedente;
    }

    public void setValMinExcedente(Double valMinExcedente) {
        this.valMinExcedente = valMinExcedente;
    }

    public Double getHoras() {
        return horas;
    }

    public void setHoras(Double horas) {
        this.horas = horas;
    }

    public int getIdTipoCostoEstandar() {
        return idTipoCostoEstandar;
    }

    public void setIdTipoCostoEstandar(int idTipoCostoEstandar) {
        this.idTipoCostoEstandar = idTipoCostoEstandar;
    }

    public String getIdConsumidor() {
        return idConsumidor;
    }

    public void setIdConsumidor(String idConsumidor) {
        this.idConsumidor = idConsumidor;
    }

    public Double getPorcentajeValidExcedente() {
        return porcentajeValidExcedente;
    }

    public void setPorcentajeValidExcedente(Double porcentajeValidExcedente) {
        this.porcentajeValidExcedente = porcentajeValidExcedente;
    }

    public Double getFactorPrecio() {
        return factorPrecio;
    }

    public void setFactorPrecio(Double factorPrecio) {
        this.factorPrecio = factorPrecio;
    }

    public String getDniUsuarioCrea() {
        return dniUsuarioCrea;
    }

    public void setDniUsuarioCrea(String dniUsuarioCrea) {
        this.dniUsuarioCrea = dniUsuarioCrea;
    }

    public String getFechaHoraCrea() {
        return fechaHoraCrea;
    }

    public void setFechaHoraCrea(String fechaHoraCrea) {
        this.fechaHoraCrea = fechaHoraCrea;
    }

    public String getDniUsuarioActualiza() {
        return dniUsuarioActualiza;
    }

    public void setDniUsuarioActualiza(String dniUsuarioActualiza) {
        this.dniUsuarioActualiza = dniUsuarioActualiza;
    }

    public String getFechaHoraActualiza() {
        return fechaHoraActualiza;
    }

    public void setFechaHoraActualiza(String fechaHoraActualiza) {
        this.fechaHoraActualiza = fechaHoraActualiza;
    }
}
