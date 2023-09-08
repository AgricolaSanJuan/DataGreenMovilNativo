package com.example.datagreenmovil.Entidades;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.datagreenmovil.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PopUpCalendario extends Dialog {
    int anio, mes, dia;
    public PopUpCalendario(@NonNull Context context, TextView txv_FechaSeleccionada) {
        super(context);
        setContentView(R.layout.popup_calendario_028);
        ImageView c028_img_Cerrar = findViewById(R.id.c028_img_Cerrar_v);
        CalendarView c028_cal_Calendario = findViewById(R.id.c028_cal_Calendario_v);
        FloatingActionButton c028_fab_Ok = findViewById(R.id.c028_fab_Ok_v);

        c028_img_Cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        c028_cal_Calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                anio=i;
                mes=i1+1;
                dia=i2;
//                String fecha = (i1+1) + "/" + i2 + "/" + i;
//                txv_FechaSeleccionada.setText(fecha);
            }
        });
        c028_fab_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fecha = (dia < 10 ? "0" + dia : dia) + "/" + (mes < 10 ? "0" + mes : mes) + "/" + anio;
                txv_FechaSeleccionada.setText(fecha);
                dismiss();
            }
        });

//        Double x, y;
//        x = context.getResources().getDisplayMetrics().widthPixels * 0.90;
//        y = context.getResources().getDisplayMetrics().heightPixels * 0.80;
//        this.getWindow().setLayout(x.intValue(),y.intValue());
        this.getWindow().setBackgroundDrawableResource(R.drawable.bg_popup);
    }
}
