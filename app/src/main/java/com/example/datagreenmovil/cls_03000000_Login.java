package com.example.datagreenmovil;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Entidades.ClaveValor;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.Querys;
import com.example.datagreenmovil.Entidades.Tabla;
import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.Logica.Swal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class cls_03000000_Login extends AppCompatActivity {
    //@Jota:2023-05-27 -> INICIO DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    ConexionSqlite objSqlite;
    ConexionBD objSql;
    ConfiguracionLocal objConfLocal;
    TextView txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador;
    Dialog dlg_PopUp;
    //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
    //...

    Querys objQuerys;
    EditText etx_Usuario;// = (EditText) findViewById(R.id.etx_Usuario_v);
    EditText etx_Password;// = (EditText) findViewById(R.id.etx_Password_v);
    Spinner c003_spi_Empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_03000000_login_003);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //@Jota:2023-05-27 -> INICIO DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        if(getIntent().getExtras()!=null){
            objConfLocal=(ConfiguracionLocal) getIntent().getSerializableExtra("ConfiguracionLocal");
        }
        objSql = new ConexionBD(objConfLocal);
        objSqlite = new ConexionSqlite(this,objConfLocal);
//        objConfLocal=new ConfiguracionLocal(objSqlite.obtenerConfiguracionLocal(objConfLocal));
        objConfLocal.set("ULTIMA_ACTIVIDAD","Login");

        referenciarControles();
        setearControles();
        Funciones.mostrarEstatusGeneral(this.getBaseContext(),
                objConfLocal,
                txv_PushTituloVentana,
                txv_PushRed,
                txv_NombreApp,
                txv_PushVersionApp,
                txv_PushVersionDataBase,
                txv_PushIdentificador
        );
        cargarControles();
        //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
        //...

    }

    //@Jota:2023-05-27 -> LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    private void setearControles() {
        //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
        //...
        setearSpinnerEmpresas();
    }

    private void referenciarControles() {
        txv_PushTituloVentana = findViewById(R.id.c003_txv_PushTituloVentana_v);
        txv_PushRed = findViewById(R.id.c003_txv_PushRed_v);
        txv_NombreApp = findViewById(R.id.c003_txv_NombreApp_v);
        txv_PushVersionApp = findViewById(R.id.c003_txv_PushVersionApp_v);
        txv_PushVersionDataBase = findViewById(R.id.c003_txv_PushVersionDataBase_v);
        txv_PushIdentificador = findViewById(R.id.c003_txv_PushIdentificador_v);

        //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
        //...
        etx_Usuario = (EditText) findViewById(R.id.c003_etx_Usuario_v);
        etx_Password = (EditText) findViewById(R.id.c003_etx_Password_v);
        c003_spi_Empresa = (Spinner) findViewById(R.id.c003_spi_Empresa_v);
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

    public void cambiarContrasenia(View view){
        dlg_PopUp = Funciones.obtenerDialogParaCambiarClave(this,objConfLocal,objSqlite,this,true);
        dlg_PopUp.show();
    }
    public void onClick(View view) {
        try {
            int idControlClickeado = view.getId();
            if (idControlClickeado == R.id.c003_txv_PushTituloVentana_v){
                Funciones.popUpTablasPendientesDeEnviar(this);
            } else if (idControlClickeado == R.id.c003_txv_PushRed_v) {
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
            } else if (idControlClickeado == R.id.c003_txv_PushVersionApp_v || idControlClickeado == R.id.c003_txv_PushVersionDataBase_v) {
                Funciones.popUpStatusVersiones(this);
            } else if (idControlClickeado == R.id.c003_txv_PushIdentificador_v) {
                mostrarMenuUsuario(this.txv_PushIdentificador);
            }
            //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
            //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
            //...
            else if (idControlClickeado == R.id.c003_btn_Login_v) {
                intentaLogin();
            } else throw new IllegalStateException("Click sin programacion: " + view.getId());
        } catch (Exception ex) {
            Funciones.mostrarError(this,ex);
        }
    }
    //@Jota:
    //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
    //...

    private void cargarControles() {
        try{
            Tabla t = new Tabla(objSqlite.doItBaby(objSqlite.obtQuery("CLAVE VALOR mst_Empresas"),null,"READ"));
            Funciones.cargarSpinner(cls_03000000_Login.this,c003_spi_Empresa,t,0,1);
        }catch (Exception ex){
            Funciones.mostrarError(this,ex);
        }
    }

    public void intentaLogin(){
        try{
            String usuario = etx_Usuario.getText().toString(), password = Funciones.generarMD5(etx_Usuario.getText().toString() + etx_Password.getText().toString());
            List<String> p = new ArrayList<String>();
            p.add(usuario);
            p.add(usuario);

            p.add(usuario);
            p.add(password);
            p.add(usuario);
            p.add(password);

            p.add(usuario);
            p.add(password);
            p.add(usuario);
            p.add(password);

            p.add(usuario);
            p.add(password);
            p.add(usuario);
            p.add(password);

            p.add(usuario);
            p.add(usuario);
            Cursor validacion = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER DATOS LOGIN"),p,"READ");
            if (!validacion.moveToFirst()){
                Funciones.notificar(this, "Error de inicio de sesion.");
            }else{
                if(/*validacion.moveToFirst() &&*/ validacion.getString(0).equals("0")){
                    objConfLocal.set("ID_USUARIO_ACTUAL",usuario);
                    dlg_PopUp = Funciones.obtenerDialogParaCambiarClave(this,objConfLocal,objSqlite,this);
                    dlg_PopUp.show();
                }else if(/*validacion.moveToFirst() && */validacion.getString(1).equals("1")){
                    iniciarSesion(validacion.getString(2),validacion.getString(3));
                    abrirMenuModulos();
                }
                Funciones.notificar(this, "Error de inicio de sesion.");
            }
        }catch (Exception ex){
            Funciones.mostrarError(this,ex);
        }
    }

    private void iniciarSesion(String columna1, String columna2) throws Exception {
        objConfLocal.set("ID_USUARIO_ACTUAL",etx_Usuario.getText().toString());
        objConfLocal.set("NOMBRE_USUARIO_ACTUAL",columna2);
        int horasExpiracion = Integer.parseInt(objConfLocal.get("DURACION_TOKEN_HORAS"));
        LocalDateTime ldt_TokenExpira = LocalDateTime.now().plusHours(horasExpiracion);
        String str_tokenExpira = ldt_TokenExpira.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        objConfLocal.set("TOKEN_EXPIRA",str_tokenExpira);
        objConfLocal.set("MODULOS_PERMITIDOS",columna1);
        objSqlite.guardarConfiguracionLocal(objConfLocal);
    }

    public void abrirMenuModulos(){
        try{
//            objSqlite.guardarConfiguracionLocal(objConfLocal);
            Intent intent = new Intent(this, cls_04000000_Modulos.class);
            intent.putExtra("ConfiguracionLocal",objConfLocal);
            startActivity(intent);
            finish();
        }catch (Exception ex){
            Funciones.mostrarError(this,ex);
        }
    }
    public void abrirMenuConfiguracion(View vista){
        try{
//            objSqlite.guardarConfiguracionLocal(objConfLocal);
            Intent intent = new Intent(this, cls_02000000_Configuracion.class );
            intent.putExtra("ConfiguracionLocal",objConfLocal);
            startActivity(intent);
            //finish();
        }catch (Exception ex){
            Funciones.mostrarError(this,ex);
        }
    }


    private void setearSpinnerEmpresas() {
        try{
            c003_spi_Empresa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
                    try{
                        ClaveValor obj = (ClaveValor)(parent.getItemAtPosition(position));
                        objConfLocal.set("ID_EMPRESA",obj.getClave());
                    }catch (Exception ex){
                        Funciones.mostrarError(cls_03000000_Login.this,ex);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }catch (Exception ex){
            Funciones.mostrarError(this,ex);
        }
    }
}