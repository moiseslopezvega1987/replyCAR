package app;

/*Clases importadas para la utilización en la clase.
 */
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.AbstractDocument;

/**
 * Clase para añadir nuevos artículos a nuestra APP
 *
 * @author Moisés López Vega
 */
public class nuevosArticulos extends javax.swing.JDialog {

    // DAO para manejar la persistencia de Artículos
    private ArticuloDAO articuloDAO;
    // DAO para manejar la persistencia de Proveedores
    private ProveedorDAO proveedorDAO;
    // DAO para manejar la persistencia de Clientes
    private ClienteDAO clienteDAO;
    // DAO para manejar datos maestros, como validaciones de CIF/DNI
    private MaestroDAO maestroDAO;
    // Objeto Artículo que representa el artículo actual en el formulario
    private articulo articulo;
    // Configurar el formateador de moneda para euros
    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.of("es", "ES"));

    /**
     * Constructor del diálogo de nuevos artículos
     *
     * @param parent el componente padre.
     * @param modal indica si el cuadro de diálogo es modal.
     * @throws java.sql.SQLException si ocurre un error al ejecutar la sentencia SQL
     */
    public nuevosArticulos(java.awt.Frame parent, boolean modal) throws SQLException {
        // Llamamos al constructor de la superclase JDialog
        super(parent, modal);
        // Inicializa componentes del formulario
        initComponents();
        // Inicializamos DAOs
        clienteDAO = new ClienteDAO();
        maestroDAO = new MaestroDAO();
        proveedorDAO = new ProveedorDAO();
        articuloDAO = new ArticuloDAO();
        // Creamos un nuevo artículo vacío
        articulo = new articulo();
        // Cargar las referencias en el comboBox
        cargarReferencias();
        // Cargar los proveedores en el comboBox
        cargarColumnaProveedores();
        // Adjudicar a los elementos la no edición
        noEditables();

        // Acción del botón Guardar
        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    guardarArticuloActionPerformed(evt);
                } catch (SQLException ex) {
                    Logger.getLogger(nuevosArticulos.class.getName()).log(Level.SEVERE, null, ex);
                }
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
                    Logger.getLogger(nuevosArticulos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Agregar MouseListener a jLabel1 para cerrar la ventana y cambiar el cursor
        maestros.salirFormulario(jLabelSalir, this::compruebaSalir);

        // Agregar MouseListener a jLabelAyuda para abrir la ayuda de la app
        maestros.consultarAyuda(jLabelAyuda, "comprar_proveedores");

        // Selecciona la primera referencia después de cargar todas las referencias
        if (jComboBoxReferencia.getItemCount() > 0) {
            jComboBoxReferencia.setSelectedIndex(0);
            try {
                referenciaSeleccionadaActionPerformed(new ActionEvent(jComboBoxReferencia, ActionEvent.ACTION_PERFORMED, null));
                referenciaSeleccionadaStockActionPerformed(new ActionEvent(jComboBoxReferencia, ActionEvent.ACTION_PERFORMED, null));
            } catch (SQLException ex) {
                Logger.getLogger(nuevosArticulos.class.getName()).log(Level.SEVERE, null, ex);
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
    }

    // Carga los proveedores desde la base de datos y llena el comboBox
    private void cargarColumnaProveedores() {
        List<String> almacenes = proveedorDAO.obtenerColumnas("nombre", "apellido_1", "apellido_2"); // Cambia "nombreColumna" por el nombre real de tu columna
        for (String almacen : almacenes) {
            jComboBoxProveedor.addItem(almacen);
        }
    }

    // Carga todo lo referente al artículo seleccionado
    private void referenciaSeleccionadaActionPerformed(ActionEvent evt) throws SQLException {
        String referenciaSeleccionada = (String) jComboBoxReferencia.getSelectedItem();
        if (referenciaSeleccionada != null) {
            articulo art = articuloDAO.obtenerArticuloPorReferencia(referenciaSeleccionada);
            articuloDAO.obtenerCantidadTotalPiezasPorReferencia(referenciaSeleccionada);
            if (art != null) {
                stockCajaTexto.setText(String.valueOf(art.getTotalPiezas()));
                familiaCajaTexto.setText(String.valueOf(art.getID_Familia()));
                descripcionCajaTexto.setText(art.getDescripcion());
                String importePVP = formatoMoneda.format(art.getpPVP());
                String importeCoste = formatoMoneda.format(art.getpCoste());
                costoCajaTexto.setText(importeCoste);
                pvpCajaTexto.setText(importePVP);
            }
        }
    }

    // Conseguimos las unidades que hay actualmente en stock
    private void referenciaSeleccionadaStockActionPerformed(ActionEvent evt) throws SQLException {
        String referenciaSeleccionada = (String) jComboBoxReferencia.getSelectedItem();
        if (referenciaSeleccionada != null) {
            articulo art = articuloDAO.obtenerCantidadTotalPiezasPorReferencia(referenciaSeleccionada);
            if (art != null) {
                stockCajaTexto.setText(String.valueOf(art.getTotalPiezas()));
            }
        }
    }

    // Método para guardar un artículo nuevo en la base de datos
    private void guardarArticuloActionPerformed(ActionEvent evt) throws SQLException {
        boolean esValido = true;
        try {
            // Comprueba Unidades
            if (!unidadesCajaTexto.getText().isEmpty() && !isNumeric(costoCajaTexto.getText())) {
                articulo.setUnidades(Integer.parseInt(unidadesCajaTexto.getText()));
            } else {
                unidadesCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Comprueba Documento
            if (!documentoCajaTexto.getText().isEmpty()) {
                articulo.setId_factura_almacen(documentoCajaTexto.getText());
            } else {
                documentoCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Comprueba Descuento
            if (!descuentoCajaTexto.getText().isEmpty()) {
                articulo.setDescuento(Integer.parseInt(descuentoCajaTexto.getText()));
            } else {
                descuentoCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            articulo.setFecha_mov(LocalDate.now());
            articulo.setReferencia((String) jComboBoxReferencia.getSelectedItem());
            articulo.setID_Familia(Integer.parseInt(familiaCajaTexto.getText()));
            articulo.setDescripcion(descripcionCajaTexto.getText());
            articulo.setProveedor((String) jComboBoxProveedor.getSelectedItem());
            // Obtener los valores de las cajas de texto
            String importePVP = pvpCajaTexto.getText();
            String importeCoste = costoCajaTexto.getText();
            // Eliminar los caracteres no numéricos y convertir las cadenas a double
            String importePVPNumero = importePVP.replaceAll("[^\\d.,]", "").replace(",", ".");
            String importeCosteNumero = importeCoste.replaceAll("[^\\d.,]", "").replace(",", ".");
            double importePVPDouble = Double.parseDouble(importePVPNumero);
            double importeCosteDouble = Double.parseDouble(importeCosteNumero);

            articulo.setpCoste(importeCosteDouble);
            articulo.setpPVP(importePVPDouble);
            articulo.setDescuento(Integer.parseInt(descuentoCajaTexto.getText()));
            articulo.setTipo("Compra");

            // Guardar el articulo en la base de datos si todas las validaciones pasan
            if (esValido) {
                articuloDAO.guardarArticulo(articulo);
                JOptionPane.showMessageDialog(null, "Artículo comprado correctamente.", "Todo ha ido bien", JOptionPane.INFORMATION_MESSAGE);
                resetearFormulario();
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, corrija los campos en rojo.", "Error de validación de datos", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            // Mostrar un mensaje de error si hay un problema con la entrada del usuario
            JOptionPane.showMessageDialog(null, "Por favor, corrija los campos en rojo.", "Error de validación de datos", JOptionPane.ERROR_MESSAGE);
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

    // Método auxiliar para resetear el formulario con los datos vacíos
    private void resetearFormulario() {
        this.jComboBoxReferencia.setSelectedIndex(0);
        this.unidadesCajaTexto.setText("");
        this.documentoCajaTexto.setText("");
        this.descuentoCajaTexto.setText("");
    }

    // Método para comprobar si los datos están correctos al salir del formulario
    private void compruebaSalir() {
        // Variable que indica si hay algún dato escrito en el formulario
        Boolean hayDatos = false;
        // Comprueba cada campo y si alguno tiene datos, marca la variable como true
        if (!unidadesCajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!documentoCajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!descuentoCajaTexto.getText().isEmpty()) {
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
                nuevosArticulos.this.dispose();
            } else {
                // Si no confirma, simplemente cerramos el frame auxiliar
                frame.dispose();
            }
        } else {
            // Si no hay datos, cerramos directamente el formulario
            nuevosArticulos.this.dispose();
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
        jLabelDocumento = new javax.swing.JLabel();
        jLabelReferencia = new javax.swing.JLabel();
        familiaCajaTexto = new javax.swing.JTextField();
        descripcionCajaTexto = new javax.swing.JTextField();
        documentoCajaTexto = new javax.swing.JTextField();
        jLabelPVP = new javax.swing.JLabel();
        jLabelCosto = new javax.swing.JLabel();
        jLabelDescuento = new javax.swing.JLabel();
        jLabelUnidades = new javax.swing.JLabel();
        pvpCajaTexto = new javax.swing.JTextField();
        descuentoCajaTexto = new javax.swing.JTextField();
        costoCajaTexto = new javax.swing.JTextField();
        unidadesCajaTexto = new javax.swing.JTextField();
        jComboBoxReferencia = new javax.swing.JComboBox<>();
        jComboBoxProveedor = new javax.swing.JComboBox<>();
        stockCajaTexto = new javax.swing.JTextField();
        jLabelStock = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        botonGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ReplyCar - Comprar Nuevo Artículo");

        jLabelSalir.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelSalir.setForeground(new java.awt.Color(0, 0, 204));
        jLabelSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/salir.png"))); // NOI18N
        jLabelSalir.setText("Salir");
        jLabelSalir.setToolTipText("Salir del formulario");

        jLabelAyuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/ayuda.png"))); // NOI18N
        jLabelAyuda.setToolTipText("Consultar ayuda para este formulario");

        jLabelTitulo.setFont(new java.awt.Font("Tempus Sans ITC", 1, 36)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 204));
        jLabelTitulo.setText("Comprar Nuevo Artículo");

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
        jLabelProveedor.setText("(Proveedor):");

        jLabelDocumento.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelDocumento.setForeground(new java.awt.Color(0, 0, 204));
        jLabelDocumento.setText("(Documento):");

        jLabelReferencia.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelReferencia.setForeground(new java.awt.Color(0, 0, 204));
        jLabelReferencia.setText("(Referencia):");

        familiaCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        familiaCajaTexto.setToolTipText("Familia del producto");

        descripcionCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        descripcionCajaTexto.setToolTipText("Descripción del producto");

        documentoCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        documentoCajaTexto.setToolTipText("Ingresa el nº de albarán");
        ((AbstractDocument) this.documentoCajaTexto.getDocument()).setDocumentFilter(new maestros.UppercaseDocumentFilter());
        documentoCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                documentoCajaTextoKeyTyped(evt);
            }
        });

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
        pvpCajaTexto.setToolTipText("PVP del producto");

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
        unidadesCajaTexto.setToolTipText("Inserte las unidades a comprar");
        unidadesCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                unidadesCajaTextoKeyTyped(evt);
            }
        });

        jComboBoxReferencia.setToolTipText("Selecciona la referencia a comprar");

        jComboBoxProveedor.setToolTipText("Seleccione el proveedor");

        stockCajaTexto.setEditable(false);
        stockCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        stockCajaTexto.setForeground(new java.awt.Color(255, 51, 0));
        stockCajaTexto.setToolTipText("Stock actual");
        stockCajaTexto.setEnabled(false);

        jLabelStock.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelStock.setForeground(new java.awt.Color(255, 51, 0));
        jLabelStock.setText("(Stock actual):");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelProveedor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBoxProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jLabelDocumento)
                        .addGap(18, 18, 18)
                        .addComponent(documentoCajaTexto))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(costoCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jLabelPVP)
                        .addGap(18, 18, 18)
                        .addComponent(pvpCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelDescuento)
                        .addGap(12, 12, 12)
                        .addComponent(descuentoCajaTexto))
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
                            .addComponent(stockCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(unidadesCajaTexto, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE))))
                .addGap(0, 10, Short.MAX_VALUE))
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
                    .addComponent(jLabelDocumento)
                    .addComponent(documentoCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCosto)
                    .addComponent(costoCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPVP)
                    .addComponent(pvpCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelDescuento)
                    .addComponent(descuentoCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(196, 196, 196))
        );

        botonGuardar.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        botonGuardar.setForeground(new java.awt.Color(0, 0, 204));
        botonGuardar.setText("Comprar Nuevo Artículo");
        botonGuardar.setToolTipText("Pincha para comprar el nuevo artículo");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(530, Short.MAX_VALUE)
                .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void documentoCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_documentoCajaTextoKeyTyped
        // Limpia el color
        documentoCajaTexto.setBackground(Color.WHITE);
        // Longitud máxima permitida: 13 caracteres
        if (documentoCajaTexto.getText().length() > 13) {
            evt.consume();
        }
    }//GEN-LAST:event_documentoCajaTextoKeyTyped

    private void descuentoCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descuentoCajaTextoKeyTyped
        // Limpia el color
        descuentoCajaTexto.setBackground(Color.WHITE);
        // Longitud máxima permitida: 1 caracteres
        if (descuentoCajaTexto.getText().length() > 1) {
            evt.consume();
        }
    }//GEN-LAST:event_descuentoCajaTextoKeyTyped

    private void unidadesCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unidadesCajaTextoKeyTyped
        // Limpia el color
        unidadesCajaTexto.setBackground(Color.WHITE);
        // Longitud máxima permitida: 4 caracteres
        if (unidadesCajaTexto.getText().length() > 4) {
            evt.consume();
        }
    }//GEN-LAST:event_unidadesCajaTextoKeyTyped

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
            java.util.logging.Logger.getLogger(nuevosArticulos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(nuevosArticulos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(nuevosArticulos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(nuevosArticulos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                nuevosArticulos dialog = null;
                try {
                    dialog = new nuevosArticulos(new javax.swing.JFrame(), false);
                } catch (SQLException ex) {
                    Logger.getLogger(nuevosArticulos.class.getName()).log(Level.SEVERE, null, ex);
                }
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
    private javax.swing.JButton botonGuardar;
    private javax.swing.JTextField costoCajaTexto;
    private javax.swing.JTextField descripcionCajaTexto;
    private javax.swing.JTextField descuentoCajaTexto;
    private javax.swing.JTextField documentoCajaTexto;
    private javax.swing.JTextField familiaCajaTexto;
    private javax.swing.JComboBox<String> jComboBoxProveedor;
    private javax.swing.JComboBox<String> jComboBoxReferencia;
    private javax.swing.JLabel jLabelAyuda;
    private javax.swing.JLabel jLabelCosto;
    private javax.swing.JLabel jLabelDescripcion;
    private javax.swing.JLabel jLabelDescuento;
    private javax.swing.JLabel jLabelDocumento;
    private javax.swing.JLabel jLabelFamilia;
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
    private javax.swing.JTextField pvpCajaTexto;
    private javax.swing.JTextField stockCajaTexto;
    private javax.swing.JTextField unidadesCajaTexto;
    // End of variables declaration//GEN-END:variables
}
