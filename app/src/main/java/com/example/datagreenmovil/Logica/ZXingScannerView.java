package com.example.datagreenmovil.Logica;

import android.content.Context;
import android.util.AttributeSet;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.journeyapps.barcodescanner.BarcodeCallback;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ZXingScannerView extends DecoratedBarcodeView {
    public ZXingScannerView(Context context) {
        super(context);
        init();
    }

    public ZXingScannerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        List<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.QR_CODE);
        formats.add(BarcodeFormat.CODE_128);

        setCameraDistance(0.5f);

        setDecoderFactory(new DefaultDecoderFactory(formats));
    }

    public void setResultHandler(OnScanResultListener resultListener) {
        decodeSingle(new BarcodeCallback() {

            @Override
            public void barcodeResult(BarcodeResult result) {
                try {
                    resultListener.onScanResult(result.getResult());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void possibleResultPoints(List  resultPoints) {
            }
        });
    }

    public interface OnScanResultListener {
        void onScanResult(Result result) throws Exception;
    }
}
