package cr.cenfotec.rentaflix.exception;

/** Se lanza cuando ocurre un error de acceso a la capa de persistencia. */
public class DAOException extends RentaException {
    public DAOException(String operacion, Throwable causa) {
        super("ERR-004", "Error en la capa de persistencia durante: " + operacion, causa);
    }

    public DAOException(String mensaje) {
        super("ERR-004", mensaje);
    }
}
