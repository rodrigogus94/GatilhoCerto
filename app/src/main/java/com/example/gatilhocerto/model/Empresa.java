package com.example.gatilhocerto.model;

import com.example.gatilhocerto.helper.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

public class Empresa {

    private String idUsuario;
    private String urlImagem;
    private String nome;
    private String tempo;
    private Double precoEntrega;
    private String categoria;


    public Empresa() {
    }

    public void salvar(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference empresaRef = firebaseRef.child("empresas")
                .child(getIdUsuario());
        empresaRef.setValue(this);
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public Double getPrecoEntrega() {
        return precoEntrega;
    }

    public void setPrecoEntrega(Double precoEntrega) {
        this.precoEntrega = precoEntrega;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
