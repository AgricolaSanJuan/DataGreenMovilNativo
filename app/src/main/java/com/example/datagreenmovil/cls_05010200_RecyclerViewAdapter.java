package com.example.datagreenmovil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.Tareo;
import com.example.datagreenmovil.Entidades.TareoDetalle;
import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.Logica.Swal;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//ESTE ES EL ADAPTADOR QUE SE USA REALMENTE, EL OTRO DEBERIA ELIMINARSE
public class cls_05010200_RecyclerViewAdapter extends RecyclerView.Adapter<cls_05010200_RecyclerViewAdapter.MyViewHolder> {
    android.content.Context mContext;
    Tareo tareo;
    ConfiguracionLocal objConfLocal;
    ConexionSqlite objSqlite;
    int itemSeleccionado=0;

    public cls_05010200_RecyclerViewAdapter(Context ct, ConfiguracionLocal objConfLocal_, ConexionSqlite pSqlite, Tareo tareo_){
        //super(objConfLocal_,ct,tareo_);
        //super();
        mContext = ct;
        tareo = tareo_;
        objConfLocal = objConfLocal_;
        objSqlite = pSqlite;
    }

    public Double obtenerTotalRendimientos() {
        Double suma = 0.00;

        for (TareoDetalle detalle : tareo.getDetalle()) {
            suma += detalle.getRdtos(); // Asume que hay un método getRdtos() en TareoDetalle
        }
        return suma;
    }

