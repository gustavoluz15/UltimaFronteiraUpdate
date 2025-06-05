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

    public boolean tentarOcorrer(Personagem jogador, Ambiente local, int numeroDoTurno) {
        double aumentoPorTurno = 0.010;
        double bonusMaximo = 0.25;
        double tetoProbabilidade = 0.95;

        double probEfetiva = this.probabilidadeOcorrenciaBase + (numeroDoTurno * aumentoPorTurno);
        probEfetiva = Math.min(probEfetiva, this.probabilidadeOcorrenciaBase + bonusMaximo);
        probEfetiva = Math.min(probEfetiva, tetoProbabilidade);

        return Math.random() < probEfetiva;
    }

    public abstract String executar(Personagem jogador, Ambiente local, int numeroDoTurno);

    @Override
    public String toString() {
        return nome + ": " + descricao;
    }
}