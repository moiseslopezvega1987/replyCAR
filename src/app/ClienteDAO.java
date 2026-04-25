package app;

/*Clases importadas para la utilización en la clase.
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextField;

/**
 *
 * Esta clase se encarga de todas las operaciones CRUD (Crear, Leer, Actualizar,
 * Borrar) sobre la tabla "clientes" de la base de datos. También incluye
 * métodos auxiliares para obtener IDs, filtrar datos o llenar componentes GUI
 * como JTable y JTextField.
 *
 * @author Moisés López Vega
 */
public class ClienteDAO {

    /**
     * Constructor de la clase ClienteDAO. Inicializa la conexión con la base de
     * datos.
     */
    public ClienteDAO() {
    }

    // Creamos conexión
    conexionBD con = new conexionBD();
    Connection cn = con.conectar();

    /**
     * Método para obtener todos los valores de una columna, devuelve una lista
     * de Strings con los valores
     *
     * @param nombreColumna cliente
     * @return un cliente
     */
    public List<String> obtenerColumna(String nombreColumna) {
        List<String> valores = new ArrayList<>();
        String query = "SELECT " + nombreColumna + " FROM clientes";
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
     * Método para obtener la lista de clientes
     *
     * @return un cliente
     */
    public List<cliente> obtenerClientes() {
        List<cliente> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, apellido_1, apellido_2 FROM clientes";
        try {
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cliente c = new cliente();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setApellido1(rs.getString("apellido_1"));
                c.setApellido2(rs.getString("apellido_2"));
                lista.add(c);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener clientes: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Método para obtener dos columnas de la tabla
     *
     * @param nombreColumna nombre
     * @param nombreColumna2 apellido1
     * @param nombreColumna3 apellido2
     * @return nombre completo del cliente
     */
    public List<String> obtenerColumnas(String nombreColumna, String nombreColumna2, String nombreColumna3) {
        List<String> valores = new ArrayList<>();
        String query = "SELECT " + nombreColumna + ", " + nombreColumna2 + ", " + nombreColumna3 + " FROM clientes";

        try (Statement stmt = cn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String cliente = rs.getString(nombreColumna) + " " + rs.getString(nombreColumna2) + " " + rs.getString(nombreColumna3);
                valores.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return valores;
    }

    /**
     * Método para obtener el siguiente ID disponible
     *
     * @param textField ingresa el nombre del cliente
     */
    public void obtenerSiguienteID(JTextField textField) {
        try {
            String query = "SELECT MAX(id) AS max_id FROM clientes";
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
     * Método para obtener el último ID registrado
     *
     * @return la última ID registrada
     */
    public int obtenerUltimaID() {
        int ultimaID = 0;
        try {
            String query = "SELECT MAX(id) AS max_id FROM clientes";
            Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                ultimaID = rs.getInt("max_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ultimaID;
    }

    /**
     * Método para mostrar todos los clientes en un JTable
     *
     * @param tablaClientes tabla de los clientes
     * @return lista de clientes
     */
    public JTable mostrarClientes(JTable tablaClientes) {
        DefaultTableModel tabla = new DefaultTableModel();
        tabla.addColumn("Nº Cliente");
        tabla.addColumn("CIF");
        tabla.addColumn("Nombre");
        tablaClientes.setModel(tabla);

        String SQL = "SELECT * FROM clientes";
        String datos[] = new String[10];

        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(9);
                datos[2] = rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4);
                tabla.addRow(datos);
            }
            tablaClientes.setModel(tabla);
        } catch (SQLException ex) {
        }
        return tablaClientes;
    }

    /**
     * Método para filtrar clientes según un criterio
     *
     * @param tablaClientes tabla de los clientes
     * @param filtro filtro utilizado
     * @param valor valor a buscar
     * @return clientes filtrados, datos...
     */
    public JTable filtrarClientes(JTable tablaClientes, String filtro, String valor) {
        DefaultTableModel tabla = new DefaultTableModel();
        tabla.addColumn("Nº Cliente");
        tabla.addColumn("CIF");
        tabla.addColumn("Nombre");
        tablaClientes.setModel(tabla);

        String SQL = "";

        switch (filtro) {
            case "Nº Cliente":
                SQL = "SELECT * FROM clientes WHERE id LIKE '%" + valor + "%'";
                break;
            case "CIF":
                SQL = "SELECT * FROM clientes WHERE nie LIKE '%" + valor + "%'";
                break;
            case "Nombre":
                SQL = "SELECT * FROM clientes WHERE CONCAT(nombre, ' ', apellido_1, ' ', apellido_2) LIKE '%" + valor + "%'";
                break;
        }

        String datos[] = new String[3];

        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(9);
                datos[2] = rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4);
                tabla.addRow(datos);
            }
            tablaClientes.setModel(tabla);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tablaClientes;
    }

    // Método para guardar un cliente en la base de datos
    /**
     * Método para guardar un cliente en la base de datos
     *
     * @param cliente cliente a guardar
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public void guardarCliente(cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (id, nombre, apellido_1, apellido_2, cp, direccion, numero, tlf_movil, nie, tipo, email, ID_formaPago, poblacion, provincia, pais, n_cuenta) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = conexionBD.conectar();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cliente.getId());
            stmt.setString(2, cliente.getNombre());
            stmt.setString(3, cliente.getApellido1());
            stmt.setString(4, cliente.getApellido2());
            stmt.setString(5, cliente.getCp());
            stmt.setString(6, cliente.getDireccion());
            stmt.setInt(7, cliente.getNumero());
            stmt.setString(8, cliente.getTlfMovil());
            stmt.setString(9, cliente.getNie());
            stmt.setBoolean(10, cliente.isTipo());
            stmt.setString(11, cliente.getEmail());
            stmt.setInt(12, cliente.getFormaPago());
            stmt.setString(13, cliente.getPoblacion());
            stmt.setString(14, cliente.getProvincia());
            stmt.setString(15, cliente.getPais());
            stmt.setString(16, cliente.getnCuenta());
            stmt.executeUpdate();
        } finally {
        }
    }

    /**
     * Método para obtener todos los clientes, devuelve una lista de objetos
     * Cliente
     *
     * @return todos los clientes
     */
    public List<cliente> obtenerTodosLosClientes() {
        List<cliente> clientes = new ArrayList<>();

        String sql = "SELECT * FROM clientes";

        try (Statement statement = cn.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                cliente cliente = new cliente();
                cliente.setId(resultSet.getInt("id"));
                cliente.setNombre(resultSet.getString("nombre"));
                cliente.setApellido1(resultSet.getString("apellido_1"));
                cliente.setApellido2(resultSet.getString("apellido_2"));
                cliente.setDireccion(resultSet.getString("direccion"));
                cliente.setNumero(resultSet.getInt("numero"));
                cliente.setCp(resultSet.getString("cp"));
                cliente.setPoblacion(resultSet.getString("poblacion"));
                cliente.setProvincia(resultSet.getString("provincia"));
                cliente.setPais(resultSet.getString("pais"));
                cliente.setEmail(resultSet.getString("email"));
                cliente.setTlfMovil(resultSet.getString("tlf_movil"));
                cliente.setNie(resultSet.getString("nie"));
                cliente.setFormaPago(resultSet.getInt("ID_formaPago"));
                cliente.setnCuenta(resultSet.getString("n_cuenta"));
                cliente.setTipo(resultSet.getBoolean("tipo"));
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    /**
     * Método para actualizar un cliente existente
     *
     * @param cliente cliente a actualizar
     */
    public void actualizarCliente(cliente cliente) {
        String sql = "UPDATE clientes SET nombre=?, apellido_1=?, apellido_2=?, direccion=?, numero=?, cp=?, poblacion=?, provincia=?, pais=?, email=?, tlf_movil=?, nie=?, ID_formaPago=?, n_cuenta=?, tipo=? WHERE id=?";

        try (PreparedStatement preparedStatement = cn.prepareStatement(sql)) {
            preparedStatement.setString(1, cliente.getNombre());
            preparedStatement.setString(2, cliente.getApellido1());
            preparedStatement.setString(3, cliente.getApellido2());
            preparedStatement.setString(4, cliente.getDireccion());
            preparedStatement.setInt(5, cliente.getNumero());
            preparedStatement.setString(6, cliente.getCp());
            preparedStatement.setString(7, cliente.getPoblacion());
            preparedStatement.setString(8, cliente.getProvincia());
            preparedStatement.setString(9, cliente.getPais());
            preparedStatement.setString(10, cliente.getEmail());
            preparedStatement.setString(11, cliente.getTlfMovil());
            preparedStatement.setString(12, cliente.getNie());
            preparedStatement.setInt(13, cliente.getFormaPago());
            preparedStatement.setString(14, cliente.getnCuenta());
            preparedStatement.setBoolean(15, cliente.isTipo());
            preparedStatement.setInt(16, cliente.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para obtener un cliente según su nombre
     *
     * @param nombre nombre del cliente a buscar
     * @return un cliente
     */
    public cliente obtenerClienteSegunNombre(String nombre) {
        cliente cliente = null;
        String sql = "SELECT * FROM clientes WHERE CONCAT(nombre, ' ', apellido_1, ' ', apellido_2) = ?";

        try (PreparedStatement statement = cn.prepareStatement(sql)) {
            statement.setString(1, nombre);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    cliente = new cliente();
                    cliente.setId(resultSet.getInt("id"));
                    cliente.setNombre(resultSet.getString("nombre"));
                    cliente.setApellido1(resultSet.getString("apellido_1"));
                    cliente.setApellido2(resultSet.getString("apellido_2"));
                    cliente.setDireccion(resultSet.getString("direccion"));
                    cliente.setNumero(resultSet.getInt("numero"));
                    cliente.setCp(resultSet.getString("cp"));
                    cliente.setPoblacion(resultSet.getString("poblacion"));
                    cliente.setProvincia(resultSet.getString("provincia"));
                    cliente.setPais(resultSet.getString("pais"));
                    cliente.setEmail(resultSet.getString("email"));
                    cliente.setTlfMovil(resultSet.getString("tlf_movil"));
                    cliente.setNie(resultSet.getString("nie"));
                    cliente.setFormaPago(resultSet.getInt("ID_formaPago"));
                    cliente.setnCuenta(resultSet.getString("n_cuenta"));
                    cliente.setTipo(resultSet.getBoolean("tipo"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }
}
