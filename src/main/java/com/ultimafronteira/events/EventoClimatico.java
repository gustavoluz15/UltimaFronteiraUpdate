package com.ultimafronteira.events;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.world.Ambiente;
<<<<<<< HEAD
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
=======

public class EventoClimatico extends Evento {
    private String tipoDeClima;
    private int duracaoTurnosBase;
    private String efeitoNoAmbiente;

    public EventoClimatico(String nome, String descricao, double probabilidade,
                           String tipoDeClima, int duracaoTurnosBase, String efeitoNoAmbiente) {
        super(nome, descricao, probabilidade, "Clima/Ambiente");
        this.tipoDeClima = tipoDeClima;
        this.duracaoTurnosBase = duracaoTurnosBase;
        this.efeitoNoAmbiente = efeitoNoAmbiente;
    }

    public String getTipoDeClima() {
        return tipoDeClima;
    }

    public int getDuracaoTurnosBase() {
        return duracaoTurnosBase;
    }

    public String getEfeitoNoAmbiente() {
        return efeitoNoAmbiente;
    }

    @Override
    public String executar(Personagem jogador, Ambiente local, int numeroDoTurno) {
        StringBuilder sb = new StringBuilder();
        sb.append("EVENTO CLIMÁTICO: ").append(getNome()).append("!\n");
        sb.append(getDescricao()).append("\n");
        sb.append("Tipo de Clima: ").append(tipoDeClima).append("\n");
        sb.append("Efeito no Ambiente: ").append(efeitoNoAmbiente).append("\n");

        if (local != null) {
            sb.append("O ambiente ").append(local.getNome()).append(" é afetado por: ").append(tipoDeClima).append("\n");
        }

        if ("Nevasca".equals(tipoDeClima)) {
            sb.append(jogador.getNome()).append(" sente o frio intenso da nevasca!\n");
            jogador.setEnergia(jogador.getEnergia() - (15 + numeroDoTurno / 5)); // Efeito escala um pouco
            jogador.setSede(jogador.getSede() - (5 + numeroDoTurno / 10));
        } else if ("Tempestade".equals(tipoDeClima)) {
            sb.append("Uma forte tempestade dificulta a movimentação de ").append(jogador.getNome()).append("\n");
            jogador.setEnergia(jogador.getEnergia() - (10 + numeroDoTurno / 5));
        } else if ("Calor Extremo".equals(tipoDeClima)) {
            sb.append(jogador.getNome()).append(" sofre com o calor escaldante.\n");
            jogador.setSede(jogador.getSede() - (20 + numeroDoTurno / 3));
            jogador.setEnergia(jogador.getEnergia() - (5 + numeroDoTurno / 10));
        }
        sb.append("Este evento pode durar aproximadamente ").append(duracaoTurnosBase).append(" turnos.\n");
        return sb.toString();
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
    }
}