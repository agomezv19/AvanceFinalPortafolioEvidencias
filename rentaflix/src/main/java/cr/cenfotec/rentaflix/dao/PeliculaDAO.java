package cr.cenfotec.rentaflix.dao;

import cr.cenfotec.rentaflix.exception.DAOException;
import cr.cenfotec.rentaflix.model.Genero;
import cr.cenfotec.rentaflix.model.Pelicula;
import java.util.List;

public interface PeliculaDAO extends GenericDAO<Pelicula, String> {

    List<Pelicula> buscarPorGenero(Genero genero)    throws DAOException;
    List<Pelicula> buscarDisponibles()                throws DAOException;
    List<Pelicula> buscarPorTitulo(String fragmento) throws DAOException;
}
