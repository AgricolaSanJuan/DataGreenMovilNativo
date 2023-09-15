package com.example.datagreenmovil;

import android.content.Intent;
import android.media.MediaPlayer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.MiData;
import com.example.datagreenmovil.Entidades.PopUpBuscarEnLista;
import com.example.datagreenmovil.Entidades.PopUpBuscarEnLista_Item;
import com.example.datagreenmovil.Entidades.PopUpCalendario;
import com.example.datagreenmovil.Entidades.Tabla;
import com.example.datagreenmovil.Logica.Funciones;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
//import com.example.datagreenmovil.Conexiones.ConexionBD;

public class cls_06000000_Eficiencias extends AppCompatActivity {
    //OBJETOS PRINCIPALES:
    ConexionSqlite objSqlite;
//    ConexionBD objSql;
    ConfiguracionLocal objConfLocal;
    ConexionBD objSql;

    //TextView txv_PushIdentificador, txv_TituloVentana, txv_PushRed;
    TextView txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador;
    AlertDialog.Builder builderDialogoCerrarSesion;

    //VARIABLES ESPECIFICAS
    //Cursor resultado;
    ResultSet Cajas;
    int NroCajaActual=0, NroCajasMax=0;
    String s_Estado="AB", s_fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //--desde, hasta, estado;
    //String estado ="AB";
//    HashMap<String, ClaveValor[]> hmDataParaControles = new HashMap<>();
//    HashMap<String, MiData> hmDataParaControles =new HashMap<>();
//    HashMap<String, Tabla> hmTablas =new HashMap<>();
    String codigoActual="", codSel="", codPes="", codEmp="", idLaborActual="", mensajeError="", idPalletActual="", dataCodigoLeido;
//    List<String> codigosHoy = new ArrayList<String>();
    String idLinea ="";
//    final MediaPlayer Notificacion = MediaPlayer.create(this, R.raw.notificacion), Alerta = MediaPlayer.create(this, R.raw.alerta);

    //CONTROLES ESPECIFICOS
    RecyclerView c006_rvw_Lista;
    FloatingActionButton c006_fab_Atras, c006_fab_CerrarPallet, c006_fab_NuevoPallet, c006_fab_LimpiarInput;
//    Spinner c006_spi_Estado, c006_spi_Pallet;
    EditText c006_etx_Codigo;
    TextView c006_txv_Fecha_Key, c006_txv_Estado_Key, c006_txv_Pallet_Key, c006_txv_Pallet_Val, c006_txv_Seleccion, c006_txv_Pesado, c006_txv_Empaque, c006_txv_Item, c006_txv_Info;
    LinearLayout c026_lly_Fecha, c006_lly_Estado, c006_lly_Pallet;
    ArrayList<PopUpBuscarEnLista_Item> arl_Estados, arl_Pallets ;


    //VARIABES DE SELECCION DE FECHA:
//    Calendar Calendario = Calendar.getInstance();
//    int Anio = Calendario.get(Calendar.YEAR);
//    int Mes = Calendario.get(Calendar.MONTH) + 1;
//    int Dia = Calendario.get(Calendar.DAY_OF_MONTH);
//    DatePickerDialog.OnDateSetListener CalendarioListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_06000000_eficiencias_006);
        //COMUN{ ///////////////////////////////////////////////////////////////////
        try {
            if(getIntent().getExtras()!=null){
                objConfLocal=(ConfiguracionLocal) getIntent().getSerializableExtra("ConfiguracionLocal");
            }
//            objSql = new ConexionBD(objConfLocal);
            objSqlite = new ConexionSqlite(this,objConfLocal);
            objConfLocal.set("ULTIMA_ACTIVIDAD","Eficiencias");
            objSql = new ConexionBD(objConfLocal);

            obtenerDataParaControles();
            referenciarControles();
//            cargarControles();
            setearControles();
            Funciones.mostrarEstatusGeneral(this.getBaseContext(),objConfLocal,txv_PushIdentificador,txv_PushTituloVentana,txv_PushRed);

//            obtenerCodigosHoy();
//            Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext(),"14/10/23-12:56",Toast.LENGTH_LONG).show();
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT);
//            c006_etx_Codigo.requestFocus();
//            c006_etx_Codigo.performClicked();

//            Snackbar.make(findViewById(R.id.c006_cly_Principal), "R.string.email_sent",Snackbar.LENGTH_SHORT).show();
/*
            PENDIENTE:
            BLOQUE DE CODIGO PARA LANZAR MENSAJE TIPO MESSAGE BOX, CONVERTIR EN CLASE PARA USAR DESDE CUALQUIER ACTIVIDAD
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("R.string.dialog_message").setTitle("R.string.dialog_title");
            builder.setPositiveButton("R.string.ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext(),"si",Toast.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton("R.string.cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext(),"no",Toast.LENGTH_LONG).show();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
 */
            //}; ///////////////////////////////////////////////////////////////////////
            //Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext(), fecha, Toast.LENGTH_SHORT).show();
            //listarTareos(estado,desde,hasta);
        } catch (Exception ex) {
             Funciones.mostrarError(this,ex);
        }
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        String fec =  (Dia  < 10 ? "0" + Dia : Dia) + "/" + (Mes < 10 ? "0" + Mes : Mes) + "/" + Anio;
//        c006_dtp_Fecha.setText(fec);
//    }

