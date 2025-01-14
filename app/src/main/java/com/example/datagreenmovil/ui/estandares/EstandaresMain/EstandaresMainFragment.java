package com.example.datagreenmovil.ui.estandares.EstandaresMain;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.datagreenmovil.Conexiones.AppDatabase;
import com.example.datagreenmovil.DAO.Estandares.Adapters.EstandaresListAdapter;
import com.example.datagreenmovil.DAO.Estandares.ReporteEstandares.ReporteEstandares;
import com.example.datagreenmovil.DAO.Estandares.ReporteEstandares.ReporteEstandaresHelper;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.Utilidades.Filtros;
import com.example.datagreenmovil.databinding.FragmentEstandaresMainBinding;
import com.example.datagreenmovil.ui.estandares.EstandaresActivity;
import com.example.datagreenmovil.ui.estandares.EstandaresMain.Dialogs.DialogEstandaresForm;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class EstandaresMainFragment extends Fragment implements Filtros.GetFilterData/*, DialogEstandaresForm.SetAction*/ {

    ReporteEstandaresHelper reporteEstandaresHelper;
    GridLayoutManager gridLayoutManager;
    NavigationView navigationView;
    private FragmentEstandaresMainBinding binding;
    private Context ctx;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private AppDatabase database;
    private List<ReporteEstandares> reporteEstandaresList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEstandaresMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listarEstandares();


//        registerForContextMenu(binding.fabMenu);
        binding.fabMenu.setOnClickListener(v -> {
            showPopupMenu(binding.fabMenu);
        });
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

        return root;
    }

    private void listarEstandares() {
        //        TRAEMOS LA DATA PARA EL RECYCLER VIEW|
        reporteEstandaresList = reporteEstandaresHelper.getReporteEstandares();
//        DEFINIMOS EL FRID LAYOUT MANAGER PARA EL RECYCLER VIEW
        gridLayoutManager = new GridLayoutManager(ctx, 2);
        binding.rvEstandaresList.setLayoutManager(gridLayoutManager);
        EstandaresListAdapter estandaresListAdapter = new EstandaresListAdapter(reporteEstandaresList);
        binding.rvEstandaresList.setAdapter(estandaresListAdapter);
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

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        if (v.getId() == R.id.fabMenu) {
//            MenuInflater inflater = getActivity().getMenuInflater();
//            inflater.inflate(R.menu.context_menu_estandares, menu);
//        }
//    }
//
//    @Override
//    public boolean onContextItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_filter) {
//            // Handle filter action
//            // You can potentially open your filtrosFragment here
//            return true;
//        } else if (id == R.id.action_sort) {
//            // Handle sort action
//            // Implement your sorting logic here
//            return true;
//        }
//        return super.onContextItemSelected(item);
//    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.context_menu_estandares, popupMenu.getMenu()); // Utiliza el mismo archivo de menú

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                EstandaresActivity estandaresActivity = (EstandaresActivity) getActivity();
                if (estandaresActivity != null) {
                    // Maneja los clics en los elementos del menú
                    int id = item.getItemId();
                    if (id == R.id.action_add) {
                        DialogEstandaresForm dialogEstandaresForm = new DialogEstandaresForm(ctx, true, true, true,
                                new DialogEstandaresForm.SetAction() {
                                    @Override
                                    public void setPositiveAction() {
                                        Swal.error(ctx, "POSITIVO", "A ALCALOIDE DE COCAINA, TAS ARRESTAO MI KING 👮‍♂️🚓🚨", 8000);
                                    }

                                    @Override
                                    public void setNegativeAction() {
                                        Swal.success(ctx, "BIEN MANO", "LLENAS DE ORGULLO A TU NACIÓN 👮‍♂️👌", 80000);
                                    }

                                    @Override
                                    public void setNeutralAction() {
                                        Swal.success(ctx, "UHMMMM", "MEJOR NO TE REVISO MANO, QUIERO ESTAR TRANQUILO 👮‍🙂‍↔️", 80000);
                                    }
                                }
                        );
                        dialogEstandaresForm.show(getChildFragmentManager(), "DialogEstandaresForm");
                        return true;
                    } else if (id == R.id.action_delete) {
                        Swal.info(ctx, "PUSH!", "Presionaste eliminar", 5000);
                        return true;
                    } else if (id == R.id.action_transfer) {
                        Swal.info(ctx, "PUSH!", "Presionaste transferir", 5000);
                        return true;
                    } else if (id == R.id.action_select_all) {
                        Swal.info(ctx, "PUSH!", "Presionaste seleccionar todo", 5000);
                        return true;
                    } else if (id == R.id.action_duplicity) {
                        Swal.info(ctx, "PUSH!", "Presionaste duplicar", 5000);
                        return true;
                    } else if (id == R.id.action_settings) {
                        estandaresActivity.goTo(R.id.nav_estandares_settings);
                        return true;
                    }
                }
                return false;
            }
        });

        popupMenu.show();
    }
}
