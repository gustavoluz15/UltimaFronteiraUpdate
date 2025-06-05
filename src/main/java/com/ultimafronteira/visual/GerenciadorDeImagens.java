package com.ultimafronteira.visual;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GerenciadorDeImagens {

    private static final Map<String, Image> imagens = new HashMap<>();

    public static void carregarImagens() {
        System.out.println("Iniciando carregamento de imagens para JavaFX...");
        try {
            carregar("fundo_caverna", "/Imagens/Caverna.jpg");
            carregar("fundo_cidade_ruinas", "/Imagens/CidadeRuinas.jpg");
            carregar("fundo_floresta", "/Imagens/Floresta.jpg");
            carregar("fundo_lago_rio", "/Imagens/LagoRio.jpg");
            carregar("fundo_pico_geada", "/Imagens/PicoGeada.jpg");

            carregar("fundo_tempestade", "/Imagens/Tempestade.jpeg");
            carregar("fundo_calor", "/Imagens/Calor.jpg");
            carregar("fundo_pico_geada_tempestade", "/Imagens/PicoGeadaTempestade.jpeg");
            carregar("fundo_pico_geada_nevasca", "/Imagens/PicoGeadaNevasca.jpeg");


            carregar("personagem_mecanico", "/Imagens/Mecanico.png");
            carregar("personagem_medico", "/Imagens/Medico.png");
            carregar("personagem_rastreador", "/Imagens/Rastreador.png");
            carregar("personagem_sobrevivente", "/Imagens/Sobrevivente.png");


            carregar("criatura_cobra", "/Imagens/Cobra.png");
            carregar("criatura_corvo", "/Imagens/Corvo.png");
            carregar("criatura_lobo", "/Imagens/Lobo.png");


            carregar("ui_inventario", "/Imagens/Inventario.jpg");
            carregar("item_comida_frutas", "/Imagens/Frutas.jpg");
            carregar("item_agua", "/Imagens/Agua.jpg");

        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO: Não foi possível carregar os recursos de imagem.");
            e.printStackTrace();
        }
    }

    private static void carregar(String chave, String caminho) {
        URL resourceUrl = GerenciadorDeImagens.class.getResource(caminho);
        if (resourceUrl != null) {
            imagens.put(chave, new Image(resourceUrl.toExternalForm()));
        } else {
            System.err.println("AVISO: Imagem não encontrada em: " + caminho + " (Chave: " + chave + ")");
        }
    }

    public static Image getImagem(String chave) {
        return imagens.get(chave);
    }

    public static ImageView getImageView(String chave, double width, double height) {
        ImageView imageView = new ImageView(getImagem(chave));
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(false);
        return imageView;
    }
}