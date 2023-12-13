package com.example.datagreenmovil.Entidades;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.core.content.res.ResourcesCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.datagreenmovil.R;

import java.util.List;
import java.util.Locale;

public class AdapatadorSpinner extends ArrayAdapter<ClaveValor> {

        private Context context;
        private ClaveValor[] cvs;
//        private ConfiguracionLocal ObjConfLocal;
        private int largo, columnaVisible;
        //private HashMap<String,String> hm;
        //Typeface fuente = Typeface.create("monospace"); //.createFromAsset(getContext().getAssets(),"monospace");
        //Typeface fuente = Typeface.createFromAsset(getContext().getAssets(),"monospace");
//        Typeface tf = Typeface.createFromAsset(context.getAssets(),"font/vmr.ttf");


        public AdapatadorSpinner(Context context, int textViewResourceId, ClaveValor[] cvs, ConfiguracionLocal cl, int v) {
            super(context, textViewResourceId, cvs);
            if(cvs.length==0){
                this.context=null;
                this.cvs=null;
                this.largo=0;
                this.columnaVisible=0;
            }else{
                this.context = context;
                this.cvs = cvs;
                int l = 0;
                if(cl!=null){
                    l = Integer.parseInt(cl.get("ANCHO_PANTALLA"));
                    l = (int) Math.floor(l / 24.00 * 0.76);
                }
                this.largo=l;
                this.columnaVisible= v;
            }
        }

//    public Adapa

        public int getCount(){
            return cvs.length;
        }

        public ClaveValor getItem(int position){
            return cvs[position];
        }

        public int getIndex(String valor){
            for(int i=0; i < cvs.length; i++){
                if(cvs[i].getValor().equals(valor)){
                    return i;
                }
            }
            return -1;
        }

        public long getItemId(int position){
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView label = new TextView(context);
            //String t=cvs[position].getClave().trim().replace(" ","") + " | " + cvs[position].getValor().trim();
            String t="";
            if(columnaVisible==0){
                t = cvs[position].getClave().trim();
            }else if(columnaVisible==1){
                t = cvs[position].getValor().trim();
            }else{
                t=cvs[position].getClave();
                t=t.trim() + " | " + cvs[position].getValor();
                t=t.substring(0, largo == 0 ? t.length(): Math.min(t.length(), largo));
            }
            label.setText(t);
            Typeface tf = ResourcesCompat.getFont(context, R.font.monoespaciada_principal); //Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);
            label.setTypeface(tf);
            label.setTextSize(18);
            label.setPadding(20,20,20,20);
            //label.setTextColor(Color.parseColor("#FF000000"));
            return label;
        }

        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            TextView label = new TextView(context);
            //String t=cvs[position].getClave().trim().replace(" ","") + " | " + cvs[position].getValor().trim();
            String t="";
            if(columnaVisible==0){
                t = cvs[position].getClave().trim();
            }else if(columnaVisible==1){
                t = cvs[position].getValor().trim();
            }else{
                t=cvs[position].getClave();
                t=t+" | "+cvs[position].getValor();
            }
            label.setText(t);
            Typeface tf = ResourcesCompat.getFont(context, R.font.monoespaciada_principal); //Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);
            label.setTypeface(tf);
            label.setTextSize(24);
            label.setPadding(5,5,5,5);
            return label;
        }



//        public View getCustomView(int position, View convertView, ViewGroup parent){
//            //Toast.makeText(getContext(),"seleccionado.", Toast.LENGTH_LONG).show();
//            TextView label = new TextView(context);
//            label.setText(label.getText().toString().substring(25));
//            label.setTypeface(tf);
//            return label;
//        }
//
//        public View getCustomDropView


    }
