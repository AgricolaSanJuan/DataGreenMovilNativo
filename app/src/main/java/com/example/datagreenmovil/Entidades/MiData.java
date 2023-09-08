package com.example.datagreenmovil.Entidades;

import android.database.Cursor;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.service.autofill.FillEventHistory;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MiData implements Serializable {
    private int Index;
    private String Nombre;
    public Integer NroFilas;
    private Integer NroColumnas;
    public List<MiFila> Filas;

    public MiData() {
        this.Index=0;
        this.Nombre="";
        this.NroFilas=0;
        this.NroColumnas=0;
        this.Filas=new ArrayList<>();
    }

    public MiData(Cursor c) {
        this.Index=0;
        this.Nombre="";
        //c.moveToLast();
        this.NroFilas=c.getCount();
        this.NroColumnas=c.getColumnCount();
        this.Filas=new ArrayList<>();
        while (c.moveToNext()){
            String[] nombresColumnas=new String[c.getColumnCount()];
            String[] valores=new String[c.getColumnCount()];
            for(int i=0; i<c.getColumnCount(); i++){
                nombresColumnas[i]=c.getColumnName(i);
                valores[i]=c.getString(i);
            }
            this.Filas.add(new MiFila(c.getPosition(),nombresColumnas,valores));
        }
    }

    public MiData(ResultSet rs) {
        try{
            this.Index=0;
            this.Nombre="";
//            rs.last();
//            this.NroFilas=rs.getRow();
//            rs.beforeFirst();
            ResultSetMetaData rsmdAux=rs.getMetaData();
            this.NroColumnas=rsmdAux.getColumnCount();

            this.Filas=new ArrayList<>();
            while (rs.next()){
                String[] nombresColumnas=new String[rsmdAux.getColumnCount()];
                String[] valores=new String[rsmdAux.getColumnCount()];
                for(int i=0; i<rsmdAux.getColumnCount(); i++){
                    nombresColumnas[i]=rsmdAux.getColumnName(i+1);
                    valores[i]=rs.getString(i+1);
                }
                this.Filas.add(new MiFila(this.Filas.size(),nombresColumnas,valores));
            }
            this.NroFilas=this.Filas.size();
        }catch (Exception ex){
            this.Filas =null;
            //throw new Exception(ex.getMessage());
        }
    }

    public MiFila Filas(int n){
        for(MiFila f: Filas){
            if(f.getIndex()==n){
                return f;
            }
        }
        return null;
    }

    public int encontrar(String valor, int c){
        //if(Filas)
        if (Filas != null){
            for(MiFila f: Filas){
                if(f.Item(c) != null && f.Item(c).equals(valor)){
                    return f.getIndex();
                }
            }
        }
        return -1;
    }

    public void insertar(MiFila f){
        Filas.add(f);
        this.NroFilas=this.NroFilas!=null?this.NroFilas+1:1;
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
        parcel.writeString(this.Nombre);
        parcel.writeInt(this.NroFilas);
        parcel.writeInt(this.NroColumnas);
        //parcel.writeParcelableList(this.Filas, i);
        parcel.writeTypedList(this.Filas);
    }

    protected MiData(Parcel in) {
        this.Index=in.readInt();
        this.Nombre=in.readString();
        this.NroFilas=in.readInt();
        this.NroColumnas=in.readInt();
        this.Filas=in.createTypedArrayList(MiFila.CREATOR);
        //this.data=in.readParcelable(MiData.class.getClassLoader());
    }


    public static final Parcelable.Creator<MiData> CREATOR = new Parcelable.Creator<MiData>(){
        @Override
        public MiData createFromParcel(Parcel source){
            return new MiData(source);
        }

        @Override
        public MiData[] newArray(int size){
            return new MiData[size];
        }
    };*/
}
