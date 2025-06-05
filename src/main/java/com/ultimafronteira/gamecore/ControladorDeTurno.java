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
<<<<<<< HEAD
    private final int TURNOS_PARA_VITORIA_TEMPO = 50;
=======

    private final int TURNOS_PARA_VITORIA_TEMPO = 10;
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd

    public ControladorDeTurno(Personagem jogador, GerenciadorDeEventos gerenciadorDeEventos) {
        this.jogador = jogador;
        this.gerenciadorDeEventos = gerenciadorDeEventos;
<<<<<<< HEAD
        this.numeroDoTurno = 1; //
=======
        this.numeroDoTurno = 0;
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
        this.jogoTerminou = false;
        this.mensagemFimDeJogo = "";
    }

<<<<<<< HEAD
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
=======
    public boolean isJogoTerminou() {
        return jogoTerminou;
    }

    public String getMensagemFimDeJogo() {
        return mensagemFimDeJogo;
    }

    private void terminarJogo(String mensagem) {
        this.jogoTerminou = true;
        this.mensagemFimDeJogo = mensagem;
    }

    private String verificarCondicoesDeFimDeJogo() {
        if (jogoTerminou) { // Se já terminou em uma fase anterior deste turno
            return "";
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

        // Condições de Vitória
        if (this.numeroDoTurno >= TURNOS_PARA_VITORIA_TEMPO) {
            terminarJogo(jogador.getNome() + " sobreviveu por " + TURNOS_PARA_VITORIA_TEMPO + " longos turnos! Uma façanha incrível!\nFIM DE JOGO - VITÓRIA!");
            return this.mensagemFimDeJogo + "\n";
        }

        return ""; // Continua
    }

    public String executarProximoTurno() {
        if (jogoTerminou) {
            return "O jogo já terminou.\n" + mensagemFimDeJogo;
        }

        this.numeroDoTurno++;
        StringBuilder logDoTurno = new StringBuilder();

        logDoTurno.append("--- INÍCIO DO TURNO ").append(numeroDoTurno).append(" ---\n");
        logDoTurno.append(faseDeInicio());

        String fimDeJogoAposInicio = verificarCondicoesDeFimDeJogo();
        if(jogoTerminou) {
            logDoTurno.append(fimDeJogoAposInicio);
            return logDoTurno.toString();
        }

        logDoTurno.append("--- Fase de Ação do Jogador (use os botões de ação) ---\n");

        logDoTurno.append(faseDeEventoAleatorio());
        String fimDeJogoAposEvento = verificarCondicoesDeFimDeJogo();
        if(jogoTerminou) {
            logDoTurno.append(fimDeJogoAposEvento);
            return logDoTurno.toString();
        }

        logDoTurno.append(faseDeManutencao());
        String fimDeJogoAposManutencao = verificarCondicoesDeFimDeJogo();
        if(jogoTerminou) {
            logDoTurno.append(fimDeJogoAposManutencao);
        }

        logDoTurno.append("--- FIM DO TURNO ").append(numeroDoTurno).append(" ---\n");
        return logDoTurno.toString();
    }

    private String faseDeInicio() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fase de Início:\n");
        Ambiente ambienteAtual = jogador.getLocalizacaoAtual();
        if (ambienteAtual != null) {
            String climaMsg = ambienteAtual.modificarClima();
            sb.append(climaMsg);
            sb.append("Condições atuais em ").append(ambienteAtual.getNome()).append(": ").append(ambienteAtual.getCondicoesClimaticasPredominantes()).append("\n");
        }
        return sb.toString();
    }

    private String faseDeEventoAleatorio() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fase de Evento Aleatório:\n");
        String resultadoEvento = gerenciadorDeEventos.sortearEExecutarEvento(jogador, jogador.getLocalizacaoAtual(), this.numeroDoTurno);
        sb.append(resultadoEvento).append("\n");
        return sb.toString();
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
    }

    private String faseDeManutencao() {
        StringBuilder sb = new StringBuilder();
<<<<<<< HEAD
        jogador.setVida(jogador.getVida() - 20);
        jogador.setFome(jogador.getFome() - 10);
        jogador.setSede(jogador.getSede() - 10);
        jogador.setEnergia(jogador.getEnergia() - 10);
        jogador.setSanidade(jogador.getSanidade() - 10);
        sb.append("O tempo cobra seu preço...");
        return sb.toString();
    }
=======
        sb.append("Fase de Manutenção:\n");

        int fomePerdida = 5 + (numeroDoTurno / 10);
        int sedePerdida = 7 + (numeroDoTurno / 8);

        jogador.setFome(Math.max(0, jogador.getFome() - fomePerdida));
        sb.append(jogador.getNome()).append(" perdeu ").append(fomePerdida).append(" pontos de fome.\n");
        if (jogador.getFome() == 0 && jogador.getVida() > 0) { // Só aplica dano se estiver vivo
            int danoFome = 2 + numeroDoTurno / 15;
            jogador.setVida(jogador.getVida() - danoFome);
            sb.append("FOME CRÍTICA! ").append(jogador.getNome()).append(" perde ").append(danoFome).append(" de vida.\n");
        }

        jogador.setSede(Math.max(0, jogador.getSede() - sedePerdida));
        sb.append(jogador.getNome()).append(" perdeu ").append(sedePerdida).append(" pontos de sede.\n");
        if (jogador.getSede() == 0 && jogador.getVida() > 0) { // Só aplica dano se estiver vivo
            int danoSede = 3 + numeroDoTurno / 12;
            jogador.setVida(jogador.getVida() - danoSede);
            sb.append("SEDE CRÍTICA! ").append(jogador.getNome()).append(" perde ").append(danoSede).append(" de vida.\n");
        }

        return sb.toString();
    }

    public int getNumeroDoTurno() {
        return numeroDoTurno;
    }
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
}