package com.ultimafronteira.model;

public abstract class Item {
    protected String nome;
    protected double peso;
    protected int durabilidade;
    private String chaveImagem;

    public Item(String nome, double peso, int durabilidade, String chaveImagem) {
        this.nome = nome;
        this.peso = peso;
        this.durabilidade = durabilidade;
        this.chaveImagem = chaveImagem;
    }

    public String getNome() { return nome; }
    public double getPeso() { return peso; }
    public int getDurabilidade() { return durabilidade; }
    public void setDurabilidade(int durabilidade) { this.durabilidade = Math.max(0, durabilidade); }
    public String getChaveImagem() { return chaveImagem; }

    public abstract String usar(Personagem jogador);

    @Override
    public String toString() {
        return nome + " (Peso: " + peso + (durabilidade != -1 ? ", Durab.: " + durabilidade : "") + ")";
    }
}