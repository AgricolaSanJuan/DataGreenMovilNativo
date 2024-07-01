package com.example.datagreenmovil.Conexiones;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.datagreenmovil.DataGreenApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyHttpTask extends AsyncTask<String, Void, String> {
    private Context ctx;

    public MyHttpTask(Context ctx){
        this.ctx = ctx;
    }
    @Override
    protected String doInBackground(String... params) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            SharedPreferences sharedPreferences = ctx.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
            String ServerIP = sharedPreferences.getString("API_SERVER", "");
            // URL de la API a la que deseas enviar la solicitud POST
            URL url = new URL("http://"+ServerIP+"/api/get-available-update");

            // Abre una conexión HTTP
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
//            connection.setRequestProperty("Content-Type", "application/json"); // Define el tipo de contenido

            // Cuerpo de la solicitud POST (datos que deseas enviar en JSON)

            // Habilita la escritura en la conexión
            connection.setDoOutput(true);
//            OutputStream os = connection.getOutputStream();
////            os.write(requestBody.getBytes("UTF-8"));
//            os.close();

            // Lee la respuesta de la API
            int responseCode = connection.getResponseCode();
            Log.d("Response Code", String.valueOf(responseCode));
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                Log.d("Response:",response.toString());
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            } else {
                // Handle error response
                Log.e("HTTP", "Error: " + responseCode);
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
        Log.d("RESULT",result);
        // Maneja la respuesta de la API aquí
        if (result != null) {
            Log.d("HTTP", "Respuesta: " + result);
            // Realiza acciones con la respuesta
        } else {
            Log.e("HTTP", "Error en la solicitud.");
            // Maneja el error
        }
    }
}
