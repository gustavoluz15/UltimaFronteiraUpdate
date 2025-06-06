package com.ultimafronteira.events;

import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.world.Ambiente;

public class EventoMudancaDeAmbiente extends Evento {

    private Ambiente novoAmbiente;

    public EventoMudancaDeAmbiente(String nome, String descricao, double probabilidade, Ambiente novoAmbiente) {
        super(nome, descricao, probabilidade, "Mudança de Cenário");
        this.novoAmbiente = novoAmbiente;
    }

    public Ambiente getNovoAmbiente() {
        return novoAmbiente;
    }

    @Override
    public String executar(Personagem jogador, Ambiente local, int numeroDoTurno) {

        return getDescricao();
    }
}