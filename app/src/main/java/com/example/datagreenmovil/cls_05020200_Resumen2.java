package com.example.datagreenmovil;

import android.content.Context;
import android.database.Cursor;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle.DTO.ReporteDTO;

import java.util.List;

//public class cls_05020200_Resumen2 {
public class cls_05020200_Resumen2 extends RecyclerView.Adapter<cls_05020200_Resumen2.MyViewHolder> {

    List<ReporteDTO> reporteDTO;
    Context ctx;

    public cls_05020200_Resumen2(android.content.Context ctx, List<ReporteDTO> reporteDTO) {
        this.ctx = ctx;
        this.reporteDTO = reporteDTO;
    }

    @NonNull
    @Override
    public cls_05020200_Resumen2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(ctx);
        View view=inflater.inflate(R.layout.v_05020200_resumen2_012,parent,false);
        return new cls_05020200_Resumen2.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cls_05020200_Resumen2.MyViewHolder holder, int position) {
        ReporteDTO itemDTO = reporteDTO.get(position);

        holder.txv_IdSupervisor.setText(itemDTO.getNombreUsuario());
        holder.txv_Fecha.setText(itemDTO.getFecha());
        holder.txv_Consumidor.setText(itemDTO.getConsumidor());
        holder.txv_Horas.setText(itemDTO.getHoras());
        holder.txv_Rdtos.setText(itemDTO.getRendimientos());
        holder.txv_Personas.setText(String.valueOf(itemDTO.getPersonas()));
        holder.txv_Jornales.setText(itemDTO.getJornales());
        holder.txv_Promedio.setText(itemDTO.getPromedio());
        holder.txv_Items.setText(String.valueOf(itemDTO.getItems()));
    }

    @Override
    public int getItemCount()
    {
        return reporteDTO.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView txv_IdSupervisor, txv_Fecha, txv_Consumidor, txv_Horas, txv_Rdtos, txv_Personas, txv_Jornales, txv_Promedio, txv_Items;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txv_IdSupervisor = itemView.findViewById(R.id.txv_TareosReportes_RCV2_IdSupervisor_v);
            txv_Fecha = itemView.findViewById(R.id.txv_TareosReportes_RCV2_Fecha_v);
            txv_Consumidor = itemView.findViewById(R.id.txv_TareosReportes_RCV2_Consumidor_v);
            txv_Horas = itemView.findViewById(R.id.txv_TareosReportes_RCV2_Horas_v);
            txv_Rdtos = itemView.findViewById(R.id.txv_TareosReportes_RCV2_Rdtos_v);
            txv_Jornales = itemView.findViewById(R.id.txv_TareosReportes_RCV2_Jornales_v);
            txv_Personas = itemView.findViewById(R.id.txv_TareosReportes_RCV2_Personas_v);
            txv_Promedio = itemView.findViewById(R.id.txv_TareosReportes_RCV2_Promedio_v);
            txv_Items = itemView.findViewById(R.id.txv_TareosReportes_RCV2_Items_v);
        }
    }
}

