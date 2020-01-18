package com.example.gatilhocerto.model;

import com.example.gatilhocerto.helper.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;

public class Pedido {

    private String idUsuario;
    private String idEmpresa;
    private String idPedido;
    private String nome;
    private String endereco;
    private String telefone;
    private String numeroCasa;
    private List<ItemPedido> itens;
    private Double total;
    private String status = "pendente";
    private int metodoDePagamento;
    private String observacao;

    public Pedido() {
    }

    public Pedido(String idUsuario, String idEmpresa) {
        setIdUsuario(idUsuario);
        setIdEmpresa(idEmpresa);

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef.child("pedidos_usuario").child(idEmpresa).child(idUsuario);
        setIdPedido(pedidoRef.push().getKey());

    }

    public void salvar(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef.child("pedidos_usuario").child(getIdEmpresa()).child(getIdUsuario());
        pedidoRef.setValue(this);

    }
    public void remover(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef.child("pedidos_usuario").child(getIdEmpresa()).child(getIdUsuario());
        pedidoRef.removeValue();
    }
     public void confimar(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef.child("pedidos").child(getIdEmpresa()).child(getIdPedido());
        pedidoRef.setValue(this);

    }

    public void atualizarStatus() {

        HashMap<String, Object> status = new HashMap<>();
        status.put("status", getStatus());


        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef.child("pedidos").child(getIdEmpresa()).child(getIdPedido());
        pedidoRef.updateChildren(status);


    }

    public String getNumeroCasa() {
        return numeroCasa;
    }

    public void setNumeroCasa(String numeroCasa) {
        this.numeroCasa = numeroCasa;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMetodoDePagamento() {
        return metodoDePagamento;
    }

    public void setMetodoDePagamento(int metodoDePagamento) {
        this.metodoDePagamento = metodoDePagamento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }


}
