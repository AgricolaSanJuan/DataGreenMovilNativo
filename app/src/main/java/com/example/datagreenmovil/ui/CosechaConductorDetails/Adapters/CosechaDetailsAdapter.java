package com.example.datagreenmovil.ui.CosechaConductorDetails.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.datagreenmovil.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CosechaDetailsAdapter extends RecyclerView.Adapter<CosechaDetailsAdapter.ViewHolder> {

    private JSONArray mData;

    public CosechaDetailsAdapter(JSONArray data) {
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cosecha_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JSONObject item = null;
        try {
            item = mData.getJSONObject(position);
            holder.tvNroDetalle.setText(String.valueOf(position + 1));
            holder.tvTipo.setText(item.getString("tipo"));
            holder.tvCantidad.setText(item.getString("cantidad"));
            holder.tvPersonas.setText(item.getString("personas"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return mData.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNroDetalle, tvTipo, tvCantidad, tvPersonas, tvFecha, tvHora;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNroDetalle = itemView.findViewById(R.id.tvNroDetalle);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvPersonas = itemView.findViewById(R.id.tvPersonas);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvHora = itemView.findViewById(R.id.tvHora);
        }
    }
}
