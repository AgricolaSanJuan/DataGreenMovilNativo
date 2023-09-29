package com.example.datagreenmovil.ui.Settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsAboutViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SettingsAboutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is SettingsAbout fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}