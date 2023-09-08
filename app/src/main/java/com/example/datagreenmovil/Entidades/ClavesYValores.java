package com.example.datagreenmovil.Entidades;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClavesYValores extends LinkedHashMap<String, LinkedHashMap<String, String>> {
    public ClavesYValores(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public ClavesYValores(int initialCapacity) {
        super(initialCapacity);
    }

    public ClavesYValores() {
    }

    public ClavesYValores(Map<? extends String, ? extends LinkedHashMap<String, String>> m) {
        super(m);
    }

    public ClavesYValores(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    public String[] obtenerClaves(String n){
        LinkedHashMap lhm = this.get(n);
        if(lhm!=null){
            String [] r = new String[lhm.size()];
            Set<String> k =lhm.keySet();
            List<String> l = new ArrayList<String>(k);
            for (int i=0; i<lhm.size(); i++){
                r[i]=l.get(i);
            }
            return r;
        }
        return null;
    }
}
