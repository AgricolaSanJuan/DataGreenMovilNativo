package com.example.datagreenmovil.DAO.Estandares.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datagreenmovil.DAO.Estandares.Adapters.NewTallerAdapter;
import com.example.datagreenmovil.DAO.Estandares.TrxDParteMaquinaria.TrxDParteMaquinaria;
import com.example.datagreenmovil.DAO.Estandares.TrxParteMaquinaria.ReporteCabeceraParteMaquinaria;
import com.example.datagreenmovil.DAO.Estandares.TrxParteMaquinaria.ReporteCabeceraParteMaquinariaHelper;
import com.example.datagreenmovil.DAO.Estandares.TrxParteMaquinaria.TrxParteMaquinariaDAO;
import com.example.datagreenmovil.R;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewTallerAdapter extends RecyclerView.Adapter<NewTallerAdapter.ViewHolder> {

    private List<ReporteCabeceraParteMaquinaria> data;
    private SimpleDateFormat parser;

    public NewTallerAdapter(List<ReporteCabeceraParteMaquinaria> data) {
        this.data = data;
        this.parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reciclerview_new_taller_activity2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReporteCabeceraParteMaquinaria reporte = data.get(position);

//        USO PARA OBTENER LA INFORMACIÓN Y ENVIARLA DESDE EL FRAGMENT HACIA EL ADAPTADOR
//        ReporteCabeceraParteMaquinaria reporte = trxParteMaquinariaDAO.obtenerReporteCabeceraParteMaquinaria();

        // Fecha formateada
        String fechaString = reporte.getFecha();
        String fechaFormateada = "Fecha no disponible";
        try {
            Date fecha = parser.parse(fechaString);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            fechaFormateada = dateFormat.format(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.txvFecha.setText(reporte.getFecha());

//         Llenar los otros TextViews con la información correspondiente
        holder.txvMaquina.setText(reporte.getNombreMaquinaria());
        holder.txvOperario.setText(reporte.getNombreOperario());
        holder.txvTurno.setText(reporte.getTurno());
        holder.txvTotalHoras.setText(String.valueOf(reporte.getTotalHoras()));
        holder.txvObservaciones.setText(reporte.getObservacion());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txvFecha;
        TextView txvMaquina;
        TextView txvOperario;
        TextView txvTurno;
        TextView txvTotalHoras;
        TextView txvObservaciones;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txvFecha = itemView.findViewById(R.id.txvEFechaInicio);
            txvMaquina = itemView.findViewById(R.id.txvMaquinaria);
            txvOperario = itemView.findViewById(R.id.txvOperario);
            txvTurno = itemView.findViewById(R.id.txvTurno);
            txvTotalHoras =itemView.findViewById(R.id.txvTotalHoras);
            txvObservaciones =itemView.findViewById(R.id.txvObservaciones);
        }
    }
}
