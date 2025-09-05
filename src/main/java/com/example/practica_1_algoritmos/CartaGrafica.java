package com.example.practica_1_algoritmos;

import DeckOfCards.CartaInglesa;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CartaGrafica {

    /*
     *   Crea lo visual de la carta, ya sea el dorso o la cara,
     *  recibe la Carta, su posición (volteada o no), y sus medidas Width y Heigth
     */
    public static StackPane crear(CartaInglesa c, boolean bocaAbajo, double ANCHO_CARTA, double ALTO_CARTA) {
        StackPane sp = new StackPane();                // Contenedor de la carta
        sp.setPrefSize(ANCHO_CARTA, ALTO_CARTA);      // Tamaño preferido

        Rectangle fondo = new Rectangle(ANCHO_CARTA, ALTO_CARTA); // Rectángulo base
        fondo.setArcWidth(12);                        // Bordes redondeados
        fondo.setArcHeight(12);

        // Texto central (símbolo grande del palo)
        Text centro = new Text();
        centro.setFont(Font.font(42));

        // Texto esquina
        Text esquina = new Text();
        esquina.setFont(Font.font(18));
        StackPane.setAlignment(esquina, Pos.TOP_LEFT);
        StackPane.setMargin(esquina, new Insets(6, 0, 0, 8));

        // Si está boca abajo dibuja el dorso
        if (bocaAbajo || c == null) {
            fondo.setFill(Color.web("#0e3a7b"));       // color dorso
            fondo.setStroke(Color.web("#e6eefc"));
            fondo.setStrokeWidth(2);
            centro.setText("");                        // sin textos
            esquina.setText("");
        } else {
            // Si está boca arriba dibuja la cara
            fondo.setFill(Color.WHITE);                // Fondo blanco
            fondo.setStroke(Color.BLACK);              // Borde negro
            fondo.setStrokeWidth(1.6);                 // Grosor del borde

            // Switch para cartas con valores J,Q,K,A: 11,12,13,14
            int v = c.getValorBajo();
            String rangoTxt;
            switch (v) {
                case 1  -> rangoTxt = "A";
                case 11 -> rangoTxt = "J";
                case 12 -> rangoTxt = "Q";
                case 13 -> rangoTxt = "K";
                default -> rangoTxt = String.valueOf(v);
            }
            // Se obtiene la simbología del palo: ♣ ♦ ♥ ♠
            String palo = c.getPalo().getFigura();
            esquina.setText(rangoTxt + palo);
            centro.setText(palo);

            // Color del texto rojo o negro
            Color col = "rojo".equalsIgnoreCase(c.getColor()) ? Color.CRIMSON : Color.BLACK;
            esquina.setFill(col);
            centro.setFill(col);
        }
        // Agregar al contenedor
        sp.getChildren().addAll(fondo, centro, esquina);
        return sp;
    }
}
