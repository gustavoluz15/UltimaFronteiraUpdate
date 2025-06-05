package com.ultimafronteira.world;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.model.Material;
import com.ultimafronteira.model.Agua;
import com.ultimafronteira.model.Item;
import com.ultimafronteira.events.GerenciadorDeEventos;

import java.util.ArrayList; // Certifique-se de que este import está presente
import java.util.List;    // Certifique-se de que este import está presente
import java.util.Random;  // Para lógica aleatória

public class AmbienteMontanha extends Ambiente {
    private boolean terrenoAcidentado;
    private boolean climaInstavel; // Maior chance de eventos climáticos adversos
    private boolean baixaVegetacao; // Menos recursos de alimento baseados em plantas
    private Random random = new Random(); // Para probabilidades

    /**
     * Construtor para o ambiente Montanha.
     * @param nome Nome da montanha (ex: "Picos Congelados").
     * @param descricao Descrição da montanha.
     */
    public AmbienteMontanha(String nome, String descricao) {
        // Chama o construtor da classe base (Ambiente)
        // Resolvido para incluir a chave da imagem de fundo específica para montanhas
        super(nome, descricao, "Difícil", "Frio e Ventoso", "fundo_pico_geada");
        this.terrenoAcidentado = true;
        this.climaInstavel = true;
        this.baixaVegetacao = true;
        inicializarRecursos();
    }

    /**
     * Inicializa os recursos específicos que podem ser encontrados nesta montanha.
     */
    private void inicializarRecursos() {
        // Material(String nome, double pesoUnitario, String tipo, int resistencia, String chaveImagem)
        adicionarRecurso(new Material("Pedra Afiada", 1.0, "Mineral", 6, "item_pedra_afiada"));
        adicionarRecurso(new Material("Minério de Ferro Bruto", 2.5, "Metal", 7, "item_minerio_ferro"));
        // Agua(String nome, double pesoUnitario, int durabilidade, int valorHidratacao, String pureza, double volume, String chaveImagem)
        adicionarRecurso(new Agua("Água de Degelo", 0.5, 1, 5, "Contaminada", 0.5, "item_agua_degelo")); // Nota: chaveImagem atualizada
        // Outros recursos possíveis: Ervas Raras, Cristais de Gelo (material)
    }

    // Getters para as propriedades específicas da montanha
    public boolean isTerrenoAcidentado() { return terrenoAcidentado; }
    public boolean isClimaInstavel() { return climaInstavel; }
    public boolean isBaixaVegetacao() { return baixaVegetacao; }

