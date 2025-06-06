package com.ultimafronteira.model;

public class Arma extends Item {
    private String tipoArma;
    private int dano;
    private int alcance;

    public Arma(String nome, double peso, int durabilidade, String tipoArma, int dano, int alcance, String chaveImagem) {
        super(nome, peso, durabilidade, chaveImagem);
        this.tipoArma = tipoArma;
        this.dano = dano;
        this.alcance = alcance;
    }

    public String getTipoArma() { return tipoArma; }
    public int getDano() { return dano; }
    public int getAlcance() { return alcance; }

    @Override
    public String usar(Personagem jogador) {
        if (jogador == null) return "Ninguém para usar a arma.";
        return jogador.equiparArma(this);
    }
}