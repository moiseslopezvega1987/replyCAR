package app;

/**
 * Clases importadas para la utilización en la clase.
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * Esta clase se encarga de todas las operaciones CRUD (Crear, Leer, Actualizar,
 * Borrar) sobre la tabla "listado_vehículos" de la base de datos. También
 * incluye métodos auxiliares para obtener IDs, filtrar datos o llenar
 * componentes GUI como JTable y JTextField.
 *
 * @author Moisés López Vega
 */
public class VehiculoDAO {

    /**
     * Constructor de la clase VehiculoDAO. Inicializa la conexión con la base
     * de datos.
     */
    public VehiculoDAO() {
    }

    // Conexión a la base de datos
    conexionBD con = new conexionBD();
    Connection cn = con.conectar();

    /**
     * Método que inserta un nuevo vehículo en la base de datos dentro de la
     * tabla listado_vehiculos
     *
     * @param vehiculo objeto que contiene los datos del vehículo que se desean
     * guardar en la base de datos
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public void guardarVehiculo(vehiculo vehiculo) throws SQLException {
        String sql = "INSERT INTO listado_vehiculos (id_proveedor, fecha_mov, fecha_compra, nfra_proveedor, modelo, bastidor, foto, color, extras, precio_compra, iva_compra, total_compra, ID_TarifaVehiculo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setInt(1, vehiculo.getId_proveedor());
            stmt.setDate(2, Date.valueOf(vehiculo.getFecha_mov()));
            stmt.setDate(3, Date.valueOf(vehiculo.getFecha_compra()));
            stmt.setString(4, vehiculo.getNfra_proveedor());
            stmt.setString(5, vehiculo.getModelo());
            stmt.setString(6, vehiculo.getBastidor());
            stmt.setString(7, vehiculo.getFoto());
            stmt.setString(8, vehiculo.getColor());
            stmt.setString(9, vehiculo.getExtras());
            stmt.setDouble(10, vehiculo.getPrecio_compra());
            stmt.setDouble(11, vehiculo.getIva_compra());
            stmt.setDouble(12, vehiculo.getTotal_compra());
            stmt.setInt(13, vehiculo.getId_TarifaVehiculo());
            stmt.executeUpdate();
        } finally {
        }
    }

    /**
     * Método que consulta todos los valores de una columna determinada de la
     * tabla tarifa_vehiculos
     *
     * @param nombreColumna nombre de la columna que se desea consultar
     * @return lista con los valores obtenidos de la columna especificada
     */
    public List<String> obtenerColumna(String nombreColumna) {
        List<String> valores = new ArrayList<>();
        String query = "SELECT " + nombreColumna + " FROM tarifa_vehiculos";

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
     * Método que obtiene un vehículo de la tabla `tarifa_vehiculos` según su
     * modelo
     *
     * @param modelo el nombre del modelo del vehículo que se desea buscar
     * @return un objeto `vehiculo` con los datos del modelo indicado, o `null`
     * si no se encuentra ningún vehículo con ese modelo
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public vehiculo obtenerVehiculoPorModelo(String modelo) throws SQLException {
        vehiculo veh = null;
        String sql = "SELECT id, modelo, precio, color, extras, foto FROM tarifa_vehiculos WHERE modelo = ?";

        try (PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setString(1, modelo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    veh = new vehiculo();
                    veh.setModelo(modelo);
                    veh.setPrecio_compra(rs.getDouble("precio"));
                    veh.setColor(rs.getString("color"));
                    veh.setExtras(rs.getString("extras"));
                    veh.setFoto(rs.getString("foto"));
                    veh.setId_TarifaVehiculo(rs.getInt("id"));
                }
            }
        }
        return veh;
    }

