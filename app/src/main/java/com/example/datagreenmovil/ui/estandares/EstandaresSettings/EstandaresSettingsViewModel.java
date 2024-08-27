package com.example.datagreenmovil.ui.estandares.EstandaresSettings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EstandaresSettingsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public EstandaresSettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}