//    private void setearSelectorFecha() {
//        c006_dtp_Fecha.setOnClickListener(new View.OnClickListener() {
//                                         @Override
//                                         public void onClick(View view) {
//                                             Mes = Mes - 1;
//                                             DatePickerDialog datePickerDialog = new DatePickerDialog(cls_06000000_Eficiencias.this, android.R.style.Theme_Holo, CalendarioListener, Anio, Mes, Dia);
//                                             datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                             datePickerDialog.show();
//                                         }
//                                     }
//        );
//        CalendarioListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int anio, int mes, int dia) {
//                mes = mes + 1;
//                Dia =dia;
//                Mes =mes;
//                Anio =anio;
//                String fec =  (Dia  < 10 ? "0" + Dia : Dia) + "/" + (Mes < 10 ? "0" + Mes : Mes) + "/" + Anio;
//                c006_dtp_Fecha.setText(fec);
//                //desde = Anio + "-" + (mes < 10 ? "0" + mes : mes) + "-" + (dia  < 10 ? "0" + dia : dia);
//                fecha = Anio + "-" + (Mes < 10 ? "0" + Mes : Mes) + "-" + (Dia  < 10 ? "0" + Dia : Dia);
//                //Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext(), fecha, Toast.LENGTH_SHORT).show();
//                c006_etx_Codigo.requestFocus();
//                try {
//                    cargarSpinnerPallets();
//                    listarCajas();
//                } catch (Exception ex) {
//                    StackTraceElement z = new Exception().getStackTrace()[0];
//                    String detalleError = z.getFileName() + "\n" + z.getMethodName() + "\n" + z.getLineNumber() + ": \n" + ex.getMessage();
//                    ex.printStackTrace();
//                    Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext(),detalleError, Toast.LENGTH_LONG).show();
//                }
//            }
//        };
//    }

//    private void obtenerDataParaControles() throws Exception {
//        try{
//            List<String> p = new ArrayList<>();
//            //p.add(objConfLocal.get("ID_EMPRESA"));
////            MiData md = new MiData(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Estados"),null,"READ"));
////            hmDataParaControles.put("ESTADOS",md);
//            hmTablas.put("ESTADOS",new Tabla(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Estados"),null,"READ")));
//            //--------------
//
////            Cursor c = objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Empresas"),null,"READ");
////            md = new MiData(c);
////            hmDataParaControles.put("EMPRESAS",md);
//        }catch (Exception ex){
//             Funciones.mostrarError(this,ex);
//        }
//    }

    private void obtenerDataParaControles() throws Exception {
        try{
//            List<String> p = new ArrayList<>();
//            p.add(objConfLocal.get("ID_EMPRESA"));
            //PENDIENTE: CREAR ESTAS CONSULTAS EN QUERYS SQLITE
            arl_Estados = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Estados"),null,"READ"));
//            arl_Rutas = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Rutas"),p,"READ"));
//            arl_Vehiculos = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Vehiculos"),p,"READ"));
//            arl_Conductores = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Conductores"),p,"READ"));
        }catch (Exception ex){
            Funciones.mostrarError(this,ex);
        }
    }

//    private void obtenerCodigosHoy() throws Exception {
//        try{
//            List<String> p = new ArrayList<>();
//            p.add(fecha);
//            MiData md = new MiData(objConexion.doItBaby("DataGreen..sp_Dg_Packing_Movimientos_LecturaEficiencias_CodigosHoy",p));
//            for(MiFila f : md.Filas){
//                codigosHoy.add(f.Item(0));
//            }
//        }catch (Exception ex){
//            StackTraceElement z = new Exception().getStackTrace()[0];
//            Toast.makeText(this,z.getFileName()+"\n"+z.getMethodName()+"\n"+ex.getMessage(), Toast.LENGTH_LONG).show();
//        }
//    }

//    private void cargarControles()throws Exception {
//        try{
////            MiData midata = new MiData(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Estados"),null,"READ"));
////            Funciones.cargarSpinner(this,c006_spi_Estado,hmDataParaControles.get("ESTADOS"));
////            Funciones.cargarSpinner(this,c006_spi_Estado,midata,0,1);
////            Funciones.cargarSpinner(cls_06000000_Eficiencias.this,c006_spi_Estado, hmDataParaControles.get("ESTADOS"),0,1);
//            Funciones.cargarSpinner(cls_06000000_Eficiencias.this,c006_spi_Estado, hmTablas.get("ESTADOS"),0,1);
//        }catch (Exception ex){
//             Funciones.mostrarError(this,ex);
//        }
//    }

    private void setearEditTextCodigo(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        c006_etx_Codigo.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                codigoActual=s.toString();
                if(codigoActual.length()==10 && procesarCodigo()){
                        c006_etx_Codigo.setText("");
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
//                if (s.length() > 0) {
//                    codigoActual=s.toString();
//                    if(codigoActual.length()==10 && procesarCodigo()){
//                        c006_etx_Codigo.setText("");
//                    }
//                    char lastCharacter = s.charAt(s.length() - 1);
//                    if (lastCharacter == '\n') {
//                        //String barcode = s.subSequence(0, s.length() - 1).toString();
//                        c006_etx_Codigo.setText("");
//                        c006_etx_Codigo.requestFocus();
//                    }
//                }
            }
        });
