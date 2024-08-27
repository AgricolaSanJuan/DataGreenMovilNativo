package com.example.datagreenmovil.ui.estandares.EstandaresRegistro;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EstandaresRegistroViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public EstandaresRegistroViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}