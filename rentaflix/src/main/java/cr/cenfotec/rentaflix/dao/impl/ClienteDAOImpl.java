package cr.cenfotec.rentaflix.dao.impl;

import cr.cenfotec.rentaflix.dao.ClienteDAO;
import cr.cenfotec.rentaflix.exception.DAOException;
import cr.cenfotec.rentaflix.model.Cliente;
import cr.cenfotec.rentaflix.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public void guardar(Cliente c) throws DAOException {
        String sql = """
            INSERT INTO clientes (cliente_id, nombre, telefono, correo, calle, ciudad, codigo_postal)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = con().prepareStatement(sql)) {
            mapear(ps, c);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("guardar cliente", e);
        }
    }

    @Override
    public void actualizar(Cliente c) throws DAOException {
        String sql = """
            UPDATE clientes SET nombre=?, telefono=?, correo=?, calle=?, ciudad=?, codigo_postal=?
            WHERE cliente_id=?
        """;
        try (PreparedStatement ps = con().prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getTelefono());
            ps.setString(3, c.getCorreo());
            ps.setString(4, c.getDireccion().getCalle());
            ps.setString(5, c.getDireccion().getCiudad());
            ps.setString(6, c.getDireccion().getCodigoPostal());
            ps.setString(7, c.getClienteId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("actualizar cliente", e);
        }
    }

    @Override
    public void eliminar(String id) throws DAOException {
        try (PreparedStatement ps = con().prepareStatement(
                "DELETE FROM clientes WHERE cliente_id=?")) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("eliminar cliente", e);
        }
    }

    @Override
    public Optional<Cliente> buscarPorId(String id) throws DAOException {
        try (PreparedStatement ps = con().prepareStatement(
                "SELECT * FROM clientes WHERE cliente_id=?")) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
        } catch (SQLException e) {
            throw new DAOException("buscar cliente por id", e);
        }
    }

    @Override
    public List<Cliente> obtenerTodos() throws DAOException {
        try (Statement st = con().createStatement()) {
            return mapearLista(st.executeQuery("SELECT * FROM clientes ORDER BY nombre"));
        } catch (SQLException e) {
            throw new DAOException("listar clientes", e);
        }
    }

    @Override
    public List<Cliente> buscarPorNombre(String fragmento) throws DAOException {
        try (PreparedStatement ps = con().prepareStatement(
                "SELECT * FROM clientes WHERE nombre LIKE ? ORDER BY nombre")) {
            ps.setString(1, "%" + fragmento + "%");
            return mapearLista(ps.executeQuery());
        } catch (SQLException e) {
            throw new DAOException("buscar clientes por nombre", e);
        }
    }

    @Override
    public boolean existeCorreo(String correo) throws DAOException {
        try (PreparedStatement ps = con().prepareStatement(
                "SELECT COUNT(*) FROM clientes WHERE correo=?")) {
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new DAOException("verificar correo", e);
        }
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    private Connection con() throws SQLException {
        return DatabaseConnection.getInstancia().getConexion();
    }

    private void mapear(PreparedStatement ps, Cliente c) throws SQLException {
        ps.setString(1, c.getClienteId());
        ps.setString(2, c.getNombre());
        ps.setString(3, c.getTelefono());
        ps.setString(4, c.getCorreo());
        ps.setString(5, c.getDireccion().getCalle());
        ps.setString(6, c.getDireccion().getCiudad());
        ps.setString(7, c.getDireccion().getCodigoPostal());
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getString("cliente_id"),
                rs.getString("nombre"),
                rs.getString("telefono"),
                rs.getString("correo"),
                rs.getString("calle"),
                rs.getString("ciudad"),
                rs.getString("codigo_postal")
        );
    }

    private List<Cliente> mapearLista(ResultSet rs) throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        while (rs.next()) lista.add(mapear(rs));
        return lista;
    }
}
