package com.ultimafronteira.events;

import com.ultimafronteira.model.Agua;
import com.ultimafronteira.model.Arma;
import com.ultimafronteira.model.Escudo;
import com.ultimafronteira.model.Item;
import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.model.Remedio;
import com.ultimafronteira.world.Ambiente;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class EventoDescoberta extends Evento {

    private Random random = new Random();

    public EventoDescoberta(String suprimentosDeixados, String s, double v) {
        super("Descoberta Inesperada", // Nome mais genérico
                "Você se depara com algo potencialmente útil ou interessante.",
                0.35, // Aumentei um pouco a chance base de uma descoberta
                "Positivo/Neutro");
    }

    @Override
    public String executar(Personagem jogador, Ambiente local, int numeroDoTurno) {
        StringBuilder sb = new StringBuilder();
        sb.append(getNome()).append("\n"); // Usa o nome genérico do evento

        List<Item> possiveisItens = new ArrayList<>();

        possiveisItens.add(new Remedio("Frasco de Energia", 0.2, 1, "Energia", 10 + (numeroDoTurno/3), "item_frasco_energia"));
        possiveisItens.add(new Agua("Cantil com Água Limpa", 0.5, 1, 15 + (numeroDoTurno/2), "Potável", 0.5, "item_frasco_agua")); // Aumentei a quantidade de sede
        possiveisItens.add(new Remedio("Tônico Calmante", 0.2, 1, "Sanidade", 10 + (numeroDoTurno/3), "item_frasco_sanidade"));
        possiveisItens.add(new Remedio("Unguento Curativo", 0.2, 1, "Vida", 10 + (numeroDoTurno/3), "item_frasco_vida"));
        possiveisItens.add(new Arma("Espada de Forja Antiga (Uso)", 1.0, 1, "Buff", 0, 0, "item_espada_buff")); // Dano/Alcance 0, efeito via usar()
        possiveisItens.add(new Escudo("Broquel Reforçado (Uso)", 1.2, 1, 0, "Buff", "item_escudo_buff")); // Defesa 0, efeito via usar()


        if (random.nextDouble() < 0.15) {
            sb.append("Você vasculha a área, mas encontra apenas detritos inúteis desta vez.\n");
            return sb.toString();
        }

        Item itemEncontrado = possiveisItens.get(random.nextInt(possiveisItens.size()));


        String[] frasesDescoberta = {
                "Você tropeça em um(a) ",
                "Escondido sob algumas folhas, você avista um(a) ",
                "Um brilho chama sua atenção para um(a) ",
                "Parece que alguém esqueceu um(a) ",
                "Que sorte! Você encontrou um(a) "
        };
        sb.append(frasesDescoberta[random.nextInt(frasesDescoberta.length)]);
        sb.append(itemEncontrado.getNome()).append("!\n");

        if (jogador.getInventario().adicionarItem(itemEncontrado)) {
            sb.append(itemEncontrado.getNome()).append(" foi adicionado ao seu inventário.");
        } else {
            sb.append("Seu inventário está cheio, e você teve que deixar o(a) ").append(itemEncontrado.getNome()).append(" para trás.");
        }

        return sb.toString();
    }
}