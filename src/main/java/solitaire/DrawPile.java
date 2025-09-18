package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Mazo;
import DeckOfCards.Pila;

import java.util.ArrayList;

/**
 * Modela un mazo de cartas de solitario.
 * @author Cecilia Curlango
 * @version 2025
 */
public class DrawPile {
    private Pila<CartaInglesa> cartas;
    private int cuantasCartasSeEntregan = 3;

    public DrawPile() {
        Mazo mazo = new Mazo();
        cartas = new Pila<> (52);
        ArrayList<CartaInglesa> cartasArray = mazo.getCartas();

        for (int i = cartasArray.size() - 1; i >= 0; i--) {
            cartas.push(cartasArray.get(i));
        }
        setCuantasCartasSeEntregan(3);
    }

    /**
     * Establece cuantas cartas se sacan cada vez.
     * Puede ser 1 o 3 normalmente.
     * @param cuantasCartasSeEntregan
     */
    public void setCuantasCartasSeEntregan(int cuantasCartasSeEntregan) {
        this.cuantasCartasSeEntregan = cuantasCartasSeEntregan;
    }

    /**
     * Regresa la cantidad de cartas que se sacan cada vez.
     * @return cantidad de cartas que se entregan
     */
    public int getCuantasCartasSeEntregan() {
        return cuantasCartasSeEntregan;
    }

    /**
     * Retirar una cantidad de cartas. Este método se utiliza al inicio
     * de una partida para cargar las cartas de los tableaus.
     * Si se tratan de remover más cartas de las que hay,
     * se provocará un error.
     * @param cantidad de cartas que se quieren a retirar
     * @return cartas retiradas
     */
    public ArrayList<CartaInglesa> getCartas(int cantidad) {
        ArrayList<CartaInglesa> retiradas = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            retiradas.add(cartas.pop());
        }
        return retiradas;
    }

    /**
     * Retira y entrega las cartas del monton. La cantidad que retira
     * depende de cuántas cartas quedan en el montón y serán hasta el máximo
     * que se configuró inicialmente.
     * @return Cartas retiradas.
     */
    public ArrayList<CartaInglesa> retirarCartas() {
        ArrayList<CartaInglesa> retiradas = new ArrayList<>();
        int maximoARetirar = Math.min(cartas.size(), cuantasCartasSeEntregan);

        for (int i = 0; i < maximoARetirar && !cartas.isEmpty(); i++) {
            CartaInglesa retirada = cartas.pop();
            retirada.makeFaceUp();
            retiradas.add(retirada);
        }
        return retiradas;
    }

    /**
     * Indica si aún quedan cartas para entregar.
     * @return true si hay cartas, false si no.
     */
    public boolean hayCartas() {
        return !cartas.isEmpty();
    }

    public CartaInglesa verCarta() {
        return cartas.isEmpty() ? null : cartas.peek();
    }
    /**
     * Agrega las cartas recibidas al monton y las voltea
     * para que no se vean las caras.
     * @param cartasAgregar cartas que se agregan
     */
    public void recargar(ArrayList<CartaInglesa> cartasAgregar) {
        for (CartaInglesa carta : cartasAgregar) {
            carta.makeFaceDown();
        }
        while (!cartas.isEmpty()) {
            cartas.pop();
        }

        for (int i = 0; i< cartasAgregar.size(); i++) {
            cartas.push(cartasAgregar.get(i));
        }
    }

    @Override
    public String toString() {
        if (cartas.isEmpty()) {
            return "-E-";
        }
        return "@";
    }
    public void regresarMovimiento(CartaInglesa carta) {
        carta.makeFaceDown();
        this.cartas.push(carta);
    }
    public ArrayList<CartaInglesa> popN(int N){
        ArrayList<CartaInglesa> retiradas = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            retiradas.add(cartas.pop());
        }
        return retiradas;
    }
}
