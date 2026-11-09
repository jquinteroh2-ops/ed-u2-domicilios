/**
 * Lista doblemente enlazada.
 *
 * Mantiene una referencia al primer nodo (inicio) y al último (fin), lo que
 * permite agregar y eliminar en ambos extremos en O(1) y recorrer la lista
 * hacia adelante (por "derecha") o hacia atrás (por "izquierda").
 *
 * En el sistema de domicilios se usa como REGISTRO GENERAL: guarda todos los
 * domicilios que alguna vez se registraron, sin importar su estado.
 */
public class Lista {

    private Nodo inicio;
    private Nodo fin;
    private int tamanio;

    public Lista() {
        inicio = null;
        fin = null;
        tamanio = 0;
    }

    public boolean esVacia() {
        return tamanio == 0;
    }

    /** Agrega al final: el nuevo nodo queda a la derecha del último. */
    public void agregar(Object dato) {
        validarDato(dato);
        Nodo nuevo = new Nodo(dato);
        if (esVacia()) {
            inicio = nuevo;
            fin = nuevo;
        } else {
            fin.setDerecha(nuevo);   // el último apunta al nuevo
            nuevo.setIzquierda(fin); // el nuevo apunta de regreso al último
            fin = nuevo;             // el nuevo pasa a ser el último
        }
        tamanio++;
    }

    /** Agrega al inicio: el nuevo nodo queda a la izquierda del primero. */
    public void agregarAlInicio(Object dato) {
        validarDato(dato);
        Nodo nuevo = new Nodo(dato);
        if (esVacia()) {
            inicio = nuevo;
            fin = nuevo;
        } else {
            nuevo.setDerecha(inicio);
            inicio.setIzquierda(nuevo);
            inicio = nuevo;
        }
        tamanio++;
    }

    /**
     * Inserta en la posición indicada (0 = inicio, tamanio = final).
     * Se ubica el nodo que hoy ocupa esa posición y el nuevo se enlaza
     * entre ese nodo y su vecino izquierdo.
     */
    public void agregarEnPosicion(int indice, Object dato) {
        validarDato(dato);
        if (indice < 0 || indice > tamanio) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
        if (indice == 0) {
            agregarAlInicio(dato);
            return;
        }
        if (indice == tamanio) {
            agregar(dato);
            return;
        }
        Nodo actual = nodoEn(indice);
        Nodo anterior = actual.getIzquierda();
        Nodo nuevo = new Nodo(dato);

        nuevo.setIzquierda(anterior);
        nuevo.setDerecha(actual);
        anterior.setDerecha(nuevo);
        actual.setIzquierda(nuevo);
        tamanio++;
    }

    public Object eliminarPrimero() {
        if (esVacia()) {
            throw new IllegalStateException("La lista está vacía");
        }
        Object dato = inicio.getDato();
        if (tamanio == 1) {
            inicio = null;
            fin = null;
        } else {
            inicio = inicio.getDerecha();
            inicio.setIzquierda(null); // se rompe el enlace con el nodo eliminado
        }
        tamanio--;
        return dato;
    }

    public Object eliminarUltimo() {
        if (esVacia()) {
            throw new IllegalStateException("La lista está vacía");
        }
        Object dato = fin.getDato();
        if (tamanio == 1) {
            inicio = null;
            fin = null;
        } else {
            fin = fin.getIzquierda();
            fin.setDerecha(null);
        }
        tamanio--;
        return dato;
    }

    /** Elimina el nodo de la posición dada uniendo a sus dos vecinos entre sí. */
    public Object eliminarEnPosicion(int indice) {
        validarIndice(indice);
        if (indice == 0) {
            return eliminarPrimero();
        }
        if (indice == tamanio - 1) {
            return eliminarUltimo();
        }
        Nodo actual = nodoEn(indice);
        Nodo anterior = actual.getIzquierda();
        Nodo siguiente = actual.getDerecha();
        anterior.setDerecha(siguiente);
        siguiente.setIzquierda(anterior);
        tamanio--;
        return actual.getDato();
    }

    /** Devuelve el dato que está en la posición indicada. */
    public Object buscarDato(int indice) {
        validarIndice(indice);
        return nodoEn(indice).getDato();
    }

    /**
     * Devuelve la posición del dato o -1 si no existe.
     * La comparación usa equals(), por eso Domicilio compara por número de orden.
     */
    public int buscarDato(Object dato) {
        Nodo actual = inicio;
        int posicion = 0;
        while (actual != null) {
            if (actual.getDato().equals(dato)) {
                return posicion;
            }
            actual = actual.getDerecha();
            posicion++;
        }
        return -1;
    }

    public boolean contiene(Object dato) {
        return buscarDato(dato) != -1;
    }

    public int cuentaElementos() {
        return tamanio;
    }

    public void limpiar() {
        inicio = null;
        fin = null;
        tamanio = 0;
    }

    /** Recorre de inicio a fin siguiendo las referencias "derecha". */
    public void mostrarAdelante() {
        if (esVacia()) {
            System.out.println("  (lista vacía)");
            return;
        }
        Nodo actual = inicio;
        int posicion = 0;
        while (actual != null) {
            System.out.println("  [" + posicion + "] " + actual.getDato());
            actual = actual.getDerecha();
            posicion++;
        }
    }

    /** Recorre de fin a inicio siguiendo las referencias "izquierda". */
    public void mostrarAtras() {
        if (esVacia()) {
            System.out.println("  (lista vacía)");
            return;
        }
        Nodo actual = fin;
        int posicion = tamanio - 1;
        while (actual != null) {
            System.out.println("  [" + posicion + "] " + actual.getDato());
            actual = actual.getIzquierda();
            posicion--;
        }
    }

    /**
     * Ubica el nodo de una posición. Si el índice está en la primera mitad
     * se recorre desde el inicio; si no, desde el fin (aprovecha el doble enlace).
     */
    private Nodo nodoEn(int indice) {
        Nodo actual;
        if (indice < tamanio / 2) {
            actual = inicio;
            for (int i = 0; i < indice; i++) {
                actual = actual.getDerecha();
            }
        } else {
            actual = fin;
            for (int i = tamanio - 1; i > indice; i--) {
                actual = actual.getIzquierda();
            }
        }
        return actual;
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
    }

    private void validarDato(Object dato) {
        if (dato == null) {
            throw new IllegalArgumentException("No se permiten datos nulos");
        }
    }
}
