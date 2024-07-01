package com.example.datagreenmovil.Popup;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.databinding.FragmentPopupConfirmacionEscaneoBinding;

import org.json.JSONException;

public class PopupConfirmacionEscaneo extends Fragment {
    private Context ctx;
    private FragmentPopupConfirmacionEscaneoBinding binding;

    public PopupConfirmacionEscaneo(Context context) {
        ctx = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("PopupConfirmacionEscaneo", "onCreateView() ejecutado");
        binding = FragmentPopupConfirmacionEscaneoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        CountDownTimer countDownTimer = new CountDownTimer(1000,1000) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() {
                binding.cosechaConfirmacionView.resume();
            }
        };
        countDownTimer.start();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ctx = context;
        Log.i("FINO", "visual");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}