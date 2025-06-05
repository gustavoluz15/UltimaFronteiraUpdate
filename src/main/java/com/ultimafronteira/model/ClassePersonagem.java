package com.ultimafronteira.model;

public enum ClassePersonagem {
    RASTREADOR("Rastreador", "Especialista em encontrar trilhas, alimentos e água na natureza selvagem."),
    MECANICO("Mecânico", "Habilidoso em consertar equipamentos, improvisar ferramentas e criar mecanismos."),
    MEDICO("Médico", "Possui conhecimento para tratar ferimentos e doenças, mesmo com recursos limitados."),
    SOBREVIVENTE_NATO("Sobrevivente Nato", "Instintos apurados e uma resiliência natural a condições adversas como fome, sede e intempéries.");

    private final String nomeDisplay;
    private final String descricao;

    ClassePersonagem(String nomeDisplay, String descricao) {
        this.nomeDisplay = nomeDisplay;
        this.descricao = descricao;
    }

    public String getNomeDisplay() {
        return nomeDisplay;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return nomeDisplay;
    }
}