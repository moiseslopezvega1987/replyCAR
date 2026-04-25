package app;

/**
 * Clases importadas para la utilización en la clase.
 */
import java.time.LocalDate;

/**
 * Clase que representa una factura de taller de la aplicación. Contiene datos
 * personales, dirección, contacto y forma de pago.
 *
 * @author Moisés López Vega
 */
public class facturasTaller {

    // Atributos de la factura de taller
    private String numero_factura;   // Nº de la factura de taller
    private LocalDate fecha_factura; // Fecha de la factura de taller
    private int id_cliente;          // ID Cliente a la que corresponde la factura de taller
    private int id_tarifa;           // ID Tarifa a la que corresponde la factura de taller
    private Double baseImponible;    // B.I. a la que corresponde la factura de taller
    private Double iva;              // IVA a la que corresponde la factura de taller
    private Double importeFactura;   // Importe total de factura a la que corresponde la factura de taller

    /**
     * Constructor, inicializa el objeto facturas de taller con todos sus datos
     *
     * @param numero_factura Nº de la factura de taller
     * @param fecha_factura Fecha de la factura de taller
     * @param id_cliente ID Cliente a la que corresponde la factura de taller
     * @param id_tarifa ID Tarifa a la que corresponde la factura de taller
     * @param baseImponible B.I. a la que corresponde la factura de taller
     * @param iva IVA a la que corresponde la factura de taller
     * @param importeFactura Importe total de factura a la que corresponde la
     * factura de taller
     */
    public facturasTaller(String numero_factura, LocalDate fecha_factura, int id_cliente, int id_tarifa, Double baseImponible, Double iva, Double importeFactura) {

        // Se asignan los valores a los atributos de la clase
        this.numero_factura = numero_factura;
        this.fecha_factura = fecha_factura;
        this.id_cliente = id_cliente;
        this.id_tarifa = id_tarifa;
        this.baseImponible = baseImponible;
        this.iva = iva;
        this.importeFactura = importeFactura;
    }

    /**
     * Constructor vacío
     */
    public facturasTaller() {
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
     * Devuelve la fecha de la factura
     *
     * @return fecha de la factura
     */
    public LocalDate getFecha_factura() {
        return fecha_factura;
    }

    /**
     * Asigna la fecha de la factura
     *
     * @param fecha_factura fecha de la factura
     */
    public void setFecha_factura(LocalDate fecha_factura) {
        this.fecha_factura = fecha_factura;
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
     * Devuelve la B.I. de la factura de taller
     *
     * @return B.I. de la factura de taller
     */
    public Double getBaseImponible() {
        return baseImponible;
    }

    /**
     * Asigna la B.I. de la factura de taller
     *
     * @param baseImponible B.I. de la factura de taller
     */
    public void setBaseImponible(Double baseImponible) {
        this.baseImponible = baseImponible;
    }

    /**
     * Devuelve el IVA de la factura de taller
     *
     * @return IVA de la factura de taller
     */
    public Double getIva() {
        return iva;
    }

    /**
     * Asigna el IVA de la factura de taller
     *
     * @param iva IVA de la factura de taller
     */
    public void setIva(Double iva) {
        this.iva = iva;
    }

    /**
     * Devuelve el importe total de la factura de taller
     *
     * @return importe total de la factura de taller
     */
    public Double getImporteFactura() {
        return importeFactura;
    }

    /**
     * Asigna el importe total de la factura de taller
     *
     * @param importeFactura importe total de la factura de taller
     */
    public void setImporteFactura(Double importeFactura) {
        this.importeFactura = importeFactura;
    }

    /**
     * toString(), devuelve una cadena con TODOS los datos de la factura de
     * taller
     *
     * @return cadena con TODOS los datos de la factura de taller
     */
    @Override
    public String toString() {
        return "facturasTaller{" + "numero_factura=" + numero_factura + ", fecha_factura=" + fecha_factura + ", id_cliente=" + id_cliente + ", id_tarifa=" + id_tarifa + ", baseImponible=" + baseImponible + ", iva=" + iva + ", importeFactura=" + importeFactura + '}';
    }
}
