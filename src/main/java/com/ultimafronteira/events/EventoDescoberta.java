package com.ultimafronteira.events;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.model.Item;
import com.ultimafronteira.model.Alimento;
import com.ultimafronteira.model.Agua;
import com.ultimafronteira.model.Ferramenta;
import com.ultimafronteira.world.Ambiente;
import java.util.List;
import java.util.ArrayList;
import java.util.Random; // Adicionado para uso potencial, embora Math.random() seja usado atualmente.

public class EventoDescoberta extends Evento {

    private String tipoDeDescoberta; // Ex: "Abrigo Abandonado", "Fonte de Água Limpa"
    private List<Item> recursosEncontrados;
    private String condicaoEspecial; // Ex: "Requer Ferramenta"
    private Random random = new Random(); // Para lógicas aleatórias

    /**
     * Construtor para EventoDescoberta.
     *
     * @param nome Nome do evento (ex: "Achado em Ruínas").
     * @param descricao Descrição geral do que pode ser encontrado.
     * @param probabilidade Probabilidade base de ocorrência do evento.
     * @param tipoDeDescoberta Define o que especificamente pode ser encontrado.
     * @param condicaoEspecial Alguma condição para acessar/maximizar a descoberta.
     */
    public EventoDescoberta(String nome, String descricao, double probabilidade,
                            String tipoDeDescoberta, String condicaoEspecial) {
        super(nome, descricao, probabilidade, "Descoberta/Recurso");
        this.tipoDeDescoberta = tipoDeDescoberta;
        this.recursosEncontrados = new ArrayList<>();
        this.condicaoEspecial = condicaoEspecial;
        inicializarRecursosDescoberta();
    }

    /**
     * Construtor padrão simplificado.
     * Cria um evento de descoberta genérico. Os detalhes podem ser definidos
     * ou sorteados no método executar ou através de um tipo de descoberta padrão.
     */
    public EventoDescoberta() {
        super("Descoberta Inesperada",
                "Você encontra vestígios de outros sobreviventes ou recursos deixados para trás.",
                0.25, // Probabilidade padrão para descobertas genéricas
                "Positivo"); // Tipo de impacto genérico
        this.tipoDeDescoberta = "Genérica"; // Tipo padrão
        this.recursosEncontrados = new ArrayList<>();
        this.condicaoEspecial = null;
        inicializarRecursosDescoberta(); // Tenta inicializar com base no tipo "Genérica"
    }


    /**
     * Inicializa os recursos que podem ser encontrados com base no tipoDeDescoberta.
     * Esta lógica define os itens específicos para cada tipo de descoberta.
     */
    private void inicializarRecursosDescoberta() {
        this.recursosEncontrados.clear(); // Limpa recursos de uma possível execução anterior

        // Usar random.nextDouble() que é mais flexível que Math.random() se precisar de sementes
        if ("Abrigo Abandonado".equals(tipoDeDescoberta)) {
            if (random.nextDouble() < 0.6) { // Chance aumentada para comida
                // Assumindo construtor: Alimento(nome, peso, durabilidade, valorNutricional, tipoAlimento)
                this.recursosEncontrados.add(new Alimento("Comida Enlatada", 0.5, 10, 30, "Enlatado"));
            }
            if (random.nextDouble() < 0.4) { // Chance aumentada para água
                // Assumindo construtor: Agua(nome, peso, durabilidade, valorSede, tipoAgua, volume)
                this.recursosEncontrados.add(new Agua("Garrafa d'água (selada)", 0.6, 10, 25, "Potável", 0.6));
            }
            if (random.nextDouble() < 0.2) {
                // Assumindo construtor: Ferramenta(nome, peso, durabilidade, tipoFerramenta, eficacia)
                this.recursosEncontrados.add(new Ferramenta("Canivete Enferrujado", 0.2, 5, "Multiuso Leve", 1));
            }
        } else if ("Fonte de Água Limpa".equals(tipoDeDescoberta)) {
            // Água da fonte não tem peso/durabilidade no inventário, mas recupera sede
            // O item "Água Fresca da Fonte" poderia ser um item consumível que se coleta.
            // Para simplificar, poderia apenas aumentar a sede do jogador diretamente no 'executar'
            // Ou adicionar um item que representa a água coletada.
            this.recursosEncontrados.add(new Agua("Água Fresca Coletada", 0.5, 1, 50, "Potável", 0.5));
            this.recursosEncontrados.add(new Agua("Água Fresca Coletada", 0.5, 1, 50, "Potável", 0.5)); // Encontra mais de uma
        } else if ("Ruínas Misteriosas".equals(tipoDeDescoberta)) {
            // Assumindo construtor: Ferramenta(nome, peso, durabilidade, tipoFerramenta, eficacia)
            this.recursosEncontrados.add(new Ferramenta("Artefato Antigo", 1.0, 1, "Desconhecido", 0)); // Item chave ou de valor
            if (random.nextDouble() < 0.15) {
                this.recursosEncontrados.add(new Alimento("Ração de Emergência Antiga", 0.3, 20, 20, "Ração"));
            }
        } else if ("Genérica".equals(tipoDeDescoberta) || random.nextDouble() < 0.1) { // Descoberta genérica ou pequena chance de algo extra
            // Lógica para descoberta genérica (baseada na versão HEAD)
            // Assumindo construtor Alimento(nome, peso, durabilidade, valorNutricional, valorCura, tipo, chaveImagem)
            // Para compatibilizar, vamos usar o construtor inferido de 5 args e omitir valorCura e chaveImagem por agora
            // Ou ajustar a classe Alimento para ter o construtor de 7 args.
            // Por ora, usaremos o construtor de 5 args.
            this.recursosEncontrados.add(new Alimento(
                    "Frutas Silvestres",
                    0.3,
                    2,  // Durabilidade baixa
                    15, // Valor nutricional
                    "Fruta"
            ));
            if (random.nextDouble() < 0.3) {
                this.recursosEncontrados.add(new Agua("Cantil com Água Duvidosa", 0.7, 3, 10, "Filtrável", 0.7));
            }
        }
        // Adicionar mais tipos de descoberta e itens conforme necessário
    }

