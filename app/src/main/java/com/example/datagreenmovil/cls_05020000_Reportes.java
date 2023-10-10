package com.example.datagreenmovil;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Entidades.ClaveValor;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.Tabla;
import com.example.datagreenmovil.Logica.Funciones;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class cls_05020000_Reportes extends AppCompatActivity {
    //@Jota:2023-05-27 -> INICIO DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    ConexionSqlite objSqlite;
    ConexionBD objSql;
    ConfiguracionLocal objConfLocal;
    TextView txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador;
    Dialog dlg_PopUp;
    //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
    //...

    RadioButton rad_TareosReportesActivity_Fecha;
    Spinner spi_TareosReportesActivity_Supervisores;
    RecyclerView rcv_TareosReportes_RCV1, rcv_TareosReportes_RCV2, rcv_TareosReportes_RCV3;
    Calendar calendar = Calendar.getInstance();
    int anioSeleccionado = calendar.get(Calendar.YEAR);
    int mesSeleccionado = calendar.get(Calendar.MONTH) + 1;
    int diaSeleccionado = calendar.get(Calendar.DAY_OF_MONTH);
    String fechaSeleccionada, idSupervisorSeleccionado, nombreSupervisorSeleccionado;


    DatePickerDialog.OnDateSetListener setListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_05020000_reportes_008);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //@Jota:2023-05-27 -> INICIO DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        try{
            if(getIntent().getExtras()!=null){
                objConfLocal=(ConfiguracionLocal) getIntent().getSerializableExtra("ConfiguracionLocal");
            }
            objSql = new ConexionBD(this);
            objSqlite = new ConexionSqlite(this,objConfLocal);
