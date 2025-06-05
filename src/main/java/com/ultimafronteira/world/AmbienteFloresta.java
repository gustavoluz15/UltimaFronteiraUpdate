package com.ultimafronteira.world;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.model.Alimento;
import com.ultimafronteira.model.Material;
import com.ultimafronteira.model.Item;
import com.ultimafronteira.events.GerenciadorDeEventos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AmbienteFloresta extends Ambiente {
    private boolean vegetacaoDensa;
    private boolean faunaAbundante; // Pode influenciar eventos de caça ou encontro com animais
    private boolean climaUmido;
    private Random random = new Random(); // Para probabilidades

    /**
     * Construtor para o ambiente Floresta.
     * @param nome Nome da floresta.
     * @param descricao Descrição da floresta.
     */
    public AmbienteFloresta(String nome, String descricao) {
        // Chama o construtor da classe base (Ambiente)
        super(nome, descricao, "Moderada", "Úmido e Temperado", "fundo_floresta");
        this.vegetacaoDensa = true;
        this.faunaAbundante = true;
        this.climaUmido = true;
        inicializarRecursos();
    }

    /**
     * Inicializa os recursos específicos que podem ser encontrados nesta floresta.
     */
    private void inicializarRecursos() {
        // Alimento(String nome, double pesoUnitario, int durabilidade, int valorNutricional, int valorCura, String tipo, String chaveImagem)
        adicionarRecurso(new Alimento("Frutas Silvestres", 0.3, 1, 5, 2, "Fruta", "item_comida_frutas"));
        // Material(String nome, double pesoUnitario, String tipo, int resistencia, String chaveImagem)
        adicionarRecurso(new Material("Madeira de Pinheiro", 2.0, "Madeira", 4, "item_madeira"));
        adicionarRecurso(new Alimento("Cogumelos Comestiveis", 0.1, 1, 3, 1, "Cogumelo", "item_cogumelo"));
        // Outros recursos: Ervas Medicinais, Pequenos Animais (se houver sistema de caça)
    }

    // Getters para propriedades específicas da floresta
    public boolean isVegetacaoDensa() { return vegetacaoDensa; }
    public boolean isFaunaAbundante() { return faunaAbundante; }
    public boolean isClimaUmido() { return climaUmido; }

    /**
     * Lógica de exploração específica para a floresta.
     */
    @Override
    public String explorar(Personagem jogador, GerenciadorDeEventos ge, int numeroDoTurno) {
        StringBuilder sb = new StringBuilder();
        sb.append(jogador.getNome()).append(" está a explorar ").append(getNome()).append(".\n");

        double chanceBaseRecurso = 0.6; // Chance base de encontrar recurso
        if (jogador.temHabilidade("Rastreamento Aguçado")) {
            chanceBaseRecurso += 0.20; // Bônus de 20% para o Rastreador
            sb.append(jogador.getNome()).append(" usa a sua habilidade de rastreamento, procurando por sinais na vegetação!\n");
        }
        chanceBaseRecurso = Math.min(chanceBaseRecurso, 0.95); // Limita a chance máxima

        if (random.nextDouble() < chanceBaseRecurso) {
            List<Item> recursosAtuais = getRecursosDisponiveis(); // Obtém cópia dos protótipos de recursos
            if (!recursosAtuais.isEmpty()) {
                int indiceRecurso = random.nextInt(recursosAtuais.size());
                Item recursoEncontradoOriginal = recursosAtuais.get(indiceRecurso);

                // Cria uma nova instância do item para adicionar ao inventário
                Item itemParaColetar = null;
                if (recursoEncontradoOriginal instanceof Alimento) {
                    Alimento alimentoOriginal = (Alimento) recursoEncontradoOriginal;
                    itemParaColetar = new Alimento(alimentoOriginal.getNome(), alimentoOriginal.getPesoUnitario(),
                            alimentoOriginal.getDurabilidadeOriginal(), alimentoOriginal.getValorNutricional(),
                            alimentoOriginal.getValorCura(), alimentoOriginal.getTipo(),
                            alimentoOriginal.getChaveImagem());
                } else if (recursoEncontradoOriginal instanceof Material) {
                    Material materialOriginal = (Material) recursoEncontradoOriginal;
                    itemParaColetar = new Material(materialOriginal.getNome(), materialOriginal.getPesoUnitario(),
                            materialOriginal.getTipo(), materialOriginal.getResistencia(),
                            materialOriginal.getChaveImagem());
                }
                // Adicionar outros 'else if' para diferentes tipos de Item se necessário

                if (itemParaColetar != null) {
                    itemParaColetar.setQuantidade(1); // Encontra uma unidade do recurso
                    sb.append("Recurso encontrado entre as árvores: ").append(itemParaColetar.getNome()).append(".\n");
                    if (jogador.getInventario().adicionarItem(itemParaColetar)) {
                        sb.append(itemParaColetar.getNome()).append(" (x1) foi coletado(a).\n");
                    } else {
                        sb.append("O inventário está cheio. Não foi possível coletar ").append(itemParaColetar.getNome()).append(".\n");
                    }
                } else {
                    sb.append("Você encontrou vestígios de um recurso, mas não conseguiu identificar ou coletar nada útil.\n");
                }
            } else {
                sb.append("Apesar dos seus esforços, a floresta não revelou recursos específicos desta vez.\n");
            }
        } else {
            sb.append("A exploração não revelou nada de novo, apenas o farfalhar das folhas ao vento.\n");
        }

        int energiaGasta = 10;
        if (jogador.temHabilidade("Metabolismo Eficiente")) { // Adaptei para uma habilidade mais genérica que pode afetar exploração
            energiaGasta = (int) (energiaGasta * 0.8); // Reduz custo de energia
            sb.append(jogador.getNome()).append(" sente-se menos cansado devido ao seu metabolismo eficiente.\n");
        }
        jogador.setEnergia(Math.max(0, jogador.getEnergia() - energiaGasta));
        sb.append(jogador.getNome()).append(" gastou ").append(energiaGasta).append(" de energia a explorar a floresta.\n");

        // Chance de evento aleatório na floresta
        sb.append("--- A verificar eventos na profundidade da floresta ---\n");
        String resultadoEventoExploracao = ge.sortearEExecutarEvento(jogador, this, numeroDoTurno);
        sb.append(resultadoEventoExploracao);

        return sb.toString();
    }

    /**
     * Modifica as condições climáticas da floresta.
     */
    @Override
    public String modificarClima() {
        StringBuilder sb = new StringBuilder();
        String climaAnterior = this.condicoesClimaticasPredominantes;
        double chanceDeChuva = 0.25; // Chance de começar a chover ou parar
        double chanceDeSol = 0.15;   // Chance de ficar ensolarado se estiver chovendo

        if (this.condicoesClimaticasPredominantes.contains("Chuva")) {
            if (random.nextDouble() < chanceDeSol) { // Chance de parar de chover
                this.condicoesClimaticasPredominantes = "Úmido e Temperado (Pós-Chuva)";
                sb.append("A chuva parou em ").append(getNome()).append(". O ar está fresco e a terra, úmida.\n");
            } else {
                sb.append("A chuva continua a cair suavemente em ").append(getNome()).append(".\n");
            }
        } else { // Se não estava chovendo
            if (random.nextDouble() < chanceDeChuva) {
                this.condicoesClimaticasPredominantes = "Chuva Leve na Floresta";
                sb.append("Nuvens escuras acumulam-se e uma chuva leve começa a cair em ").append(getNome()).append(".\n");
            } else {
                sb.append("O tempo em ").append(getNome()).append(" permanece ").append(this.condicoesClimaticasPredominantes).append(".\n");
            }
        }
        return sb.toString();
    }
}