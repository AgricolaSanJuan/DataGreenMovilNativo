package com.example.datagreenmovil;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datagreenmovil.Entidades.ConfiguracionLocal;

import java.util.List;

public class cls_04010000_Item_ListView extends RecyclerView.Adapter<cls_04010000_Item_ListView.MyViewHolder> {

    //    String data1[], data2[];
//    int images[];
    //String info[][];
    Context Context;
//    String[] ModulosPermitidos;
    List<String> modulos;
    ConfiguracionLocal objConfLocal;

    //int ImagenesModulos[];
    //String[] NombresModulos;

//    public MyAdapter(Context ct, String s1[], String s2[], int img[]){
//        context=ct;
//        data1=s1;
//        data2=s2;
//        images=img;
//    }
/*
    public cls_04010000_Item_ListView(Context ct, String mp[]){//, int[] img, String[] nm){
        Context =ct;
        //info=a;
        ModulosPermitidos =mp;
        //Toast.makeText(this,ModulosPermitidos[3],Toast.LENGTH_LONG).show();
        //ImagenesModulos=img;
        //NombresModulos = nm;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(Context);
        View view=inflater.inflate(R.layout.v_04010000_item_listview_014,parent,false);
        return new MyViewHolder(view);
    }

    @Override

    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txv_NombreModulo.setText(ModulosPermitidos[position]);

//        holder.rvTxvId.setText(data1[position]);
//        holder.rvTxvFecha.setText(data2[position]);
        //holder.rvTxvTurno.setText(data);
//        holder.myImage.setImageResource(images[position]);
        //holder.txv_NombreModulo.setText(info[0][position]);
//        for (String c: NodulosPermitidos) {
//            if (c.equals(String.valueOf(position))){
//                holder.txv_NombreModulo.setText(NombresModulos[position]);
//                //holder.img_ImagenModulo.setImageResource(ImagenesModulos[position]);
//            }
//        }

//        if (Arrays.asList(NombresModulos).indexOf(String.valueOf(position).toString()) >=0){
//
//        }

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String moduloSeleccionado = ModulosPermitidos[holder.getAdapterPosition()]; //ModulosPermitidos[position];
                switch (moduloSeleccionado){
                    case "Tareos":
                        //return getResources().getStringArray(R.array.JMERA);
                        Intent intent = new Intent(Context, cls_05010000_Edicion.class);
                        //CON PUTEXTRAS SE PUEDEN AGREGAR PARAMETROS AQUI PARA PASARLOS A LA ACTIVIDAD QUE SE VA A ABRRIR
                        Context.startActivity(intent);
                    default:
                        //return getResources().getStringArray(R.array.DEFAULT);
                }
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return ModulosPermitidos.length;
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {

        TextView txv_NombreModulo;
        //ImageView img_ImagenModulo;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txv_NombreModulo = itemView.findViewById(R.id.txv_NombreModulo_v);
            //img_ImagenModulo=itemView.findViewById(R.id.img_ImagenModulo_v);

            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
    */
    ///////////////////////////////////////////////////////////////////////////// 2.0

    public cls_04010000_Item_ListView(Context ct, List<String> l, ConfiguracionLocal cl){//, int[] img, String[] nm){
        Context = ct;
        modulos = l;
        objConfLocal= cl;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(Context);
        View view=inflater.inflate(R.layout.v_04010000_item_listview_014,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.c014_txv_NombreModulo.setText(modulos.get(position));
        //holder.c014_ivw_Icono.setImageDrawable(R.drawable.i_done);
        int IdDrawable = 0;
        switch (position){
            case 0 : IdDrawable = R.drawable.i_grass;
                break;
            case 1 : IdDrawable = R.drawable.i_barcode_scanner;
                break;
            case 2 : IdDrawable = R.drawable.i_forest;
                break;
            case 3 : IdDrawable = R.drawable.i_equalizer;
                break;
            case 4 : IdDrawable = R.drawable.i_airport_shuttle;
                break;
        }
        holder.c014_ivw_Icono.setImageDrawable(ResourcesCompat.getDrawable(Context.getResources(), IdDrawable ,null));
        holder.c014_mly_Principal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String moduloSeleccionado = modulos.get(holder.getAdapterPosition()); //ModulosPermitidos[position];
                Intent intent;
                switch (moduloSeleccionado){
                    case "Tareos":
                        //Intent intent = new Intent(Context, cls_05010000_Edicion.class);
                        intent = new Intent(Context, cls_05000000_Tareos.class);
                        //intent.putExtra("ConfiguracionLocal",objConfLocal);
                        //Context.startActivity(intent);
                        break;
                    case "Eficiencias":
                        //Intent intent2 = new Intent(Context, cls_06000000_Eficiencias.class);
                        intent = new Intent(Context, cls_06000000_Eficiencias.class);
                        //intent2.putExtra("ConfiguracionLocal",objConfLocal);
                        //Context.startActivity(intent2);
                        break;
                    case "Estandares":
                        //Intent intent2 = new Intent(Context, cls_06000000_Eficiencias.class);
                        intent = new Intent(Context, cls_07000000_Estandares.class);
                        break;
                    case "Servicios De Transporte":
                        //Intent intent2 = new Intent(Context, cls_06000000_Eficiencias.class);
                        intent = new Intent(Context, cls_08000000_ServiciosTransporte.class);
                        break;
                    default:
                        //return getResources().getStringArray(R.array.DEFAULT);
                        intent = new Intent();
                        break;
                }
                intent.putExtra("ConfiguracionLocal",objConfLocal);
                Context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return modulos.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView c014_txv_NombreModulo;
        ConstraintLayout c014_mly_Principal;
        ImageView c014_ivw_Icono;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            c014_txv_NombreModulo = itemView.findViewById(R.id.c014_txv_NombreModulo_v);
            c014_mly_Principal = itemView.findViewById(R.id.c014_mly_Principal_v);
            c014_ivw_Icono = itemView.findViewById(R.id.c014_ivw_Icono_v);
        }
    }

}

