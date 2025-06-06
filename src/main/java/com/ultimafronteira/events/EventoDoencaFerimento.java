package com.ultimafronteira.events;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.world.Ambiente;

public class EventoDoencaFerimento extends Evento {

    private final int danoVida;
    private final int perdaEnergia;
    private final int perdaFome;
    private final int perdaSede;
    private final int perdaSanidade;

    /**
     * Construtor para uma condição de saúde adversa específica.
     * @param nome O nome do evento, ex: "Corte Profundo".
     * @param descricao A descrição para o jogador.
     * @param probabilidade A chance base de ocorrência.
     * @param danoVida Quanto de vida o jogador perde.
     * @param perdaEnergia Quanto de energia o jogador perde.
     * @param perdaFome Quanto de fome o jogador perde.
     * @param perdaSede Quanto de sede o jogador perde.
     * @param perdaSanidade Quanto de sanidade o jogador perde.
     */
    public EventoDoencaFerimento(String nome, String descricao, double probabilidade, int danoVida, int perdaEnergia, int perdaFome, int perdaSede, int perdaSanidade) {
        super(nome, descricao, probabilidade, "Negativo");
        this.danoVida = danoVida;
        this.perdaEnergia = perdaEnergia;
        this.perdaFome = perdaFome;
        this.perdaSede = perdaSede;
        this.perdaSanidade = perdaSanidade;
    }

    @Override
    public String executar(Personagem jogador, Ambiente local, int numeroDoTurno) {
        StringBuilder sb = new StringBuilder(getDescricao() + "\n");

        if (danoVida > 0) {
            jogador.setVida(jogador.getVida() - danoVida);
            sb.append("Você perdeu ").append(danoVida).append(" de vida. ");
        }
        if (perdaEnergia > 0) {
            jogador.setEnergia(jogador.getEnergia() - perdaEnergia);
            sb.append("Você perdeu ").append(perdaEnergia).append(" de energia. ");
        }
        if (perdaFome > 0) {
            jogador.setFome(jogador.getFome() - perdaFome);
            sb.append("Você perdeu ").append(perdaFome).append(" de fome. ");
        }
        if (perdaSede > 0) {
            jogador.setSede(jogador.getSede() - perdaSede);
            sb.append("Você perdeu ").append(perdaSede).append(" de sede. ");
        }
        if (perdaSanidade > 0) {
            jogador.setSanidade(jogador.getSanidade() - perdaSanidade);
            sb.append("Você perdeu ").append(perdaSanidade).append(" de sanidade. ");
        }

        return sb.toString().trim();
    }
}