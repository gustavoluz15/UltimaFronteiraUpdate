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
    public String usar(Personagem jogador) {
        if (jogador == null) return "Ninguém para beber a água.";
        StringBuilder sb = new StringBuilder();
        if ("potável".equalsIgnoreCase(pureza)) {
            int sedeAntes = jogador.getSede();
            jogador.setSede(jogador.getSede() + valorHidratacao);
            sb.append("Sede recuperada em ").append(jogador.getSede() - sedeAntes).append(" pontos.");
        } else {
            int danoCausado = 5;
            jogador.setVida(jogador.getVida() - danoCausado);
            sb.append("A água estava contaminada! Você se sente mal e perde ").append(danoCausado).append(" de vida.");
        }
        setDurabilidade(getDurabilidade() - 1);
        return sb.toString();
    }
}