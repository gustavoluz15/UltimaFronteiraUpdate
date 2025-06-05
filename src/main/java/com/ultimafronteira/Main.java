package com.ultimafronteira;

// Imports from f7209b5... (right side) and merged from HEAD (left side)
import com.ultimafronteira.events.Evento;
import com.ultimafronteira.events.EventoClimatico; // Added from HEAD
import com.ultimafronteira.events.EventoCriatura;
import com.ultimafronteira.events.EventoDescoberta; // Added from HEAD
import com.ultimafronteira.events.GerenciadorDeEventos;
import com.ultimafronteira.gamecore.ControladorDeTurno;
import com.ultimafronteira.model.*;
import com.ultimafronteira.visual.GerenciadorDeImagens; // Added from HEAD
import com.ultimafronteira.world.Ambiente;
import com.ultimafronteira.world.GerenciadorDeAmbientes;

import javafx.application.Application;
import javafx.application.Platform; // Added from HEAD (for timer in combat)
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node; // <<< IMPORT ADICIONADO
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color; // Added from HEAD (for popups)
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
// import java.util.function.Consumer; // From HEAD, not used if GameView is omitted

public class Main extends Application {

    private Stage primaryStage;
    private Personagem jogador;
    private TextArea outputArea;
    private GerenciadorDeEventos gerenciadorDeEventos;
    private GerenciadorDeAmbientes gerenciadorDeAmbientes;
    private ControladorDeTurno controladorDeTurno;

    // UI Components
    private VBox layoutPrincipalControles;
    private HBox itensBox, usarItemBox, equiparArmaBox, acoesInfoBox, turnoBox, fabricacaoBox;
    private VBox painelMovimento;
    private VBox painelAcoesConfronto;
    private Button btnLutar;
    private Button btnFugir;
    private Button btnFabricarFaca;
    private Button btnNovoJogo; // <<< DECLARADO COMO CAMPO DA CLASSE

    private EventoCriatura eventoConfrontoAtual = null;
    private EventoClimatico eventoClimaticoAtivo = null;
    private boolean emConfronto = false;
    private Random random = new Random();

    // Constants
    private final int VIDA_BASE_PADRAO = 100;
    private final int FOME_BASE_PADRAO = 80;
    private final int SEDE_BASE_PADRAO = 80;
    private final int ENERGIA_BASE_PADRAO = 100;
    private final int SANIDADE_BASE_PADRAO = 70;
    private final double PESO_INV_PADRAO = 20.0;

    // Item instances (protótipos para debug/inicialização)
    private Alimento maca;
    private Agua aguaPura;
    private Ferramenta machadoSimples;
    private Arma espadaCurta;
    private Arma arcoSimples;
    private Remedio bandagem;
    private Material madeira;
    private Material pedraAfiada;

    @Override
    public void start(Stage primaryStage) {
        try {
            this.primaryStage = primaryStage;
            GerenciadorDeImagens.carregarImagens();
            primaryStage.setTitle("Última Fronteira - Criação de Personagem");
            mostrarCenaCriacaoPersonagem();
        } catch (Throwable t) {
            System.err.println("!!! ERRO FATAL DURANTE A INICIALIZAÇÃO !!!");
            t.printStackTrace();
            // Considerar mostrar um Alert aqui também, se a GUI não puder ser inicializada.
        }
    }

    private void mostrarCenaCriacaoPersonagem() {
        VBox layoutCriacao = new VBox(15);
        layoutCriacao.setPadding(new Insets(25));
        layoutCriacao.setAlignment(Pos.CENTER_LEFT);
        Label lblTitulo = new Label("Crie seu Personagem para Última Fronteira");
        lblTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        VBox.setMargin(lblTitulo, new Insets(0, 0, 20, 0));

        HBox nomeBox = new HBox(10);
        nomeBox.setAlignment(Pos.CENTER_LEFT);
        Label lblNome = new Label("Nome:");
        TextField txtNome = new TextField("Explorador");
        txtNome.setPromptText("Digite o nome");
        txtNome.setPrefWidth(200);
        nomeBox.getChildren().addAll(lblNome, txtNome);

        HBox classeBox = new HBox(10);
        classeBox.setAlignment(Pos.CENTER_LEFT);
        Label lblClasse = new Label("Classe:");
        ComboBox<ClassePersonagem> cbClasse = new ComboBox<>();
        cbClasse.getItems().addAll(ClassePersonagem.values());
        cbClasse.setValue(ClassePersonagem.SOBREVIVENTE_NATO);
        classeBox.getChildren().addAll(lblClasse, cbClasse);

        VBox detalhesClasseBox = new VBox(10);
        detalhesClasseBox.setPadding(new Insets(10, 0, 10, 0));
        detalhesClasseBox.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-padding: 10;");
        Label lblDescricaoTitulo = new Label("Descrição da Classe:");
        lblDescricaoTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label lblDescricaoClasseValor = new Label();
        lblDescricaoClasseValor.setWrapText(true);
        lblDescricaoClasseValor.setMaxWidth(350);
        Label lblAtributosTitulo = new Label("Bônus e Habilidades Chave:");
        lblAtributosTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label lblAtributosClasseValor = new Label();
        lblAtributosClasseValor.setWrapText(true);
        lblAtributosClasseValor.setMaxWidth(350);
        detalhesClasseBox.getChildren().addAll(lblDescricaoTitulo, lblDescricaoClasseValor, lblAtributosTitulo, lblAtributosClasseValor);

        cbClasse.setOnAction(e -> exibirDetalhesDaClasseSelecionada(cbClasse.getValue(), lblDescricaoClasseValor, lblAtributosClasseValor));
        exibirDetalhesDaClasseSelecionada(cbClasse.getValue(), lblDescricaoClasseValor, lblAtributosClasseValor);

        Button btnIniciarJogo = new Button("Iniciar Aventura!");
        btnIniciarJogo.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        btnIniciarJogo.setOnAction(e -> {
            String nomeJogador = txtNome.getText();
            ClassePersonagem classeEscolhida = cbClasse.getValue();
            if (nomeJogador == null || nomeJogador.trim().isEmpty()) {
                mostrarAlerta("Nome Inválido", "Por favor, digite um nome para o seu personagem.");
                return;
            }
            if (classeEscolhida == null) {
                mostrarAlerta("Classe Inválida", "Por favor, escolha uma classe para o seu personagem.");
                return;
            }
            iniciarJogoPrincipal(nomeJogador, classeEscolhida);
        });
        VBox.setMargin(btnIniciarJogo, new Insets(20, 0, 0, 0));

        layoutCriacao.getChildren().addAll(lblTitulo, nomeBox, classeBox, detalhesClasseBox, btnIniciarJogo);
        Scene cenaCriacao = new Scene(layoutCriacao, 450, 520);
        primaryStage.setScene(cenaCriacao);
        primaryStage.show();
    }

