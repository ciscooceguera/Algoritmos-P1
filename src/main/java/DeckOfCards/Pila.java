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
        return tope < 0;
    }
    public boolean isFull(){
        return tope == size-1;
    }
    public int size(){
        return tope+1;
    }
    public void push(T dato){
        if (isFull()){
            throw new IllegalStateException("Desbordamiento");
        }
        tope+=1;
        pila[tope] = dato;
    }
    public T pop(){
        if (tope<0){
            throw new NoSuchElementException("Subdesbordamiento");
        }
        T dato = pila[tope];
        pila[tope] = null;
        tope-=1;
        return dato;
    }
    public T peek(){
        if (tope < 0){
            throw new NoSuchElementException("Pila vacía");
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
