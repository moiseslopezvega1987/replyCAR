package app;

/**
 * Clase que representa un usuario de la aplicación.
 *
 * @author Moisés López Vega
 */
public class usuarios {

    // Atributos del usuario
    private int id;           // ID del usuario
    private String nombre;    // Nombre del usuario
    private String apellidos; // Apellidos del usuario
    private String rol;       // Rol del usuario
    private String user;      // Usuario de la persona registrada
    private String password;  // Clave del usuario

    /**
     * Constructor, inicializa el objeto usuario con todos sus datos
     *
     * @param id ID del usuario
     * @param nombre Nombre del usuario
     * @param apellidos Apellidos del usuario
     * @param rol Rol del usuario
     * @param user Usuario de la persona registrada
     * @param password Clave del usuario
     */
    public usuarios(int id, String nombre, String apellidos, String rol, String user, String password) {

        // Se asignan los valores a los atributos de la clase
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.rol = rol;
        this.user = user;
        this.password = password;
    }

    /**
     * Constructor vacío
     */
    public usuarios() {
    }

    /**
     * Devuelve el ID del usuario
     *
     * @return ID del usuario
     */
    public int getId() {
        return id;
    }

    /**
     * Asigna el ID del usuario
     *
     * @param id ID del usuario
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre del usuario
     *
     * @return nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre del usuari
     *
     * @param nombre nombre del usuario
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve los apellidos del usuario
     *
     * @return apellidos del usuario
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Asigna los apellidos del usuar
     *
     * @param apellidos apellidos del usuario
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Devuelve el rol del usuario
     *
     * @return rol del usuario
     */
    public String getRol() {
        return rol;
    }

    /**
     * Asigna el rol del usuario
     *
     * @param rol rol del usuario
     */
    public void setRol(String rol) {
        this.rol = rol;
    }

    /**
     * Devuelve el usuario de la persona registrada
     *
     * @return usuario de la persona registrada
     */
    public String getUser() {
        return user;
    }

    /**
     * Asigna el usuario de la persona registrada
     *
     * @param user usuario de la persona registrada
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Devuelve la clave del usuario
     *
     * @return clave del usuario
     */
    public String getPassword() {
        return password;
    }

    /**
     * Asigna la clave del usuario
     *
     * @param password clave del usuario
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * toString(), devuelve una cadena con TODOS los datos del usuario
     *
     * @return cadena con TODOS los datos del usuario
     */
    @Override
    public String toString() {
        return "usuarios{" + "id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos + ", rol=" + rol + ", user=" + user + ", password=" + password + '}';
    }
}
