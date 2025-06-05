package com.ultimafronteira.model;

import java.util.Random; // Import adicionado

public class Agua extends Item {
    private int valorHidratacao;
    private String pureza; // Ex: "Potável", "Contaminada", "Salobra"
    private double volume; // Em litros, por exemplo

    /**
     * Construtor para a classe Agua.
     *
     * @param nome Nome do item de água (ex: "Cantil de Água Pura").
     * @param pesoUnitario Peso de uma unidade do item.
     * @param durabilidade Durabilidade ou número de usos (ex: 1 para um gole único, ou mais para um cantil).
     * @param valorHidratacao Quanto de sede o item restaura.
     * @param pureza A pureza da água (ex: "Potável").
     * @param volume A quantidade de água em si.
     * @param chaveImagem A chave para a imagem do item (se houver).
     */
    public Agua(String nome, double pesoUnitario, int durabilidade, int valorHidratacao, String pureza, double volume, String chaveImagem) {
        // Chama o construtor de Item: (String nome, double pesoUnitario, int durabilidade, boolean empilhavel, String chaveImagem)
        // Água é geralmente empilhável.
        super(nome, pesoUnitario, durabilidade, true, chaveImagem);
        this.valorHidratacao = valorHidratacao;
        this.pureza = pureza;
        this.volume = volume;
    }

    /**
     * Construtor alternativo sem chave de imagem, caso a classe Item base não a exija
     * ou para cenários onde a imagem não é aplicável.
     */
    public Agua(String nome, double pesoUnitario, int durabilidade, int valorHidratacao, String pureza, double volume) {
        // Chama o construtor de Item: (String nome, double pesoUnitario, int durabilidade, boolean empilhavel, String chaveImagem)
        // Água é geralmente empilhável, chaveImagem é null.
        super(nome, pesoUnitario, durabilidade, true, null);
        this.valorHidratacao = valorHidratacao;
        this.pureza = pureza;
        this.volume = volume;
    }

    // Getters
    public int getValorHidratacao() {
        return valorHidratacao;
    }

    public String getPureza() {
        return pureza;
    }

    public double getVolume() {
        return volume;
    }

    /**
     * Método para o personagem usar (beber) a água.
     * Afeta a sede do jogador e, potencialmente, a vida se a água não for potável.
     * Decrementa a quantidade do item (se empilhável e durabilidade representa "usos por unidade" ou é 1).
     *
     * @param jogador O personagem que está usando a água.
     */
    @Override
    public void usar(Personagem jogador) {
        if (jogador == null) {
            System.out.println("Personagem não pode ser nulo ao usar água.");
            return;
        }

        if (getQuantidade() <= 0) { // Verifica se ainda há itens no stack
            System.out.println("Não há mais " + getNome() + " para consumir.");
            return;
        }

        String mensagemUso = jogador.getNome() + " bebeu " + getNome() + ".";
        System.out.println(mensagemUso);
        // Idealmente: Main.atualizarOutput(mensagemUso);


        if ("potável".equalsIgnoreCase(pureza)) {
            jogador.setSede(jogador.getSede() + valorHidratacao);
            System.out.println("Sua sede foi aliviada.");
            // Main.atualizarOutput("Sua sede foi aliviada.");
        } else {
            // Água não potável causa dano
            int danoAguaRuim = 5 + new Random().nextInt(6); // Dano entre 5-10
            jogador.setVida(Math.max(0, jogador.getVida() - danoAguaRuim));
            String mensagemContaminada = "A água estava contaminada! Você perdeu " + danoAguaRuim + " de vida.";
            System.out.println(mensagemContaminada);
            // Main.atualizarOutput(mensagemContaminada);
            // Poderia adicionar chance de doença aqui também.
        }

        // Consome uma unidade do item de água (se empilhável)
        // Se não for empilhável, a durabilidade pode representar usos.
        if (isEmpilhavel()) {
            setQuantidade(getQuantidade() - 1);
            if (getQuantidade() <= 0) {
                System.out.println(getNome() + " (todo o stack) acabou.");
                // A remoção completa do slot do inventário quando a quantidade chega a 0
                // é melhor tratada pela classe Inventario.
            }
        } else { // Se não for empilhável, a durabilidade representa usos
            setDurabilidade(getDurabilidade() - 1);
            if (getDurabilidade() <= 0) {
                System.out.println(getNome() + " acabou (durabilidade esgotada).");
                // A remoção do inventário deve ser feita pela classe Inventario.
                if (jogador.getInventario() != null) { // Check for safety
                    jogador.getInventario().removerItem(this.getNome(), 1); // Remove this specific non-stackable item
                }
            }
        }
    }

    @Override
    public String getDescricaoCompleta() {
        return super.getDescricaoCompleta() + // Chama a descrição da classe Item (inclui nome, peso, qtde, durab)
                String.format("\nHidratação: %d\nPureza: %s\nVolume: %.2f L",
                        valorHidratacao, pureza, volume);
    }
}