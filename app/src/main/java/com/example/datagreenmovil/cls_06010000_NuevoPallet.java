package com.example.datagreenmovil;

import android.app.Dialog;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.PopUpBuscarEnLista;
import com.example.datagreenmovil.Entidades.PopUpBuscarEnLista_Item;
import com.example.datagreenmovil.Entidades.PopUpCalendario;
import com.example.datagreenmovil.Logica.Funciones;

import java.sql.ResultSet;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class cls_06010000_NuevoPallet extends AppCompatActivity {

    //OBJETOS PRINCIPALES:
    ConfiguracionLocal objConfLocal;
    ConexionBD objConexion;
    ConexionSqlite objSqlite;
    Dialog dlg_PopUp;
    TextView c015_txv_PushIdentificador, c015_txv_PushTituloVentana, c015_txv_PushRed, c015_txv_NombreApp, c015_txv_PushVersionApp, c015_txv_PushVersionDataBase;
    AlertDialog.Builder builderDialogoCerrarSesion;
    SharedPreferences sharedPreferences;

    //VARIABLES ESPECIFICAS
    String s_Fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), s_IdLinea, s_IdCultivo, s_IdVariedad, s_IdEmpaque;
    int i_CantidadPallets =1;
    ArrayList<PopUpBuscarEnLista_Item> arl_Lineas, arl_Cultivos, arl_Variedades, arl_Empaques, arl_Cantidades;
//    HashMap<String, ClaveValor[]> hmDataParaControles = new HashMap<>();
//    HashMap<String, Tabla> hmTablas = new HashMap<>();


    //CONTROLES ESPECIFICOS
    //FloatingActionButton c014_fab_Atras, c006_fab_CerrarPallet, c006_fab_NuevoPallet;
//    Spinner c015_spi_Linea, c015_spi_Cultivo, c015_spi_Variedad, c015_spi_Empaque, c015_spi_CantidadPallets;
    TextView c015_txv_Fecha_Key,
            c015_txv_Linea_Key, c015_txv_Linea_Val,
            c015_txv_Cultivo_Key, c015_txv_Cultivo_Val,
            c015_txv_Variedad_Key, c015_txv_Variedad_Val,
            c015_txv_Empaque_Key, c015_txv_Empaque_Val,
            c015_txv_Cantidad_Key, c015_txv_Cantidad_Val;
    LinearLayout c015_lly_Fecha,
            c015_lly_Linea, c015_lly_Linea_Key,
            c015_lly_Cultivo, c015_lly_Cultivo_Key,
            c015_lly_Variedad, c015_lly_Variedad_Key,
            c015_lly_Empaque, c015_lly_Empaque_Key,
            c015_lly_Cantidad, c015_lly_Cantidad_Key;
//    Button c015_btn_Cancelar, c015_btn_CrearPallets;

    //VARIABES DE SELECCION DE FECHA:
