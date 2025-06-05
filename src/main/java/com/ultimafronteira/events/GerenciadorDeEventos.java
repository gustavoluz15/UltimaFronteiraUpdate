package com.ultimafronteira.events;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.world.Ambiente;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class GerenciadorDeEventos {
    private List<Evento> todosOsEventosPossiveis;
    private Random randomizador;
<<<<<<< HEAD
    private Evento eventoSorteadoAtual;
=======
    private Evento eventoSorteadoAtual; // Para guardar o último evento sorteado
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd

    public GerenciadorDeEventos() {
        this.todosOsEventosPossiveis = new ArrayList<>();
        this.randomizador = new Random();
        this.eventoSorteadoAtual = null;
        inicializarEventosPadrao();
    }

    private void inicializarEventosPadrao() {
        this.todosOsEventosPossiveis.clear();
<<<<<<< HEAD

        adicionarEventoPossivel(new EventoClimatico());
        adicionarEventoPossivel(new EventoCriatura()); // O construtor de EventoCriatura definirá o tipo de criatura internamente.
        adicionarEventoPossivel(new EventoDescoberta());
        adicionarEventoPossivel(new EventoDoencaFerimento());

=======
        adicionarEventoPossivel(new EventoClimatico("Nevasca Repentina", "Uma nevasca forte e inesperada cobre a área.", 0.15, "Nevasca", 3, "Reduz visibilidade e aumenta o frio."));
        adicionarEventoPossivel(new EventoClimatico("Onda de Calor Intenso", "O sol castiga impiedosamente, elevando a temperatura.", 0.12, "Calor Extremo", 2, "Aumenta a sede rapidamente."));
        adicionarEventoPossivel(new EventoClimatico("Tempestade Violenta", "Nuvens escuras se formam e uma tempestade desaba.", 0.18, "Tempestade", 2, "Dificulta movimentação e pode haver raios."));
        adicionarEventoPossivel(new EventoCriatura("Ataque de Lobo Faminto", "Um lobo surge das sombras, claramente faminto.", 0.20, "Lobo Faminto", "Médio", 10, 30, new String[]{"Lutar", "Fugir"}));
        adicionarEventoPossivel(new EventoCriatura("Encontro com Cobra Venenosa", "Uma cobra venenosa e ágil cruza seu caminho.", 0.15, "Cobra Venenosa", "Médio", 8, 15, new String[]{"Lutar", "Fugir", "Desviar Cautelosamente"}));
        adicionarEventoPossivel(new EventoCriatura("Bando de Corvos Furtivos", "Corvos observam dos galhos, parecendo inteligentes demais.", 0.08, "Corvos Furtivos", "Baixo (Psicológico)", 0, 5, new String[]{"Ignorar", "Espantar"}));
        adicionarEventoPossivel(new EventoDescoberta("Abrigo Abandonado Seguro", "Você encontra um pequeno abrigo que parece seguro e contém alguns suprimentos.", 0.04, "Abrigo Abandonado", "Nenhuma"));
        adicionarEventoPossivel(new EventoDescoberta("Fonte de Água Potável Escondida", "Você descobre uma nascente de água cristalina.", 0.05, "Fonte de Água Limpa", "Nenhuma"));
        adicionarEventoPossivel(new EventoDescoberta("Pista em Ruínas Antigas", "Entre escombros, você encontra um artefato ou inscrição que parece importante.", 0.03, "Ruínas Misteriosas", "Requer Ferramenta"));
        adicionarEventoPossivel(new EventoDoencaFerimento("Corte Profundo Acidental", "Ao manusear ferramentas ou explorar, você se corta feio.", 0.10, "Corte Profundo", "Perda de vida, risco de infecção.", "Bandagens, Antisséptico"));
        adicionarEventoPossivel(new EventoDoencaFerimento("Mal-Estar Súbito", "Você começa a se sentir febril e fraco.", 0.08, "Febre Desconhecida", "Perda de energia e fome/sede aumentadas.", "Descanso, Remédio para febre"));
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
    }

    public void adicionarEventoPossivel(Evento evento) {
        if (evento != null) {
            this.todosOsEventosPossiveis.add(evento);
        }
    }

    public String sortearEExecutarEvento(Personagem jogador, Ambiente local, int numeroDoTurno) {
        this.eventoSorteadoAtual = null;
        if (todosOsEventosPossiveis.isEmpty()) {
<<<<<<< HEAD
            return "O ar está estranhamente calmo.";
=======
            return "Nenhum evento possível cadastrado no gerenciador.";
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
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

<<<<<<< HEAD
    public EventoCriatura getEventoSorteadoAtualComoCriatura() {
        if (eventoSorteadoAtual instanceof EventoCriatura) {
            return (EventoCriatura) eventoSorteadoAtual;
        }
        return null;
    }

=======
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
    public void limparEventoSorteadoAtual() {
        this.eventoSorteadoAtual = null;
    }
}