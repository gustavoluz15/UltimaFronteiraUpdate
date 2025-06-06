package com.ultimafronteira.world;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.model.Material;
import com.ultimafronteira.model.Agua;
import com.ultimafronteira.model.Item;
import com.ultimafronteira.events.GerenciadorDeEventos;
import java.util.ArrayList; // Adicionado, caso precise
import java.util.List;    // Adicionado, caso precise

public class AmbienteMontanha extends Ambiente {
    private boolean terrenoAcidentado;
    private boolean climaInstavel;
    private boolean baixaVegetacao;

    public AmbienteMontanha(String nome, String descricao, String chaveImagemFundo) {

        super(nome, descricao, "Difícil", "Frio e Ventoso", chaveImagemFundo);
        this.terrenoAcidentado = true;
        this.climaInstavel = true;
        this.baixaVegetacao = true;
        inicializarRecursos();
    }

    private void inicializarRecursos() {
        adicionarRecurso(new Material("Pedra Afiada", 1.0, "Mineral", 6, "item_pedra_afiada"));
        adicionarRecurso(new Material("Minério de Ferro Bruto", 2.5, "Metal", 7, "item_minerio_ferro"));
        adicionarRecurso(new Agua("Água de Degelo", 0.5, 1, 5, "Contaminada", 0.5, "item_agua"));
    }

    public boolean isTerrenoAcidentado() { return terrenoAcidentado; }
    public boolean isClimaInstavel() { return climaInstavel; }
    public boolean isBaixaVegetacao() { return baixaVegetacao; }

    @Override
    public String explorar(Personagem jogador, GerenciadorDeEventos ge, int numeroDoTurno) {
        StringBuilder sb = new StringBuilder();
        sb.append(jogador.getNome()).append(" escala arduamente por ").append(getNome()).append(".\n");

        double chanceBaseRecurso = 0.4;
        if (jogador.temHabilidade("Rastreamento Aguçado")) {
            chanceBaseRecurso += 0.15;
            sb.append(jogador.getNome()).append(" usa sua perícia em rastreamento nas encostas!\n");
        }
        chanceBaseRecurso = Math.min(chanceBaseRecurso, 0.90);

        if (Math.random() < chanceBaseRecurso) {
            if (!recursosDisponiveis.isEmpty()) {
                int indiceRecurso = (int) (Math.random() * recursosDisponiveis.size());
                Item recursoEncontrado = recursosDisponiveis.get(indiceRecurso);
                sb.append("Recurso encontrado em ").append(getNome()).append(": ").append(recursoEncontrado.getNome()).append(".\n");
                if(jogador.getInventario().adicionarItem(recursoEncontrado)){
                    sb.append(recursoEncontrado.getNome()).append(" coletado(a).\n");
                } else {
                    sb.append("Inventário cheio. Não foi possível coletar ").append(recursoEncontrado.getNome()).append(".\n");
                }
            } else {
                sb.append("Nenhum recurso específico encontrado desta vez em ").append(getNome()).append(".\n");
            }
        } else {
            sb.append("A exploração em ").append(getNome()).append(" foi exaustiva e não revelou nada de novo.\n");
        }
        int energiaGasta = 15;
        if (terrenoAcidentado) energiaGasta += 5;
        jogador.setEnergia(jogador.getEnergia() - energiaGasta);
        sb.append(jogador.getNome()).append(" gastou ").append(energiaGasta).append(" de energia explorando.\n");

        sb.append("--- Verificando eventos durante a exploração da montanha ---\n");
        String resultadoEventoExploracao = ge.sortearEExecutarEvento(jogador, this, numeroDoTurno);
        sb.append(resultadoEventoExploracao);

        return sb.toString();
    }

    @Override
    public String modificarClima() {
        StringBuilder sb = new StringBuilder();
        if (climaInstavel && Math.random() < 0.35) {
            if (Math.random() < 0.5) {
                this.condicoesClimaticasPredominantes = "Nevasca Súbita na Montanha";
                sb.append("ALERTA: Uma nevasca súbita atingiu ").append(getNome()).append("!\n");
            } else {
                this.condicoesClimaticasPredominantes = "Ventos Cortantes na Montanha";
                sb.append("O tempo piorou em ").append(getNome()).append(". Ventos cortantes dificultam a jornada.\n");
            }
        } else {
            this.condicoesClimaticasPredominantes = "Frio Intenso e Céu Limpo";
            sb.append("O clima na montanha ").append(getNome()).append(" está: ").append(this.condicoesClimaticasPredominantes).append(".\n");
        }
        return sb.toString();
    }
}