package com.ultimafronteira.model;

public class Remedio extends Item {
    private String tipoEfeito;
    private int valorEfeito;

    public Remedio(String nome, double peso, int durabilidade, String tipoEfeito, int valorEfeito, String chaveImagem) {
        super(nome, peso, durabilidade, chaveImagem);
        this.tipoEfeito = tipoEfeito;
        this.valorEfeito = valorEfeito;
    }

    public String getTipoEfeito() { return tipoEfeito; }
    public int getValorEfeito() { return valorEfeito; }

    @Override
    public String usar(Personagem jogador) {
        if (jogador == null) return "Ninguém para usar o item.";
        StringBuilder sb = new StringBuilder();

        switch (tipoEfeito.toLowerCase()) {
            case "vida":
                int vidaAntes = jogador.getVida();
                jogador.setVida(jogador.getVida() + valorEfeito);
                sb.append("Vida recuperada em ").append(jogador.getVida() - vidaAntes).append(" pontos.");
                break;
            case "sanidade":
                int sanidadeAntes = jogador.getSanidade();
                jogador.setSanidade(jogador.getSanidade() + valorEfeito);
                sb.append("Sanidade recuperada em ").append(jogador.getSanidade() - sanidadeAntes).append(" pontos.");
                break;
            case "energia":
                int energiaAntes = jogador.getEnergia();
                jogador.setEnergia(jogador.getEnergia() + valorEfeito);
                sb.append("Energia recuperada em ").append(jogador.getEnergia() - energiaAntes).append(" pontos.");
                break;
            default:
                sb.append("Mas parece não ter tido um efeito claro...");
                break;
        }

        setDurabilidade(getDurabilidade() - 1);
        return sb.toString();
    }
}