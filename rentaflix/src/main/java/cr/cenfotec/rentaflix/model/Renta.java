package cr.cenfotec.rentaflix.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Representa una transacción de renta.
 */
public class Renta {

    private final String rentaId;
    private final Cliente  cliente;
    private final Pelicula pelicula;
    private final LocalDate fechaInicio;
    private LocalDate       fechaFin;
    private boolean         activa;
    private double          costoFinal;

    /**
     * Constructor principal: crea una renta nueva y marca la película como no disponible.
     */
    public Renta(String rentaId, Cliente cliente, Pelicula pelicula, int diasRenta) {
        this.rentaId     = rentaId;
        this.cliente     = cliente;
        this.pelicula    = pelicula;
        this.fechaInicio = LocalDate.now();
        this.fechaFin    = fechaInicio.plusDays(diasRenta);
        this.activa      = true;
        this.costoFinal  = 0.0;
        pelicula.setDisponible(false);
    }

    public Renta(String rentaId, Cliente cliente, Pelicula pelicula,
                 LocalDate fechaInicio, LocalDate fechaFin, boolean activa, double costoFinal) {
        this.rentaId     = rentaId;
        this.cliente     = cliente;
        this.pelicula    = pelicula;
        this.fechaInicio = fechaInicio;
        this.fechaFin    = fechaFin;
        this.activa      = activa;
        this.costoFinal  = costoFinal;

    }

    // ── Lógica de negocio ─────────────────────────────────────────────────

    public double calcularCostoTotal() {
        long dias = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        return Math.max(dias, 1) * pelicula.getPrecioPorDia();
    }

    public void finalizar() {
        this.activa     = false;
        this.costoFinal = calcularCostoTotal();
        this.fechaFin   = LocalDate.now();
        pelicula.setDisponible(true);
    }

    // ── Getters ────────────────────────────────────────────────────────────
    public String     getRentaId()     { return rentaId; }
    public Cliente    getCliente()     { return cliente; }
    public Pelicula   getPelicula()    { return pelicula; }
    public LocalDate  getFechaInicio() { return fechaInicio; }
    public LocalDate  getFechaFin()    { return fechaFin; }
    public boolean    isActiva()       { return activa; }
    public double     getCostoFinal()  { return costoFinal; }

    @Override
    public String toString() {
        return String.format("[%s] %s → %s | %s a %s | Activa: %b | Costo: ₡%.2f",
                rentaId, cliente.getNombre(), pelicula.getTitulo(),
                fechaInicio, fechaFin, activa,
                activa ? calcularCostoTotal() : costoFinal);
    }
}