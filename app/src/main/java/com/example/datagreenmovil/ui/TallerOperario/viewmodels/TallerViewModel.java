package com.example.datagreenmovil.ui.TallerOperario.viewmodels;

import androidx.lifecycle.ViewModel;

public class TallerViewModel extends ViewModel {

    // Ejemplo de campos que quieres compartir:
    private String idOperario;
    private String nombreOperario;
    private String maquinariaSeleccionada;
    private String turnoSeleccionado;
    private String totalHoras;
    private String observaciones;

    // Getters y Setters de cada campo
    public String getIdOperario() {
        return idOperario;
    }

    public void setIdOperario(String idOperario) {
        this.idOperario = idOperario;
    }

    public String getNombreOperario() {
        return nombreOperario;
    }

    public void setNombreOperario(String nombreOperario) {
        this.nombreOperario = nombreOperario;
    }

    public String getMaquinariaSeleccionada() {
        return maquinariaSeleccionada;
    }

    public void setMaquinariaSeleccionada(String maquinariaSeleccionada) {
        this.maquinariaSeleccionada = maquinariaSeleccionada;
    }

    public String getTurnoSeleccionado() {
        return turnoSeleccionado;
    }

    public void setTurnoSeleccionado(String turnoSeleccionado) {
        this.turnoSeleccionado = turnoSeleccionado;
    }

    public String getTotalHoras() {
        return totalHoras;
    }

    public void setTotalHoras(String totalHoras) {
        this.totalHoras = totalHoras;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
