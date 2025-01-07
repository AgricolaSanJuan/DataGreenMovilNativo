package com.example.datagreenmovil.Logica;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.nio.ByteBuffer;

public class CameraManager {
    private final Activity activity;
    private final android.hardware.camera2.CameraManager cameraManager;
    private android.hardware.camera2.CameraDevice cameraDevice;
    private android.hardware.camera2.CameraCaptureSession captureSession;
    private ImageReader imageReader;
    private TextureView textureView;
    private HandlerThread backgroundThread;
    private Handler backgroundHandler;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;

    // Interfaz para devolver el resultado capturado como un Bitmap
    public interface OnFrameCapturedListener {
        void onFrameCaptured(Bitmap bitmap);
    }

    // Constructor: inicializa el CameraManager
    public CameraManager(Activity activity) {
        this.activity = activity;
        cameraManager = (android.hardware.camera2.CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
    }

    // Método para inicializar la cámara con un TextureView y un listener para los frames capturados
    public void initialize(TextureView textureView, OnFrameCapturedListener listener) {
        this.textureView = textureView;
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                setupCamera(width, height, listener);
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
                // Sin implementar, pero necesario para la interfaz
            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                closeCamera();
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {
                // Sin implementar, pero necesario para la interfaz
            }
        });
    }

    // Configuración inicial de la cámara
    private void setupCamera(int width, int height, OnFrameCapturedListener listener) {
        try {
            String cameraId = cameraManager.getCameraIdList()[0]; // Seleccionar la primera cámara disponible
            Size previewSize = new Size(width, height);

            // Configuración del ImageReader para capturar imágenes
            imageReader = ImageReader.newInstance(previewSize.getWidth(), previewSize.getHeight(), ImageFormat.YUV_420_888, 2);

            // Iniciar el hilo en segundo plano solo una vez
            if (backgroundThread == null) {
                backgroundThread = new HandlerThread("CameraBackground");
                backgroundThread.start();
                backgroundHandler = new Handler(backgroundThread.getLooper());
            }

            imageReader.setOnImageAvailableListener(reader -> {
                Image image = reader.acquireNextImage();
                // Procesar la imagen en un hilo de fondo
                backgroundHandler.post(() -> {
                    Bitmap bitmap = convertImageToBitmap(image);
                    image.close();
                    if (bitmap != null) {
                        listener.onFrameCaptured(bitmap);
                    }
                });
            }, backgroundHandler);

            // Verificar permisos de la cámara
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                return;
            }

            // Abrir la cámara
            cameraManager.openCamera(cameraId, new android.hardware.camera2.CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull android.hardware.camera2.CameraDevice camera) {
                    cameraDevice = camera;
                    startPreview();
                }

                @Override
                public void onDisconnected(@NonNull android.hardware.camera2.CameraDevice camera) {
                    camera.close();
                    cameraDevice = null;
                }

                @Override
                public void onError(@NonNull android.hardware.camera2.CameraDevice camera, int error) {
                    camera.close();
                    cameraDevice = null;
                }
            }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Inicia la vista previa de la cámara
    private void startPreview() {
        try {
            SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
            Surface previewSurface = new Surface(surfaceTexture);

            // Crear una sesión de captura
            cameraDevice.createCaptureSession(
                    java.util.Arrays.asList(previewSurface, imageReader.getSurface()),
                    new android.hardware.camera2.CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull android.hardware.camera2.CameraCaptureSession session) {
                            captureSession = session;
                            try {
                                android.hardware.camera2.CaptureRequest.Builder captureRequestBuilder =
                                        cameraDevice.createCaptureRequest(android.hardware.camera2.CameraDevice.TEMPLATE_PREVIEW);
                                captureRequestBuilder.addTarget(previewSurface);
                                captureSession.setRepeatingRequest(captureRequestBuilder.build(), null, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull android.hardware.camera2.CameraCaptureSession session) {
                            Log.i("QRCode", "onConfigureFailed");
                        }
                    },
                    null
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Convierte un objeto Image a un Bitmap
    private Bitmap convertImageToBitmap(Image image) {
        Log.i("QRCode", "HOLI");
        if (image.getFormat() == ImageFormat.YUV_420_888) {
            Image.Plane[] planes = image.getPlanes();
            ByteBuffer buffer = planes[0].getBuffer();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);

            // Convertir los datos en un Bitmap (ejemplo simplificado)
            // Aquí puedes implementar la conversión de YUV a RGB
            return Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
        }
        return null;
    }

    // Cierra la cámara y libera recursos
    private void closeCamera() {
        if (captureSession != null) {
            captureSession.close();
            captureSession = null;
        }
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (imageReader != null) {
            imageReader.close();
            imageReader = null;
        }
        if (backgroundThread != null) {
            backgroundThread.quitSafely();
            try {
                backgroundThread.join();
                backgroundThread = null;
                backgroundHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Manejo de la respuesta de permisos en la actividad
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // El permiso fue concedido, puedes continuar con la inicialización de la cámara
                Log.i("CameraManager", "Permiso de cámara concedido.");
            } else {
                // El permiso fue denegado, maneja el caso según sea necesario
                Log.e("CameraManager", "Permiso de cámara denegado.");
            }
        }
    }
}
