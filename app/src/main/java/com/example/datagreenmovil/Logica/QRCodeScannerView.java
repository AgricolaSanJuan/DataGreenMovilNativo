package com.example.datagreenmovil.Logica;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.example.datagreenmovil.R;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.concurrent.ExecutionException;

public class QRCodeScannerView extends FrameLayout {

    public void setImagesParams(int widthLogo, int heightLogo, int widthText, int heightText, int marginBottomText) {
        logoSanJuan = findViewById(R.id.imageSanJuan);
        textoSanJuan = findViewById(R.id.imageTextSanJuan);

        // Convertir dp a píxeles
        float scale = getResources().getDisplayMetrics().density;
        int widthPxLogo = (int) (widthLogo * scale + 0.5f);
        int heightPxLogo = (int) (heightLogo * scale + 0.5f);
        int widthPxTexto = (int) (widthText * scale + 0.5f);
        int heightPxTexto = (int) (heightText * scale + 0.5f);
        int marginBottomPxTexto = (int) (marginBottomText * scale + 0.5f);

        // Aplicar el ancho y alto al ImageView
        ViewGroup.LayoutParams layoutParamsLogo = logoSanJuan.getLayoutParams();
        layoutParamsLogo.width = widthPxLogo;
        layoutParamsLogo.height = heightPxLogo;
        logoSanJuan.setLayoutParams(layoutParamsLogo);

        ViewGroup.LayoutParams layoutParamsTexto = textoSanJuan.getLayoutParams();
        layoutParamsTexto.width = widthPxTexto;
        layoutParamsTexto.height = heightPxTexto;

        if (layoutParamsTexto instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParamsTexto = (ViewGroup.MarginLayoutParams) layoutParamsTexto;
            marginLayoutParamsTexto.setMargins(0, 0, 0, marginBottomPxTexto);
        }

        textoSanJuan.setLayoutParams(layoutParamsTexto);
    }

    public interface OnCodeScanned {
        void onCodeScanned(String result);
    }

    private PreviewView previewView;
    private BarcodeScanner barcodeScanner;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private OnCodeScanned onCodeScanned;
    private Camera camera;
    private ImageView logoSanJuan, textoSanJuan;

    public QRCodeScannerView(Context context) {
        super(context);
        init(context);
    }

    public QRCodeScannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public QRCodeScannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_qr_scanner, this, true);

        previewView = findViewById(R.id.previewView); // Assuming you're using PreviewView now
        barcodeScanner = BarcodeScanning.getClient();
        cameraProviderFuture = ProcessCameraProvider.getInstance(context);

        startCamera();
    }

    public void startCamera() {
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(getContext()), imageProxy -> {
                    processImageProxy(barcodeScanner, imageProxy);
                });

                cameraProvider.unbindAll();
                camera = cameraProvider.bindToLifecycle(
                        (LifecycleOwner) getContext(),
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageAnalysis
                );

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(getContext()));
    }

    private void processImageProxy(BarcodeScanner barcodeScanner, ImageProxy imageProxy) {
        @androidx.camera.core.ExperimentalGetImage
        InputImage inputImage = InputImage.fromMediaImage(imageProxy.getImage(), imageProxy.getImageInfo().getRotationDegrees());

        barcodeScanner.process(inputImage)
                .addOnSuccessListener(barcodes -> {
                    for (Barcode barcode : barcodes) {
                        String barcodeValue = barcode.getDisplayValue();
                        if (barcodeValue != null && onCodeScanned != null) {
                            onCodeScanned.onCodeScanned(barcodeValue);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                })
                .addOnCompleteListener(task -> {
                    imageProxy.close();
                });
    }

    public void stopScanning() {
        try {
            ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
            cameraProvider.unbindAll();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setOnCodeScannedListener(OnCodeScanned listener) {
        this.onCodeScanned = listener;
    }

    public void enableFlash() {
        if (camera != null) {
            CameraControl cameraControl = camera.getCameraControl();
            cameraControl.enableTorch(true); // Enciende el flash
        }
    }

    public void disableFlash() {
        if (camera != null) {
            CameraControl cameraControl = camera.getCameraControl();
            cameraControl.enableTorch(false); // Apaga el flash
        }
    }

}