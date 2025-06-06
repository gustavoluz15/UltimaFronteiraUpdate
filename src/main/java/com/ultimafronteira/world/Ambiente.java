package com.ultimafronteira.world;

import com.ultimafronteira.events.GerenciadorDeEventos;
import com.ultimafronteira.model.Item;
import com.ultimafronteira.model.Personagem;
import java.util.ArrayList;
import java.util.List;

public abstract class Ambiente {
    protected String nome;
    protected String descricao;
    protected String dificuldadeExploracao;
    protected List<Item> recursosDisponiveis;
    protected String condicoesClimaticasPredominantes;
    private String chaveImagemFundo;

    public Ambiente(String nome, String descricao, String dificuldade, String clima, String chaveImagemFundo) {
        this.nome = nome;
        this.descricao = descricao;
        this.dificuldadeExploracao = dificuldade;
        this.condicoesClimaticasPredominantes = clima;
        this.recursosDisponiveis = new ArrayList<>();
        this.chaveImagemFundo = chaveImagemFundo;
    }

    public String getChaveImagemFundo() {
        return chaveImagemFundo;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getDificuldadeExploracao() {
        return dificuldadeExploracao;
    }

    public List<Item> getRecursosDisponiveis() {
        return new ArrayList<>(recursosDisponiveis);
    }

    public String getCondicoesClimaticasPredominantes() {
        return condicoesClimaticasPredominantes;
    }

    protected void adicionarRecurso(Item item) {
        if (item != null) {
            this.recursosDisponiveis.add(item);
        }
    }

    public abstract String explorar(Personagem jogador, GerenciadorDeEventos ge, int numeroDoTurno);
    public abstract String modificarClima();

    @Override
    public String toString() {
        return nome + " - " + descricao;
    }
}