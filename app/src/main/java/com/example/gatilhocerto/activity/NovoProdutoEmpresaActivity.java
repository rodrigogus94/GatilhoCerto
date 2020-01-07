package com.example.gatilhocerto.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.gatilhocerto.R;

public class NovoProdutoEmpresaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_produto);

        //Configurações do toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Produto");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
