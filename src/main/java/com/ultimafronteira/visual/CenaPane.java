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

    public CenaPane() {
        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());
        getChildren().add(canvas);
    }

    public void desenharCena(Personagem jogador, EventoCriatura inimigo, Ambiente ambiente, EventoClimatico clima) {
        if (ambiente == null) return;
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

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
                double pHeight = canvas.getHeight() * 0.3;
                double pWidth = spriteJogador.getWidth() * (pHeight / spriteJogador.getHeight());
                double pX = canvas.getWidth() * 0.15;
                double pY = canvas.getHeight() - pHeight - (canvas.getHeight() * 0.05);
                gc.drawImage(spriteJogador, pX, pY, pWidth, pHeight);
                desenharBarrasDeStatus(gc, jogador, pX, pY, pWidth);
            }
        }

        if (inimigo != null) {
            Image spriteInimigo = GerenciadorDeImagens.getImagem(inimigo.getChaveImagem());
            if (spriteInimigo != null) {
                double eHeight = canvas.getHeight() * 0.225;
                double eWidth = spriteInimigo.getWidth() * (eHeight / spriteInimigo.getHeight());
                double eX = canvas.getWidth() * 0.85 - eWidth;
                double eY = canvas.getHeight() - eHeight - (canvas.getHeight() * 0.05);
                gc.drawImage(spriteInimigo, eX, eY, eWidth, eHeight);
                desenharBarra(gc, inimigo.getTipoDeCriatura(), inimigo.getVidaAtualCriatura(), inimigo.getVidaMaximaCriatura(), eX, eY - 20, eWidth, 12, Color.CRIMSON);
            }
        }
    }

    private void desenharBarrasDeStatus(GraphicsContext gc, Personagem jogador, double pX, double pY, double pWidth) {
        double barWidth = pWidth < 150 ? 150 : pWidth;
        double barHeight = 12;
        double spacing = 4;
        double startY = pY - (barHeight + spacing) * 5 - 15;

        desenharBarra(gc, "Vida", jogador.getVida(), jogador.getVidaMaxima(), pX, startY, barWidth, barHeight, Color.LIMEGREEN);
        desenharBarra(gc, "Energia", jogador.getEnergia(), 100, pX, startY + (barHeight + spacing), barWidth, barHeight, Color.DEEPSKYBLUE);
        desenharBarra(gc, "Fome", jogador.getFome(), 100, pX, startY + (barHeight + spacing) * 2, barWidth, barHeight, Color.GOLD);
        desenharBarra(gc, "Sede", jogador.getSede(), 100, pX, startY + (barHeight + spacing) * 3, barWidth, barHeight, Color.ROYALBLUE);
        desenharBarra(gc, "Sanidade", jogador.getSanidade(), 100, pX, startY + (barHeight + spacing) * 4, barWidth, barHeight, Color.MEDIUMORCHID);
    }

    private void desenharBarra(GraphicsContext gc, String nome, int atual, int maximo, double x, double y, double w, double h, Color cor) {
        gc.setFill(Color.rgb(0, 0, 0, 0.6));
        gc.fillRoundRect(x - 2, y - 2, w + 4, h + 4, 12, 12);
        gc.setFill(Color.gray(0.2));
        gc.fillRoundRect(x, y, w, h, 10, 10);
        if (maximo > 0 && atual > 0) {
            double percentual = (double) atual / maximo;
            gc.setFill(cor);
            gc.fillRoundRect(x, y, w * percentual, h, 10, 10);
        }
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        gc.fillText(nome + ": " + atual + "/" + maximo, x + 5, y + h - 2);
    }
}