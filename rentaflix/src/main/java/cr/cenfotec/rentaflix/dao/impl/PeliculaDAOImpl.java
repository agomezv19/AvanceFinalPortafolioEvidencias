package cr.cenfotec.rentaflix.dao.impl;

import cr.cenfotec.rentaflix.dao.PeliculaDAO;
import cr.cenfotec.rentaflix.exception.DAOException;
import cr.cenfotec.rentaflix.model.Genero;
import cr.cenfotec.rentaflix.model.Pelicula;
import cr.cenfotec.rentaflix.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class PeliculaDAOImpl implements PeliculaDAO {

    // ── CRUD base ──────────────────────────────────────────────────────────

    @Override
    public void guardar(Pelicula p) throws DAOException {
        String sql = """
            INSERT INTO peliculas (id, titulo, genero, anio, precio_por_dia, disponible)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = con().prepareStatement(sql)) {
            mapearPelicula(ps, p);
            ps.setString(1, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("guardar pelicula", e);
        }
    }

    @Override
    public void actualizar(Pelicula p) throws DAOException {
        String sql = """
            UPDATE peliculas SET titulo=?, genero=?, anio=?, precio_por_dia=?, disponible=?
            WHERE id=?
        """;
        try (PreparedStatement ps = con().prepareStatement(sql)) {
            ps.setString(1, p.getTitulo());
            ps.setString(2, p.getGenero().name());
            ps.setInt(3, p.getAnio());
            ps.setDouble(4, p.getPrecioPorDia());
            ps.setInt(5, p.isDisponible() ? 1 : 0);
            ps.setString(6, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("actualizar pelicula", e);
        }
    }

    @Override
    public void eliminar(String id) throws DAOException {
        try (PreparedStatement ps = con().prepareStatement(
                "DELETE FROM peliculas WHERE id=?")) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("eliminar pelicula", e);
        }
    }

    @Override
    public Optional<Pelicula> buscarPorId(String id) throws DAOException {
        try (PreparedStatement ps = con().prepareStatement(
                "SELECT * FROM peliculas WHERE id=?")) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? Optional.of(mapearResultSet(rs)) : Optional.empty();
        } catch (SQLException e) {
            throw new DAOException("buscar pelicula por id", e);
        }
    }

    @Override
    public List<Pelicula> obtenerTodos() throws DAOException {
        return ejecutarListado("SELECT * FROM peliculas ORDER BY titulo");
    }

    // ── Consultas específicas del dominio ──────────────────────────────────

    @Override
    public List<Pelicula> buscarPorGenero(Genero genero) throws DAOException {
        String sql = "SELECT * FROM peliculas WHERE genero=? ORDER BY titulo";
        try (PreparedStatement ps = con().prepareStatement(sql)) {
            ps.setString(1, genero.name());
            return mapearLista(ps.executeQuery());
        } catch (SQLException e) {
            throw new DAOException("buscar peliculas por género", e);
        }
    }

    @Override
    public List<Pelicula> buscarDisponibles() throws DAOException {
        return ejecutarListado(
                "SELECT * FROM peliculas WHERE disponible=1 ORDER BY titulo");
    }

    @Override
    public List<Pelicula> buscarPorTitulo(String fragmento) throws DAOException {
        String sql = "SELECT * FROM peliculas WHERE titulo LIKE ? ORDER BY titulo";
        try (PreparedStatement ps = con().prepareStatement(sql)) {
            ps.setString(1, "%" + fragmento + "%");
            return mapearLista(ps.executeQuery());
        } catch (SQLException e) {
            throw new DAOException("buscar peliculas por título", e);
        }
    }

    // ── Helpers privados ───────────────────────────────────────────────────

    private Connection con() throws SQLException {
        return DatabaseConnection.getInstancia().getConexion();
    }

    private void mapearPelicula(PreparedStatement ps, Pelicula p) throws SQLException {
        ps.setString(1, p.getId());
        ps.setString(2, p.getTitulo());
        ps.setString(3, p.getGenero().name());
        ps.setInt(4, p.getAnio());
        ps.setDouble(5, p.getPrecioPorDia());
        ps.setInt(6, p.isDisponible() ? 1 : 0);
    }

    private Pelicula mapearResultSet(ResultSet rs) throws SQLException {
        Pelicula p = new Pelicula(
                rs.getString("id"),
                rs.getString("titulo"),
                Genero.valueOf(rs.getString("genero")),
                rs.getInt("anio"),
                rs.getDouble("precio_por_dia")
        );
        p.setDisponible(rs.getInt("disponible") == 1);
        return p;
    }

    private List<Pelicula> mapearLista(ResultSet rs) throws SQLException {
        List<Pelicula> lista = new ArrayList<>();
        while (rs.next()) lista.add(mapearResultSet(rs));
        return lista;
    }

    private List<Pelicula> ejecutarListado(String sql) throws DAOException {
        try (Statement st = con().createStatement()) {
            return mapearLista(st.executeQuery(sql));
        } catch (SQLException e) {
            throw new DAOException("listar peliculas", e);
        }
    }
}
