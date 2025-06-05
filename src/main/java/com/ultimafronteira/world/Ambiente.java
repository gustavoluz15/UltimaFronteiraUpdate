package com.ultimafronteira.world;

import com.ultimafronteira.model.Item;
import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.events.GerenciadorDeEventos;
import java.util.ArrayList;
import java.util.List;

public abstract class Ambiente {
    protected String nome;
    protected String descricao;
    protected String dificuldadeExploracao;
    protected List<Item> recursosDisponiveis;
    protected String condicoesClimaticasPredominantes;
    protected String chaveImagemFundo;

    public Ambiente(String nome, String descricao, String dificuldadeExploracao, String condicoesClimaticas, String chaveImagemFundo) {
        this.nome = nome;
        this.descricao = descricao;
        this.dificuldadeExploracao = dificuldadeExploracao;
        this.recursosDisponiveis = new ArrayList<>();
        this.condicoesClimaticasPredominantes = condicoesClimaticas;
        this.chaveImagemFundo = chaveImagemFundo;
    }

    public String getChaveImagemFundo() {
        return this.chaveImagemFundo;
    }

    // NOVO MÉTODO: Para obter a imagem para a miniatura de viagem. Por padrão, usa a mesma do fundo.
    public String getNomeImagem() {
        return this.chaveImagemFundo;
    }

    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getDificuldadeExploracao() { return dificuldadeExploracao; }
    public List<Item> getRecursosDisponiveis() { return new ArrayList<>(recursosDisponiveis); }
    public String getCondicoesClimaticasPredominantes() { return condicoesClimaticasPredominantes; }
    protected void adicionarRecurso(Item item) { if (item != null) this.recursosDisponiveis.add(item); }
    public abstract String explorar(Personagem jogador, GerenciadorDeEventos ge, int numeroDoTurno);
    public abstract String modificarClima();

    @Override
    public String toString() {
        return nome + " - " + descricao;
    }
}