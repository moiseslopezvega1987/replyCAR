package app;

/**
 * Clases importadas para la utilización en la clase.
 */
import java.time.LocalDate;

/**
 * Clase que representa los vehículos de la aplicación.
 *
 * @author Moisés López Vega
 */
public class vehiculo {

    // Atributos del vehículo
    private int id;                         // ID del vehículo
    private String modelo;                  // Modelo del vehículo
    private String bastidor;                // Bastidor del vehículo
    private String matricula;               // Matrícula del vehículo
    private Double precio_compra;           // Precio de compra del vehículo
    private int totalVehiculos;             // Total de los vehículos
    private LocalDate fecha_mov;            // Fecha de movimiento en el histórico de vehículos
    private Double iva_compra;              // IVA de la compra al proveedor
    private Double iva_venta;               // IVA de la venta del cliente
    private Double total_compra;            // Total de la compra del vehículo
    private Double total_venta;             // Total de la venta del vehículo
    private String color;                   // Color del vehículo
    private String extras;                  // Extras del vehículo
    private Double precio_venta;            // Precio de venta del vehículo
    private String nfra_proveedor;          // Nº de factura de proveedor
    private String nfra_venta;              // Nº de factura de ventas
    private String foto;                    // Foto del vehículo
    private String comentarios;             // Comentarios del vehículo
    private int id_proveedor;               // ID del proveedor donde se ha comprado el vehículo
    private int id_cliente;                 // ID del cliente a quien se vende el vehículo
    private int id_TarifaVehiculo;          // ID de la tarifa del vehículo
    private LocalDate fecha_compra;         // Fecha de compra del vehículo
    private LocalDate fecha_venta;          // Fecha de venta del vehículo

    /**
     * Constructor, inicializa el objeto vehículo con todos sus datos
     *
     * @param id ID del vehículo
     * @param modelo Modelo del vehículo
     * @param bastidor Bastidor del vehículo
     * @param matricula Matrícula del vehículo
     * @param precio_compra Precio de compra del vehículo
     * @param totalVehiculos Total de los vehículos
     * @param fecha_mov Fecha de movimiento en el histórico de vehículos
     * @param iva_compra IVA de la compra al proveedor
     * @param iva_venta IVA de la venta del cliente
     * @param total_compra Total de la compra del vehículo
     * @param total_venta Total de la venta del vehículo
     * @param color Color del vehículo
     * @param extras Extras del vehículo
     * @param precio_venta Precio de venta del vehículo
     * @param nfra_proveedor Nº de factura de proveedor
     * @param nfra_venta Nº de factura de ventas
     * @param foto Foto del vehículo
     * @param comentarios Comentarios del vehículo
     * @param id_proveedor ID del proveedor donde se ha comprado el vehículo
     * @param id_cliente ID del cliente a quien se vende el vehículo
     * @param id_TarifaVehiculo ID de la tarifa del vehículo
     * @param fecha_compra Fecha de compra del vehículo
     * @param fecha_venta Fecha de venta del vehículo
     */
    public vehiculo(int id, String modelo, String bastidor, String matricula, Double precio_compra, int totalVehiculos, LocalDate fecha_mov, Double iva_compra, Double iva_venta, Double total_compra, Double total_venta, String color, String extras, Double precio_venta, String nfra_proveedor, String nfra_venta, String foto, String comentarios, int id_proveedor, int id_cliente, int id_TarifaVehiculo, LocalDate fecha_compra, LocalDate fecha_venta) {
        // Se asignan los valores a los atributos de la clase
        this.id = id;
        this.modelo = modelo;
        this.bastidor = bastidor;
        this.matricula = matricula;
        this.precio_compra = precio_compra;
        this.totalVehiculos = totalVehiculos;
        this.fecha_mov = fecha_mov;
        this.iva_compra = iva_compra;
        this.iva_venta = iva_venta;
        this.total_compra = total_compra;
        this.total_venta = total_venta;
        this.color = color;
        this.extras = extras;
        this.precio_venta = precio_venta;
        this.nfra_proveedor = nfra_proveedor;
        this.nfra_venta = nfra_venta;
        this.foto = foto;
        this.comentarios = comentarios;
        this.id_proveedor = id_proveedor;
        this.id_cliente = id_cliente;
        this.id_TarifaVehiculo = id_TarifaVehiculo;
        this.fecha_compra = fecha_compra;
        this.fecha_venta = fecha_venta;
    }

    /**
     * Constructor vacío
     */
    public vehiculo() {
    }

    // Getters y Setters
    /**
     * Devuelve la ID de la tarifa del vehículo
     *
     * @return ID de la tarifa del vehículo
     */
    public int getId_TarifaVehiculo() {
        return id_TarifaVehiculo;
    }

