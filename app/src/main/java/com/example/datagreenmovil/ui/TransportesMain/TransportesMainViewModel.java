package com.example.datagreenmovil.ui.TransportesMain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TransportesMainViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TransportesMainViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}