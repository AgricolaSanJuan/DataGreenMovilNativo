package com.example.datagreenmovil.ui.TareosMain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TareosMainViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TareosMainViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is tareos main fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}