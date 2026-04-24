package cr.cenfotec.rentaflix.dao;

import cr.cenfotec.rentaflix.exception.DAOException;
import cr.cenfotec.rentaflix.model.Cliente;
import java.util.List;

public interface ClienteDAO extends GenericDAO<Cliente, String> {

    List<Cliente> buscarPorNombre(String fragmento) throws DAOException;
    boolean existeCorreo(String correo)              throws DAOException;
}
