package com.ultimafronteira.events;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.world.Ambiente;
import java.util.Random;

public class EventoClimatico extends Evento {

    private int duracao;
    private String chaveImagemFundoOverride;
    private Random random = new Random(); // Gerador de números aleatórios para determinar o tipo de clima

    /**
     * Construtor para EventoClimatico.
     * Inicializa um evento climático genérico que se especificará ao ser executado.
     */
    public EventoClimatico() {
        super("Mudança Climática Súbita", "O tempo muda inesperadamente.", 0.20, "Clima/Ambiente");
        this.duracao = 0; // Duração será definida quando o evento específico for escolhido
        this.chaveImagemFundoOverride = null; // Imagem de fundo será definida se necessário
    }

    /**
     * Obtém a duração restante do evento climático.
     * @return A duração em turnos.
     */
    public int getDuracao() {
        return duracao;
    }

    /**
     * Obtém a chave da imagem de fundo que deve substituir a imagem padrão do ambiente, se houver.
     * @return A chave da imagem de fundo ou null.
     */
    public String getChaveImagemFundoOverride() {
        return chaveImagemFundoOverride;
    }

    /**
     * Decrementa a duração do evento climático em um turno.
     * Não permite que a duração se torne negativa.
     */
    public void decrementarDuracao() {
        if (this.duracao > 0) {
            this.duracao--;
        }
    }

    /**
     * Executa o evento climático, determinando aleatoriamente o tipo de clima
     * e aplicando seus efeitos ao jogador e ao ambiente.
     *
     * @param jogador O personagem do jogador.
     * @param local O ambiente atual onde o evento ocorre.
     * @param numeroDoTurno O número do turno atual no jogo.
     * @return Uma string descrevendo o evento climático e seus efeitos.
     */
    @Override
    public String executar(Personagem jogador, Ambiente local, int numeroDoTurno) {
        int climaSorteado = random.nextInt(3); // Sorteia um número entre 0, 1 e 2
        this.chaveImagemFundoOverride = null; // Reseta a imagem de fundo antes de definir uma nova

        switch (climaSorteado) {
            case 0:
                this.nome = "Nevasca Repentina";
                this.descricao = "Uma nevasca forte e inesperada cobre a área.";
                this.duracao = 2; // Define a duração da nevasca
                // Verifica se o local não é nulo antes de acessar seus métodos
                if (local != null && local.getNome().contains("Geada")) {
                    this.chaveImagemFundoOverride = "fundo_pico_geada_nevasca";
                }
                // Aplica efeitos no jogador
                if (jogador != null) {
                    jogador.setEnergia(jogador.getEnergia() - 15);
                }
                return getDescricao() + " O frio intenso drena sua energia.";

            case 1:
                this.nome = "Tempestade Violenta";
                this.descricao = "Nuvens escuras formam-se e uma tempestade desaba.";
                this.duracao = 3; // Define a duração da tempestade
                if (local != null && local.getNome().contains("Geada")) {
                    this.chaveImagemFundoOverride = "fundo_pico_geada_tempestade";
                } else {
                    // Imagem genérica para tempestade em outros locais
                    this.chaveImagemFundoOverride = "fundo_tempestade";
                }
                if (jogador != null) {
                    jogador.setEnergia(jogador.getEnergia() - 10);
                }
                return getDescricao() + " A chuva e o vento dificultam a jornada.";

            case 2:
                this.nome = "Onda de Calor Intenso";
                this.descricao = "O sol castiga impiedosamente, elevando a temperatura.";
                this.duracao = 2; // Define a duração da onda de calor
                this.chaveImagemFundoOverride = "fundo_calor";
                if (jogador != null) {
                    jogador.setSede(jogador.getSede() - 20);
                }
                return getDescricao() + " A sua garganta seca rapidamente.";

            default:
                // Este caso não deveria ser alcançado com random.nextInt(3)
                this.nome = "Clima Instável";
                this.descricao = "O tempo permanece incerto e ameaçador.";
                this.duracao = 1;
                return "O tempo permanece instável.";
        }
    }
}
