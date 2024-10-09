//UPDATED:2022-03-09;
package com.example.datagreenmovil;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Entidades.ClaveValor;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.PopUpBuscarEnLista;
import com.example.datagreenmovil.Entidades.PopUpBuscarEnLista_Item;
import com.example.datagreenmovil.Entidades.PopUpCalendario;
import com.example.datagreenmovil.Entidades.PopUpObservacion;
import com.example.datagreenmovil.Entidades.Tabla;
import com.example.datagreenmovil.Entidades.Tareo;
import com.example.datagreenmovil.Entidades.TareoDetalle;
import com.example.datagreenmovil.Logica.CryptorSJ;
import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.Logica.ZXingScannerView;
import com.example.datagreenmovil.Scanner.ui.ScannerViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
//import com.example.datagreenmovil.Logica.InterfazDialog;

public class cls_05010000_Edicion extends AppCompatActivity implements View.OnClickListener, ScaleGestureDetector.OnScaleGestureListener {
    //@Jota:2023-05-27 -> INICIO DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES

    static ConexionSqlite objSqlite;
    //private Registro tareoActual;
    private final TareoDetalle detalleActual = new TareoDetalle();
    public String s_Fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //--desde, hasta, estado;);
    public LinearLayout c007_lly_Turno, c007_lly_Actividad, c007_lly_Labor, c007_lly_Consumidor, c007_lly_Cabecera, c007_lly_Actividad_Val, c007_lly_Labor_Val, c007_lly_Consumidor_Val, c007_lly_Observacion, c007_lly_Detalle2;
    public Switch switchTipoMarcacion;
    public ConstraintLayout layoutHoras, layoutRendimientos;
    ConexionBD objSql;
    ConfiguracionLocal objConfLocal;
    TextView txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador;
    Dialog dlg_PopUp;
    SharedPreferences sharedPreferences;
    HashMap<String, Tabla> hmTablas = new HashMap<>();
    ConstraintLayout layoutEscaner;
    TextView TotalRendimientos, TotalTrabajadores;
    ArrayList<Integer> detallesSeleccionados;
    ArrayList<PopUpBuscarEnLista_Item> arl_Turnos, arl_Actividades, arl_Labores = null, arl_Consumidores;
    boolean layoutAbierto = true;
    boolean flashState = false;
    boolean mostrarEscaner = false;
    int cantidadInicial, cantidadFinal;
    JSONArray listaTrabajadores = new JSONArray();
    private ScaleGestureDetector scaleGestureDetector;
    private ZXingScannerView scannerView;
    private ScannerViewModel scannerViewModel;
    private Context ctx;
    //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
    //...
//    Querys objQuerys;
    private SQLiteDatabase database;
    //    public int largoTextoSpinner = 13;
    //CONTROLES;
    private RecyclerView c007_rvw_Detalle;// = findViewById(R.id.c007_rvw_Detalle_v);
    private FloatingActionButton c007_fab_Guardar, c007_fab_AbrirCerrarCabecera, fabToggleFlash, fabMostrarEscaner, fabDuplicar, fabEditar, fabEliminar;
    private AutoCompleteTextView c007_atv_NroDocumento;
    private AutoCompleteTextView c007_atv_NombreTrabajador;
    private TextView c007_txv_Id, c007_txv_Fecha, c007_txv_Turno_Key, c007_txv_Turno_Val, c007_txv_Actividad_Key, c007_txv_Actividad_Val, c007_txv_Labor_Key, c007_txv_Labor_Val, c007_txv_Consumidor_Key, c007_txv_Consumidor_Val, c007_txv_Observacion;
    private EditText c007_etx_Horas, c007_etx_Rdtos; //, etxObservaciones;
    private cls_05010200_RecyclerViewAdapter.OnItemSelected listener;
    //VARIABLES;
    private String IdDocumentoActual = null;
    private Tareo tareoActual; //= new Tareo();
    private FloatingActionButton fabAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_05010000_edicion_007);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ctx = this;

        detallesSeleccionados = new ArrayList<>();

        scannerView = findViewById(R.id.scannerView);

        sharedPreferences = this.getSharedPreferences("objConfLocal", MODE_PRIVATE);

        //@Jota:2023-05-27 -> INICIO DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        scaleGestureDetector = new ScaleGestureDetector(this, this);
        database = SQLiteDatabase.openDatabase(this.getDatabasePath("DataGreenMovil.db").toString(), null, SQLiteDatabase.OPEN_READWRITE);
        try {
            if (getIntent().getExtras() != null) {
                objConfLocal = (ConfiguracionLocal) getIntent().getSerializableExtra("ConfiguracionLocal");
                IdDocumentoActual = (String) getIntent().getSerializableExtra("IdDocumentoActual");
            }
            objSql = new ConexionBD(objConfLocal);
            objSqlite = new ConexionSqlite(this, DataGreenApp.DB_VERSION());
            referenciarControles();

            boolean modoPacking = sharedPreferences.getBoolean("MODO_PACKING", false);
            switchTipoMarcacion = findViewById(R.id.swTipoTareo);
            layoutHoras = findViewById(R.id.layoutHoras);
            layoutRendimientos = findViewById(R.id.layoutRendimientos);
            fabAgregar = findViewById(R.id.c007_fab_Agregar_v);

            if (modoPacking) {
                switchTipoMarcacion.setVisibility(View.VISIBLE);
                layoutHoras.setVisibility(View.GONE);
                layoutRendimientos.setVisibility(View.GONE);
                fabAgregar.setVisibility(View.INVISIBLE);
                c007_atv_NombreTrabajador.setVisibility(View.INVISIBLE);
                c007_atv_NombreTrabajador.setHeight(1);
            } else {
                // Definir LayoutParams con wrap_content para ancho y alto
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                switchTipoMarcacion.setVisibility(View.GONE);
                layoutHoras.setVisibility(View.VISIBLE);
                layoutRendimientos.setVisibility(View.VISIBLE);
                c007_atv_NombreTrabajador.setLayoutParams(params);
                c007_atv_NombreTrabajador.setVisibility(View.VISIBLE);
                fabAgregar.setVisibility(View.VISIBLE);
            }

            if (switchTipoMarcacion != null) {
                switchTipoMarcacion.setOnCheckedChangeListener((compoundButton, b) -> {
                    if (!b) {
                        switchTipoMarcacion.setText("INGRESO");
                    } else {
                        switchTipoMarcacion.setText("SALIDA");
                    }
                });
            }

//            }
            c007_txv_Fecha.setText(Funciones.malograrFecha(s_Fecha));
//            setearControles();
            Funciones.mostrarEstatusGeneral(this.getBaseContext(), objConfLocal, txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador);
            //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
            //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
            //...
            tareoActual = new Tareo(IdDocumentoActual, objSqlite, this);
            detalleActual.setIdEmpresa(tareoActual.getIdEmpresa());
            obtenerDataParaControles();
            cargarControles();
            setearControles();
            listarDetalles();
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("TOUCH", "Touch event received");
        // Pasar el evento de toque al detector de gestos de escala
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    void setHandlerScanner(Context context) {
        scannerView.resume();
        scannerView.setResultHandler(result -> {
            Context ctx = this;
//            ANALIZAMOS LA CADENA DE TEXTO PARA PODER DESENCRIPTARLA SI FUERA NECESARIO
            String resultadoDesencriptado = "";
            if (result.getText().length() == 10 && !(String.valueOf(result.getText().charAt(0)).equals("S"))) {
                try {
                    resultadoDesencriptado = CryptorSJ.desencriptarCadena(result.getText());
                } catch (Exception e) {
                    Swal.warning(ctx, "Cuidado", "La cadena no cumple con el formato de San Juan.", 2000);
                }
            } else {
                resultadoDesencriptado = result.getText();
            }
            CountDownTimer countDownTimer = new CountDownTimer(1500, 1500) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    setHandlerScanner(ctx);
                }
            };
            resultado(resultadoDesencriptado);
            countDownTimer.start();
        });
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        // Se llama cuando se detecta un gesto de escala (zoom)
        float scaleFactor = detector.getScaleFactor();
        Log.i("ZOOM", "FINO");
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
    }

    // Método que simula la carga de más datos
    private void cargarMasDatos(cls_05010200_RecyclerViewAdapter adapter) {
        // Simular una carga de datos nuevos (esto podría ser de una API o base de datos)
        List<TareoDetalle> nuevosDatos = tareoActual.getDetalle();

        // Llamar al método del adaptador para añadir más elementos
        adapter.addMoreItems(nuevosDatos);
    }

    public void listarDetalles() {
        try {
            boolean modoPacking = sharedPreferences.getBoolean("MODO_PACKING", false);
            manejarLayout();
            cls_05010200_RecyclerViewAdapter adaptadorLista = new cls_05010200_RecyclerViewAdapter(this, objConfLocal, objSqlite, tareoActual);

//            PRUEBA DE SCROLL LAZYLOAD
            c007_rvw_Detalle.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (!adaptadorLista.isLoading && layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == adaptadorLista.getItemCount() - 1) {
                        // Llegamos al final de la lista
                        adaptadorLista.isLoading = true; // Cambiar la bandera de carga para evitar llamadas repetidas
                        Log.i("LazyLoad","cargando mas data");
                        // Aquí es donde deberías cargar más datos (puede ser de una base de datos o una API)
                        cargarMasDatos(adaptadorLista);
                    }
                }
            });
