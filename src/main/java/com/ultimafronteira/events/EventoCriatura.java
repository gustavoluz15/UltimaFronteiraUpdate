package com.ultimafronteira.events;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.world.Ambiente;
import java.util.Random;

public class EventoCriatura extends Evento {
    private String tipoDeCriatura;
    private int vidaBaseCriatura;
    private int danoBaseCriatura;
    private String chaveImagem;
    private int vidaAtualCriatura;
    private Random random = new Random();

    public EventoCriatura(String nome, String descricao, double probabilidade, String tipoDeCriatura, int vidaBase, int danoBase, String chaveImagem) {
        super(nome, descricao, probabilidade, "Confronto");
        this.tipoDeCriatura = tipoDeCriatura;
        this.vidaBaseCriatura = vidaBase;
        this.danoBaseCriatura = danoBase;
        this.chaveImagem = chaveImagem;
    }

    public String getTipoDeCriatura() { return tipoDeCriatura; }
    public int getVidaAtualCriatura() { return vidaAtualCriatura; }
    public int getVidaMaximaCriatura() { return this.vidaBaseCriatura; }
    public String getChaveImagem() { return this.chaveImagem; }

    @Override
    public String executar(Personagem jogador, Ambiente local, int numeroDoTurno) {
        // A vida da criatura escala um pouco com o progresso do jogo
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
        // Dano da criatura também escala um pouco
        return this.danoBaseCriatura + random.nextInt(3) + (numeroDoTurno / 5);
    }
}