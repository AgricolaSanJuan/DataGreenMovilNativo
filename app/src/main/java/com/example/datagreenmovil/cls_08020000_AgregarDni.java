package com.example.datagreenmovil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.location.Address;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.Rex;
import com.example.datagreenmovil.Helpers.LocationHelper;
import com.example.datagreenmovil.Logica.CryptorSJ;
import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.Logica.QRCodeScannerView;
import com.example.datagreenmovil.Logica.Swal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class cls_08020000_AgregarDni extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    ConexionSqlite objSqlite;
    ConexionBD objSql;
    ConfiguracionLocal objConfLocal;
    TextView txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador;
    //    AlertDialog.Builder builderDialogoCerrarSesion;
    Dialog dlg_PopUp;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    //    VARIABLES DE COMPORTAMIENTO DEL MÓDULO
    Boolean permitirTrabajadoresDesconocidos, reproducirSonidoAlerta, agregarConCamara, encenderFlash = false;
    LinearLayout c025_lly_IngresoDni;
    TextView c025_txv_Contador, c025_txv_DniMarcado, c025_txv_NombreMarcado, c025_txv_ApellidoMarcado;
    EditText c025_et_IngresoDni;
    Button c025_btn_1, c025_btn_2, c025_btn_3, c025_btn_4, c025_btn_5, c025_btn_6, c025_btn_7, c025_btn_8, c025_btn_9, c025_btn_0, c025_btn_X, c025_btn_Ok;
    FloatingActionButton fabFlash;
    ConstraintLayout llyTeclado, llyLector;
    Rex objRex;
    String s_DniMarcado = "", s_IdRex;
    int i_Items, i_Capacidad;
    Boolean registrarDni = false;
    private String dniRestringido;
    private LocationHelper locationHelper;
    private String coordinates = "";
    private GetLastLocation getLastLocation;
    private QRCodeScannerView qrCodeScannerView;
    private TextView scannedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_08020000_agregar_dni_025);
        //@Jota:2023-05-27 -> INICIO DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        locationHelper = new LocationHelper(this);

        try {
            if (getIntent().getExtras() != null) {
                objConfLocal = (ConfiguracionLocal) getIntent().getSerializableExtra("ConfiguracionLocal");
                s_IdRex = (String) getIntent().getSerializableExtra("IdRegistro");
            }
            sharedPreferences = this.getSharedPreferences("objConfLocal", MODE_PRIVATE);
            editor = sharedPreferences.edit();

            permitirTrabajadoresDesconocidos = sharedPreferences.getBoolean("PERMITIR_TRABAJADORES_DESCONOCIDOS", true);
            reproducirSonidoAlerta = sharedPreferences.getBoolean("REPRODUCIR_SONIDO_ALERTA", false);
            agregarConCamara = sharedPreferences.getBoolean("AGREGAR_CON_CAMARA", true);

            objSql = new ConexionBD(this);
            objSqlite = new ConexionSqlite(this, DataGreenApp.DB_VERSION());
            objSqlite.alterarColumnasEnServicioTransporte();

            referenciarControles();
            setearControles();
            Funciones.mostrarEstatusGeneral(this.getBaseContext(), objConfLocal, txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador);
            //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
            //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA

            if(agregarConCamara){
                llyTeclado.setVisibility(View.GONE);
                llyLector.setVisibility(View.VISIBLE);
                inicializarCamara();
            }else {
                llyTeclado.setVisibility(View.VISIBLE);
                llyLector.setVisibility(View.GONE);
            }

            obtenerRexActual();
            if (objRex.Get("IdServicioTransporte") != null && !objRex.Get("IdServicioTransporte").isEmpty()) {
                mostrarValoresRexActual();
            }
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
        }
// Agrega esto en el método onCreate de tu Activity
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        c025_et_IngresoDni.requestFocus();
        s_DniMarcado = "";
    }

    private void inicializarCamara() {
        qrCodeScannerView = findViewById(R.id.qrScannerView);
        qrCodeScannerView.setImagesParams(120, 90, 260, 40, 20);
        qrCodeScannerView.setOnCodeScannedListener(result -> {
            qrCodeScannerView.stopScanning();
//            Swal.info(this, "info", result, 8000);

            c025_et_IngresoDni.setText(result);

            getLastLocation = new GetLastLocation(this, (address) -> {
                procesarDNILector(address, null);
            }, (emptyAddress, message) -> {
                procesarDNILector(null, emptyAddress);
            });
            getLastLocation.getGeoDataDetails();

            new Handler().postDelayed(()->{
                qrCodeScannerView.startCamera();
            }, 1000);
        });
    }

    //@Jota:2023-05-27 -> LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    private void setearControles() {
