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

    // Define o número de turnos necessários para alcançar a vitória por tempo.
    private final int TURNOS_PARA_VITORIA_TEMPO = 10; // Escolhido o valor menor para facilitar testes.

    /**
     * Construtor do ControladorDeTurno.
     * @param jogador O personagem do jogador.
     * @param gerenciadorDeEventos O gerenciador de eventos do jogo.
     */
    public ControladorDeTurno(Personagem jogador, GerenciadorDeEventos gerenciadorDeEventos) {
        this.jogador = jogador;
        this.gerenciadorDeEventos = gerenciadorDeEventos;
        this.numeroDoTurno = 0; // O jogo começa no turno 0, o primeiro turno efetivo será 1.
        this.jogoTerminou = false;
        this.mensagemFimDeJogo = "";
    }

    /**
     * Verifica se o jogo terminou.
     * @return true se o jogo terminou, false caso contrário.
     */
    public boolean isJogoTerminou() {
        return jogoTerminou;
    }

    /**
     * Obtém a mensagem de fim de jogo.
     * @return A mensagem final do jogo.
     */
    public String getMensagemFimDeJogo() {
        return mensagemFimDeJogo;
    }

    /**
     * Define o estado de fim de jogo e a mensagem correspondente.
     * @param mensagem A mensagem a ser exibida no fim do jogo.
     */
    private void terminarJogo(String mensagem) {
        if (!this.jogoTerminou) { // Garante que a mensagem de fim de jogo seja definida apenas uma vez.
            this.jogoTerminou = true;
            this.mensagemFimDeJogo = mensagem;
        }
    }

    /**
     * Verifica as condições de fim de jogo (derrota ou vitória).
     * Se uma condição de fim for atendida, o jogo é terminado.
     * @return Uma string com a mensagem de fim de jogo se o jogo terminou nesta verificação, caso contrário, uma string vazia.
     */
    private String verificarCondicoesDeFimDeJogo() {
        if (jogoTerminou) { // Se o jogo já terminou em uma fase anterior deste mesmo turno.
            return this.mensagemFimDeJogo + "\n"; // Retorna a mensagem já definida.
        }

        // Condições de Derrota
        if (jogador.getVida() <= 0) {
            terminarJogo(jogador.getNome() + " não resistiu aos perigos e sucumbiu.\nFIM DE JOGO - DERROTA");
            return this.mensagemFimDeJogo + "\n";
        }
        if (jogador.getSanidade() <= 0) {
            terminarJogo(jogador.getNome() + " perdeu completamente a sanidade, entregando-se à loucura.\nFIM DE JOGO - DERROTA");
            return this.mensagemFimDeJogo + "\n";
        }
        // Outras condições de derrota (fome, sede) são verificadas e podem levar à perda de vida na faseDeManutencao.

        // Condições de Vitória
        if (this.numeroDoTurno >= TURNOS_PARA_VITORIA_TEMPO) {
            terminarJogo(jogador.getNome() + " sobreviveu por " + TURNOS_PARA_VITORIA_TEMPO + " longos turnos! Uma façanha incrível!\nFIM DE JOGO - VITÓRIA!");
            return this.mensagemFimDeJogo + "\n";
        }

        return ""; // Jogo continua.
    }

    /**
     * Executa a lógica completa de um próximo turno do jogo.
     * Inclui fase de início, evento aleatório e manutenção.
     * Verifica condições de fim de jogo após cada fase crítica.
     * @return Um log de todas as ocorrências e estados do turno.
     */
    public String executarProximoTurno() {
        if (jogoTerminou) {
            return "O jogo já terminou.\n" + mensagemFimDeJogo;
        }

        this.numeroDoTurno++;
        StringBuilder logDoTurno = new StringBuilder();

        logDoTurno.append("--- INÍCIO DO TURNO ").append(numeroDoTurno).append(" ---\n");
        logDoTurno.append(faseDeInicio()); // Efeitos do ambiente, clima, etc.

        String fimDeJogoLog = verificarCondicoesDeFimDeJogo();
        if(jogoTerminou) {
            logDoTurno.append(fimDeJogoLog);
            return logDoTurno.toString();
        }

        // Espaço para ações do jogador, se implementado de forma interativa.
        // logDoTurno.append("--- Fase de Ação do Jogador (Aguardando entrada do jogador) ---\n");

        logDoTurno.append(faseDeEventoAleatorio()); // Sorteia e executa um evento.
        fimDeJogoLog = verificarCondicoesDeFimDeJogo();
        if(jogoTerminou) {
            logDoTurno.append(fimDeJogoLog);
            return logDoTurno.toString();
        }

        logDoTurno.append(faseDeManutencao()); // Consumo de recursos, fome, sede.
        fimDeJogoLog = verificarCondicoesDeFimDeJogo();
        if(jogoTerminou) {
            logDoTurno.append(fimDeJogoLog);
        }

        logDoTurno.append("--- FIM DO TURNO ").append(numeroDoTurno).append(" ---\n");
        return logDoTurno.toString();
    }

    /**
     * Lógica para a fase de início do turno.
     * Pode incluir mudanças climáticas ou efeitos do ambiente.
     * @return Log da fase de início.
     */
    private String faseDeInicio() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fase de Início:\n");
        Ambiente ambienteAtual = jogador.getLocalizacaoAtual();
        if (ambienteAtual != null) {
            // Se houver mecânica de mudança de clima no Ambiente:
            // String climaMsg = ambienteAtual.modificarClima();
            // sb.append(climaMsg);
            sb.append("Condições atuais em ").append(ambienteAtual.getNome())
                    // .append(": ").append(ambienteAtual.getCondicoesClimaticasPredominantes()) // Se existir tal método
                    .append(".\n");
        } else {
            sb.append("Localização do jogador é desconhecida.\n");
        }
        // Outras lógicas de início de turno podem ser adicionadas aqui.
        return sb.toString();
    }

    /**
     * Lógica para a fase de evento aleatório.
     * Sorteia um evento do GerenciadorDeEventos e o executa.
     * @return Log da fase de evento.
     */
    private String faseDeEventoAleatorio() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fase de Evento Aleatório:\n");
        if (gerenciadorDeEventos != null) {
            String resultadoEvento = gerenciadorDeEventos.sortearEExecutarEvento(jogador, jogador.getLocalizacaoAtual(), this.numeroDoTurno);
            sb.append(resultadoEvento).append("\n");
        } else {
            sb.append("Gerenciador de eventos não está configurado.\n");
        }
        return sb.toString();
    }

    /**
     * Lógica para a fase de manutenção do turno.
     * Inclui perda de fome, sede e possíveis danos por fome/sede críticas.
     * @return Log da fase de manutenção.
     */
    private String faseDeManutencao() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fase de Manutenção:\n");

        // Lógica de fome
        int fomePerdida = 5 + (numeroDoTurno / 10); // Fome perdida aumenta com o tempo
        jogador.setFome(Math.max(0, jogador.getFome() - fomePerdida));
        sb.append(jogador.getNome()).append(" perdeu ").append(fomePerdida).append(" pontos de fome. (Fome atual: ").append(jogador.getFome()).append(")\n");
        if (jogador.getFome() == 0 && jogador.getVida() > 0) {
            int danoFome = 2 + numeroDoTurno / 15; // Dano por fome aumenta com o tempo
            jogador.setVida(Math.max(0, jogador.getVida() - danoFome));
            sb.append("FOME CRÍTICA! ").append(jogador.getNome()).append(" perde ").append(danoFome).append(" de vida. (Vida atual: ").append(jogador.getVida()).append(")\n");
        }

        // Lógica de sede
        int sedePerdida = 7 + (numeroDoTurno / 8); // Sede perdida aumenta com o tempo
        jogador.setSede(Math.max(0, jogador.getSede() - sedePerdida));
        sb.append(jogador.getNome()).append(" perdeu ").append(sedePerdida).append(" pontos de sede. (Sede atual: ").append(jogador.getSede()).append(")\n");
        if (jogador.getSede() == 0 && jogador.getVida() > 0) {
            int danoSede = 3 + numeroDoTurno / 12; // Dano por sede aumenta com o tempo
            jogador.setVida(Math.max(0, jogador.getVida() - danoSede));
            sb.append("SEDE CRÍTICA! ").append(jogador.getNome()).append(" perde ").append(danoSede).append(" de vida. (Vida atual: ").append(jogador.getVida()).append(")\n");
        }

        // Poderia haver também perda de energia ou sanidade base por turno aqui.
        // jogador.setEnergia(Math.max(0, jogador.getEnergia() - (5 + numeroDoTurno / 20) ));
        // jogador.setSanidade(Math.max(0, jogador.getSanidade() - (1 + numeroDoTurno / 25) ));
        // sb.append("O tempo e o cansaço cobram seu preço...\n");

        return sb.toString();
    }

    /**
     * Obtém o número do turno atual.
     * @return O número do turno.
     */
    public int getNumeroDoTurno() {
        return numeroDoTurno;
    }
}