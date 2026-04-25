package app;

/*Clases importadas para la utilización en la clase.
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Clase para la venta de artículos a nuestra APP
 *
 * @author Moisés López Vega
 */
public class ventaArticulos extends javax.swing.JDialog {

    // DAO para manejar la persistencia los Artículos
    private ArticuloDAO articuloDAO;
    // DAO para manejar la persistencia de Clientes
    private ClienteDAO clienteDAO;
    // DAO para manejar la persistencia de Facturas
    private FacturasDAO facturasDAO;
    // Objeto Artículo que representa el artículo actual en el formulario
    private articulo articulo;
    // Objeto Facturas Almacén que representa las facturas de almacén actual en el formulario
    private facturasAlmacen facturas;
    // Creación de la tabla que se utiliza para la insercción de los artículos a vender
    private DefaultTableModel tableModel;
    // Configurar el formateador de moneda para euros
    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.of("es", "ES"));

    /**
     * Constructor del diálogo para la venta de artículos
     *
     * @param parent el componente padre.
     * @param modal indica si el cuadro de diálogo es modal.
     */
    public ventaArticulos(java.awt.Frame parent, boolean modal) {
        // Llamamos al constructor de la superclase JDialog
        super(parent, modal);
        // Inicializa componentes del formulario
        initComponents();
        // Inicializamos DAOs
        clienteDAO = new ClienteDAO();
        facturasDAO = new FacturasDAO();
        articuloDAO = new ArticuloDAO();
        articulo = new articulo();
        // Crear tabla donde se van insertando las líneas a facturar
        tableModel = new DefaultTableModel(new String[]{"ID", "Referencia", "Familia", "Descripción", "Cliente", "Unidades", "Precio Costo", "Precio PVP", "Descuento", "Precio Cliente", "IVA"}, 0);
        // Establecer tabla
        jTableArticulos.setModel(tableModel);
        // Cargar las referencias en el comboBox
        cargarReferencias();
        // Cargar los clientes en el comboBox
        cargarColumnaCliente();
        // Adjudicar a los elementos la no edición
        noEditables();
        // El botón de eliminar líneas se incia desactivado
        botonEliminarLinea.setEnabled(false);
        // Oculta el nº de cliente
        nClienteCajaTexto.setVisible(false);

        // Acción del botón Guardar
        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    guardarFacturaActionPerformed(evt);
                } catch (SQLException ex) {
                    Logger.getLogger(ventaArticulos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Añadir un WindowListener para interceptar el cierre de la ventana
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                compruebaSalir();
            }
        });

        // Agregar acción al JComboBox de las referencias
        jComboBoxReferencia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    referenciaSeleccionadaActionPerformed(evt);
                    referenciaSeleccionadaStockActionPerformed(evt);
                } catch (SQLException ex) {
                    Logger.getLogger(ventaArticulos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Agregar acción al JComboBox de los clientes
        jComboBoxCliente.addActionListener(e -> {
            cliente seleccionado = (cliente) jComboBoxCliente.getSelectedItem();
            if (seleccionado != null) {
                nClienteCajaTexto.setText(String.valueOf(seleccionado.getId()));
            }
        });

        // Agregar MouseListener a jLabel1 para cerrar la ventana y cambiar el cursor
        maestros.salirFormulario(jLabelSalir, this::compruebaSalir);

        // Agregar MouseListener a jLabelAyuda para abrir la ayuda de la app
        maestros.consultarAyuda(jLabelAyuda, "vender_clientes");

        // Selecciona la primera referencia después de cargar todas las referencias
        if (jComboBoxReferencia.getItemCount() > 0) {
            jComboBoxReferencia.setSelectedIndex(0);
            try {
                referenciaSeleccionadaActionPerformed(new ActionEvent(jComboBoxReferencia, ActionEvent.ACTION_PERFORMED, null));
                referenciaSeleccionadaStockActionPerformed(new ActionEvent(jComboBoxReferencia, ActionEvent.ACTION_PERFORMED, null));
            } catch (SQLException ex) {
                Logger.getLogger(ventaArticulos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // Carga las referencias desde la base de datos y llena el comboBox
    private void cargarReferencias() {
        try {
            List<String> referencias = articuloDAO.obtenerReferencias();
            for (String referencia : referencias) {
                jComboBoxReferencia.addItem(referencia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Adjudica la no edición de los elementos includidos en el método
    private void noEditables() {
        familiaCajaTexto.setEditable(false);
        descripcionCajaTexto.setEditable(false);
        costoCajaTexto.setEditable(false);
        pvpCajaTexto.setEditable(false);
        baseImponiblejTextField.setEditable(false);
        IVAjTextField.setEditable(false);
        importeTotaljTextField.setEditable(false);
    }

    // Carga los clientes desde la base de datos y llena el comboBox
    private void cargarColumnaCliente() {
        ClienteDAO clienteDAO = new ClienteDAO();
        List<cliente> lista = clienteDAO.obtenerClientes();
        jComboBoxCliente.removeAllItems();
        for (cliente c : lista) {
            jComboBoxCliente.addItem(c);
        }
        // Esto hace que el JTextField cargue el ID del primer Cliente
        if (jComboBoxCliente.getItemCount() > 0) {
            cliente primero = (cliente) jComboBoxCliente.getItemAt(0);
            nClienteCajaTexto.setText(String.valueOf(primero.getId()));
        }
    }

    // Carga todo lo referente al artículo seleccionado
    private void referenciaSeleccionadaActionPerformed(ActionEvent evt) throws SQLException {
        String referenciaSeleccionada = (String) jComboBoxReferencia.getSelectedItem();
        if (referenciaSeleccionada != null) {
            articulo art = articuloDAO.obtenerArticuloPorReferencia(referenciaSeleccionada);
            if (art != null) {
                familiaCajaTexto.setText(String.valueOf(art.getID_Familia()));
                descripcionCajaTexto.setText(art.getDescripcion());
                String importePVP = formatoMoneda.format(art.getpPVP());
                String importeCoste = formatoMoneda.format(art.getpCoste());
                costoCajaTexto.setText(importeCoste);
                pvpCajaTexto.setText(importePVP);
            }
        }
    }

    // Método auxiliar para verificar si una cadena es numérica
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Método para eliminar la línea seleccionada
    private void actualizarTotalPrecioCoste() {
        double totalPrecio = 0.0;
        double totalIva = 0.0;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object precioObj = tableModel.getValueAt(i, 9);
            Object ivaObj = tableModel.getValueAt(i, 10);
            double precio = convertirStringADouble(precioObj);
            double iva = convertirStringADouble(ivaObj);
            totalPrecio += precio;
            totalIva += iva;
        }
        // Base imponible
        String precioTotalA = formatoMoneda.format(totalPrecio);
        baseImponiblejTextField.setText(precioTotalA);
        // IVA total
        String ivaTotalA = formatoMoneda.format(totalIva);
        IVAjTextField.setText(ivaTotalA);
        // Importe total
        double importeTotal = totalPrecio + totalIva;
        String importeTotalA = formatoMoneda.format(importeTotal);
        importeTotaljTextField.setText(importeTotalA);
    }

    // Método para convertir String a Double
    private double convertirStringADouble(Object valor) {
        if (valor == null) {
            return 0.0;
        }
        String texto = valor.toString()
                .replace("€", "")
                .replace(",", ".")
                .replaceAll("[^\\d.]", "")
                .trim();
        if (texto.isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(texto);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // Conseguimos las unidades que hay actualmente en stock
    private void referenciaSeleccionadaStockActionPerformed(ActionEvent evt) throws SQLException {
        String referenciaSeleccionada = (String) jComboBoxReferencia.getSelectedItem();
        if (referenciaSeleccionada != null) {
            articulo = articuloDAO.obtenerCantidadTotalPiezasPorReferencia(referenciaSeleccionada);
            if (articulo != null) {
                stockCajaTexto.setText(String.valueOf(articulo.getTotalPiezas()));
            } else {
                stockCajaTexto.setText("0");
            }
        }
    }

    // Método para guardar una venta de artículo
    private void guardarFacturaActionPerformed(ActionEvent evt) throws SQLException {
        boolean esValido = true;
        try {
            // Generar número de factura
            String numeroFactura = facturasDAO.generarNumeroFactura();
            // Crear y guardar la factura principal
            facturasAlmacen factura = new facturasAlmacen();
            factura.setNumero_factura(numeroFactura);
            factura.setFecha_factura(LocalDate.now());
            Object baseImponible = baseImponiblejTextField.getText();
            Object calculoIVA = IVAjTextField.getText();
            double precio = convertirStringADouble(baseImponible);
            double iva = convertirStringADouble(calculoIVA);
            factura.setbaseImponible(precio);
            factura.setCalculoIVA(iva);
            factura.setpFinal(factura.getbaseImponible(), factura.getCalculoIVA());
            factura.setnCliente(Integer.parseInt(nClienteCajaTexto.getText()));

            facturasDAO.guardarFactura(factura);

            // Insertar las líneas de la factura en movimientos
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                // Obtener ID del movimiento
                int idMov = Integer.parseInt(tableModel.getValueAt(i, 0).toString());
                // Actualizar movimiento con número de factura
                articuloDAO.actualizarDocumentoMovimiento(idMov, numeroFactura);
            }
            JOptionPane.showMessageDialog(null, "Factura generada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            resetearFormulario();
        } catch (NumberFormatException e) {
            esValido = false;
            JOptionPane.showMessageDialog(null, "Error en los datos introducidos. Por favor, verifica los campos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para resetear los campos y la tabla después de guardar la factura
    private void resetearFormulario() {
        jComboBoxCliente.setEnabled(true);
        jComboBoxReferencia.setSelectedIndex(0);
        jComboBoxCliente.setSelectedIndex(0);
        unidadesCajaTexto.setText("");
        descuentoCajaTexto.setText("");
        tableModel.setRowCount(0);
        baseImponiblejTextField.setText("");
        IVAjTextField.setText("");
        importeTotaljTextField.setText("");
    }

    // Método para comprobar si los datos están correctos al salir del formulario
    private void compruebaSalir() {
        // Variable que indica si hay algún dato escrito en el formulario
        Boolean hayDatos = false;
        // Comprueba cada campo y si alguno tiene datos, marca la variable como true
        if (!unidadesCajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!descuentoCajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        // Comprobar si hay filas en la tabla
        if (tableModel.getRowCount() > 0) {
            hayDatos = true;
        }
        // Si hay datos escritos, pregunta al usuario si realmente quiere salir
        if (hayDatos) {
            JFrame frame = new JFrame("Ey, no tan rápido");
            // Mensaje de advertencia
            String mensaje = "¿Realmente quiere salir?, Hay datos introducidos.";
            String titulo = "Confirmar Salida";
            // Muestra un cuadro de confirmación
            int opcion = JOptionPane.showConfirmDialog(frame, mensaje, titulo, JOptionPane.YES_NO_OPTION);
            // Si el usuario confirma, se cierra el formulario
            if (opcion == JOptionPane.YES_OPTION) {
                // Si hay filas en la tabla, eliminar las líneas de la tabla y la base de datos
                if (tableModel.getRowCount() > 0) {
                    try {
                        for (int i = 0; i < tableModel.getRowCount(); i++) {
                            // Obtener el id_mov de la fila
                            int idMov = (int) tableModel.getValueAt(i, 0);
                            // Eliminar de la base de datos
                            articuloDAO.eliminarArticuloPorId(idMov);
                        }
                        // Limpiar la tabla
                        tableModel.setRowCount(0);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(frame, "Error al eliminar artículos de la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                // Si no confirma, simplemente cerramos el frame auxiliar
                ventaArticulos.this.dispose();
            } else {
                frame.dispose();
            }
        } else {
            // Si no hay datos, cerramos directamente el formulario
            ventaArticulos.this.dispose();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabelSalir = new javax.swing.JLabel();
        jLabelAyuda = new javax.swing.JLabel();
        jLabelTitulo = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabelFamilia = new javax.swing.JLabel();
        jLabelDescripcion = new javax.swing.JLabel();
        jLabelProveedor = new javax.swing.JLabel();
        jLabelReferencia = new javax.swing.JLabel();
        familiaCajaTexto = new javax.swing.JTextField();
        descripcionCajaTexto = new javax.swing.JTextField();
        jLabelPVP = new javax.swing.JLabel();
        jLabelCosto = new javax.swing.JLabel();
        jLabelDescuento = new javax.swing.JLabel();
        jLabelUnidades = new javax.swing.JLabel();
        pvpCajaTexto = new javax.swing.JTextField();
        descuentoCajaTexto = new javax.swing.JTextField();
        costoCajaTexto = new javax.swing.JTextField();
        unidadesCajaTexto = new javax.swing.JTextField();
        jComboBoxReferencia = new javax.swing.JComboBox<>();
        jComboBoxCliente = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableArticulos = new javax.swing.JTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        nClienteCajaTexto = new javax.swing.JTextField();
        jLabelStock = new javax.swing.JLabel();
        botonAgregarLinea = new javax.swing.JButton();
        botonEliminarLinea = new javax.swing.JButton();
        stockCajaTexto = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        botonGuardar = new javax.swing.JButton();
        jLabelDetalleFactura = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabelBaseImponible = new javax.swing.JLabel();
        jLabelImporteTotal = new javax.swing.JLabel();
        jLabelIVA = new javax.swing.JLabel();
        baseImponiblejTextField = new javax.swing.JTextField();
        IVAjTextField = new javax.swing.JTextField();
        importeTotaljTextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ReplyCar - Venta de Artículo");
        setBackground(new java.awt.Color(255, 255, 255));

        jLabelSalir.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelSalir.setForeground(new java.awt.Color(0, 0, 204));
        jLabelSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/salir.png"))); // NOI18N
        jLabelSalir.setText("Salir");
        jLabelSalir.setToolTipText("Salir del formulario");

        jLabelAyuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/ayuda.png"))); // NOI18N
        jLabelAyuda.setToolTipText("Consultar ayuda para este formulario");

        jLabelTitulo.setFont(new java.awt.Font("Tempus Sans ITC", 1, 36)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 204));
        jLabelTitulo.setText("Venta de Artículos");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                .addComponent(jLabelAyuda)
                .addGap(18, 18, 18)
                .addComponent(jLabelSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabelSalir, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelTitulo)
                    .addComponent(jLabelAyuda))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabelFamilia.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelFamilia.setForeground(new java.awt.Color(0, 0, 204));
        jLabelFamilia.setText("(Familia):");

        jLabelDescripcion.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelDescripcion.setForeground(new java.awt.Color(0, 0, 204));
        jLabelDescripcion.setText("(Descripción):");

        jLabelProveedor.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelProveedor.setForeground(new java.awt.Color(0, 0, 204));
        jLabelProveedor.setText("(Cliente):");

        jLabelReferencia.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelReferencia.setForeground(new java.awt.Color(0, 0, 204));
        jLabelReferencia.setText("(Referencia):");

        familiaCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        familiaCajaTexto.setToolTipText("Familia del producto");

        descripcionCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        descripcionCajaTexto.setToolTipText("Descripción del producto");

        jLabelPVP.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelPVP.setForeground(new java.awt.Color(0, 0, 204));
        jLabelPVP.setText("(P.V.P.):");

        jLabelCosto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelCosto.setForeground(new java.awt.Color(0, 0, 204));
        jLabelCosto.setText("(Costo):");

        jLabelDescuento.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelDescuento.setForeground(new java.awt.Color(0, 0, 204));
        jLabelDescuento.setText("(Descuento):");

        jLabelUnidades.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelUnidades.setForeground(new java.awt.Color(0, 0, 204));
        jLabelUnidades.setText("(Unidades):");

        pvpCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        pvpCajaTexto.setToolTipText("P.V.P. del producto");

        descuentoCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        descuentoCajaTexto.setToolTipText("Ingresa el descuento del producto");
        descuentoCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                descuentoCajaTextoKeyTyped(evt);
            }
        });

        costoCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        costoCajaTexto.setToolTipText("Costo del producto");

        unidadesCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        unidadesCajaTexto.setToolTipText("Inserte las unidades a vender");
        unidadesCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                unidadesCajaTextoKeyTyped(evt);
            }
        });

        jComboBoxReferencia.setToolTipText("Selecciona referencia");

        jComboBoxCliente.setToolTipText("Selecciona el cliente");

        jTableArticulos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableArticulos.setToolTipText("Referencias a facturar");
        jScrollPane1.setViewportView(jTableArticulos);
        if (jTableArticulos.getColumnModel().getColumnCount() > 0) {
            jTableArticulos.getColumnModel().getColumn(0).setResizable(false);
            jTableArticulos.getColumnModel().getColumn(1).setResizable(false);
            jTableArticulos.getColumnModel().getColumn(2).setResizable(false);
            jTableArticulos.getColumnModel().getColumn(3).setResizable(false);
        }

        nClienteCajaTexto.setEditable(false);
        nClienteCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        nClienteCajaTexto.setForeground(new java.awt.Color(255, 51, 0));
        nClienteCajaTexto.setToolTipText("Stock actual de la referencia seleccionada");
        nClienteCajaTexto.setEnabled(false);

        jLabelStock.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelStock.setForeground(new java.awt.Color(255, 51, 0));
        jLabelStock.setText("(Stock actual):");

        botonAgregarLinea.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        botonAgregarLinea.setForeground(new java.awt.Color(0, 0, 204));
        botonAgregarLinea.setText("Agregar Línea");
        botonAgregarLinea.setToolTipText("Agregar referencia a facturar");
        botonAgregarLinea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAgregarLineaActionPerformed(evt);
            }
        });

        botonEliminarLinea.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        botonEliminarLinea.setForeground(new java.awt.Color(0, 0, 204));
        botonEliminarLinea.setText("Eliminar Línea");
        botonEliminarLinea.setToolTipText("Eliminar línea seleccionada");
        botonEliminarLinea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarLineaActionPerformed(evt);
            }
        });

        stockCajaTexto.setEditable(false);
        stockCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        stockCajaTexto.setForeground(new java.awt.Color(255, 51, 0));
        stockCajaTexto.setToolTipText("Stock actual de la referencia seleccionada");
        stockCajaTexto.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabelReferencia)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBoxReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelFamilia)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(familiaCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelUnidades))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabelDescripcion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(descripcionCajaTexto)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelStock, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(unidadesCajaTexto, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(stockCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelProveedor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBoxCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(nClienteCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabelCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(costoCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelPVP)
                                .addGap(18, 18, 18)
                                .addComponent(pvpCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(botonEliminarLinea))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabelDescuento)
                                .addGap(12, 12, 12)
                                .addComponent(descuentoCajaTexto))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(botonAgregarLinea)))))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelReferencia)
                    .addComponent(jLabelFamilia)
                    .addComponent(familiaCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelUnidades)
                    .addComponent(unidadesCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDescripcion)
                    .addComponent(descripcionCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelStock)
                    .addComponent(stockCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelProveedor)
                    .addComponent(jComboBoxCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nClienteCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCosto)
                    .addComponent(costoCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPVP)
                    .addComponent(pvpCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelDescuento)
                    .addComponent(descuentoCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonAgregarLinea)
                    .addComponent(botonEliminarLinea))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(168, 168, 168))
        );

        botonGuardar.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        botonGuardar.setForeground(new java.awt.Color(0, 0, 204));
        botonGuardar.setText("Generar Nueva Factura");
        botonGuardar.setToolTipText("Pincha para facturar referencias");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(521, Short.MAX_VALUE)
                .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jLabelDetalleFactura.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabelDetalleFactura.setForeground(new java.awt.Color(0, 0, 204));
        jLabelDetalleFactura.setText("Detalle de la factura:");

        jSeparator1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelBaseImponible.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabelBaseImponible.setForeground(new java.awt.Color(0, 0, 204));
        jLabelBaseImponible.setText("Base Imponible:");

        jLabelImporteTotal.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabelImporteTotal.setForeground(new java.awt.Color(0, 0, 204));
        jLabelImporteTotal.setText("Importe Total:");

        jLabelIVA.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabelIVA.setForeground(new java.awt.Color(0, 0, 204));
        jLabelIVA.setText("I.V.A.:");

        baseImponiblejTextField.setToolTipText("Base Imponible de la factura");

        IVAjTextField.setToolTipText("Importe IVA de la factura");

        importeTotaljTextField.setToolTipText("Importe Total de factura");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(6, 6, 6)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jLabelDetalleFactura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabelBaseImponible)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(baseImponiblejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabelIVA)
                            .addGap(4, 4, 4)
                            .addComponent(IVAjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabelImporteTotal)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(importeTotaljTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(23, 23, 23))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 730, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addContainerGap()))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDetalleFactura)
                    .addComponent(jLabelBaseImponible)
                    .addComponent(jLabelImporteTotal)
                    .addComponent(jLabelIVA)
                    .addComponent(baseImponiblejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(IVAjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(importeTotaljTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void unidadesCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unidadesCajaTextoKeyTyped
        // Limpia el color
        unidadesCajaTexto.setBackground(Color.WHITE);
        // Longitud máxima permitida: 5 caracteres
        if (unidadesCajaTexto.getText().length() > 5) {
            evt.consume();
        }
    }//GEN-LAST:event_unidadesCajaTextoKeyTyped

    private void descuentoCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descuentoCajaTextoKeyTyped
        // Limpia el color
        descuentoCajaTexto.setBackground(Color.WHITE);
        // Longitud máxima permitida: 1 caracteres
        if (descuentoCajaTexto.getText().length() > 1) {
            evt.consume();
        }
    }//GEN-LAST:event_descuentoCajaTextoKeyTyped

    // Método para insertar línea en la tabla para facturar el conjunto
    private void botonAgregarLineaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAgregarLineaActionPerformed
        int unidades = 0;
        int descuento = 0;
        boolean esValido = true;
        try { // Comprueba Unidades 
            if (unidadesCajaTexto.getText().isEmpty() && !isNumeric(costoCajaTexto.getText())) {
                unidadesCajaTexto.setBackground(Color.RED);
                esValido = false;
            } else if (Integer.parseInt(unidadesCajaTexto.getText()) > Integer.parseInt(stockCajaTexto.getText())) {
                JOptionPane.showMessageDialog(null, "No hay suficiente Stock. Por favor, verifica los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                unidadesCajaTexto.setBackground(Color.RED);
                unidadesCajaTexto.setText("");
                esValido = false;
            } else {
                unidades = Integer.parseInt(unidadesCajaTexto.getText());
                esValido = true;
            } // Comprueba Descuento 
            if (!descuentoCajaTexto.getText().isEmpty()) {
                descuento = Integer.parseInt(descuentoCajaTexto.getText());
            } else {
                descuento = Integer.parseInt("0");
            }
            if (esValido) {
                String referencia = (String) jComboBoxReferencia.getSelectedItem();
                int familia = Integer.parseInt(familiaCajaTexto.getText());
                String descripcion = descripcionCajaTexto.getText();

                cliente clienteSeleccionado = (cliente) jComboBoxCliente.getSelectedItem();
                // Para guardar en factura/artículo
                int idCliente = clienteSeleccionado.getId();
                // Para mostrar en tabla
                String nombreCliente = clienteSeleccionado.toString();

                String importePVP = (pvpCajaTexto.getText());
                String importeCoste = (costoCajaTexto.getText());
                // Eliminar los caracteres no numéricos y convertir las cadenas a double 
                String importePVPNumero = importePVP.replaceAll("[^\\d.,]", "").replace(",", ".");
                String importeCosteNumero = importeCoste.replaceAll("[^\\d.,]", "").replace(",", ".");
                double importePVPDouble = Double.parseDouble(importePVPNumero);
                double importeCosteDouble = Double.parseDouble(importeCosteNumero);
                // Calcular el precio con descuento y redondearlo a dos decimales 
                BigDecimal unidadesBD = new BigDecimal(unidades);
                BigDecimal precioPVPBD = new BigDecimal(importePVPDouble);
                BigDecimal descuentoBD = new BigDecimal(descuento).divide(new BigDecimal(100));
                BigDecimal precioConDescuentoBD = unidadesBD.multiply(precioPVPBD).subtract(unidadesBD.multiply(precioPVPBD).multiply(descuentoBD));
                precioConDescuentoBD = precioConDescuentoBD.setScale(2, RoundingMode.HALF_UP);
                double precioConDescuento = precioConDescuentoBD.doubleValue();
                String precioConDescuentoTXT = formatoMoneda.format(precioConDescuento);
                // Calcular el IVA y redondearlo a dos decimales 
                BigDecimal ivaBD = precioConDescuentoBD.multiply(new BigDecimal(0.21));
                ivaBD = ivaBD.setScale(2, RoundingMode.HALF_UP);
                double iva = ivaBD.doubleValue();
                String ivaTXT = formatoMoneda.format(iva);
                // Introducir líneas en movimientos 
                articulo art = new articulo();
                art.setReferencia(referencia);
                art.setID_Familia(familia);
                art.setDescripcion(descripcion);
                art.setProveedor(nombreCliente);
                art.setUnidades(unidades);
                art.setpCoste(importeCosteDouble);
                art.setpPVP(importePVPDouble);
                art.setDescuento(descuento);
                art.setId_factura_almacen(" ");
                art.setFecha_mov(LocalDate.now());
                art.setTipo("Venta");
                try {
                    int idMov = articuloDAO.guardarArticuloRetorna(art);
                    // Guardar el artículo y obtener el id_mov 
                    // Añadir fila con id_mov 
                    tableModel.addRow(new Object[]{idMov, referencia, familia, descripcion, nombreCliente, unidades, importeCoste, importePVP, descuento, precioConDescuentoTXT, ivaTXT});
                    // Actualizar stock 
                    articulo.setTotalPiezas(articulo.getTotalPiezas() - unidades);
                    stockCajaTexto.setText(String.valueOf(articulo.getTotalPiezas()));
                    actualizarTotalPrecioCoste();
                    // Deshabilitar el Cliente si se ha agregado alguna línea 
                    jComboBoxCliente.setEnabled(false);
                    // Habilitar el botón eliminarLinea si hay líneas en la tabla 
                    botonEliminarLinea.setEnabled(tableModel.getRowCount() > 0);
                    unidadesCajaTexto.setText("");
                    descuentoCajaTexto.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Error en los datos introducidos. Por favor, verifica los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    Logger.getLogger(ventaArticulos.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, corrija los campos en rojo.", "Error de validación de datos", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            esValido = false;
            JOptionPane.showMessageDialog(null, "Error en los datos introducidos. Por favor, verifica los campos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_botonAgregarLineaActionPerformed

    // Acción para eliminar línea de la tabla y la no facturación de la misma
    private void botonEliminarLineaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarLineaActionPerformed
        int selectedRow = jTableArticulos.getSelectedRow();
        if (selectedRow != -1) {
            // Obtener el ID único de la fila seleccionada
            int idMov = (int) tableModel.getValueAt(selectedRow, 0);
            int unidades = (int) tableModel.getValueAt(selectedRow, 5);
            String referencia = (String) jComboBoxReferencia.getSelectedItem();
            String referenciaEliminar = (String) tableModel.getValueAt(selectedRow, 1);
            // Eliminar el artículo de la base de datos
            try {
                articuloDAO.eliminarArticuloPorId(idMov);
                // Eliminar la fila de la tabla
                tableModel.removeRow(selectedRow);
                if (referencia.equalsIgnoreCase(referenciaEliminar)) {
                    // Actualizar stock
                    articulo.setTotalPiezas(articulo.getTotalPiezas() + unidades);
                    stockCajaTexto.setText(String.valueOf(articulo.getTotalPiezas()));
                }
                actualizarTotalPrecioCoste();
                // Habilitar/deshabilitar jComboBoxCliente basado en el contenido de la tabla
                if (tableModel.getRowCount() == 0) {
                    jComboBoxCliente.setEnabled(true);
                }
                // Deshabilitar el botón eliminarLinea si no hay líneas en la tabla
                botonEliminarLinea.setEnabled(tableModel.getRowCount() > 0);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al eliminar el artículo de la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione una línea para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_botonEliminarLineaActionPerformed

    /**
     * Método principal que inicia la aplicación.
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ventaArticulos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ventaArticulos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ventaArticulos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ventaArticulos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ventaArticulos dialog = new ventaArticulos(new javax.swing.JFrame(), false);
                dialog.setResizable(false);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField IVAjTextField;
    private javax.swing.JTextField baseImponiblejTextField;
    private javax.swing.JButton botonAgregarLinea;
    private javax.swing.JButton botonEliminarLinea;
    private javax.swing.JButton botonGuardar;
    private javax.swing.JTextField costoCajaTexto;
    private javax.swing.JTextField descripcionCajaTexto;
    private javax.swing.JTextField descuentoCajaTexto;
    private javax.swing.JTextField familiaCajaTexto;
    private javax.swing.JTextField importeTotaljTextField;
    private javax.swing.JComboBox<app.cliente> jComboBoxCliente;
    private javax.swing.JComboBox<String> jComboBoxReferencia;
    private javax.swing.JLabel jLabelAyuda;
    private javax.swing.JLabel jLabelBaseImponible;
    private javax.swing.JLabel jLabelCosto;
    private javax.swing.JLabel jLabelDescripcion;
    private javax.swing.JLabel jLabelDescuento;
    private javax.swing.JLabel jLabelDetalleFactura;
    private javax.swing.JLabel jLabelFamilia;
    private javax.swing.JLabel jLabelIVA;
    private javax.swing.JLabel jLabelImporteTotal;
    private javax.swing.JLabel jLabelPVP;
    private javax.swing.JLabel jLabelProveedor;
    private javax.swing.JLabel jLabelReferencia;
    private javax.swing.JLabel jLabelSalir;
    private javax.swing.JLabel jLabelStock;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelUnidades;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTableArticulos;
    private javax.swing.JTextField nClienteCajaTexto;
    private javax.swing.JTextField pvpCajaTexto;
    private javax.swing.JTextField stockCajaTexto;
    private javax.swing.JTextField unidadesCajaTexto;
    // End of variables declaration//GEN-END:variables
}
