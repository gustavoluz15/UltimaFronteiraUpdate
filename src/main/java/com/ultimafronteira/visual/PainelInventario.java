package com.ultimafronteira.visual;

import com.ultimafronteira.model.Inventario;
import com.ultimafronteira.model.Item;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import java.util.List;
import java.util.function.Consumer;

public class PainelInventario extends GridPane {

    private final int NUM_COLUNAS = 4;
    private final int NUM_LINHAS = 3;

    public PainelInventario(Consumer<Item> onUseCallback) {
        this.setHgap(10);
        this.setVgap(10);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(10));
        this.setStyle("-fx-background-color: rgba(26, 26, 26, 0.7); -fx-background-radius: 8;");

        for (int i = 0; i < NUM_LINHAS; i++) {
            for (int j = 0; j < NUM_COLUNAS; j++) {
                this.add(new SlotInventario(onUseCallback), j, i);
            }
        }
    }

    public void atualizar(Inventario inventario) {
        List<Item> itensDoJogador = inventario.getItens();
        List<javafx.scene.Node> slots = this.getChildren();

        for (int i = 0; i < slots.size(); i++) {
            if (slots.get(i) instanceof SlotInventario) {
                SlotInventario slot = (SlotInventario) slots.get(i);
                if (i < itensDoJogador.size()) {
                    slot.setItem(itensDoJogador.get(i));
                } else {
                    slot.limparSlot();
                }
            }
        }
    }
}