package com.ultimafronteira.gamecore;

import com.ultimafronteira.events.GerenciadorDeEventos;
import com.ultimafronteira.model.Personagem;
// Removido import desnecessário: import com.ultimafronteira.world.Ambiente;

public class ControladorDeTurno {
    private Personagem jogador;
    private GerenciadorDeEventos gerenciadorDeEventos;
    private int numeroDoTurno;
    private boolean jogoTerminou;
    private String mensagemFimDeJogo;
    private final int TURNOS_PARA_VITORIA_TEMPO = 50; // Aumentado para um jogo mais longo

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
            this.jogoTerminou = true;
            this.mensagemFimDeJogo = "FIM DE JOGO - " + mensagem;
        }
    }

    private void verificarCondicoesDeFimDeJogo() {
        if (jogoTerminou) return;
        if (jogador.getVida() <= 0) {
            forcarFimDeJogo("DERROTA\n" + jogador.getNome() + " não resistiu aos ferimentos.");
        } else if (jogador.getSanidade() <= 0) {
            forcarFimDeJogo("DERROTA\n" + jogador.getNome() + " perdeu a sanidade para a solidão.");
        } else if (jogador.getFome() <= 0 && jogador.getVida() > 0) { // Verifica se já morreu por outros motivos
            forcarFimDeJogo("DERROTA\n" + jogador.getNome() + " sucumbiu à inanição.");
        } else if (jogador.getSede() <= 0 && jogador.getVida() > 0) { // Verifica se já morreu por outros motivos
            forcarFimDeJogo("DERROTA\n" + jogador.getNome() + " não resistiu à desidratação.");
        } else if (this.numeroDoTurno > TURNOS_PARA_VITORIA_TEMPO) {
            forcarFimDeJogo("VITÓRIA!\n" + jogador.getNome() + " sobreviveu por " + TURNOS_PARA_VITORIA_TEMPO + " longos dias!");
        }
    }

    public String executarAcaoDoJogador(Acao acao) {
        if (jogoTerminou) return "O jogo já terminou.\n" + mensagemFimDeJogo;

        StringBuilder logDoTurno = new StringBuilder();
        logDoTurno.append("--- TURNO ").append(numeroDoTurno).append(" ---\n");

        switch (acao) {
            case EXPLORAR:
                // CORREÇÃO: Chamando o método explorarAmbiente com a assinatura correta.
                logDoTurno.append(jogador.explorarAmbiente(numeroDoTurno));
                break;
            case DESCANSAR:
                int energiaRecuperada = 20 + (jogador.temHabilidade("Descanso Eficaz") ? 10 : 0); // Exemplo de bônus
                jogador.setEnergia(Math.min(100, jogador.getEnergia() + energiaRecuperada));
                logDoTurno.append("Você descansa e recupera ").append(energiaRecuperada).append(" de energia.\n");
                break;
            case AVANCAR_TURNO:
                logDoTurno.append("Você decide esperar e observar o ambiente...\n");
                break;
        }
        verificarCondicoesDeFimDeJogo();
        if (jogoTerminou) return logDoTurno.append(mensagemFimDeJogo).append("\n").toString();

        String resultadoEvento = gerenciadorDeEventos.sortearEExecutarEvento(jogador, jogador.getLocalizacaoAtual(), this.numeroDoTurno);
        logDoTurno.append(resultadoEvento).append("\n");
        verificarCondicoesDeFimDeJogo();
        if (jogoTerminou) return logDoTurno.append(mensagemFimDeJogo).append("\n").toString();

        logDoTurno.append(faseDeManutencao());
        verificarCondicoesDeFimDeJogo();
        if (jogoTerminou) return logDoTurno.append(mensagemFimDeJogo).append("\n").toString();

        this.numeroDoTurno++;
        return logDoTurno.toString();
    }

    private String faseDeManutencao() {
        StringBuilder sb = new StringBuilder("\nO tempo passa...\n");
        int fomePerdida = 5 + (numeroDoTurno / 10);
        int sedePerdida = 7 + (numeroDoTurno / 8);

        jogador.setFome(jogador.getFome() - fomePerdida);
        sb.append("- Fome aumenta (").append(fomePerdida).append(")\n");
        if (jogador.getFome() <= 0) {
            int danoFome = 2 + (numeroDoTurno / 15);
            jogador.setVida(jogador.getVida() - danoFome);
            sb.append("FOME CRÍTICA! Você perde ").append(danoFome).append(" de vida.\n");
        }

        jogador.setSede(jogador.getSede() - sedePerdida);
        sb.append("- Sede aumenta (").append(sedePerdida).append(")\n");
        if (jogador.getSede() <= 0) {
            int danoSede = 3 + (numeroDoTurno / 12);
            jogador.setVida(jogador.getVida() - danoSede);
            sb.append("SEDE CRÍTICA! Você perde ").append(danoSede).append(" de vida.\n");
        }
        return sb.toString();
    }
}