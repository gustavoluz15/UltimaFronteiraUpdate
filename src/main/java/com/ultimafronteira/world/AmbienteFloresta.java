package com.ultimafronteira.world;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.model.Alimento;
import com.ultimafronteira.model.Material;
import com.ultimafronteira.model.Item;

public class AmbienteFloresta extends Ambiente {
    private boolean vegetacaoDensa;
    private boolean faunaAbundante;

    public AmbienteFloresta(String nome, String descricao) {
        super(nome, descricao, "Moderada", "Úmido e Temperado", "fundo_floresta");
        this.vegetacaoDensa = true;
        this.faunaAbundante = true;
        inicializarRecursos();
    }

    private void inicializarRecursos() {
        adicionarRecurso(new Alimento("Frutas Silvestres", 0.3, 1, 5, 2, "Fruta", "item_comida_frutas"));
        adicionarRecurso(new Material("Madeira de Pinheiro", 2.0, "Madeira", 4, "item_madeira"));
        adicionarRecurso(new Alimento("Cogumelos Comestíveis", 0.1, 1, 3, 1, "Cogumelo", "item_cogumelo"));
    }

    @Override
    public String explorar(Personagem jogador, int numeroDoTurno) {
        StringBuilder sb = new StringBuilder();
        sb.append(jogador.getNome()).append(" está explorando ").append(getNome()).append(".\n");

        double chanceBaseRecurso = 0.6;
        if (jogador.temHabilidade("Rastreamento Aguçado")) {
            chanceBaseRecurso += 0.20;
            sb.append(jogador.getNome()).append(" usa sua habilidade de rastreamento!\n");
        }
        chanceBaseRecurso = Math.min(chanceBaseRecurso, 0.95);

        if (Math.random() < chanceBaseRecurso) {
            if (!recursosDisponiveis.isEmpty()) {
                int indiceRecurso = (int) (Math.random() * recursosDisponiveis.size());
                Item recursoEncontrado = recursosDisponiveis.get(indiceRecurso);
                sb.append("Recurso encontrado: ").append(recursoEncontrado.getNome()).append(".\n");
                if(jogador.getInventario().adicionarItem(recursoEncontrado)){
                    sb.append(recursoEncontrado.getNome()).append(" coletado(a).\n");
                } else {
                    sb.append("Inventário cheio, não pôde coletar ").append(recursoEncontrado.getNome()).append(".\n");
                }
            } else {
                sb.append("Nenhum recurso específico encontrado desta vez.\n");
            }
        } else {
            sb.append("A exploração não revelou nada de novo.\n");
        }
        int energiaGasta = 10;
        jogador.setEnergia(jogador.getEnergia() - energiaGasta);
        sb.append(jogador.getNome()).append(" gastou ").append(energiaGasta).append(" de energia.\n");

        // A chamada ao GerenciadorDeEventos foi removida daqui.
        // O ControladorDeTurno agora é responsável por essa fase.

        return sb.toString();
    }

    @Override
    public String modificarClima() {
        // ... (seu método modificarClima permanece o mesmo) ...
        return "O clima na floresta permanece estável.\n";
    }
}