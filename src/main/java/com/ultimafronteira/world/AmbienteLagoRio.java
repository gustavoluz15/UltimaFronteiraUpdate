package com.ultimafronteira.world;

import com.ultimafronteira.model.*;
import java.util.ArrayList;
import java.util.List;

public class AmbienteLagoRio extends Ambiente {
    private boolean aguaAbundante;
    private boolean possibilidadeDePesca;
    private boolean terrenoLamacento;

    public AmbienteLagoRio(String nome, String descricao) {
        super(nome, descricao, "Moderada", "Variável, geralmente úmido", "fundo_lago_rio");
        this.aguaAbundante = true;
        this.possibilidadeDePesca = true;
        this.terrenoLamacento = (Math.random() < 0.5);
        inicializarRecursos();
    }

    private void inicializarRecursos() {
        adicionarRecurso(new Agua("Água do Rio", 0.0, -1, 15, "Potável", 0.0, "item_agua"));
        if (this.possibilidadeDePesca) {
            adicionarRecurso(new Alimento("Peixe Pequeno", 0.4, 1, 12, 5, "Carne de Peixe", "item_peixe"));
        }
        adicionarRecurso(new Material("Juncos", 0.1, "Fibra Vegetal", 2, "item_juncos"));
    }

    @Override
    public String explorar(Personagem jogador, int numeroDoTurno) {
        StringBuilder sb = new StringBuilder();
        sb.append(jogador.getNome()).append(" explora as margens de ").append(getNome()).append(".\n");

        if (terrenoLamacento) {
            sb.append("O terreno está lamacento, dificultando a movimentação.\n");
            jogador.setEnergia(jogador.getEnergia() - 3);
        }

        double chanceBaseRecurso = 0.7;
        if (jogador.temHabilidade("Rastreamento Aguçado")) {
            chanceBaseRecurso += 0.15;
            sb.append(jogador.getNome()).append(" observa atentamente as margens e a água.\n");
        }

        if (Math.random() < chanceBaseRecurso) {
            // Lógica para encontrar recursos...
            sb.append("Você avista algo brilhando perto da água!\n");
        } else {
            sb.append("A área parece tranquila, sem nada de novo para encontrar agora.\n");
        }

        int energiaGasta = 7;
        jogador.setEnergia(jogador.getEnergia() - energiaGasta);
        sb.append(jogador.getNome()).append(" gastou ").append(energiaGasta).append(" de energia.\n");

        // A chamada ao GerenciadorDeEventos foi removida daqui.

        return sb.toString();
    }

    @Override
    public String modificarClima() {
        // Lógica de clima permanece a mesma
        return "A brisa sobre a água é refrescante.\n";
    }
}