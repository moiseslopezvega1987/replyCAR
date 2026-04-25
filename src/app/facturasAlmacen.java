package app;

/*Clases importadas para la utilización en la clase.
 */
import java.time.LocalDate;

/**
 * Clase que representa una factura de almacén de la aplicación. Contiene datos
 * personales, dirección, contacto y forma de pago.
 *
 * @author Moisés López Vega
 */
public class facturasAlmacen {

    // Atributos de las facturas de almacén
    private String id_nfactura;       // Nº de la factura de almacén
    private LocalDate fechaFactura;   // Fecha de la factura de almacén
    private double baseImponible;     // B.I. de la factura de almacén
    private double iva;               // IVA de la factura de almacén
    private double importeFactura;    // Importe total de la factura de almacén
    private int ID_Cliente;           // ID del nº de cliente de la factura de almacén
    private String descripcion;       // Descripción de la línea de la factura de almacén
    private int cantidad;             // Cantidad de la línea de la factura de almacén
    private double precio_pvp;        // Precio PVP de la línea de la factura de almacén
    private int descuento;            // Descuento de la línea de la factura de almacén
    private double pFinalMenosDescuento;    // Precio final de la línea de la factura de almacén
    private int ID_Movimiento;        // ID del movimiento de la línea de la factura de almacén
    private boolean tipo;             // Compra o Venta, tipo del movimiento
    private int tlfMovil;             // Telefóno de la factura de almacén
    private String nie;               // CIF de la factura de almacén
    private String email;             // Email de la factura de almacén
    private String formaPago;         // Forma de pago de la factura de almacén
    private String poblacion;         // Población de la factura de almacén
    private String provincia;         // Provincia de la factura de almacén
    private String pais;              // País de la factura de almacén
    private String nCuenta;           // Número de cuenta de la factura de almacén

    /**
     * Constructor, inicializa el objeto facturas de almacén con todos sus datos
     *
     * @param id_nfactura Nº de la factura de almacén
     * @param fechaFactura Fecha de la factura de almacén
     * @param baseImponible B.I. de la factura de almacén
     * @param iva IVA de la factura de almacén
     * @param importeFactura Importe total de la factura de almacén
     * @param ID_Cliente ID del nº de cliente de la factura de almacén
     * @param descripcion Descripción de la línea de la factura de almacén
     * @param cantidad Cantidad de la línea de la factura de almacén
     * @param precio_pvp Precio PVP de la línea de la factura de almacén
     * @param descuento Descuento de la línea de la factura de almacén
     * @param pFinalMenosDescuento Precio final de la línea de la factura de
     * almacén
     * @param ID_Movimiento ID del movimiento de la línea de la factura de
     * almacén
     * @param tipo Compra o Venta, tipo del movimiento
     * @param tlfMovil CIF de la factura de almacén
     * @param nie CIF de la factura de almacén
     * @param email Email de la factura de almacén
     * @param formaPago Forma de pago de la factura de almacén
     * @param poblacion Población de la factura de almacén
     * @param provincia Provincia de la factura de almacén
     * @param pais País de la factura de almacén
     * @param nCuenta Número de cuenta de la factura de almacén
     */
    public facturasAlmacen(String id_nfactura, LocalDate fechaFactura, double baseImponible, double iva, double importeFactura, int ID_Cliente, String descripcion, int cantidad, double precio_pvp, int descuento, double pFinalMenosDescuento, int ID_Movimiento, boolean tipo, int tlfMovil, String nie, String email, String formaPago, String poblacion, String provincia, String pais, String nCuenta) {

        // Se asignan los valores a los atributos de la clase
        this.id_nfactura = id_nfactura;
        this.fechaFactura = fechaFactura;
        this.baseImponible = baseImponible;
        this.iva = iva;
        this.importeFactura = importeFactura;
        this.ID_Cliente = ID_Cliente;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precio_pvp = precio_pvp;
        this.descuento = descuento;
        this.pFinalMenosDescuento = pFinalMenosDescuento;
        this.ID_Movimiento = ID_Movimiento;
        this.tipo = tipo;
        this.tlfMovil = tlfMovil;
        this.nie = nie;
        this.email = email;
        this.formaPago = formaPago;
        this.poblacion = poblacion;
        this.provincia = provincia;
        this.pais = pais;
        this.nCuenta = nCuenta;
    }

