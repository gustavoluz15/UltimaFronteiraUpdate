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
        // Assumindo que AmbienteLagoRio existe e foi criado em algum momento.
        // Se não, esta linha precisará ser ajustada ou a classe AmbienteLagoRio criada.
        // Por agora, vou comentá-la se a classe não foi fornecida ainda.
        // AmbienteLagoRio rioSerpente = new AmbienteLagoRio("Rio da Serpente Prateada", "Um rio largo de águas calmas que corta a paisagem.");
        AmbienteRuinas ruinasEsquecidas = new AmbienteRuinas("Cidade Antiga em Ruínas", "Os restos desmoronados de uma civilização perdida.");

        adicionarAmbiente(florestaSussurros);
        adicionarAmbiente(montanhaGeada);
        adicionarAmbiente(cavernaEcoante);
        // adicionarAmbiente(rioSerpente); // Comentado se AmbienteLagoRio não existe ainda
        adicionarAmbiente(ruinasEsquecidas);

        if (!ambientesDoJogo.isEmpty()) {
            this.ambienteGlobalPadrao = florestaSussurros; // Define a floresta como padrão se existir
        } else {
            // Fallback se nenhum ambiente for adicionado - pode ser um ambiente simples ou null
            // this.ambienteGlobalPadrao = new Ambiente("Lugar Vazio", "Um espaço indefinido.", "Nenhuma", "Neutro", "fundo_default") {
            //     @Override
            //     public String explorar(Personagem jogador, GerenciadorDeEventos ge, int numeroDoTurno) {
            //         return "Não há nada para explorar aqui.";
            //     }
            //     @Override
            //     public String modificarClima() {
            //         return "O clima é indiferente.";
            //     }
            // };
            // Ou, mais simplesmente, deixar como null e tratar no código que o usa.
            this.ambienteGlobalPadrao = null;
        }
    }

    private void inicializarConexoesPadrao() {
        // Conexões da Floresta dos Sussurros
        definirConexaoBidirecional("Floresta dos Sussurros", "Pico da Geada Eterna");
        // definirConexaoBidirecional("Floresta dos Sussurros", "Rio da Serpente Prateada"); // Depende de Rio da Serpente existir
        definirConexaoBidirecional("Floresta dos Sussurros", "Cidade Antiga em Ruínas");

        // Conexões do Pico da Geada Eterna
        // Já conectado à Floresta (implícito pela bidirecionalidade)
        definirConexaoBidirecional("Pico da Geada Eterna", "Gruta Ecoante"); // Caverna na montanha

        // Conexões do Rio da Serpente Prateada (se existir)
        // Já conectado à Floresta (implícito pela bidirecionalidade)
        // definirConexaoBidirecional("Rio da Serpente Prateada", "Cidade Antiga em Ruínas");

        // Gruta Ecoante e Cidade Antiga em Ruínas podem ter menos conexões ou serem mais isoladas
        // A Gruta Ecoante já está conectada ao Pico da Geada Eterna.
        // A Cidade Antiga em Ruínas já está conectada à Floresta dos Sussurros (e potencialmente ao Rio).
    }

    public void adicionarAmbiente(Ambiente ambiente) {
        if (ambiente != null && ambiente.getNome() != null && !ambientesDoJogo.containsKey(ambiente.getNome().toLowerCase())) {
            ambientesDoJogo.put(ambiente.getNome().toLowerCase(), ambiente);
            mapaDeConexoes.putIfAbsent(ambiente.getNome().toLowerCase(), new ArrayList<>());
        } else if (ambiente == null || ambiente.getNome() == null) {
            System.err.println("Tentativa de adicionar ambiente nulo ou com nome nulo.");
        }
    }

    public void definirConexao(String nomeAmbienteOrigem, String nomeAmbienteDestino) {
        if (nomeAmbienteOrigem == null || nomeAmbienteDestino == null) return;
        String origemKey = nomeAmbienteOrigem.toLowerCase();
        String destinoKey = nomeAmbienteDestino.toLowerCase();

        if (ambientesDoJogo.containsKey(origemKey) && ambientesDoJogo.containsKey(destinoKey)) {
            // Evita adicionar conexões duplicadas
            if (!mapaDeConexoes.get(origemKey).contains(destinoKey)) {
                mapaDeConexoes.get(origemKey).add(destinoKey);
            }
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
        if (nomeAmbienteAtual == null) return new ArrayList<>();
        String atualKey = nomeAmbienteAtual.toLowerCase();
        if (mapaDeConexoes.containsKey(atualKey)) {
            return mapaDeConexoes.get(atualKey).stream()
                    .map(this::getAmbientePorNome) // Usa method reference
                    .filter(java.util.Objects::nonNull) // Filtra nulos se um nome de ambiente conectado não existir mais
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(); // Retorna lista vazia se não houver conexões ou o ambiente não existir
    }

    public boolean mudarAmbienteAtualDoJogador(Personagem jogador, String nomeNovoAmbiente) {
        if (jogador == null || nomeNovoAmbiente == null) return false;

        Ambiente ambienteAtualJogador = jogador.getLocalizacaoAtual();
        Ambiente novoAmbiente = getAmbientePorNome(nomeNovoAmbiente);

        if (novoAmbiente == null) {
            System.out.println("Tentativa de mover para ambiente desconhecido: " + nomeNovoAmbiente);
            return false;
        }

        if (ambienteAtualJogador == null) {
            // Se o jogador não tem localização (início do jogo, talvez), permite mover para qualquer ambiente válido.
            // Isso é útil para definir a localização inicial do jogador.
            jogador.setLocalizacaoAtual(novoAmbiente);
            return true;
        }

        // Verifica se o novo ambiente é adjacente ao atual
        List<Ambiente> adjacentes = getAmbientesAdjacentes(ambienteAtualJogador.getNome());
        if (adjacentes.contains(novoAmbiente)) {
            jogador.setLocalizacaoAtual(novoAmbiente);
            return true;
        } else {
            System.out.println(novoAmbiente.getNome() + " não é adjacente a " + ambienteAtualJogador.getNome() + ".");
            return false;
        }
    }

    public Ambiente getAmbienteGlobalPadrao() {
        if (ambienteGlobalPadrao == null && !ambientesDoJogo.isEmpty()) {
            // Tenta definir um padrão se ainda não foi definido e há ambientes
            ambienteGlobalPadrao = ambientesDoJogo.values().iterator().next();
        }
        return ambienteGlobalPadrao;
    }
}