    /**
     * Asigna la ID de la tarifa del vehículo
     *
     * @param id_TarifaVehiculo ID de la tarifa del vehículo
     */
    public void setId_TarifaVehiculo(int id_TarifaVehiculo) {
        this.id_TarifaVehiculo = id_TarifaVehiculo;
    }

    /**
     * Devuelve los comentarios del vehículo
     *
     * @return comentarios del vehículo
     */
    public String getComentarios() {
        return comentarios;
    }

    /**
     * Asigna los comentarios del vehículo
     *
     * @param comentarios comentarios del vehículo
     */
    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    /**
     * Devuelve el importe de IVA de la venta del vehículo
     *
     * @return importe de IVA de la venta del vehículo
     */
    public Double getIva_venta() {
        return iva_venta;
    }

    /**
     * Asigna el importe de IVA de la venta del vehículo
     *
     * @param iva_venta importe de IVA de la venta del vehículo
     */
    public void setIva_venta(Double iva_venta) {
        this.iva_venta = iva_venta;
    }

    /**
     * Devuelve el total de la venta del vehículo
     *
     * @return total de la venta del vehículo
     */
    public Double getTotal_venta() {
        return total_venta;
    }

    /**
     * Asigna el total de la venta del vehículo
     *
     * @param total_venta total de la venta del vehículo
     */
    public void setTotal_venta(Double total_venta) {
        this.total_venta = total_venta;
    }

    /**
     * Devuelve el total de vehículos
     *
     * @return total de vehículos
     */
    public int getTotalVehiculos() {
        return totalVehiculos;
    }

    /**
     * Asigna el total de vehículos
     *
     * @param totalVehiculos total de vehículos
     */
    public void setTotalVehiculos(int totalVehiculos) {
        this.totalVehiculos = totalVehiculos;
    }

    /**
     * Devuelve la fecha del movimiento del vehículo
     *
     * @return fecha del movimiento del vehículo
     */
    public LocalDate getFecha_mov() {
        return fecha_mov;
    }

    /**
     * Asigna la fecha del movimiento del vehículo
     *
     * @param fecha_mov fecha del movimiento del vehículo
     */
    public void setFecha_mov(LocalDate fecha_mov) {
        this.fecha_mov = fecha_mov;
    }

    /**
     * Devuelve el importe IVA de la compra del vehículo
     *
     * @return importe IVA de la compra del vehículo
     */
    public Double getIva_compra() {
        return iva_compra;
    }

    /**
     * Asigna el importe IVA de la compra del vehículo
     *
     * @param iva_compra importe IVA de la compra del vehículo
     */
    public void setIva_compra(Double iva_compra) {
        this.iva_compra = iva_compra;
    }

    /**
     * Devuelve el total de la compra del vehículo
     *
     * @return total de la compra del vehículo
     */
    public Double getTotal_compra() {
        return total_compra;
    }

    /**
     * Asigna el total de la compra del vehículo
     *
     * @param total_compra total de la compra del vehículo
     */
    public void setTotal_compra(Double total_compra) {
        this.total_compra = total_compra;
    }

    /**
     * Devuelve la foto del vehículo
     *
     * @return foto del vehículo
     */
    public String getFoto() {
        return foto;
    }

    /**
     * Asigna la foto del vehículo
     *
     * @param foto foto del vehículo
     */
    public void setFoto(String foto) {
        this.foto = foto;
    }

    /**
     * Devuelve el ID del vehículo
     *
     * @return ID del vehículo
     */
    public int getId() {
        return id;
    }

    /**
     * Asigna el ID del vehículo
     *
     * @param id ID del vehículo
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Devuelve el modelo del vehículo
     *
     * @return modelo del vehículo
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * Asigna el modelo del vehículo
     *
     * @param modelo modelo del vehículo
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * Devuelve el bastidor del vehículo
     *
     * @return bastidor del vehículo
     */
    public String getBastidor() {
        return bastidor;
    }

    /**
     * Asigna el bastidor del vehículo
     *
     * @param bastidor bastidor del vehículo
     */
    public void setBastidor(String bastidor) {
        this.bastidor = bastidor;
    }

    /**
     * Devuelve la matrícula del vehículo
     *
     * @return matrícula del vehículo
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * Asigna la matrícula del vehículo
     *
     * @param matricula matrícula del vehículo
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * Devuelve el precio de compra del vehículo
     *
     * @return precio de compra del vehículo
     */
    public Double getPrecio_compra() {
        return precio_compra;
    }

    /**
     * Asigna el precio de compra del vehículo
     *
     * @param precio_compra precio de compra del vehículo
     */
    public void setPrecio_compra(Double precio_compra) {
        this.precio_compra = precio_compra;
    }

