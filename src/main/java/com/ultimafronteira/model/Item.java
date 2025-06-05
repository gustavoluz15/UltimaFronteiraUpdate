package com.ultimafronteira.model;

public abstract class Item {
    protected String nome;
    protected double pesoUnitario; // Peso de uma única unidade do item
    protected int durabilidade; // Durabilidade atual. -1 para inquebrável.
    protected final int durabilidadeOriginal; // Durabilidade inicial para referência.
    protected String chaveImagem; // Chave para a imagem do item (pode ser null)
    protected int quantidade; // Quantidade deste item (para itens empilháveis)
    protected boolean empilhavel; // Se o item pode ser empilhado

    /**
     * Construtor principal para a classe Item.
     *
     * @param nome Nome do item.
     * @param pesoUnitario Peso de uma única unidade do item.
     * @param durabilidade Durabilidade inicial do item (-1 se inquebrável).
     * @param empilhavel True se o item puder ser empilhado, false caso contrário.
     * @param chaveImagem Chave de referência para a imagem do item (pode ser null).
     */
    public Item(String nome, double pesoUnitario, int durabilidade, boolean empilhavel, String chaveImagem) {
        this.nome = nome;
        this.pesoUnitario = pesoUnitario;
        this.durabilidade = durabilidade;
        this.durabilidadeOriginal = durabilidade; // Guarda a durabilidade inicial
        this.empilhavel = empilhavel;
        this.quantidade = 1; // Itens são criados com quantidade 1 por padrão
        this.chaveImagem = chaveImagem;
    }

    /**
     * Construtor sobrecarregado sem chaveImagem.
     */
    public Item(String nome, double pesoUnitario, int durabilidade, boolean empilhavel) {
        this(nome, pesoUnitario, durabilidade, empilhavel, null);
    }

    // Getters
    public String getNome() {
        return nome;
    }

    /**
     * Retorna o peso de uma única unidade deste item.
     * @return O peso unitário.
     */
    public double getPesoUnitario() {
        return pesoUnitario;
    }

    /**
     * Retorna o peso total deste slot de item (peso unitário * quantidade).
     * @return O peso total.
     */
    public double getPeso() {
        return this.pesoUnitario * this.quantidade;
    }

    public int getDurabilidade() {
        return durabilidade;
    }

    public int getDurabilidadeOriginal() {
        return durabilidadeOriginal;
    }

    public String getChaveImagem() {
        return this.chaveImagem;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public boolean isEmpilhavel() {
        return empilhavel;
    }

    // Setters
    public void setDurabilidade(int durabilidade) {
        this.durabilidade = Math.max(0, durabilidade); // Não permite durabilidade negativa
    }

    public void setQuantidade(int quantidade) {
        if (this.empilhavel) {
            this.quantidade = Math.max(1, quantidade); // Quantidade mínima é 1
        } else {
            this.quantidade = 1; // Itens não empilháveis sempre têm quantidade 1
        }
    }

    /**
     * Método abstrato para definir a ação de usar o item.
     * A implementação específica varia conforme o tipo de item.
     * @param jogador O personagem que está usando o item.
     */
    public abstract void usar(Personagem jogador);

    /**
     * Retorna uma descrição básica do item.
     * Subclasses podem sobrescrever para adicionar mais detalhes.
     * @return Uma string representando o item.
     */
    @Override
    public String toString() {
        String info = nome + " (Peso Un.: " + String.format("%.2f", pesoUnitario);
        if (empilhavel) {
            info += ", Qtde: " + quantidade;
        }
        if (durabilidadeOriginal != -1) { // Mostra durabilidade apenas se for relevante
            info += ", Durab.: " + durabilidade + "/" + durabilidadeOriginal;
        }
        info += ")";
        return info;
    }

    /**
     * Retorna uma descrição mais completa do item, útil para tooltips ou painéis de detalhes.
     * Subclasses devem sobrescrever este método para adicionar informações específicas.
     * @return Uma string com a descrição completa do item.
     */
    public String getDescricaoCompleta() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nome: ").append(nome);
        sb.append("\nPeso Unitário: ").append(String.format("%.2f", pesoUnitario));
        if (empilhavel) {
            sb.append("\nQuantidade: ").append(quantidade);
            sb.append("\nPeso Total: ").append(String.format("%.2f", getPeso()));
        }
        if (durabilidadeOriginal != -1) {
            sb.append("\nDurabilidade: ").append(durabilidade).append("/").append(durabilidadeOriginal);
        } else {
            sb.append("\nDurabilidade: Inquebrável");
        }
        // Subclasses adicionarão mais informações aqui (ex: dano para armas, nutrição para alimentos)
        return sb.toString();
    }
}
