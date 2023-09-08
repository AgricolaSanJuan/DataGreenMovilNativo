package com.example.datagreenmovil.Entidades;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

@SuppressLint("ValidFragment")
public class Mensaje extends DialogFragment {
    String mensaje, botonIzquierdo, botonDerecho;

    @SuppressLint("ValidFragment")
    public Mensaje(String textoMensaje, String textoBotonIzquierdo, String textoBotonDerecho){
        this.mensaje=textoMensaje;
        this.botonIzquierdo=textoBotonIzquierdo;
        this.botonDerecho=textoBotonDerecho;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mensaje)
                .setPositiveButton(botonDerecho, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // START THE GAME!
                    }
                }).setNegativeButton(botonIzquierdo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}