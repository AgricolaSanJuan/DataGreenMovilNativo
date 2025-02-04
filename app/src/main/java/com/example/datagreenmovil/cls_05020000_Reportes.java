package com.example.datagreenmovil;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle.DTO.ReporteDTO;
import com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle.ReportesHelper;
import com.example.datagreenmovil.Entidades.ClaveValor;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.PopUpCalendario;
import com.example.datagreenmovil.Entidades.Tabla;
import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.Logica.Swal;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class cls_05020000_Reportes extends AppCompatActivity {
    //@Jota:2023-05-27 -> INICIO DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    ConexionSqlite objSqlite;
    ConexionBD objSql;
    ConfiguracionLocal objConfLocal;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador;
    Dialog dlg_PopUp;
    //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
    //...

    RadioButton rad_TareosReportesActivity_Fecha;
    String s_ListarDesde = LocalDate.now().plusDays(-0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    Context ctx;
    TextView fechaReporte;
    Spinner spi_TareosReportesActivity_Supervisores;
    RecyclerView rcv_TareosReportes_RCV1, rcv_TareosReportes_RCV2, rcv_TareosReportes_RCV3;
    Calendar calendar = Calendar.getInstance();
    int anioSeleccionado = calendar.get(Calendar.YEAR);
    int mesSeleccionado = calendar.get(Calendar.MONTH) + 1;
    int diaSeleccionado = calendar.get(Calendar.DAY_OF_MONTH);
    String fechaSeleccionada, idSupervisorSeleccionado, nombreSupervisorSeleccionado;
    BottomNavigationItemView miMenu, miTotales, miConsumidor, miActividad;
    BottomNavigationView bnvReporte;
    ConstraintLayout llyTotales, llyConsumidor, llyActividad;


    DatePickerDialog.OnDateSetListener setListener;
    private ReportesHelper reportesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_05020000_reportes_008);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //@Jota:2023-05-27 -> INICIO DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        sharedPreferences = this.getSharedPreferences("objConfLocal", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        ctx = this;
        try {
            if (getIntent().getExtras() != null) {
                objConfLocal = (ConfiguracionLocal) getIntent().getSerializableExtra("ConfiguracionLocal");
            }
            objSql = new ConexionBD(this);
            objSqlite = new ConexionSqlite(this, DataGreenApp.DB_VERSION());
//            objConfLocal=new ConfiguracionLocal(objSqlite.obtenerConfiguracionLocal(objConfLocal));
//            objConfLocal.set("ULTIMA_ACTIVIDAD","PlantillaBase");

            referenciarControles();
            setearControles();

            View customViewConsumidores = LayoutInflater.from(this).inflate(R.layout.custom_bottom_nav_item, null);
            miConsumidor.removeAllViews();
            miConsumidor.addView(customViewConsumidores);
            TextView titleConsumidores = customViewConsumidores.findViewById(R.id.label);
            titleConsumidores.setText("CONSUMIDORES");
            ImageView iconConsumidores = customViewConsumidores.findViewById(R.id.icon);
            iconConsumidores.setImageDrawable(getDrawable(R.drawable.ic_consumidor));

            View customViewActividad = LayoutInflater.from(this).inflate(R.layout.custom_bottom_nav_item, null);
            miActividad.removeAllViews();
            miActividad.addView(customViewActividad);
            TextView titleActividad = customViewActividad.findViewById(R.id.label);
            titleActividad.setText("ACTIVIDADES");
            ImageView iconActividad = customViewActividad.findViewById(R.id.icon);
            iconActividad.setImageDrawable(getDrawable(R.drawable.ic_actividad));

            View customViewMenu = LayoutInflater.from(this).inflate(R.layout.custom_bottom_nav_item, null);
            miMenu.removeAllViews();
            miMenu.addView(customViewMenu);

            TextView titleMenu = customViewMenu.findViewById(R.id.label);
            titleMenu.setText("VOLVER");
            ImageView iconMenu = customViewMenu.findViewById(R.id.icon);
            iconMenu.setImageDrawable(getDrawable(R.drawable.ic_arrow_left));

            View customViewTotal = LayoutInflater.from(this).inflate(R.layout.custom_bottom_nav_item, null);
            miTotales.removeAllViews();
            miTotales.addView(customViewTotal);
            TextView titleTotal = customViewTotal.findViewById(R.id.label);
            titleTotal.setText("RESUMEN");
            ImageView iconTotal = customViewTotal.findViewById(R.id.icon);
            iconTotal.setImageDrawable(getDrawable(R.drawable.ic_all_report));


            bnvReporte.setOnItemSelectedListener(item -> {
                Log.i("ITEM", item.getTitle().toString());
                return false;
            });

            miMenu.setOnClickListener(view -> finish());

            miTotales.setOnClickListener(menuItem -> {
                bnvReporte.setSelectedItemId(R.id.miTotales);

                //                SIMULAMOS LA SELECCIÓN
                TextView textViewActividad = miActividad.findViewById(R.id.label);
                textViewActividad.setTextColor(getColor(R.color.negroOficial));
                ImageView imageViewActividad = miActividad.findViewById(R.id.icon);
                imageViewActividad.setImageTintList(getColorStateList(R.color.negroOficial));

                TextView textViewConsumidor = miConsumidor.findViewById(R.id.label);
                textViewConsumidor.setTextColor(getColor(R.color.negroOficial));
                ImageView imageViewConsumidor = miConsumidor.findViewById(R.id.icon);
                imageViewConsumidor.setImageTintList(getColorStateList(R.color.negroOficial));

                TextView textViewTotales = miTotales.findViewById(R.id.label);
                textViewTotales.setTextColor(getColor(R.color.blancoOficial));
                ImageView imageViewTotales = miTotales.findViewById(R.id.icon);
                imageViewTotales.setImageTintList(getColorStateList(R.color.blancoOficial));


                ConstraintLayout.LayoutParams layoutParamsOpen = (ConstraintLayout.LayoutParams) llyTotales.getLayoutParams();
                ConstraintLayout.LayoutParams layoutParamsConsumidor = (ConstraintLayout.LayoutParams) llyConsumidor.getLayoutParams();
                ConstraintLayout.LayoutParams layoutParamsActividad = (ConstraintLayout.LayoutParams) llyActividad.getLayoutParams();

                layoutParamsOpen.width = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
                layoutParamsOpen.height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
                layoutParamsConsumidor.width = 1;
                layoutParamsConsumidor.height = 1;
                layoutParamsActividad.width = 1;
                layoutParamsActividad.height = 1;

                llyTotales.setLayoutParams(layoutParamsOpen);
                llyConsumidor.setLayoutParams(layoutParamsConsumidor);
                llyActividad.setLayoutParams(layoutParamsActividad);
            });

            miActividad.setOnClickListener(menuItem -> {
                bnvReporte.setSelectedItemId(R.id.miActividad);

//                SIMULAMOS LA SELECCIÓN
                TextView textViewActividad = miActividad.findViewById(R.id.label);
                textViewActividad.setTextColor(getColor(R.color.blancoOficial));
                ImageView imageViewActividad = miActividad.findViewById(R.id.icon);
                imageViewActividad.setImageTintList(getColorStateList(R.color.blancoOficial));

                TextView textViewConsumidor = miConsumidor.findViewById(R.id.label);
                textViewConsumidor.setTextColor(getColor(R.color.negroOficial));
                ImageView imageViewConsumidor = miConsumidor.findViewById(R.id.icon);
                imageViewConsumidor.setImageTintList(getColorStateList(R.color.negroOficial));

                TextView textViewTotales = miTotales.findViewById(R.id.label);
                textViewTotales.setTextColor(getColor(R.color.negroOficial));
                ImageView imageViewTotales = miTotales.findViewById(R.id.icon);
                imageViewTotales.setImageTintList(getColorStateList(R.color.negroOficial));

                ConstraintLayout.LayoutParams layoutParamsOpen = (ConstraintLayout.LayoutParams) llyActividad.getLayoutParams();
                ConstraintLayout.LayoutParams layoutParamsConsumidor = (ConstraintLayout.LayoutParams) llyConsumidor.getLayoutParams();
                ConstraintLayout.LayoutParams layoutParamsTotales = (ConstraintLayout.LayoutParams) llyTotales.getLayoutParams();

                layoutParamsOpen.width = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
                layoutParamsOpen.height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
                layoutParamsConsumidor.width = 1;
                layoutParamsConsumidor.height = 1;
                layoutParamsTotales.width = 1;
                layoutParamsTotales.height = 1;

                llyActividad.setLayoutParams(layoutParamsOpen);
                llyConsumidor.setLayoutParams(layoutParamsConsumidor);
                llyTotales.setLayoutParams(layoutParamsTotales);
            });

            miConsumidor.setOnClickListener(menuItem -> {

                bnvReporte.setSelectedItemId(R.id.miConsumidor);

                //                SIMULAMOS LA SELECCIÓN
                TextView textViewActividad = miActividad.findViewById(R.id.label);
                textViewActividad.setTextColor(getColor(R.color.negroOficial));
                ImageView imageViewActividad = miActividad.findViewById(R.id.icon);
                imageViewActividad.setImageTintList(getColorStateList(R.color.negroOficial));

                TextView textViewConsumidor = miConsumidor.findViewById(R.id.label);
                textViewConsumidor.setTextColor(getColor(R.color.blancoOficial));
                ImageView imageViewConsumidor = miConsumidor.findViewById(R.id.icon);
                imageViewConsumidor.setImageTintList(getColorStateList(R.color.blancoOficial));

                TextView textViewTotales = miTotales.findViewById(R.id.label);
                textViewTotales.setTextColor(getColor(R.color.negroOficial));
                ImageView imageViewTotales = miTotales.findViewById(R.id.icon);
                imageViewTotales.setImageTintList(getColorStateList(R.color.negroOficial));

                ConstraintLayout.LayoutParams layoutParamsOpen = (ConstraintLayout.LayoutParams) llyConsumidor.getLayoutParams();
                ConstraintLayout.LayoutParams layoutParamsActividad = (ConstraintLayout.LayoutParams) llyActividad.getLayoutParams();
                ConstraintLayout.LayoutParams layoutParamsTotales = (ConstraintLayout.LayoutParams) llyTotales.getLayoutParams();

                layoutParamsOpen.width = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
                layoutParamsOpen.height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
                layoutParamsActividad.width = 1;
                layoutParamsActividad.height = 1;
                layoutParamsTotales.width = 1;
                layoutParamsTotales.height = 1;

                llyConsumidor.setLayoutParams(layoutParamsOpen);
                llyActividad.setLayoutParams(layoutParamsActividad);
                llyTotales.setLayoutParams(layoutParamsTotales);
            });

            miTotales.callOnClick();

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

        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
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
//        rad_TareosReportesActivity_Fecha = (RadioButton) findViewById(R.id.c008_rad_TareosReportesActivity_Fecha_v);
        fechaReporte = findViewById(R.id.fechaActual);

        spi_TareosReportesActivity_Supervisores = (Spinner) findViewById(R.id.c008_spi_TareosReportesActivity_Supervisores_v);
        rcv_TareosReportes_RCV1 = (RecyclerView) findViewById(R.id.c008_rcv_TareosReportes_RCV1_v);
        rcv_TareosReportes_RCV2 = (RecyclerView) findViewById(R.id.c008_rcv_TareosReportes_RCV2_v);
        rcv_TareosReportes_RCV3 = (RecyclerView) findViewById(R.id.c008_rcv_TareosReportes_RCV3_v);

        //layouts de tabs
        llyTotales = findViewById(R.id.llyTotales);
        llyConsumidor = findViewById(R.id.llyConsumidor);
        llyActividad = findViewById(R.id.llyActividad);

        bnvReporte = findViewById(R.id.bnvReportes);

        miMenu = findViewById(R.id.miMenu);
        miTotales = findViewById(R.id.miTotales);
        miConsumidor = findViewById(R.id.miConsumidor);
        miActividad = findViewById(R.id.miActividad);
    }

    public void mostrarMenuUsuario(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this::onMenuItemClick);
        popup.inflate(R.menu.mnu_00000001_menu_usuario);
        popup.show();
    }

    public boolean onMenuItemClick(MenuItem item) {
        try {
            int idControlClickeado = item.getItemId();
            if (idControlClickeado == R.id.opc_00000001_cambiar_clave_usuario_v) {
                dlg_PopUp = Funciones.obtenerDialogParaCambiarClave(this, objConfLocal, objSqlite, this);
                dlg_PopUp.show();
            } else if (idControlClickeado == R.id.opc_00000001_cerrar_sesion_v) {
                dlg_PopUp = Funciones.obtenerDialogParaCerrarSesion(this, objConfLocal, objSqlite, this);
                dlg_PopUp.show();
            } else return false;
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
            return false;
        }
        return true;
    }

    public void onClick(View view) {
        try {
            int idControlClickeado = view.getId();
            if (idControlClickeado == R.id.c008_txv_PushTituloVentana_v) {
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
//            else if (idControlClickeado == R.id.c008_fab_TareosReportesActivity_Volver_v) {
//                finish();
//            }
            else throw new IllegalStateException("Click sin programacion: " + view.getId());
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
        }
    }
    //@Jota:
    //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
    //...

    private void setearSelectorFecha() {
        fechaReporte.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                String s_Valor = "";
//                s_Valor = c022_txv_DesdeFecha.getText().toString();
                s_ListarDesde = Funciones.arreglarFecha(fechaReporte.getText().toString());
//                s_ListarDesde = Funciones.arreglarFecha(s_Valor);
                try {
                    fechaSeleccionada = s_ListarDesde.toString();
                    List<String> p = new ArrayList<>();
                    p.add(sharedPreferences.getString("ID_EMPRESA", "!ID_EMPRESA"));
                    p.add(s_ListarDesde);
                    Tabla t = new Tabla(objSqlite.doItBaby(objSqlite.obtQuery("OBTENER SUPERVISORES X DIA"), p, "READ"));
                    if (t.Filas.size() > 0) {
                        Funciones.cargarSpinner(cls_05020000_Reportes.this, spi_TareosReportesActivity_Supervisores, t, 0, 1);
                        if (t.Filas.size() > 0) {
                            spi_TareosReportesActivity_Supervisores.setSelection(0);
                        }
                    } else {
                        spi_TareosReportesActivity_Supervisores.setAdapter(null);
                        rcv_TareosReportes_RCV1.setAdapter(null);
                        rcv_TareosReportes_RCV2.setAdapter(null);
                        rcv_TareosReportes_RCV3.setAdapter(null);
                    }
                } catch (Exception ex) {
                    //throw new RuntimeException(e);
                    Funciones.mostrarError(ctx, ex);
                }
            }
        });

        fechaReporte.setText(Funciones.malograrFecha(s_ListarDesde));

        fechaReporte.setOnClickListener(view -> {
            PopUpCalendario d = new PopUpCalendario(ctx, fechaReporte);
            d.show();
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int anio, int mes, int dia) {
                try {
                    mes = mes + 1;
                    diaSeleccionado = dia;
                    mesSeleccionado = mes;
                    anioSeleccionado = anio;
                    //String fec =  (dia  < 10 ? "0" + dia : dia) + "/" + (mes < 10 ? "0" + mes : mes) + "/" + anio; //  day + "/" + month + "/" + year;
                    fechaSeleccionada = (dia < 10 ? "0" + dia : dia) + "/" + (mes < 10 ? "0" + mes : mes) + "/" + anio; //  day + "/" + month + "/" + year;
//                    rad_TareosReportesActivity_Fecha.setText(fechaSeleccionada);
                    fechaSeleccionada = "2023-12-26";
                } catch (Exception ex) {
                    Funciones.mostrarError(cls_05020000_Reportes.super.getBaseContext(), ex);
                }
            }
        };
    }

    private void setearSpinnerSupervisoresDisponibles() {
        try {
            spi_TareosReportesActivity_Supervisores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        ClaveValor obj = (ClaveValor) (parent.getItemAtPosition(position));
                        idSupervisorSeleccionado = obj.getClave();
                        nombreSupervisorSeleccionado = obj.getValor();
                        obtenerReporte();
                    } catch (Exception ex) {
                        Funciones.mostrarError(cls_05020000_Reportes.super.getBaseContext(), ex);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
        }
    }

    private void obtenerReporte() {

//        INICIALIZAMOS LOS HELPER DE LOS REPORTES
        String idUsuarioActual = sharedPreferences.getString("ID_USUARIO_ACTUAL", "!ID_USUARIO_ACTUAL");
        String idEmpresa = sharedPreferences.getString("ID_EMPRESA", "!ID_EMPRESA");
        String fecha = fechaSeleccionada;
        reportesHelper = new ReportesHelper(idEmpresa, idUsuarioActual, fecha);
//        INICIALIZAMOS LOS HELPER DE LOS REPORTES
        try {
            inflarRecyclerView1();
            inflarRecyclerView2();
            inflarRecyclerView3();
        }catch (Exception e){
            Log.e("ERRORMIGRATE", e.toString());
            Swal.error(ctx, "error", e.toString(), 15000);
        }
    }

    private void inflarRecyclerView3() {
        String query = "";
        try {
            query = objSqlite.obtQuery("TAREOS REPORTE RESUMEN 3");
        } catch (Exception e) {
            Swal.error(ctx, "info", e.toString(), 5000);
        }
        List<ReporteDTO> reporte3 = reportesHelper.obtenerReporteTareos(query);
        cls_05020300_Resumen3 adaptadorRcv = new cls_05020300_Resumen3(ctx, reporte3);
        rcv_TareosReportes_RCV3.setAdapter(adaptadorRcv);
        rcv_TareosReportes_RCV3.setLayoutManager(new LinearLayoutManager(this));
    }

    private void inflarRecyclerView2() {
        String query = "";
        try {
            query = objSqlite.obtQuery("TAREOS REPORTE RESUMEN 2");
        } catch (Exception e) {
            Swal.error(ctx, "info", e.toString(), 5000);
        }
        List<ReporteDTO> reporte2 = reportesHelper.obtenerReporteTareos(query);
        cls_05020200_Resumen2 adaptadorRcv = new cls_05020200_Resumen2(ctx, reporte2);
        rcv_TareosReportes_RCV2.setAdapter(adaptadorRcv);
        rcv_TareosReportes_RCV2.setLayoutManager(new LinearLayoutManager(this));
    }

    private void inflarRecyclerView1() {
        String query = "";
        try {
            query = objSqlite.obtQuery("TAREOS REPORTE RESUMEN 1");
        } catch (Exception e) {
            Swal.error(ctx, "info", e.toString(), 5000);
        }
        List<ReporteDTO> reporte1 = reportesHelper.obtenerReporteTareos(query);
        cls_05020100_Resumen1 adaptadorRcv = new cls_05020100_Resumen1(ctx, reporte1);
        rcv_TareosReportes_RCV1.setAdapter(adaptadorRcv);
        rcv_TareosReportes_RCV1.setLayoutManager(new LinearLayoutManager(this));
    }
}