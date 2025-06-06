package com.ultimafronteira.world;

import com.ultimafronteira.model.Personagem;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class GerenciadorDeAmbientes {
    private Map<String, Ambiente> ambientesDoJogo;
    private Map<String, List<String>> mapaDeConexoes;
    private Ambiente ambienteGlobalPadrao; // <<< CAMPO ADICIONADO

    public GerenciadorDeAmbientes() {
        this.ambientesDoJogo = new HashMap<>();
        this.mapaDeConexoes = new HashMap<>();
        inicializarAmbientesPadrao();
        inicializarConexoesPadrao();
    }

    private void inicializarAmbientesPadrao() {
        adicionarAmbiente(new AmbienteFloresta("Floresta dos Sussurros", "Uma floresta antiga com árvores altas e vegetação densa."));
        adicionarAmbiente(new AmbienteMontanha("Pico da Geada Eterna", "Uma montanha imponente com cume coberto de neve."));
        adicionarAmbiente(new AmbienteCaverna("Gruta Ecoante", "Uma caverna escura e úmida, onde cada som se propaga."));
        adicionarAmbiente(new AmbienteLagoRio("Rio da Serpente Prateada", "Um rio largo de águas calmas que corta a paisagem."));
        adicionarAmbiente(new AmbienteRuinas("Cidade Antiga em Ruínas", "Os restos desmoronados de uma civilização perdida."));

        // Define um ambiente padrão como fallback
        this.ambienteGlobalPadrao = getAmbientePorNome("Floresta dos Sussurros");
    }

    private void inicializarConexoesPadrao() {
        definirConexaoBidirecional("Floresta dos Sussurros", "Pico da Geada Eterna");
        definirConexaoBidirecional("Floresta dos Sussurros", "Rio da Serpente Prateada");
        definirConexaoBidirecional("Floresta dos Sussurros", "Cidade Antiga em Ruínas");
        definirConexaoBidirecional("Pico da Geada Eterna", "Gruta Ecoante");
        definirConexaoBidirecional("Rio da Serpente Prateada", "Cidade Antiga em Ruínas");
    }

    public void adicionarAmbiente(Ambiente ambiente) {
        if (ambiente != null) {
            String nomeKey = ambiente.getNome().toLowerCase();
            if (!ambientesDoJogo.containsKey(nomeKey)) {
                ambientesDoJogo.put(nomeKey, ambiente);
                mapaDeConexoes.putIfAbsent(nomeKey, new ArrayList<>());
            }
        }
    }

    public void definirConexaoBidirecional(String nomeAmbiente1, String nomeAmbiente2) {
        String key1 = nomeAmbiente1.toLowerCase();
        String key2 = nomeAmbiente2.toLowerCase();
        if (ambientesDoJogo.containsKey(key1) && ambientesDoJogo.containsKey(key2)) {
            mapaDeConexoes.computeIfAbsent(key1, k -> new ArrayList<>()).add(key2);
            mapaDeConexoes.computeIfAbsent(key2, k -> new ArrayList<>()).add(key1);
        }
    }

    public Ambiente getAmbientePorNome(String nome) {
        return nome == null ? null : ambientesDoJogo.get(nome.toLowerCase());
    }

    public List<Ambiente> getAmbientesAdjacentes(String nomeAmbienteAtual) {
        if (nomeAmbienteAtual == null) return new ArrayList<>();
        List<String> nomesAdjacentes = mapaDeConexoes.get(nomeAmbienteAtual.toLowerCase());
        if (nomesAdjacentes == null) return new ArrayList<>();

        return nomesAdjacentes.stream()
                .map(this::getAmbientePorNome)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
    }

    public boolean mudarAmbienteAtualDoJogador(Personagem jogador, String nomeNovoAmbiente) {
        Ambiente ambienteAtualJogador = jogador.getLocalizacaoAtual();
        Ambiente novoAmbiente = getAmbientePorNome(nomeNovoAmbiente);

        if (novoAmbiente == null) {
            System.out.println("Tentativa de mover para ambiente desconhecido: " + nomeNovoAmbiente);
            return false;
        }

        if (ambienteAtualJogador == null) {
            jogador.setLocalizacaoAtual(novoAmbiente);
            return true;
        }

        List<Ambiente> adjacentes = getAmbientesAdjacentes(ambienteAtualJogador.getNome());
        if (adjacentes.contains(novoAmbiente)) {
            jogador.setLocalizacaoAtual(novoAmbiente);
            return true;
        } else {
            System.out.println(novoAmbiente.getNome() + " não é adjacente a " + ambienteAtualJogador.getNome() + ".");
            return false;
        }
    }

    // >>>>> MÉTODO ADICIONADO PARA CORRIGIR O ERRO <<<<<
    public List<Ambiente> getTodosAmbientes() {
        return new ArrayList<>(ambientesDoJogo.values());
    }

    // >>>>> MÉTODO ADICIONADO PARA CORRIGIR O ERRO <<<<<
    public Ambiente getAmbienteGlobalPadrao() {
        return ambienteGlobalPadrao;
    }
}