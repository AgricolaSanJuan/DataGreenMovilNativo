package com.example.datagreenmovil.ui.TallerMain.Maquinaria.RecyclerView.Class;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.Rex;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.cls_08010000_Edicion;
import com.example.datagreenmovil.cls_08020000_AgregarDni;

import java.util.ArrayList;

public class tallerMaquinariaDetalle_RecyclerViewAdapter extends RecyclerView.Adapter<tallerMaquinariaDetalle_RecyclerViewAdapter.MyViewHolder> {

  public interface OnItemClickListener {
    void onItemClick(TextView idServicio, String accion);
  }
  private OnItemClickListener listener;
  // Método para establecer el listener
  public void setOnItemClickListener(OnItemClickListener listener) {
    this.listener = listener;
  }

  //String idTareoActual;
  Context contextoLocal;
  Cursor servicioTransporte;
  ConfiguracionLocal objConfLocal;
  ArrayList<String> idsSelecciones = new ArrayList<>();

  Rex objRex = null;

  public tallerMaquinariaDetalle_RecyclerViewAdapter(Context ct, Cursor r, ConfiguracionLocal cl, ArrayList<String> lista) {//, int[] img, String[] nm){
    //PENDIENTE: EN LUGAR DE PASAR UNA LISTA DE MODULOS. PASAR UN ARRAY DE TAREOS O LISTA O ALGO (CURSOR POR EJEMPLO)
    contextoLocal = ct;
    servicioTransporte = r;
    objConfLocal = cl;
    idsSelecciones = lista;
    //idTareoActual=idTareoActual_;
  }

  @NonNull
  @Override
  public tallerMaquinariaDetalle_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(contextoLocal);
    View view = inflater.inflate(R.layout.v_08000100_item_recyclerview_023, parent, false);
    return new tallerMaquinariaDetalle_RecyclerViewAdapter.MyViewHolder(view);
  }
    /*
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        }
    */


  @SuppressLint("Range")
  @Override
  public void onBindViewHolder(@NonNull tallerMaquinariaDetalle_RecyclerViewAdapter.MyViewHolder holder, int position) {
    try {
      servicioTransporte.moveToPosition(position);
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
      holder.c023_txv_Id.setText(servicioTransporte.getString(servicioTransporte.getColumnIndex("Id")));
      holder.c023_txv_Fecha.setText(servicioTransporte.getString(servicioTransporte.getColumnIndex("Fecha")));
      holder.c023_txv_IdEstado.setText(servicioTransporte.getString(servicioTransporte.getColumnIndex("IdEstado")));
      holder.c023_txv_IdUsuario.setText(servicioTransporte.getString(servicioTransporte.getColumnIndex("IdUsuarioCrea")));
      holder.c023_txv_Ruc.setText(servicioTransporte.getString(servicioTransporte.getColumnIndex("IdProveedor")));
      holder.c023_txv_RazonSocial.setText(servicioTransporte.getString(servicioTransporte.getColumnIndex("Proveedor")));
      holder.c023_txv_Placa.setText(servicioTransporte.getString(servicioTransporte.getColumnIndex("IdVehiculo")));
      holder.c023_txv_Pasajeros.setText(servicioTransporte.getString(servicioTransporte.getColumnIndex("Pasajeros")));
      holder.c023_txv_Capacidad.setText(servicioTransporte.getString(servicioTransporte.getColumnIndex("Capacidad")));
      holder.c023_txv_IdRuta.setText(servicioTransporte.getString(servicioTransporte.getColumnIndex("IdRuta")));
      holder.c023_txv_Ruta.setText(servicioTransporte.getString(servicioTransporte.getColumnIndex("Ruta")));
      holder.c023_txv_NroBrevete.setText(servicioTransporte.getString(servicioTransporte.getColumnIndex("IdConductor")));
      holder.c023_txv_Conductor.setText(servicioTransporte.getString(servicioTransporte.getColumnIndex("Conductor")));
      holder.c023_txv_Observacion.setText(servicioTransporte.getString(servicioTransporte.getColumnIndex("Observacion")));
      if(holder.c023_txv_IdEstado.getText().toString().equals("TR")){
        holder.c023_lly_Buttons.setVisibility(View.GONE);
      }
      if(holder.c023_txv_Pasajeros.getText().toString().equals("0")){
        holder.btnTransferirRegistro.setVisibility(View.GONE);
      }

//            holder.mainLayout.setBackgroundColor(ContextCompat.getColor(Context, holder.txv_IdEstado.getText().equals("PE") ? R.color.alerta : R.color.verdeClaro));
      holder.c023_cly_Principal.setBackground(ResourcesCompat.getDrawable(contextoLocal.getResources(), holder.c023_txv_IdEstado.getText().equals("PE") ? R.drawable.bg_item_recyclerview_alerta : R.drawable.bg_item_recyclerview_normal, null));


      holder.c023_cly_Principal.setOnClickListener(new View.OnClickListener() {
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
          servicioTransporte.moveToPosition(holder.getAdapterPosition());
//                    Toast.makeText(view.getContext(),tareos.getString(tareos.getColumnIndex("Id")),Toast.LENGTH_LONG).show();
          abrirDocumento(servicioTransporte.getString(servicioTransporte.getColumnIndex("Id")));
        }
      });

      holder.btnAddPasajero2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent nuevaActividad = new Intent(contextoLocal, cls_08020000_AgregarDni.class);
          nuevaActividad.putExtra("ConfiguracionLocal", objConfLocal);
          nuevaActividad.putExtra("IdRegistro", holder.c023_txv_Id.getText().toString());
          contextoLocal.startActivity(nuevaActividad);

//          Toast.makeText(contextoLocal, , Toast.LENGTH_SHORT).show();
        }
      });
      holder.btnTransferirRegistro.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//          if (listener != null) {
            listener.onItemClick(holder.c023_txv_Id, "transferir");
