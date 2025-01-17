package com.example.datagreenmovil.ui.estandares.EstandaresMain.Dialogs;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

public class DaoAdapter extends ArrayAdapter<DaoItem> {
    public DaoAdapter(@NonNull Context context, List<DaoItem> daoItems) {
        super(context, android.R.layout.simple_spinner_dropdown_item, daoItems);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // Puedes personalizar la vista aquí si lo deseas
        return super.getDropDownView(position, convertView, parent);
    }
}

