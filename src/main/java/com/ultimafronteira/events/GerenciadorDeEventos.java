package com.ultimafronteira.events;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.world.Ambiente;
import com.ultimafronteira.world.AmbienteCaverna;
import com.ultimafronteira.world.AmbienteMontanha;
import com.ultimafronteira.world.AmbienteLagoRio;
import com.ultimafronteira.world.AmbienteRuinas;
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

        adicionarEventoPossivel(new EventoMudancaDeAmbiente("Entrada de Caverna", "Você encontra a entrada escura de uma caverna!", 0.05, new AmbienteCaverna("Gruta Ecoante", "Uma caverna escura e úmida.", "fundo_caverna")));
        adicionarEventoPossivel(new EventoMudancaDeAmbiente("Trilha na Montanha", "Seus passos te levam ao início de uma trilha íngreme montanha acima!", 0.04, new AmbienteMontanha("Pico da Geada Eterna", "Uma montanha imponente...", "fundo_pico_geada")));
        adicionarEventoPossivel(new EventoMudancaDeAmbiente("Margem de Rio", "Você ouve o som de água e encontra a margem de um rio sinuoso!", 0.06, new AmbienteLagoRio("Rio da Serpente Prateada", "Um rio largo de águas calmas...", "fundo_lago_rio")));

        adicionarEventoPossivel(new EventoClimatico("Nevasca Repentina", "Uma nevasca forte e inesperada cobre a área.", 0.12, "Nevasca", 3, "Reduz visibilidade e aumenta o frio.", "fundo_pico_geada_nevasca"));
        adicionarEventoPossivel(new EventoCriatura("Ataque de Lobo Faminto", "Um lobo surge das sombras...", 0.18, "Lobo Faminto", "Médio", 10, 30, "criatura_lobo", new String[]{"Lutar", "Fugir"}));
        adicionarEventoPossivel(new EventoDescoberta("Suprimentos Deixados", "Você encontra uma mochila abandonada com alguns itens.", 0.15));
        adicionarEventoPossivel(new EventoDoencaFerimento("Corte Profundo Acidental", "Você se corta feio ao explorar.", 0.10, "Corte Profundo", "Perda de vida, risco de infecção.", "Bandagens"));
    }

    public void adicionarEventoPossivel(Evento evento) { if (evento != null) this.todosOsEventosPossiveis.add(evento); }

    public String sortearEExecutarEvento(Personagem jogador, Ambiente local, int numeroDoTurno) {
        this.eventoSorteadoAtual = null;
        if (todosOsEventosPossiveis.isEmpty()) return "Nada de diferente aconteceu.";

        List<Evento> eventosCandidatos = new ArrayList<>(todosOsEventosPossiveis);
        java.util.Collections.shuffle(eventosCandidatos, randomizador);

        for (Evento evento : eventosCandidatos) {
            if (local instanceof AmbienteCaverna && evento.getNome().contains("Caverna")) continue;
            if (local instanceof AmbienteMontanha && evento.getNome().contains("Montanha")) continue;
            if (local instanceof AmbienteLagoRio && evento.getNome().contains("Rio")) continue;

            if (evento.tentarOcorrer(jogador, local, numeroDoTurno)) {
                this.eventoSorteadoAtual = evento;
                return evento.executar(jogador, local, numeroDoTurno);
            }
        }
        return "Tudo parece calmo por enquanto.";
    }

    public Evento getEventoSorteadoAtual() { return eventoSorteadoAtual; }
    public void limparEventoSorteadoAtual() { this.eventoSorteadoAtual = null; }
}