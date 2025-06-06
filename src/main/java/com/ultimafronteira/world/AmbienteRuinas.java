package com.ultimafronteira.world;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.model.Material;
import com.ultimafronteira.model.Ferramenta;
import com.ultimafronteira.model.Alimento;
import com.ultimafronteira.model.Item;
import com.ultimafronteira.events.GerenciadorDeEventos;
import java.util.ArrayList;
import java.util.List;

public class AmbienteRuinas extends Ambiente {
    private boolean estruturasInstaveis;
    private boolean presencaOutrosSobreviventes;
    private boolean baixoRiscoClimatico;

    public AmbienteRuinas(String nome, String descricao, String chaveImagemFundo) {
        super(nome, descricao, "Moderada (risco de desmoronamento)", "Variável, parcialmente abrigado", chaveImagemFundo);
        this.estruturasInstaveis = true;
        this.presencaOutrosSobreviventes = (Math.random() < 0.3);
        this.baixoRiscoClimatico = true;
        inicializarRecursos();
    }

    private void inicializarRecursos() {
        adicionarRecurso(new Material("Metal Retorcido", 1.5, "Metal", 5, "item_metal"));
        adicionarRecurso(new Material("Tijolos Quebrados", 2.0, "Construção", 3, "item_tijolo"));
        if (Math.random() < 0.2) {
            adicionarRecurso(new Ferramenta("Ferramenta Enferrujada", 1.0, 3, "Variada", 2, "item_ferramenta"));
        }
        if (Math.random() < 0.15) {
            // CORREÇÃO: Construtor de Alimento agora com 6 argumentos (sem valorCura)
            adicionarRecurso(new Alimento("Enlatado Antigo", 0.8, 1, 25, "Enlatado", "item_enlatado"));
        }
    }

    public boolean isEstruturasInstaveis() { return estruturasInstaveis; }
    public boolean isPresencaOutrosSobreviventes() { return presencaOutrosSobreviventes; }
    public boolean isBaixoRiscoClimatico() { return baixoRiscoClimatico; }

    @Override
    public String explorar(Personagem jogador, GerenciadorDeEventos ge, int numeroDoTurno) {
        StringBuilder sb = new StringBuilder();
        sb.append(jogador.getNome()).append(" vasculha cuidadosamente os escombros de ").append(getNome()).append(".\n");

        if (estruturasInstaveis && Math.random() < 0.1) {
            sb.append("CUIDADO! Parte da estrutura rangeu perigosamente perto de você!\n");
            jogador.setEnergia(jogador.getEnergia() - 5);
        }

        double chanceBaseRecurso = 0.55;
        if (jogador.temHabilidade("Rastreamento Aguçado") || jogador.temHabilidade("Mãos Leves")) {
            chanceBaseRecurso += 0.15;
            sb.append(jogador.getNome()).append(" procura com mais atenção entre os destroços.\n");
        }
        chanceBaseRecurso = Math.min(chanceBaseRecurso, 0.90);

        if (Math.random() < chanceBaseRecurso) {
            if (!recursosDisponiveis.isEmpty()) {
                int indiceRecurso = (int) (Math.random() * recursosDisponiveis.size());
                Item recursoEncontrado = recursosDisponiveis.get(indiceRecurso);
                sb.append("Em meio aos destroços, você encontrou algo útil em ").append(getNome()).append(": ").append(recursoEncontrado.getNome()).append(".\n");
                if(jogador.getInventario().adicionarItem(recursoEncontrado)){
                    sb.append(recursoEncontrado.getNome()).append(" coletado(a).\n");
                } else {
                    sb.append("Inventário cheio. Não foi possível coletar ").append(recursoEncontrado.getNome()).append(".\n");
                }
            } else {
                sb.append(getNome()).append(" parece já ter sido completamente saqueada.\n");
            }
        } else {
            sb.append("A exploração das ruínas não trouxe nada de novo, apenas o silêncio do que foi perdido.\n");
        }

        int energiaGasta = 12;
        jogador.setEnergia(jogador.getEnergia() - energiaGasta);
        sb.append(jogador.getNome()).append(" gastou ").append(energiaGasta).append(" de energia explorando.\n");

        sb.append("--- Verificando eventos nas ruínas desoladas ---\n");
        String resultadoEventoExploracao = ge.sortearEExecutarEvento(jogador, this, numeroDoTurno);
        sb.append(resultadoEventoExploracao);

        return sb.toString();
    }

    @Override
    public String modificarClima() {
        StringBuilder sb = new StringBuilder();
        if (baixoRiscoClimatico) {
            if (Math.random() < 0.05) {
                sb.append("O vento assobia através das frestas das ruínas em ").append(getNome()).append(".\n");
            } else {
                sb.append("As ruínas de ").append(getNome()).append(" oferecem abrigo, o clima interno é estável.\n");
            }
        } else {
            if (Math.random() < 0.15) {
                this.condicoesClimaticasPredominantes = "Vento Forte com Poeira";
                sb.append("O tempo mudou. Um vento forte levanta poeira ao redor de ").append(getNome()).append(".\n");
            } else {
                sb.append("O clima em ").append(getNome()).append(" permanece: ").append(this.condicoesClimaticasPredominantes).append(".\n");
            }
        }
        return sb.toString();
    }
}