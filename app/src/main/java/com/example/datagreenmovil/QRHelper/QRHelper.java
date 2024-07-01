package com.example.datagreenmovil.QRHelper;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class QRHelper extends AsyncTask<String, Void, Bitmap> {

    public interface QRCodeGenerateListener {
        void onQRCodeGenerated(Bitmap bitmap);
    }
    private QRCodeGenerateListener mListener;

    public QRHelper(QRCodeGenerateListener listener) {
        mListener = listener;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (mListener != null) {
            mListener.onQRCodeGenerated(bitmap);
        }
    }

    @Override
    protected Bitmap doInBackground(String... content) {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = writer.encode(Arrays.toString(content), BarcodeFormat.QR_CODE, 500, 500, hints);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        return bmp;
    }
}
