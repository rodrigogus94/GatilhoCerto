package com.example.gatilhocerto.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gatilhocerto.R;
import com.example.gatilhocerto.adapter.AdapterProduto;
import com.example.gatilhocerto.helper.ConfiguracaoFirebase;
import com.example.gatilhocerto.model.Empresa;
import com.example.gatilhocerto.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CardapioActivity extends AppCompatActivity {


    private DatabaseReference firebaseRef;

    private AdapterProduto adapterProduto;
    private List<Produto> produtos = new ArrayList<>();

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


        //recupera produtos da empresa
        recuperarProdutos();



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

    private void inicializarComponentes(){

        recyclerProdutosMenu = findViewById(R.id.recycleProdutosMenu);
        imageEmpresaMenu = findViewById(R.id.imagemEmpresaMenu);
        textNomeEmpresaMenu = findViewById(R.id.textNomeEmpresaMenu);
        textCategoriaEmpresaMenu = findViewById(R.id.textCategoriaEmpresaMenu);
        textTaxaEmpresaMenu = findViewById(R.id.textEntregaEmpresaMenu);
        textTempoEmpresaMenu = findViewById(R.id.textTempoEmpresaMenu);

    }
}
