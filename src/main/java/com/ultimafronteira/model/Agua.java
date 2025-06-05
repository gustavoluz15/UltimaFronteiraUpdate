package com.ultimafronteira.model;

public class Agua extends Item {
    private int valorHidratacao;
    private String pureza;
    private double volume;

<<<<<<< HEAD
    public Agua(String nome, double peso, int durabilidade, int valorHidratacao, String pureza, double volume, String chaveImagem) {
        super(nome, peso, durabilidade, chaveImagem);
=======
    public Agua(String nome, double peso, int durabilidade, int valorHidratacao, String pureza, double volume) {
        super(nome, peso, durabilidade);
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
        this.valorHidratacao = valorHidratacao;
        this.pureza = pureza;
        this.volume = volume;
    }

<<<<<<< HEAD
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
=======
    public int getValorHidratacao() {
        return valorHidratacao;
    }

    public String getPureza() {
        return pureza;
    }

    public double getVolume() {
        return volume;
    }

    @Override
    public void usar(Personagem jogador) {
        System.out.println(jogador.getNome() + " bebeu " + getNome() + ".");
        if ("potável".equalsIgnoreCase(pureza)) {
            jogador.setSede(jogador.getSede() + valorHidratacao);
        } else {
            System.out.println("A água estava contaminada!");
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
        }
    }
}