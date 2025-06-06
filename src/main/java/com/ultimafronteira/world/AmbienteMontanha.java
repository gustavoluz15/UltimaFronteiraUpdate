package com.ultimafronteira.world;

import com.ultimafronteira.model.Agua;
import com.ultimafronteira.model.Material;
import com.ultimafronteira.model.Personagem;

public class AmbienteMontanha extends Ambiente {
    private boolean terrenoAcidentado;
    private boolean climaInstavel;

    public AmbienteMontanha(String nome, String descricao) {
        super(nome, descricao, "Difícil", "Frio e Ventoso", "fundo_pico_geada");
        this.terrenoAcidentado = true;
        this.climaInstavel = true;
        inicializarRecursos();
    }

    private void inicializarRecursos() {
        adicionarRecurso(new Material("Pedra Afiada", 1.0, "Mineral", 6, "item_pedra_afiada"));
        adicionarRecurso(new Material("Minério de Ferro Bruto", 2.5, "Metal", 7, "item_minerio_ferro"));
        adicionarRecurso(new Agua("Água de Degelo", 0.5, 1, 5, "Contaminada", 0.5, "item_agua"));
    }

    @Override
    public String explorar(Personagem jogador, int numeroDoTurno) {
        StringBuilder sb = new StringBuilder();
        sb.append(jogador.getNome()).append(" escala arduamente por ").append(getNome()).append(".\n");

        double chanceBaseRecurso = 0.4;
        if (jogador.temHabilidade("Rastreamento Aguçado")) {
            chanceBaseRecurso += 0.15;
            sb.append(jogador.getNome()).append(" usa sua perícia em rastreamento nas encostas!\n");
        }

        if (Math.random() < chanceBaseRecurso) {
            // Lógica de encontrar recursos...
            sb.append("Você encontra um veio de minério interessante!\n");
        } else {
            sb.append("A exploração foi exaustiva e não revelou nada de novo.\n");
        }
        int energiaGasta = 15;
        if (terrenoAcidentado) energiaGasta += 5;
        jogador.setEnergia(jogador.getEnergia() - energiaGasta);
        sb.append(jogador.getNome()).append(" gastou ").append(energiaGasta).append(" de energia.\n");

        return sb.toString();
    }

    @Override
    public String modificarClima() {
        // Lógica de clima permanece a mesma
        return "Ventos fortes sopram na montanha.\n";
    }
}