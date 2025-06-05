package com.ultimafronteira.model;

import java.util.Random; // Para efeitos aleatórios ou variações, se necessário

public class Remedio extends Item {
    private String tipo; // Ex: "Bandagem", "Antídoto", "Analgésico", "Kit Médico"
    private String efeitoDescritivo; // Descrição textual do efeito principal
    private int valorEfeito; // Quantidade numérica do efeito (ex: 25 para cura de vida)
    private String atributoAlvo; // Atributo do personagem que o remédio afeta (ex: "vida", "sanidade", "status_envenenado")

    /**
     * Construtor para a classe Remedio.
     * Remédios são geralmente consumíveis (durabilidade 1 por padrão) e empilháveis.
     *
     * @param nome Nome do remédio (ex: "Kit de Primeiros Socorros").
     * @param pesoUnitario Peso de uma unidade do remédio.
     * @param tipo O tipo de remédio.
     * @param efeitoDescritivo Descrição do que o remédio faz.
     * @param valorEfeito A magnitude do efeito (ex: pontos de vida curados).
     * @param atributoAlvo O atributo do personagem que este remédio primariamente afeta.
     * @param chaveImagem A chave para a imagem do item (pode ser null).
     */
    public Remedio(String nome, double pesoUnitario, String tipo, String efeitoDescritivo, int valorEfeito, String atributoAlvo, String chaveImagem) {
        // Remédios são empilháveis e, por padrão, têm 1 uso (durabilidade 1).
        super(nome, pesoUnitario, 1, true, chaveImagem); // empilhavel = true, durabilidade = 1
        this.tipo = tipo;
        this.efeitoDescritivo = efeitoDescritivo;
        this.valorEfeito = valorEfeito;
        this.atributoAlvo = atributoAlvo;
    }

    /**
     * Construtor alternativo sem chaveImagem.
     */
    public Remedio(String nome, double pesoUnitario, String tipo, String efeitoDescritivo, int valorEfeito, String atributoAlvo) {
        this(nome, pesoUnitario, tipo, efeitoDescritivo, valorEfeito, atributoAlvo, null);
    }

    /**
     * Construtor simplificado (usado na versão f7209b5... de Main.java).
     * Assume que o valorEfeito e atributoAlvo podem ser inferidos ou não são estritamente necessários para a funcionalidade básica.
     * @param nome Nome do remédio.
     * @param peso Peso do item.
     * @param tipo Tipo de remédio (ex: "Consumível").
     * @param efeito Descrição do efeito.
     */
    public Remedio(String nome, double peso, String tipo, String efeito) {
        super(nome, peso, 1, true, null); // durabilidade 1, empilhável, sem imagem
        this.tipo = tipo;
        this.efeitoDescritivo = efeito;
        // Tenta inferir valorEfeito e atributoAlvo se possível, ou define padrões
        if (efeito != null && efeito.toLowerCase().contains("cura") && efeito.toLowerCase().contains("vida")) {
            this.atributoAlvo = "vida";
            // Tenta extrair um número do efeito string. Ex: "Cura 15 de vida" -> 15
            try {
                String[] parts = efeito.replaceAll("[^0-9]", " ").trim().split("\\s+");
                if (parts.length > 0 && !parts[0].isEmpty()) {
                    this.valorEfeito = Integer.parseInt(parts[0]);
                } else {
                    this.valorEfeito = 15; // Default healing value if not parsable
                }
            } catch (NumberFormatException e) {
                this.valorEfeito = 15; // Default if parsing fails
            }
        } else {
            this.valorEfeito = 0;
            this.atributoAlvo = "geral"; // ou null
        }
    }


    // Getters
    public String getTipo() {
        return tipo;
    }

    public String getEfeitoDescritivo() {
        return efeitoDescritivo;
    }

    public int getValorEfeito() {
        return valorEfeito;
    }

    public String getAtributoAlvo() {
        return atributoAlvo;
    }

    /**
     * Método para o personagem usar o remédio.
     * Aplica o efeito do remédio ao jogador e consome o item.
     *
     * @param jogador O personagem que está usando o remédio.
     */
    @Override
    public void usar(Personagem jogador) {
        if (jogador == null) {
            System.out.println("Personagem não pode ser nulo ao usar remédio.");
            return;
        }

        if (getQuantidade() <= 0) {
            System.out.println("Não há mais " + getNome() + " para usar.");
            return;
        }

        String mensagemUso = jogador.getNome() + " usou o remédio: " + getNome() + " (" + tipo + ").";
        System.out.println(mensagemUso);
        // Idealmente: Main.atualizarOutput(mensagemUso);

        // Aplicar efeito
        if ("vida".equalsIgnoreCase(atributoAlvo)) {
            int vidaAntes = jogador.getVida();
            jogador.setVida(jogador.getVida() + this.valorEfeito);
            System.out.println("Efeito: " + this.efeitoDescritivo + ". Vida restaurada em " + this.valorEfeito + " (de " + vidaAntes + " para " + jogador.getVida() + ").");
        } else if ("sanidade".equalsIgnoreCase(atributoAlvo)) {
            int sanidadeAntes = jogador.getSanidade();
            jogador.setSanidade(jogador.getSanidade() + this.valorEfeito);
            System.out.println("Efeito: " + this.efeitoDescritivo + ". Sanidade restaurada em " + this.valorEfeito + " (de " + sanidadeAntes + " para " + jogador.getSanidade() + ").");
        } else if ("energia".equalsIgnoreCase(atributoAlvo)) {
            int energiaAntes = jogador.getEnergia();
            jogador.setEnergia(jogador.getEnergia() + this.valorEfeito);
            System.out.println("Efeito: " + this.efeitoDescritivo + ". Energia restaurada em " + this.valorEfeito + " (de " + energiaAntes + " para " + jogador.getEnergia() + ").");
        }
        // Adicionar mais casos para outros atributosAlvo (ex: remover "status_envenenado")
        else {
            System.out.println("Efeito aplicado: " + this.efeitoDescritivo);
        }

        // Consome uma unidade do remédio
        this.setQuantidade(this.getQuantidade() - 1);

        if (this.getQuantidade() <= 0) {
            // Se a quantidade chegar a 0, o item deve ser removido do inventário.
            // A classe Inventario é responsável por remover o slot do item quando a quantidade for zero.
            // Aqui apenas sinalizamos que acabou.
            System.out.println(getNome() + " acabou.");
            // Main.atualizarOutput(getNome() + " acabou.");
            if (jogador.getInventario() != null) {
                jogador.getInventario().removerItem(this.getNome(), 1); // Garante a remoção do slot se a quantidade zerar
            }
        }
    }

    @Override
    public String getDescricaoCompleta() {
        return super.getDescricaoCompleta() + // Inclui nome, peso, quantidade, durabilidade (1 uso)
                String.format("\nTipo de Remédio: %s\nEfeito: %s (Valor: %d, Alvo: %s)",
                        tipo, efeitoDescritivo, valorEfeito, atributoAlvo);
    }
}