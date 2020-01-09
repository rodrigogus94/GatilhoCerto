package com.example.gatilhocerto.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.textclassifier.SelectionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.gatilhocerto.R;

public class ConfiguracoesEmpresaActivity extends AppCompatActivity {

    private Button botaoCadastrar;
    private EditText editEmpresaNome, editEmpresaTaxa, editEmpresaTempo, editEmpresaCategoria;
    private ImageView imagePerfilEmpresa;

    private  static  final int SELECAO_GALERIA =200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao_empresa);

        //Configurações do toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        imagePerfilEmpresa.setOnClickListener(new View.OnClickListener() {
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Bitmap image = null;

            try{

                switch (requestCode){

                    case SELECAO_GALERIA:
                        Uri localImage = data.getData();
                        image = MediaStore.Images.Media.getBitmap(
                                getContentResolver(), localImage

                        );
                        break;

                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void inicializarComponentes(){

        editEmpresaNome = findViewById(R.id.editEmpresaNome);
        editEmpresaCategoria =findViewById(R.id.editEmpresaCategoria);
        editEmpresaTaxa = findViewById(R.id.editempresaTaxa);
        editEmpresaTempo = findViewById(R.id.editEmpresaTempo);
        imagePerfilEmpresa = findViewById(R.id.imagePerfilEmpresa);
        botaoCadastrar = findViewById(R.id.buttonCadastroEmpresa);

    }
}
