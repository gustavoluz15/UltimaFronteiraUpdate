package com.ultimafronteira.visual;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class GerenciadorDeImagens {

    private static final Map<String, Image> cacheDeImagens = new HashMap<>();
    private static final Map<String, String> caminhosDasImagens = new HashMap<>();

    public static void carregarImagens() {
        System.out.println("Registrando caminhos das imagens...");
        // Personagens
        caminhosDasImagens.put("personagem_medico", "/Imagens/medico.png");
        caminhosDasImagens.put("personagem_mecanico", "/Imagens/mecanico.png");
        caminhosDasImagens.put("personagem_rastreador", "/Imagens/rastreador.png");
        caminhosDasImagens.put("personagem_sobrevivente", "/Imagens/sobrevivente.png");

        // Criaturas
        caminhosDasImagens.put("criatura_lobo", "/Imagens/lobo.png");
        caminhosDasImagens.put("criatura_cobra", "/Imagens/cobra.png");
        caminhosDasImagens.put("criatura_corvo", "/Imagens/corvo.png");

        // Ambientes e Clima
        caminhosDasImagens.put("fundo_floresta", "/Imagens/floresta.jpg");
        caminhosDasImagens.put("fundo_caverna", "/Imagens/caverna.jpg");
        caminhosDasImagens.put("fundo_montanha", "/Imagens/montanha.jpg");
        caminhosDasImagens.put("fundo_ruinas", "/Imagens/ruinas.jpg");
        caminhosDasImagens.put("fundo_rio", "/Imagens/rio.jpg"); // Usado por fundo_lago_rio
        caminhosDasImagens.put("fundo_pico_geada", "/Imagens/montanha.jpg"); // Pode ser a mesma da montanha
        caminhosDasImagens.put("fundo_lago_rio", "/Imagens/rio.jpg");
        caminhosDasImagens.put("fundo_cidade_ruinas", "/Imagens/ruinas.jpg");
        caminhosDasImagens.put("fundo_tempestade", "/Imagens/tempestade.jpg");
        caminhosDasImagens.put("fundo_calor", "/Imagens/deserto.jpg"); // Ex: Imagem para calor
        caminhosDasImagens.put("fundo_pico_geada_nevasca", "/Imagens/montanha_nevasca.jpg"); // Ex: Imagem específica
        caminhosDasImagens.put("fundo_pico_geada_tempestade", "/Imagens/montanha_tempestade.jpg"); // Ex: Imagem específica


        // Itens (Exemplos, adicione as suas)
        caminhosDasImagens.put("item_comida_frutas", "/Imagens/frutas.png");
        caminhosDasImagens.put("item_cogumelo", "/Imagens/cogumelo.png");
        caminhosDasImagens.put("item_madeira", "/Imagens/madeira.png");
        caminhosDasImagens.put("item_pedra", "/Imagens/pedra.png");
        caminhosDasImagens.put("item_cristal", "/Imagens/cristal.png");
        caminhosDasImagens.put("item_agua", "/Imagens/agua.png");
        caminhosDasImagens.put("item_peixe", "/Imagens/peixe.png");
        caminhosDasImagens.put("item_juncos", "/Imagens/juncos.png");
        caminhosDasImagens.put("item_pedra_afiada", "/Imagens/pedra_afiada.png");
        caminhosDasImagens.put("item_minerio_ferro", "/Imagens/minerio_ferro.png");
        caminhosDasImagens.put("item_metal", "/Imagens/metal.png");
        caminhosDasImagens.put("item_tijolo", "/Imagens/tijolo.png");
        caminhosDasImagens.put("item_ferramenta", "/Imagens/ferramenta.png");
        caminhosDasImagens.put("item_enlatado", "/Imagens/enlatado.png");
        caminhosDasImagens.put("item_racao", "/Imagens/racao.png");
        caminhosDasImagens.put("item_carne_crua", "/Imagens/carne_crua.png");


        // UI
        caminhosDasImagens.put("ui_inventario", "/Imagens/inventario.jpg"); // Se você tiver
    }

    public static Image getImagem(String chave) {
        if (chave == null || chave.isEmpty()) {
            // System.err.println("AVISO: Tentativa de carregar imagem com chave nula ou vazia.");
            return null;
        }
        if (cacheDeImagens.containsKey(chave)) {
            return cacheDeImagens.get(chave);
        }
        String caminho = caminhosDasImagens.get(chave);
        if (caminho == null) {
            System.err.println("AVISO: Chave de imagem não registrada no GerenciadorDeImagens: " + chave);
            return null;
        }
        try (InputStream stream = GerenciadorDeImagens.class.getResourceAsStream(caminho)) {
            if (stream == null) {
                System.err.println("ERRO: Arquivo de imagem não encontrado em: " + caminho + " (Chave: " + chave + ")");
                return null;
            }
            Image imagem = new Image(stream);
            cacheDeImagens.put(chave, imagem);
            return imagem;
        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO ao carregar imagem: " + caminho + " (Chave: " + chave + ")");
            e.printStackTrace();
            return null;
        }
    }

    public static ImageView getImageView(String chave, double largura, double altura) {
        Image img = getImagem(chave);
        ImageView imageView = new ImageView(img); // ImageView lida com imagem nula (não desenha)
        imageView.setFitWidth(largura);
        imageView.setFitHeight(altura);
        imageView.setPreserveRatio(true);
        return imageView;
    }
}