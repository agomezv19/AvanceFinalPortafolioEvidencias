package cr.cenfotec.rentaflix.dao.impl;

import cr.cenfotec.rentaflix.dao.ClienteDAO;
import cr.cenfotec.rentaflix.dao.PeliculaDAO;
import cr.cenfotec.rentaflix.dao.RentaDAO;
import cr.cenfotec.rentaflix.exception.DAOException;
import cr.cenfotec.rentaflix.model.Cliente;
import cr.cenfotec.rentaflix.model.Pelicula;
import cr.cenfotec.rentaflix.model.Renta;
import cr.cenfotec.rentaflix.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class RentaDAOImpl implements RentaDAO {

    private final ClienteDAO  clienteDAO;
    private final PeliculaDAO peliculaDAO;

    public RentaDAOImpl(ClienteDAO clienteDAO, PeliculaDAO peliculaDAO) {
        this.clienteDAO  = clienteDAO;
        this.peliculaDAO = peliculaDAO;
    }

    @Override
    public void guardar(Renta r) throws DAOException {
        String sql = """
            INSERT INTO rentas (renta_id, cliente_id, pelicula_id,
                                fecha_inicio, fecha_fin, activa, costo_final)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = con().prepareStatement(sql)) {
            ps.setString(1, r.getRentaId());
            ps.setString(2, r.getCliente().getClienteId());
            ps.setString(3, r.getPelicula().getId());
            ps.setString(4, r.getFechaInicio().toString());
            ps.setString(5, r.getFechaFin().toString());
            ps.setInt(6, r.isActiva() ? 1 : 0);
            ps.setDouble(7, r.getCostoFinal());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("guardar renta", e);
        }
    }

    @Override
    public void actualizar(Renta r) throws DAOException {
        String sql = """
            UPDATE rentas SET fecha_fin=?, activa=?, costo_final=?
            WHERE renta_id=?
        """;
        try (PreparedStatement ps = con().prepareStatement(sql)) {
            ps.setString(1, r.getFechaFin().toString());
            ps.setInt(2, r.isActiva() ? 1 : 0);
            ps.setDouble(3, r.getCostoFinal());
            ps.setString(4, r.getRentaId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("actualizar renta", e);
        }
    }

    @Override
    public void eliminar(String id) throws DAOException {
        try (PreparedStatement ps = con().prepareStatement(
                "DELETE FROM rentas WHERE renta_id=?")) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("eliminar renta", e);
        }
    }

    @Override
    public Optional<Renta> buscarPorId(String id) throws DAOException {
        try (PreparedStatement ps = con().prepareStatement(
                "SELECT * FROM rentas WHERE renta_id=?")) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(rehidratar(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new DAOException("buscar renta por id", e);
        }
    }

    @Override
    public List<Renta> obtenerTodos() throws DAOException {
        try (Statement st = con().createStatement()) {
            return mapearLista(st.executeQuery(
                    "SELECT * FROM rentas ORDER BY fecha_inicio DESC"));
        } catch (SQLException e) {
            throw new DAOException("listar rentas", e);
        }
    }

    @Override
    public List<Renta> buscarPorCliente(String clienteId) throws DAOException {
        try (PreparedStatement ps = con().prepareStatement(
                "SELECT * FROM rentas WHERE cliente_id=? ORDER BY fecha_inicio DESC")) {
            ps.setString(1, clienteId);
            return mapearLista(ps.executeQuery());
        } catch (SQLException e) {
            throw new DAOException("buscar rentas por cliente", e);
        }
    }

    @Override
    public List<Renta> buscarActivas() throws DAOException {
        try (Statement st = con().createStatement()) {
            return mapearLista(st.executeQuery("SELECT * FROM rentas WHERE activa=1"));
        } catch (SQLException e) {
            throw new DAOException("buscar rentas activas", e);
        }
    }

    // ── Helpers privados ───────────────────────────────────────────────────

    private Connection con() throws SQLException {
        return DatabaseConnection.getInstancia().getConexion();
    }

    private Renta rehidratar(ResultSet rs) throws SQLException, DAOException {
        String clienteId  = rs.getString("cliente_id");
        String peliculaId = rs.getString("pelicula_id");

        Cliente cliente = clienteDAO.buscarPorId(clienteId)
                .orElseThrow(() -> new DAOException(
                        "Cliente '" + clienteId + "' no encontrado al rehidratar renta."));
        Pelicula pelicula = peliculaDAO.buscarPorId(peliculaId)
                .orElseThrow(() -> new DAOException(
                        "Pelicula '" + peliculaId + "' no encontrada al rehidratar renta."));

        return new Renta(
                rs.getString("renta_id"),
                cliente,
                pelicula,
                LocalDate.parse(rs.getString("fecha_inicio")),
                LocalDate.parse(rs.getString("fecha_fin")),
                rs.getInt("activa") == 1,
                rs.getDouble("costo_final")
        );
    }

    private List<Renta> mapearLista(ResultSet rs) throws SQLException, DAOException {
        List<Renta> lista = new ArrayList<>();
        while (rs.next()) lista.add(rehidratar(rs));
        return lista;
    }
}
