package com.example.datagreenmovil.ui.TareosMain.Dialogs;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle.TareoDetalles;
import com.example.datagreenmovil.DataGreenApp;
import com.example.datagreenmovil.Entidades.PopUpBuscarEnLista;
import com.example.datagreenmovil.Entidades.PopUpBuscarEnLista_Item;
import com.example.datagreenmovil.Entidades.Tabla;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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

    public static void inicializarDuplicado(Context ctx, DialogDetalleTareoBinding bindtwo, List<TareoDetalles> listaDetalles, ArrayList<Integer> listaTrabajadores, SweetAlertDialog sweetAlertDialog, /*Swal.ActionResult actionResult, */Swal.DismissDialog dismissDialog) {
        try {
            obtenerDataParaControles(ctx);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        bindtwo.cbActividad.setVisibility(View.GONE);
        bindtwo.cbLabor.setVisibility(View.GONE);
        bindtwo.cbRendimientos.setVisibility(View.GONE);
        bindtwo.cbHoras.setVisibility(View.GONE);
        bindtwo.cbConsumidor.setVisibility(View.GONE);

        bindtwo.txtInfo.setText("Elija los nuevos datos para duplicar a las personas seleccionadas");

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
                    duplicateWorkers(listaDetalles, listaTrabajadores, ctx, bindtwo);
                    success = true;
                    message = "Se han duplicado los detalles correctamente.";
                } catch (JSONException e) {
                    Log.e("ERROR", e.toString());
                    message = "Ha ocurrido un error al duplicar los detalles: " + e.toString();
                }
                String result = bindtwo.txvddtActividad.getText().toString(); // Lógica para obtener el resultado a duplicar
                if (message != null) {
//                    actionResult.onActionResult(result, sweetAlertDialog);
                    dismissDialog.onDismissDialog(success, message, sweetAlertDialog);
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

    private static List<TareoDetalles> inicializarEdicion(Context ctx, DialogDetalleTareoBinding bindtwo, List<TareoDetalles> listaDetalles, ArrayList<Integer> listaTrabajadores, SweetAlertDialog sweetAlertDialog, /*Swal.ActionResult actionResult,*/ Swal.DismissDialog dismissDialog) {
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

//        if (hideHours) {
//            bindtwo.llyPrincipalHoras.setVisibility(View.GONE);
//        } else {
//            bindtwo.llyPrincipalHoras.setVisibility(View.VISIBLE);
//        }

//        bindtwo.llyddtActividad.setVisibility(View.GONE);
//        bindtwo.llyPrincipalLabor.setVisibility(View.GONE);
//        bindtwo.llyddtLabor.setVisibility(View.GONE);
//        bindtwo.llyPrincipalConsumidor.setVisibility(View.GONE);
//        bindtwo.llyddtConsumidor.setVisibility(View.GONE);

        //                ACTIVIDAD
        bindtwo.cbActividad.setOnCheckedChangeListener((buttonView, isChecked) -> bindtwo.cbLabor.setChecked(isChecked));
        bindtwo.cbLabor.setOnCheckedChangeListener((buttonView, isChecked) -> bindtwo.cbActividad.setChecked(isChecked));

        bindtwo.fabConfirm.setOnClickListener(view -> {
            AtomicBoolean success = new AtomicBoolean(false);
            AtomicReference<String> message = new AtomicReference<>("");
            String idUsuario = sharedPreferences.getString("ID_USUARIO_ACTUAL", "!ID_USUARIO_ACTUAL");
            Double horasEditar = Double.valueOf(!bindtwo.txvddtHoras.getText().toString().isEmpty() ? bindtwo.txvddtHoras.getText().toString() : "0.00");
            String idActividadEditar = bindtwo.txvddtActividad.getText().toString();
            String actividadEditar = bindtwo.lblddtActividad.getText().toString();
            String idLaborEditar = bindtwo.txvddtLabor.getText().toString();
            String laborEditar = bindtwo.lblddtLabor.getText().toString();
            String idConsumidorEditar = bindtwo.txvddtConsumidor.getText().toString();
            String consumidorEditar = bindtwo.lblddtConsumidor.getText().toString();
            Double rendimientosEditar = Double.valueOf(!bindtwo.txvddtRendimientos.getText().toString().isEmpty() ? bindtwo.txvddtRendimientos.getText().toString() : "0.00");
//            NUEVO MÉTODO
            try {
                listaDetalles.stream()
                        .filter(detalle -> listaTrabajadores.contains(detalle.getItem()))
                        .forEach(detalleActual -> {
                            //                PROCESAMOS SOLO LA DATA QUE TIENE EL CHECK SELECCIONADO PARA PODER REALIZAR LA EDICIÓN DE LO NECESARIO
//                ACTIVIDAD -- LABOR
                            if (bindtwo.cbActividad.isChecked()) {
                                if (!bindtwo.txvddtActividad.getText().toString().equals(".") || !bindtwo.txvddtLabor.getText().toString().equals(".")) {
                                    detalleActual.setIdActividad(idActividadEditar);
                                    detalleActual.setActividad(actividadEditar);
                                    detalleActual.setIdLabor(idLaborEditar);
                                    detalleActual.setLabor(laborEditar);
                                }
                            }
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
                                detalleActual.setSubTotalHoras(horasEditar);
                            }
//                RENDIMIENTOS
                            if (bindtwo.cbRendimientos.isChecked()) {
                                detalleActual.setSubTotalRendimiento(rendimientosEditar);

                            }
                            success.set(true);
                            message.set("Se han editado correctamente los detalles.");
                        });
//            NUEVO MÉTODO
//            for (int itemEditar : listaTrabajadores) {
//                TareoDetalles detalleActual = listaDetalles.get(itemEditar - 1);
//
////                PROCESAMOS SOLO LA DATA QUE TIENE EL CHECK SELECCIONADO PARA PODER REALIZAR LA EDICIÓN DE LO NECESARIO
////                try {
////                    detalleActual.guardar(objSqlite, idUsuario);
//////                    sweetAlertDialog.dismissWithAnimation();
////                    success = true;
////                    message = "Se han editado correctamente los detalles.";
////                } catch (Exception e) {
////                    Log.e("ERROR", e.toString());
////                    message = "Ha ocurrido un error al editar los detalles: " + e.toString();
////                }
//            }
            } catch (Exception e) {
                message.set("Hubo un error");
            }

            if (message.get() != null) {
//                actionResult.onActionResult("result", sweetAlertDialog);
                dismissDialog.onDismissDialog(success.get(), message.get(), sweetAlertDialog);
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

        return listaDetalles;
    }

    // Método para configurar la vista
    public static void configureView(DialogDetalleTareoBinding binding, String accion, List<TareoDetalles> listaDetalles, ArrayList<Integer> listaTrabajadores, /*Swal.ActionResult actionResult,*/ SweetAlertDialog sweetAlertDialog, Swal.DismissDialog dismissDialog) {
        Context ctx = binding.getRoot().getContext();
        sharedPreferences = ctx.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
        objSqlite = new ConexionSqlite(ctx, DataGreenApp.DB_VERSION());
        switch (accion) {
            case "duplicar":
                inicializarDuplicado(ctx, binding, listaDetalles, listaTrabajadores, sweetAlertDialog, /*actionResult, */dismissDialog);
                accionFija = accion;
                break;
            case "editar":
                inicializarEdicion(ctx, binding, listaDetalles, listaTrabajadores, sweetAlertDialog,/* actionResult, */dismissDialog);
                accionFija = accion;
                break;
        }


    }

    private static List<TareoDetalles> duplicateWorkers(
            List<TareoDetalles> listaDetalles,
            ArrayList<Integer> listaTrabajadores,
            Context ctx,
            DialogDetalleTareoBinding bindtwo
    ) throws JSONException {

        // NUEVO MÉTODO
        TareoDetalles ultimaEntidad = listaDetalles.get(listaDetalles.size() - 1);
        AtomicInteger nuevoItem = new AtomicInteger(ultimaEntidad.getItem() + 1);
        String fechaHoraFormateada = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(Calendar.getInstance().getTime());
        boolean SALIDA_AUTOMATICA = sharedPreferences.getBoolean("SALIDA_AUTOMATICA", false);

        List<TareoDetalles> nuevosDetalles = new ArrayList<>();

        listaDetalles.stream()
                .filter(detalle -> listaTrabajadores.contains(detalle.getItem()))
                .forEach(detalleActual -> {
                    try {
                        // Crear un nuevo detalle duplicado
                        TareoDetalles nuevoDetalle = new TareoDetalles(detalleActual);

                        // Asignar nuevos valores
                        nuevoDetalle.setItem(nuevoItem.getAndIncrement());
                        nuevoDetalle.setIdActividad(bindtwo.txvddtActividad.getText().toString());
                        nuevoDetalle.setIdLabor(bindtwo.txvddtLabor.getText().toString());
                        nuevoDetalle.setIngreso(fechaHoraFormateada);
                        nuevoDetalle.setIdConsumidor(bindtwo.txvddtConsumidor.getText().toString());
                        nuevoDetalle.setSalida("");
                        nuevoDetalle.setSubTotalHoras(
                                Double.valueOf(bindtwo.txvddtHoras.getText().toString())
                        );
                        nuevoDetalle.setSubTotalRendimiento(
                                Double.valueOf(bindtwo.txvddtRendimientos.getText().toString())
                        );

                        // Manejo de salida automática
                        if (SALIDA_AUTOMATICA) {
                            listaDetalles.stream()
                                    .filter(t -> detalleActual.getDni().equals(t.getDni()) &&
                                            (t.getSalida() == null || t.getSalida().isEmpty()))
                                    .findFirst()
                                    .ifPresent(itemActual -> {
                                        try {
                                            Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                                    .parse(itemActual.getIngreso());
                                            Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                                    .parse(fechaHoraFormateada);

                                            // Calcular la diferencia en horas
                                            long diferenciaMilisegundos = date2.getTime() - date1.getTime();
                                            double horas = diferenciaMilisegundos / 3600000.00;
                                            BigDecimal horasRedondeadas = new BigDecimal(horas)
                                                    .setScale(2, RoundingMode.HALF_UP);

                                            itemActual.setSalida(fechaHoraFormateada);
                                            itemActual.setSubTotalHoras(horasRedondeadas.doubleValue());
                                        } catch (ParseException e) {
                                            Swal.error(ctx, "Error", "No se ha podido convertir la fecha", 2000);
                                        }
                                    });
                        }

                        // Agregar a la lista temporal
                        nuevosDetalles.add(nuevoDetalle);

                    } catch (NumberFormatException e) {
                        Swal.error(ctx, "Error", "Formato inválido en los valores numéricos", 2000);
                    }
                });

        // Agregar nuevos detalles a la lista principal
        listaDetalles.addAll(nuevosDetalles);

        return listaDetalles;
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
