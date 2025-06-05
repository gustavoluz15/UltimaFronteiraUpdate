package com.ultimafronteira.events;

import com.ultimafronteira.model.Personagem;
// import com.ultimafronteira.model.Alimento; // Import não utilizado na versão escolhida, pode ser removido ou mantido para uso futuro.
import com.ultimafronteira.world.Ambiente;
import java.util.Random; // Import adicionado pois a versão escolhida no merge pode precisar no futuro, ou a outra versão precisava.

public class EventoCriatura extends Evento {

    private String tipoDeCriatura;
    private String nivelDePerigo; // Ex: "Baixo", "Médio", "Alto"
    private int baseDanoCriatura;
    private int vidaBaseCriatura;
    private transient int vidaAtualCriatura; // transient para não ser serializado, se aplicável
    private String[] opcoesDeAcao; // Opções que o jogador pode tomar
    private String chaveImagem; // Chave para a imagem da criatura
    private Random random = new Random(); // Para lógicas aleatórias se necessário

    /**
     * Construtor para EventoCriatura.
     *
     * @param nome Nome do evento (ex: "Emboscada de Lobos").
     * @param descricao Descrição geral do que acontece.
     * @param probabilidade Probabilidade base de ocorrência do evento.
     * @param tipoDeCriatura O tipo de criatura encontrada (ex: "Lobo Cinzento").
     * @param nivelDePerigo Nível de perigo da criatura.
     * @param baseDanoCriatura Dano base que a criatura pode infligir.
     * @param vidaBaseCriatura Pontos de vida base da criatura.
     * @param opcoesDeAcao Array de strings com as possíveis ações do jogador.
     * @param chaveImagem Chave para identificar a imagem da criatura.
     */
    public EventoCriatura(String nome, String descricao, double probabilidade,
                          String tipoDeCriatura, String nivelDePerigo,
                          int baseDanoCriatura, int vidaBaseCriatura,
                          String[] opcoesDeAcao, String chaveImagem) {
        super(nome, descricao, probabilidade, "Confronto com Criatura");
        this.tipoDeCriatura = tipoDeCriatura;
        this.nivelDePerigo = nivelDePerigo;
        this.baseDanoCriatura = baseDanoCriatura;
        this.vidaBaseCriatura = vidaBaseCriatura;
        this.opcoesDeAcao = opcoesDeAcao;
        this.chaveImagem = chaveImagem;
        // A vidaAtualCriatura será inicializada no método executar.
    }

    /**
     * Construtor simplificado que sorteia uma criatura aleatória.
     * Este construtor adapta a lógica da versão HEAD do conflito.
     */
    public EventoCriatura() {
        super("Encontro Hostil", "Algo se move nas sombras, observando você.", 0.25, "Confronto com Criatura");
        // A criatura específica será definida no método executar
    }


    // Getters
    public String getTipoDeCriatura() { return tipoDeCriatura; }
    public String getNivelDePerigo() { return nivelDePerigo; }
    public int getBaseDanoCriatura() { return baseDanoCriatura; }
    public int getVidaBaseCriatura() { return vidaBaseCriatura; }
    public int getVidaAtualCriatura() { return vidaAtualCriatura; }
    public int getVidaMaximaCriatura() { return this.vidaBaseCriatura; } // Adicionado da versão HEAD
    public String[] getOpcoesDeAcao() { return opcoesDeAcao; }
    public String getChaveImagem() { return this.chaveImagem; }


    /**
     * Calcula o dano efetivo que a criatura pode causar, possivelmente escalonado pelo turno.
     * @param numeroDoTurno O turno atual do jogo.
     * @return O dano efetivo da criatura.
     */
    public int calcularDanoEfetivoCriatura(int numeroDoTurno) {
        // Lógica da versão f7209b5... (com ajuste para evitar divisão por zero se turno for baixo)
        // return this.baseDanoCriatura + (numeroDoTurno / 4);
        // Lógica da versão HEAD (mais aleatoriedade)
        return this.baseDanoCriatura + random.nextInt(3) + (numeroDoTurno / 5);
    }

    /**
     * Aplica dano à criatura.
     * @param danoRecebido A quantidade de dano recebida.
     * @return Uma mensagem descrevendo o resultado do dano.
     */
    public String receberDano(int danoRecebido) {
        this.vidaAtualCriatura -= danoRecebido;
        this.vidaAtualCriatura = Math.max(0, this.vidaAtualCriatura); // Não deixa a vida ficar negativa
        if (this.vidaAtualCriatura == 0) {
            return tipoDeCriatura + " foi derrotado(a)!\n";
        }
        return tipoDeCriatura + " recebeu " + danoRecebido + " de dano. (PV restantes: " + this.vidaAtualCriatura + ")\n";
    }

