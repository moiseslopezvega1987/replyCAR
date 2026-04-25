package app;

/*Clases importadas para la utilización en la clase.
 */
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

/**
 * Esta clase se encarga de todas las operaciones CRUD (Crear, Leer, Actualizar,
 * Borrar) sobre la tabla "artículos" de la base de datos. También incluye
 * métodos auxiliares para obtener IDs, filtrar datos o llenar componentes GUI
 * como JTable y JTextField.
 *
 * @author Moisés López Vega
 */
public class ArticuloDAO {

    /**
     * Constructor de la clase ArticuloDAO. Inicializa la conexión con la base
     * de datos.
     */
    public ArticuloDAO() {
    }

    // Creamos conexión
    conexionBD con = new conexionBD();
    Connection cn = con.conectar();

    /**
     * Método para guardar un artículo en la base de datos
     *
     * @param articulo objeto que contiene los datos del artículo que se van a
     * insertar
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public void guardarArticulo(articulo articulo) throws SQLException {
        String sql = "INSERT INTO movimientos (fecha_mov, ID_Tarifa, descripcion, id_factura_almacen, proveedor, unidades, precio_costo, precio_pvp, descuento, tipo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(articulo.getFecha_mov()));
            stmt.setString(2, articulo.getReferencia());
            stmt.setString(3, articulo.getDescripcion());
            stmt.setString(4, articulo.getId_factura_almacen());
            stmt.setString(5, articulo.getProveedor());
            stmt.setInt(6, articulo.getUnidades());
            stmt.setDouble(7, articulo.getpCoste());
            stmt.setDouble(8, articulo.getpPVP());
            stmt.setInt(9, articulo.getDescuento());
            stmt.setString(10, articulo.getTipo());
            stmt.executeUpdate();
        } finally {
        }
    }

    /**
     * Método que obtiene todas las referencias de la tabla tarifas
     *
     * @return lista de referencias almacenadas en la base de datos
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public List<String> obtenerReferencias() throws SQLException {
        List<String> referencias = new ArrayList<>();
        String sql = "SELECT id FROM tarifas";
        try (PreparedStatement stmt = cn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                referencias.add(rs.getString("id"));
            }
        }
        return referencias;
    }

    /**
     * Método que obtiene un artículo de la base de datos a partir de su
     * referencia
     *
     * @param referencia identificador del artículo en la tabla tarifas
     * @return objeto articulo con los datos obtenidos de la base de datos, o
     * null si no existe un artículo con esa referencia
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public articulo obtenerArticuloPorReferencia(String referencia) throws SQLException {
        articulo art = null;
        String sql = "SELECT macrosector, ID_Familia, descrip_familia, id, descripcion, importe_pvp, importe_coste FROM tarifas WHERE id = ?";
        try (PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setString(1, referencia);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    art = new articulo();
                    art.setReferencia(referencia);
                    art.setMacrosector(rs.getString("macrosector"));
                    art.setID_Familia(rs.getInt("ID_Familia"));
                    art.setDescrip_familia(rs.getString("descrip_familia"));
                    art.setReferencia(rs.getString("id"));
                    art.setDescripcion(rs.getString("descripcion"));
                    art.setpPVP(rs.getDouble("importe_pvp"));
                    art.setpCoste(rs.getDouble("importe_coste"));
                }
            }
        }
        return art;
    }

    /**
     * Método que obtiene la cantidad total de piezas disponibles para una
     * referencia El cálculo se realiza restando las unidades vendidas a las
     * unidades compradas registradas en la tabla movimientos
     *
     * @param referencia identificador del artículo (ID_Tarifa) del que se desea
     * calcular el total de piezas
     * @return objeto articulo que contiene la referencia y el total de piezas
     * disponibles, o null si no existen movimientos para esa referencia
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public articulo obtenerCantidadTotalPiezasPorReferencia(String referencia) throws SQLException {
        articulo art = null;
        String sql = "SELECT "
                + "    ID_Tarifa, "
                + "    SUM(CASE WHEN tipo = 'Compra' THEN unidades ELSE 0 END) - "
                + "    SUM(CASE WHEN tipo = 'Venta' THEN unidades ELSE 0 END) AS total_piezas "
                + "FROM "
                + "    movimientos "
                + "WHERE "
                + "    ID_Tarifa = ? "
                + "GROUP BY "
                + "    ID_Tarifa";
        try (PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setString(1, referencia);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    art = new articulo();
                    art.setReferencia(rs.getString("ID_Tarifa"));
                    art.setTotalPiezas(rs.getInt("total_piezas"));
                }
            }
        }
        return art;
    }

    /**
     * Método que elimina un registro de la tabla movimientos según su
     * identificador
     *
     * @param idMov identificador del movimiento que se desea eliminar
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public void eliminarArticuloPorId(int idMov) throws SQLException {
        String sql = "DELETE FROM movimientos WHERE id = ?";
        try (PreparedStatement pstmt = cn.prepareStatement(sql)) {
            pstmt.setInt(1, idMov);
            pstmt.executeUpdate();
        }
    }

    /**
     * Método que inserta un artículo en la tabla movimientos y devuelve el
     * identificador generado automáticamente para ese registro
     *
     * @param articulo articulo objeto que contiene los datos del movimiento que
     * se va a insertar (referencia, descripción, proveedor, unidades, precios,
     * etc.)
     * @return identificador del registro insertado en la tabla movimientos.
     * Devuelve -1 si no se pudo obtener el id generado
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public int guardarArticuloRetorna(articulo articulo) throws SQLException {
        String sql = "INSERT INTO movimientos (fecha_mov, ID_Tarifa, descripcion, id_factura_cliente, proveedor, unidades, precio_costo, precio_pvp, descuento, tipo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int generatedId = -1;

        try (PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(articulo.getFecha_mov()));
            stmt.setString(2, articulo.getReferencia());
            stmt.setString(3, articulo.getDescripcion());
            stmt.setString(4, articulo.getId_factura_cliente());
            stmt.setString(5, articulo.getProveedor());
            stmt.setInt(6, articulo.getUnidades());
            stmt.setDouble(7, articulo.getpCoste());
            stmt.setDouble(8, articulo.getpPVP());
            stmt.setInt(9, articulo.getDescuento());
            stmt.setString(10, articulo.getTipo());
            stmt.executeUpdate();
        }
        String sqlLastId = "SELECT LAST_INSERT_ID()";
        try (java.sql.Statement stmtLastId = cn.createStatement(); ResultSet rs = stmtLastId.executeQuery(sqlLastId)) {
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }
        }
        return generatedId;
    }

    /**
     * Método que actualiza el número de factura de cliente asociado a un
     * movimiento
     *
     * @param idMov identificador del movimiento que se desea actualizar
     * @param numeroFactura número de factura de cliente que se asignará al
     * movimiento
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public void actualizarDocumentoMovimiento(int idMov, String numeroFactura) throws SQLException {
        String sql = "UPDATE movimientos SET id_factura_cliente = ? WHERE id = ?";

        try (PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setString(1, numeroFactura);
            stmt.setInt(2, idMov);
            stmt.executeUpdate();
        }
    }
}
