package com.example.datagreenmovil;

//public class cls_05020100_Resumen1 {

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class cls_05020100_Resumen1 extends RecyclerView.Adapter<cls_05020100_Resumen1.MyViewHolder> {

    android.content.Context Context;
    Cursor Data;
    //ConfiguracionLocal objConfLocal;

    public cls_05020100_Resumen1(Context ct, Cursor data){
        Context = ct;
        Data = data;
       // objConfLocal= cl;
    }

    @NonNull
    @Override
    public cls_05020100_Resumen1.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(Context);
        View view=inflater.inflate(R.layout.v_05020100_resumen1_011,parent,false);
        return new cls_05020100_Resumen1.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cls_05020100_Resumen1.MyViewHolder holder, int position) {
        Data.moveToPosition(position);
        holder.txv_IdSupervisor.setText(Data.getString(0));
        holder.txv_Fecha.setText(Data.getString(1));
        holder.txv_Horas.setText(Data.getString(2));
        holder.txv_Rdtos.setText(Data.getString(3));
        holder.txv_Personas.setText(Data.getString(4));
        holder.txv_Jornales.setText(Data.getString(5));
        holder.txv_Promedio.setText(Data.getString(6));
        holder.txv_Items.setText(Data.getString(7));
    }

    @Override
    public int getItemCount()
    {
        return Data.getCount();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView txv_IdSupervisor, txv_Fecha, txv_Horas, txv_Rdtos, txv_Personas, txv_Jornales, txv_Promedio, txv_Items;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txv_IdSupervisor = itemView.findViewById(R.id.txv_TareosReportes_RCV1_IdSupervisor_v);
            txv_Fecha = itemView.findViewById(R.id.txv_TareosReportes_RCV1_Fecha_v);
            txv_Horas = itemView.findViewById(R.id.txv_TareosReportes_RCV1_Horas_v);
            txv_Rdtos = itemView.findViewById(R.id.txv_TareosReportes_RCV1_Rdtos_v);
            txv_Jornales = itemView.findViewById(R.id.txv_TareosReportes_RCV1_Jornales_v);
            txv_Personas = itemView.findViewById(R.id.txv_TareosReportes_RCV1_Personas_v);
            txv_Promedio = itemView.findViewById(R.id.txv_TareosReportes_RCV1_Promedio_v);
            txv_Items = itemView.findViewById(R.id.txv_TareosReportes_RCV1_Items_v);

        }
    }
}


