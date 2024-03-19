package com.example.datagreenmovil.ui.SettingsAbout;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.icu.text.IDNA;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import com.example.datagreenmovil.R;

public class ResultsSQLiteAdapter extends AbstractTableAdapter<ColumnHeader, RowHeader, Cell> {

    private final Context context;
    private OnCellClickListener onCellClickListener;

    public ResultsSQLiteAdapter(Context ctx) {
        context = ctx;
    }

    class CellViewHolder extends AbstractViewHolder {
        final LinearLayout cell_container;
        final TextView cell_textview;

        public CellViewHolder(View itemView){
            super(itemView);
            cell_container = itemView.findViewById(R.id.cell_container);
            cell_textview = itemView.findViewById(R.id.cell_data);
        }
    }
    @NonNull
    @Override
    public AbstractViewHolder onCreateCellViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_view_cell_layout, parent, false);
        return new CellViewHolder(layout);
    }
    public interface OnCellClickListener {
        void onCellClick(String columnHeader, String cellValue);
    }
    public void setOnCellClickListener(OnCellClickListener listener) {
        this.onCellClickListener = listener;
    }

    @Override
    public void onBindCellViewHolder(@NonNull AbstractViewHolder holder, @Nullable Cell cellItemModel, int columnPosition, int rowPosition) {
        Cell cell = (Cell) cellItemModel;
        CellViewHolder viewHolder = (CellViewHolder) holder;
        viewHolder.cell_textview.setText(cell.getData().toString());
        viewHolder.cell_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        viewHolder.cell_container.requestLayout();
    }
    class ColumnHeaderViewHolder extends AbstractViewHolder{
        final LinearLayout column_header_container;
        final TextView cell_textview;

        public ColumnHeaderViewHolder(View itemView){
            super(itemView);
            column_header_container = itemView.findViewById(R.id.column_header_container);
            cell_textview = itemView.findViewById(R.id.column_header_textView);
        }
    }
    @NonNull
    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_view_column_header_layout, parent, false);
        return new ColumnHeaderViewHolder(layout);
    }
    @Override
    public void onBindColumnHeaderViewHolder(@NonNull AbstractViewHolder holder, @Nullable ColumnHeader columnHeaderItemModel, int columnPosition) {
        ColumnHeader columnHeader = (ColumnHeader) columnHeaderItemModel;
        ColumnHeaderViewHolder columnHeaderViewHolder = (ColumnHeaderViewHolder) holder;
        columnHeaderViewHolder.cell_textview.setText(columnHeader.getData().toString());

//        columnHeaderViewHolder.column_header_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
//        columnHeaderViewHolder.cell_textview.requestLayout();
    }
    class RowHeaderViewHolder extends AbstractViewHolder{
        final TextView cell_textview;
        public RowHeaderViewHolder(View itemView){
            super(itemView);
            cell_textview = itemView.findViewById(R.id.row_header_textView);
        }
    }
    @NonNull
    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_view_row_header_layout, parent, false);
        return new RowHeaderViewHolder(layout);
    }
    @Override
    public void onBindRowHeaderViewHolder(@NonNull AbstractViewHolder holder, @Nullable RowHeader rowHeaderItemModel, int rowPosition) {
        RowHeader rowHeader = (RowHeader) rowHeaderItemModel;
        RowHeaderViewHolder rowHeaderViewHolder= (RowHeaderViewHolder) holder;
        rowHeaderViewHolder.cell_textview.setText(rowHeader.getData().toString());
        rowHeaderViewHolder.cell_textview.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            rowHeaderViewHolder.cell_textview.setAutoSizeTextTypeUniformWithConfiguration(
                    2, // Tamaño mínimo del texto en sp
                    20, // Tamaño máximo del texto en sp
                    1, // Incremento en sp
                    TypedValue.COMPLEX_UNIT_SP // Unidad de tamaño
            );
        }
        rowHeaderViewHolder.cell_textview.requestLayout();
        rowHeaderViewHolder.cell_textview.setOnClickListener(view -> {
            ClipboardManager clipboardManager =(ClipboardManager) this.context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("clip", rowHeader.getData().toString());
            clipboardManager.setPrimaryClip(clipData);
        });
    }




    @NonNull
    @Override
    public View onCreateCornerView(@NonNull ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.table_view_corner_layout, parent, false);
    }
    @Override
    public int getColumnHeaderItemViewType(int columnPosition) {
        return 0;
    }
    @Override
    public int getRowHeaderItemViewType(int rowPosition) {
        return 0;
    }
    @Override
    public int getCellItemViewType(int columnPosition) {
        return 0;
    }
}