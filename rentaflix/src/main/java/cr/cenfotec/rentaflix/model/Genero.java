package cr.cenfotec.rentaflix.model;

/** Enumeración de géneros cinematográficos admitidos por el sistema. */
public enum Genero {
    ACCION("Acción"),
    COMEDIA("Comedia"),
    DRAMA("Drama"),
    TERROR("Terror"),
    CIENCIA_FICCION("Ciencia Ficción"),
    ANIMACION("Animación"),
    DOCUMENTAL("Documental"),
    ROMANCE("Romance");

    private final String etiqueta;

    Genero(String etiqueta) { this.etiqueta = etiqueta; }

    public String getEtiqueta() { return etiqueta; }

    @Override
    public String toString() { return etiqueta; }
}
