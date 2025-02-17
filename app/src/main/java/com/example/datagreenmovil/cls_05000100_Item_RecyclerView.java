package com.example.datagreenmovil;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.datagreenmovil.Conexiones.AppDatabase;
import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.DAO.Tareo.TrxTareo.TareoDAO;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.Logica.Swal;

import java.util.ArrayList;
import java.util.List;

public class cls_05000100_Item_RecyclerView extends RecyclerView.Adapter<cls_05000100_Item_RecyclerView.MyViewHolder> {

    //String idTareoActual;
    Context Context;
    Cursor tareos;
    ConfiguracionLocal objConfLocal;
    ArrayList<String> tareosSeleccionados = new ArrayList<>();
    private cls_05000100_Item_RecyclerView.OnItemClickListener listener;


    public cls_05000100_Item_RecyclerView(Context ct, Cursor t, ConfiguracionLocal cl, ArrayList<String> lista) {//, int[] img, String[] nm){
        //PENDIENTE: EN LUGAR DE PASAR UNA LISTA DE MODULOS. PASAR UN ARRAY DE TAREOS O LISTA O ALGO (CURSOR POR EJEMPLO)
        Context = ct;
        tareos = t;
        objConfLocal = cl;
        tareosSeleccionados = lista;
    }

    // Método para establecer el listener
    public void setOnItemClickListener(cls_05000100_Item_RecyclerView.OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(Context);
        View view = inflater.inflate(R.layout.v_05000100_item_recyclerview_009, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull cls_05000100_Item_RecyclerView.MyViewHolder holder, int position) {
        try {
            tareos.moveToPosition(position);

//PENDIENTE: ACTIVAR ESTAS FUNCIONES DE BUSQUEDA POR NOMBRE DE COLUMNA EN LUGAR DE INDEX DE COLUMNA
            holder.txv_IdTareo.setText(tareos.getString(tareos.getColumnIndex("Id")));
            holder.txv_Fecha.setText(tareos.getString(tareos.getColumnIndex("Fecha")));
            holder.txv_IdEstado.setText(tareos.getString(tareos.getColumnIndex("IdEstado")));
            holder.txv_IdTurno.setText(tareos.getString(tareos.getColumnIndex("IdTurno")));
            holder.txv_TotalDetalles.setText(tareos.getString(tareos.getColumnIndex("TotalDetalles")));
            holder.txv_TotalHoras.setText(tareos.getString(tareos.getColumnIndex("TotalHoras")));
            holder.txv_TotalRdtos.setText(tareos.getString(tareos.getColumnIndex("TotalRendimientos")));
            holder.c009_txv_TotalJornales.setText(tareos.getString(tareos.getColumnIndex("TotalJornales")));


            holder.txv_IdUsuario.setText(tareos.getString(tareos.getColumnIndex("NombreUsuario")));
            String cantidad_trabajadores = tareos.getString(tareos.getColumnIndex("CantidadTrabajadores"));
            holder.txv_CantidadTrabajadores.setText(cantidad_trabajadores != null ? cantidad_trabajadores : "0");
            holder.txv_Observaciones.setText(tareos.getString(tareos.getColumnIndex("Observaciones")));

            //holder.mainLayout.setBackgroundColor(ContextCompat.getColor(Context, holder.txv_IdEstado.getText().equals("PE") ? R.color.alerta : R.color.verdeClaro));
            holder.mainLayout.setBackground(ResourcesCompat.getDrawable(Context.getResources(), holder.txv_IdEstado.getText().equals("PE")
                    ? R.drawable.bg_alerta_suave
                    : R.drawable.bg_transferido, null));

            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tareos.moveToPosition(holder.getAdapterPosition());
                    abrirDocumento(holder.txv_IdTareo.getText().toString());
//                    abrirDocumento(tareos.getString(tareos.getColumnIndex("Id")));
                }
            });

