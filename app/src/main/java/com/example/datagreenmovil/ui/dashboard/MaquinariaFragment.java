package com.example.datagreenmovil.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.datagreenmovil.DAO.Estandares.TrxDParteMaquinaria.TrxDParteMaquinaria;
import com.example.datagreenmovil.NewTallerActivity;
import com.example.datagreenmovil.databinding.FragmentMaquinariaBinding;
import com.example.datagreenmovil.databinding.FragmentParronBinding;
import com.example.datagreenmovil.ui.notifications.Parron;

import java.util.ArrayList;
import java.util.List;

public class MaquinariaFragment extends Fragment {

    private FragmentMaquinariaBinding binding;

    private NewTallerActivity activity;
    private List<TrxDParteMaquinaria> listParteMaquinaria=new ArrayList<>();
    private String idParteMaquinariaActual ="";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Parron notificationsViewModel =
                new ViewModelProvider(this).get(Parron.class);
        activity=(NewTallerActivity) getActivity();

        if(activity !=null){
            listParteMaquinaria=activity.listaDParteMaquinaria;
            idParteMaquinariaActual=activity.getIdParteMaquinaria();
        }


        binding = FragmentMaquinariaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}