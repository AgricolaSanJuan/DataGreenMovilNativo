package com.example.datagreenmovil;

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
import androidx.core.content.FileProvider;

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
    private Context ctx;
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        ctx = this; // Almacenar el contexto del servicio
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "mi_canal_id",
                    "Actualizaciones",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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

    public class DownloadUpdate extends AsyncTask<String, Integer, Void> {
        File outputFile;

        @Override
        protected Void doInBackground(String... params) {
            String fileUrl = params[0];
            String fileName = params[1];

            String destinationDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            outputFile = new File(destinationDirectory, fileName);

            if (outputFile.exists()) {
                publishProgress(-1); // Indicar que el archivo ya existe
                return null;
            }

            try {
                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    int fileLength = urlConnection.getContentLength();

                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    FileOutputStream outputStream = new FileOutputStream(outputFile);

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    long totalRead = 0;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        totalRead += bytesRead;
                        outputStream.write(buffer, 0, bytesRead);

                        if (fileLength > 0) {
                            int progress = (int) ((totalRead * 100) / fileLength);
//                            Log.i("PORCENTAJE", String.valueOf(progress));
                            publishProgress(progress);  // <-- Ahora funcionará
                            if(progress == 100){
                                Swal.info(getBaseContext(), "ACTUALIZACIÓN DISPONIBLE", "Existe una actualización disponible en la carpeta de descargas, instalala para obtener la últimas funcionalidades! 🚀", 8000);
                                Toast.makeText(getApplicationContext(), "Actualización descargada!", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                    outputStream.close();
                    inputStream.close();
                }
            } catch (IOException e) {
                Log.e("ERROR_DESCARGA", e.toString());
                e.printStackTrace();
            }
            return null;
        }

        // Método para actualizar el progreso de descarga
        @Override
        protected void onProgressUpdate(Integer... values) {
            if (values[0] == -1) {
//                Swal.info(ctx, "ACTUALIZACIÓN DISPONIBLE", "Existe una actualización disponible en la carpeta de descargas, instalala para obtener la últimas funcionalidades! 🚀", 8000);
//                Toast.makeText(ctx, "El archivo ya existe en la carpeta de descargas.", Toast.LENGTH_SHORT).show();
            } else {
//                Log.d("DownloadProgress", "Progreso: " + values[0] + "%");
                // Aquí podrías actualizar una notificación o una barra de progreso en la UI
            }
        }
    }


    private class ApiRequestTask extends AsyncTask<String, Void, String> {
        private Context context;

        public ApiRequestTask(Context context) {
            this.context = context.getApplicationContext(); // Usa el contexto de aplicación
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    return response.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) connection.disconnect();
                if (reader != null) try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    String appVersion = packageInfo.versionName;

                    if (!appVersion.equals(result)) {
                        SharedPreferences sharedPreferences = ctx.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
                        String serverIP = sharedPreferences.getString("API_SERVER", "");
                        String fileUrl = "http://" + serverIP + "/download/MiniGreen" + result + ".apk";
                        String fileName = "MiniGreen" + result + ".apk";
                        new DownloadUpdate().execute(fileUrl, fileName);

                        Toast.makeText(ctx, "Descargando nueva versión, espere...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ctx, "No hay nueva versión disponible.", Toast.LENGTH_SHORT).show();
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(ctx, "Error al verificar actualizaciones.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Usa el contexto de la aplicación para evitar fugas de memoria
            Context appContext = context.getApplicationContext();
            if (isConnectedToInternet()) {
                SharedPreferences sharedPreferences = appContext.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
                String serverIP = sharedPreferences.getString("API_SERVER", "");

                if (!serverIP.isEmpty()) {
                    new ApiRequestTask(appContext).execute("http://" + serverIP + "/api/minigreen/validar_version");
                } else {
                    Log.e("NetworkChangeReceiver", "Server IP no está configurada.");
                }
            }
        }
    }

}
