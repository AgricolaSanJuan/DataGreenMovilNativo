package com.example.datagreenmovil.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.datagreenmovil.DAO.Estandares.TrxDParteMaquinaria.TrxDParteMaquinaria;
import com.example.datagreenmovil.databinding.FragmentMaquinariaBinding;
import com.example.datagreenmovil.viewModel.SharedViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MaquinariaFragment extends Fragment {

    private FragmentMaquinariaBinding binding;
    private SharedViewModel sharedViewModel;
    private List<TrxDParteMaquinaria> listParteMaquinaria = new ArrayList<>();
    private String idParteMaquinariaActual = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMaquinariaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Validación para evitar NullPointerException
        if (sharedViewModel.getListaParteMaquinaria() != null && sharedViewModel.getListaParteMaquinaria().getValue() != null) {
            listParteMaquinaria = sharedViewModel.getListaParteMaquinaria().getValue();
        } else {
            listParteMaquinaria = new ArrayList<>();
        }

        if (sharedViewModel.getIdParteMaquinaria() != null && sharedViewModel.getIdParteMaquinaria().getValue() != null) {
            idParteMaquinariaActual = sharedViewModel.getIdParteMaquinaria().getValue();
        } else {
            idParteMaquinariaActual = "";
        }

        // Obtener referencias a los EditText
        EditText etFecha = binding.etFechaMaquinaria; // Referencia al campo de fecha
        EditText etHorometroInicial = binding.etHorometroInicial;
        EditText etHorometroFinal = binding.etHorometroFinal;
        EditText etNumeroMaquinaria = binding.etNumeroMaquinaria;
        EditText etNumeroTancadas = binding.etNumeroTancadas;
        Button btnRegistrar = binding.btnRegistrarMaquinaria;

        // Establecer la fecha actual en el EditText de fecha
        String fechaActual = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        etFecha.setText(fechaActual);

        // Evento para el botón Registrar
        btnRegistrar.setOnClickListener(v -> {
            guardarDatos(etHorometroInicial, etHorometroFinal, etNumeroMaquinaria, etNumeroTancadas);
        });

        return root;
    }

    private void guardarDatos(EditText etHorometroInicial, EditText etHorometroFinal, EditText etNumeroMaquinaria, EditText etNumeroTancadas) {
        try {
            double horometroInicial = Double.parseDouble(etHorometroInicial.getText().toString());
            double horometroFinal = Double.parseDouble(etHorometroFinal.getText().toString());
            int numeroMaquinaria = Integer.parseInt(etNumeroMaquinaria.getText().toString());
            int numeroTancadas = Integer.parseInt(etNumeroTancadas.getText().toString());

            String totalHoras = String.valueOf(horometroFinal - horometroInicial);

            sharedViewModel.setTotalHoras(totalHoras);
            sharedViewModel.setObservaciones("Datos registrados desde Maquinaria");

            // Validar si el método existe antes de llamarlo
            if (sharedViewModel != null) {
                sharedViewModel.guardarDatos();
            }

            Toast.makeText(getContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Por favor ingresa valores válidos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
