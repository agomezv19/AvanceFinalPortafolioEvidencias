package cr.cenfotec.rentaflix.controller;

import cr.cenfotec.rentaflix.dao.ClienteDAO;
import cr.cenfotec.rentaflix.dao.PeliculaDAO;
import cr.cenfotec.rentaflix.dao.RentaDAO;
import cr.cenfotec.rentaflix.exception.*;
import cr.cenfotec.rentaflix.model.*;
import cr.cenfotec.rentaflix.pattern.observer.EventoRenta;
import cr.cenfotec.rentaflix.pattern.observer.RentaObserver;

import java.util.ArrayList;
import java.util.List;

public class RentaController {

    // ── Dependencias inyectadas ─────────────────────────────────────────────
    private final PeliculaDAO peliculaDAO;
    private final ClienteDAO  clienteDAO;
    private final RentaDAO    rentaDAO;

    // ── Observer: lista de suscriptores ────────────────────────────────────
    private final List<RentaObserver> observadores = new ArrayList<>();

    // ── Contador de rentas para generación de IDs ──────────────────────────
    private int contadorRentas;

    public RentaController(PeliculaDAO peliculaDAO, ClienteDAO clienteDAO,
                           RentaDAO rentaDAO) throws DAOException {
        this.peliculaDAO   = peliculaDAO;
        this.clienteDAO    = clienteDAO;
        this.rentaDAO      = rentaDAO;
        this.contadorRentas = rentaDAO.obtenerTodos().size() + 1;
    }

    // ── Gestión de observadores ────────────────────────────────────────────

    public void agregarObservador(RentaObserver o)    { observadores.add(o); }
    public void eliminarObservador(RentaObserver o)   { observadores.remove(o); }

    private void notificar(EventoRenta evento) {
        for (RentaObserver o : observadores) o.onEventoRenta(evento);
    }

    // ── Casos de uso: Película ─────────────────────────────────────────────

    public void registrarPelicula(Pelicula p) throws DAOException {
        peliculaDAO.guardar(p);
    }

    public List<Pelicula> listarPeliculas() throws DAOException {
        return peliculaDAO.obtenerTodos();
    }

    public List<Pelicula> listarDisponibles() throws DAOException {
        return peliculaDAO.buscarDisponibles();
    }

    public List<Pelicula> buscarPeliculasPorTitulo(String fragmento) throws DAOException {
        return peliculaDAO.buscarPorTitulo(fragmento);
    }

    public Pelicula buscarPelicula(String id)
            throws PeliculaNotFoundException, DAOException {
        return peliculaDAO.buscarPorId(id)
                .orElseThrow(() -> new PeliculaNotFoundException(id));
    }

    // ── Casos de uso: Cliente ──────────────────────────────────────────────

    public void registrarCliente(Cliente c) throws DAOException {
        clienteDAO.guardar(c);
    }

    public List<Cliente> listarClientes() throws DAOException {
        return clienteDAO.obtenerTodos();
    }

    public Cliente buscarCliente(String id)
            throws ClienteNotFoundException, DAOException {
        return clienteDAO.buscarPorId(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));
    }

    // ── Casos de uso: Renta ────────────────────────────────────────────────

    /**
     * Crea una nueva renta,, validando la disponibilidad de la película.
     *
     * @throws ClienteNotFoundException      si el cliente no existe.
     * @throws PeliculaNotFoundException     si la película no existe.
     * @throws PeliculaNoDisponibleException si la película ya está rentada.
     * @throws DAOException                  si hay un fallo en la persistencia.
     */
    public Renta rentarPelicula(String clienteId, String peliculaId, int dias)
            throws RentaException, DAOException {

        // Validaciones de dominio — lanzan excepciones específicas
        Cliente  cliente  = buscarCliente(clienteId);
        Pelicula pelicula = buscarPelicula(peliculaId);

        if (!pelicula.isDisponible()) {
            throw new PeliculaNoDisponibleException(pelicula.getTitulo());
        }

        String rentaId = "R" + String.format("%03d", contadorRentas++);
        Renta  renta   = new Renta(rentaId, cliente, pelicula, dias);

        // Persistir la renta y el estado actualizado de la película
        rentaDAO.guardar(renta);
        peliculaDAO.actualizar(pelicula);

        // Notificar a los observadores
        notificar(new EventoRenta(EventoRenta.Tipo.RENTA_CREADA, renta));
        return renta;
    }

    public Renta devolverPelicula(String rentaId)
            throws RentaException, DAOException {

        Renta renta = rentaDAO.buscarPorId(rentaId)
                .orElseThrow(() -> new RentaException("ERR-005",
                        "No se encontró la renta con ID: " + rentaId));

        if (!renta.isActiva()) {
            throw new RentaException("ERR-006",
                    "La renta [" + rentaId + "] ya fue finalizada previamente.");
        }

        renta.finalizar();
        rentaDAO.actualizar(renta);
        peliculaDAO.actualizar(renta.getPelicula());

        notificar(new EventoRenta(EventoRenta.Tipo.RENTA_FINALIZADA, renta));
        return renta;
    }

    public List<Renta> listarRentasActivas() throws DAOException {
        return rentaDAO.buscarActivas();
    }

    public List<Renta> listarHistorialCliente(String clienteId) throws DAOException {
        return rentaDAO.buscarPorCliente(clienteId);
    }
}
