package cr.cenfotec.rentaflix.pattern.observer;

import cr.cenfotec.rentaflix.model.Renta;

public class EventoRenta {

    public enum Tipo { RENTA_CREADA, RENTA_FINALIZADA }

    private final Tipo  tipo;
    private final Renta renta;

    public EventoRenta(Tipo tipo, Renta renta) {
        this.tipo  = tipo;
        this.renta = renta;
    }

    public Tipo  getTipo()  { return tipo; }
    public Renta getRenta() { return renta; }

    @Override
    public String toString() {
        return "[EVENTO:" + tipo + "] " + renta.getRentaId();
    }
}
