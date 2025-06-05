package com.ultimafronteira.model;

import com.ultimafronteira.world.Ambiente;
import com.ultimafronteira.events.GerenciadorDeEventos;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class Personagem {
    private String nome;
    private int vida;
    private int vidaMaxima; // Atributo para a vida máxima
    private int fome;
    private int sede;
    private int energia;
    private int sanidade;
    private Inventario inventario;
    private Ambiente localizacaoAtual;
    private ClassePersonagem classePersonagem;
    private List<String> habilidades;
    private Arma armaEquipada;
    private String chaveImagem; // Chave para a imagem do personagem

    /**
     * Construtor principal para a classe Personagem.
     *
     * @param nome Nome do personagem.
     * @param classe A classe do personagem.
     * @param vidaBase Vida base inicial.
     * @param fomeBase Fome base inicial.
     * @param sedeBase Sede base inicial.
     * @param energiaBase Energia base inicial.
     * @param sanidadeBase Sanidade base inicial.
     * @param localizacaoInicial Ambiente inicial do personagem.
     * @param pesoMaximoInventario Peso máximo que o inventário pode carregar.
     * @param chaveImagem Chave para a imagem do personagem.
     */
    public Personagem(String nome, ClassePersonagem classe, int vidaBase, int fomeBase, int sedeBase, int energiaBase, int sanidadeBase, Ambiente localizacaoInicial, double pesoMaximoInventario, String chaveImagem) {
        this.nome = nome;
        this.classePersonagem = classe;
        this.habilidades = new ArrayList<>();
        this.chaveImagem = chaveImagem; // Atribui a chave da imagem

        this.vida = vidaBase;
        this.fome = fomeBase;
        this.sede = sedeBase;
        this.energia = energiaBase;
        this.sanidade = sanidadeBase;
        this.armaEquipada = null;

        // Aplicar bônus da classe e habilidades antes de definir os máximos
        aplicarBonusDeClasseEAdicionarHabilidadesIniciais();

        // Define vidaMaxima após os bônus de classe serem aplicados à vida.
        // A vida máxima pode ter um teto, por exemplo 100, ou ser baseada na vida inicial + bônus.
        // Para este exemplo, vamos assumir que a vidaMaxima é a vida após bônus, com um teto de 120 (arbitrário).
        this.vidaMaxima = Math.min(120, this.vida);
        this.vida = this.vidaMaxima; // Começa com vida cheia (considerando a máxima)

        // Normaliza outros atributos para não excederem 100 (ou outro teto desejado)
        this.fome = Math.min(100, this.fome);
        this.sede = Math.min(100, this.sede);
        this.energia = Math.min(100, this.energia);
        this.sanidade = Math.min(100, this.sanidade);

        this.localizacaoAtual = localizacaoInicial;
        this.inventario = new Inventario(pesoMaximoInventario);
    }

    /**
     * Construtor alternativo sem chaveImagem, para casos onde não é necessária.
     */
    public Personagem(String nome, ClassePersonagem classe, int vidaBase, int fomeBase, int sedeBase, int energiaBase, int sanidadeBase, Ambiente localizacaoInicial, double pesoMaximoInventario) {
        this(nome, classe, vidaBase, fomeBase, sedeBase, energiaBase, sanidadeBase, localizacaoInicial, pesoMaximoInventario, null); // Chama o construtor principal com chaveImagem null
    }


    private void aplicarBonusDeClasseEAdicionarHabilidadesIniciais() {
        if (this.classePersonagem == null) return;

        switch (this.classePersonagem) {
            case RASTREADOR:
                this.fome += 15;
                this.sede += 10;
                this.habilidades.add("Rastreamento Aguçado");
                this.habilidades.add("Conhecimento da Flora e Fauna");
                this.habilidades.add("Movimentação Silenciosa");
                break;
            case MECANICO:
                this.energia += 10;
                this.habilidades.add("Engenhosidade");
                this.habilidades.add("Fabricar Ferramentas Básicas");
                this.habilidades.add("Desmontar Sucata");
                break;
            case MEDICO:
                this.sanidade += 10;
                this.vida += 5; // Bônus de vida para o médico
                this.habilidades.add("Primeiros Socorros Avançados");
                this.habilidades.add("Conhecimento de Ervas Medicinais");
                this.habilidades.add("Resistência a Doenças Leves");
                break;
            case SOBREVIVENTE_NATO:
                this.vida += 15;
                this.fome += 25;
                this.sede += 25;
                this.energia += 5;
                this.habilidades.add("Metabolismo Eficiente");
                this.habilidades.add("Resiliência Climática");
                this.habilidades.add("Instinto de Perigo");
                break;
        }
        // Garante que os atributos base não fiquem abaixo de um mínimo após modificações
        // e que a vida não ultrapasse um limite antes de definir vidaMaxima.
        this.vida = Math.max(1, this.vida);
        this.fome = Math.max(1, this.fome);   // Fome e sede não devem ser 0 inicialmente de forma trivial
        this.sede = Math.max(1, this.sede);
        this.energia = Math.max(0, this.energia);
        this.sanidade = Math.max(0, this.sanidade);
    }

    // Getters
    public String getNome() { return nome; }
    public int getVida() { return vida; }
    public int getVidaMaxima() { return vidaMaxima; }
    public int getFome() { return fome; }
    public int getSede() { return sede; }
    public int getEnergia() { return energia; }
    public int getSanidade() { return sanidade; }
    public Inventario getInventario() { return inventario; }
    public Ambiente getLocalizacaoAtual() { return localizacaoAtual; }
    public ClassePersonagem getClassePersonagem() { return classePersonagem; }
    public List<String> getHabilidades() { return new ArrayList<>(habilidades); } // Retorna cópia
    public boolean temHabilidade(String nomeHabilidade) { return this.habilidades.contains(nomeHabilidade); }
    public Arma getArmaEquipada() { return armaEquipada; }
    public String getChaveImagem() { return this.chaveImagem; }

    // Setters com validação
    public void setNome(String nome) { this.nome = nome; }
    public void setVida(int vida) {
        this.vida = Math.max(0, vida); // Não pode ser menor que 0
        this.vida = Math.min(this.vida, this.vidaMaxima); // Não pode exceder vidaMaxima
    }
    public void setFome(int fome) { this.fome = Math.max(0, Math.min(100, fome)); } // Teto de 100
    public void setSede(int sede) { this.sede = Math.max(0, Math.min(100, sede)); } // Teto de 100
    public void setEnergia(int energia) { this.energia = Math.max(0, Math.min(100, energia)); } // Teto de 100
    public void setSanidade(int sanidade) { this.sanidade = Math.max(0, Math.min(100, sanidade)); } // Teto de 100
    public void setLocalizacaoAtual(Ambiente localizacaoAtual) { this.localizacaoAtual = localizacaoAtual; }


    /**
     * Calcula o dano base do personagem.
     * Usa o dano da arma equipada ou um valor padrão para ataque desarmado.
     * @return O dano base.
     */
    public int calcularDanoBase() {
        if (this.armaEquipada != null) {
            return this.armaEquipada.getDano();
        }
        return 2; // Dano base desarmado
    }

    /**
     * Equipa uma arma no personagem a partir do nome do item no inventário.
     * @param nomeArma O nome da arma a ser equipada.
     * @return Uma mensagem indicando o resultado da ação.
     */
    public String equiparArma(String nomeArma) {
        Optional<Item> itemNoInventario = this.inventario.buscarItemPorNome(nomeArma);

        if (itemNoInventario.isPresent() && itemNoInventario.get() instanceof Arma) {
            Arma novaArma = (Arma) itemNoInventario.get();
            return equiparArma(novaArma); // Delega para o método que recebe o objeto Arma
        } else {
            return "Arma '" + nomeArma + "' não encontrada no inventário ou não é uma arma.";
        }
    }

    /**
     * Equipa um objeto Arma diretamente no personagem.
     * Verifica se a arma está no inventário antes de equipar.
     * @param novaArma O objeto Arma a ser equipado.
     * @return Uma mensagem indicando o resultado da ação.
     */
    public String equiparArma(Arma novaArma) {
        if (novaArma == null) {
            return "Tentativa de equipar uma arma inválida (nula).";
        }

        // Verifica se a arma (objeto específico) está no inventário.
        // Se o inventário guarda referências, esta verificação é direta.
        // Se o inventário pode ter múltiplas instâncias com o mesmo nome,
        // a lógica de `equiparArma(String nomeArma)` é mais apropriada para encontrar a arma.
        boolean itemEstaNoInventario = this.inventario.getItens().stream()
                .anyMatch(item -> item == novaArma); // Compara referências

        if (!itemEstaNoInventario) {
            // Se não encontrou por referência, tenta por nome (caso o objeto passado seja uma cópia)
            Optional<Item> itemPorNome = this.inventario.buscarItemPorNome(novaArma.getNome());
            if (itemPorNome.isPresent() && itemPorNome.get() instanceof Arma) {
                // Encontrou uma arma com o mesmo nome, mas pode não ser a mesma instância.
                // Para consistência, é melhor forçar que a arma a ser equipada venha de uma busca no inventário.
                // Ou, a lógica aqui assume que `novaArma` é um item válido do inventário.
                // Por ora, vamos assumir que se chegou aqui, é uma arma válida que pode ser equipada.
            } else {
                return "Arma '" + novaArma.getNome() + "' não parece estar no seu inventário.";
            }
        }

        if (this.armaEquipada != null && this.armaEquipada.equals(novaArma)) { // .equals() pode ser mais robusto que == se Arma tiver um equals() implementado
            return novaArma.getNome() + " já está equipada.";
        }

        // Se havia uma arma equipada, ela "volta" para o inventário (já está lá, apenas deixa de ser a equipada)
        // Não é necessário adicionar de volta ao inventário, pois ela nunca foi removida, apenas a referência `armaEquipada` muda.

        this.armaEquipada = novaArma;
        return novaArma.getNome() + " equipada.";
    }


    /**
     * Desequipa a arma atualmente portada pelo personagem.
     * @return Uma mensagem indicando o resultado da ação.
     */
    public String desequiparArma() {
        if (this.armaEquipada != null) {
            String nomeArmaDesequipada = this.armaEquipada.getNome();
            this.armaEquipada = null; // Simplesmente remove a referência da arma equipada
            return nomeArmaDesequipada + " desequipada.";
        } else {
            return "Nenhuma arma equipada para desequipar.";
        }
    }

    /**
     * Gera uma string formatada com o status atual do personagem.
     * @return O status do personagem.
     */
    public String getStatus() {
        String nomeLocalizacao = (localizacaoAtual != null) ? localizacaoAtual.getNome() : "Desconhecida";
        StringBuilder habilidadesStr = new StringBuilder();
        if (habilidades != null && !habilidades.isEmpty()) {
            for (int i = 0; i < habilidades.size(); i++) {
                habilidadesStr.append(habilidades.get(i));
                if (i < habilidades.size() - 1) {
                    habilidadesStr.append(", ");
                }
            }
        } else {
            habilidadesStr.append("Nenhuma");
        }
        String armaEquipadaStr = (armaEquipada != null) ? armaEquipada.getNome() : "Nenhuma";

        return String.format(
                "%-15s: %s (%s)\n" +
                        "%-15s: %d / %d\n" +
                        "%-15s: %d / 100\n" + // Assumindo teto de 100 para fome
                        "%-15s: %d / 100\n" + // Assumindo teto de 100 para sede
                        "%-15s: %d / 100\n" + // Assumindo teto de 100 para energia
                        "%-15s: %d / 100\n" + // Assumindo teto de 100 para sanidade
                        "%-15s: %s\n" +
                        "%-15s: %s\n" +
                        "%-15s: [%s]",
                "Nome", nome, (classePersonagem != null ? classePersonagem.getNomeDisplay() : "Sem Classe"),
                "Vida", vida, vidaMaxima,
                "Fome", fome,
                "Sede", sede,
                "Energia", energia,
                "Sanidade", sanidade,
                "Localização", nomeLocalizacao,
                "Arma Equipada", armaEquipadaStr,
                "Habilidades", habilidadesStr.toString()
        );
    }

    /**
     * Executa a ação de explorar o ambiente atual do personagem.
     * Delega a lógica para o método explorar() do Ambiente.
     * @param ge O GerenciadorDeEventos para possibilitar a ocorrência de eventos.
     * @param numeroDoTurno O número do turno atual.
     * @return Uma string descrevendo o resultado da exploração.
     */
    public String explorarAmbiente(GerenciadorDeEventos ge, int numeroDoTurno) {
        if (this.localizacaoAtual != null) {
            return this.localizacaoAtual.explorar(this, ge, numeroDoTurno);
        } else {
            return this.nome + " não está em nenhum ambiente conhecido para explorar.";
        }
    }
}
