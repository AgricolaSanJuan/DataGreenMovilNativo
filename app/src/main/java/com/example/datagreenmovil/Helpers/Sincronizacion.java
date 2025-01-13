package com.example.datagreenmovil.Helpers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.datagreenmovil.Conexiones.AppDatabase;
import com.example.datagreenmovil.Conexiones.ConexionBD;
import com.example.datagreenmovil.DAO.Estandares.ConsumidoresTaller.ConsumidoresTaller;
import com.example.datagreenmovil.DAO.Estandares.ConsumidoresTaller.ConsumidoresTallerDAO;
import com.example.datagreenmovil.DAO.Estandares.Implemento.Implemento;
import com.example.datagreenmovil.DAO.Estandares.Implemento.ImplementoDAO;
import com.example.datagreenmovil.DAO.Estandares.Maquinaria.Maquinaria;
import com.example.datagreenmovil.DAO.Estandares.Maquinaria.MaquinariaDAO;
import com.example.datagreenmovil.DAO.Estandares.Operario.Operario;
import com.example.datagreenmovil.DAO.Estandares.Operario.OperarioDAO;
import com.example.datagreenmovil.Logica.Swal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Sincronizacion {

    public Context context;
    public AppDatabase database;

    public Sincronizacion(Context mContext) {
        context = mContext;
        database = AppDatabase.getDatabase();
    }

    public void sincronizarMaquinarias(){
//        MaquinariaDAO maquinariaDAO = database.maquinariaDAO();
        ConsumidoresTallerDAO consumidoresTallerDAO = database.consumidoresTallerDAO();
        String url = "http://56.10.3.24:8000/api/taller/get-maquinarias";
        List<ConsumidoresTaller> maquinariasList = new ArrayList<>();

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray responseTwo = response.getJSONArray("response");
                for (int i = 0; i < responseTwo.length(); i++){
                    JSONObject jsonObject = responseTwo.getJSONObject(i);

                    ConsumidoresTaller maquinaria = new ConsumidoresTaller();
                    Log.i("ITEM", jsonObject.toString());
                    maquinaria.setIdConsumidor(jsonObject.getString("IDCONSUMIDOR"));
                    maquinaria.setDescripcion(jsonObject.getString("DESCRIPCION"));
                    maquinaria.setTipo(jsonObject.getString("TIPO"));

                    maquinariasList.add(maquinaria);
                }

                consumidoresTallerDAO.sincronizarMaquinariasTaller(maquinariasList);
//                maquinariaDAO.sincronizarMaquinaria(maquinariasList);
            } catch (JSONException e) {
                Swal.error(context, "MAL", e.toString(), 8000);
            }
        }, error -> {
            Log.e("ERROR", error.toString());
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    public void sincronizarOperarios(){
        OperarioDAO operarioDAO = database.operarioDAO();
        String url = "http://56.10.3.24:8000/api/taller/get-operarios";
        List<Operario> operarioList = new ArrayList<>();

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray responseTwo = response.getJSONArray("response");
                for (int i = 0; i < responseTwo.length(); i++){
                    JSONObject jsonObject = responseTwo.getJSONObject(i);

                    Operario operario = new Operario();
//                    Log.i("ITEM", jsonObject.toString());
                    operario.setIdOperario(jsonObject.getString("IDOPERARIO"));
                    operario.setNombreCompleto(jsonObject.getString("NombreCompleto"));
                    operario.setNroDocumento(jsonObject.getString("NRODOCUMENTO"));

                    operarioList.add(operario);
                }

                operarioDAO.sincronizarOperario(operarioList);
            } catch (JSONException e) {
                Swal.error(context, "MAL", e.toString(), 8000);
            }
        }, error -> {
            Log.e("ERROR", error.toString());
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    public void sincronizarImplementos(){
//        ImplementoDAO implementoDAO = database.implementoDAO();
        ConsumidoresTallerDAO consumidoresTallerDAO = database.consumidoresTallerDAO();
        String url = "http://56.10.3.24:8000/api/taller/get-implementos";
//        List<Implemento> implementoList = new ArrayList<>();
        List<ConsumidoresTaller> implementoList = new ArrayList<>();

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray responseTwo = response.getJSONArray("response");
                for (int i = 0; i < responseTwo.length(); i++){
                    JSONObject jsonObject = responseTwo.getJSONObject(i);
                    Log.i("ITEM", jsonObject.toString());
                    ConsumidoresTaller implemento = new ConsumidoresTaller();
                    implemento.setIdConsumidor(jsonObject.getString("IDCONSUMIDOR"));
                    implemento.setDescripcion(jsonObject.getString("DESCRIPCION"));
                    implemento.setTipo(jsonObject.getString("TIPO"));

                    implementoList.add(implemento);
                }

                consumidoresTallerDAO.sincronizarImplementosTaller(implementoList);
            } catch (JSONException e) {
                Swal.error(context, "MAL", e.toString(), 8000);
            }
        }, error -> {
            Log.e("ERROR", error.toString());
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    public void sincronizarCombustibles(){
        ConsumidoresTallerDAO consumidoresTallerDAO = database.consumidoresTallerDAO();
        String url = "http://56.10.3.24:8000/api/taller/get-combustibles";
        List<ConsumidoresTaller> combustiblesList = new ArrayList<>();

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray responseTwo = response.getJSONArray("response");
                for (int i = 0; i < responseTwo.length(); i++){
                    JSONObject jsonObject = responseTwo.getJSONObject(i);
                    Log.i("RESPONSE", jsonObject.toString());
                    ConsumidoresTaller combustible = new ConsumidoresTaller();
                    combustible.setIdConsumidor(jsonObject.getString("IDCONSUMIDOR"));
                    combustible.setDescripcion(jsonObject.getString("DESCRIPCION"));
                    combustible.setTipo(jsonObject.getString("TIPO"));

                    combustiblesList.add(combustible);
                }

                consumidoresTallerDAO.sincronizarCombustiblesTaller(combustiblesList);
            } catch (JSONException e) {
                Swal.error(context, "MAL", e.toString(), 8000);
            }
        }, error -> {
            Log.e("ERROR", error.toString());
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

}
