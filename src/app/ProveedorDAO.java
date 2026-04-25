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
 * Esta clase se encarga de todas las operaciones CRUD (Crear, Leer, Actualizar,
 * Borrar) sobre la tabla "proveedores" de la base de datos. También incluye
 * métodos auxiliares para obtener IDs, filtrar datos o llenar componentes GUI
 * como JTable y JTextField.
 *
 * @author Moisés López Vega
 */
public class ProveedorDAO {
    
    /**
     * Constructor de la clase ProveedorDAO. 
     * Inicializa la conexión con la base de datos.
     */
    public ProveedorDAO() {
    }

    // Conexión a la base de datos
    conexionBD con = new conexionBD();
    Connection cn = con.conectar();

    /**
     * Método para obtener todos los valores de una columna
     * @param nombreColumna columna pasada por parámetros
     * @return columna pasada por parámetros
     */
    public List<String> obtenerColumna(String nombreColumna) {
        List<String> valores = new ArrayList<>();
        String query = "SELECT " + nombreColumna + " FROM proveedores";

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
     * @param columna1 columna nº 1
     * @param columna2 columna nº 2
     * @return columnas concatenadas, proveedor
     */
    public List<List<String>> obtenerDosColumnas(String columna1, String columna2) {
        List<List<String>> valores = new ArrayList<>();
        String query = "SELECT " + columna1 + ", " + columna2 + " FROM proveedores";

        try (Statement stmt = cn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                List<String> fila = new ArrayList<>();
                fila.add(rs.getString(columna1));
                fila.add(rs.getString(columna2));
                valores.add(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return valores;
    }

    /**
     * Método para obtener tres columnas concatenadas en un solo String
     * @param nombreColumna columna nº 1
     * @param nombreColumna2 columna nº 2
     * @param nombreColumna3 columna nº 3
     * @return  columnas concatenadas, proveedor
     */
    public List<String> obtenerColumnas(String nombreColumna, String nombreColumna2, String nombreColumna3) {
        List<String> valores = new ArrayList<>();
        String query = "SELECT " + nombreColumna + ", " + nombreColumna2 + ", " + nombreColumna3 + " FROM proveedores";

        try (Statement stmt = cn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String proveedor = rs.getString(nombreColumna) + " " + rs.getString(nombreColumna2) + " " + rs.getString(nombreColumna3);
                valores.add(proveedor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return valores;
    }

    /**
     * Método para obtener el siguiente ID disponible
     * @param textField valor a pasar por parámetros
     */
    public void obtenerSiguienteID(JTextField textField) {
        try {
            String query = "SELECT MAX(id) AS max_id FROM proveedores";
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
     * @return última id del proveedor
     */
    public int obtenerUltimaID() {
        int ultimaID = 0;
        try {
            String query = "SELECT MAX(id) AS max_id FROM proveedores";
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
     * Método para mostrar todos los proveedores en un JTable
     * @param tablaProveedores proveedores en un JTable
     * @return mostrar todos las proveedores en un JTable
     */
    public JTable mostrarProveedores(JTable tablaProveedores) {
        DefaultTableModel tabla = new DefaultTableModel();
        tabla.addColumn("Nº Proveedor");
        tabla.addColumn("CIF");
        tabla.addColumn("Nombre");
        tablaProveedores.setModel(tabla);

        String SQL = "SELECT * FROM proveedores";
        String datos[] = new String[10];

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(9);
                datos[2] = rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4);
                tabla.addRow(datos);
            }
            tablaProveedores.setModel(tabla);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tablaProveedores;
    }

    /**
     * Método para filtrar proveedores según un criterio
     * @param tablaProveedores proveedores
     * @param filtro filtro que se utiliza para obtener proveedores
     * @param valor valor que se busca en el filtro
     * @return filtrar en el listado de proveedores
     */
    public JTable filtrarProveedores(JTable tablaProveedores, String filtro, String valor) {
        DefaultTableModel tabla = new DefaultTableModel();
        tabla.addColumn("Nº Proveedor");
        tabla.addColumn("CIF");
        tabla.addColumn("Nombre");
        tablaProveedores.setModel(tabla);

        String SQL = "";

        switch (filtro) {
            case "Nº Proveedor":
                SQL = "SELECT * FROM proveedores WHERE id LIKE '%" + valor + "%'";
                break;
            case "CIF":
                SQL = "SELECT * FROM proveedores WHERE nie LIKE '%" + valor + "%'";
                break;
            case "Nombre":
                SQL = "SELECT * FROM proveedores WHERE CONCAT(nombre, ' ', apellido_1, ' ', apellido_2) LIKE '%" + valor + "%'";
                break;
        }

        String datos[] = new String[3];

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(9);
                datos[2] = rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4);
                tabla.addRow(datos);
            }
            tablaProveedores.setModel(tabla);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tablaProveedores;
    }

    /**
     * Método para guardar un proveedor en la base de datos
     * @param proveedor proveedores
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public void guardarProveedor(proveedor proveedor) throws SQLException {
        String sql = "INSERT INTO proveedores (id, nombre, apellido_1, apellido_2, cp, direccion, numero, tlf_movil, nie, tipo, email, ID_formaPago, poblacion, provincia, pais, n_cuenta) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = conexionBD.conectar();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, proveedor.getId());
            stmt.setString(2, proveedor.getNombre());
            stmt.setString(3, proveedor.getApellido1());
            stmt.setString(4, proveedor.getApellido2());
            stmt.setString(5, proveedor.getCp()); 
            stmt.setString(6, proveedor.getDireccion());
            stmt.setInt(7, proveedor.getNumero());
            stmt.setString(8, proveedor.getTlfMovil());
            stmt.setString(9, proveedor.getNie());
            stmt.setBoolean(10, proveedor.isTipo());
            stmt.setString(11, proveedor.getEmail());
            stmt.setInt(12, proveedor.getFormaPago());
            stmt.setString(13, proveedor.getPoblacion());
            stmt.setString(14, proveedor.getProvincia());
            stmt.setString(15, proveedor.getPais());
            stmt.setString(16, proveedor.getnCuenta());
            stmt.executeUpdate();
        }
    }

    /**
     * Método para obtener todos los proveedores
     * @return todos los proveedores
     */
    public List<proveedor> obtenerTodosLosProveedores() {
        List<proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT * FROM proveedores";

        try (Statement statement = cn.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                proveedor proveedor = new proveedor();
                proveedor.setId(resultSet.getInt("id"));
                proveedor.setNombre(resultSet.getString("nombre"));
                proveedor.setApellido1(resultSet.getString("apellido_1"));
                proveedor.setApellido2(resultSet.getString("apellido_2"));
                proveedor.setDireccion(resultSet.getString("direccion"));
                proveedor.setNumero(resultSet.getInt("numero"));
                proveedor.setCp(resultSet.getString("cp"));
                proveedor.setPoblacion(resultSet.getString("poblacion"));
                proveedor.setProvincia(resultSet.getString("provincia"));
                proveedor.setPais(resultSet.getString("pais"));
                proveedor.setEmail(resultSet.getString("email"));
                proveedor.setTlfMovil(resultSet.getString("tlf_movil"));
                proveedor.setNie(resultSet.getString("nie"));
                proveedor.setFormaPago(resultSet.getInt("ID_formaPago"));
                proveedor.setnCuenta(resultSet.getString("n_cuenta"));
                proveedor.setTipo(resultSet.getBoolean("tipo"));
                proveedores.add(proveedor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return proveedores;
    }

    /**
     * Método para obtener un proveedor según su nombre
     * @param nombre nombre pasado por parámetro    
     * @return nombre del proveedor
     */
    public proveedor obtenerProveedorSegunNombre(String nombre) {
        proveedor proveedor = null;
        String sql = "SELECT * FROM proveedores WHERE nombre = ?";

        try (PreparedStatement statement = cn.prepareStatement(sql)) {
            statement.setString(1, nombre);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    proveedor = new proveedor();
                    proveedor.setId(resultSet.getInt("id"));
                    proveedor.setNombre(resultSet.getString("nombre"));
                    proveedor.setApellido1(resultSet.getString("apellido_1"));
                    proveedor.setApellido2(resultSet.getString("apellido_2"));
                    proveedor.setDireccion(resultSet.getString("direccion"));
                    proveedor.setNumero(resultSet.getInt("numero"));
                    proveedor.setCp(resultSet.getString("cp"));
                    proveedor.setPoblacion(resultSet.getString("poblacion"));
                    proveedor.setProvincia(resultSet.getString("provincia"));
                    proveedor.setPais(resultSet.getString("pais"));
                    proveedor.setEmail(resultSet.getString("email"));
                    proveedor.setTlfMovil(resultSet.getString("tlf_movil"));
                    proveedor.setNie(resultSet.getString("nie"));
                    proveedor.setFormaPago(resultSet.getInt("ID_formaPago"));
                    proveedor.setnCuenta(resultSet.getString("n_cuenta"));
                    proveedor.setTipo(resultSet.getBoolean("tipo"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return proveedor;
    }

    /**
     * Método para actualizar un proveedor existente
     * @param proveedor proveedor pasado por parámetro
     */
    public void actualizarProveedor(proveedor proveedor) {
        String sql = "UPDATE proveedores SET nombre=?, apellido_1=?, apellido_2=?, direccion=?, numero=?, cp=?, poblacion=?, provincia=?, pais=?, email=?, tlf_movil=?, nie=?, ID_formaPago=?, n_cuenta=?, tipo=? WHERE id=?";

        try (PreparedStatement preparedStatement = cn.prepareStatement(sql)) {
            preparedStatement.setString(1, proveedor.getNombre());
            preparedStatement.setString(2, proveedor.getApellido1());
            preparedStatement.setString(3, proveedor.getApellido2());
            preparedStatement.setString(4, proveedor.getDireccion());
            preparedStatement.setInt(5, proveedor.getNumero());
            preparedStatement.setString(6, proveedor.getCp());
            preparedStatement.setString(7, proveedor.getPoblacion());
            preparedStatement.setString(8, proveedor.getProvincia());
            preparedStatement.setString(9, proveedor.getPais());
            preparedStatement.setString(10, proveedor.getEmail());
            preparedStatement.setString(11, proveedor.getTlfMovil());
            preparedStatement.setString(12, proveedor.getNie());
            preparedStatement.setInt(13, proveedor.getFormaPago());
            preparedStatement.setString(14, proveedor.getnCuenta());
            preparedStatement.setBoolean(15, proveedor.isTipo());
            preparedStatement.setInt(16, proveedor.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
