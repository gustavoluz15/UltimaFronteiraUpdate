package com.ultimafronteira.world;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.model.Alimento;
import com.ultimafronteira.model.Material;
import com.ultimafronteira.model.Item;
import com.ultimafronteira.events.GerenciadorDeEventos;
import java.util.ArrayList;
import java.util.List;

public class AmbienteFloresta extends Ambiente {
    private boolean vegetacaoDensa;
    private boolean faunaAbundante;
    private boolean climaUmido;

    public AmbienteFloresta(String nome, String descricao, String chaveImagemFundo) {
        super(nome, descricao, "Moderada", "Úmido e Temperado", chaveImagemFundo);
        this.vegetacaoDensa = true;
        this.faunaAbundante = true;
        this.climaUmido = true;
        inicializarRecursos();
    }

    private void inicializarRecursos() {
        // CORREÇÃO: Construtor de Alimento agora com 6 argumentos (sem valorCura)
        adicionarRecurso(new Alimento("Frutas Silvestres", 0.3, 1, 15, "Fruta", "item_comida_frutas"));
        adicionarRecurso(new Alimento("Cogumelos Comestiveis", 0.1, 1, 8, "Cogumelo", "item_cogumelo"));
        adicionarRecurso(new Material("Gravetos Secos", 1.0, "Madeira", 2, "item_madeira"));
    }

    public boolean isVegetacaoDensa() { return vegetacaoDensa; }
    public boolean isFaunaAbundante() { return faunaAbundante; }
    public boolean isClimaUmido() { return climaUmido; }

    @Override
    public String explorar(Personagem jogador, GerenciadorDeEventos ge, int numeroDoTurno) {
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

        sb.append("--- Verificando eventos durante a exploração ---\n");
        String resultadoEventoExploracao = ge.sortearEExecutarEvento(jogador, this, numeroDoTurno);
        sb.append(resultadoEventoExploracao);

        return sb.toString();
    }

    @Override
    public String modificarClima() {
        StringBuilder sb = new StringBuilder();
        if (Math.random() < 0.2) {
            this.condicoesClimaticasPredominantes = "Chuva Leve na Floresta";
            sb.append("O tempo mudou em ").append(getNome()).append(". Agora está: ").append(this.condicoesClimaticasPredominantes).append("\n");
        } else {
            this.condicoesClimaticasPredominantes = "Tempo Estável na Floresta";
            sb.append("O tempo em ").append(getNome()).append(" permanece: ").append(this.condicoesClimaticasPredominantes).append(".\n");
        }
        return sb.toString();
    }
}