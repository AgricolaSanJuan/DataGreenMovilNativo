package com.example.datagreenmovil.DAO.Estandares.TrxParteMaquinaria;

public class ReporteCabeceraParteMaquinaria {

    private String Fecha;
    private String NombreMaquinaria;
    private String NombreOperario;
    private String Turno;
    private Double TotalHoras;
    private String Observacion;

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getNombreMaquinaria() {
        return NombreMaquinaria;
    }

    public void setNombreMaquinaria(String nombreMaquinaria) {
        NombreMaquinaria = nombreMaquinaria;
    }

    public String getNombreOperario() {
        return NombreOperario;
    }

    public void setNombreOperario(String nombreOperario) {
        NombreOperario = nombreOperario;
    }

    public String getTurno() {
        return Turno;
    }

    public void setTurno(String turno) {
        Turno = turno;
    }

    public Double getTotalHoras() {
        return TotalHoras;
    }

    public void setTotalHoras(Double totalHoras) {
        TotalHoras = totalHoras;
    }

    public String getObservacion() {
        return Observacion;
    }

    public void setObservacion(String observacion) {
        Observacion = observacion;
    }
}
