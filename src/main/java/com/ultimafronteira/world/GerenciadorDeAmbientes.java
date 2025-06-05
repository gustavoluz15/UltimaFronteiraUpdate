package com.ultimafronteira.world;

import com.ultimafronteira.model.Personagem;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class GerenciadorDeAmbientes {
    private Map<String, Ambiente> ambientesDoJogo;
    private Map<String, List<String>> mapaDeConexoes; // Nome do ambiente -> Lista de nomes de ambientes conectados
    private Ambiente ambienteGlobalPadrao;

    public GerenciadorDeAmbientes() {
        this.ambientesDoJogo = new HashMap<>();
        this.mapaDeConexoes = new HashMap<>();
        inicializarAmbientesPadrao();
        inicializarConexoesPadrao();
    }

    private void inicializarAmbientesPadrao() {
        AmbienteFloresta florestaSussurros = new AmbienteFloresta("Floresta dos Sussurros", "Uma floresta antiga com árvores altas e vegetação densa.");
        AmbienteMontanha montanhaGeada = new AmbienteMontanha("Pico da Geada Eterna", "Uma montanha imponente com cume coberto de neve.");
        AmbienteCaverna cavernaEcoante = new AmbienteCaverna("Gruta Ecoante", "Uma caverna escura e úmida, onde cada som se propaga.");
        AmbienteLagoRio rioSerpente = new AmbienteLagoRio("Rio da Serpente Prateada", "Um rio largo de águas calmas que corta a paisagem.");
        AmbienteRuinas ruinasEsquecidas = new AmbienteRuinas("Cidade Antiga em Ruínas", "Os restos desmoronados de uma civilização perdida.");

        adicionarAmbiente(florestaSussurros);
        adicionarAmbiente(montanhaGeada);
        adicionarAmbiente(cavernaEcoante);
        adicionarAmbiente(rioSerpente);
        adicionarAmbiente(ruinasEsquecidas);

        if (!ambientesDoJogo.isEmpty()) {
            this.ambienteGlobalPadrao = florestaSussurros;
        }
    }

    private void inicializarConexoesPadrao() {
<<<<<<< HEAD
=======
        // Conexões da Floresta dos Sussurros
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
        definirConexaoBidirecional("Floresta dos Sussurros", "Pico da Geada Eterna");
        definirConexaoBidirecional("Floresta dos Sussurros", "Rio da Serpente Prateada");
        definirConexaoBidirecional("Floresta dos Sussurros", "Cidade Antiga em Ruínas");

<<<<<<< HEAD

        definirConexaoBidirecional("Pico da Geada Eterna", "Gruta Ecoante"); // Caverna na montanha


        definirConexaoBidirecional("Rio da Serpente Prateada", "Cidade Antiga em Ruínas");

=======
        // Conexões do Pico da Geada Eterna
        // Já conectado à Floresta
        definirConexaoBidirecional("Pico da Geada Eterna", "Gruta Ecoante"); // Caverna na montanha

        // Conexões do Rio da Serpente Prateada
        // Já conectado à Floresta
        definirConexaoBidirecional("Rio da Serpente Prateada", "Cidade Antiga em Ruínas");

        // Gruta Ecoante e Cidade Antiga em Ruínas podem ter menos conexões ou serem mais isoladas
        // A Gruta já está conectada à Montanha.
        // A Cidade Antiga já está conectada à Floresta e ao Rio.
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
    }

    public void adicionarAmbiente(Ambiente ambiente) {
        if (ambiente != null && !ambientesDoJogo.containsKey(ambiente.getNome().toLowerCase())) {
            ambientesDoJogo.put(ambiente.getNome().toLowerCase(), ambiente);
            mapaDeConexoes.putIfAbsent(ambiente.getNome().toLowerCase(), new ArrayList<>());
        }
    }

    public void definirConexao(String nomeAmbienteOrigem, String nomeAmbienteDestino) {
        String origemKey = nomeAmbienteOrigem.toLowerCase();
        String destinoKey = nomeAmbienteDestino.toLowerCase();

        if (ambientesDoJogo.containsKey(origemKey) && ambientesDoJogo.containsKey(destinoKey)) {
            mapaDeConexoes.get(origemKey).add(destinoKey);
        }
    }

    public void definirConexaoBidirecional(String nomeAmbiente1, String nomeAmbiente2) {
        definirConexao(nomeAmbiente1, nomeAmbiente2);
        definirConexao(nomeAmbiente2, nomeAmbiente1);
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

    public boolean mudarAmbienteAtualDoJogador(Personagem jogador, String nomeNovoAmbiente) {
        Ambiente ambienteAtualJogador = jogador.getLocalizacaoAtual();
        if (ambienteAtualJogador == null) {
<<<<<<< HEAD
=======
            // Se o jogador não tem localização, permite mover para qualquer ambiente válido
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
            Ambiente novoAmbiente = getAmbientePorNome(nomeNovoAmbiente);
            if (novoAmbiente != null) {
                jogador.setLocalizacaoAtual(novoAmbiente);
                return true;
            }
            return false;
        }
        List<Ambiente> adjacentes = getAmbientesAdjacentes(ambienteAtualJogador.getNome());
        Ambiente novoAmbiente = getAmbientePorNome(nomeNovoAmbiente);

        if (novoAmbiente != null && adjacentes.contains(novoAmbiente)) {
            jogador.setLocalizacaoAtual(novoAmbiente);
            return true;
        } else if (novoAmbiente != null) {
            System.out.println(novoAmbiente.getNome() + " não é adjacente a " + ambienteAtualJogador.getNome() + ".");
            return false;
        } else {
            System.out.println("Tentativa de mover para ambiente desconhecido: " + nomeNovoAmbiente);
            return false;
        }
    }

    public Ambiente getAmbienteGlobalPadrao() {
        return ambienteGlobalPadrao;
    }
}