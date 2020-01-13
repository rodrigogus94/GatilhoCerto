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
import com.example.gatilhocerto.model.Produto;
import com.squareup.picasso.Picasso;

import java.util.List;
public class AdapterProduto extends RecyclerView.Adapter<AdapterProduto.MyViewHolder>{

    private List<Produto> produtos;
    private Context context;

    public AdapterProduto(List<Produto> produtos, Context context) {
        this.produtos = produtos;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_produto, parent, false);
        return new MyViewHolder( item );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Produto produto = produtos.get(position);
        holder.nome.setText( produto.getNome());
        holder.valor.setText("R$ " + produto.getPreco() );
        holder.descricao.setText(produto.getDescricao());

        //Pega a imagem do produto
        String urlFotos = produto.getUrlImagem();
        Picasso.get().load(urlFotos).into(holder.foto);

    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        TextView valor;
        TextView descricao;
        ImageView foto;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textNomeProduto);
            descricao = itemView.findViewById(R.id.textDescricaoProduto);
            valor  = itemView.findViewById(R.id.textPrecoProduto);
            foto   = itemView.findViewById(R.id.imagemProduto);

        }
    }

}