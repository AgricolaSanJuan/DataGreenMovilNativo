package com.example.datagreenmovil.ui.notifications;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.datagreenmovil.Conexiones.AppDatabase;
import com.example.datagreenmovil.DAO.Estandares.ConsumidoresTaller.ConsumidoresTaller;
import com.example.datagreenmovil.DAO.Estandares.ConsumidoresTaller.ConsumidoresTallerDAO;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.databinding.FragmentParronBinding;
import com.example.datagreenmovil.databinding.FragmentParronBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ParronFragment extends Fragment {

    private FragmentParronBinding binding;
    private Context mContext;
    private Spinner spinnerConsumidores, spinnerCombustible, spinnerActividad, spinnerLabor;
    private EditText etHorometroInicial, etHorometroFinal, etHorasTrabajadas,etFechaParron;

    private AppDatabase database;
    private ConsumidoresTallerDAO consumidorDAO;

    private int horaInicial = -1, minutoInicial = 0;
    private int horaFinal = -1, minutoFinal = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentParronBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mContext = getContext();
        etFechaParron = binding.etFechaParron;
        configurarFechaHoy();


        // Inicializar base de datos
        database = AppDatabase.getDatabase();
        consumidorDAO = database.consumidoresTallerDAO();

        // Vincular vistas con el binding
        spinnerConsumidores = binding.spImplementos;
        spinnerCombustible = binding.spCombustible;
        spinnerActividad = binding.spActividad;
        spinnerLabor = binding.spLabor;
        etHorometroInicial = binding.etHoraInicial;
        etHorometroFinal = binding.etHoraFinal;
        etHorasTrabajadas = binding.etHoraTrabajada;
        // Bloquear la edición manual en los campos de hora
        etHorometroInicial.setFocusable(false);
        etHorometroInicial.setClickable(true);
        etHorometroInicial.setKeyListener(null);

        etHorometroFinal.setFocusable(false);
        etHorometroFinal.setClickable(true);
        etHorometroFinal.setKeyListener(null);

        // Hacer el campo de horas trabajadas no editable
        etHorasTrabajadas.setFocusable(false);
        etHorasTrabajadas.setClickable(false);

        // Configurar la fecha actual
        configurarFechaHoy();

        // Cargar datos en los Spinners
        List<ConsumidoresTaller> consumidoresTaller = consumidorDAO.obtenerConsumidoresTaller();
        cargarSpinnerCombustible(consumidoresTaller);
        cargarSpinnerImplementos(consumidoresTaller);

        //Cargar datos en los spinner de actividad y labor



        // Configurar TimePickers para Horómetro Inicial y Final
        configurarTimePickers();

        return view;
    }

    private void configurarFechaHoy() {
        String fechaHoy = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        etFechaParron.setText(fechaHoy);
    }


    private void cargarSpinnerCombustible(List<ConsumidoresTaller> consumidores) {
        Executors.newSingleThreadExecutor().execute(() -> {

            final List<String> nombresConsumidores = new ArrayList<>();

            List<ConsumidoresTaller> listaMaquinarias = consumidores.stream()
                    .filter(consumidor -> consumidor.getTipo().equals("COM"))
                    .collect(Collectors.toList());

            for (ConsumidoresTaller c : listaMaquinarias) {
                nombresConsumidores.add(c.getIdConsumidor());
            }

            getActivity().runOnUiThread(() -> {
                if (!nombresConsumidores.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, nombresConsumidores);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spCombustible.setAdapter(adapter);
                }
            });
        });
    }

    private void cargarSpinnerImplementos(List<ConsumidoresTaller> consumidores) {
        Executors.newSingleThreadExecutor().execute(() -> {

            final List<String> Implementos = new ArrayList<>();

            List<ConsumidoresTaller> listaMaquinarias = consumidores.stream()
                    .filter(consumidor -> consumidor.getTipo().equals("IMP"))
                    .collect(Collectors.toList());

            for (ConsumidoresTaller c : listaMaquinarias) {
                Implementos.add(c.getIdConsumidor());
            }

            getActivity().runOnUiThread(() -> {
                if (!Implementos.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, Implementos);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spImplementos.setAdapter(adapter);
                }
            });
        });
    }




    private void configurarTimePickers() {
        etHorometroInicial.setOnClickListener(v -> mostrarTimePicker(true));
        etHorometroFinal.setOnClickListener(v -> mostrarTimePicker(false));
    }

    private void mostrarTimePicker(boolean esInicial) {
        Calendar calendar = Calendar.getInstance();
        int horaActual = calendar.get(Calendar.HOUR_OF_DAY);
        int minutoActual = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, (view, hourOfDay, minute) -> {
            String amPm = (hourOfDay < 12) ? "AM" : "PM";
            int horaFormato12 = (hourOfDay == 0) ? 12 : (hourOfDay > 12 ? hourOfDay - 12 : hourOfDay);
            String horaSeleccionada = String.format(Locale.getDefault(), "%02d:%02d %s", horaFormato12, minute, amPm);

            if (esInicial) {
                horaInicial = hourOfDay;
                minutoInicial = minute;
                etHorometroInicial.setText(horaSeleccionada);
            } else {
                horaFinal = hourOfDay;
                minutoFinal = minute;
                etHorometroFinal.setText(horaSeleccionada);
            }

            calcularHorasTrabajadas();
        }, horaActual, minutoActual, false);

        timePickerDialog.show();
    }

    private void calcularHorasTrabajadas() {
        if (horaInicial != -1 && horaFinal != -1) {
            int minutosInicio = (horaInicial * 60) + minutoInicial;
            int minutosFin = (horaFinal * 60) + minutoFinal;

            int diferenciaMinutos = minutosFin - minutosInicio;
            if (diferenciaMinutos < 0) {
                Toast.makeText(mContext, "La hora final no puede ser menor a la inicial", Toast.LENGTH_SHORT).show();
                etHorasTrabajadas.setText("0:00");
                return;
            }

            int horas = diferenciaMinutos / 60;
            int minutos = diferenciaMinutos % 60;
            String resultado = String.format(Locale.getDefault(), "%d:%02d", horas, minutos);

            etHorasTrabajadas.setText(resultado);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