    // Getters
    public String getTipoDeDescoberta() {
        return tipoDeDescoberta;
    }

    public List<Item> getRecursosEncontrados() {
        // Retorna uma cópia para evitar modificação externa da lista interna
        return new ArrayList<>(recursosEncontrados);
    }

    public String getCondicaoEspecial() {
        return condicaoEspecial;
    }

    /**
     * Executa o evento de descoberta.
     * Verifica condições especiais e tenta adicionar os itens encontrados ao inventário do jogador.
     *
     * @param jogador O personagem do jogador.
     * @param local O ambiente atual.
     * @param numeroDoTurno O número do turno atual.
     * @return Uma string descrevendo o resultado da descoberta.
     */
    @Override
    public String executar(Personagem jogador, Ambiente local, int numeroDoTurno) {
        // Re-inicializa os recursos para garantir que cada execução seja fresca,
        // especialmente se o mesmo objeto EventoDescoberta for reutilizado.
        inicializarRecursosDescoberta();

        StringBuilder sb = new StringBuilder();
        sb.append("EVENTO DE DESCOBERTA: ").append(getNome().toUpperCase()).append("!\n");
        sb.append(getDescricao()).append("\n");
        sb.append("Tipo: ").append(tipoDeDescoberta).append("\n");

        if (jogador == null) {
            return sb.append("Erro: Personagem não definido para este evento.\n").toString();
        }
        if (jogador.getInventario() == null) {
            return sb.append("Erro: Inventário do personagem não definido.\n").toString();
        }


        if (condicaoEspecial != null && !condicaoEspecial.isEmpty()) {
            sb.append("Condição Especial: ").append(condicaoEspecial).append("\n");
            if ("Requer Ferramenta".equals(condicaoEspecial)) {
                // Verifica se o jogador possui uma ferramenta adequada (ex: pá, picareta)
                boolean possuiFerramentaAdequada = jogador.getInventario().getItens().stream()
                        .anyMatch(item -> item instanceof Ferramenta &&
                                (item.getNome().toLowerCase().contains("pá") ||
                                        item.getNome().toLowerCase().contains("picareta") ||
                                        item.getNome().toLowerCase().contains("canivete") // Adicionando canivete como ferramenta básica
                                ));
                if (!possuiFerramentaAdequada) {
                    sb.append(jogador.getNome()).append(" não possui a ferramenta adequada para aproveitar esta descoberta totalmente.\n");
                    // Mesmo sem ferramenta, pode haver alguns itens básicos. Não retorna aqui.
                } else {
                    sb.append(jogador.getNome()).append(" usa uma ferramenta e consegue acesso a mais recursos!\n");
                    // Aqui poderia haver uma lógica para adicionar itens EXTRAS se a ferramenta for usada.
                    // Por exemplo, a lista `recursosEncontrados` poderia ser modificada/expandida.
                }
            }
            // Outras condições especiais podem ser tratadas aqui
        }

        if (!recursosEncontrados.isEmpty()) {
            sb.append(jogador.getNome()).append(" encontrou os seguintes itens:\n");
            for (Item item : recursosEncontrados) {
                if (item == null) continue; // Segurança extra
                sb.append("- ").append(item.getNome());
                if (jogador.getInventario().adicionarItem(item)) {
                    sb.append(" (adicionado ao inventário).\n");
                } else {
                    sb.append(" (inventário cheio, não pôde ser coletado).\n");
                }
            }
        } else {
            sb.append("Apesar da exploração, nada de útil foi encontrado desta vez.\n");
        }
        return sb.toString();
    }
}