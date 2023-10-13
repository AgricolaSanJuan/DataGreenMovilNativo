//UPDATED:2022-03-09;
package com.example.datagreenmovil;

import android.app.Dialog;
//import android.app.DialogFragment;
//import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.example.datagreenmovil.Logica.Funciones;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.example.datagreenmovil.Logica.InterfazDialog;


public class cls_05010000_Edicion extends AppCompatActivity implements View.OnClickListener {
    //@Jota:2023-05-27 -> INICIO DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    static ConexionSqlite objSqlite;
    ConexionBD objSql;
    ConfiguracionLocal objConfLocal;
    TextView txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador;
    Dialog dlg_PopUp;
    //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
    //...
//    Querys objQuerys;
    SharedPreferences sharedPreferences;

    //CONTROLES;
    //private ArrayAdapter<String>
//    private HashMap<String, ClaveValor[]> hmDataParaControles = new HashMap<>();
//    HashMap<String, MiData> hmNuevaDataParaControles =new HashMap<>();
    HashMap<String, Tabla> hmTablas =new HashMap<>();
    //    ArrayList<PopUpBuscarEnLista_Item> listaControl;
    //CONTROLES;
    private RecyclerView c007_rvw_Detalle;// = findViewById(R.id.c007_rvw_Detalle_v);
    private FloatingActionButton c007_fab_Guardar, c007_fab_AbrirCerrarCabecera;
    //    private Spinner spiTurnos;
//    private Spinner spiCultivos;
//    private Spinner spiVariedades;
//    private Spinner spiActividades;
//    private Spinner spiLabores;
//    private Spinner spiConsumidores;
    private AutoCompleteTextView c007_atv_NroDocumento;
    private AutoCompleteTextView c007_atv_NombreTrabajador;
    private TextView c007_txv_Id, c007_txv_Fecha, c007_txv_Turno_Key, c007_txv_Turno_Val, c007_txv_Actividad_Key, c007_txv_Actividad_Val, c007_txv_Labor_Key, c007_txv_Labor_Val,
            c007_txv_Consumidor_Key, c007_txv_Consumidor_Val, c007_txv_Observacion;
    private EditText c007_etx_Horas, c007_etx_Rdtos; //, etxObservaciones;
//    private LinearLayout lloObservaciones, llyFechaTurno, c007_lly_Cabecera, c007_lly_CultivoVariedad, c007_lly_DniNombre, c007_cly_HorasRdtos; // c007_lly_Resumen, c007_lly_CultivoVariedad;
//    private ConstraintLayout c007_cly_Resumen;
//    private TextView txv_Tareos_PushIdentificador;
//    private TextView txv_Tareos_PushRed;

//    Calendar calendar = Calendar.getInstance();
//    int year = calendar.get(Calendar.YEAR);
//    int month = calendar.get(Calendar.MONTH) + 1;
//    int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//    DatePickerDialog.OnDateSetListener setListener;

    //VARIABLES;
    private String IdDocumentoActual = null;
    private Tareo tareoActual; //= new Tareo();
    //private Registro tareoActual;
    private final TareoDetalle detalleActual=new TareoDetalle();
//    public int largoTextoSpinner = 13;

    public String s_Fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //--desde, hasta, estado;);
    ArrayList<PopUpBuscarEnLista_Item> arl_Turnos, arl_Actividades, arl_Labores = null, arl_Consumidores;
    public LinearLayout c007_lly_Turno, c007_lly_Actividad, c007_lly_Labor, c007_lly_Consumidor, c007_lly_Cabecera,
            c007_lly_Actividad_Val, c007_lly_Labor_Val, c007_lly_Consumidor_Val, c007_lly_Observacion, c007_lly_Detalle2;
    boolean layoutAbierto = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_05010000_edicion_007);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sharedPreferences = this.getSharedPreferences("objConfLocal", MODE_PRIVATE);
        //@Jota:2023-05-27 -> INICIO DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        try{
            if(getIntent().getExtras()!=null){
                objConfLocal=(ConfiguracionLocal) getIntent().getSerializableExtra("ConfiguracionLocal");
                IdDocumentoActual =(String) getIntent().getSerializableExtra("IdDocumentoActual");
            }
            objSql = new ConexionBD(objConfLocal);
            objSqlite = new ConexionSqlite(this,objConfLocal);
//            objConfLocal=new ConfiguracionLocal(objSqlite.obtenerConfiguracionLocal(objConfLocal));
//            objConfLocal.set("ULTIMA_ACTIVIDAD","PlantillaBase");
//            if (!tareoActual.getIdEstado().equals("TR")){
            referenciarControles();
//            }
            c007_txv_Fecha.setText(Funciones.malograrFecha(s_Fecha));
