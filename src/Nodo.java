/**
 * Nodo doblemente enlazado.
 *
 * Cada nodo guarda un dato y dos referencias:
 *  - izquierda: apunta al nodo anterior (hacia el inicio de la estructura).
 *  - derecha:   apunta al nodo siguiente (hacia el final de la estructura).
 *
 * Lista, Cola y Pila se construyen encadenando nodos de este tipo.
 * Ninguna de ellas usa arreglos ni colecciones de Java: toda la memoria
 * se maneja moviendo estas dos referencias.
 */
public class Nodo {

    private Object dato;
    private Nodo izquierda;
    private Nodo derecha;

    public Nodo(Object dato) {
        this.dato = dato;
        this.izquierda = null;
        this.derecha = null;
    }

    public Object getDato() {
        return dato;
    }

    public void setDato(Object dato) {
        this.dato = dato;
    }

    public Nodo getIzquierda() {
        return izquierda;
    }

    public void setIzquierda(Nodo izquierda) {
        this.izquierda = izquierda;
    }

    public Nodo getDerecha() {
        return derecha;
    }

    public void setDerecha(Nodo derecha) {
        this.derecha = derecha;
    }
}