    public JSONObject obtenerUltimosDatos(){
        JSONObject ultimoRegistro = new JSONObject();
        TareoDetalle tareoDetalle = new TareoDetalle();
        return ultimoRegistro;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mContext);
        View view=inflater.inflate(R.layout.v_05010100_item_recyclerview_010,parent,false);
        return new MyViewHolder(view);
    }

    public String cantidadRendimientos(){
        return "hola";
    }

    @Override
    public void onBindViewHolder(@NonNull cls_05010200_RecyclerViewAdapter.MyViewHolder holder, int position) {
        //tareos.moveToPosition(position);
        String s ="";
        TareoDetalle td = tareo.getDetalle().get(position);
        holder.c010_txv_Item.setText(String.valueOf(td.getItem()));
        holder.c010_txv_Dni.setText(td.getDni());
        holder.c010_txv_Nombres.setText(td.getNombres());
        s=String.format("(%s)",td.getIdCultivo().trim());
        holder.c010_txv_IdCultivo.setText(s);
        s=String.format("%s",td.getCultivo().trim());
        holder.c010_txv_Cultivo.setText(s);
        s=String.format("(%s)",td.getIdVariedad().trim());
        holder.c010_txv_IdVariedad.setText(s);
        s=String.format("%s",td.getVariedad().trim());
        holder.c010_txv_Variedad.setText(s);
        s=String.format("(%s)",td.getIdActividad().trim());
        holder.c010_txv_IdActividad.setText(s);
        s=String.format("%s",td.getActividad().trim());
        holder.c010_txv_Actividad.setText(s);
        s=String.format("(%s)",td.getIdLabor().trim());
        holder.c010_txv_IdLabor.setText(s);
        s=String.format("%s",td.getLabor().trim());
        holder.c010_txv_Labor.setText(s);
        s=String.format("(%s)",td.getIdConsumidor().trim());
        holder.c010_txv_IdConsumidor.setText(s);
        s=String.format("%s",td.getConsumidor().trim());
        holder.c010_txv_Consumidor.setText(s);
        holder.c010_txv_Horas.setText(String.valueOf(td.getHoras()));
        holder.c010_txv_Rdtos.setText(String.valueOf(td.getRdtos()));
        holder.c010_txv_Observacion.setText(td.getObservacion());
        holder.c010_lly_CultivoVariedad.setVisibility(View.GONE);
        holder.c010_lly_Principal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemSeleccionado = Integer.parseInt(holder.c010_txv_Item.getText().toString());
                mostrarMenuDetalle(holder.c010_lly_Principal);
            }
            public void mostrarMenuDetalle(View v) {
                PopupMenu popup = new PopupMenu(mContext, v);
                popup.setOnMenuItemClickListener(this::onMenuItemClick);
                popup.inflate(R.menu.mnu_05010101_opciones_detalle);
                popup.show();
            }
            public boolean onMenuItemClick(MenuItem item) {
                try{
                    int idOpcionClickeada = item.getItemId();
                    if(idOpcionClickeada == R.id.opc_05010101_EliminarDetalle_v){
                        Swal.confirm(mContext, "Estás seguro?", "Una vez eliminado este detalle no se podrá recuperar").
                                setConfirmClickListener(sweetAlertDialog -> {
                                    if (mContext instanceof cls_05010000_Edicion) {
                                        ((cls_05010000_Edicion)mContext).eliminarDetalle(itemSeleccionado);
                                    }
                                    sweetAlertDialog.dismissWithAnimation();
                                }).setCancelClickListener(sweetAlertDialog -> {
                                    sweetAlertDialog.dismissWithAnimation();
                                });
                    } else if (idOpcionClickeada == R.id.opc_05010101_ActualizarDetalle_v) {
                        if (mContext instanceof cls_05010000_Edicion) {
                            ((cls_05010000_Edicion)mContext).popUpActualizarDetalleTareos(td);
                        }
                    }
                }catch(Exception ex){
                    Funciones.mostrarError(mContext,ex);
                    return false;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return tareo.getDetalle().size();
    }

    public String getLastActividad(){
        int cantidad = tareo.getTotalDetalles();
        return tareo.getDetalle().get(cantidad-1).getIdActividad();
    }
    public String getLastLabor(){
        int cantidad = tareo.getTotalDetalles();
        return tareo.getDetalle().get(cantidad-1).getIdLabor();
    }
    public String getLastConsumidor(){
        int cantidad = tareo.getTotalDetalles();
        return tareo.getDetalle().get(cantidad-1).getIdConsumidor();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView c010_txv_Item, c010_txv_Dni, c010_txv_Nombres, c010_txv_IdCultivo, c010_txv_Cultivo,
                c010_txv_IdVariedad, c010_txv_Variedad, c010_txv_IdConsumidor, c010_txv_Consumidor,
                c010_txv_IdActividad, c010_txv_Actividad, c010_txv_IdLabor, c010_txv_Labor, c010_txv_Horas, c010_txv_Rdtos, c010_txv_Observacion ;
        ConstraintLayout c010_lly_Principal;
        LinearLayout c010_lly_CultivoVariedad;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            c010_lly_Principal = (ConstraintLayout) itemView.findViewById(R.id.c010_lly_Principal_v);
            c010_txv_Item = (TextView) itemView.findViewById(R.id.c010_txv_Item_v);
            c010_txv_Dni = (TextView) itemView.findViewById(R.id.c010_txv_Dni_v);
            c010_txv_Nombres = (TextView) itemView.findViewById(R.id.c010_txv_Nombres_v);
            c010_txv_IdCultivo = (TextView) itemView.findViewById(R.id.c010_txv_IdCultivo_v);
            c010_txv_Cultivo = (TextView) itemView.findViewById(R.id.c010_txv_Cultivo_v);
            c010_txv_IdVariedad = (TextView) itemView.findViewById(R.id.c010_txv_IdVariedad_v);
            c010_txv_Variedad = (TextView) itemView.findViewById(R.id.c010_txv_Variedad_v);
            c010_txv_IdActividad = (TextView) itemView.findViewById(R.id.c010_txv_IdActividad_v);
            c010_txv_Actividad = (TextView) itemView.findViewById(R.id.c010_txv_Actividad_v);
            c010_txv_IdLabor = (TextView) itemView.findViewById(R.id.c010_txv_IdLabor_v);
            c010_txv_Labor = (TextView) itemView.findViewById(R.id.c010_txv_Labor_v);
            c010_txv_IdConsumidor = (TextView) itemView.findViewById(R.id.c010_txv_IdConsumidor_v);
            c010_txv_Consumidor = (TextView) itemView.findViewById(R.id.c010_txv_Consumidor_v);
            c010_txv_Horas = (TextView) itemView.findViewById(R.id.c010_txv_Horas_v);
            c010_txv_Rdtos = (TextView) itemView.findViewById(R.id.c010_txv_Rdtos_v);
            c010_lly_CultivoVariedad = (LinearLayout) itemView.findViewById(R.id.c010_lly_CultivoVariedad_v);
            c010_txv_Observacion = (TextView)  itemView.findViewById(R.id.c010_txv_Observacion_v);
        }
    }
}
