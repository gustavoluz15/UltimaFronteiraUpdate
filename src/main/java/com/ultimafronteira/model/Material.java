package com.ultimafronteira.model;

public class Material extends Item {
    private String tipo;
    private int resistencia;

    public Material(String nome, double peso, String tipo, int resistencia, String chaveImagem) {
        super(nome, peso, -1, chaveImagem);
        this.tipo = tipo;
        this.resistencia = resistencia;
    }

    public String getTipo() { return tipo; }
    public int getResistencia() { return resistencia; }

    @Override
    public String usar(Personagem jogador) {
        return "O material '" + getNome() + "' não pode ser usado diretamente. Use-o para fabricar itens.";
    }
}