    /**
     * Constructor vacío
     */
    public facturasAlmacen() {
    }

    // Getters y Setters
    /**
     * Asigna el ID del movimiento de almacén
     *
     * @param ID_Movimiento ID del movimiento de almacén
     */
    public void setID_Movimiento(int ID_Movimiento) {
        this.ID_Movimiento = ID_Movimiento;
    }

    /**
     * Devuelve el ID del movimiento de almacén
     *
     * @return ID del movimiento de almacén
     */
    public int getID_Movimiento() {
        return ID_Movimiento;
    }

    /**
     * Asigna el ID del cliente
     *
     * @param nCliente ID del cliente
     */
    public void setnCliente(int nCliente) {
        this.ID_Cliente = nCliente;
    }

    /**
     * Devuelve el ID del cliente
     *
     * @return ID del cliente
     */
    public int getnCliente() {
        return ID_Cliente;
    }

    /**
     * Devuelve precio final menos el descuento de la factura
     *
     * @return precio final menos el descuento de la factura
     */
    public double getpFinalMenosDescuento() {
        return pFinalMenosDescuento;
    }

    /**
     * Asigna precio final menos el descuento de la factura
     *
     * @param pvp precio pvp de la factura
     * @param dto descuento de la factura
     */
    public void setpFinalMenosDescuento(double pvp, int dto) {
        this.pFinalMenosDescuento = pvp - (pvp * dto / 100);
    }

    /**
     * Devuelve el precio final de la factura
     *
     * @return precio final de la factura
     */
    public double getpFinal() {
        return importeFactura;
    }

    /**
     * Asigna el precio final de la factura
     *
     * @param bImponible precio base imponible de la factura
     * @param iva importe iva de la factura
     */
    public void setpFinal(double bImponible, double iva) {
        bImponible = getbaseImponible();
        iva = getCalculoIVA();
        this.importeFactura = bImponible + iva;
    }

    /**
     * Devuelve el cálculo del IVA de la factura
     *
     * @return cálculo del IVA de la factura
     */
    public double getCalculoIVA() {
        return iva;
    }

    /**
     * Asigna el cálculo del IVA de la factura
     *
     * @param iva cálculo del IVA de la factura
     */
    public void setCalculoIVA(double iva) {
        this.iva = iva;
    }

    /**
     * Devuelve el nº de factura
     *
     * @return nº de factura
     */
    public String getNumero_factura() {
        return id_nfactura;
    }

    /**
     * Asigna el nº de factura
     *
     * @param numero_factura nº de factura
     */
    public void setNumero_factura(String numero_factura) {
        this.id_nfactura = numero_factura;
    }

    /**
     * Devuelve la fecha de la factura
     *
     * @return fecha de la factura
     */
    public LocalDate getFecha_factura() {
        return fechaFactura;
    }

    /**
     * Asigna la fecha de la factura
     *
     * @param fecha_factura fecha de la factura
     */
    public void setFecha_factura(LocalDate fecha_factura) {
        this.fechaFactura = fecha_factura;
    }

    /**
     * Devuelve la descripción del movimiento de almacén
     *
     * @return descripción del movimiento de almacén
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Asigna la descripción del movimiento de almacén
     *
     * @param descripcion descripción del movimiento de almacén
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Devuelve la cantidad del movimiento de almacén
     *
     * @return cantidad del movimiento de almacén
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Asigna la cantidad del movimiento de almacén
     *
     * @param cantidad cantidad del movimiento de almacén
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Devuelve el precio PVP de la factura
     *
     * @return precio PVP de la factura
     */
    public double getPrecio_pvp() {
        return precio_pvp;
    }

    /**
     * Asigna el precio PVP de la factura
     *
     * @param precio_pvp precio PVP de la factura
     */
    public void setPrecio_pvp(double precio_pvp) {
        this.precio_pvp = precio_pvp;
    }

    /**
     * Devuelve el descuento del movimiento de almacén
     *
     * @return descuento del movimiento de almacén
     */
    public int getDescuento() {
        return descuento;
    }

