package com.example.datagreenmovil.ui.TareosSettings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TareosSettingsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TareosSettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}