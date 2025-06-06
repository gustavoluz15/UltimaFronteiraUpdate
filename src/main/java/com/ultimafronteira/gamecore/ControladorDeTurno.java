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

    private final int TURNOS_PARA_VITORIA_TEMPO = 10;

    public ControladorDeTurno(Personagem jogador, GerenciadorDeEventos gerenciadorDeEventos) {
        this.jogador = jogador;
        this.gerenciadorDeEventos = gerenciadorDeEventos;
        this.numeroDoTurno = 1;
        this.jogoTerminou = false;
        this.mensagemFimDeJogo = "";
    }

    public boolean isJogoTerminou() { return jogoTerminou; }
    public String getMensagemFimDeJogo() { return mensagemFimDeJogo; }
    public int getNumeroDoTurno() { return numeroDoTurno; }

    public void forcarFimDeJogo(String mensagem) {
        if (!this.jogoTerminou) {
            terminarJogo(mensagem);
        }
    }

    private void terminarJogo(String mensagem) {
        this.jogoTerminou = true;
        this.mensagemFimDeJogo = mensagem;
    }

    public void verificarCondicoesDeFimDeJogo() {
        if (jogoTerminou) return;

        if (jogador.getVida() <= 0) {
            terminarJogo(jogador.getNome() + " não resistiu aos perigos e sucumbiu.\nFIM DE JOGO - DERROTA");
        } else if (jogador.getSanidade() <= 0) {
            terminarJogo(jogador.getNome() + " perdeu a sanidade, entregando-se à loucura.\nFIM DE JOGO - DERROTA");
        } else if (jogador.getFome() <= 0) {
            terminarJogo(jogador.getNome() + " morreu de inanição.\nFIM DE JOGO - DERROTA");
        } else if (jogador.getSede() <= 0) {
            terminarJogo(jogador.getNome() + " morreu de desidratação.\nFIM DE JOGO - DERROTA");
        }

        if (!jogoTerminou && this.numeroDoTurno >= TURNOS_PARA_VITORIA_TEMPO) {
            terminarJogo(jogador.getNome() + " sobreviveu por " + TURNOS_PARA_VITORIA_TEMPO + " dias! Uma façanha incrível!\nFIM DE JOGO - VITÓRIA!");
        }
    }

    public String executarProximoTurno() {
        if (jogoTerminou) { return "O jogo já terminou.\n" + mensagemFimDeJogo; }

        StringBuilder logDoTurno = new StringBuilder();

        String logManutencao = faseDeManutencao();
        logDoTurno.append(logManutencao); // Adiciona o log da manutenção

        verificarCondicoesDeFimDeJogo();
        if(jogoTerminou) {
            logDoTurno.append("\n").append(this.mensagemFimDeJogo);
            return logDoTurno.toString();
        }

        this.numeroDoTurno++;
        logDoTurno.append("\n--- Início do Dia ").append(numeroDoTurno).append(" ---\n");
        logDoTurno.append(faseDeInicio());

        String logEvento = faseDeEventoAleatorio();
        logDoTurno.append(logEvento);
        verificarCondicoesDeFimDeJogo();
        if(jogoTerminou) { logDoTurno.append("\n").append(this.mensagemFimDeJogo); }

        return logDoTurno.toString();
    }

    private String faseDeInicio() {
        Ambiente ambienteAtual = jogador.getLocalizacaoAtual();
        if (ambienteAtual != null) {
            return ambienteAtual.modificarClima();
        }
        return "Um novo dia começa.\n";
    }

    private String faseDeEventoAleatorio() {
        return gerenciadorDeEventos.sortearEExecutarEvento(jogador, jogador.getLocalizacaoAtual(), this.numeroDoTurno);
    }

    private String faseDeManutencao() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Fim do Dia ").append(numeroDoTurno).append(" ---\n");
        int fomePerdida = 5 + (numeroDoTurno / 10);
        int sedePerdida = 7 + (numeroDoTurno / 8);

        if (jogador.temHabilidade("Metabolismo Eficiente")) {
            fomePerdida = (int) (fomePerdida * 0.7);
            sedePerdida = (int) (sedePerdida * 0.7);
            sb.append("Seu metabolismo eficiente reduz o cansaço da noite.\n");
        }

        jogador.setFome(jogador.getFome() - fomePerdida);
        jogador.setSede(jogador.getSede() - sedePerdida);
        sb.append("A noite cai... Fome -").append(fomePerdida).append(", Sede -").append(sedePerdida).append(".\n");

        return sb.toString();
    }
}