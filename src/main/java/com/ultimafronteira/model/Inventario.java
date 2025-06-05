package com.ultimafronteira.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Inventario {
    private List<Item> itens;
    private double pesoMaximo;
    private double pesoAtual;

    public Inventario(double pesoMaximo) {
        this.itens = new ArrayList<>();
        this.pesoMaximo = pesoMaximo;
        this.pesoAtual = 0;
    }

    public List<Item> getItens() {
<<<<<<< HEAD
        return new ArrayList<>(itens); // Retorna uma cópia para proteger a lista interna
=======
        return new ArrayList<>(itens);
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
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

    public boolean adicionarItem(Item item) {
        if (item == null) {
<<<<<<< HEAD
            System.out.println("Tentativa de adicionar item nulo ao inventário.");
=======
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
            return false;
        }
        if (pesoAtual + item.getPeso() <= pesoMaximo) {
            itens.add(item);
            pesoAtual += item.getPeso();
            return true;
        } else {
<<<<<<< HEAD
            System.out.println("Inventário cheio. Não foi possível adicionar: " + item.getNome());
=======
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
            return false;
        }
    }

    public boolean removerItem(String nomeItem) {
        Optional<Item> itemParaRemover = itens.stream()
                .filter(item -> item.getNome().equalsIgnoreCase(nomeItem))
                .findFirst();
        if (itemParaRemover.isPresent()) {
            Item item = itemParaRemover.get();
            itens.remove(item);
            pesoAtual -= item.getPeso();
            return true;
        } else {
            return false;
        }
    }

    public void usarItem(String nomeItem, Personagem jogador) {
        Optional<Item> itemParaUsarOpt = itens.stream()
                .filter(item -> item.getNome().equalsIgnoreCase(nomeItem))
                .findFirst();
        if (itemParaUsarOpt.isPresent()) {
            Item item = itemParaUsarOpt.get();
            item.usar(jogador);

<<<<<<< HEAD
            if (item instanceof Alimento || item instanceof Agua || item instanceof Remedio) { // Remedios geralmente são consumíveis
                removerItem(item.getNome()); // Remove após o uso
                System.out.println(item.getNome() + " foi consumido(a).");
            } else if (item.getDurabilidade() != -1) {

                if (item.getDurabilidade() == 0) {
                    System.out.println(item.getNome() + " quebrou após o uso e foi removido.");
=======
            if (item instanceof Alimento || item instanceof Agua) {
                removerItem(item.getNome());
            } else if (item.getDurabilidade() > 0) {
                item.setDurabilidade(item.getDurabilidade() - 1);
                if (item.getDurabilidade() == 0) {
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
                    removerItem(item.getNome());
                }
            }
        } else {
<<<<<<< HEAD
            System.out.println("Item '" + nomeItem + "' não encontrado no inventário para usar.");
=======
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
        }
    }

    public void listarItensConsole() {
        if (itens.isEmpty()) {
            System.out.println("Inventário vazio.");
            return;
        }
        System.out.println("--- Inventário (Console) ---");
        for (Item item : itens) {
<<<<<<< HEAD
            System.out.println("- " + item.toString()); // Usa o toString() do Item
=======
            System.out.println("- " + item.toString());
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
        }
        System.out.println("Peso atual: " + String.format("%.2f", pesoAtual) + "/" + String.format("%.2f", pesoMaximo));
        System.out.println("----------------------------");
    }
}