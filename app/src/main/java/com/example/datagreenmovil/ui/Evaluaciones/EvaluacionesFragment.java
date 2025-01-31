
package com.example.datagreenmovil.ui.Evaluaciones;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle.ActividadHelper;

import com.example.datagreenmovil.databinding.FragmentEvaluacionesBinding;

import java.util.Calendar;

public class EvaluacionesFragment extends Fragment {

    public class EntidadPrueba{
        private String id;
        private String idActividad;
        private String idLabor;
        private String fecha;

        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getIdActividad() {
            return idActividad;
        }
        public void setIdActividad(String idActividad) {
            this.idActividad = idActividad;
        }
        public String getIdLabor() {
            return idLabor;
        }
        public void setIdLabor(String idLabor) {
            this.idLabor = idLabor;
        }

        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

        // falta referenciar las demás variables de la pantalla


    }


    private FragmentEvaluacionesBinding binding;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        EvaluacionesViewModel homeViewModel =
                new ViewModelProvider(this).get(EvaluacionesViewModel.class);

        binding = FragmentEvaluacionesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //definir una lista de dato para la data

        ActividadHelper actividadHelper = new ActividadHelper();

//        descomentar al solucionar el error
//        List<DaoItem> importePrueba = actividadHelper.obtenerActivdadesParaLista();
//        DaoAdapter actividadesAdapter = new DaoAdapter(context,importePrueba);
//
//        EntidadPrueba nuevaEntidad = new EntidadPrueba();
//        binding.cboActividad.setOnItemClickListener((parent, view, position, id) -> {
//            DaoItem item = (DaoItem) parent.getItemAtPosition(position);
//            nuevaEntidad.setIdActividad(item.getId());
//            Swal.info(context, "SELECCIONADO", item.getDescripcion(), 8000);
//        });
//        binding.cboActividad.setAdapter(actividadesAdapter);
//        binding.cboActividad.setOnFocusChangeListener((v, hasFocus) -> {
//            if (hasFocus) {
//                binding.cboActividad.showDropDown();
//            } else {
//                binding.cboActividad.dismissDropDown();
//            }
//        });

        // Configuración de los campos
        binding.etFecha.setInputType(InputType.TYPE_NULL);
        binding.etFecha.setFocusable(false);  // Desactivar el enfoque para evitar que se muestre el teclado
        binding.etFecha.setClickable(true);  // Asegurarse de que el EditText sea clickeable

        // Configurar el listener para el DatePickerDialog (ya configurado)
        binding.etFecha.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view, year1, month1, dayOfMonth1) -> {
                        String selectedDate = String.format("%d-%02d-%02d", year1, month1 + 1, dayOfMonth1);
                        binding.etFecha.setText(selectedDate);
                    }, year, month, dayOfMonth);
            datePickerDialog.show();
        });

        // Configuración del TimePickerDialog para el campo 'etInicio'
        binding.etInicio.setInputType(InputType.TYPE_NULL);  // Desactivar el teclado
        binding.etInicio.setFocusable(false);  // Desactivar el enfoque
        binding.etInicio.setClickable(true);  // Asegurarse de que el EditText sea clickeable

        binding.etInicio.setOnClickListener(v -> {
            // Obtener la hora actual
            Calendar calendarioInicio = Calendar.getInstance();
            int horaInicio = calendarioInicio.get(Calendar.HOUR);
            int minutoInicio = calendarioInicio.get(Calendar.MINUTE);
            boolean isAM = calendarioInicio.get(Calendar.AM_PM) == Calendar.AM;

            // Crear el TimePickerDialog para 'etInicio' con formato AM/PM
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    (view, selectedHour, selectedMinute) -> {
                        // Formatear la hora seleccionada y establecerla en el EditText
                        String period = selectedHour < 12 ? "AM" : "PM";
                        String formattedTime = String.format("%02d:%02d %s", selectedHour == 0 ? 12 : (selectedHour > 12 ? selectedHour - 12 : selectedHour), selectedMinute, period);
                        binding.etInicio.setText(formattedTime);
                    }, horaInicio, minutoInicio, false);  // 'false' para formato AM/PM
            timePickerDialog.show();
        });



        // Configuración del TimePickerDialog para el campo 'etFin'
        binding.etFin.setInputType(InputType.TYPE_NULL);  // Desactivar el teclado
        binding.etFin.setFocusable(false);  // Desactivar el enfoque
        binding.etFin.setClickable(true);  // Asegurarse de que el EditText sea clickeable

        binding.etFin.setOnClickListener(v -> {
            // Obtener la hora actual para 'etFin'
            Calendar calendarioFin = Calendar.getInstance();
            int horaFin = calendarioFin.get(Calendar.HOUR);
            int minutoFin = calendarioFin.get(Calendar.MINUTE);
            boolean isAM = calendarioFin.get(Calendar.AM_PM) == Calendar.AM;

            // Crear el TimePickerDialog para 'etFin' con formato AM/PM
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    (view, selectedHour, selectedMinute) -> {
                        // Formatear la hora seleccionada y establecerla en el EditText
                        String period = selectedHour < 12 ? "AM" : "PM";
                        String formattedTime = String.format("%02d:%02d %s", selectedHour == 0 ? 12 : (selectedHour > 12 ? selectedHour - 12 : selectedHour), selectedMinute, period);
                        binding.etFin.setText(formattedTime);
                    }, horaFin, minutoFin, false);  // 'false' para formato AM/PM
            timePickerDialog.show();
        });

        binding.btnSalir.setOnClickListener(v -> {
            // Obtener el NavController y navegar hacia atrás
            requireActivity().onBackPressed();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
