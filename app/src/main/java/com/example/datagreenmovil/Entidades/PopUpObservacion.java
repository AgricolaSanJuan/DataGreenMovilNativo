package com.example.datagreenmovil.Entidades;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.datagreenmovil.R;
import com.example.datagreenmovil.cls_05010000_Edicion;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PopUpObservacion extends Dialog {

    public PopUpObservacion(@NonNull Context context, String antiguaObservacion, TextView etx_Observacion) {
        super(context);
        //CREANDO Y REFERENCIANDO CONTROLES
        setContentView(R.layout.popup_observacion_029);
        ImageView c029_img_Cerrar = findViewById(R.id.c029_img_Cerrar_v);
        EditText c029_eml_Observacion = findViewById(R.id.c029_eml_Observacion_v);
        FloatingActionButton c029_fab_Ok = findViewById(R.id.c029_fab_Ok_v);

        //CARGANDO VALORES EN CONTROLES
        c029_eml_Observacion.setText(antiguaObservacion);
        //SETEANDO COMPORTAMIENTO DE CONTROLES
        c029_img_Cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        c029_fab_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ASIGNACION DEL VALOR HACIA LA VARIABLE QUE VIENE POR REFERENCIA
                String nuevaObservacion = c029_eml_Observacion.getText().toString();
                if (!antiguaObservacion.equals(nuevaObservacion)){
                    etx_Observacion.setText(nuevaObservacion);
                }
//
//                if (context instanceof cls_05010000_Edicion) {
//                    ((cls_05010000_Edicion)mContext).eliminarDetalle(itemSeleccionado);
//                }
                dismiss();
            }
        });

        //SETEAR FONDO DE CONTENEDOR PRINCIPAL
        this.getWindow().setBackgroundDrawableResource(R.drawable.bg_popup);

        //OPCIONAL: AJUSTAR TAMAÑO DE CONTENEDOR PRINCIPAL A PORCENTAJE DE LA PANTALLA
        Double x, y;
        x = context.getResources().getDisplayMetrics().widthPixels * 0.90;
        y = context.getResources().getDisplayMetrics().heightPixels * 0.40;
//        int y = this.getWindow().getAttributes().height;
        this.getWindow().setLayout(x.intValue(),y.intValue());
    }
}
