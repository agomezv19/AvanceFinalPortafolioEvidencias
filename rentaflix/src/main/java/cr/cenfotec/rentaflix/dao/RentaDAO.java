package cr.cenfotec.rentaflix.dao;

import cr.cenfotec.rentaflix.exception.DAOException;
import cr.cenfotec.rentaflix.model.Renta;
import java.util.List;

public interface RentaDAO extends GenericDAO<Renta, String> {

    List<Renta> buscarPorCliente(String clienteId) throws DAOException;
    List<Renta> buscarActivas()                     throws DAOException;
}