    private void exibirDetalhesDaClasseSelecionada(ClassePersonagem classe, Label lblDescricao, Label lblAtributos) {
        if (classe == null) return;
        lblDescricao.setText(classe.getDescricao());
        StringBuilder atributosStr = new StringBuilder();
        int vidaPreview = VIDA_BASE_PADRAO;
        int energiaPreview = ENERGIA_BASE_PADRAO;
        int sanidadePreview = SANIDADE_BASE_PADRAO;
        int fomePreview = FOME_BASE_PADRAO;
        int sedePreview = SEDE_BASE_PADRAO;
        List<String> habilidadesPreview = new ArrayList<>();

        switch (classe) {
            case RASTREADOR:
                fomePreview += 15; sedePreview += 10;
                habilidadesPreview.add("Rastreamento Aguçado");
                habilidadesPreview.add("Conhecimento da Flora e Fauna");
                habilidadesPreview.add("Movimentação Silenciosa");
                atributosStr.append(String.format("Fome Inicial: ~%d (+15)\nSede Inicial: ~%d (+10)\n", fomePreview, sedePreview));
                break;
            case MECANICO:
                energiaPreview += 10;
                habilidadesPreview.add("Engenhosidade");
                habilidadesPreview.add("Fabricar Ferramentas Básicas");
                habilidadesPreview.add("Desmontar Sucata");
                atributosStr.append(String.format("Energia Inicial: ~%d (+10)\n", energiaPreview));
                break;
            case MEDICO:
                sanidadePreview += 10; vidaPreview +=5;
                habilidadesPreview.add("Primeiros Socorros Avançados");
                habilidadesPreview.add("Conhecimento de Ervas Medicinais");
                habilidadesPreview.add("Resistência a Doenças Leves");
                atributosStr.append(String.format("Vida Inicial: ~%d (+5)\nSanidade Inicial: ~%d (+10)\n", vidaPreview, sanidadePreview));
                break;
            case SOBREVIVENTE_NATO:
                vidaPreview += 15; fomePreview += 25; sedePreview += 25; energiaPreview += 5;
                habilidadesPreview.add("Metabolismo Eficiente");
                habilidadesPreview.add("Resiliência Climática");
                habilidadesPreview.add("Instinto de Perigo");
                atributosStr.append(String.format("Vida: ~%d (+15)\nFome: ~%d (+25)\nSede: ~%d (+25)\nEnergia: ~%d (+5)\n", vidaPreview, fomePreview, sedePreview, energiaPreview));
                break;
        }
        if (!habilidadesPreview.isEmpty()) {
            atributosStr.append("Habilidades Chave: ").append(String.join(", ", habilidadesPreview)).append("\n");
        }
        lblAtributos.setText(atributosStr.toString());
    }

