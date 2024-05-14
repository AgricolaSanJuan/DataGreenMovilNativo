package com.example.datagreenmovil.Entidades;

import android.database.Cursor;

import com.example.datagreenmovil.Conexiones.ConexionSqlite;

import java.util.ArrayList;
import java.util.List;

public class TareoDetalle {
    private String idEmpresa;
    private String idTareo;
    private int item;
    private String dni;
    private String idPlanilla;
    private String nombres;
    private String idCultivo;
    private String cultivo;
    private String idVariedad;
    private String variedad;
    private String idConsumidor;
    private String consumidor;
    private String idActividad;
    private String actividad;
    private String idLabor;
    private String labor;
    private Double rdtos;
    private Double horas;
    private String observacion;
    private String ingreso;
    private String salida;

    public TareoDetalle() {

        this.idTareo = "";
        this.item = 0;
        this.dni = "";
        this.idPlanilla = "";
        this.nombres = "";
        this.idCultivo = "";
        this.cultivo = "";
        this.idVariedad = "";
        this.variedad = "";
        this.idConsumidor = "";
        this.consumidor = "";
        this.idActividad = "";
        this.actividad = "";
        this.idLabor = "";
        this.labor = "";
        //this.inicio=LocalDateTime.now();
        this.rdtos = 0.0;
        this.horas = 0.0;
        this.observacion ="";
        this.ingreso = "";
        this.salida = "";
    }

    public TareoDetalle(TareoDetalle td) {
        this.idEmpresa = td.idEmpresa;
        this.idTareo = td.idTareo;
        this.item = td.item;
        this.dni = td.dni;
        this.idPlanilla = td.idPlanilla;
        this.nombres = td.nombres;
        this.idCultivo = td.idCultivo;
        this.cultivo = td.cultivo;
        this.idVariedad = td.idVariedad;
        this.variedad = td.variedad;
        this.idConsumidor = td.idConsumidor;
        this.consumidor = td.consumidor;
        this.idActividad = td.idActividad;
        this.actividad = td.actividad;
        this.idLabor = td.idLabor;
        this.labor = td.labor;
        this.rdtos = td.rdtos;
        this.horas = td.horas;
        this.observacion = td.observacion;
        this.ingreso = td.ingreso;
        this.salida = td.salida;
    }

    public TareoDetalle(Cursor c, int i) {
        c.moveToPosition(i);
        //IdEmpresa	Idtareo	Item	Dni	IdPlanilla	IdConsumidor	IdCultivo	IdVariedad	IdActividad	IdLabor	SubTotalHoras	SubTotalRendimiento
        //01	001000000001	1	44363337	EMP	100         	0000	001	039	001   	5	5
        this.idEmpresa = c.getString(0);
        this.idTareo = c.getString(1);
        this.item = Integer.parseInt(c.getString(2));
        this.dni = c.getString(3);
        this.idPlanilla = c.getString(4);
        this.nombres = c.getString(5);
        this.idCultivo = c.getString(6);
        this.cultivo = c.getString(7);
        this.idVariedad = c.getString(8);
        this.variedad = c.getString(9);
        this.idConsumidor = c.getString(10);
        this.consumidor = c.getString(11);
        this.idActividad = c.getString(12);
        this.actividad = c.getString(13);
        this.idLabor = c.getString(14);
        this.labor = c.getString(15);
        this.rdtos = c.getDouble(16);
        this.horas = c.getDouble(17);
        this.observacion = c.getString(18);
        this.ingreso = c.getString(19) == null ? "" : c.getString(19);
        this.salida = c.getString(20) == null ? "" : c.getString(20);
    }

    /*
    public TareoDetalle(String idTareo, String item, String dni, String nombres, String idCultivo, String cultivo, String idVariedad, String variedad, String idConsumidor, String consumidor, String idActividad, String actividad, String idLabor, String labor, Double rdtos, Double horas) {
        this.idTareo = idTareo;
        this.item = item;
        this.dni = dni;
        this.nombres = nombres;
        this.idCultivo = idCultivo;
        this.cultivo = cultivo;
        this.idVariedad = idVariedad;
        this.variedad = variedad;
        this.idConsumidor = idConsumidor;
        this.consumidor = consumidor;
        this.idActividad = idActividad;
        this.actividad = actividad;
        this.idLabor = idLabor;
        this.labor = labor;
        this.rdtos = rdtos;
        this.horas = horas;
    }*/

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getIdTareo() {
        return idTareo;
    }

