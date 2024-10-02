package com.example.datagreenmovil.Entidades;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import org.threeten.bp.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

//TODO: PENDIENTE IMPLEMENTAR ESTA MIERDA EN TODO EL PROYECTO COMO CLASE GENERICA DE MANEJO DE REGISTROS Y DETALLES DE REGISTROS
public class Registro extends HashMap<String,Object> {

	public List<Registro> Detalle;

	public Registro(){
		this.Detalle = new List<Registro>() {
			@Override
			public int size() {
				return 0;
			}

			@Override
			public boolean isEmpty() {
				return false;
			}

			@Override
			public boolean contains(@Nullable Object o) {
				return false;
			}

			@NonNull
			@Override
			public Iterator<Registro> iterator() {
				return null;
			}

			@NonNull
			@Override
			public Object[] toArray() {
				return new Object[0];
			}

			@NonNull
			@Override
			public <T> T[] toArray(@NonNull T[] ts) {
				return null;
			}

			@Override
			public boolean add(Registro registro) {
				return false;
			}

			@Override
			public boolean remove(@Nullable Object o) {
				return false;
			}

			@Override
			public boolean containsAll(@NonNull Collection<?> collection) {
				return false;
			}

			@Override
			public boolean addAll(@NonNull Collection<? extends Registro> collection) {
				return false;
			}

			@Override
			public boolean addAll(int i, @NonNull Collection<? extends Registro> collection) {
				return false;
			}

			@Override
			public boolean removeAll(@NonNull Collection<?> collection) {
				return false;
			}

			@Override
			public boolean retainAll(@NonNull Collection<?> collection) {
				return false;
			}

			@Override
			public void clear() {

			}

			@Override
			public Registro get(int i) {
				return null;
			}

			@Override
			public Registro set(int i, Registro registro) {
				return null;
			}

			@Override
			public void add(int i, Registro registro) {

			}

			@Override
			public Registro remove(int i) {
				return null;
			}

			@Override
			public int indexOf(@Nullable Object o) {
				return 0;
			}

			@Override
			public int lastIndexOf(@Nullable Object o) {
				return 0;
			}

			@NonNull
			@Override
			public ListIterator<Registro> listIterator() {
				return null;
			}

			@NonNull
			@Override
			public ListIterator<Registro> listIterator(int i) {
				return null;
			}

			@NonNull
			@Override
			public List<Registro> subList(int i, int i1) {
				return null;
			}

		};
	}

	public ArrayList<String> valores(){
		ArrayList<String> r = new ArrayList<>();
		for (HashMap.Entry<String, Object> e : this.entrySet() ) {
			r.add(e.getValue().toString());
		}
		return r;
	}

	public String g(String clave){
		if (this.containsKey(clave)){
			return Objects.requireNonNull(this.get(clave)).toString();
		}
		return "!"+clave;
	}

	public void s(String clave, String valor) {
	}
}
