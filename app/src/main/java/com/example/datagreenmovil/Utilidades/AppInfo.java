package com.example.datagreenmovil.Utilidades;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.datagreenmovil.databinding.AppInfoBinding;

public class AppInfo extends Fragment {
    AppInfoBinding binding;
    SharedPreferences sharedPreferences;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = AppInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String appname = sharedPreferences.getString("NOMBRE_APP","!NOMBRE_APP");

        binding.txvAppName.setText(appname);

        return root;
    }

    void getAppVersion(){
        String appVersion = "";
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
        super.onAttach(context);
    }
}