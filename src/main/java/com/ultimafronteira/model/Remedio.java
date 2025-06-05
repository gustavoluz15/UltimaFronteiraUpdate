package com.ultimafronteira.model;

public class Remedio extends Item {
<<<<<<< HEAD
    private String tipo; // Ex: Bandagem, Antídoto, Analgésico
    private String efeito; // Descrição do efeito, ex: "Cura 25 de vida", "Remove veneno"
    private int valorEfeito; // Quantidade numérica do efeito, se aplicável

    public Remedio(String nome, double peso, String tipo, String efeito, int valorEfeito, String chaveImagem) {
        super(nome, peso, 1, chaveImagem); // Remédios são geralmente consumíveis (durabilidade 1)
        this.tipo = tipo;
        this.efeito = efeito;
        this.valorEfeito = valorEfeito;
=======
    private String tipo;
    private String efeito;

    public Remedio(String nome, double peso, String tipo, String efeito) {
        super(nome, peso, 1);
        this.tipo = tipo;
        this.efeito = efeito;
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
    }

    public String getTipo() {
        return tipo;
    }

    public String getEfeito() {
        return efeito;
    }

<<<<<<< HEAD
    public int getValorEfeito() {
        return valorEfeito;
    }

    @Override
    public void usar(Personagem jogador) {
        System.out.println(jogador.getNome() + " usou o remédio: " + getNome() + " (" + tipo + ").");

        if (this.efeito.toLowerCase().contains("cura") && this.efeito.toLowerCase().contains("vida")) {
            int vidaAntes = jogador.getVida();
            jogador.setVida(jogador.getVida() + this.valorEfeito);
            System.out.println("Efeito: " + this.efeito + ". Vida restaurada em " + this.valorEfeito + " (de " + vidaAntes + " para " + jogador.getVida() + ").");
        } else if (this.efeito.toLowerCase().contains("cura") && this.efeito.toLowerCase().contains("sanidade")) {
            jogador.setSanidade(jogador.getSanidade() + this.valorEfeito);
            System.out.println("Efeito: " + this.efeito + ". Sanidade restaurada em " + this.valorEfeito + ".");
        } else {
            // Para outros efeitos, você pode adicionar mais condições ou uma descrição genérica
            System.out.println("Efeito aplicado: " + this.efeito);
=======
    @Override
    public void usar(Personagem jogador) {
        System.out.println(jogador.getNome() + " usou o remédio: " + getNome() + " (" + tipo + "). Efeito: " + efeito);
        // A lógica de aplicar o efeito (ex: curar vida, sanidade) será adicionada aqui
        // Exemplo:
        if (this.efeito.toLowerCase().contains("cura")) {
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
        }
    }
}