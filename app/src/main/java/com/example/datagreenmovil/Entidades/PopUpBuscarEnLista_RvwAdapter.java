package com.example.datagreenmovil.Entidades;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datagreenmovil.Logica.Funciones;
import com.example.datagreenmovil.R;

import java.util.ArrayList;

public class PopUpBuscarEnLista_RvwAdapter extends RecyclerView.Adapter<PopUpBuscarEnLista_RvwAdapter.PopUpBuscarEnListaViewHolder> {

    //String idTareoActual;
    Context contexto;
//    ArrayList<PopUpBuscarEnLista_Item> items = new ArrayList<>();
    ArrayList<PopUpBuscarEnLista_Item> itemsFiltrado; //= new ArrayList<>();
    ArrayList<PopUpBuscarEnLista_Item> items; //= new ArrayList<>();
    TextView txv_Id, txv_Descripcion;
//    RecyclerView mRecyclerView;
    PopUpBuscarEnLista dialogPadre;
    public PopUpBuscarEnLista_RvwAdapter(Context ct, ArrayList<PopUpBuscarEnLista_Item> lista, TextView txt_Id, TextView txv_Descripcion, PopUpBuscarEnLista padre){
        contexto = ct;
        this.itemsFiltrado =lista;
        items = new ArrayList<>();
        items.addAll(this.itemsFiltrado);
        this.txv_Id =txt_Id;
        this.txv_Descripcion =txv_Descripcion;
        this.dialogPadre =padre;
    }

    @NonNull
    @Override
    public PopUpBuscarEnListaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try{
            LayoutInflater inflater=LayoutInflater.from(contexto);
            View view=inflater.inflate(R.layout.popup_buscar_en_lista_item_027,parent,false);
            return new PopUpBuscarEnListaViewHolder(view);
        }catch (Exception ex){
            Toast.makeText(contexto,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
        return null;
    }
    /*
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        }
    */
    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull PopUpBuscarEnLista_RvwAdapter.PopUpBuscarEnListaViewHolder holder, int position) {
        try{
            holder.c027_txv_Id.setText(itemsFiltrado.get(holder.getAdapterPosition()).getId());
            holder.c027_txv_Descripcion.setText(itemsFiltrado.get(holder.getAdapterPosition()).getDescripcion().trim());

            holder.c027_cly_Principal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    AQUI REALIZAR CAMBIOS PARA UN VALOR SELECCIONADO
                    if (txv_Id!=null)
                        txv_Id.setText(itemsFiltrado.get(holder.getAdapterPosition()).getId().trim());
                    if (txv_Descripcion!=null)
                        txv_Descripcion.setText(itemsFiltrado.get(holder.getAdapterPosition()).getDescripcion().trim());
                    dialogPadre.dismiss();
//                    ((PopUpBuscarEnLista)mRecyclerView.getParent().getParent()).dismiss();

                    //super.getClass() instanceof PopUpBuscarEnLista ? (() super) : null;
//                    if (contexto instanceof  com.example.datagreenmovil.Entidades.PopUpBuscarEnLista) {
//                        ((PopUpBuscarEnLista)contexto).dismiss();
//                    }
                }
            });
        }catch (Exception ex){
            //Toast.makeText(contexto,ex.getMessage(),Toast.LENGTH_LONG).show();
            Funciones.mostrarError(contexto,ex);
        }
    }

    public  void filtrado(String s){
        int longitud = s.length();
        itemsFiltrado.clear();
        if (longitud==0){
            itemsFiltrado.addAll(items);
            //Toast.makeText(contexto,"long=0",Toast.LENGTH_LONG).show();
            notifyDataSetChanged();
        }else {
            //List<PopUpBuscarEnLista_Item> colecion = itemsFiltrados.stream().filter(i -> i.getId().toLowerCase().contains(s.toLowerCase())).collect(Collectors.toList());
            for(PopUpBuscarEnLista_Item i : items){
                if (i.getId().toLowerCase().contains(s.toLowerCase()) || i.getDescripcion().toLowerCase().contains(s.toLowerCase()) ){
                    itemsFiltrado.add(i);
                }
            }
            //Toast.makeText(contexto,"else",Toast.LENGTH_LONG).show();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount()
    {
        return itemsFiltrado.size();
    }

    public class PopUpBuscarEnListaViewHolder extends  RecyclerView.ViewHolder {
        TextView c027_txv_Id, c027_txv_Descripcion;
        ConstraintLayout c027_cly_Principal;

        public PopUpBuscarEnListaViewHolder(@NonNull View itemView) {
            super(itemView);
            c027_txv_Id = itemView.findViewById(R.id.c027_txv_Id_v);
            c027_txv_Descripcion = itemView.findViewById(R.id.c027_txv_Descripcion_v);
            c027_cly_Principal = itemView.findViewById(R.id.c027_cly_Principal_v);

        }
    }

//        @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//
//        mRecyclerView = recyclerView;
//    }

//    private void abrirDocumento(String id) {
//        Intent nuevaActividad = new Intent(Context, cls_05010000_Edicion.class);
//        nuevaActividad.putExtra("ConfiguracionLocal",objConfLocal);
//        nuevaActividad.putExtra("IdDocumentoActual",id);
//        Context.startActivity(nuevaActividad);
//    }
}
