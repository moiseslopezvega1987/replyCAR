package app;

/**
 * Clase que representa los proveedores de la aplicación.
 *
 * @author Moisés López Vega
 */
public class proveedor {

    // Atributos del proveedor
    private int id;                  // ID del proveedor
    private String nombre;           // Nombre del proveedor
    private String apellido1;        // Primer apellido del proveedor
    private String apellido2;        // Segundo apellido del proveedor
    private String cp;               // C.P. del proveedor
    private String direccion;        // Dirección del proveedor
    private int numero;              // Nº de la dirección del proveedor
    private String tlfMovil;         // Nº móvil del proveedor
    private String nie;              // Nº identificación del proveedor
    private boolean tipo;            // Tipo de proveedor
    private String email;            // E-mail del proveedor
    private int formaPago;           // Forma de pago del proveedor
    private String poblacion;        // Población del proveedor
    private String provincia;        // Provincia del proveedor
    private String pais;             // País del proveedor
    private String nCuenta;          // Nº de cuenta del proveedor

    /**
     * Constructor, inicializa el objeto proveedor con todos sus datos
     *
     * @param id ID del proveedor
     * @param nombre Nombre del proveedor
     * @param apellido1 Primer apellido del proveedor
     * @param apellido2 Segundo apellido del proveedor
     * @param cp C.P. del proveedor
     * @param direccion Dirección del proveedor
     * @param numero Nº de la dirección del proveedor
     * @param tlfMovil Nº móvil del proveedor
     * @param nie Nº identificación del proveedor
     * @param tipo Tipo de proveedor
     * @param email E-mail del proveedor
     * @param formaPago Forma de pago del proveedor
     * @param poblacion Población del proveedor
     * @param provincia Provincia del proveedor
     * @param pais País del proveedor
     * @param nCuenta Nº de cuenta del proveedor
     */
    public proveedor(int id, String nombre, String apellido1, String apellido2, String cp, String direccion, int numero, String tlfMovil, String nie, boolean tipo, String email, int formaPago, String poblacion, String provincia, String pais, String nCuenta) {

        // Se asignan los valores a los atributos de la clase
        this.id = id;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.cp = cp;
        this.direccion = direccion;
        this.numero = numero;
        this.tlfMovil = tlfMovil;
        this.nie = nie;
        this.tipo = tipo;
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
    public proveedor() {
    }

    // Getters y Setters     
    /**
     * Asigna el nº de cuenta del proveedor
     *
     * @param nCuenta nº de cuenta del proveedor
     */
    public void setnCuenta(String nCuenta) {
        this.nCuenta = nCuenta;
    }

    /**
     * Devuelve el nº de cuenta del proveedor
     *
     * @return nº de cuenta del proveedor
     */
    public String getnCuenta() {
        return nCuenta;
    }

    /**
     * Devuelve el ID del proveedor
     *
     * @return ID del proveedor
     */
    public int getId() {
        return id;
    }

    /**
     * Asigna el ID del proveedor
     *
     * @param id ID del proveedor
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre del proveedor
     *
     * @return nombre del proveedor
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre del proveedor
     *
     * @param nombre nombre del proveedor
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve el primer apellido del proveedo
     *
     * @return primer apellido del proveedo
     */
    public String getApellido1() {
        return apellido1;
    }

    /**
     * Asigna el primer apellido del proveedor
     *
     * @param apellido1 primer apellido del proveedor
     */
    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    /**
     * Devuelve el segundo apellido del proveedor
     *
     * @return segundo apellido del proveedor
     */
    public String getApellido2() {
        return apellido2;
    }

    /**
     * Asigna el segundo apellido del proveedor
     *
     * @param apellido2 segundo apellido del proveedor
     */
    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    /**
     * Devuelve el C.P. del proveedor
     *
     * @return C.P. del proveedor
     */
    public String getCp() {
        return cp;
    }

    /**
     * Asigna el C.P. del proveedor
     *
     * @param cp C.P. del proveedor
     */
    public void setCp(String cp) {
        this.cp = cp;
    }

    /**
     * Devuelve la dirección del proveedor
     *
     * @return dirección del proveedor
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Asigna la dirección del proveedor
     *
     * @param direccion dirección del proveedor
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Devuelve el nº de la dirección del proveedor
     *
     * @return nº de la dirección del proveedor
     */
    public int getNumero() {
        return numero;
    }

    /**
     * Asigna el nº de la dirección del proveedor
     *
     * @param numero nº de la dirección del proveedor
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }

    /**
     * Devuelve el nº de movil del proveedor
     *
     * @return nº de movil del proveedor
     */
    public String getTlfMovil() {
        return tlfMovil;
    }

    /**
     * Asigna el nº de movil del proveedor
     *
     * @param tlfMovil nº de movil del proveedor
     */
    public void setTlfMovil(String tlfMovil) {
        this.tlfMovil = tlfMovil;
    }

    /**
     * Devuelve el nº de identificación del proveedor
     *
     * @return nº de identificación del proveedor
     */
    public String getNie() {
        return nie;
    }

    /**
     * Asigna el nº de identificación del proveedor
     *
     * @param nie nº de identificación del proveedor
     */
    public void setNie(String nie) {
        this.nie = nie;
    }

    /**
     * Devuelve el tipo de proveedor
     *
     * @return tipo de proveedor
     */
    public boolean isTipo() {
        return tipo;
    }

    /**
     * Asigna el tipo de proveedor
     *
     * @param tipo tipo de proveedor
     */
    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }

    /**
     * Devuelve el e-mail del proveedor
     *
     * @return e-mail del proveedor
     */
    public String getEmail() {
        return email;
    }

    /**
     * Asigna el e-mail del proveedor
     *
     * @param email e-mail del proveedor
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Devuelve la forma de pago del proveedor
     *
     * @return forma de pago del proveedor
     */
    public int getFormaPago() {
        return formaPago;
    }

    /**
     * Asigna la forma de pago del proveedor
     *
     * @param formaPago forma de pago del proveedor
     */
    public void setFormaPago(int formaPago) {
        this.formaPago = formaPago;
    }

    /**
     * Devuelve la población del proveedor
     *
     * @return población del proveedor
     */
    public String getPoblacion() {
        return poblacion;
    }

    /**
     * Asigna la población del proveedor
     *
     * @param poblacion población del proveedor
     */
    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    /**
     * Devuelve la provincia del proveedor
     *
     * @return provincia del proveedor
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * Asigna la provincia del proveedor
     *
     * @param provincia provincia del proveedor
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * Devuelve el país del proveedor
     *
     * @return país del proveedor
     */
    public String getPais() {
        return pais;
    }

    /**
     * Asigna el país del proveedor
     *
     * @param pais país del proveedor
     */
    public void setPais(String pais) {
        this.pais = pais;
    }

    /**
     * toString(), devuelve una cadena con TODOS los datos del proveedor
     *
     * @return cadena con TODOS los datos del proveedor
     */
    @Override
    public String toString() {
        return "proveedor{" + "id=" + id + ", nombre=" + nombre + ", apellido1=" + apellido1 + ", apellido2=" + apellido2 + ", cp=" + cp + ", direccion=" + direccion + ", numero=" + numero + ", tlfMovil=" + tlfMovil + ", nie=" + nie + ", tipo=" + tipo + ", email=" + email + ", formaPago=" + formaPago + ", poblacion=" + poblacion + ", provincia=" + provincia + ", pais=" + pais + ", nCuenta=" + nCuenta + '}';
    }
}
