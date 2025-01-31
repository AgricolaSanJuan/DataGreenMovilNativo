package com.example.datagreenmovil;

//public class cls_05020100_Resumen1 {

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle.DTO.ReporteDTO;
import com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle.DTO.ReporteDTO;

import java.util.List;

public class cls_05020100_Resumen1 extends RecyclerView.Adapter<cls_05020100_Resumen1.MyViewHolder> {
    List<ReporteDTO> reporte1DTO;
    Context ctx;

    public cls_05020100_Resumen1(Context ctx, List<ReporteDTO> reporte1DTO) {
        this.ctx = ctx;
        this.reporte1DTO = reporte1DTO;
    }

    @NonNull
    @Override
    public cls_05020100_Resumen1.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.v_05020100_resumen1_011, parent, false);
        return new cls_05020100_Resumen1.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cls_05020100_Resumen1.MyViewHolder holder, int position) {
        ReporteDTO itemDTO = reporte1DTO.get(position);

        holder.txv_IdSupervisor.setText(itemDTO.getNombreUsuario());
        holder.txv_Fecha.setText(itemDTO.getFecha());
        holder.txv_Horas.setText(itemDTO.getHoras());
        holder.txv_Rdtos.setText(itemDTO.getRendimientos());
        holder.txv_Personas.setText(String.valueOf(itemDTO.getPersonas()));
        holder.txv_Jornales.setText(itemDTO.getJornales());
        holder.txv_Promedio.setText(itemDTO.getPromedio());
        holder.txv_Items.setText(String.valueOf(itemDTO.getItems()));
    }

    @Override
    public int getItemCount() {
        return reporte1DTO.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
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