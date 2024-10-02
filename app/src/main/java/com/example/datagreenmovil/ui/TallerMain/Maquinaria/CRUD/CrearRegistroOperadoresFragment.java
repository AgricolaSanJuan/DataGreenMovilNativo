package com.example.datagreenmovil.ui.TallerMain.Maquinaria.CRUD;

import androidx.lifecycle.ViewModelProvider;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.RadioGroup;

import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.databinding.FragmentTallerMainBinding;
import com.example.datagreenmovil.databinding.FragmentTallerMaquinariaCrearRegistroOperadoresBinding;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

public class CrearRegistroOperadoresFragment extends Fragment {

    private CrearRegistroOperadoresViewModel mViewModel;
    String s_fechaRegistro = LocalDate.now().plusDays(-0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    ConfiguracionLocal objConfLocal;
    Context ctx;
    public static CrearRegistroOperadoresFragment newInstance() {
        return new CrearRegistroOperadoresFragment();
    }

    private FragmentTallerMaquinariaCrearRegistroOperadoresBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding =  FragmentTallerMaquinariaCrearRegistroOperadoresBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Funciones.mostrarEstatusGeneral(ctx,
                objConfLocal,
                binding.tmcroTxvPushTituloVentanaV,
                binding.tmcroTxvPushRedV,
                binding.tmcroTxvNombreAppV,
                binding.tmcroTxvPushVersionAppV,
                binding.tmcroTxvPushVersionDataBaseV,
                binding.tmcroTxvPushIdentificadorV
        );

//        MAQUINARIA
        binding.tvMaquinaria.setOnClickListener(view -> {
            if (binding.llyMaquinaria.getVisibility() == View.VISIBLE) {
                // Si ya está visible, ocultarlo con animación
                binding.llyMaquinaria.setVisibility(View.GONE);
            } else {
                // Si no está visible, mostrarlo con animación
                binding.llyMaquinaria.setVisibility(View.VISIBLE);
            }
        });

//        OPERARIO
        binding.tvOperario.setOnClickListener(view -> {
            if (binding.llyOperario.getVisibility() == View.VISIBLE) {
                // Si ya está visible, ocultarlo con animación
                binding.llyOperario.setVisibility(View.GONE);
            } else {
                // Si no está visible, mostrarlo con animación
                binding.llyOperario.setVisibility(View.VISIBLE);
            }
        });

//        ACTIVIDAD
        binding.tvActividad.setOnClickListener(view -> {
            if (binding.llyActividad.getVisibility() == View.VISIBLE) {
                // Si ya está visible, ocultarlo con animación
                binding.llyActividad.setVisibility(View.GONE);
            } else {
                // Si no está visible, mostrarlo con animación
                binding.llyActividad.setVisibility(View.VISIBLE);
            }
        });

//        CONSUMIDOR
        binding.tvConsumidor.setOnClickListener(view -> {
            if (binding.llyConsumidor.getVisibility() == View.VISIBLE) {
                // Si ya está visible, ocultarlo con animación
                binding.llyConsumidor.setVisibility(View.GONE);
            } else {
                // Si no está visible, mostrarlo con animación
                binding.llyConsumidor.setVisibility(View.VISIBLE);
            }
        });


        return root;
//        return inflater.inflate(R.layout.fragment_taller_maquinaria_crear_registro_operadores, container, false);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ctx = context;
    }

    private void setearControles() {
        //        builderDialogoCerrarSesion= Funciones.setearAlertDialogParaCerrarSesion_(objConfLocal,objSqlite,this);

        //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
        //...

        binding.tmcroTxtFechaV.setText(Funciones.malograrFecha(s_fechaRegistro));


        binding.tmcroTxtFechaV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                String s_Valor = "";
//                s_Valor = talmai_txv_DesdeFecha.getText().toString();
                s_fechaRegistro = Funciones.arreglarFecha(binding.tmcroTxtFechaV.getText().toString());
//                s_fechaRegistro = Funciones.arreglarFecha(s_Valor);
            }
        });
    }

    private void expandView(View view) {
        // Configurar una animación de desplazamiento hacia abajo
        ObjectAnimator slideDown = ObjectAnimator.ofFloat(view, "translationY", - view.getHeight(), 0);
        slideDown.setDuration(500);  // Duración de la animación en milisegundos
        slideDown.setInterpolator(new DecelerateInterpolator());  // Opcional: establecer un interpolador para la animación
        slideDown.start();  // Iniciar la animación
        view.setVisibility(View.VISIBLE);  // Hacer visible el LinearLayout
    }


    private void collapseView(View view) {
        // Configurar una animación de desplazamiento hacia arriba
        ObjectAnimator slideUp = ObjectAnimator.ofFloat(view, "translationY", 0, binding.llyMaquinaria.getHeight());
        slideUp.setDuration(500);  // Duración de la animación en milisegundos
        slideUp.setInterpolator(new DecelerateInterpolator());  // Opcional: establecer un interpolador para la animación
        slideUp.start();  // Iniciar la animación
        view.setVisibility(View.GONE);  // Ocultar el LinearLayout cuando la animación termine
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CrearRegistroOperadoresViewModel.class);
        // TODO: Use the ViewModel
    }

}