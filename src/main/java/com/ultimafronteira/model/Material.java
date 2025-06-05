package com.ultimafronteira.model;

public class Material extends Item {
    private String tipo;
    private int resistencia;

    public Material(String nome, double peso, String tipo, int resistencia, String chaveImagem) {
        super(nome, peso, -1, chaveImagem);
        this.tipo = tipo;
        this.resistencia = resistencia;
    }

    public String getTipo() {
        return tipo;
    }

    public int getResistencia() {
        return resistencia;
    }

    @Override
    public void usar(Personagem jogador) {
        System.out.println("O material " + getNome() + " não pode ser usado diretamente desta forma. Use-o para combinar ou construir.");
    }

    public void combinar(Material outroMaterial) {
        System.out.println("Tentando combinar " + getNome() + " com " + outroMaterial.getNome());
    }
}