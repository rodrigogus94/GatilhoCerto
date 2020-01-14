package com.example.gatilhocerto.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gatilhocerto.R;

import com.example.gatilhocerto.model.Empresa;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterEmpresa extends RecyclerView.Adapter<AdapterEmpresa.MyViewHolder>{

    private List<Empresa> empresas;


    public AdapterEmpresa(List<Empresa> empresas) {
        this.empresas = empresas;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_empresa, parent, false);
        return new MyViewHolder( item );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Empresa empresa = empresas.get(position);
        holder.nome.setText( empresa.getNome());
        holder.valor.setText("R$ " + empresa.getPrecoEntrega() );
        holder.categoria.setText(empresa.getCategoria());
        holder.tempo.setText(empresa.getTempo() + " min");

        //Pega a imagem do produto
        String urlFotos = empresa.getUrlImagem();
        Picasso.get().load(urlFotos).into(holder.foto);

    }

    @Override
    public int getItemCount() {
        return empresas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        TextView valor;
        TextView categoria;
        TextView tempo;
        ImageView foto;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textNomeEmpresa);
            categoria = itemView.findViewById(R.id.textCategoriaEmpresa);
            valor  = itemView.findViewById(R.id.textEntregaEmpresa);
            tempo = itemView.findViewById(R.id.textTempoEmpresa);
            foto   = itemView.findViewById(R.id.imagemEmpresa);

        }
    }

}