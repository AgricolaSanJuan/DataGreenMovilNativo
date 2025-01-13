package com.example.datagreenmovil.ui.TareosMain.Dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.DataGreenApp;
import com.example.datagreenmovil.Entidades.ClaveValor;
import com.example.datagreenmovil.Entidades.PopUpBuscarEnLista;
import com.example.datagreenmovil.Entidades.PopUpBuscarEnLista_Item;
import com.example.datagreenmovil.Entidades.Tabla;
import com.example.datagreenmovil.Entidades.Tareo;
import com.example.datagreenmovil.Entidades.TareoDetalle;
import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.databinding.DialogDetalleTareoBinding;

import org.json.JSONException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DialogDetalleTareo extends DialogFragment {

    static ConexionSqlite objSqlite;
    static HashMap<String, Tabla> hmTablas = new HashMap<>();
    static ArrayList<PopUpBuscarEnLista_Item> arl_Turnos;
    static ArrayList<PopUpBuscarEnLista_Item> arl_Actividades;
    static ArrayList<PopUpBuscarEnLista_Item> arl_Labores = null;
    static ArrayList<PopUpBuscarEnLista_Item> arl_Consumidores;
    private static DialogDetalleTareoBinding binding;
    private static SharedPreferences sharedPreferences;
    private static String accionFija;
    Context ctx;

    private static void obtenerDataParaControles(Context ctx) {
        try {
            List<String> p = new ArrayList<>();
            p.add(sharedPreferences.getString("ID_EMPRESA", "!ID_EMPRESA"));
            arl_Turnos = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Turnos"), p, "READ"));
            arl_Actividades = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Actividades"), p, "READ"));
            arl_Consumidores = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Consumidores"), p, "READ"));
            hmTablas.put("PERSONAS", new Tabla(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Personas"), p, "READ")));
        } catch (Exception ex) {
            Funciones.mostrarError(ctx, ex);
        }
    }

    public static void inicializarDuplicado(Context ctx, DialogDetalleTareoBinding bindtwo, Tareo tareo, ArrayList<Integer> listaTrabajadores, SweetAlertDialog sweetAlertDialog, Swal.ActionResult actionResult, Swal.DismissDialog dismissDialog) {
        try {
            obtenerDataParaControles(ctx);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        bindtwo.fabConfirm.setOnClickListener(view -> {

            boolean success = false;
            String message = "";
            if (accionFija.equals("duplicar") && (bindtwo.lblddtActividadValue.getText().toString().equals(".")
                    || bindtwo.txvddtLabor.getText().toString().equals(".")
                    || bindtwo.txvddtConsumidor.getText().toString().equals("."))) {
                Swal.warning(ctx, "Cuidado!", "Llene todos los datos antes de continuar.", 2000);
            } else {
                if (bindtwo.txvddtHoras.getText().toString().isEmpty()) {
                    bindtwo.txvddtHoras.setText("0.00");
                }
                if (bindtwo.txvddtRendimientos.getText().toString().isEmpty()) {
                    bindtwo.txvddtRendimientos.setText("0.00");
                }
                try {
                    sweetAlertDialog.dismissWithAnimation();
                    duplicateWorkers(tareo, listaTrabajadores, ctx, bindtwo);
                    success = true;
                    message = "Se han duplicado los detalles correctamente.";
                } catch (JSONException e) {
                    Log.e("ERROR", e.toString());
                    message = "Ha ocurrido un error al duplicar los detalles: " + e.toString();
                }
                String result = bindtwo.txvddtActividad.getText().toString(); // Lógica para obtener el resultado a duplicar
                if (actionResult != null) {
                    actionResult.onActionResult(result, sweetAlertDialog);
                    dismissDialog.onDismissDialog(success, message);
                }
            }
        });

        bindtwo.llyinputsActividad.setOnClickListener(view1 -> {
            PopUpBuscarEnLista d = new PopUpBuscarEnLista(ctx, arl_Actividades, bindtwo.txvddtActividad, bindtwo.lblddtActividadValue);
            d.show();
        });

        bindtwo.lblddtActividadValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    List<String> p = new ArrayList<>();
                    p.add("01"); //PENDIENTE: OBTENER EMPRESA DINAMICAMENTE;
                    p.add(bindtwo.txvddtActividad.getText().toString());
                    arl_Labores = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Labores"), p, "READ"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        bindtwo.llyinputsLabor.setOnClickListener(view1 -> {
            if (arl_Labores == null) {
                Funciones.notificar(ctx, "Primero debe de seleccionar una actividad.");
            } else {
                PopUpBuscarEnLista d = new PopUpBuscarEnLista(ctx, arl_Labores, bindtwo.txvddtLabor, bindtwo.lblddtLaborValue);
                d.show();
            }
        });

        bindtwo.llyinputsConsumidor.setOnClickListener(view1 -> {
            PopUpBuscarEnLista d = new PopUpBuscarEnLista(ctx, arl_Consumidores, bindtwo.txvddtConsumidor, bindtwo.lblddtConsumidorValue);
            d.show();
        });
    }

    private static void inicializarEdicion(Context ctx, DialogDetalleTareoBinding bindtwo, Tareo tareo, ArrayList<Integer> listaTrabajadores, SweetAlertDialog sweetAlertDialog, Swal.ActionResult actionResult, Swal.DismissDialog dismissDialog) {
        try {
            obtenerDataParaControles(ctx);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        boolean hideHours = sharedPreferences.getBoolean("ACTUALIZAR_HORAS", true);
//        OCULTAMOS LOS VALORES QUE NO DEBERÍAMOS TENER EN CUENTA
//        bindtwo.lblddtActividadValue.setVisibility(View.GONE);
//        bindtwo.llyPrincipalActividad.setVisibility(View.GONE);
//        bindtwo.llyddtActividad.setVisibility(View.GONE);

        if (hideHours) {
            bindtwo.llyPrincipalHoras.setVisibility(View.GONE);
        } else {
            bindtwo.llyPrincipalHoras.setVisibility(View.VISIBLE);
        }

//        bindtwo.llyddtActividad.setVisibility(View.GONE);
//        bindtwo.llyPrincipalLabor.setVisibility(View.GONE);
//        bindtwo.llyddtLabor.setVisibility(View.GONE);
//        bindtwo.llyPrincipalConsumidor.setVisibility(View.GONE);
//        bindtwo.llyddtConsumidor.setVisibility(View.GONE);

        //                ACTIVIDAD
        bindtwo.cbActividad.setOnCheckedChangeListener((buttonView, isChecked) -> bindtwo.cbLabor.setChecked(isChecked));
        bindtwo.cbLabor.setOnCheckedChangeListener((buttonView, isChecked) -> bindtwo.cbActividad.setChecked(isChecked));

        bindtwo.fabConfirm.setOnClickListener(view -> {
            boolean success = false;
            String message = "";
            String idUsuario = sharedPreferences.getString("ID_USUARIO_ACTUAL", "!ID_USUARIO_ACTUAL");
            Double horasEditar = Double.valueOf(!bindtwo.txvddtHoras.getText().toString().isEmpty() ? bindtwo.txvddtHoras.getText().toString() : "0.00");
            String idActividadEditar = bindtwo.txvddtActividad.getText().toString();
            String actividadEditar = bindtwo.lblddtActividad.getText().toString();
            String idLaborEditar = bindtwo.txvddtLabor.getText().toString();
            String laborEditar = bindtwo.lblddtLabor.getText().toString();
            String idConsumidorEditar = bindtwo.txvddtConsumidor.getText().toString();
            String consumidorEditar = bindtwo.lblddtConsumidor.getText().toString();
            Double rendimientosEditar = Double.valueOf(!bindtwo.txvddtRendimientos.getText().toString().isEmpty() ? bindtwo.txvddtRendimientos.getText().toString() : "0.00");
            ;
            for (int itemEditar : listaTrabajadores) {
                TareoDetalle detalleActual = new TareoDetalle();
                detalleActual = tareo.getDetalle().get(itemEditar - 1);
//                PROCESAMOS SOLO LA DATA QUE TIENE EL CHECK SELECCIONADO PARA PODER REALIZAR LA EDICIÓN DE LO NECESARIO
//                ACTIVIDAD
                if (bindtwo.cbActividad.isChecked()) {
                    if (!bindtwo.txvddtActividad.getText().toString().equals(".") || !bindtwo.txvddtLabor.getText().toString().equals(".")) {
                        detalleActual.setIdActividad(idActividadEditar);
                        detalleActual.setActividad(actividadEditar);
                        detalleActual.setIdLabor(idLaborEditar);
                        detalleActual.setLabor(laborEditar);
                    }
                }
//                LABOR
//                if(bindtwo.cbLabor.isChecked()){
////                    bindtwo.cbActividad.setChecked(bindtwo.cbLabor.isChecked());
//
//                }
//                CONSUMIDOR
                if (bindtwo.cbConsumidor.isChecked()) {
                    if (!bindtwo.txvddtConsumidor.getText().toString().equals(".")) {
                        detalleActual.setIdConsumidor(idConsumidorEditar);
                        detalleActual.setConsumidor(consumidorEditar);
                    } else {
                        Swal.warning(ctx, "Cuidado!", "Llene todos los datos antes de continuar.", 2000);
                    }
                }
//                HORAS
                if (bindtwo.cbHoras.isChecked()) {
                    detalleActual.setHoras(horasEditar);
                }
//                RENDIMIENTOS
                if (bindtwo.cbRendimientos.isChecked()) {
                    detalleActual.setRdtos(rendimientosEditar);

                }

//                PROCESAMOS SOLO LA DATA QUE TIENE EL CHECK SELECCIONADO PARA PODER REALIZAR LA EDICIÓN DE LO NECESARIO
                try {
                    detalleActual.guardar(objSqlite, idUsuario);
                    sweetAlertDialog.dismissWithAnimation();
                    success = true;
                    message = "Se han editado correctamente los detalles.";
                } catch (Exception e) {
                    Log.e("ERROR", e.toString());
                    message = "Ha ocurrido un error al editar los detalles: " + e.toString();
                }
            }

            if (actionResult != null) {
                actionResult.onActionResult("result", sweetAlertDialog);
                dismissDialog.onDismissDialog(success, message);
            }
        });

        bindtwo.llyinputsActividad.setOnClickListener(view1 -> {
            PopUpBuscarEnLista d = new PopUpBuscarEnLista(ctx, arl_Actividades, bindtwo.txvddtActividad, bindtwo.lblddtActividadValue);
            d.show();
        });

        bindtwo.lblddtActividadValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    List<String> p = new ArrayList<>();
                    p.add("01"); //PENDIENTE: OBTENER EMPRESA DINAMICAMENTE;
                    p.add(bindtwo.txvddtActividad.getText().toString());
                    arl_Labores = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Labores"), p, "READ"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        bindtwo.llyinputsLabor.setOnClickListener(view1 -> {
            if (arl_Labores == null) {
                Funciones.notificar(ctx, "Primero debe de seleccionar una actividad.");
            } else {
                PopUpBuscarEnLista d = new PopUpBuscarEnLista(ctx, arl_Labores, bindtwo.txvddtLabor, bindtwo.lblddtLaborValue);
                d.show();
            }
        });

        bindtwo.llyinputsConsumidor.setOnClickListener(view1 -> {
            PopUpBuscarEnLista d = new PopUpBuscarEnLista(ctx, arl_Consumidores, bindtwo.txvddtConsumidor, bindtwo.lblddtConsumidorValue);
            d.show();
        });
    }

    // Método para configurar la vista
    public static void configureView(DialogDetalleTareoBinding binding, String accion, Tareo tareo, ArrayList<Integer> listaTrabajadores, Swal.ActionResult actionResult, SweetAlertDialog sweetAlertDialog, Swal.DismissDialog dismissDialog) {
        Context ctx = binding.getRoot().getContext();
        sharedPreferences = ctx.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
        objSqlite = new ConexionSqlite(ctx, DataGreenApp.DB_VERSION());
        switch (accion) {
            case "duplicar":
                inicializarDuplicado(ctx, binding, tareo, listaTrabajadores, sweetAlertDialog, actionResult, dismissDialog);
                accionFija = accion;
                break;
            case "editar":
                inicializarEdicion(ctx, binding, tareo, listaTrabajadores, sweetAlertDialog, actionResult, dismissDialog);
                accionFija = accion;
                break;
        }


    }

    private static void duplicateWorkers(Tareo tareo, ArrayList<Integer> listaTrabajadores, Context ctx, DialogDetalleTareoBinding bindtwo) throws JSONException {
        SQLiteDatabase database;
        database = SQLiteDatabase.openDatabase(ctx.getDatabasePath("DataGreenMovil.db").toString(), null, SQLiteDatabase.OPEN_READWRITE);


        String idUsuario = sharedPreferences.getString("ID_USUARIO_ACTUAL", "!ID_USUARIO_ACTUAL");
        Calendar calendar = Calendar.getInstance();
        for (int itemDuplicar : listaTrabajadores
        ) {
            // Obtener la fecha y hora actual
            // Formatear la fecha y hora actual
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String fechaHoraFormateada = sdf.format(calendar.getTime());
            String nombres = "";
            String dniDuplicar = tareo.getDetalle().get(itemDuplicar - 1).getDni();
            String observacionDuplicar = tareo.getDetalle().get(itemDuplicar - 1).getObservacion();
            String idPlanillaDuplicar = "";


            boolean SALIDA_AUTOMATICA = sharedPreferences.getBoolean("SALIDA_AUTOMATICA", false);
//            AGREGAR SALIDA
            if (SALIDA_AUTOMATICA) {
                Cursor cVerificarSinSalida;

                //                        MARCAR LA SALIDA Y HACER RECURSIVIDAD PARA LUEGO MARCAR SU ENTRADA:
                String[] selectionArgs = {tareo.getId(), dniDuplicar};
                cVerificarSinSalida = database.rawQuery("SELECT * FROM TRX_TAREOS_DETALLE WHERE Idtareo = ? AND Dni = ? AND salida = ''", selectionArgs);
                if (cVerificarSinSalida.getCount() > 0) {
                    cVerificarSinSalida.moveToFirst();
                    int itemIndex;
                    itemIndex = cVerificarSinSalida.getColumnIndex("Item");
                    String itemIndexU;
                    itemIndexU = cVerificarSinSalida.getString(itemIndex);
                    Optional<TareoDetalle> resultado = tareo.getDetalle().stream().filter(detalle -> detalle.getItem() == Integer.parseInt(itemIndexU)).findFirst();
                    Date date1 = null;
                    Date date2 = null;
                    try {
                        date1 = sdf.parse(resultado.get().getIngreso());
                        date2 = sdf.parse(fechaHoraFormateada);
                    } catch (ParseException e) {
                        Swal.error(ctx, "Error", "No se ha podido convertir la fecha", 2000);
                    }
                    // Calcular la diferencia en milisegundos
                    long diferenciaMilisegundos = date2.getTime() - date1.getTime();
                    // Convertir la diferencia de milisegundos a horas
                    resultado.get().setSalida(fechaHoraFormateada);
                    double horas = diferenciaMilisegundos / 3600000.00;
                    BigDecimal horasRedondeadas = new BigDecimal(horas).setScale(2, RoundingMode.HALF_UP);
                    resultado.get().setHoras(horasRedondeadas.doubleValue());
                } else {
                    Swal.warning(ctx, "Cuidado", "No se encuentra un tareo de ingreso de este trabajador, pruebe a guardar el tareo y volver a intentar.", 2000);
                }

                try {
                    tareo.guardarDetalle(objSqlite);
                } catch (Exception e) {
                    Swal.error(ctx, "Error", "Error al guardar el detalle", 5000);
                }

                try {
                    if (!tareo.getIdEstado().equals("TR")) {

                        if (tareo.guardar(objSqlite, null)) {
                            objSqlite.ActualizarDataPendiente(null);
                        }
                    } else {
                        Funciones.notificar(ctx, "El tareo no cuenta con el estado PENDIENTE, imposible actualizar.");
                    }
                } catch (Exception ex) {
                    Funciones.mostrarError(ctx, ex);
                }
            }
//            FIN AGREGAR SALIDA


            List<String> p = new ArrayList<>();
            p.add("01");
            p.add(dniDuplicar);

            try {
                nombres = ClaveValor.obtenerValorDesdeClave(dniDuplicar, ClaveValor.getArrayClaveValor(hmTablas.get("PERSONAS"), 0, 2));
                idPlanillaDuplicar = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER PLANILLA"), p, "READ", "");
            } catch (Exception e) {
                Log.e("ERROR", "ERROR AL OBTENER NOMBRES");
            }

            String laborDuplicar = bindtwo.txvddtLabor.getText().toString();
            String actividadDuplicar = bindtwo.txvddtActividad.getText().toString();
            String consumidorDuplicar = bindtwo.txvddtConsumidor.getText().toString();
            Double horasDuplicar = Double.valueOf(bindtwo.txvddtHoras.getText().toString());
            Double rendimientosDuplicar = Double.valueOf(bindtwo.txvddtRendimientos.getText().toString());
            TareoDetalle detalleActual = new TareoDetalle();
            detalleActual.setItem(tareo.getTotalDetalles() + 1);
            detalleActual.setIdEmpresa("01");
            detalleActual.setDni(dniDuplicar);
            detalleActual.setNombres(nombres);
            detalleActual.setIdPlanilla(idPlanillaDuplicar);
            detalleActual.setIdActividad(actividadDuplicar);
            detalleActual.setIdLabor(laborDuplicar);
            detalleActual.setIdConsumidor(consumidorDuplicar);
            detalleActual.setHoras(horasDuplicar);
            detalleActual.setIngreso(fechaHoraFormateada);
            detalleActual.setSalida("");
            detalleActual.setRdtos(rendimientosDuplicar);
            detalleActual.setIdTareo(tareo.getId());
            detalleActual.setObservacion(observacionDuplicar);

            try {
                detalleActual.guardar(objSqlite, idUsuario);
                tareo.agregarDetalle(detalleActual, ctx);
//                tareo.setTotalDetalles(tareo.getTotalDetalles() + 1);
                tareo.guardar(objSqlite, null);
            } catch (Exception e) {
                Log.e("ERORRDETALLE", e.toString());
            }

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogDetalleTareoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ctx = view.getContext();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Toast.makeText(context, "HOLI BOLI", Toast.LENGTH_SHORT).show();
    }
}
