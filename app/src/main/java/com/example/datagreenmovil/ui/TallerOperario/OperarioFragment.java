package com.example.datagreenmovil.ui.TallerOperario;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.datagreenmovil.Conexiones.AppDatabase;
import com.example.datagreenmovil.DAO.Estandares.ConsumidoresTaller.ConsumidoresTaller;
import com.example.datagreenmovil.DAO.Estandares.ConsumidoresTaller.ConsumidoresTallerDAO;
import com.example.datagreenmovil.DAO.Estandares.Operario.Operario;
import com.example.datagreenmovil.DAO.Estandares.Operario.OperarioDAO;
import com.example.datagreenmovil.Helpers.Sincronizacion;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.viewModel.SharedViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class OperarioFragment extends Fragment {
    private SharedViewModel sharedViewModel;
    private Context mContext;
    private EditText etTotalHoras, etObservaciones, etFecha_operario;
    private Spinner spinnerMaquinaria, spinnerOperario, spinnerTurno;
    private ArrayAdapter<String> maquinariaAdapter, operarioAdapter, turnoAdapter;
    ConsumidoresTallerDAO consumidorDAO;
    OperarioDAO operarioDAO;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operadores, container, false);


        // Inicializar ViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Inicializar vistas
        etTotalHoras = view.findViewById(R.id.etTotalHoras);
        etObservaciones = view.findViewById(R.id.etObservaciones);
        spinnerMaquinaria = view.findViewById(R.id.spinnerMaquinaria);
        spinnerOperario = view.findViewById(R.id.spinnerOperario);
        spinnerTurno = view.findViewById(R.id.spinnerTurno);
        etFecha_operario = view.findViewById(R.id.et_fecha_operadores);

        // Establecer la fecha actual en el EditText de fecha y bloquear edición
        String fechaActual = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        etFecha_operario.setText(fechaActual);
        etFecha_operario.setFocusable(false);
        etFecha_operario.setClickable(false);

        // Configurar Spinners con datos de prueba
        maquinariaAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, new String[]{"Seleccionar", "Tractor", "Excavadora"});
        operarioAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, new String[]{"Seleccionar", "Juan Pérez", "María López"});
        turnoAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, new String[]{"Mañana", "Tarde", "Noche"});

        maquinariaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        operarioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        turnoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMaquinaria.setAdapter(maquinariaAdapter);
        spinnerOperario.setAdapter(operarioAdapter);
        spinnerTurno.setAdapter(turnoAdapter);

//        Inicializamos los DAO
        inicializarDaos();
        // Restaurar valores guardados en SharedViewModel
        recuperarDatosGuardados();

        // Listeners para almacenar datos en el ViewModel
        setupListeners();

        sincronizarInformacion();

        List<ConsumidoresTaller> consumidores = consumidorDAO.obtenerConsumidoresTaller();
        List<Operario> operarios = operarioDAO.obtenerOperarios();

        cargarSpinnerMaquinaria(consumidores);
        cargarSpinnerOperario(operarios);

        return view;
    }

    private void inicializarDaos() {
        consumidorDAO = AppDatabase.getDatabase().consumidoresTallerDAO();
        operarioDAO = AppDatabase.getDatabase().operarioDAO();
    }

    private void sincronizarInformacion() {
        Sincronizacion sincronizacion = new Sincronizacion(mContext);

        sincronizacion.sincronizarOperarios();
        sincronizacion.sincronizarMaquinarias();
        sincronizacion.sincronizarImplementos();
        sincronizacion.sincronizarCombustibles();
    }

    private void cargarSpinnerMaquinaria(List<ConsumidoresTaller> consumidores) {
        Executors.newSingleThreadExecutor().execute(() -> {

            final List<String> nombresConsumidores = new ArrayList<>();

            List<ConsumidoresTaller> listaMaquinarias = consumidores.stream()
                    .filter(consumidor -> consumidor.getTipo().equals("MAQ"))
                    .collect(Collectors.toList());

            for (ConsumidoresTaller c : listaMaquinarias) {
                nombresConsumidores.add(c.getIdConsumidor());
            }

            getActivity().runOnUiThread(() -> {
                if (!nombresConsumidores.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, nombresConsumidores);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMaquinaria.setAdapter(adapter);
                }
            });
        });
    }

    private void cargarSpinnerOperario(List<Operario> operarios) {
        final List<String> nombresOperarios = new ArrayList<>();

        for (Operario c:
             operarios) {
            nombresOperarios.add(c.getNombreCompleto());
        }

        getActivity().runOnUiThread(() -> {
            if (!nombresOperarios.isEmpty()) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, nombresOperarios);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerOperario.setAdapter(adapter);
            }
        });
    }

    private void recuperarDatosGuardados() {
        if (sharedViewModel.getTotalHoras().getValue() != null) {
            etTotalHoras.setText(sharedViewModel.getTotalHoras().getValue());
        }
        if (sharedViewModel.getObservaciones().getValue() != null) {
            etObservaciones.setText(sharedViewModel.getObservaciones().getValue());
        }
        if (sharedViewModel.getMaquinariaSeleccionada().getValue() != null) {
            int pos = maquinariaAdapter.getPosition(sharedViewModel.getMaquinariaSeleccionada().getValue());
            spinnerMaquinaria.setSelection(pos);
        }
        if (sharedViewModel.getOperarioSeleccionado().getValue() != null) {
            int pos = operarioAdapter.getPosition(sharedViewModel.getOperarioSeleccionado().getValue());
            spinnerOperario.setSelection(pos);
        }
        if (sharedViewModel.getTurnoSeleccionado().getValue() != null) {
            int pos = turnoAdapter.getPosition(sharedViewModel.getTurnoSeleccionado().getValue());
            spinnerTurno.setSelection(pos);
        }
    }

    private void setupListeners() {
        etTotalHoras.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                String totalHoras = editable.toString().trim();
                if (!TextUtils.isEmpty(totalHoras) && !validarTotalHoras(totalHoras)) {
                    Toast.makeText(mContext, "Ingrese un valor válido para las horas.", Toast.LENGTH_SHORT).show();
                }
                sharedViewModel.setTotalHoras(totalHoras);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        etObservaciones.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                sharedViewModel.setObservaciones(editable.toString().trim());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        spinnerMaquinaria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sharedViewModel.setMaquinariaSeleccionada(maquinariaAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerOperario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sharedViewModel.setOperarioSeleccionado(operarioAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerTurno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sharedViewModel.setTurnoSeleccionado(turnoAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private boolean validarTotalHoras(String totalHoras) {
        try {
            int horas = Integer.parseInt(totalHoras);
            return horas >= 0 && horas <= 24;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
