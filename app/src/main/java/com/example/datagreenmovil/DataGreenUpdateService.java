package com.example.datagreenmovil;

import android.Manifest;
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
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

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
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        // Crear canal de notificación
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("mi_canal_id", "Mi Canal", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Verificar y solicitar permisos
        checkStoragePermissions();
    }

    private void checkStoragePermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // No tenemos permiso, se podría solicitar aquí
            // Si estás usando un servicio, considera que esto normalmente se hace en una actividad
            // Aquí solo estamos mostrando un mensaje, puedes implementar la lógica que necesites
            Toast.makeText(this, "Se requiere permiso para acceder al almacenamiento", Toast.LENGTH_SHORT).show();
        } else {
            // Ya tenemos permiso, puedes proceder
            proceedWithFileAccess();
        }
    }

    private void proceedWithFileAccess() {
        // Lógica para acceder a archivos o continuar con la descarga
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
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver);
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
            Toast.makeText(ctx, "Descarga completada", Toast.LENGTH_SHORT).show();

            if (outputFile != null && outputFile.exists()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = FileProvider.getUriForFile(ctx, "com.example.datagreenmovil.fileprovider", outputFile);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // Crea el PendingIntent para la notificación
                PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                // Crea la notificación de descarga utilizando NotificationCompat.Builder
                NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, "mi_canal_id")
                        .setContentTitle("Descarga completada")
                        .setContentText("Pulsa para instalar la actualización.")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent) // Asocia el PendingIntent
                        .setAutoCancel(true); // Cierra la notificación después de hacer clic en ella

                // Finalmente, construye y muestra la notificación
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);
                notificationManager.notify(1, builder.build()); // Puedes usar un ID diferente para cada notificación
            } else {
                Toast.makeText(ctx, "Error: archivo no encontrado", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private class ApiRequestTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    return response.toString();
                } else {
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
            if (result != null) {
                String appVersion = "";
                try {
                    PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    appVersion = packageInfo.versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                if (!appVersion.equals(result) && ctx != null) {
                    SharedPreferences sharedPreferences = ctx.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
                    String ServerIP = sharedPreferences.getString("API_SERVER", "");
                    String fileUrl = "http://192.168.30.99:8090/DataGreenMovil/MiniGreen" + result + ".apk";
                    String fileName = "MiniGreen" + result + ".apk";
                    new DownloadUpdate().execute(fileUrl, fileName);

                    Toast.makeText(DataGreenUpdateService.this, "Se está descargando una versión disponible, por favor espere!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ctx, "No hay nueva versión disponible.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(DataGreenUpdateService.this, "Error en la solicitud de descarga.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ctx = context;
            if (isConnectedToInternet()) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
                String ServerIP = sharedPreferences.getString("API_SERVER", "");
                new ApiRequestTask().execute("http://" + ServerIP + "/api/minigreen/validar_version");
            }
        }
    }
}
