package com.example.datagreenmovil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

//ESTE ES EL ADAPTADOR QUE SE USA REALMENTE, EL OTRO DEBERIA ELIMINARSE
public class cls_05010200_RecyclerViewAdapter extends RecyclerView.Adapter<cls_05010200_RecyclerViewAdapter.MyViewHolder> {
    android.content.Context mContext;
    SharedPreferences sharedPreferences;
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
        sharedPreferences = mContext.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
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

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull cls_05010200_RecyclerViewAdapter.MyViewHolder holder, int position) {
        String s ="";
        TareoDetalle td = tareo.getDetalle().get(position);
        holder.c010_txv_Item.setText(String.valueOf(td.getItem()));
        holder.c010_txv_Dni.setText(td.getDni());
        holder.c010_txv_Nombres.setText(td.getNombres());
        s = String.format("(%s)",td.getIdCultivo().trim());
        holder.c010_txv_IdCultivo.setText(s);
        s = String.format("%s",td.getCultivo().trim());
        holder.c010_txv_Cultivo.setText(s);
        s = String.format("(%s)",td.getIdVariedad().trim());
        holder.c010_txv_IdVariedad.setText(s);
        s = String.format("%s",td.getVariedad().trim());
        holder.c010_txv_Variedad.setText(s);
        s = String.format("(%s)",td.getIdActividad().trim());
        holder.c010_txv_IdActividad.setText(s);
        s = String.format("%s",td.getActividad().trim());
        holder.c010_txv_Actividad.setText(s);
        s = String.format("(%s)",td.getIdLabor().trim());
        holder.c010_txv_IdLabor.setText(s);
        s = String.format("%s",td.getLabor().trim());
        holder.c010_txv_Labor.setText(s);
        s = String.format("(%s)",td.getIdConsumidor().trim());
        holder.c010_txv_IdConsumidor.setText(s);
        s = String.format("%s",td.getConsumidor().trim());
        holder.c010_txv_Consumidor.setText(s);
        Log.i("HORASENTIDAD", String.valueOf(td.getHoras()));
        holder.c010_txv_Horas.setText(String.valueOf(td.getHoras()));
        holder.c010_txv_Rdtos.setText(String.valueOf(td.getRdtos()));
        holder.c010_txv_Observacion.setText(td.getObservacion());
        holder.c010_lly_CultivoVariedad.setVisibility(View.GONE);
        holder.tvIngreso.setText(td.getIngreso());
        holder.tvSalida.setText(td.getSalida());

//        DEFINIMOS SI ESTAMOS EN MODO PACKING O NO
        boolean modoPacking = sharedPreferences.getBoolean("MODO_PACKING", false);
        // Definir los valores para el Spinner
        Double[] values = {0.0, 0.5, 1.0};

        // Crear el ArrayAdapter usando un layout simple de Spinner
        ArrayAdapter<Double> adapter = new ArrayAdapter<>(mContext, R.layout.simple_list_item_custom, values);

        // Especificar el layout a usar cuando se despliegan las opciones
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Asignar el adaptador al Spinner
        holder.spinnerAlmuerzo.setAdapter(adapter);

        if(!!modoPacking) {
            AtomicBoolean openByUser = new AtomicBoolean(true);

            holder.spinnerAlmuerzo.setOnTouchListener((view, motionEvent) -> {
                openByUser.set(true);
                return false;
            });

            holder.spinnerAlmuerzo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    double selectedValue = Double.parseDouble(holder.spinnerAlmuerzo.getItemAtPosition(i).toString());

                    if (!td.getIngreso().equals("") && !td.getSalida().equals("") && openByUser.get()) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            // Convertir las cadenas a objetos Date
                            Date date1 = sdf.parse(td.getIngreso());
                            Date date2 = sdf.parse(td.getSalida());

                            Date dateComparar1 = sdf.parse(td.getIngreso());
                            Date dateComparar2 = sdf.parse(td.getSalida());



                            // Calcular la diferencia en milisegundos
                            long diferenciaMilisegundos = date2.getTime() - date1.getTime();
                            long diferenciaMilisegundosComparar = dateComparar2.getTime() - dateComparar1.getTime();

                            // Convertir la diferencia de milisegundos a horas
                            long diferenciaHoras = TimeUnit.MILLISECONDS.toHours(diferenciaMilisegundos);
                            double horas = diferenciaMilisegundos / 3600000.00;
                            double horasComparar = diferenciaMilisegundosComparar / 3600000.00;
                            BigDecimal horasRedondeadas = new BigDecimal(horas).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal horasRedondeadasComparar = new BigDecimal(horasComparar).setScale(2, RoundingMode.HALF_UP);
                            double horasCalculadasComparar = horasRedondeadasComparar.doubleValue();
                            double horasCalculadas = horasRedondeadas.doubleValue() - selectedValue;
                            Log.i("horasCalculadasComparar", String.valueOf(horasCalculadasComparar));
                            Log.i("horasCalculadas", String.valueOf(horasCalculadasComparar));
                            if(horasCalculadas != td.getHoras()){
                                double diferencia = horasCalculadas - td.getHoras();
                                boolean cambiar = false;
                                int indexSelect = 0;
                                if(diferencia == 0.5){
                                    indexSelect = 1;
                                    cambiar = true;
                                } else if (diferencia == 1.0) {
                                    indexSelect = 2;
                                    cambiar = true;
                                }else if (diferencia == 0.0){
                                    indexSelect = 0;
                                    cambiar = true;
                                }
                                if(cambiar){
                                    holder.spinnerAlmuerzo.setSelection(indexSelect, true);
                                    selectedValue = Double.parseDouble(holder.spinnerAlmuerzo.getItemAtPosition(indexSelect).toString());
                                }
                                horasCalculadas = horasRedondeadas.doubleValue() - selectedValue;
                                td.setHoras(horasCalculadas);
                                holder.c010_txv_Horas.setText(String.valueOf(td.getHoras()));
                            }else{
                                selectedValue = Double.parseDouble(holder.spinnerAlmuerzo.getSelectedItem().toString());
                                horasCalculadas = horasRedondeadas.doubleValue() - selectedValue;
                                td.setHoras(horasCalculadas);
                                holder.c010_txv_Horas.setText(String.valueOf(td.getHoras()));
                                if (horasCalculadas < 0.00) {
                                    horasCalculadas = 0.00;
                                }
                                td.setHoras(horasCalculadas);
                                holder.c010_txv_Horas.setText(String.valueOf(td.getHoras()));
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }finally {
                            openByUser.set(false);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    double selectedValue = Double.parseDouble(holder.spinnerAlmuerzo.getSelectedItem().toString());
                }
            });
        }else {
            holder.spinnerAlmuerzo.setVisibility(View.GONE);
            holder.labelAlmuerzo.setVisibility(View.GONE);
        }

