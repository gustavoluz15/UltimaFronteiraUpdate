package com.ultimafronteira.world;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.model.Alimento;
import com.ultimafronteira.model.Agua;
import com.ultimafronteira.model.Material;
import com.ultimafronteira.model.Item;
import com.ultimafronteira.events.GerenciadorDeEventos;
import java.util.List;
import java.util.ArrayList;

public class AmbienteLagoRio extends Ambiente {
    private boolean aguaAbundante;
    private boolean possibilidadeDePesca;
    private boolean terrenoLamacento;

    public AmbienteLagoRio(String nome, String descricao, String chaveImagemFundo) {
        super(nome, descricao, "Moderada (risco de afogamento)", "Variável, geralmente úmido", chaveImagemFundo);
        this.aguaAbundante = true;
        this.possibilidadeDePesca = true;
        this.terrenoLamacento = (Math.random() < 0.5);
        inicializarRecursos();
    }

    private void inicializarRecursos() {
        adicionarRecurso(new Agua("Água do Rio", 0.0, -1, 15, "Potável", 0.0, "item_agua"));
        if (this.possibilidadeDePesca) {
            // CORREÇÃO: Construtor de Alimento agora com 6 argumentos (sem valorCura)
            adicionarRecurso(new Alimento("Peixe Pequeno", 0.4, 1, 12, "Carne de Peixe", "item_peixe"));
        }
        adicionarRecurso(new Material("Juncos", 0.1, "Fibra Vegetal", 2, "item_juncos"));
    }

    public boolean isAguaAbundante() { return aguaAbundante; }
    public boolean isPossibilidadeDePesca() { return this.possibilidadeDePesca; }
    public boolean isTerrenoLamacento() { return terrenoLamacento; }

    @Override
    public String explorar(Personagem jogador, GerenciadorDeEventos ge, int numeroDoTurno) {
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
        chanceBaseRecurso = Math.min(chanceBaseRecurso, 0.95);

        if (Math.random() < chanceBaseRecurso) {
            if (this.possibilidadeDePesca && Math.random() < (0.4 + (jogador.temHabilidade("Rastreamento Aguçado") ? 0.1 : 0.0) )) {
                sb.append("Você avista peixes na água! Parece um bom lugar para pescar.\n");
            } else if (!recursosDisponiveis.isEmpty()) {
                Item recursoEncontrado = null;
                List<Item> itensColetaveis = new ArrayList<>();
                for(Item i : recursosDisponiveis) {
                    if (!(i instanceof Agua && i.getNome().equals("Água do Rio"))) {
                        itensColetaveis.add(i);
                    }
                }
                if(!itensColetaveis.isEmpty()){
                    recursoEncontrado = itensColetaveis.get((int)(Math.random() * itensColetaveis.size()));
                }

                if (recursoEncontrado != null) {
                    sb.append("Recurso encontrado próximo a ").append(getNome()).append(": ").append(recursoEncontrado.getNome()).append(".\n");
                    if(jogador.getInventario().adicionarItem(recursoEncontrado)){
                        sb.append(recursoEncontrado.getNome()).append(" coletado(a).\n");
                    } else {
                        sb.append("Inventário cheio. Não foi possível coletar ").append(recursoEncontrado.getNome()).append(".\n");
                    }
                } else {
                    sb.append("Apenas água corrente por perto.\n");
                }
            } else {
                sb.append("Apesar da água abundante, nada mais chamou a atenção.\n");
            }
        } else {
            sb.append("A área parece tranquila, sem nada de novo para encontrar agora.\n");
        }

        int energiaGasta = 7;
        jogador.setEnergia(jogador.getEnergia() - energiaGasta);
        sb.append(jogador.getNome()).append(" gastou ").append(energiaGasta).append(" de energia explorando.\n");

        sb.append("--- Verificando eventos próximos à água ---\n");
        String resultadoEventoExploracao = ge.sortearEExecutarEvento(jogador, this, numeroDoTurno);
        sb.append(resultadoEventoExploracao);

        return sb.toString();
    }

    @Override
    public String modificarClima() {
        StringBuilder sb = new StringBuilder();
        if (Math.random() < 0.25) {
            if (Math.random() < 0.6) {
                this.condicoesClimaticasPredominantes = "Névoa sobre a Água";
                sb.append("Uma névoa densa cobre ").append(getNome()).append(".\n");
            } else {
                this.condicoesClimaticasPredominantes = "Chuvisco Leve";
                sb.append("Começou um chuvisco leve sobre ").append(getNome()).append(".\n");
            }
        } else {
            this.condicoesClimaticasPredominantes = "Brisa Suave e Água Calma";
            sb.append("O clima em ").append(getNome()).append(" está: ").append(this.condicoesClimaticasPredominantes).append(".\n");
        }
        return sb.toString();
    }
}