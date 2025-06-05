package com.ultimafronteira.events;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.world.Ambiente;
<<<<<<< HEAD
import java.util.Random;

public class EventoDoencaFerimento extends Evento {

    private Random random = new Random();

    public EventoDoencaFerimento() {

        super("Condição Adversa",
                "Você não está se sentindo bem. Algo no ambiente ou um descuido afetou sua saúde.",
                0.20,
                "Negativo");
=======

public class EventoDoencaFerimento extends Evento {
    private String tipoDeCondicao;
    private String impactoDescricao;
    private String curaSugerida;

    public EventoDoencaFerimento(String nome, String descricao, double probabilidade,
                                 String tipoDeCondicao, String impactoDescricao, String curaSugerida) {
        super(nome, descricao, probabilidade, "Saúde/Condição");
        this.tipoDeCondicao = tipoDeCondicao;
        this.impactoDescricao = impactoDescricao;
        this.curaSugerida = curaSugerida;
    }

    public String getTipoDeCondicao() {
        return tipoDeCondicao;
    }

    public String getImpactoDescricao() {
        return impactoDescricao;
    }

    public String getCuraSugerida() {
        return curaSugerida;
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
    }

    @Override
    public String executar(Personagem jogador, Ambiente local, int numeroDoTurno) {
        StringBuilder sb = new StringBuilder();
<<<<<<< HEAD
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

=======
        sb.append("EVENTO DE DOENÇA/FERIMENTO: ").append(getNome()).append("!\n");
        sb.append(getDescricao()).append("\n");
        sb.append("Condição Adquirida: ").append(tipoDeCondicao).append("\n");
        sb.append("Impacto: ").append(impactoDescricao).append("\n");

        if (curaSugerida != null && !curaSugerida.isEmpty()) {
            sb.append("Tratamento Sugerido: ").append(curaSugerida).append("\n");
        }

        if ("Hipotermia".equals(tipoDeCondicao)) {
            jogador.setVida(jogador.getVida() - (10 + numeroDoTurno / 7));
            jogador.setEnergia(jogador.getEnergia() - (15 + numeroDoTurno / 5));
            sb.append(jogador.getNome()).append(" está tremendo de frio e perde vida e energia.\n");
        } else if ("Infecção Alimentar".equals(tipoDeCondicao)) {
            jogador.setVida(jogador.getVida() - (5 + numeroDoTurno / 8));
            jogador.setFome(Math.max(0, jogador.getFome() - (20 + numeroDoTurno / 4)));
            jogador.setEnergia(jogador.getEnergia() - (10 + numeroDoTurno / 6));
            sb.append(jogador.getNome()).append(" sente-se fraco e enjoado devido à infecção.\n");
        } else if ("Desidratação Severa".equals(tipoDeCondicao)) {
            jogador.setVida(jogador.getVida() - (10 + numeroDoTurno / 5));
            jogador.setSede(Math.max(0, jogador.getSede() - (30 + numeroDoTurno / 3)));
            jogador.setSanidade(jogador.getSanidade() - (5 + numeroDoTurno / 10));
            sb.append(jogador.getNome()).append(" está gravemente desidratado, afetando sua vida e sanidade.\n");
        } else if ("Fratura Exposta".equals(tipoDeCondicao)) {
            jogador.setVida(jogador.getVida() - (25 + numeroDoTurno / 3));
            jogador.setEnergia(jogador.getEnergia() - (20 + numeroDoTurno / 4));
            sb.append(jogador.getNome()).append(" sofreu uma fratura grave! Precisa de cuidados médicos urgentes.\n");
        }

        if (jogador.getVida() <= 0) {
            sb.append(jogador.getNome()).append(" sucumbiu à sua condição...\n");
        }
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
        return sb.toString();
    }
}