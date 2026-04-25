package app;

/*Clases importadas para la utilización en la clase.
 */
import java.time.LocalDate;

/**
 * Clase que representa los artículos de la aplicación.
 *
 * @author Moisés López Vega
 */
public class articulo {

    // Atributos del artículo
    private String referencia;          // Referencia del artículo
    private String tipo;                // Tipo de artículo
    private String macrosector;         // Macrosector al que pertenece el artículo
    private String familia;             // Familia a la que pertenece el artículo
    private int ID_Familia;             // ID de la familia a la que pertenece el artículo
    private String descrip_familia;     // Descripción de la familia del artículo
    private String descripcion;         // Descricpion de la referencia (artículo)
    private String proveedor;           // Proveedor al que se ha comprado el artículo
    private Double pCoste;              // Precio de coste del artículo
    private Double pPVP;                // Precio PVP del artículo
    private double pFinal;              // Precio final del artículo
    private double calculoIVA;          // IVA del precio final del artículo
    private int descuento;              // Descuento correspondiente al artículo
    private int unidades;               // Unidades que se han utilizado en el movimiento del artículo
    private LocalDate fecha_mov;        // Fecha del movimiento en el almacén
    private String documento;           // Nº de documento del proveedor al que se le ha comprado el artículo
    private int totalPiezas;            // Nº total de artículos que hay de una referencia
    private int stockActual;            // Stock actual de artículos que hay de una referencia
    private String id_factura_almacen;  // Nº de factura de compra del almacén que ha realizado el proveedor
    private String id_factura_cliente;  // Nº de factura de ventas del almacén que se ha realizado al cliente

    /**
     * Constructor, inicializa el objeto artículo con todos sus datos
     *
     * @param referencia Referencia del artículo
     * @param tipo Tipo de artículo
     * @param macrosector Macrosector al que pertenece el artículo
     * @param familia Familia a la que pertenece el artículo
     * @param descrip_familia Descripción de la familia del artículo
     * @param descripcion Descricpion de la referencia (artículo)
     * @param proveedor Proveedor al que se ha comprado el artículo
     * @param pCoste Precio de coste del artículo
     * @param pPVP Precio PVP del artículo
     * @param pFinal Precio final del artículo
     * @param calculoIVA IVA del precio final del artículo
     * @param descuento Descuento correspondiente al artículo
     * @param unidades Unidades que se han utilizado en el movimiento del
     * artículo
     * @param fecha_mov Fecha del movimiento en el almacén
     * @param documento Nº de documento del proveedor al que se le ha comprado
     * el artículo
     * @param totalPiezas Nº total de artículos que hay de una referencia
     * @param stockActual Stock actual de artículos que hay de una referencia
     * @param id_factura_almacen Nº de factura de compra del almacén que ha
     * realizado el proveedor
     * @param id_factura_cliente Nº de factura de ventas del almacén que se ha
     * realizado al cliente
     * @param ID_Familia ID de la familia a la que pertenece el artículo
     */
    public articulo(String referencia, String tipo, String macrosector, String familia, String descrip_familia, String descripcion, String proveedor, Double pCoste, Double pPVP, double pFinal, double calculoIVA, int descuento, int unidades, LocalDate fecha_mov, String documento, int totalPiezas, int stockActual, String id_factura_almacen, String id_factura_cliente, int ID_Familia) {
        // Se asignan los valores a los atributos de la clase
        this.referencia = referencia;
        this.tipo = tipo;
        this.macrosector = macrosector;
        this.familia = familia;
        this.descrip_familia = descrip_familia;
        this.descripcion = descripcion;
        this.proveedor = proveedor;
        this.pCoste = pCoste;
        this.pPVP = pPVP;
        this.pFinal = pFinal;
        this.calculoIVA = calculoIVA;
        this.descuento = descuento;
        this.unidades = unidades;
        this.fecha_mov = fecha_mov;
        this.documento = documento;
        this.totalPiezas = totalPiezas;
        this.stockActual = stockActual;
        this.id_factura_almacen = id_factura_almacen;
        this.id_factura_cliente = id_factura_cliente;
        this.ID_Familia = ID_Familia;
    }

    /**
     * Constructor vacío
     */
    public articulo() {
    }

