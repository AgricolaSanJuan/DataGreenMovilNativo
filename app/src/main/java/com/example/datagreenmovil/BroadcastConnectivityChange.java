package com.example.datagreenmovil;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

public class BroadcastConnectivityChange extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast","fino");
        Toast.makeText(context, "Conectado", Toast.LENGTH_SHORT).show();
        if (intent.getAction() != null && intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            // Verificar si hay una conexión a Internet.
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                if (connectivityManager.getActiveNetworkInfo() != null) {
                    // Hay una conexión a Internet, programa el JobService.

                }
            }
        }
    }

}