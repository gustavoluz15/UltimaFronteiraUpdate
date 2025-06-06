package com.ultimafronteira.world;

import com.ultimafronteira.model.Item;
import com.ultimafronteira.model.Personagem;
// Removido import desnecessário: import com.ultimafronteira.events.GerenciadorDeEventos;
import java.util.ArrayList;
import java.util.List;

public abstract class Ambiente {
    protected String nome;
    protected String descricao;
    protected String dificuldadeExploracao;
    protected List<Item> recursosDisponiveis;
    protected String condicoesClimaticasPredominantes;
    protected String chaveImagemFundo;

    public Ambiente(String nome, String descricao, String dificuldade, String clima, String chaveImagemFundo) {
        this.nome = nome;
        this.descricao = descricao;
        this.dificuldadeExploracao = dificuldade;
        this.recursosDisponiveis = new ArrayList<>();
        this.condicoesClimaticasPredominantes = clima;
        this.chaveImagemFundo = chaveImagemFundo;
    }

    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getChaveImagemFundo() { return this.chaveImagemFundo; }
    public String getNomeImagem() { return this.chaveImagemFundo; }
    public String getCondicoesClimaticasPredominantes() { return condicoesClimaticasPredominantes; }
    protected void adicionarRecurso(Item item) { if (item != null) this.recursosDisponiveis.add(item); }

    // CORREÇÃO: Assinatura do método explorar ajustada.
    public abstract String explorar(Personagem jogador, int numeroDoTurno);

    public abstract String modificarClima();

    @Override
    public String toString() {
        return nome + " - " + descricao;
    }
}