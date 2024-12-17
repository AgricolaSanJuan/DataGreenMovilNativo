//UPDATED:2022-03-09;
package com.example.datagreenmovil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
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
import org.json.JSONObject;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import cn.pedant.SweetAlert.SweetAlertDialog;
//import com.example.datagreenmovil.Logica.InterfazDialog;

public class cls_05010000_Edicion extends AppCompatActivity implements View.OnClickListener, ScaleGestureDetector.OnScaleGestureListener, cls_05010200_RecyclerViewAdapter.OnDataChangeListener {

    static ConexionSqlite objSqlite;
    private static boolean AGREGAR_SOLO_CAMARA_TAREOS = false;
    private static boolean REPRODUCIR_SONIDO_TAREOS = false;
    private static boolean MOSTRAR_CONTROLES_HORAS_TAREOS = false;
    private static int ITEMS_PER_PAGE = 10; // Cambia esto a la cantidad de elementos que desees por página
    //private Registro tareoActual;
    private final TareoDetalle detalleActual = new TareoDetalle();
    public String s_Fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //--desde, hasta, estado;);
    public LinearLayout c007_lly_Turno, c007_lly_Actividad, c007_lly_Labor, c007_lly_Consumidor, c007_lly_Cabecera, c007_lly_Actividad_Val, c007_lly_Labor_Val, c007_lly_Consumidor_Val, c007_lly_Observacion, c007_lly_Detalle2;
    public Switch switchTipoTareo;
    public ConstraintLayout layoutHoras, layoutRendimientos;
    ConexionBD objSql;
    ConfiguracionLocal objConfLocal;
    TextView txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador;
    Dialog dlg_PopUp;
    SharedPreferences sharedPreferences;
    HashMap<String, Tabla> hmTablas = new HashMap<>();
    ConstraintLayout layoutEscaner, llyNombres;
    TextView TotalRendimientos, TotalTrabajadores;
    ArrayList<Integer> detallesSeleccionados;
    ArrayList<PopUpBuscarEnLista_Item> arl_Turnos, arl_Actividades, arl_Labores = null, arl_Consumidores;
    boolean layoutAbierto = true;
    boolean flashState = false;
    boolean mostrarEscaner = false;
    private ScaleGestureDetector scaleGestureDetector;
    private ZXingScannerView scannerView;
    private ScannerViewModel scannerViewModel;
    private Context ctx;
    private SQLiteDatabase database;
    //CONTROLES;
    private RecyclerView c007_rvw_Detalle;
    private FloatingActionButton fabToggleFlash, fabMostrarEscaner, fabDuplicar, fabEditar, fabEliminar, fabFirst, fabLast, fabGenerarQR;
    private AutoCompleteTextView c007_atv_NroDocumento;
    private AutoCompleteTextView c007_atv_NombreTrabajador;
    private TextView c007_txv_Id, c007_txv_Fecha, c007_txv_Turno_Key, c007_txv_Turno_Val, c007_txv_Actividad_Key, c007_txv_Actividad_Val, c007_txv_Labor_Key, c007_txv_Labor_Val, c007_txv_Consumidor_Key, c007_txv_Consumidor_Val, c007_txv_Observacion;
    private EditText c007_etx_Horas, c007_etx_Rdtos;
    //VARIABLES;
    private String IdDocumentoActual = null;
    private Tareo tareoActual;
    private FloatingActionButton fabAgregar;
    private GestureDetector gestureDetector;
    private int currentPage = 0;
    private TextView txvPagination;
    private cls_05010200_RecyclerViewAdapter adaptadorLista;
    private int startIndex;
    private int endIndex;
    private MediaPlayer mediaPlayer;
    private boolean VALIDAR_TRABAJADOR_REPETIDO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_05010000_edicion_007);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ctx = this;

        detallesSeleccionados = new ArrayList<>();

        scannerView = findViewById(R.id.scannerView);

        sharedPreferences = this.getSharedPreferences("objConfLocal", MODE_PRIVATE);