            holder.mainLayout.setOnTouchListener(new View.OnTouchListener() {
                private Handler handler = new Handler(Looper.getMainLooper());
                private Handler handlerSeleccion = new Handler(Looper.getMainLooper());
                private boolean isLongPressed = false;
                private long pressStartTime;
                private ValueAnimator shake; // Declaramos la animación

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Crear la animación de sacudida (solo si no está creada)
                    if (shake == null) {
                        shake = ValueAnimator.ofFloat(0, 20, -20, 0);
                        shake.setDuration(2000);  // Ajusta la duración como lo necesites
                        shake.setRepeatCount(ValueAnimator.INFINITE);
                        shake.setRepeatMode(ValueAnimator.REVERSE); // Volver al punto inicial después de cada ciclo
                        shake.addUpdateListener(animation -> {
                            float value = (float) animation.getAnimatedValue();
                            v.setTranslationX(value); // Aplicar la animación al `View`
                        });
                        shake.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                // Regresar el layout a su posición original cuando termine la animación
                                v.setTranslationX(0); // Asegurarse de que vuelva al valor inicial (0)
                            }
                        });
                    }

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            pressStartTime = System.currentTimeMillis();
                            handlerSeleccion.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (System.currentTimeMillis() - pressStartTime >= 500) {
                                        isLongPressed = true;
                                        boolean transferidoSeleccionado = holder.txv_IdEstado.getText().equals("TR");
                                        if (listener != null) {
                                            holder.cbx_Seleccionado.setChecked(!holder.cbx_Seleccionado.isChecked());
                                            holder.mainLayout.setBackground(ResourcesCompat.getDrawable(Context.getResources(),
                                                    !holder.cbx_Seleccionado.isChecked() ? R.drawable.bg_alerta_suave : R.drawable.bg_seleccionado, null));
                                            listener.onItemClick(holder.cbx_Seleccionado, holder.txv_IdTareo);
                                            if (holder.txv_IdEstado.getText().toString().equals("TR")) {
                                                if (!holder.cbx_Seleccionado.isChecked()) {
                                                    holder.mainLayout.setBackground(ResourcesCompat.getDrawable(Context.getResources(), R.drawable.bg_transferido, null));
                                                }
                                            }}

                                        if (holder.txv_IdEstado.getText().toString().equals("TR")) {
                                            holder.cbx_Seleccionado.setVisibility(View.GONE);
                                        }
                                    }
                                }
                            }, 500);

                            if (holder.txv_IdEstado.getText().toString().equals("PE")) {
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (System.currentTimeMillis() - pressStartTime >= 200) {
                                            Log.i("SHAKE!", "MOVE IT!");
                                            if (!shake.isRunning()) {  // Solo inicia si la animación no está corriendo
                                                shake.start();  // Inicia la animación de sacudida
                                            }
                                        }
                                        if (isLongPressed) {
                                            // Acción después de 7 segundos de presión
                                            Swal.confirm(Context, "Está seguro?", "Seguro que desea actualizar el id de este tareo?")
                                                    .setConfirmClickListener(sweetAlertDialog -> {
                                                        String idTareo = tareos.getString(tareos.getColumnIndex("Id"));
                                                        AppDatabase db = DataGreenApp.getAppDatabase();
                                                        TareoDAO tareoDAO = db.tareoDAO();
                                                        String last = obtenerUltimoIdDesdeCorrelativo();
//                                                        String last = tareoDAO.getLastId();
                                                        String nuevoId = Funciones.siguienteCorrelativo(last, 'A');
                                                        tareoDAO.actualizarIdTareo(idTareo, nuevoId);
                                                        ConexionSqlite objSqlite = new ConexionSqlite(Context, DataGreenApp.DB_VERSION());
                                                        try {
                                                            objSqlite.ActualizarCorrelativos(objConfLocal,"trx_Tareos",nuevoId);
                                                        } catch (Exception e) {
                                                            Toast.makeText(Context, "No se han podido actualizar los correlativos", Toast.LENGTH_LONG).show();
                                                        }

                                                        holder.txv_IdTareo.setText(nuevoId);
                                                        sweetAlertDialog.dismissWithAnimation();
                                                        if (listener != null) {
                                                            shake.end(); // Cancelar la animación si se confirma la acción
                                                            holder.cbx_Seleccionado.setChecked(false);
                                                            holder.mainLayout.setBackground(ResourcesCompat.getDrawable(Context.getResources(), R.drawable.bg_alerta_suave, null));
                                                            listener.onItemClick(holder.cbx_Seleccionado, holder.txv_IdTareo);
                                                            if (holder.txv_IdEstado.getText().toString().equals("TR")) {
                                                                if (!holder.cbx_Seleccionado.isChecked()) {
                                                                    holder.mainLayout.setBackground(ResourcesCompat.getDrawable(Context.getResources(), R.drawable.bg_transferido, null));
                                                                }
                                                            }
                                                        }
                                                    })
                                                    .setCancelClickListener(sweetAlertDialog -> {
                                                        isLongPressed = false;
                                                        handler.removeCallbacksAndMessages(null);
                                                        handlerSeleccion.removeCallbacksAndMessages(null);
                                                        shake.end(); // Cancelar la animación si se cancela
                                                        sweetAlertDialog.dismissWithAnimation();
                                                    })
                                                    .setOnDismissListener(dialog -> {
                                                        shake.end(); // Cancelar la animación si el diálogo se cierra
                                                    });
                                        }
                                    }
                                }, 3000); // 7 segundos
                            }
                            return true;

                        case MotionEvent.ACTION_UP:
                            handler.removeCallbacksAndMessages(null);
                            if (!isLongPressed) {
                                holder.mainLayout.performClick();
                            }
                            isLongPressed = false;
