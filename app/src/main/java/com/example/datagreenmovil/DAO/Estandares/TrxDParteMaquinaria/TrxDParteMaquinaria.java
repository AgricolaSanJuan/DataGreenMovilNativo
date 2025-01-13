package com.example.datagreenmovil.DAO.Estandares.TrxDParteMaquinaria;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "trx_DParteMaquinaria")
public class TrxDParteMaquinaria {
    @PrimaryKey @NonNull private String IdParteMaquinaria;
    private String Item;
    private String IdConsumidor;
    private Double TotalHoras;
    private Double HorometroInicial;
    private Double HorometroFinal;
    private String IdImplemento;
    private String IdActividad;
    private String IdLabor;
    private String IdCombustible;
    private Double Cantidad;

    public String getIdParteMaquinaria() {
        return IdParteMaquinaria;
    }

    public void setIdParteMaquinaria(String idParteMaquinaria) {
        IdParteMaquinaria = idParteMaquinaria;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }

    public String getIdConsumidor() {
        return IdConsumidor;
    }

    public void setIdConsumidor(String idConsumidor) {
        IdConsumidor = idConsumidor;
    }

    public Double getTotalHoras() {
        return TotalHoras;
    }

    public void setTotalHoras(Double totalHoras) {
        TotalHoras = totalHoras;
    }

    public Double getHorometroInicial() {
        return HorometroInicial;
    }

    public void setHorometroInicial(Double horometroInicial) {
        HorometroInicial = horometroInicial;
    }

    public Double getHorometroFinal() {
        return HorometroFinal;
    }

    public void setHorometroFinal(Double horometroFinal) {
        HorometroFinal = horometroFinal;
    }

    public String getIdImplemento() {
        return IdImplemento;
    }

    public void setIdImplemento(String idImplemento) {
        IdImplemento = idImplemento;
    }

    public String getIdActividad() {
        return IdActividad;
    }

    public void setIdActividad(String idActividad) {
        IdActividad = idActividad;
    }

    public String getIdLabor() {
        return IdLabor;
    }

    public void setIdLabor(String idLabor) {
        IdLabor = idLabor;
    }

    public String getIdCombustible() {
        return IdCombustible;
    }

    public void setIdCombustible(String idCombustible) {
        IdCombustible = idCombustible;
    }

    public Double getCantidad() {
        return Cantidad;
    }

    public void setCantidad(Double cantidad) {
        Cantidad = cantidad;
    }
}
