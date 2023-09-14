package com.example.datagreenmovil;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.net.ConnectivityManager;
import android.util.Log;

public class BroadcastConnectivityChange extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    Log.i("FINO","GRUESO");
    if (intent.getAction() != null && intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
      // Verificar si hay una conexión a Internet.
      ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      if (connectivityManager != null) {
        if (connectivityManager.getActiveNetworkInfo() != null) {
          // Hay una conexión a Internet, programa el JobService.

          scheduleJob(context);
        }
      }
    }
  }

  private void scheduleJob(Context context) {
    JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
    ComponentName componentName = new ComponentName(context, SyncDBService.class);

    JobInfo jobInfo = new JobInfo.Builder(1, componentName)
        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        .build();

    if (jobScheduler != null) {
      jobScheduler.schedule(jobInfo);
    }
  }
}