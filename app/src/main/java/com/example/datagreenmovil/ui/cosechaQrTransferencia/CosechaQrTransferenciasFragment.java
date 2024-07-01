package com.example.datagreenmovil.ui.cosechaQrTransferencia;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;

import com.example.datagreenmovil.CosechaActivity;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.QRHelper.QRHelper;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.databinding.FragmentCosechaQrTransferenciasBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.WriterException;

import org.json.JSONArray;
import org.json.JSONException;

public class CosechaQrTransferenciasFragment extends Fragment {

    private FragmentCosechaQrTransferenciasBinding binding;
    private Context ctx;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CosechaQrTransferenciasViewModel homeViewModel =
                new ViewModelProvider(this).get(CosechaQrTransferenciasViewModel.class);

        Bundle parametros = getArguments();

        binding = FragmentCosechaQrTransferenciasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        String content = "[]";
        if(parametros != null) {
            content = parametros.getString("trabajadores");
            Log.i("DEBUG", content);
        }
        new QRHelper(bitmap -> {
            // Aquí puedes manejar el bitmap generado, por ejemplo, mostrarlo en un ImageView
            if (bitmap != null) {
                binding.imageQR.setImageBitmap(bitmap);
                binding.pbQr.setVisibility(View.GONE);
            } else {
                Swal.error(ctx, "Oops!", "La generación del código QR ha tenido un error", 4000);
            }
        }).execute(content);

        binding.buttonScan.setOnClickListener(view -> {
            navigateToConductorFragment();
        });

        return root;
    }

    public void navigateToConductorFragment() {
        CosechaActivity cosechaActivity = (CosechaActivity) getActivity();
        if (cosechaActivity.obtenerNavigationController() != null) {
            NavController nc = cosechaActivity.obtenerNavigationController();
            nc.navigate(R.id.nav_cosecha_conductor,
                    null,
                    new NavOptions.Builder().setPopUpTo(R.id.nav_cosecha_transferencia, true).build());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        ctx = context;
        super.onAttach(context);
    }
}