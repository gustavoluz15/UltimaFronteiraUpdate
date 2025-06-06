package com.ultimafronteira.events;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.world.Ambiente;
import java.util.Random;

public class EventoCriatura extends Evento {

    private String tipoDeCriatura;
    private int vidaBaseCriatura;
    private int vidaAtualCriatura;
    private int danoBaseCriatura;
    private String chaveImagem;
    private Random random = new Random();

    public EventoCriatura(String ataqueDeLoboFaminto, String s, double v, String loboFaminto, String médio, int i, int i1, String criaturaLobo, String[] strings) {
        super("Encontro Hostil", "Algo se move nas sombras, observando você.", 0.25, "Confronto com Criatura");
    }

    public String getTipoDeCriatura() { return tipoDeCriatura; }
    public int getVidaAtualCriatura() { return vidaAtualCriatura; }
    public int getVidaMaximaCriatura() { return this.vidaBaseCriatura; }
    public String getChaveImagem() { return this.chaveImagem; }

    @Override
    public String executar(Personagem jogador, Ambiente local, int numeroDoTurno) {
        int criaturaSorteada = random.nextInt(3);

        switch (criaturaSorteada) {
            case 0:
                this.tipoDeCriatura = "Lobo Faminto";
                this.vidaBaseCriatura = 12;
                this.danoBaseCriatura = 10;
                this.chaveImagem = "criatura_lobo";
                break;
            case 1:
                this.tipoDeCriatura = "Cobra Venenosa";
                this.vidaBaseCriatura = 10;
                this.danoBaseCriatura = 8;
                this.chaveImagem = "criatura_cobra";
                break;
            case 2:
                this.tipoDeCriatura = "Corvo Ameaçador";
                this.vidaBaseCriatura = 8;
                this.danoBaseCriatura = 5;
                this.chaveImagem = "criatura_corvo";
                break;
        }

        this.vidaAtualCriatura = this.vidaBaseCriatura + (numeroDoTurno / 2);

        return "Um " + this.tipoDeCriatura + " surge, pronto para atacar!";
    }

    public String receberDano(int danoRecebido) {
        this.vidaAtualCriatura -= danoRecebido;
        this.vidaAtualCriatura = Math.max(0, this.vidaAtualCriatura);
        if (this.vidaAtualCriatura == 0) {
            return tipoDeCriatura + " foi derrotado(a)!\n";
        }
        return tipoDeCriatura + " recebeu " + danoRecebido + " de dano.";
    }

    public int calcularDanoEfetivoCriatura(int numeroDoTurno) {
        return this.danoBaseCriatura + random.nextInt(3) + (numeroDoTurno / 5);
    }
}