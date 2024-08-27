package com.example.datagreenmovil.ui.estandares.EstandaresMain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EstandaresMainViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public EstandaresMainViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}