//        c006_etx_Codigo.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent e) {
//                if (e.getAction() == KeyEvent.ACTION_UP && e.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//                    procesarCodigo();
//                }
//                return true;
////                if (e.getAction() == KeyEvent.ACTION_UP && e.getKeyCode() != KeyEvent.KEYCODE_SHIFT_LEFT) {
////                    String tecla = Character.toString((char) e.getUnicodeChar()).toUpperCase();
////                    if(e.getKeyCode() == KeyEvent.KEYCODE_ENTER){
////                        procesarCodigo();
////                        return true;
////                    }else if(e.getKeyCode() == KeyEvent.KEYCODE_DEL){
////                        if(codigoActual.length()>0){
////                            codigoActual = codigoActual.substring(0,codigoActual.length()-1);
////                            c006_etx_Codigo.setText(codigoActual);
////                        }
////                        return true;
////                    }else{
////                        codigoActual += tecla;
////                        c006_etx_Codigo.setText(codigoActual);
////                        return true;
////                    }
////                }
////                return false;
//
////                if (e.getKeyCode() == KeyEvent.KEYCODE_ENTER && e.getAction() == KeyEvent.ACTION_UP) {
////                    //Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext(),"enter",Toast.LENGTH_LONG).show();
////                    codigoActual = c006_etx_Codigo.getText().toString();
//////                    Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext(),codigoActual + ix , Toast.LENGTH_LONG).show();
//////                    ix++;
////                    c006_etx_Codigo.requestFocus();
////                    if(procesarCodigo()){
////                        c006_etx_Codigo.setText("");
//////                        c006_etx_Codigo.requestFocus();
////                    }else{
////                        Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext(),mensajeError, Toast.LENGTH_LONG).show();
////                    }
////                }
//////                c006_etx_Codigo.requestFocus();
//////                enfocarControlInput();
////                return false;
//            }
//        });
//        c006_etx_Codigo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus){
//                if(!hasFocus) {
//                    codigoActual = c006_etx_Codigo.getText().toString();
//                    if (procesarCodigo()) {
////                        c006_etx_Codigo.setText("");
//                    } else {
//                        Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext(), mensajeError, Toast.LENGTH_LONG).show();
//                    }
////                    c006_etx_Codigo.requestFocus();
//                }
//            }
//        });
    }

//    private void setearFAB(){
//        c006_fab_LimpiarInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus){
//                if(hasFocus) {
////                    codigoActual = c006_etx_Codigo.getText().toString();
////                    if (procesarCodigo()) {
////                    } else {
////                        Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext(), mensajeError, Toast.LENGTH_LONG).show();
////                    }
//                    c006_fab_LimpiarInput.requestFocus(1);
//                }
//            }
//        });
//    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent e) {
//        if (e.getAction() == KeyEvent.ACTION_UP && e.getKeyCode() != KeyEvent.KEYCODE_SHIFT_LEFT) {
////            char tecla = (char) e.getUnicodeChar();
//            String tecla = Character.toString((char) e.getUnicodeChar()).toUpperCase();
////            Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext(),Character.toString(tecla), Toast.LENGTH_LONG).show();
////            Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext(),String.valueOf(e.getKeyCode()), Toast.LENGTH_LONG).show();
////            return true;
//            if(e.getKeyCode() == KeyEvent.KEYCODE_ENTER){
//                procesarCodigo();
////                if(codigoActual.length()>0 && procesarCodigo()){
////                    c006_etx_Codigo.setText("");
////                    codigoActual ="";
////                }else{
////                    Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext(),mensajeError, Toast.LENGTH_LONG).show();
////                }
//                return true;
//            }else if(e.getKeyCode() == KeyEvent.KEYCODE_DEL){
//                if(codigoActual.length()>0){
//                    codigoActual = codigoActual.substring(0,codigoActual.length()-1);
//                    c006_etx_Codigo.setText(codigoActual);
//                }
//                return true;
//            }else{
//                codigoActual += tecla;
//                c006_etx_Codigo.setText(codigoActual);
////                if(codigoActual.length()==10 && procesarCodigo()){
////                    c006_etx_Codigo.setText("");
////                    codigoActual ="";
////                }
//                //codigoActual = c006_etx_Codigo.getText().toString();
//                return true;
//            }
////            else if(esAlfaNumerico((char) e.getUnicodeChar())){
////                char pressedKey = (char) e.getUnicodeChar();
////                codigoActual += pressedKey;
////                c006_etx_Codigo.setText(codigoActual);
////            }else if(e.getKeyCode() == KeyEvent.KEYCODE_DEL){
////                if(codigoActual.length()>0){
////                    codigoActual = codigoActual.substring(0,codigoActual.length()-1);
////                    c006_etx_Codigo.setText(codigoActual);
////                }
////            }
//        }
////        if (e.getAction() == KeyEvent.ACTION_UP && e.getKeyCode() == KeyEvent.KEYCODE_ENTER)
////        {
////            if(procesarCodigo()){
////                c006_etx_Codigo.setText("");
////                codigoActual ="";
////            }else{
////                Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext(),mensajeError, Toast.LENGTH_LONG).show();
////            }
////        }
//        return false;
//    }

