package com.example.practica_1_algoritmos;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import solitaire.TableauDeck;

import java.util.ArrayList;

public class TableroGrafico {

    private final HBox contenedor = new HBox(16);    // Contiene las 7 columnas
    private final ColumnaGrafica[] columnas = new ColumnaGrafica[7];
    // Recibe las medidas de las cartas, y el espacio a dejar entre cartas si está volteada o no
    public TableroGrafico(double anchoCarta, double altoCarta, double gapCubierta, double gapDescubierta) {
        // Tablero de 7 columnas alineado a la izquierda
        contenedor.setAlignment(Pos.TOP_LEFT);
        contenedor.setFillHeight(false);
        // espacios en sus bordes
        contenedor.setPadding(new Insets(10, 16, 16, 16));

        // Crear 7 columnas gráficas
        for (int i = 0; i < 7; i++) {
            ColumnaGrafica tg = new ColumnaGrafica(anchoCarta, altoCarta, gapCubierta, gapDescubierta);
            columnas[i] = tg;
            contenedor.getChildren().add(tg.getCol());
        }
    }

    // Devuelve el HBox completo, el tablero con las columnas integradas
    public HBox getHBox() {
        return contenedor;
    }

    // Devuelve el Pane de una columna por índice
    public Pane getColumna(int idx0) {
        return columnas[idx0].getCol();
    }

    /*
     * Dibuja todas las columnas y devuelve un arreglo con la vista
     * de la carta superior descubierta (o null) en cada columna.
     */
    public StackPane[] dibujar(ArrayList<TableauDeck> tableau) {
        StackPane[] vistasSuperiores = new StackPane[7];
        for (int i = 0; i < 7 && i < tableau.size(); i++) {
            var deck = tableau.get(i);
            vistasSuperiores[i] = columnas[i].dibujar(deck.getCards());
        }
        return vistasSuperiores;
    }
}
