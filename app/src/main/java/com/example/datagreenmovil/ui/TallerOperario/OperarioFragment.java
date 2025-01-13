package com.example.datagreenmovil.ui.TallerOperario;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.datagreenmovil.Conexiones.AppDatabase;
import com.example.datagreenmovil.DAO.Estandares.Adapters.ConsumidoresTallerAdapter;
import com.example.datagreenmovil.DAO.Estandares.Adapters.OperarioTallerAdapter;
import com.example.datagreenmovil.DAO.Estandares.ConsumidoresTaller.ConsumidoresTaller;
import com.example.datagreenmovil.DAO.Estandares.TrxDParteMaquinaria.TrxDParteMaquinaria;
import com.example.datagreenmovil.DAO.Estandares.TrxParteMaquinaria.TrxParteMaquinaria;
import com.example.datagreenmovil.Helpers.Sincronizacion;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.NewTallerActivity;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.databinding.FragmentOperadoresBinding;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OperarioFragment extends Fragment {

    private FragmentOperadoresBinding binding;
    private Context mContext;

    private EditText etSucursal, etTotalHoras, etObservaciones;
    private Spinner spinnerMaquinaria, spinnerOperario, spinnerTurno;
    private TextView tvFechaHoy;

    private ConsumidoresTallerAdapter maquinariaAdapter;
    private OperarioTallerAdapter operarioAdapter;
    private ArrayAdapter<String> turnoAdapter;

    // DAO
    private AppDatabase database;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Operario homeViewModel = new ViewModelProvider(this).get(Operario.class);

        binding = FragmentOperadoresBinding.inflate(inflater, container, false);
//        DEFINIMOS EL VALOR DE database PARA PODER OPERAR CON LOS DAO
        database = AppDatabase.getDatabase();
        View root = binding.getRoot();

        // Inicializacion de las vistas

        etSucursal = binding.etSucursal;
        etTotalHoras = binding.etTotalHoras;
        etObservaciones = binding.etObservaciones;
        spinnerMaquinaria = binding.spinnerMaquinaria;
        spinnerOperario = binding.spinnerOperario;
        spinnerTurno = binding.spinnerTurno;
        tvFechaHoy = binding.tvFechaHoy;

        binding.spinnerMaquinaria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("SELECCIONADO", String.valueOf(i));
//                TextView idSelected = view.findViewById(R.id.txtIdConsumidor);
//                Swal.info(mContext, "SELECCIONADO", idSelected.getText().toString(), 5000);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // Fecha actual

        String fechaHoy = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        tvFechaHoy.setText(fechaHoy);

        // Sucursal 002 por defecto

        etSucursal.setText("002");
        etSucursal.setEnabled(false);

        // Sincronizamos los datos de la BD
        Sincronizacion sincronizacion = new Sincronizacion(mContext);
        sincronizacion.sincronizarMaquinarias();
        sincronizacion.sincronizarOperarios();
        sincronizacion.sincronizarImplementos();
        sincronizacion.sincronizarCombustibles();

        // Cargamos los datos de la BD para los spinners
        cargarDatosBD();

        // Observamos cambios en los campos para registrar o procesar la información
        setupTextWatcher();

        return root;
    }

    // Cargar los datos de la base de datos para los spinners
    private void cargarDatosBD() {
        // Aquí, simula la carga de datos de la base de datos (se puede usar Retrofit, Room, etc.)
        List<ConsumidoresTaller> maquinarias = database.consumidoresTallerDAO().obtenerMaquinarias();
        List<ConsumidoresTaller> implementos = database.consumidoresTallerDAO().obtenerImplementos();
        List<ConsumidoresTaller> combustibles = database.consumidoresTallerDAO().obtenerCombustibles();
//        List<String> maquinaria = obtenerDatosDeBD("maquinaria");
        List<String> operarios = obtenerDatosDeBD("operario");
        List<String> turnos = obtenerDatosDeBD("turno");

        // Crear adaptadores para los spinners
        maquinariaAdapter = new ConsumidoresTallerAdapter(maquinarias);
        operarioAdapter = new OperarioTallerAdapter();
        turnoAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, turnos);

        // Establecer el estilo de los elementos del spinner
