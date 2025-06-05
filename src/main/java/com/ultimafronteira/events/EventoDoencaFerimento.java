package com.ultimafronteira.events;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.world.Ambiente;
import java.util.Random;

public class EventoDoencaFerimento extends Evento {

    private String tipoDeCondicao; // Ex: "Febre", "Infecção", "Intoxicação Alimentar", "Hipotermia"
    private String impactoDescricao; // Descrição do que a condição causa
    private String curaSugerida; // Item ou ação que pode curar/aliviar
    private Random random = new Random(); // Para aleatoriedade na seleção da condição

    /**
     * Construtor para criar um evento de doença ou ferimento com detalhes específicos.
     *
     * @param nome Nome genérico do evento (ex: "Mal Súbito").
     * @param descricao Descrição geral do que está a acontecer.
     * @param probabilidade Probabilidade base de ocorrência.
     * @param tipoDeCondicao O tipo específico de doença ou ferimento.
     * @param impactoDescricao Como a condição afeta o jogador.
     * @param curaSugerida O que pode ajudar a tratar a condição.
     */
    public EventoDoencaFerimento(String nome, String descricao, double probabilidade,
                                 String tipoDeCondicao, String impactoDescricao, String curaSugerida) {
        super(nome, descricao, probabilidade, "Saúde/Condição"); // "Saúde/Condição" é um bom tipo de impacto
        this.tipoDeCondicao = tipoDeCondicao;
        this.impactoDescricao = impactoDescricao;
        this.curaSugerida = curaSugerida;
    }

    /**
     * Construtor padrão que cria um evento de condição adversa genérica.
     * Uma condição específica será sorteada quando o evento for executado.
     */
    public EventoDoencaFerimento() {
        super("Condição Adversa Inesperada", // Nome mais específico para o evento
                "Você não se está a sentir bem. Algo no ambiente ou um descuido afetou a sua saúde.",
                0.20, // Probabilidade base
                "Negativo"); // Tipo de impacto genérico
        // tipoDeCondicao, impactoDescricao, e curaSugerida serão definidos no método executar.
    }

    // Getters
    public String getTipoDeCondicao() {
        return tipoDeCondicao;
    }

    public String getImpactoDescricao() {
        return impactoDescricao;
    }

    public String getCuraSugerida() {
        return curaSugerida;
    }

