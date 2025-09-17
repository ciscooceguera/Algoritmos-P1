package DeckOfCards;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Pila <T> implements Iterable<T> {
    private T[] pila;
    private int tope, size;
    public Pila(int size){
        pila = (T[]) new Object[size];
        this.size = size;
        tope = -1;
    }
    public boolean isEmpty(){
        return tope == -1;
    }
    public boolean isFull(){
        return tope == size;
    }
    public int size(){
        return size;
    }
    public void push(T dato){
        tope+=1;
        if (tope == size){
            System.out.println("Desbordamiento");
            return;
        }
        pila[tope] = dato;
    }
    public T pop(){
        if (tope == -1){
            System.out.println("Subdesbordamiento");
            return null;
        }
        T dato = pila[tope];
        tope-=1;
        return dato;
    }
    public T peek(){
        if (tope == -1){
            System.out.println("Pila vacia");
            return null;
        }
        return pila[tope];
    }
    public Iterator<T> iterator(){
        return new Iterator<T>(){
            private int indice = tope;

            public boolean hasNext(){
                return indice >= 0;
            }

            public T next(){
                if (!hasNext()){
                    throw new NoSuchElementException();
                }
                return pila[indice--];
            }
        };
    }

    public void pushAll(Pila<T> pila){
        for (T dato : pila){
            push(dato);
        }
    }
}
