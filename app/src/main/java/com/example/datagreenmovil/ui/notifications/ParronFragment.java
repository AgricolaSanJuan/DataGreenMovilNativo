package com.example.datagreenmovil.ui.notifications;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.datagreenmovil.Conexiones.AppDatabase;
import com.example.datagreenmovil.DAO.Estandares.Adapters.ConsumidoresTallerAdapter;
import com.example.datagreenmovil.DAO.Estandares.Adapters.OperarioTallerAdapter;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.databinding.FragmentParronBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ParronFragment extends Fragment {

    private FragmentParronBinding binding;
    private Context mContext;

    private TextView tvFechaHoy;
    private Spinner spinnerConsumidores, spinnerCombustible, spinnerActividad, spinnerLabor;
    private EditText etHorometroInicial, etHorometroFinal, etHorasTrabajadas;

    private ConsumidoresTallerAdapter maquinariaAdapter;
    private OperarioTallerAdapter operarioAdapter;

    private AppDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentParronBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mContext = getContext();

        // Inicializar la base de datos
        database = AppDatabase.getInstance(mContext);

        // Vincular vistas
        tvFechaHoy = binding.et_fecha;
        spinnerConsumidores = binding.sp_consumidores;
        spinnerCombustible = binding.sp_combustible;
        spinnerActividad = binding.sp_actividad;
        spinnerLabor = binding.sp_labor;
        etHorometroInicial = binding.et_hora_inicial;
        etHorometroFinal = binding.et_hora_final;
        etHorasTrabajadas = binding.et_hora_trabajada;

        // Configurar la fecha de hoy
        configurarFechaHoy();

        // Configurar spinners
        configurarSpinners();


        return view;
    }

    private void configurarFechaHoy() {
        String fechaHoy = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        tvFechaHoy.setText(fechaHoy);
    }

    private void configurarSpinners() {
        // Datos de ejemplo para los spinners (puedes reemplazarlos con datos reales)
        String[] consumidores = {"Consumidor 1", "Consumidor 2", "Consumidor 3"};
        String[] combustibles = {"Diesel", "Gasolina", "Eléctrico"};
        String[] actividades = {"Actividad 1", "Actividad 2", "Actividad 3"};
        String[] labores = {"Labor 1", "Labor 2", "Labor 3"};

        ArrayAdapter<String> consumidoresAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, consumidores);
        consumidoresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerConsumidores.setAdapter(consumidoresAdapter);

        ArrayAdapter<String> combustiblesAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, combustibles);
        combustiblesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCombustible.setAdapter(combustiblesAdapter);

        ArrayAdapter<String> actividadesAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, actividades);
        actividadesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerActividad.setAdapter(actividadesAdapter);

        ArrayAdapter<String> laboresAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, labores);
        laboresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLabor.setAdapter(laboresAdapter);

        // Configurar eventos (si es necesario)
        spinnerConsumidores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Acciones al seleccionar un consumidor
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Manejo si no se selecciona nada
            }
        });
    }

    private void registrarParron() {
        // Obtener los datos de los campos
        String consumidorSeleccionado = (String) spinnerConsumidores.getSelectedItem();
        String combustibleSeleccionado = (String) spinnerCombustible.getSelectedItem();
        String actividadSeleccionada = (String) spinnerActividad.getSelectedItem();
        String laborSeleccionada = (String) spinnerLabor.getSelectedItem();

        String horometroInicial = etHorometroInicial.getText().toString();
        String horometroFinal = etHorometroFinal.getText().toString();
        String horasTrabajadas = etHorasTrabajadas.getText().toString();

        // Validar los campos
        if (horometroInicial.isEmpty() || horometroFinal.isEmpty() || horasTrabajadas.isEmpty()) {
            Toast.makeText(mContext, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Agregar lógica para guardar en la base de datos
        Toast.makeText(mContext, "Parrón registrado exitosamente.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
