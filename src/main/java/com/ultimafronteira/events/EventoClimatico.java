package com.ultimafronteira.events;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.world.Ambiente;
import java.util.Random;

public class EventoClimatico extends Evento {

    private int duracao;
    private String chaveImagemFundoOverride;
    private Random random = new Random();

    public EventoClimatico() {
        super("Mudança Climática Súbita", "O tempo muda inesperadamente.", 0.20, "Clima/Ambiente");
        this.duracao = 0;
        this.chaveImagemFundoOverride = null;
    }

    public int getDuracao() { return duracao; }
    public String getChaveImagemFundoOverride() { return chaveImagemFundoOverride; }
    public void decrementarDuracao() { if (this.duracao > 0) this.duracao--; }

    @Override
    public String executar(Personagem jogador, Ambiente local, int numeroDoTurno) {
        int climaSorteado = random.nextInt(3);
        this.chaveImagemFundoOverride = null;

        switch (climaSorteado) {
            case 0:
                this.nome = "Nevasca Repentina";
                this.descricao = "Uma nevasca forte e inesperada cobre a área.";
                this.duracao = 2;
                if (local.getNome().contains("Geada")) {
                    this.chaveImagemFundoOverride = "fundo_pico_geada_nevasca";
                }
                jogador.setEnergia(jogador.getEnergia() - 15);
                return getDescricao() + " O frio intenso drena sua energia.";

            case 1:
                this.nome = "Tempestade Violenta";
                this.descricao = "Nuvens escuras se formam e uma tempestade desaba.";
                this.duracao = 3;
                if (local.getNome().contains("Geada")) {
                    this.chaveImagemFundoOverride = "fundo_pico_geada_tempestade";
                } else {
                    this.chaveImagemFundoOverride = "fundo_tempestade"; // Fundo genérico de tempestade
                }
                jogador.setEnergia(jogador.getEnergia() - 10);
                return getDescricao() + " A chuva e o vento dificultam a jornada.";

            case 2:
                this.nome = "Onda de Calor Intenso";
                this.descricao = "O sol castiga impiedosamente, elevando a temperatura.";
                this.duracao = 2;
                this.chaveImagemFundoOverride = "fundo_calor";
                jogador.setSede(jogador.getSede() - 20);
                return getDescricao() + " Sua garganta seca rapidamente.";
        }
        return "O tempo permanece instável.";
    }
}