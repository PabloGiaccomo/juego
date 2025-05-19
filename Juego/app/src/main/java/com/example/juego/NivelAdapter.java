package com.example.juego;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.juego.R;
import com.example.juego.Desafio;
import java.util.List;

public class NivelAdapter extends RecyclerView.Adapter<NivelAdapter.ViewHolder> {
    private List<Desafio> desafios;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Desafio desafio);
    }

    public NivelAdapter(List<Desafio> desafios, OnItemClickListener listener) {
        this.desafios = desafios;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nivel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Desafio desafio = desafios.get(position);
        holder.titulo.setText(desafio.getTitulo());
        holder.dificultad.setText("Dificultad: " + desafio.getDificultad());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(desafio));
    }

    @Override
    public int getItemCount() {
        return desafios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo;
        public TextView dificultad;

        public ViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.nivel_titulo);
            dificultad = itemView.findViewById(R.id.nivel_dificultad);
        }
    }
}