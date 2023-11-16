package com.example.datagreenmovil.ui.TallerMain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TallerMainViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TallerMainViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}