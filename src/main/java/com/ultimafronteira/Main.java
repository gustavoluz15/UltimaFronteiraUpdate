package com.ultimafronteira;

import com.ultimafronteira.events.Evento;
import com.ultimafronteira.events.EventoCriatura;
import com.ultimafronteira.events.EventoClimatico;
import com.ultimafronteira.events.EventoMudancaDeAmbiente;
import com.ultimafronteira.events.GerenciadorDeEventos;
import com.ultimafronteira.gamecore.ControladorDeTurno;
import com.ultimafronteira.model.*;
import com.ultimafronteira.visual.GameView;
import com.ultimafronteira.visual.GerenciadorDeImagens;
import com.ultimafronteira.world.Ambiente;
import com.ultimafronteira.world.GerenciadorDeAmbientes;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.DialogPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
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
            mostrarAlerta("Erro Crítico", "Ocorreu um erro fatal ao iniciar o jogo: " + t.getMessage());
            Platform.exit();
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
            if (nomeJogador == null || nomeJogador.trim().isEmpty() || nomeJogador.equals("Nome do Personagem")) {
                mostrarAlerta("Nome Inválido", "Por favor, digite um nome válido para o seu personagem.");
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

    private void iniciarJogoPrincipal(String nomeJogador, ClassePersonagem classeEscolhida) {
        gerenciadorDeAmbientes = new GerenciadorDeAmbientes();
        Ambiente ambienteInicial = gerenciadorDeAmbientes.getAmbientePorNome("Floresta dos Sussurros");
        if (ambienteInicial == null && !gerenciadorDeAmbientes.getTodosAmbientes().isEmpty()) {
            ambienteInicial = gerenciadorDeAmbientes.getTodosAmbientes().get(0);
        } else if (ambienteInicial == null) {
            mostrarAlerta("Erro Crítico", "Não foi possível carregar os ambientes do jogo.");
            Platform.exit();
            return;
        }

        String chaveImagemPersonagem = "personagem_sobrevivente";
        switch (classeEscolhida) {
            case MECANICO: chaveImagemPersonagem = "personagem_mecanico"; break;
            case MEDICO: chaveImagemPersonagem = "personagem_medico"; break;
            case RASTREADOR: chaveImagemPersonagem = "personagem_rastreador"; break;
        }

        jogador = new Personagem(nomeJogador, classeEscolhida, VIDA_BASE_PADRAO, FOME_BASE_PADRAO, SEDE_BASE_PADRAO, ENERGIA_BASE_PADRAO, SANIDADE_BASE_PADRAO, ambienteInicial, PESO_INV_PADRAO, chaveImagemPersonagem);
        gerenciadorDeEventos = new GerenciadorDeEventos();
        controladorDeTurno = new ControladorDeTurno(jogador, gerenciadorDeEventos);
        eventoClimaticoAtivo = null;
        emConfronto = false;
        eventoConfrontoAtual = null;

        gameView = new GameView(this::processarUsoDeItemInventario);

        gameView.getBtnExplorar().setOnAction(e -> executarAcaoPrincipal(this::processarExploracao, false));
        gameView.getBtnProximoTurno().setOnAction(e -> executarAcaoPrincipal(this::processarProximoTurno, true));
        gameView.getBtnDescansar().setOnAction(e -> executarAcaoPrincipal(this::processarDescanso, true));
        gameView.getBtnNovoJogo().setOnAction(e -> mostrarCenaCriacaoPersonagem());

        primaryStage.setTitle("Última Fronteira");
        primaryStage.setScene(gameView.getScene());
        primaryStage.centerOnScreen();

        exibirMensagem("Bem-vindo ao Última Fronteira, " + jogador.getNome() + "!");
        atualizarTodaUI();
        gameView.habilitarAcoes();
    }

    private void atualizarTodaUI() {
        if (gameView == null || jogador == null || controladorDeTurno == null) return;
        EventoCriatura inimigoParaDesenhar = emConfronto ? eventoConfrontoAtual : null;
        Ambiente ambienteParaDesenhar = jogador.getLocalizacaoAtual();
        EventoClimatico climaParaDesenhar = eventoClimaticoAtivo;

        gameView.getCenaPane().desenharCena(jogador, inimigoParaDesenhar, ambienteParaDesenhar, climaParaDesenhar);
        gameView.atualizarInventario(jogador.getInventario());
        atualizarBotoesDeMovimento();
        gameView.atualizarTurno(controladorDeTurno.getNumeroDoTurno());
        if(ambienteParaDesenhar != null) {
            gameView.atualizarAmbienteAtual(ambienteParaDesenhar.getNome());
        }

        if (emConfronto && eventoConfrontoAtual != null && jogador.getVida() > 0 && eventoConfrontoAtual.getVidaAtualCriatura() > 0) {
            gameView.mostrarControlesCombate(
                    "Confronto com " + eventoConfrontoAtual.getTipoDeCriatura() + " (PV: " + eventoConfrontoAtual.getVidaAtualCriatura() + ")",
                    this::processarAcaoCombate
            );
        } else {
            gameView.esconderControlesCombate();
        }
    }

    private void executarAcaoPrincipal(Runnable acao, boolean podeExecutarCansado) {
        if (emConfronto) {
            exibirMensagem("Você está em confronto! Resolva-o primeiro.");
            return;
        }
        if (controladorDeTurno.isJogoTerminou()) {
            exibirMensagem("O jogo já terminou.");
            return;
        }

        if (jogador.getEnergia() <= 0 && !podeExecutarCansado) {
            exibirMensagem("Você está exausto demais para isso.", "Precisa descansar ou usar um item de energia.");
            return;
        }

        acao.run();

        if (jogador.getEnergia() <= 0) {
            exibirMensagem("Sua energia acabou! Você precisa descansar.");
        }

        if (controladorDeTurno.isJogoTerminou() && !emConfronto) {
            finalizarJogo();
        }
    }

    private void processarProximoTurno() {
        jogador.setVida(jogador.getVida() - 15);
        exibirMensagem("Avançar o tempo de forma imprudente tem seus custos... Você perde 15 de vida.");
        if (jogador.getVida() <= 0) {
            controladorDeTurno.forcarFimDeJogo("Seu corpo não aguentou o esforço de continuar sem um plano.");
            finalizarJogo();
            return;
        }

        String logDoTurno = controladorDeTurno.executarProximoTurno();
        gameView.appendOutput(logDoTurno); // Log detalhado vai para o Diário
        verificarEventosEEntrarEmConfrontoSeNecessario();
        if (!emConfronto) {
            atualizarTodaUI();
        }
    }

    private void processarExploracao() {
        String resultadoExploracao = jogador.explorarAmbiente(gerenciadorDeEventos, controladorDeTurno.getNumeroDoTurno());
        gameView.appendOutput(resultadoExploracao);
        verificarEventosEEntrarEmConfrontoSeNecessario();
        if (!emConfronto) {
            atualizarTodaUI();
        }
    }

    private void processarDescanso() {
        int energiaRecuperada = 30 + random.nextInt(11);
        int fomeConsumida = 10;
        int sedeConsumida = 10;
        jogador.setEnergia(jogador.getEnergia() + energiaRecuperada);
        jogador.setFome(Math.max(0, jogador.getFome() - fomeConsumida));
        jogador.setSede(Math.max(0, jogador.getSede() - sedeConsumida));
        exibirMensagem("Você descansa um pouco...", "Energia recuperada: +" + energiaRecuperada, "Fome e Sede alteradas: -" + fomeConsumida);

        controladorDeTurno.verificarCondicoesDeFimDeJogo();

        if(controladorDeTurno.isJogoTerminou()) {
            finalizarJogo();
        } else {
            processarProximoTurno(); // Descansar também avança para o próximo dia
        }
    }

    private void processarUsoDeItemInventario(Item item) {
        if (item == null || controladorDeTurno.isJogoTerminou()) return;

        String resultadoUso = item.usar(jogador);
        exibirMensagem("Você usou: " + item.getNome(), resultadoUso);

        if (item.getDurabilidade() <= 1 && !(item instanceof Arma || item instanceof Escudo)) {
            jogador.getInventario().removerItem(item.getNome());
        }

        atualizarTodaUI();

        if (emConfronto) {
            if (jogador.getVida() <= 0) {
                controladorDeTurno.forcarFimDeJogo(jogador.getNome() + " sucumbiu ao usar " + item.getNome() + ".");
                finalizarJogo();
                return;
            }
            if (!(item instanceof Arma || item instanceof Escudo)) {
                acaoDaCriaturaNoCombate();
            }
        }
    }

    private void processarMovimento(Ambiente destino) {
        int custoEnergia = 15;
        if (jogador.temHabilidade("Movimentação Silenciosa")) custoEnergia -= 5;
        custoEnergia = Math.max(5, custoEnergia);

        if (jogador.getEnergia() >= custoEnergia) {
            jogador.setEnergia(jogador.getEnergia() - custoEnergia);
            gerenciadorDeAmbientes.mudarAmbienteAtualDoJogador(jogador, destino.getNome());
            exibirMensagem("Você viajou para " + destino.getNome() + ".", "Energia gasta: " + custoEnergia);
            String resultadoEvento = gerenciadorDeEventos.sortearEExecutarEvento(jogador, destino, controladorDeTurno.getNumeroDoTurno());
            gameView.appendOutput(resultadoEvento);
            verificarEventosEEntrarEmConfrontoSeNecessario();
            if (!emConfronto) {
                atualizarTodaUI();
            }
        } else {
            exibirMensagem("Energia insuficiente para viajar ("+jogador.getEnergia()+"/"+custoEnergia+").");
        }
    }

    private void atualizarBotoesDeMovimento() {
        if (gameView == null || gerenciadorDeAmbientes == null || jogador == null || jogador.getLocalizacaoAtual() == null) return;
        List<Ambiente> destinos = gerenciadorDeAmbientes.getAmbientesAdjacentes(jogador.getLocalizacaoAtual().getNome());
        boolean desabilitarMovimento = emConfronto || controladorDeTurno.isJogoTerminou() || jogador.getEnergia() <= 0;
        gameView.atualizarPainelMovimento(destinos, this::processarMovimento, desabilitarMovimento);
    }

    private void verificarEventosEEntrarEmConfrontoSeNecessario() {
        Evento eventoSorteado = gerenciadorDeEventos.getEventoSorteadoAtual();
        if (eventoSorteado instanceof EventoCriatura) {
            this.eventoConfrontoAtual = (EventoCriatura) eventoSorteado;
            entrarModoConfronto();
        } else if (eventoSorteado instanceof EventoMudancaDeAmbiente) {
            EventoMudancaDeAmbiente evtMudanca = (EventoMudancaDeAmbiente) eventoSorteado;
            gerenciadorDeAmbientes.mudarAmbienteAtualDoJogador(jogador, evtMudanca.getNovoAmbiente().getNome());
            exibirMensagem(evtMudanca.getDescricao(), "Você agora está em " + evtMudanca.getNovoAmbiente().getNome() + ".");
            gerenciadorDeEventos.limparEventoSorteadoAtual();
            atualizarTodaUI();
        } else if (eventoSorteado != null) {
            if (eventoSorteado instanceof EventoClimatico) {
                if (this.eventoClimaticoAtivo == null || this.eventoClimaticoAtivo.getDuracao() <= 0) {
                    this.eventoClimaticoAtivo = (EventoClimatico) eventoSorteado;
                }
            }
            gerenciadorDeEventos.limparEventoSorteadoAtual();
        }
    }

    private void entrarModoConfronto() {
        this.emConfronto = true;
        exibirMensagem("CONFRONTO COM " + eventoConfrontoAtual.getTipoDeCriatura().toUpperCase() + "!");
        atualizarTodaUI();
    }

    private void sairModoConfronto(String mensagem) {
        emConfronto = false;
        String nomeCriaturaAnterior = (eventoConfrontoAtual != null) ? eventoConfrontoAtual.getTipoDeCriatura() : "um inimigo";
        eventoConfrontoAtual = null;
        gerenciadorDeEventos.limparEventoSorteadoAtual();

        exibirMensagem(mensagem, ">>> CONFRONTO COM " + nomeCriaturaAnterior + " TERMINADO <<<");

        atualizarTodaUI();

        if (controladorDeTurno.isJogoTerminou()) {
            finalizarJogo();
        }
    }

    private void processarAcaoCombate(String acao) {
        if (!emConfronto || eventoConfrontoAtual == null || jogador.getVida() <= 0) return;

        if ("lutar".equalsIgnoreCase(acao)) {
            processarLuta();
        } else if ("fugir".equalsIgnoreCase(acao)) {
            processarFuga();
        }
    }

    private void processarLuta() {
        int danoBase = jogador.calcularDanoBase();
        int danoBonus = jogador.consumirBuffDano();
        int danoTotal = danoBase + danoBonus;

        String logAtaqueJogador = eventoConfrontoAtual.receberDano(danoTotal);
        String msgBonus = danoBonus > 0 ? "Você usa o bônus e ataca com força extra!" : jogador.getNome() + " ataca!";
        exibirMensagem(msgBonus, logAtaqueJogador);

        atualizarTodaUI();

        if (eventoConfrontoAtual.getVidaAtualCriatura() <= 0) {
            sairModoConfronto("Você venceu o combate contra " + eventoConfrontoAtual.getTipoDeCriatura() + "!");
            return;
        }
        acaoDaCriaturaNoCombate();
    }

    private void processarFuga() {
        exibirMensagem(jogador.getNome() + " tenta fugir...");
        double chanceDeFuga = 0.5;
        if (jogador.temHabilidade("Movimentação Silenciosa")) {
            chanceDeFuga += 0.25;
        }

        if (random.nextDouble() < chanceDeFuga) {
            sairModoConfronto("Você conseguiu escapar!");
        } else {
            exibirMensagem("A fuga falhou!");
            acaoDaCriaturaNoCombate();
        }
    }

    private void acaoDaCriaturaNoCombate() {
        if (eventoConfrontoAtual == null || eventoConfrontoAtual.getVidaAtualCriatura() <= 0 || jogador.getVida() <= 0) {
            return;
        }
        int danoCriatura = eventoConfrontoAtual.calcularDanoEfetivoCriatura(controladorDeTurno.getNumeroDoTurno());
        int danoFinalRecebido = danoCriatura;

        if(jogador.isBuffDefesaAtivo()) {
            int reducao = jogador.consumirBuffDefesa();
            danoFinalRecebido = Math.max(0, danoCriatura - reducao);
            exibirMensagem("Você se defende e absorve " + reducao + " de dano!");
        }

        jogador.setVida(jogador.getVida() - danoFinalRecebido);
        exibirMensagem("O " + eventoConfrontoAtual.getTipoDeCriatura() + " revida!", "Você sofreu " + danoFinalRecebido + " de dano.");

        atualizarTodaUI();

        if (jogador.getVida() <= 0) {
            controladorDeTurno.forcarFimDeJogo(jogador.getNome() + " foi derrotado em combate.");
            finalizarJogo();
        } else {
            gameView.exibirLegenda("Sua vez. O " + eventoConfrontoAtual.getTipoDeCriatura() + " tem " + eventoConfrontoAtual.getVidaAtualCriatura() + " PV.");
        }
    }

    private void exibirMensagem(String... mensagens) {
        if (gameView != null) gameView.exibirMensagem(mensagens);
    }

    private void finalizarJogo() {
        if(gameView != null) gameView.desabilitarAcoes();
        if (emConfronto) {
            emConfronto = false;
            gameView.esconderControlesCombate();
        }

        atualizarTodaUI();

        String mensagemFinal = controladorDeTurno.getMensagemFimDeJogo();
        if (mensagemFinal == null || mensagemFinal.isEmpty()) {
            mensagemFinal = (jogador != null && jogador.getVida() <= 0) ? jogador.getNome() + " não sobreviveu." : "O jogo terminou.";
        }

        exibirMensagem("--- FIM DE JOGO ---", mensagemFinal);

        final String conteudoPopup = mensagemFinal;
        PauseTransition delay = new PauseTransition(Duration.seconds(2.0));
        delay.setOnFinished(event -> {
            if (conteudoPopup.contains("VITÓRIA")) {
                mostrarPopupDeVitoria(conteudoPopup);
            } else {
                mostrarPopupDeDerrota(conteudoPopup);
            }
        });
        delay.play();
    }

    private void mostrarPopupDeDerrota(String conteudo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fim de Jogo");
        alert.setHeaderText("VOCÊ MORREU!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #5B2C2C;");
        Label headerLabel = (Label) dialogPane.lookup(".header-panel .label");
        if(headerLabel != null) headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #ff8080;");
        alert.setContentText(conteudo.replace("FIM DE JOGO - DERROTA", "").trim());
        Label contentLabel = (Label) dialogPane.lookup(".content.label");
        if(contentLabel != null) contentLabel.setStyle("-fx-text-fill: white;");
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();
    }

    private void mostrarPopupDeVitoria(String conteudo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fim de Jogo");
        alert.setHeaderText("PARABÉNS, VOCÊ SOBREVIVEU!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #2C5B2C;");
        Label headerLabel = (Label) dialogPane.lookup(".header-panel .label");
        if(headerLabel != null) headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #80ff80;");
        alert.setContentText(conteudo.replace("FIM DE JOGO - VITÓRIA!", "").trim());
        Label contentLabel = (Label) dialogPane.lookup(".content.label");
        if(contentLabel != null) contentLabel.setStyle("-fx-text-fill: white;");
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();
    }

    private void exibirDetalhesDaClasseSelecionada(ClassePersonagem classe, Label lblDescricao, Label lblAtributos) {
        if (classe == null) return; lblDescricao.setText(classe.getDescricao()); StringBuilder atributosStr = new StringBuilder();
        int vidaPreview = VIDA_BASE_PADRAO; int energiaPreview = ENERGIA_BASE_PADRAO; int sanidadePreview = SANIDADE_BASE_PADRAO; int fomePreview = FOME_BASE_PADRAO; int sedePreview = SEDE_BASE_PADRAO;
        List<String> habilidadesPreview = new ArrayList<>();
        switch (classe) {
            case RASTREADOR: fomePreview += 15; sedePreview += 10; habilidadesPreview.add("Rastreamento Aguçado"); habilidadesPreview.add("Conhecimento da Flora e Fauna"); habilidadesPreview.add("Movimentação Silenciosa"); atributosStr.append(String.format("Fome Inicial: ~%d\nSede Inicial: ~%d\n", fomePreview, sedePreview)); break;
            case MECANICO: energiaPreview += 10; vidaPreview += 5; habilidadesPreview.add("Engenhosidade"); habilidadesPreview.add("Fabricar Ferramentas Básicas"); habilidadesPreview.add("Desmontar Sucata"); atributosStr.append(String.format("Vida Inicial: ~%d\nEnergia Inicial: ~%d\n", vidaPreview, energiaPreview)); break;
            case MEDICO: sanidadePreview += 10; vidaPreview += 5; habilidadesPreview.add("Primeiros Socorros Avançados"); habilidadesPreview.add("Conhecimento de Ervas Medicinais"); habilidadesPreview.add("Resistência a Doenças Leves"); atributosStr.append(String.format("Sanidade Inicial: ~%d\nVida Inicial: ~%d\n", sanidadePreview, vidaPreview)); break;
            case SOBREVIVENTE_NATO: vidaPreview += 15; fomePreview += 25; sedePreview += 25; energiaPreview += 5; habilidadesPreview.add("Metabolismo Eficiente"); habilidadesPreview.add("Resiliência Climática"); habilidadesPreview.add("Instinto de Perigo"); atributosStr.append(String.format("Vida: ~%d\nFome: ~%d\nSede: ~%d\nEnergia: ~%d\n", vidaPreview, fomePreview, sedePreview, energiaPreview)); break;
        }
        if (!habilidadesPreview.isEmpty()) { atributosStr.append("Habilidades Chave: ").append(String.join(", ", habilidadesPreview)).append("\n"); }
        lblAtributos.setText(atributosStr.toString());
    }

    private void mostrarAlerta(String titulo, String conteudo) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}