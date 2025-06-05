package com.ultimafronteira.model;

public class Material extends Item {
    private String tipo;
    private int resistencia;

<<<<<<< HEAD
    public Material(String nome, double peso, String tipo, int resistencia, String chaveImagem) {
        super(nome, peso, -1, chaveImagem);
=======
    public Material(String nome, double peso, String tipo, int resistencia) {
        super(nome, peso, -1);
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
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