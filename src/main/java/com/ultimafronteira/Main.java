package com.ultimafronteira;

import com.ultimafronteira.events.*;
import com.ultimafronteira.gamecore.Acao;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
// Removido import desnecessário: import java.util.function.Consumer;

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

    private final int VIDA_BASE_PADRAO = 100; // Aumentado para mais tolerância
    private final int FOME_BASE_PADRAO = 100;
    private final int SEDE_BASE_PADRAO = 100;
    private final int ENERGIA_BASE_PADRAO = 100;
    private final int SANIDADE_BASE_PADRAO = 80;
    private final double PESO_INV_PADRAO = 25.0;

    @Override
    public void start(Stage primaryStage) {
        try {
            this.primaryStage = primaryStage;
            GerenciadorDeImagens.carregarImagens(); // Carrega as imagens uma vez
            primaryStage.setTitle("Última Fronteira - Criação de Personagem");
            mostrarCenaCriacaoPersonagem();
        } catch (Throwable t) {
            System.err.println("!!! ERRO FATAL DURANTE A INICIALIZAÇÃO !!!");
            t.printStackTrace();
        }
    }

    private void reiniciarAplicacao() {
        Main novaInstancia = new Main();
        try {
            novaInstancia.start(this.primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
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
        TextField txtNome = new TextField("Aventureiro");
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

    private void iniciarJogoPrincipal(String nomeJogador, ClassePersonagem classeEscolhida) {
        gerenciadorDeAmbientes = new GerenciadorDeAmbientes();
        Ambiente ambienteInicial = gerenciadorDeAmbientes.getAmbientePorNome("Floresta dos Sussurros");
        if (ambienteInicial == null) { // Fallback caso o ambiente padrão não seja encontrado
            ambienteInicial = gerenciadorDeAmbientes.getTodosAmbientes().stream().findFirst().orElse(null);
            if (ambienteInicial == null) {
                mostrarAlerta("Erro Crítico", "Nenhum ambiente inicial pôde ser carregado. O jogo não pode começar.");
                return;
            }
        }

        String chaveImagemPersonagem = "personagem_sobrevivente"; // Padrão
        switch (classeEscolhida) {
            case MECANICO: chaveImagemPersonagem = "personagem_mecanico"; break;
            case MEDICO: chaveImagemPersonagem = "personagem_medico"; break;
            case RASTREADOR: chaveImagemPersonagem = "personagem_rastreador"; break;
            case SOBREVIVENTE_NATO: chaveImagemPersonagem = "personagem_sobrevivente"; break;
        }

        jogador = new Personagem(nomeJogador, classeEscolhida, VIDA_BASE_PADRAO, FOME_BASE_PADRAO, SEDE_BASE_PADRAO, ENERGIA_BASE_PADRAO, SANIDADE_BASE_PADRAO, ambienteInicial, PESO_INV_PADRAO, chaveImagemPersonagem);
        gerenciadorDeEventos = new GerenciadorDeEventos();
        controladorDeTurno = new ControladorDeTurno(jogador, gerenciadorDeEventos);
        gameView = new GameView(this::processarUsoDeItem);

        gameView.getBtnExplorar().setOnAction(e -> processarAcaoDoControlador(Acao.EXPLORAR));
        gameView.getBtnDescansar().setOnAction(e -> processarAcaoDoControlador(Acao.DESCANSAR));
        gameView.getBtnProximoTurno().setOnAction(e -> processarAcaoDoControlador(Acao.AVANCAR_TURNO));
        gameView.getBtnNovoJogo().setOnAction(e -> reiniciarAplicacao());

        primaryStage.setTitle("Última Fronteira");
        primaryStage.setScene(gameView.getScene());
        primaryStage.centerOnScreen();
        gameView.appendOutput("Bem-vindo ao Última Fronteira, " + jogador.getNome() + "!\n");
        atualizarTodaUI();
    }

    private void processarAcaoDoControlador(Acao acao) {
        if (emConfronto || (controladorDeTurno != null && controladorDeTurno.isJogoTerminou())) return;

        gameView.limparLog(); // Limpa o log antes de adicionar novas mensagens
        String resultadoDoTurno = controladorDeTurno.executarAcaoDoJogador(acao);
        gameView.appendOutput(resultadoDoTurno);

        verificarEventosPosAcao(); // Renomeado para clareza

        if (controladorDeTurno.isJogoTerminou()) {
            finalizarJogo();
        } else if (!emConfronto) {
            atualizarTodaUI();
        }
    }

    private void atualizarTodaUI() {
        if (gameView == null || jogador == null) return;
        EventoCriatura inimigo = emConfronto ? eventoConfrontoAtual : null;
        // Garante que o ambiente atual do jogador é usado para desenhar
        Ambiente ambienteParaDesenhar = jogador.getLocalizacaoAtual();
        if (ambienteParaDesenhar == null && gerenciadorDeAmbientes != null) { // Fallback se o jogador não tiver localização
            ambienteParaDesenhar = gerenciadorDeAmbientes.getAmbienteGlobalPadrao();
        }

        gameView.getCenaPane().desenharCena(jogador, inimigo, ambienteParaDesenhar, eventoClimaticoAtivo);
        gameView.atualizarInventario(jogador.getInventario());
        atualizarBotoesDeMovimento();
        if(controladorDeTurno != null) gameView.atualizarTurno(controladorDeTurno.getNumeroDoTurno());
        if(ambienteParaDesenhar != null) gameView.atualizarAmbienteAtual(ambienteParaDesenhar.getNome());
    }

    private void processarUsoDeItem(Item item) {
        if (item == null || (emConfronto && !(item instanceof Remedio || item instanceof Arma))) {
            exibirMensagem("Você não pode usar '" + (item != null ? item.getNome() : "") + "' agora.");
            return;
        }
        item.usar(jogador);
        gameView.appendOutput("Você usou: " + item.getNome() + "\n");
        if (item.getDurabilidade() == 1 && !(item instanceof Arma)) { // Armas não são consumidas no uso
            jogador.getInventario().removerItem(item.getNome());
        }
        atualizarTodaUI();
    }

    private void processarMovimento(Ambiente destino) {
        if (emConfronto || (controladorDeTurno != null && controladorDeTurno.isJogoTerminou())) return;

        gameView.limparLog();
        int custoEnergia = 15;
        if (jogador.getEnergia() >= custoEnergia) {
            jogador.setEnergia(jogador.getEnergia() - custoEnergia);

            // CORREÇÃO: Usando a assinatura correta do método
            gerenciadorDeAmbientes.mudarAmbienteAtualDoJogador(jogador, destino.getNome());

            gameView.appendOutput("Você viajou para " + destino.getNome() + ".\n");

            String resultadoEvento = gerenciadorDeEventos.sortearEExecutarEvento(jogador, destino, controladorDeTurno.getNumeroDoTurno());
            gameView.appendOutput(resultadoEvento);

            verificarEventosPosAcao();
            if (!emConfronto) {
                atualizarTodaUI();
            }
        } else {
            exibirMensagem("Energia insuficiente para viajar.");
            atualizarTodaUI(); // Atualiza a UI mesmo se a viagem falhar
        }
    }

    private void atualizarBotoesDeMovimento() {
        if (gameView == null || gerenciadorDeAmbientes == null || jogador == null || jogador.getLocalizacaoAtual() == null) return;
        List<Ambiente> destinos = gerenciadorDeAmbientes.getAmbientesAdjacentes(jogador.getLocalizacaoAtual().getNome());
        gameView.atualizarPainelMovimento(destinos, this::processarMovimento, emConfronto);
    }

    private void verificarEventosPosAcao() { // Renomeado e lógica ajustada
        Evento eventoSorteado = gerenciadorDeEventos.getEventoSorteadoAtual();
        if (eventoSorteado != null) {
            if (eventoSorteado instanceof EventoCriatura) {
                this.eventoConfrontoAtual = (EventoCriatura) eventoSorteado;
                entrarModoConfronto();
            } else if (eventoSorteado instanceof EventoClimatico) {
                this.eventoClimaticoAtivo = (EventoClimatico) eventoSorteado;
                // A mensagem do evento climático já é dada pelo evento.executar()
                atualizarTodaUI(); // Atualiza para mostrar mudança de clima
            }
            // Eventos de Descoberta e DoencaFerimento já exibem suas mensagens via executar()
            // e não precisam de tratamento especial aqui, a menos que alterem estado que exija UI imediata.
            gerenciadorDeEventos.limparEventoSorteadoAtual(); // Limpa após processar
        }
    }

    private void entrarModoConfronto() {
        this.emConfronto = true;
        gameView.limparLegenda();
        atualizarTodaUI(); // Desenha o inimigo
        gameView.mostrarControlesCombate(
                "Um " + eventoConfrontoAtual.getTipoDeCriatura() + " selvagem aparece!",
                this::processarAcaoCombate
        );
    }

    private void sairModoConfronto(String mensagem) {
        gameView.exibirMensagem(mensagem);
        gameView.esconderControlesCombate();
        emConfronto = false;
        eventoConfrontoAtual = null;
        gerenciadorDeEventos.limparEventoSorteadoAtual();
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
        gameView.setControlesCombateDesabilitado(true);

        int danoJogador = jogador.calcularDanoBase();
        eventoConfrontoAtual.receberDano(danoJogador);
        exibirMensagem(jogador.getNome() + " ataca!", "A criatura sofre " + danoJogador + " de dano.");
        atualizarTodaUI();

        if (eventoConfrontoAtual.getVidaAtualCriatura() <= 0) {
            PauseTransition vitoriaDelay = new PauseTransition(Duration.seconds(1.5));
            vitoriaDelay.setOnFinished(e -> sairModoConfronto("Você venceu o combate!"));
            vitoriaDelay.play();
            return;
        }

        PauseTransition delayAtaqueInimigo = new PauseTransition(Duration.seconds(1.5));
        delayAtaqueInimigo.setOnFinished(event -> {
            try {
                if (!emConfronto) return; // Verifica se o combate ainda está ativo
                int danoCriatura = eventoConfrontoAtual.calcularDanoEfetivoCriatura(controladorDeTurno.getNumeroDoTurno());
                jogador.setVida(jogador.getVida() - danoCriatura);
                exibirMensagem("O inimigo revida!", "Você sofre " + danoCriatura + " de dano.");
                atualizarTodaUI();

                if (jogador.getVida() <= 0) {
                    controladorDeTurno.forcarFimDeJogo("DERROTA\n" + jogador.getNome() + " foi derrotado em combate.");
                    finalizarJogo();
                } else {
                    gameView.setControlesCombateDesabilitado(false);
                }
            } catch (Exception e) {
                System.err.println("### ERRO INESPERADO NO COMBATE ###");
                e.printStackTrace();
            }
        });
        delayAtaqueInimigo.play();
    }

    private void processarFuga() {
        if (!emConfronto || eventoConfrontoAtual == null) return;
        gameView.setControlesCombateDesabilitado(true);
        exibirMensagem("Você tenta fugir...");

        PauseTransition delayFuga = new PauseTransition(Duration.seconds(1.5));
        delayFuga.setOnFinished(event -> {
            if (random.nextDouble() < 0.5) {
                sairModoConfronto("Você conseguiu escapar!");
            } else {
                if (!emConfronto) return;
                int danoCriatura = eventoConfrontoAtual.calcularDanoEfetivoCriatura(controladorDeTurno.getNumeroDoTurno());
                jogador.setVida(jogador.getVida() - danoCriatura);
                exibirMensagem("A fuga falhou!", "Você sofre " + danoCriatura + " de dano.");
                atualizarTodaUI();

                if (jogador.getVida() <= 0) {
                    controladorDeTurno.forcarFimDeJogo("DERROTA\n" + jogador.getNome() + " foi derrotado ao tentar fugir.");
                    finalizarJogo();
                } else {
                    gameView.setControlesCombateDesabilitado(false);
                }
            }
        });
        delayFuga.play();
    }

    private void exibirMensagem(String... mensagens) {
        gameView.exibirMensagem(mensagens);
    }

    private void finalizarJogo() {
        gameView.desabilitarAcoes();
        if (emConfronto) {
            // Não precisa exibir mensagem aqui, o popup cuidará disso.
            // Apenas garante que saia do modo de confronto.
            gameView.esconderControlesCombate();
            emConfronto = false;
            eventoConfrontoAtual = null;
            gerenciadorDeEventos.limparEventoSorteadoAtual();
        }
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
        alert.setHeaderText("Você Morreu!"); // Simplificado
        alert.setContentText(conteudo.replace("FIM DE JOGO - DERROTA\n", "").trim());
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
        alert.setHeaderText("Parabéns, Você Venceu!"); // Simplificado
        alert.setContentText(conteudo.replace("FIM DE JOGO - VITÓRIA!\n", "").trim());
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