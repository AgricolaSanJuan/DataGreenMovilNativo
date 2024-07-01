package com.example.datagreenmovil.ui.CosechaConductorMain;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;

import com.example.datagreenmovil.CosechaActivity;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.Logica.ZXingScannerView;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.databinding.FragmentCosechaQrConductorBinding;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CosechaConductorMainFragment extends Fragment {

    private FragmentCosechaQrConductorBinding binding;
    private Context ctx;
    private SharedPreferences sharedPreferences;
    private  SharedPreferences.Editor editor;
    private SQLiteDatabase database;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCosechaQrConductorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setHandlerScanner();

        generarTablaNuevaCosecha();

        return root;
    }

    private void generarTablaNuevaCosecha() {
        database = SQLiteDatabase.openDatabase(ctx.getDatabasePath("DataGreenMovil.db").toString(), null, SQLiteDatabase.OPEN_READWRITE);
        database.execSQL("CREATE TABLE IF NOT EXISTS 'cs_contenedor_cosecha' (idEmpresa CHAR(3), idDispositivo CHAR(3), idContenedorCosechaRemoto VARCHAR (12) ,idContenedorCosechaMovil VARCHAR (12),fechaProceso DATETIME,fechaCreacion DATETIME, fechaActualizacion DATETIME,idUsuarioCrea VARCHAR (50),idUsuarioActualiza VARCHAR (50),idEstado CHAR(3),PRIMARY KEY (idEmpresa, idContenedorCosechaRemoto, idContenedorCosechaMovil),FOREIGN KEY (idUsuarioCrea)REFERENCES mst_Usuarios (Id),FOREIGN KEY (idUsuarioActualiza)REFERENCES mst_Usuarios (Id),FOREIGN KEY (idEstado)REFERENCES mst_Estados (Id));");
    }

    //    SCANNER
void setHandlerScanner(){
    if(binding.cosechaScannerView != null){
        binding.cosechaScannerView.resume();
        binding.cosechaScannerView.setResultHandler(result -> {
            try {
                resultado(result.getText());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
    public String getNewId(){
        Cursor c = database.rawQuery("SELECT PRINTF('%03d', CASE WHEN COUNT(*) = 0 THEN 1 ELSE COUNT(*) END) AS correlativo FROM cs_contenedor_cosecha", null);
        c.moveToFirst();

        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String idDispositivo = sharedPreferences.getString("ID_DISPOSITIVO", "!!!");
        String correlativoRegistro = c.getString(0);
        return idDispositivo + formattedDate + correlativoRegistro;
    }
    public void resultado(String barcodeValue) throws JSONException {
        binding.cosechaScannerView.pause();

//        VERIFICAMOS SI EL BIN EXISTE, SI ES ASÍ, INSERTAMOS LOS DETALLES
        Cursor c = database.rawQuery("SELECT COUNT(*) contador FROM cs_contenedor_cosecha WHERE idContenedorCosechaRemoto = '"+barcodeValue+"'",null);
        c.moveToFirst();
        Boolean existe = c.getInt(0) > 0;
        if(existe){
//            SI EL BIN YA EXISTE, REDIRECCIONAMOS A LOS DETALLES
            redireccionarInsercionDetalles("BIN YA REGISTRADO");

        }else {
//        SETEAMOS LOS VALORES QUE VAMOS A USAR PARA LA INSERCIÓN
            String idEmpresa = sharedPreferences.getString("ID_EMPRESA", "01");
            String idDispositivo = sharedPreferences.getString("ID_DISPOSITIVO", "000");
            String idBinLocal = getNewId();
            String idUsuario = sharedPreferences.getString("ID_USUARIO_ACTUAL", "00000000");
//        INSERTAR REGISTRO DE BIN REMOTO EN LOCAL
            String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            ContentValues values = new ContentValues();
            values.put("idEmpresa", idEmpresa);
            values.put("idDispositivo", idDispositivo);
            values.put("idContenedorCosechaRemoto", barcodeValue);
            values.put("idContenedorCosechaMovil", idBinLocal);
            values.put("fechaProceso", currentDate);
            values.put("fechaCreacion", currentDateTime);
            values.put("fechaActualizacion", "");
            values.put("idUsuarioCrea", idUsuario);
            values.put("idUsuarioActualiza", "");
            values.put("idEstado", "AB");

            long result = database.insert("cs_contenedor_cosecha", null, values);

            if (result == -1) {
                Swal.error(ctx, "Oops!", "El BIN no se ha podido registrar, intente nuevamente", 2000);
            } else {
                redireccionarInsercionDetalles("El BIN se ha registrado correctamente, puede registrar los detalles");
            }
        }

        CountDownTimer countDownTimer = new CountDownTimer(1000,1000) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() {
                setHandlerScanner();
            }
        };
        countDownTimer.start();

    }

    private void redireccionarInsercionDetalles(String condicion) {
        SweetAlertDialog sweetAlertDialog = Swal.redirectioner(ctx, condicion,"REDIRECCIONANDO PARA INSERTAR DETALLES");
        CountDownTimer countDownTimer = new CountDownTimer(2000, 2000) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() {
                CosechaActivity cosechaActivity = (CosechaActivity) getActivity();
                if(cosechaActivity.obtenerNavigationController() != null){
                    NavController nc = cosechaActivity.obtenerNavigationController();
                    nc.navigate(R.id.nav_cosecha_conductor_details,
                            null,
                            new NavOptions.Builder().setPopUpTo(R.id.nav_cosecha_conductor, true).build());
                }
                sweetAlertDialog.dismissWithAnimation();
            }
        };
        countDownTimer.start();
    }

    //    SETEAMOS EL HANDLER RESULT PARA EL MODAL


    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ctx = context;
        sharedPreferences = ctx.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.cosechaScannerView.pause();
        binding = null;
    }
}