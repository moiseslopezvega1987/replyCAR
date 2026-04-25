package app;

/**
 * Clase que representa a las intervenciones de taller de la aplicación.
 *
 * @author Moisés López Vega
 */
public class intervenciones {

    // Atributos de la factura de taller
    private String numero_factura;   // Nº de la factura de taller
    private String bastidor;         // Bastidor a la que corresponde la factura de taller
    private String modelo;           // Modelo a la que corresponde la factura de taller
    private String matricula;        // Matrícula a la que corresponde la factura de taller
    private String diag1;            // Diagnóstico a la que corresponde la factura de taller
    private String resum1;           // Resumen a la que corresponde la factura de taller
    private Double precio1;          // Precio a la que corresponde la factura de taller
    private int id_cliente;          // ID Cliente a la que corresponde la factura de taller
    private int id_tarifa;           // ID Tarifa a la que corresponde la factura de taller

    /**
     * Constructor, inicializa el objeto facturas de taller con todos sus datos
     *
     * @param numeroFactura Nº de la factura de taller
     * @param bastidor Bastidor a la que corresponde la factura de taller
     * @param modelo Modelo a la que corresponde la factura de taller
     * @param matricula Matrícula a la que corresponde la factura de taller
     * @param diagnostico Diagnóstico a la que corresponde la factura de taller
     * @param resumen Resumen a la que corresponde la factura de taller
     * @param precio Precio a la que corresponde la factura de taller
     * @param idCliente ID Cliente a la que corresponde la factura de taller
     * @param idTarifa ID Tarifa a la que corresponde la factura de taller
     */
    public intervenciones(String numeroFactura, String bastidor, String modelo, String matricula, String diagnostico, String resumen, double precio, int idCliente, int idTarifa) {
        // Se asignan los valores a los atributos de la clase
        this.numero_factura = numeroFactura;
        this.bastidor = bastidor;
        this.modelo = modelo;
        this.matricula = matricula;
        this.diag1 = diagnostico;
        this.resum1 = resumen;
        this.precio1 = precio;
        this.id_cliente = idCliente;
        this.id_tarifa = idTarifa;
    }

    /**
     * Constructor vacío
     */
    public intervenciones() {
    }

    // Getters y Setters
    /**
     * Devuelve el número de la factura de taller
     *
     * @return número de la factura de taller
     */
    public String getNumero_factura() {
        return numero_factura;
    }

    /**
     * Asigna el número de factura
     *
     * @param numero_factura número de factura
     */
    public void setNumero_factura(String numero_factura) {
        this.numero_factura = numero_factura;
    }

    /**
     * Devuelve el bastidor
     *
     * @return bastidor
     */
    public String getBastidor() {
        return bastidor;
    }

    /**
     * Asigna el bastidor
     *
     * @param bastidor bastidor
     */
    public void setBastidor(String bastidor) {
        this.bastidor = bastidor;
    }

    /**
     * Devuelve el ID del cliente
     *
     * @return ID del cliente
     */
    public int getId_cliente() {
        return id_cliente;
    }

    /**
     * Asigna el ID del cliente
     *
     * @param id_cliente ID del cliente
     */
    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    /**
     * Devuelve el modelo
     *
     * @return modelo
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * Asigna el modelo
     *
     * @param modelo modelo
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * Devuelve la matrícula
     *
     * @return matrícula
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * Asigna la matrícula
     *
     * @param matricula matrícula
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * Devuelve el diagnóstico
     *
     * @return diagnóstico
     */
    public String getDiag1() {
        return diag1;
    }

    /**
     * Asigna el diagnóstico
     *
     * @param diag1 diagnóstico
     */
    public void setDiag1(String diag1) {
        this.diag1 = diag1;
    }

    /**
     * Devuelve el ID de la tarifa
     *
     * @return ID de la tarifa
     */
    public int getId_tarifa() {
        return id_tarifa;
    }

    /**
     * Asigna el ID de la tarifa
     *
     * @param id_tarifa ID de la tarifa
     */
    public void setId_tarifa(int id_tarifa) {
        this.id_tarifa = id_tarifa;
    }

    /**
     * Devuelve el resumen del diagnóstico
     *
     * @return resumen del diagnóstico
     */
    public String getResum1() {
        return resum1;
    }

    /**
     * Asigna el resumen del diagnóstico
     *
     * @param resum1 resumen del diagnóstico
     */
    public void setResum1(String resum1) {
        this.resum1 = resum1;
    }

    /**
     * Devuelve el precio del diagnóstico
     *
     * @return precio del diagnóstico
     */
    public Double getPrecio1() {
        return precio1;
    }

    /**
     * Asigna el precio del diagnóstic
     *
     * @param precio1 precio del diagnóstic
     */
    public void setPrecio1(Double precio1) {
        this.precio1 = precio1;
    }

    /**
     * toString(), devuelve una cadena con TODOS los datos de la factura de
     * taller
     *
     * @return cadena con TODOS los datos de la factura de taller
     */
    @Override
    public String toString() {
        return "Intervenciones{" + "numero_factura=" + numero_factura + ", bastidor=" + bastidor + ", modelo=" + modelo + ", matricula=" + matricula + ", diag1=" + diag1 + ", resum1=" + resum1 + ", precio1=" + precio1 + ", id_cliente=" + id_cliente + ", id_tarifa=" + id_tarifa + '}';
    }
}
