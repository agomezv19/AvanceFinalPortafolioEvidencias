package cr.cenfotec.rentaflix.model;

/**
 * Representa un cliente de la tienda.
 */
public class Cliente extends Persona implements Comparable<Cliente> {

    // Composición: Direccion nace y muere con este objeto.
    private final Direccion direccion;
    private String correo;

    public Cliente(String clienteId, String nombre, String telefono,
                   String correo, String calle, String ciudad, String codigoPostal) {
        super(clienteId, nombre, telefono);
        this.correo    = correo;
        this.direccion = new Direccion(calle, ciudad, codigoPostal); // composición
    }

    // ── Getters y setters ──────────────────────────────────────────────────
    public String getClienteId()   { return id; }
    public Direccion getDireccion(){ return direccion; }
    public String getCorreo()      { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    // ── Interfaz Comparable ────────────────────────────────────────────────
    @Override
    public int compareTo(Cliente otro) {
        return this.nombre.compareToIgnoreCase(otro.nombre);
    }

    // ── Método abstracto de Persona ────────────────────────────────────────
    @Override
    public String getResumen() {
        return "[" + id + "] " + nombre + " | Tel: " + telefono
                + " | " + direccion;
    }

    @Override
    public String toString() {
        return getResumen();
    }
}
