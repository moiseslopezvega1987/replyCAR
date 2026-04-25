package app;

/**
 * Clases importadas para la utilización en la clase.
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
 *
 * Esta clase se encarga de todas las operaciones CRUD (Crear, Leer, Actualizar,
 * Borrar) sobre la tabla "Usuarios" de la base de datos. También incluye
 * métodos auxiliares para obtener IDs, filtrar datos o llenar componentes GUI
 * como JTable y JTextField.
 *
 * @author Moisés López Vega
 */
public class UsuariosDAO {

    /**
     * Constructor de la clase UsuariosDAO. Inicializa la conexión con la base
     * de datos.
     */
    public UsuariosDAO() {
    }

    // Creamos conexión
    conexionBD con = new conexionBD();
    Connection cn = con.conectar();

    /**
     * Método que obtiene el siguiente ID disponible en la tabla usuarios y lo
     * asigna al JTextField indicado
     *
     * @param textField campo de texto donde se mostrará el siguiente ID
     * disponible
     */
    public void obtenerSiguienteID(JTextField textField) {
        try {
            String query = "SELECT MAX(id) AS max_id FROM usuarios";
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
     * Método que inserta un nuevo usuario en la base de datos
     *
     * @param usuarios objeto que contiene los datos del usuario que se van a
     * guardar
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public void guardarUsuario(usuarios usuarios) throws SQLException {
        String sql = "INSERT INTO usuarios (id, nombre, apellidos, rol, user, password) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = conexionBD.conectar();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarios.getId());
            stmt.setString(2, usuarios.getNombre());
            stmt.setString(3, usuarios.getApellidos());
            stmt.setString(4, usuarios.getRol());
            stmt.setString(5, usuarios.getUser());
            stmt.setString(6, usuarios.getPassword());
            stmt.executeUpdate();
        } finally {
        }
    }

    /**
     * Método que obtiene todos los usuarios almacenados en la base de datos
     *
     * @return lista de objetos usuarios con la información de todos los
     * registros de la tabla usuarios
     */
    public List<usuarios> obtenerTodosLosUsuarios() {
        List<usuarios> usuario = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Statement statement = cn.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                usuarios usuarios = new usuarios();
                usuarios.setId(resultSet.getInt("id"));
                usuarios.setNombre(resultSet.getString("nombre"));
                usuarios.setApellidos(resultSet.getString("apellidos"));
                usuarios.setRol(resultSet.getString("rol"));
                usuarios.setUser(resultSet.getString("user"));
                usuarios.setPassword(resultSet.getString("password"));
                usuario.add(usuarios);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    /**
     * Método que obtiene el siguiente ID disponible en la tabla usuarios a
     * partir del último ID registrado
     *
     * @return siguiente ID disponible para un nuevo usuario
     */
    public int obtenerUltimaID() {
        int ultimaID = 0;
        try {
            String query = "SELECT MAX(id) AS max_id FROM usuarios";
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
     * Método que actualiza los datos de un usuario existente en la base de
     * datos
     *
     * @param usuarios objeto que contiene los nuevos datos del usuario que se
     * actualizarán en la tabla usuarios
     */
    public void actualizarUsuario(usuarios usuarios) {
        String sql = "UPDATE usuarios SET nombre=?, apellidos=?, rol=?, user=?, password=? WHERE id=?";

        try (PreparedStatement preparedStatement = cn.prepareStatement(sql)) {
            preparedStatement.setString(1, usuarios.getNombre());
            preparedStatement.setString(2, usuarios.getApellidos());
            preparedStatement.setString(3, usuarios.getRol());
            preparedStatement.setString(4, usuarios.getUser());
            preparedStatement.setString(5, usuarios.getPassword());
            preparedStatement.setInt(6, usuarios.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que obtiene todos los usuarios de la base de datos y los muestra
     * en el JTable proporcionado
     *
     * @param tablaUsuarios tabla donde se cargarán los datos de los usuarios
     * @return JTable con la información de los usuarios obtenida de la base de
     * datos
     */
    public JTable mostrarUsuarios(JTable tablaUsuarios) {
        DefaultTableModel tabla = new DefaultTableModel();
        tabla.addColumn("ID");
        tabla.addColumn("Nombre");
        tabla.addColumn("Apellidos");
        tabla.addColumn("Rol");
        tabla.addColumn("Usuario");
        tabla.addColumn("Clave");
        tablaUsuarios.setModel(tabla);

        String SQL = "SELECT * FROM usuarios";
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
                datos[4] = rs.getString(5);
                datos[5] = rs.getString(6);
                tabla.addRow(datos);
            }
            tablaUsuarios.setModel(tabla);
        } catch (SQLException ex) {
        }
        return tablaUsuarios;
    }

    /**
     * Método que filtra los usuarios de la base de datos según un criterio y
     * muestra los resultados en el JTable indicado
     *
     * @param tablaUsuarios tabla donde se mostrarán los usuarios filtrados
     * @param filtro campo por el que se realizará la búsqueda (ID, Nombre o
     * Rol)
     * @param valor valor que se utilizará para realizar el filtro
     * @return JTable con los resultados del filtro aplicado
     */
    public JTable filtrarUsuarios(JTable tablaUsuarios, String filtro, String valor) {
        DefaultTableModel tabla = new DefaultTableModel();
        tabla.addColumn("ID");
        tabla.addColumn("Nombre");
        tabla.addColumn("Apellidos");
        tabla.addColumn("Rol");
        tabla.addColumn("Usuario");
        tabla.addColumn("Clave");
        tablaUsuarios.setModel(tabla);

        String SQL = "";

        switch (filtro) {
            case "ID":
                SQL = "SELECT * FROM usuarios WHERE id LIKE '%" + valor + "%'";
                break;
            case "Nombre":
                SQL = "SELECT * FROM usuarios WHERE nombre LIKE '%" + valor + "%'";
                break;
            case "Rol":
                SQL = "SELECT * FROM usuarios WHERE rol LIKE '%" + valor + "%'";
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
                datos[4] = rs.getString(5);
                datos[5] = rs.getString(6);
                tabla.addRow(datos);
            }
            tablaUsuarios.setModel(tabla);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tablaUsuarios;
    }

    /**
     * Método que elimina un usuario de la base de datos según su identificador
     *
     * @param usuarioId identificador del usuario que se desea eliminar
     * @return true si el usuario fue eliminado correctamente, false si no se
     * eliminó ningún registro
     */
    public boolean eliminarUsuarioPorId(int usuarioId) {
        try {
            String query = "DELETE FROM usuarios WHERE id = ?";
            PreparedStatement stmt = cn.prepareStatement(query);
            stmt.setInt(1, usuarioId);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
