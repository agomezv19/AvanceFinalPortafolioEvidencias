package cr.cenfotec.rentaflix.model;

/**
 * Clase de valor que representa la dirección de un cliente.
 */
public class Direccion {

    private String calle;
    private String ciudad;
    private String codigoPostal;

    public Direccion(String calle, String ciudad, String codigoPostal) {
        this.calle = calle;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
    }

    // ── Getters y setters ──────────────────────────────────────────────────
    public String getCalle()        { return calle; }
    public String getCiudad()       { return ciudad; }
    public String getCodigoPostal() { return codigoPostal; }

    public void setCalle(String calle)               { this.calle = calle; }
    public void setCiudad(String ciudad)             { this.ciudad = ciudad; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }

    @Override
    public String toString() {
        return calle + ", " + ciudad + " " + codigoPostal;
    }
}
