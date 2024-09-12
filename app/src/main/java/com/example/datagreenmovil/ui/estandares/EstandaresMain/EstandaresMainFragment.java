package com.example.datagreenmovil.ui.estandares.EstandaresMain;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.Utilidades.Filtros;
import com.example.datagreenmovil.databinding.FragmentEstandaresMainBinding;

public class EstandaresMainFragment extends Fragment implements Filtros.GetFilterData {

    private FragmentEstandaresMainBinding binding;
    private Context ctx;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEstandaresMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Crear una instancia del fragmento
        Filtros filtrosFragment = new Filtros();

        // Establecer el callback
        filtrosFragment.setFilterDataCallback(this);

        // Obtener el FragmentManager y comenzar una transacción
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Añadir o reemplazar el fragmento en el contenedor
        fragmentTransaction.replace(R.id.container_filtros, filtrosFragment);
        fragmentTransaction.commit();  // Confirmar la transacción

        binding.btnCrear.setOnClickListener(v -> {

        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ctx = context;
        sharedPreferences = ctx.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    public void onChangeFilterData(Swal.DialogResult filterData) {

    }
}
