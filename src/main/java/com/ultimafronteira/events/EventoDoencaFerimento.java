package com.ultimafronteira.events;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.world.Ambiente;
import java.util.Random;

public class EventoDoencaFerimento extends Evento {

    private Random random = new Random();

    public EventoDoencaFerimento(String corteProfundoAcidental, String s, double v, String corteProfundo, String string, String bandagens) {

        super("Condição Adversa",
                "Você não está se sentindo bem. Algo no ambiente ou um descuido afetou sua saúde.",
                0.20,
                "Negativo");
    }

    @Override
    public String executar(Personagem jogador, Ambiente local, int numeroDoTurno) {
        StringBuilder sb = new StringBuilder();
        sb.append("De repente, você se sente mal!\n");

        int tipoCondicao = random.nextInt(3);

        switch (tipoCondicao) {
            case 0:
                sb.append("Um calafrio percorre sua espinha e sua testa queima. Você está com febre.\n");
                int perdaEnergia = 15;
                int perdaSanidade = 5;
                jogador.setEnergia(jogador.getEnergia() - perdaEnergia);
                jogador.setSanidade(jogador.getSanidade() - perdaSanidade);
                sb.append("Você perdeu ").append(perdaEnergia).append(" de energia e ").append(perdaSanidade).append(" de sanidade.");
                break;

            case 1:
                sb.append("Você percebe um corte antigo em seu braço, agora vermelho e inchado. Parece estar infeccionado.\n");
                int danoVida = 10;
                jogador.setVida(jogador.getVida() - danoVida);
                sb.append("A infecção te causa ").append(danoVida).append(" de dano imediato! Você precisa de um remédio.");
                break;

            case 2:
                sb.append("Seu estômago se revira violentamente. A última coisa que você comeu não caiu bem.\n");
                int perdaFome = 20;
                int perdaSede = 10;
                jogador.setFome(jogador.getFome() - perdaFome);
                jogador.setSede(jogador.getSede() - perdaSede);
                sb.append("Você perdeu ").append(perdaFome).append(" de fome e ").append(perdaSede).append(" de sede.");
                break;
        }

        return sb.toString();
    }
}