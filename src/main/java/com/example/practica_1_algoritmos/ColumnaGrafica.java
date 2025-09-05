package com.example.practica_1_algoritmos;

import DeckOfCards.CartaInglesa;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import java.util.List;

public class ColumnaGrafica {

    private final Pane columna = new Pane();   // Pane para usar setTranslateY() por carta
    private final double ANCHO_CARTA, ALTO_CARTA, GAP_CUBIERTA, GAP_DESCUBIERTA;

    public ColumnaGrafica(double ancho, double alto, double gapCubierta, double gapDescubierta) {
        this.ANCHO_CARTA = ancho;
        this.ALTO_CARTA = alto;
        this.GAP_CUBIERTA = gapCubierta;
        this.GAP_DESCUBIERTA = gapDescubierta;

        /*
         *   Permite clickear aunque no hayan elementos dentro,
         *   lo utilicé para que cuando ya no hayan cartas,
         *   se pueda mover otras cartas a una columna vacía
         */
        columna.setPickOnBounds(true);
        columna.setPrefSize(ANCHO_CARTA, ALTO_CARTA + 6 * GAP_DESCUBIERTA);
        columna.setMinSize(ANCHO_CARTA, ALTO_CARTA);
        columna.setMaxWidth(ANCHO_CARTA);
        columna.setCursor(Cursor.HAND); // Cambiar el cursor a mano al pasar por encima
    }

    // Devuelve el nodo principal de esta columna
    public Pane getCol() {
        return columna;
    }

    /*
     * Dibuja la columna completa. Devuelve la vista de la carta superior
     * si está descubierta, en otro caso devuelve null.
     */
    public StackPane dibujar(List<CartaInglesa> cartas) {
        columna.getChildren().clear(); // Limpia para redibujar
        double y = 0;                  // desplazamiento en Y
        StackPane vistaSuperiorDescubierta = null;

        // Itera cada carta para dibujarla
        for (int i = 0; i < cartas.size(); i++) {
            CartaInglesa c = cartas.get(i);        // Carta actual
            boolean bocaAbajo = !c.isFaceup();     // Se determina si está volteada
            StackPane vista = CartaGrafica.crear(c, bocaAbajo, ANCHO_CARTA, ALTO_CARTA);
            vista.setTranslateY(y);                 // Se acomoda
            columna.getChildren().add(vista);       // Se añade a la columna

            // if-else ternario: si boca abajo usa gap cubierta, si no gap descubierta
            y += bocaAbajo ? GAP_CUBIERTA : GAP_DESCUBIERTA;

            // Si es la carta superior (última) y está descubierta, la regreso para hacerla clickeable
            if (i == cartas.size() - 1 && !bocaAbajo) {
                vistaSuperiorDescubierta = vista;
            }
        }
        return vistaSuperiorDescubierta;
    }
}
