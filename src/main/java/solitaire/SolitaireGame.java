package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;
import DeckOfCards.Pila;

import java.util.ArrayList;
/**
 * Juego de solitario.
 *
 * @author (Cecilia Curlango Rosas)
 * @version (2025-2)
 */
public class SolitaireGame {
    private ArrayList<TableauDeck> tableau = new ArrayList<>();
    private ArrayList<FoundationDeck> foundation = new ArrayList<>();
    private FoundationDeck lastFoundationUpdated;
    private DrawPile drawPile;
    private WastePile wastePile;
    private Pila<RegistroMovimiento> registroMovimientos;

    public SolitaireGame() {
        drawPile = new DrawPile();
        wastePile = new WastePile();
        registroMovimientos = new Pila<>(1000);
        createTableaux();
        createFoundations();
        wastePile.addCartas(drawPile.retirarCartas());
    }

    /**
     * Move cards from Waste pile to Draw Pile.
     */
    public void reloadDrawPile() {
        ArrayList<CartaInglesa> cards = wastePile.emptyPile();
        drawPile.recargar(cards);
        if (!cards.isEmpty()) {
            registroMovimientos.push(RegistroMovimiento.recargar(cards.size()));
        }
    }

    /**
     * Move cards from Draw pile to Waste Pile.
     */
    public void drawCards() {
        ArrayList<CartaInglesa> cards = drawPile.retirarCartas();
        wastePile.addCartas(cards);
        if (!cards.isEmpty()) {
            registroMovimientos.push(RegistroMovimiento.draw(cards.size()));
        }
    }


    /**
     * Tomar la carta del Waste pile y ponerla en el tableau
     *
     * @param tableauDestino donde se coloca la carta
     * @return true si se pudo hacer el movimiento, false si no
     */
    public boolean moveWasteToTableau(int tableauDestino) {
        boolean movimientoRealizado = false;
        TableauDeck destino = tableau.get(tableauDestino - 1);
        CartaInglesa carta = wastePile.verCarta();
        if (moveWasteToTableau(destino)) {
            carta = wastePile.getCarta();
            registroMovimientos.push(RegistroMovimiento.waste2Tableau(carta, tableauDestino-1));
            movimientoRealizado = true;
        }
        return movimientoRealizado;
    }

    /**
     * Tomar varias cartas del Tableau fuente y colocarlas en el
     * Tableau destino.
     *
     * @param tableauFuente  de donde se toma la carta (1-7)
     * @param tableauDestino donde se coloca la carta (1-7)
     * @return true si se pudo hacer el movimiento, false si no
     */
    public boolean moveTableauToTableau(int tableauFuente, int tableauDestino) {
        boolean movimientoRealizado = false;
        TableauDeck fuente = tableau.get(tableauFuente - 1);
        if (!fuente.isEmpty()) {
            TableauDeck destino = tableau.get(tableauDestino - 1);

            int valorQueDebeTenerLaCartaInicialDeLaFuente;
            CartaInglesa cartaUltimaDelDestino;
            if (!destino.isEmpty()) {
                cartaUltimaDelDestino = destino.verUltimaCarta();
                valorQueDebeTenerLaCartaInicialDeLaFuente = cartaUltimaDelDestino.getValor() - 1;
            } else {
                valorQueDebeTenerLaCartaInicialDeLaFuente = 13;
            }
            CartaInglesa cartaInicialDePrueba = fuente.viewCardStartingAt(valorQueDebeTenerLaCartaInicialDeLaFuente);
            if (cartaInicialDePrueba != null && destino.sePuedeAgregarCarta(cartaInicialDePrueba)) {
                ArrayList<CartaInglesa> cartas = fuente.removeStartingAt(valorQueDebeTenerLaCartaInicialDeLaFuente);
                if (destino.agregarBloqueDeCartas(cartas)) {
                    boolean volteo = false;
                    if (!fuente.isEmpty()) {
                        CartaInglesa nuevaSuperior = fuente.verUltimaCarta();
                        if (!nuevaSuperior.isFaceup()) {
                            nuevaSuperior.makeFaceUp();
                            volteo = true;
                        }
                    }

                    registroMovimientos.push(
                            RegistroMovimiento.tableau2Tableau(
                                    tableauFuente - 1,
                                    tableauDestino - 1,
                                    cartas.size(),
                                    volteo
                            )
                    );
                    movimientoRealizado = true;
                }
            }
        }
        return movimientoRealizado;
    }


