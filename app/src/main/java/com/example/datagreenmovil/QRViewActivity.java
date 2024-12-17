package com.example.datagreenmovil;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.QRHelper.QRHelper;
import com.example.datagreenmovil.databinding.ActivityQrviewBinding;

import java.util.List;

public class QRViewActivity extends AppCompatActivity {
    private ActivityQrviewBinding binding;
    private int current = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityQrviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Obtener el Intent que inició esta actividad y los extras
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            // Recuperar el string que enviaste en el Bundle
            String trabajadoresJson = extras.getString("trabajadores");

            // Usa el valor recuperado como lo necesites
            // Por ejemplo, si es un JSON, puedes convertirlo a un objeto o procesarlo
            // JSONObject jsonObject = new JSONObject(trabajadoresJson);
            Log.i("BUNDLEPARAMS", trabajadoresJson);
            new QRHelper(bitmap -> {
                // Aquí puedes manejar el bitmap generado, por ejemplo, mostrarlo en un ImageView
                if (bitmap != null) {
                    binding.pbQr.setVisibility(View.GONE);
                    setearImagen(bitmap);
                } else {
                    Swal.error(this, "Oops!", "La generación del código QR ha tenido un error", 4000);
                }
            }).execute(trabajadoresJson);
        }

//
//        String content = "[]";
//        if(parametros != null) {
//            content = parametros.getString("trabajadores");
//            Log.i("DEBUG", content);
//        }


    }

    void setearImagen(List<Bitmap> bitmap){

        int contador = bitmap.size();

        binding.fabPrev.setOnClickListener(v -> {
            if(current >= 1){
                --current;
            }else {
                current = 0;
            }
            binding.imageQR.setImageBitmap(bitmap.get(current));
        });

        binding.fabNext.setOnClickListener(v -> {
            if(current < contador - 1){
                ++current;
            }else {
                current = contador - 1;
            }
            binding.imageQR.setImageBitmap(bitmap.get(current));
        });

    }


}