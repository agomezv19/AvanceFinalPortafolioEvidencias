package cr.cenfotec.rentaflix.model;

/**
 * Clase abstracta que modela los atributos comunes de cualquier
 * persona dentro del sistema.
 */
public abstract class Persona {

    protected String id;
    protected String nombre;
    protected String telefono;

    public Persona(String id, String nombre, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getId()       { return id; }
    public String getNombre()   { return nombre; }
    public String getTelefono() { return telefono; }

    public void setNombre(String nombre)     { this.nombre = nombre; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    /**
     * Devuelve un resumen de la persona para mostrar en pantalla.
     */
    public abstract String getResumen();
}
