package com.ultimafronteira.world;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.model.Alimento;
import com.ultimafronteira.model.Agua;
import com.ultimafronteira.model.Material;
import com.ultimafronteira.model.Item;
import com.ultimafronteira.events.GerenciadorDeEventos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AmbienteLagoRio extends Ambiente {
    private boolean aguaAbundante;
    private boolean possibilidadeDePesca;
    private boolean terrenoLamacento;
    private Random random = new Random();

    /**
     * Construtor para o ambiente Lago/Rio.
     * @param nome Nome do lago ou rio.
     * @param descricao Descrição do ambiente aquático.
     */
    public AmbienteLagoRio(String nome, String descricao) {
        // Chama o construtor da classe base (Ambiente)
        // Resolvido para incluir a chave da imagem de fundo específica.
        super(nome, descricao, "Moderada (risco de afogamento)", "Variável, geralmente úmido", "fundo_lago_rio");
        this.aguaAbundante = true;
        this.possibilidadeDePesca = true;
        this.terrenoLamacento = (random.nextDouble() < 0.4); // 40% de chance de terreno lamacento
        inicializarRecursos();
    }

    /**
     * Inicializa os recursos específicos que podem ser encontrados neste ambiente.
     */
    private void inicializarRecursos() {
        // Agua(String nome, double pesoUnitario, int durabilidade, int valorHidratacao, String pureza, double volume, String chaveImagem)
        // Água do rio é abundante, então durabilidade -1 (infinita no local, mas ao coletar vira um item com usos)
        // O item "Água do Rio" aqui representa a possibilidade de coletar.
        adicionarRecurso(new Agua("Água Fresca do Rio", 0.0, -1, 15, "Potável", 0.0, "item_agua_rio")); // Representa a fonte

        if (this.possibilidadeDePesca) {
            // Alimento(String nome, double pesoUnitario, int durabilidade, int valorNutricional, int valorCura, String tipo, String chaveImagem)
            adicionarRecurso(new Alimento("Peixe Pequeno", 0.4, 1, 12, 5, "Carne de Peixe", "item_peixe_pequeno"));
        }
        // Material(String nome, double pesoUnitario, String tipo, int resistencia, String chaveImagem)
        adicionarRecurso(new Material("Juncos Flexíveis", 0.1, "Fibra Vegetal", 2, "item_juncos"));
        if (random.nextDouble() < 0.2) {
            adicionarRecurso(new Material("Argila Úmida", 1.5, "Mineral", 3, "item_argila"));
        }
    }

    // Getters para propriedades específicas
    public boolean isAguaAbundante() { return aguaAbundante; }
    public boolean isPossibilidadeDePesca() { return this.possibilidadeDePesca; }
    public boolean isTerrenoLamacento() { return terrenoLamacento; }

    /**
     * Lógica de exploração específica para o ambiente de lago/rio.
     */
    @Override
    public String explorar(Personagem jogador, GerenciadorDeEventos ge, int numeroDoTurno) {
        StringBuilder sb = new StringBuilder();
        sb.append(jogador.getNome()).append(" explora as margens de ").append(getNome()).append(".\n");

        if (terrenoLamacento && random.nextDouble() < 0.3) {
            sb.append("O terreno está lamacento e escorregadio, dificultando a movimentação.\n");
            jogador.setEnergia(Math.max(0, jogador.getEnergia() - (random.nextInt(3) + 2) )); // Perde 2-4 de energia
        }

        double chanceBaseRecurso = 0.7; // Boa chance de encontrar algo perto da água
        if (jogador.temHabilidade("Rastreamento Aguçado")) {
            chanceBaseRecurso += 0.15;
            sb.append(jogador.getNome()).append(" observa atentamente as margens e a superfície da água.\n");
        }
        chanceBaseRecurso = Math.min(chanceBaseRecurso, 0.95);

        if (random.nextDouble() < chanceBaseRecurso) {
            List<Item> recursosAtuais = getRecursosDisponiveis();
            if (this.possibilidadeDePesca && random.nextDouble() < (0.3 + (jogador.temHabilidade("Rastreamento Aguçado") ? 0.15 : 0.0))) {
                sb.append("Você avista peixes na água! Parece um bom lugar para tentar pescar (se tiver o equipamento).\n");
                // A pesca em si seria uma ação separada, aqui apenas notamos a possibilidade.
            } else if (!recursosAtuais.isEmpty()) {
                // Tenta encontrar um recurso que não seja a "fonte" de água em si para coleta aleatória
                List<Item> itensColetaveis = new ArrayList<>();
                for (Item i : recursosAtuais) {
                    // Evita "coletar" a própria fonte de água desta forma, mas permite coletar outros tipos de água
                    if (!(i instanceof Agua && i.getDurabilidade() == -1 && i.getNome().equals("Água Fresca do Rio"))) {
                        itensColetaveis.add(i);
                    }
                }

                if (!itensColetaveis.isEmpty()) {
                    Item recursoEncontradoOriginal = itensColetaveis.get(random.nextInt(itensColetaveis.size()));
                    Item itemParaColetar = null;

                    // Cria nova instância do item
                    if (recursoEncontradoOriginal instanceof Alimento) {
                        Alimento alimOriginal = (Alimento) recursoEncontradoOriginal;
                        itemParaColetar = new Alimento(alimOriginal.getNome(), alimOriginal.getPesoUnitario(), alimOriginal.getDurabilidadeOriginal(), alimOriginal.getValorNutricional(), alimOriginal.getValorCura(), alimOriginal.getTipo(), alimOriginal.getChaveImagem());
                    } else if (recursoEncontradoOriginal instanceof Material) {
                        Material matOriginal = (Material) recursoEncontradoOriginal;
                        itemParaColetar = new Material(matOriginal.getNome(), matOriginal.getPesoUnitario(), matOriginal.getTipo(), matOriginal.getResistencia(), matOriginal.getChaveImagem());
                    } else if (recursoEncontradoOriginal instanceof Agua) { // Para outros tipos de água coletável
                        Agua aguaOriginal = (Agua) recursoEncontradoOriginal;
                        itemParaColetar = new Agua(aguaOriginal.getNome(), aguaOriginal.getPesoUnitario(), aguaOriginal.getDurabilidadeOriginal(), aguaOriginal.getValorHidratacao(), aguaOriginal.getPureza(), aguaOriginal.getVolume(), aguaOriginal.getChaveImagem());
                    }


                    if (itemParaColetar != null) {
                        itemParaColetar.setQuantidade(1); // Encontra uma unidade
                        sb.append("Recurso encontrado próximo a ").append(getNome()).append(": ").append(itemParaColetar.getNome()).append(".\n");
                        if (jogador.getInventario().adicionarItem(itemParaColetar)) {
                            sb.append(itemParaColetar.getNome()).append(" (x1) foi coletado(a).\n");
                        } else {
                            sb.append("O seu inventário está cheio. Não foi possível coletar ").append(itemParaColetar.getNome()).append(".\n");
                        }
                    } else {
                        sb.append("Apenas a água corrente chama a atenção por perto.\n");
                    }
                } else {
                    sb.append("Apenas a água corrente chama a atenção por perto.\n");
                }
            } else {
                sb.append("Apesar da água abundante, nada mais chamou a atenção desta vez.\n");
            }
        } else {
            sb.append("A área parece tranquila, sem nada de novo para encontrar agora.\n");
        }

        // Lógica para coletar água diretamente
        if (aguaAbundante) {
            sb.append("Você pode tentar coletar água de ").append(getNome()).append(".\n");
            // A ação de coletar água seria separada, talvez um botão "Coletar Água" se estiver perto de um rio/lago.
            // Ou, automaticamente adiciona se tiver um recipiente:
            // Optional<Item> recipiente = jogador.getInventario().getItens().stream().filter(i -> i.getNome().toLowerCase().contains("cantil") && i.getQuantidade() > 0).findFirst();
            // if(recipiente.isPresent()){ /* encher o cantil */ }
        }


        int energiaGasta = 7;
        if (jogador.temHabilidade("Metabolismo Eficiente")) energiaGasta = (int)(energiaGasta * 0.9);
        jogador.setEnergia(Math.max(0, jogador.getEnergia() - energiaGasta));
        sb.append(jogador.getNome()).append(" gastou ").append(energiaGasta).append(" de energia a explorar.\n");

        sb.append("--- A verificar eventos próximos à água ---\n");
        String resultadoEventoExploracao = ge.sortearEExecutarEvento(jogador, this, numeroDoTurno);
        sb.append(resultadoEventoExploracao);

        return sb.toString();
    }

    /**
     * Modifica as condições climáticas do ambiente de lago/rio.
     */
    @Override
    public String modificarClima() {
        StringBuilder sb = new StringBuilder();
        String climaAnterior = this.condicoesClimaticasPredominantes;

        if (random.nextDouble() < 0.20) { // 20% de chance de mudança
            int tipoMudanca = random.nextInt(3);
            switch (tipoMudanca) {
                case 0:
                    this.condicoesClimaticasPredominantes = "Névoa Densa sobre a Água";
                    sb.append("Uma névoa fria e densa paira sobre ").append(getNome()).append(", reduzindo a visibilidade.\n");
                    break;
                case 1:
                    this.condicoesClimaticasPredominantes = "Chuvisco Leve e Constante";
                    sb.append("Um chuvisco leve e constante começou a cair sobre ").append(getNome()).append(", molhando tudo lentamente.\n");
                    break;
                case 2:
                    this.condicoesClimaticasPredominantes = "Brisa Forte e Água Agitada";
                    sb.append("Uma brisa mais forte agita a superfície de ").append(getNome()).append(".\n");
                    break;
            }
        } else { // Se não houver mudança drástica, volta ao padrão ou permanece.
            if (!this.condicoesClimaticasPredominantes.startsWith("Variável, geralmente úmido")) { // Evita redundância
                this.condicoesClimaticasPredominantes = "Variável, geralmente úmido";
            }
            sb.append("O clima em ").append(getNome()).append(" permanece: ").append(this.condicoesClimaticasPredominantes).append(".\n");
        }
        return sb.toString();
    }
}