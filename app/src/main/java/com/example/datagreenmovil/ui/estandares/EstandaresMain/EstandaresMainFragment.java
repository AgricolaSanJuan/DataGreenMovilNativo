package com.example.datagreenmovil.ui.estandares.EstandaresMain;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.datagreenmovil.Conexiones.AppDatabase;
import com.example.datagreenmovil.DAO.Estandares.Adapters.EstandaresListAdapter;
import com.example.datagreenmovil.DAO.Estandares.ReporteEstandares.ReporteEstandares;
import com.example.datagreenmovil.DAO.Estandares.ReporteEstandares.ReporteEstandaresHelper;
import com.example.datagreenmovil.DAO.Estandares.TrxEstandares.TrxEstandaresNew;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.Utilidades.AppInfo;
import com.example.datagreenmovil.Utilidades.Filtros;
import com.example.datagreenmovil.databinding.FragmentEstandaresMainBinding;

import java.util.List;

public class EstandaresMainFragment extends Fragment implements Filtros.GetFilterData {

    ReporteEstandaresHelper reporteEstandaresHelper;
    GridLayoutManager gridLayoutManager;
    private FragmentEstandaresMainBinding binding;
    private Context ctx;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private AppDatabase database;
    private List<ReporteEstandares> reporteEstandaresList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEstandaresMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        TRAEMOS LA DATA PARA EL RECYCLER VIEW|
        reporteEstandaresList = reporteEstandaresHelper.getReporteEstandares();
//        DEFINIMOS EL FRID LAYOUT MANAGER PARA EL RECYCLER VIEW
        gridLayoutManager = new GridLayoutManager(ctx, 2);
        binding.rvEstandaresList.setLayoutManager(gridLayoutManager);
        EstandaresListAdapter estandaresListAdapter = new EstandaresListAdapter(reporteEstandaresList);
        binding.rvEstandaresList.setAdapter(estandaresListAdapter);

        // Crear una instancia del fragmento
        Filtros filtrosFragment = new Filtros();
        AppInfo appInfo = new AppInfo();

        // Establecer el callback
        filtrosFragment.setFilterDataCallback(this);

        // Obtener el FragmentManager y comenzar una transacción
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Añadir o reemplazar el fragmento en el contenedor
        fragmentTransaction.replace(R.id.container_filtros, filtrosFragment);
//        fragmentTransaction.commit();  // Confirmar la transacción
        fragmentTransaction.replace(R.id.container_info, appInfo);
        fragmentTransaction.commit();  // Confirmar la transacción

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
        database = AppDatabase.getDatabase();
        reporteEstandaresHelper = new ReporteEstandaresHelper(context);
    }

    @Override
    public void onChangeFilterData(Swal.DialogResult filterData) {

    }
}
