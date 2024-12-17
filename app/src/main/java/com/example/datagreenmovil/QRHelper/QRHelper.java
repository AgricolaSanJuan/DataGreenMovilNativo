package com.example.datagreenmovil.QRHelper;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QRHelper extends AsyncTask<String, Void, List<Bitmap>> {

    public interface QRCodeGenerateListener {
        void onQRCodeGenerated(List<Bitmap> bitmaps);
    }

    private QRCodeGenerateListener mListener;

    public QRHelper(QRCodeGenerateListener listener) {
        mListener = listener;
    }

    @Override
    protected void onPostExecute(List<Bitmap> bitmaps) {
        if (mListener != null) {
            mListener.onQRCodeGenerated(bitmaps);
        }
    }

    @Override
    protected List<Bitmap> doInBackground(String... content) {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        QRCodeWriter writer = new QRCodeWriter();
        List<Bitmap> qrCodes = new ArrayList<>();

        // Parsear la cadena de JSON
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(content[0]);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        // Definir el tamaño del bloque (100 elementos)
        int chunkSize = 50;
        int length = jsonArray.length();
        int numChunks = (int) Math.ceil((double) length / chunkSize);

        // Dividir en bloques de 100 elementos
        for (int i = 0; i < numChunks; i++) {
            // Obtener los primeros 100 o los elementos restantes
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, length);

            // Crear un nuevo JSONArray con los elementos del bloque
            JSONArray chunk = new JSONArray();
            JSONArray chunkDual = new JSONArray();
            for (int j = start; j < end; j++) {
                try {
                    chunk.put(jsonArray.get(j));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            chunkDual.put(chunk);

            // Generar código QR para cada bloque
            BitMatrix bitMatrix;
            try {
                Log.i("CHUNKS", chunkDual.toString());
                // Convertir el bloque de 100 elementos en una cadena JSON
                String chunkString = chunkDual.toString();
                bitMatrix = writer.encode(chunkString, BarcodeFormat.QR_CODE, 500, 500, hints);
            } catch (WriterException e) {
                throw new RuntimeException(e);
            }

            // Convertir el BitMatrix en un bitmap
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            // Agregar el bitmap a la lista
            qrCodes.add(bmp);
        }

        return qrCodes;
    }
}
