package com.ultimafronteira.events;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.world.Ambiente;

public class EventoClimatico extends Evento {
    private int duracao;
    private String chaveImagemFundoOverride;
    private String tipoDeClima;

    public EventoClimatico(String nome, String descricao, double probabilidade, String tipoDeClima, int duracao, String chaveImagemOverride) {
        super(nome, descricao, probabilidade, "Clima/Ambiente");
        this.tipoDeClima = tipoDeClima;
        this.duracao = duracao;
        this.chaveImagemFundoOverride = chaveImagemOverride;
    }

    public int getDuracao() { return duracao; }
    public String getChaveImagemFundoOverride() { return chaveImagemFundoOverride; }
    public void decrementarDuracao() { if (this.duracao > 0) this.duracao--; }

    @Override
    public String executar(Personagem jogador, Ambiente local, int numeroDoTurno) {
        switch (this.tipoDeClima) {
            case "Nevasca":
                jogador.setEnergia(jogador.getEnergia() - 15);
                return getDescricao() + " O frio intenso drena sua energia.";
            case "Tempestade":
                jogador.setEnergia(jogador.getEnergia() - 10);
                return getDescricao() + " A chuva e o vento dificultam a jornada.";
            case "Calor":
                jogador.setSede(jogador.getSede() - 20);
                return getDescricao() + " Sua garganta seca rapidamente.";
        }
        return "O tempo permanece instável.";
    }
}