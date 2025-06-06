package com.ultimafronteira.visual;

import com.ultimafronteira.events.EventoClimatico;
import com.ultimafronteira.events.EventoCriatura;
import com.ultimafronteira.model.Personagem;
import com.ultimafronteira.world.Ambiente;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CenaPane extends Pane {
    private Canvas canvas;
    private GraphicsContext gc;

    // Define um tamanho fixo para a área de desenho do jogo.
    private static final double LARGURA_CENA = 900;
    private static final double ALTURA_CENA = 600;

    public CenaPane() {
        // CORREÇÃO DEFINITIVA: Criamos um canvas com tamanho fixo.
        // Removemos qualquer 'bind' ou 'listener' que ligue o tamanho do canvas
        // ao seu contêiner. Isso quebra o loop de redesenho infinito.
        canvas = new Canvas(LARGURA_CENA, ALTURA_CENA);
        gc = canvas.getGraphicsContext2D();

        // Centraliza o canvas dentro do Pane se o Pane for maior.
        // Isso é opcional, mas melhora o visual.
        canvas.layoutXProperty().bind(this.widthProperty().subtract(LARGURA_CENA).divide(2));
        canvas.layoutYProperty().bind(this.heightProperty().subtract(ALTURA_CENA).divide(2));

        getChildren().add(canvas);
    }

    public void desenharCena(Personagem jogador, EventoCriatura inimigo, Ambiente ambiente, EventoClimatico clima) {
        if (ambiente == null) return;

        // Limpa a tela com uma cor de fundo para o caso de a imagem falhar
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        String chaveFundoAtual = ambiente.getChaveImagemFundo();
        if (clima != null && clima.getChaveImagemFundoOverride() != null) {
            chaveFundoAtual = clima.getChaveImagemFundoOverride();
        }

        Image fundo = GerenciadorDeImagens.getImagem(chaveFundoAtual);
        if (fundo != null) {
            gc.drawImage(fundo, 0, 0, canvas.getWidth(), canvas.getHeight());
        }

        if (jogador != null) {
            Image spriteJogador = GerenciadorDeImagens.getImagem(jogador.getChaveImagem());
            if (spriteJogador != null) {
                double pHeight = canvas.getHeight() * 0.45; // Aumentado para melhor visualização
                double pWidth = spriteJogador.getWidth() * (pHeight / spriteJogador.getHeight());
                double pX = canvas.getWidth() * 0.15;
                double pY = canvas.getHeight() - pHeight - (canvas.getHeight() * 0.10); // Ajustado para subir um pouco
                gc.drawImage(spriteJogador, pX, pY, pWidth, pHeight);
                desenharBarrasDeStatus(gc, jogador, pX, pY, pWidth);
            }
        }

        if (inimigo != null) {
            Image spriteInimigo = GerenciadorDeImagens.getImagem(inimigo.getChaveImagem());
            if (spriteInimigo != null) {
                double eHeight = canvas.getHeight() * 0.35; // Aumentado
                double eWidth = spriteInimigo.getWidth() * (eHeight / spriteInimigo.getHeight());
                double eX = canvas.getWidth() * 0.85 - eWidth;
                double eY = canvas.getHeight() - eHeight - (canvas.getHeight() * 0.10); // Ajustado
                gc.drawImage(spriteInimigo, eX, eY, eWidth, eHeight);
                desenharBarra(gc, inimigo.getTipoDeCriatura(), inimigo.getVidaAtualCriatura(), inimigo.getVidaMaximaCriatura(), eX, eY - 20, eWidth, 15, Color.CRIMSON);
            }
        }
    }

    private void desenharBarrasDeStatus(GraphicsContext gc, Personagem jogador, double pX, double pY, double pWidth) {
        double barWidth = pWidth < 180 ? 180 : pWidth; // Aumentado
        double barHeight = 15; // Aumentado
        double spacing = 5;
        double startY = pY - (barHeight + spacing) * 5 - 20;

        desenharBarra(gc, "Vida", jogador.getVida(), jogador.getVidaMaxima(), pX, startY, barWidth, barHeight, Color.LIMEGREEN);
        desenharBarra(gc, "Energia", jogador.getEnergia(), 100, pX, startY + (barHeight + spacing), barWidth, barHeight, Color.DEEPSKYBLUE);
        desenharBarra(gc, "Fome", jogador.getFome(), 100, pX, startY + (barHeight + spacing) * 2, barWidth, barHeight, Color.GOLD);
        desenharBarra(gc, "Sede", jogador.getSede(), 100, pX, startY + (barHeight + spacing) * 3, barWidth, barHeight, Color.ROYALBLUE);
        desenharBarra(gc, "Sanidade", jogador.getSanidade(), 100, pX, startY + (barHeight + spacing) * 4, barWidth, barHeight, Color.MEDIUMORCHID);
    }

    private void desenharBarra(GraphicsContext gc, String nome, int atual, int maximo, double x, double y, double w, double h, Color cor) {
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRoundRect(x - 2, y - 2, w + 4, h + 4, 12, 12);
        gc.setFill(Color.gray(0.2));
        gc.fillRoundRect(x, y, w, h, 10, 10);
        if (maximo > 0 && atual > 0) {
            double percentual = (double) atual / maximo;
            gc.setFill(cor);
            gc.fillRoundRect(x, y, w * percentual, h, 10, 10);
        }
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        String texto = nome + ": " + atual + " / " + maximo;
        gc.fillText(texto, x + 5, y + h - 3);
    }
}