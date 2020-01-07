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

import androidx.appcompat.widget.Toolbar;

import com.example.gatilhocerto.R;
import com.example.gatilhocerto.helper.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class EmpresaActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

       //Configurações do toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Gatilho Certo - empresa");
        setSupportActionBar(toolbar);


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
        }

        return super.onOptionsItemSelected(item);
    }

    private void abrirNovoProdto() {

        startActivity(new Intent(EmpresaActivity.this, NovoProdutoEmpresaActivity.class));

    }

    private void abrirConfiguracoes() {

        startActivity(new Intent(EmpresaActivity.this, ConfiguracoesEmpresaActivity.class));
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