//        builderDialogoCerrarSesion= Funciones.setearAlertDialogParaCerrarSesion_(objConfLocal,objSqlite,this);

        //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
        //...
    }

    //    NUEVO MÉTODO PARA OBTENER LA LOCALIZACIÓN
    private void getLocation() {
        locationHelper.getLocation(new LocationHelper.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                if (location != null) {
                    coordinates = location.getLatitude() + "," + location.getLongitude();
                    Toast.makeText(cls_08020000_AgregarDni.this, coordinates, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void referenciarControles() {
        txv_PushTituloVentana = findViewById(R.id.c025_txv_PushTituloVentana_v);
        txv_PushRed = findViewById(R.id.c025_txv_PushRed_v);
        txv_NombreApp = findViewById(R.id.c025_txv_NombreApp_v);
        txv_PushVersionApp = findViewById(R.id.c025_txv_PushVersionApp_v);
        txv_PushVersionDataBase = findViewById(R.id.c025_txv_PushVersionDataBase_v);
        txv_PushIdentificador = findViewById(R.id.c025_txv_PushIdentificador_v);

        //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
        //...
        c025_lly_IngresoDni = findViewById(R.id.c025_lly_IngresoDni_v);
        c025_txv_Contador = findViewById(R.id.c025_txv_Contador_v);
        c025_txv_DniMarcado = findViewById(R.id.c025_txv_DniMarcado_v);
        c025_txv_NombreMarcado = findViewById(R.id.c025_txv_NombreMarcado_v);
        c025_txv_ApellidoMarcado = findViewById(R.id.c025_txv_ApellidoMarcado_v);
//        c025_txv_IngresoDni = findViewById(R.id.c025_txv_IngresoDni_v);
        c025_et_IngresoDni = findViewById(R.id.c025_et_IngresoDni_v);
        c025_btn_1 = findViewById(R.id.c025_btn_1_v);
        c025_btn_2 = findViewById(R.id.c025_btn_2_v);
        c025_btn_3 = findViewById(R.id.c025_btn_3_v);
        c025_btn_4 = findViewById(R.id.c025_btn_4_v);
        c025_btn_5 = findViewById(R.id.c025_btn_5_v);
        c025_btn_6 = findViewById(R.id.c025_btn_6_v);
        c025_btn_7 = findViewById(R.id.c025_btn_7_v);
        c025_btn_8 = findViewById(R.id.c025_btn_8_v);
        c025_btn_9 = findViewById(R.id.c025_btn_9_v);
        c025_btn_0 = findViewById(R.id.c025_btn_0_v);
        c025_btn_X = findViewById(R.id.c025_btn_X_v);
        c025_btn_Ok = findViewById(R.id.c025_btn_Ok_v);

        llyTeclado = findViewById(R.id.llyTeclado);
        llyLector = findViewById(R.id.llyLector);

        c025_et_IngresoDni.setOnKeyListener((view, i, keyEvent) -> {
            if (i == KeyEvent.KEYCODE_ENTER && (keyEvent.getAction() == KeyEvent.ACTION_UP)) {
                getLastLocation = new GetLastLocation(this, (address) -> {
                    procesarDNILector(address, null);
                }, (emptyAddress, message) -> {
                    procesarDNILector(null, emptyAddress);
                });
                getLastLocation.getGeoDataDetails();
            }
            return false;
        });

        fabFlash = findViewById(R.id.fabFlash);

        fabFlash.setOnClickListener(v -> {
            encenderFlash = !encenderFlash;
            if(encenderFlash){
                qrCodeScannerView.enableFlash();
            }else {
                qrCodeScannerView.disableFlash();
            }
        });
    }

    private void procesarDNIManual(Address locationData, String emptyAddress) {
        String address = "";
        String addressText = "";

        if (locationData != null) {
            addressText = locationData.getLocality() == null ? locationData.getLatitude() + "-" + locationData.getLongitude() : locationData.getLocality();
        }
        coordinates = emptyAddress == null ? addressText : emptyAddress;

//            Swal.info(this, "Location", addressText, 8000);
        Log.d("GeoData", "Dirección: " + addressText);
//            coordinates = addressText;
        s_DniMarcado = c025_et_IngresoDni.getText().toString();
        if (s_DniMarcado.length() == 10 && !s_DniMarcado.startsWith("SJ")) {
            try {
                s_DniMarcado = CryptorSJ.desencriptarCadena(s_DniMarcado);
                c025_et_IngresoDni.setText(s_DniMarcado);
            } catch (Exception e) {
                final MediaPlayer Error = MediaPlayer.create(this, R.raw.error);
                c025_txv_NombreMarcado.setText("CÓDIGO");
                c025_txv_ApellidoMarcado.setText("INVÁLIDO");
                c025_et_IngresoDni.setText("");
                s_DniMarcado = "";
                if(reproducirSonidoAlerta){
                    Error.start();
                }else {
                    Swal.error(this, "Error", "CÓDIGO INVÁLIDO", 3000);
                }
                dniRestringido = "no_permitido";
            }
        } else if (s_DniMarcado.startsWith("SJ")) {
            final MediaPlayer Error = MediaPlayer.create(this, R.raw.error);
            c025_txv_NombreMarcado.setText("NO PERMITE");
            c025_txv_ApellidoMarcado.setText("PREFIJOS");
            c025_et_IngresoDni.setText("");
            s_DniMarcado = "";
            if(reproducirSonidoAlerta){
                Error.start();
            }else {
                Swal.error(this, "Error", "NO SE PERMITEN PREFIJOS", 3000);
            }
            dniRestringido = "no_permitido";
        } else {
            try {
//                        AQUI SE AGREGA EL DNI
                if (cumpleRestricciones() && guardarDni(s_DniMarcado)) {
                    Funciones.mostrarEstatusGeneral(this.getBaseContext(), objConfLocal, txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador);
                    actualizarNItems(s_IdRex);
                    obtenerRexActual();
                    mostrarValoresRexActual();
                }
                c025_et_IngresoDni.setText("");
                c025_et_IngresoDni.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(c025_et_IngresoDni.getWindowToken(), 0); // Donde "editText" es tu EditText
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                s_DniMarcado = "";
            }
        }
    }

    private void procesarDNILector(Address locationData, String emptyAddress) {
        String address = "";
        String addressText = "";

        if (locationData != null) {
            addressText = locationData.getLocality() == null ? locationData.getLatitude() + "-" + locationData.getLongitude() : locationData.getLocality();
        }

//        Swal.info(this, "Location", addressText, 8000);
//        Log.d("GeoData", "Dirección: " + addressText);
        coordinates = emptyAddress == null ? addressText : emptyAddress;

        if (c025_et_IngresoDni.getText().toString().length() == 10) {
            try {
                c025_et_IngresoDni.setText(CryptorSJ.desencriptarCadena(c025_et_IngresoDni.getText().toString()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        s_DniMarcado = c025_et_IngresoDni.getText().toString();
//                PREGISTRAMOS LA MARCA DESDE AQUÍ
        s_DniMarcado = c025_et_IngresoDni.getText().toString().trim();
        if (s_DniMarcado.length() == 10 && !s_DniMarcado.startsWith("SJ")) {
            try {
                s_DniMarcado = CryptorSJ.desencriptarCadena(s_DniMarcado);
                c025_et_IngresoDni.setText(s_DniMarcado);
                if (cumpleRestricciones() && guardarDni(s_DniMarcado)) {
                    Funciones.mostrarEstatusGeneral(this.getBaseContext(), objConfLocal, txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador);
                    actualizarNItems(s_IdRex);
                    obtenerRexActual();
                    mostrarValoresRexActual();
                }
                c025_et_IngresoDni.setText("");
                c025_et_IngresoDni.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(c025_et_IngresoDni.getWindowToken(), 0); // Donde "editText" es tu EditText
//                        return true;
            } catch (Exception e) {
                final MediaPlayer Error = MediaPlayer.create(this, R.raw.error);
                c025_txv_NombreMarcado.setText("CÓDIGO");
                c025_txv_ApellidoMarcado.setText("INVÁLIDO");
                c025_et_IngresoDni.setText("");
                s_DniMarcado = "";
                if(reproducirSonidoAlerta){
                    Error.start();
                }else {
                    Swal.error(this, "Error", "CÓDIGO INVÁLIDO", 3000);
                }
                dniRestringido = "no_permitido";
            }
        } else if (s_DniMarcado.startsWith("SJ")) {
            final MediaPlayer Error = MediaPlayer.create(this, R.raw.error);
            c025_txv_NombreMarcado.setText("NO PERMITE");
            c025_txv_ApellidoMarcado.setText("PREFIJOS");
            c025_et_IngresoDni.setText("");
            s_DniMarcado = "";
            if(reproducirSonidoAlerta){
                Error.start();
            }else {
                Swal.error(this, "Error", "NO PERMITE PREFIJOS", 3000);
            }
            dniRestringido = "no_permitido";
        } else {
            try {
                if (cumpleRestricciones() && guardarDni(s_DniMarcado)) {
                    Funciones.mostrarEstatusGeneral(this.getBaseContext(), objConfLocal, txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador);
                    actualizarNItems(s_IdRex);
                    obtenerRexActual();
                    mostrarValoresRexActual();
                }
                c025_et_IngresoDni.setText("");
                c025_et_IngresoDni.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(c025_et_IngresoDni.getWindowToken(), 0); // Donde "editText" es tu EditText
//                        return true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                s_DniMarcado = "";
            }
        }
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
            if (idControlClickeado == R.id.c025_txv_PushTituloVentana_v) {
                Funciones.popUpTablasPendientesDeEnviar(this);
            } else if (idControlClickeado == R.id.c025_txv_PushRed_v) {
                objSql.probarConexion(objConfLocal);
                Funciones.mostrarEstatusGeneral(this.getBaseContext(), objConfLocal, txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador);
            } else if (idControlClickeado == R.id.c025_txv_PushVersionApp_v || idControlClickeado == R.id.c025_txv_PushVersionDataBase_v) {
                Funciones.popUpStatusVersiones(this);
            } else if (idControlClickeado == R.id.c025_txv_PushIdentificador_v) {
                mostrarMenuUsuario(this.txv_PushIdentificador);
            }
            //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
            //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
            //...
            else if (idControlClickeado == R.id.c025_btn_1_v) {
                s_DniMarcado = s_DniMarcado + "1";
                c025_et_IngresoDni.setText(s_DniMarcado);
            } else if (idControlClickeado == R.id.c025_btn_2_v) {
                s_DniMarcado = s_DniMarcado + "2";
                c025_et_IngresoDni.setText(s_DniMarcado);
            } else if (idControlClickeado == R.id.c025_btn_3_v) {
                s_DniMarcado = s_DniMarcado + "3";
                c025_et_IngresoDni.setText(s_DniMarcado);
            } else if (idControlClickeado == R.id.c025_btn_4_v) {
                s_DniMarcado = s_DniMarcado + "4";
                c025_et_IngresoDni.setText(s_DniMarcado);
            } else if (idControlClickeado == R.id.c025_btn_5_v) {
                s_DniMarcado = s_DniMarcado + "5";
                c025_et_IngresoDni.setText(s_DniMarcado);
            } else if (idControlClickeado == R.id.c025_btn_6_v) {
                s_DniMarcado = s_DniMarcado + "6";
                c025_et_IngresoDni.setText(s_DniMarcado);
            } else if (idControlClickeado == R.id.c025_btn_7_v) {
                s_DniMarcado = s_DniMarcado + "7";
                c025_et_IngresoDni.setText(s_DniMarcado);
            } else if (idControlClickeado == R.id.c025_btn_8_v) {
                s_DniMarcado = s_DniMarcado + "8";
                c025_et_IngresoDni.setText(s_DniMarcado);
            } else if (idControlClickeado == R.id.c025_btn_9_v) {
                s_DniMarcado = s_DniMarcado + "9";
                c025_et_IngresoDni.setText(s_DniMarcado);
            } else if (idControlClickeado == R.id.c025_btn_0_v) {
                s_DniMarcado = s_DniMarcado + "0";
                c025_et_IngresoDni.setText(s_DniMarcado);
            } else if (idControlClickeado == R.id.c025_btn_X_v) {
                if (s_DniMarcado.length() > 0) {
                    s_DniMarcado = s_DniMarcado.substring(0, s_DniMarcado.length() - 1);
                    c025_et_IngresoDni.setText(s_DniMarcado);
                }
            } else if (idControlClickeado == R.id.c025_btn_Ok_v) {
//                OBTENEMOS LA LOCALIZACIÓN DESDE LA PRESIÓN PARA OBTENER LAS COORDENADAS Y ALMACENARLAS EN LA VARIABLE coordinates
//                getLocation();

//                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                getLastLocation = new GetLastLocation(this, address -> {
                    procesarDNIManual(address, null);
                }, (emptyAddress, message) -> {
                    Log.e("ERRORLOCATION", message);
                    procesarDNIManual(null, emptyAddress);
                });
//                }
                getLastLocation.getGeoDataDetails();
//                Swal.info(this, "HOLI", CryptorSJ.desencriptarCadena(s_DniMarcado), 5000);
//                if(s_DniMarcado.substring(1,1).equals("S")){
//                    s_DniMarcado = s_DniMarcado.substring(3).toString();
//                    Log.i("DNI", s_DniMarcado);
//                }
            } else if (idControlClickeado == R.id.c025_fab_Volver_v) {
                finish();
            } else throw new IllegalStateException("Click sin programacion: " + view.getId());
            c025_lly_IngresoDni.setBackground(ResourcesCompat.getDrawable(this.getResources(), !cumpleRestricciones() ? R.drawable.bg_alerta_suave : R.drawable.bg_r_blanco, null));
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
        }
    }

    //@Jota:
    //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
    //...

    public boolean cumpleRestricciones() {
        return cumpleLongitudCadena();
    }

    public boolean cumpleLongitudCadena() {
        return s_DniMarcado.length() == 10 || s_DniMarcado.length() == 8;
    }

    private boolean guardarDni(String s_dniMarcado) throws Exception {
        AtomicReference<Boolean> status = new AtomicReference<>(false);
        if (s_dniMarcado.substring(1, 1).equals("S")) {
            s_dniMarcado = s_dniMarcado.substring(3);
        }
//        else if(s_DniMarcado.length() == 10){
//            s_DniMarcado = CryptorSJ.desencriptarCadena(s_DniMarcado);
//        }
//        Swal.info(this, "HOLI", s_DniMarcado, 5000);

        try {


            final MediaPlayer Notificacion = MediaPlayer.create(this, R.raw.notificacion);
            final MediaPlayer Error = MediaPlayer.create(this, R.raw.error);
//            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//            Date fechaHora = new Date();
            objRex.Set("Item", i_Items + 1);
            objRex.Set("NroDocumento", s_dniMarcado);
            //objRex.Set("Hora",dateFormat.format(fechaHora));
            objRex.Set("FechaHora", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
//            if(coordinates!=null){
            objRex.Set("coordenadas_marca", coordinates);
//            }
//            objRex.Set("coordenadas_marca", coordinatesLocal);

            Cursor verificarExistencia;
            verificarExistencia = objSqlite.doItBaby("select count(*) exist from mst_personas where IDEMPRESA = '01' AND NroDocumento = '" + s_dniMarcado + "'", null, "READ");
            verificarExistencia.moveToNext();

            Cursor verificarRegistroExiste;

            verificarRegistroExiste = objSqlite.doItBaby("select count(*) repetido from trx_serviciostransporte_detalle where idserviciotransporte = '" + s_IdRex + "' and Nrodocumento = '" + s_dniMarcado + "'", null, "READ");
            verificarRegistroExiste.moveToNext();

            if (verificarRegistroExiste.getInt(0) <= 0) {
                if (permitirTrabajadoresDesconocidos) {
                    if(reproducirSonidoAlerta){
                        Notificacion.start();
                    }else {
                        Swal.success(this, "Correcto!", "TRABAJADOR AGREGADO", 3000);
                    }
                    dniRestringido = "permitido";
                    try {
                        if (objSqlite.GuardarRex(objConfLocal, "trx_ServiciosTransporte_Detalle", objRex)) {
                            i_Items++;
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    if (verificarExistencia.getInt(0) > 0) {
                        if(reproducirSonidoAlerta){
                            Notificacion.start();
                        }else {
                            Swal.success(this, "Correcto!", "TRABAJADOR AGREGADO", 3000);
                        }
                        dniRestringido = "permitido";
                        try {
                            if (objSqlite.GuardarRex(objConfLocal, "trx_ServiciosTransporte_Detalle", objRex)) {
                                i_Items++;
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        if(reproducirSonidoAlerta){
                            Error.start();
                        }else {
                            Swal.error(this, "Error", "NO PERMITIDO", 3000);
                        }
                        dniRestringido = "no_permitido";
                    }
                }
            } else {
                if(reproducirSonidoAlerta){
                    Error.start();
                }else {
                    Swal.error(this, "Error", "NO PERMITIDO", 3000);
                }
                dniRestringido = "repetido";
            }
            return true;
        } catch (Exception ex) {
            Funciones.mostrarError(this, ex);
            return false;
        }
//        return false;
    }

    public int obtenerItems(String idRegistro) throws Exception {
        List<String> p = new ArrayList<>();
        p.add(sharedPreferences.getString("ID_EMPRESA", "!ID_EMPRESA"));
        p.add(idRegistro);
        Cursor c = objSqlite.doItBaby(objSqlite.obtQuery("CONTAR trx_ServiciosTransporte_Detalle"), p, "READ");
        c.moveToFirst();
        return Integer.parseInt(c.getString(0));
    }

    public int obtenerCapacidad(String idRegistro) throws Exception {
        List<String> p = new ArrayList<>();
        p.add(sharedPreferences.getString("ID_EMPRESA", "!ID_EMPRESA"));
        p.add(idRegistro);
        Cursor c = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER CAPACIDAD trx_ServiciosTransporte"), p, "READ");
        c.moveToFirst();
        return Integer.parseInt(c.getString(0));
    }

    public void actualizarNItems(String idRegistro) throws Exception {
        List<String> p = new ArrayList<>();
        p.add(sharedPreferences.getString("ID_EMPRESA", "!ID_EMPRESA"));
        p.add(idRegistro);
        p.add(sharedPreferences.getString("ID_EMPRESA", "!ID_EMPRESA"));
        p.add(idRegistro);
        Cursor c = objSqlite.doItBaby(objSqlite.obtQuery("ACTUALIZAR N ITEMS trx_ServiciosTransporte"), p, "UPDATE");
    }

    @SuppressLint("Range")
    public void mostrarValoresRexActual() throws Exception {
        String contador = i_Items + "/" + i_Capacidad;
        c025_txv_Contador.setText(contador);

        if (dniRestringido.equals("permitido")) {
            c025_txv_DniMarcado.setText(objRex.Get("NroDocumento"));
            c025_txv_NombreMarcado.setText(objRex.Get("Nombres"));
            c025_txv_ApellidoMarcado.setText(objRex.Get("Apellidos"));
        } else if (dniRestringido.equals("repetido")) {
            c025_txv_DniMarcado.setText("");
            c025_txv_NombreMarcado.setText("REGISTRO REPETIDO");
            c025_txv_ApellidoMarcado.setText("");
        } else if (dniRestringido.equals("no_permitido")) {
            c025_txv_DniMarcado.setText("");
            c025_txv_NombreMarcado.setText("RESTRINGIDO");
            c025_txv_ApellidoMarcado.setText("");
        }
    }

    private void obtenerRexActual() throws Exception {
        List<String> p = new ArrayList<>();
        p.add(sharedPreferences.getString("ID_EMPRESA", "!ID_EMPRESA"));
        p.add(s_IdRex);
        p.add(sharedPreferences.getString("ID_EMPRESA", "!ID_EMPRESA"));
        p.add(s_IdRex);
        //objRex = objSqlite.CursorARex(objSqlite.doItBaby(objSqlite.obtQuery("OBTENER trx_ServiciosTransporte_Detalle X ID"),p,"READ"));
        objRex = objSqlite.CursorARex(objSqlite.doItBaby(objSqlite.obtQuery("OBTENER DATA XA MOSTRAR trx_ServiciosTransporte_Detalle"), p, "READ"));
        objRex.Set("IdEmpresa", sharedPreferences.getString("ID_EMPRESA", "!ID_EMPRESA"));
        objRex.Set("IdServicioTransporte", s_IdRex);
//        }
        i_Items = obtenerItems(s_IdRex);
        i_Capacidad = obtenerCapacidad(s_IdRex);
    }
}