    /**
     * Lógica de exploração específica para a montanha.
     * A exploração em montanhas é árdua e pode ser perigosa.
     */
    @Override
    public String explorar(Personagem jogador, GerenciadorDeEventos ge, int numeroDoTurno) {
        StringBuilder sb = new StringBuilder();
        sb.append(jogador.getNome()).append(" escala arduamente por ").append(getNome()).append(".\n");

        if (terrenoAcidentado) {
            sb.append("O terreno acidentado torna cada passo um desafio.\n");
            // Poderia haver uma chance de perder energia extra ou um pequeno dano por queda
            if (random.nextDouble() < 0.1) { // 10% de chance de um pequeno percalço
                int energiaPerdidaExtra = random.nextInt(3) + 2; // 2-4 de energia extra
                jogador.setEnergia(Math.max(0, jogador.getEnergia() - energiaPerdidaExtra));
                sb.append("Você escorrega numa pedra solta, perdendo ").append(energiaPerdidaExtra).append(" de energia extra!\n");
            }
        }

        double chanceBaseRecurso = 0.4; // Chance original da montanha
        if (jogador.temHabilidade("Rastreamento Aguçado")) {
            chanceBaseRecurso += 0.15; // Bônus menor em ambiente difícil e com baixa vegetação
            sb.append(jogador.getNome()).append(" usa a sua perícia em rastreamento, procurando por sinais entre as rochas e o gelo!\n");
        }
        chanceBaseRecurso = Math.min(Math.max(0.05, chanceBaseRecurso), 0.90); // Garante uma chance mínima e limita a máxima

        if (random.nextDouble() < chanceBaseRecurso) {
            List<Item> recursosAtuais = getRecursosDisponiveis();
            if (!recursosAtuais.isEmpty()) {
                int indiceRecurso = random.nextInt(recursosAtuais.size());
                Item recursoEncontradoOriginal = recursosAtuais.get(indiceRecurso);

                Item itemParaColetar = null;
                if (recursoEncontradoOriginal instanceof Material) {
                    Material matOriginal = (Material) recursoEncontradoOriginal;
                    itemParaColetar = new Material(matOriginal.getNome(), matOriginal.getPesoUnitario(), matOriginal.getTipo(), matOriginal.getResistencia(), matOriginal.getChaveImagem());
                } else if (recursoEncontradoOriginal instanceof Agua) {
                    Agua aguaOriginal = (Agua) recursoEncontradoOriginal;
                    itemParaColetar = new Agua(aguaOriginal.getNome(), aguaOriginal.getPesoUnitario(), aguaOriginal.getDurabilidadeOriginal(), aguaOriginal.getValorHidratacao(), aguaOriginal.getPureza(), aguaOriginal.getVolume(), aguaOriginal.getChaveImagem());
                }
                // Adicionar mais 'else if' para outros tipos de Item se necessário

                if (itemParaColetar != null) {
                    itemParaColetar.setQuantidade(1); // Encontra uma unidade do recurso
                    sb.append("Encrustado nas rochas, você encontra: ").append(itemParaColetar.getNome()).append(".\n");
                    if (jogador.getInventario().adicionarItem(itemParaColetar)) {
                        sb.append(itemParaColetar.getNome()).append(" (x1) foi coletado(a).\n");
                    } else {
                        sb.append("O seu inventário está cheio. Não foi possível coletar ").append(itemParaColetar.getNome()).append(".\n");
                    }
                } else {
                    sb.append("Você avista algo que poderia ser um recurso, mas não consegue alcançá-lo ou identificá-lo.\n");
                }
            } else {
                sb.append("Apesar da busca minuciosa, as encostas rochosas não revelam recursos úteis desta vez em ").append(getNome()).append(".\n");
            }
        } else {
            sb.append("A exploração em ").append(getNome()).append(" foi exaustiva e não revelou nada de novo, apenas o vento uivante.\n");
        }

        int energiaGasta = 15; // Custo base de energia para explorar montanhas
        if (terrenoAcidentado) energiaGasta += 5; // Custo adicional por terreno difícil
        if (jogador.temHabilidade("Metabolismo Eficiente")) energiaGasta = (int)(energiaGasta * 0.85); // Leve redução

        jogador.setEnergia(Math.max(0, jogador.getEnergia() - energiaGasta));
        sb.append(jogador.getNome()).append(" gastou ").append(energiaGasta).append(" de energia a explorar as altitudes.\n");

        // Chance de evento aleatório na montanha
        sb.append("--- A verificar eventos durante a exploração da montanha ---\n");
        String resultadoEventoExploracao = ge.sortearEExecutarEvento(jogador, this, numeroDoTurno);
        sb.append(resultadoEventoExploracao);

        return sb.toString();
    }

    /**
     * Modifica as condições climáticas da montanha, que são tipicamente instáveis.
     */
    @Override
    public String modificarClima() {
        StringBuilder sb = new StringBuilder();
        String climaAnterior = this.condicoesClimaticasPredominantes;

        if (climaInstavel && random.nextDouble() < 0.40) { // Chance maior de mudança climática na montanha
            int tipoMudanca = random.nextInt(3);
            switch (tipoMudanca) {
                case 0:
                    this.condicoesClimaticasPredominantes = "Nevasca Súbita na Montanha";
                    sb.append("ALERTA METEOROLÓGICO: Uma nevasca súbita e violenta atingiu ").append(getNome()).append("! A visibilidade é quase nula e o frio é cortante!\n");
                    break;
                case 1:
                    this.condicoesClimaticasPredominantes = "Ventos Cortantes e Perigosos";
                    sb.append("O tempo piorou drasticamente em ").append(getNome()).append(". Ventos gelados e cortantes dificultam cada movimento e ameaçam derrubá-lo(a)!\n");
                    break;
                case 2:
                    this.condicoesClimaticasPredominantes = "Névoa Densa e Gelada";
                    sb.append("Uma névoa densa e gelada desceu sobre ").append(getNome()).append(", reduzindo a visibilidade e aumentando a sensação de isolamento.\n");
                    break;
                default: // Caso improvável, mas para segurança
                    this.condicoesClimaticasPredominantes = "Frio Intenso e Céu Limpo";
                    break;
            }
        } else { // Se o clima não mudar drasticamente, volta a uma condição mais padrão ou permanece como estava.
            if (!this.condicoesClimaticasPredominantes.equals("Frio e Ventoso")) { // Evita redundância se já estiver no padrão
                this.condicoesClimaticasPredominantes = "Frio e Ventoso";
            }
            sb.append("O clima na montanha ").append(getNome()).append(" permanece: ").append(this.condicoesClimaticasPredominantes).append(". O ar é rarefeito e gélido.\n");
        }
        return sb.toString();
    }
}