package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Pila;

import java.util.ArrayList;
/**
 * Modela el montículo donde se colocan las cartas
 * que se extraen de Draw pile.
 *
 * @author (Cecilia Curlango Rosas)
 * @version (2025-2)
 */
public class WastePile {
    private Pila<CartaInglesa> cartas;

    public WastePile() {
        cartas = new Pila<>(52);
    }

    public void addCartas(ArrayList<CartaInglesa> nuevas) {
        for (CartaInglesa carta : nuevas) {
            cartas.push(carta);
        }
    }

    public void addCarta(CartaInglesa carta) {
        if (carta != null){
            cartas.push(carta);
        }
    }

    public ArrayList<CartaInglesa> emptyPile() {
        ArrayList<CartaInglesa> pile = new ArrayList<>();
        while (!cartas.isEmpty()) {
            pile.add(cartas.pop());
        }
        return pile;
    }

    /**
     * Obtener la última carta sin removerla.
     * @return Carta que está encima. Si está vacía, es null.
     */
    public CartaInglesa verCarta() {
        return cartas.isEmpty() ? null : cartas.peek();
    }
    public CartaInglesa getCarta() {
        return cartas.isEmpty() ? null : cartas.pop();
    }

    @Override
    public String toString() {
        return cartas.isEmpty() ? "[]" : cartas.peek().toString();
    }

    public boolean hayCartas() {
        return !cartas.isEmpty();
    }

}