    /**
     * Método que obtiene un vehículo de la tabla `listado_vehiculos` según su
     * número de bastidor
     *
     * @param bastidor el número de bastidor del vehículo que se desea buscar
     * @return un objeto `vehiculo` con los datos del vehículo correspondiente
     * al bastidor, o `null` si no se encuentra ningún vehículo con ese bastidor
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public vehiculo obtenerVehiculoPorBastidor(String bastidor) throws SQLException {
        vehiculo veh = null;
        String sql = "SELECT bastidor, modelo, precio_compra, color, extras, foto FROM listado_vehiculos WHERE bastidor = ?";

        try (PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setString(1, bastidor);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    veh = new vehiculo();
                    veh.setBastidor(bastidor);
                    veh.setModelo(rs.getString("modelo"));
                    veh.setPrecio_compra(rs.getDouble("precio_compra"));
                    veh.setColor(rs.getString("color"));
                    veh.setExtras(rs.getString("extras"));
                    veh.setFoto(rs.getString("foto"));
                }
            }
        }
        return veh;
    }

    /**
     * Método que obtiene un vehículo de la tabla `listado_vehiculos` según su
     * matrícula
     *
     * @param matricula la matrícula del vehículo que se desea buscar
     * @return un objeto `vehiculo` con los datos correspondientes a la
     * matrícula, o `null` si no se encuentra ningún vehículo con esa matrícula
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public vehiculo obtenerVehiculoPorMatricula(String matricula) throws SQLException {
        vehiculo veh = null;
        String sql = "SELECT bastidor, modelo, precio_compra, color, extras, foto FROM listado_vehiculos WHERE matricula = ?";

        try (PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setString(1, matricula);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    veh = new vehiculo();
                    veh.setMatricula(matricula);
                    veh.setModelo(rs.getString("modelo"));
                    veh.setBastidor(rs.getString("bastidor"));
                    veh.setPrecio_compra(rs.getDouble("precio_compra"));
                    veh.setColor(rs.getString("color"));
                    veh.setExtras(rs.getString("extras"));
                    veh.setFoto(rs.getString("foto"));
                }
            }
        }
        return veh;
    }

    /**
     * Método que comprueba si un vehículo con la matrícula indicada existe en
     * la base de datos
     *
     * @param matricula la matrícula que se desea verificar
     * @return `true` si existe al menos un vehículo con esa matrícula, `false`
     * en caso contrario
     * @throws SQLException si ocurre un error al ejecutar la consulta SQL
     */
    public boolean existeMatricula(String matricula) throws SQLException {
        String query = "SELECT COUNT(*) FROM listado_vehiculos WHERE matricula = ?";
        try (PreparedStatement stmt = cn.prepareStatement(query)) {
            stmt.setString(1, matricula);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Métodoque obtiene la lista de todos los modelos de vehículos registrados
     * en la tabla `tarifa_vehiculos`
     *
     * @return una lista de cadenas (`List`) con los nombres de todos los
     * modelos
     * @throws SQLException si ocurre un error al ejecutar la consulta SQL
     */
    public List<String> obtenerModelos() throws SQLException {
        List<String> referencias = new ArrayList<>();
        String sql = "SELECT modelo FROM tarifa_vehiculos";

        try (PreparedStatement stmt = cn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                referencias.add(rs.getString("modelo"));
            }
        }
        return referencias;
    }

    /**
     * Método que obtiene la lista de todos los bastidores de vehículos que
     * actualmente no están asignados a ningún cliente (es decir, vehículos en
     * stock)
     *
     * @return una lista de cadenas (`List`) con los bastidores disponibles
     * @throws SQLException si ocurre un error al ejecutar la consulta SQL
     */
    public List<String> obtenerBastidoresSinCliente() throws SQLException {
        List<String> bastidores = new ArrayList<>();
        String sql = "SELECT bastidor FROM listado_vehiculos WHERE id_cliente IS NULL";

        try (PreparedStatement stmt = cn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                bastidores.add(rs.getString("bastidor"));
            }
        }
        return bastidores;
    }

