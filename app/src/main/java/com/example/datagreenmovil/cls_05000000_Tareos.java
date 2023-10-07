//package com.example.datagreenmovil;
//
//import android.app.Dialog;
//import android.content.Intent;
//import android.content.pm.ActivityInfo;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.PopupMenu;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.datagreenmovil.Conexiones.ConexionBD;
//import com.example.datagreenmovil.Conexiones.ConexionSqlite;
//import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
//import com.example.datagreenmovil.Entidades.PopUpCalendario;
//import com.example.datagreenmovil.Entidades.Tareo;
//import com.example.datagreenmovil.Logica.Funciones;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//
//import java.sql.ResultSet;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//public class cls_05000000_Tareos extends AppCompatActivity {
//    //@Jota:2023-05-27 -> INICIO DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
//    ConexionSqlite objSqlite;
//    ConexionBD objSql;
//    ConfiguracionLocal objConfLocal;
//    TextView c005_txv_PushTituloVentana, c005_txv_PushRed, c005_txv_NombreApp, c005_txv_PushVersionApp, c005_txv_PushVersionDataBase, c005_txv_PushIdentificador;
//    //    AlertDialog.Builder builderDialogoCerrarSesion;
//    Dialog dlg_PopUp;
//    //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
//    //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
//    //...
//
//    Cursor c_Registros;
//    RecyclerView c005_rcv_Reciclador;
//    RadioGroup c005_rgr_Estado;
//    RadioButton c005_rad_Pendientes, c005_rad_Todos;// rad_Fecha, rad_Ultimos7Dias;
//    FloatingActionButton c005_fab_MainTareos_Volver, c005_fab_MainTareos_Opciones, c005_fab_MainTareos_NuevoTareo;
//    //TextView c005_txv_Desde, c005_txv_Hasta;
//    TextView c005_txv_DesdeFecha, c005_txv_HastaFecha;
//    ArrayList<String> al_RegistrosSeleccionados=new ArrayList<>();
//    String s_DesdeFecha = LocalDate.now().plusDays(-7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//    String s_HastaFecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //--desde, hasta, estado;
//    String s_ListarIdEstado ="PE";
//
//    //VARIABES DE SELECCION DE FECHA:
////    Calendar Calendario = Calendar.getInstance();
////    int Anio = Calendario.get(Calendar.YEAR);
////    int Mes = Calendario.get(Calendar.MONTH) + 1;
////    int Dia = Calendario.get(Calendar.DAY_OF_MONTH);
////    DatePickerDialog.OnDateSetListener CalendarioListenerDesde;
////    DatePickerDialog.OnDateSetListener CalendarioListenerHasta;
//
//    String s_ListarDesde = LocalDate.now().plusDays(-7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//    String s_ListarHasta = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //--desde, hasta, estado;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.v_05000000_tareos_005);
//        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        //@Jota:2023-05-27 -> INICIO DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
//        try{
//            if(getIntent().getExtras()!=null){
//                objConfLocal=(ConfiguracionLocal) getIntent().getSerializableExtra("ConfiguracionLocal");
////                objConfLocal=new ConfiguracionLocal(objSqlite.obtenerConfiguracionLocal(objConfLocal));
//            }
//            objSql = new ConexionBD(objConfLocal);
//            objSqlite = new ConexionSqlite(this,objConfLocal);
//            objConfLocal.set("ULTIMA_ACTIVIDAD","PlantillaBase");
//
//            referenciarControles();
//            setearControles();
//            Funciones.mostrarEstatusGeneral(this.getBaseContext(),
//                    objConfLocal,
//                    c005_txv_PushTituloVentana,
//                    c005_txv_PushRed,
//                    c005_txv_NombreApp,
//                    c005_txv_PushVersionApp,
//                    c005_txv_PushVersionDataBase,
//                    c005_txv_PushIdentificador
//            );
//            //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
//            //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
//            //...
//
//            listarRegistros();
//        } catch (Exception ex) {
//             Funciones.mostrarError(this,ex);
//        }
//    }
//
//    @Override
//    public void onResume(){
//        super.onResume();
//        // put your code here...
//        try {
//            objSqlite.ActualizarDataPendiente(objConfLocal);
//            Funciones.mostrarEstatusGeneral(this.getBaseContext(),
//                    objConfLocal,
//                    c005_txv_PushTituloVentana,
//                    c005_txv_PushRed,
//                    c005_txv_NombreApp,
//                    c005_txv_PushVersionApp,
//                    c005_txv_PushVersionDataBase,
//                    c005_txv_PushIdentificador
//            );
//            listarRegistros();
//        } catch (Exception ex) {
//             Funciones.mostrarError(this,ex);
//        }
//
//    }
//
//    /*ESTE CODIGO ES IMPORTANTE PORQUE IMPLEMENTA OPCIONES DE MENU EN BARRA DE TITULO, PERO POR AHORA USAREMOS MENU POPUPS
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.mnu_05000000_menu_principal, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        //Toast.makeText(this, "Selected Item: " +item.getTitle(), Toast.LENGTH_SHORT).show();
//        switch (item.getItemId()) {
//            case R.id.opc_05000000_transferir:
//                Toast.makeText(this,"Transferir",Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.opc_05000000_extornar:
//                // do your code
//                return true;
//            case R.id.opc_05000000_eliminar:
//                // do your code
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//    */
//
//    //@Jota:2023-05-27 -> LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
//    private void setearControles() {
//        //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
//        //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
//        //...
////        setearSelectorFechaDesde();
////        setearSelectorFechaHasta();
//        c005_txv_DesdeFecha.setText(Funciones.malograrFecha(s_ListarDesde));
//        c005_txv_HastaFecha.setText(Funciones.malograrFecha(s_ListarHasta));
//        c005_txv_DesdeFecha.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
////                String s_Valor = "";
////                s_Valor = c022_txv_DesdeFecha.getText().toString();
//                s_ListarDesde = Funciones.arreglarFecha(c005_txv_DesdeFecha.getText().toString());
////                s_ListarDesde = Funciones.arreglarFecha(s_Valor);
//                try {
//                    listarRegistros();
//                } catch (Exception ex) {
//                    //throw new RuntimeException(e);
//                    Funciones.mostrarError(getBaseContext(), ex);
//                }
//            }
//        });
//        c005_txv_HastaFecha.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                s_ListarHasta = Funciones.arreglarFecha(c005_txv_HastaFecha.getText().toString());
//                try {
//                    listarRegistros();
//                } catch (Exception ex) {
//                    //throw new RuntimeException(e);
//                    Funciones.mostrarError(getBaseContext(), ex);
//                }
//            }
//        });
//        c005_rgr_Estado.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                if (i == R.id.c005_rad_Todos_v) {
//                    //Toast.makeText(getBaseContext(),"todos",Toast.LENGTH_LONG).show();
//                    s_ListarIdEstado = "**";
//                } else if (i == R.id.c005_rad_Pendientes_v) {
//                    //Toast.makeText(getBaseContext(),"pendientes",Toast.LENGTH_LONG).show();
//                    s_ListarIdEstado = "PE";
//                }
//                try {
//                    listarRegistros();
//                } catch (Exception ex) {
//                    //throw new RuntimeException(e);
//                    Funciones.mostrarError(getBaseContext(), ex);
//                }
//            }
//        });
//    }
//
//    private void referenciarControles() {
//        c005_txv_PushTituloVentana = findViewById(R.id.c005_txv_PushTituloVentana_v);
//        c005_txv_PushRed = findViewById(R.id.c005_txv_PushRed_v);
//        c005_txv_NombreApp = findViewById(R.id.c005_txv_NombreApp_v);
//        c005_txv_PushVersionApp = findViewById(R.id.c005_txv_PushVersionApp_v);
//        c005_txv_PushVersionDataBase = findViewById(R.id.c005_txv_PushVersionDataBase_v);
//        c005_txv_PushIdentificador = findViewById(R.id.c005_txv_PushIdentificador_v);
//
//        //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
//        //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
//        //...
//        c005_rcv_Reciclador =findViewById(R.id.c005_rcv_Reciclador_v);
//        c005_rgr_Estado = findViewById(R.id.c005_rgr_Estado_v);
//        c005_rad_Pendientes = findViewById(R.id.c005_rad_Pendientes_v);
//        c005_rad_Todos = findViewById(R.id.c005_rad_Todos_v);
//        c005_fab_MainTareos_Volver = findViewById(R.id.c005_fab_MainTareos_Volver_v);
//        c005_fab_MainTareos_Opciones = findViewById(R.id.c005_fab_MainTareos_Opciones_v);
//        c005_fab_MainTareos_NuevoTareo = findViewById(R.id.c005_fab_MainTareos_NuevoTareo_v);
//        c005_txv_DesdeFecha = findViewById(R.id.c005_txv_DesdeFecha_v);
//        c005_txv_HastaFecha = findViewById(R.id.c005_txv_HastaFecha_v);
//    }
//
//    public void mostrarMenuUsuario(View v) {
//        PopupMenu popup = new PopupMenu(this, v);
//        popup.setOnMenuItemClickListener(this::onMenuItemClick);
//        popup.inflate(R.menu.mnu_00000001_menu_usuario);
//        popup.show();
//    }
//
//    public boolean onMenuItemClick(MenuItem item) {
//        try{
//            int idControlClickeado = item.getItemId();
//            if (idControlClickeado==R.id.opc_00000001_cambiar_clave_usuario_v){
//                dlg_PopUp = Funciones.obtenerDialogParaCambiarClave(this,objConfLocal,objSqlite,this);
//                dlg_PopUp.show();
//            } else if (idControlClickeado==R.id.opc_00000001_cerrar_sesion_v) {
//                dlg_PopUp = Funciones.obtenerDialogParaCerrarSesion(this,objConfLocal,objSqlite,this);
//                dlg_PopUp.show();
//            }else return false;
//        }catch(Exception ex){
//            Funciones.mostrarError(this,ex);
//            return false;
//        }
//        return true;
//    }
//
//    public void onClick(View view) {
//        try {
//            int idControlClickeado = view.getId();
//            if (idControlClickeado == R.id.c005_txv_PushTituloVentana_v){
//                Funciones.popUpTablasPendientesDeEnviar(this);
//            } else if (idControlClickeado == R.id.c005_txv_PushRed_v) {
//                objSql.probarConexion(objConfLocal);
//                Funciones.mostrarEstatusGeneral(this.getBaseContext(),
//                        objConfLocal,
//                        c005_txv_PushTituloVentana,
//                        c005_txv_PushRed,
//                        c005_txv_NombreApp,
//                        c005_txv_PushVersionApp,
//                        c005_txv_PushVersionDataBase,
//                        c005_txv_PushIdentificador
//                );
//            } else if (idControlClickeado == R.id.c005_txv_PushVersionApp_v || idControlClickeado == R.id.c005_txv_PushVersionDataBase_v) {
//                Funciones.popUpStatusVersiones(this);
//            } else if (idControlClickeado == R.id.c005_txv_PushIdentificador_v) {
//                mostrarMenuUsuario(this.c005_txv_PushIdentificador);
//            }
//            //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
//            //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
//            //...
//            else if (idControlClickeado == R.id.c005_fab_MainTareos_Volver_v) {
//                finish();
//            } else if (idControlClickeado == R.id.c005_fab_MainTareos_NuevoTareo_v) {
//                abrirDocumento(null);
//            } else if (idControlClickeado == R.id.c005_rad_Pendientes_v) {
//                s_ListarIdEstado ="PE";
//                listarRegistros();
//            } else if (idControlClickeado == R.id.c005_rad_Todos_v) {
//                s_ListarIdEstado ="";
//                listarRegistros();
//            }else if (idControlClickeado == R.id.c005_txv_DesdeFecha_v) {
//                PopUpCalendario d = new PopUpCalendario(this, c005_txv_DesdeFecha);
//                d.show();
//            } else if (idControlClickeado == R.id.c005_txv_HastaFecha_v) {
//                PopUpCalendario d = new PopUpCalendario(this, c005_txv_HastaFecha);
//                d.show();
//            }
//            else throw new IllegalStateException("Click sin programacion: " + view.getId());
//        } catch (Exception ex) {
//            Funciones.mostrarError(this,ex);
//        }
//    }
//    //@Jota:
//    //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
//    //...
//
//    public void mostrarMenuOpciones(View v) {
//        PopupMenu popup = new PopupMenu(this, v);
//        popup.setOnMenuItemClickListener(this::onMenuItemClickPrincipal);
//        popup.inflate(R.menu.mnu_05000000_menu_principal);
//        popup.show();
//    }
//
//    //@Override
//    public boolean onMenuItemClickPrincipal(MenuItem item) {
//        try{
//            int idControlClickeado = item.getItemId();
//            if (idControlClickeado==R.id.opc_05000000_transferir){
//                if (transferirTareos()){
//                    al_RegistrosSeleccionados.clear();
//                    listarRegistros();
//                    Funciones.notificar(this,"Proceso finalizado correctamente.");
//                    //Toast.makeText(this,"Tareos transferidos correctamente.",Toast.LENGTH_SHORT).show();
//                    return true;
//                }
//                Funciones.notificar(this,objConfLocal.get("MENSAJE"));
//                return false;
//            } else if (idControlClickeado==R.id.opc_05000000_extornar) {
//                if (procesarTareos("EXTORNAR")){
//                    Funciones.notificar(this,"Tareos extronados correctamente.");
//                    //Toast.makeText(this,"Tareos extornados correctamente.",Toast.LENGTH_SHORT).show();
//                }
//                return true;
//            } else if (idControlClickeado==R.id.opc_05000000_eliminar) {
//                if (procesarTareos("ELIMINAR")){
//                    listarRegistros();
//                    Funciones.notificar(this,"Tareos eliminados correctamente.");
//                    //Toast.makeText(this,"Tareos eliminados correctamente.",Toast.LENGTH_SHORT).show();
//                }
//                return true;
//            } else if (idControlClickeado==R.id.opc_05000000_reporte) {
//                abrirActividadReportes();
//                return true;
//            }
//        }catch(Exception ex){
//             Funciones.mostrarError(this,ex);
//            return false;
//        }
//        return true;
//    }
//
//    private void abrirActividadReportes() {
//        Intent nuevaActividad = new Intent(this, cls_05020000_Reportes.class);
//        nuevaActividad.putExtra("ConfiguracionLocal",objConfLocal);
//        startActivity(nuevaActividad);
//    }
//
//    private boolean transferirTareos(){
//        try{
//            List<String> pSqlite = new ArrayList<String>();
//            String pSql = "";
//            ResultSet rS;
//            for(String id: al_RegistrosSeleccionados){
//                Tareo aux = new Tareo(id,objSqlite,objConfLocal);
//                if (aux.getIdEstado().equals("PE")){
//                    if (objSql.existeId("trx_Tareos",objConfLocal.get("ID_EMPRESA"), id)){
//                        String nuevoId = objSql.obtenerNuevoId("trx_Tareos",objConfLocal.get("ID_EMPRESA"), objConfLocal.get("ID_DISPOSITIVO")  );
//                        objSqlite.ActualizarId("trx_Tareos",objConfLocal.get("ID_EMPRESA"), id,nuevoId); //PROBAR
//                        id=nuevoId;
//                        objSqlite.ActualizarCorrelativos(objConfLocal,"trx_Tareos",id); //PROBAR
//                    }
//                    pSql = obtenerParametrosDeExecPara(id,"TRANSFERIR trx_Tareos");
//                    rS = objSql.doItBaby(objSqlite.obtQuery("TRANSFERIR trx_Tareos") + pSql, null);
//                    rS.next();
//                    if(rS.getString("Resultado").equals("1")){
//                        if(transferirTareosDetalle(id)){
//                            pSqlite.clear();
//                            pSqlite.add(rS.getString("Detalle"));
//                            pSqlite.add(objConfLocal.get("ID_USUARIO_ACTUAL"));
//                            pSqlite.add(objConfLocal.get("ID_EMPRESA"));
//                            pSqlite.add(id);
//                            objSqlite.doItBaby(objSqlite.obtQuery("MARCAR TAREO COMO TRANSFERIDO") , pSqlite, "WRITE","");
//                            //return true;
//                        }
//                        else{
//                            List<String> p = new ArrayList<>();
//                            p.add(id);
//                            objSql.doItBaby(objSqlite.obtQuery("ROLLBACK trx_Tareos"), p); //PROBAR
//                            return false;
//                        }
////                    if (!id.equals(rS.getString("IdTrx")) && rS.getString("IdTrx").length()>0){
////                        objSqlite.ActualizarId("trx_Tareos",objConfLocal.get("ID_EMPRESA"), id,rS.getString("IdTrx")); //PROBAR
////                    }
//
//                    }else{
//                        //Toast.makeText(this,rS.getString("Detalle"),Toast.LENGTH_SHORT).show();
//                        objConfLocal.set("MENSAJE",rS.getString("Detalle"));
//                        return false;
//                    }
//                }else {
//                    Funciones.notificar(this,"El tareo ["+id+"] no se puede transferir porque ya fue transferido anteriormente.");
//                }
//            }
//            objConfLocal=objSqlite.ActualizarDataPendiente(objConfLocal,true);
//            Funciones.mostrarEstatusGeneral(this.getBaseContext(),
//                    objConfLocal,
//                    c005_txv_PushTituloVentana,
//                    c005_txv_PushRed,
//                    c005_txv_NombreApp,
//                    c005_txv_PushVersionApp,
//                    c005_txv_PushVersionDataBase,
//                    c005_txv_PushIdentificador);
//            return true;
//        }catch (Exception ex){
//            Funciones.mostrarError(this,ex);
//         return false;
//        }
//    }
//
//    private boolean procesarTareos(String proceso) {
//        try{
//            List<String> pSqlite = new ArrayList<String>();
//            String pSql = "";
//            ResultSet rS;
//            Cursor c;
//            switch (proceso){
//                case "TRANSFERIR":
//                    for(String id: al_RegistrosSeleccionados){
//                        pSql = obtenerParametrosDeExecPara(id,"TRANSFERIR trx_Tareos");
//                        rS = objSql.doItBaby(objSqlite.obtQuery("TRANSFERIR trx_Tareos") + pSql, null);
//                        rS.next();
//                        if(rS.getString("Resultado").equals("1")){
//                            if (!id.equals(rS.getString("IdTrx")) && rS.getString("IdTrx").length()>0){
//                                objSqlite.ActualizarId("trx_Tareos",objConfLocal.get("ID_EMPRESA"), id,rS.getString("IdTrx")); //PROBAR
//                            }
//                            if(transferirTareosDetalle(id)){
//                                pSqlite.clear();
//                                pSqlite.add(rS.getString("Detalle"));
//                                pSqlite.add(objConfLocal.get("ID_USUARIO_ACTUAL"));
//                                pSqlite.add(objConfLocal.get("ID_EMPRESA"));
//                                pSqlite.add(id);
//                                objSqlite.doItBaby(objSqlite.obtQuery("MARCAR TAREO COMO TRANSFERIDO") , pSqlite, "WRITE","");
//                                //return true;
//                            }
//                            else{
//                                List<String> p = new ArrayList<>();
//                                p.add(id);
//                                objSql.doItBaby(objSqlite.obtQuery("ROLLBACK trx_Tareos"), p); //PROBAR
//                                return false;
//                            }
//                            objSqlite.ActualizarCorrelativos(objConfLocal,"trx_Tareos",id); //PROBAR
//                        }else{
//                            //Toast.makeText(this,rS.getString("Detalle"),Toast.LENGTH_SHORT).show();
//                            objConfLocal.set("MENSAJE",rS.getString("Detalle"));
//                            return false;
//                        }
//                    }
//                    objSqlite.ActualizarDataPendiente(objConfLocal);
//                    Funciones.mostrarEstatusGeneral(this.getBaseContext(),
//                            objConfLocal,
//                            c005_txv_PushTituloVentana,
//                            c005_txv_PushRed,
//                            c005_txv_NombreApp,
//                            c005_txv_PushVersionApp,
//                            c005_txv_PushVersionDataBase,
//                            c005_txv_PushIdentificador
//                    );
//                    return true;
//                case "EXTORNAR":
//                    Toast.makeText(this,"exgetMessage()",Toast.LENGTH_SHORT).show();
//                    return true;
//                case "ELIMINAR":
//                    for(String id: al_RegistrosSeleccionados){
//                        Tareo aux = new Tareo(id,objSqlite,objConfLocal);
//                        if (aux.getIdEstado().equals("PE")){
//                            pSqlite.clear();
//                            pSqlite.add(objConfLocal.get("ID_EMPRESA"));
//                            pSqlite.add(id);
//                            c = objSqlite.doItBaby(objSqlite.obtQuery("ELIMINAR trx_Tareos_Detalle PENDIENTES X ID"),pSqlite,"WRITE");
//                            c = objSqlite.doItBaby(objSqlite.obtQuery("ELIMINAR trx_Tareos PENDIENTES X ID"),pSqlite,"WRITE");
//                        }else {
//                            Funciones.notificar(this,"El tareo ["+id+"] se encuentra transferido, imposible eliminar.");
//                            return false;
//                        }
//                    }
//                    objSqlite.ActualizarDataPendiente(objConfLocal);
//                    Funciones.mostrarEstatusGeneral(this.getBaseContext(),
//                            objConfLocal,
//                            c005_txv_PushTituloVentana,
//                            c005_txv_PushRed,
//                            c005_txv_NombreApp,
//                            c005_txv_PushVersionApp,
//                            c005_txv_PushVersionDataBase,
//                            c005_txv_PushIdentificador
//                    );
//                    return true;
//                default: return false;
//            }
//        }catch(Exception ex){
////             Funciones.mostrarError(this,ex);
//            return false;
//        }
//    }
//
//    private boolean transferirTareosDetalle(String id) {
//        try{
//            List<String> pSqlite = new ArrayList<String>();
//            String pSql = "";
//            ResultSet rS;
//            pSqlite.add(objConfLocal.get("ID_EMPRESA"));
//            pSqlite.add(id);
//            Cursor c = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER trx_Tareos_Detalle XA TRANSFERIR"),pSqlite,"READ");
//            c.moveToFirst();
//            for(int i = 0; i<c.getCount(); i++){
//                pSql = concatenarParametros(c,i);
//                rS = objSql.doItBaby(objSqlite.obtQuery("TRANSFERIR trx_Tareos_Detalle") + pSql,null);
//                rS.next();
//                if(!rS.getString("Resultado").equals("1")){
//                    objConfLocal.set("MENSAJE",rS.getString("Detalle"));
//                    return false;
//                    }
//                }
//            return true;
//        }catch(Exception ex){
//             Funciones.mostrarError(this,ex);
//            return false;
//        }
//    }
//
//    private String concatenarParametros(Cursor c, int index) {
//        c.moveToPosition(index);
//        String r=" '";
//        for(int i=0; i<c.getColumnCount(); i++){
//            r = r + c.getString(i) + "|";
//        }
//        r = r + "'";
//        return r;
//    }
//
//    private String obtenerParametrosDeExecPara(String id, String proceso) {
//        try{
//            String r=" '";
//            Cursor c;
//            List<String> p = new ArrayList<String>();
//            p.add(objConfLocal.get("ID_EMPRESA"));
//            p.add(id);
//            switch (proceso){
//                case "TRANSFERIR trx_Tareos":
//                    c = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER trx_Tareos XA TRANSFERIR"),p, "READ");
//                    c.moveToFirst();
//                    for(int i=0; i<c.getColumnCount(); i++){
//                       r = r + c.getString(i) + "|";
//                    }
//                    //r = r + "'";
//                    r = r + objConfLocal.get("MAC") + "|" + objConfLocal.get("IMEI");
//                    r = r + "'";
//                    return r;
//                case "EXTORNAR":
//
//                    return r;
//                case "ELIMINAR":
//
//                    return r;
//                default: return null;
//            }
//        }catch(Exception ex){
//             Funciones.mostrarError(this,ex);
//            return null;
//        }
//    }
//
////    private void setearSelectorFechaDesde() {
////        c005_txv_Desde.setOnClickListener(new View.OnClickListener() {
////                                         @Override
////                                         public void onClick(View view) {
////                                             Mes = Mes - 1;
////                                             DatePickerDialog datePickerDialog = new DatePickerDialog(cls_05000000_Tareos.this, android.R.style.Theme_Holo , CalendarioListenerDesde, Anio, Mes, Dia);
////                                             datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
////                                             datePickerDialog.show();
////                                         }
////                                     }
////        );
////        CalendarioListenerDesde = new DatePickerDialog.OnDateSetListener() {
////            @Override
////            public void onDateSet(DatePicker datePicker, int anio, int mes, int dia) {
////                mes = mes + 1;
////                Dia =dia;
////                Mes =mes;
////                Anio =anio;
////                String fec =  (dia  < 10 ? "0" + dia : dia) + "/" + (mes < 10 ? "0" + mes : mes) + "/" + anio;
////                c005_txv_Desde.setText("Desde: " + fec);
////                desde = Anio + "-" + (mes < 10 ? "0" + mes : mes) + "-" + (dia  < 10 ? "0" + dia : dia);
////                //hasta = Anio + "-" + (mes < 10 ? "0" + mes : mes) + "-" + (dia  < 10 ? "0" + dia : dia);
////                //Toast.makeText(cls_05000000_Tareos.super.getBaseContext(), desde + hasta, Toast.LENGTH_SHORT).show();
////                try {
////                    listarTareos(s_ListarIdEstado,desde,hasta);
////                } catch (Exception ex) {
////                    ex.printStackTrace();
////                }
////            }
////        };
////    }
////
////    private void setearSelectorFechaHasta() {
////        c005_txv_Hasta.setOnClickListener(new View.OnClickListener() {
////                                              @Override
////                                              public void onClick(View view) {
////                                                  Mes = Mes - 1;
////                                                  DatePickerDialog datePickerDialog = new DatePickerDialog(cls_05000000_Tareos.this, android.R.style.Theme_Holo, CalendarioListenerHasta, Anio, Mes, Dia);
////                                                  datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
////                                                  datePickerDialog.show();
////                                              }
////                                          }
////        );
////        CalendarioListenerHasta = new DatePickerDialog.OnDateSetListener() {
////            @Override
////            public void onDateSet(DatePicker datePicker, int anio, int mes, int dia) {
////                mes = mes + 1;
////                Dia =dia;
////                Mes =mes;
////                Anio =anio;
////                String fec =  (dia  < 10 ? "0" + dia : dia) + "/" + (mes < 10 ? "0" + mes : mes) + "/" + anio;
////                c005_txv_Hasta.setText("Hasta: " + fec);
////                //desde = Anio + "-" + (mes < 10 ? "0" + mes : mes) + "-" + (dia  < 10 ? "0" + dia : dia);
////                hasta = Anio + "-" + (mes < 10 ? "0" + mes : mes) + "-" + (dia  < 10 ? "0" + dia : dia);
////                //Toast.makeText(cls_05000000_Tareos.super.getBaseContext(), desde + hasta, Toast.LENGTH_SHORT).show();
////                try {
////                    listarTareos(s_ListarIdEstado,desde,hasta);
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////            }
////        };
////    }
//
//    public void listarRegistros() throws Exception {
////        try{
////            List<String> p = new ArrayList<String>();
////            p.add(objConfLocal.get("ID_EMPRESA"));
////            p.add(idEstado);
////            p.add(d);
////            p.add(h);
////            c_Registros = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER trx_Tareos X ESTADO Y RANGO FECHA"),p,"READ");
////            cls_05000100_Item_RecyclerView miAdaptador = new cls_05000100_Item_RecyclerView(this, c_Registros,objConfLocal,tareosSeleccionados);
////            c005_rcv_Reciclador.setAdapter(miAdaptador);
////            c005_rcv_Reciclador.setLayoutManager(new LinearLayoutManager(this));
////            //reciclador.setAdapter(miAdaptador);
////        }catch (Exception ex){
////             Funciones.mostrarError(this,ex);
////        }
//        /////////////////////////////////////////////////////////
//        try {
//            //BINGO! METODO PARA LISTAR EN RECYCLERVIEW DESDE CURSOR
//            List<String> p = new ArrayList<String>();
//            p.add(objConfLocal.get("ID_EMPRESA"));
//            p.add(s_ListarIdEstado);
//            p.add(s_ListarDesde);
//            p.add(s_ListarHasta);
//            c_Registros = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER trx_Tareos X ESTADO Y RANGO FECHA"), p, "READ");
//            if (c_Registros.moveToFirst()){
//                cls_05000100_Item_RecyclerView miAdaptador = new cls_05000100_Item_RecyclerView(this, c_Registros, objConfLocal, al_RegistrosSeleccionados);
//                c005_rcv_Reciclador.setAdapter(miAdaptador);
//                c005_rcv_Reciclador.setLayoutManager(new LinearLayoutManager(this));
//            }else{
//                c005_rcv_Reciclador.setAdapter(null);
//                c005_rcv_Reciclador.setLayoutManager(new LinearLayoutManager(this));
//            }
//            //reciclador.setAdapter(miAdaptador);
//        } catch (Exception ex) {
//            Funciones.mostrarError(this, ex);
//        }
//    }
//
//    private void abrirDocumento(String id) {
//        Intent nuevaActividad = new Intent(this, cls_05010000_Edicion.class);
//        nuevaActividad.putExtra("ConfiguracionLocal",objConfLocal);
//        nuevaActividad.putExtra("IdDocumentoActual",id);
//        startActivity(nuevaActividad);
//    }
//}