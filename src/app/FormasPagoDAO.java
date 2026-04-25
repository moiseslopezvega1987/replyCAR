package app;

/*Clases importadas para la utilización en la clase.
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Esta clase se encarga de todas las operaciones CRUD (Crear, Leer, Actualizar,
 * Borrar) sobre la tabla "Formas de pago" de la base de datos. También incluye
 * métodos auxiliares para obtener IDs, filtrar datos o llenar componentes GUI
 * como JTable y JTextField.
 *
 * @author Moisés López Vega
 */
public class FormasPagoDAO {

    /**
     * Constructor de la clase FormasPagoDAO. Inicializa la conexión con la base
     * de datos.
     */
    public FormasPagoDAO() {
    }

    // Creamos conexión
    conexionBD con = new conexionBD();
    Connection cn = con.conectar();

    /**
     * Método para obtener el siguiente ID disponible
     *
     * @param textField valor a pasar por parámetros
     */
    public void obtenerSiguienteID(JTextField textField) {

        try {
            String query = "SELECT MAX(id) AS max_id FROM formas_pago";
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
     * Método para obtener columna de la tabla
     *
     * @param nombreColumna columna nº 1
     * @return el valor de la columna nº 1
     */
    public List<String> obtenerColumna(String nombreColumna) {
        List<String> valores = new ArrayList<>();
        String query = "SELECT " + nombreColumna + " FROM formas_pago";

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
     * Método para obtener dos columnas de la tabla
     *
     * @param columna1 columna nº1
     * @param columna2 columna nº2
     * @return devuelve el valor de las dos columnas
     */
    public List<String[]> obtenerDosColumnas(String columna1, String columna2) {
        List<String[]> valores = new ArrayList<>();
        String query = "SELECT " + columna1 + ", " + columna2 + " FROM formas_pago";

        try (Statement stmt = cn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String[] fila = new String[2];
                fila[0] = rs.getString(columna1);
                fila[1] = rs.getString(columna2);
                valores.add(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return valores;
    }

    /**
     * Método para guardar una forma de pago en la base de datos
     *
     * @param formasPago formas de pago
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public void guardarFormaPago(formasPago formasPago) throws SQLException {

        String sql = "INSERT INTO formas_pago (id, tipo_pago) "
                + "VALUES (?, ?)";
        Connection conn = conexionBD.conectar();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, formasPago.getId());
            stmt.setString(2, formasPago.getTipoPago());
            stmt.executeUpdate();
        } finally {
        }
    }

    /**
     * Método para obtener todas las formas de pago de la base de datos
     *
     * @return todas las formas de pago de la base de datos
     */
    public List<formasPago> obtenerTodasLasFormasPago() {
        List<formasPago> formaPago = new ArrayList<>();

        String sql = "SELECT * FROM formas_pago";

        try (Statement statement = cn.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                formasPago formasPago = new formasPago();
                formasPago.setId(resultSet.getInt("id"));
                formasPago.setTipoPago(resultSet.getString("tipo_pago"));
                formaPago.add(formasPago);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return formaPago;
    }

    /**
     * Método para obtener el último ID registrado
     *
     * @return último ID registrado
     */
    public int obtenerUltimaID() {
        int ultimaID = 0;
        try {
            String query = "SELECT MAX(id) AS max_id FROM formas_pago";
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
     * Método para actualizar una forma de pago existente en la base de datos
     *
     * @param formasPago formas de pago existente en la base de datos
     */
    public void actualizarFormaPago(formasPago formasPago) {
        String sql = "UPDATE formas_pago SET tipo_pago=? WHERE id=?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, formasPago.getTipoPago());
            ps.setInt(2, formasPago.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para mostrar todos las formas de pago en un JTable
     *
     * @param formasPago formas de pago en un JTable
     * @return mostrar todos las formas de pago en un JTable
     */
    public JTable mostrarFormasPago(JTable formasPago) {
        DefaultTableModel tabla = new DefaultTableModel();
        tabla.addColumn("ID");
        tabla.addColumn("Tipo");
        formasPago.setModel(tabla);

        String SQL = "SELECT * FROM formas_pago";

        String datos[] = new String[50];

        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                tabla.addRow(datos);
            }
            formasPago.setModel(tabla);
        } catch (SQLException ex) {
        }
        return formasPago;
    }

    /**
     * Método para filtrar formas de pago según un criterio
     *
     * @param formasPago formas de pago
     * @param filtro filtro que se utiliza para obtener formas de pago
     * @param valor valor que se busca en el filtro
     * @return filtrar en el listado de formas de pago
     */
    public JTable filtrarFormaPago(JTable formasPago, String filtro, String valor) {
        DefaultTableModel tabla = new DefaultTableModel();
        tabla.addColumn("ID");
        tabla.addColumn("Tipo");
        formasPago.setModel(tabla);

        String SQL = "";

        switch (filtro) {
            case "ID":
                SQL = "SELECT * FROM formas_pago WHERE id LIKE '%" + valor + "%'";
                break;
            case "Tipo":
                SQL = "SELECT * FROM formas_pago WHERE tipo_pago LIKE '%" + valor + "%'";
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
                tabla.addRow(datos);
            }
            formasPago.setModel(tabla);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return formasPago;
    }

    /**
     * Método para eliminar formas de pago por ID
     *
     * @param formaPagoID forma de pago ID
     * @return ID forma de pago a eliminar
     */
    public boolean eliminarFormasPagoPorId(int formaPagoID) {
        try {
            String query = "DELETE FROM formas_pago WHERE id = ?";
            PreparedStatement stmt = cn.prepareStatement(query);
            stmt.setInt(1, formaPagoID);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