    /**
     * Tomar la carta de Tableau y colocarla en el Foundation.
     *
     * @param numero de tableau donde se moverá la carta (1-7)
     * @return true si se pudo move la carta, false si no
     */
    public boolean moveTableauToFoundation(int numero) {
        boolean movimientoRealizado = false;

        TableauDeck fuente = tableau.get(numero - 1);
        if (fuente.isEmpty()) {
            return false;
        }
        CartaInglesa cartaPenultima = fuente.getPenultimaCarta();
        boolean volteo = (cartaPenultima != null && !cartaPenultima.isFaceup());

        CartaInglesa carta = fuente.removerUltimaCarta();
        if (carta == null){
            return false;
        }
        if (moveCartaToFoundation(carta)) {
            int foundationIdx = carta.getPalo().ordinal();
            registroMovimientos.push(RegistroMovimiento.tableau2Foundation(numero-1, foundationIdx, carta, volteo));
            movimientoRealizado = true;
        } else {
            // regresar la carta al tableau porque no se puede hacer el movimiento
            fuente.agregarCarta(carta);
        }
        return movimientoRealizado;
    }

    /**
     * Tomar la carta de Waste y colocarla en el Tableau.
     *
     * @param tableau donde se moverá la carta
     * @return true si se pudo move la carta, false si no
     */
    public boolean moveWasteToTableau(TableauDeck tableau) {
        boolean movimientoRealizado = false;

        CartaInglesa carta = wastePile.verCarta();
        if (moveCartaToTableau(carta, tableau)) {
            // si es movimiento válido, elimina la carta de la pila
            carta = wastePile.getCarta();
            movimientoRealizado = true;
        }
        return movimientoRealizado;
    }

    /**
     * Tomar una carta de Waste y ponerla en una de las Foundations.
     *
     * @return true si se pudo hacer el movimiento.
     */
    public boolean moveWasteToFoundation() {
        boolean movimientoRealizado = false;

        CartaInglesa carta = wastePile.verCarta();
        if (moveCartaToFoundation(carta)) {
            // si es movimiento válido, elimina la carta de la pila
            carta = wastePile.getCarta();
            int foundationIdx = carta.getPalo().ordinal();
            registroMovimientos.push(RegistroMovimiento.waste2Foundation(carta, foundationIdx));
            movimientoRealizado = true;
        }
        return movimientoRealizado;
    }

    /**
     * Coloca la carta recibida en el Tableau recibido.
     *
     * @param carta   a colocar
     * @param destino Tableau que recibe la carta.
     * @return true si se pudo hacer el movimiento, false si no
     */
    private boolean moveCartaToTableau(CartaInglesa carta, TableauDeck destino) {
        return destino.agregarCarta(carta);
    }

    /**
     * Coloca la carta recibida en el Foundation correspondiente.
     *
     * @param carta a colocar
     * @return true si se pudo hacer el movimiento, false si no.
     */
    private boolean moveCartaToFoundation(CartaInglesa carta) {
        int cualFoundation = carta.getPalo().ordinal();
        FoundationDeck destino = foundation.get(cualFoundation);
        lastFoundationUpdated = destino;
        return destino.agregarCarta(carta);
    }

    /**
     * Determina si se terminó el juego. El juego se
     * termina cuando todas las cartas están en Foundation
     *
     * @return true si se terminó el juego
     */
    public boolean isGameOver() {
        boolean gameOver = true;
        for (FoundationDeck foundation : foundation) {
            if (foundation.estaVacio()) {
                gameOver = false;
            } else {
                CartaInglesa ultimaCarta = foundation.getUltimaCarta();
                // si la última carta no es rey, no se ha terminado
                if (ultimaCarta.getValor() != 13) {
                    gameOver = false;
                }
            }
        }
        return gameOver;
    }

