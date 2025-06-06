package com.ultimafronteira.world;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.model.Material;
import com.ultimafronteira.model.Agua;
import com.ultimafronteira.model.Item;

public class AmbienteCaverna extends Ambiente {
    private boolean poucaLuz;

    public AmbienteCaverna(String nome, String descricao) {
        super(nome, descricao, "Variável (requer iluminação)", "Escuro e Úmido", "fundo_caverna");
        this.poucaLuz = true;
        inicializarRecursos();
    }

    private void inicializarRecursos() {
        adicionarRecurso(new Material("Pedra Calcária", 1.2, "Mineral", 5, "item_pedra"));
        adicionarRecurso(new Material("Cristais Opacos", 0.5, "Mineral Raro", 8, "item_cristal"));
        adicionarRecurso(new Agua("Água de Gotejamento", 0.1, 1, 3, "Potável", 0.1, "item_agua"));
    }

    @Override
    public String explorar(Personagem jogador, int numeroDoTurno) {
        StringBuilder sb = new StringBuilder();
        sb.append(jogador.getNome()).append(" adentra cuidadosamente ").append(getNome()).append(".\n");

        boolean temLuzSuficiente = jogador.getInventario().getItens().stream()
                .anyMatch(item -> item.getNome().toLowerCase().contains("lanterna") || item.getNome().toLowerCase().contains("tocha"));

        if (poucaLuz && !temLuzSuficiente) {
            sb.append("Está muito escuro para explorar efetivamente. Você precisa de uma fonte de luz.\n");
            jogador.setEnergia(jogador.getEnergia() - 2);
            return sb.toString();
        }

        double chanceBaseRecurso = 0.5;
        if (jogador.temHabilidade("Rastreamento Aguçado") && temLuzSuficiente) {
            chanceBaseRecurso += 0.20;
            sb.append(jogador.getNome()).append(" usa seus sentidos aguçados na penumbra!\n");
        }

        if (Math.random() < chanceBaseRecurso) {
            if (!recursosDisponiveis.isEmpty()) {
                int indiceRecurso = (int) (Math.random() * recursosDisponiveis.size());
                Item recursoEncontrado = recursosDisponiveis.get(indiceRecurso);
                sb.append("Algo brilha na escuridão! Encontrado: ").append(recursoEncontrado.getNome()).append(".\n");
                if(jogador.getInventario().adicionarItem(recursoEncontrado)){
                    sb.append(recursoEncontrado.getNome()).append(" coletado(a).\n");
                } else {
                    sb.append("Inventário cheio.\n");
                }
            } else {
                sb.append("A caverna parece vazia por aqui.\n");
            }
        } else {
            sb.append("Os ecos da caverna são a única resposta à sua exploração.\n");
        }
        int energiaGasta = 8;
        jogador.setEnergia(jogador.getEnergia() - energiaGasta);
        sb.append("Você gastou ").append(energiaGasta).append(" de energia.\n");

        return sb.toString();
    }

    @Override
    public String modificarClima() {
        // ... (seu método modificarClima permanece o mesmo) ...
        return "O ambiente interno da caverna permanece estável.\n";
    }
}