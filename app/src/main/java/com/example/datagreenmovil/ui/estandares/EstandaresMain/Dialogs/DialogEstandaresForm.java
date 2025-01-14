package com.example.datagreenmovil.ui.estandares.EstandaresMain.Dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.datagreenmovil.Conexiones.AppDatabase;
import com.example.datagreenmovil.DAO.Estandares.MstMedidasEstandares.MstMedidasEstandaresDAO;
import com.example.datagreenmovil.DAO.Estandares.MstTiposBonoEstandar.MstTiposBonoEstandarDAO;
import com.example.datagreenmovil.DAO.Estandares.MstTiposCostoEstandar.MstTiposCostoEstandarDAO;
import com.example.datagreenmovil.DAO.Estandares.MstTiposEstandar.MstTiposEstandarDAO;
import com.example.datagreenmovil.DAO.Estandares.TrxEstandares.TrxEstandaresNew;
import com.example.datagreenmovil.DAO.Estandares.TrxEstandares.TrxEstandaresNewDAO;
import com.example.datagreenmovil.DataGreenApp;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.databinding.FragmentEstandaresFormBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.Calendar;
import java.util.List;

public class DialogEstandaresForm extends DialogFragment {

    boolean positiveButton, negativeButton, neutralButton;
    SetAction setAction;
    FragmentEstandaresFormBinding binding;
    Context context;
    AppDatabase database;
    MstTiposEstandarDAO tiposEstandar;
    MstTiposBonoEstandarDAO tipoBonos;
    MstMedidasEstandaresDAO medidas;
    MstTiposCostoEstandarDAO tipoCosto;
    TrxEstandaresNewDAO estandaresDAO;
    TrxEstandaresNew estandarActual = new TrxEstandaresNew();