//        SETEAR LAS HORAS SI ENCUENTRA AMBAS
//        if(!td.getIngreso().equals("") && !td.getSalida().equals("")){
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            try {
//                // Convertir las cadenas a objetos Date
//                Date date1 = sdf.parse(td.getIngreso());
//                Date date2 = sdf.parse(td.getSalida());
//
//                // Calcular la diferencia en milisegundos
//                long diferenciaMilisegundos = date2.getTime() - date1.getTime();
//
//                // Convertir la diferencia de milisegundos a horas
//                long diferenciaHoras = TimeUnit.MILLISECONDS.toHours(diferenciaMilisegundos);
//
//                // Mostrar la diferencia de horas
//                holder.c010_txv_Horas.setText(String.valueOf(diferenciaHoras));
//                tareo.getDetalle().get(position).setHoras(diferenciaHoras);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }

//        SETEAR LAS HORAS SI ENCUENTRA AMBAS

        holder.c010_lly_Principal.setOnLongClickListener(view -> {
            holder.selected = !holder.selected;
            holder.imageCheck.setVisibility(holder.selected ? View.VISIBLE : View.INVISIBLE);
            return true;
        });

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
    public int getTotalDetalles(){
        int cantidad = tareo.getTotalDetalles();
        return cantidad;
    }

    public String getLastHoras() {
        int cantidad = tareo.getTotalDetalles();
        return String.valueOf(tareo.getDetalle().get(cantidad-1).getHoras());
    }

    public String getLastRendimientos() {
        int cantidad = tareo.getTotalDetalles();
        return String.valueOf(tareo.getDetalle().get(cantidad-1).getRdtos());
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {
        boolean selected = false;
        ImageView imageCheck;
        TextView c010_txv_Item, c010_txv_Dni, c010_txv_Nombres, c010_txv_IdCultivo, c010_txv_Cultivo,
                c010_txv_IdVariedad, c010_txv_Variedad, c010_txv_IdConsumidor, c010_txv_Consumidor,
                c010_txv_IdActividad, c010_txv_Actividad, c010_txv_IdLabor, c010_txv_Labor, c010_txv_Horas, c010_txv_Rdtos, c010_txv_Observacion, labelAlmuerzo;
        Spinner spinnerAlmuerzo;
        TextView tvIngreso, tvSalida;
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
            imageCheck = itemView.findViewById(R.id.imageCheck);
            tvIngreso = itemView.findViewById(R.id.tvIngreso);
            tvSalida = itemView.findViewById(R.id.tvSalida);
            labelAlmuerzo = itemView.findViewById(R.id.labelAlmuerzo);
            spinnerAlmuerzo = itemView.findViewById(R.id.spinnerAlmuerzo);
        }
    }
}
