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
import com.example.datagreenmovil.Entidades.ClaveValor;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.PopUpBuscarEnLista;
import com.example.datagreenmovil.Entidades.PopUpBuscarEnLista_Item;
import com.example.datagreenmovil.Entidades.Tabla;
import com.example.datagreenmovil.Entidades.Tareo;
import com.example.datagreenmovil.Entidades.TareoDetalle;
import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.databinding.DialogDetalleTareoBinding;

import org.json.JSONException;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DialogDetalleTareo extends DialogFragment {

    private static DialogDetalleTareoBinding binding;
    static ConexionSqlite objSqlite;
    ConfiguracionLocal objConfLocal;
    static HashMap<String, Tabla> hmTablas = new HashMap<>();
    static ArrayList<PopUpBuscarEnLista_Item> arl_Turnos;
    static ArrayList<PopUpBuscarEnLista_Item> arl_Actividades;
    static ArrayList<PopUpBuscarEnLista_Item> arl_Labores = null;
    static ArrayList<PopUpBuscarEnLista_Item> arl_Consumidores;
    private static SharedPreferences sharedPreferences;
    Context ctx;

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
    public static void inicializarDuplicado(Context ctx, DialogDetalleTareoBinding bindtwo, Tareo tareo, ArrayList<Integer> listaTrabajadores, SweetAlertDialog sweetAlertDialog, Swal.ActionResult actionResult, Swal.DismissDialog dismissDialog){
        try {
            obtenerDataParaControles(ctx);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        bindtwo.fabConfirm.setOnClickListener(view -> {
            boolean success = false;
            String message = "";
            try {
                duplicateWorkers(tareo, listaTrabajadores, ctx, bindtwo);
                sweetAlertDialog.dismissWithAnimation();
                success = true;
                message = "Se han duplicado los detalles correctamente.";
            } catch (JSONException e) {
                Log.e("ERROR", e.toString());
                message = "Ha ocurrido un error al duplicar los detalles: "+e.toString();
            }
            String result = bindtwo.txvddtActividad.getText().toString(); // Lógica para obtener el resultado a duplicar
            if (actionResult != null) {
                actionResult.onActionResult(result, sweetAlertDialog);
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
                    arl_Labores = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Labores"),p,"READ"));
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
//        OCULTAMOS LOS VALORES QUE NO DEBERÍAMOS TENER EN CUENTA
//        bindtwo.lblddtActividadValue.setVisibility(View.GONE);
        bindtwo.llyPrincipalActividad.setVisibility(View.GONE);
//        bindtwo.llyddtActividad.setVisibility(View.GONE);
        bindtwo.llyPrincipalLabor.setVisibility(View.GONE);
//        bindtwo.llyddtLabor.setVisibility(View.GONE);
        bindtwo.llyPrincipalConsumidor.setVisibility(View.GONE);
//        bindtwo.llyddtConsumidor.setVisibility(View.GONE);

        bindtwo.fabConfirm.setOnClickListener(view -> {
            boolean success = false;
            String message = "";
            String idUsuario = sharedPreferences.getString("ID_USUARIO_ACTUAL","!ID_USUARIO_ACTUAL");
            Double horasEditar = Double.valueOf(bindtwo.txvddtHoras.getText().toString());
            Double rendimientosEditar = Double.valueOf(bindtwo.txvddtRendimientos.getText().toString());
            for (int itemEditar: listaTrabajadores){
                TareoDetalle detalleActual = new TareoDetalle();
                detalleActual = tareo.getDetalle().get(itemEditar - 1);
                detalleActual.setHoras(horasEditar);
                detalleActual.setRdtos(rendimientosEditar);
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
    }
    // Método para configurar la vista
    public static void configureView(DialogDetalleTareoBinding binding, String accion, Tareo tareo , ArrayList<Integer> listaTrabajadores, Swal.ActionResult actionResult, SweetAlertDialog sweetAlertDialog, Swal.DismissDialog dismissDialog) {
        Context ctx = binding.getRoot().getContext();
        sharedPreferences = ctx.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
        objSqlite = new ConexionSqlite(ctx,  null);
        switch (accion){
            case "duplicar":
                inicializarDuplicado(ctx, binding, tareo, listaTrabajadores, sweetAlertDialog, actionResult, dismissDialog);
                break;
            case "editar":
                inicializarEdicion(ctx, binding, tareo, listaTrabajadores, sweetAlertDialog, actionResult, dismissDialog);
                break;
        }


    }

    private static void duplicateWorkers(Tareo tareo, ArrayList<Integer> listaTrabajadores, Context ctx, DialogDetalleTareoBinding bindtwo) throws JSONException {
        String idUsuario = sharedPreferences.getString("ID_USUARIO_ACTUAL","!ID_USUARIO_ACTUAL");
        Calendar calendar = Calendar.getInstance();
        for (int itemDuplicar: listaTrabajadores
             ) {
            // Obtener la fecha y hora actual
            // Formatear la fecha y hora actual
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String fechaHoraFormateada = sdf.format(calendar.getTime());
            String nombres = "";
            String dniDuplicar = tareo.getDetalle().get(itemDuplicar - 1).getDni();
            String observacionDuplicar = tareo.getDetalle().get(itemDuplicar - 1).getObservacion();
            String idPlanillaDuplicar = "";


            List<String> p = new ArrayList<>();
            p.add("01");
            p.add(dniDuplicar);

            try {
                nombres= ClaveValor.obtenerValorDesdeClave(dniDuplicar ,ClaveValor.getArrayClaveValor(hmTablas.get("PERSONAS"),  0, 2));
                idPlanillaDuplicar = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER PLANILLA"), p, "READ", "");
            } catch (Exception e) {
                Log.e("ERROR","ERROR AL OBTENER NOMBRES");
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

//            tareo.agregarDetalle(detalleActual, ctx);
//
//            Log.i("DNIADUPLICAR", dniDuplicar);
//
//            Toast.makeText(ctx, dniDuplicar, Toast.LENGTH_SHORT).show();
//                                                                                  cambia p
//            String query = "INSERT INTO trx_Tareos_Detalle VALUES('01', '"+tareo.getId()+"', '30', '"+dniDuplicar+"', ')";
//            Log.i("querysql", query);
        }
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
