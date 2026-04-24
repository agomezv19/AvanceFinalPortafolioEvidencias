package cr.cenfotec.rentaflix.model;

/**
 * Representa una película del catálogo.
 */
public class Pelicula implements Comparable<Pelicula> {

    private final String id;
    private String titulo;
    private Genero genero;
    private int anio;
    private double precioPorDia;
    private boolean disponible;

    public Pelicula(String id, String titulo, Genero genero, int anio, double precioPorDia) {
        this.id           = id;
        this.titulo       = titulo;
        this.genero       = genero;
        this.anio         = anio;
        this.precioPorDia = precioPorDia;
        this.disponible   = true;
    }

    // ── Getters ────────────────────────────────────────────────────────────
    public String getId()           { return id; }
    public String getTitulo()       { return titulo; }
    public Genero getGenero()       { return genero; }
    public int getAnio()            { return anio; }
    public double getPrecioPorDia() { return precioPorDia; }
    public boolean isDisponible()   { return disponible; }

    // ── Setters controlados ────────────────────────────────────────────────
    public void setTitulo(String titulo)           { this.titulo = titulo; }
    public void setGenero(Genero genero)           { this.genero = genero; }
    public void setAnio(int anio)                  { this.anio = anio; }
    public void setPrecioPorDia(double precio)     { this.precioPorDia = precio; }

    /**
     * Único punto de modificación del estado de disponibilidad.
     * Centralizar aquí permite agregar lógica de validación futura
     * sin modificar otras clases.
     */
    public void setDisponible(boolean disponible)  { this.disponible = disponible; }

    // ── Comparable ─────────────────────────────────────────────────────────
    @Override
    public int compareTo(Pelicula otra) {
        return this.titulo.compareToIgnoreCase(otra.titulo);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%d) | %s | ₡%.2f/día | %s",
                id, titulo, anio, genero,
                precioPorDia, disponible ? "Disponible" : "Rentada");
    }
}
