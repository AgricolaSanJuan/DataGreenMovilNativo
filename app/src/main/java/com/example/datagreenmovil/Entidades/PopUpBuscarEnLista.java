package com.example.datagreenmovil.Entidades;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.datagreenmovil.R;

import java.util.ArrayList;

public class PopUpBuscarEnLista extends Dialog implements SearchView.OnQueryTextListener {

    ArrayList<PopUpBuscarEnLista_Item> lista;
    //String idSeleccionado, DescripcionSeleccionada;
    PopUpBuscarEnLista_RvwAdapter adapter;

    public PopUpBuscarEnLista(@NonNull Context context, ArrayList<PopUpBuscarEnLista_Item> l, TextView txv_Id, TextView txv_Descripcion) {
        super(context);
        setContentView(R.layout.popup_buscar_en_lista_026);
        lista= (ArrayList<PopUpBuscarEnLista_Item>) l.clone();
        SearchView c026_svw_Texto = findViewById(R.id.c026_svw_Texto_v);
        ///////CONTINUAR AQUI: PROCESO DE FILTRADO DEL RECYCLER VIEW
        ImageView c026_img_Cerrar = findViewById(R.id.c026_img_Cerrar_v);
        RecyclerView c026_rvw_Resultado = findViewById(R.id.c026_rvw_Resultado_v);

//        c026_rvw_Resultado.setLayoutManager(new LinearLayoutManager(this.getContext()));

        c026_img_Cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        c026_svw_Texto.setOnQueryTextListener(this);

        adapter = new PopUpBuscarEnLista_RvwAdapter(context, lista,txv_Id, txv_Descripcion, this);
        c026_rvw_Resultado.setAdapter(adapter);
        c026_rvw_Resultado.setLayoutManager(new LinearLayoutManager(context));

        Double x, y;
        x = context.getResources().getDisplayMetrics().widthPixels * 0.90;
        y = context.getResources().getDisplayMetrics().heightPixels * 0.80;
        this.getWindow().setLayout(x.intValue(),y.intValue());
        this.getWindow().setBackgroundDrawableResource(R.drawable.bg_popup);

    }



    @Override
    public boolean onQueryTextSubmit(String s) {
//        adapter.filtrado(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
//        Funciones.notificar(getContext(),"onquerytextchange");
        adapter.filtrado(s);
        return false;
    }

}