    /**
     * Método que obtiene la cantidad total de vehículos en stock de un modelo
     * específico solo cuenta los vehículos que no están asignados a ningún
     * cliente
     *
     *
     * @param modelo el modelo del vehículo cuyo stock se desea consultar
     * @return un objeto `vehiculo` con el modelo especificado y el total de
     * unidades en stock; si no hay registros, retorna null
     * @throws SQLException si ocurre un error al ejecutar la consulta SQL
     */
    public vehiculo obtenerCantidadTotalVehiculosPorModeloStock(String modelo) throws SQLException {
        vehiculo veh = null;
        String sql = "SELECT "
                + "    modelo, "
                + "    COUNT(*) AS total_vehiculos "
                + "FROM "
                + "    listado_vehiculos "
                + "WHERE "
                + "    modelo = ? "
                + "    AND id_cliente IS NULL "
                + "GROUP BY "
                + "    modelo";

        try (PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setString(1, modelo);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    veh = new vehiculo();
                    veh.setModelo(rs.getString("modelo"));
                    veh.setTotalVehiculos(rs.getInt("total_vehiculos"));
                }
            }
        }
        return veh;
    }

    /**
     * Método que actualiza la información de un vehículo existente en la base
     * de datos utilizando su bastidor como identificador único.
     *
     * Se actualizan los campos de matrícula, cliente, precios de venta, IVA,
     * total de venta, fecha de venta, número de factura de venta y comentarios.
     *
     * @param vehiculo objeto `vehiculo` que contiene los datos actualizados; su
     * propiedad `bastidor` se usa para identificar el registro a modificar
     */
    public void actualizarVehiculoSegunBastidor(vehiculo vehiculo) {
        String sql = "UPDATE listado_vehiculos SET matricula=?, id_cliente=?, precio_venta=?, iva_venta=?, total_venta=?, fecha_venta=?, nfra_venta=?, comentarios=? WHERE bastidor=?";

        try (PreparedStatement preparedStatement = cn.prepareStatement(sql)) {
            preparedStatement.setString(1, vehiculo.getMatricula());
            preparedStatement.setInt(2, vehiculo.getId_cliente());
            preparedStatement.setDouble(3, vehiculo.getPrecio_venta());
            preparedStatement.setDouble(4, vehiculo.getIva_venta());
            preparedStatement.setDouble(5, vehiculo.getTotal_venta());
            preparedStatement.setDate(6, Date.valueOf(vehiculo.getFecha_venta()));
            preparedStatement.setString(7, vehiculo.getNfra_venta());
            preparedStatement.setString(8, vehiculo.getComentarios());
            preparedStatement.setString(9, vehiculo.getBastidor());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que llena un JTable con los vehículos que actualmente no tienen
     * cliente asignado Se muestran las columnas: Fecha de Compra, Modelo,
     * Bastidor, Color y Extras. Las celdas del JTable no son editables.
     *
     * @param tablaStock JTable que se actualizará con los datos de los
     * vehículos sin cliente
     * @return el mismo JTable recibido en el parámetro, ahora poblado con los
     * datos
     */
    public JTable mostrarVehiculosSinCliente(JTable tablaStock) {
        DefaultTableModel tabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla.addColumn("Fecha Compra");
        tabla.addColumn("Modelo");
        tabla.addColumn("Bastidor");
        tabla.addColumn("Color");
        tabla.addColumn("Extras");
        tablaStock.setModel(tabla);

        String SQL = "SELECT * FROM listado_vehiculos WHERE id_cliente IS NULL";
        String datos[] = new String[5];
        DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                LocalDate fecha = rs.getDate("fecha_compra").toLocalDate();
                datos[0] = fecha.format(formatoSalida);
                datos[1] = rs.getString("modelo");
                datos[2] = rs.getString("bastidor");
                datos[3] = rs.getString("color");
                datos[4] = rs.getString("extras");
                tabla.addRow(datos);
            }
            tablaStock.setModel(tabla);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tablaStock;
    }

    /**
     * Método que llena un JTable con los vehículos que ya han sido facturados,
     * es decir, aquellos que tienen un cliente asignado Las columnas que se
     * muestran son: Fecha de Venta, Modelo, Bastidor, Matrícula, Precio de
     * Venta y Nº de Cliente Las celdas del JTable no son editables
     *
     * @param tablaStock JTable que se actualizará con los datos de los
     * vehículos facturados
     * @return el mismo JTable recibido en el parámetro, ahora poblado con los
     * datos
     */
    public JTable mostrarVehiculosFacturados(JTable tablaStock) {
        DefaultTableModel tabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla.addColumn("Fecha Venta");
        tabla.addColumn("Modelo");
        tabla.addColumn("Bastidor");
        tabla.addColumn("Matrícula");
        tabla.addColumn("Precio Venta");
        tabla.addColumn("Nº Cliente");
        tablaStock.setModel(tabla);

        String SQL = "SELECT * FROM listado_vehiculos WHERE id_cliente IS NOT NULL";
        String datos[] = new String[6];

        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.of("es", "ES"));

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                LocalDate fecha = rs.getDate("fecha_venta").toLocalDate();
                datos[0] = fecha.format(formatoFecha);
                datos[1] = rs.getString("modelo");
                datos[2] = rs.getString("bastidor");
                datos[3] = rs.getString("matricula");
                double precio = rs.getDouble("precio_venta");
                datos[4] = formatoMoneda.format(precio);
                datos[5] = rs.getString("id_cliente");
                tabla.addRow(datos);
            }

            tablaStock.setModel(tabla);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tablaStock;
    }

    /**
     * Método que filtra el stock de vehículos que no tienen cliente según un
     * criterio específico y actualiza un JTable con los resultados
     *
     * @param tablaStock JTable que se actualizará con los resultados del filtro
     * @param filtro criterio de filtrado: "Color", "Modelo" o "Bastidor"
     * @param valor valor que se busca según el criterio de filtrado
     * @return el mismo JTable recibido, ahora poblado con los vehículos que
     * cumplen el filtro
     */
    public JTable filtrarStock(JTable tablaStock, String filtro, String valor) {
        DefaultTableModel tabla = new DefaultTableModel();
        tabla.addColumn("Fecha Compra");
        tabla.addColumn("Modelo");
        tabla.addColumn("Bastidor");
        tabla.addColumn("Color");
        tabla.addColumn("Extras");
        tablaStock.setModel(tabla);

        String SQL = "";

        switch (filtro) {
            case "Color":
                SQL = "SELECT fecha_compra, modelo, bastidor, color, extras "
                        + "FROM listado_vehiculos WHERE id_cliente IS NULL AND color LIKE ?";
                break;
            case "Modelo":
                SQL = "SELECT fecha_compra, modelo, bastidor, color, extras "
                        + "FROM listado_vehiculos WHERE id_cliente IS NULL AND modelo LIKE ?";
                break;
            case "Bastidor":
                SQL = "SELECT fecha_compra, modelo, bastidor, color, extras "
                        + "FROM listado_vehiculos WHERE id_cliente IS NULL AND bastidor LIKE ?";
                break;
            default:
                return tablaStock;
        }

        try (PreparedStatement ps = cn.prepareStatement(SQL)) {
            ps.setString(1, "%" + valor + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String[] datos = {
                    rs.getString("fecha_compra"),
                    rs.getString("modelo"),
                    rs.getString("bastidor"),
                    rs.getString("color"),
                    rs.getString("extras")
                };
                tabla.addRow(datos);
            }
            tablaStock.setModel(tabla);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tablaStock;
    }

    /**
     * Método que giltra los vehículos que ya han sido facturados según un
     * criterio específico y actualiza un JTable con los resultados
     *
     * @param tablaStock JTable que se actualizará con los resultados del filtro
     * @param filtro criterio de filtrado: "Nº Cliente", "Modelo" o "Bastidor"
     * @param valor valor que se busca según el criterio de filtrado
     * @return el mismo JTable recibido, ahora poblado con los vehículos que
     * cumplen el filtro
     */
    public JTable filtrarFacturados(JTable tablaStock, String filtro, String valor) {
        DefaultTableModel tabla = new DefaultTableModel();
        tabla.addColumn("Fecha Venta");
        tabla.addColumn("Modelo");
        tabla.addColumn("Bastidor");
        tabla.addColumn("Matrícula");
        tabla.addColumn("Precio Venta");
        tabla.addColumn("Nº Cliente");
        tablaStock.setModel(tabla);

        String SQL = "";
        switch (filtro) {
            case "Nº Cliente":
                SQL = "SELECT fecha_venta, modelo, bastidor, matricula, precio_venta, id_cliente "
                        + "FROM listado_vehiculos WHERE id_cliente IS NOT NULL AND CAST(id_cliente AS CHAR) LIKE ?";
                break;
            case "Modelo":
                SQL = "SELECT fecha_venta, modelo, bastidor, matricula, precio_venta, id_cliente "
                        + "FROM listado_vehiculos WHERE id_cliente IS NOT NULL AND modelo LIKE ?";
                break;
            case "Bastidor":
                SQL = "SELECT fecha_venta, modelo, bastidor, matricula, precio_venta, id_cliente "
                        + "FROM listado_vehiculos WHERE id_cliente IS NOT NULL AND bastidor LIKE ?";
                break;
            default:
                return tablaStock;
        }

        try (PreparedStatement ps = cn.prepareStatement(SQL)) {
            ps.setString(1, "%" + valor + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String[] datos = {
                    rs.getString("fecha_venta"),
                    rs.getString("modelo"),
                    rs.getString("bastidor"),
                    rs.getString("matricula"),
                    rs.getString("precio_venta"),
                    rs.getString("id_cliente")
                };
                tabla.addRow(datos);
            }
            tablaStock.setModel(tabla);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tablaStock;
    }

    /**
     * Método que exporta los datos de un JTable a un archivo Excel (.xlsx)
     *
     * Se utiliza Apache POI para generar el archivo Excel y JFileChooser para
     * seleccionar la ubicación de guardado. Se muestran mensajes de éxito o
     * error según corresponda
     *
     * @param table JTable cuyos datos se desean exportar a Excel.
     */
    public void exportarTablaAExcel(JTable table) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Datos Exportados");
        TableModel model = table.getModel();
        Row row;
        Cell cell;
        row = sheet.createRow(0);

        for (int col = 0; col < model.getColumnCount(); col++) {
            cell = row.createCell(col);
            cell.setCellValue(model.getColumnName(col));
        }

        for (int fila = 0; fila < model.getRowCount(); fila++) {
            row = sheet.createRow(fila + 1);
            for (int col = 0; col < model.getColumnCount(); col++) {
                Object valor = model.getValueAt(fila, col);
                cell = row.createCell(col);
                if (valor instanceof Number) {
                    cell.setCellValue(Double.parseDouble(valor.toString()));
                } else {
                    cell.setCellValue(valor.toString());
                }
            }
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar como");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos Excel", "xlsx"));
        int seleccion = fileChooser.showSaveDialog(null);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File file = new File(fileChooser.getSelectedFile() + ".xlsx");
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
                JOptionPane.showMessageDialog(null, "Archivo guardado con éxito: " + file.getAbsolutePath(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al guardar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
