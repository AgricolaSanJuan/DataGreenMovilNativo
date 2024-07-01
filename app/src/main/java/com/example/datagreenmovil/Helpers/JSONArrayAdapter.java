package com.example.datagreenmovil.Helpers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONArrayAdapter extends ArrayAdapter<JSONObject> {

    private LayoutInflater mInflater;
    private int mResource;
    private String mKey; // El nombre del campo que deseas mostrar en el TextView

    public JSONArrayAdapter(Context context, int resource, JSONArray jsonArray, String key) {
        super(context, resource);
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        mKey = key;

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                add(jsonObject);
            } catch (JSONException e) {
                // En caso de una excepción, simplemente registramos un mensaje de error
                Log.e("JSONArrayAdapter", "Error al procesar el elemento " + i + " del JSONArray", e);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JSONObject jsonObject = getItem(position);
        if (jsonObject != null) {
            try {
                String value = jsonObject.getString(mKey);
                Log.i("ARRAYBUSQUEDA",jsonObject.getString(mKey));
                holder.textView.setText(value);
            } catch (JSONException e) {
                Log.i("AEA", e.toString());
                e.printStackTrace();
            }
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView textView;
    }
}
