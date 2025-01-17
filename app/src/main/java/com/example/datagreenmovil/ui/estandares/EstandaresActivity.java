package com.example.datagreenmovil.ui.estandares;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.example.datagreenmovil.Conexiones.AppDatabase;
import com.example.datagreenmovil.DAO.Estandares.MstMedidasEstandares.MstMedidasEstandares;
import com.example.datagreenmovil.DAO.Estandares.MstMedidasEstandares.MstMedidasEstandaresDAO;
import com.example.datagreenmovil.DAO.Estandares.MstTiposBonoEstandar.MstTiposBonoEstandarDAO;
import com.example.datagreenmovil.DAO.Estandares.MstTiposCostoEstandar.MstTiposCostoEstandar;
import com.example.datagreenmovil.DAO.Estandares.MstTiposCostoEstandar.MstTiposCostoEstandarDAO;
import com.example.datagreenmovil.DAO.Estandares.MstTiposEstandar.MstTiposEstandar;
import com.example.datagreenmovil.DAO.Estandares.MstTiposEstandar.MstTiposEstandarDAO;
import com.example.datagreenmovil.DAO.Estandares.TrxEstandares.TrxEstandaresNewDAO;
import com.example.datagreenmovil.DataGreenApp;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.Utilidades.Filtros;
import com.example.datagreenmovil.databinding.ActivityEstandaresBinding;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.List;
import java.util.Objects;

public class EstandaresActivity extends AppCompatActivity {
//public class EstandaresActivity extends AppCompatActivity implements Filtros.GetFilterData {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityEstandaresBinding binding;

    private TrxEstandaresNewDAO trxEstandaresNewDAO;
    private MstTiposEstandarDAO mstTiposEstandarDAO;
    private MstTiposCostoEstandarDAO mstTiposCostoEstandarDAO;
    private MstTiposBonoEstandarDAO mstTiposBonoEstandarDAO;
    private MstMedidasEstandaresDAO mstMedidasEstandaresDAO;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEstandaresBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_estandares_main, R.id.nav_estandares_registro, R.id.nav_estandares_settings)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_estandares);
        NavigationUI.setupWithNavController(navigationView, navController);

        inicializarBase();

        mstTiposEstandarDAO.obtenerTiposEstandar();

        List<MstTiposEstandar> mstTiposEstandarList = mstTiposEstandarDAO.obtenerTiposEstandar();
        if(!mstTiposEstandarList.isEmpty()){
            Log.i("RESULTADOF", mstTiposEstandarList.get(0).getDescripcion());
            try {
                Swal.info(this,"Hola", mstTiposEstandarList.get(0).getDescripcion(), 5000);
            }catch (Exception e){
                Swal.error(this,"Hola", "Ocurrió un error", 5000);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.estandares, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_estandares);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void inicializarBase() {
        AppDatabase db = DataGreenApp.getAppDatabase();
        trxEstandaresNewDAO = db.estandaresNewDAO();
        mstTiposEstandarDAO = db.mstTiposEstandarDAO();
        mstTiposBonoEstandarDAO = db.mstTiposBonoEstandarDAO();
        mstTiposCostoEstandarDAO = db.mstTiposCostoEstandarDAO();
        mstMedidasEstandaresDAO = db.mstMedidasEstandaresDAO();
    }

    public void goTo(int resId){
//    public void goTo(String vista){
            navController.navigate(resId);
//        if (Objects.equals(vista, "agregar")) {
//        }
//        else if (vista == R.vista.action_delete) {
//            Swal.info(ctx, "PUSH!", "Presionaste eliminar", 5000);
//            return true;
//        } else if (vista == R.vista.action_transfer) {
//            Swal.info(ctx, "PUSH!", "Presionaste transferir", 5000);
//            return true;
//        } else if (vista == R.vista.action_select_all) {
//            Swal.info(ctx, "PUSH!", "Presionaste seleccionar todo", 5000);
//            return true;
//        } else if (vista == R.vista.action_duplicity) {
//            Swal.info(ctx, "PUSH!", "Presionaste duplicar", 5000);
//            return true;
//        }
    }

}