package cr.cenfotec.rentaflix.exception;

/** Se lanza cuando no se encuentra un cliente con el ID indicado. */
public class ClienteNotFoundException extends RentaException {
    public ClienteNotFoundException(String clienteId) {
        super("ERR-002", "No se encontró ningún cliente con el ID: " + clienteId);
    }
}
