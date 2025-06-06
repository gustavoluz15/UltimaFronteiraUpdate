package com.ultimafronteira.visual;

import com.ultimafronteira.model.Inventario;
import com.ultimafronteira.model.Item;
import com.ultimafronteira.world.Ambiente;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.List;
import java.util.function.Consumer;

public class GameView {

    private BorderPane root;
    private CenaPane cenaPane;
    private TextArea outputArea;
    private PainelInventario painelInventario;
    private VBox listaDeAdjacentes;
    private Button btnExplorar, btnDescansar, btnProximoTurno, btnNovoJogo;
    private Label turnoLabel;
    private Label ambienteAtualLabel;
    private VBox legendaPanel;
    private Label combateMensagemTopo;
    private HBox painelAcoesCombate; // Variável de classe para o painel de combate
    private Button btnLutar;
    private Button btnFugir;
    private final int THUMBNAIL_SIZE = 80;

    public GameView(Consumer<Item> onUseItemCallback) {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #101010;");

        cenaPane = new CenaPane();

        turnoLabel = new Label("Dia 1");
        turnoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        turnoLabel.setTextFill(Color.WHITE);
        turnoLabel.setStyle("-fx-effect: dropshadow(gaussian, black, 3, 0.7, 0, 0);");
        StackPane.setAlignment(turnoLabel, Pos.TOP_LEFT);
        StackPane.setMargin(turnoLabel, new Insets(10));

        ambienteAtualLabel = new Label("Nome do Ambiente");
        ambienteAtualLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        ambienteAtualLabel.setTextFill(Color.WHITE);
        ambienteAtualLabel.setStyle("-fx-effect: dropshadow(gaussian, black, 3, 0.7, 0, 0);");
        StackPane.setAlignment(ambienteAtualLabel, Pos.TOP_CENTER);
        StackPane.setMargin(ambienteAtualLabel, new Insets(10));

        combateMensagemTopo = new Label();
        combateMensagemTopo.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        combateMensagemTopo.setTextFill(Color.WHITE);
        combateMensagemTopo.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6); -fx-padding: 15; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, black, 10, 0.5, 0, 0);");
        combateMensagemTopo.setVisible(false);
        StackPane.setAlignment(combateMensagemTopo, Pos.CENTER);
        combateMensagemTopo.setTranslateY(-150);

        btnLutar = new Button("Lutar");
        btnFugir = new Button("Fugir");
        String lutarStyle = "-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 18px; -fx-padding: 12 28; -fx-cursor: hand; -fx-background-radius: 5; -fx-border-color: #1e7e34; -fx-border-width: 2;";
        String fugirStyle = "-fx-background-color: #c82333; -fx-text-fill: white; -fx-font-size: 18px; -fx-padding: 12 28; -fx-cursor: hand; -fx-background-radius: 5; -fx-border-color: #a31221; -fx-border-width: 2;";
        btnLutar.setStyle(lutarStyle);
        btnFugir.setStyle(fugirStyle);

        painelAcoesCombate = new HBox(40, btnLutar, btnFugir);
        painelAcoesCombate.setAlignment(Pos.CENTER);
        painelAcoesCombate.setVisible(false);
        StackPane.setAlignment(painelAcoesCombate, Pos.CENTER);
        painelAcoesCombate.setTranslateY(80);

        legendaPanel = new VBox(5);
        legendaPanel.setAlignment(Pos.TOP_CENTER);
        StackPane.setAlignment(legendaPanel, Pos.TOP_CENTER);
        StackPane.setMargin(legendaPanel, new Insets(50, 0, 0, 0));
        legendaPanel.setVisible(false);

        btnNovoJogo = new Button("Novo Jogo");
        btnNovoJogo.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        StackPane.setAlignment(btnNovoJogo, Pos.BOTTOM_LEFT);
        StackPane.setMargin(btnNovoJogo, new Insets(10));

        StackPane centroStack = new StackPane(cenaPane, turnoLabel, ambienteAtualLabel, combateMensagemTopo, painelAcoesCombate, legendaPanel, btnNovoJogo);
        root.setCenter(centroStack);

        VBox painelDireito = new VBox(10);
        painelDireito.setPadding(new Insets(10));
        painelDireito.setPrefWidth(320);
        painelDireito.setStyle("-fx-background-color: #2a2a2a;");
        Label lblLog = new Label("Diário de Bordo");
        lblLog.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lblLog.setTextFill(Color.WHITE);
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);
        outputArea.setPrefHeight(450);
        outputArea.setStyle("-fx-control-inner-background:#1a1a1a; -fx-font-family: 'monospaced'; -fx-text-fill: #e0e0e0;");
        Label lblInv = new Label("Inventário");
        lblInv.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        lblInv.setTextFill(Color.WHITE);
        lblInv.setMaxWidth(Double.MAX_VALUE);
        lblInv.setAlignment(Pos.CENTER);
        painelInventario = new PainelInventario(onUseItemCallback);
        painelDireito.getChildren().addAll(lblLog, outputArea, lblInv, painelInventario);
        root.setRight(painelDireito);

        listaDeAdjacentes = new VBox(10);
        listaDeAdjacentes.setPadding(new Insets(10));
        listaDeAdjacentes.setPrefWidth(120);
        listaDeAdjacentes.setStyle("-fx-background-color: #2a2a2a;");
        Label lblAdjacentes = new Label("Viajar Para:");
        lblAdjacentes.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lblAdjacentes.setTextFill(Color.WHITE);
        listaDeAdjacentes.getChildren().add(lblAdjacentes);
        root.setLeft(listaDeAdjacentes);

        FlowPane painelAcoesPrincipais = new FlowPane(15, 15);
        painelAcoesPrincipais.setAlignment(Pos.CENTER);
        painelAcoesPrincipais.setPadding(new Insets(15));
        painelAcoesPrincipais.setStyle("-fx-background-color: #2a2a2a; -fx-border-color: #404040; -fx-border-width: 2 0 0 0;");
        btnExplorar = new Button("Explorar");
        btnDescansar = new Button("Descansar");
        btnProximoTurno = new Button("Avançar Tempo");
        painelAcoesPrincipais.getChildren().addAll(btnExplorar, btnDescansar, btnProximoTurno);
        root.setBottom(painelAcoesPrincipais);
    }

    public Scene getScene() { return new Scene(root, 1366, 768); }
    public Button getBtnExplorar() { return btnExplorar; }
    public Button getBtnDescansar() { return btnDescansar; }
    public Button getBtnProximoTurno() { return btnProximoTurno; }
    public Button getBtnNovoJogo() { return btnNovoJogo; }
    public CenaPane getCenaPane() { return cenaPane; }

    public void limparLog() { outputArea.clear(); }
    public void appendOutput(String text) { if (text != null) outputArea.appendText(text.endsWith("\n") ? text : text + "\n"); }
    public void atualizarInventario(Inventario inventario) { painelInventario.atualizar(inventario); }

    public void atualizarPainelMovimento(List<Ambiente> destinos, Consumer<Ambiente> onMoverClick, boolean emCombate) {
        if (listaDeAdjacentes.getChildren().size() > 1) {
            listaDeAdjacentes.getChildren().remove(1, listaDeAdjacentes.getChildren().size());
        }
        for (Ambiente destino : destinos) {
            ImageView thumbnail = GerenciadorDeImagens.getImageView(destino.getNomeImagem(), THUMBNAIL_SIZE, THUMBNAIL_SIZE);
            Button btnMover = new Button("", thumbnail);
            btnMover.setPrefSize(THUMBNAIL_SIZE, THUMBNAIL_SIZE);
            btnMover.setStyle("-fx-background-color: transparent; -fx-padding: 2;");
            btnMover.setOnAction(e -> onMoverClick.accept(destino));
            btnMover.setDisable(emCombate);
            Tooltip.install(btnMover, new Tooltip(destino.getNome()));
            listaDeAdjacentes.getChildren().add(btnMover);
        }
    }

    public void desabilitarAcoes() {
        btnExplorar.setDisable(true);
        btnDescansar.setDisable(true);
        btnProximoTurno.setDisable(true);
        if (listaDeAdjacentes != null) listaDeAdjacentes.setDisable(true);
    }

    public void mostrarControlesCombate(String mensagem, Consumer<String> onAction) {
        combateMensagemTopo.setText(mensagem);
        combateMensagemTopo.setVisible(true);
        btnLutar.setOnAction(e -> onAction.accept("lutar"));
        btnFugir.setOnAction(e -> onAction.accept("fugir"));
        painelAcoesCombate.setVisible(true);
        root.getBottom().setVisible(false);
    }

    public void esconderControlesCombate() {
        painelAcoesCombate.setVisible(false);
        combateMensagemTopo.setVisible(false);
        limparLegenda();
        root.getBottom().setVisible(true);
    }

    public void exibirMensagem(String... mensagens) {
        limparLegenda();
        for (String msg : mensagens) {
            if (msg != null && !msg.isEmpty()) {
                Label linha = new Label(msg);
                linha.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
                linha.setTextFill(Color.WHITE);
                linha.setStyle("-fx-effect: dropshadow(gaussian, black, 3, 0.7, 0, 0);");
                legendaPanel.getChildren().add(linha);
            }
        }
        legendaPanel.setVisible(true);
    }

    public void limparLegenda() {
        legendaPanel.getChildren().clear();
        legendaPanel.setVisible(false);
    }

    public void atualizarTurno(int turno) {
        turnoLabel.setText("Dia " + turno);
    }

    public void atualizarAmbienteAtual(String nomeAmbiente) {
        ambienteAtualLabel.setText(nomeAmbiente);
    }

    public void exibirLogCombate(String... logs) {
        exibirMensagem(logs);
    }

    public void setControlesCombateDesabilitado(boolean desabilitado) {
        if (painelAcoesCombate != null) {
            painelAcoesCombate.setDisable(desabilitado);
        }
    }

    public HBox getPainelCombate() {
        return painelAcoesCombate;
    }
}