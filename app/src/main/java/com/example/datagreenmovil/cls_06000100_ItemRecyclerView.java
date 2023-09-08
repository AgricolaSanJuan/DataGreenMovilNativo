package com.example.datagreenmovil;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.MiData;

import java.sql.ResultSet;

public class cls_06000100_ItemRecyclerView extends RecyclerView.Adapter<cls_06000100_ItemRecyclerView.MyViewHolder> {

    Context Context;
    MiData Cajas;

//    ConfiguracionLocal objConfLocal;

    public cls_06000100_ItemRecyclerView(Context ct, MiData cajas, ConfiguracionLocal cl){
        Context = ct;
        Cajas = cajas;
//        objConfLocal= cl;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(Context);
        View view=inflater.inflate(R.layout.v_06000100_item_recycler_view_016,parent,false);
        return new MyViewHolder(view);
    }

//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
//
//    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.c016_txv_NroCaja.setText(Cajas.Filas(i).Item(0));
        holder.c016_txv_CodSel.setText(Cajas.Filas(i).Item(1));
        holder.c016_txv_CodPes.setText(Cajas.Filas(i).Item(2));
        holder.c016_txv_CodEmp.setText(Cajas.Filas(i).Item(3));
        holder.c016_txv_Hora.setText(Cajas.Filas(i).Item(4));
    }

//        @Override
//        public void onBindViewHolder(@NonNull cls_06000100_ItemRecyclerView.MyViewHolder holder, int position) {
//            Cajas.moveToPosition(position);
//            holder.txv_IdTareo.setText(Cajas.getString(0));
//            holder.txv_Fecha.setText(Cajas.getString(1));
//            holder.txv_IdEstado.setText(Cajas.getString(2));
//            holder.txv_IdTurno.setText(Cajas.getString(3));
//            holder.txv_TotalDetalles.setText(Cajas.getString(4));
//            holder.txv_TotalHoras.setText(Cajas.getString(5));
//            holder.txv_TotalRdtos.setText(Cajas.getString(6));
//
//            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                /*
//                tareos.moveToPosition(holder.getAdapterPosition());
//                String moduloSeleccionado = ; //ModulosPermitidos[position];
//                switch (moduloSeleccionado){
//                    case "Tareos":
//                        Intent intent = new Intent(Context, cls_05010000_Edicion.class);
//                        //CON PUTEXTRAS SE PUEDEN AGREGAR PARAMETROS AQUI PARA PASARLOS A LA ACTIVIDAD QUE SE VA A ABRRIR
//
//                        //intent.putExtra("ConfiguracionLocal",objConfLocal);
//
//                        Context.startActivity(intent);
//                    default:
//                        //return getResources().getStringArray(R.array.DEFAULT);
//                }*/
//                    //Toast.makeText(view.getContext(), "MAIN LAYOUT: " + holder.txv_IdTareo.getText(), Toast.LENGTH_SHORT).show();
//                    //idTareoActual = holder.txv_IdTareo.getText().toString();
//                    abrirDocumento(holder.txv_IdTareo.getText().toString());
//                }
//            });
//
//            holder.cbx_Seleccionado.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                /*
//                tareos.moveToPosition(holder.getAdapterPosition());
//                String moduloSeleccionado = ; //ModulosPermitidos[position];
//                switch (moduloSeleccionado){
//                    case "Tareos":
//                        Intent intent = new Intent(Context, cls_05010000_Edicion.class);
//                        //CON PUTEXTRAS SE PUEDEN AGREGAR PARAMETROS AQUI PARA PASARLOS A LA ACTIVIDAD QUE SE VA A ABRRIR
//
//                        //intent.putExtra("ConfiguracionLocal",objConfLocal);
//
//                        Context.startActivity(intent);
//                    default:
//                        //return getResources().getStringArray(R.array.DEFAULT);
//                }*/
//                    //Toast.makeText(view.getContext(), "Check", Toast.LENGTH_SHORT).show();
//                    String idTareoCheckeado=holder.txv_IdTareo.getText().toString();
//                    if(holder.cbx_Seleccionado.isChecked() && !tareosSeleccionados.contains(idTareoCheckeado)){
//                        tareosSeleccionados.add(idTareoCheckeado);
//                    }else if (!holder.cbx_Seleccionado.isChecked() && tareosSeleccionados.contains(idTareoCheckeado)){
//                        tareosSeleccionados.remove(idTareoCheckeado);
//                    }
//                }
//            });
//
//        }

//        @Override
//        public int getItemCount() {
//            try{
//                if(Cajas.last()){
//                    return Cajas.getRow();
//                }
//                return 0;
//        }catch(Exception ex){
//                Toast.makeText(this.Context, ex.getMessage()+'2', Toast.LENGTH_SHORT).show();
//                return 0;
//            }
//        }

        @Override
        public int getItemCount() {
            try{
                return Cajas.NroFilas;
            }catch(Exception ex){
                StackTraceElement z = new Exception().getStackTrace()[0];
                String detalleError = z.getFileName() + "\n" + z.getMethodName() + "\n" + z.getLineNumber() + ": \n" + ex.getMessage();
                ex.printStackTrace();
                Toast.makeText(this.Context,detalleError, Toast.LENGTH_LONG).show();
                return 0;
            }
        }

        public static class MyViewHolder extends  RecyclerView.ViewHolder {
            TextView c016_txv_NroCaja, c016_txv_CodSel, c016_txv_CodPes, c016_txv_CodEmp, c016_txv_Hora;
            ConstraintLayout c016_cly_LayOutPrincipal;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                c016_txv_NroCaja = itemView.findViewById(R.id.c016_txv_NroCaja_v);
                c016_txv_CodSel = itemView.findViewById(R.id.c016_txv_CodSel_v);
                c016_txv_CodPes = itemView.findViewById(R.id.c016_txv_CodPes_v);
                c016_txv_CodEmp = itemView.findViewById(R.id.c016_txv_CodEmp_v);
                c016_txv_Hora = itemView.findViewById(R.id.c016_txv_Hora_v);
                c016_cly_LayOutPrincipal = itemView.findViewById(R.id.c016_cly_LayOutPrincipal);
            }
        }

//        private void abrirDocumento(String id) {
//            Intent nuevaActividad = new Intent(Context, cls_06000100_ItemRecyclerView.class);
//            nuevaActividad.putExtra("ConfiguracionLocal",objConfLocal);
//            nuevaActividad.putExtra("IdDocumentoActual",id);
//            Context.startActivity(nuevaActividad);
//        }
    }