//            setearControles();
            Funciones.mostrarEstatusGeneral(this.getBaseContext(),
                    objConfLocal,
                    txv_PushTituloVentana,
                    txv_PushRed,
                    txv_NombreApp,
                    txv_PushVersionApp,
                    txv_PushVersionDataBase,
                    txv_PushIdentificador
            );
            //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
            //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
            //...
            tareoActual=new Tareo(IdDocumentoActual,objSqlite,objConfLocal, this);
            detalleActual.setIdEmpresa(tareoActual.getIdEmpresa());
//            year = tareoActual.getFecha().getYear();
//            month =  tareoActual.getFecha().getMonthValue();
//            day =  tareoActual.getFecha().getDayOfMonth();
//            if (!tareoActual.getIdEstado().equals("TR")){
            obtenerDataParaControles();
            cargarControles();
            setearControles();
//            }
//            lloObservaciones.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
//            c007_cly_Resumen.setVisibility(View.GONE);
//            c007_lly_CultivoVariedad.setVisibility(View.GONE);
            mostrarValoresDocumentoActual();
            manejarLayout();
            //PENDIENTE: IMPLEMENTAR BLOQUEO DE EDICION SEGUN ESTADO DEL REGISTRO
//            if (tareoActual.getIdEstado().equals("TR")){
//                c007_lly_Cabecera.setEnabled(false);
//                c007_lly_DniNombre.setEnabled(false);
//                c007_cly_HorasRdtos.setEnabled(false);
//                c007_rvw_Detalle.setEnabled(false);
//                c007_fab_Guardar.setEnabled(false);
//            }
        }catch (Exception ex){
            Funciones.mostrarError(this,ex);
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        mostrarValoresDocumentoActual();
//    }

    //@Jota:2023-05-27 -> LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    private void setearControles() {
//        builderDialogoCerrarSesion= Funciones.setearAlertDialogParaCerrarSesion_(objConfLocal,objSqlite,this);

        //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
        //...
//        setearSpinnerTurnos();
//        setearSpinnerCultivos();
//        setearSpinnerVariedades();
//        setearSpinnerActividades();
//        setearSpinnerLabores();
//        setearSpinnerConsumidores();
        //--------------------------------



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
                //objRex.Set("IdTurno", c007_txv_Turno_Key.getText().toString());
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
                    arl_Labores = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Labores"),p,"READ"));
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
                if (!obs.equals("...")){
                    tareoActual.setObservaciones(obs);
                    c007_txv_Observacion.setText("...");
                }
            }
        });


        setearAutoCompleteTextViewNroDocumento(hmTablas.get("PERSONAS"));
        setearAutoCompleteTextViewNombreTrabajador(hmTablas.get("PERSONAS"));
