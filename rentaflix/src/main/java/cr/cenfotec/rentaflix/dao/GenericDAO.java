package cr.cenfotec.rentaflix.dao;

import cr.cenfotec.rentaflix.exception.DAOException;
import java.util.List;
import java.util.Optional;


public interface GenericDAO<T, ID> {

    void guardar(T entidad)    throws DAOException;
    void actualizar(T entidad) throws DAOException;
    void eliminar(ID id)       throws DAOException;

    Optional<T>  buscarPorId(ID id)  throws DAOException;
    List<T>      obtenerTodos()      throws DAOException;
}
