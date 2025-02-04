package com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;

import com.example.datagreenmovil.Conexiones.AppDatabase;
import com.example.datagreenmovil.DAO.Tareo.TrxTareo.Tareo;
import com.example.datagreenmovil.DataGreenApp;

@Entity(tableName = "trx_Tareos_Detalle", primaryKeys = {"IdTareo", "Item"},
        indices = {
                @Index(value = {"Dni"}),
                @Index(value = {"IdPlanilla"}),
                @Index(value = {"IdConsumidor"}),
                @Index(value = {"IdCultivo"}),
                @Index(value = {"IdVariedad"}),
                @Index(value = {"IdActividad"}),
                @Index(value = {"IdLabor"})
        })
public class TareoDetalles {
    @ColumnInfo(name = "IdEmpresa")
    private String idEmpresa;
    @NonNull
    @ColumnInfo(name = "IdTareo")
    private String idTareo;
    @NonNull
    @ColumnInfo(name = "Item")
    private int item;
    @ColumnInfo(name = "Dni")
    private String dni;
    @ColumnInfo(name = "IdPlanilla")
    private String idPlanilla;
    @ColumnInfo(name = "IdConsumidor")
    private String idConsumidor;
    @Ignore
    private String consumidor;
    @Ignore
    private String nombres;
    @ColumnInfo(name = "IdCultivo")
    private String idCultivo;
    @Ignore
    private String cultivo;
    @ColumnInfo(name = "IdVariedad")
    private String idVariedad;
    @Ignore
    private String variedad;
    @ColumnInfo(name = "IdActividad")
    private String idActividad;
    @Ignore
    private String actividad;
    @ColumnInfo(name = "IdLabor")
    private String idLabor;
    @Ignore
    private String labor;
    @ColumnInfo(name = "SubTotalHoras")
    private Double subTotalHoras;
    @ColumnInfo(name = "SubTotalRendimiento")
    private Double subTotalRendimiento;
    @ColumnInfo(name = "Observacion")
    private String observacion;
    @ColumnInfo(name = "ingreso")
    private String ingreso;
    @ColumnInfo(name = "salida")
    private String salida;
    @ColumnInfo(name = "homologar")
    private int homologar;

    @Ignore
    AppDatabase db;

    public TareoDetalles() {
        db = DataGreenApp.getAppDatabase();
    }

    public TareoDetalles(TareoDetalles detalle){
        this.idEmpresa = detalle.idEmpresa;
        this.idTareo = detalle.idTareo;
        this.item = detalle.item;
        this.dni = detalle.dni;
        this.idPlanilla = detalle.idPlanilla;
        this.idConsumidor = detalle.idConsumidor;
        this.consumidor = detalle.consumidor;
        this.nombres = detalle.nombres;
        this.idCultivo = detalle.idCultivo;
        this.cultivo = detalle.cultivo;
        this.idVariedad = detalle.idVariedad;
        this.variedad = detalle.variedad;
        this.idActividad = detalle.idActividad;
        this.actividad = detalle.actividad;
        this.idLabor = detalle.idLabor;
        this.labor = detalle.labor;
        this.subTotalHoras = detalle.subTotalHoras;
        this.subTotalRendimiento = detalle.subTotalRendimiento;
        this.observacion = detalle.observacion;
        this.ingreso = detalle.ingreso;
        this.salida = detalle.salida;
        this.homologar = detalle.homologar;
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
