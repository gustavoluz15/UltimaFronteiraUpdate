package com.ultimafronteira.model;

public class Alimento extends Item {
    private int valorNutricional;
<<<<<<< HEAD
    private int valorCura;
    private String tipo;


    public Alimento(String nome, double peso, int durabilidade, int valorNutricional, int valorCura, String tipo, String chaveImagem) {
        super(nome, peso, durabilidade, chaveImagem);
        this.valorNutricional = valorNutricional;
        this.valorCura = valorCura;
        this.tipo = tipo;
    }

    public int getValorNutricional() { return valorNutricional; }
    public int getValorCura() { return valorCura; }
    public String getTipo() { return tipo; }

    @Override
    public void usar(Personagem jogador) {
        if (jogador == null) return;

        jogador.setFome(jogador.getFome() + this.valorNutricional);
        jogador.setVida(jogador.getVida() + this.valorCura);

        System.out.println(jogador.getNome() + " consumiu " + getNome() + ".");
=======
    private String tipo;

    public Alimento(String nome, double peso, int durabilidade, int valorNutricional, String tipo) {
        super(nome, peso, durabilidade);
        this.valorNutricional = valorNutricional;
        this.tipo = tipo;
    }

    public int getValorNutricional() {
        return valorNutricional;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public void usar(Personagem jogador) {
        // Lógica para consumir o alimento
        System.out.println(jogador.getNome() + " consumiu " + getNome() + ".");
        jogador.setFome(jogador.getFome() + valorNutricional);
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
    }
}