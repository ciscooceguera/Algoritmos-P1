package DeckOfCards;

public class Pila <T>{
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
}
