package app;

/**
 * Clase que representa las averías de la aplicación.
 *
 * @author Moisés López Vega
 */
public class averias {

    // Atributos de la avería
    private int id;                 // ID de la avería
    private String tipo;            // Tipo de avería
    private Double precio;          // Precio de la avería
    private String descripcion;     // Descripción de la avería

    /**
     * Constructor, inicializa el objeto averías con todos sus datos
     *
     * @param id ID de la avería
     * @param tipo_averia Tipo de avería
     * @param precio_averia Precio de la avería
     * @param texto Descripción de la avería
     */
    public averias(int id, String tipo_averia, Double precio_averia, String texto) {
        // Se asignan los valores a los atributos de la clase
        this.id = id;
        this.tipo = tipo_averia;
        this.precio = precio_averia;
        this.descripcion = texto;
    }

    /**
     * Constructor vacío
     */
    public averias() {
    }

    // Getters y Setters
    /**
     * Devuelve la ID de la avería
     *
     * @return Devuelve la ID de la avería
     */
    public int getId() {
        return id;
    }

    /**
     * Asigna la ID de la avería
     *
     * @param id Asigna la ID de la avería
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Devuelve el tipo de avería
     *
     * @return Devuelve el tipo de avería
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Asigna el tipo de avería
     *
     * @param tipo_averia Asigna el tipo de avería
     */
    public void setTipo(String tipo_averia) {
        this.tipo = tipo_averia;
    }

    /**
     * Devuelve el precio de la avería
     *
     * @return Devuelve el precio de la avería
     */
    public Double getPrecio() {
        return precio;
    }

    /**
     * Asigna el precio de la avería
     *
     * @param precio_averia Asigna el precio de la avería
     */
    public void setPrecio(Double precio_averia) {
        this.precio = precio_averia;
    }

    /**
     * Devuelve la descripción de la avería
     *
     * @return Devuelve la descripción de la avería
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Asigna la descripción de la avería
     *
     * @param texto Asigna la descripción de la avería
     */
    public void setDescripcion(String texto) {
        this.descripcion = texto;
    }

    /**
     * toString(), devuelve una cadena con TODOS los datos del artículo
     *
     * @return devuelve una cadena con TODOS los datos del artículo
     */
    @Override
    public String toString() {
        return "averias{" + "id=" + id + ", tipo=" + tipo + ", precio=" + precio + ", descripcion=" + descripcion + '}';
    }
}
