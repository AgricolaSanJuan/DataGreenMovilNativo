package com.example.datagreenmovil;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datagreenmovil.Entidades.TareoDetalle;
import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.Logica.Swal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class cls_05010200_RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private List<TareoDetalle> tareoDetalleList;
    private Context mContext;
    private boolean isLoading = false;
    private boolean ACTIVAR_PERMISO = false;
    Double[] values = {0.0, 0.5, 1.0};
    int itemSeleccionado = 0;
    SharedPreferences sharedPreferences;
    private cls_05010200_RecyclerViewAdapter.OnItemSelected listener;
    private cls_05010200_RecyclerViewAdapter.OnButtonClickListener onButtonClickListener;

    public interface OnItemSelected {
        void onItemSelected(String texto, boolean agregar);
    }

    public interface OnButtonClickListener {
        void onButtonClickListener(String texto);
    }

    // Interfaz para comunicar cambios
    public interface OnDataChangeListener {
        void onDataChanged();
    }

    private OnDataChangeListener dataChangeListener;

    // Método para asignar el listener
    public void setOnDataChangeListener(OnDataChangeListener listener) {
        this.dataChangeListener = listener;
    }

    // Método que puedes llamar cuando los datos cambien
    public void notifyDataChangedExternally() {
        if (dataChangeListener != null) {
            dataChangeListener.onDataChanged();
        }
    }

    public cls_05010200_RecyclerViewAdapter(Context context, List<TareoDetalle> tareoDetalleList) {
        this.mContext = context;
        this.tareoDetalleList = tareoDetalleList;
        sharedPreferences = mContext.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
    }

    public Double obtenerTotalRendimientos() {
        Double suma = 0.00;

        for (TareoDetalle detalle : tareoDetalleList) {
            suma += detalle.getRdtos(); // Asume que hay un método getRdtos() en TareoDetalle
        }
        return suma;
    }

    public String getLastActividad() {
        int cantidad = tareoDetalleList.size();
        return tareoDetalleList.get(cantidad - 1).getIdActividad();
    }

    public String getLastLabor() {
        int cantidad = tareoDetalleList.size();
        return tareoDetalleList.get(cantidad - 1).getIdLabor();
    }

    public String getLastConsumidor() {
        int cantidad = tareoDetalleList.size();
        return tareoDetalleList.get(cantidad - 1).getIdConsumidor();
    }

    public String getLastHoras() {
        int cantidad = tareoDetalleList.size();
        return String.valueOf(tareoDetalleList.get(cantidad - 1).getHoras());
    }

    public String getLastRendimientos() {
        int cantidad = tareoDetalleList.size();
        return String.valueOf(tareoDetalleList.get(cantidad - 1).getRdtos());
    }

    @Override
    public int getItemViewType(int position) {
        return tareoDetalleList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.v_05010100_item_recyclerview_010, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            TareoDetalle tareoDetalle = tareoDetalleList.get(position);
            ((ItemViewHolder) holder).bind(tareoDetalle);

            // Definir los valores para el Spinner
        } else if (holder instanceof LoadingViewHolder) {
            // Mostrar la vista de carga
        }
    }

    @Override
    public int getItemCount() {
        return tareoDetalleList == null ? 0 : tareoDetalleList.size();
    }

    public void addData(List<TareoDetalle> newItems) {
        int startPosition = newItems.size();
        tareoDetalleList = newItems;
        notifyItemRangeInserted(startPosition, newItems.size());
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void setOnItemSelected(cls_05010200_RecyclerViewAdapter.OnItemSelected listener) {
        this.listener = listener;
    }

    public void setOnButtonClickListener(cls_05010200_RecyclerViewAdapter.OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        // Define tus views aquí
        boolean selected = false;
        ImageView imageCheck;
        TextView c010_txv_Item, c010_txv_Dni, c010_txv_Nombres, c010_txv_IdCultivo, c010_txv_Cultivo, c010_txv_IdVariedad, c010_txv_Variedad, c010_txv_IdConsumidor, c010_txv_Consumidor, c010_txv_IdActividad, c010_txv_Actividad, c010_txv_IdLabor, c010_txv_Labor, c010_txv_Horas, c010_txv_Rdtos, c010_txv_Observacion, labelAlmuerzo;
        Spinner spinnerAlmuerzo;
        TextView tvIngreso, tvSalida;
        ConstraintLayout c010_lly_Principal, llyBody;
        CheckBox cboHomologar;
        Button btnTransferir;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            c010_lly_Principal = itemView.findViewById(R.id.c010_lly_Principal_v);
            c010_txv_Item = itemView.findViewById(R.id.c010_txv_Item_v);
            c010_txv_Dni = itemView.findViewById(R.id.c010_txv_Dni_v);
            c010_txv_Nombres = itemView.findViewById(R.id.c010_txv_Nombres_v);
            c010_txv_IdCultivo = itemView.findViewById(R.id.c010_txv_IdCultivo_v);
            c010_txv_Cultivo = itemView.findViewById(R.id.c010_txv_Cultivo_v);
            c010_txv_IdVariedad = itemView.findViewById(R.id.c010_txv_IdVariedad_v);
            c010_txv_Variedad = itemView.findViewById(R.id.c010_txv_Variedad_v);
            c010_txv_IdActividad = itemView.findViewById(R.id.c010_txv_IdActividad_v);
            c010_txv_Actividad = itemView.findViewById(R.id.c010_txv_Actividad_v);
            c010_txv_IdLabor = itemView.findViewById(R.id.c010_txv_IdLabor_v);
            c010_txv_Labor = itemView.findViewById(R.id.c010_txv_Labor_v);
            c010_txv_IdConsumidor = itemView.findViewById(R.id.c010_txv_IdConsumidor_v);
            c010_txv_Consumidor = itemView.findViewById(R.id.c010_txv_Consumidor_v);
            c010_txv_Horas = itemView.findViewById(R.id.c010_txv_Horas_v);
            c010_txv_Rdtos = itemView.findViewById(R.id.c010_txv_Rdtos_v);
            c010_txv_Observacion = itemView.findViewById(R.id.c010_txv_Observacion_v);
            llyBody = itemView.findViewById(R.id.lyBody);
//            imageCheck = itemView.findViewById(R.id.imageCheck);
            tvIngreso = itemView.findViewById(R.id.tvIngreso);
            tvSalida = itemView.findViewById(R.id.tvSalida);
            labelAlmuerzo = itemView.findViewById(R.id.labelAlmuerzo);
            spinnerAlmuerzo = itemView.findViewById(R.id.spinnerAlmuerzo);
            cboHomologar = itemView.findViewById(R.id.cbDescanso);
            btnTransferir = itemView.findViewById(R.id.btnTransferir);
        }

        public void bind(TareoDetalle tareoDetalle) {
            ACTIVAR_PERMISO = sharedPreferences.getBoolean("ACTIVAR_PERMISO", false);

            c010_txv_Item.setText(String.valueOf(tareoDetalle.getItem()));
            c010_txv_Dni.setText(tareoDetalle.getDni());
            c010_txv_Nombres.setText(tareoDetalle.getNombres() != null ? tareoDetalle.getNombres() : "TRABAJADOR DESCONOCIDO");
            c010_txv_IdCultivo.setText(String.format("(%s)", tareoDetalle.getIdCultivo().trim()));
            c010_txv_Cultivo.setText(String.format("%s", tareoDetalle.getCultivo().trim()));
            c010_txv_IdVariedad.setText(String.format("(%s)", tareoDetalle.getIdVariedad().trim()));
            c010_txv_Variedad.setText(String.format("%s", tareoDetalle.getVariedad().trim()));
            c010_txv_IdActividad.setText(String.format("(%s)", tareoDetalle.getIdActividad().trim()));
            c010_txv_Actividad.setText(String.format("%s", tareoDetalle.getActividad().trim()));
            c010_txv_IdLabor.setText(String.format("(%s)", tareoDetalle.getIdLabor().trim()));
            c010_txv_Labor.setText(String.format("%s", tareoDetalle.getLabor().trim()));
            c010_txv_IdConsumidor.setText(String.format("(%s)", tareoDetalle.getIdConsumidor().trim()));
            c010_txv_Consumidor.setText(String.format("%s", tareoDetalle.getConsumidor().trim()));
            c010_txv_Horas.setText(String.valueOf(tareoDetalle.getHoras()));
            c010_txv_Rdtos.setText(String.valueOf(tareoDetalle.getRdtos()));
            c010_txv_Observacion.setText(tareoDetalle.getObservacion());
//            c010_lly_CultivoVariedad.setVisibility(View.GONE);
            tvIngreso.setText(tareoDetalle.getIngreso());
            tvSalida.setText(tareoDetalle.getSalida());
            cboHomologar.setChecked(tareoDetalle.getHomologar() == 1);

            c010_lly_Principal.setOnLongClickListener(view -> {
                if (listener != null) {
                    selected = !selected;
//                    imageCheck.setVisibility(selected ? View.VISIBLE : View.INVISIBLE);


                    llyBody.setBackground(ResourcesCompat.getDrawable(mContext.getResources(), selected ? R.drawable.fondo_delineado_seleccionado : R.drawable.fondo_delineado_3, null) );
//                    llyBody.setBackground(view.getResources().getDrawable(selected ? R.drawable.fondo_delineado_seleccionado : R.drawable.fondo_delineado_3, null));



                    listener.onItemSelected(c010_txv_Item.getText().toString(), selected);
                }
                return true;
            });

            cboHomologar.setOnCheckedChangeListener((buttonView, isChecked) -> {
                tareoDetalle.setHomologar(isChecked ? 1 : 0);
            });

            btnTransferir.setOnClickListener(v -> {
                onButtonClickListener.onButtonClickListener(c010_txv_Item.getText().toString());
            });

            c010_lly_Principal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    itemSeleccionado = Integer.parseInt(c010_txv_Item.getText().toString());
                    mostrarMenuDetalle(c010_lly_Principal);
                }

                public void mostrarMenuDetalle(View v) {
                    PopupMenu popup = new PopupMenu(mContext, v);
                    popup.setOnMenuItemClickListener(this::onMenuItemClick);
                    popup.inflate(R.menu.mnu_05010101_opciones_detalle);
                    popup.show();
                }

                public boolean onMenuItemClick(MenuItem item) {
                    try {
                        int idOpcionClickeada = item.getItemId();
                        if (idOpcionClickeada == R.id.opc_05010101_EliminarDetalle_v) {
                            Swal.confirm(mContext, "Estás seguro?", "Una vez eliminado este detalle no se podrá recuperar").setConfirmClickListener(sweetAlertDialog -> {
                                if (mContext instanceof cls_05010000_Edicion) {
                                    ((cls_05010000_Edicion) mContext).eliminarDetalle(itemSeleccionado);
                                }
                                sweetAlertDialog.dismissWithAnimation();
                            }).setCancelClickListener(sweetAlertDialog -> {
                                sweetAlertDialog.dismissWithAnimation();
                            });
                        } else if (idOpcionClickeada == R.id.opc_05010101_ActualizarDetalle_v) {
                            if (mContext instanceof cls_05010000_Edicion) {
                                ((cls_05010000_Edicion) mContext).popUpActualizarDetalleTareos(tareoDetalle);
                            }
                        }
                    } catch (Exception ex) {
                        Funciones.mostrarError(mContext, ex);
                        return false;
                    }
                    return false;
                }
            });


            //        DEFINIMOS SI ESTAMOS EN MODO PACKING O NO
            boolean modoPacking = sharedPreferences.getBoolean("MODO_PACKING", false);
            // Crear el ArrayAdapter usando un layout simple de Spinner
            ArrayAdapter<Double> adapter = new ArrayAdapter<>(mContext, R.layout.simple_list_item_custom, values);

            // Especificar el layout a usar cuando se despliegan las opciones
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Asignar el adaptador al Spinner
            spinnerAlmuerzo.setAdapter(adapter);

            if (modoPacking) {
                AtomicBoolean openByUser = new AtomicBoolean(true);

                spinnerAlmuerzo.setOnTouchListener((view, motionEvent) -> {
                    openByUser.set(true);
                    return false;
                });

                spinnerAlmuerzo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        double selectedValue = Double.parseDouble(spinnerAlmuerzo.getItemAtPosition(i).toString());

                        if (!tareoDetalle.getIngreso().equals("") && !tareoDetalle.getSalida().equals("") && openByUser.get()) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                // Convertir las cadenas a objetos Date
                                Date date1 = sdf.parse(tareoDetalle.getIngreso());
                                Date date2 = sdf.parse(tareoDetalle.getSalida());

                                Date dateComparar1 = sdf.parse(tareoDetalle.getIngreso());
                                Date dateComparar2 = sdf.parse(tareoDetalle.getSalida());


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
//                            Log.i("horasCalculadasComparar", String.valueOf(horasCalculadasComparar));
//                            Log.i("horasCalculadas", String.valueOf(horasCalculadasComparar));
                                if (horasCalculadas != tareoDetalle.getHoras()) {
                                    double diferencia = horasCalculadas - tareoDetalle.getHoras();
                                    boolean cambiar = false;
                                    int indexSelect = 0;
                                    if (diferencia == 0.5) {
                                        indexSelect = 1;
                                        cambiar = true;
                                    } else if (diferencia == 1.0) {
                                        indexSelect = 2;
                                        cambiar = true;
                                    } else if (diferencia == 0.0) {
                                        indexSelect = 0;
                                        cambiar = true;
                                    }
                                    if (cambiar) {
                                        spinnerAlmuerzo.setSelection(indexSelect, true);
                                        selectedValue = Double.parseDouble(spinnerAlmuerzo.getItemAtPosition(indexSelect).toString());
                                    }
                                    horasCalculadas = horasRedondeadas.doubleValue() - selectedValue;
                                    tareoDetalle.setHoras(horasCalculadas);
                                    c010_txv_Horas.setText(String.valueOf(tareoDetalle.getHoras()));
                                } else {
                                    selectedValue = Double.parseDouble(spinnerAlmuerzo.getSelectedItem().toString());
                                    horasCalculadas = horasRedondeadas.doubleValue() - selectedValue;
                                    tareoDetalle.setHoras(horasCalculadas);
                                    c010_txv_Horas.setText(String.valueOf(tareoDetalle.getHoras()));
                                    if (horasCalculadas < 0.00) {
                                        horasCalculadas = 0.00;
                                    }
                                    tareoDetalle.setHoras(horasCalculadas);
                                    c010_txv_Horas.setText(String.valueOf(tareoDetalle.getHoras()));
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            } finally {
                                openByUser.set(false);
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        double selectedValue = Double.parseDouble(spinnerAlmuerzo.getSelectedItem().toString());
                    }
                });
            } else {
                spinnerAlmuerzo.setVisibility(View.GONE);
                labelAlmuerzo.setVisibility(View.GONE);
            }

            if(ACTIVAR_PERMISO) {
                cboHomologar.setVisibility(View.INVISIBLE);
            }


        }
    }



    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);

        }

        public void showLoading(boolean isLoading) {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }

    }
}
