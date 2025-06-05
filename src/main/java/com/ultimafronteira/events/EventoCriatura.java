package com.ultimafronteira.events;

import com.ultimafronteira.model.Personagem;
<<<<<<< HEAD
import com.ultimafronteira.world.Ambiente;
import java.util.Random;

public class EventoCriatura extends Evento {

    private String tipoDeCriatura;
    private int vidaBaseCriatura;
    private int vidaAtualCriatura;
    private int danoBaseCriatura;
    private String chaveImagem;
    private Random random = new Random();

    public EventoCriatura() {
        super("Encontro Hostil", "Algo se move nas sombras, observando você.", 0.25, "Confronto com Criatura");
    }

    public String getTipoDeCriatura() { return tipoDeCriatura; }
    public int getVidaAtualCriatura() { return vidaAtualCriatura; }
    public int getVidaMaximaCriatura() { return this.vidaBaseCriatura; }
    public String getChaveImagem() { return this.chaveImagem; }

    @Override
    public String executar(Personagem jogador, Ambiente local, int numeroDoTurno) {
        int criaturaSorteada = random.nextInt(3);

        switch (criaturaSorteada) {
            case 0:
                this.tipoDeCriatura = "Lobo Faminto";
                this.vidaBaseCriatura = 30;
                this.danoBaseCriatura = 10;
                this.chaveImagem = "criatura_lobo";
                break;
            case 1:
                this.tipoDeCriatura = "Cobra Venenosa";
                this.vidaBaseCriatura = 15;
                this.danoBaseCriatura = 8;
                this.chaveImagem = "criatura_cobra";
                break;
            case 2:
                this.tipoDeCriatura = "Corvo Ameaçador";
                this.vidaBaseCriatura = 10;
                this.danoBaseCriatura = 5;
                this.chaveImagem = "criatura_corvo";
                break;
        }

        this.vidaAtualCriatura = this.vidaBaseCriatura + (numeroDoTurno / 2);

        return "Um " + this.tipoDeCriatura + " surge, pronto para atacar!";
=======
import com.ultimafronteira.model.Alimento;
import com.ultimafronteira.world.Ambiente;

public class EventoCriatura extends Evento {
    private String tipoDeCriatura;
    private String nivelDePerigo;
    private int baseDanoCriatura;
    private int vidaBaseCriatura;
    private transient int vidaAtualCriatura;
    private String[] opcoesDeAcao;

    public EventoCriatura(String nome, String descricao, double probabilidade,
                          String tipoDeCriatura, String nivelDePerigo,
                          int baseDanoCriatura, int vidaBaseCriatura, String[] opcoesDeAcao) {
        super(nome, descricao, probabilidade, "Confronto com Criatura");
        this.tipoDeCriatura = tipoDeCriatura;
        this.nivelDePerigo = nivelDePerigo;
        this.baseDanoCriatura = baseDanoCriatura;
        this.vidaBaseCriatura = vidaBaseCriatura;
        this.opcoesDeAcao = opcoesDeAcao;
        // vidaAtualCriatura será inicializada no executar, com base no turno
    }

    public String getTipoDeCriatura() { return tipoDeCriatura; }
    public String getNivelDePerigo() { return nivelDePerigo; }
    public int getBaseDanoCriatura() { return baseDanoCriatura; }
    public int getVidaBaseCriatura() { return vidaBaseCriatura; }
    public int getVidaAtualCriatura() { return vidaAtualCriatura; }
    public String[] getOpcoesDeAcao() { return opcoesDeAcao; }

    public int calcularDanoEfetivoCriatura(int numeroDoTurno) {
        return this.baseDanoCriatura + (numeroDoTurno / 4); // Exemplo de escalonamento
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
    }

    public String receberDano(int danoRecebido) {
        this.vidaAtualCriatura -= danoRecebido;
<<<<<<< HEAD
        this.vidaAtualCriatura = Math.max(0, this.vidaAtualCriatura);
        if (this.vidaAtualCriatura == 0) {
            return tipoDeCriatura + " foi derrotado(a)!\n";
        }
        return tipoDeCriatura + " recebeu " + danoRecebido + " de dano.";
    }

    public int calcularDanoEfetivoCriatura(int numeroDoTurno) {
        return this.danoBaseCriatura + random.nextInt(3) + (numeroDoTurno / 5);
=======
        this.vidaAtualCriatura = Math.max(0, this.vidaAtualCriatura); // Não deixa vida negativa
        if (this.vidaAtualCriatura == 0) {
            return tipoDeCriatura + " foi derrotado(a)!\n";
        }
        return tipoDeCriatura + " recebeu " + danoRecebido + " de dano. (PV restantes: " + this.vidaAtualCriatura + ")\n";
    }

    @Override
    public String executar(Personagem jogador, Ambiente local, int numeroDoTurno) {
        StringBuilder sb = new StringBuilder();

        this.vidaAtualCriatura = this.vidaBaseCriatura + (numeroDoTurno / 2);
        this.vidaAtualCriatura = Math.max(1, this.vidaAtualCriatura);

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
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
    }
}