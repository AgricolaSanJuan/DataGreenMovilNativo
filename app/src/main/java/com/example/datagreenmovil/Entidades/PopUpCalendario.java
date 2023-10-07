package com.example.datagreenmovil.Entidades;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;

import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.datagreenmovil.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class PopUpCalendario extends Dialog {
    int anio, mes, dia;
    CalendarView c028_cal_Calendario;
    FloatingActionButton c028_fab_Ok;
    String fecha;
    SimpleDateFormat sdf;
    TextView txv_FechaSeleccionada;

    public PopUpCalendario(@NonNull Context context, TextView txv_FechaSeleccionada) {
        super(context);
        setContentView(R.layout.popup_calendario_028);
        ImageView c028_img_Cerrar = findViewById(R.id.c028_img_Cerrar_v);
        c028_cal_Calendario = findViewById(R.id.c028_cal_Calendario_v);
        c028_fab_Ok = findViewById(R.id.c028_fab_Ok_v);
        this.txv_FechaSeleccionada = txv_FechaSeleccionada;

        c028_img_Cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        obtenerFechaSeleccionada();

        this.getWindow().setBackgroundDrawableResource(R.drawable.bg_popup);
    }

    private void obtenerFechaSeleccionada() {
        c028_cal_Calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                anio = i;
                mes = i1 + 1;  // Agregamos 1 porque el valor del mes es 0-based (0 representa enero, 1 representa febrero, etc.)
                dia = i2;
                fecha =  (dia < 10 ? "0" + dia : dia) + "/" + (mes < 10 ? "0" + mes : mes) + "/" + anio;
                Log.i("FECHA:", fecha);
                txv_FechaSeleccionada.setText(fecha);
            }
        });
    }
}
