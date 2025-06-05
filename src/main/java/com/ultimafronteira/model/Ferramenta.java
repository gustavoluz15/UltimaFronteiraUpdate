package com.ultimafronteira.model;

import java.util.Random; // Embora não usado diretamente, pode ser útil para futuras lógicas

public class Ferramenta extends Item {
    private String tipo; // Ex: "Corte", "Escavação", "Construção", "Reparo"
    private int eficiencia; // Um valor que indica quão boa a ferramenta é para sua função

    /**
     * Construtor para a classe Ferramenta.
     *
     * @param nome Nome da ferramenta (ex: "Machadinha de Pedra").
     * @param pesoUnitario Peso de uma unidade do item.
     * @param durabilidade Durabilidade da ferramenta (quantos usos antes de quebrar, -1 para inquebrável).
     * @param tipo O tipo de ferramenta, indicando seu propósito principal.
     * @param eficiencia A eficácia da ferramenta em sua função.
     * @param chaveImagem A chave para a imagem do item (se houver).
     */
    public Ferramenta(String nome, double pesoUnitario, int durabilidade, String tipo, int eficiencia, String chaveImagem) {
        // Chama o construtor de Item: (String nome, double pesoUnitario, int durabilidade, boolean empilhavel, String chaveImagem)
        // Ferramentas geralmente não são empilháveis.
        super(nome, pesoUnitario, durabilidade, false, chaveImagem);
        this.tipo = tipo;
        this.eficiencia = eficiencia;
    }

    /**
     * Construtor alternativo para a classe Ferramenta sem a chave de imagem.
     */
    public Ferramenta(String nome, double pesoUnitario, int durabilidade, String tipo, int eficiencia) {
        // Chama o construtor de Item: (String nome, double pesoUnitario, int durabilidade, boolean empilhavel, String chaveImagem)
        // Ferramentas geralmente não são empilháveis, chaveImagem é null.
        super(nome, pesoUnitario, durabilidade, false, null);
        this.tipo = tipo;
        this.eficiencia = eficiencia;
    }

    // Getters
    public String getTipo() {
        return tipo;
    }

    public int getEficiencia() {
        return eficiencia;
    }

    /**
     * Método para o personagem usar a ferramenta.
     * A ação específica de "usar" uma ferramenta pode variar (ex: coletar recursos, reparar algo).
     * Este método lida com o feedback básico e a durabilidade.
     * A lógica de efeito real (ex: adicionar item ao inventário após coleta) seria tratada
     * no contexto onde a ferramenta é usada (ex: em um método de interação com o ambiente).
     *
     * @param jogador O personagem que está usando a ferramenta.
     */
    @Override
    public void usar(Personagem jogador) {
        if (jogador == null) {
            System.out.println("Personagem não pode ser nulo ao tentar usar uma ferramenta.");
            return;
        }

        // Ferramentas não empilháveis têm quantidade 1. Se a durabilidade for o controle de uso:
        if (getDurabilidade() <= 0 && getDurabilidadeOriginal() != -1) { // Verifica se já está quebrada (e não é inquebrável)
            System.out.println(getNome() + " está quebrado(a) e não pode ser usado(a).");
            return;
        }

        String mensagemUso = jogador.getNome() + " usou a ferramenta: " + getNome() + " (Tipo: " + tipo + ", Eficiência: " + eficiencia + ").";
        System.out.println(mensagemUso);
        // Idealmente: Main.atualizarOutput(mensagemUso);

        if (getDurabilidadeOriginal() != -1) { // Apenas decrementa se não for inquebrável
            setDurabilidade(getDurabilidade() - 1);
            // System.out.println(getNome() + " - Durabilidade restante: " + getDurabilidade());
            // Main.atualizarOutput(getNome() + " - Durabilidade restante: " + getDurabilidade());

            if (getDurabilidade() == 0) {
                String mensagemQuebra = getNome() + " quebrou!";
                System.out.println(mensagemQuebra);
                // Main.atualizarOutput(mensagemQuebra);
                // A remoção do item quebrado do inventário deve ser tratada pela classe Inventario
                // ou pela lógica do jogo que chamou o método usar.
                if (jogador.getInventario() != null) {
                    jogador.getInventario().removerItem(this.getNome(), 1); // Remove este item específico
                }
            }
        }
        // A lógica específica do que a ferramenta FAZ (coletar madeira, minerar, etc.)
        // seria chamada pelo sistema de jogo, e não diretamente aqui.
        // Este método 'usar' é mais sobre o ato de interagir com o item em si e gerir sua durabilidade.
    }

    @Override
    public String getDescricaoCompleta() {
        return super.getDescricaoCompleta() + // Chama a descrição da classe Item (inclui nome, peso, qtde=1, durab)
                String.format("\nTipo: %s\nEficiência: %d",
                        tipo, eficiencia);
    }
}