package com.example.datagreenmovil.ui.SettingsLocal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsLocalViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SettingsLocalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is SettingsLocalViewModel fragment");
    }



    public LiveData<String> getText() {
        return mText;
    }
}