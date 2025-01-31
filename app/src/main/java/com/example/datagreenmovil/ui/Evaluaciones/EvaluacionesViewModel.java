package com.example.datagreenmovil.ui.Evaluaciones;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EvaluacionesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public EvaluacionesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}