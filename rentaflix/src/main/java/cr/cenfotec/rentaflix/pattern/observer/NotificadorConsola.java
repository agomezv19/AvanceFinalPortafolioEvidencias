package cr.cenfotec.rentaflix.pattern.observer;

public class NotificadorConsola implements RentaObserver {

    @Override
    public void onEventoRenta(EventoRenta evento) {
        String simbolo = evento.getTipo() == EventoRenta.Tipo.RENTA_CREADA ? "📥" : "📤";
        System.out.println(simbolo + " [Notificación] " + evento);
    }
}