    // Getters y Setters
    /**
     * Devuelve la ID de la factura del cliente
     *
     * @return Devuelve la ID de la factura del cliente
     */
    public String getId_factura_cliente() {
        return id_factura_cliente;
    }

    /**
     * Asigna la ID de la factura del cliente
     *
     * @param id_factura_cliente Asigna la ID de la factura del cliente
     */
    public void setId_factura_cliente(String id_factura_cliente) {
        this.id_factura_cliente = id_factura_cliente;
    }

    /**
     * Devuelve la ID de la familia a la que pertenece el artículo
     *
     * @return Devuelve la ID de la familia a la que pertenece el artículo
     */
    public int getID_Familia() {
        return ID_Familia;
    }

    /**
     * Asigna la ID de la familia a la que pertenece el artículo
     *
     * @param ID_Familia Asigna la ID de la familia a la que pertenece el
     * artículo
     */
    public void setID_Familia(int ID_Familia) {
        this.ID_Familia = ID_Familia;
    }

    /**
     * Devuelve la ID de la factura del almacén
     *
     * @return Devuelve la ID de la factura del almacén
     */
    public String getId_factura_almacen() {
        return id_factura_almacen;
    }

    /**
     * Asigna la ID de la factura del almacén
     *
     * @param id_factura_almacen Asigna la ID de la factura del almacén
     */
    public void setId_factura_almacen(String id_factura_almacen) {
        this.id_factura_almacen = id_factura_almacen;
    }

    /**
     * Devuelve el precio de coste del artículo
     *
     * @return Devuelve el precio de coste del artículo
     */
    public Double getpCoste() {
        return pCoste;
    }

    /**
     * Asigna el precio de coste del artículo
     *
     * @param pCoste Asigna el precio de coste del artículo
     */
    public void setpCoste(Double pCoste) {
        this.pCoste = pCoste;
    }

    /**
     * Devuelve el precio de PVP del artículo
     *
     * @return Devuelve el precio de PVP del artículo
     */
    public Double getpPVP() {
        return pPVP;
    }

    /**
     * Asigna el precio de PVP del artículo
     *
     * @param pPVP Asigna el precio de PVP del artículo
     */
    public void setpPVP(Double pPVP) {
        this.pPVP = pPVP;
    }

    /**
     * Devuelve el stock actual de una referencia en el almacén
     *
     * @return Devuelve el stock actual de una referencia en el almacén
     */
    public int getStockActual() {
        return stockActual;
    }

    /**
     * Asigna el stock actual de una referencia en el almacén
     *
     * @param stockActual Asigna el stock actual de una referencia en el almacén
     */
    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    /**
     * Devuelve el precio final de una referencia
     *
     * @return Devuelve el precio final de una referencia
     */
    public double getpFinal() {
        return pFinal;
    }

    /**
     * // Asigna el precio final de una referencia
     *
     * @param pFinal Asigna el precio final de una referencia
     */
    public void setpFinal(double pFinal) {
        this.pFinal = pFinal + calculoIVA;
    }

    /**
     * Devuelve la cuota del IVA de una referencia
     *
     * @return Devuelve la cuota del IVA de una referencia
     */
    public double getCalculoIVA() {
        return calculoIVA;
    }

    /**
     * Asigna la cuota del IVA de una referencia
     *
     * @param pPVP Asigna la cuota del IVA de una referencia
     */
    public void setCalculoIVA(double pPVP) {
        this.calculoIVA = pPVP * 0.21;
    }

    /**
     * Devuelve el total de una referencia en el almacén
     *
     * @return Devuelve el total de una referencia en el almacén
     */
    public int getTotalPiezas() {
        return totalPiezas;
    }

    /**
     * Asigna el total de una referencia en el almacén
     *
     * @param totalPiezas Asigna el total de una referencia en el almacén
     */
    public void setTotalPiezas(int totalPiezas) {
        this.totalPiezas = totalPiezas;
    }

    /**
     * Devuelve el tipo de una referencia en el almacén
     *
     * @return Devuelve el tipo de una referencia en el almacén
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Asigna el tipo de una referencia en el almacén
     *
     * @param tipo Asigna el tipo de una referencia en el almacén
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Devuelve la descripción de una familia
     *
     * @return Devuelve la descripción de una familia
     */
    public String getDescrip_familia() {
        return descrip_familia;
    }

