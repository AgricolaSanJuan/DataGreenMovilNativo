package com.example.datagreenmovil.Entidades;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MiFila implements Serializable {
    private Integer Index;
    public Integer NroCeldas;
    public List<MiCelda> Data;

    public MiFila(Integer n) {
        this.NroCeldas = n;
        this.Data = new ArrayList<MiCelda>(n);
    }

    public MiFila(String[] nombresColumnas) {
        this.Data = new ArrayList<MiCelda>();
        int i=0;
        for(String nc : nombresColumnas){
            this.Data.add(new MiCelda(i,nc));
            i++;
        }
    }

    public MiFila(Integer index, String[] nombresColumnas, String[] valores) {
        this.Data = new ArrayList<MiCelda>();
        this.Index=index;
        this.NroCeldas=this.NroCeldas!=null?this.NroCeldas+1:1;
        for(int i=0; i<=nombresColumnas.length-1; i++){
            this.Data.add(new MiCelda(i,nombresColumnas[i],valores[i]));
        }
    }

    public String Item(Integer i){
        for(MiCelda mc : Data){
            if(mc.Index()==i){
                return mc.Valor();
            }
        }
        return null;
    }
/*
    public String Item(String nombreColumna){
        for(MiCelda mc : Data){
            if(mc.Nombre()==nombreColumna){
                return mc.Valor();
            }
        }
        return null;
    }*/

    public MiCelda Item(String nombreColumna){
        for(MiCelda mc : Data){
            if(mc.Nombre().equals(nombreColumna)){
                return mc;
            }
        }
        return null;
    }

    public int getIndex(){
        return Index;
    }


    //////////////
    /*
    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.Index);
        parcel.writeInt(this.NroCeldas);
        //parcel.writeParcelableList(this.Data,i);
        parcel.writeTypedList(this.Data);
    }

    protected MiFila(Parcel in) {
        this.Index=in.readInt();
        this.NroCeldas=in.readInt();
        this.Data=in.createTypedArrayList(MiCelda.CREATOR);
    }

    public static final Parcelable.Creator<MiFila> CREATOR = new Parcelable.Creator<MiFila>(){
        @Override
        public MiFila createFromParcel(Parcel source){
            return new MiFila(source);
        }

        @Override
        public MiFila[] newArray(int size){
            return new MiFila[size];
        }
    };*/

}
