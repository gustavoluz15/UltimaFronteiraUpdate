package com.ultimafronteira.world;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.model.Material;
import com.ultimafronteira.model.Agua; // Import não utilizado diretamente, mas mantido por consistência se necessário no futuro.
import com.ultimafronteira.model.Alimento;
import com.ultimafronteira.model.Ferramenta;
import com.ultimafronteira.model.Item;
import com.ultimafronteira.events.GerenciadorDeEventos;

import java.util.ArrayList; // Certifique-se de que este import está presente
import java.util.List;    // Certifique-se de que este import está presente
import java.util.Random;  // Para lógica aleatória

public class AmbienteRuinas extends Ambiente {
    private boolean estruturasInstaveis;
    private boolean presencaOutrosSobreviventes; // Pode levar a eventos especiais
    private boolean baixoRiscoClimatico; // Ruínas podem oferecer algum abrigo
    private Random random = new Random();

    /**
     * Construtor para o ambiente Ruínas.
     * @param nome Nome das ruínas (ex: "Cidade Devastada").
     * @param descricao Descrição das ruínas.
     */
    public AmbienteRuinas(String nome, String descricao) {
        // Chama o construtor da classe base (Ambiente)
        // Resolvido para incluir a chave da imagem de fundo específica para ruínas.
        super(nome, descricao, "Moderada (risco de desmoronamento)", "Variável, parcialmente abrigado", "fundo_cidade_ruinas");
        this.estruturasInstaveis = true;
        this.presencaOutrosSobreviventes = (random.nextDouble() < 0.25); // 25% de chance de haver outros sobreviventes
        this.baixoRiscoClimatico = true;
        inicializarRecursos();
    }

    /**
     * Inicializa os recursos específicos que podem ser encontrados nestas ruínas.
     */
    private void inicializarRecursos() {
        // Material(String nome, double pesoUnitario, String tipo, int resistencia, String chaveImagem)
        adicionarRecurso(new Material("Metal Retorcido", 1.5, "Metal", 5, "item_metal_retorcido"));
        adicionarRecurso(new Material("Tijolos Quebrados", 2.0, "Construção", 3, "item_tijolo_quebrado"));

        if (random.nextDouble() < 0.25) { // Chance um pouco maior de encontrar uma ferramenta
            // Ferramenta(String nome, double pesoUnitario, int durabilidade, String tipo, int eficiencia, String chaveImagem)
            adicionarRecurso(new Ferramenta("Ferramenta Enferrujada", 1.0, random.nextInt(3) + 2, "Variada", random.nextInt(2) + 1, "item_ferramenta_enferrujada")); // Durabilidade e eficiência variáveis
        }
        if (random.nextDouble() < 0.20) { // Chance um pouco maior de encontrar enlatados
            // Alimento(String nome, double pesoUnitario, int durabilidade, int valorNutricional, int valorCura, String tipo, String chaveImagem)
            adicionarRecurso(new Alimento("Enlatado Antigo", 0.8, 1, 25, 5, "Enlatado", "item_comida_enlatado"));
        }
        if (random.nextDouble() < 0.1) {
            adicionarRecurso(new Material("Componentes Eletrónicos Quebrados", 0.5, "Eletrónico", 2, "item_eletronicos_quebrados"));
        }
    }

    // Getters para as propriedades específicas das ruínas
    public boolean isEstruturasInstaveis() { return estruturasInstaveis; }
    public boolean isPresencaOutrosSobreviventes() { return presencaOutrosSobreviventes; }
    public boolean isBaixoRiscoClimatico() { return baixoRiscoClimatico; }

