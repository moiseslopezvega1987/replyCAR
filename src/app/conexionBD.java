package app;

/*Clases importadas para la utilización en la clase.
 */
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * Esta clase se encarga de la conexíon de la base de datos.
 *
 * @author Moisés López Vega
 */
public class conexionBD {

    private static final Logger LOGGER = Logger.getLogger(conexionBD.class.getName());
    // Establecemos la conexión en nulo
    private static Connection conexion = null;
    // Driver de conexión
    private static String DRIVER;
    // String donde almacenar la URL de la base de datos a la que vamos a conectar
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    
    /**
    * Constructor de la clase conexionBD.
    * Inicializa la conexión con la base de datos.
    */
    public conexionBD() {
    }

    /**
     * Cargamos la configuración del archivo con los datos de la base de datos
     *
     */
    private static void cargarConfiguracion() {
        Properties prop = new Properties();
        InputStream is = null;
        try {
            File archivoExterno = new File("configBD.properties");
            if (archivoExterno.exists()) {
                is = new FileInputStream(archivoExterno);
            } else {
                is = conexionBD.class.getClassLoader().getResourceAsStream("resources/configBD.properties");
            }
            if (is == null) {
                throw new RuntimeException("No se encontró configBD.properties (ni externo ni dentro del JAR)");
            }
            prop.load(is);
            URL = prop.getProperty("db.url");
            USERNAME = prop.getProperty("db.user");
            PASSWORD = prop.getProperty("db.password");
            DRIVER = prop.getProperty("db.driver");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "No se pudo cargar el archivo de configuración", "Error configuración", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método para establecer la conexión con la base de datos
     *
     * @return conexión con la base de datos
     */
    public static Connection conectar() {
        try {
            if (conexion == null || conexion.isClosed()) {
                cargarConfiguracion();
                Class.forName(DRIVER);
                conexion = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (HeadlessException | ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "No hay conexión con la base de datos, revise los ajustes.", "Conexión inexistente", JOptionPane.ERROR_MESSAGE);
        }
        return conexion;
    }

    /**
     * Método para cerrar la conexión con la base de datos
     */
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                LOGGER.info("Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al cerrar la conexión con la base de datos.", e);
        }
    }
}
