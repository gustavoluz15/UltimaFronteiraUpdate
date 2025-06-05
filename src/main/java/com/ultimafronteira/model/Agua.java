package com.ultimafronteira.model;

public class Agua extends Item {
    private int valorHidratacao;
    private String pureza;
    private double volume;

    public Agua(String nome, double peso, int durabilidade, int valorHidratacao, String pureza, double volume, String chaveImagem) {
        super(nome, peso, durabilidade, chaveImagem);
        this.valorHidratacao = valorHidratacao;
        this.pureza = pureza;
        this.volume = volume;
    }

    public int getValorHidratacao() { return valorHidratacao; }
    public String getPureza() { return pureza; }
    public double getVolume() { return volume; }

    @Override
    public void usar(Personagem jogador) {
        if (jogador == null) return;
        if ("potável".equalsIgnoreCase(pureza)) {
            jogador.setSede(jogador.getSede() + valorHidratacao);
        } else {
            jogador.setVida(jogador.getVida() - 5);
        }
    }
}