//        setearSelectorFecha();
    }

    private void referenciarControles() {
        txv_PushTituloVentana = findViewById(R.id.c007_txv_PushTituloVentana_v);
        txv_PushRed = findViewById(R.id.c007_txv_PushRed_v);
        txv_NombreApp = findViewById(R.id.c007_txv_NombreApp_v);
        txv_PushVersionApp = findViewById(R.id.c007_txv_PushVersionApp_v);
        txv_PushVersionDataBase = findViewById(R.id.c007_txv_PushVersionDataBase_v);
        txv_PushIdentificador = findViewById(R.id.c007_txv_PushIdentificador_v);

        //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
        //...
        c007_fab_Guardar = findViewById(R.id.c007_fab_Guardar_v);
        c007_fab_AbrirCerrarCabecera = findViewById(R.id.c007_fab_AbrirCerrarCabecera_v);
//        spiTurnos = findViewById(R.id.spiTurnos_v);
//        spiCultivos = findViewById(R.id.spiCultivo_v);
//        spiVariedades = findViewById(R.id.spiVariedad_v);
//        spiActividades = findViewById(R.id.spiActividad_v);
//        spiLabores = findViewById(R.id.spiLabor_v);
//        spiConsumidores = findViewById(R.id.spiConsumidor_v);
        c007_atv_NroDocumento = findViewById(R.id.c007_atv_NroDocumento_v);
        c007_atv_NombreTrabajador = findViewById(R.id.c007_atv_NombreTrabajador_v);
        c007_txv_Fecha = findViewById(R.id.c007_txv_Fecha);
        c007_txv_Id = findViewById(R.id.c007_txv_IdTareo_v);
//        c007_txv_ResumenCultivo = findViewById(R.id.c007_txv_ResumenCultivo_v);
//        c007_txv_ResumenVariedad = findViewById(R.id.c007_txv_ResumenVariedad_v);
//        c007_txv_ResumenActividad = findViewById(R.id.c007_txv_ResumenActividad_v);
//        c007_txv_ResumenLabor = findViewById(R.id.c007_txv_ResumenLabor_v);
//        c007_txv_ResumenConsumidor = findViewById(R.id.c007_txv_ResumenConsumidor_v);

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
        c007_lly_Labor  = findViewById(R.id.c007_lly_Labor_v);
        c007_lly_Consumidor = findViewById(R.id.c007_lly_Consumidor_v);
        c007_lly_Cabecera = findViewById(R.id.c007_lly_Cabecera_v);
        c007_lly_Actividad_Val = findViewById(R.id.c007_lly_Actividad_Val_v);
        c007_lly_Labor_Val = findViewById(R.id.c007_lly_Labor_Val_v);
        c007_lly_Consumidor_Val = findViewById(R.id.c007_lly_Consumidor_Val_v);
        c007_lly_Observacion = findViewById(R.id.c007_lly_Observacion_v);
        c007_lly_Detalle2 = findViewById(R.id.c007_lly_Detalle2_v);


        c007_etx_Horas = findViewById(R.id.etxHoras_v);
        c007_etx_Rdtos = findViewById(R.id.etxRtos_v);
//        etxObservaciones = findViewById(R.id.etxObservaciones_v);
//        lloObservaciones = (LinearLayout) findViewById(R.id.llyObservaciones_v);
//        c007_lly_Cabecera = (LinearLayout) findViewById(R.id.c007_lly_Cabecera_v);
//        c007_lly_DniNombre = (LinearLayout) findViewById(R.id.c007_lly_DniNombre_v);
//        c007_cly_Resumen = findViewById(R.id.c007_cly_Resumen_v);
//        c007_cly_HorasRdtos = (LinearLayout) findViewById(R.id.c007_cly_HorasRdtos_v);

//        llyFechaTurno = (LinearLayout) findViewById(R.id.llyFechaTurno_v);
        c007_rvw_Detalle = findViewById(R.id.c007_rvw_Detalle_v);
//        c007_lly_CultivoVariedad= findViewById(R.id.c007_lly_CultivoVariedad_v);
//        controlTest = findViewById(R.id.controlTest);
    }
    public void mostrarMenuUsuario(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this::onMenuItemClick);
        popup.inflate(R.menu.mnu_00000001_menu_usuario);
        popup.show();
    }

    public boolean onMenuItemClick(MenuItem item) {
        try{
            int idControlClickeado = item.getItemId();
            if (idControlClickeado==R.id.opc_00000001_cambiar_clave_usuario_v){
                dlg_PopUp = Funciones.obtenerDialogParaCambiarClave(this,objConfLocal,objSqlite,this);
                dlg_PopUp.show();
            } else if (idControlClickeado==R.id.opc_00000001_cerrar_sesion_v) {
                dlg_PopUp = Funciones.obtenerDialogParaCerrarSesion(this,objConfLocal,objSqlite,this);
                dlg_PopUp.show();
            }else return false;
        }catch(Exception ex){
            Funciones.mostrarError(this,ex);
            return false;
        }
        return true;
    }

    public void onClick(View view) {
        try {
            int idControlClickeado = view.getId();
            if (idControlClickeado == R.id.c007_txv_PushTituloVentana_v){
                Funciones.popUpTablasPendientesDeEnviar(this);
            } else if (idControlClickeado == R.id.c007_txv_PushRed_v) {
                objSql.probarConexion(objConfLocal);
                Funciones.mostrarEstatusGeneral(this.getBaseContext(),
                        objConfLocal,
                        txv_PushTituloVentana,
                        txv_PushRed,
                        txv_NombreApp,
                        txv_PushVersionApp,
                        txv_PushVersionDataBase,
                        txv_PushIdentificador
                );
            } else if (idControlClickeado == R.id.c007_txv_PushVersionApp_v || idControlClickeado == R.id.c007_txv_PushVersionDataBase_v) {
                Funciones.popUpStatusVersiones(this);
            } else if (idControlClickeado == R.id.c007_txv_PushIdentificador_v) {
//                showNoticeDialog();
                mostrarMenuUsuario(this.txv_PushIdentificador);
            }
            //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
            //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
            //...
            else if (idControlClickeado == R.id.c007_txv_Fecha){
                PopUpCalendario d = new PopUpCalendario(this,c007_txv_Fecha);
                d.show();
            }else if (idControlClickeado == R.id.c007_lly_Turno_v){
                PopUpBuscarEnLista d = new PopUpBuscarEnLista(this,arl_Turnos,c007_txv_Turno_Key,c007_txv_Turno_Val);
                d.show();
            }else if (idControlClickeado == R.id.c007_lly_Actividad_v){
                PopUpBuscarEnLista d = new PopUpBuscarEnLista(this,arl_Actividades,c007_txv_Actividad_Key,c007_txv_Actividad_Val);
                d.show();
            }else if (idControlClickeado == R.id.c007_lly_Labor_v){
                if (arl_Labores==null){
                    Funciones.notificar(this,"Primero debe de seleccionar una actividad.");
                }else{
                    PopUpBuscarEnLista d = new PopUpBuscarEnLista(this,arl_Labores,c007_txv_Labor_Key,c007_txv_Labor_Val);
                    d.show();
                }
            }else if (idControlClickeado == R.id.c007_lly_Consumidor_v){
                PopUpBuscarEnLista d = new PopUpBuscarEnLista(this,arl_Consumidores,c007_txv_Consumidor_Key,c007_txv_Consumidor_Val);
                d.show();
            }else if (idControlClickeado == R.id.c007_lly_Observacion_v){
                PopUpObservacion d = new PopUpObservacion(this, tareoActual.getObservaciones(), c007_txv_Observacion);
                d.show();
            }

            else if (idControlClickeado == R.id.c007_fab_Agregar_v) {
                detalleActual.setIdEmpresa(tareoActual.getIdEmpresa());
                detalleActual.setIdTareo(tareoActual.getId());
                detalleActual.setItem(tareoActual.getDetalle().size()+1);
                detalleActual.setHoras(Double.parseDouble(c007_etx_Horas.getText().length()>0? c007_etx_Horas.getText().toString():"0") );
                detalleActual.setRdtos(Double.parseDouble(c007_etx_Rdtos.getText().length()>0? c007_etx_Rdtos.getText().toString():"0") );
                detalleActual.setDni(c007_atv_NroDocumento.getText().toString());
                List<String> p = new ArrayList<>();
                p.add(sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"));
                p.add(detalleActual.getDni());
                detalleActual.setIdPlanilla(objSqlite.doItBaby(objSqlite.obtQuery("OBTENER PLANILLA"),p,"READ",""));
                detalleActual.setNombres(c007_atv_NombreTrabajador.getText().toString());
                if(validarDetalleTareo(detalleActual)){
                    tareoActual.agregarDetalle(detalleActual);
                    //cls_05010100_Item_RecyclerView adaptadorLista = new cls_05010100_Item_RecyclerView(this, R.layout.v_05010100_item_recyclerview_010, tareoActual.getDetalle(),objSqlite,tareoActual);
                    //c007_rvw_Detalle.setAdapter(adaptadorLista);
                    cls_05010200_RecyclerViewAdapter adaptadorLista = new cls_05010200_RecyclerViewAdapter(this ,objConfLocal,objSqlite,tareoActual);
                    c007_rvw_Detalle.setAdapter(adaptadorLista);
                    c007_rvw_Detalle.setLayoutManager(new LinearLayoutManager(this));
                    c007_atv_NombreTrabajador.setText("");
                    c007_atv_NroDocumento.setText("");
                }else{
                    Funciones.notificar(this,"Datos incompletos. Revisar.");
                }
                //PENDIENTE AQUI: VALIDAR TAREOS REPETIDOS POR CULTIVO, VARIEDAD, ACTIVIDAD, LABOR, CONSUMIDOR
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
            } else if (idControlClickeado == R.id.c007_fab_Guardar_v) {
                try{
                    if (!tareoActual.getIdEstado().equals("TR")){
                        //tareoActual.setObservaciones(etxObservaciones.getText().toString());
//                        if (detalleActual.getIdActividad().length()==0 || detalleActual.getIdLabor().length()==0 || detalleActual.getIdConsumidor().length()==0)

                        if(tareoActual.guardar(objSqlite,objConfLocal)) {
                            objSqlite.ActualizarDataPendiente(objConfLocal);
                            Funciones.mostrarEstatusGeneral(this.getBaseContext(),
                                    objConfLocal,
                                    txv_PushTituloVentana,
                                    txv_PushRed,
                                    txv_NombreApp,
                                    txv_PushVersionApp,
                                    txv_PushVersionDataBase,
                                    txv_PushIdentificador
                            );
                            Funciones.notificar(this, "Tareo guardado correctamente.");
                        }
                    }else {
                        Funciones.notificar(this,"El tareo no cuenta con el estado PENDIENTE, imposible actualizar.");
                    }
                }catch (Exception ex){
                    Funciones.mostrarError(this,ex);
                }
            } else if (idControlClickeado == R.id.c007_fab_AbrirCerrarCabecera_v) {
                layoutAbierto = !layoutAbierto;
                manejarLayout();
//            } else if (idControlClickeado == R.id.c007_fab_Observaciones_v) {
//                boolean abierto = lloObservaciones.getHeight() > 0;
//                if (abierto){
//                    //lloObservaciones.setVisibility(View.VISIBLE);
//                    lloObservaciones.setLayoutParams(new LinearLayout.LayoutParams(llyFechaTurno.getWidth(), 0));
//                }else{
//                    //lloObservaciones.setVisibility(View.GONE);
//                    lloObservaciones.setLayoutParams(new LinearLayout.LayoutParams(llyFechaTurno.getWidth(), 250));
//                }
            } else if (idControlClickeado == R.id.c007_fab_volver_v) {
                finish();
            } /*else if (idControlClickeado == R.id.controlTest) {
                PopUpCalendario d = new PopUpCalendario(this, controlTest);
                d.show();
//                Double x, y;
//                x = getResources().getDisplayMetrics().widthPixels * 0.90;
//                y = getResources().getDisplayMetrics().heightPixels * 0.80;
//                d.getWindow().setLayout(x.intValue(),y.intValue());
//                d.getWindow().setBackgroundDrawableResource(R.drawable.bg_spinner_popup);
            }*/
            else throw new IllegalStateException("Click sin programacion: " + view.getId());
        } catch (Exception ex) {
            Funciones.mostrarError(this,ex);
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

//        boolean cabeceraraAbierta = c007_lly_Cabecera.getHeight() > 0;
//        if (cabeceraraAbierta){
//            c007_lly_Cabecera.setLayoutParams(new LinearLayout.LayoutParams(llyFechaTurno.getWidth(), 0));
//            //c007_cly_Resumen.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            c007_cly_Resumen.setVisibility(View.VISIBLE);
//        }else{
//            c007_lly_Cabecera.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            //c007_cly_Resumen.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
//            c007_cly_Resumen.setVisibility(View.GONE);
//        }
    }
    //@Jota:
    //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
    //...

    private void cargarControles() {
        try{
//            Funciones.cargarSpinner(this,spiTurnos, hmTablas.get("TURNOS"),0,1,objConfLocal,2);
//            Funciones.cargarSpinner(this,spiCultivos, hmTablas.get("CULTIVOS"),0,1,objConfLocal,2);
//            Funciones.cargarSpinner(this,spiActividades, hmTablas.get("ACTIVIDADES"),0,1,objConfLocal,2);
//            Funciones.cargarSpinner(this,spiConsumidores, hmTablas.get("CONSUMIDORES"),0,1,objConfLocal,2);
            Funciones.cargarAutoCompleteTextView(this,c007_atv_NroDocumento,ClaveValor.obtenerValores(ClaveValor.getArrayClaveValor(hmTablas.get("PERSONAS"),  0, 2)));
            Funciones.cargarAutoCompleteTextView(this,c007_atv_NombreTrabajador,ClaveValor.obtenerClaves(ClaveValor.getArrayClaveValor(hmTablas.get("PERSONAS"),  0, 2)));
        }catch (Exception ex){
            Funciones.mostrarError(this,ex);
        }

    }

//    private void setearSelectorFecha() {
//        c007_txv_Fecha.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            //Locale.setDefault(Spani);
//                                            month = month - 1;
//                                            DatePickerDialog datePickerDialog = new DatePickerDialog(cls_05010000_Edicion.this, android.R.style.Theme_Holo, setListener, year, month, day);
//                                            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                            datePickerDialog.show();
//                                        }
//                                    }
//        );
//        setListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int anio, int mes, int dia) {
//                mes = mes + 1;
//                day =dia;
//                month =mes;
//                year =anio;
//                String fec =  (dia  < 10 ? "0" + dia : dia) + "/" + (mes < 10 ? "0" + mes : mes) + "/" + anio; //  day + "/" + month + "/" + year;
//                c007_txv_Fecha.setText(fec);
//                tareoActual.setFecha(LocalDate.of(year,month,day));
//            }
//        };
//    }

//    private void setearSpinnerTurnos() {
//        try{
//            spiTurnos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
//                    try{
//                        ClaveValor obj = (ClaveValor)(parent.getItemAtPosition(position));
//                        String idValorSeleccionado=obj.getClave();
//                        tareoActual.setIdTurno(idValorSeleccionado);
//                    }catch (Exception ex){
//                        Funciones.mostrarError(cls_05010000_Edicion.super.getBaseContext(), ex);
//                    }
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {}
//            });
//        }catch (Exception ex){
//             Funciones.mostrarError(this,ex);
//        }
//    }

//    private void setearSpinnerCultivos() {
//        try{
//            spiCultivos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
//                    try{
//                        ClaveValor obj = (ClaveValor)(parent.getItemAtPosition(position));
//                        String idValorSeleccionado=obj.getClave();
//                        String valorSeleccionado=obj.getValor();
//                        List<String> p = new ArrayList<>();
//                        p.add(objConfLocal.get("ID_EMPRESA")); //PENDIENTE: OBTENER EMPRESA DINAMICAMENTE;
//                        p.add(idValorSeleccionado);
//                        hmTablas.put("VARIEDADES", new Tabla(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Variedades"),p,"READ")));
//                        Funciones.cargarSpinner(cls_05010000_Edicion.this ,spiVariedades,hmTablas.get("VARIEDADES"),0,1,objConfLocal,2);
//                        detalleActual.setIdCultivo(idValorSeleccionado.trim());
//                        detalleActual.setCultivo(valorSeleccionado.trim());
//                        c007_txv_ResumenCultivo.setText(String.format("(%s)%s,", detalleActual.getIdCultivo(), detalleActual.getCultivo()));
//                        //PENDIENTE DEFINIR CAMBIO DE TEXTO AQUI PARA CADA SPINNER;
////                        spiCultivos
////                        String t =((TextView) view).getText().toString().trim().replace(" ","");
////                        ((TextView) view).setText(t.substring(0, Math.min(t.length(), 12)));
//                    }catch (Exception ex){
//                        Funciones.mostrarError(cls_05010000_Edicion.super.getBaseContext(),ex);
//                    }
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {}
//            });
//        }catch (Exception ex){
//            throw ex;
//        }
//    }

//    private void setearSpinnerVariedades() {
//        try{
//            spiVariedades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
//                    try{
//                        ClaveValor obj = (ClaveValor)(parent.getItemAtPosition(position));
//                        String idValorSeleccionado=obj.getClave();
//                        String valorSeleccionado=obj.getValor();
//                        detalleActual.setIdVariedad(idValorSeleccionado.trim());
//                        detalleActual.setVariedad(valorSeleccionado.trim());
//                        c007_txv_ResumenVariedad.setText(String.format("(%s)%s,", detalleActual.getIdVariedad(), detalleActual.getVariedad()));
//                    }catch (Exception ex){
//                        StackTraceElement z = new Exception().getStackTrace()[0];
//                        String detalleError = z.getFileName() + "\n" + z.getMethodName() + "\n" + z.getLineNumber() + ": \n" + ex.getMessage();
//                        ex.printStackTrace();
//                        Toast.makeText(cls_05010000_Edicion.super.getBaseContext(),detalleError, Toast.LENGTH_LONG).show();
//                    }
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {}
//            });
//        }catch (Exception ex){
//            throw ex;
//        }
//    }

//    private void setearSpinnerActividades() {
//        try{
//            spiActividades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
//                    try{
//                        ClaveValor obj = (ClaveValor)(parent.getItemAtPosition(position));
//                        String idValorSeleccionado=obj.getClave();
//                        String valorSeleccionado=obj.getValor();
//                        List<String> p = new ArrayList<>();
//                        p.add("01"); //PENDIENTE: OBTENER EMPRESA DINAMICAMENTE;
//                        p.add(idValorSeleccionado);
//                        //hmTablas.put("LABORES", new Tabla(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Labores"),p,"READ")));
//                        arl_Rutas = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Actividades"),p,"READ"));
//                        Funciones.cargarSpinner(cls_05010000_Edicion.this ,spiLabores,hmTablas.get("LABORES"),0,1,objConfLocal,2);
//                        detalleActual.setIdActividad(idValorSeleccionado.trim());
//                        detalleActual.setActividad(valorSeleccionado.trim());
//                        c007_txv_ResumenActividad.setText(String.format("(%s)%s,", detalleActual.getIdActividad(), detalleActual.getActividad()));
//                    }catch (Exception ex){
//                        Funciones.mostrarError(cls_05010000_Edicion.super.getBaseContext(),ex);
//                    }
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {}
//            });
//        }catch (Exception ex){
//             Funciones.mostrarError(this,ex);
//        }
//    }

//    private void setearSpinnerLabores() {
//        try{
//            spiLabores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
//                    try{
//                        ClaveValor obj = (ClaveValor)(parent.getItemAtPosition(position));
//                        String idValorSeleccionado=obj.getClave();
//                        String valorSeleccionado=obj.getValor();
//                        detalleActual.setIdLabor(idValorSeleccionado.trim());
//                        detalleActual.setLabor(valorSeleccionado.trim());
//                        c007_txv_ResumenLabor.setText(String.format("(%s)%s,", detalleActual.getIdLabor(), detalleActual.getLabor()));
//                    }catch (Exception ex){
//                        Funciones.mostrarError(cls_05010000_Edicion.super.getBaseContext(),ex);
//                    }
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {}
//            });
//        }catch (Exception ex){
//             Funciones.mostrarError(this,ex);
//        }
//    }

//    private void setearSpinnerConsumidores() {
//        try{
//            spiConsumidores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
//                    try{
//                        ClaveValor obj = (ClaveValor)(parent.getItemAtPosition(position));
//                        String idValorSeleccionado=obj.getClave();
//                        String valorSeleccionado=obj.getValor();
//                        detalleActual.setIdConsumidor(idValorSeleccionado.trim());
//                        detalleActual.setConsumidor(valorSeleccionado.trim());
//                        c007_txv_ResumenConsumidor.setText(String.format("(%s)%s", detalleActual.getIdConsumidor(), detalleActual.getConsumidor()));
//                    }catch (Exception ex){
//                        Funciones.mostrarError(cls_05010000_Edicion.super.getBaseContext(),ex);
//                    }
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {}
//            });
//        }catch (Exception ex){
//             Funciones.mostrarError(this,ex);
//        }
//    }

    private void setearAutoCompleteTextViewNroDocumento(Tabla cv) {
        c007_atv_NroDocumento.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id){
                try{
                    String dni= c007_atv_NroDocumento.getText().toString();
                    String nombres=ClaveValor.obtenerValorDesdeClave(dni ,ClaveValor.getArrayClaveValor(cv,  0, 2));
                    c007_atv_NombreTrabajador.setText(nombres);
                    detalleActual.setDni(dni);
                    detalleActual.setNombres(nombres);
                }catch (Exception ex){
                    StackTraceElement z = new Exception().getStackTrace()[0];
                    String detalleError = z.getFileName() + "\n" + z.getMethodName() + "\n" + z.getLineNumber() + ": \n" + ex.getMessage();
                    ex.printStackTrace();
                    Toast.makeText(cls_05010000_Edicion.super.getBaseContext(),detalleError, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setearAutoCompleteTextViewNombreTrabajador(Tabla cv) {
        c007_atv_NombreTrabajador.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id){
                try{
                    String nombres= c007_atv_NombreTrabajador.getText().toString(); //ClaveValor.obtenerClaveDesdeValor(dni ,cv);
                    String dni=ClaveValor.obtenerClaveDesdeValor(nombres ,ClaveValor.getArrayClaveValor(cv,  0, 2));
                    c007_atv_NroDocumento.setText(dni);
                    detalleActual.setDni(dni);
                    detalleActual.setNombres(nombres);
                }catch (Exception ex){
                    Funciones.mostrarError(cls_05010000_Edicion.super.getBaseContext(),ex);
                }
            }
        });
    }

//    private void obtenerDataParaControles() throws Exception {
//        try{
//            List<String> p = new ArrayList<>();
//            p.add(objConfLocal.get("ID_EMPRESA"));
////            hmTablas.put("TURNOS",new Tabla(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Turnos"),p,"READ")));
//            hmTablas.put("PERSONAS",new Tabla(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Personas"),p,"READ")));
////            hmTablas.put("CULTIVOS",new Tabla(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Cultivos"),p,"READ")));
////            hmTablas.put("ACTIVIDADES",new Tabla(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Actividades"),p,"READ")));
////            hmTablas.put("CONSUMIDORES",new Tabla(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Consumidores"),p,"READ")));
////            listaControl = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Consumidores"),p,"READ"));
//        }catch (Exception ex){
//             Funciones.mostrarError(this,ex);
//        }
//    }

    private void obtenerDataParaControles() throws Exception {
        try{
            List<String> p = new ArrayList<>();
            p.add(sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"));
            //PENDIENTE: CREAR ESTAS CONSULTAS EN QUERYS SQLITE
            arl_Turnos = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Turnos"),p,"READ"));
            arl_Actividades = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Actividades"),p,"READ"));
            arl_Consumidores = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Consumidores"),p,"READ"));
            //arl_Conductores = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Conductores"),p,"READ"));
            hmTablas.put("PERSONAS",new Tabla(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Personas"),p,"READ")));
        }catch (Exception ex){
            Funciones.mostrarError(this,ex);
        }
    }

    private boolean validarDetalleTareo(TareoDetalle d) {
        if(d.getDni().length()<8) return false;
        if(d.getNombres().trim().length()==0) return false;
        if (d.getIdActividad().length()==0) return false;
        if (d.getIdLabor().length()==0) return false;
        if (d.getIdConsumidor().length()==0) return false;
        return d.getHoras() > 0;
    }

    public void mostrarValoresDocumentoActual() {
//        int i = ((AdapatadorSpinner) spiTurnos.getAdapter()).getIndex(tareoActual.getIdTurno());
//        spiTurnos.setSelection(i);
        if (tareoActual.getId().length()>0){
            c007_txv_Turno_Key.setText(tareoActual.getIdTurno());
            c007_txv_Turno_Val.setText(tareoActual.getIdTurno());

            c007_txv_Id.setText(tareoActual.getId());
//        String fechaTexto= (day  < 10 ? "0" + day : day) + "/" + (month < 10 ? "0" + month : month) + "/" + year;
//        c007_txv_Fecha.setText(fechaTexto);

            s_Fecha=String.format(tareoActual.getFecha().toString(), "yyyy-MM-dd");
            c007_txv_Fecha.setText(Funciones.malograrFecha(s_Fecha));

//        etxObservaciones.setText(tareoActual.getObservaciones());
            cls_05010200_RecyclerViewAdapter adaptadorLista = new cls_05010200_RecyclerViewAdapter(this ,objConfLocal,objSqlite,tareoActual);
            c007_rvw_Detalle.setAdapter(adaptadorLista);
            c007_rvw_Detalle.setLayoutManager(new LinearLayoutManager(this));
        }

    }

    public void eliminarDetalle(int item){
        try{
            if (tareoActual.getIdEstado().equals("PE")){
                tareoActual.eliminarItemDetalle(item);
                Funciones.notificar(this, "Detalle elimiado: " + item);
                tareoActual.guardarDetalle(objSqlite);
                cls_05010200_RecyclerViewAdapter adaptadorLista = new cls_05010200_RecyclerViewAdapter(this ,objConfLocal,objSqlite,tareoActual);
                c007_rvw_Detalle.setAdapter(adaptadorLista);
                c007_rvw_Detalle.setLayoutManager(new LinearLayoutManager(this));
                //mostrarValoresDocumentoActual();
            } else{
                Funciones.notificar(this,"El tareo no cuenta con el estado PENDIENTE, imposible actualizar.");
            }
        }catch (Exception ex){
            Funciones.notificar(this,ex.getMessage());
        }
    }

    //////////////////////////////////////////// PRUEBA 2
    public void popUpActualizarDetalleTareos(TareoDetalle detalle){
        try{
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
                        }
//                        mostrarValoresDocumentoActual();
                        popUp.dismiss();
                    }
                });

//                c021_btn_Aceptar.setOnClickListener(new View.OnClickListener(){
//                    @Override
//                    public void onClick(View view){
//                        detalle.setHoras(Double.parseDouble(c021_etx_Horas.getText().toString()));
//                        detalle.setRdtos(Double.parseDouble(c021_etx_Rdtos.getText().toString()));
//                        detalle.setObservacion(c021_etx_Observacion.getText().toString());
//                        try {
//                            detalle.guardar(objSqlite, tareoActual.getIdUsuario());
//                        } catch (Exception e) {
//                            throw new RuntimeException(e);
//                        }
//                        mostrarValoresDocumentoActual();
//                        popUp.dismiss();
//                    }
//                });

                c021_imv_Cerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popUp.dismiss();
                    }
                });

