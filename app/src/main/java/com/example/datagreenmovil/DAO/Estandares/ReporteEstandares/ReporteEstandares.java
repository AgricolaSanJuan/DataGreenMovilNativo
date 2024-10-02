package com.example.datagreenmovil.DAO.Estandares.ReporteEstandares;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class ReporteEstandares {
    private int id;
    private String idEmpresa;
    private String tipoEstandar;
    private String descripcionActividad;
    private String descripcionLabor;
    private String periodo;
    private String fechaInicio;
    private String medida;
    private Double cantidad;
    private Double precio;
    private Double base;
    private String tipoBono;
    private Double horas;
    private String tipoCosto;
    private String idConsumidor;

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

    public String getTipoEstandar() {
        return tipoEstandar;
    }

    public void setTipoEstandar(String tipoEstandar) {
        this.tipoEstandar = tipoEstandar;
    }

    public String getDescripcionActividad() {
        return descripcionActividad;
    }

    public void setDescripcionActividad(String descripcionActividad) {
        this.descripcionActividad = descripcionActividad;
    }

    public String getDescripcionLabor() {
        return descripcionLabor;
    }

    public void setDescripcionLabor(String descripcionLabor) {
        this.descripcionLabor = descripcionLabor;
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

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
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

    public String getTipoBono() {
        return tipoBono;
    }

    public void setTipoBono(String tipoBono) {
        this.tipoBono = tipoBono;
    }

    public Double getHoras() {
        return horas;
    }

    public void setHoras(Double horas) {
        this.horas = horas;
    }

    public String getTipoCosto() {
        return tipoCosto;
    }

    public void setTipoCosto(String tipoCosto) {
        this.tipoCosto = tipoCosto;
    }

    public String getIdConsumidor() {
        return idConsumidor;
    }

    public void setIdConsumidor(String idConsumidor) {
        this.idConsumidor = idConsumidor;
    }
}
