package app;

/**
 * Clase que representa un forma de pago de la aplicación.
 *
 * @author Moisés López Vega
 */
public class formasPago {

    // Atributos de la forma de pago
    private int id;              // ID de la forma de pago
    private String tipo_pago;    // Tipo de pago

    /**
     * Constructor, inicializa el objeto forma de pago con todos sus datos
     *
     * @param id ID de la forma de pago
     * @param tipo_pago Tipo de pago
     */
    public formasPago(int id, String tipo_pago) {
        // Se asignan los valores a los atributos de la clase
        this.id = id;
        this.tipo_pago = tipo_pago;
    }

    /**
     * Constructor vacío
     */
    public formasPago() {
    }

    // Getters y Setters
    /**
     * Devuelve el ID de la forma de pago
     *
     * @return Devuelve el ID de la forma de pago
     */
    public int getId() {
        return id;
    }

    /**
     * Asigna el ID de la forma de pago
     *
     * @param id Asigna el ID de la forma de pago
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Devuelve el tipo de pago
     *
     * @return Devuelve el tipo de pago
     */
    public String getTipoPago() {
        return tipo_pago;
    }

    /**
     * Asigna el tipo de pago
     *
     * @param tipo_pago Asigna el tipo de pago
     */
    public void setTipoPago(String tipo_pago) {
        this.tipo_pago = tipo_pago;
    }

    /**
     * toString(), devuelve una cadena con TODOS los datos de la forma de pago
     *
     * @return devuelve una cadena con TODOS los datos de la forma de pago
     */
    @Override
    public String toString() {
        return "formasPago{" + "id=" + id + ", tipo_pago=" + tipo_pago + '}';
    }
}
