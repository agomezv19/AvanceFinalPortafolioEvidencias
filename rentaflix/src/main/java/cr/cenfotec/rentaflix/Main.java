package cr.cenfotec.rentaflix;

import cr.cenfotec.rentaflix.controller.RentaController;
import cr.cenfotec.rentaflix.dao.ClienteDAO;
import cr.cenfotec.rentaflix.dao.PeliculaDAO;
import cr.cenfotec.rentaflix.dao.RentaDAO;
import cr.cenfotec.rentaflix.dao.impl.ClienteDAOImpl;
import cr.cenfotec.rentaflix.dao.impl.PeliculaDAOImpl;
import cr.cenfotec.rentaflix.dao.impl.RentaDAOImpl;
import cr.cenfotec.rentaflix.pattern.observer.NotificadorConsola;
import cr.cenfotec.rentaflix.util.DatabaseInitializer;
import cr.cenfotec.rentaflix.view.ConsoleView;

public class Main {

    public static void main(String[] args) {
        try {
            // 1. Inicializar esquema de base de datos
            DatabaseInitializer.inicializar();
            System.out.println(" Base de datos inicializada correctamente.");

            // 2. Instanciar DAOs
            PeliculaDAO peliculaDAO = new PeliculaDAOImpl();
            ClienteDAO  clienteDAO  = new ClienteDAOImpl();
            RentaDAO    rentaDAO    = new RentaDAOImpl(clienteDAO, peliculaDAO);

            // 3. Componer el controlador con las dependencias necesarias
            RentaController controlador = new RentaController(
                    peliculaDAO, clienteDAO, rentaDAO);

            // 4. Registrar observadores (patrón Observer)
            controlador.agregarObservador(new NotificadorConsola());

            // 5. Arrancar la vista de consola (patrón MVC)
            ConsoleView vista = new ConsoleView(controlador);
            vista.iniciar();

        } catch (Exception e) {
            System.err.println(" Error al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
