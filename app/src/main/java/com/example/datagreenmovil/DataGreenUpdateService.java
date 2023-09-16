package com.example.datagreenmovil;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.widget.Toast;

public class DataGreenUpdateService extends Service {

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
                    // Mover la obtención de la versión de la aplicación aquí
                    PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    String versionName = packageInfo.versionName;
                    int versionCode = packageInfo.versionCode;
                    // Hacer algo con la versión, por ejemplo, mostrarla en un TextView o un Log

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                // Realizar acciones cuando hay conexión a Internet
                Toast.makeText(context, "Conexión a Internet disponible", Toast.LENGTH_SHORT).show();
            } else {
                // Realizar acciones cuando no hay conexión a Internet
                Toast.makeText(context, "No hay conexión a Internet", Toast.LENGTH_SHORT).show();
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
