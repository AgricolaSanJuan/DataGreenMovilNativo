package com.example.datagreenmovil;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.datagreenmovil.Entidades.TareoDetalle;

import java.util.List;

public class cls_05000100_DiffCallback extends DiffUtil.Callback {
    private final List<TareoDetalle> oldList;
    private final List<TareoDetalle> newList;

    public cls_05000100_DiffCallback(List<TareoDetalle> oldList, List<TareoDetalle> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // Aquí comparas si son el mismo objeto o tienen el mismo ID
        return oldList.get(oldItemPosition).getItem() == newList.get(newItemPosition).getItem();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // Aquí comparas si el contenido de los objetos es el mismo
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Si solo algunos campos han cambiado, puedes usar esto para pasar solo los cambios específicos
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
