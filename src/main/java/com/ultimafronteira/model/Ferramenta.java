package com.ultimafronteira.model;

public class Ferramenta extends Item {
    private String tipo;
    private int eficiencia;

    public Ferramenta(String nome, double peso, int durabilidade, String tipo, int eficiencia, String chaveImagem) {
        super(nome, peso, durabilidade, chaveImagem);
        this.tipo = tipo;
        this.eficiencia = eficiencia;
    }

    public String getTipo() { return tipo; }
    public int getEficiencia() { return eficiencia; }

    @Override
    public String usar(Personagem jogador) {
        StringBuilder sb = new StringBuilder();
        if (getDurabilidade() > 0 || getDurabilidade() == -1) {
            sb.append(jogador.getNome()).append(" usou a ferramenta: ").append(getNome());
            if (getDurabilidade() != -1) {
                setDurabilidade(getDurabilidade() - 1);
                if (getDurabilidade() == 0) {
                    sb.append("\nA ferramenta ").append(getNome()).append(" quebrou!");
                }
            }
        } else {
            sb.append(getNome()).append(" está quebrada.");
        }
        return sb.toString();
    }
}