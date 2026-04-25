package app;

/*Clases importadas para la utilización en la clase.
 */
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.AbstractDocument;

/**
 * Clase para añadir nuevos vehículos a nuestra APP, procedentes de la marca con
 * la que tiene concesión la empresa.
 *
 * @author Moisés López Vega
 */
public class nuevosVehiculos extends javax.swing.JDialog {

    // Declaramos el objeto VehiculoDAO para manejar operaciones de base de datos relacionadas con vehículos
    private VehiculoDAO vehiculoDAO;
    // Declaramos el objeto ProveedorDAO para manejar operaciones de base de datos relacionadas con proveedores
    private ProveedorDAO proveedorDAO;
    // Declaramos el objeto Vehiculo a nivel de clase para manejar la información del vehículo actual
    private vehiculo vehiculo;
    // Configuramos el formateador de moneda para euros
    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.of("es", "ES"));
    // Configuramos NumberFormat para reconocer los formatos de número
    NumberFormat formatearNumero = NumberFormat.getInstance(Locale.getDefault());
    // Configuramos el formateador de fechas y nos da formato "dd/MM/yyyy"
    DateTimeFormatter formatearFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Creamos el formulario para nuevos vehículos, Constructor de la clase
     * nuevosVehiculos
     *
     * @param parent el componente padre.
     * @param modal indica si el cuadro de diálogo es modal.
     * @throws SQLException excepción que puede lanzarse durante la creación del
     * objeto.
     */
    public nuevosVehiculos(java.awt.Frame parent, boolean modal) throws SQLException {
        // Llamamos al constructor de la superclase JDialog
        super(parent, modal);
        // Inicializa los componentes visuales de la interfaz gráfica
        initComponents();
        // Inicializamos el objeto proveedorDAO para operaciones con proveedores en BD
        proveedorDAO = new ProveedorDAO();
        // Inicializamos el objeto vehiculoDAO para operaciones con vehículos en BD
        vehiculoDAO = new VehiculoDAO();
        // Inicializamos el objeto Vehiculo para representar el vehículo actual en la clase
        vehiculo = new vehiculo();
        // Oculta la caja de texto para la tarifa del vehículo
        nTarifaVehiculoCajaTexto.setVisible(false);
        // Oculta la caja de texto para la direción web de la foto del vehículo
        fotoCajaTexto.setVisible(false);
        // Oculta la caja de texto para el número del Proveedor
        nProveedorCajaTexto.setVisible(false);
        // Llama al método que carga la lista de clientes en un comboBox
        cargarColumnaProveedores();
        // Llama al método que carga la lista de modelos en un comboBox
        cargarModelos();
        // Llama al método que establece ciertos campos (cajas de texto) que no pueden ser editadas
        noEditables();

        // Acción que ocurre al seleccionar un modelo en el comboBox
        jComboBoxModelo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    // Llama al método que maneja la selección del vehículo
                    vehiculoSeleccionadoActionPerformed(evt);
                    vehiculoSeleccionadoStockActionPerformed(evt);
                    // Llama al método para calcular los importes de los precios
                    calcularImportes();
                    // Manejo de excepciones de SQL en caso de error de BD
                } catch (SQLException ex) {
                    Logger.getLogger(nuevosArticulos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Acción que ocurre al seleccionar un Proveedor en el comboBox
        jComboBoxProveedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    // Llama al método que maneja la selección del Proveedor
                    proveedorSeleccionadoActionPerformed(evt);
                    vehiculoSeleccionadoStockActionPerformed(evt);
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
                    guardarVehiculoActionPerformed(evt);
                    vehiculoSeleccionadoStockActionPerformed(evt);
                    // Manejo de excepciones de SQL en caso de error de BD
                } catch (SQLException | ParseException ex) {
                    Logger.getLogger(nuevosVehiculos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Agregar MouseListener a jLabel1 para cerrar la ventana y cambiar el cursor
        maestros.salirFormulario(jLabelSalir, this::compruebaSalir);

        // Agregar MouseListener a jLabelAyuda para abrir la ayuda de la app
        maestros.consultarAyuda(jLabelAyuda, "compra_vehiculos");

        // Seleccionamos el primer modelo en jComboBoxModelo después de cargar todos los modelos
        // Verifica que haya elementos en jComboBoxModelo
        if (jComboBoxModelo.getItemCount() > 0) {
            // Selecciona el primer elemento en el comboBox de los modelos
            jComboBoxModelo.setSelectedIndex(0);
            try {
                // Activamos el evento de selección de vehículo por modelo
                vehiculoSeleccionadoActionPerformed(new ActionEvent(jComboBoxModelo, ActionEvent.ACTION_PERFORMED, null));
                // Activamos el evento de selección de stock del vehículo
                vehiculoSeleccionadoStockActionPerformed(new ActionEvent(jComboBoxModelo, ActionEvent.ACTION_PERFORMED, null));
                // Manejo de excepción en caso de error al acceder a la BD
            } catch (SQLException ex) {
                Logger.getLogger(nuevosVehiculos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Seleccionamos el primer Proveedor en jComboBoxProveedor después de cargar los proveedores
        // Verifica que haya elementos en jComboBoxProveedor
        if (jComboBoxProveedor.getItemCount() > 0) {
            // Selecciona el primer Proveedor
            jComboBoxProveedor.setSelectedIndex(0);
            try {
                // Activamos el evento de selección de Proveedor
                proveedorSeleccionadoActionPerformed(new ActionEvent(jComboBoxProveedor, ActionEvent.ACTION_PERFORMED, null));
                // Manejo de excepción en caso de error al acceder a la BD
            } catch (SQLException ex) {
                Logger.getLogger(nuevosVehiculos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // Método para cargar los modelos de vehículos en jComboBoxModelo
    private void cargarModelos() {
        try {
            // Obtenemos la lista de modelos de la base de datos
            List<String> modelos = vehiculoDAO.obtenerModelos();
            for (String modelo : modelos) {
                // Agregamos cada modelo al jComboBoxModelo
                jComboBoxModelo.addItem(modelo);
            }
        } catch (SQLException e) {
            // Informa del error en la consola si ocurre un fallo en la BD
            e.printStackTrace();
        }
    }

    // Método para hacer campos que no sean editables
    private void noEditables() {
        // Hace que la caja de texto de base imponible no sea editable
        baseImponibleCajaTexto.setEditable(false);
        // Hace que la caja de texto de IVA no sea editable
        ivaCajaTexto.setEditable(false);
        // Hace que la caja de texto del total de factura no sea editable
        totalFacturaCajaTexto.setEditable(false);
        // Hace que la caja de texto de color no sea editable
        colorCajaTexto.setEditable(false);
        // Hace que la caja de texto de extras no sea editable
        extrasCajaTexto.setEditable(false);
    }

    // Método para calcular los importes del IVA y total de la factura
    private void calcularImportes() {
        // Variable para el importe de IVA
        double importeIva;
        // Variable para el importe total de la factura
        double importeTotalFactura;
        // Variable para almacenar la base imponible
        double bi;
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
            // Calculamos el importe de IVA
            importeIva = (bi * 1.21) - bi;
            // Calculamos el importe total de la factura
            importeTotalFactura = bi * 1.21;
            // Muestra los resultados en los campos correspondientes con formato:
            // Formateamos el importe de IVA en formato de moneda
            String importe_Iva = formatoMoneda.format(importeIva);
            // Mostramos el importe de IVA formateado en la caja de texto
            ivaCajaTexto.setText(importe_Iva);
            // Formateamos el importe total en formato de moneda
            String importe_TotalFactura = formatoMoneda.format(importeTotalFactura);
            // Mostramos el importe total de la factura formateado en la caja de texto
            totalFacturaCajaTexto.setText(importe_TotalFactura);
        } catch (ParseException e) {
            // Mostramos un mensaje de error si el formato numérico en la base imponible es incorrecto
            JOptionPane.showMessageDialog(null, "Error: formato numérico incorrecto en la base imponible", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para cargar nombres de Proveedor en jComboBoxProveedor desde la base de datos
    private void cargarColumnaProveedores() {
        // Obtenemos la lista de nombres de proveedores llamando a obtenerColumna() de proveedorDAO
        List<String> proveedores = proveedorDAO.obtenerColumnas("nombre", "apellido_1", "apellido_2");
        for (String proveedor : proveedores) {
            // Añadimos cada nombre de Proveedor al jComboBoxProveedor
            jComboBoxProveedor.addItem(proveedor);
        }
    }

    // Acción al seleccionar un Proveedor en jComboBoxProveedor
    private void proveedorSeleccionadoActionPerformed(ActionEvent evt) throws SQLException {
        // Obtenemos el nombre del Proveedor seleccionado en el jComboBoxProveedor
        String proveedorSeleccionado = (String) jComboBoxProveedor.getSelectedItem();
        // Verifica que haya un Proveedor seleccionado
        if (proveedorSeleccionado != null) {
            // Llamamos obtenerProveedorSegunNombre() en proveedorDAO para obtener el objeto Proveedor según el nombre
            proveedor prov = proveedorDAO.obtenerProveedorSegunNombre(proveedorSeleccionado);
            // Verifica que el Proveedor exista
            if (prov != null) {
                // Mostramos el ID del Proveedor en la caja de texto
                nProveedorCajaTexto.setText(String.valueOf(prov.getId()));
            }
        }
    }

    // Acción al seleccionar un vehículo en jComboBoxModelo
    private void vehiculoSeleccionadoActionPerformed(ActionEvent evt) throws SQLException {
        // Obtenemos el modelo de vehículo seleccionado en jComboBoxModelo
        String vehiculoSeleccionado = (String) jComboBoxModelo.getSelectedItem();
        // Verificamos que haya un modelo seleccionado
        if (vehiculoSeleccionado != null) {
            // Llamamos obtenerVehiculoPorModelo() en vehiculoDAO para obtener el objeto vehículo según el modelo
            vehiculo veh = vehiculoDAO.obtenerVehiculoPorModelo(vehiculoSeleccionado);
            // Actualizamos la cantidad total de vehículos en stock para el modelo
            vehiculoDAO.obtenerCantidadTotalVehiculosPorModeloStock(vehiculoSeleccionado);
            // Verifica que el vehículo exista
            if (veh != null) {
                // Muestra el total de vehículos en stock
                stockCajaTexto.setText(String.valueOf(veh.getTotalVehiculos()));
                // Muestra el color del vehículo
                colorCajaTexto.setText(veh.getColor());
                // Muestra los extras del vehículo
                extrasCajaTexto.setText(veh.getExtras());
                // Formateamos el precio de compra en formato de moneda
                String baseImponibleFormat = formatoMoneda.format(veh.getPrecio_compra());
                // Mostramos el precio de compra formateado en la caja de texto
                baseImponibleCajaTexto.setText(baseImponibleFormat);
                // Mostramos la ruta o URL de la foto en la caja de texto
                fotoCajaTexto.setText(veh.getFoto());
                // Asgina la tarifa a la caja de texto para posteriormente guardarla
                nTarifaVehiculoCajaTexto.setText(String.valueOf(veh.getId_TarifaVehiculo()));
                // Capturamos error:
                try {
                    // Cargamos la imagen del vehículo desde la URL o ruta de la imagen
                    ImageIcon icono = new ImageIcon(new URI(veh.getFoto()).toURL());
                    // Redimensiona la imagen para que se ajuste al tamaño del JLabel
                    Image imagenRedimensionada = icono.getImage().getScaledInstance(
                            // Ancho del JLabel
                            jLabelFoto.getWidth(),
                            // Alto del JLabel
                            jLabelFoto.getHeight(),
                            // Escala la imagen suavemente
                            Image.SCALE_SMOOTH
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

    // Acción al seleccionar el stock de un vehículo en jComboBoxModelo
    private void vehiculoSeleccionadoStockActionPerformed(ActionEvent evt) throws SQLException {
        // Obtenemos el modelo de vehículo seleccionado en jComboBoxModelo
        String vehiculoSeleccionado = (String) jComboBoxModelo.getSelectedItem();
        // Verificamos que haya un modelo seleccionado
        if (vehiculoSeleccionado != null) {
            // Llamamos obtenerCantidadTotalVehiculosPorModeloStock() en vehiculoDAO para obtener el total de vehículos en stock según el modelo
            vehiculo veh = vehiculoDAO.obtenerCantidadTotalVehiculosPorModeloStock(vehiculoSeleccionado);
            if (veh != null) {
                // Mostramos el total de vehículos en stock en la caja de texto
                stockCajaTexto.setText(String.valueOf(veh.getTotalVehiculos()));
            }
        }
    }

    // Acción para guardar los datos de un vehículo pulsando el botón guardar vehículo
    private void guardarVehiculoActionPerformed(ActionEvent evt) throws SQLException, ParseException {
        // Variable para rastrear si todos los campos son válidos
        boolean esValido = true;
        // Capturamos error:
        try {
            // Validamos y asignamos el campo "documentoCajaTexto" (Nº de factura del Proveedor)
            if (!documentoCajaTexto.getText().isEmpty()) {
                // Asigna el valor si no está vacío
                vehiculo.setNfra_proveedor(documentoCajaTexto.getText());
            } else {
                // Cambia el fondo a rojo si está vacío
                documentoCajaTexto.setBackground(Color.RED);
                // Marca como inválido si el campo está vacío
                esValido = false;
            }
            // Validamos y asignamos el campo "fechaFraCajaTexto" (Fecha de factura)
            if (!fechaFraCajaTexto.getText().isEmpty()) {
                // Capturamos error:
                try {
                    // Parseamos la fecha ingresada usando el formato especificado (dd/MM/yyyy)
                    vehiculo.setFecha_compra(LocalDate.parse(fechaFraCajaTexto.getText(), formatearFecha));
                    // Muestra un mensaje de error si el formato de la fecha es incorrecto
                } catch (DateTimeParseException e) {
                    JOptionPane.showMessageDialog(null, "Formato de fecha incorrecto. Por favor, use el formato: dd/MM/yyyy", "Error en la fecha", JOptionPane.ERROR_MESSAGE);
                    // Marca el campo en rojo si el formato es incorrecto
                    fechaFraCajaTexto.setBackground(Color.RED);
                    // Marca como inválido si el campo está vacío
                    esValido = false;
                }
            } else {
                // Marca el fondo en rojo si está vacío.
                fechaFraCajaTexto.setBackground(Color.RED);
                // Marca como inválido si el campo está vacío
                esValido = false;
            }
            // Validamos y asignamos el campo "bastidorCajaTexto" (Número de bastidor)
            if (!bastidorCajaTexto.getText().isEmpty()) {
                // Asigna el valor si no está vacío
                vehiculo.setBastidor(bastidorCajaTexto.getText());
            } else {
                // Marca el fondo en rojo si está vacío
                bastidorCajaTexto.setBackground(Color.RED);
                // Marca como inválido si el campo está vacío
                esValido = false;
            }
            // Asignamos los valores adicionales a la instancia de "Vehiculo"
            vehiculo.setFecha_mov(LocalDate.now()); // Asigna la fecha actual al campo "fecha_mov"
            vehiculo.setNfra_proveedor(documentoCajaTexto.getText()); // Asigna el número de factura del Proveedor
            vehiculo.setId_proveedor(Integer.parseInt(nProveedorCajaTexto.getText())); //Convierte y asigna el ID del Proveedor
            vehiculo.setModelo((String) jComboBoxModelo.getSelectedItem()); // Asigna el modelo seleccionado
            vehiculo.setBastidor(bastidorCajaTexto.getText()); // Asigna el bastidor
            vehiculo.setFoto(fotoCajaTexto.getText()); // Asigna la foto
            vehiculo.setColor(colorCajaTexto.getText()); // Asigna el color
            vehiculo.setExtras(extrasCajaTexto.getText()); // Asigna los extras
            vehiculo.setId_TarifaVehiculo(Integer.parseInt(nTarifaVehiculoCajaTexto.getText())); // Asigna tarifa de vehículo
            // Conversión y asignación de precios utilizando el formateador de números
            double precioCompra = formatearNumero.parse(baseImponibleCajaTexto.getText()).doubleValue();
            double ivaCompra = formatearNumero.parse(ivaCajaTexto.getText()).doubleValue();
            double totalCompra = formatearNumero.parse(totalFacturaCajaTexto.getText()).doubleValue();
            vehiculo.setPrecio_compra(precioCompra); // Asigna el precio de compra
            vehiculo.setIva_compra(ivaCompra); // Asigna el IVA de la compra
            vehiculo.setTotal_compra(totalCompra);// Asigna el total de la compra
            // Verificamos si todos los campos son válidos
            if (esValido) {
                // Capturamos error:
                try {
                    // Intentamos guardar el vehículo en la base de datos usando "vehiculoDAO"
                    vehiculoDAO.guardarVehiculo(vehiculo);
                    // Muestra un mensaje de éxito al usuario si el guardado fue correcto
                    JOptionPane.showMessageDialog(null, "Vehículo comprado correctamente.", "Todo ha ido bien", JOptionPane.INFORMATION_MESSAGE);
                    resetearFormulario(); //Limpia el formulario tras el guardado exitoso
                } catch (SQLException e) {
                    // Captura errores SQL específicos y muestra mensajes de error detallados
                    String mensajeError = e.getMessage();
                    if (mensajeError.contains("bastidor")) {
                        // Si el error indica un bastidor duplicado, muestra un mensaje específico
                        JOptionPane.showMessageDialog(null, "Error: el bastidor ya existe en la base de datos.", "Bastidor duplicado", JOptionPane.ERROR_MESSAGE);
                        // Marca el campo en rojo
                        bastidorCajaTexto.setBackground(Color.RED);
                    } else if (mensajeError.contains("nfra_proveedor")) {
                        // Si el error indica un número de factura duplicado, muestra un mensaje específico
                        JOptionPane.showMessageDialog(null, "Error: el número de factura ya existe en la base de datos.", "Nº factura proveedor duplicado", JOptionPane.ERROR_MESSAGE);
                        // Marca el campo en rojo
                        documentoCajaTexto.setBackground(Color.RED);
                    } else {
                        // Muestra un mensaje genérico si ocurre otro tipo de error de base de datos
                        JOptionPane.showMessageDialog(null, "Error al guardar el vehículo: " + mensajeError, "Error de base de datos", JOptionPane.ERROR_MESSAGE);
                    }
                }
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
        this.fechaFraCajaTexto.setText("");
        this.documentoCajaTexto.setText("");
        this.bastidorCajaTexto.setText("");
    }

    // Método para verificar si hay datos en el formulario antes de cerrar
    private void compruebaSalir() {
        // Variable para rastrear si hay datos ingresados
        Boolean hayDatos = false;
        // Verifica si el campo documentoCajaTexto contiene texto
        if (!documentoCajaTexto.getText().isEmpty()) {
            // Marca hayDatos como true si el campo no está vacío
            hayDatos = true;
        }
        // Verifica si el campo fechaFraCajaTexto contiene texto
        if (!fechaFraCajaTexto.getText().isEmpty()) {
            // Marca hayDatos como true si el campo no está vacío
            hayDatos = true;
        }
        // Verifica si el campo bastidorCajaTexto contiene texto
        if (!bastidorCajaTexto.getText().isEmpty()) {
            //Marca hayDatos como true si el campo no está vacío
            hayDatos = true;
        }
        // Si hay datos, muestra un diálogo de confirmación
        if (hayDatos) {
            // Crea un nuevo JFrame para el mensaje
            JFrame frame = new JFrame("Ey, no tan rápido");
            // Mensaje de advertencia
            String mensaje = "¿Realmente quiere salir?, Hay datos introducidos.";
            // Título del diálogo de confirmación
            String titulo = "Confirmar Salida";
            int opcion = JOptionPane.showConfirmDialog(frame, mensaje, titulo, JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                // Cierra el formulario si el usuario confirma
                nuevosVehiculos.this.dispose();
            } else {
                // Cierra solo el diálogo si el usuario cancela
                frame.dispose();
            }
        } else {
            // Si no hay datos, cierra directamente el formulario sin confirmación
            nuevosVehiculos.this.dispose();
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
        jLabelFechaFra = new javax.swing.JLabel();
        jLabelBastidor = new javax.swing.JLabel();
        jLabelProveedor = new javax.swing.JLabel();
        jLabelDocumento = new javax.swing.JLabel();
        jLabelModelo = new javax.swing.JLabel();
        fechaFraCajaTexto = new javax.swing.JTextField();
        bastidorCajaTexto = new javax.swing.JTextField();
        documentoCajaTexto = new javax.swing.JTextField();
        jLabelIVA = new javax.swing.JLabel();
        jLabelBaseImponible = new javax.swing.JLabel();
        jLabelTotalFactura = new javax.swing.JLabel();
        ivaCajaTexto = new javax.swing.JTextField();
        totalFacturaCajaTexto = new javax.swing.JTextField();
        baseImponibleCajaTexto = new javax.swing.JTextField();
        jComboBoxProveedor = new javax.swing.JComboBox<>();
        jLabelStock = new javax.swing.JLabel();
        jComboBoxModelo = new javax.swing.JComboBox<>();
        stockCajaTexto = new javax.swing.JTextField();
        jLabelFoto = new javax.swing.JLabel();
        jLabelColor = new javax.swing.JLabel();
        colorCajaTexto = new javax.swing.JTextField();
        jLabelExtras = new javax.swing.JLabel();
        extrasCajaTexto = new javax.swing.JTextField();
        nProveedorCajaTexto = new javax.swing.JTextField();
        nTarifaVehiculoCajaTexto = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        fotoCajaTexto = new javax.swing.JTextField();
        botonGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ReplyCar - Comprar Nuevo Vehículo");

        jLabelSalir.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelSalir.setForeground(new java.awt.Color(0, 0, 204));
        jLabelSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/salir.png"))); // NOI18N
        jLabelSalir.setText("Salir");
        jLabelSalir.setToolTipText("Salir del formulario");

        jLabelAyuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/ayuda.png"))); // NOI18N
        jLabelAyuda.setToolTipText("Consultar ayuda para este formulario");

        jLabelTitulo.setFont(new java.awt.Font("Tempus Sans ITC", 1, 36)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 204));
        jLabelTitulo.setText("Comprar Nuevo Vehículo");

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

        jLabelFechaFra.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelFechaFra.setForeground(new java.awt.Color(0, 0, 204));
        jLabelFechaFra.setText("(Fecha Factura):");

        jLabelBastidor.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelBastidor.setForeground(new java.awt.Color(0, 0, 204));
        jLabelBastidor.setText("(Bastidor):");

        jLabelProveedor.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelProveedor.setForeground(new java.awt.Color(0, 0, 204));
        jLabelProveedor.setText("(Proveedor):");

        jLabelDocumento.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelDocumento.setForeground(new java.awt.Color(0, 0, 204));
        jLabelDocumento.setText("(Documento):");

        jLabelModelo.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelModelo.setForeground(new java.awt.Color(0, 0, 204));
        jLabelModelo.setText("(Modelo):");

        fechaFraCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        fechaFraCajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fechaFraCajaTexto.setToolTipText("Ingresa la Fecha de Factura, formato ##/##/####");
        fechaFraCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                fechaFraCajaTextoKeyTyped(evt);
            }
        });

        bastidorCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        bastidorCajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        bastidorCajaTexto.setToolTipText("Inserte el nº de bastidor correspondiente");
        ((AbstractDocument) this.bastidorCajaTexto.getDocument()).setDocumentFilter(new maestros.UppercaseDocumentFilter());
        bastidorCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                bastidorCajaTextoKeyTyped(evt);
            }
        });

        documentoCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        documentoCajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        documentoCajaTexto.setToolTipText("Ingresa el nº de factura del proveedor");
        ((AbstractDocument) this.documentoCajaTexto.getDocument()).setDocumentFilter(new maestros.UppercaseDocumentFilter());
        documentoCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                documentoCajaTextoKeyTyped(evt);
            }
        });

        jLabelIVA.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelIVA.setForeground(new java.awt.Color(0, 0, 204));
        jLabelIVA.setText("(I.V.A.):");

        jLabelBaseImponible.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelBaseImponible.setForeground(new java.awt.Color(0, 0, 204));
        jLabelBaseImponible.setText("(Base Imponible):");

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
        baseImponibleCajaTexto.setToolTipText("Base Imponible");

        jComboBoxProveedor.setToolTipText("Seleccione el proveedor");

        jLabelStock.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelStock.setForeground(new java.awt.Color(255, 51, 0));
        jLabelStock.setText("(Stock actual):");

        jComboBoxModelo.setToolTipText("Selecciona el modelo a comprar");

        stockCajaTexto.setEditable(false);
        stockCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        stockCajaTexto.setForeground(new java.awt.Color(255, 51, 0));
        stockCajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        stockCajaTexto.setToolTipText("Stock actual de este modelo");
        stockCajaTexto.setEnabled(false);

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

        nProveedorCajaTexto.setEditable(false);
        nProveedorCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        nProveedorCajaTexto.setToolTipText("");
        nProveedorCajaTexto.setEnabled(false);

        nTarifaVehiculoCajaTexto.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabelProveedor)
                                            .addComponent(jLabelModelo))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(16, 16, 16)
                                        .addComponent(nTarifaVehiculoCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addGap(41, 41, 41)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jComboBoxModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabelBastidor))
                                            .addComponent(jComboBoxProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(bastidorCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabelStock, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(stockCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(jLabelFechaFra)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(fechaFraCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabelDocumento)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(documentoCajaTexto))))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabelColor, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(colorCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabelExtras)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(extrasCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jLabelFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 486, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(232, 232, 232))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabelBaseImponible, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                                .addGap(40, 40, 40))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(nProveedorCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(12, 12, 12))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelFechaFra)
                            .addComponent(fechaFraCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelProveedor)
                            .addComponent(jComboBoxProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelDocumento)
                            .addComponent(documentoCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelModelo)
                            .addComponent(jLabelBastidor)
                            .addComponent(bastidorCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelStock)
                            .addComponent(stockCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelFoto, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(105, 105, 105)
                        .addComponent(nProveedorCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(nTarifaVehiculoCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 203, Short.MAX_VALUE)))
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
        botonGuardar.setText("Comprar Nuevo Vehículo");
        botonGuardar.setToolTipText("Pincha para comprar el nuevo vehículo");

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
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bastidorCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bastidorCajaTextoKeyTyped
        // Cambia el fondo a blanco
        bastidorCajaTexto.setBackground(Color.WHITE);
        // Verifica si la longitud del texto en bastidorCajaTexto es mayor a 16 caracteres
        if (bastidorCajaTexto.getText().length() > 16) {
            evt.consume();
        }
    }//GEN-LAST:event_bastidorCajaTextoKeyTyped

    private void documentoCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_documentoCajaTextoKeyTyped
        // Cambia el fondo a blanco
        documentoCajaTexto.setBackground(Color.WHITE);
        // Verifica si la longitud del texto en documentoCajaTexto es mayor a 10 caracteres
        if (documentoCajaTexto.getText().length() > 10) {
            evt.consume();
        }
    }//GEN-LAST:event_documentoCajaTextoKeyTyped

    private void fechaFraCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fechaFraCajaTextoKeyTyped
        // Cambia el fondo a blanco
        fechaFraCajaTexto.setBackground(Color.WHITE);
        // Verifica si la longitud del texto en fechaFraCajaTexto es mayor a 9 caracteres
        if (fechaFraCajaTexto.getText().length() > 9) {
            evt.consume();
        }
    }//GEN-LAST:event_fechaFraCajaTextoKeyTyped

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
            java.util.logging.Logger.getLogger(nuevosVehiculos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(nuevosVehiculos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(nuevosVehiculos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(nuevosVehiculos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                nuevosVehiculos dialog = null;
                try {
                    dialog = new nuevosVehiculos(new javax.swing.JFrame(), false);
                } catch (SQLException ex) {
                    Logger.getLogger(nuevosVehiculos.class.getName()).log(Level.SEVERE, null, ex);
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
    private javax.swing.JTextField baseImponibleCajaTexto;
    private javax.swing.JTextField bastidorCajaTexto;
    private javax.swing.JButton botonGuardar;
    private javax.swing.JTextField colorCajaTexto;
    private javax.swing.JTextField documentoCajaTexto;
    private javax.swing.JTextField extrasCajaTexto;
    private javax.swing.JTextField fechaFraCajaTexto;
    private javax.swing.JTextField fotoCajaTexto;
    private javax.swing.JTextField ivaCajaTexto;
    private javax.swing.JComboBox<String> jComboBoxModelo;
    private javax.swing.JComboBox<String> jComboBoxProveedor;
    private javax.swing.JLabel jLabelAyuda;
    private javax.swing.JLabel jLabelBaseImponible;
    private javax.swing.JLabel jLabelBastidor;
    private javax.swing.JLabel jLabelColor;
    private javax.swing.JLabel jLabelDocumento;
    private javax.swing.JLabel jLabelExtras;
    private javax.swing.JLabel jLabelFechaFra;
    private javax.swing.JLabel jLabelFoto;
    private javax.swing.JLabel jLabelIVA;
    private javax.swing.JLabel jLabelModelo;
    private javax.swing.JLabel jLabelProveedor;
    private javax.swing.JLabel jLabelSalir;
    private javax.swing.JLabel jLabelStock;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelTotalFactura;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField nProveedorCajaTexto;
    private javax.swing.JTextField nTarifaVehiculoCajaTexto;
    private javax.swing.JTextField stockCajaTexto;
    private javax.swing.JTextField totalFacturaCajaTexto;
    // End of variables declaration//GEN-END:variables
}
