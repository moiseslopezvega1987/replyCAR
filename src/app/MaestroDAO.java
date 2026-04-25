package app;

/*Clases importadas para la utilización en la clase.
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Esta clase se encarga de todas las operaciones CRUD (Crear, Leer, Actualizar,
 * Borrar) sobre la tabla "Maestros" de la base de datos. También incluye
 * métodos auxiliares para obtener IDs, filtrar datos o llenar componentes GUI
 * como JTable y JTextField.
 *
 * @author Moisés López Vega
 */
public class MaestroDAO {

    /**
     * Constructor de la clase MaestroDAO. Inicializa la conexión con la base de
     * datos.
     */
    public MaestroDAO() {
    }

    // Creamos conexión
    Connection cn = conexionBD.conectar();
    
    // Ruta del archivo de la tarifa que pasa la marca con los precios de los vehículos
    String excelFilePath = "resources/tarifas/vehiculos.csv";

    /**
     * Método para obtener una columna
     *
     * @param tabla tabla pasada por parámetros
     * @param nombreColumna columna pasada por parámetros
     * @return devuelve el valor de una columna predeterminada
     */
    public List<String> obtenerColumnaDeterminada(String tabla, String nombreColumna) {
        List<String> valores = new ArrayList<>();
        String query = "SELECT " + nombreColumna + " FROM " + tabla;
        try (Statement stmt = cn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                valores.add(rs.getString(nombreColumna));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return valores;
    }

    /**
     * Método para actualizar la tarifa de los vehículos (fichero que pasa la
     * marca)
     *
     * @throws FileNotFoundException si ocurre un error al ejecutar la sentencia
     * SQL
     * @throws IOException si ocurre un error al ejecutar la sentencia SQL
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public void actualizarTarifaVehiculos() throws FileNotFoundException, IOException, SQLException {
        String line;
        String csvSeparator = ";";

        String deleteSql = "DELETE FROM tarifa_vehiculos";
        String sql = "INSERT INTO tarifa_vehiculos (id, modelo, precio, color, extras, foto) VALUES (?, ?, ?, ?, ?, ?)";

        try (BufferedReader br = new BufferedReader(new FileReader(excelFilePath)); PreparedStatement deleteStatement = cn.prepareStatement(deleteSql); PreparedStatement statement = cn.prepareStatement(sql)) {
            deleteStatement.executeUpdate();
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSeparator);
                int id = Integer.parseInt(values[0]);
                String modelo = values[1];
                double precio = Double.parseDouble(values[2]);
                String color = values[3];
                String extras = values[4];
                String foto = values[5];
                statement.setInt(1, id);
                statement.setString(2, modelo);
                statement.setDouble(3, precio);
                statement.setString(4, color);
                statement.setString(5, extras);
                statement.setString(6, foto);
                statement.addBatch();
            }
            statement.executeBatch();
            JOptionPane.showMessageDialog(null, "Tarifa actualizada correctamente.", "Todo ha ido bien", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "No se ha encontrado el fichero CSV.", "Error de ruta", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se ha podido insertar datos en la base de datos.", "Error al insertar", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Método para validar correo electrónico
     *
     * @param email email
     * @return email validado
     */
    public boolean validarEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Método para validar CIF
     *
     * @param cif cif
     * @return cif validado
     */
    public boolean validarCIF(String cif) {
        String cifRegex = "^[A-Za-z]\\d{7}[A-Za-z0-9]$";
        Pattern pattern = Pattern.compile(cifRegex);
        Matcher matcher = pattern.matcher(cif);
        return matcher.matches();
    }

    /**
     * Método para validar DNI
     *
     * @param dni dni
     * @return dni validado
     */
    public boolean validarDNI(String dni) {
        String dniRegex = "^\\d{8}[A-Za-z]$";
        Pattern pattern = Pattern.compile(dniRegex);
        Matcher matcher = pattern.matcher(dni);
        return matcher.matches();
    }

    /**
     * Método para obtener tarifa de taller según tipo de avería
     *
     * @param averia avería pasada por parámetros
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public void obtenerTarifaTallerPorTipoAveria(String averia) throws SQLException {
        vehiculo veh = null;
        String sql = "SELECT tipo_averia, precio_averia, texto FROM listado_vehiculos WHERE tipo_averia = ?";

        try (PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setString(1, averia);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    veh = new vehiculo();
                    veh.setBastidor(averia);
                    veh.setModelo(rs.getString("modelo"));
                    veh.setPrecio_compra(rs.getDouble("precio_compra"));
                    veh.setColor(rs.getString("color"));
                    veh.setExtras(rs.getString("extras"));
                    veh.setFoto(rs.getString("foto"));
                }
            }
        }
    }

    /**
     * Método para validar el login y obtener el rol
     *
     * @param usuario usuario
     * @param contrasena contraseña
     * @return login validado
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public String validarLogin(String usuario, String contrasena) throws SQLException {
        String rol = null;
        String sql = "SELECT rol, nombre, apellidos FROM usuarios WHERE user = ? AND password = ?";
        try {
            if (cn == null) {
                JOptionPane.showMessageDialog(null, "No se pudo conectar con la base de datos.", "Error conexión B.D.", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            try (PreparedStatement stmt = cn.prepareStatement(sql)) {
                stmt.setString(1, usuario);
                stmt.setString(2, contrasena);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        rol = rs.getString("rol");
                    }
                }
            }
        } catch (SQLException | NullPointerException ex) {
            Logger.getLogger(principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rol;
    }
}
