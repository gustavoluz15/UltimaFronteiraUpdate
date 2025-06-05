package com.ultimafronteira.world;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.model.Material;
import com.ultimafronteira.model.Agua;
import com.ultimafronteira.model.Item;
import com.ultimafronteira.events.GerenciadorDeEventos;

import java.util.ArrayList; // Certifique-se de que este import está presente se não estiver em Ambiente.java
import java.util.List;    // Certifique-se de que este import está presente
import java.util.Random;  // Para lógica aleatória na exploração ou clima

public class AmbienteCaverna extends Ambiente {
    private boolean poucaLuz;
    private boolean presencaCriaturasDesconhecidas;
    private boolean aguaDeGotejamento;
    private Random random = new Random(); // Para probabilidades

    /**
     * Construtor para o ambiente Caverna.
     *
     * @param nome Nome da caverna (ex: "Gruta Sombria").
     * @param descricao Descrição da caverna.
     */
    public AmbienteCaverna(String nome, String descricao) {
        // Chama o construtor da classe base (Ambiente)
        // Inclui a chave da imagem de fundo específica para cavernas
        super(nome, descricao, "Variável (requer iluminação)", "Escuro e Úmido", "fundo_caverna");
        this.poucaLuz = true;
        this.presencaCriaturasDesconhecidas = true; // Pode influenciar tipos de eventos de criatura
        this.aguaDeGotejamento = true; // Pode ser uma fonte de água
        inicializarRecursos();
    }

    /**
     * Inicializa os recursos específicos que podem ser encontrados nesta caverna.
     */
    private void inicializarRecursos() {
        // Adiciona recursos usando o construtor de Material que inclui a chaveImagem
        // Material(String nome, double pesoUnitario, String tipo, int resistencia, String chaveImagem)
        adicionarRecurso(new Material("Pedra Calcária", 1.2, "Mineral", 5, "item_pedra"));
        adicionarRecurso(new Material("Cristais Opacos", 0.5, "Mineral Raro", 8, "item_cristal"));

        if (aguaDeGotejamento) {
            // Adiciona água usando o construtor de Agua que inclui a chaveImagem
            // Agua(String nome, double peso, int durabilidade, int valorHidratacao, String pureza, double volume, String chaveImagem)
            // A durabilidade de "Água de Gotejamento" como item pode ser 1 (uso único ao coletar)
            // ou pode ser um item que representa a capacidade de coletar continuamente.
            // Aqui, vamos assumir que é um pequeno item coletável de uso único.
            adicionarRecurso(new Agua("Água de Gotejamento", 0.1, 1, 3, "Potável", 0.1, "item_agua_gotejamento"));
        }
        // Outros recursos potenciais: Fungos Luminosos (Alimento ou Material), Minério de Ferro, etc.
    }

    // Getters para as propriedades específicas da caverna
    public boolean isPoucaLuz() {
        return poucaLuz;
    }

    public boolean isPresencaCriaturasDesconhecidas() {
        return presencaCriaturasDesconhecidas;
    }

    public boolean isAguaDeGotejamento() {
        return aguaDeGotejamento;
    }

