package com.example.gatilhocerto.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gatilhocerto.R;
import com.example.gatilhocerto.adapter.AdapterProduto;
import com.example.gatilhocerto.helper.ConfiguracaoFirebase;
import com.example.gatilhocerto.helper.UsuarioFirebase;
import com.example.gatilhocerto.listener.RecyclerItemClickListener;
import com.example.gatilhocerto.model.Produto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmpresaActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private DatabaseReference firebaseRef;

    private RecyclerView recyclerProdutos;
    private AdapterProduto adapterProduto;
    private List<Produto> produtos = new ArrayList<>();
    private String idUsuarioLogado;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        inicializarComponentes();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

       //Configurações do toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Gatilho Certo - empresa");
        setSupportActionBar(toolbar);

        //Configurar RecyclerView
        recyclerProdutos.setLayoutManager(new LinearLayoutManager(this));
        recyclerProdutos.setHasFixedSize(true);
        adapterProduto = new AdapterProduto(produtos,this);
        recyclerProdutos.setAdapter(adapterProduto);


        //Adiciona evento de clique no recycleView
        recyclerProdutos.addOnItemTouchListener(new RecyclerItemClickListener(
                this, recyclerProdutos,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Produto produtoSelecionado = produtos.get(position);
                        produtoSelecionado.remover();
                        Toast.makeText(EmpresaActivity.this,"Produto excluído com sucesso!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }


        ));

        //recupera produtos da empresa
        recuperarProdutos();


    }


    private void recuperarProdutos(){

        DatabaseReference produtosref = firebaseRef
                .child("produtos")
                .child(idUsuarioLogado);

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

        recyclerProdutos = findViewById(R.id.recyclerProdutos);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_empresa, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuSair:
                deslogarEmpresa();
                break;
                
            case R.id.menuConfiguracoes:
                abrirConfiguracoes();
                break;

            case R.id.menuNovoProduto:
                abrirNovoProdto();
                break;

            case R.id.menuPedidos:
                abrirPedidos();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void abrirNovoProdto() {

        startActivity(new Intent(EmpresaActivity.this, NovoProdutoEmpresaActivity.class));

    }

    private void abrirConfiguracoes() {

        startActivity(new Intent(EmpresaActivity.this, ConfiguracoesEmpresaActivity.class));
    }

    private void abrirPedidos() {

        startActivity(new Intent(EmpresaActivity.this, PedidosActivity.class));
    }

    private void deslogarEmpresa() {

        try {

            autenticacao.signOut();
            finish();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
