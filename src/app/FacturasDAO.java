package app;

/*Clases importadas para la utilización en la clase.
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Esta clase se encarga de todas las operaciones CRUD (Crear, Leer, Actualizar,
 * Borrar) sobre la tabla "Facturas" de la base de datos. También incluye
 * métodos auxiliares para obtener IDs, filtrar datos o llenar componentes GUI
 * como JTable y JTextField.
 *
 * @author Moisés López Vega
 */
public class FacturasDAO {

    /**
     * Constructor de la clase FacturasDAO. Inicializa la conexión con la base
     * de datos.
     */
    public FacturasDAO() {
    }

    // Formateador de fechas y de moneda
    DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.of("es", "ES"));

    // Creamos conexión
    conexionBD con = new conexionBD();
    Connection cn = con.conectar();

    /**
     * Método para guardar el nº de factura de almacén
     *
     * @return nº de factura de almacén
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public String generarNumeroFactura() throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM facturas_almacen WHERE YEAR(fecha_factura) = YEAR(CURDATE())";
        try (PreparedStatement stmt = cn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int count = rs.getInt("count");
                return String.format("%s-%d-%d", "A", LocalDate.now().getYear(), count + 1);
            } else {
                throw new SQLException("No se pudo generar el número de factura.");
            }
        }
    }

    /**
     * Método para guardar el nº de factura de ventas de vehículos
     *
     * @return nº de factura de ventas de vehículos
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public String generarNumeroFacturaVenta() throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM listado_vehiculos WHERE YEAR(fecha_venta) = YEAR(CURDATE())";
        try (PreparedStatement stmt = cn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int count = rs.getInt("count");
                return String.format("%s-%d-%d", "V", LocalDate.now().getYear(), count + 1);
            } else {
                throw new SQLException("No se pudo generar el número de factura.");
            }
        }
    }

    /**
     * Método para guardar el nº de factura de venta de taller
     *
     * @return nº de factura de venta de taller
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public String generarNumeroFacturaTaller() throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM facturas_taller WHERE YEAR(fecha_factura) = YEAR(CURDATE())";
        try (PreparedStatement stmt = cn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int count = rs.getInt("count");
                return String.format("%s-%d-%d", "T", LocalDate.now().getYear(), count + 1);
            } else {
                throw new SQLException("No se pudo generar el número de factura.");
            }
        }
    }

    /**
     * Método para guardar la factura de almacén en la base de datos
     *
     * @param factura factura de almacén en la base de datos
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public void guardarFactura(facturasAlmacen factura) throws SQLException {
        String sql = "INSERT INTO facturas_almacen (id_nfactura, fecha_factura, base_imponible, iva, importe_factura, ID_Cliente) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setString(1, factura.getNumero_factura());
            stmt.setDate(2, Date.valueOf(factura.getFecha_factura()));
            stmt.setDouble(3, factura.getbaseImponible());
            stmt.setDouble(4, factura.getCalculoIVA());
            stmt.setDouble(5, factura.getpFinal());
            stmt.setInt(6, factura.getnCliente());
            stmt.executeUpdate();
        }
    }

    /**
     * Método para guardar la factura de taller en la base de datos
     *
     * @param factura factura de taller en la base de datos
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public void guardarFacturaTaller(facturasTaller factura) throws SQLException {
        String sql = "INSERT INTO facturas_taller (id_nfactura, fecha_factura, base_imponible, iva, importe_factura, ID_Cliente) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setString(1, factura.getNumero_factura());
            stmt.setDate(2, Date.valueOf(factura.getFecha_factura()));
            stmt.setDouble(3, factura.getBaseImponible());
            stmt.setDouble(4, factura.getIva());
            stmt.setDouble(5, factura.getImporteFactura());
            stmt.setInt(6, factura.getId_cliente());
            stmt.executeUpdate();
        }
    }

    /**
     * Método para guardar una línea de la factura de almacén en la base de
     * datos
     *
     * @param lineaFactura una línea de la factura de almacén en la base de
     * datos
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public void guardarLineaFactura(facturasAlmacen lineaFactura) throws SQLException {
        String sql = "INSERT INTO lineas_factura_almacen (numero_factura, descripcion, cantidad, precio_pvp, descuento, precio_final) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setString(1, lineaFactura.getNumero_factura());
            stmt.setString(2, lineaFactura.getDescripcion());
            stmt.setInt(3, lineaFactura.getCantidad());
            stmt.setDouble(4, lineaFactura.getPrecio_pvp());
            stmt.setInt(5, lineaFactura.getDescuento());
            stmt.setDouble(6, lineaFactura.getpFinalMenosDescuento());
            stmt.executeUpdate();
        }
    }

    /**
     * Método para guardar la intervención de taller en la base de datos
     *
     * @param i intervención de taller en la base de dato
     * @throws SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public void guardarIntervencion(intervenciones i) throws SQLException {
        String sql = """
        INSERT INTO listado_intervenciones
        (modelo, bastidor, matricula, numero_factura, diag1, resum1, precio1, ID_Cliente, ID_Tarifa)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;
        try (PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setString(1, i.getModelo());
            stmt.setString(2, i.getBastidor());
            stmt.setString(3, i.getMatricula());
            stmt.setString(4, i.getNumero_factura());
            stmt.setString(5, i.getDiag1());
            stmt.setString(6, i.getResum1());
            stmt.setDouble(7, i.getPrecio1());
            stmt.setInt(8, i.getId_cliente());
            stmt.setInt(9, i.getId_tarifa());
            stmt.executeUpdate();
        }
    }

    /**
     * Método para mostrar la lista de las facturas de almacén
     *
     * @param facturasAlmacen facturas de almacén
     * @return lista de las facturas de almacén
     */
    public JTable mostrarFacturasAlmacen(JTable facturasAlmacen) {
        DefaultTableModel tabla = new DefaultTableModel();
        tabla.addColumn("Nº Factura");
        tabla.addColumn("Fecha Factura");
        tabla.addColumn("Base Imponible");
        tabla.addColumn("Importe IVA");
        tabla.addColumn("Importe Total");
        tabla.addColumn("Nº Cliente");
        facturasAlmacen.setModel(tabla);

        String SQL = "SELECT * FROM facturas_almacen";
        String datos[] = new String[50];

        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                datos[0] = rs.getString(1);
                LocalDate fecha = rs.getDate("fecha_factura").toLocalDate();
                datos[1] = fecha.format(formatoFecha);
                double base = rs.getDouble("base_imponible");
                datos[2] = formatoMoneda.format(base);
                double iva = rs.getDouble("iva");
                datos[3] = formatoMoneda.format(iva);
                double total = rs.getDouble("importe_factura");
                datos[4] = formatoMoneda.format(total);
                datos[5] = rs.getString(6);
                tabla.addRow(datos);
            }
            facturasAlmacen.setModel(tabla);
        } catch (SQLException ex) {
        }
        return facturasAlmacen;
    }

    /**
     * Método para mostrar la lista de las facturas de taller
     *
     * @param facturasTaller facturas de taller
     * @return lista de las facturas de taller
     */
    public JTable mostrarFacturasTaller(JTable facturasTaller) {
        DefaultTableModel tabla = new DefaultTableModel();
        tabla.addColumn("Nº Factura");
        tabla.addColumn("Fecha Factura");
        tabla.addColumn("Base Imponible");
        tabla.addColumn("Importe IVA");
        tabla.addColumn("Importe Total");
        tabla.addColumn("Nº Cliente");
        facturasTaller.setModel(tabla);

        String SQL = "SELECT * FROM facturas_taller";
        String datos[] = new String[50];

        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                datos[0] = rs.getString(1);
                LocalDate fecha = rs.getDate("fecha_factura").toLocalDate();
                datos[1] = fecha.format(formatoFecha);
                double base = rs.getDouble("base_imponible");
                datos[2] = formatoMoneda.format(base);
                double iva = rs.getDouble("iva");
                datos[3] = formatoMoneda.format(iva);
                double total = rs.getDouble("importe_factura");
                datos[4] = formatoMoneda.format(total);
                datos[5] = rs.getString(6);
                tabla.addRow(datos);
            }
            facturasTaller.setModel(tabla);
        } catch (SQLException ex) {
        }
        return facturasTaller;
    }

    /**
     * Método para mostrar la lista de las facturas de venta de vehículos
     *
     * @param facturasVehiculos facturas de venta de vehículos
     * @return lista de las facturas de venta de vehículos
     */
    public JTable mostrarFacturasVehiculos(JTable facturasVehiculos) {
        DefaultTableModel tabla = new DefaultTableModel();
        tabla.addColumn("Nº Factura");
        tabla.addColumn("Fecha Factura");
        tabla.addColumn("Base Imponible");
        tabla.addColumn("Importe IVA");
        tabla.addColumn("Importe Total");
        tabla.addColumn("Nº Cliente");
        facturasVehiculos.setModel(tabla);

        String SQL = "SELECT * FROM listado_vehiculos WHERE nfra_venta IS NOT NULL";
        String datos[] = new String[6];

        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                datos[0] = rs.getString(20);
                LocalDate fecha = rs.getDate("fecha_venta").toLocalDate();
                datos[1] = fecha.format(formatoFecha);
                double base = rs.getDouble("precio_venta");
                datos[2] = formatoMoneda.format(base);
                double iva = rs.getDouble("iva_venta");
                datos[3] = formatoMoneda.format(iva);
                double total = rs.getDouble("total_venta");
                datos[4] = formatoMoneda.format(total);
                datos[5] = rs.getString(11);
                tabla.addRow(datos);
            }
            facturasVehiculos.setModel(tabla);
        } catch (SQLException ex) {
        }
        return facturasVehiculos;
    }

    /**
     * Método para obtener el nº de cliente por factura de almacén
     *
     * @param nFactura factura de almacén
     * @return nº de cliente por factura de almacén
     */
    public cliente obtenerClientePorFacturaAlmacen(String nFactura) {

        cliente cli = new cliente();

        String sql = """
        SELECT c.nombre, c.apellido_1, c.apellido_2,
               c.direccion, c.numero, c.cp, c.nie, c.provincia, c.n_cuenta,
               f_pago.tipo_pago AS forma_pago
        FROM facturas_almacen f
        JOIN clientes c ON f.ID_Cliente = c.id
        JOIN formas_pago f_pago ON c.id_formaPago = f_pago.id
        WHERE f.id_nfactura = ?
    """;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nFactura);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cli.setNombre(rs.getString("nombre"));
                cli.setApellido1(rs.getString("apellido_1"));
                cli.setApellido2(rs.getString("apellido_2"));
                cli.setDireccion(rs.getString("direccion"));
                cli.setnCuenta(rs.getString("n_cuenta"));
                cli.setNumero(rs.getInt("numero"));
                cli.setProvincia(rs.getString("provincia"));
                cli.setCp(rs.getString("cp"));
                cli.setNie(rs.getString("nie"));
                cli.setFormaPagoTxt(rs.getString("forma_pago"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cli;
    }

    /**
     * Método para obtener el nº de cliente por factura de vehículo
     *
     * @param nFactura factura de vehículo
     * @return nº de cliente por factura de vehículo
     */
    public cliente obtenerClientePorFacturaVehiculo(String nFactura) {

        cliente cli = new cliente();

        String sql = """
        SELECT c.nombre, c.apellido_1, c.apellido_2,
               c.direccion, c.numero, c.cp, c.nie, c.provincia, c.n_cuenta,
               f_pago.tipo_pago AS forma_pago
        FROM listado_vehiculos f
        JOIN clientes c ON f.ID_Cliente = c.id
        JOIN formas_pago f_pago ON c.id_formaPago = f_pago.id
        WHERE f.nfra_venta = ?
    """;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nFactura);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cli.setNombre(rs.getString("nombre"));
                cli.setApellido1(rs.getString("apellido_1"));
                cli.setApellido2(rs.getString("apellido_2"));
                cli.setDireccion(rs.getString("direccion"));
                cli.setnCuenta(rs.getString("n_cuenta"));
                cli.setNumero(rs.getInt("numero"));
                cli.setProvincia(rs.getString("provincia"));
                cli.setCp(rs.getString("cp"));
                cli.setNie(rs.getString("nie"));
                cli.setFormaPagoTxt(rs.getString("forma_pago"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cli;
    }

    /**
     * Método para obtener la factura completa de la venta de vehículos
     *
     * @param nFactura factura completa de la venta de vehículos
     * @return factura completa de la venta de vehículos
     */
    public vehiculo obtenerFacturaVehiculoCompleta(String nFactura) {

        vehiculo veh = new vehiculo();

        try {
            String sql = """
                SELECT nfra_venta, fecha_venta, ID_Cliente, modelo, bastidor, extras, color, matricula, precio_venta, iva_venta, total_venta, comentarios
                FROM listado_vehiculos
                WHERE nfra_venta = ?
            """;

            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, nFactura);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                veh.setNfra_venta(rs.getString("nfra_venta"));
                veh.setFecha_venta(LocalDate.parse(rs.getString("fecha_venta")));
                veh.setBastidor(rs.getString("bastidor"));
                veh.setMatricula(rs.getString("matricula"));
                veh.setModelo(rs.getString("modelo"));
                veh.setExtras(rs.getString("extras"));
                veh.setId_cliente(rs.getInt("ID_Cliente"));
                veh.setColor(rs.getString("color"));
                veh.setComentarios(rs.getString("comentarios"));
                veh.setPrecio_venta(rs.getDouble("precio_venta"));
                veh.setIva_venta(rs.getDouble("iva_venta"));
                veh.setTotal_venta(rs.getDouble("total_venta"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return veh;
    }

    /**
     * Método para obtener el nº de cliente por factura de taller
     *
     * @param nFactura factura de taller
     * @return nº de cliente por factura de taller
     */
    public cliente obtenerClientePorFacturaTaller(String nFactura) {

        cliente cli = new cliente();

        String sql = """
        SELECT c.nombre, c.apellido_1, c.apellido_2,
               c.direccion, c.numero, c.cp, c.nie, c.provincia, c.n_cuenta,
               f_pago.tipo_pago AS forma_pago
        FROM facturas_taller f
        JOIN clientes c ON f.ID_Cliente = c.id
        JOIN formas_pago f_pago ON c.id_formaPago = f_pago.id
        WHERE f.id_nfactura = ?
    """;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nFactura);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cli.setNombre(rs.getString("nombre"));
                cli.setApellido1(rs.getString("apellido_1"));
                cli.setApellido2(rs.getString("apellido_2"));
                cli.setDireccion(rs.getString("direccion"));
                cli.setnCuenta(rs.getString("n_cuenta"));
                cli.setNumero(rs.getInt("numero"));
                cli.setProvincia(rs.getString("provincia"));
                cli.setCp(rs.getString("cp"));
                cli.setNie(rs.getString("nie"));
                cli.setFormaPagoTxt(rs.getString("forma_pago"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cli;
    }

    /**
     * Método para obtener la factura completa de taller
     *
     * @param nFactura factura completa de taller
     * @return factura completa de taller
     */
    public facturasTaller obtenerFacturaFacturaTallerCompleta(String nFactura) {

        facturasTaller fac = new facturasTaller();

        try {
            String sql = """
                SELECT id_nfactura, fecha_factura, ID_Cliente, base_imponible, iva, importe_factura
                FROM facturas_taller
                WHERE id_nfactura = ?
            """;

            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, nFactura);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                fac.setNumero_factura(rs.getString("id_nfactura"));
                fac.setFecha_factura(LocalDate.parse(rs.getString("fecha_factura")));
                fac.setId_cliente(rs.getInt("ID_Cliente"));
                fac.setBaseImponible(rs.getDouble("base_imponible"));
                fac.setIva(rs.getDouble("iva"));
                fac.setImporteFactura(rs.getDouble("importe_factura"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fac;
    }

    /**
     * Método para obtener las intervenciones por nº de factura
     *
     * @param nFactura intervenciones por nº de factura
     * @return intervenciones por nº de factura
     */
    public List<intervenciones> obtenerIntervencionesPorFactura(String nFactura) {

        List<intervenciones> lista = new ArrayList<>();

        String sql = """
        SELECT modelo, bastidor, matricula,
               diag1, resum1, precio1
        FROM listado_intervenciones
        WHERE numero_factura = ?
    """;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nFactura);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                intervenciones i = new intervenciones();
                i.setModelo(rs.getString("modelo"));
                i.setBastidor(rs.getString("bastidor"));
                i.setMatricula(rs.getString("matricula"));
                i.setDiag1(rs.getString("diag1"));
                i.setResum1(rs.getString("resum1"));
                i.setPrecio1(rs.getDouble("precio1"));
                lista.add(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Método para obtener los movimientos de almacén por nº de factura
     *
     * @param nFactura movimientos de almacén por nº de factura
     * @return movimientos de almacén por nº de factura
     */
    public List<String[]> obtenerMovimientosVentaPorFactura(String nFactura) {

        List<String[]> lista = new ArrayList<>();

        String sql = """
        SELECT ID_Tarifa, descripcion, unidades, precio_pvp, descuento, precioDescuento
        FROM movimientos
        WHERE tipo = 'Venta'
        AND id_factura_cliente = ?
    """;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nFactura);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] fila = new String[6];
                fila[0] = rs.getString("ID_Tarifa");
                fila[1] = rs.getString("descripcion");
                fila[2] = String.valueOf(rs.getInt("unidades"));
                fila[3] = String.valueOf(rs.getDouble("precio_pvp"));
                fila[4] = String.valueOf(rs.getInt("descuento"));
                fila[5] = String.valueOf(rs.getDouble("precioDescuento"));
                lista.add(fila);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Método para obtener la factura completa de la venta de almacén
     *
     * @param nFactura factura completa de la venta de almacén
     * @return factura completa de la venta de almacén
     */
    public facturasAlmacen obtenerFacturaFacturaAlmacenCompleta(String nFactura) {

        facturasAlmacen fac = new facturasAlmacen();

        try {
            String sql = """
                SELECT f.id_nfactura,f.fecha_factura,f.ID_Cliente,f.base_imponible,f.iva,f.importe_factura,
            m.ID_Tarifa,m.descripcion,m.unidades,m.precio_pvp,m.descuento
            FROM facturas_almacen f
            INNER JOIN movimientos m ON m.id_factura_cliente = f.id_nfactura
            WHERE f.id_nfactura = ?
            """;

            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, nFactura);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                fac.setNumero_factura(rs.getString("id_nfactura"));
                fac.setFecha_factura(LocalDate.parse(rs.getString("fecha_factura")));
                fac.setnCliente(rs.getInt("ID_Cliente"));
                fac.setbaseImponible(rs.getDouble("base_imponible"));
                fac.setCalculoIVA(rs.getDouble("iva"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fac;
    }

    /**
     * Método para filtrar en el listado de facturas de almacén
     *
     * @param facturasAlmacen facturas de almacén
     * @param filtro filtro que se utiliza para obtener factura
     * @param valor valor que se busca en el filtro
     * @return filtrar en el listado de facturas de almacén
     */
    public JTable filtrarFacturasAlmacen(JTable facturasAlmacen, String filtro, String valor) {
        DefaultTableModel tabla = new DefaultTableModel();
        tabla.addColumn("Nº Factura");
        tabla.addColumn("Fecha Factura");
        tabla.addColumn("Base Imponible");
        tabla.addColumn("Importe IVA");
        tabla.addColumn("Importe Total");
        tabla.addColumn("Nº Cliente");
        facturasAlmacen.setModel(tabla);

        String SQL = "";

        switch (filtro) {
            case "Nº Factura":
                SQL = "SELECT * FROM facturas_almacen WHERE id_nfactura	 LIKE '%" + valor + "%'";
                break;
            case "Nº Cliente":
                SQL = "SELECT * FROM facturas_almacen WHERE ID_Cliente LIKE '%" + valor + "%'";
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
            facturasAlmacen.setModel(tabla);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return facturasAlmacen;
    }

    /**
     * Método para filtrar en el listado de facturas de taller
     *
     * @param facturasTaller facturas de taller
     * @param filtro filtro que se utiliza para obtener factura
     * @param valor valor que se busca en el filtro
     * @return listado de facturas de taller
     */
    public JTable filtrarFacturasTaller(JTable facturasTaller, String filtro, String valor) {
        DefaultTableModel tabla = new DefaultTableModel();
        tabla.addColumn("Nº Factura");
        tabla.addColumn("Fecha Factura");
        tabla.addColumn("Base Imponible");
        tabla.addColumn("Importe IVA");
        tabla.addColumn("Importe Total");
        tabla.addColumn("Nº Cliente");
        facturasTaller.setModel(tabla);

        String SQL = "";

        switch (filtro) {
            case "Nº Factura":
                SQL = "SELECT * FROM facturas_taller WHERE id_nfactura	 LIKE '%" + valor + "%'";
                break;
            case "Nº Cliente":
                SQL = "SELECT * FROM facturas_taller WHERE ID_Cliente LIKE '%" + valor + "%'";
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
            facturasTaller.setModel(tabla);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return facturasTaller;
    }

    /**
     * Método para filtrar en el listado de facturas de vehículos
     *
     * @param facturasVehiculos facturas de vehículos
     * @param filtro filtro que se utiliza para obtener factura
     * @param valor valor que se busca en el filtro
     * @return listado de facturas de vehículos
     */
    public JTable filtrarFacturasVehiculos(JTable facturasVehiculos, String filtro, String valor) {
        DefaultTableModel tabla = new DefaultTableModel();
        tabla.addColumn("Nº Factura");
        tabla.addColumn("Fecha Factura");
        tabla.addColumn("Base Imponible");
        tabla.addColumn("Importe IVA");
        tabla.addColumn("Importe Total");
        tabla.addColumn("Nº Cliente");
        facturasVehiculos.setModel(tabla);

        String SQL = "";

        switch (filtro) {
            case "Nº Factura":
                SQL = "SELECT * FROM listado_vehiculos WHERE nfra_venta	LIKE '%" + valor + "%'";
                break;
            case "Nº Cliente":
                SQL = "SELECT * FROM listado_vehiculos WHERE ID_Cliente LIKE '%" + valor + "%'";
                break;
        }

        String datos[] = new String[50];

        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                datos[0] = rs.getString(20);
                datos[1] = rs.getString(17);
                datos[2] = rs.getString(12);
                datos[3] = rs.getString(13);
                datos[4] = rs.getString(14);
                datos[5] = rs.getString(11);
                tabla.addRow(datos);
            }
            facturasVehiculos.setModel(tabla);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return facturasVehiculos;
    }

    /**
     * Método para exportar a excel una JTable
     *
     * @param table tabla con los datos a exportar
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
