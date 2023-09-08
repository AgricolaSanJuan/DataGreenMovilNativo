package com.example.datagreenmovil.Logica;

//import android.app.AlertDialog;
//import android.app.DialogFragment;
//import android.app.DialogFragment;
import android.app.Dialog;
import android.content.Context;
//import android.content.DialogInterface;
//import android.content.DialogInterface;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.datagreenmovil.R;

//ESTA CLASE NO ESTA USADA POR NADIE PERO NO LA VOY A BORRAR PORQUE PODRÍA USARSE DE PLANTILLA A FUTURO,
//CLASE QUE CREA EL DIALOG FRAGMENT QUE RETORNA UN EVENTO A LA ACTIVIDAD PADRE
//ESTÁ PENDIENTE QUE RESPONDA A CONTROLES CUSTOMIZADOS DEL LAYOUT
public class NoticeDialogFragment extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
        public void btnAceptarPresionado(DialogFragment dialog, String s);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener listener;


    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.popup_actualizar_detalle_tareo_021, null))
                // Add action buttons
                .setPositiveButton("boton si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        listener.onDialogPositiveClick(NoticeDialogFragment.this);
                    }
                })
                .setNegativeButton("boton no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        listener.onDialogNegativeClick(NoticeDialogFragment.this);
                    }
                });
//        Button c021_btn_Aceptar = builder. .findViewById(R.id.c021_btn_Aceptar_v); //continuar aqui
//////        Button c021_btn_Aceptar =  .findViewById(R.id.c021_btn_Aceptar_v);
//        c021_btn_Aceptar.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
////                detalle.setHoras(Double.parseDouble(c021_etx_Horas.getText().toString()));
////                detalle.setRdtos(Double.parseDouble(c021_etx_Rdtos.getText().toString()));
////                detalle.setObservacion(c021_etx_Observacion.getText().toString());
////                try {
////                    detalle.guardar(objSqlite);
////                } catch (Exception e) {
////                    throw new RuntimeException(e);
////                }
////                popUp.dismiss();
////                listener.btnAceptarPresionado(NoticeDialogFragment.this, "todo bien.");
////                dismiss();
//            }
//        });
        return builder.create();

//        builder.setMessage("este es un mnensaje de prueba")
//                .setPositiveButton("boton si", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // Send the positive button event back to the host activity
//                        listener.onDialogPositiveClick(NoticeDialogFragment.this);
//                    }
//                })
//                .setNegativeButton("boton no", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // Send the negative button event back to the host activity
//                        listener.onDialogNegativeClick(NoticeDialogFragment.this);
//                    }
//                });
//        return builder.create();
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout to use as dialog or embedded fragment
//        View view = container.getFocusedChild();
//        super.onViewCreated(view, savedInstanceState);
////        btnInvia = view.findViewById(R.id.btnInvia)
//        Button c021_btn_Aceptar = view.findViewById(R.id.c021_btn_Aceptar_v);
//        c021_btn_Aceptar.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
////                detalle.setHoras(Double.parseDouble(c021_etx_Horas.getText().toString()));
////                detalle.setRdtos(Double.parseDouble(c021_etx_Rdtos.getText().toString()));
////                detalle.setObservacion(c021_etx_Observacion.getText().toString());
////                try {
////                    detalle.guardar(objSqlite);
////                } catch (Exception e) {
////                    throw new RuntimeException(e);
////                }
////                popUp.dismiss();
//                listener.btnAceptarPresionado(NoticeDialogFragment.this, "todo bien.");
//            }
//        });
//        return inflater.inflate(R.layout.v_popup_actualizar_detalle_tareo_021, container, false);
//    }
}