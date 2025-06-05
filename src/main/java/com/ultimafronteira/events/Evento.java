package com.ultimafronteira.events;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.world.Ambiente;

public abstract class Evento {
    protected String nome;
    protected String descricao;
    protected double probabilidadeOcorrenciaBase;
    protected String tipoImpacto;

    public Evento(String nome, String descricao, double probabilidadeOcorrenciaBase, String tipoImpacto) {
        this.nome = nome;
        this.descricao = descricao;
        this.probabilidadeOcorrenciaBase = probabilidadeOcorrenciaBase;
        this.tipoImpacto = tipoImpacto;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getProbabilidadeOcorrenciaBase() {
        return probabilidadeOcorrenciaBase;
    }

    public String getTipoImpacto() {
        return tipoImpacto;
    }

    /**
     * Tenta fazer o evento ocorrer com base na probabilidade ajustada.
     * A probabilidade efetiva aumenta com o número do turno, até um bônus máximo
     * e um teto de probabilidade geral.
     *
     * @param jogador O personagem do jogador.
     * @param local O ambiente atual.
     * @param numeroDoTurno O número do turno atual no jogo.
     * @return true se o evento ocorrer, false caso contrário.
     */
    public boolean tentarOcorrer(Personagem jogador, Ambiente local, int numeroDoTurno) {
        // Bloco de código resolvido. Você precisa decidir qual lógica é a correta para o seu jogo.
        // Opção 1 (do commit f7209b5...):
        double aumentoPorTurno = 0.002; // 0.2% de aumento na chance por turno
        // Opção 2 (do HEAD, se preferir o valor 0.010):
        // double aumentoPorTurno = 0.010;

        double bonusMaximo = 0.25; // Aumento máximo de 25% na probabilidade absoluta
        double tetoProbabilidade = 0.95; // Probabilidade efetiva não passará de 95%

        // Calcula a probabilidade efetiva do evento ocorrer
        double probEfetiva = this.probabilidadeOcorrenciaBase + (numeroDoTurno * aumentoPorTurno);

        // Aplica o bônus máximo: a probabilidade efetiva não pode exceder a base + bônus máximo
        probEfetiva = Math.min(probEfetiva, this.probabilidadeOcorrenciaBase + bonusMaximo);

        // Aplica o teto de probabilidade: a probabilidade efetiva não pode exceder o teto geral
        probEfetiva = Math.min(probEfetiva, tetoProbabilidade);

        // Gera um número aleatório entre 0.0 (inclusivo) e 1.0 (exclusivo)
        // Se o número aleatório for menor que a probabilidade efetiva, o evento ocorre
        return Math.random() < probEfetiva;
    }

    /**
     * Executa a lógica do evento.
     * Este método deve ser implementado pelas subclasses de Evento.
     *
     * @param jogador O personagem do jogador afetado pelo evento.
     * @param local O ambiente onde o evento ocorre.
     * @param numeroDoTurno O número do turno atual.
     * @return Uma string descrevendo o resultado do evento.
     */
    public abstract String executar(Personagem jogador, Ambiente local, int numeroDoTurno);

    @Override
    public String toString() {
        return nome + ": " + descricao;
    }
}