    /**
     * Lógica de exploração específica para a caverna.
     * Considera a necessidade de luz e a chance de encontrar recursos ou eventos.
     */
    @Override
    public String explorar(Personagem jogador, GerenciadorDeEventos ge, int numeroDoTurno) {
        StringBuilder sb = new StringBuilder();
        sb.append(jogador.getNome()).append(" adentra cuidadosamente ").append(getNome()).append(".\n");

        // Verifica se o jogador tem uma fonte de luz
        boolean temLuzSuficiente = jogador.getInventario().getItens().stream()
                .anyMatch(item -> {
                    String nomeItem = item.getNome().toLowerCase();
                    // Considera itens que explicitamente fornecem luz
                    return nomeItem.contains("lanterna") || nomeItem.contains("tocha") || nomeItem.contains("archote");
                    // Poderia também verificar se o item é de um tipo "FonteDeLuz" se essa categoria existir
                });

        if (poucaLuz && !temLuzSuficiente) {
            sb.append("Está muito escuro para explorar efetivamente. Você precisa de uma fonte de luz.\n");
            jogador.setEnergia(Math.max(0, jogador.getEnergia() - 2)); // Custo mínimo de energia
            sb.append(jogador.getNome()).append(" gastou 2 de energia tateando na escuridão.\n");
            // Possível evento de tropeçar ou se machucar levemente devido à escuridão.
            if (random.nextDouble() < 0.15) { // 15% de chance de um pequeno acidente
                int danoLeve = random.nextInt(3) + 1; // 1-3 de dano
                jogador.setVida(jogador.getVida() - danoLeve);
                sb.append("Na escuridão, você tropeça e se arranha, perdendo ").append(danoLeve).append(" de vida!\n");
            }
            return sb.toString();
        }

        sb.append(temLuzSuficiente ? "Com sua fonte de luz, você avança.\n" : "Mesmo com pouca luz, você tenta a sorte.\n");

        double chanceBaseRecurso = 0.45; // Chance base de encontrar recurso
        if (jogador.temHabilidade("Rastreamento Aguçado") && temLuzSuficiente) {
            chanceBaseRecurso += 0.20; // Bônus se tiver luz e habilidade
            sb.append(jogador.getNome()).append(" usa seus sentidos aguçados, perscrutando as sombras iluminadas!\n");
        }
        chanceBaseRecurso = Math.min(chanceBaseRecurso, 0.85); // Limita a chance máxima

        if (random.nextDouble() < chanceBaseRecurso) {
            List<Item> recursosAtuais = getRecursosDisponiveis(); // Obtém cópia
            if (!recursosAtuais.isEmpty()) {
                int indiceRecurso = random.nextInt(recursosAtuais.size());
                Item recursoEncontradoOriginal = recursosAtuais.get(indiceRecurso);
                // Cria uma nova instância do item para adicionar ao inventário,
                // especialmente se os recursos forem "infinitos" no ambiente ou se a quantidade for importante.
                // Para simplificar, vamos assumir que o método adicionarRecurso já lida com cópias se necessário,
                // ou que os itens em recursosDisponiveis são protótipos.
                // A lógica de Item e Inventario agora suporta empilhamento.
                Item itemParaColetar;
                if(recursoEncontradoOriginal instanceof Agua){
                    Agua aguaOriginal = (Agua) recursoEncontradoOriginal;
                    itemParaColetar = new Agua(aguaOriginal.getNome(), aguaOriginal.getPesoUnitario(), aguaOriginal.getDurabilidadeOriginal(), aguaOriginal.getValorHidratacao(), aguaOriginal.getPureza(), aguaOriginal.getVolume(), aguaOriginal.getChaveImagem());
                } else if (recursoEncontradoOriginal instanceof Material){
                    Material matOriginal = (Material) recursoEncontradoOriginal;
                    itemParaColetar = new Material(matOriginal.getNome(), matOriginal.getPesoUnitario(), matOriginal.getTipo(), matOriginal.getResistencia(), matOriginal.getChaveImagem());
                } else {
                    // Fallback para outros tipos de item, pode precisar de construtores específicos
                    itemParaColetar = new Item(recursoEncontradoOriginal.getNome(), recursoEncontradoOriginal.getPesoUnitario(), recursoEncontradoOriginal.getDurabilidadeOriginal(), recursoEncontradoOriginal.isEmpilhavel(), recursoEncontradoOriginal.getChaveImagem()) {
                        @Override public void usar(Personagem p) { System.out.println(this.getNome() + " não tem uso específico aqui."); }
                    };
                }
                itemParaColetar.setQuantidade(1); // Encontra uma unidade

                sb.append("Algo brilha na escuridão! Recurso encontrado em ").append(getNome()).append(": ").append(itemParaColetar.getNome()).append(".\n");
                if (jogador.getInventario().adicionarItem(itemParaColetar)) {
                    sb.append(itemParaColetar.getNome()).append(" (x1) coletado(a).\n");
                } else {
                    sb.append("Inventário cheio. Não foi possível coletar ").append(itemParaColetar.getNome()).append(".\n");
                }
            } else {
                sb.append("A caverna parece despojada de recursos úteis neste local.\n");
            }
        } else {
            sb.append("Os ecos da caverna são a única resposta à sua exploração cuidadosa.\n");
        }

        int energiaGasta = temLuzSuficiente ? 8 : 12; // Gasta mais energia se estiver no escuro
        if (jogador.temHabilidade("Metabolismo Eficiente")) energiaGasta = (int) (energiaGasta * 0.8);
        jogador.setEnergia(Math.max(0, jogador.getEnergia() - energiaGasta));
        sb.append(jogador.getNome()).append(" gastou ").append(energiaGasta).append(" de energia explorando a caverna.\n");

        // Chance de evento aleatório na caverna
        sb.append("--- Verificando eventos na escuridão da caverna ---\n");
        String resultadoEventoExploracao = ge.sortearEExecutarEvento(jogador, this, numeroDoTurno);
        sb.append(resultadoEventoExploracao);

        return sb.toString();
    }

    /**
     * Modifica as condições climáticas da caverna (geralmente estáveis, mas pode haver pequenas variações).
     */
    @Override
    public String modificarClima() {
        StringBuilder sb = new StringBuilder();
        String climaAnterior = this.condicoesClimaticasPredominantes;

        // Cavernas são menos suscetíveis a mudanças climáticas externas, mas podem ter microclimas.
        double chanceMudanca = 0.05; // Pequena chance de alguma mudança sutil
        if (random.nextDouble() < chanceMudanca) {
            int tipoMudanca = random.nextInt(3);
            switch (tipoMudanca) {
                case 0:
                    this.condicoesClimaticasPredominantes = "Úmido e Abafado";
                    sb.append("A umidade na caverna parece ter aumentado, tornando o ar pesado.\n");
                    break;
                case 1:
                    this.condicoesClimaticasPredominantes = "Correntes de Ar Gelado";
                    sb.append("Uma corrente de ar frio e súbita percorre os túneis da caverna.\n");
                    break;
                case 2:
                    this.condicoesClimaticasPredominantes = "Silêncio Inquietante"; // Mais um estado atmosférico
                    sb.append("Um silêncio profundo e um tanto inquietante toma conta da caverna.\n");
                    break;
                default:
                    this.condicoesClimaticasPredominantes = "Escuro e Úmido"; // Volta ao padrão
                    sb.append("O ar da caverna permanece escuro e úmido.\n");
                    break;
            }
        } else {
            sb.append("O ambiente interno de ").append(getNome()).append(" permanece ").append(this.condicoesClimaticasPredominantes).append(". ");
            if (aguaDeGotejamento && random.nextDouble() < 0.1) {
                sb.append("O gotejamento constante ecoa pelas paredes.\n");
            } else {
                sb.append("Nenhuma mudança atmosférica perceptível.\n");
            }
        }
        return sb.toString();
    }
}
