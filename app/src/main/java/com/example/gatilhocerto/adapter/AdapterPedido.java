package com.example.gatilhocerto.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gatilhocerto.R;
import com.example.gatilhocerto.model.ItemPedido;
import com.example.gatilhocerto.model.Pedido;

import java.util.ArrayList;
import java.util.List;


public class AdapterPedido extends RecyclerView.Adapter<AdapterPedido.MyViewHolder> {

    private List<Pedido> pedidos;

    public AdapterPedido(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pedidos, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        Pedido pedido = pedidos.get(i);
        holder.nome.setText( pedido.getNome() );
        holder.endereco.setText( "Endereço: "+pedido.getEndereco() );
        holder.numeroCasa.setText(", N: "+ pedido.getNumeroCasa());
        holder.telefone.setText("Telefone: " +pedido.getTelefone());
        holder.observacao.setText( "Obs: "+ pedido.getObservacao() );


        List<ItemPedido> itens = new ArrayList<>();
        itens = pedido.getItens();
        String descricaoItens = "";

        int numeroItem = 1;
        Double total = 0.0;
        for( ItemPedido itemPedido : itens ){

            int qtde = itemPedido.getQuantidade();
            Double preco = itemPedido.getPreco();
            total += (qtde * preco);

            String nome = itemPedido.getNomePedido();
            descricaoItens += numeroItem + ") " + nome + " / (" + qtde + " x R$ " + preco + ") \n";
            numeroItem++;
        }
        descricaoItens += "Total: R$ " + total;
        holder.itens.setText(descricaoItens);

        int metodoPagamento = pedido.getMetodoDePagamento();
        String pagamento = metodoPagamento == 0 ? "Dinheiro" : "Máquina cartão" ;
        holder.pgto.setText( "pgto: " + pagamento );

    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        TextView endereco;
        TextView numeroCasa;
        TextView telefone;
        TextView pgto;
        TextView observacao;
        TextView itens;

        public MyViewHolder(View itemView) {
            super(itemView);

                nome        = itemView.findViewById(R.id.textPedidoNome);
                endereco    = itemView.findViewById(R.id.textPedidoEndereco);
                numeroCasa  = itemView.findViewById(R.id.textPedidoNumeroCasa);
                telefone    = itemView.findViewById(R.id.textPedidoTelefone);
                pgto        = itemView.findViewById(R.id.textPedidoPgto);
                observacao  = itemView.findViewById(R.id.textPedidoObs);
                itens       = itemView.findViewById(R.id.textPedidoItens);
        }
    }

}
