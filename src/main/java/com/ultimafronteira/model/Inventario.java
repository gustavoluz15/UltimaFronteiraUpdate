package com.ultimafronteira.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Iterator; // Importado para remoção segura durante iteração se necessário

public class Inventario {
    private List<Item> itens;
    private double pesoMaximo;
    private double pesoAtual;

    /**
     * Construtor do Inventario.
     * @param pesoMaximo O peso máximo que o inventário pode carregar.
     */
    public Inventario(double pesoMaximo) {
        this.itens = new ArrayList<>();
        this.pesoMaximo = pesoMaximo;
        this.pesoAtual = 0;
    }

    /**
     * Retorna uma cópia da lista de itens para proteger a lista interna contra modificações externas.
     * @return Uma nova lista contendo os itens do inventário.
     */
    public List<Item> getItens() {
        return new ArrayList<>(itens); // Retorna uma cópia para proteger a lista interna
    }

    public double getPesoAtual() {
        return pesoAtual;
    }

    public double getPesoMaximo() {
        return pesoMaximo;
    }

    public double getEspacoDisponivel() {
        return pesoMaximo - pesoAtual;
    }

    /**
     * Adiciona um item ao inventário se houver espaço e o item não for nulo.
     * @param item O item a ser adicionado.
     * @return true se o item foi adicionado com sucesso, false caso contrário.
     */
    public boolean adicionarItem(Item item) {
        if (item == null) {
            System.out.println("Tentativa de adicionar item nulo ao inventário."); // Feedback útil
            return false;
        }
        if (pesoAtual + item.getPeso() <= pesoMaximo) {
            // Verifica se já existe um item empilhável com o mesmo nome
            Optional<Item> itemExistenteOpt = itens.stream()
                    .filter(i -> i.getNome().equalsIgnoreCase(item.getNome()) && i.isEmpilhavel())
                    .findFirst();

            if (itemExistenteOpt.isPresent() && item.isEmpilhavel()) {
                Item itemExistente = itemExistenteOpt.get();
                itemExistente.setQuantidade(itemExistente.getQuantidade() + item.getQuantidade());
            } else {
                itens.add(item); // Adiciona como um novo slot se não for empilhável ou não existir
            }
            pesoAtual += item.getPeso() * item.getQuantidade(); // Considera a quantidade para o peso
            // System.out.println(item.getNome() + " adicionado ao inventário."); // Feedback
            return true;
        } else {
            System.out.println("Inventário cheio ou item muito pesado. Não foi possível adicionar: " + item.getNome()); // Feedback útil
            return false;
        }
    }

    /**
     * Remove uma quantidade específica de um item do inventário pelo nome.
     * Se a quantidade a ser removida for maior ou igual à quantidade existente, o item é completamente removido.
     * @param nomeItem O nome do item a ser removido.
     * @param quantidadeARemover A quantidade do item a ser removida.
     * @return true se o item (ou parte dele) foi removido com sucesso, false caso contrário.
     */
    public boolean removerItem(String nomeItem, int quantidadeARemover) {
        if (quantidadeARemover <= 0) return false;

        Optional<Item> itemParaRemoverOpt = itens.stream()
                .filter(item -> item.getNome().equalsIgnoreCase(nomeItem))
                .findFirst();

        if (itemParaRemoverOpt.isPresent()) {
            Item item = itemParaRemoverOpt.get();
            pesoAtual -= item.getPesoUnitario() * Math.min(item.getQuantidade(), quantidadeARemover); // Subtrai o peso corretamente

            if (item.isEmpilhavel() && item.getQuantidade() > quantidadeARemover) {
                item.setQuantidade(item.getQuantidade() - quantidadeARemover);
            } else {
                itens.remove(item);
            }
            // System.out.println(nomeItem + " ("+ quantidadeARemover + ") removido(s) do inventário.");
            return true;
        } else {
            // System.out.println("Item '" + nomeItem + "' não encontrado para remoção.");
            return false;
        }
    }


    /**
     * Remove a primeira ocorrência de um item do inventário pelo nome.
     * Este método é mantido para compatibilidade, mas removerItem(String, int) é preferível para itens empilháveis.
     * @param nomeItem O nome do item a ser removido.
     * @return true se o item foi removido com sucesso, false caso contrário.
     */
    public boolean removerItem(String nomeItem) {
        return removerItem(nomeItem, 1); // Delega para o método mais específico, removendo 1 unidade
    }


