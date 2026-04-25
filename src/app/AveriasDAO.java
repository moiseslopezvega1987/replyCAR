package app;

/*Clases importadas para la utilización en la clase.
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;

/**
 * Esta clase se encarga de todas las operaciones CRUD (Crear, Leer, Actualizar,
 * Borrar) sobre la tabla "averías" de la base de datos. También incluye métodos
 * auxiliares para obtener IDs, filtrar datos o llenar componentes GUI como
 * JTable y JTextField.
 *
 * @author Moisés López Vega
 */
public class AveriasDAO {

    /**
     * Constructor de la clase AveriasDAO. 
     * Inicializa la conexión con la base de datos.
     */
    public AveriasDAO() {
    }

    // Creamos conexión
    conexionBD con = new conexionBD();
    Connection cn = con.conectar();

    /**
     * Método que obtiene los datos de una tarifa de taller según el tipo de
     * avería
     *
     * @param averia el tipo de avería que se desea buscar
     * @return un objeto averias con los datos de la tarifa, o null si no existe
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public averias obtenerTarifaTallerPorTipoAveria(String averia) throws SQLException {
        averias ave = null;
        String sql = "SELECT id, tipo_averia, precio_averia, texto FROM tarifa_taller WHERE tipo_averia = ?";

        try (PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setString(1, averia);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ave = new averias();
                    ave.setTipo(rs.getString("tipo_averia"));
                    ave.setPrecio(rs.getDouble("precio_averia"));
                    ave.setId(rs.getInt("id"));
                }
            }
        }
        return ave;
    }

    /**
     * Método que Obtiene todos los valores de una columna específica de la
     * tabla tarifa_taller
     *
     * Este método devuelve una lista de strings con los valores de la columna
     * indicada Si ocurre un error al ejecutar la consulta, se imprime la traza
     * de la excepción y se devuelve una lista vacía.
     *
     * @param nombreColumna nombre de la columna que se desea obtener
     * @return lista de valores de la columna; puede estar vacía si no hay
     * registros o si ocurre un error
     */
    public List<String> obtenerColumna(String nombreColumna) {
        List<String> valores = new ArrayList<>();
        String query = "SELECT " + nombreColumna + " FROM tarifa_taller";

        try (Statement stmt = cn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                valores.add(rs.getString(nombreColumna));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return valores;
    }
}
