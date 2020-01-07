package com.example.gatilhocerto.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.gatilhocerto.R;

public class ConfiguracoesEmpresaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao_empresa);

        //Configurações do toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