    private void createFoundations() {
        for (Palo palo : Palo.values()) {
            foundation.add(new FoundationDeck(palo));
        }
    }

    private void createTableaux() {
        for (int i = 0; i < 7; i++) {
            TableauDeck tableauDeck = new TableauDeck();
            tableauDeck.inicializar(drawPile.getCartas(i + 1));
            tableau.add(tableauDeck);
        }
    }

    public DrawPile getDrawPile() {
        return drawPile;
    }

    public ArrayList<TableauDeck> getTableau() {
        return tableau;
    }

    public WastePile getWastePile() {
        return wastePile;
    }

    public FoundationDeck getLastFoundationUpdated() {
        return lastFoundationUpdated;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        // add foundations
        str.append("Foundation\n");
        for (FoundationDeck foundationDeck : foundation) {
            str.append(foundationDeck);
            str.append("\n");
        }

        // add tableaux
        str.append("\nTableaux\n");
        int tableauNumber = 1;
        for (TableauDeck tableauDeck : tableau) {
            str.append(tableauNumber + " ");
            str.append(tableauDeck);
            str.append("\n");
            tableauNumber++;
        }
        str.append("Waste\n");
        str.append(wastePile);
        str.append("\nDraw\n");
        str.append(drawPile);
        return str.toString();
    }

    public FoundationDeck getFoundation(int index) {
        return foundation.get(index); // índice 0..3 en el mismo orden de Palo.values()
    }

    public int getFoundationCount() {
        return foundation.size(); // normalmente 4
    }
    /*
    * Lógica del undo, utilizando la pila de tipo RegistroMovimiento, dependiendo del caso
    * se ejecuta el movimiento contrario
     */
    public boolean undo() {
        if (registroMovimientos.isEmpty()) return false;

        RegistroMovimiento r = registroMovimientos.pop();

        switch (r.tipoMovimiento) {
            case "DRAW": {
                for (int i = 0; i < r.cantidad; i++) {
                    CartaInglesa c = wastePile.getCarta();
                    if (c != null) {
                        drawPile.regresarMovimiento(c);
                    }
                }
                return true;
            }
            case "RECARGA": {
                ArrayList<CartaInglesa> devueltas = drawPile.popN(r.cantidad);
                for (CartaInglesa c : devueltas) {
                    c.makeFaceUp();
                }
                wastePile.addCartas(devueltas);
                return true;
            }
            case "W2T": {
                TableauDeck dest = tableau.get(r.destinoTableau);
                CartaInglesa top = dest.removerUltimaCarta();
                wastePile.addCarta(top);
                return true;
            }
            case "W2F": {
                FoundationDeck f = foundation.get(r.foundation);
                CartaInglesa top = f.removerUltimaCarta();
                wastePile.addCarta(top);
                return true;
            }
            case "T2T": {
                TableauDeck src = tableau.get(r.origenTableau);
                TableauDeck dst = tableau.get(r.destinoTableau);

                ArrayList<CartaInglesa> bloque = dst.removerUltima(r.cantidad);


                if (r.volteo) {
                    CartaInglesa u = src.getUltimaCarta();
                    if (u != null) u.makeFaceDown();
                }


                src.agregarDirecto(bloque);
                return true;
            }
            case "T2F": {
                TableauDeck src = tableau.get(r.origenTableau);
                FoundationDeck f = foundation.get(r.foundation);


                CartaInglesa c = f.removerUltimaCarta();
                if (c == null) return false;

                if (r.volteo) {
                    CartaInglesa card = src.getUltimaCarta();
                    if (card != null && card.isFaceup()){
                        card.makeFaceDown();
                    }
                }

                src.agregarCartaDirecto(c,true);
                return true;
            }
            default:
                return false;
        }
    }
    // Pila de Registro de Movimientos
    public boolean habilitarUndo(){
        return !registroMovimientos.isEmpty();
    }
    public int getNumUndos(){
        return registroMovimientos.size();
    }
}
