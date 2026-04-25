package app;

/**
 * Clases importadas para la utilización en la clase.
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Esta clase se encarga de todas las operaciones CRUD (Crear, Leer, Actualizar,
 * Borrar) sobre la tabla "Tarifa Taller" de la base de datos. También incluye
 * métodos auxiliares para obtener IDs, filtrar datos o llenar componentes GUI
 * como JTable y JTextField.
 *
 * @author Moisés López Vega
 */
public class TarifaTallerDAO {

    /**
     * Constructor de la clase TarifaTallerDAO. Inicializa la conexión con la
     * base de datos.
     */
    public TarifaTallerDAO() {
    }

    // Creamos conexión
    conexionBD con = new conexionBD();
    Connection cn = con.conectar();

    /**
     * Método que obtiene el siguiente ID disponible de la tabla tarifa_taller y
     * lo coloca en un JTextField proporcionado
     *
     * @param textField el JTextField donde se mostrará el siguiente ID
     * disponible
     */
    public void obtenerSiguienteID(JTextField textField) {
        try {
            String query = "SELECT MAX(id) AS max_id FROM tarifa_taller";
            Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                int nextId = rs.getInt("max_id") + 1;
                textField.setText(String.valueOf(nextId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que guarda una nueva tarifa de taller en la base de datos
     *
     * @param tarifaTaller objeto averias que contiene los datos de la tarifa a
     * insertar
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL de
     * inserción
     */
    public void guardarTarifaTaller(averias tarifaTaller) throws SQLException {
        String sql = "INSERT INTO tarifa_taller (id, tipo_averia, precio_averia, texto) "
                + "VALUES (?, ?, ?, ?)";
        Connection conn = conexionBD.conectar();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tarifaTaller.getId());
            stmt.setString(2, tarifaTaller.getTipo());
            stmt.setDouble(3, tarifaTaller.getPrecio());
            stmt.setString(4, tarifaTaller.getDescripcion());
            stmt.executeUpdate();
        } finally {
        }
    }

    /**
     * Método que obtiene todas las tarifas de taller registradas en la base de
     * datos
     *
     * @return una List de objetos averias con todas las tarifas registradas
     */
    public List<averias> obtenerTodasLasTarifasTaller() {
        List<averias> tarifa = new ArrayList<>();
        String sql = "SELECT * FROM tarifa_taller";

        try (Statement statement = cn.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                averias tarifaTaller = new averias();
                tarifaTaller.setId(resultSet.getInt("id"));
                tarifaTaller.setTipo(resultSet.getString("tipo_averia"));
                tarifaTaller.setPrecio(resultSet.getDouble("precio_averia"));
                tarifaTaller.setDescripcion(resultSet.getString("texto"));
                tarifa.add(tarifaTaller);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tarifa;
    }

    /**
     * Método que obtiene el próximo ID disponible para la tabla tarifa_taller
     *
     * @return el siguiente ID disponible en la tabla tarifa_taller
     */
    public int obtenerUltimaID() {
        int ultimaID = 0;
        try {
            String query = "SELECT MAX(id) AS max_id FROM tarifa_taller";
            Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                ultimaID = rs.getInt("max_id") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ultimaID;
    }

    /**
     * Método que actualiza una tarifa de taller existente en la base de datos
     *
     * @param tarifaTaller objeto averias que contiene el ID de la tarifa a
     * actualizar y los nuevos valores de tipo, precio y descripción
     */
    public void actualizarTarifaTaller(averias tarifaTaller) {
        String sql = "UPDATE tarifa_taller SET tipo_averia=?, precio_averia=?, texto=? WHERE id=?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, tarifaTaller.getTipo());
            ps.setDouble(2, tarifaTaller.getPrecio());
            ps.setString(3, tarifaTaller.getDescripcion());
            ps.setInt(4, tarifaTaller.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que uestra todas las tarifas de taller en un JTable
     *
     * @param tarifasTaller el JTable donde se mostrarán las tarifas de taller
     * @return el JTable actualizado con todas las tarifas de taller
     */
    public JTable mostrarTarifasTaller(JTable tarifasTaller) {
        DefaultTableModel tabla = new DefaultTableModel();
        tabla.addColumn("ID");
        tabla.addColumn("Tipo avería");
        tabla.addColumn("Precio avería");
        tabla.addColumn("Descripción");
        tarifasTaller.setModel(tabla);

        String SQL = "SELECT * FROM tarifa_taller";
        String datos[] = new String[50];

        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.of("es", "ES"));

        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                double precio = rs.getDouble("precio_averia");
                datos[2] = formatoMoneda.format(precio);
                datos[3] = rs.getString(4);
                tabla.addRow(datos);
            }
            tarifasTaller.setModel(tabla);
        } catch (SQLException ex) {
        }
        return tarifasTaller;
    }

    /**
     * Método que filtra la tabla de tarifas de taller según un criterio
     * específico
     *
     * @param tarifaTaller el JTable que contiene las tarifas de taller a
     * filtrar
     * @param filtro el campo por el que se desea filtrar; puede ser "ID", "Tipo
     * avería", "Precio avería" o "Descripción"
     * @param valor el valor a buscar dentro del campo especificado
     * @return el JTable actualizado con las filas que cumplen el filtro
     */
    public JTable filtrarTarifaTaller(JTable tarifaTaller, String filtro, String valor) {
        DefaultTableModel tabla = new DefaultTableModel();
        tabla.addColumn("ID");
        tabla.addColumn("Tipo avería");
        tabla.addColumn("Precio avería");
        tabla.addColumn("Descripción");
        tarifaTaller.setModel(tabla);

        String SQL = "";

        switch (filtro) {
            case "ID":
                SQL = "SELECT * FROM tarifa_taller WHERE id LIKE '%" + valor + "%'";
                break;
            case "Tipo avería":
                SQL = "SELECT * FROM tarifa_taller WHERE tipo_averia LIKE '%" + valor + "%'";
                break;
            case "Precio avería":
                SQL = "SELECT * FROM tarifa_taller WHERE precio_averia LIKE '%" + valor + "%'";
                break;
            case "Descripción":
                SQL = "SELECT * FROM tarifa_taller WHERE texto LIKE '%" + valor + "%'";
                break;
        }

        String datos[] = new String[50];

        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                tabla.addRow(datos);
            }
            tarifaTaller.setModel(tabla);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tarifaTaller;
    }

    /**
     * Método que elimina una tarifa del taller según su ID
     *
     * @param tarifaID el identificador único de la tarifa que se desea eliminar
     * @return true si se eliminó al menos una fila; false si no se encontró la
     * tarifa o ocurrió un error
     */
    public boolean eliminarTarifaPorId(int tarifaID) {
        try {
            String query = "DELETE FROM tarifa_taller WHERE id = ?";
            PreparedStatement stmt = cn.prepareStatement(query);
            stmt.setInt(1, tarifaID);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
