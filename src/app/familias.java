package app;

/**
 * Clase que representa las familias de la aplicación.
 *
 * @author Moisés López Vega
 */
public class familias {

    // Atributos de la familia
    private int id;             // Identificador único de la familia
    private String nombre;      // Nombre del artículo
    private String sector;      // Nombre del sector

    /**
     * Constructor, inicializa el objeto familia con todos sus datos
     *
     * @param id Identificador único de la familia
     * @param nombre Nombre del artículo
     * @param sector Nombre del sector
     */
    public familias(int id, String nombre, String sector) {
        // Se asignan los valores a los atributos de la clase
        this.id = id;
        this.nombre = nombre;
        this.sector = sector;
    }

    /**
     * Constructor vacío
     */
    public familias() {
    }

    // Getters y Setters
    /**
     * Devuelve la ID del artículo
     *
     * @return Devuelve la ID del artículo
     */
    public int getId() {
        return id;
    }

    /**
     * Asigna la ID del artículo
     *
     * @param id Asigna la ID del artículo
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre del artículo
     *
     * @return Devuelve el nombre del artículo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre del artículo
     *
     * @param nombre Asigna el nombre del artículo
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve el sector del artículo
     *
     * @return Devuelve el sector del artículo
     */
    public String getSector() {
        return sector;
    }

    /**
     * Asigna l sector del artículo
     *
     * @param sector Asigna l sector del artículo
     */
    public void setSector(String sector) {
        this.sector = sector;
    }

    /**
     * toString(), devuelve una cadena con TODOS los datos de la familia
     *
     * @return devuelve una cadena con TODOS los datos de la familia
     */
    @Override
    public String toString() {
        return "familias{" + "id=" + id + ", nombre=" + nombre + ", sector=" + sector + '}';
    }
}
