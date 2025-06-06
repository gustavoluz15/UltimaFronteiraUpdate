package com.ultimafronteira.visual;

import com.ultimafronteira.model.Alimento;
import com.ultimafronteira.model.Arma;
import com.ultimafronteira.model.Item;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.function.Consumer;

public class SlotInventario extends StackPane {

    private ImageView iconeItemView;
    private Item item;
    private Tooltip tooltipDoItem;

    public SlotInventario(Consumer<Item> onUseCallback) {
        setStyle("-fx-background-color: rgba(10, 10, 10, 0.75); -fx-background-radius: 5; -fx-border-color: #707070; -fx-border-width: 2; -fx-border-radius: 5; -fx-cursor: hand;");
        setPrefSize(64, 64);

        iconeItemView = new ImageView();
        iconeItemView.setFitWidth(50);
        iconeItemView.setFitHeight(50);

        tooltipDoItem = new Tooltip();
        tooltipDoItem.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        tooltipDoItem.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-background-radius: 4; -fx-padding: 10;");
        Tooltip.install(this, tooltipDoItem);

        getChildren().add(iconeItemView);

        this.setOnMouseClicked((MouseEvent event) -> {
            if (this.item != null) {
                onUseCallback.accept(this.item);
            }
        });

        limparSlot();
    }

    public void setItem(Item item) {
        this.item = item;

        if (item != null && item.getChaveImagem() != null) {
            Image icone = GerenciadorDeImagens.getImagem(item.getChaveImagem());
            iconeItemView.setImage(icone);

            StringBuilder tooltipTexto = new StringBuilder();
            tooltipTexto.append(item.getNome().toUpperCase()).append("\n");
            tooltipTexto.append("--------------------\n");
            tooltipTexto.append("Peso: ").append(String.format("%.2f", item.getPeso()));
            if (item.getDurabilidade() != -1) {
                tooltipTexto.append(" | Durabilidade: ").append(item.getDurabilidade());
            }
            if (item instanceof Alimento) {
                tooltipTexto.append("\nTipo: Alimento");
                tooltipTexto.append("\nRestaura ").append(((Alimento) item).getValorNutricional()).append(" de fome.");

            } else if (item instanceof Arma) {
                tooltipTexto.append("\nTipo: Arma");
                tooltipTexto.append("\nDano: ").append(((Arma) item).getDano());
            }
            tooltipDoItem.setText(tooltipTexto.toString());
        } else {
            limparSlot();
        }
    }

    public void limparSlot() {
        this.item = null;
        iconeItemView.setImage(null);
        tooltipDoItem.setText("");
    }
}