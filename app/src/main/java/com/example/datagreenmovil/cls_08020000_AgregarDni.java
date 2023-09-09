package com.example.datagreenmovil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Size;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.Conexiones.ConexionSqlite;
import com.example.datagreenmovil.Entidades.ConfiguracionLocal;
import com.example.datagreenmovil.Entidades.Rex;
import com.example.datagreenmovil.Logica.Funciones;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class cls_08020000_AgregarDni extends AppCompatActivity {

    ConexionSqlite objSqlite;
    ConexionBD objSql;
    ConfiguracionLocal objConfLocal;
    TextView txv_PushTituloVentana, txv_PushRed, txv_NombreApp, txv_PushVersionApp, txv_PushVersionDataBase, txv_PushIdentificador;
    //    AlertDialog.Builder builderDialogoCerrarSesion;
    Dialog dlg_PopUp;

    LinearLayout c025_lly_IngresoDni;
    TextView c025_txv_Contador, c025_txv_DniMarcado, c025_txv_NombreMarcado, c025_txv_ApellidoMarcado, c025_txv_IngresoDni;
    Button c025_btn_1, c025_btn_2, c025_btn_3, c025_btn_4, c025_btn_5, c025_btn_6, c025_btn_7, c025_btn_8, c025_btn_9, c025_btn_0, c025_btn_X, c025_btn_Ok;
    TextureView tvLector;
    Rex objRex;
    String s_DniMarcado = "", s_IdRex;
    int i_Items, i_Capacidad;

    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_08020000_agregar_dni_025);
        //@Jota:2023-05-27 -> INICIO DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        try{
            if(getIntent().getExtras()!=null){
                objConfLocal=(ConfiguracionLocal) getIntent().getSerializableExtra("ConfiguracionLocal");
                s_IdRex =(String) getIntent().getSerializableExtra("IdRegistro");
            }

            objSql = new ConexionBD(objConfLocal);
            objSqlite = new ConexionSqlite(this,objConfLocal);
            objConfLocal.set("ULTIMA_ACTIVIDAD","PlantillaBase");

            referenciarControles();
            setearControles();
            Funciones.mostrarEstatusGeneral(this.getBaseContext(),
                objConfLocal,
                txv_PushTituloVentana,
                txv_PushRed,
                txv_NombreApp,
                txv_PushVersionApp,
                txv_PushVersionDataBase,
                txv_PushIdentificador
            );
            //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
            //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
            //...
            //PENDIENTE REEMPLZAR ESTE METODO EN LINEAS MAS ABAJO DESPUES DE MARCAR

            obtenerRexActual();
            if (objRex.Get("IdServicioTransporte") != null && !objRex.Get("IdServicioTransporte").equals("")){
                mostrarValoresRexActual();
            }
            openCamera();
        }catch (Exception ex){
            Funciones.mostrarError(this,ex);
        }
    }

    //@Jota:2023-05-27 -> LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
    private void setearControles() {
//        builderDialogoCerrarSesion= Funciones.setearAlertDialogParaCerrarSesion_(objConfLocal,objSqlite,this);

        //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
        //...
    }

    private void referenciarControles() {
        txv_PushTituloVentana = findViewById(R.id.c025_txv_PushTituloVentana_v);
        txv_PushRed = findViewById(R.id.c025_txv_PushRed_v);
        txv_NombreApp = findViewById(R.id.c025_txv_NombreApp_v);
        txv_PushVersionApp = findViewById(R.id.c025_txv_PushVersionApp_v);
        txv_PushVersionDataBase = findViewById(R.id.c025_txv_PushVersionDataBase_v);
        txv_PushIdentificador = findViewById(R.id.c025_txv_PushIdentificador_v);

        //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
        //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
        //...
        c025_lly_IngresoDni = findViewById(R.id.c025_lly_IngresoDni_v);
        c025_txv_Contador = findViewById(R.id.c025_txv_Contador_v);
        c025_txv_DniMarcado = findViewById(R.id.c025_txv_DniMarcado_v);
        c025_txv_NombreMarcado = findViewById(R.id.c025_txv_NombreMarcado_v);
        c025_txv_ApellidoMarcado = findViewById(R.id.c025_txv_ApellidoMarcado_v);
        c025_txv_IngresoDni = findViewById(R.id.c025_txv_IngresoDni_v);
        c025_btn_1 = findViewById(R.id.c025_btn_1_v);
        c025_btn_2 = findViewById(R.id.c025_btn_2_v);
        c025_btn_3 = findViewById(R.id.c025_btn_3_v);
        c025_btn_4 = findViewById(R.id.c025_btn_4_v);
        c025_btn_5 = findViewById(R.id.c025_btn_5_v);
        c025_btn_6 = findViewById(R.id.c025_btn_6_v);
        c025_btn_7 = findViewById(R.id.c025_btn_7_v);
        c025_btn_8 = findViewById(R.id.c025_btn_8_v);
        c025_btn_9 = findViewById(R.id.c025_btn_9_v);
        c025_btn_0 = findViewById(R.id.c025_btn_0_v);
        c025_btn_X = findViewById(R.id.c025_btn_X_v);
        c025_btn_Ok = findViewById(R.id.c025_btn_Ok_v);

        tvLector = findViewById(R.id.tvLector);
    }

    public void mostrarMenuUsuario(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this::onMenuItemClick);
        popup.inflate(R.menu.mnu_00000001_menu_usuario);
        popup.show();
    }

    public boolean onMenuItemClick(MenuItem item) {
        try{
            int idControlClickeado = item.getItemId();
            if (idControlClickeado==R.id.opc_00000001_cambiar_clave_usuario_v){
                dlg_PopUp = Funciones.obtenerDialogParaCambiarClave(this,objConfLocal,objSqlite,this);
                dlg_PopUp.show();
            } else if (idControlClickeado==R.id.opc_00000001_cerrar_sesion_v) {
                dlg_PopUp = Funciones.obtenerDialogParaCerrarSesion(this,objConfLocal,objSqlite,this);
                dlg_PopUp.show();
            }else return false;
        }catch(Exception ex){
            Funciones.mostrarError(this,ex);
            return false;
        }
        return true;
    }

    public void onClick(View view) {
        try {
            int idControlClickeado = view.getId();
            if (idControlClickeado == R.id.c025_txv_PushTituloVentana_v){
                Funciones.popUpTablasPendientesDeEnviar(this);
            } else if (idControlClickeado == R.id.c025_txv_PushRed_v) {
                objSql.probarConexion(objConfLocal);
                Funciones.mostrarEstatusGeneral(this.getBaseContext(),
                    objConfLocal,
                    txv_PushTituloVentana,
                    txv_PushRed,
                    txv_NombreApp,
                    txv_PushVersionApp,
                    txv_PushVersionDataBase,
                    txv_PushIdentificador
                );
            } else if (idControlClickeado == R.id.c025_txv_PushVersionApp_v || idControlClickeado == R.id.c025_txv_PushVersionDataBase_v) {
                Funciones.popUpStatusVersiones(this);
            } else if (idControlClickeado == R.id.c025_txv_PushIdentificador_v) {
                mostrarMenuUsuario(this.txv_PushIdentificador);
            }
            //@Jota:2023-05-27 -> FIN DE LINEAS DE CODIGO COMUNES PARA TODAS LAS ACTIVIDADES
            //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
            //...
            else if (idControlClickeado == R.id.c025_btn_1_v) {
                s_DniMarcado = s_DniMarcado + "1";
                c025_txv_IngresoDni.setText(s_DniMarcado);
            }else if (idControlClickeado == R.id.c025_btn_2_v) {
                s_DniMarcado = s_DniMarcado + "2";
                c025_txv_IngresoDni.setText(s_DniMarcado);
            }else if (idControlClickeado == R.id.c025_btn_3_v) {
                s_DniMarcado = s_DniMarcado + "3";
                c025_txv_IngresoDni.setText(s_DniMarcado);
            }else if (idControlClickeado == R.id.c025_btn_4_v) {
                s_DniMarcado = s_DniMarcado + "4";
                c025_txv_IngresoDni.setText(s_DniMarcado);
            }else if (idControlClickeado == R.id.c025_btn_5_v) {
                s_DniMarcado = s_DniMarcado + "5";
                c025_txv_IngresoDni.setText(s_DniMarcado);
            }else if (idControlClickeado == R.id.c025_btn_6_v) {
                s_DniMarcado = s_DniMarcado + "6";
                c025_txv_IngresoDni.setText(s_DniMarcado);
            }else if (idControlClickeado == R.id.c025_btn_7_v) {
                s_DniMarcado = s_DniMarcado + "7";
                c025_txv_IngresoDni.setText(s_DniMarcado);
            }else if (idControlClickeado == R.id.c025_btn_8_v) {
                s_DniMarcado = s_DniMarcado + "8";
                c025_txv_IngresoDni.setText(s_DniMarcado);
            }else if (idControlClickeado == R.id.c025_btn_9_v) {
                s_DniMarcado = s_DniMarcado + "9";
                c025_txv_IngresoDni.setText(s_DniMarcado);
            }else if (idControlClickeado == R.id.c025_btn_0_v) {
                s_DniMarcado = s_DniMarcado + "0";
                c025_txv_IngresoDni.setText(s_DniMarcado);
            }else if (idControlClickeado == R.id.c025_btn_X_v) {
                if (s_DniMarcado.length()>0){
                    s_DniMarcado = s_DniMarcado.substring(0,s_DniMarcado.length() - 1);
                    c025_txv_IngresoDni.setText(s_DniMarcado);
                }
            }else if (idControlClickeado == R.id.c025_btn_Ok_v) {
                if(cumpleRestricciones() && guardarDni(s_DniMarcado)){
                    actualizarNItems(s_IdRex);
                    c025_txv_IngresoDni.setText("");
                    s_DniMarcado="";
                    obtenerRexActual();
                    mostrarValoresRexActual();
                }else Funciones.notificar(this,objConfLocal.get("MENSAJE"));
            }else if (idControlClickeado == R.id.c025_fab_Volver_v){
                finish();
            } else throw new IllegalStateException("Click sin programacion: " + view.getId());
            c025_lly_IngresoDni.setBackground(ResourcesCompat.getDrawable(this.getResources(), !cumpleRestricciones() ? R.drawable.bg_alerta_suave : R.drawable.bg_r_blanco,null));
        } catch (Exception ex) {
            Funciones.mostrarError(this,ex);
        }
    }

    //@Jota:
    //METER CODIGO PROPIO DE CADA ACTIVIDAD DESPUES DE ESTA LINEA
    //...

    public boolean cumpleRestricciones(){
        return cumpleLongitudCadena();
    }

    public boolean cumpleLongitudCadena(){
        return s_DniMarcado.length()==8;
    }
    private boolean guardarDni(String s_dniMarcado) {
        try{
            final MediaPlayer Notificacion = MediaPlayer.create(this, R.raw.notificacion);
//            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//            Date fechaHora = new Date();
            objRex.Set("Item",i_Items + 1);
            objRex.Set("NroDocumento",s_dniMarcado);
            //objRex.Set("Hora",dateFormat.format(fechaHora));
            objRex.Set("FechaHora",LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
            if (objSqlite.GuardarRex(objConfLocal,"trx_ServiciosTransporte_Detalle" , objRex)){
                i_Items++;
                Notificacion.start();
                Funciones.mostrarEstatusGeneral(this.getBaseContext(),
                    objConfLocal,
                    txv_PushTituloVentana,
                    txv_PushRed,
                    txv_NombreApp,
                    txv_PushVersionApp,
                    txv_PushVersionDataBase,
                    txv_PushIdentificador
                );
//                List<String> p = new ArrayList<>();
//                p.add(s_dniMarcado);
//                Cursor c = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER DATOS mst_Personas"),p,"READ");
//                c.moveToFirst();
            }
            return true;
        }catch (Exception ex){
            Funciones.mostrarError(this,ex);
            return false;
        }
    }

    public int obtenerItems(String idRegistro) throws Exception {
        List<String> p = new ArrayList<>();
        p.add(objConfLocal.get("ID_EMPRESA"));
        p.add(idRegistro);
        Cursor c = objSqlite.doItBaby(objSqlite.obtQuery("CONTAR trx_ServiciosTransporte_Detalle"),p,"READ");
        c.moveToFirst();
        return Integer.parseInt(c.getString(0));
    }

    public int obtenerCapacidad(String idRegistro) throws Exception {
        List<String> p = new ArrayList<>();
        p.add(objConfLocal.get("ID_EMPRESA"));
        p.add(idRegistro);
        Cursor c = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER CAPACIDAD trx_ServiciosTransporte"),p,"READ");
        c.moveToFirst();
        return Integer.parseInt(c.getString(0));
    }

    public void actualizarNItems(String idRegistro) throws Exception {
        List<String> p = new ArrayList<>();
        p.add(objConfLocal.get("ID_EMPRESA"));
        p.add(idRegistro);
        p.add(objConfLocal.get("ID_EMPRESA"));
        p.add(idRegistro);
        Cursor c = objSqlite.doItBaby(objSqlite.obtQuery("ACTUALIZAR N ITEMS trx_ServiciosTransporte"),p,"UPDATE");
    }

    @SuppressLint("Range")
    public void mostrarValoresRexActual() throws Exception {
        String contador = String.valueOf(i_Items) + "/" + String.valueOf(i_Capacidad);
//        String q ="SELECT D.Item, D.Dni, RTRIM(P.Nombres) Nombres, RTRIM(P.Paterno) Paterno, RTRIM(P.Materno) Materno \n" +
//                "FROM trx_ServiciosTransporte_Detalle D\n" +
//                "INNER JOIN (\n" +
//                "\tSELECT IdEmpresa, IdServicioTransporte, Max(Item) Item \n" +
//                "\tFROM trx_ServiciosTransporte_Detalle \n" +
//                "\tWHERE IdEmpresa=@IdEmpresa AND IdServicioTransporte=@IdServicioTransporte \n" +
//                "\tGROUP BY IdEmpresa, IdServicioTransporte\n" +
//                "\t) M ON D.IdEmpresa=M.IdEmpresa AND D.IdServicioTransporte=M.IdServicioTransporte AND D.Item=M.Item\n" +
//                "INNER JOIN mst_Personas P ON D.Dni=P.NroDocumento";
//        q = q.replace("@IdEmpresa",objRex_Detalle.Get("IdEmpresa"));
//        q = q.replace("@IdServicioTransporte",objRex_Detalle.Get("IdServicioTransporte"));
//        List<String> p = new ArrayList<>();
//        p.add(objRex.Get("IdEmpresa"));
//        p.add(objRex.Get("IdServicioTransporte"));
//        p.add(objRex.Get("IdEmpresa"));
//        p.add(objRex.Get("IdServicioTransporte"));
//        Cursor c = objSqlite.doItBaby(objSqlite.obtQuery("OBTENER DATA XA MOSTRAR trx_ServiciosTransporte_Detalle"),p,"READ");
//        c.moveToFirst();
        c025_txv_Contador.setText(contador);
//        c025_txv_DniMarcado.setText(c.getString(c.getColumnIndex("NroDocumento")));
//        c025_txv_NombreMarcado.setText(c.getString(c.getColumnIndex("Nombres")));
//        c025_txv_ApellidoMarcado.setText(c.getString(c.getColumnIndex("Apellidos")));
//        c025_txv_IngresoDni.setText("");
        c025_txv_DniMarcado.setText(objRex.Get("NroDocumento"));
        c025_txv_NombreMarcado.setText(objRex.Get("Nombres"));
        c025_txv_ApellidoMarcado.setText(objRex.Get("Apellidos"));
        c025_txv_IngresoDni.setText("");
    }

    private void obtenerRexActual() throws Exception {
        List<String> p = new ArrayList<>();
        p.add(objConfLocal.get("ID_EMPRESA"));
        p.add(s_IdRex);
        p.add(objConfLocal.get("ID_EMPRESA"));
        p.add(s_IdRex);
        //objRex = objSqlite.CursorARex(objSqlite.doItBaby(objSqlite.obtQuery("OBTENER trx_ServiciosTransporte_Detalle X ID"),p,"READ"));
        objRex = objSqlite.CursorARex(objSqlite.doItBaby(objSqlite.obtQuery("OBTENER DATA XA MOSTRAR trx_ServiciosTransporte_Detalle"),p,"READ"));
//        if (objRex.Get("IdServicioTransporte").length()==0){
//            String fechaHoraActual;// = dateFormat.format(date);
//            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//            LocalDateTime now = LocalDateTime.now();
//            fechaHoraActual = dtf.format(now);
        objRex.Set("IdEmpresa",objConfLocal.get("ID_EMPRESA"));
        objRex.Set("IdServicioTransporte",s_IdRex);
//        }
        i_Items = obtenerItems(s_IdRex);
        i_Capacidad = obtenerCapacidad(s_IdRex);
    }

//    VIEW CAMERA IN SURFACE VIEW

    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            // El permiso ya está concedido, puedes abrir la cámara.
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, puedes abrir la cámara.
                openCamera();
            } else {
                // Permiso denegado, muestra un mensaje de error o cierra la aplicación.
            }
        }
    }
    private void openCamera() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0]; // Puedes cambiar esto para usar la cámara frontal o trasera según tus necesidades.

            // Configurar el TextureView para mostrar la vista previa de la cámara.
            tvLector = findViewById(R.id.tvLector);
            tvLector.setSurfaceTextureListener(textureListener);

            // Abrir la cámara.
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                cameraManager.openCamera(cameraId, stateCallback, null);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            // Configurar la vista previa de la cámara aquí.
            try {
                SurfaceTexture texture = tvLector.getSurfaceTexture();
                texture.setDefaultBufferSize(width, height);

                // Configurar el tamaño de la vista previa para que coincida con el tamaño de TextureView.
//                Size previewSize = chooseOptimalSize(width, height);
//                texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());

                // Configurar la solicitud de captura de la cámara.
                final CaptureRequest.Builder captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                captureRequestBuilder.addTarget(new Surface(texture));

                // Crear una sesión de captura para mostrar la vista previa.
                cameraDevice.createCaptureSession(Collections.singletonList(new Surface(texture)),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            if (cameraDevice == null) {
                                return;
                            }
                            cameraCaptureSession = session;
                            try {
                                session.setRepeatingRequest(captureRequestBuilder.build(), null, null);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                            // Manejar un error en la configuración de la sesión.
                        }
                    }, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            // Manejar cambios en el tamaño de la vista previa si es necesario.
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            // Liberar recursos cuando la vista previa se destruye.
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            // Manejar actualizaciones de la vista previa si es necesario.
        }
    };

    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    private Size chooseOptimalSize(int width, int height) {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0]; // Puedes cambiar esto para usar la cámara frontal o trasera según tus necesidades.

            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            // Obtener los tamaños admitidos para la vista previa de la cámara.
            Size[] outputSizes = map.getOutputSizes(SurfaceTexture.class);

            // Elegir un tamaño que coincida con la relación de aspecto del TextureView.
            float targetRatio = (float) width / height;
            Size selectedSize = null;
            float minDiff = Float.MAX_VALUE;

            for (Size size : outputSizes) {
                float supportedRatio = (float) size.getWidth() / size.getHeight();
                float diff = Math.abs(targetRatio - supportedRatio);
                if (diff < minDiff) {
                    selectedSize = size;
                    minDiff = diff;
                }
            }

            if (selectedSize != null) {
                return selectedSize;
            }

            // Si no se encuentra un tamaño adecuado, simplemente elige el primero de la lista.
            return outputSizes[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}