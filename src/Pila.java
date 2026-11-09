/**
 * Pila enlazada (LIFO: el último en entrar es el primero en salir).
 *
 * Solo se trabaja sobre el "tope". Cada nuevo nodo se coloca encima y su
 * referencia "izquierda" apunta al nodo que estaba debajo.
 *
 * En el sistema se usa como HISTORIAL DE ENTREGADOS: el tope siempre es el
 * último domicilio entregado, que es justo el que se debe revertir al deshacer.
 */
public class Pila {

    private Nodo tope;
    private int tamanio;

    public Pila() {
        tope = null;
        tamanio = 0;
    }

    /** Coloca el dato encima de la pila. */
    public void apilar(Object dato) {
        if (dato == null) {
            throw new IllegalArgumentException("No se permiten datos nulos");
        }
        Nodo nuevo = new Nodo(dato);
        if (tope != null) {
            nuevo.setIzquierda(tope); // el nuevo apunta al de abajo
            tope.setDerecha(nuevo);   // el de abajo apunta al nuevo
        }
        tope = nuevo;
        tamanio++;
    }

    /** Retira y devuelve el dato del tope. */
    public Object desapilar() {
        if (esVacia()) {
            throw new IllegalStateException("El historial está vacío");
        }
        Object dato = tope.getDato();
        tope = tope.getIzquierda();
        if (tope != null) {
            tope.setDerecha(null);
        }
        tamanio--;
        return dato;
    }

    public Object peek() {
        if (esVacia()) {
            throw new IllegalStateException("El historial está vacío");
        }
        return tope.getDato();
    }

    public int tamanio() {
        return tamanio;
    }

    public boolean esVacia() {
        return tamanio == 0;
    }

    public boolean contiene(Object dato) {
        return buscar(dato) != -1;
    }

    public void limpiar() {
        tope = null;
        tamanio = 0;
    }

    /** Muestra desde el tope (más reciente) hacia el fondo (más antiguo). */
    public void mostrar() {
        if (esVacia()) {
            System.out.println("  (historial vacío)");
            return;
        }
        Nodo actual = tope;
        int nivel = 1;
        while (actual != null) {
            System.out.println("  " + nivel + ". " + actual.getDato());
            actual = actual.getIzquierda();
            nivel++;
        }
    }

    /**
     * Distancia desde el tope (1 = está en el tope) o -1 si no está.
     * Es el mismo criterio que usa java.util.Stack.search().
     */
    public int buscar(Object dato) {
        Nodo actual = tope;
        int distancia = 1;
        while (actual != null) {
            if (actual.getDato().equals(dato)) {
                return distancia;
            }
            actual = actual.getIzquierda();
            distancia++;
        }
        return -1;
    }
}
