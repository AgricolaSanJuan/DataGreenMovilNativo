package com.example.datagreenmovil.Entidades;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.datagreenmovil.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.time.Year;

public class PopUpAnio extends Dialog {
    int anioSeleccionado;
    CalendarView c028_cal_Calendario;
    FloatingActionButton c028_fab_Ok;
    String fecha;
    SimpleDateFormat sdf;
    TextView txv_FechaSeleccionada;

    public PopUpAnio(@NonNull Context context, TextView txv_FechaSeleccionada) {
        super(context);
        setContentView(R.layout.dialog_selector_anio);
        NumberPicker npAnio = findViewById(R.id.npAnio);
        FloatingActionButton fabOk = findViewById(R.id.fabAnioOk);
        npAnio.setMinValue(Year.now().getValue() - 5);
        npAnio.setMaxValue(Year.now().getValue());
        npAnio.setValue(Year.now().getValue());
        this.txv_FechaSeleccionada = txv_FechaSeleccionada;

        fabOk.setOnClickListener(view -> {
            anioSeleccionado = npAnio.getValue();
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
                txv_FechaSeleccionada.setText(String.valueOf(anioSeleccionado));
//            }
//        });
    }
}
