package com.ultimafronteira.world;

import com.ultimafronteira.model.Item;
import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.events.GerenciadorDeEventos;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstrata que representa um ambiente no mundo do jogo.
 * Cada ambiente tem um nome, descrição, dificuldade de exploração, recursos,
 * condições climáticas e uma imagem de fundo associada.
 */
public abstract class Ambiente {
    protected String nome;
    protected String descricao;
    protected String dificuldadeExploracao; // Ex: "Fácil", "Moderada", "Difícil"
    protected List<Item> recursosDisponiveis; // Itens que podem ser encontrados ao explorar
    protected String condicoesClimaticasPredominantes; // Ex: "Ensolarado", "Chuvoso", "Nevando"
    protected String chaveImagemFundo; // Chave para identificar a imagem de fundo do ambiente

    /**
     * Construtor para a classe Ambiente.
     *
     * @param nome O nome do ambiente (ex: "Floresta Densa").
     * @param descricao Uma breve descrição do ambiente.
     * @param dificuldadeExploracao Nível de dificuldade para explorar este ambiente.
     * @param condicoesClimaticas As condições climáticas típicas do ambiente.
     * @param chaveImagemFundo A chave de referência para a imagem de fundo do ambiente.
     */
    public Ambiente(String nome, String descricao, String dificuldadeExploracao, String condicoesClimaticas, String chaveImagemFundo) {
        this.nome = nome;
        this.descricao = descricao;
        this.dificuldadeExploracao = dificuldadeExploracao;
        this.recursosDisponiveis = new ArrayList<>();
        this.condicoesClimaticasPredominantes = condicoesClimaticas;
        this.chaveImagemFundo = chaveImagemFundo;
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getDificuldadeExploracao() {
        return dificuldadeExploracao;
    }

    /**
     * Retorna uma cópia da lista de recursos disponíveis para proteger a lista interna.
     * @return Uma nova lista contendo os recursos disponíveis.
     */
    public List<Item> getRecursosDisponiveis() {
        return new ArrayList<>(recursosDisponiveis);
    }

    public String getCondicoesClimaticasPredominantes() {
        return condicoesClimaticasPredominantes;
    }

    public String getChaveImagemFundo() {
        return this.chaveImagemFundo;
    }

    /**
     * Retorna o nome/chave da imagem para ser usada em miniaturas ou pré-visualizações de viagem.
     * Por padrão, usa a mesma imagem de fundo do ambiente.
     * Pode ser sobrescrito por subclasses se uma imagem diferente for necessária para miniaturas.
     * @return A chave da imagem para miniaturas.
     */
    public String getNomeImagem() {
        return this.chaveImagemFundo;
    }

    /**
     * Adiciona um recurso à lista de recursos disponíveis neste ambiente.
     * @param item O item a ser adicionado como recurso.
     */
    protected void adicionarRecurso(Item item) {
        if (item != null) {
            this.recursosDisponiveis.add(item);
        }
    }

    /**
     * Método abstrato que define a lógica de exploração do ambiente.
     * Deve ser implementado por subclasses para detalhar o que acontece quando
     * um personagem explora este ambiente específico.
     *
     * @param jogador O personagem que está explorando.
     * @param ge O gerenciador de eventos, para possibilitar a ocorrência de eventos durante a exploração.
     * @param numeroDoTurno O número do turno atual do jogo.
     * @return Uma string descrevendo o resultado da exploração.
     */
    public abstract String explorar(Personagem jogador, GerenciadorDeEventos ge, int numeroDoTurno);

    /**
     * Método abstrato que define como o clima pode mudar neste ambiente.
     * Deve ser implementado por subclasses.
     * @return Uma string descrevendo a mudança climática ou o estado atual do clima.
     */
    public abstract String modificarClima();

    @Override
    public String toString() {
        return nome + " - " + descricao + " (Clima: " + condicoesClimaticasPredominantes + ")";
    }
}