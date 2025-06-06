package com.ultimafronteira.world;

import com.ultimafronteira.model.Alimento;
import com.ultimafronteira.model.Ferramenta;
import com.ultimafronteira.model.Material;
import com.ultimafronteira.model.Personagem;

public class AmbienteRuinas extends Ambiente {
    private boolean estruturasInstaveis;

    public AmbienteRuinas(String nome, String descricao) {
        super(nome, descricao, "Moderada (risco de desmoronamento)", "Variável, parcialmente abrigado", "fundo_cidade_ruinas");
        this.estruturasInstaveis = true;
        inicializarRecursos();
    }

    private void inicializarRecursos() {
        adicionarRecurso(new Material("Metal Retorcido", 1.5, "Metal", 5, "item_metal"));
        if (Math.random() < 0.2) {
            adicionarRecurso(new Ferramenta("Ferramenta Enferrujada", 1.0, 3, "Variada", 2, "item_ferramenta"));
        }
        if (Math.random() < 0.15) {
            adicionarRecurso(new Alimento("Enlatado Antigo", 0.8, 1, 25, 5, "Enlatado", "item_enlatado"));
        }
    }

    @Override
    public String explorar(Personagem jogador, int numeroDoTurno) {
        StringBuilder sb = new StringBuilder();
        sb.append(jogador.getNome()).append(" vasculha os escombros de ").append(getNome()).append(".\n");

        if (estruturasInstaveis && Math.random() < 0.1) {
            sb.append("CUIDADO! Parte da estrutura rangeu perigosamente perto de você!\n");
            jogador.setEnergia(jogador.getEnergia() - 5);
        }

        double chanceBaseRecurso = 0.55;
        if (jogador.temHabilidade("Mãos Leves")) { // Exemplo de habilidade
            chanceBaseRecurso += 0.15;
            sb.append(jogador.getNome()).append(" procura com mais atenção entre os destroços.\n");
        }

        if (Math.random() < chanceBaseRecurso) {
            // Lógica de encontrar recursos...
            sb.append("Você encontra um objeto de metal que parece útil!\n");
        } else {
            sb.append("A exploração não trouxe nada de novo, apenas o silêncio.\n");
        }

        int energiaGasta = 12;
        jogador.setEnergia(jogador.getEnergia() - energiaGasta);
        sb.append(jogador.getNome()).append(" gastou ").append(energiaGasta).append(" de energia.\n");

        return sb.toString();
    }

    @Override
    public String modificarClima() {
        // Lógica de clima permanece a mesma
        return "O vento assobia através das frestas das ruínas.\n";
    }
}