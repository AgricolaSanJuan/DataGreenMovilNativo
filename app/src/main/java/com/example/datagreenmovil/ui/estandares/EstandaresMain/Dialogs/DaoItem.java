package com.example.datagreenmovil.ui.estandares.EstandaresMain.Dialogs;

public class DaoItem {
    private String id;
    private String descripcion;
    

    // Constructor
    public DaoItem(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    // Getter y Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = String.valueOf(id);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;  // Esto es lo que se mostrará en el dropdown
    }
}

