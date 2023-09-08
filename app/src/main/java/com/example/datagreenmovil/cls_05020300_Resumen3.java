package com.example.datagreenmovil;

import android.database.Cursor;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class cls_05020300_Resumen3 extends RecyclerView.Adapter<cls_05020300_Resumen3.MyViewHolder> {

    android.content.Context Context;
    Cursor cursorData;
    //ConfiguracionLocal objConfLocal;

    public cls_05020300_Resumen3(android.content.Context ct, Cursor data){
        Context = ct;
        cursorData = data;
        // objConfLocal= cl;
    }

    @NonNull
    @Override
    public cls_05020300_Resumen3.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(Context);
        View view=inflater.inflate(R.layout.v_05020300_resumen3_013,parent,false);
        return new cls_05020300_Resumen3.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cls_05020300_Resumen3.MyViewHolder holder, int position) {
        cursorData.moveToPosition(position);
        holder.txv_IdSupervisor.setText(cursorData.getString(0));
        holder.txv_Fecha.setText(cursorData.getString(1));
        holder.txv_Consumidor.setText(cursorData.getString(2));
        holder.txv_Actividad.setText(cursorData.getString(3));
        holder.txv_Labor.setText(cursorData.getString(4));
        holder.txv_Horas.setText(cursorData.getString(5));
        holder.txv_Rdtos.setText(cursorData.getString(6));
        holder.txv_Personas.setText(cursorData.getString(7));
        holder.txv_Jornales.setText(cursorData.getString(8));
        holder.txv_Promedio.setText(cursorData.getString(9));
        holder.txv_Items.setText(cursorData.getString(10));
    }

    @Override
    public int getItemCount()
    {
        return cursorData.getCount();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView txv_IdSupervisor, txv_Fecha, txv_Consumidor, txv_Actividad, txv_Labor, txv_Horas, txv_Rdtos, txv_Personas, txv_Jornales, txv_Promedio, txv_Items;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txv_IdSupervisor = itemView.findViewById(R.id.txv_TareosReportes_RCV3_IdSupervisor_v);
            txv_Fecha = itemView.findViewById(R.id.txv_TareosReportes_RCV3_Fecha_v);
            txv_Consumidor = itemView.findViewById(R.id.txv_TareosReportes_RCV3_Consumidor_v);
            txv_Actividad = itemView.findViewById(R.id.txv_TareosReportes_RCV3_Actividad_v);
            txv_Labor = itemView.findViewById(R.id.txv_TareosReportes_RCV3_Labor_v);
            txv_Horas = itemView.findViewById(R.id.txv_TareosReportes_RCV3_Horas_v);
            txv_Rdtos = itemView.findViewById(R.id.txv_TareosReportes_RCV3_Rdtos_v);
            txv_Jornales = itemView.findViewById(R.id.txv_TareosReportes_RCV3_Jornales_v);
            txv_Personas = itemView.findViewById(R.id.txv_TareosReportes_RCV3_Personas_v);
            txv_Promedio = itemView.findViewById(R.id.txv_TareosReportes_RCV3_Promedio_v);
            txv_Items = itemView.findViewById(R.id.txv_TareosReportes_RCV3_Items_v);
        }
    }
}