//          }
        }
      });
      holder.btnEliminar.setOnClickListener(view -> {
//        if (listener != null) {
          listener.onItemClick(holder.c023_txv_Id, "eliminar");
//        }
      });
//      holder.c023_cbx_Seleccion.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//          if (listener != null) {
//            listener.onItemClick(holder.c023_cbx_Seleccion, holder.c023_txv_Id);
//          }
//        }
////        @Override
////        public void onClick(View view) {
////                    /*
////                    tareos.moveToPosition(holder.getAdapterPosition());
////                    String moduloSeleccionado = ; //ModulosPermitidos[position];
////                    switch (moduloSeleccionado){
////                        case "Tareos":
////                            Intent intent = new Intent(Context, cls_05010000_Edicion.class);
////                            //CON PUTEXTRAS SE PUEDEN AGREGAR PARAMETROS AQUI PARA PASARLOS A LA ACTIVIDAD QUE SE VA A ABRRIR
////
////                            //intent.putExtra("ConfiguracionLocal",objConfLocal);
////
////                            Context.startActivity(intent);
////                        default:
////                            //return getResources().getStringArray(R.array.DEFAULT);
////                    }*/
//////          Toast.makeText(view.getContext(), objRex.Get("Id"), Toast.LENGTH_SHORT).show();
////          String idSeleccionado = holder.c023_txv_Id.getText().toString();
////          Log.i("ID",idSeleccionado);
////          if (holder.c023_cbx_Seleccion.isChecked() && !idsSelecciones.contains(idSeleccionado)) {
////            idsSelecciones.add(idSeleccionado);
////          } else if (!holder.c023_cbx_Seleccion.isChecked() && idsSelecciones.contains(idSeleccionado)) {
////            idsSelecciones.remove(idSeleccionado);
////          }
////        }
//      });
    } catch (Exception ex) {
      throw ex;
    }
  }

  @Override
  public int getItemCount() {
    return servicioTransporte.getCount();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView c023_txv_Id,
        c023_txv_Fecha,
        c023_txv_IdEstado,
        c023_txv_IdUsuario,
        c023_txv_Ruc,
        c023_txv_RazonSocial,
        c023_txv_Placa,
        c023_txv_Pasajeros,
        c023_txv_Capacidad,
        c023_txv_IdRuta,
        c023_txv_Ruta,
        c023_txv_NroBrevete,
        c023_txv_Conductor,
        c023_txv_Observacion;
    CheckBox c023_cbx_Seleccion;
    Button btnAddPasajero2, btnTransferirRegistro, btnEliminar;
    ConstraintLayout c023_cly_Principal;
    LinearLayout c023_lly_Buttons;

    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
      c023_txv_Id = itemView.findViewById(R.id.c023_txv_Id_v);
      c023_txv_Fecha = itemView.findViewById(R.id.c023_txv_Fecha_v);
      c023_txv_IdEstado = itemView.findViewById(R.id.c023_txv_IdEstado_v);
      c023_txv_IdUsuario = itemView.findViewById(R.id.c023_txv_IdUsuario_v);
      c023_txv_Ruc = itemView.findViewById(R.id.c023_txv_Ruc_v);
      c023_txv_RazonSocial = itemView.findViewById(R.id.c023_txv_RazonSocial_v);
      c023_txv_Placa = itemView.findViewById(R.id.c023_txv_Placa_v);
      c023_txv_Pasajeros = itemView.findViewById(R.id.c023_txv_Pasajeros_v);
      c023_txv_Capacidad = itemView.findViewById(R.id.c023_txv_Capacidad_v);
      c023_txv_IdRuta = itemView.findViewById(R.id.c023_txv_IdRuta_v);
      c023_txv_Ruta = itemView.findViewById(R.id.c023_txv_Ruta_v);
      c023_txv_NroBrevete = itemView.findViewById(R.id.c023_txv_NroBrevete_v);
      c023_txv_Conductor = itemView.findViewById(R.id.c023_txv_Conductor_v);
      c023_txv_Observacion = itemView.findViewById(R.id.c023_txv_Observacion_v);
      c023_cbx_Seleccion = itemView.findViewById(R.id.c023_cbx_Seleccionado_v);
      c023_cly_Principal = itemView.findViewById(R.id.c023_cly_Principal_v);
      btnAddPasajero2 = itemView.findViewById(R.id.btnAddPasajeros);
      btnTransferirRegistro = itemView.findViewById(R.id.c023_btn_transferir_v);
      btnEliminar = itemView.findViewById(R.id.c023_btn_eliminar_v);
      c023_lly_Buttons = itemView.findViewById(R.id.c023_lly_buttons);
    }
  }

  private void abrirDocumento(String id) {
    Intent nuevaActividad = new Intent(contextoLocal, cls_08010000_Edicion.class);
    nuevaActividad.putExtra("ConfiguracionLocal", objConfLocal);
    nuevaActividad.putExtra("s_IdRex", id);
    contextoLocal.startActivity(nuevaActividad);
  }
}
