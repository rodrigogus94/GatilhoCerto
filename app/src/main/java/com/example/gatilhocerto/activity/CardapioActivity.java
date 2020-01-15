package com.example.gatilhocerto.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gatilhocerto.R;
import com.example.gatilhocerto.adapter.AdapterProduto;
import com.example.gatilhocerto.helper.ConfiguracaoFirebase;
import com.example.gatilhocerto.helper.UsuarioFirebase;
import com.example.gatilhocerto.listener.RecyclerItemClickListener;
import com.example.gatilhocerto.model.Empresa;
import com.example.gatilhocerto.model.ItemPedido;
import com.example.gatilhocerto.model.Pedido;
import com.example.gatilhocerto.model.Produto;
import com.example.gatilhocerto.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class CardapioActivity extends AppCompatActivity {


    private DatabaseReference firebaseRef;

    private AdapterProduto adapterProduto;
    private List<Produto> produtos = new ArrayList<>();
    private List<ItemPedido> itemCarrinho = new ArrayList<>();



    private AlertDialog dialog;
    private String idUsuarioLogado;
    private Usuario usuario;
    private Pedido pedidoRecuperado;

    private RecyclerView recyclerProdutosMenu;
    private ImageView imageEmpresaMenu;
    private TextView textNomeEmpresaMenu,textCategoriaEmpresaMenu,textTaxaEmpresaMenu,textTempoEmpresaMenu;
    private Empresa empresaSelecionada;
    private String idEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);


        //configuração inicial
        inicializarComponentes();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

        //Recuperar empresa selecionada
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            empresaSelecionada = (Empresa) bundle.getSerializable("empresa");
            textNomeEmpresaMenu.setText(empresaSelecionada.getNome() );
            textTempoEmpresaMenu.setText(empresaSelecionada.getTempo() + " min ");
            textTaxaEmpresaMenu.setText("- R$ " +empresaSelecionada.getPrecoEntrega().toString() + " -");
            textCategoriaEmpresaMenu.setText(empresaSelecionada.getCategoria());
            idEmpresa = empresaSelecionada.getIdUsuario();

            String url = empresaSelecionada.getUrlImagem();
            Picasso.get().load(url).into(imageEmpresaMenu);

        }


        //Configurações do toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Produtos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Configurar RecyclerView
        recyclerProdutosMenu.setLayoutManager(new LinearLayoutManager(this));
        recyclerProdutosMenu.setHasFixedSize(true);
        adapterProduto = new AdapterProduto(produtos,this);
        recyclerProdutosMenu.setAdapter(adapterProduto);

        //Configurar o evento de clique
        recyclerProdutosMenu.addOnItemTouchListener(new RecyclerItemClickListener(
                this, recyclerProdutosMenu,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        confirmarQuantidade(position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }

        ));


        //recupera produtos da empresa
        recuperarProdutos();
        recuperarDadosUsuarios();



    }

    private void confirmarQuantidade(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quantidade");
        builder.setMessage("Digite a quantidade");

        final EditText editQuantidade = new EditText(this);
        editQuantidade.setText("1");

        builder.setView(editQuantidade);


        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String quantidade = editQuantidade.getText().toString();

                Produto produtoSelecionado = produtos.get(position);
                ItemPedido itemPedido = new ItemPedido();
                itemPedido.setIdPedido(produtoSelecionado.getIdProduto());
                itemPedido.setNomePedido(produtoSelecionado.getNome());
                itemPedido.setPreco(produtoSelecionado.getPreco());
                itemPedido.setQuantidade(Integer.parseInt(quantidade));


                itemCarrinho.add(itemPedido);

                if(pedidoRecuperado == null){
                    pedidoRecuperado = new Pedido(idUsuarioLogado, idEmpresa);
                }
                pedidoRecuperado.setNome(usuario.getNome());
                pedidoRecuperado.setEndereco(usuario.getEndereco());
                pedidoRecuperado.setTelefone(usuario.getTelefone());
                pedidoRecuperado.setItens(itemCarrinho);
                pedidoRecuperado.salvar();




            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void recuperarDadosUsuarios() {
        dialog = new SpotsDialog.Builder().setContext(this).setMessage("Carregando Daods").setCancelable(false).build();
        dialog.show();

        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuarioLogado);

        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    usuario = dataSnapshot.getValue(Usuario.class);

                }
                recuperarPedido();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void recuperarPedido(){

        dialog.dismiss();
    }


    private void recuperarProdutos(){

        DatabaseReference produtosref = firebaseRef
                .child("produtos")
                .child(idEmpresa);

        produtosref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                produtos.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    produtos.add(ds.getValue(Produto.class));
                }

                adapterProduto.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cardapio, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuPedido:

                break;

        }

        return super.onOptionsItemSelected(item);
    }


    private void inicializarComponentes(){

        recyclerProdutosMenu = findViewById(R.id.recycleProdutosMenu);
        imageEmpresaMenu = findViewById(R.id.imagemEmpresaMenu);
        textNomeEmpresaMenu = findViewById(R.id.textNomeEmpresaMenu);
        textCategoriaEmpresaMenu = findViewById(R.id.textCategoriaEmpresaMenu);
        textTaxaEmpresaMenu = findViewById(R.id.textEntregaEmpresaMenu);
        textTempoEmpresaMenu = findViewById(R.id.textTempoEmpresaMenu);

    }
}
