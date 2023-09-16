import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyHttpTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            // URL de la API a la que deseas enviar la solicitud POST
            URL url = new URL("https://tu.url/api/");

            // Abre una conexión HTTP
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json"); // Define el tipo de contenido

            // Cuerpo de la solicitud POST (datos que deseas enviar en JSON)
            String requestBody = "{\"key1\": \"value1\", \"key2\": \"value2\"}";

            // Habilita la escritura en la conexión
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(requestBody.getBytes("UTF-8"));
            os.close();

            // Lee la respuesta de la API
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
