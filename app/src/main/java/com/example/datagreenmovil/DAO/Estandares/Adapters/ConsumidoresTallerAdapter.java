package com.example.datagreenmovil.DAO.Estandares.Adapters;

import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datagreenmovil.DAO.Estandares.ConsumidoresTaller.ConsumidoresTaller;
import com.example.datagreenmovil.R;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ConsumidoresTallerAdapter extends BaseAdapter implements SpinnerAdapter {
//public class ConsumidoresTallerAdapter extends RecyclerView.Adapter<ConsumidoresTallerAdapter.ViewHolder> {

    private List<ConsumidoresTaller> data;
    SimpleDateFormat dateFormat;

    public interface OnItemSelected{
        void setOnItemSelected(String id);
    }

    private OnItemSelected onItemSelected;

    public ConsumidoresTallerAdapter(List<ConsumidoresTaller> data) {
        this.data = data;
    }

//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consumidores_taller, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
////        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//        String IdConsumidor = data.get(position).getIdConsumidor();
//        String Descripcion = data.get(position).getDescripcion();
//
//        holder.IdConsumidor.setText(IdConsumidor);
//        holder.Descripcion.setText(Descripcion);
//
//
//
//
//    }

//    @Override
//    public int getItemCount() {
//        return data.size();
//    }

    @Override
    public int getCount() {
        return data.size();

    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Long.parseLong("013");
//        return Long.parseLong(data.get(i).getIdConsumidor());
    }



//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView IdConsumidor, Descripcion;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            IdConsumidor = itemView.findViewById(R.id.txtIdConsumidor);
//            Descripcion = itemView.findViewById(R.id.txtDescripcion);
//        }
//    }


@Override
public View getView(int i, View convertView, ViewGroup parent) {
    // Si la vista no ha sido creada, la inflamos
    if (convertView == null) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        convertView = inflater.inflate(R.layout.item_consumidores_taller, parent, false);
    }

    // Obtenemos el item en la posición actual
    ConsumidoresTaller consumidor = data.get(i);

    // Referenciamos los elementos del layout
    TextView txtIdConsumidor = convertView.findViewById(R.id.txtIdConsumidor);
    TextView txtDescripcion = convertView.findViewById(R.id.txtDescripcion);

    // Asignamos los valores a los TextViews
    txtIdConsumidor.setText(consumidor.getIdConsumidor());
    txtDescripcion.setText(consumidor.getDescripcion());

    return convertView;
}


}
