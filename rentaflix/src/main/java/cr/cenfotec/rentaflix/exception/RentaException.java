package cr.cenfotec.rentaflix.exception;

/**
 * Excepción base.
 * Toda excepción específica de la aplicación hereda de esta clase
 */
public class RentaException extends Exception {

    private final String codigo;

    public RentaException(String codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
    }

    public RentaException(String codigo, String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    @Override
    public String toString() {
        return "[" + codigo + "] " + getMessage();
    }
}
