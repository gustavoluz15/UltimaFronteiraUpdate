package com.ultimafronteira.model;

public class Ferramenta extends Item {
    private String tipo;
    private int eficiencia;

    public Ferramenta(String nome, double peso, int durabilidade, String tipo, int eficiencia, String chaveImagem) {
        super(nome, peso, durabilidade, chaveImagem);
        this.tipo = tipo;
        this.eficiencia = eficiencia;
    }

    public String getTipo() {
        return tipo;
    }

    public int getEficiencia() {
        return eficiencia;
    }

    @Override
    public void usar(Personagem jogador) {
        if (getDurabilidade() > 0 || getDurabilidade() == -1) {
            System.out.println(jogador.getNome() + " usou a ferramenta: " + getNome() + " (" + tipo + ")");
            if (getDurabilidade() != -1) {
                setDurabilidade(getDurabilidade() -1);
                if (getDurabilidade() == 0) {
                    System.out.println(getNome() + " quebrou!");
                }
            }
        } else {
            System.out.println(getNome() + " está quebrado(a) e não pode ser usado(a).");
        }
    }
}