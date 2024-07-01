package com.example.datagreenmovil.ui.CosechaSupervisorMain.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.datagreenmovil.CosechaActivity;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;

public class CosechaSupervisorDetailsAdapter extends RecyclerView.Adapter<CosechaSupervisorDetailsAdapter.ViewHolder> {

    private JSONArray mData;
    private Context ctx;
    private int cantidadPerItem;
    private CosechaActivity activity;

    public CosechaSupervisorDetailsAdapter(Context ctx, JSONArray data, CosechaActivity activity) {
        this.mData = data;
        this.ctx = ctx;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cosecha_supervisor_detalle_bin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            JSONObject item = null;
        try {
            item = mData.getJSONObject(position);
            holder.tvDniTrabajador.setText(item.getString("dni"));
            holder.tvNombreTrabajador.setText(item.getString("nombreTrabajador"));
            holder.tvCantidadContenedorCosecha.setText(String.valueOf(item.getInt("cantidad")));
//            SETEAMOS LOS BUTTONS
            holder.btnRestarCapachon.setOnClickListener(view -> {
                AtomicInteger cantidad = new AtomicInteger(Integer.parseInt(holder.tvCantidadContenedorCosecha.getText().toString()));
                if(cantidad.get() > 0){
                    Swal.confirm(ctx, "VERIFIQUE!","SEGURO QUE DESEA QUITAR UN CONTENEDOR MAS A ESTE TRABAJADOR?").setConfirmClickListener(sweetAlertDialog -> {

                        cantidad.addAndGet(-1);
                        cantidadPerItem = cantidad.get();
                        if (activity != null) {
                            try {
                                activity.setQuantityTrabajador(cantidad.get(), position);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        holder.tvCantidadContenedorCosecha.setText(String.valueOf(cantidad.get()));
                        sweetAlertDialog.dismissWithAnimation();
                    }).setCancelClickListener(sweetAlertDialog -> {
                        sweetAlertDialog.dismissWithAnimation();
                    });
                }else {
                    Swal.confirm(ctx, "VERIFIQUE!","SEGURO QUE DESEA ELIMINAR AL TRABAJADOR DE LA LISTA?").setConfirmClickListener(sweetAlertDialog -> {
                        mData.remove(position);
                        cantidadPerItem = 0;
                        holder.tvCantidadContenedorCosecha.setText("0");
                        sweetAlertDialog.dismissWithAnimation();
                    }).setCancelClickListener(sweetAlertDialog -> {
                        sweetAlertDialog.dismissWithAnimation();
                    });
                }
            });

            JSONObject finalItem = item;
            holder.btnSumarCapachon.setOnClickListener(view -> {
                Swal.confirm(ctx, "VERIFIQUE!","SEGURO QUE DESEA AGREGAR UN CONTENEDOR MAS A ESTE TRABAJADOR?").setConfirmClickListener(sweetAlertDialog -> {
                    int cantidad = Integer.parseInt(holder.tvCantidadContenedorCosecha.getText().toString());
                    cantidad += 1;
                    if (activity != null) {
                        try {
                            activity.setQuantityTrabajador(cantidad, position);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    cantidadPerItem = cantidad;
                    try {
                        finalItem.put("cantidad", cantidad);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    holder.tvCantidadContenedorCosecha.setText(String.valueOf(cantidad));
                    sweetAlertDialog.dismissWithAnimation();
                }).setCancelClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                });
            });

            item.put("cantidad", 2);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return mData.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreTrabajador, tvDniTrabajador, tvCantidadContenedorCosecha;
        FloatingActionButton btnRestarCapachon, btnSumarCapachon;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNombreTrabajador = itemView.findViewById(R.id.tvNombreTrabajador);
            tvDniTrabajador = itemView.findViewById(R.id.tvDniTrabajador);
            tvCantidadContenedorCosecha = itemView.findViewById(R.id.tvCantidadContenedorCosecha);
            btnRestarCapachon = itemView.findViewById(R.id.btnRestarCapachon);
            btnSumarCapachon = itemView.findViewById(R.id.btnSumarCapachon);
        }
    }
}
