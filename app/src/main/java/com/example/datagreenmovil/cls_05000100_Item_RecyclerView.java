package com.example.datagreenmovil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datagreenmovil.Entidades.ConfiguracionLocal;

import java.util.ArrayList;

public class cls_05000100_Item_RecyclerView extends RecyclerView.Adapter<cls_05000100_Item_RecyclerView.MyViewHolder> {

    //String idTareoActual;
    Context Context;
    Cursor tareos;
    ConfiguracionLocal objConfLocal;
    ArrayList<String> tareosSeleccionados = new ArrayList<>();

    public cls_05000100_Item_RecyclerView(Context ct, Cursor t, ConfiguracionLocal cl, ArrayList<String> lista){//, int[] img, String[] nm){
        //PENDIENTE: EN LUGAR DE PASAR UNA LISTA DE MODULOS. PASAR UN ARRAY DE TAREOS O LISTA O ALGO (CURSOR POR EJEMPLO)
        Context = ct;
        tareos = t;
        objConfLocal= cl;
        tareosSeleccionados=lista;
        //idTareoActual=idTareoActual_;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(Context);
        View view=inflater.inflate(R.layout.v_05000100_item_recyclerview_009,parent,false);
        return new MyViewHolder(view);
    }
/*
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

    }
*/
    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull cls_05000100_Item_RecyclerView.MyViewHolder holder, int position) {
        try{
            tareos.moveToPosition(position);
//            holder.txv_IdTareo.setText(tareos.getString(0));
//            holder.txv_Fecha.setText(tareos.getString(1));
//            holder.txv_IdEstado.setText(tareos.getString(2));
//            holder.txv_IdTurno.setText(tareos.getString(3));
//            holder.txv_TotalDetalles.setText(tareos.getString(4));
//            holder.txv_TotalHoras.setText(tareos.getString(5));
//            holder.txv_TotalRdtos.setText(tareos.getString(6));
//            holder.txv_IdUsuario.setText(tareos.getString(8));
//            holder.txv_Observaciones.setText(tareos.getString(9));

//PENDIENTE: ACTIVAR ESTAS FUNCIONES DE BUSQUEDA POR NOMBRE DE COLUMNA EN LUGAR DE INDEX DE COLUMNA
            holder.txv_IdTareo.setText(tareos.getString(tareos.getColumnIndex("Id")));
            holder.txv_Fecha.setText(tareos.getString(tareos.getColumnIndex("Fecha")));
            holder.txv_IdEstado.setText(tareos.getString(tareos.getColumnIndex("IdEstado")));
            holder.txv_IdTurno.setText(tareos.getString(tareos.getColumnIndex("IdTurno")));
            holder.txv_TotalDetalles.setText(tareos.getString(tareos.getColumnIndex("TotalDetalles")));
            holder.txv_TotalHoras.setText(tareos.getString(tareos.getColumnIndex("TotalHoras")));
            holder.txv_TotalRdtos.setText(tareos.getString(tareos.getColumnIndex("TotalRendimientos")));
            holder.c009_txv_TotalJornales.setText(tareos.getString(tareos.getColumnIndex("TotalJornales")));


            holder.txv_IdUsuario.setText(tareos.getString(tareos.getColumnIndex("NombreUsuario")));
            holder.txv_Observaciones.setText(tareos.getString(tareos.getColumnIndex("Observaciones")));
            //holder.txv_TotalRdtos.setText(tareos.getString(7));

            //holder.mainLayout.setBackgroundColor(ContextCompat.getColor(Context, holder.txv_IdEstado.getText().equals("PE") ? R.color.alerta : R.color.verdeClaro));
            holder.mainLayout.setBackground(ResourcesCompat.getDrawable(Context.getResources(), holder.txv_IdEstado.getText().equals("PE") ? R.drawable.bg_alerta_suave : R.drawable.bg_r_blanco,null));

            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*
                    tareos.moveToPosition(holder.getAdapterPosition());
                    String moduloSeleccionado = ; //ModulosPermitidos[position];
                    switch (moduloSeleccionado){
                        case "Tareos":
                            Intent intent = new Intent(Context, cls_05010000_Edicion.class);
                            //CON PUTEXTRAS SE PUEDEN AGREGAR PARAMETROS AQUI PARA PASARLOS A LA ACTIVIDAD QUE SE VA A ABRRIR

                            //intent.putExtra("ConfiguracionLocal",objConfLocal);

                            Context.startActivity(intent);
                        default:
                            //return getResources().getStringArray(R.array.DEFAULT);
                    }*/
                    //Toast.makeText(view.getContext(), "MAIN LAYOUT: " + holder.txv_IdTareo.getText(), Toast.LENGTH_SHORT).show();
                    //idTareoActual = holder.txv_IdTareo.getText().toString();
                    //abrirDocumento(holder.txv_IdTareo.getText().toString());
                    //abrirDocumento(tareos.getString(tareos.getColumnIndex("Id")));
                    tareos.moveToPosition(holder.getAdapterPosition());
//                    Toast.makeText(view.getContext(),tareos.getString(tareos.getColumnIndex("Id")),Toast.LENGTH_LONG).show();
                    abrirDocumento(tareos.getString(tareos.getColumnIndex("Id")));
                }
            });

            holder.cbx_Seleccionado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*
                    tareos.moveToPosition(holder.getAdapterPosition());
                    String moduloSeleccionado = ; //ModulosPermitidos[position];
                    switch (moduloSeleccionado){
                        case "Tareos":
                            Intent intent = new Intent(Context, cls_05010000_Edicion.class);
                            //CON PUTEXTRAS SE PUEDEN AGREGAR PARAMETROS AQUI PARA PASARLOS A LA ACTIVIDAD QUE SE VA A ABRRIR

                            //intent.putExtra("ConfiguracionLocal",objConfLocal);

                            Context.startActivity(intent);
                        default:
                            //return getResources().getStringArray(R.array.DEFAULT);
                    }*/
                    //Toast.makeText(view.getContext(), "Check", Toast.LENGTH_SHORT).show();
                    String idTareoCheckeado=holder.txv_IdTareo.getText().toString();
                    if(holder.cbx_Seleccionado.isChecked() && !tareosSeleccionados.contains(idTareoCheckeado)){
                        tareosSeleccionados.add(idTareoCheckeado);
                    }else if (!holder.cbx_Seleccionado.isChecked() && tareosSeleccionados.contains(idTareoCheckeado)){
                        tareosSeleccionados.remove(idTareoCheckeado);
                    }
                }
            });
        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public int getItemCount()
    {
        return tareos.getCount();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView txv_IdTareo, txv_Fecha, txv_IdEstado, txv_IdTurno, txv_TotalDetalles, txv_TotalHoras, txv_TotalRdtos, c009_txv_TotalJornales, txv_IdUsuario, txv_Observaciones;
        CheckBox cbx_Seleccionado;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txv_IdTareo = itemView.findViewById(R.id.c009_txv_IdTareo_v);
            txv_Fecha = itemView.findViewById(R.id.c009_txv_Fecha_v);
            txv_IdEstado = itemView.findViewById(R.id.c009_txv_IdEstado_v);
            txv_IdTurno = itemView.findViewById(R.id.c009_txv_IdTurno_v);
            txv_TotalDetalles = itemView.findViewById(R.id.c009_txv_TotalDetalles_v);
            txv_TotalHoras = itemView.findViewById(R.id.c009_txv_TotalHoras_v);
            txv_TotalRdtos = itemView.findViewById(R.id.c009_txv_TotalRdtos_v);
            c009_txv_TotalJornales = itemView.findViewById(R.id.c009_txv_TotalJornales_v);
            cbx_Seleccionado = itemView.findViewById(R.id.cbx_Seleccionado_v);
            txv_IdUsuario = itemView.findViewById(R.id.c009_txv_IdUsuario_v);
            txv_Observaciones = itemView.findViewById(R.id.c009_txv_Observaciones_v);

            mainLayout = itemView.findViewById(R.id.c009_mly_Principal_v);
        }
    }

    private void abrirDocumento(String id) {
        Intent nuevaActividad = new Intent(Context, cls_05010000_Edicion.class);
        nuevaActividad.putExtra("ConfiguracionLocal",objConfLocal);
        nuevaActividad.putExtra("IdDocumentoActual",id);
        Context.startActivity(nuevaActividad);
    }
}
