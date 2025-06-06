package com.ultimafronteira.world;

import com.ultimafronteira.model.Personagem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GerenciadorDeAmbientes {
    private Map<String, Ambiente> ambientesDoJogo;
    private Map<String, List<String>> mapaDeConexoes;
    private Ambiente ambienteGlobalPadrao;

    public GerenciadorDeAmbientes() {
        this.ambientesDoJogo = new HashMap<>();
        this.mapaDeConexoes = new HashMap<>();
        inicializarAmbientesPadrao();
        inicializarConexoesPadrao();
    }

    private void inicializarAmbientesPadrao() {
        adicionarAmbiente(new AmbienteFloresta("Floresta dos Sussurros", "Uma floresta antiga...", "fundo_floresta"));
        adicionarAmbiente(new AmbienteMontanha("Pico da Geada Eterna", "Uma montanha imponente...", "fundo_pico_geada"));
        adicionarAmbiente(new AmbienteCaverna("Gruta Ecoante", "Uma caverna escura...", "fundo_caverna"));
        adicionarAmbiente(new AmbienteLagoRio("Rio da Serpente Prateada", "Um rio largo...", "fundo_lago_rio"));
        adicionarAmbiente(new AmbienteRuinas("Cidade Antiga em Ruínas", "Os restos de uma civilização...", "fundo_cidade_ruinas"));

        if (!ambientesDoJogo.isEmpty()) {
            this.ambienteGlobalPadrao = getAmbientePorNome("Floresta dos Sussurros");
        }
    }

    private void inicializarConexoesPadrao() {
        definirConexaoBidirecional("Floresta dos Sussurros", "Pico da Geada Eterna");
        definirConexaoBidirecional("Floresta dos Sussurros", "Rio da Serpente Prateada");
        definirConexaoBidirecional("Floresta dos Sussurros", "Cidade Antiga em Ruínas");
        definirConexaoBidirecional("Pico da Geada Eterna", "Gruta Ecoante");
        definirConexaoBidirecional("Rio da Serpente Prateada", "Cidade Antiga em Ruínas");
    }

    public void adicionarAmbiente(Ambiente ambiente) {
        if (ambiente != null && !ambientesDoJogo.containsKey(ambiente.getNome().toLowerCase())) {
            ambientesDoJogo.put(ambiente.getNome().toLowerCase(), ambiente);
            mapaDeConexoes.putIfAbsent(ambiente.getNome().toLowerCase(), new ArrayList<>());
        }
    }

    public void definirConexao(String nomeOrigem, String nomeDestino) {
        String origemKey = nomeOrigem.toLowerCase();
        String destinoKey = nomeDestino.toLowerCase();
        if (ambientesDoJogo.containsKey(origemKey) && ambientesDoJogo.containsKey(destinoKey)) {
            if (!mapaDeConexoes.get(origemKey).contains(destinoKey)) {
                mapaDeConexoes.get(origemKey).add(destinoKey);
            }
        }
    }

    public void definirConexaoBidirecional(String nome1, String nome2) {
        definirConexao(nome1, nome2);
        definirConexao(nome2, nome1);
    }

    public Ambiente getAmbientePorNome(String nome) {
        if (nome == null) return null;
        return ambientesDoJogo.get(nome.toLowerCase());
    }

    public List<Ambiente> getTodosAmbientes() {
        return new ArrayList<>(ambientesDoJogo.values());
    }

    public List<Ambiente> getAmbientesAdjacentes(String nomeAmbienteAtual) {
        String atualKey = nomeAmbienteAtual.toLowerCase();
        if (mapaDeConexoes.containsKey(atualKey)) {
            return mapaDeConexoes.get(atualKey).stream()
                    .map(this::getAmbientePorNome)
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public void mudarAmbienteAtualDoJogador(Personagem jogador, String nomeNovoAmbiente) {
        Ambiente novoAmbiente = getAmbientePorNome(nomeNovoAmbiente);
        if (novoAmbiente != null) {
            jogador.setLocalizacaoAtual(novoAmbiente);
        }
    }

    public Ambiente getAmbienteGlobalPadrao() {
        return ambienteGlobalPadrao;
    }
}