package com.example.datagreenmovil.DAO.Estandares.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.datagreenmovil.Conexiones.AppDatabase;
import com.example.datagreenmovil.DAO.Estandares.Operario.Operario;
//import com.example.datagreenmovil.DAO.Estandares.Operarios.Operario;
import com.example.datagreenmovil.R;

import java.util.List;

public class OperarioTallerAdapter extends BaseAdapter implements SpinnerAdapter {

    private AppDatabase database;
    private List<Operario> data;

    public OperarioTallerAdapter() {
        database = AppDatabase.getDatabase();

        this.data = database.operarioDAO().obtenerOperario();
    }

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
        return Long.parseLong(data.get(i).getIdOperario());
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        // Si la vista no ha sido creada, la inflamos
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.item_operario, parent, false);
        }

        // Obtenemos el item en la posición actual
        Operario operario = data.get(i);

        // Referenciamos los elementos del layout
        TextView txtIdOperario = convertView.findViewById(R.id.txtIdOperario);
        TextView txtNombreOperario = convertView.findViewById(R.id.txtNombreOperario);

        // Asignamos los valores a los TextViews
        txtIdOperario.setText(operario.getIdOperario());
        txtNombreOperario.setText(operario.getNombreCompleto());

        return convertView;
    }
}
