<<<<<<< HEAD
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
=======
package com.ultimafronteira.events;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.model.Item;
import com.ultimafronteira.model.Alimento;
import com.ultimafronteira.model.Agua;
import com.ultimafronteira.model.Ferramenta;
import com.ultimafronteira.world.Ambiente;
import java.util.List;
import java.util.ArrayList;

public class EventoDescoberta extends Evento {
    private String tipoDeDescoberta;
    private List<Item> recursosEncontrados;
    private String condicaoEspecial;

    public EventoDescoberta(String nome, String descricao, double probabilidade,
                            String tipoDeDescoberta, String condicaoEspecial) {
        super(nome, descricao, probabilidade, "Descoberta/Recurso");
        this.tipoDeDescoberta = tipoDeDescoberta;
        this.recursosEncontrados = new ArrayList<>();
        this.condicaoEspecial = condicaoEspecial;
        inicializarRecursosDescoberta();
    }

    private void inicializarRecursosDescoberta() {
        if ("Abrigo Abandonado".equals(tipoDeDescoberta)) {
            if (Math.random() < 0.5) {
                this.recursosEncontrados.add(new Alimento("Comida Enlatada", 0.5, 1, 30, "Enlatado"));
            }
            if (Math.random() < 0.3) {
                this.recursosEncontrados.add(new Agua("Garrafa d'água (selada)", 0.6, 1, 25, "Potável", 0.6));
            }
        } else if ("Fonte de Água Limpa".equals(tipoDeDescoberta)) {
            this.recursosEncontrados.add(new Agua("Água Fresca da Fonte", 0.0, -1, 50, "Potável", 0.0));
        } else if ("Ruínas Misteriosas".equals(tipoDeDescoberta) && "Requer Ferramenta".equals(condicaoEspecial)) {
            this.recursosEncontrados.add(new Ferramenta("Artefato Antigo", 1.0, 1, "Desconhecido", 0));
        }
    }

    public String getTipoDeDescoberta() {
        return tipoDeDescoberta;
    }

    public List<Item> getRecursosEncontrados() {
        return new ArrayList<>(recursosEncontrados);
    }

    public String getCondicaoEspecial() {
        return condicaoEspecial;
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
    }

    @Override
    public String executar(Personagem jogador, Ambiente local, int numeroDoTurno) {
        StringBuilder sb = new StringBuilder();
<<<<<<< HEAD
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

=======
        sb.append("EVENTO DE DESCOBERTA: ").append(getNome()).append("!\n");
        sb.append(getDescricao()).append("\n");
        sb.append("Tipo: ").append(tipoDeDescoberta).append("\n");

        if (condicaoEspecial != null && !condicaoEspecial.isEmpty()) {
            sb.append("Condição Especial: ").append(condicaoEspecial).append("\n");
            if ("Requer Ferramenta".equals(condicaoEspecial)) {
                boolean possuiFerramentaAdequada = jogador.getInventario().getItens().stream()
                        .anyMatch(item -> item instanceof Ferramenta && (item.getNome().toLowerCase().contains("pá") || item.getNome().toLowerCase().contains("picareta")));
                if (!possuiFerramentaAdequada) {
                    sb.append(jogador.getNome()).append(" não possui a ferramenta adequada para aproveitar esta descoberta totalmente.\n");
                    return sb.toString();
                } else {
                    sb.append(jogador.getNome()).append(" usa uma ferramenta e consegue acesso.\n");
                }
            }
        }

        if (!recursosEncontrados.isEmpty()) {
            sb.append(jogador.getNome()).append(" encontrou os seguintes itens:\n");
            for (Item item : recursosEncontrados) {
                sb.append("- ").append(item.getNome()).append("\n");
                if(jogador.getInventario().adicionarItem(item)) {
                    sb.append(item.getNome()).append(" adicionado(a) ao inventário.\n");
                } else {
                    sb.append("Inventário cheio, ").append(item.getNome()).append(" não pôde ser coletado.\n");
                }
            }
        } else {
            sb.append("Apesar da descoberta, nenhum item útil foi encontrado desta vez.\n");
        }
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
        return sb.toString();
    }
}