//            END PRUEBA DE SCROLL LAZY LOAD
            adaptadorLista.setOnItemSelected(new cls_05010200_RecyclerViewAdapter.OnItemSelected() {
                @Override
                public void onItemSelected(String item, boolean agregar) {
                    if (agregar) {
                        detallesSeleccionados.add(Integer.parseInt(item));
                    } else {
                        int valorComoInteger = Integer.valueOf(item);
                        detallesSeleccionados.remove((Integer) valorComoInteger);
                    }
                }
            });
            c007_rvw_Detalle.setAdapter(adaptadorLista);
            c007_rvw_Detalle.setLayoutManager(new LinearLayoutManager(this));
            mostrarValoresDocumentoActual();
            Double sumaTotal = adaptadorLista.obtenerTotalRendimientos();
            // Formatear el número con dos decimales
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String sumaFormateada = decimalFormat.format(sumaTotal);
            if (adaptadorLista.getItemCount() > 0) {
                String lastActividad, lastLabor, lastConsumidor, lastHoras, lastRendimientos;
                lastActividad = adaptadorLista.getLastActividad();
                lastLabor = adaptadorLista.getLastLabor();
                lastConsumidor = adaptadorLista.getLastConsumidor();
                lastHoras = adaptadorLista.getLastHoras();
                lastRendimientos = adaptadorLista.getLastRendimientos();
                c007_txv_Actividad_Key.setText(lastActividad);
                c007_txv_Labor_Key.setText(lastLabor);
                c007_txv_Consumidor_Key.setText(lastConsumidor);
                if (modoPacking) {
                    c007_etx_Horas.setText("0.00");
                    c007_etx_Rdtos.setText("0.00");
                } else {
                    c007_etx_Horas.setText(lastHoras);
                    c007_etx_Rdtos.setText(lastRendimientos);
                }
            }
            TotalRendimientos.setText(String.valueOf(sumaFormateada));
            int cantidadRegistros = adaptadorLista.getItemCount();
            TotalTrabajadores.setText(String.valueOf(cantidadRegistros));
            adaptadorLista.getItemId(cantidadRegistros);
            if (cantidadRegistros > 0) {
                layoutAbierto = false;
                manejarLayout();
            }
        } catch (Exception e) {
            Log.e("ERRORLISTAR", e.toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        listarDetalles();
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.pause();
    }

    //    @Override
    public void resultado(String barcodeValue) {

        if (c007_txv_Consumidor_Key.length() > 0 && c007_txv_Actividad_Key.length() > 0 && c007_txv_Labor_Key.length() > 0) {
            boolean modoPacking = sharedPreferences.getBoolean("MODO_PACKING", false);

            c007_atv_NroDocumento.setText(barcodeValue.replaceAll("[^0-9]", ""));
//            Swal.info(this, "ID", barcodeValue.replaceAll("[^0-9]", ""), 5000);
            // Obtener la fecha y hora actual
            Calendar calendar = Calendar.getInstance();

            // Formatear la fecha y hora actual
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String fechaHoraFormateada = sdf.format(calendar.getTime());
            Cursor cVerificarSinSalida;
            try {
                if (switchTipoMarcacion != null && !switchTipoMarcacion.isChecked()) {
                    String[] selectionArgs = {tareoActual.getId(), c007_atv_NroDocumento.getText().toString()};
                    cVerificarSinSalida = database.rawQuery("SELECT * FROM TRX_TAREOS_DETALLE td inner join trx_tareos t WHERE td.Idtareo = ? AND td.Dni = ? AND td.salida = '';", selectionArgs);
                    if (cVerificarSinSalida.getCount() > 0 && modoPacking) {
                        Swal.warning(this, "Cuidado", "El trabajador tiene un tareo sin salida, registra su salida y luego vuelve a intentarlo.", 5000);
                    } else {
                        //                if (!switchTipoMarcacion.isChecked()) {
                        obtenerDataTrabajador(hmTablas.get("PERSONAS"));
                        agregarDetalle("lector");
                    }
                } else {
//                    ESTO SOLO SE EJECUTARÁ CUANDO SE MARQUE LA SALIDA, MAS NO CUANDO SE CARGUEN LOS DETALLES,
//                    Y ES DONDE SE DEBE HACER EL CÁLCULO PARA LA CANTIDAD DE HORAS DE ACUERDO A LA HORA DE INGRESO Y A LA HORA DE SALIDA
//
//                    OBTENEMOS EL ULTIMO TAREO QUE NO TENGA UNA FECHA DE SALIDA
                    String[] selectionArgs = {tareoActual.getId(), c007_atv_NroDocumento.getText().toString()};
                    cVerificarSinSalida = database.rawQuery("SELECT * FROM TRX_TAREOS_DETALLE WHERE Idtareo = ? AND Dni = ? AND salida = ''", selectionArgs);
                    if (cVerificarSinSalida.getCount() > 0) {
                        cVerificarSinSalida.moveToFirst();
                        int itemIndex;
                        itemIndex = cVerificarSinSalida.getColumnIndex("Item");
                        String itemIndexU;
                        itemIndexU = cVerificarSinSalida.getString(itemIndex);

                        TareoDetalle tareoDetalle = tareoActual.getDetalle().get(Integer.parseInt(itemIndexU) - 1);
                        Date date1 = sdf.parse(tareoDetalle.getIngreso());
                        Date date2 = sdf.parse(fechaHoraFormateada);

                        // Calcular la diferencia en milisegundos
                        long diferenciaMilisegundos = date2.getTime() - date1.getTime();
                        // Convertir la diferencia de milisegundos a horas
                        long diferenciaHoras = TimeUnit.MILLISECONDS.toHours(diferenciaMilisegundos);
                        tareoDetalle.setSalida(fechaHoraFormateada);
                        double horas = diferenciaMilisegundos / 3600000.00;
                        BigDecimal horasRedondeadas = new BigDecimal(horas).setScale(2, RoundingMode.HALF_UP);
                        Log.i("DIFERENCIAFINA", String.valueOf(horasRedondeadas.doubleValue()));
                        tareoDetalle.setHoras(horasRedondeadas.doubleValue());
                    } else {
                        Swal.warning(this, "Cuidado", "No se encuentra un tareo de ingreso de este trabajador, pruebe a guardar el tareo y volver a intentar.", 2000);
                    }
                }
                guardarTareo();
                cVerificarSinSalida.close();
            } catch (Exception e) {
                Log.e("ERROR!", e.getMessage());
                Swal.error(this, "Error!", e.toString(), 8000);
                throw new RuntimeException(e);
            } finally {
                c007_atv_NroDocumento.setText("");
                c007_atv_NombreTrabajador.setText("");
            }

        } else {
            Swal.warning(this, "Alerta!", "No se ha agregado el tareo por que faltan campos", 1500);
        }
    }

    //@Jota:2023-05-27 -> LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    private void setearControles() {

        c007_txv_Fecha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                s_Fecha = Funciones.arreglarFecha(c007_txv_Fecha.getText().toString());
                //LocalDate localDate = LocalDate.parse(s_Fecha);
                tareoActual.setFecha(LocalDate.parse(s_Fecha));
            }
        });

        c007_txv_Turno_Key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                tareoActual.setIdTurno(c007_txv_Turno_Key.getText().toString());
            }
        });

        c007_txv_Actividad_Key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //objRex.Set("IdActividad",c007_txv_Actividad_Key.getText().toString());
                //tareoActual.setIdTurno(c007_txv_Actividad_Key.getText().toString());
                detalleActual.setIdActividad(c007_txv_Actividad_Key.getText().toString());

                List<String> p = new ArrayList<>();
                p.add(tareoActual.getIdEmpresa()); //PENDIENTE: OBTENER EMPRESA DINAMICAMENTE;
                p.add(detalleActual.getIdActividad());
                //hmTablas.put("LABORES", new Tabla(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Labores"),p,"READ")));
                try {
                    arl_Labores = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Labores"), p, "READ"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                c007_txv_Labor_Key.setText("");
                c007_txv_Labor_Val.setText("");
                detalleActual.setIdLabor("");
            }
        });

        c007_txv_Labor_Key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //objRex.Set("IdActividad",c007_txv_Actividad_Key.getText().toString());
                detalleActual.setIdLabor(c007_txv_Labor_Key.getText().toString());
            }
        });

        c007_txv_Consumidor_Key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //objRex.Set("IdActividad",c007_txv_Actividad_Key.getText().toString());
                detalleActual.setIdConsumidor(c007_txv_Consumidor_Key.getText().toString());
            }
        });

        c007_txv_Observacion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String obs = c007_txv_Observacion.getText().toString();
                if (!obs.equals("...")) {
                    tareoActual.setObservaciones(obs);
                    c007_txv_Observacion.setText("...");
                }
            }
        });

        setearAutoCompleteTextViewNroDocumento(hmTablas.get("PERSONAS"));
        setearAutoCompleteTextViewNombreTrabajador(hmTablas.get("PERSONAS"));
    }

    private void referenciarControles() {
        txv_PushTituloVentana = findViewById(R.id.c007_txv_PushTituloVentana_v);
        txv_PushRed = findViewById(R.id.c007_txv_PushRed_v);
        txv_NombreApp = findViewById(R.id.c007_txv_NombreApp_v);
        txv_PushVersionApp = findViewById(R.id.c007_txv_PushVersionApp_v);
        txv_PushVersionDataBase = findViewById(R.id.c007_txv_PushVersionDataBase_v);
        txv_PushIdentificador = findViewById(R.id.c007_txv_PushIdentificador_v);

        TotalRendimientos = findViewById(R.id.c007_txv_TotalRendimientosView);
        TotalTrabajadores = findViewById(R.id.c007_txv_TotalTrabajadoresView);

        //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
        //...
        c007_fab_Guardar = findViewById(R.id.c007_fab_Guardar_v);
        fabDuplicar = findViewById(R.id.c007_fab_Duplicar_v);
        fabEditar = findViewById(R.id.c007_fab_Editar_v);
        fabEliminar = findViewById(R.id.c007_fab_Eliminar_v);
        c007_fab_AbrirCerrarCabecera = findViewById(R.id.c007_fab_AbrirCerrarCabecera_v);

        layoutEscaner = findViewById(R.id.layoutEscaner);
        fabToggleFlash = findViewById(R.id.fabToggleFlash);
        fabMostrarEscaner = findViewById(R.id.fabMostrarEscaner);

        c007_atv_NroDocumento = findViewById(R.id.c007_atv_NroDocumento_v);
        c007_atv_NombreTrabajador = findViewById(R.id.c007_atv_NombreTrabajador_v);
        c007_txv_Fecha = findViewById(R.id.c007_txv_Fecha);
        c007_txv_Id = findViewById(R.id.c007_txv_IdTareo_v);

        c007_txv_Turno_Key = findViewById(R.id.c007_txv_Turno_Key_v);
        c007_txv_Turno_Val = findViewById(R.id.c007_txv_Turno_Val_v);
        c007_txv_Actividad_Key = findViewById(R.id.c007_txv_Actividad_Key_v);
        c007_txv_Actividad_Val = findViewById(R.id.c007_txv_Actividad_Val_v);
        c007_txv_Labor_Key = findViewById(R.id.c007_txv_Labor_Key_v);
        c007_txv_Labor_Val = findViewById(R.id.c007_txv_Labor_Val_v);
        c007_txv_Consumidor_Key = findViewById(R.id.c007_txv_Consumidor_Key_v);
        c007_txv_Consumidor_Val = findViewById(R.id.c007_txv_Consumidor_Val_v);
        c007_txv_Observacion = findViewById(R.id.c007_txv_Observacion_v);
        c007_lly_Turno = findViewById(R.id.c007_lly_Turno_v);
        c007_lly_Actividad = findViewById(R.id.c007_lly_Actividad_v);
        c007_lly_Labor = findViewById(R.id.c007_lly_Labor_v);
        c007_lly_Consumidor = findViewById(R.id.c007_lly_Consumidor_v);
        c007_lly_Cabecera = findViewById(R.id.c007_lly_Cabecera_v);
        c007_lly_Actividad_Val = findViewById(R.id.c007_lly_Actividad_Val_v);
        c007_lly_Labor_Val = findViewById(R.id.c007_lly_Labor_Val_v);
        c007_lly_Consumidor_Val = findViewById(R.id.c007_lly_Consumidor_Val_v);
        c007_lly_Observacion = findViewById(R.id.c007_lly_Observacion_v);
        c007_lly_Detalle2 = findViewById(R.id.c007_lly_Detalle2_v);


        c007_etx_Horas = findViewById(R.id.etxHoras_v);
        c007_etx_Rdtos = findViewById(R.id.etxRtos_v);

        c007_rvw_Detalle = findViewById(R.id.c007_rvw_Detalle_v);

        fabToggleFlash.setOnClickListener(view -> {
            flashState = !flashState;
            if (flashState) {
                scannerView.setTorchOn();
            } else {
                scannerView.setTorchOff();
            }
        });

//        NUEVO MÉTODO PARA LECTURAR FOTOCHECKS
        scannerViewModel = new ViewModelProvider(this).get(ScannerViewModel.class);

        scannerViewModel.getScannedCode().observe(this, code -> {
            // Maneja el código escaneado aquí
            if (code != null) {
                // Hacer algo con el código escaneado
                try {
                    String dni = CryptorSJ.desencriptarCadena(code);
//                    evaluarMarca(dni);
//                    Toast.makeText(this, "Código escaneado: " + dni, Toast.LENGTH_LONG).show();
                    scannerViewModel.setScannedCode(null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });

        fabEliminar.setOnClickListener(view -> {
            if (detallesSeleccionados.size() > 0) {
                Swal.confirm(ctx, "Estás seguro?", "Estás seguro que deseas eliminar los registros seleccionados? Una vez borrados, no se podrán recuperar.").setConfirmClickListener(sweetAlertDialog -> {
                    for (int index : detallesSeleccionados) {
                        tareoActual.eliminarItemDetalle(index);
                    }
                    guardarTareo();
                    detallesSeleccionados = new ArrayList<>();
                    listarDetalles();
                    sweetAlertDialog.dismissWithAnimation();
                }).setCancelClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                });
            } else {
                Swal.warning(ctx, "¡Cuidado!", "Selecciona al menos un trabajador antes.", 2000);
            }
        });

        fabEditar.setOnClickListener(view -> {
            if (detallesSeleccionados.size() > 0) {
                Swal.customDialog(ctx, "editar", tareoActual, detallesSeleccionados, (result, sweetAlertDialog1) -> {
//                    Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
                }, (success, message) -> {
                    if (success) {
                        Swal.success(ctx, "Correcto!", message, 1500);
                        listarDetalles();
                        detallesSeleccionados = new ArrayList<>();
                    } else {
                        Swal.error(ctx, "Oops!", message, 1500);
                        detallesSeleccionados = new ArrayList<>();
                    }
                });
            } else {
                Swal.warning(ctx, "¡Cuidado!", "Selecciona al menos un trabajador antes.", 2000);
            }
        });

        fabDuplicar.setOnClickListener(view -> {
            if (detallesSeleccionados.size() > 0) {
                Swal.customDialog(ctx, "duplicar", tareoActual, detallesSeleccionados, (result, sweetAlertDialog1) -> {
                }, (success, message) -> {
                    if (success) {
                        Swal.success(ctx, "Correcto!", message, 1500);
                        listarDetalles();
                        detallesSeleccionados = new ArrayList<>();
                    } else {
                        Swal.error(ctx, "Oops!", message, 1500);
                        detallesSeleccionados = new ArrayList<>();
                    }
                });
            } else {
                Swal.warning(ctx, "¡Cuidado!", "Selecciona al menos un trabajador antes.", 2000);
            }
        });

        fabMostrarEscaner.setOnLongClickListener(view -> {
            Swal.scanDialog(this, (resultado, sweetAlertDialog) -> {
                Toast.makeText(this, resultado, Toast.LENGTH_SHORT).show();
            });
            return false;
        });

        fabMostrarEscaner.setOnClickListener(view -> {
            mostrarEscaner = !mostrarEscaner;
            if (mostrarEscaner) {
                layoutEscaner.setVisibility(View.VISIBLE);

                setHandlerScanner(this);

//                scannerView.setResultHandler(this);
//                scannerView.set (1000);

//                scannerView.setAutoFocus(false);
//                scannerView.startCamera();
//                scannerView.setAutoFocus(false);
//                scannerView.resumeCameraPreview(cls_05010000_Edicion.this::handleResult);
            } else {
                scannerView.pause();
                layoutEscaner.setVisibility(View.GONE);
            }
        });

    }

    public void mostrarMenuUsuario(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this::onMenuItemClick);
        popup.inflate(R.menu.mnu_00000001_menu_usuario);
        popup.show();
    }

    public boolean onMenuItemClick(MenuItem item) {
        try {
            int idControlClickeado = item.getItemId();
            if (idControlClickeado == R.id.opc_00000001_cambiar_clave_usuario_v) {
                dlg_PopUp = Funciones.obtenerDialogParaCambiarClave(this, objConfLocal, objSqlite, this);
                dlg_PopUp.show();
            } else if (idControlClickeado == R.id.opc_00000001_cerrar_sesion_v) {
                dlg_PopUp = Funciones.obtenerDialogParaCerrarSesion(this, objConfLocal, objSqlite, this);
                dlg_PopUp.show();
            } else return false;
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
            return false;
        }
        return true;
    }

    public void onClick(View view) {
        try {
            int idControlClickeado = view.getId();
            if (idControlClickeado == R.id.c007_txv_PushTituloVentana_v) {
                Funciones.popUpTablasPendientesDeEnviar(this);
            } else if (idControlClickeado == R.id.c007_txv_PushRed_v) {
                objSql.probarConexion(objConfLocal);
                Funciones.mostrarEstatusGeneral(this.getBaseContext(), objConfLocal, txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador);
            } else if (idControlClickeado == R.id.c007_txv_PushVersionApp_v || idControlClickeado == R.id.c007_txv_PushVersionDataBase_v) {
                Funciones.popUpStatusVersiones(this);
            } else if (idControlClickeado == R.id.c007_txv_PushIdentificador_v) {
//                showNoticeDialog();
                mostrarMenuUsuario(this.txv_PushIdentificador);
            }
            //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
            //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
            //...
            else if (idControlClickeado == R.id.c007_txv_Fecha) {
                PopUpCalendario d = new PopUpCalendario(this, c007_txv_Fecha);
                d.show();
            } else if (idControlClickeado == R.id.c007_lly_Turno_v) {
                PopUpBuscarEnLista d = new PopUpBuscarEnLista(this, arl_Turnos, c007_txv_Turno_Key, c007_txv_Turno_Val);
                d.show();
            } else if (idControlClickeado == R.id.c007_lly_Actividad_v) {
                PopUpBuscarEnLista d = new PopUpBuscarEnLista(this, arl_Actividades, c007_txv_Actividad_Key, c007_txv_Actividad_Val);
                d.show();
            } else if (idControlClickeado == R.id.c007_lly_Labor_v) {
                if (arl_Labores == null) {
                    Funciones.notificar(this, "Primero debe de seleccionar una actividad.");
                } else {
                    PopUpBuscarEnLista d = new PopUpBuscarEnLista(this, arl_Labores, c007_txv_Labor_Key, c007_txv_Labor_Val);
                    d.show();
                }
            } else if (idControlClickeado == R.id.c007_lly_Consumidor_v) {
                PopUpBuscarEnLista d = new PopUpBuscarEnLista(this, arl_Consumidores, c007_txv_Consumidor_Key, c007_txv_Consumidor_Val);
                d.show();
            } else if (idControlClickeado == R.id.c007_lly_Observacion_v) {
                PopUpObservacion d = new PopUpObservacion(this, tareoActual.getObservaciones(), c007_txv_Observacion);
                d.show();
            } else if (idControlClickeado == R.id.c007_fab_Agregar_v) {
                agregarDetalle("manual");
            } else if (idControlClickeado == R.id.c007_fab_Guardar_v) {
                guardarTareo();
            } else if (idControlClickeado == R.id.c007_fab_AbrirCerrarCabecera_v) {
                layoutAbierto = !layoutAbierto;
                manejarLayout();
            } else if (idControlClickeado == R.id.c007_fab_volver_v) {
                onBackPressed();
            } else throw new IllegalStateException("Click sin programacion: " + view.getId());
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
        }
    }

    public void duplicarRegistros(View view) {
        Swal.info(this, "CLICK", "DUPLICITY", 5000);
    }

    private void agregarDetalle(String tipoEntrada) throws Exception {

        boolean modoPacking = sharedPreferences.getBoolean("MODO_PACKING", false);
//            if (objSqlite.verificarExistenciaMarca(this.detalleActual.getDni(), validarMarca)) {
        // Obtener la fecha y hora actual
        Calendar calendar = Calendar.getInstance();

        // Formatear la fecha y hora actual
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String fechaHoraFormateada = sdf.format(calendar.getTime());

        //                AGREGAMOS EL VALOR A INGRESO O SALIDA SEGÚN CORRESPONDA
        //                CHECK ES SALIDA, UNCHECK ENTRADA
//            SI ES ENTRADA, QUE REALICE EL NUEVO REGISTRO, SI NO, QUE ACTUALICE LA SALIDA

        boolean validarMarca = sharedPreferences.getBoolean("PERMITIR_SIN_TAREO", true);
        boolean marcaExistente = false;
//        VALIDACION DE EXISTENCIA
        for (TareoDetalle td : tareoActual.getDetalle()) {
            if (td.getDni().equals(this.detalleActual.getDni()) && td.getIdActividad().equals(this.detalleActual.getIdActividad()) && td.getIdConsumidor().equals(this.detalleActual.getIdConsumidor()) && td.getIdLabor().equals(this.detalleActual.getIdLabor())) {
                marcaExistente = true;
                break;
            }
        }
        if (!marcaExistente) {
            detalleActual.setIngreso(fechaHoraFormateada);
            detalleActual.setSalida("");
            detalleActual.setIdEmpresa(tareoActual.getIdEmpresa());
            detalleActual.setIdTareo(tareoActual.getId());
            detalleActual.setItem(tareoActual.getDetalle().size() + 1);
//                        EVALUAMOS SI ESTÁ EN MODO PACKING O NO
            if (modoPacking) {
                detalleActual.setHoras(Double.valueOf("0"));
            } else {
                detalleActual.setHoras(Double.parseDouble(c007_etx_Horas.getText().length() > 0 ? c007_etx_Horas.getText().toString() : "0"));
            }
            detalleActual.setRdtos(Double.parseDouble(c007_etx_Rdtos.getText().length() > 0 ? c007_etx_Rdtos.getText().toString() : "0"));
            detalleActual.setDni(c007_atv_NroDocumento.getText().toString());

            List<String> p = new ArrayList<>();
            p.add(sharedPreferences.getString("ID_EMPRESA", "!ID_EMPRESA"));
            p.add(detalleActual.getDni());
            detalleActual.setIdPlanilla(objSqlite.doItBaby(objSqlite.obtQuery("OBTENER PLANILLA"), p, "READ", ""));

//                        ESTO SE AGREGA PARA COMPROBAR QUE LA PERSONA QUE SE ESTÁ TAREANDO EXISTA EN LOS REGISTROS DEL TELÉFONO AL MOMENTO DE REALIZAR EL TAREO
//                                EN CASO NO EXISTA, EL NOMBRE SERÁ AGREGADO COMO LA OBSERVACIÓN PARA QUE LO TENGA EN CUENTA EL ENCARGADO DE HACER
//                                EL CRUCE DE INFORMACIÓN
            Cursor cpersona = objSqlite.doItBaby("select count(*) from mst_personas where nrodocumento = '" + detalleActual.getDni() + "'", null, "READ");
            cpersona.moveToFirst();
            if (cpersona.getInt(0) > 0) {
                detalleActual.setNombres(c007_atv_NombreTrabajador.getText().toString());
            } else {
                detalleActual.setNombres("TRABAJADOR DESCONOCIDO");
                detalleActual.setObservacion(c007_atv_NombreTrabajador.getText().toString());
            }

//                        INTENSIVO

            if (validarDetalleTareo(detalleActual)) {
                tareoActual.agregarDetalle(detalleActual, this);
                Swal.success(ctx, "Correcto!", "Se ha agregado el tareo correctamente", 1500);
                listarDetalles();
            } else {
                if (tipoEntrada.equals("lector")) {
                    Swal.warning(this, "Aviso!", "Trabajador no encontrado, por favor, ingresa el nombre.", 3000);
                    c007_atv_NroDocumento.requestFocus();
                    c007_atv_NroDocumento.setEnabled(true);
                } else {
                    Funciones.notificar(this, "Datos incompletos. Revisar.");
                }
            }

        } else {
            Swal.warning(this, "Cuidado", "Ya existe el trabajador", 1500);
        }
        detalleActual.setDni("");
        detalleActual.setNombres("");
        c007_atv_NombreTrabajador.setText("");
        c007_atv_NroDocumento.setText("");
    }

    public void guardarTareo() {
        try {
            if (!tareoActual.getIdEstado().equals("TR")) {

                if (tareoActual.guardar(objSqlite, objConfLocal)) {
                    objSqlite.ActualizarDataPendiente(objConfLocal);
                    Funciones.mostrarEstatusGeneral(this.getBaseContext(), objConfLocal, txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador);
//                    SE QUITA PARA PODER CONTINUAR SIN AVISO
//                    Funciones.notificar(this, "Tareo guardado correctamente.");
                }
            } else {
                Funciones.notificar(this, "El tareo no cuenta con el estado PENDIENTE, imposible actualizar.");
            }
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
        }
    }

    private void manejarLayout() {
        // = c007_lly_Cabecera_v.getHeight() > 0;
        c007_lly_Cabecera.setVisibility(layoutAbierto ? View.VISIBLE : View.GONE);
        c007_lly_Actividad_Val.setVisibility(layoutAbierto ? View.VISIBLE : View.GONE);
        c007_lly_Labor_Val.setVisibility(layoutAbierto ? View.VISIBLE : View.GONE);
        c007_lly_Consumidor_Val.setVisibility(layoutAbierto ? View.VISIBLE : View.GONE);
        c007_lly_Observacion.setVisibility(layoutAbierto ? View.VISIBLE : View.GONE);
        c007_lly_Detalle2.setVisibility(layoutAbierto ? View.GONE : View.VISIBLE);
    }

    //@Jota:
    //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
    //...

    private void cargarControles() {
        try {
            Funciones.cargarAutoCompleteTextView(this, c007_atv_NroDocumento, ClaveValor.obtenerValores(ClaveValor.getArrayClaveValor(hmTablas.get("PERSONAS"), 0, 2)));
            Funciones.cargarAutoCompleteTextView(this, c007_atv_NombreTrabajador, ClaveValor.obtenerClaves(ClaveValor.getArrayClaveValor(hmTablas.get("PERSONAS"), 0, 2)));
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
        }

    }

    private void setearAutoCompleteTextViewNroDocumento(Tabla cv) {
        c007_atv_NroDocumento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                try {
                    obtenerDataTrabajador(cv);
                } catch (Exception e) {
                    Log.e("ERROR OBTENER", e.toString());
                }
            }
        });
    }

    private void obtenerDataTrabajador(Tabla cv) throws Exception {
        try {
            String dni = c007_atv_NroDocumento.getText().toString();
            String nombres = ClaveValor.obtenerValorDesdeClave(dni, ClaveValor.getArrayClaveValor(cv, 0, 2));
            if (!nombres.isEmpty()) {
                c007_atv_NombreTrabajador.setText(nombres);
                detalleActual.setDni(dni);
                detalleActual.setNombres(nombres);
            }
        } catch (Exception ex) {
            Log.e("ERROR", ex.toString());
            Swal.error(cls_05010000_Edicion.this, "Error", ex.toString(), 8000);
        }
    }

    private void setearAutoCompleteTextViewNombreTrabajador(Tabla cv) {
        c007_atv_NombreTrabajador.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                try {
                    String nombres = c007_atv_NombreTrabajador.getText().toString(); //ClaveValor.obtenerClaveDesdeValor(dni ,cv);
                    String dni = ClaveValor.obtenerClaveDesdeValor(nombres, ClaveValor.getArrayClaveValor(cv, 0, 2));
                    c007_atv_NroDocumento.setText(dni);
                    detalleActual.setDni(dni);
                    detalleActual.setNombres(nombres);
                } catch (Exception ex) {
                    Funciones.mostrarError(cls_05010000_Edicion.super.getBaseContext(), ex);
                }
            }
        });
    }

    private void obtenerDataParaControles() throws Exception {
        try {
            List<String> p = new ArrayList<>();
            p.add(sharedPreferences.getString("ID_EMPRESA", "!ID_EMPRESA"));
            //PENDIENTE: CREAR ESTAS CONSULTAS EN QUERYS SQLITE
            arl_Turnos = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Turnos"), p, "READ"));
            arl_Actividades = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Actividades"), p, "READ"));
            arl_Consumidores = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Consumidores"), p, "READ"));
            //arl_Conductores = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Conductores"),p,"READ"));
            hmTablas.put("PERSONAS", new Tabla(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Personas"), p, "READ")));
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
        }
    }

    private boolean validarDetalleTareo(TareoDetalle d) {
        if (d.getDni().length() < 8) return false;
        if (d.getNombres().trim().length() == 0) return false;
        if (d.getIdActividad().length() == 0) return false;
        if (d.getIdLabor().length() == 0) return false;
        if (d.getIdConsumidor().length() == 0) return false;
        return d.getHoras() >= 0;
    }

    public void mostrarValoresDocumentoActual() {
        boolean modoPacking = sharedPreferences.getBoolean("MODO_PACKING", false);
//        int i = ((AdapatadorSpinner) spiTurnos.getAdapter()).getIndex(tareoActual.getIdTurno());
//        spiTurnos.setSelection(i);
        if (tareoActual.getId().length() > 0) {
            c007_txv_Turno_Key.setText(tareoActual.getIdTurno());
            c007_txv_Turno_Val.setText(tareoActual.getIdTurno());

            c007_txv_Id.setText(tareoActual.getId());

            s_Fecha = String.format(tareoActual.getFecha().toString(), "yyyy-MM-dd");
            c007_txv_Fecha.setText(Funciones.malograrFecha(s_Fecha));
        }
    }

    public void eliminarDetalle(int item) {
        try {
            if (tareoActual.getIdEstado().equals("PE")) {
                tareoActual.eliminarItemDetalle(item);
                tareoActual.guardarDetalle(objSqlite);
                listarDetalles();
                Funciones.notificar(this, "Detalle elimiado: " + item);
            } else {
                Funciones.notificar(this, "El tareo no cuenta con el estado PENDIENTE, imposible actualizar.");
            }
        } catch (Exception ex) {
            Funciones.notificar(this, ex.getMessage());
        } finally {
            detallesSeleccionados = new ArrayList<>();
        }
    }

    //////////////////////////////////////////// PRUEBA 2
    public void popUpActualizarDetalleTareos(TareoDetalle detalle) {

        scannerView.pause();
        try {
            if (tareoActual.getIdEstado().equals("PE")) {
                Dialog popUp = new Dialog(this);
                popUp.setContentView(R.layout.popup_actualizar_detalle_tareo_021);
                // Aplica los atributos a la ventana del diálogo
                popUp.getWindow().setBackgroundDrawableResource(R.drawable.bg_popup);

                //MANEJO DE CONTROLES INTERNOR DEL POPUP
                EditText c021_etx_Horas, c021_etx_Rdtos, c021_etx_Observacion;
                TextView c021_txv_IdTareo, c021_txv_Item, c021_txv_Dni, c021_txv_NombreTrabajador, c021_txv_Actividad, c021_txv_Labor, c021_txv_Consumidor;
                ImageView c021_imv_Cerrar;
                FloatingActionButton c021_fab_Ok;

                c021_etx_Horas = popUp.findViewById(R.id.c021_etx_Horas_v);
                c021_etx_Rdtos = popUp.findViewById(R.id.c021_etx_Rdtos_v);
                c021_etx_Observacion = popUp.findViewById(R.id.c021_etx_Observacion_v);
                c021_txv_IdTareo = popUp.findViewById(R.id.c021_txv_IdTareo_v);
                c021_txv_Item = popUp.findViewById(R.id.c021_txv_Item_v);
                c021_txv_Dni = popUp.findViewById(R.id.c021_txv_Dni_v);
                c021_txv_NombreTrabajador = popUp.findViewById(R.id.c021_txv_NombreTrabajador_v);
                c021_txv_Actividad = popUp.findViewById(R.id.c021_txv_Actividad_v);
                c021_txv_Labor = popUp.findViewById(R.id.c021_txv_Labor_v);
                c021_txv_Consumidor = popUp.findViewById(R.id.c021_txv_Consumidor_v);
                c021_imv_Cerrar = popUp.findViewById(R.id.c021_imv_Cerrar_v);
                c021_fab_Ok = popUp.findViewById(R.id.c021_fab_Ok_v);

                c021_txv_IdTareo.setText(detalle.getIdTareo());
                c021_txv_Item.setText(String.valueOf(detalle.getItem()));
                c021_txv_Dni.setText(detalle.getDni());
                c021_txv_NombreTrabajador.setText(detalle.getNombres());
                String s;
                s = detalle.getIdActividad() + "-" + detalle.getActividad();
                c021_txv_Actividad.setText(s);
                s = detalle.getIdLabor() + "-" + detalle.getLabor();
                c021_txv_Labor.setText(s);
                s = detalle.getIdConsumidor() + "-" + detalle.getConsumidor();
                c021_txv_Consumidor.setText(s);
                c021_etx_Horas.setText(String.valueOf(detalle.getHoras()));
                c021_etx_Rdtos.setText(String.valueOf(detalle.getRdtos()));
                c021_etx_Observacion.setText(detalle.getObservacion());
                c021_fab_Ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        detalle.setHoras(Double.parseDouble(c021_etx_Horas.getText().toString()));
                        detalle.setRdtos(Double.parseDouble(c021_etx_Rdtos.getText().toString()));
                        detalle.setObservacion(c021_etx_Observacion.getText().toString());
                        try {
                            detalle.guardar(objSqlite, tareoActual.getIdUsuario());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        } finally {
                            listarDetalles();
                        }
                        popUp.dismiss();
                    }
                });

                c021_imv_Cerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popUp.dismiss();
                    }
                });

                //SETEA DIMENSIONES:
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                Double x = displayMetrics.widthPixels * 0.95;
                Double y = displayMetrics.heightPixels * 0.65;

                Log.i("VALUES", x + " " + y);

                popUp.getWindow().setLayout(x.intValue(), y.intValue());
                popUp.show();
            } else {
                Funciones.notificar(this, "El tareo no cuenta con el estado PENDIENTE, imposible actualizar.");
            }
        } catch (Exception ex) {
            Funciones.mostrarError(super.getBaseContext(), ex);
        } finally {
            setHandlerScanner(this);
//            scannerView.resumeCameraPreview(cls_05010000_Edicion.this::handleResult);
        }
    }

    @Override
    public void onBackPressed() {
        String value = c007_txv_Turno_Val.getText().toString();

        String estadoActual = tareoActual.getIdEstado();

        if (!estadoActual.equals("TR")) {
            Swal.confirm(this, "CUIDADO!", "DESEA GUARDAR EL TAREO ANTES DE SALIR?").setConfirmClickListener(sweetAlertDialog -> {
                if (value.isEmpty()) {
                    sweetAlertDialog.dismissWithAnimation();
                    Swal.warning(ctx, "Alerta!", "No puedes guardar un tareo sin turno.", 2000);
                } else {
                    guardarTareo();
                    finish();
                    sweetAlertDialog.dismissWithAnimation();
                }
            }).setCancelClickListener(sweetAlertDialog -> {
                finish();
                sweetAlertDialog.dismissWithAnimation();
            });
        } else {
            finish();
        }
    }
}