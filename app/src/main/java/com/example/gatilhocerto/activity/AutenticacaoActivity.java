package com.example.gatilhocerto.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.gatilhocerto.R;
import com.example.gatilhocerto.helper.ConfigurarFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class AutenticacaoActivity extends AppCompatActivity {

    private Button botaoAcesso;
    private EditText campoEmail, campoSenha;
    private Switch tipoAcesso;

    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacao);
        getSupportActionBar().hide();

        inicializaComponentes();
        autenticacao = ConfigurarFirebase.getFirebaseautenticacao();

        //verifica se o usuario esta logado
        verificarUsuarioLogado();

        botaoAcesso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if(email.isEmpty()){
                    Toast.makeText(AutenticacaoActivity.this,
                            "Preencha o campo de E-mail!", Toast.LENGTH_SHORT).show();
                }else if(senha.isEmpty()){
                    Toast.makeText(AutenticacaoActivity.this,
                            "Preencha o campo de senha!", Toast.LENGTH_SHORT).show();
                }else {
                    //verifica o estado do switch
                    if(tipoAcesso.isChecked()){//Cadastro

                        autenticacao.createUserWithEmailAndPassword(
                                email,senha
                        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(AutenticacaoActivity.this,
                                            "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                                   abrirTelaPrincipal();

                                }else {
                                    String erroExcesso;

                                    try {
                                        throw task.getException();

                                    }
                                    catch (FirebaseAuthWeakPasswordException e){
                                        erroExcesso = " Digite uma senha mais forte!";

                                    }catch (FirebaseAuthInvalidCredentialsException e){
                                        erroExcesso = " Por favor digite um e-mail válido";

                                    }catch (FirebaseAuthUserCollisionException e){
                                        erroExcesso = "  conta já está cadastrada";

                                    }catch (Exception e){
                                        erroExcesso = "ao cadastra o usuário " + e.getMessage();
                                        e.printStackTrace();
                                    }

                                    Toast.makeText(AutenticacaoActivity.this,
                                            "Erro:" + erroExcesso, Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }else{//login

                        autenticacao.signInWithEmailAndPassword(
                                email, senha
                        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(AutenticacaoActivity.this,
                                            "Logado com sucesso!", Toast.LENGTH_SHORT).show();
                                    abrirTelaPrincipal();
                                }else {

                                    String erroExcecao;

                                    try {
                                        throw task.getException();
                                    }catch ( FirebaseAuthInvalidUserException e ) {
                                        erroExcecao = "Usuário não está cadastrado.";
                                    }catch ( FirebaseAuthInvalidCredentialsException e ){
                                        erroExcecao = "E-mail ou senha não correspondem a um usuário cadastrado";
                                    }catch (Exception e){
                                        erroExcecao = "Erro ao cadastrar usuário: "  + e.getMessage();
                                        e.printStackTrace();
                                    }

                                    Toast.makeText(AutenticacaoActivity.this,
                                            erroExcecao,
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                    }

                }


            }
        });

    }

    private void verificarUsuarioLogado(){
        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();
        if(usuarioAtual != null){
            abrirTelaPrincipal();
        }
    }

    private void abrirTelaPrincipal(){
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
    }

    private void inicializaComponentes(){
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        botaoAcesso = findViewById(R.id.buttonAcesso);
        tipoAcesso = findViewById(R.id.switchAcesso);

    }



}
