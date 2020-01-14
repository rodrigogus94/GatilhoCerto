package com.example.gatilhocerto.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gatilhocerto.R;
import com.example.gatilhocerto.helper.ConfiguracaoFirebase;
import com.example.gatilhocerto.helper.UsuarioFirebase;
import com.example.gatilhocerto.model.Usuario;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ConfiguracoesUsuarioActivity extends AppCompatActivity {

    private EditText editUsuarioNome,editUsuarioEdereco,editUsuarioNumeroCasa,editUsuarioTelefone;
    private String idUsuario;
    private DatabaseReference firebaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_usuario);


        //Inicializar Componentes
        inicializarComponentes();
        idUsuario = UsuarioFirebase.getIdUsuario();
        firebaseRef = ConfiguracaoFirebase.getFirebase();


        //Configurações do toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações Usuário");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recuperar dados da empresa
        recuperarDadosUsuario();



    }

    private void recuperarDadosUsuario(){

        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    editUsuarioNome.setText(usuario.getNome());
                    editUsuarioEdereco.setText(usuario.getEndereco());
                    editUsuarioNumeroCasa.setText(usuario.getNumeroCasa());
                    editUsuarioTelefone.setText(usuario.getTelefone());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void validarDadosUsuario(View view){

        String nome = editUsuarioNome.getText().toString();
        String endereco = editUsuarioEdereco.getText().toString();
        String numeroCasa = editUsuarioNumeroCasa.getText().toString();
        String telefone = editUsuarioTelefone.getText().toString();


        if( nome.isEmpty()) {
            exibirMensagem("Digite seu nome!");

        }else if( endereco.isEmpty()) {
            exibirMensagem("Digite seu endereço completo!");

        }else if( numeroCasa.isEmpty()) {
            exibirMensagem("Digite o número da sua casa!");

        }else if( telefone.isEmpty()){
            exibirMensagem("Digite um telefone para contato!");


        }else{

            Usuario usuario = new Usuario();
            usuario.setIdUsuario( idUsuario);
            usuario.setNome( nome );
            usuario.setEndereco(endereco);
            usuario.setNumeroCasa(numeroCasa);
            usuario.setTelefone(telefone);

            usuario.salvar();
            finish();
            exibirMensagem("Dados atualizados com sucesso!");


        }


    }


    private void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT)
                .show();
    }



    private void inicializarComponentes(){

        editUsuarioNome = findViewById(R.id.editUsuarioNome);
        editUsuarioEdereco = findViewById(R.id.editUsuarioEndereca);
        editUsuarioNumeroCasa = findViewById(R.id.editUsuarioNumeroCasa);
        editUsuarioTelefone = findViewById(R.id.editUsuarioTelefone);


        SimpleMaskFormatter smfTelefone = new SimpleMaskFormatter("(NN)NNNNN-NNNN" );
        SimpleMaskFormatter smfNumeroCasa = new SimpleMaskFormatter("NNNNN");

        MaskTextWatcher mtwTelefone = new MaskTextWatcher(editUsuarioTelefone, smfTelefone);
        MaskTextWatcher mtwNumeroCasa = new MaskTextWatcher(editUsuarioNumeroCasa, smfNumeroCasa);

        editUsuarioTelefone.addTextChangedListener(mtwTelefone);
        editUsuarioNumeroCasa.addTextChangedListener(mtwNumeroCasa);

    }
}
