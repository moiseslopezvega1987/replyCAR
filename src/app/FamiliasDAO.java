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
 * Borrar) sobre la tabla "Familias" de la base de datos. También incluye
 * métodos auxiliares para obtener IDs, filtrar datos o llenar componentes GUI
 * como JTable y JTextField.
 *
 * @author Moisés López Vega
 */
public class FamiliasDAO {

    /**
     * Constructor de la clase FamiliasDAO. Inicializa la conexión con la base
     * de datos.
     */
    public FamiliasDAO() {
    }

    // Creamos conexión
    conexionBD con = new conexionBD();
    Connection cn = con.conectar();

    /**
     * Método para obtener el siguiente ID
     *
     * @param textField valor a pasar por parámetros
     */
    public void obtenerSiguienteID(JTextField textField) {
        try {
            String query = "SELECT MAX(id) AS max_id FROM familias";
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
     * Método para guardar una familia en la base de datos
     *
     * @param familias familias
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public void guardarFamilia(familias familias) throws SQLException {

        String sql = "INSERT INTO familias (id, nombre, sector) "
                + "VALUES (?, ?, ?)";
        Connection conn = conexionBD.conectar();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, familias.getId());
            stmt.setString(2, familias.getNombre());
            stmt.setString(3, familias.getSector());
            stmt.executeUpdate();
        } finally {
        }
    }

    /**
     * Método para obtener una columna
     *
     * @param nombreColumna columna pasada por parámetros
     * @return columna pasada por parámetros
     */
    public List<String> obtenerColumna(String nombreColumna) {
        List<String> valores = new ArrayList<>();
        String query = "SELECT " + nombreColumna + " FROM familias";

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
     * Método para obtener todas las familias de la base de datos
     *
     * @return todas las familias de la base de datos
     */
    public List<familias> obtenerTodasLasFamilias() {
        List<familias> familia = new ArrayList<>();
        String sql = "SELECT * FROM familias";

        try (Statement statement = cn.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                familias familias = new familias();
                familias.setId(resultSet.getInt("id"));
                familias.setNombre(resultSet.getString("nombre"));
                familias.setSector(resultSet.getString("sector"));
                familia.add(familias);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return familia;
    }

    /**
     * Método para obtener el último ID registrado
     *
     * @return último ID registrado
     */
    public int obtenerUltimaID() {
        int ultimaID = 0;
        try {
            String query = "SELECT MAX(id) AS max_id FROM familias";
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
     * Método para actualizar una familia existente en la base de datos
     *
     * @param familias familia existente en la base de datos
     */
    public void actualizarFamilia(familias familias) {
        String sql = "UPDATE familias SET nombre=?, sector=? WHERE id=?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, familias.getNombre());
            ps.setString(2, familias.getSector());
            ps.setInt(3, familias.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para mostrar todos las familias en un JTable
     *
     * @param familias familias en un JTable
     * @return mostrar todos las familias en un JTable
     */
    public JTable mostrarFamilias(JTable familias) {
        DefaultTableModel tabla = new DefaultTableModel();
        tabla.addColumn("ID");
        tabla.addColumn("Nombre");
        tabla.addColumn("Sector");
        familias.setModel(tabla);

        String SQL = "SELECT * FROM familias";
        String datos[] = new String[50];

        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                tabla.addRow(datos);
            }
            familias.setModel(tabla);
        } catch (SQLException ex) {
        }
        return familias;
    }

    /**
     * Método para filtrar familias según un criterio
     *
     * @param familias familias
     * @param filtro filtro que se utiliza para obtener familia
     * @param valor valor que se busca en el filtro
     * @return filtrar en el listado de familias
     */
    public JTable filtrarFamilia(JTable familias, String filtro, String valor) {
        DefaultTableModel tabla = new DefaultTableModel();
        tabla.addColumn("ID");
        tabla.addColumn("Nombre");
        tabla.addColumn("Sector");
        familias.setModel(tabla);

        String SQL = "";

        switch (filtro) {
            case "ID":
                SQL = "SELECT * FROM familias WHERE id LIKE '%" + valor + "%'";
                break;
            case "Nombre":
                SQL = "SELECT * FROM familias WHERE nombre LIKE '%" + valor + "%'";
                break;
            case "Sector":
                SQL = "SELECT * FROM familias WHERE sector LIKE '%" + valor + "%'";
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
                tabla.addRow(datos);
            }
            familias.setModel(tabla);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return familias;
    }

    /**
     * Eliminar familias por ID de la base de datos
     *
     * @param familiaID familia ID
     * @return ID de familia a eliminar
     */
    public boolean eliminarFamiliaPorId(int familiaID) {
        try {
            String query = "DELETE FROM familias WHERE id = ?";
            PreparedStatement stmt = cn.prepareStatement(query);
            stmt.setInt(1, familiaID);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
