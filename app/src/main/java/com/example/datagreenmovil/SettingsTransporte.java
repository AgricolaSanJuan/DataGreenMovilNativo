package com.example.datagreenmovil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.Logica.Swal;
import java.util.HashMap;

import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;

import com.google.android.material.textfield.TextInputLayout;

public class SettingsTransporte extends AppCompatActivity {
    //@Jota:2023-05-27 -> INICIO DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    ConexionSqlite objSqlite;
    ConexionBD objSql;
    ConfiguracionLocal objConfLocal;
    TextView txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador;
    //    AlertDialog.Builder builderDialogoCerrarSesion;
    Dialog dlg_PopUp;
    //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
    //...

    EditText txtPlaca, txtRucProveedor, txtMarca, txtModelo, txtColor, txtCapacidad;

  HashMap<String, String> paramsBus = new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_settings_transporte);

      //@Jota:2023-05-27 -> INICIO DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
      try{
        if(getIntent().getExtras()!=null){
          objConfLocal=(ConfiguracionLocal) getIntent().getSerializableExtra("ConfiguracionLocal");
        }
        objSql = new ConexionBD(objConfLocal);
        objSqlite = new ConexionSqlite(this,objConfLocal);
        objConfLocal.set("ULTIMA_ACTIVIDAD","PlantillaBase");

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
        //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
        //...

      }catch (Exception ex){
        Funciones.mostrarError(this,ex);
      }
    }

    //@Jota:2023-05-27 -> LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    private void setearControles() {
//        builderDialogoCerrarSesion= Funciones.setearAlertDialogParaCerrarSesion_(objConfLocal,objSqlite,this);

      //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
      //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
      //...
    }

    private void referenciarControles() {
      txv_PushTituloVentana = findViewById(R.id.c000_txv_PushTituloVentana_v);
      txv_PushRed = findViewById(R.id.c000_txv_PushRed_v);
      txv_NombreApp = findViewById(R.id.c000_txv_NombreApp_v);
      txv_PushVersionApp = findViewById(R.id.c000_txv_PushVersionApp_v);
      txv_PushVersionDataBase = findViewById(R.id.c000_txv_PushVersionDataBase_v);
      txv_PushIdentificador = findViewById(R.id.c000_txv_PushIdentificador_v);
      txtPlaca = findViewById(R.id.txtPlaca);
      txtRucProveedor = findViewById(R.id.txtRucProveedor);
      txtMarca = findViewById(R.id.txtMarca);
      txtModelo = findViewById(R.id.txtModelo);
      txtColor = findViewById(R.id.txtColor);
      txtCapacidad = findViewById(R.id.txtCapacidad);

      //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
      //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
      //...
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
        if (idControlClickeado == R.id.c000_txv_PushTituloVentana_v){
          Funciones.popUpTablasPendientesDeEnviar(this);
        } else if (idControlClickeado == R.id.c000_txv_PushRed_v) {
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
        } else if (idControlClickeado == R.id.c000_txv_PushVersionApp_v || idControlClickeado == R.id.c000_txv_PushVersionDataBase_v) {
          Funciones.popUpStatusVersiones(this);
        } else if (idControlClickeado == R.id.c000_txv_PushIdentificador_v) {
          mostrarMenuUsuario(this.txv_PushIdentificador);
        } else if(idControlClickeado == R.id.btnSave) {
          registrarNuevobus();
          Swal.success(this,"Guardado!","Los datos del bus se han ingresado correctamente!",1500);
        }
        //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
        //...

        else throw new IllegalStateException("Click sin programacion: " + view.getId());
      } catch (Exception ex) {
        Funciones.mostrarError(this,ex);
      }
    }

    //@Jota:
    //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
    //...

  public void registrarNuevobus(){
      ConexionSqlite sqlite = new ConexionSqlite(this, objConfLocal);


      referenciarControles();
    paramsBus.put("Placa",txtPlaca.getText().toString());
    paramsBus.put("IdProveedor",txtRucProveedor.getText().toString());
    paramsBus.put("Marca",txtMarca.getText().toString());
    paramsBus.put("Modelo",txtModelo.getText().toString());
    paramsBus.put("Color",txtColor.getText().toString());
    paramsBus.put("Capacidad",txtCapacidad.getText().toString());

    sqlite.insertInto(this, paramsBus, "mst_Vehiculos");

//    for (String elemento:paramsBus) {
//      Toast.makeText(this, elemento, Toast.LENGTH_SHORT).show();
//    }
  }

  }