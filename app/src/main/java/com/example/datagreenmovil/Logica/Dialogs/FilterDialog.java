package com.example.datagreenmovil.Logica.Dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.LocaleData;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.DataGreenApp;
import com.example.datagreenmovil.Entidades.PopUpAnio;
import com.example.datagreenmovil.Entidades.PopUpCalendario;
import com.example.datagreenmovil.Entidades.PopUpSemana;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.databinding.DialogFiltrosBinding;

import org.json.JSONException;
import org.json.JSONObject;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.MonthDay;
import org.threeten.bp.Year;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.WeekFields;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FilterDialog extends DialogFragment {

    private static DialogFiltrosBinding binding;
    static ConexionSqlite objSqlite;
    Context ctx;
    static SharedPreferences sharedPreferences;
    static boolean vistaEstados, vistaFechasPeriodo, vistaFechasRango;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogFiltrosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ctx = view.getContext();
    }
    public static void configureView(DialogFiltrosBinding bindingReceptor,
                                     boolean vEstados,
                                     boolean vFechasPeriodo,
                                     boolean vFechasRango,
                                     JSONObject props,
                                     Swal.FilterOpenDialog filterOpenDialog,
                                     Swal.FilterCloseDialog filterCloseDialog,
                                     Swal.FilterDialogResult filterDialogResult,
                                     SweetAlertDialog sweetAlertDialog) throws JSONException {
        Context ctx = bindingReceptor.getRoot().getContext();
        sharedPreferences = ctx.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
        vistaEstados = vEstados;
        vistaFechasPeriodo = vFechasPeriodo;
        vistaFechasRango = vFechasRango;
        objSqlite = new ConexionSqlite(ctx, DataGreenApp.DB_VERSION());
        binding = bindingReceptor;
        //MANEJAMOS LA VISTA
        if (props.getInt("radioButtonGroupIndexSelected") != -1) {
            RadioButton radioButtonToSelect = (RadioButton) binding.rbgEstados.getChildAt(props.getInt("radioButtonGroupIndexSelected"));
            radioButtonToSelect.setChecked(true);
        }else {
            RadioButton radioButtonToSelect = (RadioButton) binding.rbgEstados.getChildAt(0);
            radioButtonToSelect.setChecked(true);
        }
        // Define el formato deseado
        int anioActual = Year.now().getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate actualDate = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        int numeroSemana = LocalDate.now().get(weekFields.weekOfWeekBasedYear());

        binding.txvFechaAnio.setText(!props.getString("filtroAnio").equals("") ? props.getString("filtroAnio") : String.valueOf(anioActual));
        binding.txvFechaSemana.setText(!props.getString("filtroSemana").equals("") ? props.getString("filtroSemana") : String.valueOf(numeroSemana));
        binding.txvFechaDesde.setText(!props.getString("filtroDesde").equals("") ? props.getString("filtroDesde") : String.valueOf(actualDate.minusDays(7).format(formatter)));
        binding.txvFechaHasta.setText(!props.getString("filtroHasta").equals("") ? props.getString("filtroHasta") : String.valueOf(actualDate.format(formatter)));

        configureLayouts();
        // CONFIGURAMOS LOS LISTENERS
        binding.fabOk.setOnClickListener(view ->{
            Swal.DialogResult dialogResult = new Swal.DialogResult();
//            OBTENEMOS EL ID DEL RadioButton SELECCIONADO PARA DEFINIR EL ESTADO
            int radioButtonIdSelected = binding.rbgEstados.getCheckedRadioButtonId();
            int radioButtonIndexSelected = 0;
            String estadoSeleccionado = "";
//            SETEAMOS EL VALOR EN TODOS SI NO SE HA CAMBIADO ANTES
            if(radioButtonIdSelected != -1){
                RadioButton selectedRadioButton = binding.getRoot().findViewById(radioButtonIdSelected);
                estadoSeleccionado = selectedRadioButton.getText().toString();
                radioButtonIndexSelected = bindingReceptor.rbgEstados.indexOfChild(selectedRadioButton);
            }
//            DEFINIMOS LOS PARÁMETROS
            dialogResult.setEstado(estadoSeleccionado);
            dialogResult.setIdEstado(radioButtonIndexSelected);
            dialogResult.setAnio(binding.txvFechaAnio.getText().toString());
            dialogResult.setSemana(binding.txvFechaSemana.getText().toString());
            dialogResult.setDesde(binding.txvFechaDesde.getText().toString());
            dialogResult.setHasta(binding.txvFechaHasta.getText().toString());
            dialogResult.setMensaje(obtenerMensaje());
            filterDialogResult.onFilterDialogResult(dialogResult, sweetAlertDialog);
        });
        binding.txvFechaDesde.setOnClickListener(view -> {
            PopUpCalendario d = new PopUpCalendario(ctx, binding.txvFechaDesde);
            d.show();
        });
        binding.txvFechaHasta.setOnClickListener(view -> {
            PopUpCalendario d = new PopUpCalendario(ctx, binding.txvFechaHasta);
            d.show();
        });
        binding.txvFechaAnio.setOnClickListener(view -> {
            PopUpAnio popUpAnio = new PopUpAnio(ctx, binding.txvFechaAnio);
            popUpAnio.show();
        });
        binding.txvFechaSemana.setOnClickListener(view -> {
            PopUpSemana popUpSemana = new PopUpSemana(ctx, binding.txvFechaSemana);
            popUpSemana.show();
        });
    }
    private static String obtenerMensaje(){
        String mensaje = "Mostrando";
        //            OBTENEMOS EL ID DEL RadioButton SELECCIONADO PARA DEFINIR EL ESTADO
        int radioButtonIdSelected = binding.rbgEstados.getCheckedRadioButtonId();
        String estadoSeleccionado = "";
        if(radioButtonIdSelected != -1){
            RadioButton selectedRadioButton = binding.getRoot().findViewById(radioButtonIdSelected);
            estadoSeleccionado = selectedRadioButton.getText().toString().toLowerCase();
        }
//        SETEAMOS EL ESTADO EN EL MENSAJE
        mensaje += estadoSeleccionado.equals("todos") ? " todos los estandares" : " los estandares pendientes";
//        SETEAMOS EL PERIODO SI FUERA EL CASO
        if(vistaFechasPeriodo){
            mensaje += " en el periodo " + binding.txvFechaAnio.getText().toString() + binding.txvFechaSemana.getText().toString();
        }

        if(vistaFechasRango){
            mensaje += " desde: " + binding.txvFechaDesde.getText().toString() + " hasta: " + binding.txvFechaHasta.getText().toString();
        }

        return mensaje + ".";
    }
    private static void configureLayouts(){
        binding.lyFechasPeriodo.setVisibility(vistaEstados ? View.VISIBLE : View.INVISIBLE);
        binding.lyFechasPeriodo.setVisibility(vistaFechasPeriodo ? View.VISIBLE : View.INVISIBLE);
        binding.lyFechasPeriodo.setVisibility(vistaFechasRango ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}