//    public static boolean esAlfaNumerico(char c) {
//        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9');


    private boolean procesarCodigo() {
        try {
            final MediaPlayer Notificacion = MediaPlayer.create(this, R.raw.notificacion);
            if (!HayPalletSeleccionado()) {
                throw new Exception("No ha seleccionado ningun pallet.");
            }
            if (!validarLongitud()) {
                throw new Exception("Codigo invalido: " + codigoActual + " (" + codigoActual.length() + ")");
            } else if (!validarNroCajas()) {
                throw new Exception("El limite de cajas se ha superado.");
            }
//            else if (!validarCodigoLibre()) {
//                //Toast.makeText(this,mensajeError,Toast.LENGTH_LONG).show();
//                throw new Exception(mensajeError);
//            }
            idLaborActual = obtenerLabor();
            if (!validarLaborLibre()) {
                throw new Exception(mensajeError);
            }
            actualizarControlesYVariables();
            if (!cajaCompleta()) {
                return true;
            }
            if (!guardarCaja()) {
                throw new Exception("No se ha podido guardar la caja.");
            }
            Notificacion.start();
            //incrementarCajas();
            limpiarControlesYVariables();
//            c006_txv_Item.setText(String.valueOf(NroCajaActual));
            listarCajas();
            if (!palletCompleto()) {
//                cargarSpinnerPallets(); //PUEDE SER PRESCINDIBLE
//                listarCajas();
                return true;
            }
            if(cerrarPallet()){
                Toast.makeText(this,"Pallet " + idPalletActual + " cerrado correctamente.", Toast.LENGTH_LONG).show();
            }
//            cargarSpinnerPallets();
            //enfocarControlInput();
        }catch(Exception ex){
            final MediaPlayer Alerta = MediaPlayer.create(this, R.raw.alerta);
            Alerta.start();
            StackTraceElement z = new Exception().getStackTrace()[0];
            Toast.makeText(this,z.getFileName()+"\n"+z.getMethodName()+"\n"+ex.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
//            enfocarControlInput();
            c006_etx_Codigo.setText("");
            codigoActual ="";
        }
        return false;
/*
            if(c.length() == 10 && NroCajaActual<=NroCajasMax){ //PODRIA EJECUTARSE COMO UN METODO INDEPENDIENTE LLAMADO VALIDAR CODIGO QUE RETORNA UN BOOL
                if(codigosHoy.contains(c)){
                    String detalle="";
                    List<String> p = new ArrayList<>();
                    p.add(c);
                    MiData md = new MiData(objConexion.doItBaby("DataGreen..sp_Dg_Packing_Movimientos_LecturasEficiencias_ConsultarCodigo",p));
                    detalle = md.Filas(0).Item(0).toString();
//                    sonido();
//                    PopUp("El código " + c + " ya ha sido leido anteriormente.\n" + detalle);
                }else{
                    idLaborActual=obtenerLabor(c);
                    if(idLaborActual.equals("S") && codSel.length()==10){
                        mensajeError="Ha leido un codigo de SELECCION sin cerrar la caja actual.";
                        return false;
                    }else if(idLaborActual.equals("P") && codPes.length()==10){
                        mensajeError="Ha leido un codigo de PESADO sin cerrar la caja actual.";
                        return false;
                    }else if(idLaborActual.equals("E") && codEmp.length()==10){
                        mensajeError="Ha leido un codigo de EMPAQUE sin cerrar la caja actual.";
                        return false;
                    }
                    codSel=idLaborActual.equals("S")?c:codSel;
                    codPes=idLaborActual.equals("P")?c:codPes;
                    codEmp=idLaborActual.equals("E")?c:codEmp;
                    c006_txv_Seleccion.setText(idLaborActual.equals("S")?c:codSel);
                    c006_txv_Pesado.setText(idLaborActual.equals("P")?c:codPes);
                    c006_txv_Empaque.setText(idLaborActual.equals("E")?c:codEmp);
                    if(codSel.length()==10 && codPes.length()==10 && codEmp.length()==10){
                        if(guardarCaja()){
                            codSel="";
                            codPes="";
                            codEmp="";
                            NroCajaActual++;
                            if(NroCajaActual>NroCajasMax && cerrarPallet()){
                                cargarSpinnerPallets();
                            }//else{
                            listarCajas();
                            //}
                            return true;
                        }else{
                            mensajeError="Error al intentar guardar la caja.";
                            return false;
                        }
                    }
                    return true;
                }
            }else{
                return false;
            }
            return false;
        }catch (Exception ex){
            StackTraceElement z = new Exception().getStackTrace()[0];
            Toast.makeText(this,z.getFileName()+"\n"+z.getMethodName()+"\n"+ex.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }*/
    }

    private boolean HayPalletSeleccionado() {
        return idPalletActual.length() != 0;
    }

//    private void enfocarControlInput() {
//        c006_etx_Codigo.requestFocus();
//    }

    private boolean palletCompleto() {
        return NroCajaActual>NroCajasMax;
    }

    private void limpiarControlesYVariables() {
        codSel="";
        codPes="";
        codEmp="";
        c006_txv_Seleccion.setText("");
        c006_txv_Pesado.setText("");
        c006_txv_Empaque.setText("");
        c006_etx_Codigo.setText("");
        c006_txv_Info.setText("");
        codigoActual="";

    }

//    private void incrementarCajas() {
//        NroCajaActual++;
//    }

    private boolean cajaCompleta() {
        return codSel.length()==10 && codEmp.length()==10 && codPes.length()==10;
    }

    private void actualizarControlesYVariables() {
        codSel=idLaborActual.equals("S")?codigoActual:codSel;
        codPes=idLaborActual.equals("P")?codigoActual:codPes;
        codEmp=idLaborActual.equals("E")?codigoActual:codEmp;
        c006_txv_Seleccion.setText(idLaborActual.equals("S")?codigoActual:codSel);
        c006_txv_Pesado.setText(idLaborActual.equals("P")?codigoActual:codPes);
        c006_txv_Empaque.setText(idLaborActual.equals("E")?codigoActual:codEmp);
    }

    private boolean validarLaborLibre() {
        if((codSel.length()==10 || codPes.length()==10 || codEmp.length()==10) && idLaborActual.equals("0")){
            if(!asignarLaborNula()){
                mensajeError="No puede asigar codigo 0000000000 a una caja vacia.";
                return false;
            }
        }
        if(idLaborActual.equals("S") && codSel.length()==10){
            mensajeError="Ha leido un codigo de SELECCION sin cerrar la caja actual.";
            return false;
        }else if(idLaborActual.equals("P") && codPes.length()==10){
            mensajeError="Ha leido un codigo de PESADO sin cerrar la caja actual.";
            return false;
        }else if(idLaborActual.equals("E") && codEmp.length()==10){
            mensajeError="Ha leido un codigo de EMPAQUE sin cerrar la caja actual.";
            return false;
        }
        return true;
    }

    private boolean asignarLaborNula() {
        if(codSel.isEmpty() && codPes.isEmpty() && codEmp.isEmpty()){
            return false;
        }else if(!codSel.isEmpty() && !codPes.isEmpty() && codEmp.isEmpty()){
            codEmp=codigoActual;
        }else if(!codSel.isEmpty() && codPes.isEmpty() && !codEmp.isEmpty()){
            codPes=codigoActual;
        }else if(codSel.isEmpty() && !codPes.isEmpty() && !codEmp.isEmpty()){
            codSel=codigoActual;
        }else if(!codSel.isEmpty() && codPes.isEmpty() && codEmp.isEmpty()){
            codPes=codigoActual;
        }else if(codSel.isEmpty() && !codPes.isEmpty() && codEmp.isEmpty()){
            codSel=codigoActual;
        }else if(codSel.isEmpty() && codPes.isEmpty() && !codEmp.isEmpty()){
            codSel=codigoActual;
        }
        return true;
    }

    private boolean validarCodigoLibre() {
        try{
//            if(codigosHoy.contains(codigoActual)){
//                return false;
//            }
            List<String> p = new ArrayList<String>();
            p.add(codigoActual);
            //ResultSet rs = objConexion.doItBaby("sp_Dgm_Gen_ValidarCodigoLeido",p);
            ResultSet rs = objSql.doItBaby("DataGreen..sp_Dg_Packing_Movimientos_LecturasEficiencias_ConsultarCodigo",p);
            if (!rs.next()) {
                //Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext(),"ResultSet Vacio.",Toast.LENGTH_LONG).show();
                return true;
            }else if(rs.getString(1).toString().length()>0){
                mensajeError=rs.getString(1).toString();
                return false; //rs.getString(1).toString().equals("SI");
            }
        }catch(Exception ex){
             Funciones.mostrarError(this,ex);
        }
        return false;
    }

    private boolean validarNroCajas() {
        return NroCajaActual<=NroCajasMax;
    }

    private boolean validarLongitud() {
        return codigoActual.length()==10;
    }

    private boolean cerrarPallet() {
        try{
            List<String> p = new ArrayList<String>();
            p.add(idPalletActual);
            p.add(objConfLocal.get("ID_USUARIO_ACTUAL"));
            ResultSet rs = objSql.doItBaby("sp_Dgm_Gen_CerrarPallet",p);
//            if (!rs.next()) {
//                Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext(),"ResultSet Vacio.",Toast.LENGTH_LONG).show();
//                return false;
//            }else
            if(rs.next() && rs.getString(1).toString().equals("1")){
                Funciones.notificar(this,"Pallet "+ idPalletActual + " Cerrado Correctamente.");
                return true;
            }else{
                //mensajeError=rs.getString(2).toString();
                Funciones.notificar(this,"Erro al cerrar pallet: " + rs.getString(2).toString());
            }
            return false;
        }catch(Exception ex){
             Funciones.mostrarError(this,ex);
            return false;
        }
    }

    //private boolean guardarCaja(String idPalletActual_, String codSel_, String codPes, String codEmp) {
    private boolean guardarCaja() {
        try{
            //MODIFICACION:
            //IdPallet,NroCaja,CodSel,CodPes,CodEmp,FechaHora,IdLinea,Usuario
            String pText=idPalletActual;
            pText=pText+","+String.valueOf(NroCajaActual);
            pText=pText+","+codSel;
            pText=pText+","+codPes;
            pText=pText+","+codEmp;
            pText=pText+","+"fechahora";
            pText=pText+","+ idLinea;
            pText=pText+","+objConfLocal.get("ID_USUARIO_ACTUAL");

            List<String> p = new ArrayList<String>();
            p.add(pText);
//            p.add(idPalletActual);
//            p.add(String.valueOf(NroCajaActual));
//            p.add(codSel);
//            p.add(codPes);
//            p.add(codEmp);
//            p.add("fechahora");
//            p.add("linea");
//            p.add(objConfLocal.get("ID_USUARIO_ACTUAL"));
            ResultSet rs = objSql.doItBaby("DataGreen..sp_Dg_Packing_Movimientos_LecturaEficiencias_InsertarCodigo_V3",p);
            if (!rs.next()) {
                Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext(),"ResultSet Vacio.",Toast.LENGTH_LONG).show();
                return false;
            }else{
                if(rs.getString(1).length()<=3){
                    NroCajaActual = Integer.parseInt(rs.getString(1)) + 1;
                    c006_txv_Item.setText(String.valueOf(NroCajaActual));
                    return true;
                }
                limpiarControlesYVariables();
                throw new Exception(rs.getString(1));
//                do{
//                   //algo
//                } while (rs.next());
            }
            //return rs.next();
        }catch(Exception ex){
            StackTraceElement z = new Exception().getStackTrace()[0];
            String detalleError = z.getFileName() + "\n" + z.getMethodName() + "\n" + z.getLineNumber() + ": \n" + ex.getMessage();
            ex.printStackTrace();
            c006_txv_Info.setText(detalleError);
//            Toast.makeText(this,detalleError, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private String obtenerLabor() {
        try{
            if(codigoActual.equals("0000000000")){
                return "0";
            }
            String aux= codigoActual.substring(2,3);
            switch(aux){
                case "0": case "1": case "2": case "3": case "C":
                    return "S";
                case "4": case "5": case "6": case "7": case "D":
                    return "P";
                case "8": case "9": case "A": case "B": case "E":
                    return "E";
                default:
            }
        }
        catch (Exception ex){
             Funciones.mostrarError(this,ex);
        }
        return "0";
    }

//    private void setearSpinnerEstados() {
//        try{
//            c006_spi_Estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
//                    try{
//                        ClaveValor obj = (ClaveValor)(parent.getItemAtPosition(position));
//                        //String idValorSeleccionado=obj.getValor();
//                        estado = obj.getClave();
//                        //Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext() ,idValorSeleccionado, Toast.LENGTH_LONG).show();
//                        cargarSpinnerPallets();
//                        listarCajas();
//                    }catch (Exception ex){
//                        StackTraceElement z = new Exception().getStackTrace()[0];
//                        String detalleError = z.getFileName() + "\n" + z.getMethodName() + "\n" + z.getLineNumber() + ": \n" + ex.getMessage();
//                        ex.printStackTrace();
//                        Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext(),detalleError, Toast.LENGTH_LONG).show();
//                    }
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {}
//            });
//        }catch (Exception ex){
//             Funciones.mostrarError(this,ex);
//        }
//    }

    //COMUN{ ///////////////////////////////////////////////////////////////////
    public void onClick(View view) {
        try {
            int idControlClickeado = view.getId();
            if (idControlClickeado == R.id.c024_txv_PushTituloVentana_v){
                Funciones.popUpTablasPendientesDeEnviar(this);
            } else if (idControlClickeado == R.id.c024_txv_PushRed_v) {
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
            } else if (idControlClickeado == R.id.c024_txv_PushVersionApp_v || idControlClickeado == R.id.c024_txv_PushVersionDataBase_v) {
                Funciones.popUpStatusVersiones(this);
            } else if (idControlClickeado == R.id.c024_txv_PushIdentificador_v) {
                mostrarMenuUsuario(this.txv_PushIdentificador);
            } else if (idControlClickeado==R.id.c006_lly_Fecha_v) {
                PopUpCalendario d = new PopUpCalendario(this, c006_txv_Fecha_Key);
                d.show();
            } else if (idControlClickeado==R.id.c006_lly_Estado_v) {
                PopUpBuscarEnLista d = new PopUpBuscarEnLista(this, arl_Estados, c006_txv_Estado_Key, null);
                d.show();
            } else if (idControlClickeado==R.id.c006_lly_Pallet_v) {
                PopUpBuscarEnLista d = new PopUpBuscarEnLista(this, arl_Pallets, c006_txv_Pallet_Key, c006_txv_Pallet_Val);
                d.show();
            } else if (idControlClickeado==R.id.c006_fab_Atras_v) {
                finish();
            } else if (idControlClickeado==R.id.c006_fab_CerrarPallet_v) {
                if(cerrarPallet()){
                    //Toast.makeText(this, "Pallet cerrado correctamente.", Toast.LENGTH_SHORT).show();
                    obtenerPallets();
                    c006_txv_Pallet_Key.setText("");
                    c006_txv_Pallet_Val.setText("");
                }else{
                    Toast.makeText(this, mensajeError, Toast.LENGTH_SHORT).show();
                }
            } else if (idControlClickeado==R.id.c006_fab_NuevoPallet_v) {
                abrirDocumento();
            } else if (idControlClickeado==R.id.c006_fab_LimpiarInput_v) {
                limpiarControlesYVariables();
            }else throw new IllegalStateException("Click sin programacion: " + view.getId());
//
//            switch (view.getId()) {
//                case R.id.c006_txv_PushIdentificador_v:
//                    mostrarMenuUsuario(this.txv_PushIdentificador);
//                    break;
//                case R.id.c006_txv_PushRed_v:
//                    //objSql.probarConexion(objConfLocal);
//                    objConexion.probarConexion(objConfLocal);
//                    Funciones.mostrarEstatusGeneral(this.getBaseContext(),objConfLocal,txv_PushIdentificador,txv_TituloVentana,txv_PushRed);
//                    break;
//                //CODIGO PARTICULAR PARA CADA ACTIVIDAD
//                case R.id.c006_lly_Fecha_v:
//                    PopUpCalendario d = new PopUpCalendario(this,c006_txv_Fecha_Key);
//                    d.show();
//                case R.id.c006_fab_Atras_v:
//                    finish();
//                    break;
//                case R.id.c006_fab_CerrarPallet_v:
//                    if(cerrarPallet()){
//                        Toast.makeText(this, "Pallet cerrado correctamente.", Toast.LENGTH_SHORT).show();
//                    }else{
//                        Toast.makeText(this, mensajeError, Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//                case R.id.c006_fab_NuevoPallet_v:
//                    abrirDocumento();
//                    break;
//                case R.id.c006_fab_LimpiarInput_v:
////                    c006_etx_Codigo.setText("");
////                    codigoActual="";
//                    limpiarControlesYVariables();
//                    break;
//                default:
//                    throw new IllegalStateException("Click sin programacion: " + view.getId());
//            }
        } catch (Exception ex) {
             Funciones.mostrarError(this,ex);
        }
    }

//    private void setearControles() {
//        builderDialogoCerrarSesion= Funciones.setearAlertDialogParaCerrarSesion_(objConfLocal,objSqlite,this);
//        //CODIGO PARTICULAR PARA CADA ACTIVIDAD
//        setearSelectorFecha();
//        setearSpinnerEstados();
//        setearSpinnerPallets();
//        setearEditTextCodigo();
////        setearFAB();
//    }

    private void setearControles() {
//        builderDialogoCerrarSesion= Funciones.setearAlertDialogParaCerrarSesion_(objConfLocal,objSqlite,this);
        //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
        //...
//        setearSelectorFecha();
        setearEditTextCodigo();
        c006_txv_Fecha_Key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    s_fecha = Funciones.arreglarFecha(c006_txv_Fecha_Key.getText().toString());
                    c006_etx_Codigo.requestFocus();
                    try {
                        obtenerPallets();
                        limpiarSelectorPallets();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    //listarCajas();
                } catch (Exception e) {
                    StackTraceElement z = new Exception().getStackTrace()[0];
                    String detalleError = z.getFileName() + "\n" + z.getMethodName() + "\n" + z.getLineNumber() + ": \n" + e.getMessage();
                    e.printStackTrace();
                    Funciones.mostrarError(cls_06000000_Eficiencias.super.getBaseContext(),e);
                    //Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext(),detalleError, Toast.LENGTH_LONG).show();
                }
//                objRex.Set("Fecha",f);
//                registroModificado = true;
            }
        });

        c006_txv_Fecha_Key.setText(Funciones.malograrFecha(s_fecha));
        c006_txv_Estado_Key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                s_Estado = c006_txv_Estado_Key.getText().toString();
                try {
                    obtenerPallets();
                    limpiarSelectorPallets();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        c006_txv_Pallet_Val.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (c006_txv_Pallet_Val.getText().length()>0){
                    try {
                        actualizarValores();
                        listarCajas();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    }

    private void limpiarSelectorPallets() throws Exception {
        c006_txv_Pallet_Key.setText("");
        c006_txv_Pallet_Val.setText("");
        listarCajas();
    }

    private void referenciarControles() {
        txv_PushIdentificador = (TextView) findViewById(R.id.c006_txv_PushIdentificador_v);
        txv_PushRed = (TextView) findViewById(R.id.c006_txv_PushRed_v);
        txv_PushTituloVentana = (TextView) findViewById(R.id.c006_txv_PushTituloVentana_v);

        //CODIGO PARTICULAR PARA CADA ACTIVIDAD

        c006_rvw_Lista = findViewById(R.id.c006_rvw_Lista_v);
        c006_fab_Atras = findViewById(R.id.c006_fab_Atras_v);
        c006_fab_CerrarPallet = findViewById(R.id.c006_fab_CerrarPallet_v);
        c006_fab_NuevoPallet = findViewById(R.id.c006_fab_NuevoPallet_v);
//        c006_spi_Estado = findViewById(R.id.c006_spi_Estado_v);
//        c006_spi_Pallet = findViewById(R.id.c006_spi_Pallet_v);
        c006_etx_Codigo = findViewById(R.id.c006_etx_Codigo_v);
//        c006_dtp_Fecha = findViewById(R.id.c006_dtp_Fecha_v);
        c006_txv_Seleccion = findViewById(R.id.c006_txv_Seleccion_v);
        c006_txv_Pesado = findViewById(R.id.c006_txv_Pesado_v);
        c006_txv_Empaque = findViewById(R.id.c006_txv_Empaque_v);
        c006_txv_Item = findViewById(R.id.c006_txv_Item_v);
        c006_txv_Info = findViewById(R.id.c006_txv_Info_v);
        c006_fab_LimpiarInput = findViewById(R.id.c006_fab_LimpiarInput_v);

        c006_lly_Estado = findViewById(R.id.c006_lly_Estado_v);
        c026_lly_Fecha = findViewById(R.id.c006_lly_Fecha_v);
        c006_lly_Pallet = findViewById(R.id.c006_lly_Pallet_v);

        c006_txv_Fecha_Key = findViewById(R.id.c006_txv_Fecha_Key_v);
        c006_txv_Estado_Key = findViewById(R.id.c006_txv_Estado_Key_v);
        c006_txv_Pallet_Key = findViewById(R.id.c006_txv_Pallet_Key_v);
        c006_txv_Pallet_Val = findViewById(R.id.c006_txv_Pallet_Val_v);
    }
    public void mostrarMenuUsuario(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this::onMenuItemClick);
        popup.inflate(R.menu.mnu_00000001_menu_usuario);
        popup.show();
    }

    public boolean onMenuItemClick(MenuItem item) {
        try{
            switch (item.getItemId()) {
                case R.id.opc_00000001_cambiar_clave_usuario_v:
                    Funciones.abrirActividadCambiarClave();
                    break;
                case R.id.opc_00000001_cerrar_sesion_v:
                    Funciones.preguntarSiCierraSesion(builderDialogoCerrarSesion);
                    break;
                default:
                    return false;
            }
        }catch(Exception ex){
             Funciones.mostrarError(this,ex);
            return false;
        }
        return false;
    }
    //}; ///////////////////////////////////////////////////////////////////////

//    public void cargarSpinnerPallets(){
//        ResultSet rs = null;
//        try{
//            List<String> p = new ArrayList<String>();
//            //p.add(objConfLocal.get("ID_EMPRESA"));
//            p.add(fecha);
//            p.add(estado);
//            p.add(objConfLocal.get(("ID_USUARIO_ACTUAL")));
////            rs = objConexion.doItBaby("sp_Dgm_Gen_ObtenerPallets",p);
////            MiData md = new MiData(rs);
//            Tabla t = new Tabla(objConexion.doItBaby("sp_Dgm_Gen_ObtenerPallets",p));
////            Funciones.cargarSpinner(cls_06000000_Eficiencias.this ,c006_spi_Pallet,t,0,1);
//            if(t.Filas.size()>0){
//                Funciones.cargarSpinner(cls_06000000_Eficiencias.this ,c006_spi_Pallet,t,0,1);
//                actualizarValores(((ClaveValor)(c006_spi_Pallet.getItemAtPosition(0))).getClave(),((ClaveValor)(c006_spi_Pallet.getItemAtPosition(0))).getValor());
//            }else{
//                c006_spi_Pallet.setAdapter(null);
//            }
//
//            //idPalletActual = ((ClaveValor)(c006_spi_Pallet.getItemAtPosition(0))).getClave();
////            c006_spi_Pallet.setSelection(0, true);
////            View v = c006_spi_Pallet.getSelectedView();
////            ((TextView)v).setTextColor(getResources().getColor(R.color.blanco));
//        }catch (Exception ex){
//             Funciones.mostrarError(this,ex);
//        }
//    }


    public void obtenerPallets(){
        try{
            List<String> p = new ArrayList<String>();
            p.add(s_fecha);
            p.add(s_Estado);
            p.add(objConfLocal.get(("ID_USUARIO_ACTUAL")));
//            Tabla t = new Tabla(objSql.doItBaby("sp_Dgm_Gen_ObtenerPallets",p));
            //arl_Pallets = objSqlite.arrayParaXaPopUpBuscarEnLista(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Labores"),p,"READ"));
            arl_Pallets = objSql.arrayParaXaPopUpBuscarEnLista(objSql.doItBaby("sp_Dgm_Gen_ObtenerPallets",p));
        }catch (Exception ex){
             Funciones.mostrarError(this,ex);
        }
    }

    //CONTINUAR AQUI: CUANDO CAMBIE LA DESCRIPCION DEL PALLET SE EJECUTE "ACTUALIZAR VALORES"
    private void actualizarValores() throws  Exception {
        String clave="", valor="";
        clave = c006_txv_Pallet_Key.getText().toString();
        valor = c006_txv_Pallet_Val.getText().toString();
        idPalletActual=clave;
        NroCajaActual=Integer.parseInt(valor.substring(0,valor.indexOf('/')))+1;
        NroCajasMax=Integer.parseInt(valor.substring(valor.indexOf('/') +1,valor.indexOf(' ')));
        idLinea = obtenerLinea();
        c006_txv_Item.setText(String.valueOf(NroCajaActual));
    }

    private String obtenerLinea() throws Exception {
        List<String> p = new ArrayList<String>();
        p.add(idPalletActual);
        ResultSet rs = objSql.doItBaby("DataGreen..sp_Dg_Packing_Movimientos_LecturaEficiencias_ObtenerLinea",p);
        if (!rs.next()) {
            Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext(),"ResultSet Vacio.",Toast.LENGTH_LONG).show();
            throw new Exception("Linea no encontrada");
        }else{
            //Toast.makeText(super.getBaseContext(),idLinea,Toast.LENGTH_LONG).show();
            return rs.getString(1);
        }
    }

