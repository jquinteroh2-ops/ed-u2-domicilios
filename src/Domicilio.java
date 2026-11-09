/**
 * Entidad principal del caso 25: Empresa de domicilios.
 *
 * Un domicilio es un pedido que la empresa debe llevar a la dirección de un
 * cliente. Su identificador es el número de orden; dos objetos Domicilio con
 * el mismo número de orden se consideran el mismo pedido (ver equals()).
 */
public class Domicilio {

    public static final String PENDIENTE = "PENDIENTE";
    public static final String ENTREGADO = "ENTREGADO";
    public static final String CANCELADO = "CANCELADO";

    private int numeroOrden;
    private String cliente;
    private String direccion;
    private String telefono;
    private String productos;
    private double valor;
    private String estado;

    public Domicilio(int numeroOrden, String cliente, String direccion, String telefono,
                     String productos, double valor) {
        this.numeroOrden = numeroOrden;
        this.cliente = cliente;
        this.direccion = direccion;
        this.telefono = telefono;
        this.productos = productos;
        this.valor = valor;
        this.estado = PENDIENTE;
    }

    /** Constructor de búsqueda: solo importa el número de orden para equals(). */
    public Domicilio(int numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public int getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(int numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getProductos() {
        return productos;
    }

    public void setProductos(String productos) {
        this.productos = productos;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    /** Dos domicilios son iguales si tienen el mismo número de orden. */
    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (!(otro instanceof Domicilio)) {
            return false;
        }
        return numeroOrden == ((Domicilio) otro).numeroOrden;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(numeroOrden);
    }

    @Override
    public String toString() {
        return String.format("Orden #%d | %-18s | %-26s | Tel %s | %s | $%,.0f | %s",
                numeroOrden, cliente, direccion, telefono, productos, valor, estado);
    }
}
