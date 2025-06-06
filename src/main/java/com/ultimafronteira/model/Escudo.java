package com.ultimafronteira.model;

public class Escudo extends Item {
    private int valorDeDefesa;
    private String tipoEscudo;

    public Escudo(String nome, double peso, int durabilidade, int valorDeDefesa, String tipoEscudo, String chaveImagem) {
        super(nome, peso, durabilidade, chaveImagem);
        this.valorDeDefesa = valorDeDefesa;
        this.tipoEscudo = tipoEscudo;
    }

    public int getValorDeDefesa() { return valorDeDefesa; }
    public String getTipoEscudo() { return tipoEscudo; }


    @Override
    public String usar(Personagem jogador) {
        if ("Buff".equalsIgnoreCase(this.tipoEscudo)) {
            jogador.aplicarBuffDefesa(10); // Nega -10 de dano no próximo ataque sofrido
            setDurabilidade(0); // Consome o item
            return getNome() + " usado! Você bloqueará os próximos 10 de dano.";
        } else {
            return getNome() + " é um escudo para ser equipado, não usado diretamente para buff.";
        }
    }
}