    /**
     * Executa o evento de doença ou ferimento.
     * Se nenhuma condição específica foi definida no construtor, uma será sorteada.
     * Aplica os efeitos da condição ao jogador.
     *
     * @param jogador O personagem do jogador.
     * @param local O ambiente atual (pode influenciar o tipo de condição).
     * @param numeroDoTurno O número do turno atual (pode influenciar a gravidade).
     * @return Uma string descrevendo a condição e seus efeitos.
     */
    @Override
    public String executar(Personagem jogador, Ambiente local, int numeroDoTurno) {
        StringBuilder sb = new StringBuilder();

        // Se o tipoDeCondicao não foi definido no construtor, sorteia um.
        if (this.tipoDeCondicao == null) {
            int condicaoSorteada = random.nextInt(4); // Aumentado para 4 tipos de condições aleatórias
            switch (condicaoSorteada) {
                case 0:
                    this.nome = "Febre Alta";
                    this.tipoDeCondicao = "Febre";
                    this.descricao = "Um calafrio percorre a sua espinha e a sua testa queima.";
                    this.impactoDescricao = "Perda de energia e sanidade.";
                    this.curaSugerida = "Analgésico ou descanso em local seguro.";
                    break;
                case 1:
                    this.nome = "Ferimento Infeccionado";
                    this.tipoDeCondicao = "Infecção";
                    this.descricao = "Você percebe um corte antigo, agora vermelho e inchado. Parece estar infecionado.";
                    this.impactoDescricao = "Dano contínuo à vida e risco de piorar.";
                    this.curaSugerida = "Antisséptico ou Bandagens Limpas.";
                    break;
                case 2:
                    this.nome = "Intoxicação Alimentar";
                    this.tipoDeCondicao = "Intoxicação Alimentar";
                    this.descricao = "O seu estômago revira-se violentamente. A última coisa que você comeu não caiu bem.";
                    this.impactoDescricao = "Perda de fome, sede e energia.";
                    this.curaSugerida = "Água limpa e descanso. Evitar comida suspeita.";
                    break;
                case 3:
                    this.nome = "Princípio de Hipotermia";
                    this.tipoDeCondicao = "Hipotermia Leve";
                    // Considerar o ambiente para esta condição.
                    // Se o local for frio, a probabilidade poderia ser maior ou a descrição diferente.
                    this.descricao = "Um frio intenso toma conta do seu corpo, dificultando os movimentos.";
                    this.impactoDescricao = "Perda gradual de vida e energia. Redução da destreza.";
                    this.curaSugerida = "Encontrar abrigo, fazer uma fogueira, roupas quentes.";
                    break;
            }
        }

        sb.append("ALERTA DE SAÚDE: ").append(getNome().toUpperCase()).append("!\n");
        sb.append(getDescricao()).append("\n");
        sb.append("Condição Adquirida: ").append(tipoDeCondicao).append(".\n");
        sb.append("Impacto: ").append(impactoDescricao).append("\n");

        if (curaSugerida != null && !curaSugerida.isEmpty()) {
            sb.append("Tratamento Sugerido: ").append(curaSugerida).append("\n");
        }

        // Aplicar efeitos com base no tipoDeCondicao
        if (jogador == null) {
            sb.append("AVISO: Personagem não encontrado para aplicar efeitos da condição.\n");
            return sb.toString();
        }

        // Aplica os efeitos com base no tipoDeCondicao agora preenchido
        switch (this.tipoDeCondicao) {
            case "Febre":
                int perdaEnergiaFebre = 15 + (numeroDoTurno / 10); // Escalonamento leve
                int perdaSanidadeFebre = 5 + (numeroDoTurno / 15);
                jogador.setEnergia(jogador.getEnergia() - perdaEnergiaFebre);
                jogador.setSanidade(jogador.getSanidade() - perdaSanidadeFebre);
                sb.append(jogador.getNome()).append(" perdeu ").append(perdaEnergiaFebre).append(" de energia e ").append(perdaSanidadeFebre).append(" de sanidade devido à febre.\n");
                break;

            case "Infecção":
                int danoVidaInfeccao = 10 + (numeroDoTurno / 8);
                jogador.setVida(jogador.getVida() - danoVidaInfeccao);
                sb.append("A infecção causa ").append(danoVidaInfeccao).append(" de dano a ").append(jogador.getNome()).append("! Precisa de tratamento.\n");
                break;

            case "Intoxicação Alimentar":
                int perdaFomeIntox = 20 + (numeroDoTurno / 5);
                int perdaSedeIntox = 10 + (numeroDoTurno / 7);
                jogador.setFome(Math.max(0, jogador.getFome() - perdaFomeIntox));
                jogador.setSede(Math.max(0, jogador.getSede() - perdaSedeIntox));
                jogador.setEnergia(jogador.getEnergia() - (10 + numeroDoTurno/10));
                sb.append(jogador.getNome()).append(" perdeu ").append(perdaFomeIntox).append(" de fome e ").append(perdaSedeIntox).append(" de sede.\n");
                break;

            case "Hipotermia Leve": // Corresponde ao caso sorteado
            case "Hipotermia": // Corresponde ao caso da versão f7209b5...
                jogador.setVida(jogador.getVida() - (10 + numeroDoTurno / 7));
                jogador.setEnergia(jogador.getEnergia() - (15 + numeroDoTurno / 5));
                sb.append(jogador.getNome()).append(" está a tremer de frio e perde vida e energia.\n");
                break;

            // Casos da versão f7209b5... que não foram sorteados, mas podem ser usados se o construtor específico for chamado
            case "Desidratação Severa":
                jogador.setVida(jogador.getVida() - (10 + numeroDoTurno / 5));
                jogador.setSede(Math.max(0, jogador.getSede() - (30 + numeroDoTurno / 3)));
                jogador.setSanidade(jogador.getSanidade() - (5 + numeroDoTurno / 10));
                sb.append(jogador.getNome()).append(" está gravemente desidratado, afetando a sua vida e sanidade.\n");
                break;
            case "Fratura Exposta":
                jogador.setVida(jogador.getVida() - (25 + numeroDoTurno / 3));
                jogador.setEnergia(jogador.getEnergia() - (20 + numeroDoTurno / 4));
                sb.append(jogador.getNome()).append(" sofreu uma fratura grave! Precisa de cuidados médicos urgentes.\n");
                break;
            default:
                sb.append("Nenhum efeito de condição específico aplicado desta vez.\n");
                break;
        }

        if (jogador.getVida() <= 0) {
            sb.append(jogador.getNome()).append(" sucumbiu à sua condição...\n");
            // Aqui poderia haver lógica adicional para fim de jogo ou consequências graves.
        }

        return sb.toString();
    }
}