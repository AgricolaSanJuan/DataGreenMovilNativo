package com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.example.datagreenmovil.Conexiones.AppDatabase;
import com.example.datagreenmovil.DAO.Tareo.TrxTareo.Tareo;
import com.example.datagreenmovil.DataGreenApp;

@Entity(tableName = "trx_Tareos_Detalle", primaryKeys = {"idTareo", "item"})
public class TareoDetalles {
    private String idEmpresa;
    @NonNull
    private String idTareo;
    @NonNull
    private int item;
    private String dni;
    private String idPlanilla;
    private String idConsumidor;
    @Ignore
    private String consumidor;
    @Ignore
    private String nombres;
    private String idCultivo;
    @Ignore
    private String cultivo;
    private String idVariedad;
    @Ignore
    private String variedad;

    private String idActividad;
    @Ignore
    private String actividad;
    private String idLabor;
    @Ignore
    private String labor;
    private Double subTotalHoras;
    private Double subTotalRendimiento;
    private String observacion;
    private String ingreso;
    private String salida;
    private int homologar;

    @Ignore
    AppDatabase db;

    public TareoDetalles() {
        db = DataGreenApp.getAppDatabase();
    }

//    public TareoDetalles(String idTareo) {
//        AppDatabase db = DataGreenApp.getAppDatabase();
//        Tareo tareo = db.tareoDAO().obtenerTareoPorId(idTareo);
//        this.idEmpresa = tareo.getIdEmpresa();
//        this.idTareo = tareo.getId();
//        this.item = db.tareoDetallesDAO().obtenerSiguienteItem(idTareo);
//        this.homologar = 0;
//    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    @NonNull
    public String getIdTareo() {
        return idTareo;
    }

    public void setIdTareo(@NonNull String idTareo) {
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

    public String getIdConsumidor() {
        return idConsumidor;
    }

    public void setIdConsumidor(String idConsumidor) {
        this.idConsumidor = idConsumidor;
    }

    public String getConsumidor() {
        if(idConsumidor != null){
            ConsumidorHelper consumidorHelper = new ConsumidorHelper(idConsumidor);
            return idConsumidor != null ? consumidorHelper.obtenerDecripcionConsumidor() : "";
        }
        return consumidor;
    }

    public void setConsumidor(String consumidor) {
        this.consumidor = consumidor;
    }

    public String getNombres() {
        if(dni != null){
            PersonaHelper personaHelper = new PersonaHelper(dni);
            return dni != null ? personaHelper.obtenerNombreTrabajador() : "";
        }
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getIdCultivo() {
        return idCultivo != null ? idCultivo : "";

    }

    public void setIdCultivo(String idCultivo) {
        this.idCultivo = idCultivo;
    }

    public String getCultivo() {
        return cultivo != null ? cultivo : "";

    }

    public void setCultivo(String cultivo) {
        this.cultivo = cultivo;
    }

    public String getIdVariedad() {
        return idVariedad != null ? idVariedad : "";

    }

    public void setIdVariedad(String idVariedad) {
        this.idVariedad = idVariedad;
    }

    public String getVariedad() {
        return variedad != null ? variedad : "";
    }

    public void setVariedad(String variedad) {
        this.variedad = variedad;
    }

    public String getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(String idActividad) {
        this.idActividad = idActividad;
    }

    public String getActividad() {
        if(idActividad != null){
            ActividadHelper actividadHelper = new ActividadHelper(idActividad);
            return idActividad != null ? actividadHelper.obtenerDescripcionActividad() : "";
        }
        return "";
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
        if(idActividad != null && idLabor != null){
            LaborHelper laborHelper = new LaborHelper(idLabor, idActividad);
            return idActividad != null ? laborHelper.obtenerDescripcionLabor() : "";
        }
        return labor;
    }

    public void setLabor(String labor) {
        this.labor = labor;
    }

    public Double getSubTotalHoras() {
        return subTotalHoras;
    }

    public void setSubTotalHoras(Double subTotalHoras) {
        this.subTotalHoras = subTotalHoras;
    }

    public Double getSubTotalRendimiento() {
        return subTotalRendimiento;
    }

    public void setSubTotalRendimiento(Double subTotalRendimiento) {
        this.subTotalRendimiento = subTotalRendimiento;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getIngreso() {
        return ingreso != null ? ingreso : "";
    }

    public void setIngreso(String ingreso) {
        this.ingreso = ingreso;
    }

    public String getSalida() {
        return salida != null ? salida : "";
    }

    public void setSalida(String salida) {
        this.salida = salida;
    }

    public int getHomologar() {
        return homologar;
    }

    public void setHomologar(int homologar) {
        this.homologar = homologar;
    }
}