    private void mostrarAlerta(String titulo, String conteudo) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }

    private void iniciarJogoPrincipal(String nomeJogador, ClassePersonagem classeEscolhida) {
        primaryStage.setTitle("Última Fronteira");
        gerenciadorDeAmbientes = new GerenciadorDeAmbientes();
        Ambiente ambienteInicial = gerenciadorDeAmbientes.getAmbientePorNome("Floresta dos Sussurros");
        if (ambienteInicial == null) {
            ambienteInicial = gerenciadorDeAmbientes.getAmbienteGlobalPadrao();
            if (ambienteInicial == null) {
                mostrarAlerta("Erro Crítico", "Não foi possível carregar o ambiente inicial. O jogo não pode continuar.");
                Platform.exit();
                return;
            }
        }

        String chaveImagemPersonagem = "personagem_sobrevivente_nato";
        switch (classeEscolhida) {
            case MECANICO: chaveImagemPersonagem = "personagem_mecanico"; break;
            case MEDICO: chaveImagemPersonagem = "personagem_medico"; break;
            case RASTREADOR: chaveImagemPersonagem = "personagem_rastreador"; break;
        }

        jogador = new Personagem(nomeJogador, classeEscolhida, VIDA_BASE_PADRAO, FOME_BASE_PADRAO, SEDE_BASE_PADRAO, ENERGIA_BASE_PADRAO, SANIDADE_BASE_PADRAO, ambienteInicial, PESO_INV_PADRAO, chaveImagemPersonagem);
        gerenciadorDeEventos = new GerenciadorDeEventos();
        controladorDeTurno = new ControladorDeTurno(jogador, gerenciadorDeEventos);

        maca = new Alimento("Maçã Vermelha", 0.2, 1, 20, 5, "Fruta", "item_maca");
        aguaPura = new Agua("Cantil de Água Pura", 0.5, 3, 30, "Potável", 0.5, "item_agua_pura");
        machadoSimples = new Ferramenta("Machadinha de Pedra", 1.5, 10, "Corte", 5, "item_machado_pedra");
        espadaCurta = new Arma("Adaga Enferrujada", 1.0, 15, "Lâmina Curta", 8, 1, "item_adaga");
        arcoSimples = new Arma("Arco Curto Gasto", 1.2, 20, "À Distância", 6, 15, "item_arco_simples");
        bandagem = new Remedio("Tiras de Pano Limpas", 0.1, "Bandagem", "Cura ferimentos leves.", 15, "vida", "item_bandagem");
        madeira = new Material("Gravetos Secos", 0.5, "Madeira", 2, "item_gravetos");
        pedraAfiada = new Material("Pedra Lascada Afiada", 0.4, "Mineral", 3, "item_pedra_afiada");

        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);
        outputArea.setFont(Font.font("Monospaced", 12));
        outputArea.setPrefHeight(340);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        layoutPrincipalControles = new VBox(10);
        layoutPrincipalControles.setPadding(new Insets(5, 0, 5, 0));

        Label lblItens = new Label("Adicionar Itens (Debug):");
        itensBox = new HBox(5);
        Button btnAdicionarMaca = new Button("Add Maçã");
        Button btnAdicionarAgua = new Button("Add Água");
        Button btnAdicionarMachado = new Button("Add Machado");
        Button btnAdicionarEspada = new Button("Add Adaga");
        Button btnAdicionarArco = new Button("Add Arco");
        Button btnAdicionarBandagem = new Button("Add Bandagem");
        Button btnAdicionarMadeira = new Button("Add Gravetos");
        Button btnAdicionarPedra = new Button("Add Pedra Afiada");
        itensBox.getChildren().addAll(btnAdicionarMaca, btnAdicionarAgua, btnAdicionarMachado, btnAdicionarEspada, btnAdicionarArco, btnAdicionarBandagem, btnAdicionarMadeira, btnAdicionarPedra);

        Label lblUsarItem = new Label("Usar Item:");
        usarItemBox = new HBox(5);
        TextField nomeItemParaUsar = new TextField();
        nomeItemParaUsar.setPromptText("Nome do item");
        nomeItemParaUsar.setPrefWidth(150);
        Button btnUsarItem = new Button("Usar");
        usarItemBox.getChildren().addAll(nomeItemParaUsar, btnUsarItem);

        Label lblEquiparArma = new Label("Equipar Arma:");
        equiparArmaBox = new HBox(5);
        TextField txtNomeArmaParaEquipar = new TextField();
        txtNomeArmaParaEquipar.setPromptText("Nome da arma");
        txtNomeArmaParaEquipar.setPrefWidth(180);
        Button btnEquiparArma = new Button("Equipar");
        Button btnDesequiparArma = new Button("Desequipar");
        equiparArmaBox.getChildren().addAll(txtNomeArmaParaEquipar, btnEquiparArma, btnDesequiparArma);

        Label lblFabricacao = new Label("Habilidades de Classe:");
        fabricacaoBox = new HBox(5);
        btnFabricarFaca = new Button("Fabricar Faca de Pedra (Mecânico)");
        fabricacaoBox.getChildren().add(btnFabricarFaca);


        Label lblAcoesGerais = new Label("Ações/Info:");
        acoesInfoBox = new HBox(5);
        Button btnListarInventario = new Button("Inventário");
        Button btnStatusJogador = new Button("Status de " + jogador.getNome());
        Button btnExplorar = new Button("Explorar");
        Button btnDescansar = new Button("Descansar");
        acoesInfoBox.getChildren().addAll(btnListarInventario, btnStatusJogador, btnExplorar, btnDescansar);

        Label lblMovimento = new Label("Mover para:");
        painelMovimento = new VBox(5);

        Label lblTurno = new Label("Jogo:");
        turnoBox = new HBox(5);
        Button btnProximoTurno = new Button("Próximo Turno");
        this.btnNovoJogo = new Button("Novo Jogo"); // <<< btnNovoJogo agora é atribuído ao campo da classe
        turnoBox.getChildren().addAll(btnProximoTurno, this.btnNovoJogo);

        painelAcoesConfronto = new VBox(10);
        painelAcoesConfronto.setAlignment(Pos.CENTER);
        Label lblTituloConfronto = new Label("EM CONFRONTO!");
        lblTituloConfronto.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        btnLutar = new Button("Lutar com Habilidade/Arma");
        btnFugir = new Button("Tentar Fugir");
        HBox botoesLutaFuga = new HBox(10, btnLutar, btnFugir);
        botoesLutaFuga.setAlignment(Pos.CENTER);
        painelAcoesConfronto.getChildren().addAll(lblTituloConfronto, botoesLutaFuga);

        layoutPrincipalControles.getChildren().addAll(lblItens, itensBox, lblUsarItem, usarItemBox, lblEquiparArma, equiparArmaBox, lblFabricacao, fabricacaoBox, lblAcoesGerais, acoesInfoBox, lblMovimento, painelMovimento, lblTurno, turnoBox, painelAcoesConfronto);
        setVisibilidadePainelConfronto(false);
        atualizarVisibilidadePainelFabricacao();


        btnAdicionarMaca.setOnAction(e -> executarAcaoNormal(() -> adicionarItemAoInventario(maca, "Maçã Vermelha")));
        btnAdicionarAgua.setOnAction(e -> executarAcaoNormal(() -> adicionarItemAoInventario(aguaPura, "Cantil de Água Pura")));
        btnAdicionarMachado.setOnAction(e -> executarAcaoNormal(() -> adicionarItemAoInventario(machadoSimples, "Machadinha de Pedra")));
        btnAdicionarEspada.setOnAction(e -> executarAcaoNormal(() -> adicionarItemAoInventario(espadaCurta, "Adaga Enferrujada")));
        btnAdicionarArco.setOnAction(e -> executarAcaoNormal(() -> adicionarItemAoInventario(arcoSimples, "Arco Curto Gasto")));
        btnAdicionarBandagem.setOnAction(e -> executarAcaoNormal(() -> adicionarItemAoInventario(bandagem, "Tiras de Pano Limpas")));
        btnAdicionarMadeira.setOnAction(e -> executarAcaoNormal(() -> adicionarItemAoInventario(madeira, "Gravetos Secos")));
        btnAdicionarPedra.setOnAction(e -> executarAcaoNormal(() -> adicionarItemAoInventario(pedraAfiada, "Pedra Lascada Afiada")));

        btnUsarItem.setOnAction(e -> executarAcaoNormal(() -> {
            String nomeItem = nomeItemParaUsar.getText();
            if (nomeItem != null && !nomeItem.trim().isEmpty()) {
                Optional<Item> itemOpt = jogador.getInventario().buscarItemPorNome(nomeItem);
                if (itemOpt.isPresent()) {
                    Item itemReal = itemOpt.get();
                    if (emConfronto && !(itemReal instanceof Remedio || itemReal instanceof Arma)) {
                        atualizarOutput("Você não pode usar '" + nomeItem + "' durante um confronto, a menos que seja um remédio ou para equipar uma arma.");
                        return;
                    }
                    atualizarOutput("AÇÃO: Usando item '" + nomeItem + "'");
                    itemReal.usar(jogador);
                } else {
                    atualizarOutput("Item '" + nomeItem + "' não encontrado no inventário.");
                }
                exibirStatusJogador();
                exibirInventario();
                nomeItemParaUsar.clear();
            } else {
                atualizarOutput("Por favor, digite o nome de um item para usar.");
            }
        }));

        btnEquiparArma.setOnAction(e -> executarAcaoNormal(() -> {
            String nomeArma = txtNomeArmaParaEquipar.getText();
            if (nomeArma != null && !nomeArma.trim().isEmpty()) {
                String resultadoEquipar = jogador.equiparArma(nomeArma);
                atualizarOutput(resultadoEquipar);
                txtNomeArmaParaEquipar.clear();
                exibirStatusJogador();
            } else {
                atualizarOutput("Digite o nome da arma do inventário para equipar.");
            }
        }));
        btnDesequiparArma.setOnAction(e -> executarAcaoNormal(() -> {
            String resultadoDesequipar = jogador.desequiparArma();
            atualizarOutput(resultadoDesequipar);
            exibirStatusJogador();
        }));

        btnFabricarFaca.setOnAction(e -> executarAcaoNormal(this::tentarFabricarFacaDePedra));
        btnListarInventario.setOnAction(e -> executarAcaoNormal(this::exibirInventario));
        btnStatusJogador.setOnAction(e -> executarAcaoNormal(this::exibirStatusJogador));
        btnExplorar.setOnAction(e -> executarAcaoNormal(this::processarExploracao));
        btnDescansar.setOnAction(e -> executarAcaoNormal(this::processarDescanso));
        btnProximoTurno.setOnAction(e -> processarProximoTurno());
        this.btnNovoJogo.setOnAction(e -> mostrarCenaCriacaoPersonagem()); // <<< btnNovoJogo agora é referenciado como campo


        btnLutar.setOnAction(e -> processarLuta());
        btnFugir.setOnAction(e -> processarFuga());

        root.setTop(layoutPrincipalControles);
        root.setCenter(outputArea);
        Scene cenaPrincipal = new Scene(root, 850, 800);
        primaryStage.setScene(cenaPrincipal);
        primaryStage.centerOnScreen();

        atualizarBotoesDeMovimento();
        exibirStatusJogador();
        exibirInventario();
        atualizarOutput("Bem-vindo ao Última Fronteira, " + jogador.getNome() + "! Turno: " + controladorDeTurno.getNumeroDoTurno());
        atualizarLocalizacaoNaTela();
    }

    private void processarDescanso() {
        atualizarOutput("--- AÇÃO: Descansar ---");
        int energiaRecuperada = 15 + random.nextInt(11);
        int fomePerdida = 5 + random.nextInt(6);
        int sedePerdida = 5 + random.nextInt(6);
        int sanidadeRecuperadaLeve = 2 + random.nextInt(4);

        jogador.setEnergia(jogador.getEnergia() + energiaRecuperada);
        jogador.setFome(Math.max(0, jogador.getFome() - fomePerdida));
        jogador.setSede(Math.max(0, jogador.getSede() - sedePerdida));
        jogador.setSanidade(jogador.getSanidade() + sanidadeRecuperadaLeve);

        atualizarOutput("Você descansa um pouco...");
        atualizarOutput("Energia recuperada: +" + energiaRecuperada);
        atualizarOutput("Sanidade recuperada: +" + sanidadeRecuperadaLeve);
        atualizarOutput("Fome aumentou em: " + fomePerdida);
        atualizarOutput("Sede aumentou em: " + sedePerdida);
        exibirStatusJogador();
    }


    private void tentarFabricarFacaDePedra() {
        if (jogador.getClassePersonagem() != ClassePersonagem.MECANICO || !jogador.temHabilidade("Fabricar Ferramentas Básicas")) {
            atualizarOutput("Apenas Mecânicos com a habilidade 'Fabricar Ferramentas Básicas' podem fazer isso.");
            return;
        }
        Optional<Item> gravetosOpt = jogador.getInventario().buscarItemPorNome("Gravetos Secos");
        Optional<Item> pedraOpt = jogador.getInventario().buscarItemPorNome("Pedra Lascada Afiada");

        boolean temMadeira = gravetosOpt.isPresent() && gravetosOpt.get().getQuantidade() >= 1;
        boolean temPedra = pedraOpt.isPresent() && pedraOpt.get().getQuantidade() >= 1;

        if (temMadeira && temPedra) {
            jogador.getInventario().removerItem("Gravetos Secos", 1);
            jogador.getInventario().removerItem("Pedra Lascada Afiada", 1);
            Ferramenta facaDePedra = new Ferramenta("Faca de Pedra Improvisada", 0.3, 8, "Corte/Defesa Leve", 4, "item_faca_pedra");
            jogador.getInventario().adicionarItem(facaDePedra);
            atualizarOutput(jogador.getNome() + " usou sua engenhosidade e fabricou uma Faca de Pedra Improvisada!");
            exibirInventario();
        } else {
            String faltando = "";
            if (!temMadeira) faltando += "Gravetos Secos (mín. 1) ";
            if (!temPedra) faltando += "Pedra Lascada Afiada (mín. 1) ";
            atualizarOutput("Materiais insuficientes para fabricar Faca de Pedra. Falta: " + faltando.trim().replace(" ", ", "));
        }
    }

    private void processarExploracao() {
        atualizarOutput("--- AÇÃO: Explorar Ambiente ---");
        String resultadoExploracao = jogador.explorarAmbiente(gerenciadorDeEventos, controladorDeTurno.getNumeroDoTurno());
        atualizarOutput(resultadoExploracao);
        verificarEventosAposAcao();
    }

    private void processarProximoTurno() {
        if (controladorDeTurno.isJogoTerminou()) {
            finalizarJogoSeNecessario();
            return;
        }

        atualizarOutput("\n> O tempo avança...\n");

        if (eventoClimaticoAtivo != null) {
            eventoClimaticoAtivo.decrementarDuracao();
            if (eventoClimaticoAtivo.getDuracao() <= 0) {
                atualizarOutput("O tempo voltou ao normal. " + eventoClimaticoAtivo.getNome() + " terminou.");
                eventoClimaticoAtivo = null;
            } else {
                atualizarOutput("Condição climática ativa: " + eventoClimaticoAtivo.getNome() + " (Duração restante: " + eventoClimaticoAtivo.getDuracao() + " turnos).");
            }
        }

        String logDoTurno = controladorDeTurno.executarProximoTurno();
        atualizarOutput(logDoTurno);

        verificarEventosAposAcao();

        if (!emConfronto && !controladorDeTurno.isJogoTerminou()) {
            atualizarOutput("======================================================");
            exibirStatusJogador();
            exibirInventario();
            atualizarBotoesDeMovimento();
            atualizarLocalizacaoNaTela();
        }
        finalizarJogoSeNecessario();
    }

    private void verificarEventosAposAcao() {
        if (controladorDeTurno != null && controladorDeTurno.isJogoTerminou() && !emConfronto) {
            finalizarJogoSeNecessario();
            return;
        }

        Evento eventoSorteado = gerenciadorDeEventos.getEventoSorteadoAtual();
        if (eventoSorteado != null) {
            if (eventoSorteado instanceof EventoCriatura) {
                this.eventoConfrontoAtual = (EventoCriatura) eventoSorteado;
                entrarModoConfronto();
            } else if (eventoSorteado instanceof EventoClimatico) {
                if (this.eventoClimaticoAtivo == null || !this.eventoClimaticoAtivo.getNome().equals(eventoSorteado.getNome())) {
                    this.eventoClimaticoAtivo = (EventoClimatico) eventoSorteado;
                }
            }
            gerenciadorDeEventos.limparEventoSorteadoAtual();
        }

        if (!emConfronto && (controladorDeTurno != null && !controladorDeTurno.isJogoTerminou()) ) {
            exibirStatusJogador();
            exibirInventario();
            atualizarLocalizacaoNaTela();
            atualizarBotoesDeMovimento();
        }
        finalizarJogoSeNecessario();
    }


    private void setVisibilidadePaineisNormais(boolean visivel) {
        itensBox.setVisible(visivel); itensBox.setManaged(visivel);
        usarItemBox.setVisible(visivel); usarItemBox.setManaged(visivel);
        equiparArmaBox.setVisible(visivel); equiparArmaBox.setManaged(visivel);

        if (!visivel) {
            fabricacaoBox.setVisible(false); fabricacaoBox.setManaged(false);
        } else {
            atualizarVisibilidadePainelFabricacao();
        }

        acoesInfoBox.setVisible(visivel); acoesInfoBox.setManaged(visivel);
        painelMovimento.setVisible(visivel); painelMovimento.setManaged(visivel);
        turnoBox.setVisible(visivel); turnoBox.setManaged(visivel);
    }

    private void atualizarVisibilidadePainelFabricacao() {
        boolean podeFabricar = jogador != null &&
                jogador.getClassePersonagem() == ClassePersonagem.MECANICO &&
                jogador.temHabilidade("Fabricar Ferramentas Básicas") &&
                !emConfronto &&
                (controladorDeTurno != null && !controladorDeTurno.isJogoTerminou());
        fabricacaoBox.setVisible(podeFabricar);
        fabricacaoBox.setManaged(podeFabricar);
    }

    private void setVisibilidadePainelConfronto(boolean visivel) {
        painelAcoesConfronto.setVisible(visivel);
        painelAcoesConfronto.setManaged(visivel);
    }

    private void entrarModoConfronto() {
        emConfronto = true;
        setVisibilidadePaineisNormais(false);
        setVisibilidadePainelConfronto(true);
        atualizarBotoesDeMovimento();
        atualizarOutput(">>> MODO DE CONFRONTO ATIVADO: " + eventoConfrontoAtual.getTipoDeCriatura() + " (PV: " + eventoConfrontoAtual.getVidaAtualCriatura() + ") <<<");
    }

    private void sairModoConfronto(String mensagemResultado) {
        emConfronto = false;
        if (mensagemResultado != null && !mensagemResultado.isEmpty()) {
            atualizarOutput(mensagemResultado);
        }
        if (eventoConfrontoAtual != null) {
            atualizarOutput(">>> MODO DE CONFRONTO DESATIVADO <<<");
        }
        eventoConfrontoAtual = null;
        gerenciadorDeEventos.limparEventoSorteadoAtual();

        setVisibilidadePaineisNormais(true);
        setVisibilidadePainelConfronto(false);
        atualizarBotoesDeMovimento();

        exibirStatusJogador();
        exibirInventario();
        finalizarJogoSeNecessario();
    }

    private void processarLuta() {
        if (!emConfronto || eventoConfrontoAtual == null || jogador.getVida() <= 0) return;

        StringBuilder sbLuta = new StringBuilder();
        sbLuta.append("--- Rodada de Combate (Turno ").append(controladorDeTurno.getNumeroDoTurno()).append(") ---\n");
        int danoJogador = jogador.calcularDanoBase();
        String tipoAtaqueJogador = "com " + (jogador.getArmaEquipada() != null ? jogador.getArmaEquipada().getNome() : "as próprias mãos");

        if (jogador.getClassePersonagem() == ClassePersonagem.MECANICO && jogador.getArmaEquipada() != null && jogador.temHabilidade("Engenhosidade")) {
            int custoEnergiaHabilidade = 5;
            if (jogador.getEnergia() >= custoEnergiaHabilidade) {
                jogador.setEnergia(jogador.getEnergia() - custoEnergiaHabilidade);
                danoJogador += 3 + (controladorDeTurno.getNumeroDoTurno() / 6);
                tipoAtaqueJogador = "um golpe engenhoso com sua " + jogador.getArmaEquipada().getNome() + " (-" + custoEnergiaHabilidade + " Energia)";
            } else {
                sbLuta.append(jogador.getNome()).append(" tenta um golpe engenhoso, mas falta energia!\n");
            }
        } else if (jogador.getClassePersonagem() == ClassePersonagem.SOBREVIVENTE_NATO && jogador.getArmaEquipada() != null) {
            danoJogador += 1 + (controladorDeTurno.getNumeroDoTurno() / 7);
            tipoAtaqueJogador = "um ataque resiliente com " + jogador.getArmaEquipada().getNome();
        }

        sbLuta.append(jogador.getNome()).append(" ataca ").append(eventoConfrontoAtual.getTipoDeCriatura()).append(" ").append(tipoAtaqueJogador).append("!\n");
        sbLuta.append(eventoConfrontoAtual.receberDano(danoJogador));

        if (eventoConfrontoAtual.getVidaAtualCriatura() <= 0) {
            atualizarOutput(sbLuta.toString());
            sairModoConfronto(eventoConfrontoAtual.getTipoDeCriatura() + " foi derrotado!");
            // TODO: Adicionar recompensas/loot aqui
            return;
        }

        atualizarOutput(sbLuta.toString());

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            if (!emConfronto || eventoConfrontoAtual == null || eventoConfrontoAtual.getVidaAtualCriatura() <=0) return;

                            StringBuilder sbRetaliacao = new StringBuilder();
                            int danoCriatura = eventoConfrontoAtual.calcularDanoEfetivoCriatura(controladorDeTurno.getNumeroDoTurno());
                            sbRetaliacao.append(eventoConfrontoAtual.getTipoDeCriatura()).append(" revida e ataca ").append(jogador.getNome()).append(" causando ").append(danoCriatura).append(" de dano!\n");
                            jogador.setVida(jogador.getVida() - danoCriatura);
                            atualizarOutput(sbRetaliacao.toString());
                            exibirStatusJogador();

                            if (jogador.getVida() <= 0) {
                                sairModoConfronto(jogador.getNome() + " foi derrotado pelo " + eventoConfrontoAtual.getTipoDeCriatura() + ".");
                            } else {
                                atualizarOutput("O " + eventoConfrontoAtual.getTipoDeCriatura() + " ainda tem " + eventoConfrontoAtual.getVidaAtualCriatura() + " PV. O confronto continua.");
                            }
                        });
                    }
                },
                1500
        );
    }

    private void processarFuga() {
        if (!emConfronto || eventoConfrontoAtual == null || jogador.getVida() <= 0) return;
        StringBuilder sbFuga = new StringBuilder();
        sbFuga.append(jogador.getNome()).append(" tenta fugir do ").append(eventoConfrontoAtual.getTipoDeCriatura()).append("...\n");
        double chanceDeFuga = 0.35;
        if (jogador.temHabilidade("Movimentação Silenciosa") || jogador.getClassePersonagem() == ClassePersonagem.RASTREADOR) {
            chanceDeFuga += 0.25;
        }
        chanceDeFuga = Math.min(chanceDeFuga, 0.85);

        if (random.nextDouble() < chanceDeFuga) {
            sbFuga.append("Você conseguiu escapar!\n");
            atualizarOutput(sbFuga.toString());
            sairModoConfronto("");
        } else {
            sbFuga.append("A fuga falhou! O ").append(eventoConfrontoAtual.getTipoDeCriatura()).append(" ataca enquanto você se vira!\n");

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(() -> {
                                if (!emConfronto || eventoConfrontoAtual == null) return;

                                StringBuilder sbRetaliacaoFuga = new StringBuilder();
                                int danoCriatura = eventoConfrontoAtual.calcularDanoEfetivoCriatura(controladorDeTurno.getNumeroDoTurno());
                                jogador.setVida(jogador.getVida() - danoCriatura);
                                sbRetaliacaoFuga.append(jogador.getNome()).append(" recebeu ").append(danoCriatura).append(" de dano na tentativa de fuga.\n");

                                atualizarOutput(sbFuga.toString() + sbRetaliacaoFuga.toString());
                                exibirStatusJogador();

                                if (jogador.getVida() <= 0) {
                                    sairModoConfronto(jogador.getNome() + " foi derrotado ao tentar fugir do " + eventoConfrontoAtual.getTipoDeCriatura() + ".");
                                } else {
                                    atualizarOutput("O " + eventoConfrontoAtual.getTipoDeCriatura() + " ainda tem " + eventoConfrontoAtual.getVidaAtualCriatura() + " PV. O confronto continua.");
                                }
                            });
                        }
                    },
                    1000
            );
        }
    }


    private void executarAcaoNormal(Runnable acao) {
        if (controladorDeTurno != null && controladorDeTurno.isJogoTerminou()) {
            if (!emConfronto) {
                atualizarOutput("O jogo já terminou. Nenhuma ação adicional permitida.");
                if (controladorDeTurno.getMensagemFimDeJogo() != null && !controladorDeTurno.getMensagemFimDeJogo().isEmpty()) {
                    atualizarOutput(controladorDeTurno.getMensagemFimDeJogo());
                }
            }
            desabilitarTodosOsControles();
            return;
        }
        if (emConfronto) {
            atualizarOutput("Você está em confronto! Use as ações de Lutar ou Fugir.");
            return;
        }
        acao.run();
        verificarEventosAposAcao();
        finalizarJogoSeNecessario();
    }

    private void desabilitarTodosOsControles() {
        if (layoutPrincipalControles == null) return;
        setVisibilidadePaineisNormais(false);
        setVisibilidadePainelConfronto(false);
        layoutPrincipalControles.getChildren().forEach(node -> node.setDisable(true));
        if (this.btnNovoJogo != null) this.btnNovoJogo.setDisable(false); // <<< Usa this.btnNovoJogo

        outputArea.appendText("\n--- JOGO TERMINADO. CONTROLES DESABILITADOS (EXCETO NOVO JOGO) ---");
    }

    private void finalizarJogoSeNecessario() {
        if (controladorDeTurno != null && controladorDeTurno.isJogoTerminou()) {
            if (emConfronto) {
                sairModoConfronto(controladorDeTurno.getMensagemFimDeJogo());
            }
            if (outputArea != null && !outputArea.getText().contains(controladorDeTurno.getMensagemFimDeJogo())) {
                atualizarOutput("\n" + controladorDeTurno.getMensagemFimDeJogo());
            }
            desabilitarTodosOsControles();

            String mensagemFinal = controladorDeTurno.getMensagemFimDeJogo();
            Platform.runLater(() -> {
                if (mensagemFinal.contains("VITÓRIA")) {
                    mostrarPopupDeVitoria(mensagemFinal);
                } else {
                    mostrarPopupDeDerrota(mensagemFinal);
                }
            });
        }
    }


    private void mostrarPopupDeDerrota(String conteudo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fim de Jogo");
        alert.setHeaderText("Você Não Sobreviveu...");
        alert.setContentText(conteudo.replace("FIM DE JOGO - DERROTA", "").trim() + "\n\nQue tal tentar novamente?");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #402020; -fx-border-color: #703030; -fx-border-width: 1;");
        Node headerPanel = dialogPane.lookup(".header-panel");
        if (headerPanel instanceof Label) {
            ((Label)headerPanel).setStyle("-fx-text-fill: #FFC0C0; -fx-font-weight: bold; -fx-font-size: 1.5em;");
        } else if (headerPanel != null) {
            Node headerLabel = headerPanel.lookup(".label");
            if (headerLabel instanceof Label) {
                ((Label)headerLabel).setStyle("-fx-text-fill: #FFC0C0; -fx-font-weight: bold; -fx-font-size: 1.5em;");
            }
        }
        Node contentLabel = dialogPane.lookup(".content.label");
        if (contentLabel instanceof Label) {
            ((Label)contentLabel).setStyle("-fx-text-fill: white; -fx-font-size: 1.1em;");
        }
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();
    }

    private void mostrarPopupDeVitoria(String conteudo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fim de Jogo");
        alert.setHeaderText("Parabéns, Você Sobreviveu!");
        alert.setContentText(conteudo.replace("FIM DE JOGO - VITÓRIA!", "").trim() + "\n\nVocê demonstrou grande resiliência!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #204020; -fx-border-color: #307030; -fx-border-width: 1;");
        Node headerPanel = dialogPane.lookup(".header-panel");
        if (headerPanel instanceof Label) {
            ((Label)headerPanel).setStyle("-fx-text-fill: #C0FFC0; -fx-font-weight: bold; -fx-font-size: 1.5em;");
        } else if (headerPanel != null) {
            Node headerLabel = headerPanel.lookup(".label");
            if (headerLabel instanceof Label) {
                ((Label)headerLabel).setStyle("-fx-text-fill: #C0FFC0; -fx-font-weight: bold; -fx-font-size: 1.5em;");
            }
        }
        Node contentLabel = dialogPane.lookup(".content.label");
        if (contentLabel instanceof Label) {
            ((Label)contentLabel).setStyle("-fx-text-fill: white; -fx-font-size: 1.1em;");
        }
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();
    }


    private void atualizarLocalizacaoNaTela() {
        if (jogador != null && jogador.getLocalizacaoAtual() != null && outputArea != null) {
            atualizarOutput("Você está em: " + jogador.getLocalizacaoAtual().getNome() + " (" + jogador.getLocalizacaoAtual().getDescricao() + ")");
        }
    }

    private void atualizarBotoesDeMovimento() {
        if (painelMovimento == null) return;
        painelMovimento.getChildren().clear();
        boolean desabilitar = emConfronto || (controladorDeTurno != null && controladorDeTurno.isJogoTerminou());

        if (jogador == null || jogador.getLocalizacaoAtual() == null || gerenciadorDeAmbientes == null) {
            if (!emConfronto && (controladorDeTurno == null || !controladorDeTurno.isJogoTerminou())) {
                painelMovimento.getChildren().add(new Label("Movimentação indisponível no momento."));
            }
            painelMovimento.setDisable(true);
            return;
        }
        painelMovimento.setDisable(desabilitar);

        if (desabilitar) {
            if (emConfronto) painelMovimento.getChildren().add(new Label("Movimentação bloqueada durante confronto."));
            else if (controladorDeTurno != null && controladorDeTurno.isJogoTerminou()) painelMovimento.getChildren().add(new Label("O jogo terminou."));
            return;
        }

        List<Ambiente> adjacentes = gerenciadorDeAmbientes.getAmbientesAdjacentes(jogador.getLocalizacaoAtual().getNome());
        if (adjacentes.isEmpty()) {
            painelMovimento.getChildren().add(new Label("Nenhum lugar para ir daqui."));
        } else {
            for (Ambiente destino : adjacentes) {
                Button btnMover = new Button("Ir para " + destino.getNome());
                btnMover.setOnAction(e -> {
                    if (emConfronto || (controladorDeTurno != null && controladorDeTurno.isJogoTerminou())) return;

                    int custoEnergiaMovimento = 15;
                    if (jogador.getEnergia() >= custoEnergiaMovimento) {
                        atualizarOutput("--- AÇÃO: Mover para " + destino.getNome() + " ---");
                        boolean moveu = gerenciadorDeAmbientes.mudarAmbienteAtualDoJogador(jogador, destino.getNome());
                        if (moveu) {
                            jogador.setEnergia(jogador.getEnergia() - custoEnergiaMovimento);

                            verificarEventosAposAcao();

                            if (!emConfronto && (controladorDeTurno != null && !controladorDeTurno.isJogoTerminou()) ) {
                                atualizarOutput("Você chegou em " + destino.getNome() + ".");
                                atualizarOutput("Energia gasta no movimento: " + custoEnergiaMovimento);
                                atualizarOutput("------------------------------------------------------");
                                atualizarBotoesDeMovimento();
                                exibirStatusJogador();
                                atualizarLocalizacaoNaTela();
                            }
                        } else {
                            atualizarOutput("Não foi possível mover para " + destino.getNome() + ".");
                        }
                    } else {
                        atualizarOutput("Energia insuficiente para mover para " + destino.getNome() + ". Você tem " + jogador.getEnergia() + ", precisa de " + custoEnergiaMovimento + ".");
                    }
                    finalizarJogoSeNecessario();
                });
                painelMovimento.getChildren().add(btnMover);
            }
        }
    }


    private void adicionarItemAoInventario(Item itemOriginal, String nomeAmigavel) {
        if (itemOriginal == null) {
            atualizarOutput("Tentativa de adicionar um item nulo (" + nomeAmigavel + "). Operação cancelada.");
            return;
        }
        Item itemParaAdicionar = duplicarItemParaAdicao(itemOriginal);

        if (itemParaAdicionar == null) {
            atualizarOutput("Erro ao tentar duplicar " + nomeAmigavel + " para o inventário.");
            return;
        }

        if (jogador.getInventario().adicionarItem(itemParaAdicionar)) {
            atualizarOutput(nomeAmigavel + " (x" + itemParaAdicionar.getQuantidade() + ") adicionado(a) ao inventário.");
        } else {
            atualizarOutput("Inventário cheio ou item inválido. Não foi possível adicionar " + nomeAmigavel + ".");
        }
        exibirInventario();
    }

    private Item duplicarItemParaAdicao(Item original) {
        if (original == null) return null;
        String nome = original.getNome();
        double peso = original.getPesoUnitario();
        int durab = original.getDurabilidadeOriginal();
        String chaveImg = original.getChaveImagem();

        if (original instanceof Alimento) {
            Alimento o = (Alimento) original;
            return new Alimento(nome, peso, durab, o.getValorNutricional(), o.getValorCura(), o.getTipo(), chaveImg);
        } else if (original instanceof Agua) {
            Agua o = (Agua) original;
            return new Agua(nome, peso, durab, o.getValorHidratacao(), o.getPureza(), o.getVolume(), chaveImg);
        } else if (original instanceof Ferramenta) {
            Ferramenta o = (Ferramenta) original;
            return new Ferramenta(nome, peso, durab, o.getTipo(), o.getEficiencia(), chaveImg);
        } else if (original instanceof Arma) {
            Arma o = (Arma) original;
            return new Arma(nome, peso, durab, o.getTipoArma(), o.getDano(), o.getAlcance(), chaveImg);
        } else if (original instanceof Remedio) {
            Remedio o = (Remedio) original;
            return new Remedio(nome, peso, o.getTipo(), o.getEfeitoDescritivo(), o.getValorEfeito(), o.getAtributoAlvo(), chaveImg);
        } else if (original instanceof Material) {
            Material o = (Material) original;
            return new Material(nome, peso, o.getTipo(), o.getResistencia(), chaveImg);
        }
        atualizarOutput("Atenção: Tentativa de duplicar item de tipo desconhecido: " + original.getClass().getSimpleName());
        return null;
    }


    private void atualizarOutput(String mensagem) {
        if (outputArea == null || mensagem == null || mensagem.trim().isEmpty()) return;
        Platform.runLater(() -> {
            outputArea.appendText(mensagem + "\n");
            outputArea.positionCaret(outputArea.getLength());
        });
    }

    private void exibirStatusJogador() {
        if (controladorDeTurno == null || jogador == null || outputArea == null) return;
        String tituloStatus = String.format("===== STATUS DE %S (Turno: %d) ======", jogador.getNome().toUpperCase(), controladorDeTurno.getNumeroDoTurno());
        atualizarOutput("\n" + tituloStatus);
        atualizarOutput(jogador.getStatus());
        atualizarOutput("============================================");
    }

    private void exibirInventario() {
        if (jogador == null || jogador.getInventario() == null || outputArea == null) return;
        atualizarOutput("\n============== INVENTÁRIO ==============");
        Inventario inv = jogador.getInventario();
        if (inv.getItens().isEmpty()) {
            atualizarOutput(String.format("%-30s", "Vazio"));
        } else {
            atualizarOutput(String.format("%-25s | %-10s | %-5s | %s", "Item", "Peso Total", "Qtde", "Durab."));
            atualizarOutput("---------------------------------------------------------");
            for (Item item : inv.getItens()) {
                String durabStr = (item.getDurabilidadeOriginal() == -1) ?
                        "Inquebrável" :
                        (item.getDurabilidade() + "/" + item.getDurabilidadeOriginal());
                String qtdeStr = String.valueOf(item.getQuantidade());
                atualizarOutput(String.format("%-25s | %-10.2f | %-5s | %s", item.getNome(), item.getPeso(), qtdeStr, durabStr));
            }
            atualizarOutput("---------------------------------------------------------");
        }
        atualizarOutput(String.format("Peso Total Ocupado : %.2f / %.2f", inv.getPesoAtual(), inv.getPesoMaximo()));
        atualizarOutput("======================================");
    }

    public static void main(String[] args) {
        launch(args);
    }
}