    /**
     * Lógica de exploração específica para as ruínas.
     * Considera estruturas instáveis e a chance de encontrar recursos ou eventos.
     */
    @Override
    public String explorar(Personagem jogador, GerenciadorDeEventos ge, int numeroDoTurno) {
        StringBuilder sb = new StringBuilder();
        sb.append(jogador.getNome()).append(" vasculha cuidadosamente os escombros de ").append(getNome()).append(".\n");

        if (estruturasInstaveis && random.nextDouble() < 0.15) { // Aumentei a chance de perigo
            int danoQueda = random.nextInt(5) + 3; // 3-7 de dano
            int energiaPerdidaQueda = random.nextInt(5) + 5; // 5-9 de energia
            sb.append("CUIDADO! Parte da estrutura rangeu e alguns detritos caíram perto de você!\n");
            jogador.setVida(jogador.getVida() - danoQueda);
            jogador.setEnergia(Math.max(0, jogador.getEnergia() - energiaPerdidaQueda));
            sb.append("Você se assustou e alguns escombros atingiram-no de raspão. Perdeu ").append(danoQueda).append(" de vida e ").append(energiaPerdidaQueda).append(" de energia!\n");
        }

        // Chance base de encontrar recurso
        double chanceBaseRecurso = 0.55;
        if (jogador.temHabilidade("Engenhosidade") || jogador.temHabilidade("Rastreamento Aguçado")) { // Mecânico pode ser bom em encontrar coisas em ruínas
            chanceBaseRecurso += 0.20;
            sb.append(jogador.getNome()).append(" procura com mais atenção e método entre os destroços.\n");
        }
        chanceBaseRecurso = Math.min(Math.max(0.1, chanceBaseRecurso), 0.90); // Garante chance mínima

        if (random.nextDouble() < chanceBaseRecurso) {
            List<Item> recursosAtuais = getRecursosDisponiveis();
            if (!recursosAtuais.isEmpty()) {
                int indiceRecurso = random.nextInt(recursosAtuais.size());
                Item recursoEncontradoOriginal = recursosAtuais.get(indiceRecurso);

                Item itemParaColetar = null;
                // Criar nova instância do item encontrado
                if (recursoEncontradoOriginal instanceof Material) {
                    Material matOriginal = (Material) recursoEncontradoOriginal;
                    itemParaColetar = new Material(matOriginal.getNome(), matOriginal.getPesoUnitario(), matOriginal.getTipo(), matOriginal.getResistencia(), matOriginal.getChaveImagem());
                } else if (recursoEncontradoOriginal instanceof Ferramenta) {
                    Ferramenta ferrOriginal = (Ferramenta) recursoEncontradoOriginal;
                    itemParaColetar = new Ferramenta(ferrOriginal.getNome(), ferrOriginal.getPesoUnitario(), ferrOriginal.getDurabilidadeOriginal(), ferrOriginal.getTipo(), ferrOriginal.getEficiencia(), ferrOriginal.getChaveImagem());
                } else if (recursoEncontradoOriginal instanceof Alimento) {
                    Alimento alimOriginal = (Alimento) recursoEncontradoOriginal;
                    itemParaColetar = new Alimento(alimOriginal.getNome(), alimOriginal.getPesoUnitario(), alimOriginal.getDurabilidadeOriginal(), alimOriginal.getValorNutricional(), alimOriginal.getValorCura(), alimOriginal.getTipo(), alimOriginal.getChaveImagem());
                }
                // Adicionar mais 'else if' para outros tipos de Item, se houver

                if (itemParaColetar != null) {
                    itemParaColetar.setQuantidade(1); // Encontra uma unidade do recurso
                    sb.append("Em meio aos destroços, você encontrou algo útil em ").append(getNome()).append(": ").append(itemParaColetar.getNome()).append(".\n");
                    if (jogador.getInventario().adicionarItem(itemParaColetar)) {
                        sb.append(itemParaColetar.getNome()).append(" (x1) foi coletado(a).\n");
                    } else {
                        sb.append("O seu inventário está cheio. Não foi possível coletar ").append(itemParaColetar.getNome()).append(".\n");
                    }
                } else {
                    sb.append("Você encontrou alguns fragmentos, mas nada que pudesse ser aproveitado.\n");
                }
            } else {
                sb.append(getNome()).append(" parece já ter sido completamente saqueada, ou os recursos estão bem escondidos.\n");
            }
        } else {
            sb.append("A exploração das ruínas não trouxe nada de novo, apenas o silêncio ecoante do que foi perdido.\n");
        }

        int energiaGasta = 12; // Custo base para explorar ruínas
        if (jogador.temHabilidade("Metabolismo Eficiente")) energiaGasta = (int)(energiaGasta * 0.85);

        jogador.setEnergia(Math.max(0, jogador.getEnergia() - energiaGasta));
        sb.append(jogador.getNome()).append(" gastou ").append(energiaGasta).append(" de energia a explorar as ruínas.\n");

        // Chance de evento aleatório nas ruínas
        // Eventos podem incluir encontrar outros sobreviventes (amigáveis ou hostis), armadilhas, etc.
        sb.append("--- A verificar eventos nas ruínas desoladas ---\n");
        String resultadoEventoExploracao = ge.sortearEExecutarEvento(jogador, this, numeroDoTurno);
        sb.append(resultadoEventoExploracao);

        return sb.toString();
    }

    /**
     * Modifica as condições climáticas dentro das ruínas.
     * Geralmente oferece algum abrigo, mas pode haver correntes de ar.
     */
    @Override
    public String modificarClima() {
        StringBuilder sb = new StringBuilder();
        String climaAnterior = this.condicoesClimaticasPredominantes;

        if (baixoRiscoClimatico) {
            if (random.nextDouble() < 0.10) { // Pequena chance de alguma mudança
                this.condicoesClimaticasPredominantes = "Correntes de Ar Gelado";
                sb.append("O vento assobia através das frestas das ruínas em ").append(getNome()).append(", trazendo um ar frio.\n");
            } else {
                this.condicoesClimaticasPredominantes = "Parcialmente Protegido e Estável";
                sb.append("As ruínas de ").append(getNome()).append(" oferecem um bom abrigo. O clima interno é estável e mais ameno que no exterior.\n");
            }
        } else { // Se baixoRiscoClimatico for false (improvável por definição, mas para consistência)
            if (random.nextDouble() < 0.20) {
                this.condicoesClimaticasPredominantes = "Vento Forte com Poeira e Detritos";
                sb.append("O tempo mudou! Um vento forte levanta poeira e pequenos detritos ao redor de ").append(getNome()).append(".\n");
            } else {
                sb.append("O clima em ").append(getNome()).append(" permanece: ").append(this.condicoesClimaticasPredominantes).append(".\n");
            }
        }
        return sb.toString();
    }
}