    /**
     * Devuelve el color del vehículo
     *
     * @return color del vehículo
     */
    public String getColor() {
        return color;
    }

    /**
     * Asigna el color del vehículo
     *
     * @param color color del vehículo
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Devuelve los extras del vehículo
     *
     * @return extras del vehículo
     */
    public String getExtras() {
        return extras;
    }

    /**
     * Asigna los extras del vehículo
     *
     * @param extras extras del vehículo
     */
    public void setExtras(String extras) {
        this.extras = extras;
    }

    /**
     * Devuelve el precio de venta del vehículo
     *
     * @return precio de venta del vehículo
     */
    public Double getPrecio_venta() {
        return precio_venta;
    }

    /**
     * Asigna el precio de venta del vehículo
     *
     * @param precio_venta precio de venta del vehículo
     */
    public void setPrecio_venta(Double precio_venta) {
        this.precio_venta = precio_venta;
    }

    /**
     * Devuelve el nº factura proveedor del vehículo
     *
     * @return nº factura proveedor del vehículo
     */
    public String getNfra_proveedor() {
        return nfra_proveedor;
    }

    /**
     * Asigna el nº factura proveedor del vehículo
     *
     * @param nfra_proveedor nº factura proveedor del vehículo
     */
    public void setNfra_proveedor(String nfra_proveedor) {
        this.nfra_proveedor = nfra_proveedor;
    }

    /**
     * Devuelve el nº factura de venta del vehículo
     *
     * @return nº factura de venta del vehículo
     */
    public String getNfra_venta() {
        return nfra_venta;
    }

    /**
     * Asigna el nº factura de venta del vehículo
     *
     * @param nfra_venta nº factura de venta del vehículo
     */
    public void setNfra_venta(String nfra_venta) {
        this.nfra_venta = nfra_venta;
    }

    /**
     * Devuelve el ID del proveedor de la compra del vehículo
     *
     * @return ID del proveedor de la compra del vehículo
     */
    public int getId_proveedor() {
        return id_proveedor;
    }

    /**
     * Asigna el ID del proveedor de la compra del vehículo
     *
     * @param id_proveedor ID del proveedor de la compra del vehículo
     */
    public void setId_proveedor(int id_proveedor) {
        this.id_proveedor = id_proveedor;
    }

    /**
     * Devuelve el ID del cliente en la venta del vehículo
     *
     * @return ID del cliente en la venta del vehículo
     */
    public int getId_cliente() {
        return id_cliente;
    }

    /**
     * Asigna el ID del cliente en la venta del vehículo
     *
     * @param id_cliente ID del cliente en la venta del vehículo
     */
    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    /**
     * Devuelve la fecha de compra del vehículo
     *
     * @return fecha de compra del vehículo
     */
    public LocalDate getFecha_compra() {
        return fecha_compra;
    }

    /**
     * Asigna la fecha de compra del vehículo
     *
     * @param fecha_compra fecha de compra del vehículo
     */
    public void setFecha_compra(LocalDate fecha_compra) {
        this.fecha_compra = fecha_compra;
    }

    /**
     * Devuelve la fecha de venta del vehículo
     *
     * @return fecha de venta del vehículo
     */
    public LocalDate getFecha_venta() {
        return fecha_venta;
    }

    /**
     * Asigna la fecha de venta del vehículo
     *
     * @param fecha_venta fecha de venta del vehículo
     */
    public void setFecha_venta(LocalDate fecha_venta) {
        this.fecha_venta = fecha_venta;
    }

    /**
     * toString(), devuelve una cadena con TODOS los datos del vehículo
     *
     * @return cadena con TODOS los datos del vehículo
     */
    @Override
    public String toString() {
        return "vehiculo{" + "id=" + id + ", modelo=" + modelo + ", bastidor=" + bastidor + ", matricula=" + matricula + ", precio_compra=" + precio_compra + ", totalVehiculos=" + totalVehiculos + ", fecha_mov=" + fecha_mov + ", iva_compra=" + iva_compra + ", iva_venta=" + iva_venta + ", total_compra=" + total_compra + ", total_venta=" + total_venta + ", color=" + color + ", extras=" + extras + ", precio_venta=" + precio_venta + ", nfra_proveedor=" + nfra_proveedor + ", nfra_venta=" + nfra_venta + ", foto=" + foto + ", comentarios=" + comentarios + ", id_proveedor=" + id_proveedor + ", id_cliente=" + id_cliente + ", id_TarifaVehiculo=" + id_TarifaVehiculo + ", fecha_compra=" + fecha_compra + ", fecha_venta=" + fecha_venta + '}';
    }
}
