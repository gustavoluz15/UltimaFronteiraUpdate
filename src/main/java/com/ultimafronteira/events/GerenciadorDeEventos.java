package com.ultimafronteira.events;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.world.Ambiente;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections; // Import para java.util.Collections

public class GerenciadorDeEventos {
    private List<Evento> todosOsEventosPossiveis;
    private Random randomizador;
    private Evento eventoSorteadoAtual; // Para guardar o último evento sorteado

    /**
     * Construtor do GerenciadorDeEventos.
     * Inicializa a lista de eventos possíveis e o gerador de números aleatórios.
     * Carrega os eventos padrão.
     */
    public GerenciadorDeEventos() {
        this.todosOsEventosPossiveis = new ArrayList<>();
        this.randomizador = new Random();
        this.eventoSorteadoAtual = null;
        inicializarEventosPadrao();
    }

    /**
     * Inicializa a lista com instâncias dos tipos de eventos padrão.
     * Cada evento, ao ser instanciado com o construtor padrão,
     * deverá ser capaz de sortear seus detalhes específicos ao ser executado.
     */
    private void inicializarEventosPadrao() {
        this.todosOsEventosPossiveis.clear();

        // Adiciona instâncias dos eventos usando seus construtores padrão.
        // A lógica de variação específica (ex: tipo de clima, tipo de criatura)
        // deve estar dentro do método executar() de cada classe de evento
        // quando são instanciadas desta forma.
        adicionarEventoPossivel(new EventoClimatico());
        adicionarEventoPossivel(new EventoCriatura());
        adicionarEventoPossivel(new EventoDescoberta());
        adicionarEventoPossivel(new EventoDoencaFerimento());

        // Para adicionar eventos pré-configurados específicos, você usaria os construtores parametrizados:
        // Exemplo (assumindo que os construtores parametrizados existem e estão corretos):
        // adicionarEventoPossivel(new EventoClimatico("Nevasca Suave", "Flocos de neve começam a cair.", 0.10, "Nevasca Leve", 2, "Visibilidade ligeiramente reduzida."));
        // adicionarEventoPossivel(new EventoCriatura("Lobo Solitário", "Um lobo solitário observa de longe.", 0.05, "Lobo", "Baixo", 8, 20, new String[]{"Observar", "Ignorar"}, "criatura_lobo_solitario"));
    }

    /**
     * Adiciona um evento à lista de todos os eventos possíveis.
     * @param evento O evento a ser adicionado.
     */
    public void adicionarEventoPossivel(Evento evento) {
        if (evento != null) {
            this.todosOsEventosPossiveis.add(evento);
        }
    }

    /**
     * Tenta sortear e executar um evento da lista de eventos possíveis.
     * A probabilidade de cada evento ocorrer é verificada.
     *
     * @param jogador O personagem do jogador.
     * @param local O ambiente atual.
     * @param numeroDoTurno O número do turno atual.
     * @return Uma string descrevendo o evento ocorrido ou uma mensagem padrão se nenhum evento ocorrer.
     */
    public String sortearEExecutarEvento(Personagem jogador, Ambiente local, int numeroDoTurno) {
        this.eventoSorteadoAtual = null;
        if (todosOsEventosPossiveis.isEmpty()) {
            return "O ar está estranhamente calmo."; // Mensagem mais imersiva
        }

        // Cria uma cópia da lista para embaralhar e tentar os eventos
        List<Evento> eventosCandidatos = new ArrayList<>(todosOsEventosPossiveis);
        Collections.shuffle(eventosCandidatos, randomizador); // Embaralha para não testar sempre na mesma ordem

        for (Evento evento : eventosCandidatos) {
            if (evento.tentarOcorrer(jogador, local, numeroDoTurno)) {
                this.eventoSorteadoAtual = evento; // Guarda o evento que ocorreu
                StringBuilder sb = new StringBuilder();
                if (evento instanceof EventoCriatura) {
                    sb.append("--- CONFRONTO IMINENTE! ---\n");
                } else {
                    sb.append("--- OCORREU UM EVENTO! ---\n");
                }
                // Chama o executar do evento específico, que deve ter a lógica de variação interna
                sb.append(evento.executar(jogador, local, numeroDoTurno));
                return sb.toString();
            }
        }
        return "Nenhum evento especial ocorreu desta vez."; // Mensagem padrão se nenhum evento passar na sua probabilidade
    }

    /**
     * Retorna o último evento que foi sorteado e executado.
     * @return O evento atual, ou null se nenhum evento ocorreu recentemente ou foi limpo.
     */
    public Evento getEventoSorteadoAtual() {
        return eventoSorteadoAtual;
    }

    /**
     * Retorna o evento atual especificamente como um EventoCriatura, se aplicável.
     * Útil para aceder a métodos específicos de EventoCriatura após um confronto.
     * @return O EventoCriatura atual, ou null se o evento atual não for uma criatura.
     */
    public EventoCriatura getEventoSorteadoAtualComoCriatura() {
        if (eventoSorteadoAtual instanceof EventoCriatura) {
            return (EventoCriatura) eventoSorteadoAtual;
        }
        return null;
    }

    /**
     * Limpa a referência ao evento sorteado atual.
     * Pode ser útil após o evento ter sido completamente processado pelo jogo.
     */
    public void limparEventoSorteadoAtual() {
        this.eventoSorteadoAtual = null;
    }
}