package app;

/*Clases importadas para la utilización en la clase.
 */
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.net.URI;
import java.text.NumberFormat;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;

/**
 * Clase para la venta de vehículos de nuestra APP, que se venden a los
 * clientes.
 *
 * @author Moisés López Vega
 */
public class ventaVehiculos extends javax.swing.JDialog {

    // Declaramos el objeto VehiculoDAO para manejar operaciones de base de datos relacionadas con vehículos
    private VehiculoDAO vehiculoDAO;
    // Declaramos el objeto ClienteDAO para manejar operaciones de base de datos relacionadas con clientes
    private ClienteDAO clienteDAO;
    // Declaramos el objeto Vehiculo a nivel de clase para manejar la información del vehículo actual
    private vehiculo vehiculo;
    // Declaramos el objeto FacturasDAO para manejar operaciones de base de datos relacionadas con facturas
    private FacturasDAO facturasDAO;
    // Configuramos el formateador de moneda para euros
    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.of("es", "ES"));
    // Configuramos NumberFormat para reconocer los formatos de número
    NumberFormat formatearNumero = NumberFormat.getInstance(Locale.getDefault());

    /**
     * Creamos el formulario para ventaVehiculos Constructor de la clase
     * ventaVehiculos
     *
     * @param parent el componente padre.
     * @param modal indica si el cuadro de diálogo es modal.
     * @throws SQLException excepción que puede lanzarse durante la creación del
     * objeto.
     */
    public ventaVehiculos(java.awt.Frame parent, boolean modal) throws SQLException {
        // Llamamos al constructor de la superclase JDialog
        super(parent, modal);
        // Inicializa los componentes visuales de la interfaz gráfica
        initComponents();
        // Inicializamos el objeto clienteDAO para operaciones con clientes en BD
        clienteDAO = new ClienteDAO();
        // Inicializamos el objeto vehiculoDAO para operaciones con vehículos en BD
        vehiculoDAO = new VehiculoDAO();
        // Inicializamos el objeto facturasDAO para operaciones con facturas en BD
        facturasDAO = new FacturasDAO();
        // Inicializamos el objeto Vehiculo para representar el vehículo actual en la clase
        vehiculo = new vehiculo();
        // Oculta la caja de texto para la dirreción web de la foto del vehículo
        fotoCajaTexto.setVisible(false);
        // Oculta la caja de texto para el número del proveedor
        nClienteCajaTexto.setVisible(false);
        // Llama al método que carga la lista de proveedores en un comboBox
        cargarColumnaClientes();
        // Llama al método que carga la lista de modelos en un comboBox
        cargarBastidores();
        // Llama al método que establece ciertos campos (cajas de texto) que no pueden ser editadas
        noEditables();
        // Formatear base imponible a tipo formado moneda €
        formatearCajaMoneda(baseImponibleCajaTexto);

        // Acción que ocurre al seleccionar un modelo en el comboBox
        jComboBoxBastidor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    // Llama al método que maneja la selección del vehículo
                    bastidorSeleccionadoActionPerformed(evt);
                    // Llama al método para calcular los importes de los precios
                    calcularImportes();
                    // Manejo de excepciones de SQL en caso de error de BD  
                } catch (SQLException ex) {
                    Logger.getLogger(nuevosArticulos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Acción que ocurre al seleccionar un proveedor en el comboBox
        jComboBoxCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    // Llama al método que maneja la selección del proveedor
                    clienteSeleccionadoActionPerformed(evt);
                    // Manejo de excepciones de SQL en caso de error de BD
                } catch (SQLException ex) {
                    Logger.getLogger(nuevosArticulos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Acción que ocurre al presionar el botón de guardar vehículo
        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    // Llama al método que guarda el vehículo en la base de datos
                    facturarVehiculoActionPerformed(evt);
                    // Manejo de excepciones de SQL en caso de error de BD
                } catch (SQLException | ParseException ex) {
                    Logger.getLogger(ventaVehiculos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Agregar MouseListener a jLabel1 para cerrar la ventana y cambiar el cursor
        maestros.salirFormulario(jLabelSalir, this::compruebaSalir);

        // Agregar MouseListener a jLabelAyuda para abrir la ayuda de la app
        maestros.consultarAyuda(jLabelAyuda, "venta_vehiculos");

        // Dentro del constructor o método de inicialización de la interfaz gráfica
        baseImponibleCajaTexto.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                calcularImportes();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calcularImportes();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calcularImportes();
            }
        });

        // Seleccionamos el primer modelo en jComboBoxModelo después de cargar todos los modelos
        // Verifica que haya elementos en jComboBoxModelo
        if (jComboBoxBastidor.getItemCount() > 0) {
            // Selecciona el primer elemento en el comboBox de los modelos
            jComboBoxBastidor.setSelectedIndex(0);
            try {
                // Activamos el evento de selección de vehículo por modelo
                bastidorSeleccionadoActionPerformed(new ActionEvent(jComboBoxBastidor, ActionEvent.ACTION_PERFORMED, null));
                // Manejo de excepción en caso de error al acceder a la BD
            } catch (SQLException ex) {
                Logger.getLogger(ventaVehiculos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // Seleccionamos el primer proveedor en jComboBoxProveedor después de cargar los proveedores
        // Verifica que haya elementos en jComboBoxProveedor
        if (jComboBoxCliente.getItemCount() > 0) {
            // Selecciona el primer proveedor
            jComboBoxCliente.setSelectedIndex(0);
            try {
                // Activamos el evento de selección de proveedor
                clienteSeleccionadoActionPerformed(new ActionEvent(jComboBoxCliente, ActionEvent.ACTION_PERFORMED, null));
                // Manejo de excepción en caso de error al acceder a la BD
            } catch (SQLException ex) {
                Logger.getLogger(ventaVehiculos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // Método para cargar los bastidores de vehículos en jComboBoxBastidor
    private void cargarBastidores() {
        try {
            // Obtenemos la lista de bastidores de la base de datos
            List<String> bastidores = vehiculoDAO.obtenerBastidoresSinCliente();
            for (String bastidor : bastidores) {
                // Agregamos cada bastidor al jComboBoxBastidor
                jComboBoxBastidor.addItem(bastidor);
            }
        } catch (SQLException e) {
            // Informa del error en la consola si ocurre un fallo en la BD
            e.printStackTrace();
        }
    }

    // Método para hacer campos que no sean editables
    private void noEditables() {
        ivaCajaTexto.setEditable(false); // Hace que la caja de texto de IVA no sea editable
        totalFacturaCajaTexto.setEditable(false); // Hace que la caja de texto del total de factura no sea editable
        colorCajaTexto.setEditable(false); // Hace que la caja de texto de color no sea editable
        extrasCajaTexto.setEditable(false); // Hace que la caja de texto de extras no sea editable
    }

    // Método para calcular los importes del IVA y total de la factura
    private void calcularImportes() {
        double importeIva; // Variable para el importe de IVA
        double importeTotalFactura; // Variable para el importe total de la factura
        double bi; // Variable para almacenar la base imponible
        // Captura error:
        try {
            // Usa NumberFormat para interpretar el formato de la base imponible con coma decimal y punto de mil
            // Formateamos los números según la configuración regional
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            // Parseamos el texto de base imponible a número
            Number number = format.parse(baseImponibleCajaTexto.getText());
            // Asignamos el valor parseado a la base imponible
            bi = number.doubleValue();
            // Calculamos importes:
            importeIva = (bi * 1.21) - bi; // Calculamos el importe de IVA
            importeTotalFactura = bi * 1.21; // Calculamos el importe total de la factura
            // Muestra los resultados en los campos correspondientes con formato:
            String importe_Iva = formatoMoneda.format(importeIva); // Formateamos el importe de IVA en formato de moneda
            ivaCajaTexto.setText(importe_Iva); // Mostramos el importe de IVA formateado en la caja de texto
            String importe_TotalFactura = formatoMoneda.format(importeTotalFactura); // Formateamos el importe total en formato de moneda
            totalFacturaCajaTexto.setText(importe_TotalFactura); // Mostramos el importe total de la factura formateado en la caja de texto
        } catch (ParseException e) {
            // Mostramos un mensaje de error si el formato numérico en la base imponible es incorrecto
            //JOptionPane.showMessageDialog(null, "Error: formato numérico incorrecto en la base imponible", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para cargar nombres de clientes en jComboBoxCliente desde la base de datos
    private void cargarColumnaClientes() {
        // Obtenemos la lista de nombres de clientes llamando a obtenerColumna() de clienteDAO
        List<String> clientes = clienteDAO.obtenerColumnas("nombre", "apellido_1", "apellido_2");
        for (String cliente : clientes) {
            // Añadimos cada nombre de proveedor al jComboBoxProveedor
            jComboBoxCliente.addItem(cliente);
        }
    }

    // Acción al seleccionar un Cliente en jComboBoxCliente
    private void clienteSeleccionadoActionPerformed(ActionEvent evt) throws SQLException {
        // Obtenemos el nombre del Cliente seleccionado en el jComboBoxCliente
        String clienteSeleccionado = (String) jComboBoxCliente.getSelectedItem();
        // Verifica que haya un Cliente seleccionado
        if (clienteSeleccionado != null) {
            // Llamamos obtenerClienteSegunNombre() en clienteDAO para obtener el objeto Cliente según el nombre
            cliente cli = clienteDAO.obtenerClienteSegunNombre(clienteSeleccionado);
            // Verifica que el Cliente exista
            if (cli != null) {
                // Mostramos el ID del Cliente en la caja de texto
                nClienteCajaTexto.setText(String.valueOf(cli.getId()));
            }
        }
    }

    // Acción al seleccionar un bastidor en jComboBoxBastidor
    private void bastidorSeleccionadoActionPerformed(ActionEvent evt) throws SQLException {
        // Obtenemos el bastidor de vehículo seleccionado en jComboBoxBastidtor
        String bastidorSeleccionado = (String) jComboBoxBastidor.getSelectedItem();
        // Verificamos que haya un bastidor seleccionado
        if (bastidorSeleccionado != null) {
            // Llamamos obtenerVehiculoPorModelo() en vehiculoDAO para obtener el objeto vehículo según el modelo
            vehiculo veh = vehiculoDAO.obtenerVehiculoPorBastidor(bastidorSeleccionado);
            // Verifica que el vehículo exista
            if (veh != null) {
                ModeloCajaTexto.setText(veh.getModelo()); // Muestra el bastidor del vehículo
                colorCajaTexto.setText(veh.getColor()); // Muestra el color del vehículo
                extrasCajaTexto.setText(veh.getExtras()); // Muestra los extras del vehículo
                String baseImponibleFormat = formatoMoneda.format(veh.getPrecio_compra()); // Formateamos el precio de compra en formato de moneda
                precioCompraCajaTexto.setText(baseImponibleFormat);// Mostramos el precio de compra formateado en la caja de texto
                fotoCajaTexto.setText(veh.getFoto());// Mostramos la ruta o URL de la foto en la caja de texto
                // Capturamos error:
                try {
                    // Cargamos la imagen del vehículo desde la URL o ruta de la imagen
                    ImageIcon icono = new ImageIcon(new URI(veh.getFoto()).toURL());
                    // Redimensiona la imagen para que se ajuste al tamaño del JLabel
                    Image imagenRedimensionada = icono.getImage().getScaledInstance(
                            jLabelFoto.getWidth(), // Ancho del JLabel
                            jLabelFoto.getHeight(), // Alto del JLabel
                            Image.SCALE_SMOOTH // Escala la imagen suavemente
                    );
                    // Establecemos la imagen redimensionada en el JLabel jLabelFoto
                    jLabelFoto.setIcon(new ImageIcon(imagenRedimensionada));
                } catch (Exception e) {
                    // Muestra el error en la consola si ocurre un problema al cargar la imagen
                    e.printStackTrace();
                    // Muestra un mensaje de error en jLabelFoto si no se puede cargar la imagen
                    jLabelFoto.setText("No se pudo cargar la imagen.");
                }
            }
        }
    }

    // Acción para guardar los datos de un vehículo pulsando el botón guardar vehículo
    private void facturarVehiculoActionPerformed(ActionEvent evt) throws SQLException, ParseException {
        // Variable para rastrear si todos los campos son válidos
        boolean esValido = true;
        // Capturamos error:
        try {
            // Primero, generar el número de factura
            String numeroFactura = facturasDAO.generarNumeroFacturaVenta();
            vehiculo.setNfra_venta(numeroFactura);
            // Validamos y asignamos el campo "baseImponibleCajaTexto" (Número de bastidor)
            if (!baseImponibleCajaTexto.getText().isEmpty()) {
                try {
                    // Quitamos todo lo que no sea dígito o coma
                    String texto = baseImponibleCajaTexto.getText().replaceAll("[^\\d,]", "");
                    // Reemplazamos la coma decimal por punto
                    texto = texto.replace(",", ".");
                    // Convertimos a double
                    double valor = Double.parseDouble(texto);
                    // Asignamos el valor
                    vehiculo.setPrecio_venta(valor);
                    // Restauramos el color si todo va bien
                    baseImponibleCajaTexto.setBackground(Color.WHITE);
                } catch (NumberFormatException ex) {
                    baseImponibleCajaTexto.setBackground(Color.RED);
                    esValido = false;
                }
            } else {
                baseImponibleCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Validamos y asignamos el campo "MatriculaCajaTexto" (Número de matrícula)
            if (!MatriculaCajaTexto.getText().isEmpty()) {
                // Forzamos a mayúsculas
                String matricula = MatriculaCajaTexto.getText().toUpperCase();
                // Comprobación de formato con regex
                if (!matricula.matches("^\\d{4}[B-DF-HJ-NP-TV-Z]{3}$")) {
                    JOptionPane.showMessageDialog(null, "Formato de matrícula inválido.\nDebe ser: 4 números seguidos de 3 consonantes (ej: 1234BCD).", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                    MatriculaCajaTexto.setBackground(Color.RED);
                    esValido = false;
                } else {
                    try {
                        // Verifica si la matrícula ya existe en la base de datos
                        if (vehiculoDAO.existeMatricula(matricula)) {
                            JOptionPane.showMessageDialog(null, "La matrícula ya está registrada en la base de datos.", "Error: Matrícula duplicada", JOptionPane.ERROR_MESSAGE);
                            MatriculaCajaTexto.setBackground(Color.RED);
                            esValido = false;
                        } else {
                            // Si la matrícula no está duplicada y es válida, asigna el valor.
                            vehiculo.setMatricula(matricula);
                            MatriculaCajaTexto.setBackground(Color.WHITE);
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error al comprobar la matrícula: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
                        esValido = false;
                    }
                }
            } else {
                MatriculaCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Validamos y asignamos el campo "ivaCajaTexto" (Número de matrícula)
            if (!ivaCajaTexto.getText().isEmpty()) {
                double ivaCompra = formatearNumero.parse(ivaCajaTexto.getText()).doubleValue();
                // Asignamos el importe IVA
                vehiculo.setIva_venta(ivaCompra);
            } else {
                // Marca como inválido si el campo está vacío
                esValido = false;
            }
            // Validamos y asignamos el campo "totalFacturaCajaTexto" (Número de matrícula)
            if (!totalFacturaCajaTexto.getText().isEmpty()) {
                double totalCompra = formatearNumero.parse(totalFacturaCajaTexto.getText()).doubleValue();
                // Asignamos el precio total de venta
                vehiculo.setTotal_venta(totalCompra);
            } else {
                // Marca como inválido si el campo está vacío
                esValido = false;
            }
            vehiculo.setId_cliente(Integer.parseInt(nClienteCajaTexto.getText())); // Almacenamos la id del Cliente
            vehiculo.setFecha_venta(LocalDate.now()); // Asignamos la fecha actual = fecha factura
            vehiculo.setBastidor((String) jComboBoxBastidor.getSelectedItem()); // Asigna el bastidor
            vehiculo.setComentarios(jTextAreaComentarios.getText()); // Asigna los comentarios

            if (esValido) {
                // Intentamos guardar el vehículo en la base de datos usando "vehiculoDAO"
                vehiculoDAO.actualizarVehiculoSegunBastidor(vehiculo);
                // Muestra un mensaje de éxito al usuario si el guardado fue correcto
                JOptionPane.showMessageDialog(null, "Vehículo facturado correctamente.", "Todo ha ido bien", JOptionPane.INFORMATION_MESSAGE);
                // Limpia el formulario tras el guardado exitoso
                resetearFormulario();
                // Cerrar formulario
                cerrarFormulario();
            } else {
                // Muestra un mensaje de advertencia si hay campos inválidos
                JOptionPane.showMessageDialog(null, "Por favor, corrija los campos en rojo.", "Error de validación de datos", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            // Maneja el caso en que el formato de algún dato numérico es incorrecto
            JOptionPane.showMessageDialog(null, "Error en el formato de los datos numéricos.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método auxiliar para verificar si una cadena es numérica
    private boolean isNumeric(String str) {
        try {
            // Intenta convertir la cadena a un número entero
            Integer.parseInt(str);
            // Devuelve true si la conversión es exitosa
            return true;
        } catch (NumberFormatException e) {
            // Devuelve false si ocurre una excepción, es decir, si no es un número
            return false;
        }
    }

    // Método para limpiar los campos del formulario
    private void resetearFormulario() {
        // Restablece los campos de texto a vacío para eliminar cualquier dato ingresado
        this.baseImponibleCajaTexto.setText("");
        this.MatriculaCajaTexto.setText("");
        this.jTextAreaComentarios.setText("");
    }

    // Método para cerrar el formulario cuando el vehículo ha sido facturado
    private void cerrarFormulario() {
        ventaVehiculos.this.dispose();
    }

    // Método para formatear precios
    private void formatearCajaMoneda(JTextField campo) {
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    String texto = campo.getText().replaceAll("[^\\d,]", "");
                    if (!texto.isEmpty()) {
                        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.of("es", "ES"));
                        texto = texto.replace(".", "").replace(",", ".");
                        double valor = Double.parseDouble(texto);
                        campo.setText(nf.format(valor));
                    }
                } catch (NumberFormatException ex) {
                    campo.setText("");
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
                String texto = campo.getText().replaceAll("[^\\d,]", "");
                campo.setText(texto);
            }
        });
    }

    // Método para verificar si hay datos en el formulario antes de cerrar
    private void compruebaSalir() {
        // Variable para rastrear si hay datos ingresados
        Boolean hayDatos = false;
        // Verifica si el campo de precio de venta contiene texto
        if (!baseImponibleCajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!MatriculaCajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!jTextAreaComentarios.getText().isEmpty()) {
            hayDatos = true;
        }
        // Si hay datos, muestra un diálogo de confirmación
        if (hayDatos) {
            JFrame frame = new JFrame("Ey, no tan rápido");  // Crea un nuevo JFrame para el mensaje
            String mensaje = "¿Realmente quiere salir?, Hay datos introducidos."; // Mensaje de advertencia
            String titulo = "Confirmar Salida"; // Título del diálogo de confirmación
            int opcion = JOptionPane.showConfirmDialog(frame, mensaje, titulo, JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                // Cierra el formulario si el usuario confirma
                ventaVehiculos.this.dispose();
            } else {
                // Cierra solo el diálogo si el usuario cancela
                frame.dispose();
            }
        } else {
            // Si no hay datos, cierra directamente el formulario sin confirmación
            ventaVehiculos.this.dispose();
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
        jLabelModelo = new javax.swing.JLabel();
        jLabelCliente = new javax.swing.JLabel();
        jLabelBastidor = new javax.swing.JLabel();
        ModeloCajaTexto = new javax.swing.JTextField();
        jLabelIVA = new javax.swing.JLabel();
        jLabelBaseImponible = new javax.swing.JLabel();
        jLabelTotalFactura = new javax.swing.JLabel();
        ivaCajaTexto = new javax.swing.JTextField();
        totalFacturaCajaTexto = new javax.swing.JTextField();
        baseImponibleCajaTexto = new javax.swing.JTextField();
        jComboBoxCliente = new javax.swing.JComboBox<>();
        jComboBoxBastidor = new javax.swing.JComboBox<>();
        jLabelFoto = new javax.swing.JLabel();
        jLabelColor = new javax.swing.JLabel();
        colorCajaTexto = new javax.swing.JTextField();
        jLabelExtras = new javax.swing.JLabel();
        extrasCajaTexto = new javax.swing.JTextField();
        nClienteCajaTexto = new javax.swing.JTextField();
        precioCompraCajaTexto = new javax.swing.JTextField();
        jLabelPrecioCompra = new javax.swing.JLabel();
        jLabelMatricula = new javax.swing.JLabel();
        MatriculaCajaTexto = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaComentarios = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        fotoCajaTexto = new javax.swing.JTextField();
        botonGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ReplyCar - Venta de Vehículo");

        jLabelSalir.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelSalir.setForeground(new java.awt.Color(0, 0, 204));
        jLabelSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/salir.png"))); // NOI18N
        jLabelSalir.setText("Salir");
        jLabelSalir.setToolTipText("Salir del formulario");

        jLabelAyuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/ayuda.png"))); // NOI18N
        jLabelAyuda.setToolTipText("Consultar ayuda para este formulario");

        jLabelTitulo.setFont(new java.awt.Font("Tempus Sans ITC", 1, 36)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 204));
        jLabelTitulo.setText("Venta de Vehículo");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 339, Short.MAX_VALUE)
                .addComponent(jLabelAyuda)
                .addGap(18, 18, 18)
                .addComponent(jLabelSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabelSalir, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTitulo)
                    .addComponent(jLabelAyuda))
                .addContainerGap())
        );

        jLabelModelo.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelModelo.setForeground(new java.awt.Color(0, 0, 204));
        jLabelModelo.setText("(Modelo):");

        jLabelCliente.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelCliente.setForeground(new java.awt.Color(0, 0, 204));
        jLabelCliente.setText("(Cliente):");

        jLabelBastidor.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelBastidor.setForeground(new java.awt.Color(0, 0, 204));
        jLabelBastidor.setText("(Bastidor):");

        ModeloCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        ModeloCajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ModeloCajaTexto.setToolTipText("Modelo del vehículo");

        jLabelIVA.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelIVA.setForeground(new java.awt.Color(0, 0, 204));
        jLabelIVA.setText("(I.V.A.):");

        jLabelBaseImponible.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelBaseImponible.setForeground(new java.awt.Color(0, 0, 204));
        jLabelBaseImponible.setText("(Precio Venta (B.I.):");

        jLabelTotalFactura.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelTotalFactura.setForeground(new java.awt.Color(0, 0, 204));
        jLabelTotalFactura.setText("(Total Factura):");

        ivaCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        ivaCajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ivaCajaTexto.setToolTipText("Importe IVA");

        totalFacturaCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        totalFacturaCajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        totalFacturaCajaTexto.setToolTipText("Total Factura");

        baseImponibleCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        baseImponibleCajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        baseImponibleCajaTexto.setToolTipText("Inserte el precio a facturar sin IVA");
        baseImponibleCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                baseImponibleCajaTextoKeyTyped(evt);
            }
        });

        jComboBoxCliente.setToolTipText("Seleccione el cliente");

        jComboBoxBastidor.setToolTipText("Selecciona el bastidor a facturar");

        jLabelFoto.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        jLabelFoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelFoto.setText("FOTO");
        jLabelFoto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelColor.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelColor.setForeground(new java.awt.Color(0, 0, 204));
        jLabelColor.setText("(Color):");

        colorCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        colorCajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        colorCajaTexto.setToolTipText("Color del vehículo");

        jLabelExtras.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelExtras.setForeground(new java.awt.Color(0, 0, 204));
        jLabelExtras.setText("(Extras):");

        extrasCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        extrasCajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        extrasCajaTexto.setToolTipText("Extras del vehículo");

        nClienteCajaTexto.setEditable(false);
        nClienteCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        nClienteCajaTexto.setToolTipText("");
        nClienteCajaTexto.setEnabled(false);

        precioCompraCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        precioCompraCajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        precioCompraCajaTexto.setToolTipText("Precio Compra");

        jLabelPrecioCompra.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelPrecioCompra.setForeground(new java.awt.Color(0, 0, 204));
        jLabelPrecioCompra.setText("(Precio Compra):");

        jLabelMatricula.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelMatricula.setForeground(new java.awt.Color(0, 0, 204));
        jLabelMatricula.setText("(Matrícula):");

        MatriculaCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        MatriculaCajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        MatriculaCajaTexto.setToolTipText("Inserte la matrícula del vehículo a facturar");
        ((AbstractDocument) this.MatriculaCajaTexto.getDocument()).setDocumentFilter(new maestros.UppercaseDocumentFilter());
        MatriculaCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                MatriculaCajaTextoKeyTyped(evt);
            }
        });

        jTextAreaComentarios.setColumns(20);
        jTextAreaComentarios.setRows(5);
        jTextAreaComentarios.setToolTipText("Ponga los comentarios que desee");
        ((AbstractDocument) this.jTextAreaComentarios.getDocument()).setDocumentFilter(new maestros.UppercaseDocumentFilter());
        jScrollPane1.setViewportView(jTextAreaComentarios);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelCliente)
                        .addGap(14, 14, 14)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jComboBoxCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(23, 23, 23)
                                .addComponent(jLabelPrecioCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(precioCompraCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabelColor, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(colorCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelExtras)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(extrasCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabelFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabelBaseImponible, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(baseImponibleCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelIVA)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ivaCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelTotalFactura)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalFacturaCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelBastidor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxBastidor, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelModelo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ModeloCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelMatricula)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(MatriculaCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(nClienteCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(precioCompraCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelPrecioCompra))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelCliente)
                        .addComponent(jComboBoxCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxBastidor, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelBastidor)
                    .addComponent(jLabelModelo)
                    .addComponent(ModeloCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelMatricula)
                    .addComponent(MatriculaCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(nClienteCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelFoto, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(colorCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelColor)
                    .addComponent(jLabelExtras)
                    .addComponent(extrasCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(baseImponibleCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelBaseImponible)
                    .addComponent(jLabelIVA)
                    .addComponent(ivaCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTotalFactura)
                    .addComponent(totalFacturaCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        fotoCajaTexto.setEditable(false);
        fotoCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        fotoCajaTexto.setToolTipText("");
        fotoCajaTexto.setEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(fotoCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(162, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(fotoCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        botonGuardar.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        botonGuardar.setForeground(new java.awt.Color(0, 0, 204));
        botonGuardar.setText("Facturar Vehículo");
        botonGuardar.setToolTipText("Pincha para facturar el vehículo");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void MatriculaCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MatriculaCajaTextoKeyTyped
        MatriculaCajaTexto.setBackground(Color.WHITE); // Cambia el fondo a blanco
        // Verifica si la longitud del texto en MatriculaCajaTexto es mayor a 6 caracteres
        if (MatriculaCajaTexto.getText().length() > 6) {
            evt.consume();
        }
    }//GEN-LAST:event_MatriculaCajaTextoKeyTyped

    private void baseImponibleCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_baseImponibleCajaTextoKeyTyped
        baseImponibleCajaTexto.setBackground(Color.WHITE); // Cambia el fondo a blanco
        // Verifica si la longitud del texto en MatriculaCajaTexto es mayor a 6 caracteres
        if (baseImponibleCajaTexto.getText().length() > 15) {
            evt.consume();
        }
    }//GEN-LAST:event_baseImponibleCajaTextoKeyTyped

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
            java.util.logging.Logger.getLogger(ventaVehiculos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ventaVehiculos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ventaVehiculos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ventaVehiculos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                ventaVehiculos dialog = null;
                try {
                    dialog = new ventaVehiculos(new javax.swing.JFrame(), false);
                } catch (SQLException ex) {
                    Logger.getLogger(ventaVehiculos.class.getName()).log(Level.SEVERE, null, ex);
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
    private javax.swing.JTextField MatriculaCajaTexto;
    private javax.swing.JTextField ModeloCajaTexto;
    private javax.swing.JTextField baseImponibleCajaTexto;
    private javax.swing.JButton botonGuardar;
    private javax.swing.JTextField colorCajaTexto;
    private javax.swing.JTextField extrasCajaTexto;
    private javax.swing.JTextField fotoCajaTexto;
    private javax.swing.JTextField ivaCajaTexto;
    private javax.swing.JComboBox<String> jComboBoxBastidor;
    private javax.swing.JComboBox<String> jComboBoxCliente;
    private javax.swing.JLabel jLabelAyuda;
    private javax.swing.JLabel jLabelBaseImponible;
    private javax.swing.JLabel jLabelBastidor;
    private javax.swing.JLabel jLabelCliente;
    private javax.swing.JLabel jLabelColor;
    private javax.swing.JLabel jLabelExtras;
    private javax.swing.JLabel jLabelFoto;
    private javax.swing.JLabel jLabelIVA;
    private javax.swing.JLabel jLabelMatricula;
    private javax.swing.JLabel jLabelModelo;
    private javax.swing.JLabel jLabelPrecioCompra;
    private javax.swing.JLabel jLabelSalir;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelTotalFactura;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaComentarios;
    private javax.swing.JTextField nClienteCajaTexto;
    private javax.swing.JTextField precioCompraCajaTexto;
    private javax.swing.JTextField totalFacturaCajaTexto;
    // End of variables declaration//GEN-END:variables
}
