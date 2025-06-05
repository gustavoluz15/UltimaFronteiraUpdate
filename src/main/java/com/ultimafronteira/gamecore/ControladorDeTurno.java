package com.ultimafronteira.gamecore;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.events.GerenciadorDeEventos;
import com.ultimafronteira.world.Ambiente;

public class ControladorDeTurno {
    private Personagem jogador;
    private GerenciadorDeEventos gerenciadorDeEventos;
    private int numeroDoTurno;
    private boolean jogoTerminou;
    private String mensagemFimDeJogo;
    private final int TURNOS_PARA_VITORIA_TEMPO = 50;

    public ControladorDeTurno(Personagem jogador, GerenciadorDeEventos gerenciadorDeEventos) {
        this.jogador = jogador;
        this.gerenciadorDeEventos = gerenciadorDeEventos;
        this.numeroDoTurno = 1; //
        this.jogoTerminou = false;
        this.mensagemFimDeJogo = "";
    }

    public boolean isJogoTerminou() { return jogoTerminou; }
    public String getMensagemFimDeJogo() { return mensagemFimDeJogo; }
    public int getNumeroDoTurno() { return numeroDoTurno; }

    public void forcarFimDeJogo(String mensagem) {
        if (!this.jogoTerminou) {
            this.jogoTerminou = true;
            this.mensagemFimDeJogo = mensagem;
        }
    }

    private void verificarCondicoesDeFimDeJogo() {
        if (jogoTerminou) return;
        if (jogador.getVida() <= 0) {
            forcarFimDeJogo(jogador.getNome() + " não resistiu aos ferimentos.\nFIM DE JOGO - DERROTA");
        } else if (jogador.getSanidade() <= 0) {
            forcarFimDeJogo(jogador.getNome() + " perdeu a sanidade para a solidão.\nFIM DE JOGO - DERROTA");
        } else if (jogador.getFome() <= 0) {
            forcarFimDeJogo(jogador.getNome() + " sucumbiu à inanição.\nFIM DE JOGO - DERROTA");
        } else if (jogador.getSede() <= 0) {
            forcarFimDeJogo(jogador.getNome() + " não resistiu à desidratação.\nFIM DE JOGO - DERROTA");
        } else if (this.numeroDoTurno > TURNOS_PARA_VITORIA_TEMPO) {
            forcarFimDeJogo(jogador.getNome() + " sobreviveu por " + TURNOS_PARA_VITORIA_TEMPO + " longos dias!\nFIM DE JOGO - VITÓRIA!");
        }
    }

    public String executarProximoTurno() {
        if (jogoTerminou) return "O jogo já terminou.\n" + mensagemFimDeJogo;
        this.numeroDoTurno++;
        String logDoTurno = faseDeManutencao();
        verificarCondicoesDeFimDeJogo();
        if(jogoTerminou) {
            logDoTurno += "\n" + mensagemFimDeJogo;
        }
        return logDoTurno;
    }

    private String faseDeManutencao() {
        StringBuilder sb = new StringBuilder();
        jogador.setVida(jogador.getVida() - 20);
        jogador.setFome(jogador.getFome() - 10);
        jogador.setSede(jogador.getSede() - 10);
        jogador.setEnergia(jogador.getEnergia() - 10);
        jogador.setSanidade(jogador.getSanidade() - 10);
        sb.append("O tempo cobra seu preço...");
        return sb.toString();
    }
}