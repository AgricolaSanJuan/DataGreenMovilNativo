package com.example.datagreenmovil;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;

import com.example.datagreenmovil.Logica.Swal;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataGreenUpdateService extends Service {
    Context ctx;
    Snackbar snackbar;
    public class DownloadUpdate extends AsyncTask<String, Void, Void> {
        File outputFile;
        @Override
        protected Void doInBackground(String... params) {
            String fileUrl = params[0]; // URL completa del archivo que deseas descargar
            String fileName = params[1]; // Nombre del archivo en el dispositivo

            try {
                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                    // Carpeta de destino para la descarga
                    String destinationDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                    outputFile = new File(destinationDirectory, fileName);

                    // Flujo de salida para escribir el archivo
                    FileOutputStream outputStream = new FileOutputStream(outputFile);

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    outputStream.close();
                    inputStream.close();

                    // Notificar al sistema que se ha descargado un archivo
                } else {
                    Log.e("HTTP", "Error en la solicitud. Código de respuesta: " + responseCode);
                }
            } catch (IOException e) {
                Log.e("ERROR AL DESCARGAR", e.toString());
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            // Muestra un Toast para indicar que la descarga ha finalizado
            Toast.makeText(ctx, "Descarga completada", Toast.LENGTH_SHORT).show();

            // Comprueba si el dispositivo está ejecutando Android Oreo (API 26) o superior
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Crea un canal de notificación para Android Oreo y versiones superiores
                NotificationChannel channel = new NotificationChannel("mi_canal_id", "Mi Canal", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            // Define la intención (intent) para abrir el archivo después de la descarga
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = FileProvider.getUriForFile(ctx, "com.example.datagreenmovil.fileprovider", outputFile);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");

            // Asegúrate de que el PendingIntent tenga permisos de lectura para la URI del archivo
            PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            ctx.grantUriPermission("com.android.packageinstaller", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Crea la notificación de descarga utilizando NotificationCompat.Builder
            NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, "mini_green_update")
                    .setContentTitle("Descarga completada")
                    .setContentText("Busque el archivo en la carpeta de descargas y ejecutelo.")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true); // Cierra la notificación después de hacer clic en ella

            // Finalmente, construye y muestra la notificación
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);
            notificationManager.notify(1, builder.build()); // Puedes usar un ID diferente para cada notificación
        }


    }
    private class ApiRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                // Crear una URL desde el primer parámetro
                URL url = new URL(params[0]);

                // Abrir una conexión HTTP
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Leer la respuesta de la API
                int responseCode = connection.getResponseCode();
                Log.d("Response Code", String.valueOf(responseCode));
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    return response.toString();
                } else {
                    // Manejar errores aquí (por ejemplo, si la respuesta no es HTTP_OK)
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Actualizar la vista con el valor de la respuesta
            if (result != null) {

//                OBTENEMOS LA VERSIONA ACTUAL DE NUESTRA APLICACION
                String appVersion = ""; // Asigna el valor predeterminado
                try {
                    PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    appVersion = packageInfo.versionName; // Obtiene la versión de la aplicación desde el archivo build.gradle
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                if(!appVersion.equals(result) && ctx != null){
                    SharedPreferences sharedPreferences = ctx.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
                    String ServerIP = sharedPreferences.getString("API_SERVER", "");
                    String fileUrl = "http://192.168.30.99:8090/DataGreenMovil/MiniGreen"+result+".apk";
                    String fileName = "MiniGreen"+result+".apk";
                    new DownloadUpdate().execute(fileUrl, fileName);

                    Toast.makeText(DataGreenUpdateService.this, "Se está descargando una versión disponible, por favor espere!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ctx, "No hay nueva versión disponible.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(DataGreenUpdateService.this, "Error en la solicitud de descarga.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private NetworkChangeReceiver networkChangeReceiver;

    public DataGreenUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Inicializa y registra el receptor de cambios en la red
        networkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);

        // Aquí puedes realizar las operaciones del servicio en segundo plano.

        return START_STICKY; // Esto hace que el servicio se reinicie automáticamente si se detiene.
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Libera recursos y desregistra el receptor cuando se detiene el servicio
        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver);
        }
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ctx = context;
            // Verificar el estado de la conexión cuando cambia la red
            if (isConnectedToInternet()) {
                try {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
                    String ServerIP = sharedPreferences.getString("API_SERVER", "");
                    new ApiRequestTask().execute("http://"+ServerIP+"/api/minigreen/validar_version");
                    // Mover la obtención de la versión de la aplicación aquí
                    PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    String versionName = packageInfo.versionName;
                    int versionCode = packageInfo.versionCode;
                    // Hacer algo con la versión, por ejemplo, mostrarla en un TextView o un Log

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                // Realizar acciones cuando hay conexión a Internet
                // Iniciar la tarea para obtener la respuesta de la API
//                Toast.makeText(context, "Conexión a Internet disponible", Toast.LENGTH_SHORT).show();
            } else {
                // Realizar acciones cuando no hay conexión a Internet
//                Toast.makeText(context, "No hay conexión a Internet", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

}
