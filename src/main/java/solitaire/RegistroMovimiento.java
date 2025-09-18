package solitaire;

import DeckOfCards.CartaInglesa;

import java.util.ArrayList;

public class RegistroMovimiento {
     String tipoMovimiento;

     int origenTableau = -1;
     int destinoTableau = -1;
     int foundation = -1;
     int cantidad = 0;
     boolean volteo = false;

     CartaInglesa carta;
     ArrayList<CartaInglesa> cartas;

    public static RegistroMovimiento draw(int n) {
        RegistroMovimiento movimiento = new RegistroMovimiento();
        movimiento.tipoMovimiento = "DRAW";
        movimiento.cantidad = n;
        return movimiento;
    }
    public static RegistroMovimiento recargar(int n) {
        RegistroMovimiento movimiento = new RegistroMovimiento();
        movimiento.tipoMovimiento = "RECARGA";
        movimiento.cantidad = n;
        return movimiento;
    }
    public static RegistroMovimiento waste2Tableau(CartaInglesa carta, int toTableau) {
        RegistroMovimiento movimiento = new RegistroMovimiento();
        movimiento.tipoMovimiento = "W2T";
        movimiento.carta = carta;
        movimiento.destinoTableau = toTableau;
        return movimiento;
    }
    public static RegistroMovimiento waste2Foundation(CartaInglesa carta, int foundationIdx) {
        RegistroMovimiento movimiento = new RegistroMovimiento();
        movimiento.tipoMovimiento = "W2F";
        movimiento.carta = carta;
        movimiento.foundation = foundationIdx;
        return movimiento;
    }
    public static RegistroMovimiento tableau2Tableau(int origen, int destino, int n, boolean volteo){
        RegistroMovimiento movimiento = new RegistroMovimiento();
        movimiento.tipoMovimiento = "T2T";
        movimiento.origenTableau = origen;
        movimiento.destinoTableau = destino;
        movimiento.cantidad = n;
        movimiento.volteo = volteo;
        return movimiento;
    }
    public static RegistroMovimiento tableau2Foundation(int origen, int foundationIdx, CartaInglesa carta, boolean volteo){
        RegistroMovimiento movimiento = new RegistroMovimiento();
        movimiento.tipoMovimiento = "T2F";
        movimiento.origenTableau = origen;
        movimiento.foundation = foundationIdx;
        movimiento.carta = carta;
        movimiento.volteo = volteo;
        return movimiento;
    }
}
