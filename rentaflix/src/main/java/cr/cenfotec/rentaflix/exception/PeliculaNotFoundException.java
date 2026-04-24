package cr.cenfotec.rentaflix.exception;

/** Se lanza cuando no se encuentra una película con el ID indicado. */
public class PeliculaNotFoundException extends RentaException {
    public PeliculaNotFoundException(String peliculaId) {
        super("ERR-003", "No se encontró ninguna película con el ID: " + peliculaId);
    }
}
