package com.ultimafronteira.model;

import com.ultimafronteira.world.Ambiente;
import com.ultimafronteira.events.GerenciadorDeEventos;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class Personagem {
    private String nome;
    private String chaveImagem;

    private int vida;
    private int vidaMaxima;
    private int fome;
    private int fomeMaxima;
    private int sede;
    private int sedeMaxima;
    private int energia;
    private int energiaMaxima;
    private int sanidade;
    private int sanidadeMaxima;

    private Inventario inventario;
    private Ambiente localizacaoAtual;
    private ClassePersonagem classePersonagem;
    private List<String> habilidades;
    private Arma armaEquipada;

    private int danoBonusProximoAtaque;
    private int reducaoDanoProximaDefesa;
    private boolean buffDanoAtivo;
    private boolean buffDefesaAtivo;

    public Personagem(String nome, ClassePersonagem classe, int vidaBase, int fomeBase, int sedeBase, int energiaBase, int sanidadeBase, Ambiente localizacaoInicial, double pesoMaximoInventario, String chaveImagem) {
        this.nome = nome;
        this.classePersonagem = classe;
        this.habilidades = new ArrayList<>();

        this.vida = vidaBase;
        this.fome = fomeBase;
        this.sede = sedeBase;
        this.energia = energiaBase;
        this.sanidade = sanidadeBase;
        this.armaEquipada = null;
        this.chaveImagem = chaveImagem;

        this.vidaMaxima = vidaBase;
        this.fomeMaxima = 100;
        this.sedeMaxima = 100;
        this.energiaMaxima = 100;
        this.sanidadeMaxima = 100;

        aplicarBonusDeClasseEAdicionarHabilidadesIniciais();

        this.vidaMaxima = this.vida > this.vidaMaxima ? this.vida : this.vidaMaxima;
        this.fomeMaxima = this.fome > this.fomeMaxima ? this.fome : this.fomeMaxima;
        this.sedeMaxima = this.sede > this.sedeMaxima ? this.sede : this.sedeMaxima;
        this.energiaMaxima = this.energia > this.energiaMaxima ? this.energia : this.energiaMaxima;
        this.sanidadeMaxima = this.sanidade > this.sanidadeMaxima ? this.sanidade : this.sanidadeMaxima;

        this.vida = Math.min(this.vida, this.vidaMaxima);

        this.danoBonusProximoAtaque = 0;
        this.reducaoDanoProximaDefesa = 0;
        this.buffDanoAtivo = false;
        this.buffDefesaAtivo = false;

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
                this.energia += 10; this.vida += 5;
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
    public void setNome(String nome) { this.nome = nome; }
    public String getChaveImagem() { return chaveImagem; }

    public int getVida() { return vida; }
    public int getVidaMaxima() { return vidaMaxima; }
    public void setVida(int vida) { this.vida = Math.max(0, Math.min(vida, this.vidaMaxima));}

    public int getFome() { return fome; }
    public int getFomeMaxima() { return fomeMaxima; }
    public void setFome(int fome) { this.fome = Math.max(0, Math.min(fome, this.fomeMaxima)); }

    public int getSede() { return sede; }
    public int getSedeMaxima() { return sedeMaxima; }
    public void setSede(int sede) { this.sede = Math.max(0, Math.min(sede, this.sedeMaxima)); }

    public int getEnergia() { return energia; }
    public int getEnergiaMaxima() { return energiaMaxima; }
    public void setEnergia(int energia) { this.energia = Math.max(0, Math.min(energia, this.energiaMaxima)); }

    public int getSanidade() { return sanidade; }
    public int getSanidadeMaxima() { return sanidadeMaxima; }
    public void setSanidade(int sanidade) { this.sanidade = Math.max(0, Math.min(sanidade, this.sanidadeMaxima)); }

    public Inventario getInventario() { return inventario; }
    public Ambiente getLocalizacaoAtual() { return localizacaoAtual; }
    public void setLocalizacaoAtual(Ambiente localizacaoAtual) { this.localizacaoAtual = localizacaoAtual; }
    public ClassePersonagem getClassePersonagem() { return classePersonagem; }
    public List<String> getHabilidades() { return new ArrayList<>(habilidades); }
    public boolean temHabilidade(String nomeHabilidade) { return this.habilidades.contains(nomeHabilidade); }
    public Arma getArmaEquipada() { return armaEquipada; }

    public String equiparArma(Arma novaArma) {
        if (novaArma == null) return "Arma inválida.";

        boolean temArmaNoInventario = this.inventario.getItens().stream()
                .anyMatch(i -> i.equals(novaArma));

        if (temArmaNoInventario) {
            if (this.armaEquipada != null && this.armaEquipada.equals(novaArma)) {
                return novaArma.getNome() + " já está equipada.";
            }
            this.armaEquipada = novaArma;
            return novaArma.getNome() + " equipada.";
        } else {
            return "Arma '" + novaArma.getNome() + "' não encontrada no inventário.";
        }
    }

    public String desequiparArma() {
        if (this.armaEquipada != null) {
            String nomeArmaDesequipada = this.armaEquipada.getNome();
            this.armaEquipada = null;
            return nomeArmaDesequipada + " desequipada.";
        } else {
            return "Nenhuma arma equipada para desequipar.";
        }
    }

    public void aplicarBuffDano(int valor) { this.danoBonusProximoAtaque = valor; this.buffDanoAtivo = true; }
    public int consumirBuffDano() { if (!buffDanoAtivo) return 0; int bonus = this.danoBonusProximoAtaque; this.danoBonusProximoAtaque = 0; this.buffDanoAtivo = false; return bonus; }
    public boolean isBuffDanoAtivo() { return buffDanoAtivo; }
    public int getDanoBonus() { return this.danoBonusProximoAtaque; }
    public void aplicarBuffDefesa(int valor) { this.reducaoDanoProximaDefesa = valor; this.buffDefesaAtivo = true;}
    public int consumirBuffDefesa() { if (!buffDefesaAtivo) return 0; int reducao = this.reducaoDanoProximaDefesa; this.reducaoDanoProximaDefesa = 0; this.buffDefesaAtivo = false; return reducao; }
    public boolean isBuffDefesaAtivo() { return buffDefesaAtivo; }
    public int getDefesaBonus() { return this.reducaoDanoProximaDefesa; }

    public int calcularDanoBase() {
        int dano = 2;
        if (getArmaEquipada() != null) {
            dano = getArmaEquipada().getDano();
        }
        if (getClassePersonagem() == ClassePersonagem.MECANICO && temHabilidade("Engenhosidade") && getArmaEquipada() != null) {
            dano += 3;
        } else if (getClassePersonagem() == ClassePersonagem.SOBREVIVENTE_NATO) {
            dano += 1;
        }
        return Math.max(1, dano);
    }

    public String getStatus() {
        String nomeLocalizacao = (localizacaoAtual != null) ? localizacaoAtual.getNome() : "Desconhecida";
        StringBuilder habilidadesStr = new StringBuilder();
        if (!habilidades.isEmpty()) { for(String hab : habilidades) { habilidadesStr.append(hab).append(", "); } if (habilidadesStr.length() > 0) { habilidadesStr.setLength(habilidadesStr.length() - 2); }} else { habilidadesStr.append("Nenhuma"); }
        String armaEquipadaStr = (armaEquipada != null) ? armaEquipada.getNome() : "Nenhuma";
        String buffDanoStr = buffDanoAtivo ? " (Dano Extra: +" + danoBonusProximoAtaque + ")" : "";
        String buffDefesaStr = buffDefesaAtivo ? " (Defesa Extra: +" + reducaoDanoProximaDefesa + ")" : "";

        return String.format(
                "%-15s: %s (%s)\n" + "%-15s: %d / %d %s%s\n" + "%-15s: %d / %d\n" + "%-15s: %d / %d\n" + "%-15s: %d / %d\n" + "%-15s: %d / %d\n" + "%-15s: %s\n" + "%-15s: %s\n" + "%-15s: [%s]",
                "Nome", nome, (classePersonagem != null ? classePersonagem.getNomeDisplay() : "Sem Classe"),
                "Vida", vida, vidaMaxima, buffDanoStr, buffDefesaStr,
                "Fome", fome, fomeMaxima,
                "Sede", sede, sedeMaxima,
                "Energia", energia, energiaMaxima,
                "Sanidade", sanidade, sanidadeMaxima,
                "Localização", nomeLocalizacao,
                "Arma Equipada", armaEquipadaStr,
                "Habilidades", habilidadesStr.toString()
        );
    }
    public String explorarAmbiente(GerenciadorDeEventos ge, int numeroDoTurno) {
        if (this.localizacaoAtual != null) { return this.localizacaoAtual.explorar(this, ge, numeroDoTurno); }
        else { return this.nome + " não está em nenhum ambiente conhecido para explorar."; }
    }
}