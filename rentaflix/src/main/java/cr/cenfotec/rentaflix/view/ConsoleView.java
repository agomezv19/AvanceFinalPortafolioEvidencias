package cr.cenfotec.rentaflix.view;

import cr.cenfotec.rentaflix.controller.RentaController;
import cr.cenfotec.rentaflix.exception.*;
import cr.cenfotec.rentaflix.model.*;

import java.util.List;
import java.util.Scanner;

/**
 * Capa Vista del patrón MVC
 */
public class ConsoleView {

    private final RentaController controlador;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleView(RentaController controlador) {
        this.controlador = controlador;
    }

    // ── Menú principal ─────────────────────────────────────────────────────

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            mostrarMenuPrincipal();
            int opcion = leerEntero("Seleccione una opción: ");
            try {
                switch (opcion) {
                    case 1 -> menuPeliculas();
                    case 2 -> menuClientes();
                    case 3 -> menuRentas();
                    case 0 -> salir = true;
                    default -> System.out.println(" Opción inválida.");
                }
            } catch (DAOException e) {
                System.out.println(" Error de persistencia: " + e);
            }
        }
        System.out.println("👋 ¡Hasta luego!");
    }

    private void mostrarMenuPrincipal() {
        System.out.println("""
                
                ╔══════════════════════════════╗
                ║           RentaFlix          ║
                ╠══════════════════════════════╣
                ║  1. Películas                ║
                ║  2. Clientes                 ║
                ║  3. Rentas                   ║
                ║  0. Salir                    ║
                ╚══════════════════════════════╝""");
    }

    // ── Submenú Películas ──────────────────────────────────────────────────

    private void menuPeliculas() throws DAOException {
        System.out.println("""
                
                — Películas —
                1. Registrar película
                2. Listar todas
                3. Listar disponibles
                4. Buscar por título
                0. Volver""");
        int op = leerEntero("Opción: ");
        switch (op) {
            case 1 -> registrarPelicula();
            case 2 -> listarPeliculas(controlador.listarPeliculas());
            case 3 -> listarPeliculas(controlador.listarDisponibles());
            case 4 -> {
                String q = leerTexto("Fragmento del título: ");
                listarPeliculas(controlador.buscarPeliculasPorTitulo(q));
            }
        }
    }

    private void registrarPelicula() throws DAOException {
        System.out.println("\n— Registrar Película —");
        String id     = leerTexto("ID: ");
        String titulo = leerTexto("Título: ");
        Genero genero = leerGenero();
        int anio      = leerEntero("Año: ");
        double precio = leerDouble("Precio por día (₡): ");
        Pelicula p    = new Pelicula(id, titulo, genero, anio, precio);
        controlador.registrarPelicula(p);
        System.out.println("✅ Película registrada: " + p.getTitulo());
    }

    private void listarPeliculas(List<Pelicula> lista) {
        if (lista.isEmpty()) { System.out.println("⚠ No hay películas."); return; }
        System.out.println("\n— Catálogo —");
        lista.forEach(p -> System.out.println("  " + p));
    }

    private Genero leerGenero() {
        System.out.println("Géneros: ");
        Genero[] generos = Genero.values();
        for (int i = 0; i < generos.length; i++) {
            System.out.println("  " + (i + 1) + ". " + generos[i].getEtiqueta());
        }
        int idx = leerEntero("Seleccione género: ") - 1;
        if (idx < 0 || idx >= generos.length) {
            System.out.println("⚠ Género inválido. Se asignará DRAMA.");
            return Genero.DRAMA;
        }
        return generos[idx];
    }

    // ── Submenú Clientes ───────────────────────────────────────────────────

    private void menuClientes() throws DAOException {
        System.out.println("""
                
                — Clientes —
                1. Registrar cliente
                2. Listar todos
                0. Volver""");
        int op = leerEntero("Opción: ");
        switch (op) {
            case 1 -> registrarCliente();
            case 2 -> controlador.listarClientes()
                    .forEach(c -> System.out.println("  " + c));
        }
    }

    private void registrarCliente() throws DAOException {
        System.out.println("\n— Registrar Cliente —");
        String id     = leerTexto("ID: ");
        String nombre = leerTexto("Nombre: ");
        String tel    = leerTexto("Teléfono: ");
        String correo = leerTexto("Correo: ");
        String calle  = leerTexto("Calle: ");
        String ciudad = leerTexto("Ciudad: ");
        String cp     = leerTexto("Código postal: ");
        Cliente c     = new Cliente(id, nombre, tel, correo, calle, ciudad, cp);
        controlador.registrarCliente(c);
        System.out.println("✅ Cliente registrado: " + c.getNombre());
    }

    // ── Submenú Rentas ─────────────────────────────────────────────────────

    private void menuRentas() throws DAOException {
        System.out.println("""
                
                — Rentas —
                1. Rentar película
                2. Devolver película
                3. Ver rentas activas
                4. Historial de cliente
                0. Volver""");
        int op = leerEntero("Opción: ");
        try {
            switch (op) {
                case 1 -> rentarPelicula();
                case 2 -> devolverPelicula();
                case 3 -> controlador.listarRentasActivas()
                        .forEach(r -> System.out.println("  " + r));
                case 4 -> {
                    String cid = leerTexto("ID del cliente: ");
                    controlador.listarHistorialCliente(cid)
                            .forEach(r -> System.out.println("  " + r));
                }
            }
        } catch (RentaException e) {
            // Errores de dominio: se muestran amigablemente sin stacktrace
            System.out.println("⚠ " + e);
        }
    }

    private void rentarPelicula() throws RentaException, DAOException {
        String cid = leerTexto("ID del cliente: ");
        String pid = leerTexto("ID de la película: ");
        int dias   = leerEntero("Días de renta: ");
        Renta r    = controlador.rentarPelicula(cid, pid, dias);
        System.out.printf("✅ Renta [%s] creada. Costo estimado: ₡%.2f%n",
                r.getRentaId(), r.calcularCostoTotal());
    }

    private void devolverPelicula() throws RentaException, DAOException {
        String rid = leerTexto("ID de la renta: ");
        Renta r    = controlador.devolverPelicula(rid);
        System.out.printf("✅ Renta [%s] finalizada. Costo total: ₡%.2f%n",
                r.getRentaId(), r.getCostoFinal());
    }

    // ── Helpers de lectura ─────────────────────────────────────────────────

    private String leerTexto(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int leerEntero(String prompt) {
        System.out.print(prompt);
        try {
            int v = Integer.parseInt(scanner.nextLine().trim());
            return v;
        } catch (NumberFormatException e) {
            System.out.println("⚠ Valor inválido. Se usará 0.");
            return 0;
        }
    }

    private double leerDouble(String prompt) {
        System.out.print(prompt);
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("⚠ Valor inválido. Se usará 0.");
            return 0.0;
        }
    }
}
