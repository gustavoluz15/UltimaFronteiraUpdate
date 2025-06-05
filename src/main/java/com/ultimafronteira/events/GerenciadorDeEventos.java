package com.ultimafronteira.events;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.world.Ambiente;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class GerenciadorDeEventos {
    private List<Evento> todosOsEventosPossiveis;
    private Random randomizador;
    private Evento eventoSorteadoAtual;

    public GerenciadorDeEventos() {
        this.todosOsEventosPossiveis = new ArrayList<>();
        this.randomizador = new Random();
        this.eventoSorteadoAtual = null;
        inicializarEventosPadrao();
    }

    private void inicializarEventosPadrao() {
        this.todosOsEventosPossiveis.clear();

        adicionarEventoPossivel(new EventoClimatico());
        adicionarEventoPossivel(new EventoCriatura()); // O construtor de EventoCriatura definirá o tipo de criatura internamente.
        adicionarEventoPossivel(new EventoDescoberta());
        adicionarEventoPossivel(new EventoDoencaFerimento());

    }

    public void adicionarEventoPossivel(Evento evento) {
        if (evento != null) {
            this.todosOsEventosPossiveis.add(evento);
        }
    }

    public String sortearEExecutarEvento(Personagem jogador, Ambiente local, int numeroDoTurno) {
        this.eventoSorteadoAtual = null;
        if (todosOsEventosPossiveis.isEmpty()) {
            return "O ar está estranhamente calmo.";
        }

        List<Evento> eventosCandidatos = new ArrayList<>(todosOsEventosPossiveis);
        java.util.Collections.shuffle(eventosCandidatos, randomizador);

        for (Evento evento : eventosCandidatos) {
            if (evento.tentarOcorrer(jogador, local, numeroDoTurno)) {
                this.eventoSorteadoAtual = evento;
                StringBuilder sb = new StringBuilder();
                if (evento instanceof EventoCriatura) {
                    sb.append("--- CONFRONTO IMINENTE! ---\n");
                } else {
                    sb.append("--- OCORREU UM EVENTO! ---\n");
                }
                sb.append(evento.executar(jogador, local, numeroDoTurno));
                return sb.toString();
            }
        }
        return "Nenhum evento especial ocorreu desta vez.";
    }

    public Evento getEventoSorteadoAtual() {
        return eventoSorteadoAtual;
    }

    public EventoCriatura getEventoSorteadoAtualComoCriatura() {
        if (eventoSorteadoAtual instanceof EventoCriatura) {
            return (EventoCriatura) eventoSorteadoAtual;
        }
        return null;
    }

    public void limparEventoSorteadoAtual() {
        this.eventoSorteadoAtual = null;
    }
}