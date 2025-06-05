package com.ultimafronteira.model;

public abstract class Item {
    protected String nome;
    protected double peso;
<<<<<<< HEAD
    protected int durabilidade;


    protected String chaveImagem;

    public Item(String nome, double peso, int durabilidade, String chaveImagem) {
        this.nome = nome;
        this.peso = peso;
        this.durabilidade = durabilidade;
        this.chaveImagem = chaveImagem; // <-- MUDANÇA AQUI
=======
    protected int durabilidade; // -1 para itens não duráveis ou com durabilidade infinita

    public Item(String nome, double peso, int durabilidade) {
        this.nome = nome;
        this.peso = peso;
        this.durabilidade = durabilidade;
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
    }

    public String getNome() {
        return nome;
    }

    public double getPeso() {
        return peso;
    }

    public int getDurabilidade() {
        return durabilidade;
    }

    public void setDurabilidade(int durabilidade) {
        this.durabilidade = Math.max(0, durabilidade);
    }

<<<<<<< HEAD

    public String getChaveImagem() {
        return this.chaveImagem;
    }

=======
    // Método abstrato a ser implementado pelas subclasses
    // O parâmetro 'jogador' permite que o item afete o personagem
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
    public abstract void usar(Personagem jogador);

    @Override
    public String toString() {
        return nome + " (Peso: " + peso + (durabilidade != -1 ? ", Durabilidade: " + durabilidade : "") + ")";
    }
}