//    Calendar Calendario = Calendar.getInstance();
//    int Anio = Calendario.get(Calendar.YEAR);
//    int Mes = Calendario.get(Calendar.MONTH) + 1;
//    int Dia = Calendario.get(Calendar.DAY_OF_MONTH);
//    DatePickerDialog.OnDateSetListener CalendarioListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_06010000_nuevopallet_015);
        //COMUN{ ///////////////////////////////////////////////////////////////////
        try {
            if(getIntent().getExtras()!=null){
                objConfLocal=(ConfiguracionLocal) getIntent().getSerializableExtra("ConfiguracionLocal");
            }
            objSqlite = new ConexionSqlite(this, DataGreenApp.DB_VERSION());
//            objConfLocal.set("ULTIMA_ACTIVIDAD","NuevoPallet");
            objConexion = new ConexionBD(this);
            sharedPreferences = this.getSharedPreferences("objConfLocal", MODE_PRIVATE);

            referenciarControles();
            obtenerDataParaControles();
//            cargarControles();
            setearControles();
            Funciones.mostrarEstatusGeneral(this.getBaseContext(),objConfLocal, c015_txv_PushIdentificador, c015_txv_PushTituloVentana, c015_txv_PushRed);
        } catch (Exception ex) {
             Funciones.mostrarError(this,ex);
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        String fec =  (Dia  < 10 ? "0" + Dia : Dia) + "/" + (Mes < 10 ? "0" + Mes : Mes) + "/" + Anio;
//        c015_dtp_Fecha.setText(fec);
//    }

    //COMUN{ ///////////////////////////////////////////////////////////////////
    public void onClick(View view) {
        try {
            int idControlClickeado = view.getId();
            if (idControlClickeado == R.id.c024_txv_PushTituloVentana_v){
                Funciones.popUpTablasPendientesDeEnviar(this);
            } else if (idControlClickeado == R.id.c024_txv_PushRed_v) {
                objConexion.probarConexion(objConfLocal);
                Funciones.mostrarEstatusGeneral(this.getBaseContext(),
                        objConfLocal,
                        c015_txv_PushTituloVentana,
                        c015_txv_PushRed,
                        c015_txv_NombreApp,
                        c015_txv_PushVersionApp,
                        c015_txv_PushVersionDataBase,
                        c015_txv_PushIdentificador
                );
            } else if (idControlClickeado == R.id.c024_txv_PushVersionApp_v || idControlClickeado == R.id.c024_txv_PushVersionDataBase_v) {
                Funciones.popUpStatusVersiones(this);
            } else if (idControlClickeado == R.id.c024_txv_PushIdentificador_v) {
                mostrarMenuUsuario(this.c015_txv_PushIdentificador);
            } else if (idControlClickeado==R.id.c015_lly_Fecha_v) {
                PopUpCalendario d = new PopUpCalendario(this,c015_txv_Fecha_Key);
                d.show();
            } else if (idControlClickeado==R.id.c015_lly_Linea_Key_v) {
                PopUpBuscarEnLista d = new PopUpBuscarEnLista(this,arl_Lineas,c015_txv_Linea_Key,c015_txv_Linea_Val);
                d.show();
            } else if (idControlClickeado==R.id.c015_lly_Cultivo_Key_v) {
                PopUpBuscarEnLista d = new PopUpBuscarEnLista(this,arl_Cultivos,c015_txv_Cultivo_Key,c015_txv_Cultivo_Val);
                d.show();
            } else if (idControlClickeado==R.id.c015_lly_Variedad_Key_v) {
                PopUpBuscarEnLista d = new PopUpBuscarEnLista(this,arl_Variedades,c015_txv_Variedad_Key,c015_txv_Variedad_Val);
                d.show();
            } else if (idControlClickeado==R.id.c015_lly_Empaque_Key_v) {
                PopUpBuscarEnLista d = new PopUpBuscarEnLista(this,arl_Empaques,c015_txv_Empaque_Key,c015_txv_Empaque_Val);
                d.show();
            } else if (idControlClickeado==R.id.c015_lly_Cantidad_Key_v) {
                PopUpBuscarEnLista d = new PopUpBuscarEnLista(this,arl_Cantidades,c015_txv_Cantidad_Key,c015_txv_Cantidad_Val);
                d.show();
            } else if (idControlClickeado==R.id.c015_fab_CrearPallet_v) {
                crearPallets();
            } else if (idControlClickeado==R.id.c015_fab_Atras_v) {
                finish();
            }else throw new IllegalStateException("Click sin programacion: " + view.getId());

//            switch (view.getId()) {
//                case R.id.c015_txv_PushIdentificador_v:
//                    mostrarMenuUsuario(this.c015_txv_PushIdentificador);
//                    break;
//                case R.id.c015_txv_PushRed_v:
//                    objConexion.probarConexion(objConfLocal);
//                    Funciones.mostrarEstatusGeneral(this.getBaseContext(),objConfLocal, c015_txv_PushIdentificador, c015_txv_TituloVentana, c015_txv_PushRed);
//                    break;
//                //CODIGO PARTICULAR PARA CADA ACTIVIDAD
//                case R.id.c015_btn_Cancelar_v:
//                    finish();
////                    Intent intent = new Intent(this, cls_05000000_Tareos.class);
////                    intent.putExtra("ConfiguracionLocal",objConfLocal);
////                    startActivity(intent);
//                    break;
//                case R.id.c015_btn_CrearPallets_v:
//                    crearPallets();
//                    break;
//                default:
//                    throw new IllegalStateException("Click sin programacion: " + view.getId());
//            }
        } catch (Exception ex) {
             Funciones.mostrarError(this,ex);
        }
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
                    //Funciones.abrirActividadCambiarClave();
                    dlg_PopUp = Funciones.obtenerDialogParaCambiarClave(this,objConfLocal,objSqlite,this);
                    dlg_PopUp.show();
                    break;
                case R.id.opc_00000001_cerrar_sesion_v:
                    //Funciones.preguntarSiCierraSesion(builderDialogoCerrarSesion);
                    dlg_PopUp = Funciones.obtenerDialogParaCerrarSesion(this,objConfLocal,objSqlite,this);
                    dlg_PopUp.show();
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

    private void referenciarControles() {
        c015_txv_PushIdentificador = (TextView) findViewById(R.id.c015_txv_PushIdentificador_v);
        c015_txv_PushRed = (TextView) findViewById(R.id.c015_txv_PushRed_v);
        c015_txv_PushTituloVentana = (TextView) findViewById(R.id.c015_txv_PushTituloVentana_v);

        //CODIGO PARTICULAR PARA CADA ACTIVIDAD
//        c015_spi_Linea = findViewById(R.id.c015_spi_Linea_v);
//        c015_spi_Cultivo = findViewById(R.id.c015_spi_Cultivo_v);
//        c015_spi_Variedad = findViewById(R.id.c015_spi_Variedad_v);
//        c015_spi_Empaque = findViewById(R.id.c015_spi_Empaque_v);
//        c015_spi_CantidadPallets = findViewById(R.id.c015_spi_CantidadPalletes_v);
//        c015_dtp_Fecha = findViewById(R.id.c015_dtp_Fecha_v);
//        c015_btn_Cancelar = findViewById(R.id.c015_btn_Cancelar_v);
//        c015_btn_CrearPallets = findViewById(R.id.c015_btn_CrearPallets_v);
        c015_txv_Fecha_Key = findViewById(R.id.c015_txv_Fecha_Key_v);
        c015_txv_Linea_Key = findViewById(R.id.c015_txv_Linea_Key_v);
        c015_txv_Linea_Val = findViewById(R.id.c015_txv_Linea_Val_v);
        c015_txv_Cultivo_Key = findViewById(R.id.c015_txv_Cultivo_Key_v);
        c015_txv_Cultivo_Val = findViewById(R.id.c015_txv_Cultivo_Val_v);
        c015_txv_Variedad_Key = findViewById(R.id.c015_txv_Variedad_Key_v);
        c015_txv_Variedad_Val = findViewById(R.id.c015_txv_Variedad_Val_v);
        c015_txv_Empaque_Key = findViewById(R.id.c015_txv_Empaque_Key_v);
        c015_txv_Empaque_Val = findViewById(R.id.c015_txv_Empaque_Val_v);
        c015_txv_Cantidad_Key = findViewById(R.id.c015_txv_Cantidad_Key_v);
        c015_txv_Cantidad_Val = findViewById(R.id.c015_txv_Cantidad_Val_v);
        c015_lly_Fecha = findViewById(R.id.c015_lly_Fecha_v);
        c015_lly_Linea = findViewById(R.id.c015_lly_Linea_v);
        c015_lly_Linea_Key = findViewById(R.id.c015_lly_Linea_Key_v);
        c015_lly_Cultivo = findViewById(R.id.c015_lly_Cultivo_v);
        c015_lly_Cultivo_Key = findViewById(R.id.c015_lly_Cultivo_Key_v);
        c015_lly_Variedad = findViewById(R.id.c015_lly_Variedad_v);
        c015_lly_Variedad_Key = findViewById(R.id.c015_lly_Variedad_Key_v);
        c015_lly_Empaque = findViewById(R.id.c015_lly_Empaque_v);
        c015_lly_Empaque_Key = findViewById(R.id.c015_lly_Empaque_Key_v);
        c015_lly_Cantidad = findViewById(R.id.c015_lly_Cantidad_v);
        c015_lly_Cantidad_Key = findViewById(R.id.c015_lly_Cantidad_Key_v);
    }

    //PARA SELECTOR DE FECHA /////////////////////////////////////////
    private void setearSelectorFecha() {
//        c015_txv_Fecha_Key.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Mes = Mes - 1;
//                DatePickerDialog datePickerDialog = new DatePickerDialog(cls_06010000_NuevoPallet.this, android.R.style.Theme_Holo, CalendarioListener, Anio, Mes, Dia);
//                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                datePickerDialog.show();
//            }
//        }
//        );
//        CalendarioListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int anio, int mes, int dia) {
//                mes = mes + 1;
//                Dia =dia;
//                Mes =mes;
//                Anio =anio;
//                String fec =  (Dia  < 10 ? "0" + Dia : Dia) + "/" + (Mes < 10 ? "0" + Mes : Mes) + "/" + Anio;
//                c015_txv_Fecha_Key.setText(fec);
//                //desde = Anio + "-" + (mes < 10 ? "0" + mes : mes) + "-" + (dia  < 10 ? "0" + dia : dia);
//                fecha = Anio + "-" + (Mes < 10 ? "0" + Mes : Mes) + "-" + (Dia  < 10 ? "0" + Dia : Dia);
//                //Toast.makeText(cls_06000000_Eficiencias.super.getBaseContext(), fecha, Toast.LENGTH_SHORT).show();
//            }
//        };
        c015_txv_Fecha_Key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    s_Fecha =Funciones.arreglarFecha(c015_txv_Fecha_Key.getText().toString());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    //FUNCIONES ESPECIFICAS{////////////////////////////
    private void obtenerDataParaControles() throws Exception {
//        ResultSet rs = null;
//        try{
//            List<String> p = new ArrayList<String>();
//            p.add(objConfLocal.get("ID_EMPRESA"));
//            rs = objConexion.doItBaby("sp_Dgm_Gen_ObtenerLineas",p);
////            MiData md = new MiData(rs);
//            Tabla t = new Tabla(rs);
////            hmDataParaControles.put("LINEAS",ClaveValor.getArrayClaveValor(md,0,2) );
//            hmTablas.put("LINEAS",t);
//
//            rs = objConexion.doItBaby("sp_Dgm_Gen_ObtenerCultivosPacking",p);
////            md = new MiData(rs);
//            t = new Tabla(rs);
////            hmDataParaControles.put("CULTIVOS",ClaveValor.getArrayClaveValor(md,0,2) );
//            hmTablas.put("CULTIVOS",t);
//
//            rs = objConexion.doItBaby("sp_Dgm_Gen_ObtenerCantidadesPallets",p);
////            md = new MiData(rs);
//            t = new Tabla(rs);
////            hmDataParaControles.put("CANTIDADES_PALLETS",ClaveValor.getArrayClaveValor(md,0,2) );
//            hmTablas.put("CANTIDADES_PALLETS",t);
//
//            /*
//            List<String> p = new ArrayList<>();
//            p.add(objConfLocal.get("ID_EMPRESA"));
//            MiData md = new MiData(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Lineas"),p,"READ"));
//            hmDataParaControles.put("LINEAS",ClaveValor.getArrayClaveValor(md,0,2) );
//            md = new MiData(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Cultivos"),p,"READ"));
//            hmDataParaControles.put("CULTIVOS",ClaveValor.getArrayClaveValor(md,0,2) );
//            md = new MiData(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Variedades"),p,"READ"));
//            hmDataParaControles.put("VARIEDADES",ClaveValor.getArrayClaveValor(md,0,2) );
//            md = new MiData(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Empaques"),p,"READ"));
//            hmDataParaControles.put("EMPAQUES",ClaveValor.getArrayClaveValor(md,0,2) );
//            md = new MiData(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_CantidadesPallets"),p,"READ"));
//            hmDataParaControles.put("CANTIDADES_PALLETS",ClaveValor.getArrayClaveValor(md,0,2) );
//            */
//
//        }catch (Exception ex){
//             Funciones.mostrarError(this,ex);
//        }

        try{
            List<String> p = new ArrayList<String>();
//            p.add(objConfLocal.get("ID_EMPRESA"));
            p.add(sharedPreferences.getString("ID_EMPRESA", "!ID_EMPRESA"));
//            sharedPreferences.getString("ID_EMPRESA", "!ID_EMPRESA");
            arl_Lineas = objConexion.arrayParaXaPopUpBuscarEnLista(objConexion.doItBaby("sp_Dgm_Gen_ObtenerLineas",p));
            arl_Cultivos = objConexion.arrayParaXaPopUpBuscarEnLista(objConexion.doItBaby("sp_Dgm_Gen_ObtenerCultivosPacking",p));
//            arl_Variedades = objConexion.arrayParaXaPopUpBuscarEnLista(objConexion.doItBaby("sp_Dgm_Gen_ObtenerLineas",p));
//            arl_Empaques = objConexion.arrayParaXaPopUpBuscarEnLista(objConexion.doItBaby("sp_Dgm_Gen_ObtenerLineas",p));
            arl_Cantidades = objConexion.arrayParaXaPopUpBuscarEnLista(objConexion.doItBaby("sp_Dgm_Gen_ObtenerCantidadesPallets",p));
        }catch (Exception ex){
            Funciones.mostrarError(this,ex);
        }
    }

//    private void cargarControles()throws Exception {
//        try{
////            Funciones.cargarSpinner(this, c015_spi_Linea,hmDataParaControles.get("LINEAS"));
//            Funciones.cargarSpinner(this, c015_spi_Linea,hmTablas.get("LINEAS"),0,1);
//            Funciones.cargarSpinner(this, c015_spi_Cultivo,hmTablas.get("CULTIVOS"),0,1);
////            Funciones.cargarSpinner(this, c015_spi_Variedad,hmDataParaControles.get("VARIEDADES"));
////            Funciones.cargarSpinner(this, c015_spi_Empaque,hmDataParaControles.get("EMPAQUES"));
//            Funciones.cargarSpinner(this, c015_spi_CantidadPallets,hmTablas.get("CANTIDADES_PALLETS"),0,1);
//        }catch (Exception ex){
//             Funciones.mostrarError(this,ex);
//        }
//    }

    private void setearControles() {
//        builderDialogoCerrarSesion= Funciones.setearAlertDialogParaCerrarSesion_(objConfLocal,objSqlite,this);
        //CODIGO PARTICULAR PARA CADA ACTIVIDAD
        setearSelectorFecha();
        setearSelectorLinea();
        setearSelectorCultivo();
        setearSelectorVariedad();
        setearSelectorEmpaque();
        setearSelectorCantidadPallets();
    }

    private void setearSelectorLinea() {
//        try{
//            c015_spi_Linea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
//                    idLinea = ((ClaveValor)(parent.getItemAtPosition(position))).getClave();
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {}
//            });
//        }catch (Exception ex){
//             Funciones.mostrarError(this,ex);
//        }

        c015_txv_Linea_Val.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                s_IdLinea = c015_txv_Linea_Key.getText().toString();
            }
        });
    }

    private void setearSelectorCultivo() {
//        try{
//            c015_spi_Cultivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
//                    try{
//                        idCultivo = ((ClaveValor)(parent.getItemAtPosition(position))).getClave();
//                        List<String> p = new ArrayList<String>();
//                        //p.add(objConfLocal.get("ID_EMPRESA"));
//                        p.add(idCultivo);
//                        p.add("D");
//                        ResultSet rs = objConexion.doItBaby("DataGreen..sp_Gen_ObtenerVariedadesPacking",p);
//                        Tabla t = new Tabla(rs);
////                        hmDataParaControles.put("VARIEDADES",ClaveValor.getArrayClaveValor(new MiData(rs),0,2) );
//                        hmTablas.put("VARIEDADES",t);
//                        Funciones.cargarSpinner(cls_06010000_NuevoPallet.super.getBaseContext(), c015_spi_Variedad,hmTablas.get("VARIEDADES"),0,1);
//                    }catch (Exception ex){
//                        StackTraceElement z = new Exception().getStackTrace()[0];
//                        String detalleError = z.getFileName() + "\n" + z.getMethodName() + "\n" + z.getLineNumber() + ": \n" + ex.getMessage();
//                        ex.printStackTrace();
//                        Toast.makeText(cls_06010000_NuevoPallet.super.getBaseContext(),detalleError, Toast.LENGTH_LONG).show();
//                    }
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {}
//            });
//        }catch (Exception ex){
//             Funciones.mostrarError(this,ex);
//        }
        try{
            c015_txv_Cultivo_Key.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void afterTextChanged(Editable editable) {
                    s_IdCultivo = c015_txv_Cultivo_Key.getText().toString();
                    List<String> p = new ArrayList<String>();
                    p.add(s_IdCultivo);
                    p.add("D");
                    try {
                        arl_Variedades = objConexion.arrayParaXaPopUpBuscarEnLista(objConexion.doItBaby("DataGreen..sp_Gen_ObtenerVariedadesPacking",p));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    c015_txv_Variedad_Key.setText("");
                    c015_txv_Variedad_Val.setText("");
                    s_IdVariedad="";

                    c015_txv_Empaque_Key.setText("");
                    c015_txv_Empaque_Val.setText("");
                    s_IdEmpaque="";
//                ResultSet rs = objConexion.doItBaby("DataGreen..sp_Gen_ObtenerVariedadesPacking",p);
//                Tabla t = new Tabla(rs);
////                        hmDataParaControles.put("VARIEDADES",ClaveValor.getArrayClaveValor(new MiData(rs),0,2) );
//                hmTablas.put("VARIEDADES",t);
//                Funciones.cargarSpinner(cls_06010000_NuevoPallet.super.getBaseContext(), c015_spi_Variedad,hmTablas.get("VARIEDADES"),0,1);

//                try{
//                    List<String> p = new ArrayList<String>();
//                    p.add(objConfLocal.get("ID_EMPRESA"));
//                    arl_Lineas = objConexion.arrayParaXaPopUpBuscarEnLista(objConexion.doItBaby("sp_Dgm_Gen_ObtenerLineas",p));
//                    arl_Cultivos = objConexion.arrayParaXaPopUpBuscarEnLista(objConexion.doItBaby("sp_Dgm_Gen_ObtenerCultivosPacking",p));
////            arl_Variedades = objConexion.arrayParaXaPopUpBuscarEnLista(objConexion.doItBaby("sp_Dgm_Gen_ObtenerLineas",p));
////            arl_Empaques = objConexion.arrayParaXaPopUpBuscarEnLista(objConexion.doItBaby("sp_Dgm_Gen_ObtenerLineas",p));
//                    arl_Cantidades = objConexion.arrayParaXaPopUpBuscarEnLista(objConexion.doItBaby("sp_Dgm_Gen_ObtenerCantidadesPallets",p));
//                }catch (Exception ex){
//                    Funciones.mostrarError(this,ex);
//                }
                }
            });
        }catch (Exception ex){
            StackTraceElement z = new Exception().getStackTrace()[0];
            String detalleError = z.getFileName() + "\n" + z.getMethodName() + "\n" + z.getLineNumber() + ": \n" + ex.getMessage();
            Funciones.notificar(cls_06010000_NuevoPallet.super.getBaseContext(),detalleError);
        }
    }

    private void setearSelectorVariedad() {
//        try{
//            c015_spi_Variedad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
//                    try{
//                        idVariedad = ((ClaveValor)(parent.getItemAtPosition(position))).getClave();
//                        List<String> p = new ArrayList<String>();
//                        p.add(idCultivo);
//                        p.add(idVariedad);
//                        p.add("");
//                        ResultSet rs = objConexion.doItBaby("DataGreen..sp_Gen_ObtenerEmpaquesXConsumidorPacking",p);
//                        Tabla t = new Tabla(rs);
////                        hmDataParaControles.put("EMPAQUES",ClaveValor.getArrayClaveValor(new MiData(rs),0,2) );
//                        hmTablas.put("EMPAQUES",t);
//                        Funciones.cargarSpinner(cls_06010000_NuevoPallet.super.getBaseContext(), c015_spi_Empaque,hmTablas.get("EMPAQUES"),0,1);
//                    }catch (Exception ex){
//                        StackTraceElement z = new Exception().getStackTrace()[0];
//                        String detalleError = z.getFileName() + "\n" + z.getMethodName() + "\n" + z.getLineNumber() + ": \n" + ex.getMessage();
//                        ex.printStackTrace();
//                        Toast.makeText(cls_06010000_NuevoPallet.super.getBaseContext(),detalleError, Toast.LENGTH_LONG).show();
//                    }
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {}
//            });
//        }catch (Exception ex){
//             Funciones.mostrarError(this,ex);
//        }
        c015_txv_Variedad_Val.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
//                try{
//                        idVariedad = ((ClaveValor)(parent.getItemAtPosition(position))).getClave();
//                        List<String> p = new ArrayList<String>();
//                        p.add(idCultivo);
//                        p.add(idVariedad);
//                        p.add("");
//                        ResultSet rs = objConexion.doItBaby("DataGreen..sp_Gen_ObtenerEmpaquesXConsumidorPacking",p);
//                        Tabla t = new Tabla(rs);
////                        hmDataParaControles.put("EMPAQUES",ClaveValor.getArrayClaveValor(new MiData(rs),0,2) );
//                        hmTablas.put("EMPAQUES",t);
//                        Funciones.cargarSpinner(cls_06010000_NuevoPallet.super.getBaseContext(), c015_spi_Empaque,hmTablas.get("EMPAQUES"),0,1);
//                    }catch (Exception ex){
//                        StackTraceElement z = new Exception().getStackTrace()[0];
//                        String detalleError = z.getFileName() + "\n" + z.getMethodName() + "\n" + z.getLineNumber() + ": \n" + ex.getMessage();
//                        ex.printStackTrace();
//                        Toast.makeText(cls_06010000_NuevoPallet.super.getBaseContext(),detalleError, Toast.LENGTH_LONG).show();
//                    }

                try{
                    s_IdVariedad = c015_txv_Variedad_Key.getText().toString();
                    List<String> p = new ArrayList<String>();
                    p.add(s_IdCultivo);
                    p.add(s_IdVariedad);
                    p.add("");
                    try {
                        arl_Empaques = objConexion.arrayParaXaPopUpBuscarEnLista(objConexion.doItBaby("DataGreen..sp_Gen_ObtenerEmpaquesXConsumidorPacking",p));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }catch (Exception ex){
                    StackTraceElement z = new Exception().getStackTrace()[0];
                    String detalleError = z.getFileName() + "\n" + z.getMethodName() + "\n" + z.getLineNumber() + ": \n" + ex.getMessage();
                    Funciones.notificar(cls_06010000_NuevoPallet.super.getBaseContext(),detalleError);
                }

//                ResultSet rs = objConexion.doItBaby("DataGreen..sp_Gen_ObtenerEmpaquesXConsumidorPacking",p);
////                Tabla t = new Tabla(rs);
//////                        hmDataParaControles.put("EMPAQUES",ClaveValor.getArrayClaveValor(new MiData(rs),0,2) );
////                hmTablas.put("EMPAQUES",t);
////                Funciones.cargarSpinner(cls_06010000_NuevoPallet.super.getBaseContext(), c015_spi_Empaque,hmTablas.get("EMPAQUES"),0,1);
//
//                s_IdCultivo = c015_txv_Cultivo_Key.getText().toString();
//                List<String> p = new ArrayList<String>();
//                p.add(s_IdCultivo);
//                p.add("D");
//                try {
//                    arl_Lineas = objConexion.arrayParaXaPopUpBuscarEnLista(objConexion.doItBaby("DataGreen..sp_Gen_ObtenerVariedadesPacking",p));
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
            }
        });
    }

    private void setearSelectorEmpaque() {
//        try{
//            c015_spi_Empaque.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
//                    idEmpaque = ((ClaveValor)(parent.getItemAtPosition(position))).getClave();
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {}
//            });
//        }catch (Exception ex){
//             Funciones.mostrarError(this,ex);
//        }
        c015_txv_Empaque_Val.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                s_IdEmpaque = c015_txv_Empaque_Key.getText().toString();
            }
        });
    }

    private void setearSelectorCantidadPallets() {
//        try{
//            c015_spi_CantidadPallets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
//                    idCantidadPallets = Integer.parseInt(((ClaveValor)(parent.getItemAtPosition(position))).getClave());
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {}
//            });
//        }catch (Exception ex){
//             Funciones.mostrarError(this,ex);
//        }
        c015_txv_Cantidad_Val.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                i_CantidadPallets = Integer.parseInt(c015_txv_Cantidad_Key.getText().toString());
            }
        });
    }

    private void crearPallets() {
        try{
            for(int i = 0; i< i_CantidadPallets; i++){
                List<String> p = new ArrayList<String>();
                //p.add(objConfLocal.get("ID_EMPRESA"));
                //p.add(idCultivo);
                p.add(s_IdVariedad);
                p.add(s_IdEmpaque);
                p.add(s_IdLinea);
//                p.add(objConfLocal.get("ID_USUARIO_ACTUAL"));
                p.add(sharedPreferences.getString("ID_USUARIO_ACTUAL", "!ID_USUARIO_ACTUAL"));
                p.add(s_Fecha);
                ResultSet rs = objConexion.doItBaby("DataGreen..sp_Dg_Packing_Movimientos_LecturaEficiencias_CrearPallet",p);
                //hmDataParaControles.put("EMPAQUES",ClaveValor.getArrayClaveValor(new MiData(rs),0,2) );
            }
            Toast.makeText(this, i_CantidadPallets + " pallet(s) creado(s) correctamente.",Toast.LENGTH_LONG).show();
            finish();
        }catch (Exception ex){
             Funciones.mostrarError(this,ex);
        }
    }

}