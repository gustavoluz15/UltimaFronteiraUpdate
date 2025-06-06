package com.ultimafronteira.model;

import com.ultimafronteira.world.Ambiente;
// Removido import desnecessário: import com.ultimafronteira.events.GerenciadorDeEventos;
import java.util.List;
import java.util.ArrayList;

public class Personagem {
    private String nome;
    private int vida;
    private int vidaMaxima;
    private int fome;
    private int sede;
    private int energia;
    private int sanidade;
    private Inventario inventario;
    private Ambiente localizacaoAtual;
    private ClassePersonagem classePersonagem;
    private List<String> habilidades;
    private Arma armaEquipada;
    private String chaveImagem;

    public Personagem(String nome, ClassePersonagem classe, int vidaBase, int fomeBase, int sedeBase, int energiaBase, int sanidadeBase, Ambiente localizacaoInicial, double pesoMaximoInventario, String chaveImagem) {
        this.nome = nome;
        this.classePersonagem = classe;
        this.habilidades = new ArrayList<>();
        this.chaveImagem = chaveImagem;

        this.vida = vidaBase;
        this.fome = fomeBase;
        this.sede = sedeBase;
        this.energia = energiaBase;
        this.sanidade = sanidadeBase;
        this.armaEquipada = null;

        aplicarBonusDeClasseEAdicionarHabilidadesIniciais();

        // Garante que os status não ultrapassem os máximos após os bônus
        this.vidaMaxima = Math.min(100, this.vida); // Vida máxima é definida aqui
        this.vida = this.vidaMaxima;                // Começa com vida máxima

        this.fome = Math.min(100, this.fome);
        this.sede = Math.min(100, this.sede);
        this.energia = Math.min(100, this.energia);
        this.sanidade = Math.min(100, this.sanidade);

        this.localizacaoAtual = localizacaoInicial;
        this.inventario = new Inventario(pesoMaximoInventario);
    }

    private void aplicarBonusDeClasseEAdicionarHabilidadesIniciais() {
        if (this.classePersonagem == null) return;
        switch (this.classePersonagem) {
            case RASTREADOR:
                this.fome += 15; this.sede += 10;
                this.habilidades.add("Rastreamento Aguçado"); this.habilidades.add("Conhecimento da Flora e Fauna"); this.habilidades.add("Movimentação Silenciosa");
                break;
            case MECANICO:
                this.energia += 10;
                this.habilidades.add("Engenhosidade"); this.habilidades.add("Fabricar Ferramentas Básicas"); this.habilidades.add("Desmontar Sucata");
                break;
            case MEDICO:
                this.sanidade += 10; this.vida += 5;
                this.habilidades.add("Primeiros Socorros Avançados"); this.habilidades.add("Conhecimento de Ervas Medicinais"); this.habilidades.add("Resistência a Doenças Leves");
                break;
            case SOBREVIVENTE_NATO:
                this.vida += 15; this.fome += 25; this.sede += 25; this.energia += 5;
                this.habilidades.add("Metabolismo Eficiente"); this.habilidades.add("Resiliência Climática"); this.habilidades.add("Instinto de Perigo");
                break;
        }
    }

    public String getNome() { return nome; }
    public int getVida() { return vida; }
    public int getVidaMaxima() { return vidaMaxima; }
    public void setVida(int vida) { this.vida = Math.max(0, Math.min(vida, this.vidaMaxima)); }
    public int getFome() { return fome; }
    public void setFome(int fome) { this.fome = Math.max(0, Math.min(100, fome)); }
    public int getSede() { return sede; }
    public void setSede(int sede) { this.sede = Math.max(0, Math.min(100, sede)); }
    public int getEnergia() { return energia; }
    public void setEnergia(int energia) { this.energia = Math.max(0, Math.min(100, energia)); }
    public int getSanidade() { return sanidade; }
    public void setSanidade(int sanidade) { this.sanidade = Math.max(0, Math.min(100, sanidade)); }
    public Inventario getInventario() { return inventario; }
    public Ambiente getLocalizacaoAtual() { return localizacaoAtual; }
    public void setLocalizacaoAtual(Ambiente localizacaoAtual) { this.localizacaoAtual = localizacaoAtual; }
    public ClassePersonagem getClassePersonagem() { return classePersonagem; }
    public boolean temHabilidade(String nomeHabilidade) { return this.habilidades.contains(nomeHabilidade); }
    public Arma getArmaEquipada() { return armaEquipada; }
    public String getChaveImagem() { return this.chaveImagem; }

    public int calcularDanoBase() {
        if (this.armaEquipada != null) {
            return this.armaEquipada.getDano();
        }
        return 2; // Dano base desarmado
    }

    public String equiparArma(Arma novaArma) {
        if (novaArma == null) return "Arma inválida.";
        boolean itemEstaNoInventario = this.inventario.getItens().contains(novaArma);
        if (itemEstaNoInventario) {
            if (this.armaEquipada != null && this.armaEquipada.equals(novaArma)) {
                return novaArma.getNome() + " já está equipada.";
            }
            this.armaEquipada = novaArma;
            return novaArma.getNome() + " equipada.";
        }
        return "Arma '" + novaArma.getNome() + "' não encontrada no inventário.";
    }

    // CORREÇÃO: Assinatura do método explorarAmbiente ajustada.
    public String explorarAmbiente(int numeroDoTurno) {
        if (this.localizacaoAtual != null) {
            return this.localizacaoAtual.explorar(this, numeroDoTurno);
        }
        return this.nome + " não está em nenhum ambiente conhecido para explorar.";
    }
}