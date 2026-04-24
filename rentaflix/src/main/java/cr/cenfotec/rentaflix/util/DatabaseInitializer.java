package cr.cenfotec.rentaflix.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Crea las tablas de la base de datos si no existen al iniciar la aplicación.

 */
public class DatabaseInitializer {

    private DatabaseInitializer() {  }

    public static void inicializar() throws SQLException {
        Connection con = DatabaseConnection.getInstancia().getConexion();
        try (Statement st = con.createStatement()) {
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS peliculas (
                    id            TEXT PRIMARY KEY,
                    titulo        TEXT NOT NULL,
                    genero        TEXT NOT NULL,
                    anio          INTEGER NOT NULL,
                    precio_por_dia REAL NOT NULL,
                    disponible    INTEGER NOT NULL DEFAULT 1
                )
            """);

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS clientes (
                    cliente_id    TEXT PRIMARY KEY,
                    nombre        TEXT NOT NULL,
                    telefono      TEXT,
                    correo        TEXT UNIQUE,
                    calle         TEXT,
                    ciudad        TEXT,
                    codigo_postal TEXT
                )
            """);

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS rentas (
                    renta_id      TEXT PRIMARY KEY,
                    cliente_id    TEXT NOT NULL,
                    pelicula_id   TEXT NOT NULL,
                    fecha_inicio  TEXT NOT NULL,
                    fecha_fin     TEXT NOT NULL,
                    activa        INTEGER NOT NULL DEFAULT 1,
                    costo_final   REAL NOT NULL DEFAULT 0.0,
                    FOREIGN KEY (cliente_id)  REFERENCES clientes(cliente_id),
                    FOREIGN KEY (pelicula_id) REFERENCES peliculas(id)
                )
            """);
        }
    }
}
