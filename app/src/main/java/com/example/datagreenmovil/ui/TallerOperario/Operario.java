package com.example.datagreenmovil.ui.TallerOperario;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Operario extends ViewModel {

    private final MutableLiveData<String> mText;

    public Operario() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}