    public void setIdTareo(String idTareo) {
        this.idTareo = idTareo;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getIdPlanilla() {
        return idPlanilla;
    }

    public void setIdPlanilla(String idPlanilla) {
        this.idPlanilla = idPlanilla;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getIdCultivo() {
        return idCultivo;
    }

    public void setIdCultivo(String idCultivo) {
        this.idCultivo = idCultivo;
    }

    public String getCultivo() {
        return cultivo;
    }

    public void setCultivo(String cultivo) {
        this.cultivo = cultivo;
    }

    public String getIdVariedad() {
        return idVariedad;
    }

    public void setIdVariedad(String idVariedad) {
        this.idVariedad = idVariedad;
    }

    public String getVariedad() {
        return variedad;
    }

    public void setVariedad(String variedad) {
        this.variedad = variedad;
    }

    public String getIdConsumidor() {
        return idConsumidor;
    }

    public void setIdConsumidor(String idConsumidor) {
        this.idConsumidor = idConsumidor;
    }

    public String getConsumidor() {
        return consumidor;
    }

    public void setConsumidor(String consumidor) {
        this.consumidor = consumidor;
    }

    public String getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(String idActividad) {
        this.idActividad = idActividad;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getIdLabor() {
        return idLabor;
    }

    public void setIdLabor(String idLabor) {
        this.idLabor = idLabor;
    }

    public String getLabor() {
        return labor;
    }

    public void setLabor(String labor) {
        this.labor = labor;
    }

    public Double getRdtos() {
        return rdtos;
    }

    public void setRdtos(Double rdtos) {
        this.rdtos = rdtos;
    }

    public Double getHoras() {
        return horas;
    }

    public void setHoras(Double horas) { this.horas = horas; }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) { this.observacion = observacion; }

    public String getIngreso() {
        return ingreso;
    }

    public void setIngreso(String ingreso) {
        this.ingreso = ingreso;
    }

    public String getSalida() {
        return salida;
    }

    public void setSalida(String salida) {
        this.salida = salida;
    }

    //PENDIENTE: IMPLEMENTAR UN METOD PARA CONVERTIR CUALQUIER ENTIDAD EN LIST, EJEMPLO LA ENTIDAD TAREDODETALLE->LIST PARA QUE? PARA PODER USARLA EN LA NUEVA IMPLEMENTACION
    //DE LA CLASE CONEXIONSQLITE LA CUAL GUARDA CUALQUIER ENTIDAD COMO "REGISTRO" GENERICO Y NO ENTIDAD POR ENTIDAD
    public boolean guardar(ConexionSqlite objSqlite, String IdUsuario) throws Exception {
        //IdEmpresa	Idtareo	Item	Dni	IdPlanilla	IdConsumidor	IdCultivo	IdVariedad	IdActividad	IdLabor	SubTotalHoras	SubTotalRendimiento
        //01	004000000001	1	46135611	PAS	100	0000	001	039	001	8	0
        List<String> l = new ArrayList<>();
        l.add(this.idEmpresa);
        l.add(this.idTareo);
        l.add(String.valueOf(this.item));
        l.add(this.dni);
        l.add(this.idPlanilla);
        //l.add(this.nombres);
        l.add(this.idConsumidor);
        l.add(this.idCultivo);
        //l.add(this.cultivo);
        l.add(this.idVariedad);
        l.add(this.idActividad);
        l.add(this.idLabor);
        l.add(Double.toString(this.horas));
        l.add(Double.toString(this.rdtos));
        l.add(this.observacion);
        l.add(this.ingreso);
        l.add(this.salida);
        objSqlite.GuardarRegistro("trx_Tareos_Detalle",l);
        //RECALCULAR SUBTOTALES GENERALES
        List<String> p = new ArrayList<>();
        p.add(this .getIdEmpresa());
        p.add(this.getIdTareo());
        p.add(this .getIdEmpresa());
        p.add(this.getIdTareo());
        p.add(this .getIdEmpresa());
        p.add(this.getIdTareo());
        p.add(IdUsuario);
        p.add(this .getIdEmpresa());
        p.add(this.getIdTareo());
        objSqlite.doItBaby(objSqlite.obtQuery("ACTUALIZAR SUBTOTALES trx_Tareos"),p,"WRITE");
        return true;
    }
}