//        maquinariaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        operarioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        turnoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Establecer los adaptadores a los spinners
        spinnerMaquinaria.setAdapter(maquinariaAdapter);
        spinnerOperario.setAdapter(operarioAdapter);
        spinnerTurno.setAdapter(turnoAdapter);

        TrxParteMaquinaria parteMaquinariaActual = new TrxParteMaquinaria();

        NewTallerActivity activity = (NewTallerActivity) getActivity();

        if(activity != null){
            parteMaquinariaActual = activity.maquinaria;
        }

        parteMaquinariaActual.setIdMaquinaria("DG_0000210");
        parteMaquinariaActual.setIdOperario("088781");
        parteMaquinariaActual.setFecha("2025-01-08");
        parteMaquinariaActual.setIdSucursal("002");
        parteMaquinariaActual.setIdEmpresa("001");
        parteMaquinariaActual.setIdTurno("1");
        parteMaquinariaActual.setIdMaquinaria("M072");

    }

    // Obteniendo datos de la bd
    private List<String> obtenerDatosDeBD(String tipo) {

        List<String> datos = new ArrayList<>();
        switch (tipo) {
            case "maquinaria":
                datos.add("Maquinaria 1");
                datos.add("Maquinaria 2");
                break;
            case "operario":
                datos.add("Operario A");
                datos.add("Operario B");
                break;
            case "turno":
                datos.add("Mañana");
                datos.add("Tarde");
                datos.add("Noche");
                break;
        }
        return datos;
    }

    // watcher para los campos y validacion de campos cuando estos cambian
    private void setupTextWatcher() {
        etTotalHoras.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(android.text.Editable editable) {
                // Validar si las horas son correctas
                String totalHoras = editable.toString().trim();
                if (!TextUtils.isEmpty(totalHoras) && !validarTotalHoras(totalHoras)) {
                    Toast.makeText(mContext, "Ingrese un valor válido para las horas.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Observamos cuando se cambian los valores de los Spinners
        spinnerMaquinaria.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parentView, View view, int position, long id) {
                // Lógica adicional al seleccionar maquinaria
                registrarDatos();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parentView) {}
        });

        spinnerOperario.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parentView, View view, int position, long id) {
                // Lógica adicional al seleccionar operario
                registrarDatos();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parentView) {}
        });

        spinnerTurno.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parentView, View view, int position, long id) {
                // Lógica adicional al seleccionar turno
                registrarDatos();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parentView) {}
        });
    }

    // Validar que las horas ingresadas sean un número válido
    private boolean validarTotalHoras(String totalHoras) {
        try {
            Double.parseDouble(totalHoras.replace(",", "."));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Método para procesar los datos cuando cambian
    private void registrarDatos() {
        // Obtener los datos
        String operario = spinnerOperario.getSelectedItem().toString();
        String maquinaria = spinnerMaquinaria.getSelectedItem().toString();
        String turno = spinnerTurno.getSelectedItem().toString();
        String totalHoras = etTotalHoras.getText().toString().trim();
        String observaciones = etObservaciones.getText().toString().trim();

        // Validar que todo esté correcto antes de registrar
        if (validarTotalHoras(totalHoras)) {
            // Registrar los datos (puedes hacer una llamada a la base de datos aquí)
            Toast.makeText(mContext, "Datos registrados correctamente: \n" +
                    "Operario: " + operario + "\nMaquinaria: " + maquinaria + "\nTurno: " + turno, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Por favor, ingrese un valor válido para las horas.", Toast.LENGTH_SHORT).show();
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
        mContext = context;
    }
}
