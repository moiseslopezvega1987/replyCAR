package app;

/**
 * Clase que representa los clientes de la aplicación.
 *
 * @author Moisés López Vega
 */
public class cliente {

    // Atributos del cliente
    private int id;                  // ID del cliente
    private String nombre;           // Nombre del cliente
    private String apellido1;        // Primer apellido del cliente
    private String apellido2;        // Segundo apellido del cliente
    private String cp;               // C.P. del cliente
    private String direccion;        // Dirección del cliente
    private int numero;              // Nº de la dirección del cliente
    private String tlfMovil;         // Nº Móvil del cliente
    private String nie;              // Nº identificación del cliente
    private boolean tipo;            // Tipo de cliente
    private String email;            // E-mail del cliente
    private int formaPago;           // Forma de pago del cliente
    private String formaPagoTxt;     // ID forma de pago del cliente
    private String poblacion;        // Población del cliente
    private String provincia;        // Provincia del cliente
    private String pais;             // País del cliente
    private String nCuenta;          // Número de cuenta del cliente

    /**
     * Constructor, inicializa el objeto cliente con todos sus datos
     *
     * @param id ID del cliente
     * @param nombre Nombre del cliente
     * @param apellido1 Primer apellido del cliente
     * @param apellido2 Segundo apellido del cliente
     * @param cp C.P. del cliente
     * @param direccion Dirección del cliente
     * @param numero Nº de la dirección del cliente
     * @param tlfMovil Nº Móvil del cliente
     * @param nie Nº identificación del cliente
     * @param tipo Tipo de cliente
     * @param email E-mail del cliente
     * @param formaPago Forma de pago del cliente
     * @param formaPagoTxt ID forma de pago del cliente
     * @param poblacion Población del cliente
     * @param provincia Provincia del cliente
     * @param pais País del cliente
     * @param nCuenta Número de cuenta del cliente
     */
    public cliente(int id, String nombre, String apellido1, String apellido2, String cp, String direccion, int numero, String tlfMovil, String nie, boolean tipo, String email, int formaPago, String formaPagoTxt, String poblacion, String provincia, String pais, String nCuenta) {
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
        this.formaPagoTxt = formaPagoTxt;
        this.poblacion = poblacion;
        this.provincia = provincia;
        this.pais = pais;
        this.nCuenta = nCuenta;
    }

    /**
     * Constructor vacío
     */
    public cliente() {
    }

    // Getters y Setters
    /**
     * Devuelve la forma de pago en formato texto
     *
     * @return forma de pago en formato texto
     */
    public String getFormaPagoTxt() {
        return formaPagoTxt;
    }

    /**
     * Asigna la forma de pago en formato texto
     *
     * @param formaPagoTXT forma de pago en formato texto
     */
    public void setFormaPagoTxt(String formaPagoTXT) {
        this.formaPagoTxt = formaPagoTXT;
    }

    /**
     * Asigna el nº de cuenta del cliente
     *
     * @param nCuenta nº de cuenta del cliente
     */
    public void setnCuenta(String nCuenta) {
        this.nCuenta = nCuenta;
    }

    /**
     * Devuelve el nº de cuenta del cliente
     *
     * @return nº de cuenta del cliente
     */
    public String getnCuenta() {
        return nCuenta;
    }

    /**
     * Devuelve el ID del cliente
     *
     * @return ID del cliente
     */
    public int getId() {
        return id;
    }

    /**
     * Asigna el ID del cliente
     *
     * @param id ID del cliente
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre del cliente
     *
     * @return nombre del cliente
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre del cliente
     *
     * @param nombre nombre del cliente
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve el primer apellido del cliente
     *
     * @return primer apellido del cliente
     */
    public String getApellido1() {
        return apellido1;
    }

    /**
     * Asigna el primer apellido del cliente
     *
     * @param apellido1 primer apellido del cliente
     */
    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    /**
     * Devuelve el segundo apellido del cliente
     *
     * @return segundo apellido del cliente
     */
    public String getApellido2() {
        return apellido2;
    }

    /**
     * Asigna el segundo apellido del cliente
     *
     * @param apellido2 segundo apellido del cliente
     */
    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    /**
     * Devuelve del código postal del cliente
     *
     * @return código postal del cliente
     */
    public String getCp() {
        return cp;
    }

    /**
     * Asigna del código postal del cliente
     *
     * @param cp código postal del cliente
     */
    public void setCp(String cp) {
        this.cp = cp;
    }

    /**
     * Devuelve la dirección del cliente
     *
     * @return dirección del cliente
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Asigna la dirección del cliente
     *
     * @param direccion dirección del cliente
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Devuelve el nº de la dirección del cliente
     *
     * @return nº de la dirección del cliente
     */
    public int getNumero() {
        return numero;
    }

    /**
     * Asigna el nº de la dirección del cliente
     *
     * @param numero nº de la dirección del cliente
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }

    /**
     * Devuelve el Nº móvil del cliente
     *
     * @return Nº móvil del cliente
     */
    public String getTlfMovil() {
        return tlfMovil;
    }

    /**
     * Asigna el Nº móvil del cliente
     *
     * @param tlfMovil Nº móvil del cliente
     */
    public void setTlfMovil(String tlfMovil) {
        this.tlfMovil = tlfMovil;
    }

    /**
     * Devuelve el nº de indentificación del cliente
     *
     * @return nº de indentificación del cliente
     */
    public String getNie() {
        return nie;
    }

    /**
     * Asigna el nº de indentificación del cliente
     *
     * @param nie nº de indentificación del cliente
     */
    public void setNie(String nie) {
        this.nie = nie;
    }

    /**
     * Devuelve el tipo del cliente
     *
     * @return tipo del cliente
     */
    public boolean isTipo() {
        return tipo;
    }

    /**
     * Asigna el tipo del cliente
     *
     * @param tipo tipo del cliente
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
    public int getFormaPago() {
        return formaPago;
    }

    /**
     * Asigna la forma de pago del cliente
     *
     * @param formaPago forma de pago del cliente
     */
    public void setFormaPago(int formaPago) {
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
     * toString(), devuelve una cadena con TODOS los datos del cliente
     *
     * @return cadena con TODOS los datos del cliente
     */
    @Override
    public String toString() {
        return nombre + " " + apellido1 + " " + apellido2;
    }
}