//            objConfLocal=new ConfiguracionLocal(objSqlite.obtenerConfiguracionLocal(objConfLocal));
//            objConfLocal.set("ULTIMA_ACTIVIDAD","PlantillaBase");

            referenciarControles();
            setearControles();
            Funciones.mostrarEstatusGeneral(this.getBaseContext(),
                    objConfLocal,
                    txv_PushTituloVentana,
                    txv_PushRed,
                    txv_NombreApp,
                    txv_PushVersionApp,
                    txv_PushVersionDataBase,
                    txv_PushIdentificador
            );
            //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
            //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
            //...

        }catch (Exception ex){
            Funciones.mostrarError(this,ex);
        }
    }

    //@Jota:2023-05-27 -> LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    private void setearControles() {
        //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
        //...
        setearSelectorFecha();
        setearSpinnerSupervisoresDisponibles();
    }

    private void referenciarControles() {
        txv_PushTituloVentana = findViewById(R.id.c008_txv_PushTituloVentana_v);
        txv_PushRed = findViewById(R.id.c008_txv_PushRed_v);
        txv_NombreApp = findViewById(R.id.c008_txv_NombreApp_v);
        txv_PushVersionApp = findViewById(R.id.c008_txv_PushVersionApp_v);
        txv_PushVersionDataBase = findViewById(R.id.c008_txv_PushVersionDataBase_v);
        txv_PushIdentificador = findViewById(R.id.c008_txv_PushIdentificador_v);

        //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
        //...
        rad_TareosReportesActivity_Fecha = (RadioButton) findViewById(R.id.c008_rad_TareosReportesActivity_Fecha_v);
        spi_TareosReportesActivity_Supervisores = (Spinner) findViewById(R.id.c008_spi_TareosReportesActivity_Supervisores_v);
        rcv_TareosReportes_RCV1 = (RecyclerView) findViewById(R.id.c008_rcv_TareosReportes_RCV1_v);
        rcv_TareosReportes_RCV2 = (RecyclerView) findViewById(R.id.c008_rcv_TareosReportes_RCV2_v);
        rcv_TareosReportes_RCV3 = (RecyclerView) findViewById(R.id.c008_rcv_TareosReportes_RCV3_v);
    }
    public void mostrarMenuUsuario(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this::onMenuItemClick);
        popup.inflate(R.menu.mnu_00000001_menu_usuario);
        popup.show();
    }

    public boolean onMenuItemClick(MenuItem item) {
        try{
            int idControlClickeado = item.getItemId();
            if (idControlClickeado==R.id.opc_00000001_cambiar_clave_usuario_v){
                dlg_PopUp = Funciones.obtenerDialogParaCambiarClave(this,objConfLocal,objSqlite,this);
                dlg_PopUp.show();
            } else if (idControlClickeado==R.id.opc_00000001_cerrar_sesion_v) {
                dlg_PopUp = Funciones.obtenerDialogParaCerrarSesion(this,objConfLocal,objSqlite,this);
                dlg_PopUp.show();
            }else return false;
        }catch(Exception ex){
            Funciones.mostrarError(this,ex);
            return false;
        }
        return true;
    }

    public void onClick(View view) {
        try {
            int idControlClickeado = view.getId();
            if (idControlClickeado == R.id.c008_txv_PushTituloVentana_v){
                Funciones.popUpTablasPendientesDeEnviar(this);
            } else if (idControlClickeado == R.id.c008_txv_PushRed_v) {
                objSql.probarConexion(objConfLocal);
                Funciones.mostrarEstatusGeneral(this.getBaseContext(),
                        objConfLocal,
                        txv_PushTituloVentana,
                        txv_PushRed,
                        txv_NombreApp,
                        txv_PushVersionApp,
                        txv_PushVersionDataBase,
                        txv_PushIdentificador
                );
            } else if (idControlClickeado == R.id.c008_txv_PushVersionApp_v || idControlClickeado == R.id.c008_txv_PushVersionDataBase_v) {
                Funciones.popUpStatusVersiones(this);
            } else if (idControlClickeado == R.id.c008_txv_PushIdentificador_v) {
                mostrarMenuUsuario(this.txv_PushIdentificador);
            }
            //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
            //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
            //...
            else if (idControlClickeado == R.id.c008_fab_TareosReportesActivity_Volver_v) {
                finish();
            } else throw new IllegalStateException("Click sin programacion: " + view.getId());
        } catch (Exception ex) {
            Funciones.mostrarError(this,ex);
        }
    }
    //@Jota:
    //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
    //...

    private void setearSelectorFecha() {
        rad_TareosReportesActivity_Fecha.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //Locale.setDefault(Spani);
                                            mesSeleccionado = mesSeleccionado - 1;
                                            DatePickerDialog datePickerDialog = new DatePickerDialog(cls_05020000_Reportes.this, android.R.style.Theme_Holo, setListener, anioSeleccionado, mesSeleccionado, diaSeleccionado);
                                            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            datePickerDialog.show();
                                        }
                                    }
        );
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int anio, int mes, int dia) {
                try{
                    mes = mes + 1;
                    diaSeleccionado =dia;
                    mesSeleccionado =mes;
                    anioSeleccionado =anio;
                    //String fec =  (dia  < 10 ? "0" + dia : dia) + "/" + (mes < 10 ? "0" + mes : mes) + "/" + anio; //  day + "/" + month + "/" + year;
                    fechaSeleccionada = (dia  < 10 ? "0" + dia : dia) + "/" + (mes < 10 ? "0" + mes : mes) + "/" + anio; //  day + "/" + month + "/" + year;
                    rad_TareosReportesActivity_Fecha.setText(fechaSeleccionada);
                    fechaSeleccionada = anio + "-" + (mes < 10 ? "0" + mes : mes) + "-" + (dia  < 10 ? "0" + dia : dia) ;

                    List<String> p = new ArrayList<>();
                    p.add(objConfLocal.get("ID_EMPRESA"));
                    p.add(fechaSeleccionada);
                    Tabla t = new Tabla(objSqlite.doItBaby(objSqlite.obtQuery("OBTENER SUPERVISORES X DIA"),p,"READ"));
                    Funciones.cargarSpinner(cls_05020000_Reportes.this,spi_TareosReportesActivity_Supervisores,t,0,1);
                }catch (Exception ex){
                    Funciones.mostrarError(cls_05020000_Reportes.super.getBaseContext(),ex);
                }
            }
        };
    }

    private void setearSpinnerSupervisoresDisponibles() {
        try{
            spi_TareosReportesActivity_Supervisores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
                    try{
                        ClaveValor obj = (ClaveValor)(parent.getItemAtPosition(position));
                        idSupervisorSeleccionado = obj.getClave();
                        nombreSupervisorSeleccionado = obj.getValor();
                        obtenerReporte();
                    }catch (Exception ex){
                        Funciones.mostrarError(cls_05020000_Reportes.super.getBaseContext(),ex);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }catch (Exception ex){
             Funciones.mostrarError(this,ex);
        }
    }

    private void obtenerReporte() {
        try{
            List<String> p = new ArrayList<>();
            p.add(objConfLocal.get("ID_EMPRESA"));
            p.add(fechaSeleccionada);
            p.add(idSupervisorSeleccionado);
            Cursor c = objSqlite.doItBaby(objSqlite.obtQuery("TAREOS REPORTE RESUMEN 1"),p,"READ");
            inflarRecyclerView1(c);
            c = objSqlite.doItBaby(objSqlite.obtQuery("TAREOS REPORTE RESUMEN 2"),p,"READ");
            inflarRecyclerView2(c);
            c = objSqlite.doItBaby(objSqlite.obtQuery("TAREOS REPORTE RESUMEN 3"),p,"READ");
            inflarRecyclerView3(c);
        }catch (Exception ex){
             Funciones.mostrarError(this,ex);
        }
    }

    private void inflarRecyclerView3(Cursor c) {
        try{
            cls_05020300_Resumen3 adaptadorRcv = new cls_05020300_Resumen3(this,c);
            rcv_TareosReportes_RCV3.setAdapter(adaptadorRcv);
            rcv_TareosReportes_RCV3.setLayoutManager(new LinearLayoutManager(this));
        }catch (Exception ex){
             Funciones.mostrarError(this,ex);
        }
    }

    private void inflarRecyclerView2(Cursor c) {
        try{
            cls_05020200_Resumen2 adaptadorRcv = new cls_05020200_Resumen2(this,c);
            rcv_TareosReportes_RCV2.setAdapter(adaptadorRcv);
            rcv_TareosReportes_RCV2.setLayoutManager(new LinearLayoutManager(this));
        }catch (Exception ex){
             Funciones.mostrarError(this,ex);
        }
    }

    private void inflarRecyclerView1(Cursor c) {
        try{
            cls_05020100_Resumen1 adaptadorRcv = new cls_05020100_Resumen1(this,c);
            rcv_TareosReportes_RCV1.setAdapter(adaptadorRcv);
            rcv_TareosReportes_RCV1.setLayoutManager(new LinearLayoutManager(this));
        }catch (Exception ex){
             Funciones.mostrarError(this,ex);
        }
    }
}