//                            shake.end(); // Cancelar la animación si el touch es liberado
                            return true;
                        case MotionEvent.ACTION_CANCEL:
                            isLongPressed = false;
                            handler.removeCallbacksAndMessages(null);
                            handlerSeleccion.removeCallbacksAndMessages(null);
//                            shake.end(); // Cancelar la animación si la acción es cancelada
                            return true;
                    }

                    return false;
                }
            });

//            holder.mainLayout.setOnLongClickListener(view -> {
//                boolean transferidoSeleccionado = holder.txv_IdEstado.getText().equals("TR");
//                if (listener != null
////                        && !holder.txv_IdEstado.getText().equals("TR")
//                ) {
//                    holder.cbx_Seleccionado.setChecked(!holder.cbx_Seleccionado.isChecked());
//                    holder.mainLayout.setBackground(ResourcesCompat.getDrawable(Context.getResources(), !holder.cbx_Seleccionado.isChecked()
//                            ? R.drawable.bg_alerta_suave
//                            : R.drawable.bg_seleccionado, null));
//                    listener.onItemClick(holder.cbx_Seleccionado, holder.txv_IdTareo);
//                    if (holder.txv_IdEstado.getText().toString().equals("TR")) {
//                        if (!holder.cbx_Seleccionado.isChecked()) {
//                            holder.mainLayout.setBackground(ResourcesCompat.getDrawable(Context.getResources(), R.drawable.bg_transferido, null));
//                        }
//                    }
//                }
//                return true;
//            });

            if (holder.txv_IdEstado.getText().toString().equals("TR")) {
                holder.cbx_Seleccionado.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @SuppressLint("Range")
    private void actualizarTextos(MyViewHolder holder, Cursor tareos) {
        tareos.moveToPosition(holder.getAdapterPosition());

    }

    public String obtenerUltimoIdDesdeCorrelativo(){
        SharedPreferences sharedPreferences = Context.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
        TareoDAO tareoDAO = DataGreenApp.getAppDatabase().tareoDAO();
        String mac, imei;
        mac = sharedPreferences.getString("MAC", "!MAC");
        imei = sharedPreferences.getString("IMEI", "!IMEI");
        String query = "select COALESCE(Correlativo, '000000000') Correlativo from trx_correlativos where MacDispositivoMovil = '"+mac+"' AND ImeiDispositivoMovil = '"+imei+"'";
        SupportSQLiteQuery supportSQLiteQuery = new SimpleSQLiteQuery(query);

        return tareoDAO.getLasIdFromCorrelativos(supportSQLiteQuery);
    }

    @Override
    public int getItemCount() {
        return tareos.getCount();
    }

    private void abrirDocumento(String id) {
        Intent nuevaActividad = new Intent(Context, cls_05010000_Edicion.class);
        nuevaActividad.putExtra("ConfiguracionLocal", objConfLocal);
        nuevaActividad.putExtra("IdDocumentoActual", id);
        Context.startActivity(nuevaActividad);
    }

    public List<String> retornarTareos() {
        return tareosSeleccionados;
    }

    public interface OnItemClickListener {
        void onItemClick(CheckBox cbxSeleccionado, TextView txtId);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txv_IdTareo, txv_Fecha, txv_IdEstado, txv_IdTurno, txv_TotalDetalles, txv_TotalHoras, txv_TotalRdtos, c009_txv_TotalJornales, txv_IdUsuario, txv_CantidadTrabajadores, txv_Observaciones;
        CheckBox cbx_Seleccionado;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txv_IdTareo = itemView.findViewById(R.id.c009_txv_IdTareo_v);
            txv_Fecha = itemView.findViewById(R.id.c009_txv_Fecha_v);
            txv_IdEstado = itemView.findViewById(R.id.c009_txv_IdEstado_v);
            txv_IdTurno = itemView.findViewById(R.id.c009_txv_IdTurno_v);
            txv_TotalDetalles = itemView.findViewById(R.id.c009_txv_TotalDetalles_v);
            txv_TotalHoras = itemView.findViewById(R.id.c009_txv_TotalHoras_v);
            txv_TotalRdtos = itemView.findViewById(R.id.c009_txv_TotalRdtos_v);
            c009_txv_TotalJornales = itemView.findViewById(R.id.c009_txv_TotalJornales_v);
            cbx_Seleccionado = itemView.findViewById(R.id.cbx_Seleccionado_v);
            txv_IdUsuario = itemView.findViewById(R.id.c009_txv_IdUsuario_v);
            txv_CantidadTrabajadores = itemView.findViewById(R.id.c009_txv_CantidadTrabajadores_v);
            txv_Observaciones = itemView.findViewById(R.id.c009_txv_Observaciones_v);

            mainLayout = itemView.findViewById(R.id.c009_mly_Principal_v);
        }
    }
}
