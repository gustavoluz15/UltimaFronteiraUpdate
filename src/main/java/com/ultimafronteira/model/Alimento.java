package com.ultimafronteira.model;

public class Alimento extends Item {
    private int valorNutricional;
    private String tipo;
    private int curaVida = 10; // Valor de cura adicionado

    public Alimento(String nome, double peso, int durabilidade, int valorNutricional, String tipo, String chaveImagem) {
        super(nome, peso, durabilidade, chaveImagem);
        this.valorNutricional = valorNutricional;
        this.tipo = tipo;
    }

    public int getValorNutricional() { return valorNutricional; }
    public String getTipo() { return tipo; }
    public int getCuraVida() { return curaVida; }

    @Override
    public String usar(Personagem jogador) {
        if (jogador == null) return "Ninguém para comer.";
        StringBuilder sb = new StringBuilder();
        int fomeAntes = jogador.getFome();
        int vidaAntes = jogador.getVida();
        jogador.setFome(jogador.getFome() + this.valorNutricional);
        jogador.setVida(jogador.getVida() + this.curaVida);
        int fomeRecuperada = jogador.getFome() - fomeAntes;
        int vidaRecuperada = jogador.getVida() - vidaAntes;
        setDurabilidade(getDurabilidade() - 1);
        sb.append("Fome recuperada em ").append(fomeRecuperada).append(" pontos.\n");
        sb.append("Vida recuperada em ").append(vidaRecuperada).append(" pontos.");
        return sb.toString();
    }
}