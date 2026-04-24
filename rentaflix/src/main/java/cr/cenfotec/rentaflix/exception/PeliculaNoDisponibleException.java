package cr.cenfotec.rentaflix.exception;

/** Se lanza cuando se intenta rentar una película que ya no está disponible. */
public class PeliculaNoDisponibleException extends RentaException {
    public PeliculaNoDisponibleException(String tituloPelicula) {
        super("ERR-001", "La película \"" + tituloPelicula + "\" no está disponible para renta.");
    }
}
