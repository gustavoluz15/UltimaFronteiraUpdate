package com.ultimafronteira.events;

import com.ultimafronteira.model.Alimento;
import com.ultimafronteira.model.Item;
import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.world.Ambiente;

import java.util.ArrayList;
import java.util.List;
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

        // --- Eventos de Criatura ---
        adicionarEventoPossivel(new EventoCriatura("Ataque de Lobo", "Um lobo faminto surge da mata.", 0.25, "Lobo Faminto", 30, 10, "criatura_lobo"));
        adicionarEventoPossivel(new EventoCriatura("Cobra Sorrateira", "Uma cobra venenosa se prepara para o bote.", 0.15, "Cobra Venenosa", 15, 8, "criatura_cobra"));
        adicionarEventoPossivel(new EventoCriatura("Corvo Observador", "Um corvo encara você, parecendo inteligente demais.", 0.10, "Corvo Ameaçador", 10, 5, "criatura_corvo"));

        // --- Eventos Climáticos ---
        adicionarEventoPossivel(new EventoClimatico("Nevasca", "O céu se fecha e uma nevasca começa.", 0.10, "Nevasca", 2, "fundo_pico_geada_nevasca"));
        adicionarEventoPossivel(new EventoClimatico("Tempestade", "Nuvens pesadas trazem uma tempestade violenta.", 0.15, "Tempestade", 3, "fundo_tempestade"));
        adicionarEventoPossivel(new EventoClimatico("Onda de Calor", "O sol se torna impiedoso, secando o ar.", 0.12, "Calor", 2, "fundo_calor"));

        // --- Eventos de Descoberta ---
        adicionarEventoPossivel(new EventoDescoberta(
                "Mochila Rasgada",
                "Você encontra uma mochila abandonada.",
                0.20,
                List.of(new Alimento("Ração de Emergência", 0.2, 1, 25, 0, "Ração", "item_racao")), // Exemplo de item
                null
        ));
        adicionarEventoPossivel(new EventoDescoberta(
                "Carcaça de Animal",
                "Você encontra os restos de um animal. Talvez algo possa ser aproveitado.",
                0.15,
                List.of(new Alimento("Carne Crua", 0.8, 1, 5, -5, "Carne", "item_carne_crua")), // Exemplo, comer cru pode dar dano
                null
        ));

        // --- Eventos de Doença/Ferimento ---
        adicionarEventoPossivel(new EventoDoencaFerimento(
                "Tropeço e Queda",
                "Você tropeça em uma raiz e cai de mal jeito.",
                0.18,
                5, 10, 0, 0, 2 // danoVida, perdaEnergia, perdaFome, perdaSede, perdaSanidade
        ));
        adicionarEventoPossivel(new EventoDoencaFerimento(
                "Enxaqueca Súbita",
                "Uma dor de cabeça lancinante te atinge, dificultando a concentração.",
                0.10,
                0, 5, 0, 0, 10
        ));
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
                return evento.executar(jogador, local, numeroDoTurno);
            }
        }
        return "Nenhum evento especial ocorreu desta vez.";
    }

    public Evento getEventoSorteadoAtual() {
        return eventoSorteadoAtual;
    }

    public void limparEventoSorteadoAtual() {
        this.eventoSorteadoAtual = null;
    }
}