package com.example.datagreenmovil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.Tareo;
import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.Logica.Swal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datagreenmovil.databinding.ActivityTareosBinding;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TareosActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityTareosBinding binding;
    DrawerLayout drawer;
    ArrayList<String> al_RegistrosSeleccionados=new ArrayList<>();
    ConexionSqlite objSqlite;
    ConexionBD objSql;
    ConfiguracionLocal objConfLocal;
    Context ctx;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String s_DesdeFecha = LocalDate.now().plusDays(-7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    String s_HastaFecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //--desde, hasta, estado;
    String s_ListarIdEstado ="PE";
    String s_ListarDesde = LocalDate.now().plusDays(-7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    String s_ListarHasta = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //--desde, hasta, estado;
    Cursor c_Registros;
    RecyclerView c005_rcv_Reciclador;
    cls_05000100_Item_RecyclerView miAdaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = this.getSharedPreferences("objConfLocal",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        ctx = this;
        binding = ActivityTareosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        drawer = binding.drawerLayoutTareos;

        NavigationView navigationView = binding.navViewTareos;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_tareos_main)
                .setOpenableLayout(drawer)
                .build();

        FloatingActionButton nuevoTareo = (FloatingActionButton) binding.getRoot().findViewById(R.id.c005_fab_MainTareos_NuevoTareo_v);
        nuevoTareo.setOnClickListener(view -> {
            abrirDocumento(null);
        });

        FloatingActionButton opciones = (FloatingActionButton) binding.getRoot().findViewById(R.id.c005_fab_MainTareos_Opciones_v);
        opciones.setOnClickListener(view -> {
            drawer.openDrawer(GravityCompat.START);
        });

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_tareos);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_item_sync) {
                if (transferirTareos()){
                    al_RegistrosSeleccionados.clear();
                    try {
                        listarRegistros();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    Funciones.notificar(ctx,"Proceso finalizado correctamente.");
                    //Toast.makeText(this,"Tareos transferidos correctamente.",Toast.LENGTH_SHORT).show();
                    return true;
                }
                Funciones.notificar(ctx,sharedPreferences.getString("MENSAJE","!MENSAJE"));
                    return false;
                }

                // Cierra el cajón de navegación después de realizar la acción.
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_tareos);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void abrirDocumento(String id) {
        ConfiguracionLocal objConfLocal = null;
        Intent nuevaActividad = new Intent(this, cls_05010000_Edicion.class);
        nuevaActividad.putExtra("ConfiguracionLocal",objConfLocal);
        nuevaActividad.putExtra("IdDocumentoActual",id);
        startActivity(nuevaActividad);
    }

    private boolean transferirTareos(){
        try{
            List<String> tareos = new ArrayList<String>();
            tareos = miAdaptador.retornarTareos();
            List<String> pSqlite = new ArrayList<String>();
            String pSql = "";
            ResultSet rS;

            for(String id: al_RegistrosSeleccionados){
                Tareo aux = new Tareo(id,objSqlite,objConfLocal, ctx);
                if (aux.getIdEstado().equals("PE")){
                    if (objSql.existeId("trx_Tareos",sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"), id)){
                        String nuevoId = objSql.obtenerNuevoId("trx_Tareos",sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"), sharedPreferences.getString("ID_DISPOSITIVO","!ID_DISPOSITIVO")  );
                        objSqlite.ActualizarId("trx_Tareos",sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"), id,nuevoId); //PROBAR
                        id=nuevoId;
                        objSqlite.ActualizarCorrelativos(objConfLocal,"trx_Tareos",id); //PROBAR
                    }
                    pSql = obtenerParametrosDeExecPara(id,"TRANSFERIR trx_Tareos");
                    rS = objSql.doItBaby(objSqlite.obtQuery("TRANSFERIR trx_Tareos") + pSql, null);
                    rS.next();
                    if(rS.getString("Resultado").equals("1")){
                        if(transferirTareosDetalle(id)){
                            pSqlite.clear();
                            pSqlite.add(rS.getString("Detalle"));
                            pSqlite.add(sharedPreferences.getString("ID_USUARIO_ACTUAL","!ID_USUARIO_ACTUAL"));
                            pSqlite.add(sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"));
                            pSqlite.add(id);
                            objSqlite.doItBaby(objSqlite.obtQuery("MARCAR TAREO COMO TRANSFERIDO") , pSqlite, "WRITE","");
                            //return true;
                        }
                        else{
                            List<String> p = new ArrayList<>();
                            p.add(id);
                            objSql.doItBaby(objSqlite.obtQuery("ROLLBACK trx_Tareos"), p); //PROBAR
                            return false;
                        }
//                    if (!id.equals(rS.getString("IdTrx")) && rS.getString("IdTrx").length()>0){
//                        objSqlite.ActualizarId("trx_Tareos",objConfLocal.get("ID_EMPRESA"), id,rS.getString("IdTrx")); //PROBAR
//                    }

                    }else{
                        //Toast.makeText(this,rS.getString("Detalle"),Toast.LENGTH_SHORT).show();
                        editor.putString("MENSAJE",rS.getString("Detalle")).apply();
                        return false;
                    }
                }else {
                    Funciones.notificar(this,"El tareo ["+id+"] no se puede transferir porque ya fue transferido anteriormente.");
                }
            }
            objConfLocal=objSqlite.ActualizarDataPendiente(objConfLocal,true);
//            Funciones.mostrarEstatusGeneral(ctx,
//                    objConfLocal,
//                    c005_txv_PushTituloVentana,
//                    c005_txv_PushRed,
//                    c005_txv_NombreApp,
//                    c005_txv_PushVersionApp,
//                    c005_txv_PushVersionDataBase,
//                    c005_txv_PushIdentificador);
            return true;
        }catch (Exception ex){
            Funciones.mostrarError(this,ex);
            return false;
        }
    }
    public void listarRegistros() throws Exception {
//        try{
//            List<String> p = new ArrayList<String>();
//            p.add(objConfLocal.get("ID_EMPRESA"));
//            p.add(idEstado);
//            p.add(d);
//            p.add(h);
//            c_Registros = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER trx_Tareos X ESTADO Y RANGO FECHA"),p,"READ");
//            cls_05000100_Item_RecyclerView miAdaptador = new cls_05000100_Item_RecyclerView(this, c_Registros,objConfLocal,tareosSeleccionados);
//            c005_rcv_Reciclador.setAdapter(miAdaptador);
//            c005_rcv_Reciclador.setLayoutManager(new LinearLayoutManager(this));
//            //reciclador.setAdapter(miAdaptador);
//        }catch (Exception ex){
//             Funciones.mostrarError(this,ex);
//        }
        /////////////////////////////////////////////////////////
        try {
            //BINGO! METODO PARA LISTAR EN RECYCLERVIEW DESDE CURSOR
            List<String> p = new ArrayList<String>();
            p.add(sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"));
            p.add(s_ListarIdEstado);
            p.add(s_ListarDesde);
            p.add(s_ListarHasta);
            c_Registros = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER trx_Tareos X ESTADO Y RANGO FECHA"), p, "READ");
            if (c_Registros.moveToFirst()){
                miAdaptador = new cls_05000100_Item_RecyclerView(this, c_Registros, objConfLocal, al_RegistrosSeleccionados);
//                miAdaptador.tareosSeleccionados
                c005_rcv_Reciclador.setAdapter(miAdaptador);
                c005_rcv_Reciclador.setLayoutManager(new LinearLayoutManager(this));
            }else{
                c005_rcv_Reciclador.setAdapter(null);
                c005_rcv_Reciclador.setLayoutManager(new LinearLayoutManager(this));
            }
//            reciclador.setAdapter(miAdaptador);
        } catch (Exception ex)
        {
            Funciones.mostrarError(this, ex);
        }
    }

    private String obtenerParametrosDeExecPara(String id, String proceso) {
        try{
            String r=" '";
            Cursor c;
            List<String> p = new ArrayList<String>();
            p.add(sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"));
            p.add(id);
            switch (proceso){
                case "TRANSFERIR trx_Tareos":
                    c = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER trx_Tareos XA TRANSFERIR"),p, "READ");
                    c.moveToFirst();
                    for(int i=0; i<c.getColumnCount(); i++){
                        r = r + c.getString(i) + "|";
                    }
                    //r = r + "'";
                    r = r + sharedPreferences.getString("MAC","!MAC") + "|" + sharedPreferences.getString("IMEI","!IMEI");
                    r = r + "'";
                    return r;
                case "EXTORNAR":

                    return r;
                case "ELIMINAR":

                    return r;
                default: return null;
            }
        }catch(Exception ex){
            Funciones.mostrarError(this,ex);
            return null;
        }
    }

    private boolean transferirTareosDetalle(String id) {
        try{
            List<String> pSqlite = new ArrayList<String>();
            String pSql = "";
            ResultSet rS;
            pSqlite.add(sharedPreferences.getString("ID_EMPRESA","!ID_EMPRESA"));
            pSqlite.add(id);
            Cursor c = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER trx_Tareos_Detalle XA TRANSFERIR"),pSqlite,"READ");
            c.moveToFirst();
            for(int i = 0; i<c.getCount(); i++){
                pSql = concatenarParametros(c,i);
                rS = objSql.doItBaby(objSqlite.obtQuery("TRANSFERIR trx_Tareos_Detalle") + pSql,null);
                rS.next();
                if(!rS.getString("Resultado").equals("1")){
                    editor.putString("MENSAJE",rS.getString("Detalle"));
                    return false;
                }
            }
            return true;
        }catch(Exception ex){
            Funciones.mostrarError(this,ex);
            return false;
        }
    }

    private String concatenarParametros(Cursor c, int index) {
        c.moveToPosition(index);
        String r=" '";
        for(int i=0; i<c.getColumnCount(); i++){
            r = r + c.getString(i) + "|";
        }
        r = r + "'";
        return r;
    }
}