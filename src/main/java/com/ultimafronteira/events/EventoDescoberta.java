// Local do Arquivo: src/main/java/com/ultimafronteira/events/EventoDescoberta.java
package com.ultimafronteira.events;

import com.ultimafronteira.model.Alimento;
import com.ultimafronteira.model.Item;
import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.world.Ambiente;
import java.util.Random;

public class EventoDescoberta extends Evento {

    private Random random = new Random();

    public EventoDescoberta() {
        super("Descoberta de Suprimentos",
                "Você encontra vestígios de outros sobreviventes ou recursos deixados para trás.",
                0.25,
                "Positivo");
    }

    @Override
    public String executar(Personagem jogador, Ambiente local, int numeroDoTurno) {
        StringBuilder sb = new StringBuilder();
        sb.append(getDescricao()).append("\n");

        Item itemEncontrado = new Alimento(
                "Frutas Silvestres Encontradas", // 1. nome
                0.3,                           // 2. peso
                1,                             // 3. durabilidade
                10,                            // 4. valorNutricional
                3,                             // 5. valorCura  <-- O ARGUMENTO QUE ESTAVA IMPLÍCITO ANTES
                "Fruta",                       // 6. tipo
                "item_comida_frutas"           // 7. chaveImagem
        );

        sb.append("Você encontrou um(a) ").append(itemEncontrado.getNome()).append("!\n");

        if (jogador.getInventario().adicionarItem(itemEncontrado)) {
            sb.append(itemEncontrado.getNome()).append(" foi adicionado ao seu inventário.");
        } else {
            sb.append("Seu inventário está cheio, e você teve que deixar o item para trás.");
        }

        return sb.toString();
    }
}