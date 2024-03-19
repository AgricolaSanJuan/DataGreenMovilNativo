package com.example.datagreenmovil.ui.SettingsAbout;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.evrencoskun.tableview.TableView;
import com.example.datagreenmovil.Logica.Swal;
import com.example.datagreenmovil.databinding.FragmentSettingsAboutBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SettingsAboutFragment extends Fragment {
    Context ctx;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    SQLiteDatabase database;
//    TableView tableView;

    private List<RowHeader> mRowHeaderList;
    private List<ColumnHeader> mColumnHeaderList;
    private List<List<Cell>> mCellList;

    private FragmentSettingsAboutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        mRowHeaderList = new ArrayList<>();
        mColumnHeaderList = new ArrayList<>();
        mCellList = new ArrayList<>();


        SettingsAboutViewModel settingsAboutViewModel =
                new ViewModelProvider(this).get(SettingsAboutViewModel.class);

        binding = FragmentSettingsAboutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.switchTrabajadoresDesconocidos.setOnCheckedChangeListener((compoundButton, b) -> {
            editor.putBoolean("PERMITIR_TRABAJADORES_DESCONOCIDOS", b).apply();
            if(b){
                Swal.success(ctx, "CAMBIO CORRECTO!", "Ahora se permitirán los trabajadores que no están registrados en la base de datos local.", 3000);
            }else{
                Swal.warning(ctx, "CAMBIO CORRECTO!", "Ahora no se permitirán los trabajadores que no están registrados en la base de datos local.", 3000);
            }
        });

        binding.fabStartQuery.setOnClickListener(view -> {
//            if (!binding.textQuery.getText().toString().equals("")) {

                // Create our custom TableView Adapter
                TableView tableView = new TableView(getContext());
                ResultsSQLiteAdapter adapter = new ResultsSQLiteAdapter(ctx);
                try {

                    Cursor c;
                    String[] columns;
                    c = database.rawQuery(binding.textQuery.getText().toString(), null);
                    c.moveToFirst();
                    columns = c.getColumnNames();

                    // Suponiendo que tienes estos datos
                    List<String> columnNames = new ArrayList<>();
                    List<String> rowNames = new ArrayList<>();

                    List<List<String>> cellData = new ArrayList<>();
                    c.moveToFirst();
                    for(int i = 0; i < c.getCount(); i++) {
                        List<String> rowDatos = new ArrayList<>();
                        for(int j = 0 ; j < columns.length; j++){
                            rowDatos.add(c.getString(j));
                        }
                        cellData.add(rowDatos);
                        c.moveToNext();
                    }

                    Collections.addAll(columnNames, columns);

                    // Limpiar las listas si es necesario
                    mColumnHeaderList.clear();
                    mRowHeaderList.clear();
                    mCellList.clear();

                    // Agregar columnas
                    for (String columnName : columnNames) {
                        ColumnHeader columnHeader = new ColumnHeader(columnName);
                        mColumnHeaderList.add(columnHeader);
                    }

                    c.moveToFirst();
                    int posId = retornarPosId(mColumnHeaderList);
                    for( int i = 0 ; i < c.getCount(); i++){
                        rowNames.add(c.getString(posId));
                        c.moveToNext();
                    }

                    // Agregar filas
                    for (String rowName : rowNames) {
                        RowHeader rowHeader = new RowHeader(rowName);
                        mRowHeaderList.add(rowHeader);
                    }

                    // Agregar celdas
                    for (List<String> rowData : cellData) {
                        List<Cell> cellRow = new ArrayList<>();
                        for (String cellValue : rowData) {
                            Cell cell = new Cell(cellValue);
                            cellRow.add(cell);
                        }
                        mCellList.add(cellRow);
                    }

                    tableView.setAdapter(adapter);
                    tableView.setIgnoreSelectionColors(false);
                    binding.contentContainer.setAdapter(adapter);
                    adapter.setAllItems(mColumnHeaderList, mRowHeaderList, mCellList);

                    c.close();
                }catch (Exception e){
                    Log.e("ERROR", e.toString());
                    Swal.info(ctx, "Error", e.toString(), 5500);
                }

//                Set this adapter to the our TableView
//
//                Cursor c;
//                String[] columnNames;
//                c = database.rawQuery(binding.textQuery.getText().toString(), null);
//                c.moveToFirst();
//                columnNames = c.getColumnNames();
//
//                int cantidad = columnNames.length;
//                StringBuilder resultBuilder = new StringBuilder();
//                // Construye la primera fila con los nombres de las columnas
//                for (String columnName : columnNames) {
//                    resultBuilder.append(columnName).append("\t");
//                }
//                resultBuilder.append("\n");
//                // Itera sobre los resultados y construye las filas de la tabla
//                while (!c.isAfterLast()) {
//                    for (int i = 0; i < cantidad; i++) {
//                        resultBuilder.append(c.getString(i)).append("\t");
//                    }
//                    resultBuilder.append("\n");
//                    c.moveToNext();
//                }
//                // Muestra la tabla en el TextView
////                binding.textResult.setText(resultBuilder.toString());
//                // Cierra el cursor después de su uso
//                c.close();
//            }
        });

//        final TextView textView = binding.textGallery;
//        settingsAboutViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    public int retornarPosId(List<ColumnHeader> mColumnHeaderList){
        for (int i = 0; i < mColumnHeaderList.size(); i++){
            ColumnHeader columnHeader = mColumnHeaderList.get(i);
            if(columnHeader.getData().toString().equalsIgnoreCase("id")){
                return i;
            }
        }
        return 1;
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
        database = SQLiteDatabase.openDatabase(ctx.getDatabasePath("DataGreenMovil.db").toString(), null, SQLiteDatabase.OPEN_READWRITE);
        sharedPreferences = ctx.getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
}