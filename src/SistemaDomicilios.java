import java.util.Scanner;

/**
 * Sistema de gestión de una empresa de domicilios (caso 25).
 *
 * Usa las tres estructuras propias:
 *  - listaGeneral (Lista):          registro de TODOS los domicilios.
 *  - colaPendientes (Cola):         domicilios que esperan ser despachados (FIFO).
 *  - historialEntregados (Pila):    domicilios ya entregados (LIFO), permite deshacer.
 *
 * Un mismo objeto Domicilio está referenciado desde la lista y, según su estado,
 * desde la cola o desde la pila. No se hacen copias: si cambia el estado en la
 * cola, el cambio se ve también en la lista general.
 */
public class SistemaDomicilios {

    private final Lista listaGeneral = new Lista();
    private final Cola colaPendientes = new Cola();
    private final Pila historialEntregados = new Pila();
    private final Scanner entrada = new Scanner(System.in);

    public static void main(String[] args) {
        new SistemaDomicilios().iniciar();
    }

    private void iniciar() {
        System.out.println("==============================================");
        System.out.println("   EMPRESA DE DOMICILIOS - Lista, Cola y Pila");
        System.out.println("==============================================");
        if (leerTexto("¿Cargar domicilios de prueba? (s/n): ").equalsIgnoreCase("s")) {
            cargarDatosDePrueba();
        }

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opción: ");
            try {
                ejecutar(opcion);
            } catch (IllegalStateException | IllegalArgumentException e) {
                System.out.println("  [!] " + e.getMessage());
            }
        } while (opcion != 10);
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("----------------- MENÚ -----------------");
        System.out.println(" 1. Registrar domicilio");
        System.out.println(" 2. Ver todos los domicilios registrados");
        System.out.println(" 3. Ver domicilios pendientes");
        System.out.println(" 4. Despachar siguiente domicilio");
        System.out.println(" 5. Ver historial de domicilios entregados");
        System.out.println(" 6. Buscar domicilio por número de orden");
        System.out.println(" 7. Cancelar domicilio pendiente");
        System.out.println(" 8. Deshacer última entrega");
        System.out.println(" 9. Ver cantidad de domicilios");
        System.out.println("10. Salir");
    }

    private void ejecutar(int opcion) {
        switch (opcion) {
            case 1 -> registrarDomicilio();
            case 2 -> verRegistroGeneral();
            case 3 -> verPendientes();
            case 4 -> despacharSiguiente();
            case 5 -> verHistorial();
            case 6 -> buscarPorNumero();
            case 7 -> cancelarPendiente();
            case 8 -> deshacerUltimaEntrega();
            case 9 -> verCantidades();
            case 10 -> System.out.println("Hasta pronto.");
            default -> System.out.println("  [!] Opción no válida. Elija un número del 1 al 10.");
        }
    }

    // ---------------------------------------------------------------- 1
    private void registrarDomicilio() {
        System.out.println("\n== Registrar domicilio ==");
        int numero = leerEntero("Número de orden: ");
        if (numero <= 0) {
            throw new IllegalArgumentException("El número de orden debe ser mayor que cero.");
        }
        if (listaGeneral.contiene(new Domicilio(numero))) {
            throw new IllegalArgumentException("Ya existe un domicilio con el número de orden " + numero + ".");
        }
        String cliente = leerTextoObligatorio("Cliente: ");
        String direccion = leerTextoObligatorio("Dirección de entrega: ");
        String telefono = leerTextoObligatorio("Teléfono: ");
        if (!telefono.matches("\\d{7,10}")) {
            throw new IllegalArgumentException("El teléfono debe tener entre 7 y 10 dígitos.");
        }
        String productos = leerTextoObligatorio("Productos: ");
        double valor = leerDecimal("Valor del pedido: ");
        if (valor <= 0) {
            throw new IllegalArgumentException("El valor debe ser mayor que cero.");
        }

        Domicilio domicilio = new Domicilio(numero, cliente, direccion, telefono, productos, valor);
        registrar(domicilio);
        System.out.println("  [OK] Domicilio registrado y enviado a la cola de pendientes.");
    }

    /** Regla obligatoria de registro: va a la lista general y a la cola. */
    private void registrar(Domicilio domicilio) {
        listaGeneral.agregar(domicilio);
        colaPendientes.encolar(domicilio);
    }

    // ---------------------------------------------------------------- 2
    private void verRegistroGeneral() {
        System.out.println("\n== Registro general (de inicio a fin) ==");
        listaGeneral.mostrarAdelante();
        if (!listaGeneral.esVacia()
                && leerTexto("¿Ver también en orden inverso? (s/n): ").equalsIgnoreCase("s")) {
            System.out.println("== Registro general (de fin a inicio) ==");
            listaGeneral.mostrarAtras();
        }
    }

    // ---------------------------------------------------------------- 3
    private void verPendientes() {
        System.out.println("\n== Domicilios pendientes (orden de despacho) ==");
        colaPendientes.mostrar();
        if (!colaPendientes.esVacia()) {
            System.out.println("  Próximo a despachar: Orden #"
                    + ((Domicilio) colaPendientes.peek()).getNumeroOrden());
        }
    }

    // ---------------------------------------------------------------- 4
    /** Regla obligatoria de procesamiento: sale de la cola y entra a la pila. */
    private void despacharSiguiente() {
        Domicilio despachado = (Domicilio) colaPendientes.desencolar();
        despachado.setEstado(Domicilio.ENTREGADO);
        historialEntregados.apilar(despachado);
        System.out.println("  [OK] Entregado: " + despachado);
    }

    // ---------------------------------------------------------------- 5
    private void verHistorial() {
        System.out.println("\n== Historial de entregados (más reciente primero) ==");
        historialEntregados.mostrar();
    }

    // ---------------------------------------------------------------- 6
    private void buscarPorNumero() {
        int numero = leerEntero("Número de orden a buscar: ");
        Domicilio clave = new Domicilio(numero);
        int posicion = listaGeneral.buscarDato(clave);
        if (posicion == -1) {
            System.out.println("  [X] No existe ningún domicilio con el número de orden " + numero + ".");
            return;
        }
        Domicilio encontrado = (Domicilio) listaGeneral.buscarDato(posicion);
        System.out.println("  [OK] Encontrado en la posición " + posicion + " del registro general:");
        System.out.println("    " + encontrado);
        if (colaPendientes.contiene(clave)) {
            System.out.println("    Ubicación: en la cola de pendientes.");
        } else if (historialEntregados.contiene(clave)) {
            System.out.println("    Ubicación: en el historial, a " + historialEntregados.buscar(clave)
                    + " posición(es) del tope.");
        } else {
            System.out.println("    Ubicación: fuera de la cola y del historial (cancelado).");
        }
    }

    // ---------------------------------------------------------------- 7
    /**
     * Cancela un domicilio pendiente SIN sacarlo del centro de la cola.
     * Se vacía la cola original pasando cada elemento a una cola auxiliar,
     * excepto el que se quiere cancelar. Luego se devuelven a la original,
     * así los demás conservan su orden de llegada.
     */
    private void cancelarPendiente() {
        if (colaPendientes.esVacia()) {
            throw new IllegalStateException("No hay domicilios pendientes para cancelar.");
        }
        int numero = leerEntero("Número de orden a cancelar: ");
        Domicilio clave = new Domicilio(numero);
        if (!colaPendientes.contiene(clave)) {
            throw new IllegalArgumentException("La orden " + numero + " no está pendiente (no existe, ya se entregó o ya se canceló).");
        }

        Cola auxiliar = new Cola();
        Domicilio cancelado = null;
        while (!colaPendientes.esVacia()) {
            Domicilio actual = (Domicilio) colaPendientes.desencolar();
            if (actual.equals(clave)) {
                cancelado = actual;
            } else {
                auxiliar.encolar(actual);
            }
        }
        while (!auxiliar.esVacia()) {
            colaPendientes.encolar(auxiliar.desencolar());
        }

        cancelado.setEstado(Domicilio.CANCELADO);
        System.out.println("  [OK] Cancelado: " + cancelado);
    }

    // ---------------------------------------------------------------- 8
    /** Regla obligatoria de deshacer: sale de la pila y vuelve a la cola. */
    private void deshacerUltimaEntrega() {
        Domicilio ultimo = (Domicilio) historialEntregados.desapilar();
        ultimo.setEstado(Domicilio.PENDIENTE);
        colaPendientes.encolar(ultimo);
        System.out.println("  [OK] Se deshizo la entrega de la orden #" + ultimo.getNumeroOrden()
                + "; vuelve a la cola de pendientes.");
    }

    // ---------------------------------------------------------------- 9
    private void verCantidades() {
        int cancelados = 0;
        double totalEntregado = 0;
        for (int i = 0; i < listaGeneral.cuentaElementos(); i++) {
            Domicilio d = (Domicilio) listaGeneral.buscarDato(i);
            if (Domicilio.CANCELADO.equals(d.getEstado())) {
                cancelados++;
            } else if (Domicilio.ENTREGADO.equals(d.getEstado())) {
                totalEntregado += d.getValor();
            }
        }
        System.out.println("\n== Reporte ==");
        System.out.println("  Registrados (lista):   " + listaGeneral.cuentaElementos());
        System.out.println("  Pendientes (cola):     " + colaPendientes.tamanio());
        System.out.println("  Entregados (pila):     " + historialEntregados.tamanio());
        System.out.println("  Cancelados:            " + cancelados);
        System.out.printf("  Valor entregado:       $%,.0f%n", totalEntregado);
    }

    // ------------------------------------------------------------ datos
    private void cargarDatosDePrueba() {
        registrar(new Domicilio(101, "Laura Gómez", "Cra 15 #31-20, Turbaco", "3001112233", "2 hamburguesas", 38000));
        registrar(new Domicilio(102, "Carlos Pérez", "Calle 30 #12-45, Bocagrande", "3012223344", "Pizza familiar", 52000));
        registrar(new Domicilio(103, "Ana Torres", "Mz 4 Lt 7, El Bosque", "3023334455", "Medicamentos", 27500));
        registrar(new Domicilio(104, "Jorge Ramírez", "Av Pedro de Heredia #50", "3034445566", "Mercado básico", 115000));
        registrar(new Domicilio(105, "Sofía Herrera", "Cra 8 #22-10, Manga", "3045556677", "Almuerzo ejecutivo", 18000));
        System.out.println("  [OK] Se cargaron 5 domicilios de prueba (órdenes 101 a 105).");
    }

    // ------------------------------------------------------- lectura
    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return entrada.hasNextLine() ? entrada.nextLine().trim() : "";
    }

    private String leerTextoObligatorio(String mensaje) {
        String texto = leerTexto(mensaje);
        if (texto.isEmpty()) {
            throw new IllegalArgumentException("Este campo es obligatorio.");
        }
        return texto;
    }

    private int leerEntero(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            if (!entrada.hasNextLine() && texto.isEmpty()) {
                return 10; // fin de la entrada: salir del programa
            }
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Debe escribir un número entero.");
            }
        }
    }

    private double leerDecimal(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            if (!entrada.hasNextLine() && texto.isEmpty()) {
                throw new IllegalArgumentException("No se recibió el valor.");
            }
            try {
                return Double.parseDouble(texto.replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("  [!] Debe escribir un valor numérico.");
            }
        }
    }
}
