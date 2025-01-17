//UPDATED:2022-03-09;
package com.example.datagreenmovil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.datagreenmovil.Conexiones.AppDatabase;
import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.DAO.Tareo.TrxTareo.TareoDAO;
import com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle.ActividadHelper;
import com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle.ConsumidorHelper;
import com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle.LaborHelper;
import com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle.PersonaHelper;
import com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle.TareoDetalles;
import com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle.TareoDetallesDAO;
import com.example.datagreenmovil.Entidades.ClaveValor;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.PopUpBuscarEnLista;
import com.example.datagreenmovil.Entidades.PopUpBuscarEnLista_Item;
import com.example.datagreenmovil.Entidades.PopUpCalendario;
import com.example.datagreenmovil.Entidades.PopUpObservacion;
import com.example.datagreenmovil.Entidades.Tabla;
import com.example.datagreenmovil.Logica.CryptorSJ;
import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.Logica.QRCodeScannerView;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.Scanner.ui.ScannerViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class cls_05010000_Edicion extends AppCompatActivity implements View.OnClickListener, ScaleGestureDetector.OnScaleGestureListener, cls_05010200_RecyclerViewAdapter.OnDataChangeListener {

    static ConexionSqlite objSqlite;
    private static boolean AGREGAR_SOLO_CAMARA_TAREOS = false;
    private static boolean REPRODUCIR_SONIDO_TAREOS = false;
    private static boolean MOSTRAR_CONTROLES_HORAS_TAREOS = false;
    private static boolean MOSTRAR_CONTROLES_RDTOS_TAREOS = false;
    private static boolean SALIDA_AUTOMATICA = false;
    private static boolean MOSTRAR_ENTRADA_SALIDA = false;
    private static int ITEMS_PER_PAGE = 10;
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
    ConstraintLayout layoutEscaner, llyNombres;
    TextView TotalRendimientos, TotalTrabajadores;
    ArrayList<Integer> detallesSeleccionados;
    ArrayList<PopUpBuscarEnLista_Item> arl_Turnos, arl_Actividades, arl_Labores = null, arl_Consumidores;
    boolean layoutAbierto = true;
    boolean flashState = false;
    boolean mostrarEscaner = false;
    //    REFACTORIZACIÓN
    AppDatabase db;
    TareoDAO tareoDAO;
    TareoDetallesDAO tareoDetallesDAO;
    com.example.datagreenmovil.DAO.Tareo.TrxTareo.Tareo tareoEnFuncion; /* = new com.example.datagreenmovil.DAO.Tareo.TrxTareo.Tareo();*/
    List<TareoDetalles> tareoDetalleList = new ArrayList<>();
    TareoDetalles nuevoDetalle;
    private ScaleGestureDetector scaleGestureDetector;
    private QRCodeScannerView qrCodeScannerView;
    private ScannerViewModel scannerViewModel;
    private Context ctx;
    //CONTROLES;
    private RecyclerView c007_rvw_Detalle;
    private FloatingActionButton fabToggleFlash, fabMostrarEscaner, fabDuplicar, fabEditar, fabEliminar, fabFirst, fabLast, fabGenerarQR;
    private AutoCompleteTextView c007_atv_NroDocumento;
    private AutoCompleteTextView c007_atv_NombreTrabajador;
    private TextView c007_txv_Id, c007_txv_Fecha, c007_txv_Turno_Key, c007_txv_Turno_Val, c007_txv_Actividad_Key, c007_txv_Actividad_Val, c007_txv_Labor_Key, c007_txv_Labor_Val, c007_txv_Consumidor_Key, c007_txv_Consumidor_Val, c007_txv_Observacion;
    private EditText c007_etx_Horas, c007_etx_Rdtos;
    private String IdDocumentoActual = null;
    private FloatingActionButton fabAgregar;
    private GestureDetector gestureDetector;
    private int currentPage = 0;
    private TextView txvPagination;
    private cls_05010200_RecyclerViewAdapter adaptadorLista;
    private int startIndex;
    private int endIndex;
    private MediaPlayer mediaPlayer;
    private boolean VALIDAR_TRABAJADOR_REPETIDO;
    private String ID_USUARIO_ACTUAL;

//    REFACTORIZACIÓN

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_05010000_edicion_007);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ctx = this;

        detallesSeleccionados = new ArrayList<>();

        sharedPreferences = this.getSharedPreferences("objConfLocal", MODE_PRIVATE);
        db = DataGreenApp.getAppDatabase();
        ITEMS_PER_PAGE = sharedPreferences.getInt("ITEMS_PER_PAGE_TAREOS", 20);
        MOSTRAR_CONTROLES_HORAS_TAREOS = sharedPreferences.getBoolean("MOSTRAR_CONTROLES_HORAS_TAREOS", false);
        MOSTRAR_CONTROLES_RDTOS_TAREOS = sharedPreferences.getBoolean("MOSTRAR_CONTROLES_RDTOS_TAREOS", false);
        AGREGAR_SOLO_CAMARA_TAREOS = sharedPreferences.getBoolean("AGREGAR_SOLO_CAMARA_TAREOS", false);
        REPRODUCIR_SONIDO_TAREOS = sharedPreferences.getBoolean("REPRODUCIR_SONIDO_TAREOS", false);
        SALIDA_AUTOMATICA = sharedPreferences.getBoolean("SALIDA_AUTOMATICA", false);
        MOSTRAR_ENTRADA_SALIDA = sharedPreferences.getBoolean("MOSTRAR_ENTRADA_SALIDA", false);

        ID_USUARIO_ACTUAL = sharedPreferences.getString("ID_USUARIO_ACTUAL", "!ID_USUARIO_ACTUAL");

        VALIDAR_TRABAJADOR_REPETIDO = sharedPreferences.getBoolean("VALIDAR_TRABAJADOR_REPETIDO", false);

        scaleGestureDetector = new ScaleGestureDetector(this, this);
        try {
            if (getIntent().getExtras() != null) {
                objConfLocal = (ConfiguracionLocal) getIntent().getSerializableExtra("ConfiguracionLocal");
                IdDocumentoActual = (String) getIntent().getSerializableExtra("IdDocumentoActual");
            }

            //        REFACTORIZANDO
            db = AppDatabase.getDatabase();
            tareoDAO = db.tareoDAO();
            tareoDetallesDAO = db.tareoDetallesDAO();
//        SETEAMOS EL TAREO EXISTENTE O CREAMOS UNO NUEVO
            if (IdDocumentoActual == null) {
                String last = tareoDAO.getLastId();
                String nuevoId = Funciones.siguienteCorrelativo(last, 'A');
                if (nuevoId.length() == 9) {
                    nuevoId = sharedPreferences.getString("ID_DISPOSITIVO", "!ID_DISPOSITIVO") + nuevoId;
                }
                IdDocumentoActual = nuevoId;
                tareoEnFuncion = new com.example.datagreenmovil.DAO.Tareo.TrxTareo.Tareo();
                tareoEnFuncion.setId(nuevoId);
                tareoEnFuncion.setIdEmpresa("01");
                tareoEnFuncion.setIdEstado("PE");
                tareoEnFuncion.setIdUsuarioCrea(ID_USUARIO_ACTUAL);
                tareoEnFuncion.setFechaHoraCreacion(getCurrentFormattedDate());
                tareoEnFuncion.setIdUsuarioActualiza(ID_USUARIO_ACTUAL);
                tareoEnFuncion.setFechaHoraActualizacion(getCurrentFormattedDate());
                Log.i("IDTAREO", "nuevo");
            } else {
                tareoEnFuncion = tareoDAO.obtenerTareoPorId(IdDocumentoActual);
                Log.i("IDTAREO", "existente");
            }
            generarNuevoDetalle(IdDocumentoActual);
//        SETEAMOS EL TAREO EXISTENTE O CREAMOS UNO NUEVO
            tareoDetalleList = tareoDetallesDAO.obtenerDetalles(IdDocumentoActual);
//        REFACTORIZANDO

            objSql = new ConexionBD(objConfLocal);
            objSqlite = new ConexionSqlite(this, DataGreenApp.DB_VERSION());
            referenciarControles();

            switchTipoMarcacion = findViewById(R.id.swTipoTareo);
            txvPagination = findViewById(R.id.txvPagination);
            layoutHoras = findViewById(R.id.layoutHoras);
            layoutRendimientos = findViewById(R.id.layoutRendimientos);
            fabAgregar = findViewById(R.id.c007_fab_Agregar_v);

            if (!MOSTRAR_CONTROLES_HORAS_TAREOS) {
                layoutHoras.setVisibility(View.GONE);
            }

            if (!MOSTRAR_CONTROLES_RDTOS_TAREOS) {
                layoutRendimientos.setVisibility(View.GONE);
            }

            if (AGREGAR_SOLO_CAMARA_TAREOS) {
                llyNombres.setMaxHeight(0);
                c007_atv_NombreTrabajador.setVisibility(View.INVISIBLE);
                c007_atv_NombreTrabajador.setHeight(1);
                fabAgregar.setVisibility(View.INVISIBLE);
            }

            if (MOSTRAR_ENTRADA_SALIDA) {
                switchTipoMarcacion.setVisibility(View.GONE);

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

            c007_txv_Fecha.setText(Funciones.malograrFecha(s_Fecha));
            Funciones.mostrarEstatusGeneral(this.getBaseContext(), objConfLocal, txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador);

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
        tareoEnFuncion.setFecha(Funciones.arreglarFecha(c007_txv_Fecha.getText().toString()));

        qrCodeScannerView = findViewById(R.id.scannerView);
        qrCodeScannerView.setImagesParams(75, 50, 210, 20, 5);
        qrCodeScannerView.setOnCodeScannedListener(result -> {
            String resultadoDesencriptado = "";
            boolean LECTURA_CONTINUA = sharedPreferences.getBoolean("LECTURA_CONTINUA", false);
            int DELAY_POR_LECTURA = (sharedPreferences.getInt("DELAY_POR_LECTURA", 1) * 1000);

            if (LECTURA_CONTINUA) {
                qrCodeScannerView.stopScanning();
                if (result.length() == 10 && !(String.valueOf(result.charAt(0)).equals("S"))) {
                    try {
                        resultadoDesencriptado = CryptorSJ.desencriptarCadena(result);
                    } catch (Exception e) {
                        Swal.warning(ctx, "Cuidado", "La cadena no cumple con el formato de San Juan.", 2000);
                    }
                } else {
                    resultadoDesencriptado = result;
                }
                resultado(resultadoDesencriptado);
                c007_atv_NroDocumento.setText(resultadoDesencriptado);

                new Handler().postDelayed(() -> {
                    qrCodeScannerView.startCamera();
                }, DELAY_POR_LECTURA);
            }else {
                if (result.length() == 10 && !(String.valueOf(result.charAt(0)).equals("S"))) {
                    try {
                        resultadoDesencriptado = CryptorSJ.desencriptarCadena(result);
                    } catch (Exception e) {
                        Swal.warning(ctx, "Cuidado", "La cadena no cumple con el formato de San Juan.", 2000);
                    }
                } else {
                    resultadoDesencriptado = result;
                }

                PersonaHelper personaHelper = new PersonaHelper(resultadoDesencriptado);
                nuevoDetalle.setNombres(personaHelper.obtenerNombreTrabajador());

                c007_atv_NroDocumento.setText(resultadoDesencriptado);

                c007_atv_NombreTrabajador.setText(personaHelper.obtenerNombreTrabajador());
            }
        });
    }

    private void generarNuevoDetalle(String nuevoId) {
        nuevoDetalle = new TareoDetalles();
        int lasItem = tareoDetalleList.isEmpty() ? 1 : tareoDetalleList.get(tareoDetalleList.size() - 1).getItem() + 1;
        nuevoDetalle.setIdTareo(nuevoId);
        nuevoDetalle.setItem(lasItem);
        nuevoDetalle.setHomologar(0);
        nuevoDetalle.setIdEmpresa(tareoEnFuncion.getIdEmpresa());
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

//    void setHandlerScanner() {
//        scannerView.resume();
//        scannerView.setResultHandler(result -> {
//            Context ctx = this;
//            String resultadoDesencriptado = "";
//            if (result.getText().length() == 10 && !(String.valueOf(result.getText().charAt(0)).equals("S"))) {
//                try {
//                    resultadoDesencriptado = CryptorSJ.desencriptarCadena(result.getText());
//                } catch (Exception e) {
//                    Swal.warning(ctx, "Cuidado", "La cadena no cumple con el formato de San Juan.", 2000);
//                }
//            } else {
//                resultadoDesencriptado = result.getText();
//            }
//            CountDownTimer countDownTimer = new CountDownTimer(1500, 1500) {
//                @Override
//                public void onTick(long millisUntilFinished) {
//                }
//
//                @Override
//                public void onFinish() {
//                    setHandlerScanner();
//                }
//            };
//            resultado(resultadoDesencriptado);
//            countDownTimer.start();
//        });
//    }

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
            endIndex = Math.min(startIndex + ITEMS_PER_PAGE, tareoDetalleList.size());
            txvPagination.setText("Del: " + (startIndex + 1) + " Al: " + endIndex);
            List<TareoDetalles> detallesPaginados = tareoDetalleList.subList(startIndex, endIndex);

            adaptadorLista = new cls_05010200_RecyclerViewAdapter(this, detallesPaginados, db, IdDocumentoActual);

            adaptadorLista.setOnDataChangeListener(this);

            adaptadorLista.setOnItemSelected((item, agregar) -> {
                if (agregar) {
                    detallesSeleccionados.add(Integer.parseInt(item));
                } else {
                    int valorComoInteger = Integer.parseInt(item);
                    detallesSeleccionados.remove((Integer) valorComoInteger);
                }
            });

            adaptadorLista.setOnButtonClickListener((texto) -> {
                Swal.customDialog(ctx, "duplicar", tareoDetalleList, detallesSeleccionados, /*(result, sweetAlertDialog1) -> {
                },*/ (success, message, sweetAlertDialog) -> {
                    if (success) {
                        Swal.success(ctx, "Correcto!", message, 1500);
                        actualizarDatos();
                        detallesSeleccionados = new ArrayList<>();
                    } else {
                        Swal.error(ctx, "Oops!", message, 1500);
                        detallesSeleccionados = new ArrayList<>();
                    }
                });
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
            int cantidadRegistros = tareoDetalleList.size();
            TotalTrabajadores.setText(String.valueOf(cantidadRegistros));

            if (cantidadRegistros > 0 && !tareoDetalleList.get(0).getDni().isEmpty()) {
                layoutAbierto = false;
                manejarLayout();
            }
        } catch (Exception e) {
            Log.e("ERRORLISTAR", e.toString());
        }
    }

    public void obtenerUltimaData() {
        boolean modoPacking = sharedPreferences.getBoolean("MODO_PACKING", false);
        if (!tareoDetalleList.isEmpty()) {
            String lastActividad, lastLabor, lastConsumidor, lastHoras, lastRendimientos;

            int cantidad = tareoDetalleList.size();
            TareoDetalles lastDetalle = tareoDetalleList.get(cantidad - 1);

            lastActividad = lastDetalle.getIdActividad();
            lastLabor = lastDetalle.getIdLabor();
            lastConsumidor = lastDetalle.getIdConsumidor();
            lastHoras = String.valueOf(lastDetalle.getSubTotalHoras());
            lastRendimientos = String.valueOf(lastDetalle.getSubTotalRendimiento());
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
        if ((currentPage + 1) * ITEMS_PER_PAGE < tareoDetalleList.size()) {
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
        currentPage = tareoDetalleList.size() / ITEMS_PER_PAGE;
//        listarDetalles();
        actualizarDatos();
    }

    @Override
    public void onResume() {
        super.onResume();
        listarDetalles();
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        scannerView.pause();
//    }

    public void resultado(String barcodeValue) {
        Log.i("ESCANER", barcodeValue);

        if (isRequiredFieldsFilled()) {
            try {
                boolean modoPacking = sharedPreferences.getBoolean("MODO_PACKING", false);
                c007_atv_NroDocumento.setText(barcodeValue.replaceAll("[^0-9]", ""));
                String dni = c007_atv_NroDocumento.getText().toString();
                String fechaHoraFormateada = getCurrentFormattedDate();

                if (switchTipoMarcacion != null && !switchTipoMarcacion.isChecked() && !dni.isEmpty()) {
                    handleEntradaSalida(barcodeValue, modoPacking, fechaHoraFormateada);
                } else {
                    handleSoloSalida(fechaHoraFormateada);
                }

            } catch (Exception e) {
                Log.e("ERROR!", e.getMessage());
                Swal.error(this, "Error!", e.toString(), 8000);
            } finally {
                clearInputFields();
            }
        } else {
            Swal.warning(this, "Alerta!", "No se ha agregado el tareo porque faltan campos", 1500);
        }
    }

    private boolean isRequiredFieldsFilled() {
        return c007_txv_Consumidor_Key.length() > 0 && c007_txv_Actividad_Key.length() > 0 && c007_txv_Labor_Key.length() > 0;
    }

    private String getCurrentFormattedDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    private void handleEntradaSalida(String barcodeValue, boolean modoPacking, String fechaHoraFormateada) {
        List<TareoDetalles> resultadosSinSalida = tareoDetalleList.stream()
                .filter(t -> barcodeValue.equals(t.getDni()) && (t.getSalida() == null || t.getSalida().isEmpty()))
                .collect(Collectors.toList());

        if (!resultadosSinSalida.isEmpty() && modoPacking) {
            if (SALIDA_AUTOMATICA) {
                try {
                    marcarSalidaYRecursividad(resultadosSinSalida, fechaHoraFormateada, barcodeValue);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Swal.warning(this, "Cuidado", "El trabajador tiene un tareo sin salida, registra su salida y vuelve a intentarlo.", 5000);
            }
        } else {
            try {
                obtenerDataTrabajador(hmTablas.get("PERSONAS"));
            } catch (Exception e) {
                Swal.warning(ctx, "Huy!", "No se ha podido obtener información del trabajador", 5000);
            }

            try {
                agregarDetalle("lector");
            } catch (Exception e) {
                Log.e("ERRORPERSONA", e.toString());
                Swal.warning(ctx, "Huy!", "No se ha podido agregar el detalle", 5000);
            }
        }
    }

    private void handleSoloSalida(String fechaHoraFormateada) throws ParseException {
        String dniBusqueda = c007_atv_NroDocumento.getText().toString();
        List<TareoDetalles> resultadosSinSalida = tareoDetalleList.stream()
                .filter(t -> dniBusqueda.equals(t.getDni()) && (t.getSalida() == null || t.getSalida().isEmpty()))
                .collect(Collectors.toList());

        if (resultadosSinSalida.size() > 0) {
            resultadosSinSalida.get(0).setSalida(fechaHoraFormateada);
        } else {
            Swal.warning(this, "Cuidado", "No se encuentra un tareo de ingreso de este trabajador. Guarde el tareo e intente nuevamente.", 2000);
        }
    }

    private void marcarSalidaYRecursividad(List<TareoDetalles> resultadosSinSalida, String fechaHoraFormateada, String barcodeValue) throws ParseException {

        if (!resultadosSinSalida.isEmpty()) {
            resultadosSinSalida.get(0).setSalida(fechaHoraFormateada);
        }

        guardarTareoCompleto(barcodeValue);
    }

    private void guardarTareoCompleto(String barcodeValue) {
        resultado(barcodeValue);
        if (REPRODUCIR_SONIDO_TAREOS) {
            mediaPlayer = MediaPlayer.create(ctx, R.raw.notificacion);
            mediaPlayer.start();
        } else {
            Swal.success(ctx, "Correcto!", "Se ha marcado la salida correctamente", 1200);
        }
    }

    private void clearInputFields() {
        c007_atv_NroDocumento.setText("");
        c007_atv_NombreTrabajador.setText("");
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
                tareoEnFuncion.setFecha(String.valueOf(LocalDate.parse(s_Fecha)));
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
                tareoEnFuncion.setIdTurno(c007_txv_Turno_Key.getText().toString());
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
                String idActividad = c007_txv_Actividad_Key.getText().toString();
                nuevoDetalle.setIdActividad(idActividad);

                List<String> p = new ArrayList<>();
                p.add(tareoEnFuncion.getIdEmpresa());
                p.add(nuevoDetalle.getIdActividad());
                try {
                    arl_Labores = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Labores"), p, "READ"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                c007_txv_Labor_Key.setText("");
                c007_txv_Labor_Val.setText("");
                nuevoDetalle.setIdLabor("");
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
                String idLabor = c007_txv_Labor_Key.getText().toString();
                nuevoDetalle.setIdLabor(idLabor);
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
                String idConsumidor = c007_txv_Consumidor_Key.getText().toString();
                nuevoDetalle.setIdConsumidor(idConsumidor);
//                detalleActual.setIdConsumidor(c007_txv_Consumidor_Key.getText().toString());
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
                    tareoEnFuncion.setObservaciones(obs);
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

        fabDuplicar = findViewById(R.id.c007_fab_Duplicar_v);
        fabEditar = findViewById(R.id.c007_fab_Editar_v);
        fabEliminar = findViewById(R.id.c007_fab_Eliminar_v);
        fabGenerarQR = findViewById(R.id.c007_fab_GenerarQR_v);

        fabFirst = findViewById(R.id.First);
        fabLast = findViewById(R.id.Last);

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
                qrCodeScannerView.enableFlash();
            } else {
                qrCodeScannerView.disableFlash();
            }
        });

        scannerViewModel = new ViewModelProvider(this).get(ScannerViewModel.class);

        scannerViewModel.getScannedCode().observe(this, code -> {
            if (code != null) {
                try {
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
                    detallesSeleccionados.sort(Collections.reverseOrder());
                    for (Integer item : detallesSeleccionados) {
                        TareoDetalles resultadoEliminar = tareoDetalleList.stream()
                                .filter(t -> item == t.getItem())
                                .collect(Collectors.toList()).get(0);
                        tareoDetalleList.remove(resultadoEliminar);
                    }
                    actualizarDatos();

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
                Swal.customDialog(ctx, "editar", tareoDetalleList, detallesSeleccionados, /*(result, sweetAlertDialog) -> {
                },*/ (success, message, sweetAlertDialog) -> {
                    if (success) {
                        Swal.success(ctx, "Correcto!", message, 1500);
                        actualizarDatos();
                        detallesSeleccionados = new ArrayList<>();
                        sweetAlertDialog.dismissWithAnimation();
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
                Swal.customDialog(ctx, "duplicar", tareoDetalleList, detallesSeleccionados, /*(result, sweetAlertDialog1) -> {
                },*/ (success, message, sweetAlertDialog) -> {
                    if (success) {
                        Swal.success(ctx, "Correcto!", message, 1500);
                        actualizarDatos();
                        detallesSeleccionados = new ArrayList<>();
                        sweetAlertDialog.dismissWithAnimation();
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
                qrCodeScannerView.startCamera();
//                setHandlerScanner();
            } else {
//                scannerView.pause();
                qrCodeScannerView.stopScanning();
                layoutEscaner.setVisibility(View.GONE);
            }
        });
    }

    public void navigateToQR() {
        JSONArray jsonDetallesPersonal = new JSONArray();
        try {

            Cursor cursorTrabajadores = objSqlite.doItBaby("select Dni from trx_tareos_detalle where Idtareo = '" + tareoEnFuncion.getId() + "'", null, "READ");

            while (cursorTrabajadores.moveToNext()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("dni", cursorTrabajadores.getString(0));
                jsonDetallesPersonal.put(jsonObject);
            }
            Log.i("detallestrabajadores", jsonDetallesPersonal.toString());
        } catch (Exception e) {
            Swal.error(ctx, "Oops!", "No se han podido obtener los trabajadores", 5000);
        }
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
                PopUpObservacion d = new PopUpObservacion(this, tareoEnFuncion.getObservaciones(), c007_txv_Observacion);
                d.show();
            } else if (idControlClickeado == R.id.c007_fab_Agregar_v) {
                resultado(c007_atv_NroDocumento.getText().toString());
//                agregarDetalle("manual");
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

    private void agregarDetalle(String tipoEntrada) {

        boolean modoPacking = sharedPreferences.getBoolean("MODO_PACKING", false);
        Calendar calendar = Calendar.getInstance();
        // Formatear la fecha y hora actual
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String fechaHoraFormateada = sdf.format(calendar.getTime());

        boolean marcaExistente = false;
//        VALIDACION DE EXISTENCIA
        for (TareoDetalles td : tareoDetalleList) {
            boolean existeTrabajador = td.getDni().equals(nuevoDetalle.getDni());
            boolean existeActividad = td.getIdActividad().equals(nuevoDetalle.getIdActividad());
            boolean existeLabor = td.getIdLabor().equals(nuevoDetalle.getIdLabor());
            boolean existeConsumidor = td.getIdConsumidor().equals(nuevoDetalle.getIdConsumidor());
            if (VALIDAR_TRABAJADOR_REPETIDO && existeTrabajador && existeActividad && existeLabor && existeConsumidor) {
                marcaExistente = true;
                break;
            }
        }

        if (!marcaExistente) {
            nuevoDetalle.setIngreso(fechaHoraFormateada);
            nuevoDetalle.setSalida("");
            if (modoPacking) {
                nuevoDetalle.setSubTotalHoras(Double.valueOf("0"));
            } else {
                nuevoDetalle.setSubTotalHoras(Double.parseDouble(c007_etx_Horas.getText().length() > 0 ? c007_etx_Horas.getText().toString() : "0"));
            }
            nuevoDetalle.setSubTotalRendimiento(Double.parseDouble(c007_etx_Rdtos.getText().length() > 0 ? c007_etx_Rdtos.getText().toString() : "0"));
            nuevoDetalle.setDni(c007_atv_NroDocumento.getText().toString());

            PersonaHelper personaHelper = new PersonaHelper(nuevoDetalle.getDni());
            nuevoDetalle.setIdPlanilla(personaHelper.obtenerPlanilla());
            nuevoDetalle.setNombres(personaHelper.obtenerNombreTrabajador());

            ActividadHelper actividadHelper = new ActividadHelper(nuevoDetalle.getIdActividad());
            nuevoDetalle.setActividad(actividadHelper.obtenerDescripcionActividad());

            LaborHelper laborHelper = new LaborHelper(nuevoDetalle.getIdLabor(), nuevoDetalle.getIdActividad());
            nuevoDetalle.setLabor(laborHelper.obtenerDescripcionLabor());

            ConsumidorHelper consumidorHelper = new ConsumidorHelper(nuevoDetalle.getIdConsumidor());
            nuevoDetalle.setConsumidor(consumidorHelper.obtenerDecripcionConsumidor());
//                        INTENSIVO
            if (validarDetalleTareo(nuevoDetalle)) {
                if (REPRODUCIR_SONIDO_TAREOS) {
                    mediaPlayer = MediaPlayer.create(ctx, R.raw.notificacion);
                    mediaPlayer.start();
                } else {
                    Swal.success(ctx, "Correcto!", "Se ha agregado el tareo correctamente", 1200);
                }
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

        c007_atv_NombreTrabajador.setText("");
        c007_atv_NroDocumento.setText("");

        tareoDetalleList.add(nuevoDetalle);
//        VOLVEMOS A INICIALIZAR OTRO TAREO
        generarNuevoDetalle(tareoEnFuncion.getId());
        actualizarDatos();

    }

    private void manejarLayout() {
        c007_lly_Cabecera.setVisibility(layoutAbierto ? View.VISIBLE : View.GONE);
        c007_lly_Actividad_Val.setVisibility(layoutAbierto ? View.VISIBLE : View.GONE);
        c007_lly_Labor_Val.setVisibility(layoutAbierto ? View.VISIBLE : View.GONE);
        c007_lly_Consumidor_Val.setVisibility(layoutAbierto ? View.VISIBLE : View.GONE);
        c007_lly_Observacion.setVisibility(layoutAbierto ? View.VISIBLE : View.GONE);
        c007_lly_Detalle2.setVisibility(layoutAbierto ? View.GONE : View.VISIBLE);
    }

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

    private void obtenerDataTrabajador(Tabla cv) {
//        try {
        String dni = c007_atv_NroDocumento.getText().toString();
        String nombres = null;
        try {
            nombres = ClaveValor.obtenerValorDesdeClave(dni, ClaveValor.getArrayClaveValor(cv, 0, 2));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (!nombres.isEmpty()) {
            c007_atv_NombreTrabajador.setText(nombres);
            nuevoDetalle.setDni(dni);
            nuevoDetalle.setNombres(nombres);
        }
    }

    private void setearAutoCompleteTextViewNombreTrabajador(Tabla cv) {
        c007_atv_NombreTrabajador.setOnItemClickListener((parent, arg1, pos, id) -> {
            try {
                String nombres = c007_atv_NombreTrabajador.getText().toString(); //ClaveValor.obtenerClaveDesdeValor(dni ,cv);
                String dni = ClaveValor.obtenerClaveDesdeValor(nombres, ClaveValor.getArrayClaveValor(cv, 0, 2));
                c007_atv_NroDocumento.setText(dni);
                nuevoDetalle.setDni(dni);
                nuevoDetalle.setNombres(nombres);
            } catch (Exception ex) {
                Funciones.mostrarError(cls_05010000_Edicion.super.getBaseContext(), ex);
            }
        });
    }

    private void obtenerDataParaControles() {
        try {
            List<String> p = new ArrayList<>();
            p.add(sharedPreferences.getString("ID_EMPRESA", "!ID_EMPRESA"));
            arl_Turnos = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Turnos"), p, "READ"));
            arl_Actividades = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Actividades"), p, "READ"));
            arl_Consumidores = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Consumidores"), p, "READ"));
            hmTablas.put("PERSONAS", new Tabla(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Personas"), p, "READ")));
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
        }
    }

    private boolean validarDetalleTareo(TareoDetalles d) {
        if (d.getDni().length() < 8) return false;
        if (d.getNombres().trim().length() == 0) return false;
        if (d.getIdActividad().length() == 0) return false;
        if (d.getIdLabor().length() == 0) return false;
        if (d.getIdConsumidor().length() == 0) return false;
        return d.getSubTotalHoras() >= 0;
    }

    public void mostrarValoresDocumentoActual() {
        boolean modoPacking = sharedPreferences.getBoolean("MODO_PACKING", false);
        if (tareoEnFuncion.getId().length() > 0) {
            c007_txv_Turno_Key.setText(tareoEnFuncion.getIdTurno());
            c007_txv_Turno_Val.setText(tareoEnFuncion.getIdTurno());
            c007_txv_Id.setText(tareoEnFuncion.getId());
            s_Fecha = String.format(tareoEnFuncion.getFecha().toString(), "yyyy-MM-dd");
            c007_txv_Fecha.setText(Funciones.malograrFecha(s_Fecha));
        }
    }

    public void eliminarDetalle(int item) {
        try {
            if (tareoEnFuncion.getIdEstado().equals("PE")) {
                TareoDetalles tareoDelete = tareoDetalleList.stream()
                        .filter(t -> item == t.getItem())
                        .collect(Collectors.toList()).get(0);
                tareoDetalleList.remove(tareoDelete);
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

    public void popUpActualizarDetalleTareos(TareoDetalles detalle) {

//        scannerView.pause();
        try {
            if (tareoEnFuncion.getIdEstado().equals("PE")) {
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
                c021_etx_Horas.setText(String.valueOf(detalle.getSubTotalHoras()));
                c021_etx_Rdtos.setText(String.valueOf(detalle.getSubTotalRendimiento()));
                c021_etx_Observacion.setText(detalle.getObservacion());
                c021_fab_Ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        detalle.setSubTotalHoras(Double.parseDouble(c021_etx_Horas.getText().toString()));
                        detalle.setSubTotalRendimiento(Double.parseDouble(c021_etx_Rdtos.getText().toString()));
                        detalle.setObservacion(c021_etx_Observacion.getText().toString());
                        actualizarDatos();
                        popUp.dismiss();
                    }
                });

                c021_imv_Cerrar.setOnClickListener(view -> popUp.dismiss());
                //SETEA DIMENSIONES:
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                Double x = displayMetrics.widthPixels * 0.95;
                Double y = displayMetrics.heightPixels * 0.65;

                popUp.getWindow().setLayout(x.intValue(), y.intValue());
                popUp.show();
            } else {
                Funciones.notificar(this, "El tareo no cuenta con el estado PENDIENTE, imposible actualizar.");
            }
        } catch (Exception ex) {
            Funciones.mostrarError(super.getBaseContext(), ex);
        } finally {
//            setHandlerScanner();
        }
    }

    @Override
    public void onBackPressed() {
        String value = c007_txv_Turno_Val.getText().toString();

        String estadoActual = tareoEnFuncion.getIdEstado();

        if (!estadoActual.equals("TR")) {
            Swal.confirm(this, "CUIDADO!", "DESEA GUARDAR EL TAREO ANTES DE SALIR?").setConfirmClickListener(sweetAlertDialog -> {
                if (value.isEmpty()) {
                    sweetAlertDialog.dismissWithAnimation();
                    Swal.warning(ctx, "Alerta!", "No puedes guardar un tareo sin turno.", 2000);
                } else {
                    double totalRendimientos = tareoDetalleList.stream().
                            mapToDouble(TareoDetalles::getSubTotalRendimiento)
                            .sum();

                    double totalHoras = tareoDetalleList.stream().
                            mapToDouble(TareoDetalles::getSubTotalHoras)
                            .sum();

                    int totalDetalles = tareoDetalleList.size();

                    tareoEnFuncion.setTotalRendimientos(totalRendimientos);
                    tareoEnFuncion.setTotalHoras(totalHoras);
                    tareoEnFuncion.setTotalDetalles(totalDetalles);
                    tareoDAO.guardarTareo(tareoEnFuncion);
                    tareoDetallesDAO.insertarDetalleMasivo(tareoDetalleList);
                    Funciones.mostrarEstatusGeneral(this.getBaseContext(), objConfLocal, txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador);
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
        if (adaptadorLista != null) {
            adaptadorLista.notifyDataSetChanged();
        }
    }

    public void actualizarDatos() {
        obtenerUltimaData();
        startIndex = currentPage * ITEMS_PER_PAGE;
        endIndex = Math.min(startIndex + ITEMS_PER_PAGE, tareoDetalleList.size());
        List<TareoDetalles> nuevosDatos = tareoDetalleList.subList(startIndex, endIndex);
        adaptadorLista.addData(nuevosDatos);
        adaptadorLista.notifyDataChangedExternally();
        txvPagination.setText("Del: " + (startIndex + 1) + " Al: " + endIndex);
        detallesSeleccionados = new ArrayList<>();
        manejarLayout();
    }
}