//                //Button c021_btn_Cancelar = popUp.findViewById(R.id.c021_btn_Cancelar_v);
//                c021_btn_Cancelar.setOnClickListener(new View.OnClickListener(){
//                    @Override
//                    public void onClick(View view){
//                        popUp.dismiss();
//                    }
//                });

                //SETEA DIMENSIONES:
                Double x, y;
                x = this.getResources().getDisplayMetrics().widthPixels * 0.90;
                y = this.getResources().getDisplayMetrics().heightPixels * 0.40;
                popUp.getWindow().setLayout(x.intValue(),y.intValue());
                popUp.show();
            }else {
                Funciones.notificar(this,"El tareo no cuenta con el estado PENDIENTE, imposible actualizar.");
            }
        }catch(Exception ex){
            Funciones.mostrarError(super.getBaseContext(),ex);
        }
    }
    //////////////////////////////////////////////////////////////////////////////////// PRUEBA 2

    /////////////////////////////////////////////////////////
//    public void showNoticeDialog() {
//        // Create an instance of the dialog fragment and show it
//        DialogFragment dialog = new NoticeDialogFragment();
////        dialog.setContentView(R.layout.v_popup_actualizar_detalle_tareo_021);
//        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
//    }
//
//    // The dialog fragment receives a reference to this Activity through the
//    // Fragment.onAttach() callback, which it uses to call the following methods
//    // defined by the NoticeDialogFragment.NoticeDialogListener interface
//    @Override
//    public void onDialogPositiveClick(DialogFragment dialog) {
//        // User touched the dialog's positive button
//        Toast.makeText(this,"boton positivo presionado", Toast.LENGTH_LONG).show();
////        ...
//    }
//
//    @Override
//    public void onDialogNegativeClick(DialogFragment dialog) {
//        // User touched the dialog's negative button
//        Toast.makeText(this,"boton negativo presionado", Toast.LENGTH_LONG).show();
////        ...
//    }
//
//    @Override
//    public void btnAceptarPresionado(DialogFragment dialog, String s) {
//        // User touched the dialog's negative button
//        Toast.makeText(this,s, Toast.LENGTH_LONG).show();
////        ...
//    }
}