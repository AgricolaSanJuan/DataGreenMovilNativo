package com.example.datagreenmovil.Entidades;

import android.app.Dialog;
import android.content.Context;
import android.widget.CalendarView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class PopUpSemana extends Dialog {
    int semanaSeleccionada;
    TextView txvSemanaSeleccionada;

    public PopUpSemana(@NonNull Context context, TextView txvSemanaSeleccionada) {
        super(context);
        setContentView(R.layout.dialog_selector_semana);
        NumberPicker npSemana = findViewById(R.id.npSemana);
        FloatingActionButton fabSemanaOk = findViewById(R.id.fabSemanaOk);
        npSemana.setMinValue(1);
        npSemana.setMaxValue(52);
        this.txvSemanaSeleccionada = txvSemanaSeleccionada;
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int numeroSemana = LocalDate.now().get(weekFields.weekOfWeekBasedYear());
        npSemana.setValue(numeroSemana);
        fabSemanaOk.setOnClickListener(view -> {
            semanaSeleccionada = npSemana.getValue();
            setTextView();
            dismiss();
        });

        this.getWindow().setBackgroundDrawableResource(R.drawable.bg_popup);
    }

    private void setTextView() {
//        c028_cal_Calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
//                anio = i;
//                mes = i1 + 1;  // Agregamos 1 porque el valor del mes es 0-based (0 representa enero, 1 representa febrero, etc.)
//                dia = i2;
//                fecha =  (dia < 10 ? "0" + dia : dia) + "/" + (mes < 10 ? "0" + mes : mes) + "/" + anio;
//                Log.i("FECHA:", fecha);
                txvSemanaSeleccionada.setText(String.valueOf(semanaSeleccionada));
//            }
//        });
    }
}
