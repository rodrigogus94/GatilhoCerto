package com.example.gatilhocerto.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gatilhocerto.R;
import com.example.gatilhocerto.helper.ConfiguracaoFirebase;
import com.example.gatilhocerto.helper.UsuarioFirebase;
import com.example.gatilhocerto.model.Produto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class NovoProdutoEmpresaActivity extends AppCompatActivity {

    private EditText editProdutoNome, editProdutoDescricao, editProdutoPreco;
    private ImageView imagemProduto;

    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageReference;
    private DatabaseReference firebaseRef;
    private String idUsuarioLogado;
    private String urlImagemSelecionada = "";
    Produto produto = new Produto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_produto);

        //Configurações iniciais
        inicializarComponentes();
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();


        //Configurações do toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Produto");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imagemProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                );
                if( i.resolveActivity(getPackageManager()) != null ){
                    startActivityForResult(i, SELECAO_GALERIA);
                }
            }
        });

        /*Recuperar dados da Produto*/
        recuperarDadosProduto();


    }

    private void recuperarDadosProduto(){

        DatabaseReference ProdutoRef = firebaseRef
                .child("Produtos")
                .child( idUsuarioLogado );
        ProdutoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if( dataSnapshot.getValue() != null ){
                    Produto produto = dataSnapshot.getValue(Produto.class);
                    editProdutoNome.setText(produto.getNome());
                    editProdutoDescricao.setText(produto.getDescricao());
                    editProdutoPreco.setText(produto.getPreco().toString());


                    urlImagemSelecionada = produto.getUrlImagem();
                    if( urlImagemSelecionada != "" ){
                        Picasso.get()
                                .load(urlImagemSelecionada)
                                .into(imagemProduto);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void validarDadosProduto(View view){

        //Valida se os campos foram preenchidos
        String nome = editProdutoNome.getText().toString();
        String preco = editProdutoPreco.getText().toString();
        String descricao = editProdutoDescricao.getText().toString();


        if(nome.isEmpty()) {
            exibirMensagem("Digite um nome do produto");


        }else if( preco.isEmpty()) {
            exibirMensagem("Digite uma preço de produto");

        }else if( descricao.isEmpty()) {
            exibirMensagem("Digite a descrição do produto");



        }else{


            produto.setIdUsuario( idUsuarioLogado );
            produto.setNome( nome );
            produto.setPreco( Double.parseDouble(preco) );
            produto.setDescricao(descricao);
            produto.setUrlImagem(urlImagemSelecionada);
            produto.salvar();
            finish();
            exibirMensagem("Produto salvo com sucesso!");
        }

    }

    private void exibirMensagem(String texto){
        Toast.makeText(NovoProdutoEmpresaActivity.this, texto, Toast.LENGTH_SHORT)
                .show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == RESULT_OK){
            Bitmap imagem = null;

            try {

                switch (requestCode) {
                    case SELECAO_GALERIA:
                        Uri localImagem = data.getData();
                        imagem = MediaStore.Images
                                .Media
                                .getBitmap(
                                        getContentResolver(),
                                        localImagem
                                );
                        break;
                }

                if( imagem != null){

                    imagemProduto.setImageBitmap( imagem );


                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    StorageReference imagemRef = storageReference
                            .child("imagensProdutos")
                            .child("produtos")
                            .child(idUsuarioLogado)
                            .child( imagem + "jpeg");

                    UploadTask uploadTask = imagemRef.putBytes( dadosImagem );
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NovoProdutoEmpresaActivity.this,
                                    "Erro ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            urlImagemSelecionada = taskSnapshot.getDownloadUrl().toString();
                            Toast.makeText(NovoProdutoEmpresaActivity.this,
                                    "Sucesso ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();

                        }
                    });

                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }
    private void inicializarComponentes(){
        editProdutoNome = findViewById(R.id.editProdutoNome);
        editProdutoDescricao = findViewById(R.id.editProdutoDescricao);
        editProdutoPreco = findViewById(R.id.editProdutoPreco);
        imagemProduto = findViewById(R.id.imageProduto);
    }

}
