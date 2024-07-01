package com.example.datagreenmovil.ui.CosechaSupervisorMain;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.datagreenmovil.CosechaActivity;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.databinding.FragmentCosechaSupervisorMainBinding;
import com.example.datagreenmovil.ui.CosechaSupervisorMain.Adapters.CosechaSupervisorDetailsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CosechaSupervisorMainFragment extends Fragment {
    private FragmentCosechaSupervisorMainBinding binding;
    private Context ctx;
    private JSONObject jsonBIN = new JSONObject();
    private JSONArray jsonDetallesBin = new JSONArray();
    private SQLiteDatabase database;
    String dniSeleccionado, nombreSeleccionado;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){

        binding = FragmentCosechaSupervisorMainBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        database = SQLiteDatabase.openDatabase(ctx.getDatabasePath("DataGreenMovil.db").toString(), null, SQLiteDatabase.OPEN_READWRITE);

        try {
            jsonBIN.put("id", binding.txtId.getText().toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        binding.txtTrabajador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String textoBusqueda = binding.txtTrabajador.getText().toString();
                String[] selectionArgs = {textoBusqueda, textoBusqueda, textoBusqueda};
                Cursor c = database.rawQuery("SELECT NroDocumento, NOMBRES || ' ' || PATERNO || ' ' || MATERNO trabajador FROM MST_PERSONAS WHERE NOMBRES || ' ' || PATERNO || ' ' || MATERNO LIKE '%'||?||'%' OR NRODOCUMENTO LIKE '%'||?||'%' OR IDCODIGOGENERAL LIKE '%'||?||'%'",selectionArgs);
                List<String> trabajadores = new ArrayList<>();

                while (c.moveToNext()) {
                    String clave = c.getString(0); // Supongamos que la columna 0 es la clave
                    String valor = c.getString(1); // Supongamos que la columna 1 es el valor
                    trabajadores.add(valor); // Añadir la clave a la lista
                    if(binding.txtTrabajador.isPopupShowing()){
                        dniSeleccionado = clave;
                        nombreSeleccionado = valor;
                    }
                }

                ArrayAdapter<String> adaptador = new ArrayAdapter<>(ctx, android.R.layout.simple_list_item_1, trabajadores);
                // Configura el adaptador en tu AutoCompleteTextView
                binding.txtTrabajador.setAdapter(adaptador);
            }
        });

        binding.txtTrabajador.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("TRABAJADOR 0", dniSeleccionado);
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(ctx, 1);
        binding.listDetalleBinSupervisor.setLayoutManager(layoutManager);

        try {
            setListDetalles();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        binding.btnAgregarTrabajador.setOnClickListener(view -> {
            if(binding.txtTrabajador.getText().toString().length() > 0){
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("dni", dniSeleccionado);
                    jsonObject.put("cantidad", 0);
                    jsonObject.put("nombreTrabajador", nombreSeleccionado);
                    jsonDetallesBin.put(jsonObject);
                    binding.txtTrabajador.setText("");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                try {
                    setListDetalles();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }else{
                Swal.info(ctx, "AEA","escribe algo ps men",2000);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            setListDetalles();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void setListDetalles() throws JSONException {
        jsonBIN.put("detalles", jsonDetallesBin);
        CosechaActivity cosechaActivity = (CosechaActivity) getActivity();

        if (cosechaActivity != null) {
            cosechaActivity.setTrabajadores(jsonBIN.toString());
        }

        CosechaSupervisorDetailsAdapter adapter = new CosechaSupervisorDetailsAdapter(ctx, jsonDetallesBin, (CosechaActivity) getActivity());
        binding.listDetalleBinSupervisor.setAdapter(adapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ctx = context;
    }
}
