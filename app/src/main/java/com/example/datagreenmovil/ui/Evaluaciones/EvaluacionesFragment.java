package com.example.datagreenmovil.ui.Evaluaciones;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.datagreenmovil.Conexiones.AppDatabase;
import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.DAO.Evaluaciones.MstAlas.MstAlas;
import com.example.datagreenmovil.DAO.Evaluaciones.MstAlas.MstAlasDAO;
import com.example.datagreenmovil.DataGreenApp;
import com.example.datagreenmovil.Helpers.SQLMapper;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.databinding.FragmentEvaluacionesBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EvaluacionesFragment extends Fragment {

    private FragmentEvaluacionesBinding binding;
    private Context context;
    private MstAlasDAO alasDAO;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        binding = FragmentEvaluacionesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        iniciarDaos();

        sincronizarData();

        configurarFechaPicker(binding.etFecha);
        configurarTimePicker(binding.etInicio);
        configurarTimePicker(binding.etFin);

        binding.btnSalir.setOnClickListener(v -> salirDelFragment());

        return root;
    }

    private void salirDelFragment() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }


    private void sincronizarData() {
        try {
            ConexionBD sql = new ConexionBD(context);
            ResultSet rsAlas = sql.doItBaby("EXEC Evolet..sp_obtener_alas", null);
//            ResultSet rsAlas = sql.doItBaby("SELECT * FROM EVOLET..mst_alas", null);

            List<MstAlas> alasLista = SQLMapper.mapResultSetToList(rsAlas, MstAlas.class);

            if (alasDAO != null) {
                alasDAO.sincronizarAlas(alasLista);
                Log.d("SQLSUCCESS", "Sincronización completada con " + alasLista.size() + " registros.");
            } else {
                Log.e("SQLERROR", "alasDAO es nulo. Asegúrate de inicializarlo en iniciarDaos().");
            }
        } catch (Exception e) {
            Log.e("SQLERROR", "Error en sincronizarData: " + e.toString());
        }
    }


    private void iniciarDaos() {
        AppDatabase database = DataGreenApp.getAppDatabase();
        alasDAO = database.mstAlasDAO();
    }

    private void configurarFechaPicker(TextInputEditText editText) {
        editText.setInputType(InputType.TYPE_NULL);
        editText.setFocusable(false);
        editText.setClickable(true);

        editText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                        editText.setText(selectedDate);
                    }, year, month, day);

            datePickerDialog.show();
        });
    }

    private void configurarTimePicker(TextInputEditText editText) {
        editText.setInputType(InputType.TYPE_NULL);
        editText.setFocusable(false);
        editText.setClickable(true);

        editText.setOnClickListener(v -> {
            Calendar calendario = Calendar.getInstance();
            int hora = calendario.get(Calendar.HOUR_OF_DAY);
            int minuto = calendario.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    editText.getContext(),
                    (view, selectedHour, selectedMinute) -> {
                        String period = selectedHour < 12 ? "AM" : "PM";
                        String formattedTime = String.format("%02d:%02d %s",
                                (selectedHour == 0 ? 12 : (selectedHour > 12 ? selectedHour - 12 : selectedHour)),
                                selectedMinute, period);
                        editText.setText(formattedTime);
                    }, hora, minuto, false);

            timePickerDialog.show();
        });
    }
}