    /**
     * Asigna la descripción de una familia
     *
     * @param descrip_familia Asigna la descripción de una familia
     */
    public void setDescrip_familia(String descrip_familia) {
        this.descrip_familia = descrip_familia;
    }

    /**
     * Devuelve la fecha del movimiento en el almacén
     *
     * @return Devuelve la fecha del movimiento en el almacén
     */
    public LocalDate getFecha_mov() {
        return fecha_mov;
    }

    /**
     * Asigna la fecha del movimiento en el almacén
     *
     * @param fecha_mov Asigna la fecha del movimiento en el almacén
     */
    public void setFecha_mov(LocalDate fecha_mov) {
        this.fecha_mov = fecha_mov;
    }

    /**
     * Devuelve el documento con el que se compra la referencia
     *
     * @return Devuelve el documento con el que se compra la referencia
     */
    public String getDocumento() {
        return documento;
    }

    /**
     * Asigna el documento con el que se compra la referencia
     *
     * @param documento Asigna el documento con el que se compra la referencia
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /**
     * Devuelve las unidades del mov. en el almacén
     *
     * @return Devuelve las unidades del mov. en el almacén
     */
    public int getUnidades() {
        return unidades;
    }

    /**
     * Asigna las unidades del mov. en el almacén
     *
     * @param unidades Asigna las unidades del mov. en el almacén
     */
    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    /**
     * Devuelve el macrosector al que corresponde la referencia
     *
     * @return Devuelve el macrosector al que corresponde la referencia
     */
    public String getMacrosector() {
        return macrosector;
    }

    /**
     * Asigna el macrosector al que corresponde la referencia
     *
     * @param macrosector Asigna el macrosector al que corresponde la referencia
     */
    public void setMacrosector(String macrosector) {
        this.macrosector = macrosector;
    }

    /**
     * Devuelve la referencia solicitada
     *
     * @return Devuelve la referencia solicitada
     */
    public String getReferencia() {
        return referencia;
    }

    /**
     * Asigna la referencia solicitada
     *
     * @param referencia Asigna la referencia solicitada
     */
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    /**
     * Devuelve la familia de la referencia
     *
     * @return Devuelve la familia de la referencia
     */
    public String getFamilia() {
        return familia;
    }

    /**
     * Asigna la familia de la referencia
     *
     * @param familia Asigna la familia de la referencia
     */
    public void setFamilia(String familia) {
        this.familia = familia;
    }

    /**
     * Devuelve la descripción de la referencia
     *
     * @return Devuelve la descripción de la referencia
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Asigna la descripción de la referencia
     *
     * @param descripcion Asigna la descripción de la referencia
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Devuelve el proveedor de la referencia
     *
     * @return Devuelve el proveedor de la referencia
     */
    public String getProveedor() {
        return proveedor;
    }

    /**
     * Asigna el proveedor de la referencia
     *
     * @param proveedor Asigna el proveedor de la referencia
     */
    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    /**
     * Devuelve el descuento de la referencia
     *
     * @return Devuelve el descuento de la referencia
     */
    public int getDescuento() {
        return descuento;
    }

    /**
     * Asigna el descuento de la referencia
     *
     * @param descuento Asigna el descuento de la referencia
     */
    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }

    /**
     * toString(), devuelve una cadena con TODOS los datos del artículo
     *
     * @return devuelve una cadena con TODOS los datos del artículo
     */
    @Override
    public String toString() {
        return "articulo{" + "referencia=" + referencia + ", tipo=" + tipo + ", macrosector=" + macrosector + ", familia=" + familia + ", descrip_familia=" + descrip_familia + ", descripcion=" + descripcion + ", proveedor=" + proveedor + ", pCoste=" + pCoste + ", pPVP=" + pPVP + ", pFinal=" + pFinal + ", calculoIVA=" + calculoIVA + ", descuento=" + descuento + ", unidades=" + unidades + ", fecha_mov=" + fecha_mov + ", documento=" + documento + ", totalPiezas=" + totalPiezas + ", stockActual=" + stockActual + ", id_factura_almacen=" + id_factura_almacen + ", id_factura_cliente=" + id_factura_cliente + ", ID_Familia=" + ID_Familia + '}';
    }
}