    /**
     * Asigna el descuento del movimiento de almacén
     *
     * @param descuento descuento del movimiento de almacén
     */
    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }

    /**
     * Devuelve la base imponible de la factura
     *
     * @return base imponible de la factura
     */
    public double getbaseImponible() {
        return baseImponible;
    }

    /**
     * Asigna la base imponible de la factura
     *
     * @param baseImponible base imponible de la factura
     */
    public void setbaseImponible(double baseImponible) {
        this.baseImponible = baseImponible;
    }

    /**
     * Devuelve el teléfono movil del cliente
     *
     * @return teléfono movil del cliente
     */
    public int getTlfMovil() {
        return tlfMovil;
    }

    /**
     * Asigna el teléfono movil del cliente
     *
     * @param tlfMovil teléfono movil del cliente
     */
    public void setTlfMovil(int tlfMovil) {
        this.tlfMovil = tlfMovil;
    }

    /**
     * Devuelve el NIE del cliente
     *
     * @return NIE del cliente
     */
    public String getNie() {
        return nie;
    }

    /**
     * Asigna el NIE del cliente
     *
     * @param nie NIE del cliente
     */
    public void setNie(String nie) {
        this.nie = nie;
    }

    /**
     * Devuelve el tipo de movimiento del almacén
     *
     * @return tipo de movimiento del almacén
     */
    public boolean isTipo() {
        return tipo;
    }

    /**
     * Asigna el tipo de movimiento del almacén
     *
     * @param tipo tipo de movimiento del almacén
     */
    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }

    /**
     * Devuelve el e-mail del cliente
     *
     * @return e-mail del cliente
     */
    public String getEmail() {
        return email;
    }

    /**
     * Asigna el e-mail del cliente
     *
     * @param email e-mail del cliente
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Devuelve la forma de pago del cliente
     *
     * @return forma de pago del cliente
     */
    public String getFormaPago() {
        return formaPago;
    }

    /**
     * Asigna la forma de pago del cliente
     *
     * @param formaPago forma de pago del cliente
     */
    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    /**
     * Devuelve la población del cliente
     *
     * @return población del cliente
     */
    public String getPoblacion() {
        return poblacion;
    }

    /**
     * Asigna la población del cliente
     *
     * @param poblacion población del cliente
     */
    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    /**
     * Devuelve la provincia del cliente
     *
     * @return provincia del cliente
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * Asigna la provincia del cliente
     *
     * @param provincia provincia del cliente
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * Devuelve el país del cliente
     *
     * @return país del cliente
     */
    public String getPais() {
        return pais;
    }

    /**
     * Asigna el país del cliente
     *
     * @param pais país del cliente
     */
    public void setPais(String pais) {
        this.pais = pais;
    }

    /**
     * Devuelve el nº de cuenta bancaria del client
     *
     * @return Devuelve el nº de cuenta bancaria del client
     */
    public String getnCuenta() {
        return nCuenta;
    }

    /**
     * Asigna el nº de cuenta bancaria del cliente
     *
     * @param nCuenta Asigna el nº de cuenta bancaria del cliente
     */
    public void setnCuenta(String nCuenta) {
        this.nCuenta = nCuenta;
    }

    /**
     * toString(), devuelve una cadena con TODOS los datos de la factura de
     * almacén
     *
     * @return cadena con TODOS los datos de la factura de almacén
     */
    @Override
    public String toString() {
        return "facturasAlmacen{" + "id_nfactura=" + id_nfactura + ", fechaFactura=" + fechaFactura + ", baseImponible=" + baseImponible + ", iva=" + iva + ", importeFactura=" + importeFactura + ", ID_Cliente=" + ID_Cliente + ", descripcion=" + descripcion + ", cantidad=" + cantidad + ", precio_pvp=" + precio_pvp + ", descuento=" + descuento + ", pFinalMenosDescuento=" + pFinalMenosDescuento + ", ID_Movimiento=" + ID_Movimiento + ", tipo=" + tipo + ", tlfMovil=" + tlfMovil + ", nie=" + nie + ", email=" + email + ", formaPago=" + formaPago + ", poblacion=" + poblacion + ", provincia=" + provincia + ", pais=" + pais + ", nCuenta=" + nCuenta + '}';
    }
}
