package com.example.datagreenmovil.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.datagreenmovil.DAO.Estandares.TrxDParteMaquinaria.TrxDParteMaquinaria;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> totalHoras = new MutableLiveData<>();
    private final MutableLiveData<String> observaciones = new MutableLiveData<>();
    private final MutableLiveData<String> maquinariaSeleccionada = new MutableLiveData<>();
    private final MutableLiveData<String> operarioSeleccionado = new MutableLiveData<>();
    private final MutableLiveData<String> turnoSeleccionado = new MutableLiveData<>();

    private final MutableLiveData<List<TrxDParteMaquinaria>> listaParteMaquinaria = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> idParteMaquinaria = new MutableLiveData<>("");

    public LiveData<String> getTotalHoras() { return totalHoras; }
    public void setTotalHoras(String value) { totalHoras.setValue(value); }

    public LiveData<String> getObservaciones() { return observaciones; }
    public void setObservaciones(String value) { observaciones.setValue(value); }

    public LiveData<String> getMaquinariaSeleccionada() { return maquinariaSeleccionada; }
    public void setMaquinariaSeleccionada(String value) { maquinariaSeleccionada.setValue(value); }

    public LiveData<String> getOperarioSeleccionado() { return operarioSeleccionado; }
    public void setOperarioSeleccionado(String value) { operarioSeleccionado.setValue(value); }

    public LiveData<String> getTurnoSeleccionado() { return turnoSeleccionado; }
    public void setTurnoSeleccionado(String value) { turnoSeleccionado.setValue(value); }

    // Nuevo método para obtener la lista de maquinaria
    public LiveData<List<TrxDParteMaquinaria>> getListaParteMaquinaria() {
        return listaParteMaquinaria;
    }

    public void setListaParteMaquinaria(List<TrxDParteMaquinaria> lista) {
        listaParteMaquinaria.setValue(lista);
    }

    // Nuevo método para obtener el ID de la parte de maquinaria
    public LiveData<String> getIdParteMaquinaria() {
        return idParteMaquinaria;
    }

    public void setIdParteMaquinaria(String id) {
        idParteMaquinaria.setValue(id);
    }

    // Método guardarDatos (vacío por ahora, depende de lo que necesites hacer)
    public void guardarDatos() {
        // Aquí puedes agregar la lógica de guardado en base de datos o en otro lugar
    }
}
