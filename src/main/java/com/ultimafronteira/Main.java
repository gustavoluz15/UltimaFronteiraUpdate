package com.ultimafronteira;

<<<<<<< HEAD
import com.ultimafronteira.events.Evento;
import com.ultimafronteira.events.EventoCriatura;
import com.ultimafronteira.events.EventoClimatico;
import com.ultimafronteira.events.EventoDescoberta;
import com.ultimafronteira.events.GerenciadorDeEventos;
import com.ultimafronteira.gamecore.ControladorDeTurno;
import com.ultimafronteira.model.*;
import com.ultimafronteira.visual.GameView;
import com.ultimafronteira.visual.GerenciadorDeImagens;
import com.ultimafronteira.world.Ambiente;
import com.ultimafronteira.world.GerenciadorDeAmbientes;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;

public class Main extends Application {

    private Personagem jogador;
    private GerenciadorDeEventos gerenciadorDeEventos;
    private GerenciadorDeAmbientes gerenciadorDeAmbientes;
    private ControladorDeTurno controladorDeTurno;
    private EventoCriatura eventoConfrontoAtual = null;
    private EventoClimatico eventoClimaticoAtivo = null;
    private boolean emConfronto = false;
    private Random random = new Random();

    private GameView gameView;
    private Stage primaryStage;

    private final int VIDA_BASE_PADRAO = 85;
    private final int FOME_BASE_PADRAO = 75;
    private final int SEDE_BASE_PADRAO = 75;
    private final int ENERGIA_BASE_PADRAO = 90;
    private final int SANIDADE_BASE_PADRAO = 60;
    private final double PESO_INV_PADRAO = 20.0;

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
        TextField txtNome = new TextField("Nome do Personagem");
        txtNome.setPromptText("Digite o nome");
        txtNome.setPrefWidth(200);
        nomeBox.getChildren().addAll(lblNome, txtNome);
        HBox classeBox = new HBox(10);
        classeBox.setAlignment(Pos.CENTER_LEFT);
        Label lblClasse = new Label("Classe:");
        ComboBox<ClassePersonagem> cbClasse = new ComboBox<>();
        cbClasse.getItems().addAll(ClassePersonagem.values());
        cbClasse.setValue(ClassePersonagem.MECANICO);
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
=======
import com.ultimafronteira.model.*;
import com.ultimafronteira.world.Ambiente;
import com.ultimafronteira.world.GerenciadorDeAmbientes;
import com.ultimafronteira.events.Evento;
import com.ultimafronteira.events.EventoCriatura;
import com.ultimafronteira.events.GerenciadorDeEventos;
import com.ultimafronteira.gamecore.ControladorDeTurno;
import com.ultimafronteira.model.ClassePersonagem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Optional;

public class Main extends Application {

    private Stage primaryStage;
    private Personagem jogador;
    private TextArea outputArea;
    private GerenciadorDeEventos gerenciadorDeEventos;
    private GerenciadorDeAmbientes gerenciadorDeAmbientes;
    private ControladorDeTurno controladorDeTurno;

    private VBox layoutPrincipalControles;
    private HBox itensBox, usarItemBox, equiparArmaBox, acoesInfoBox, turnoBox, fabricacaoBox;
    private VBox painelMovimento;

    private VBox painelAcoesConfronto;
    private Button btnLutar;
    private Button btnFugir;
    private Button btnFabricarFaca;

    private EventoCriatura eventoConfrontoAtual = null;
    private boolean emConfronto = false;
    private Random random = new Random();

    private final int VIDA_BASE_PADRAO = 100;
    private final int FOME_BASE_PADRAO = 80;
    private final int SEDE_BASE_PADRAO = 80;
    private final int ENERGIA_BASE_PADRAO = 100;
    private final int SANIDADE_BASE_PADRAO = 70;
    private final double PESO_INV_PADRAO = 20.0;

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
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Última Fronteira - Criação de Personagem");
        mostrarCenaCriacaoPersonagem();
    }

    private void mostrarCenaCriacaoPersonagem() {
        VBox layoutCriacao = new VBox(15); layoutCriacao.setPadding(new Insets(25)); layoutCriacao.setAlignment(Pos.CENTER_LEFT);
        Label lblTitulo = new Label("Crie seu Personagem para Última Fronteira"); lblTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 20)); VBox.setMargin(lblTitulo, new Insets(0, 0, 20, 0));
        HBox nomeBox = new HBox(10); nomeBox.setAlignment(Pos.CENTER_LEFT); Label lblNome = new Label("Nome:"); TextField txtNome = new TextField("Nome do Personagem"); txtNome.setPromptText("Digite o nome"); txtNome.setPrefWidth(200); nomeBox.getChildren().addAll(lblNome, txtNome);
        HBox classeBox = new HBox(10); classeBox.setAlignment(Pos.CENTER_LEFT); Label lblClasse = new Label("Classe:"); ComboBox<ClassePersonagem> cbClasse = new ComboBox<>(); cbClasse.getItems().addAll(ClassePersonagem.values()); cbClasse.setValue(ClassePersonagem.MECANICO); classeBox.getChildren().addAll(lblClasse, cbClasse);
        VBox detalhesClasseBox = new VBox(10); detalhesClasseBox.setPadding(new Insets(10,0,10,0)); detalhesClasseBox.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-padding: 10;");
        Label lblDescricaoTitulo = new Label("Descrição da Classe:"); lblDescricaoTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 14)); Label lblDescricaoClasseValor = new Label(); lblDescricaoClasseValor.setWrapText(true); lblDescricaoClasseValor.setMaxWidth(350);
        Label lblAtributosTitulo = new Label("Bônus e Habilidades Chave:"); lblAtributosTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 14)); Label lblAtributosClasseValor = new Label(); lblAtributosClasseValor.setWrapText(true); lblAtributosClasseValor.setMaxWidth(350);
        detalhesClasseBox.getChildren().addAll(lblDescricaoTitulo, lblDescricaoClasseValor, lblAtributosTitulo, lblAtributosClasseValor);
        cbClasse.setOnAction(e -> exibirDetalhesDaClasseSelecionada(cbClasse.getValue(), lblDescricaoClasseValor, lblAtributosClasseValor));
        exibirDetalhesDaClasseSelecionada(cbClasse.getValue(), lblDescricaoClasseValor, lblAtributosClasseValor);
        Button btnIniciarJogo = new Button("Iniciar Aventura!"); btnIniciarJogo.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        btnIniciarJogo.setOnAction(e -> {
            String nomeJogador = txtNome.getText(); ClassePersonagem classeEscolhida = cbClasse.getValue();
            if (nomeJogador == null || nomeJogador.trim().isEmpty()) { mostrarAlerta("Nome Inválido", "Por favor, digite um nome para o seu personagem."); return; }
            if (classeEscolhida == null) { mostrarAlerta("Classe Inválida", "Por favor, escolha uma classe para o seu personagem."); return; }
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
            iniciarJogoPrincipal(nomeJogador, classeEscolhida);
        });
        VBox.setMargin(btnIniciarJogo, new Insets(20, 0, 0, 0));
        layoutCriacao.getChildren().addAll(lblTitulo, nomeBox, classeBox, detalhesClasseBox, btnIniciarJogo);
