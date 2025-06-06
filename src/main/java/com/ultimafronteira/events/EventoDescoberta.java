package com.ultimafronteira.events;

import com.ultimafronteira.model.Item;
import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.world.Ambiente;
import java.util.List;

public class EventoDescoberta extends Evento {

    private final List<Item> recursosEncontrados;
    private final String condicaoEspecial;

    /**
     * Construtor para um evento de descoberta específico.
     * @param nome O nome do evento, ex: "Abrigo Abandonado".
     * @param descricao A descrição para o jogador.
     * @param probabilidade A chance base de ocorrência.
     * @param recursos A lista de itens a serem encontrados neste evento.
     * @param condicaoEspecial Uma condição opcional, como "Requer Ferramenta".
     */
    public EventoDescoberta(String nome, String descricao, double probabilidade, List<Item> recursos, String condicaoEspecial) {
        super(nome, descricao, probabilidade, "Descoberta");
        this.recursosEncontrados = recursos;
        this.condicaoEspecial = condicaoEspecial;
    }

    @Override
    public String executar(Personagem jogador, Ambiente local, int numeroDoTurno) {
        StringBuilder sb = new StringBuilder(getDescricao() + "\n");

        // Verifica se há uma condição especial para este evento
        if (condicaoEspecial != null && !condicaoEspecial.isEmpty()) {
            if ("Requer Ferramenta".equals(condicaoEspecial)) {
                // Lógica para verificar se o jogador tem a ferramenta necessária
                boolean possuiFerramenta = jogador.getInventario().getItens().stream()
                        .anyMatch(item -> item.getNome().toLowerCase().contains("pá") || item.getNome().toLowerCase().contains("picareta"));

                if (!possuiFerramenta) {
                    sb.append("Você encontrou algo, mas não tem a ferramenta certa para acessá-lo.");
                    return sb.toString();
                }
                sb.append("Usando suas ferramentas, você revela o que estava escondido!\n");
            }
        }

        if (recursosEncontrados == null || recursosEncontrados.isEmpty()) {
            sb.append("...mas não havia nada de útil.");
            return sb.toString();
        }

        sb.append("Você encontrou:\n");
        for (Item item : recursosEncontrados) {
            sb.append("- ").append(item.getNome()).append("\n");
            if (jogador.getInventario().adicionarItem(item)) {
                sb.append("Item adicionado ao inventário.\n");
            } else {
                sb.append("Seu inventário está cheio e o item foi deixado para trás.\n");
            }
        }

        return sb.toString();
    }
}