package com.example.datagreenmovil.ui.SettingsSync;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsSyncViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SettingsSyncViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is SettingsSync fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}