<<<<<<< HEAD
        Scene cenaCriacao = new Scene(layoutCriacao, 450, 520);
        primaryStage.setScene(cenaCriacao);
        primaryStage.show();
    }

    private void iniciarJogoPrincipal(String nomeJogador, ClassePersonagem classeEscolhida) {
        gerenciadorDeAmbientes = new GerenciadorDeAmbientes();
        Ambiente ambienteInicial = gerenciadorDeAmbientes.getAmbientePorNome("Floresta dos Sussurros");

        String chaveImagemPersonagem = "personagem_sobrevivente";
        switch (classeEscolhida) {
            case MECANICO: chaveImagemPersonagem = "personagem_mecanico"; break;
            case MEDICO: chaveImagemPersonagem = "personagem_medico"; break;
            case RASTREADOR: chaveImagemPersonagem = "personagem_rastreador"; break;
        }

        jogador = new Personagem(nomeJogador, classeEscolhida, VIDA_BASE_PADRAO, FOME_BASE_PADRAO, SEDE_BASE_PADRAO, ENERGIA_BASE_PADRAO, SANIDADE_BASE_PADRAO, ambienteInicial, PESO_INV_PADRAO, chaveImagemPersonagem);
        gerenciadorDeEventos = new GerenciadorDeEventos();
        controladorDeTurno = new ControladorDeTurno(jogador, gerenciadorDeEventos);

        gameView = new GameView(this::processarUsoDeItem);

        gameView.getBtnExplorar().setOnAction(e -> executarAcao(this::processarExploracao));
        gameView.getBtnProximoTurno().setOnAction(e -> executarAcao(this::processarProximoTurno));
        gameView.getBtnDescansar().setOnAction(e -> executarAcao(this::processarDescanso));
        gameView.getBtnNovoJogo().setOnAction(e -> mostrarCenaCriacaoPersonagem());

        primaryStage.setTitle("Última Fronteira");
        primaryStage.setScene(gameView.getScene());
        primaryStage.centerOnScreen();

        gameView.appendOutput("Bem-vindo ao Última Fronteira, " + jogador.getNome() + "!");
        atualizarTodaUI();
    }

    private void atualizarTodaUI() {
        if (gameView == null || jogador == null) return;

        EventoCriatura inimigo = emConfronto ? eventoConfrontoAtual : null;
        gameView.getCenaPane().desenharCena(jogador, inimigo, jogador.getLocalizacaoAtual(), eventoClimaticoAtivo);
        gameView.atualizarInventario(jogador.getInventario());
        atualizarBotoesDeMovimento();
        gameView.atualizarTurno(controladorDeTurno.getNumeroDoTurno());
        gameView.atualizarAmbienteAtual(jogador.getLocalizacaoAtual().getNome());
    }

    private void executarAcao(Runnable acao) {
        if (emConfronto) {
            return;
        }
        if (controladorDeTurno.isJogoTerminou()) {
            return;
        }
        gameView.limparLegenda();
        acao.run();

        if (controladorDeTurno.isJogoTerminou()) {
            finalizarJogo();
        }
    }

    private void processarProximoTurno() {
        gameView.limparLog();
        if (eventoClimaticoAtivo != null) {
            eventoClimaticoAtivo.decrementarDuracao();
            if (eventoClimaticoAtivo.getDuracao() <= 0) {
                gameView.appendOutput("O tempo voltou ao normal.");
                eventoClimaticoAtivo = null;
            }
        }
        String logDoTurno = controladorDeTurno.executarProximoTurno();
        gameView.appendOutput("> O tempo avança...\n" + logDoTurno);
        verificarEventos();
        if (!emConfronto) {
            atualizarTodaUI();
        }
    }

    private void processarExploracao() {
        String resultadoExploracao = jogador.explorarAmbiente(gerenciadorDeEventos, controladorDeTurno.getNumeroDoTurno());
        String[] linhas = resultadoExploracao.split("\n");
        exibirMensagem(linhas);

        verificarEventos();
        if (!emConfronto) {
            atualizarTodaUI();
        }
    }

    private void processarDescanso() {
        int energiaRecuperada = 10;
        int fomePerdida = 10;
        jogador.setEnergia(jogador.getEnergia() + energiaRecuperada);
        jogador.setFome(jogador.getFome() - fomePerdida);
        exibirMensagem("Você recuperou " + energiaRecuperada + " de energia.", "Sua fome aumentou em " + fomePerdida + ".");
        atualizarTodaUI();
    }

    private void processarUsoDeItem(Item item) {
        if (item == null || (emConfronto && !(item instanceof Remedio))) {
            exibirMensagem("Você não pode usar '" + (item != null ? item.getNome() : "") + "' agora.");
            return;
        }
        item.usar(jogador);
        exibirMensagem("Você usou: " + item.getNome());

        if (item.getDurabilidade() == 1 && !(item instanceof Arma)) {
            jogador.getInventario().removerItem(item.getNome());
        }
        atualizarTodaUI();
    }

    private void processarMovimento(Ambiente destino) {
        int custoEnergia = 15;
        if (jogador.getEnergia() >= custoEnergia) {
            jogador.setEnergia(jogador.getEnergia() - custoEnergia);
            gerenciadorDeAmbientes.mudarAmbienteAtualDoJogador(jogador, destino.getNome());
            exibirMensagem("Você viajou para " + destino.getNome() + ".");
            String resultadoEvento = gerenciadorDeEventos.sortearEExecutarEvento(jogador, destino, controladorDeTurno.getNumeroDoTurno());
            gameView.appendOutput(resultadoEvento);
            verificarEventos();
            if (!emConfronto) {
                atualizarTodaUI();
            }
        } else {
            exibirMensagem("Energia insuficiente para viajar.");
        }
    }

    private void atualizarBotoesDeMovimento() {
        if (gameView == null || gerenciadorDeAmbientes == null) return;
        List<Ambiente> destinos = gerenciadorDeAmbientes.getAmbientesAdjacentes(jogador.getLocalizacaoAtual().getNome());
        gameView.atualizarPainelMovimento(destinos, this::processarMovimento, emConfronto);
    }

    private void verificarEventos() {
        Evento eventoSorteado = gerenciadorDeEventos.getEventoSorteadoAtual();
        if (eventoSorteado != null) {
            if (eventoSorteado instanceof EventoCriatura) {
                this.eventoConfrontoAtual = (EventoCriatura) eventoSorteado;
                entrarModoConfronto();
            } else if (eventoSorteado instanceof EventoClimatico) {
                if (this.eventoClimaticoAtivo == null) {
                    this.eventoClimaticoAtivo = (EventoClimatico) eventoSorteado;
                    exibirMensagem(eventoSorteado.getNome(), eventoSorteado.getDescricao());
                }
            } else if (eventoSorteado instanceof EventoDescoberta) {
                exibirMensagem(eventoSorteado.getNome(), eventoSorteado.getDescricao());
            }
            gerenciadorDeEventos.limparEventoSorteadoAtual();
        }
    }

    private void entrarModoConfronto() {
        this.emConfronto = true;
        gameView.limparLegenda();
        atualizarTodaUI();
        gameView.mostrarControlesCombate(
                "Um " + eventoConfrontoAtual.getTipoDeCriatura() + " selvagem aparece!",
                this::processarAcaoCombate
        );
    }

    private void sairModoConfronto(String mensagem) {
        emConfronto = false;
        eventoConfrontoAtual = null;
        gameView.exibirMensagem(mensagem);
        gameView.esconderControlesCombate();
        atualizarTodaUI();
    }

    private void processarAcaoCombate(String acao) {
        if (!emConfronto) return;
        gameView.limparLegenda();
        if (acao.equals("lutar")) {
            processarLuta();
        } else if (acao.equals("fugir")) {
            processarFuga();
        }
    }

    private void processarLuta() {
        if (!emConfronto || eventoConfrontoAtual == null) return;

        int danoJogador = jogador.calcularDanoBase();
        String logAtaqueJogador = eventoConfrontoAtual.receberDano(danoJogador);

        exibirMensagem(jogador.getNome() + " ataca!", logAtaqueJogador);

        if (eventoConfrontoAtual.getVidaAtualCriatura() <= 0) {
            sairModoConfronto("Você venceu o combate!");
            // Adicionar recompensas aqui
            return;
        }

        int danoCriatura = eventoConfrontoAtual.calcularDanoEfetivoCriatura(controladorDeTurno.getNumeroDoTurno());
        jogador.setVida(jogador.getVida() - danoCriatura);

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            exibirMensagem("O inimigo revida!", "Você sofre " + danoCriatura + " de dano.");
                            atualizarTodaUI();
                            if (jogador.getVida() <= 0) {
                                controladorDeTurno.forcarFimDeJogo(jogador.getNome() + " foi derrotado em combate.");
                                finalizarJogo();
                            }
                        });
                    }
                },
                2000
        );
    }

    private void processarFuga() {
        if (!emConfronto) return;
        exibirMensagem("Você tenta fugir...");
        double chanceDeFuga = 0.5;
        if (random.nextDouble() < chanceDeFuga) {
            sairModoConfronto("Você conseguiu escapar!");
        } else {
            int danoCriatura = eventoConfrontoAtual.calcularDanoEfetivoCriatura(controladorDeTurno.getNumeroDoTurno());
            jogador.setVida(jogador.getVida() - danoCriatura);
            exibirMensagem("A fuga falhou!", "Você sofre " + danoCriatura + " de dano.");
            atualizarTodaUI();

            if (jogador.getVida() <= 0) {
                controladorDeTurno.forcarFimDeJogo(jogador.getNome() + " foi derrotado ao tentar fugir.");
                finalizarJogo();
            }
        }
    }

    private void exibirMensagem(String... mensagens) {
        gameView.exibirMensagem(mensagens);
    }

    private void finalizarJogo() {
        gameView.desabilitarAcoes();
        sairModoConfronto("");

        String mensagemFinal = controladorDeTurno.getMensagemFimDeJogo();

        Platform.runLater(() -> {
            if (mensagemFinal.contains("VITÓRIA")) {
                mostrarPopupDeVitoria(mensagemFinal);
            } else {
                mostrarPopupDeDerrota(mensagemFinal);
            }
        });
    }

    private void mostrarPopupDeDerrota(String conteudo) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Fim de Jogo");
        alert.setHeaderText("Você Morreu, Fim de Jogo!");
        alert.setContentText(conteudo.replace("FIM DE JOGO - DERROTA", "").trim());
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #5B2C2C; -fx-border-color: #a04040;");
        Label headerLabel = (Label) dialogPane.lookup(".header-panel .label");
        headerLabel.setStyle("-fx-text-fill: #FFA0A0; -fx-font-weight: bold; -fx-font-size: 1.5em;");
        dialogPane.lookupAll(".content.label").forEach(l -> ((Label)l).setTextFill(Color.WHITE));
        alert.getButtonTypes().add(ButtonType.OK);
        alert.showAndWait();
    }

    private void mostrarPopupDeVitoria(String conteudo) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Fim de Jogo");
        alert.setHeaderText("Parabéns, você sobreviveu!");
        alert.setContentText(conteudo.replace("FIM DE JOGO - VITÓRIA!", "").trim());
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #2C5B2C; -fx-border-color: #40a040;");
        Label headerLabel = (Label) dialogPane.lookup(".header-panel .label");
        headerLabel.setStyle("-fx-text-fill: #A0FFA0; -fx-font-weight: bold; -fx-font-size: 1.5em;");
        dialogPane.lookupAll(".content.label").forEach(l -> ((Label)l).setTextFill(Color.WHITE));
        alert.getButtonTypes().add(ButtonType.OK);
        alert.showAndWait();
    }

    private void exibirDetalhesDaClasseSelecionada(ClassePersonagem classe, Label lblDescricao, Label lblAtributos) {
        if (classe == null) return;
        lblDescricao.setText(classe.getDescricao());
        StringBuilder atributosStr = new StringBuilder();
        int vidaPreview = VIDA_BASE_PADRAO, energiaPreview = ENERGIA_BASE_PADRAO, sanidadePreview = SANIDADE_BASE_PADRAO, fomePreview = FOME_BASE_PADRAO, sedePreview = SEDE_BASE_PADRAO;
        List<String> habilidadesPreview = new ArrayList<>();
        switch (classe) {
            case RASTREADOR: fomePreview += 15; sedePreview += 10; habilidadesPreview.add("Rastreamento Aguçado"); habilidadesPreview.add("Conhecimento da Flora e Fauna"); habilidadesPreview.add("Movimentação Silenciosa"); atributosStr.append(String.format("Fome Inicial: ~%d\nSede Inicial: ~%d\n", fomePreview, sedePreview)); break;
            case MECANICO: energiaPreview += 10; habilidadesPreview.add("Engenhosidade"); habilidadesPreview.add("Fabricar Ferramentas Básicas"); habilidadesPreview.add("Desmontar Sucata"); atributosStr.append(String.format("Energia Inicial: ~%d\n", energiaPreview)); break;
            case MEDICO: sanidadePreview += 10; vidaPreview += 5; habilidadesPreview.add("Primeiros Socorros Avançados"); habilidadesPreview.add("Conhecimento de Ervas Medicinais"); habilidadesPreview.add("Resistência a Doenças Leves"); atributosStr.append(String.format("Sanidade Inicial: ~%d\nVida Inicial: ~%d\n", sanidadePreview, vidaPreview)); break;
            case SOBREVIVENTE_NATO: vidaPreview += 15; fomePreview += 25; sedePreview += 25; energiaPreview += 5; habilidadesPreview.add("Metabolismo Eficiente"); habilidadesPreview.add("Resiliência Climática"); habilidadesPreview.add("Instinto de Perigo"); atributosStr.append(String.format("Vida: ~%d\nFome: ~%d\nSede: ~%d\nEnergia: ~%d\n", vidaPreview, fomePreview, sedePreview, energiaPreview)); break;
        }
        if (!habilidadesPreview.isEmpty()) { atributosStr.append("Habilidades Chave: ").append(String.join(", ", habilidadesPreview)).append("\n"); }
=======
        Scene cenaCriacao = new Scene(layoutCriacao, 450, 520); primaryStage.setScene(cenaCriacao); primaryStage.show();
    }

    private void exibirDetalhesDaClasseSelecionada(ClassePersonagem classe, Label lblDescricao, Label lblAtributos) {
        if (classe == null) return; lblDescricao.setText(classe.getDescricao()); StringBuilder atributosStr = new StringBuilder();
        int vidaPreview = VIDA_BASE_PADRAO; int energiaPreview = ENERGIA_BASE_PADRAO; int sanidadePreview = SANIDADE_BASE_PADRAO; int fomePreview = FOME_BASE_PADRAO; int sedePreview = SEDE_BASE_PADRAO;
        List<String> habilidadesPreview = new ArrayList<>();
        switch (classe) {
            case RASTREADOR: fomePreview += 15; sedePreview +=10; habilidadesPreview.add("Rastreamento Aguçado"); habilidadesPreview.add("Conhecimento da Flora e Fauna"); habilidadesPreview.add("Movimentação Silenciosa");
                atributosStr.append(String.format("Fome Inicial: ~%d (+15)\nSede Inicial: ~%d (+10)\n", fomePreview, sedePreview)); break;
            case MECANICO: energiaPreview += 10; habilidadesPreview.add("Engenhosidade"); habilidadesPreview.add("Fabricar Ferramentas Básicas"); habilidadesPreview.add("Desmontar Sucata");
                atributosStr.append(String.format("Energia Inicial: ~%d (+10)\n", energiaPreview)); break;
            case MEDICO: sanidadePreview += 10; habilidadesPreview.add("Primeiros Socorros Avançados"); habilidadesPreview.add("Conhecimento de Ervas Medicinais"); habilidadesPreview.add("Resistência a Doenças Leves");
                atributosStr.append(String.format("Sanidade Inicial: ~%d (+10)\n", sanidadePreview)); break;
            case SOBREVIVENTE_NATO: vidaPreview += 15; fomePreview += 25; sedePreview += 25; energiaPreview += 5;
                habilidadesPreview.add("Metabolismo Eficiente"); habilidadesPreview.add("Resiliência Climática"); habilidadesPreview.add("Instinto de Perigo");
                atributosStr.append(String.format("Vida: ~%d (+15)\nFome: ~%d (+25)\nSede: ~%d (+25)\nEnergia: ~%d (+5)\n", vidaPreview, fomePreview, sedePreview, energiaPreview)); break;
        }
        if (!habilidadesPreview.isEmpty()){ atributosStr.append("Habilidades Chave: ").append(String.join(", ", habilidadesPreview)).append("\n");}
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
        lblAtributos.setText(atributosStr.toString());
    }

    private void mostrarAlerta(String titulo, String conteudo) {
<<<<<<< HEAD
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(conteudo);
        alert.showAndWait();
=======
        Alert alert = new Alert(AlertType.ERROR); alert.setTitle(titulo); alert.setHeaderText(null); alert.setContentText(conteudo); alert.showAndWait();
    }

    private void iniciarJogoPrincipal(String nomeJogador, ClassePersonagem classeEscolhida) {
        primaryStage.setTitle("Última Fronteira");
        gerenciadorDeAmbientes = new GerenciadorDeAmbientes();
        Ambiente ambienteInicial = gerenciadorDeAmbientes.getAmbientePorNome("Floresta dos Sussurros");
        if (ambienteInicial == null) ambienteInicial = gerenciadorDeAmbientes.getAmbienteGlobalPadrao();

        jogador = new Personagem(nomeJogador, classeEscolhida, VIDA_BASE_PADRAO, FOME_BASE_PADRAO, SEDE_BASE_PADRAO, ENERGIA_BASE_PADRAO, SANIDADE_BASE_PADRAO, ambienteInicial, PESO_INV_PADRAO);
        gerenciadorDeEventos = new GerenciadorDeEventos();
        controladorDeTurno = new ControladorDeTurno(jogador, gerenciadorDeEventos);

        maca = new Alimento("Maçã Vermelha", 0.2, 1, 20, "Fruta"); aguaPura = new Agua("Cantil de Água Pura", 0.5, 1, 30, "Potável", 0.5);
        machadoSimples = new Ferramenta("Machadinha de Pedra", 1.5, 10, "Corte", 5); espadaCurta = new Arma("Adaga Enferrujada", 2.0, 15, "Corpo a corpo", 10, 1);
        arcoSimples = new Arma("Arco Curto Gasto", 1.2, 20, "À Distância", 8, 10);
        bandagem = new Remedio("Tiras de Pano", 0.1, "Consumível", "Cura 15 de vida");
        madeira = new Material("Gravetos Secos", 1.0, "Madeira", 2);
        pedraAfiada = new Material("Pedra Afiada", 0.4, "Mineral", 3);

        outputArea = new TextArea(); outputArea.setEditable(false); outputArea.setWrapText(true); outputArea.setFont(Font.font("Monospaced", 12)); outputArea.setPrefHeight(340);
        BorderPane root = new BorderPane(); root.setPadding(new Insets(10));
        layoutPrincipalControles = new VBox(10); layoutPrincipalControles.setPadding(new Insets(5,0,5,0));

        Label lblItens = new Label("Adicionar Itens:"); itensBox = new HBox(5);
        Button btnAdicionarMaca = new Button("Add Maçã"); Button btnAdicionarAgua = new Button("Add Água"); Button btnAdicionarMachado = new Button("Add Machado");
        Button btnAdicionarEspada = new Button("Add Adaga"); Button btnAdicionarArco = new Button("Add Arco");
        Button btnAdicionarBandagem = new Button("Add Bandagem");
        Button btnAdicionarMadeira = new Button("Add Gravetos"); Button btnAdicionarPedra = new Button("Add Pedra Afiada");
        itensBox.getChildren().addAll(btnAdicionarMaca, btnAdicionarAgua, btnAdicionarMachado, btnAdicionarEspada, btnAdicionarArco, btnAdicionarBandagem, btnAdicionarMadeira, btnAdicionarPedra);

        Label lblUsarItem = new Label("Usar Item:"); usarItemBox = new HBox(5);
        TextField nomeItemParaUsar = new TextField(); nomeItemParaUsar.setPromptText("Nome do item"); nomeItemParaUsar.setPrefWidth(150);
        Button btnUsarItem = new Button("Usar"); usarItemBox.getChildren().addAll(nomeItemParaUsar, btnUsarItem);

        Label lblEquiparArma = new Label("Equipar Arma:"); equiparArmaBox = new HBox(5);
        TextField txtNomeArmaParaEquipar = new TextField(); txtNomeArmaParaEquipar.setPromptText("Nome da arma"); txtNomeArmaParaEquipar.setPrefWidth(180);
        Button btnEquiparArma = new Button("Equipar"); Button btnDesequiparArma = new Button("Desequipar");
        equiparArmaBox.getChildren().addAll(txtNomeArmaParaEquipar, btnEquiparArma, btnDesequiparArma);

        Label lblFabricacao = new Label("Habilidades de Classe:"); fabricacaoBox = new HBox(5);
        btnFabricarFaca = new Button("Fabricar Faca de Pedra (Mecânico)");
        fabricacaoBox.getChildren().add(btnFabricarFaca);
        atualizarVisibilidadePainelFabricacao();


        Label lblAcoesGerais = new Label("Ações/Info:"); acoesInfoBox = new HBox(5);
        Button btnListarInventario = new Button("Inventário"); Button btnStatusJogador = new Button("Status de " + jogador.getNome()); Button btnExplorar = new Button("Explorar");
        acoesInfoBox.getChildren().addAll(btnListarInventario, btnStatusJogador, btnExplorar);

        Label lblMovimento = new Label("Mover para:"); painelMovimento = new VBox(5);
        Label lblTurno = new Label("Jogo:"); turnoBox = new HBox(5);
        Button btnProximoTurno = new Button("Próximo Turno"); turnoBox.getChildren().add(btnProximoTurno);

        painelAcoesConfronto = new VBox(10); painelAcoesConfronto.setAlignment(Pos.CENTER);
        Label lblTituloConfronto = new Label("EM CONFRONTO!"); lblTituloConfronto.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        btnLutar = new Button("Lutar com Habilidade/Arma"); btnFugir = new Button("Tentar Fugir");
        HBox botoesLutaFuga = new HBox(10, btnLutar, btnFugir); botoesLutaFuga.setAlignment(Pos.CENTER);
        painelAcoesConfronto.getChildren().addAll(lblTituloConfronto, botoesLutaFuga);

        layoutPrincipalControles.getChildren().addAll( lblItens, itensBox, lblUsarItem, usarItemBox, lblEquiparArma, equiparArmaBox, lblFabricacao, fabricacaoBox, lblAcoesGerais, acoesInfoBox, lblMovimento, painelMovimento, lblTurno, turnoBox, painelAcoesConfronto);
        setVisibilidadePainelConfronto(false);

        btnAdicionarMaca.setOnAction(e -> executarAcaoNormal(() -> adicionarItemAoInventario(maca, "Maçã Vermelha")));
        btnAdicionarAgua.setOnAction(e -> executarAcaoNormal(() -> adicionarItemAoInventario(aguaPura, "Cantil de Água Pura")));
        btnAdicionarMachado.setOnAction(e -> executarAcaoNormal(() -> adicionarItemAoInventario(machadoSimples, "Machadinha de Pedra")));
        btnAdicionarEspada.setOnAction(e -> executarAcaoNormal(() -> adicionarItemAoInventario(espadaCurta, "Adaga Enferrujada")));
        btnAdicionarArco.setOnAction(e -> executarAcaoNormal(() -> adicionarItemAoInventario(arcoSimples, "Arco Curto Gasto")));
        btnAdicionarBandagem.setOnAction(e -> executarAcaoNormal(() -> adicionarItemAoInventario(bandagem, "Tiras de Pano")));
        btnAdicionarMadeira.setOnAction(e -> executarAcaoNormal(() -> adicionarItemAoInventario(madeira, "Gravetos Secos")));
        btnAdicionarPedra.setOnAction(e -> executarAcaoNormal(() -> adicionarItemAoInventario(pedraAfiada, "Pedra Afiada")));

        btnUsarItem.setOnAction(e -> executarAcaoNormal(() -> {
            String nomeItem = nomeItemParaUsar.getText();
            if (nomeItem != null && !nomeItem.trim().isEmpty()) {
                atualizarOutput("AÇÃO: Usando item '" + nomeItem + "'");
                jogador.getInventario().usarItem(nomeItem, jogador); exibirStatusJogador(); exibirInventario(); nomeItemParaUsar.clear();
            } else { atualizarOutput("Por favor, digite o nome de um item para usar.");}
        }));

        btnEquiparArma.setOnAction(e -> executarAcaoNormal(() -> {
            String nomeArma = txtNomeArmaParaEquipar.getText();
            if (nomeArma != null && !nomeArma.trim().isEmpty()) {
                String resultadoEquipar = jogador.equiparArma(nomeArma);
                atualizarOutput(resultadoEquipar); txtNomeArmaParaEquipar.clear(); exibirStatusJogador();
            } else { atualizarOutput("Digite o nome da arma do inventário para equipar.");}
        }));
        btnDesequiparArma.setOnAction(e -> executarAcaoNormal(() -> {
            String resultadoDesequipar = jogador.desequiparArma();
            atualizarOutput(resultadoDesequipar); exibirStatusJogador();
        }));

        btnFabricarFaca.setOnAction(e -> executarAcaoNormal(this::tentarFabricarFacaDePedra));

        btnListarInventario.setOnAction(e -> executarAcaoNormal(this::exibirInventario));
        btnStatusJogador.setOnAction(e -> executarAcaoNormal(this::exibirStatusJogador));
        btnExplorar.setOnAction(e -> executarAcaoNormal(this::processarExploracao));
        btnProximoTurno.setOnAction(e -> processarProximoTurno());
        btnLutar.setOnAction(e -> processarLuta()); btnFugir.setOnAction(e -> processarFuga());

        root.setTop(layoutPrincipalControles); root.setCenter(outputArea);
        Scene cenaPrincipal = new Scene(root, 800, 780);
        primaryStage.setScene(cenaPrincipal);

        atualizarBotoesDeMovimento(); exibirStatusJogador(); exibirInventario();
        atualizarOutput("Bem-vindo ao Última Fronteira, " + jogador.getNome() + "! Turno: " + controladorDeTurno.getNumeroDoTurno());
        atualizarLocalizacaoNaTela();
    }

    private void tentarFabricarFacaDePedra() {
        if (jogador.getClassePersonagem() != ClassePersonagem.MECANICO || !jogador.temHabilidade("Fabricar Ferramentas Básicas")) {
            atualizarOutput("Apenas Mecânicos com a habilidade 'Fabricar Ferramentas Básicas' podem fazer isso.");
            return;
        }
        boolean temMadeira = jogador.getInventario().getItens().stream().anyMatch(i -> "Gravetos Secos".equals(i.getNome()));
        boolean temPedra = jogador.getInventario().getItens().stream().anyMatch(i -> "Pedra Afiada".equals(i.getNome()));

        if (temMadeira && temPedra) {
            jogador.getInventario().removerItem("Gravetos Secos");
            jogador.getInventario().removerItem("Pedra Afiada");
            Ferramenta facaDePedra = new Ferramenta("Faca de Pedra Improvisada", 0.3, 8, "Corte/Defesa Leve", 4);
            jogador.getInventario().adicionarItem(facaDePedra);
            atualizarOutput(jogador.getNome() + " usou sua engenhosidade e fabricou uma Faca de Pedra Improvisada!");
            exibirInventario();
        } else {
            String faltando = "";
            if (!temMadeira) faltando += "Gravetos Secos ";
            if (!temPedra) faltando += "Pedra Afiada ";
            atualizarOutput("Materiais insuficientes para fabricar Faca de Pedra. Falta: " + faltando.trim().replace(" ", ", "));
        }
    }

    private void processarExploracao() {
        atualizarOutput("--- AÇÃO: Explorar Ambiente ---");
        String resultadoExploracao = jogador.explorarAmbiente(gerenciadorDeEventos, controladorDeTurno.getNumeroDoTurno());
        atualizarOutput(resultadoExploracao);
        if (!verificarInicioDeConfronto()) {
            exibirStatusJogador(); exibirInventario(); atualizarLocalizacaoNaTela();
        }
    }

    private void processarProximoTurno() {
        if (controladorDeTurno.isJogoTerminou()) {
            atualizarOutput("\n" + controladorDeTurno.getMensagemFimDeJogo()); desabilitarTodosOsControles(); return;
        }
        String logDoTurno = controladorDeTurno.executarProximoTurno();
        atualizarOutput(logDoTurno);
        if (!verificarInicioDeConfronto()) {
            atualizarOutput("======================================================");
            exibirStatusJogador(); exibirInventario(); atualizarBotoesDeMovimento(); atualizarLocalizacaoNaTela();
        }
        if (controladorDeTurno.isJogoTerminou()) {
            atualizarOutput("\n" + controladorDeTurno.getMensagemFimDeJogo()); desabilitarTodosOsControles();
        }
    }

    private boolean verificarInicioDeConfronto() {
        Evento eventoAtivo = gerenciadorDeEventos.getEventoSorteadoAtual();
        if (eventoAtivo instanceof EventoCriatura) {
            this.eventoConfrontoAtual = (EventoCriatura) eventoAtivo;
            entrarModoConfronto();
            return true;
        }
        this.eventoConfrontoAtual = null;
        gerenciadorDeEventos.limparEventoSorteadoAtual();
        return false;
    }

    private void setVisibilidadePaineisNormais(boolean visivel) {
        itensBox.setVisible(visivel); itensBox.setManaged(visivel);
        usarItemBox.setVisible(visivel); usarItemBox.setManaged(visivel);
        equiparArmaBox.setVisible(visivel); equiparArmaBox.setManaged(visivel);
        // Atualiza visibilidade do painel de fabricação baseado na classe e se os controles normais estão visíveis
        atualizarVisibilidadePainelFabricacao();
        if(!visivel) { // Se for para esconder os painéis normais, esconde o de fabricação também.
            fabricacaoBox.setVisible(false); fabricacaoBox.setManaged(false);
        }
        acoesInfoBox.setVisible(visivel); acoesInfoBox.setManaged(visivel);
        painelMovimento.setVisible(visivel); painelMovimento.setManaged(visivel);
        turnoBox.setVisible(visivel); turnoBox.setManaged(visivel);
    }

    private void atualizarVisibilidadePainelFabricacao() {
        boolean podeFabricar = jogador != null && jogador.getClassePersonagem() == ClassePersonagem.MECANICO && jogador.temHabilidade("Fabricar Ferramentas Básicas") && !emConfronto && (controladorDeTurno != null && !controladorDeTurno.isJogoTerminou());
        fabricacaoBox.setVisible(podeFabricar);
        fabricacaoBox.setManaged(podeFabricar);
    }


    private void setVisibilidadePainelConfronto(boolean visivel) {
        painelAcoesConfronto.setVisible(visivel);
        painelAcoesConfronto.setManaged(visivel);
    }

    private void entrarModoConfronto() {
        emConfronto = true;
        atualizarOutput(">>> MODO DE CONFRONTO ATIVADO: " + eventoConfrontoAtual.getTipoDeCriatura() + " (PV: " + eventoConfrontoAtual.getVidaAtualCriatura() + ") <<<");
        setVisibilidadePaineisNormais(false);
        setVisibilidadePainelConfronto(true);
        atualizarBotoesDeMovimento(); // Para desabilitar/esconder botões de movimento
    }

    private void sairModoConfronto(String mensagemResultado) {
        emConfronto = false;
        atualizarOutput(mensagemResultado);
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
        if (controladorDeTurno.isJogoTerminou()) {
            atualizarOutput("\n" + controladorDeTurno.getMensagemFimDeJogo()); desabilitarTodosOsControles();
        }
    }

    private void processarLuta() {
        if (!emConfronto || eventoConfrontoAtual == null || jogador.getVida() <= 0) return;
        StringBuilder sbLuta = new StringBuilder();
        sbLuta.append("--- Rodada de Combate (Turno ").append(controladorDeTurno.getNumeroDoTurno()).append(") ---\n");
        int danoJogador = 2; String tipoAtaqueJogador = "um soco fraco"; int custoEnergiaHabilidade = 0;
        Arma armaAtual = jogador.getArmaEquipada();

        if (jogador.getClassePersonagem() == ClassePersonagem.MECANICO && armaAtual != null && jogador.temHabilidade("Engenhosidade")) {
            custoEnergiaHabilidade = 5;
            if (jogador.getEnergia() >= custoEnergiaHabilidade) {
                jogador.setEnergia(jogador.getEnergia() - custoEnergiaHabilidade);
                danoJogador = armaAtual.getDano() + 3 + (controladorDeTurno.getNumeroDoTurno() / 6);
                tipoAtaqueJogador = "um golpe engenhoso com sua " + armaAtual.getNome() + " (-"+custoEnergiaHabilidade+" Energia)";
            } else {
                sbLuta.append(jogador.getNome()).append(" tenta um golpe engenhoso, mas falta energia!\n");
                danoJogador = armaAtual.getDano(); tipoAtaqueJogador = "com " + armaAtual.getNome();
            }
        } else if (jogador.getClassePersonagem() == ClassePersonagem.SOBREVIVENTE_NATO && armaAtual != null) {
            danoJogador = armaAtual.getDano() + 1 + (controladorDeTurno.getNumeroDoTurno() / 7);
            tipoAtaqueJogador = "um ataque resiliente com " + armaAtual.getNome();
        } else if (armaAtual != null) {
            danoJogador = armaAtual.getDano(); tipoAtaqueJogador = "com " + armaAtual.getNome();
        }

        sbLuta.append(jogador.getNome()).append(" ataca ").append(eventoConfrontoAtual.getTipoDeCriatura()).append(" ").append(tipoAtaqueJogador).append("!\n");
        sbLuta.append(eventoConfrontoAtual.receberDano(danoJogador));

        if (eventoConfrontoAtual.getVidaAtualCriatura() <= 0) {
            sairModoConfronto(sbLuta.toString()); return;
        }
        int danoCriatura = eventoConfrontoAtual.calcularDanoEfetivoCriatura(controladorDeTurno.getNumeroDoTurno());
        sbLuta.append(eventoConfrontoAtual.getTipoDeCriatura()).append(" revida e ataca ").append(jogador.getNome()).append(" causando ").append(danoCriatura).append(" de dano!\n");
        jogador.setVida(jogador.getVida() - danoCriatura);
        atualizarOutput(sbLuta.toString()); exibirStatusJogador();
        if (jogador.getVida() <= 0) {
            controladorDeTurno.isJogoTerminou(); sairModoConfronto(jogador.getNome() + " foi derrotado pelo " + eventoConfrontoAtual.getTipoDeCriatura() + ".\n");
        } else {
            atualizarOutput("O " + eventoConfrontoAtual.getTipoDeCriatura() + " ainda tem " + eventoConfrontoAtual.getVidaAtualCriatura() + " PV. O confronto continua.");
        }
    }

    private void processarFuga() {
        if (!emConfronto || eventoConfrontoAtual == null || jogador.getVida() <= 0) return;
        StringBuilder sbFuga = new StringBuilder();
        sbFuga.append(jogador.getNome()).append(" tenta fugir do ").append(eventoConfrontoAtual.getTipoDeCriatura()).append("...\n");
        double chanceDeFuga = 0.35;
        if (jogador.temHabilidade("Movimentação Silenciosa") || jogador.getClassePersonagem() == ClassePersonagem.RASTREADOR) {
            chanceDeFuga += 0.25;
        }
        if (random.nextDouble() < chanceDeFuga) {
            sbFuga.append("Você conseguiu escapar!\n"); sairModoConfronto(sbFuga.toString());
        } else {
            sbFuga.append("A fuga falhou! O ").append(eventoConfrontoAtual.getTipoDeCriatura()).append(" ataca enquanto você se vira!\n");
            int danoCriatura = eventoConfrontoAtual.calcularDanoEfetivoCriatura(controladorDeTurno.getNumeroDoTurno());
            jogador.setVida(jogador.getVida() - danoCriatura);
            sbFuga.append(jogador.getNome()).append(" recebeu ").append(danoCriatura).append(" de dano.\n");
            atualizarOutput(sbFuga.toString()); exibirStatusJogador();
            if (jogador.getVida() <= 0) {
                controladorDeTurno.isJogoTerminou(); sairModoConfronto(jogador.getNome() + " foi derrotado ao tentar fugir do " + eventoConfrontoAtual.getTipoDeCriatura() + ".\n");
            } else {
                atualizarOutput("O " + eventoConfrontoAtual.getTipoDeCriatura() + " ainda tem " + eventoConfrontoAtual.getVidaAtualCriatura() + " PV. O confronto continua.");
            }
        }
    }

    private void executarAcaoNormal(Runnable acao) {
        if (controladorDeTurno != null && controladorDeTurno.isJogoTerminou()) {
            if(!emConfronto ){
                atualizarOutput("O jogo já terminou. Nenhuma ação adicional permitida.");
                if (controladorDeTurno.getMensagemFimDeJogo() != null && !controladorDeTurno.getMensagemFimDeJogo().isEmpty()) {
                    atualizarOutput(controladorDeTurno.getMensagemFimDeJogo());
                }
            }
            desabilitarTodosOsControles(); return;
        }
        if (emConfronto) {
            atualizarOutput("Você está em confronto! Use as ações de Lutar ou Fugir."); return;
        }
        acao.run();
        if (controladorDeTurno != null && controladorDeTurno.isJogoTerminou()) {
            if (controladorDeTurno.getMensagemFimDeJogo() != null && !controladorDeTurno.getMensagemFimDeJogo().isEmpty()) {
                atualizarOutput(controladorDeTurno.getMensagemFimDeJogo());
            }
            desabilitarTodosOsControles();
        }
    }

    private void desabilitarTodosOsControles() {
        if (layoutPrincipalControles == null) return;
        setVisibilidadePaineisNormais(false);
        setVisibilidadePainelConfronto(false);
        outputArea.appendText("\n--- TODOS OS CONTROLES DESABILITADOS ---");
    }

    private void atualizarLocalizacaoNaTela() {
        if (jogador != null && jogador.getLocalizacaoAtual() != null) {
            atualizarOutput("Você está em: " + jogador.getLocalizacaoAtual().getNome() + " (" + jogador.getLocalizacaoAtual().getDescricao() + ")");
        }
    }

    private void atualizarBotoesDeMovimento() {
        if (painelMovimento == null) return;
        painelMovimento.getChildren().clear();
        boolean desabilitar = emConfronto || (controladorDeTurno != null && controladorDeTurno.isJogoTerminou());

        if (jogador == null || jogador.getLocalizacaoAtual() == null || gerenciadorDeAmbientes == null ) {
            if(controladorDeTurno != null && controladorDeTurno.isJogoTerminou()){
                painelMovimento.getChildren().add(new Label("O jogo terminou."));
            } else if (!emConfronto){
                painelMovimento.getChildren().add(new Label("Movimentação indisponível no momento."));
            }
            painelMovimento.setDisable(true); return;
        }
        painelMovimento.setDisable(desabilitar);
        List<Ambiente> adjacentes = gerenciadorDeAmbientes.getAmbientesAdjacentes(jogador.getLocalizacaoAtual().getNome());
        if (adjacentes.isEmpty() && !desabilitar) {
            painelMovimento.getChildren().add(new Label("Nenhum lugar para ir daqui."));
        } else if (!desabilitar) {
            for (Ambiente destino : adjacentes) {
                Button btnMover = new Button("Ir para " + destino.getNome());
                btnMover.setOnAction(e -> {
                    if(emConfronto || (controladorDeTurno != null && controladorDeTurno.isJogoTerminou())) return;
                    int custoEnergiaMovimento = 15;
                    if (jogador.getEnergia() >= custoEnergiaMovimento) {
                        atualizarOutput("--- AÇÃO: Mover para " + destino.getNome() + " ---");
                        boolean moveu = gerenciadorDeAmbientes.mudarAmbienteAtualDoJogador(jogador, destino.getNome());
                        if (moveu) {
                            jogador.setEnergia(jogador.getEnergia() - custoEnergiaMovimento);
                            String eventoChegada = gerenciadorDeEventos.sortearEExecutarEvento(jogador, jogador.getLocalizacaoAtual(), controladorDeTurno.getNumeroDoTurno());
                            atualizarOutput(eventoChegada);
                            if (!verificarInicioDeConfronto()){
                                atualizarOutput("Você chegou em " + destino.getNome() + ".");
                                atualizarOutput("Energia gasta no movimento: " + custoEnergiaMovimento);
                                atualizarOutput("------------------------------------------------------");
                                atualizarBotoesDeMovimento(); exibirStatusJogador(); atualizarLocalizacaoNaTela();
                            }
                            if (controladorDeTurno.isJogoTerminou()) {
                                atualizarOutput("\n" + controladorDeTurno.getMensagemFimDeJogo()); desabilitarTodosOsControles();
                            }
                        } else {
                            atualizarOutput("Não foi possível mover para " + destino.getNome() + " (Talvez não seja adjacente?).");
                        }
                    } else {
                        atualizarOutput("Energia insuficiente para mover para " + destino.getNome() + ". Você tem " + jogador.getEnergia() + ", precisa de " + custoEnergiaMovimento + ".");
                    }
                });
                painelMovimento.getChildren().add(btnMover);
            }
        }
        if (desabilitar && controladorDeTurno !=null && !controladorDeTurno.isJogoTerminou() && painelMovimento.getChildren().isEmpty()) {
            painelMovimento.getChildren().add(new Label("Movimentação bloqueada durante confronto."));
        }
    }

    private void adicionarItemAoInventario(Item item, String nomeAmigavel) {
        if (item == null) {
            atualizarOutput("Tentativa de adicionar um item nulo (" + nomeAmigavel + "). Operação cancelada."); return;
        }
        Item itemParaAdicionar = duplicarItemParaTeste(item);
        if (jogador.getInventario().adicionarItem(itemParaAdicionar)) {
            atualizarOutput(nomeAmigavel + " adicionado(a) ao inventário.");
        } else {
            atualizarOutput("Inventário cheio ou item inválido. Não foi possível adicionar " + nomeAmigavel + ".");
        }
        exibirInventario();
    }

    private Item duplicarItemParaTeste(Item original) {
        if (original instanceof Alimento) { Alimento o = (Alimento) original; return new Alimento(o.getNome(), o.getPeso(), o.getDurabilidade(), o.getValorNutricional(), o.getTipo()); }
        else if (original instanceof Agua) { Agua o = (Agua) original; return new Agua(o.getNome(), o.getPeso(), o.getDurabilidade(), o.getValorHidratacao(), o.getPureza(), o.getVolume()); }
        else if (original instanceof Ferramenta) { Ferramenta o = (Ferramenta) original; return new Ferramenta(o.getNome(), o.getPeso(), o.getDurabilidade(), o.getTipo(), o.getEficiencia()); }
        else if (original instanceof Arma) { Arma o = (Arma) original; return new Arma(o.getNome(), o.getPeso(), o.getDurabilidade(), o.getTipoArma(), o.getDano(), o.getAlcance()); }
        else if (original instanceof Remedio) { Remedio o = (Remedio) original; return new Remedio(o.getNome(), o.getPeso(), o.getTipo(), o.getEfeito()); }
        else if (original instanceof Material) { Material o = (Material) original; return new Material(o.getNome(), o.getPeso(), o.getTipo(), o.getResistencia()); }
        return original;
    }

    private void atualizarOutput(String mensagem) {
        if (mensagem == null || mensagem.trim().isEmpty()) return;
        outputArea.appendText(mensagem.endsWith("\n") ? mensagem : mensagem + "\n");
    }

    private void exibirStatusJogador() {
        if (controladorDeTurno == null || jogador == null) return;
        outputArea.appendText("\n");
        String tituloStatus = String.format("===== STATUS DE %S (Turno: %d) ======", jogador.getNome().toUpperCase(), controladorDeTurno.getNumeroDoTurno());
        outputArea.appendText(tituloStatus + "\n");
        outputArea.appendText(jogador.getStatus());
        outputArea.appendText("\n============================================\n");
    }

    private void exibirInventario() {
        if (jogador == null || jogador.getInventario() == null) return;
        outputArea.appendText("\n");
        outputArea.appendText("============== INVENTÁRIO ==============\n");
        Inventario inv = jogador.getInventario();
        if (inv.getItens().isEmpty()) {
            outputArea.appendText(String.format("%-30s\n", "Vazio"));
        } else {
            outputArea.appendText(String.format("%-25s | %-6s | %-10s\n", "Item", "Peso", "Durab."));
            outputArea.appendText("----------------------------------------------\n");
            for (Item item : inv.getItens()) {
                String durabStr = (item.getDurabilidade() != -1 ? String.valueOf(item.getDurabilidade()) : "N/A");
                outputArea.appendText(String.format("%-25s | %-6.2f | %-10s\n", item.getNome(), item.getPeso(), durabStr));
            }
            outputArea.appendText("----------------------------------------------\n");
        }
        outputArea.appendText(String.format("Peso Total  : %.2f / %.2f\n", inv.getPesoAtual(), inv.getPesoMaximo()));
        outputArea.appendText("======================================\n");
>>>>>>> f7209b58956d6d2fcbc7f29dfcf96ffd5093dbcd
    }

    public static void main(String[] args) {
        launch(args);
    }
}