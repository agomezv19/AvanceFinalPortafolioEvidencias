package cr.cenfotec.rentaflix.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Patrón aplicado a la conexion de base de datos
 */
public class DatabaseConnection {

    // Configuración de la base de datos SQLite
    private static final String URL = "jdbc:sqlite:rentaflix.db";

    // Instancia única
    private static volatile DatabaseConnection instancia;
    private Connection conexion;

    // Constructor
    private DatabaseConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            this.conexion = DriverManager.getConnection(URL);
            this.conexion.setAutoCommit(true);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver SQLite no encontrado: " + e.getMessage());
        }
    }

    public static DatabaseConnection getInstancia() throws SQLException {
        if (instancia == null) {
            synchronized (DatabaseConnection.class) {
                if (instancia == null) {
                    instancia = new DatabaseConnection();
                }
            }
        }
        return instancia;
    }

    public Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conexion = DriverManager.getConnection(URL);
        }
        return conexion;
    }

    public void cerrar() throws SQLException {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
        }
        instancia = null;
    }
}
