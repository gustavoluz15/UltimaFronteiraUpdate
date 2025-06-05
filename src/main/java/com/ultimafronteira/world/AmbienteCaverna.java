package com.ultimafronteira.world;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.model.Material;
import com.ultimafronteira.model.Agua;
import com.ultimafronteira.model.Item;
import com.ultimafronteira.events.GerenciadorDeEventos;

public class AmbienteCaverna extends Ambiente {
    private boolean poucaLuz;
    private boolean presencaCriaturasDesconhecidas;
    private boolean aguaDeGotejamento;

    public AmbienteCaverna(String nome, String descricao) {
<<<<<<< HEAD
        // MUDANÇA AQUI: Adicionado "fundo_caverna"
        super(nome, descricao, "Variável (requer iluminação)", "Escuro e Úmido", "fundo_caverna");
=======
        super(nome, descricao, "Variável (requer iluminação)", "Escuro e Úmido");
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
        this.poucaLuz = true;
        this.presencaCriaturasDesconhecidas = true;
        this.aguaDeGotejamento = true;
        inicializarRecursos();
    }

    private void inicializarRecursos() {
<<<<<<< HEAD
        adicionarRecurso(new Material("Pedra Calcária", 1.2, "Mineral", 5, "item_pedra"));
        adicionarRecurso(new Material("Cristais Opacos", 0.5, "Mineral Raro", 8, "item_cristal"));
        if (aguaDeGotejamento) {
            adicionarRecurso(new Agua("Água de Gotejamento", 0.1, 1, 3, "Potável", 0.1, "item_agua"));
=======
        adicionarRecurso(new Material("Pedra Calcária", 1.2, "Mineral", 5));
        adicionarRecurso(new Material("Cristais Opacos", 0.5, "Mineral Raro", 8));
        if (aguaDeGotejamento) {
            adicionarRecurso(new Agua("Água de Gotejamento", 0.1, 1, 3, "Potável", 0.1));
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
        }
    }

    public boolean isPoucaLuz() { return poucaLuz; }
    public boolean isPresencaCriaturasDesconhecidas() { return presencaCriaturasDesconhecidas; }
    public boolean isAguaDeGotejamento() { return aguaDeGotejamento; }

    @Override
    public String explorar(Personagem jogador, GerenciadorDeEventos ge, int numeroDoTurno) {
        StringBuilder sb = new StringBuilder();
        sb.append(jogador.getNome()).append(" adentra cuidadosamente ").append(getNome()).append(".\n");

        boolean temLuzSuficiente = jogador.getInventario().getItens().stream()
                .anyMatch(item -> item.getNome().toLowerCase().contains("lanterna") || item.getNome().toLowerCase().contains("tocha"));

        if (poucaLuz && !temLuzSuficiente) {
            sb.append("Está muito escuro para explorar efetivamente. Você precisa de uma fonte de luz.\n");
            jogador.setEnergia(jogador.getEnergia() - 2);
            sb.append(jogador.getNome()).append(" gastou 2 de energia tentando.\n");
            return sb.toString();
        }

        double chanceBaseRecurso = 0.5;
<<<<<<< HEAD
        if (jogador.temHabilidade("Rastreamento Aguçado") && temLuzSuficiente) {
=======
        if (jogador.temHabilidade("Rastreamento Aguçado") && temLuzSuficiente) { // Rastrear melhor com luz
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
            chanceBaseRecurso += 0.20;
            sb.append(jogador.getNome()).append(" usa seus sentidos aguçados na penumbra!\n");
        }
        chanceBaseRecurso = Math.min(chanceBaseRecurso, 0.90);

        if (Math.random() < chanceBaseRecurso) {
            if (!recursosDisponiveis.isEmpty()) {
                int indiceRecurso = (int) (Math.random() * recursosDisponiveis.size());
                Item recursoEncontrado = recursosDisponiveis.get(indiceRecurso);
                sb.append("Algo brilha na escuridão! Recurso encontrado em ").append(getNome()).append(": ").append(recursoEncontrado.getNome()).append(".\n");
                if(jogador.getInventario().adicionarItem(recursoEncontrado)){
                    sb.append(recursoEncontrado.getNome()).append(" coletado(a).\n");
                } else {
                    sb.append("Inventário cheio. Não foi possível coletar ").append(recursoEncontrado.getNome()).append(".\n");
                }
            } else {
                sb.append("A caverna parece vazia por aqui.\n");
            }
        } else {
            sb.append("Os ecos da caverna são a única resposta à sua exploração.\n");
        }
        int energiaGasta = 8;
        jogador.setEnergia(jogador.getEnergia() - energiaGasta);
        sb.append(jogador.getNome()).append(" gastou ").append(energiaGasta).append(" de energia explorando a caverna.\n");

        sb.append("--- Verificando eventos na escuridão da caverna ---\n");
        String resultadoEventoExploracao = ge.sortearEExecutarEvento(jogador, this, numeroDoTurno);
        sb.append(resultadoEventoExploracao);

        return sb.toString();
    }

    @Override
    public String modificarClima() {
        StringBuilder sb = new StringBuilder();
        sb.append("O ambiente interno de ").append(getNome()).append(" permanece estável. ");
        if (aguaDeGotejamento && Math.random() < 0.1) {
            sb.append("O gotejamento parece ter aumentado.\n");
        } else if (Math.random() < 0.05) {
            sb.append("Uma corrente de ar frio percorre o local.\n");
        } else {
            sb.append("Nenhuma mudança perceptível.\n");
        }
        return sb.toString();
    }
}