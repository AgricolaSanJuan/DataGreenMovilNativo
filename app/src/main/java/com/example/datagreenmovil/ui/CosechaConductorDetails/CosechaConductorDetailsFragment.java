package com.example.datagreenmovil.ui.CosechaConductorDetails;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.Logica.ZXingScannerView;
import com.example.datagreenmovil.databinding.FragmentCosechaConductorDetailsBinding;
import com.example.datagreenmovil.ui.CosechaConductorDetails.Adapters.CosechaDetailsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CosechaConductorDetailsFragment extends Fragment {
    private FragmentCosechaConductorDetailsBinding binding;
    private Context ctx;
    private SweetAlertDialog sweetAlertDialogScan;
    private JSONArray jsonDetallesContenedorCosecha = new JSONArray();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){

        binding = FragmentCosechaConductorDetailsBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

//        CREAMOS EL LAYOUT MANAGER
        GridLayoutManager layoutManager = new GridLayoutManager(ctx, 1);
        binding.listContenedorCosecha.setLayoutManager(layoutManager);
        setHandlerModalScanner();
        setListDetalles();
        return root;
    }

    private void setListDetalles() {
        CosechaDetailsAdapter adapter = new CosechaDetailsAdapter(jsonDetallesContenedorCosecha);
        binding.listContenedorCosecha.setAdapter(adapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ctx = context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    //        PopupConfirmacionEscaneo popupConfirmacionEscaneo = new PopupConfirmacionEscaneo(ctx);
//        View view = popupConfirmacionEscaneo.onCreateView(LayoutInflater.from(ctx), null, null);
//
//        ZXingScannerView zXingScannerView = view.findViewById(R.id.cosechaConfirmacionView);
//        setHandlerModalScanner(zXingScannerView);
//        // Verificar si la vista está disponible
//        if (view != null) {
//            binding.cosechaScannerView.pause();
//            // La vista está disponible, puedes usarla como lo desees
//            // Por ejemplo, puedes agregarla a un contenedor en tu diseño de actividad o fragmento principal
//            sweetAlertDialogScan = Swal.confirm(ctx, "Está seguro?", view)
//                    .setConfirmClickListener(sweetAlertDialog -> {
//                        JSONObject result = null;
//                        try {
//                            result = new JSONObject(barcodeValue);
//                            JSONArray registers = result.getJSONArray("detalles");
//
//                            String values = "";
//                            for(int i = 0; i < registers.length(); i++){
//                                JSONObject registro = registers.getJSONObject(i);
//                                values += registro.get("dni");
//                                values += registro.get("cantidad");
//                                Log.i("REGISTRO: "+i, values);
//                                values = "";
//                            }
//                            Swal.info(ctx, "resultado", result.getString("id"), 5000);
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                        finally {
//                            sweetAlertDialog.dismissWithAnimation();
//                            setHandlerScanner();
//                        }
//                    });
//        } else {
//            // La vista aún no está disponible, puedes manejar este caso según tus necesidades
//        }
//    PROCESOS PARA SCANNER
void setHandlerModalScanner(){
    binding.cosechaScannerDetailsView.resume();
    binding.cosechaScannerDetailsView.setResultHandler(result -> {
        try {
            resultadoModal(result.getText());
        } catch (JSONException e) {
            Log.e("ERROR CAMARA:", e.toString());
            throw new RuntimeException(e);
        }
    });
}
    public void resultadoModal(String barcodeValue) throws JSONException {
        String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        binding.cosechaScannerDetailsView.pause();
//        CONVERTIMOS EL RESULTADO A UN JSON
        JSONArray jsonArrayResult = new JSONArray(barcodeValue);
        JSONObject jsonObject = jsonArrayResult.getJSONObject(0);
//        OBTENEMOS EL ID
        String idBin = jsonObject.getString("id");
//        OBTENEMOS LOS DETALLES
        JSONArray detalles = jsonObject.getJSONArray("detalles");
//        CREAMOS UN JSONObject PARA AGREGARLO A LOS DETALLES
        int cantidades = 0;
        for(int i = 0 ; i < detalles.length() ; i++){
            Log.i("detalle "+i, detalles.getJSONObject(i).toString());
            cantidades = detalles.getJSONObject(i).getInt("cantidad");
        }
        JSONObject newDetail = new JSONObject();
        newDetail.put("tipo", "Capachón");
        newDetail.put("cantidad", cantidades);
        newDetail.put("personas", detalles.length());
        newDetail.put("fecha", currentDate);
        newDetail.put("hora", currentTime);
        jsonDetallesContenedorCosecha.put(newDetail);
        Swal.success(ctx, "Correcto!", "Se han agregado los registros", 1.000);
        setListDetalles();

//        Swal.info(ctx, "DETALLE 1", String.valueOf(detalles.getJSONObject(0)), 5000);
        CountDownTimer countDownTimer = new CountDownTimer(500, 500) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() {
                setHandlerModalScanner();
            }
        };
        countDownTimer.start();
    }
}
