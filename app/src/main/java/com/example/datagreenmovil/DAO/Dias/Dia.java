package com.example.datagreenmovil.DAO.Dias;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "mst_Dias",
        indices = {
                @Index(name = "index_mst_Dias_Semana", value = {"Semana"}),
                @Index(name = "index_mst_Dias_Mes", value = {"Mes"}),
                @Index(name = "index_mst_Dias_Anio", value = {"Anio"})
        })
public class Dia {

    @PrimaryKey
    @ColumnInfo(name = "Dia")
    @NonNull
    private String dia; // Usar String para representar la fecha en formato "YYYY-MM-DD"

    @ColumnInfo(name = "Semana")
    private int semana;

    @ColumnInfo(name = "Mes")
    private int mes;

    @ColumnInfo(name = "Anio")
    private String anio;

    @ColumnInfo(name = "NombreDia")
    private String nombreDia;

    @ColumnInfo(name = "NombreDiaCorto")
    private String nombreDiaCorto;

    @ColumnInfo(name = "NombreMes")
    private String nombreMes;

    @ColumnInfo(name = "SemanaNisira")
    private String semanaNisira;

    @ColumnInfo(name = "Periodo")
    private String periodo;

    @ColumnInfo(name = "InicioSemanaNisira")
    private String inicioSemanaNisira; // Usar String o Long si son timestamps

    @ColumnInfo(name = "FinSemanaNisira")
    private String finSemanaNisira; // Usar String o Long si son timestamps

    @ColumnInfo(name = "InicioPeriodo")
    private String inicioPeriodo; // Usar String o Long si son timestamps

    @ColumnInfo(name = "FinPeriodo")
    private String finPeriodo; // Usar String o Long si son timestamps

    // Getters y Setters
    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public int getSemana() {
        return semana;
    }

    public void setSemana(int semana) {
        this.semana = semana;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getNombreDia() {
        return nombreDia;
    }

    public void setNombreDia(String nombreDia) {
        this.nombreDia = nombreDia;
    }

    public String getNombreDiaCorto() {
        return nombreDiaCorto;
    }

    public void setNombreDiaCorto(String nombreDiaCorto) {
        this.nombreDiaCorto = nombreDiaCorto;
    }

    public String getNombreMes() {
        return nombreMes;
    }

    public void setNombreMes(String nombreMes) {
        this.nombreMes = nombreMes;
    }

    public String getSemanaNisira() {
        return semanaNisira;
    }

    public void setSemanaNisira(String semanaNisira) {
        this.semanaNisira = semanaNisira;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getInicioSemanaNisira() {
        return inicioSemanaNisira;
    }

    public void setInicioSemanaNisira(String inicioSemanaNisira) {
        this.inicioSemanaNisira = inicioSemanaNisira;
    }

    public String getFinSemanaNisira() {
        return finSemanaNisira;
    }

    public void setFinSemanaNisira(String finSemanaNisira) {
        this.finSemanaNisira = finSemanaNisira;
    }

    public String getInicioPeriodo() {
        return inicioPeriodo;
    }

    public void setInicioPeriodo(String inicioPeriodo) {
        this.inicioPeriodo = inicioPeriodo;
    }

    public String getFinPeriodo() {
        return finPeriodo;
    }

    public void setFinPeriodo(String finPeriodo) {
        this.finPeriodo = finPeriodo;
    }
}
