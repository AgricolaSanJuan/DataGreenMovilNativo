package com.example.datagreenmovil.ui.TransportesSettings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TransportesSettingsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TransportesSettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}