package com.example.datagreenmovil.Utilidades;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.databinding.UtilidadesFiltrosBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class Filtros extends Fragment{
    public static GetFilterData getFilterData;
    UtilidadesFiltrosBinding binding;
    private Context ctx;
    static ConexionSqlite objSqlite;
    static SharedPreferences sharedPreferences;
//    static boolean vistaEstados, vistaFechasPeriodo, vistaFechasRango;
    static String filtroEstado = "", filtroAnio = "", filtroSemana = "", filtroDesde = "", filtroHasta = "";
    int radioButtonGroupIndexSelected = -1;

    public interface GetFilterData {
        void onChangeFilterData(Swal.DialogResult filterData);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = UtilidadesFiltrosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inicializarDescripcionFiltro();
        binding.lyFiltros.setOnClickListener(view -> {
//            Swal.info(ctx, "Hola", "Aqui se abrirán los filtros pe mafrencito", 5000);
            JSONObject props = new JSONObject();
            try {
                props.put("radioButtonGroupIndexSelected", radioButtonGroupIndexSelected);
                props.put("filtroEstado", filtroEstado);
                props.put("filtroAnio", filtroAnio);
                props.put("filtroSemana", filtroSemana);
                props.put("filtroDesde", filtroDesde);
                props.put("filtroHasta", filtroHasta);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            try {
                Swal.filterDialog(ctx,
                        true,
                        true,
                        true,
                        props,
                        (filterOpenDialogParams, sweetAlertDialog) -> {

                        },
                        (filterCloseDialogParams, sweetAlertDialog) -> {
                        },
                        (dialogResult, sweetAlertDialog) ->{
                            sweetAlertDialog.dismissWithAnimation();
                            filtroEstado = dialogResult.getEstado();
                            filtroAnio = dialogResult.getAnio();
                            filtroSemana = dialogResult.getSemana();
                            filtroDesde = dialogResult.getDesde();
                            filtroHasta = dialogResult.getHasta();
                            radioButtonGroupIndexSelected = dialogResult.getIdEstado();
                            binding.lblDescripcionFiltro.setText(dialogResult.getMensaje());
                            if(getFilterData != null){
                                getFilterData.onChangeFilterData(dialogResult);
                            }
                        }
                );
            } catch (JSONException e) {
                Swal.error(ctx, "Oops!", e.toString(), 2000);
            }
        });
        return root;
    }
    public void setFilterDataCallback(GetFilterData callback) {
        getFilterData = callback;
    }
    private void inicializarDescripcionFiltro() {
        //                        Swal.info(ctx, "Resultado", dialogResult.getMensaje(), 8000);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate actualDate = LocalDate.now();

        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int numeroSemana = actualDate.get(weekFields.weekOfWeekBasedYear());

        filtroAnio = String.valueOf(actualDate.getYear());
        filtroSemana = String.valueOf(numeroSemana);
        filtroDesde = String.valueOf(actualDate.minusDays(7).format(formatter));
        filtroHasta = String.valueOf(actualDate.format(formatter));
        radioButtonGroupIndexSelected = 0;
        binding.lblDescripcionFiltro.setText("Mostrando todos los estandares en el periodo "+actualDate.getYear()+numeroSemana + " desde el "+actualDate.minusDays(7).format(formatter)+" al "+actualDate.format(formatter));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("aea", "mongol");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        ctx = context;
        super.onAttach(context);
    }
}
