package com.example.datagreenmovil.DAO.Estandares.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datagreenmovil.DAO.Estandares.ReporteEstandares.ReporteEstandares;
import com.example.datagreenmovil.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EstandaresListAdapter extends RecyclerView.Adapter<EstandaresListAdapter.ViewHolder> {

    private List<ReporteEstandares> data;
    SimpleDateFormat dateFormat;

    public EstandaresListAdapter(List<ReporteEstandares> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_estandares, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String fechaString = data.get(position).getFechaInicio();
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date fechaInicio = null;
        try {
            fechaInicio = parser.parse(fechaString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String fechaFormateada = "";

        if (fechaInicio != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            fechaFormateada = dateFormat.format(fechaInicio);
            holder.txvEFecha.setText(fechaFormateada);
        } else {
            holder.txvEFecha.setText("Fecha no disponible");
        }

        holder.txvEFecha.setText(fechaFormateada);
        holder.txvEPeriodo.setText(data.get(position).getPeriodo());
//        holder.txvECultivoVariedad.setText(data.get(position));
        holder.txvEConsumidor.setText(data.get(position).getIdConsumidor());
        holder.txvEActividad.setText(data.get(position).getDescripcionActividad());
        holder.txvELabor.setText(data.get(position).getDescripcionLabor());
        holder.txvECantidad.setText(String.valueOf(data.get(position).getCantidad()));
        holder.txvEPrecio.setText("S/" + data.get(position).getPrecio());
        holder.txvEHoras.setText(String.valueOf(data.get(position).getHoras()));
        holder.txvEBase.setText("S/"+ data.get(position).getBase());
        holder.txvETipoCosto.setText("Tipo de Costo: " + data.get(position).getTipoCosto());
        holder.txvETipoBono.setText("Tipo de Bono: " + data.get(position).getTipoBono());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txvEFecha;
        TextView txvEPeriodo;
        TextView txvECultivoVariedad;
        TextView txvEConsumidor;
        TextView txvEActividad;
        TextView txvELabor;
        TextView txvECantidad;
        TextView txvEPrecio;
        TextView txvEHoras;
        TextView txvEBase;
        TextView txvETipoCosto;
        TextView txvETipoBono;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txvEFecha = itemView.findViewById(R.id.txvEFecha);
            txvEPeriodo = itemView.findViewById(R.id.txvEPeriodo);
            txvECultivoVariedad = itemView.findViewById(R.id.txvECultivoVariedad);
            txvEConsumidor = itemView.findViewById(R.id.txvEConsumidor);
            txvEActividad = itemView.findViewById(R.id.txvEActividad);
            txvELabor = itemView.findViewById(R.id.txvELabor);
            txvECantidad = itemView.findViewById(R.id.txvECantidad);
            txvEPrecio = itemView.findViewById(R.id.txvEPrecio);
            txvEHoras = itemView.findViewById(R.id.txvEHoras);
            txvEBase = itemView.findViewById(R.id.txvEBase);
            txvETipoCosto = itemView.findViewById(R.id.txvETipoCosto);
            txvETipoBono = itemView.findViewById(R.id.txvETipoBono);
        }
    }
}
