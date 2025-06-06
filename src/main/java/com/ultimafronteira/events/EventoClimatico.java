package com.ultimafronteira.events;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.world.Ambiente;

public class EventoClimatico extends Evento {
    private String tipoDeClima;
    private int duracaoBase;
    private int duracaoAtual;
    private String efeitoNoAmbiente;
    private String chaveImagemFundoOverride;

    public EventoClimatico(String nome, String descricao, double probabilidade,
                           String tipoDeClima, int duracao, String efeitoNoAmbiente, String chaveImagemFundoOverride) {
        super(nome, descricao, probabilidade, "Clima/Ambiente");
        this.tipoDeClima = tipoDeClima;
        this.duracaoBase = duracao;
        this.duracaoAtual = duracao;
        this.efeitoNoAmbiente = efeitoNoAmbiente;
        this.chaveImagemFundoOverride = chaveImagemFundoOverride;
    }

    public String getTipoDeClima() { return tipoDeClima; }
    public String getChaveImagemFundoOverride() { return chaveImagemFundoOverride; }
    public int getDuracao() { return duracaoAtual; }
    public void decrementarDuracao() { this.duracaoAtual = Math.max(0, this.duracaoAtual - 1); }

    @Override
    public String executar(Personagem jogador, Ambiente local, int numeroDoTurno) {
        StringBuilder sb = new StringBuilder();
        sb.append(getDescricao()).append("\n");
        sb.append("Efeito: ").append(efeitoNoAmbiente);
        return sb.toString();
    }
}