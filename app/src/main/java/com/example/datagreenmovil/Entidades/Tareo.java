package com.example.datagreenmovil.Entidades;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.Logica.Swal;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Tareo {
    private String idEmpresa;
    private String id;
    private String idUsuario;
    private LocalDate fecha;
    private String idTurno;
    private String idEstado;
    private int totalDetalles;
    private double totalHoras;
    private double totalRdtos;
    private String observaciones;
    private Context ctx;
    ArrayList<TareoDetalle> detalle = new ArrayList<>();
    SharedPreferences sharedPreferences;


    public Tareo() {
        this.id ="";
        this.idUsuario="";
        this.fecha= LocalDate.now();
        this.idTurno="";
        this.totalDetalles=0;
        this.totalHoras=0;
        this.totalRdtos=0;
        this.observaciones ="";
        this.detalle=new ArrayList<>();
    }

    public Tareo(String ultimoIdTareo, String idUsuario) {
        this.id = Funciones.siguienteCorrelativo(ultimoIdTareo,'A');
        this.idUsuario=idUsuario;
        this.fecha= LocalDate.now();
        this.idTurno="";
        this.totalDetalles=0;
        this.totalHoras=0;
        this.totalRdtos=0;
        this.observaciones ="";
        this.detalle=new ArrayList<>();
    }


    public Tareo(String idTareo, ConexionSqlite objSqlite, Context context) throws Exception {
        sharedPreferences = context.getSharedPreferences("objConfLocal",ctx.MODE_PRIVATE);
        try {
            if (idTareo == null || idTareo.length() == 0){
                //idTareo = new Tareo(objSqlite.doItBaby(objSqlite.obtQuery("OBTENER ULTIMO trx_Tareos"),null,"READ",""),objConfLocal.get("ID_USUARIO_ACTUAL"));
                this.idEmpresa = sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA");
                List<String> p = new ArrayList<>();
                p.add(sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"));
//                p.add(sharedPreferences.getString("MAC","!MAC"));
                p.add(sharedPreferences.getString("IMEI","IMEI"));
                Cursor lastId = objSqlite.doItBaby("SELECT IFNULL((SELECT MAX(Id) Id FROM trx_Tareos), '000000000')", null, "READ");
                lastId.moveToFirst();
                String last = lastId.getString(0);
                this.id = Funciones.siguienteCorrelativo(last,'A');
//                ActualizarCorrelativos()
// //                NOS ASEGURAMOS QUE EL ID CONTENGA EL ID DE DISPOSITIVO
                if(this.id.length() == 9){
                    this.id = sharedPreferences.getString("ID_DISPOSITIVO", "!ID_DISPOSITIVO") + this.id;
                }
                this.idUsuario=sharedPreferences.getString("ID_USUARIO_ACTUAL","!ID_USUARIO_ACTUAL");
                this.fecha= LocalDate.now();
                this.idTurno="";
                this.idEstado="PE";
                this.totalDetalles=0;
                this.totalHoras=0;
                this.totalRdtos=0;
                this.observaciones ="";
                this.detalle=new ArrayList<>();
            }else{
                Cursor c;
                List<String> p = new ArrayList<>();
                p.add(sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"));
                p.add(idTareo);
                c = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER trx_Tareos X ID") ,p,"READ");
                c.moveToFirst();
                //IdEmpresa	Id	Fecha	IdTurno	IdEstado	IdUsuarioCrea	FechaHoraCreacion	IdUsuarioActualiza	FechaHoraActualizacion	FechaHoraTransferencia	TotalHoras	TotalRendimientos	TotalDetalles	Observaciones
                this.idEmpresa = c.getString(0);
                this.id = c.getString(1);
                this.fecha= LocalDate.parse(c.getString(2));
                this.idTurno=c.getString(3);
                this.idEstado=c.getString(4);
                this.idUsuario=c.getString(5);
                this.totalHoras=c.getDouble(10);
                this.totalRdtos=c.getDouble(11);
                this.totalDetalles=c.getInt(12);
                this.observaciones =c.getString(13);
                this.detalle=new ArrayList<>();

                String query = objSqlite.obtQuery("OBTENER trx_Tareos_Detalle X ID");
                c = objSqlite.doItBaby(query ,p,"READ");
                //c.moveToFirst();
                while (c.moveToNext()){
                    this.detalle.add(new TareoDetalle(c,c.getPosition()));
                }
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    public Tareo(String idTareo, String idUsuario, LocalDate fecha, String idTurno, int totalDetalles, double totalHoras, double totalRdtos, ArrayList<TareoDetalle> detalle) {
        this.id = idTareo;
        this.idUsuario = idUsuario;
        this.fecha = fecha;
        this.idTurno = idTurno;
        this.totalDetalles = totalDetalles;
        this.totalHoras = totalHoras;
        this.totalRdtos = totalRdtos;
        this.observaciones ="";
        this.detalle = detalle;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(String idTurno) {
        this.idTurno = idTurno;
    }

    public String getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(String idEstado) {
        this.idEstado = idEstado;
    }

    public int getTotalDetalles() {
        return totalDetalles;
    }

    public void setTotalDetalles(int totalDetalles) {
        this.totalDetalles = totalDetalles;
    }

    public double getTotalHoras() {
        return totalHoras;
    }

    public void setTotalHoras(double totalHoras) {
        this.totalHoras = totalHoras;
    }

    public double getTotalRdtos() {
        return totalRdtos;
    }

    public void setTotalRdtos(double totalRdtos) {
        this.totalRdtos = totalRdtos;
    }

    public String  getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    public void setCtx(Context ctx){
        this.ctx = ctx;
    }
    public Context getCtx(){ return ctx;}

    public List<TareoDetalle> getDetalle() { return detalle; }



    public void setDetalle(ArrayList<TareoDetalle> detalle) {
        this.detalle = detalle;
    }
    public void agregarDetalle(TareoDetalle detalle, Context ctx) {
        TareoDetalle aux = new TareoDetalle(detalle);
            this.detalle.add(aux);
            this.setTotalHoras(this.getTotalHoras() + aux.getHoras());
            this.setTotalRdtos(this.getTotalRdtos() + aux.getRdtos());
            this.setTotalDetalles(this.getTotalDetalles()+1);
    }
    public boolean eliminarItemDetalle(int item){
        TareoDetalle aux = new TareoDetalle();
        int i=0;
        for(TareoDetalle td : this.getDetalle()){
            if(item==td.getItem()){
                aux=td;
                this.getDetalle().remove(i);
                this.setTotalDetalles(this.getTotalDetalles()-1);
                this.setTotalHoras(this.getTotalHoras() - aux.getHoras());
                this.setTotalRdtos(this.getTotalRdtos() - aux.getRdtos());
                this.actualizarIndex();
                //break;
                //guardarDetalle(objSqlite);
                return true;
            }
//            if(aux.getIdTareo().length()>0 && item!=td.getItem()){
//                td.setItem(i+1);
//            }
            i++;
        }
        return false; //aux.getIdTareo().length() == 0;
    }

    public void actualizarIndex(){
        int i=0;
        for(TareoDetalle td : this.getDetalle()){
            td.setItem(i+1);
            i++;
        }
    }

    public void obtenerUltimoDetalle(){
        TareoDetalle td = new TareoDetalle();
        Swal.success(ctx, "TITULO", td.getActividad(), 5000);
    }

    public boolean guardar(ConexionSqlite objSqlite, ConfiguracionLocal objConfLocal) throws Exception {
        //IdEmpresa	Id	Fecha	IdTurno	IdEstado	IdUsuarioCrea	FechaHoraCreacion	IdUsuarioActualiza	FechaHoraActualizacion	FechaHoraTransferencia	TotalHoras	TotalRendimientos	TotalDetalles	Observaciones
        //01	00I000000001	2022-11-08	M	PE	44363337	2022-11-08 17:24:10	44363337	2022-11-08 17:24:10		40	0	5
        //Date date = Calendar.getInstance().getTime();
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String fechaHora;// = dateFormat.format(date);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        fechaHora= dtf.format(now);
        //System.out.println(dtf.format(now));

        List<String> l = new ArrayList<>();
        l.add(idEmpresa);
        l.add(id);
        String y = String.valueOf(fecha.getYear());
        String m = fecha.getMonthValue()<10? "0"+fecha.getMonthValue():String.valueOf(fecha.getMonthValue());
        String d = fecha.getDayOfMonth()<10? "0"+fecha.getDayOfMonth():String.valueOf(fecha.getDayOfMonth());
        //String fechaHora =y + "-" + m + "-" + d;
        l.add(y + "-" + m + "-" + d);
        l.add(idTurno);
        l.add(idEstado);
        l.add(idUsuario);
        l.add(fechaHora);
        l.add(idUsuario);
        l.add(fechaHora);
        l.add(null);
        l.add(String.valueOf(totalHoras));
        l.add(String.valueOf(totalRdtos));
        l.add(String.valueOf(totalDetalles));
        l.add(observaciones);
        if(!objSqlite.GuardarRegistro("trx_Tareos",l)) return false;
        //ArrayList<TareoDetalle> detalle = this.detalle; //new ArrayList<>();//--->CONTINUAR AQUI: IMPLEMENTAR GUARDAR DETALLE DEL TAREO
//        for (TareoDetalle det : this.detalle) {
//           if(!det.guardar(objSqlite)) return false;
//        }
        guardarDetalle(objSqlite);
        objSqlite.ActualizarCorrelativos(objConfLocal,"trx_Tareos",id);
        return true;
    }

    public boolean guardarDetalle(ConexionSqlite objSqlite) throws Exception {
        List<String> p = new ArrayList<>();
        p.add(this.getIdEmpresa());
        p.add(this.getId());
        objSqlite.doItBaby(objSqlite.obtQuery("ELIMINAR trx_Tareos_Detalle EN BLOQUE"),p,"WRITE");
        for (TareoDetalle det : this.detalle) {
            if (!det.guardar(objSqlite,this.getIdUsuario()))
                return false;
        }
//        //RECALCULAR SUBTOTALES GENERALES
////        List<String> p = new ArrayList<>();
//        p.clear();
//        p.add(this.getIdUsuario());
//        p.add(this .getIdEmpresa());
//        p.add(this.getId());
//        objSqlite.doItBaby(objSqlite.obtQuery("ACTUALIZAR SUBTOTALES trx_Tareos"),p,"WRITE");
        return true;
    }
}
