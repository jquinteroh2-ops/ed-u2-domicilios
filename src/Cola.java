/**
 * Cola enlazada (FIFO: el primero en entrar es el primero en salir).
 *
 * - frente: nodo que será atendido primero.
 * - fin:    último nodo que llegó.
 * Se encola por el fin y se desencola por el frente, ambos en O(1).
 *
 * En el sistema se usa para los DOMICILIOS PENDIENTES: los pedidos se
 * despachan en el mismo orden en que llegaron.
 */
public class Cola {

    private Nodo frente;
    private Nodo fin;
    private int tamanio;

    public Cola() {
        frente = null;
        fin = null;
        tamanio = 0;
    }

    /** Agrega por el final de la cola. */
    public void encolar(Object dato) {
        if (dato == null) {
            throw new IllegalArgumentException("No se permiten datos nulos");
        }
        Nodo nuevo = new Nodo(dato);
        if (esVacia()) {
            frente = nuevo;
        } else {
            fin.setDerecha(nuevo);
            nuevo.setIzquierda(fin);
        }
        fin = nuevo;
        tamanio++;
    }

    /** Saca y devuelve el elemento del frente (el que más tiempo lleva esperando). */
    public Object desencolar() {
        if (esVacia()) {
            throw new IllegalStateException("No hay domicilios pendientes");
        }
        Object dato = frente.getDato();
        frente = frente.getDerecha();
        if (frente == null) {
            fin = null; // la cola quedó vacía
        } else {
            frente.setIzquierda(null);
        }
        tamanio--;
        return dato;
    }

    /** Consulta el frente sin sacarlo. */
    public Object peek() {
        if (esVacia()) {
            throw new IllegalStateException("No hay domicilios pendientes");
        }
        return frente.getDato();
    }

    public int tamanio() {
        return tamanio;
    }

    public boolean esVacia() {
        return tamanio == 0;
    }

    public boolean contiene(Object dato) {
        Nodo actual = frente;
        while (actual != null) {
            if (actual.getDato().equals(dato)) {
                return true;
            }
            actual = actual.getDerecha();
        }
        return false;
    }

    public void limpiar() {
        frente = null;
        fin = null;
        tamanio = 0;
    }

    public void mostrar() {
        if (esVacia()) {
            System.out.println("  (no hay domicilios pendientes)");
            return;
        }
        Nodo actual = frente;
        int turno = 1;
        while (actual != null) {
            System.out.println("  " + turno + ". " + actual.getDato());
            actual = actual.getDerecha();
            turno++;
        }
    }
}