    public DialogEstandaresForm(Context context, boolean positiveButton, boolean negativeButton, boolean neutralButton, SetAction setAction) {
        this.context = context;
        this.positiveButton = positiveButton;
        this.negativeButton = negativeButton;
        this.neutralButton = neutralButton;
        this.setAction = setAction;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Usando MaterialAlertDialogBuilder
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = FragmentEstandaresFormBinding.inflate(inflater);
        builder.setView(binding.getRoot());

        database = DataGreenApp.getAppDatabase();
        inicializarDaos();

        List<DaoItem> listaMedidas = medidas.obtenerMedidasEstandaresParaLista();
        List<DaoItem> listaTiposEstandar = tiposEstandar.obtenerTiposEstandarParaLista();
        List<DaoItem> listaTiposBono = tipoBonos.obtenerTiposBonoEstandarParaLista();
        List<DaoItem> listaTiposCosto = tipoCosto.obtenerTiposCostoEstandarParaLista();

// Crear el adaptador y asociarlo al MaterialAutoCompleteTextView

//        ADAPTADOR PARA COMBO MEDIDAS
        DaoAdapter medidasAdapter = new DaoAdapter(context, listaMedidas);
        setAdapterInArray(binding.cboMedida, medidasAdapter);
        binding.cboMedida.setOnItemClickListener((parent, view, position, id) -> {
            DaoItem selectedItem = (DaoItem) parent.getItemAtPosition(position);
            estandarActual.setIdMedidaEstandar(Integer.parseInt(selectedItem.getId()));
        });
//        ADAPTADOR PARA COMBO TIPOS DE ESTANDAR
        DaoAdapter tiposEstandarAdapter = new DaoAdapter(context, listaTiposEstandar);
        setAdapterInArray(binding.cboTipoEstandar, tiposEstandarAdapter);
        binding.cboTipoEstandar.setOnItemClickListener((parent, view, position, id) -> {
            DaoItem selectedItem = (DaoItem) parent.getItemAtPosition(position);
            estandarActual.setIdTipoEstandar(selectedItem.getId());
        });
//        ADAPTADOR PARA COMBO TIPOS DE BONO
        DaoAdapter tiposBonoAdapter = new DaoAdapter(context, listaTiposBono);
        setAdapterInArray(binding.cboTipoBono, tiposBonoAdapter);
        binding.cboTipoBono.setOnItemClickListener((parent, view, position, id) -> {
            DaoItem selectedItem = (DaoItem) parent.getItemAtPosition(position);
            estandarActual.setIdTipoBonoEstandar(Integer.parseInt(selectedItem.getId()));
        });
//        ADAPTADOR PARA COMBO TIPOS DE COSTO
        DaoAdapter tiposCostoAdapter = new DaoAdapter(context, listaTiposCosto);
        setAdapterInArray(binding.cboTipoCosto, tiposCostoAdapter);
        binding.cboTipoCosto.setOnItemClickListener((parent, view, position, id) -> {
            DaoItem selectedItem = (DaoItem) parent.getItemAtPosition(position);
            estandarActual.setIdTipoCostoEstandar(Integer.parseInt(selectedItem.getId()));
        });

        estandarActual.setId(11);
        estandarActual.setIdEmpresa("001");
        estandarActual.setIdActividad("U2C");
        estandarActual.setIdLabor("002");
        estandarActual.setCantidad(1.00);
        estandarActual.setPrecio(0.023634);
        estandarActual.setBase(1827.50);
        estandarActual.setPrecioBase(0.0);
        estandarActual.setValMinExcedente(0.0);
        estandarActual.setHoras(8.00);
        estandarActual.setIdConsumidor("1104-R");
        estandarActual.setPorcentajeValidExcedente(0.00);
        estandarActual.setFactorPrecio(0.00);
        estandarActual.setDniUsuarioCrea("72450801");
        estandarActual.setFechaHoraCrea("2025-01-14 12:35:17.833");
        estandarActual.setDniUsuarioActualiza("72450801");
        estandarActual.setFechaHoraActualiza("2025-01-14 12:35:17.833");


//        SETEAMOS EL CONTROL DEL CALENDARIO
        // Establecer el inputType a "none" para deshabilitar el teclado
        binding.etFecha.setInputType(InputType.TYPE_NULL);

// Configurar el listener para abrir el DatePickerDialog
        binding.etFecha.setFocusable(false); // Desactivar el enfoque para evitar que se muestre el teclado
        binding.etFecha.setClickable(true);  // Asegurarse de que el EditText sea clickeable

        binding.etFecha.setOnClickListener(v -> {
            // Obtener la fecha actual
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            // Crear el DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view, year1, month1, dayOfMonth1) -> {
                        // Formatear la fecha seleccionada y establecerla en el EditText
                        String selectedDate = String.format("%d-%02d-%02d", year1, month1 + 1, dayOfMonth1);
                        String selectedPeriodo = String.format("%d%02d", year1, month1 + 1, dayOfMonth1);
                        binding.etFecha.setText(selectedDate);
                        binding.etPeriodo.setText(selectedPeriodo);
                        estandarActual.setFechaInicio(selectedDate);
                        estandarActual.setFechaFinal(selectedDate);
                        estandarActual.setPeriodo(selectedPeriodo);
                    }, year, month, dayOfMonth);

            // Mostrar el DatePickerDialog
            datePickerDialog.show();
        });

        // Configuración de los botones
        if (positiveButton) {
            builder.setPositiveButton("Guardar", (dialog, which) -> {
                Swal.confirm(context, "GUARDAR ESTANDAR", "Está seguro de guardar este estandar?").setConfirmClickListener(sweetAlertDialog -> {
                    estandaresDAO.insertarEstandares(estandarActual);
                    setAction.setPositiveAction();
                }).setCancelClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                });
            });
        }
        if (negativeButton) {
            builder.setNegativeButton("Cancelar", (dialog, which) -> {
                setAction.setNegativeAction();
            });
        }
        if (neutralButton) {
            builder.setNeutralButton("Cerrar", (dialog, which) -> {
                setAction.setNeutralAction();
            });
        }

        // Crear el diálogo
        AlertDialog dialog = builder.create();

        // Asegúrate de que el diálogo se muestre completamente antes de modificar los botones
        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

            // Asegúrate de que las referencias a los botones no sean null
            if (positiveButton != null) {
                positiveButton.setTextColor(ContextCompat.getColor(context, R.color.blancoOficial));
                positiveButton.setBackgroundColor(ContextCompat.getColor(context, R.color.azulPrimario));
            }

            if (negativeButton != null) {
                negativeButton.setTextColor(ContextCompat.getColor(context, R.color.blancoOficial));
                negativeButton.setBackgroundColor(ContextCompat.getColor(context, R.color.alerta));
            }

            if (neutralButton != null) {
                neutralButton.setTextColor(ContextCompat.getColor(context, R.color.blancoOficial));
                neutralButton.setBackgroundColor(ContextCompat.getColor(context, R.color.grisOscuro));
            }

            // Modificar la altura del diálogo
            View dialogView = dialog.findViewById(android.R.id.content);
            int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
            int top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
            int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
            int bottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
            dialogView.setPadding(left, top, right, bottom);


        });

        return dialog;
    }



    private void setAdapterInArray(MaterialAutoCompleteTextView componente, DaoAdapter adapter) {
        componente.setAdapter(adapter);
        componente.setThreshold(1);  // Esto asegura que el dropdown se muestre después de escribir un carácter

        componente.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                componente.showDropDown();
            } else {
                componente.dismissDropDown();
            }
        });
    }

    private void inicializarDaos() {
        estandaresDAO = database.estandaresNewDAO();
        tipoBonos = database.mstTiposBonoEstandarDAO();
        tipoCosto = database.mstTiposCostoEstandarDAO();
        tiposEstandar = database.mstTiposEstandarDAO();
        medidas = database.mstMedidasEstandaresDAO();
    }

    public interface SetAction {
        void setPositiveAction();

        void setNegativeAction();

        void setNeutralAction();
    }
}