    /**
     * Permite que o jogador use um item do inventário.
     * A lógica específica de uso é delegada ao método usar() do próprio Item.
     * Itens consumíveis ou itens que quebram (durabilidade chega a 0) são removidos após o uso.
     * @param nomeItem O nome do item a ser usado.
     * @param jogador O personagem que está usando o item.
     */
    public void usarItem(String nomeItem, Personagem jogador) {
        Optional<Item> itemParaUsarOpt = itens.stream()
                .filter(item -> item.getNome().equalsIgnoreCase(nomeItem))
                .findFirst();

        if (itemParaUsarOpt.isPresent()) {
            Item item = itemParaUsarOpt.get();
            item.usar(jogador); // O método usar do item deve decrementar sua própria durabilidade interna

            // Verifica se o item deve ser removido após o uso
            // (ex: consumíveis ou itens cuja durabilidade chegou a zero)
            // A classe Item ou suas subclasses deveriam idealmente sinalizar se devem ser removidas.
            // Por agora, assumimos que o método 'usar' do item não o remove automaticamente do inventário,
            // mas ajusta sua durabilidade. A remoção acontece aqui se necessário.
            if (item.getDurabilidade() == 0 && item.getDurabilidadeOriginal() != -1) { // Considera durabilidade original != -1 para itens não inquebráveis
                System.out.println(item.getNome() + " quebrou após o uso e foi removido.");
                removerItem(item.getNome(), item.getQuantidade()); // Remove todas as unidades do slot se quebrou
            } else if (item instanceof Alimento || item instanceof Agua || item instanceof Remedio) {
                // Para consumíveis que podem ter durabilidade > 0 (ex: cantil com vários goles, comida com várias porções)
                // a remoção total só ocorre se a durabilidade for 0.
                // Se um item consumível tem durabilidade 1 (uso único), ele será removido pelo if anterior.
                // Se a lógica é que todo alimento/água/remédio é sempre consumido totalmente em um uso,
                // independentemente da durabilidade > 0, então a remoção seria feita aqui.
                // A lógica atual no 'usar' de Alimento e Agua já trata a remoção se durabilidade <= 0.
                // Esta parte pode precisar de refinamento com base em como a durabilidade de consumíveis é tratada.
                // Exemplo: Se um Alimento tem 3 usos (durabilidade = 3), e 'usar' decrementa para 2, ele não é removido aqui.
                // Se 'usar' o consome todo, e a classe Alimento/Agua/Remedio já lida com sua remoção via getDurabilidade() == 0,
                // então a lógica abaixo pode ser redundante ou precisar de ajuste.
                // Por segurança, se o item ainda existir (não foi auto-removido pela sua classe),
                // e sua durabilidade após o uso for 0, removemos.
                if (item.getDurabilidade() == 0 && itens.contains(item)) {
                    System.out.println(item.getNome() + " foi completamente consumido(a).");
                    removerItem(item.getNome(), item.getQuantidade());
                }
            }
        } else {
            System.out.println("Item '" + nomeItem + "' não encontrado no inventário para usar.");
        }
    }

    /**
     * Busca um item no inventário pelo nome.
     * @param nomeItem O nome do item a ser buscado.
     * @return Um Optional contendo o Item se encontrado, ou Optional.empty() caso contrário.
     */
    public Optional<Item> buscarItemPorNome(String nomeItem) {
        return itens.stream()
                .filter(item -> item.getNome().equalsIgnoreCase(nomeItem))
                .findFirst();
    }


    /**
     * Lista todos os itens presentes no inventário no console.
     * Inclui o peso total e máximo.
     */
    public void listarItensConsole() {
        if (itens.isEmpty()) {
            System.out.println("Inventário vazio.");
            return;
        }
        System.out.println("--- Inventário (Console) ---");
        // Formato para exibição: Nome, Peso Unitário, Quantidade, Durabilidade
        System.out.printf("%-25s | %-7s | %-5s | %-12s%n", "Item", "Peso Un.", "Qtde", "Durabilidade");
        System.out.println("-----------------------------------------------------------------");
        for (Item item : itens) {
            System.out.printf("%-25s | %-7.2f | %-5d | %-12s%n",
                    item.getNome(),
                    item.getPesoUnitario(),
                    item.getQuantidade(),
                    (item.getDurabilidadeOriginal() == -1 ? "Inquebrável" : String.valueOf(item.getDurabilidade()))
            );
        }
        System.out.println("-----------------------------------------------------------------");
        System.out.println("Peso atual: " + String.format("%.2f", pesoAtual) + "/" + String.format("%.2f", pesoMaximo));
        System.out.println("----------------------------");
    }
}