    /**
     * Executa a lógica do encontro com a criatura.
     * Se o construtor padrão foi usado, sorteia uma criatura.
     * Inicializa a vida da criatura e retorna a descrição do encontro.
     *
     * @param jogador O personagem do jogador.
     * @param local O ambiente atual.
     * @param numeroDoTurno O número do turno atual.
     * @return Uma string descrevendo o início do confronto.
     */
    @Override
    public String executar(Personagem jogador, Ambiente local, int numeroDoTurno) {
        // Se tipoDeCriatura não foi definido no construtor, sorteia uma (lógica da versão HEAD)
        if (this.tipoDeCriatura == null) {
            int criaturaSorteada = random.nextInt(3);
            switch (criaturaSorteada) {
                case 0:
                    this.nome = "Lobo Faminto Ronda"; // Nome do evento mais descritivo
                    this.descricao = "Um lobo faminto emerge da escuridão, com os olhos fixos em você.";
                    this.tipoDeCriatura = "Lobo Faminto";
                    this.vidaBaseCriatura = 30;
                    this.baseDanoCriatura = 10;
                    this.chaveImagem = "criatura_lobo";
                    this.nivelDePerigo = "Médio";
                    this.opcoesDeAcao = new String[]{"Atacar", "Fugir", "Observar"};
                    break;
                case 1:
                    this.nome = "Bote da Serpente";
                    this.descricao = "Uma cobra venenosa sibila e prepara o bote.";
                    this.tipoDeCriatura = "Cobra Venenosa";
                    this.vidaBaseCriatura = 15;
                    this.baseDanoCriatura = 8; // Dano pode incluir veneno em outra lógica
                    this.chaveImagem = "criatura_cobra";
                    this.nivelDePerigo = "Médio";
                    this.opcoesDeAcao = new String[]{"Atacar", "Recuar", "Procurar Antídoto"};
                    break;
                case 2:
                    this.nome = "Sobrevoo Ameaçador";
                    this.descricao = "Um corvo de aspecto sinistro circula acima, grasnando ameaçadoramente.";
                    this.tipoDeCriatura = "Corvo Ameaçador";
                    this.vidaBaseCriatura = 10;
                    this.baseDanoCriatura = 5;
                    this.chaveImagem = "criatura_corvo";
                    this.nivelDePerigo = "Baixo";
                    this.opcoesDeAcao = new String[]{"Ignorar", "Atirar Pedra", "Assustar"};
                    break;
                default: // Caso improvável
                    this.tipoDeCriatura = "Criatura Misteriosa";
                    this.vidaBaseCriatura = 20;
                    this.baseDanoCriatura = 7;
                    this.chaveImagem = "criatura_desconhecida";
                    this.nivelDePerigo = "Desconhecido";
                    this.opcoesDeAcao = new String[]{"Investigar"};
                    break;
            }
        }

        // Inicializa a vida atual da criatura, com um pequeno bônus baseado no turno
        this.vidaAtualCriatura = this.vidaBaseCriatura + (numeroDoTurno / 2); // Lógica de escalonamento de vida
        this.vidaAtualCriatura = Math.max(1, this.vidaAtualCriatura); // Garante que a criatura tenha pelo menos 1 de vida

        StringBuilder sb = new StringBuilder();
        sb.append("ALERTA DE CRIATURA: ").append(getNome().toUpperCase()).append("!\n");
        sb.append(getDescricao()).append("\n");
        sb.append("Um(a) hostil ").append(tipoDeCriatura).append(" (PV: ").append(this.vidaAtualCriatura).append(") apareceu!\n");
        sb.append("Nível de Perigo Percebido: ").append(nivelDePerigo).append(".\n");

        if (local != null) {
            sb.append("Local do Encontro: ").append(local.getNome()).append("\n");
        }

        if (opcoesDeAcao != null && opcoesDeAcao.length > 0) {
            sb.append("Você pode tentar: ");
            for (int i = 0; i < opcoesDeAcao.length; i++) {
                sb.append("[").append(opcoesDeAcao[i]).append("]");
                sb.append(i < opcoesDeAcao.length - 1 ? ", " : "");
            }
            sb.append(".\n");
        }
        sb.append("Prepare-se para o confronto!\n");

        return sb.toString();
    }
}
