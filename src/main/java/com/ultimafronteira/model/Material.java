package com.ultimafronteira.model;

public class Material extends Item {
    private String tipo; // Ex: "Madeira", "Metal", "Tecido", "Mineral"
    private int resistencia; // Pode ser usado para determinar a qualidade em fabricação ou durabilidade de estruturas

    /**
     * Construtor para a classe Material.
     * Materiais geralmente são empilháveis e têm durabilidade -1 (inquebrável como item,
     * mas sua quantidade diminui ao ser usado em fabricação).
     *
     * @param nome Nome do material (ex: "Tronco de Pinheiro").
     * @param pesoUnitario Peso de uma unidade do material.
     * @param tipo O tipo de material.
     * @param resistencia Uma medida da qualidade ou robustez do material.
     * @param chaveImagem A chave para a imagem do item (se houver).
     */
    public Material(String nome, double pesoUnitario, String tipo, int resistencia, String chaveImagem) {
        // Materiais são geralmente empilháveis e não têm durabilidade no sentido de "quebrar com o uso"
        // A "durabilidade" aqui é -1 (inquebrável), mas a quantidade diminui.
        super(nome, pesoUnitario, -1, true, chaveImagem); // empilhavel = true, durabilidade = -1 (inquebrável)
        this.tipo = tipo;
        this.resistencia = resistencia;
    }

    /**
     * Construtor alternativo para a classe Material sem a chave de imagem.
     */
    public Material(String nome, double pesoUnitario, String tipo, int resistencia) {
        super(nome, pesoUnitario, -1, true, null); // empilhavel = true, durabilidade = -1, chaveImagem = null
        this.tipo = tipo;
        this.resistencia = resistencia;
    }

    // Getters
    public String getTipo() {
        return tipo;
    }

    public int getResistencia() {
        return resistencia;
    }

    /**
     * Materiais geralmente não são "usados" diretamente da mesma forma que alimentos ou ferramentas.
     * Eles são consumidos em processos de fabricação ou construção.
     * Este método pode ser sobrescrito se um material específico tiver um uso direto.
     *
     * @param jogador O personagem que está tentando usar o material.
     */
    @Override
    public void usar(Personagem jogador) {
        String mensagemUso = "O material " + getNome() + " (" + getQuantidade() + "x) não pode ser usado diretamente desta forma. Utilize-o para fabricar ou construir.";
        System.out.println(mensagemUso);
        // Idealmente, esta mensagem seria enviada para a UI: Main.atualizarOutput(mensagemUso);

        // A lógica de consumir o material ocorreria em um sistema de fabricação.
        // Exemplo: se usado em crafting, a quantidade seria decrementada lá.
        // Se este método 'usar' significasse "descartar 1 unidade", a lógica seria:
        // if (getQuantidade() > 0) {
        //     setQuantidade(getQuantidade() - 1);
        //     System.out.println("Você descartou uma unidade de " + getNome() + ".");
        //     if (getQuantidade() == 0) {
        //         System.out.println(getNome() + " acabou.");
        //         if (jogador != null && jogador.getInventario() != null) {
        //             jogador.getInventario().removerItem(this.getNome(), 1); // Remove o slot se a quantidade zerar
        //         }
        //     }
        // }
    }

    /**
     * Um exemplo de método que poderia existir para interagir com materiais.
     * A lógica real de combinação seria mais complexa.
     *
     * @param outroMaterial O outro material para tentar combinar.
     */
    public void combinar(Material outroMaterial) {
        // Lógica de combinação seria implementada aqui ou em um sistema de crafting.
        String mensagemCombinar = "Tentando combinar " + getNome() + " com " + outroMaterial.getNome() + "... (Lógica de combinação não implementada)";
        System.out.println(mensagemCombinar);
        // Main.atualizarOutput(mensagemCombinar);
    }

    @Override
    public String getDescricaoCompleta() {
        return super.getDescricaoCompleta() + // Inclui nome, peso, quantidade, durabilidade (inquebrável)
                String.format("\nTipo de Material: %s\nResistência: %d",
                        tipo, resistencia);
    }
}
