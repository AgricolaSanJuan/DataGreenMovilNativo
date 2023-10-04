package com.example.datagreenmovil.Services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class SyncDBService extends JobService {
  @Override
  public boolean onStartJob(JobParameters params) {
    Log.i("StartJOB","INICIADO");
    // Verificar la conectividad a Internet aquí.
    new InternetCheckTask().execute(params);
    return true;
  }

  @Override
  public boolean onStopJob(JobParameters params) {
    return true; // Para reprogramar el trabajo si se detiene.
  }

  private class InternetCheckTask extends AsyncTask<JobParameters, Void, Boolean> {
    private JobParameters jobParameters;

    @Override
    protected Boolean doInBackground(JobParameters... params) {
      jobParameters = params[0];
      return isConnectedToInternet();
    }

    @Override
    protected void onPostExecute(Boolean isConnected) {
      if (isConnected) {
        // Ejecuta la lógica cuando hay una conexión a Internet.
        // Puedes iniciar aquí cualquier tarea que necesites.
      }
      jobFinished(jobParameters, false);
    }

    private boolean isConnectedToInternet() {
      ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
      if (connectivityManager != null) {
        Toast.makeText(SyncDBService.this, "hay internet", Toast.LENGTH_SHORT).show();
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
      }
      return false;
    }
  }
}
