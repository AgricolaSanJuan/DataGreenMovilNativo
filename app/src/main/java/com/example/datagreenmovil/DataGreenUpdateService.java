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
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.datagreenmovil.Logica.Swal;

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

    public class DownloadUpdate extends AsyncTask<String, Void, Void> {
        File outputFile;
        @Override
        protected Void doInBackground(String... params) {
            String fileUrl = params[0]; // URL del archivo que deseas descargar
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

                    byte[] buffer = new byte[1024];
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
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {

//            Toast.makeText(DataGreenUpdateService.this,  Toast.LENGTH_SHORT).show();
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                NotificationChannel channel = new NotificationChannel("mi_canal_id", "Mi Canal", NotificationManager.IMPORTANCE_DEFAULT);
//                NotificationManager notificationManager = getSystemService(NotificationManager.class);
//                notificationManager.createNotificationChannel(channel);
//            }
//
//            // Define la intención (intent) para abrir el archivo después de la descarga
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            Uri uri = Uri.fromFile(new File(outputFile.toURI())); // Reemplaza "ruta_del_archivo" con la ubicación de tu archivo descargado
//            intent.setDataAndType(uri, "application/pdf"); // Cambia el tipo MIME según el tipo de archivo
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            PendingIntent pendingIntent = PendingIntent.getActivity(DataGreenUpdateService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            // Crea la notificación de descarga
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(DataGreenUpdateService.this, "mi_canal_id")
//                    .setContentTitle("Descarga completada")
//                    .setContentText("Haz clic para abrir el archivo descargado")
//                    .setSmallIcon(R.drawable.ic_launcher_foreground)
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                    .setContentIntent(pendingIntent)
//                    .setAutoCancel(true); // Cierra la notificación después de hacer clic en ella
//
//            // Muestra la notificación
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(DataGreenUpdateService.this);
//            notificationManager.notify(1, builder.build()); // Puedes usar un ID diferente para cada notificación
            super.onPostExecute(unused);
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

                if(!appVersion.equals(result)){
                    String fileUrl = "http://192.168.30.158:8000/api/download-file";
                    String fileName = "DataGreen"+result+".apk";
                    new DownloadUpdate().execute(fileUrl,fileName);
                    Toast.makeText(DataGreenUpdateService.this, "Se ha descargado una version disponible!", Toast.LENGTH_SHORT).show();

                }

            } else {
//                Toast.makeText(DataGreenUpdateService.this, "Error en la solicitud de descarga.", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, "CANCELADO", Toast.LENGTH_SHORT).show();
    }

    private class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Verificar el estado de la conexión cuando cambia la red
            if (isConnectedToInternet()) {
                try {
                    new ApiRequestTask().execute("http://192.168.30.158:8000/api/get-available-update");
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
