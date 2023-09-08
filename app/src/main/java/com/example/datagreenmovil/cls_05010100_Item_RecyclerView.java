package com.example.datagreenmovil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Entidades.Tareo;
import com.example.datagreenmovil.Entidades.TareoDetalle;

import java.util.ArrayList;
import java.util.List;

public class cls_05010100_Item_RecyclerView extends ArrayAdapter<TareoDetalle> {
    //private static final String TAG = "cls_05010100_Item_RecyclerView";
    private Context mContext;
    int mResource;
    int itemSeleccionado;
    ConexionSqlite objSqliteL;
    Tareo tareo;

    public cls_05010100_Item_RecyclerView(@NonNull Context context, int resource, @NonNull List<TareoDetalle> objects, ConexionSqlite pSqlite, Tareo tareo_) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        objSqliteL = pSqlite;
        tareo = tareo_;
    }
/*
    public String obtenerItem(int posicion){

    }
*/
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int itemTareo = getItem(position).getItem();
        String dni = getItem(position).getDni();
        String nombres = getItem(position).getNombres();
        String idCultivo = getItem(position).getIdCultivo();
        String cultivo = getItem(position).getCultivo();
        String idVariedad = getItem(position).getIdVariedad();
        String variedad = getItem(position).getVariedad();
        String idActividad = getItem(position).getIdActividad();
        String actividad = getItem(position).getActividad();
        String idLabor = getItem(position).getIdLabor();
        String labor = getItem(position).getLabor();
        String idConsumidor = getItem(position).getIdConsumidor();
        String consumidor = getItem(position).getConsumidor();
        //LocalDateTime inicio = getItem(position).getInicio();
        double horas = getItem(position).getHoras();
        double rdtos = getItem(position).getRdtos();

        //TareoDetalle item = new TareoDetalle();

        LayoutInflater inflador = LayoutInflater.from(mContext);
        convertView = inflador.inflate(mResource,parent,false);

        LinearLayout c010_lly_Principal = (LinearLayout) convertView.findViewById(R.id.c010_lly_Principal_v);
        LinearLayout c010_lly_CultivoVariedad = (LinearLayout) convertView.findViewById(R.id.c010_lly_CultivoVariedad_v);

        TextView txvAVL_ItemTareo = (TextView) convertView.findViewById(R.id.c010_txv_Item_v);
        TextView txvAVL_Dni = (TextView) convertView.findViewById(R.id.c010_txv_Dni_v);
        TextView txvAVL_Nombres = (TextView) convertView.findViewById(R.id.c010_txv_Nombres_v);
        TextView txvAVL_Cultivo = (TextView) convertView.findViewById(R.id.c010_txv_IdCultivo_v);
        TextView txvAVL_Variedad = (TextView) convertView.findViewById(R.id.c010_txv_IdVariedad_v);
        TextView txvAVL_Consumidor = (TextView) convertView.findViewById(R.id.c010_txv_IdConsumidor_v);
        TextView txvAVL_Actividad = (TextView) convertView.findViewById(R.id.c010_txv_IdActividad_v);
        TextView txvAVL_Labor = (TextView) convertView.findViewById(R.id.c010_txv_IdLabor_v);
        TextView txvAVL_Horas = (TextView) convertView.findViewById(R.id.c010_txv_Horas_v);
        TextView txvAVL_Rdtos = (TextView) convertView.findViewById(R.id.c010_txv_Rdtos_v);

        txvAVL_ItemTareo.setText(itemTareo);
        txvAVL_Dni.setText(dni);
        txvAVL_Nombres.setText(nombres);
        txvAVL_Consumidor.setText(idConsumidor.trim());
        txvAVL_Cultivo.setText(idCultivo + " | "+ cultivo);
        txvAVL_Variedad.setText(idVariedad);
        txvAVL_Actividad.setText(idActividad);
        txvAVL_Labor.setText(idLabor);
        txvAVL_Horas.setText(Double.toString(horas));
        txvAVL_Rdtos.setText(Double.toString(rdtos));

        c010_lly_CultivoVariedad.setVisibility(View.GONE);

        c010_lly_Principal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemSeleccionado = Integer.parseInt(txvAVL_ItemTareo.getText().toString());
                //Toast.makeText(view.getContext(), "MAIN LAYOUT: " + txvAVL_ItemTareo.getText(), Toast.LENGTH_SHORT).show();
                mostrarMenuUsuario(txvAVL_Actividad);
            }
            @SuppressLint("ViewHolder")
            public void mostrarMenuUsuario(View v) {
                PopupMenu popup = new PopupMenu(mContext, v);
                popup.setOnMenuItemClickListener(this::onMenuItemClick);
                popup.inflate(R.menu.mnu_05010101_opciones_detalle);
                popup.show();
            }
            public boolean onMenuItemClick(MenuItem item) {
                try{
                    switch (item.getItemId()) {
                        case R.id.opc_05010101_EliminarDetalle_v:
                            List<String> p = new ArrayList<>();
                            p.add(tareo.getIdEmpresa());
                            p.add(tareo.getId());
                            p.add(String.valueOf(itemSeleccionado));
                            objSqliteL.doItBaby(objSqliteL.obtQuery("ELIMINAR trx_Tareos_Detalle"),p,"WRITE");
                            p.remove(2);
                            objSqliteL.doItBaby(objSqliteL.obtQuery("ACTUALIZAR ITEM trx_Tareos_Detalle"),p,"WRITE");

                            //tareo.getDetalle().remove(tareo.getDetalle());
                            tareo.eliminarItemDetalle(itemSeleccionado);
                            //Toast.makeText(mContext, "tareo elimiado: " + itemSeleccionado, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            return false;
                    }
                }catch(Exception ex){
                    StackTraceElement z = new Exception().getStackTrace()[0];
                    String detalleError = z.getFileName() + "\n" + z.getMethodName() + "\n" + z.getLineNumber() + ": \n" + ex.getMessage();
                    ex.printStackTrace();
                    Toast.makeText(cls_05010100_Item_RecyclerView.super.getContext(),detalleError, Toast.LENGTH_LONG).show();
                    return false;
                }
                return false;
            }
        });

        return convertView;

    }
}
