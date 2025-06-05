package com.ultimafronteira.model;

public class Arma extends Item {
    private String tipoArma; // Ex: "Corpo a corpo", "À Distância", "Arremesso"
    private int dano;
    private int alcance; // Em unidades de jogo, 0 ou 1 para corpo a corpo, >1 para distância

    /**
     * Construtor para a classe Arma.
     *
     * @param nome Nome da arma (ex: "Adaga Enferrujada").
     * @param pesoUnitario Peso de uma unidade do item.
     * @param durabilidade Durabilidade da arma (quantos ataques pode fazer antes de quebrar, -1 para inquebrável).
     * @param tipoArma O tipo da arma (ex: "Corpo a corpo").
     * @param dano O dano base que a arma causa.
     * @param alcance O alcance da arma.
     * @param chaveImagem A chave para a imagem do item (se houver).
     */
    public Arma(String nome, double pesoUnitario, int durabilidade, String tipoArma, int dano, int alcance, String chaveImagem) {
        // Chama o construtor de Item: (String nome, double pesoUnitario, int durabilidade, boolean empilhavel, String chaveImagem)
        // Armas geralmente não são empilháveis.
        super(nome, pesoUnitario, durabilidade, false, chaveImagem);
        this.tipoArma = tipoArma;
        this.dano = dano;
        this.alcance = alcance;
    }

    /**
     * Construtor alternativo para a classe Arma sem a chave de imagem.
     */
    public Arma(String nome, double pesoUnitario, int durabilidade, String tipoArma, int dano, int alcance) {
        // Chama o construtor de Item: (String nome, double pesoUnitario, int durabilidade, boolean empilhavel, String chaveImagem)
        // Armas geralmente não são empilháveis, e chaveImagem é null.
        super(nome, pesoUnitario, durabilidade, false, null);
        this.tipoArma = tipoArma;
        this.dano = dano;
        this.alcance = alcance;
    }

    // Getters
    public String getTipoArma() {
        return tipoArma;
    }

    public int getDano() {
        return dano;
    }

    public int getAlcance() {
        return alcance;
    }

    /**
     * Método para usar a arma. Para armas, "usar" geralmente significa equipá-la.
     * A lógica de ataque real pode estar em outro método ou ser gerenciada pelo Personagem.
     *
     * @param jogador O personagem que está usando (equipando) a arma.
     */
    @Override
    public void usar(Personagem jogador) {
        if (jogador == null) {
            System.out.println("Personagem não pode ser nulo ao tentar usar/equipar uma arma.");
            return;
        }
        // A lógica de equipar a arma é movida para o Personagem para centralizar o gerenciamento do equipamento.
        String resultadoEquipar = jogador.equiparArma(this); // O método equiparArma no Personagem lidará com a lógica.
        System.out.println(resultadoEquipar);
        // Idealmente, esta mensagem seria enviada para a UI: Main.atualizarOutput(resultadoEquipar);
    }

    /**
     * Simula um ataque com a arma, verificando e decrementando a durabilidade.
     * Esta é uma função de exemplo; a lógica de combate real geralmente é mais complexa
     * e envolve um alvo.
     * Este método não é chamado diretamente pela lógica de 'usar' item, mas pode ser
     * usado pelo sistema de combate.
     *
     * @return Uma string descrevendo o resultado do ataque simulado.
     */
    public String simularAtaque() {
        if (getDurabilidade() > 0 || getDurabilidadeOriginal() == -1) { // -1 na durabilidadeOriginal significa inquebrável
            String mensagemAtaque = "Atacando com " + getNome() + ", causando " + dano + " de dano.";
            if (getDurabilidadeOriginal() != -1) { // Apenas decrementa se não for inquebrável
                setDurabilidade(getDurabilidade() - 1);
                if (getDurabilidade() == 0) {
                    mensagemAtaque += "\n" + getNome() + " quebrou!";
                }
            }
            return mensagemAtaque;
        } else {
            return getNome() + " está quebrada e não pode ser usada para atacar.";
        }
    }

    @Override
    public String getDescricaoCompleta() {
        return super.getDescricaoCompleta() + // Inclui nome, peso, quantidade (sempre 1 para arma não empilhável), durabilidade
                String.format("\nTipo: %s\nDano: %d\nAlcance: %d",
                        tipoArma, dano, alcance);
    }
}