//        DEFINIMOS PARAMETROS PARA LA PAGINACIÓN
        ITEMS_PER_PAGE = sharedPreferences.getInt("ITEMS_PER_PAGE_TAREOS", 20);
        MOSTRAR_CONTROLES_HORAS_TAREOS = sharedPreferences.getBoolean("MOSTRAR_CONTROLES_HORAS_TAREOS", false);
        AGREGAR_SOLO_CAMARA_TAREOS = sharedPreferences.getBoolean("AGREGAR_SOLO_CAMARA_TAREOS", false);
        REPRODUCIR_SONIDO_TAREOS = sharedPreferences.getBoolean("REPRODUCIR_SONIDO_TAREOS", false);

        VALIDAR_TRABAJADOR_REPETIDO = sharedPreferences.getBoolean("VALIDAR_TRABAJADOR_REPETIDO", false);

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
            switchTipoTareo = findViewById(R.id.swTipoTareo);
            txvPagination = findViewById(R.id.txvPagination);
            layoutHoras = findViewById(R.id.layoutHoras);
            layoutRendimientos = findViewById(R.id.layoutRendimientos);
            fabAgregar = findViewById(R.id.c007_fab_Agregar_v);

            if (!MOSTRAR_CONTROLES_HORAS_TAREOS) {
                layoutHoras.setMaxHeight(0);
                layoutRendimientos.setMaxHeight(0);
            }

            if (AGREGAR_SOLO_CAMARA_TAREOS) {
                llyNombres.setMaxHeight(0);
            }

            if (modoPacking) {
                llyNombres.setMaxHeight(0);
                switchTipoTareo.setVisibility(View.VISIBLE);
//                txvPagination.setVisibility(View.VISIBLE);
                layoutHoras.setVisibility(View.GONE);
                layoutRendimientos.setVisibility(View.GONE);
                fabAgregar.setVisibility(View.INVISIBLE);
                c007_atv_NombreTrabajador.setVisibility(View.INVISIBLE);
                c007_atv_NombreTrabajador.setHeight(1);
            } else {
                // Definir LayoutParams con wrap_content para ancho y alto
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                switchTipoTareo.setVisibility(View.GONE);
//                txvPagination.setVisibility(View.GONE);
                layoutHoras.setVisibility(View.VISIBLE);
                layoutRendimientos.setVisibility(View.VISIBLE);
//                c007_atv_NombreTrabajador.setLayoutParams(params);
                c007_atv_NombreTrabajador.setVisibility(View.VISIBLE);
                fabAgregar.setVisibility(View.VISIBLE);
            }

            if (switchTipoTareo != null) {
                switchTipoTareo.setOnCheckedChangeListener((compoundButton, b) -> {
                    if (!b) {
                        switchTipoTareo.setText("INGRESO");
                    } else {
                        switchTipoTareo.setText("SALIDA");
                    }
                });
            }

            c007_txv_Fecha.setText(Funciones.malograrFecha(s_Fecha));
            Funciones.mostrarEstatusGeneral(this.getBaseContext(), objConfLocal, txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador);

            tareoActual = new Tareo(IdDocumentoActual, objSqlite, this);
            detalleActual.setIdEmpresa(tareoActual.getIdEmpresa());
            obtenerDataParaControles();
            cargarControles();
            setearControles();
            listarDetalles();
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
        }

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float diffX = e2.getX() - e1.getX();
                float diffY = e2.getY() - e1.getY();

                // Convertir 40 mm (4 cm) a píxeles
                float minDistanceInMm = 30f; // 4 cm = 40 mm
                float minDistanceInPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, minDistanceInMm, getResources().getDisplayMetrics());
                if (Math.abs(diffX) > Math.abs(diffY) && Math.abs(diffX) > minDistanceInPixels && Math.abs(velocityX) > 100) {
                    if (diffX > 0) {
                        onSwipeRight();
                    } else {
                        onSwipeLeft();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void onSwipeRight() {
        previousPage();
    }

    private void onSwipeLeft() {
        nextPage();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("TOUCH", "Touch event received");
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    void setHandlerScanner() {
        scannerView.resume();
        scannerView.setResultHandler(result -> {
            Context ctx = this;
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
                    setHandlerScanner();
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

    public void listarDetalles() {
        try {
            manejarLayout();

            // Calcular el inicio y el fin de la lista para la paginación
            startIndex = currentPage * ITEMS_PER_PAGE;
            endIndex = Math.min(startIndex + ITEMS_PER_PAGE, tareoActual.getDetalle().size());
            txvPagination.setText("Del: " + (startIndex + 1) + " Al: " + endIndex);
            // Obtener la sublista para la página actual
            List<TareoDetalle> detallesPaginated = tareoActual.getDetalle().subList(startIndex, endIndex);

            adaptadorLista = new cls_05010200_RecyclerViewAdapter(this, detallesPaginated);

            adaptadorLista.setOnDataChangeListener(this);

            adaptadorLista.setOnItemSelected((item, agregar) -> {
                if (agregar) {
                    detallesSeleccionados.add(Integer.parseInt(item));
                } else {
                    int valorComoInteger = Integer.parseInt(item);
                    detallesSeleccionados.remove((Integer) valorComoInteger);
                }
            });

            c007_rvw_Detalle.setAdapter(adaptadorLista);

            c007_rvw_Detalle.setLayoutManager(new LinearLayoutManager(this));

            mostrarValoresDocumentoActual();

            c007_rvw_Detalle.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                    gestureDetector.onTouchEvent(e);
                    return false;
                }

                @Override
                public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                }
            });

            obtenerUltimaData();

            Double sumaTotal = adaptadorLista.obtenerTotalRendimientos();
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String sumaFormateada = decimalFormat.format(sumaTotal);

            TotalRendimientos.setText(String.valueOf(sumaFormateada));
            int cantidadRegistros = adaptadorLista.getItemCount();
            TotalTrabajadores.setText(String.valueOf(cantidadRegistros));

            if (cantidadRegistros > 0) {
                layoutAbierto = false;
                manejarLayout();
            }
        } catch (Exception e) {
            Log.e("ERRORLISTAR", e.toString());
        }
    }

    public void obtenerUltimaData() {
        boolean modoPacking = sharedPreferences.getBoolean("MODO_PACKING", false);
//        if (adaptadorLista. getItemCount() > 0) {
        if (!tareoActual.getDetalle().isEmpty()) {
            String lastActividad, lastLabor, lastConsumidor, lastHoras, lastRendimientos;

            int cantidad = tareoActual.getTotalDetalles();
            TareoDetalle lastDetalle = tareoActual.getDetalle().get(cantidad - 1);

            lastActividad = lastDetalle.getIdActividad();
            lastLabor = lastDetalle.getIdLabor();
            lastConsumidor = lastDetalle.getIdConsumidor();
            lastHoras = String.valueOf(lastDetalle.getHoras());
            lastRendimientos = String.valueOf(lastDetalle.getRdtos());
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
    }

    public void nextPage() {
        if ((currentPage + 1) * ITEMS_PER_PAGE < tareoActual.getDetalle().size()) {
            currentPage++;
            actualizarDatos();
        }
    }

    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            actualizarDatos();
        }
    }

    public void firstPage() {
        if (currentPage > 0) {
            currentPage = 0;
            actualizarDatos();
        }
    }

    public void lastPage() {
        currentPage = tareoActual.getDetalle().size() / ITEMS_PER_PAGE;
//        listarDetalles();
        actualizarDatos();
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
        Log.i("ESCANER", barcodeValue);
        if (c007_txv_Consumidor_Key.length() > 0 && c007_txv_Actividad_Key.length() > 0 && c007_txv_Labor_Key.length() > 0) {
            boolean modoPacking = sharedPreferences.getBoolean("MODO_PACKING", false);
            c007_atv_NroDocumento.setText(barcodeValue.replaceAll("[^0-9]", ""));
            Calendar calendar = Calendar.getInstance();
            // Formatear la fecha y hora actual
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String fechaHoraFormateada = sdf.format(calendar.getTime());
            Cursor cVerificarSinSalida;
            try {
                if (switchTipoTareo != null && !switchTipoTareo.isChecked()) {
                    String[] selectionArgs = {tareoActual.getId(), c007_atv_NroDocumento.getText().toString()};
                    cVerificarSinSalida = database.rawQuery("SELECT * FROM TRX_TAREOS_DETALLE td inner join trx_tareos t WHERE td.Idtareo = ? AND td.Dni = ? AND td.salida = '';", selectionArgs);
                    if (cVerificarSinSalida.getCount() > 0 && modoPacking && VALIDAR_TRABAJADOR_REPETIDO) {
                        Swal.warning(this, "Cuidado", "El trabajador tiene un tareo sin salida, registra su salida y luego vuelve a intentarlo.", 5000);
                    } else {
                        //                if (!switchTipoTareo.isChecked()) {
                        obtenerDataTrabajador(hmTablas.get("PERSONAS"));
                        agregarDetalle("lector");
                    }
                } else {
                    String[] selectionArgs = {tareoActual.getId(), c007_atv_NroDocumento.getText().toString()};
                    cVerificarSinSalida = database.rawQuery("SELECT * FROM TRX_TAREOS_DETALLE WHERE Idtareo = ? AND Dni = ? AND salida = ''", selectionArgs);
                    if (cVerificarSinSalida.getCount() > 0) {
                        cVerificarSinSalida.moveToFirst();
                        int itemIndex;
                        itemIndex = cVerificarSinSalida.getColumnIndex("Item");
                        String itemIndexU;
                        itemIndexU = cVerificarSinSalida.getString(itemIndex);
                        Optional<TareoDetalle> resultado = tareoActual.getDetalle().stream().filter(detalle -> detalle.getItem() == Integer.parseInt(itemIndexU)).findFirst();
                        Date date1 = sdf.parse(resultado.get().getIngreso());
                        Date date2 = sdf.parse(fechaHoraFormateada);
                        // Calcular la diferencia en milisegundos
                        long diferenciaMilisegundos = date2.getTime() - date1.getTime();
                        // Convertir la diferencia de milisegundos a horas
                        resultado.get().setSalida(fechaHoraFormateada);
                        double horas = diferenciaMilisegundos / 3600000.00;
                        BigDecimal horasRedondeadas = new BigDecimal(horas).setScale(2, RoundingMode.HALF_UP);
                        resultado.get().setHoras(horasRedondeadas.doubleValue());
                    } else {
                        Swal.warning(this, "Cuidado", "No se encuentra un tareo de ingreso de este trabajador, pruebe a guardar el tareo y volver a intentar.", 2000);
                    }
                }
                guardarTareo();
                cVerificarSinSalida.close();
                tareoActual = new Tareo(IdDocumentoActual, objSqlite, this);
                actualizarDatos();
            } catch (Exception e) {
                Log.e("ERROR!", e.getMessage());
                Swal.error(this, "Error!", e.toString(), 8000);
            } finally {
                c007_atv_NroDocumento.setText("");
                c007_atv_NombreTrabajador.setText("");
            }

        } else {
            Swal.warning(this, "Alerta!", "No se ha agregado el tareo por que faltan campos", 1500);
        }
    }

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
                detalleActual.setIdActividad(c007_txv_Actividad_Key.getText().toString());

                List<String> p = new ArrayList<>();
                p.add(tareoActual.getIdEmpresa()); //PENDIENTE: OBTENER EMPRESA DINAMICAMENTE;
                p.add(detalleActual.getIdActividad());
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

        llyNombres = findViewById(R.id.llyNombres);

        //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
        //...
//        c007_fab_Guardar = findViewById(R.id.c007_fab_Guardar_v);
        fabDuplicar = findViewById(R.id.c007_fab_Duplicar_v);
        fabEditar = findViewById(R.id.c007_fab_Editar_v);
        fabEliminar = findViewById(R.id.c007_fab_Eliminar_v);
        fabGenerarQR = findViewById(R.id.c007_fab_GenerarQR_v);

        fabFirst = findViewById(R.id.First);
        fabLast = findViewById(R.id.Last);
//        c007_fab_AbrirCerrarCabecera = findViewById(R.id.c007_fab_AbrirCerrarCabecera_v);

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
                    scannerViewModel.setScannedCode(null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });

        fabGenerarQR.setOnClickListener(v -> {
            navigateToQR();
        });


        fabEliminar.setOnClickListener(view -> {
            if (detallesSeleccionados.size() > 0) {
                Swal.confirm(ctx, "¿Estás seguro?", "Estás seguro que deseas eliminar los tareos seleccionados?").setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    String whereIn = "(";
                    for (int i = 0; i < detallesSeleccionados.size(); i++) {
                        if (i == detallesSeleccionados.size() - 1) {
                            whereIn += "'" + detallesSeleccionados.get(i) + "')";
                            Log.i("CANTIDAD - 1", String.valueOf(i));
                        } else {
                            whereIn += "'" + detallesSeleccionados.get(i) + "',";
                        }
                    }
                    try {
                        detallesSeleccionados.sort(Collections.reverseOrder());
                        for (Integer item : detallesSeleccionados) {
                            tareoActual.eliminarItemDetalle(item);
                            tareoActual.guardarDetalle(objSqlite);
                        }
                        tareoActual.guardar(objSqlite, null);
                        tareoActual = new Tareo(IdDocumentoActual, objSqlite, this);
                        actualizarDatos();
                    } catch (Exception e) {
                        Swal.error(ctx, "Oops!", "Ocurrió un error al eliminar los detalles seleccionados", 5000);
                    } finally {
                        detallesSeleccionados = new ArrayList<>();
                    }

                }).setCancelClickListener(SweetAlertDialog::dismissWithAnimation);
            } else {
                Swal.warning(ctx, "¡Cuidado!", "Selecciona al menos un trabajador antes.", 2000);
            }

        });

        fabFirst.setOnClickListener(v -> {
            previousPage();
            detallesSeleccionados = new ArrayList<>();
        });

        fabFirst.setOnLongClickListener(v -> {
            firstPage();
            detallesSeleccionados = new ArrayList<>();
            return false;
        });

        fabLast.setOnClickListener(v -> {
            nextPage();
            detallesSeleccionados = new ArrayList<>();
        });

        fabLast.setOnLongClickListener(v -> {
            lastPage();
            detallesSeleccionados = new ArrayList<>();
            return false;
        });

        fabEditar.setOnClickListener(view -> {
            if (detallesSeleccionados.size() > 0) {
                Swal.customDialog(ctx, "editar", tareoActual, detallesSeleccionados, (result, sweetAlertDialog1) -> {
                }, (success, message) -> {
                    if (success) {
                        Swal.success(ctx, "Correcto!", message, 1500);
                        actualizarDatos();
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
                        actualizarDatos();
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

                setHandlerScanner();

            } else {
                scannerView.pause();
                layoutEscaner.setVisibility(View.GONE);
            }
        });
    }

    public void navigateToQR() {
        JSONArray jsonDetallesPersonal = new JSONArray();
        try {

            Cursor cursorTrabajadores = objSqlite.doItBaby("select Dni from trx_tareos_detalle where Idtareo = '"+tareoActual.getId()+"'", null, "READ");

            while (cursorTrabajadores.moveToNext()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("dni", cursorTrabajadores.getString(0));
                jsonDetallesPersonal.put(jsonObject);
            }
            Log.i("detallestrabajadores", jsonDetallesPersonal.toString());
        } catch (Exception e) {
            Swal.error(ctx, "Oops!", "No se han podido obtener los trabajadores", 5000);
        }
//        // Obtener los trabajadores de la actividad implementando la interfaz
//        // Crear Bundle y pasar los trabajadores como String
        Bundle parametros = new Bundle();
        parametros.putString("trabajadores", jsonDetallesPersonal.toString());

        Intent intent = new Intent(this, QRViewActivity.class);
        intent.putExtras(parametros);

        startActivity(intent);
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
            } else if (idControlClickeado == R.id.c007_txv_Fecha) {
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

    private void agregarDetalle(String tipoEntrada) throws Exception {

        boolean modoPacking = sharedPreferences.getBoolean("MODO_PACKING", false);
        Calendar calendar = Calendar.getInstance();

        // Formatear la fecha y hora actual
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String fechaHoraFormateada = sdf.format(calendar.getTime());

        boolean validarMarca = sharedPreferences.getBoolean("PERMITIR_SIN_TAREO", true);
        boolean marcaExistente = false;
//        VALIDACION DE EXISTENCIA
        for (TareoDetalle td : tareoActual.getDetalle()) {
            if (VALIDAR_TRABAJADOR_REPETIDO && td.getDni().equals(this.detalleActual.getDni()) && td.getIdActividad().equals(this.detalleActual.getIdActividad()) && td.getIdConsumidor().equals(this.detalleActual.getIdConsumidor()) && td.getIdLabor().equals(this.detalleActual.getIdLabor())) {
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

            detalleActual.setHomologar(0);

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

                if (REPRODUCIR_SONIDO_TAREOS) {
                    mediaPlayer = MediaPlayer.create(ctx, R.raw.notificacion);
                    mediaPlayer.start();
                } else {
                    Swal.success(ctx, "Correcto!", "Se ha agregado el tareo correctamente", 1200);
                }
//                listarDetalles();
                actualizarDatos();
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

    public void eliminarDetalles() {
//        detallesSeleccionados
    }

    public void eliminarDetalle(int item) {
        try {
            if (tareoActual.getIdEstado().equals("PE")) {
                tareoActual.eliminarItemDetalle(item);
                tareoActual.guardarDetalle(objSqlite);
//                List<TareoDetalle> detallesPaginated = tareoActual.getDetalle().subList(startIndex, endIndex);
                actualizarDatos();
                Funciones.notificar(this, "Detalle eliminado: " + item);
            } else {
                Swal.warning(ctx, "No se puede realizar la acción", "El tareo no cuenta con el estado PENDIENTE, imposible actualizar.", 5000);
            }
        } catch (Exception ex) {
            Swal.error(ctx, "Oops!", ex.getMessage(), 8000);
        } finally {
            detallesSeleccionados = new ArrayList<>();
        }
    }

    public void popUpActualizarDetalleTareos(TareoDetalle detalle) {

        scannerView.pause();
        try {
            if (tareoActual.getIdEstado().equals("PE")) {
                Dialog popUp = new Dialog(this);
                popUp.setContentView(R.layout.popup_actualizar_detalle_tareo_021);
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
//                            listarDetalles();
                            actualizarDatos();
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
            setHandlerScanner();
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

    @Override
    public void onDataChanged() {
        // Aquí puedes notificar al adaptador sobre los cambios
        if (adaptadorLista != null) {
            adaptadorLista.notifyDataSetChanged();
        }
    }

    // Método que se ejecuta cuando tienes nuevos datos
    public void actualizarDatos() {
        obtenerUltimaData();
        startIndex = currentPage * ITEMS_PER_PAGE;
        endIndex = Math.min(startIndex + ITEMS_PER_PAGE, tareoActual.getDetalle().size());
        List<TareoDetalle> nuevosDatos = tareoActual.getDetalle().subList(startIndex, endIndex);
        adaptadorLista.addData(nuevosDatos);
        adaptadorLista.notifyDataChangedExternally();
        txvPagination.setText("Del: " + (startIndex + 1) + " Al: " + endIndex);
    }
}