//    private void setearSpinnerPallets() {
//        try{
//            c006_spi_Pallet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
//                    try{
//                        ClaveValor obj = (ClaveValor)(parent.getItemAtPosition(position));
//                        //String idValorSeleccionado=obj.getValor();
//                        //idPalletActual = obj.getClave();
//                        actualizarValores(obj.getClave(),obj.getValor());
//                        //Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext() ,idPalletActual + ' '+NroCajaActual+'/'+NroCajasMax, Toast.LENGTH_LONG).show();
//                        listarCajas();
//                        //cargarSpinnerPallets(fecha, estado);
//                    }catch (Exception ex){
//                        StackTraceElement z = new Exception().getStackTrace()[0];
//                        String detalleError = z.getFileName() + "\n" + z.getMethodName() + "\n" + z.getLineNumber() + ": \n" + ex.getMessage();
//                        ex.printStackTrace();
//                        Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext(),detalleError, Toast.LENGTH_LONG).show();
//                    }
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {}
//            });
//        }catch (Exception ex){
//             Funciones.mostrarError(this,ex);
//        }
//    }

    public void listarCajas() throws Exception {
        try{
            List<String> p = new ArrayList<String>();
            //p.add(objConfLocal.get("ID_EMPRESA"));
            p.add(idPalletActual);
            //p.add(objConfLocal.get(("ID_USUARIO_ACTUAL")));
            Cajas = objSql.doItBaby("DataGreen.dbo.sp_Dg_Packing_Movimientos_LecturaEficiencias_ObtenerCajasDelPalletActual",p);
            MiData md = new MiData(Cajas);
            cls_06000100_ItemRecyclerView miAdaptador = new cls_06000100_ItemRecyclerView(this,md,objConfLocal);
            c006_rvw_Lista.setAdapter(miAdaptador);
            c006_rvw_Lista.setLayoutManager(new LinearLayoutManager(this));
            //reciclador.setAdapter(miAdaptador);
        }catch (Exception ex){
             Funciones.mostrarError(this,ex);
        }
    }

    private void abrirDocumento() {
        Intent nuevaActividad = new Intent(this, cls_06010000_NuevoPallet.class);
        nuevaActividad.putExtra("ConfiguracionLocal",objConfLocal);
        //nuevaActividad.putExtra("IdDocumentoActual",id);
        startActivity(nuevaActividad);
    }
}

