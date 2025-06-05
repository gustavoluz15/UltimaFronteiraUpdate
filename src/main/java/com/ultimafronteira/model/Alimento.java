package com.ultimafronteira.model;

import java.util.Random; // Import para funcionalidades aleatórias, se necessário

public class Alimento extends Item {
    private int valorNutricional;
    private int valorCura; // Quanto de vida o alimento restaura
    private String tipo;   // Ex: "Fruta", "Carne", "Enlatado", "Raiz"
    // private boolean estragado; // Poderia ser uma propriedade adicional

    /**
     * Construtor para a classe Alimento.
     *
     * @param nome Nome do alimento (ex: "Maçã Vermelha").
     * @param pesoUnitario Peso de uma unidade do item.
     * @param durabilidade Durabilidade ou número de usos (ex: 1 para um item de uso único).
     * @param valorNutricional Quanto de fome o alimento restaura.
     * @param valorCura Quanto de vida o alimento restaura.
     * @param tipo O tipo de alimento.
     * @param chaveImagem A chave para a imagem do item (se houver).
     */
    public Alimento(String nome, double pesoUnitario, int durabilidade, int valorNutricional, int valorCura, String tipo, String chaveImagem) {
        // Chama o construtor de Item: (String nome, double pesoUnitario, int durabilidade, boolean empilhavel, String chaveImagem)
        // Alimentos são geralmente empilháveis.
        super(nome, pesoUnitario, durabilidade, true, chaveImagem);
        this.valorNutricional = valorNutricional;
        this.valorCura = valorCura;
        this.tipo = tipo;
    }

    /**
     * Construtor alternativo que não inclui chaveImagem, para consistência ou casos onde não é necessária.
     * Assume que o valor de cura padrão é 0 se não especificado.
     * Define o alimento como empilhável por padrão.
     */
    public Alimento(String nome, double pesoUnitario, int durabilidade, int valorNutricional, String tipo) {
        // Chama o construtor de Item: (String nome, double pesoUnitario, int durabilidade, boolean empilhavel, String chaveImagem)
        // Alimentos são geralmente empilháveis, chaveImagem é null.
        super(nome, pesoUnitario, durabilidade, true, null);
        this.valorNutricional = valorNutricional;
        this.valorCura = 0; // Valor de cura padrão
        this.tipo = tipo;
    }

    // Getters
    public int getValorNutricional() {
        return valorNutricional;
    }

    public int getValorCura() {
        return valorCura;
    }

    public String getTipo() {
        return tipo;
    }

    /**
     * Método para o personagem usar (comer) o alimento.
     * Afeta a fome e a vida do jogador.
     * Decrementa a quantidade do item (se empilhável e durabilidade representa "usos por unidade" ou é 1).
     *
     * @param jogador O personagem que está consumindo o alimento.
     */
    @Override
    public void usar(Personagem jogador) {
        if (jogador == null) {
            System.out.println("Personagem não pode ser nulo ao usar alimento.");
            return;
        }

        if (getQuantidade() <= 0) { // Verifica se ainda há itens no stack
            System.out.println("Não há mais " + getNome() + " para consumir.");
            return;
        }

        String mensagemUso = jogador.getNome() + " consumiu " + getNome() + ".";
        System.out.println(mensagemUso);
        // Idealmente: Main.atualizarOutput(mensagemUso);

        jogador.setFome(jogador.getFome() + this.valorNutricional);
        System.out.println("Sua fome foi saciada em " + this.valorNutricional + " pontos.");
        // Main.atualizarOutput("Sua fome foi saciada em " + this.valorNutricional + " pontos.");


        if (this.valorCura > 0) {
            jogador.setVida(jogador.getVida() + this.valorCura);
            System.out.println("Suas energias foram restauradas em " + this.valorCura + " pontos de vida.");
            // Main.atualizarOutput("Suas energias foram restauradas em " + this.valorCura + " pontos de vida.");
        }

        // Lógica de deterioração ou chance de doença se o alimento estiver "estragado" poderia ser adicionada aqui.
        // Exemplo:
        // if (this.tipo.equals("Carne Crua") && new Random().nextDouble() < 0.25) {
        //    System.out.println("A carne crua não pareceu cair bem...");
        //    // Aplicar efeito de doença
        // }

        // Consome uma unidade do alimento (se empilhável)
        // A durabilidade em Alimento pode representar "frescor" ou "usos" se for um grande pedaço de comida.
        // Se durabilidade é 1 e é empilhável, significa que cada unidade é um uso.
        if (isEmpilhavel()) {
            setQuantidade(getQuantidade() - 1);
            if (getQuantidade() <= 0) {
                System.out.println(getNome() + " (todo o stack) acabou.");
                // A remoção completa do slot do inventário quando a quantidade chega a 0
                // é melhor tratada pela classe Inventario, para manter o pesoAtualizado.
            }
        } else { // Se não for empilhável, a durabilidade pode representar usos de um único item
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
                String.format("\nNutrição: %d\nCura: %d\nTipo: %s",
                        valorNutricional, valorCura, tipo);
    }
}
