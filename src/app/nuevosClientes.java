package app;

/*Clases importadas para la utilización en la clase.
 */
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.AbstractDocument;

/**
 * Clase para añadir nuevos clientes a nuestra APP
 *
 * @author Moisés López Vega
 */
public class nuevosClientes extends javax.swing.JDialog {

    // DAO para manejar la persistencia de Clientes
    private ClienteDAO clienteDAO;
    // DAO para manejar datos maestros, como validaciones de CIF/DNI
    private MaestroDAO maestroDAO;
    // Objeto Cliente que representa el cliente actual en el formulario
    private cliente cliente;
    // DAO para manejar formas de pago
    private FormasPagoDAO formasPagoDAO;
    // Lista que almacena los IDs reales de las formas de pago
    private List<String> idsFormaPago;

    /**
     * Constructor del diálogo de nuevos clientes
     *
     * @param parent el componente padre.
     * @param modal indica si el cuadro de diálogo es modal.
     */
    public nuevosClientes(java.awt.Frame parent, boolean modal) {
        // Llamamos al constructor de la superclase JDialog
        super(parent, modal);
        // Inicializa componentes del formulario
        initComponents();
        // Inicializamos DAOs
        formasPagoDAO = new FormasPagoDAO();
        clienteDAO = new ClienteDAO();
        maestroDAO = new MaestroDAO();
        // Creamos un nuevo cliente vacío
        cliente = new cliente();
        // Cargar los tipos de forma de pago en el comboBox
        cargarColumnaFormaPago();
        // Obtener siguiente ID de cliente automáticamente
        clienteDAO.obtenerSiguienteID(idCajaTexto);
        // Mostrar el primer ID de forma de pago disponible
        if (!idsFormaPago.isEmpty()) {
            idFormaPagoCajaTexto.setText(idsFormaPago.get(0));
        }
        // Configuración inicial: seleccionamos persona física
        jRadioButtonFisica.setSelected(true);
        apellido1CajaTexto.setEnabled(true);
        apellido2CajaTexto.setEnabled(true);
        // Ocultamos campos que no deben ser editables directamente
        idFormaPagoCajaTexto.setVisible(false);
        idCajaTexto.setEnabled(false);

        // Acción del botón Guardar
        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    guardarClienteActionPerformed(evt);
                } catch (SQLException ex) {
                    Logger.getLogger(nuevosClientes.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Configuración de los RadioButtons para persona física o jurídica
        jRadioButtonFisica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jRadioButtonFisica.isSelected()) {
                    jRadioButtonJuridica.setSelected(false);
                    apellido1CajaTexto.setEnabled(true);
                    apellido2CajaTexto.setEnabled(true);
                } else {
                    jRadioButtonJuridica.setSelected(true);
                    apellido1CajaTexto.setText("");
                    apellido1CajaTexto.setEnabled(false);
                    apellido2CajaTexto.setText("");
                    apellido2CajaTexto.setEnabled(false);
                }
            }
        });

        jRadioButtonJuridica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jRadioButtonJuridica.isSelected()) {
                    jRadioButtonFisica.setSelected(false);
                    apellido1CajaTexto.setText("");
                    apellido1CajaTexto.setEnabled(false);
                    apellido2CajaTexto.setText("");
                    apellido2CajaTexto.setEnabled(false);
                } else {
                    jRadioButtonFisica.setSelected(true);
                    apellido1CajaTexto.setEnabled(true);
                    apellido2CajaTexto.setEnabled(true);
                }
            }
        });

        // Acción al cambiar la forma de pago en el ComboBox
        jComboBoxFP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Habilitar o deshabilitar campo de número de cuenta según el tipo de pago
                if (jComboBoxFP.getSelectedItem().toString().equalsIgnoreCase("Recibo 30 días")) {
                    jLabelNcuenta.setEnabled(true);
                    nCuentaCajaTexto.setEnabled(true);
                } else {
                    jLabelNcuenta.setEnabled(false);
                    nCuentaCajaTexto.setEnabled(false);
                }
                // Mostrar el ID real de la forma de pago seleccionada
                int index = jComboBoxFP.getSelectedIndex();
                if (index >= 0 && idsFormaPago != null && index < idsFormaPago.size()) {
                    idFormaPagoCajaTexto.setText(idsFormaPago.get(index));
                }
            }
        });

        // Agregar MouseListener a jLabel1 para cerrar la ventana y cambiar el cursor
        maestros.salirFormulario(jLabelSalir, this::compruebaSalir);

        // Agregar MouseListener a jLabelAyuda para abrir la ayuda de la app
        maestros.consultarAyuda(jLabelAyuda, "nuevos_clientes");
    }

    // Carga los tipos de forma de pago desde la base de datos y llena el comboBox
    private void cargarColumnaFormaPago() {
        List<String[]> datos = formasPagoDAO.obtenerDosColumnas("id", "tipo_pago");
        idsFormaPago = new ArrayList<>();
        for (String[] fila : datos) {
            String id = fila[0];
            String tipo = fila[1];
            idsFormaPago.add(id);
            jComboBoxFP.addItem(tipo);
        }
    }

    // Método para guardar un cliente nuevo en la base de datos
    private void guardarClienteActionPerformed(ActionEvent evt) throws SQLException {
        boolean esValido = true;
        try {
            // Validación de CIF/NIE según tipo de cliente
            if (jRadioButtonFisica.isSelected()) {
                cliente.setTipo(true);
            } else {
                cliente.setTipo(false);
            }
            // Establecer el ID
            cliente.setId(Integer.parseInt(idCajaTexto.getText()));
            // Comprueba CIF para clientes jurídicos
            if (jRadioButtonJuridica.isSelected()) {
                if (!nieCajaTexto.getText().isEmpty() && maestroDAO.validarCIF(nieCajaTexto.getText())) {
                    cliente.setNie(nieCajaTexto.getText());
                } else {
                    nieCajaTexto.setBackground(Color.RED);
                    esValido = false;
                }
            }
            // Comprueba DNI para clientes físicos
            if (jRadioButtonFisica.isSelected()) {
                if (!nieCajaTexto.getText().isEmpty() && maestroDAO.validarDNI(nieCajaTexto.getText())) {
                    cliente.setNie(nieCajaTexto.getText());
                } else {
                    nieCajaTexto.setBackground(Color.RED);
                    esValido = false;
                }
            }
            // Comprueba Nombre
            if (!nombreCajaTexto.getText().isEmpty()) {
                cliente.setNombre(nombreCajaTexto.getText());
            } else {
                nombreCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Comprueba apellidos
            if (!apellido1CajaTexto.getText().isEmpty() && !apellido2CajaTexto.getText().isEmpty() || jRadioButtonJuridica.isSelected()) {
                cliente.setApellido1(apellido1CajaTexto.getText());
                cliente.setApellido2(apellido2CajaTexto.getText());
            } else {
                apellido1CajaTexto.setBackground(Color.RED);
                apellido2CajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Comprueba dirección
            if (!direccionCajaTexto.getText().isEmpty()) {
                cliente.setDireccion(direccionCajaTexto.getText());
            } else {
                direccionCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Comprueba numero
            if (!numeroCajaTexto.getText().isEmpty() && isNumeric(numeroCajaTexto.getText())) {
                cliente.setNumero(Integer.parseInt(numeroCajaTexto.getText()));
            } else {
                numeroCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Comprueba CP
            if (!cpCajaTexto.getText().isEmpty() && isNumeric(cpCajaTexto.getText())) {
                cliente.setCp(cpCajaTexto.getText());
            } else {
                cpCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Comprueba Población
            if (!poblacionCajaTexto.getText().isEmpty()) {
                cliente.setPoblacion(poblacionCajaTexto.getText());
            } else {
                poblacionCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Comprueba Provincia
            if (!provinciaCajaTexto.getText().isEmpty()) {
                cliente.setProvincia(provinciaCajaTexto.getText());
            } else {
                provinciaCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Comprueba País
            if (!paisCajaTexto.getText().isEmpty()) {
                cliente.setPais(paisCajaTexto.getText());
            } else {
                paisCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Comprueba E-mail
            if (!emailCajaTexto.getText().isEmpty() && maestroDAO.validarEmail(emailCajaTexto.getText())) {
                cliente.setEmail(emailCajaTexto.getText());
            } else {
                emailCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Comprueba Teléfono
            if (!telefonoCajaTexto.getText().isEmpty() && isNumeric(telefonoCajaTexto.getText())) {
                cliente.setTlfMovil(telefonoCajaTexto.getText());
            } else {
                telefonoCajaTexto.setBackground(Color.RED);
                esValido = false;
            }

            int variable = Integer.parseInt(idFormaPagoCajaTexto.getText());
            cliente.setFormaPago(variable);

            if (nCuentaCajaTexto.isEnabled() && !nCuentaCajaTexto.getText().isEmpty() || !nCuentaCajaTexto.isEnabled()) {
                cliente.setnCuenta(nCuentaCajaTexto.getText());
            } else {
                nCuentaCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Guardar el cliente en la base de datos si todas las validaciones pasan
            if (esValido) {
                clienteDAO.guardarCliente(cliente);
                JOptionPane.showMessageDialog(null, "Cliente guardado Correctamente.", "Todo ha ido bien", JOptionPane.INFORMATION_MESSAGE);
                resetearFormulario();
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, corrija los campos en rojo.", "Error de validación de datos", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            // Mostrar un mensaje de error si hay un problema con la entrada del usuario
            System.err.println("Error al guardar el cliente: " + e.getMessage());
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
        // Limpia el campo NIE / CIF
        this.nieCajaTexto.setText("");
        // Solicita al DAO el siguiente ID disponible y lo coloca en el campo
        this.clienteDAO.obtenerSiguienteID(idCajaTexto);
        // Marca el tipo de persona por defecto como FÍSICA
        this.jRadioButtonFisica.setSelected(true);
        this.jRadioButtonJuridica.setSelected(false);
        // Limpia nombre y activa apellidos
        this.nombreCajaTexto.setText("");
        this.apellido1CajaTexto.setEnabled(true);
        this.apellido1CajaTexto.setText("");
        this.apellido2CajaTexto.setEnabled(true);
        this.apellido2CajaTexto.setText("");
        // Limpia dirección completa
        this.direccionCajaTexto.setText("");
        this.numeroCajaTexto.setText("");
        this.cpCajaTexto.setText("");
        this.poblacionCajaTexto.setText("");
        this.provinciaCajaTexto.setText("");
        this.paisCajaTexto.setText("");
        // Limpia contacto
        this.emailCajaTexto.setText("");
        this.telefonoCajaTexto.setText("");
        // Limpia nº de cuenta bancaria
        this.nCuentaCajaTexto.setText("");
        // Selecciona la primera forma de pago en el combo
        this.jComboBoxFP.setSelectedIndex(0);
    }

    // Método para comprobar si los datos están correctos al salir del formulario
    private void compruebaSalir() {
        // Variable que indica si hay algún dato escrito en el formulario
        Boolean hayDatos = false;
        // Comprueba cada campo y si alguno tiene datos, marca la variable como true
        if (!nieCajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!nombreCajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!apellido1CajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!apellido2CajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!direccionCajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!numeroCajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!cpCajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!poblacionCajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!paisCajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!emailCajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!provinciaCajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!telefonoCajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!nCuentaCajaTexto.getText().isEmpty()) {
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
                nuevosClientes.this.dispose();
            } else {
                // Si no confirma, simplemente cerramos el frame auxiliar
                frame.dispose();
            }
        } else {
            // Si no hay datos, cerramos directamente el formulario
            nuevosClientes.this.dispose();
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
        jLabelNcliente = new javax.swing.JLabel();
        jLabelNombre = new javax.swing.JLabel();
        jLabelApellido1 = new javax.swing.JLabel();
        jLabelApellido2 = new javax.swing.JLabel();
        jLabelDireccion = new javax.swing.JLabel();
        jLabelNumero = new javax.swing.JLabel();
        jLabelNie = new javax.swing.JLabel();
        idCajaTexto = new javax.swing.JTextField();
        nieCajaTexto = new javax.swing.JTextField();
        jRadioButtonFisica = new javax.swing.JRadioButton();
        jRadioButtonJuridica = new javax.swing.JRadioButton();
        nombreCajaTexto = new javax.swing.JTextField();
        apellido1CajaTexto = new javax.swing.JTextField();
        apellido2CajaTexto = new javax.swing.JTextField();
        direccionCajaTexto = new javax.swing.JTextField();
        numeroCajaTexto = new javax.swing.JTextField();
        jLabelPoblacion = new javax.swing.JLabel();
        jLabelCodigoPostal = new javax.swing.JLabel();
        jLabelProvincia = new javax.swing.JLabel();
        jLabelEmail = new javax.swing.JLabel();
        jLabelTelefono = new javax.swing.JLabel();
        jLabelPais = new javax.swing.JLabel();
        jLabelFormaPago = new javax.swing.JLabel();
        poblacionCajaTexto = new javax.swing.JTextField();
        provinciaCajaTexto = new javax.swing.JTextField();
        cpCajaTexto = new javax.swing.JTextField();
        paisCajaTexto = new javax.swing.JTextField();
        telefonoCajaTexto = new javax.swing.JTextField();
        emailCajaTexto = new javax.swing.JTextField();
        jComboBoxFP = new javax.swing.JComboBox<>();
        jLabelNcuenta = new javax.swing.JLabel();
        nCuentaCajaTexto = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        botonGuardar = new javax.swing.JButton();
        idFormaPagoCajaTexto = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ReplyCar - Nuevo Cliente");

        jLabelSalir.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelSalir.setForeground(new java.awt.Color(0, 0, 204));
        jLabelSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/salir.png"))); // NOI18N
        jLabelSalir.setText("Salir");
        jLabelSalir.setToolTipText("Salir del formulario");

        jLabelAyuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/ayuda.png"))); // NOI18N
        jLabelAyuda.setToolTipText("Consultar ayuda para este formulario");

        jLabelTitulo.setFont(new java.awt.Font("Tempus Sans ITC", 1, 36)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 204));
        jLabelTitulo.setText("Nuevo Cliente");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabelTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 504, Short.MAX_VALUE)
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

        jLabelNcliente.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelNcliente.setForeground(new java.awt.Color(0, 0, 204));
        jLabelNcliente.setText("(Nº cliente):");

        jLabelNombre.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelNombre.setForeground(new java.awt.Color(0, 0, 204));
        jLabelNombre.setText("(Nombre):");

        jLabelApellido1.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelApellido1.setForeground(new java.awt.Color(0, 0, 204));
        jLabelApellido1.setText("(Apellido 1):");

        jLabelApellido2.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelApellido2.setForeground(new java.awt.Color(0, 0, 204));
        jLabelApellido2.setText("(Apellido 2):");

        jLabelDireccion.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelDireccion.setForeground(new java.awt.Color(0, 0, 204));
        jLabelDireccion.setText("(Dirección):");

        jLabelNumero.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelNumero.setForeground(new java.awt.Color(0, 0, 204));
        jLabelNumero.setText("(Nº):");

        jLabelNie.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelNie.setForeground(new java.awt.Color(0, 0, 204));
        jLabelNie.setText("(NIF/DNI):");

        idCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        idCajaTexto.setToolTipText("Nº de cliente adjudicado");
        idCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNClienteKeyTyped(evt);
            }
        });

        nieCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        nieCajaTexto.setToolTipText("Ingresa nº de DNI o CIF del cliente");
        ((AbstractDocument) this.nieCajaTexto.getDocument()).setDocumentFilter(new maestros.UppercaseDocumentFilter());
        nieCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtnie(evt);
            }
        });

        jRadioButtonFisica.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jRadioButtonFisica.setForeground(new java.awt.Color(0, 0, 204));
        jRadioButtonFisica.setText("Persona Física");
        jRadioButtonFisica.setToolTipText("Selecciona si es una persona: \nformato XXXXXXXXA");

        jRadioButtonJuridica.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jRadioButtonJuridica.setForeground(new java.awt.Color(0, 0, 204));
        jRadioButtonJuridica.setText("Persona Jurídica");
        jRadioButtonJuridica.setToolTipText("Selecciona si es una empresa: \nformato BXXXXXXXX");

        nombreCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        nombreCajaTexto.setToolTipText("Ingresa nombre del cliente");
        ((AbstractDocument) this.nombreCajaTexto.getDocument()).setDocumentFilter(new maestros.UppercaseDocumentFilter());
        nombreCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nombreCajaTextoKeyTyped(evt);
            }
        });

        apellido1CajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        apellido1CajaTexto.setToolTipText("Ingresa apellido 1 del cliente");
        ((AbstractDocument) this.apellido1CajaTexto.getDocument()).setDocumentFilter(new maestros.UppercaseDocumentFilter());
        apellido1CajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                apellido1CajaTextoKeyTyped(evt);
            }
        });

        apellido2CajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        apellido2CajaTexto.setToolTipText("Ingresa apellido 2 del cliente");
        ((AbstractDocument) this.apellido2CajaTexto.getDocument()).setDocumentFilter(new maestros.UppercaseDocumentFilter());
        apellido2CajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                apellido2CajaTextoKeyTyped(evt);
            }
        });

        direccionCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        direccionCajaTexto.setToolTipText("Ingresa dirección del cliente");
        ((AbstractDocument) this.direccionCajaTexto.getDocument()).setDocumentFilter(new maestros.UppercaseDocumentFilter());
        direccionCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                direccionCajaTextoKeyTyped(evt);
            }
        });

        numeroCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        numeroCajaTexto.setToolTipText("Ingresa número del cliente");
        ((AbstractDocument) this.numeroCajaTexto.getDocument()).setDocumentFilter(new maestros.UppercaseDocumentFilter());
        numeroCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                numeroCajaTextoKeyTyped(evt);
            }
        });

        jLabelPoblacion.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelPoblacion.setForeground(new java.awt.Color(0, 0, 204));
        jLabelPoblacion.setText("(Población):");

        jLabelCodigoPostal.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelCodigoPostal.setForeground(new java.awt.Color(0, 0, 204));
        jLabelCodigoPostal.setText("(C.P.):");

        jLabelProvincia.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelProvincia.setForeground(new java.awt.Color(0, 0, 204));
        jLabelProvincia.setText("(Provincia):");

        jLabelEmail.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelEmail.setForeground(new java.awt.Color(0, 0, 204));
        jLabelEmail.setText("(E-mail):");

        jLabelTelefono.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelTelefono.setForeground(new java.awt.Color(0, 0, 204));
        jLabelTelefono.setText("(Teléfono):");

        jLabelPais.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelPais.setForeground(new java.awt.Color(0, 0, 204));
        jLabelPais.setText("(País):");

        jLabelFormaPago.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelFormaPago.setForeground(new java.awt.Color(0, 0, 204));
        jLabelFormaPago.setText("(Forma de Pago):");

        poblacionCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        poblacionCajaTexto.setToolTipText("Ingresa población del cliente");
        ((AbstractDocument) this.poblacionCajaTexto.getDocument()).setDocumentFilter(new maestros.UppercaseDocumentFilter());
        poblacionCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                poblacionCajaTextoKeyTyped(evt);
            }
        });

        provinciaCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        provinciaCajaTexto.setToolTipText("Ingresa provincia del cliente");
        ((AbstractDocument) this.provinciaCajaTexto.getDocument()).setDocumentFilter(new maestros.UppercaseDocumentFilter());
        provinciaCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                provinciaCajaTextoKeyTyped(evt);
            }
        });

        cpCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        cpCajaTexto.setToolTipText("Ingresa código postal del cliente");
        ((AbstractDocument) this.cpCajaTexto.getDocument()).setDocumentFilter(new maestros.UppercaseDocumentFilter());
        cpCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cpCajaTextoKeyTyped(evt);
            }
        });

        paisCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        paisCajaTexto.setToolTipText("Ingresa país del cliente");
        ((AbstractDocument) this.paisCajaTexto.getDocument()).setDocumentFilter(new maestros.UppercaseDocumentFilter());
        paisCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                paisCajaTextoKeyTyped(evt);
            }
        });

        telefonoCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        telefonoCajaTexto.setToolTipText("Ingresa teléfono del cliente");
        ((AbstractDocument) this.telefonoCajaTexto.getDocument()).setDocumentFilter(new maestros.UppercaseDocumentFilter());
        telefonoCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                telefonoCajaTextoKeyTyped(evt);
            }
        });

        emailCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        emailCajaTexto.setToolTipText("Ingresa e-mail del cliente, formato: (####@####.###)");
        ((AbstractDocument) this.emailCajaTexto.getDocument()).setDocumentFilter(new maestros.LowercaseDocumentFilter());
        emailCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                emailCajaTextoKeyTyped(evt);
            }
        });

        jComboBoxFP.setToolTipText("Selecciona forma de pago del cliente");

        jLabelNcuenta.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelNcuenta.setForeground(new java.awt.Color(0, 0, 204));
        jLabelNcuenta.setText("(Nº Cuenta):");
        jLabelNcuenta.setEnabled(false);

        nCuentaCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        nCuentaCajaTexto.setToolTipText("Ingresa número de cuenta del cliente");
        nCuentaCajaTexto.setEnabled(false);
        ((AbstractDocument) this.nCuentaCajaTexto.getDocument()).setDocumentFilter(new maestros.UppercaseDocumentFilter());
        nCuentaCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nCuentaCajaTextoKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelNie)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nieCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButtonFisica, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButtonJuridica, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelPais)
                            .addComponent(jLabelCodigoPostal))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cpCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelPoblacion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(poblacionCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelProvincia))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(paisCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelEmail)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(emailCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelTelefono)
                                .addGap(9, 9, 9)
                                .addComponent(telefonoCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(provinciaCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabelNcliente)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(idCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabelDireccion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(direccionCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelNumero)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(numeroCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabelNombre)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nombreCajaTexto))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabelApellido1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(apellido1CajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelApellido2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(apellido2CajaTexto))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabelFormaPago)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxFP, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jLabelNcuenta)
                        .addGap(18, 18, 18)
                        .addComponent(nCuentaCajaTexto)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNie)
                    .addComponent(nieCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButtonFisica)
                    .addComponent(jRadioButtonJuridica)
                    .addComponent(jLabelNcliente)
                    .addComponent(idCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNombre)
                    .addComponent(nombreCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelApellido1)
                    .addComponent(apellido1CajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelApellido2)
                    .addComponent(apellido2CajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDireccion)
                    .addComponent(direccionCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNumero)
                    .addComponent(numeroCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCodigoPostal)
                    .addComponent(cpCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPoblacion)
                    .addComponent(poblacionCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelProvincia)
                    .addComponent(provinciaCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelPais)
                    .addComponent(paisCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(telefonoCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTelefono)
                    .addComponent(jLabelEmail)
                    .addComponent(emailCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelNcuenta)
                        .addComponent(nCuentaCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelFormaPago)
                        .addComponent(jComboBoxFP, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(23, 23, 23))
        );

        botonGuardar.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        botonGuardar.setForeground(new java.awt.Color(0, 0, 204));
        botonGuardar.setText("Guardar Nuevo Cliente");
        botonGuardar.setToolTipText("Pincha para guardar el nuevo cliente");

        idFormaPagoCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        idFormaPagoCajaTexto.setEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(idFormaPagoCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 627, Short.MAX_VALUE)
                .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idFormaPagoCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtnie(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnie
        // Restablece el color de fondo a blanco al escribir
        nieCajaTexto.setBackground(Color.WHITE);
        // Si el NIE supera 8 caracteres, no permite seguir escribiendo
        if (nieCajaTexto.getText().length() > 8) {
            evt.consume();
        }
    }//GEN-LAST:event_txtnie

    private void txtNClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNClienteKeyTyped
        // Restablece el fondo del campo
        idCajaTexto.setBackground(Color.WHITE);
        // Si el ID supera los 4 caracteres, no permite seguir escribiendoo
        if (idCajaTexto.getText().length() > 4) {
            evt.consume();
        }
    }//GEN-LAST:event_txtNClienteKeyTyped

    private void nombreCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombreCajaTextoKeyTyped
        // Limpia el color
        nombreCajaTexto.setBackground(Color.WHITE);
        // Longitud máxima permitida: 50 caracteres
        if (nombreCajaTexto.getText().length() > 50) {
            evt.consume();
        }
    }//GEN-LAST:event_nombreCajaTextoKeyTyped

    private void apellido1CajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_apellido1CajaTextoKeyTyped
        // Limpia el color
        apellido1CajaTexto.setBackground(Color.WHITE);
        // Máximo 30 caracteres para primer apellido
        if (apellido1CajaTexto.getText().length() > 30) {
            evt.consume();
        }
    }//GEN-LAST:event_apellido1CajaTextoKeyTyped

    private void apellido2CajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_apellido2CajaTextoKeyTyped
        // Limpia el color
        apellido2CajaTexto.setBackground(Color.WHITE);
        // Máximo 30 caracteres para segundo apellido
        if (apellido2CajaTexto.getText().length() > 30) {
            evt.consume();
        }
    }//GEN-LAST:event_apellido2CajaTextoKeyTyped

    private void direccionCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_direccionCajaTextoKeyTyped
        // Limpia el color
        direccionCajaTexto.setBackground(Color.WHITE);
        // Máximo 50 caracteres para la dirección
        if (direccionCajaTexto.getText().length() > 50) {
            evt.consume();
        }
    }//GEN-LAST:event_direccionCajaTextoKeyTyped

    private void numeroCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numeroCajaTextoKeyTyped
        // Limpia el color
        numeroCajaTexto.setBackground(Color.WHITE);
        // Máximo 2 caracteres → números de portal muy cortos
        if (numeroCajaTexto.getText().length() > 2) {
            evt.consume();
        }
    }//GEN-LAST:event_numeroCajaTextoKeyTyped

    private void cpCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cpCajaTextoKeyTyped
        // Limpia el color
        cpCajaTexto.setBackground(Color.WHITE);
        // Máximo 4 caracteres
        if (cpCajaTexto.getText().length() > 4) {
            evt.consume();
        }
    }//GEN-LAST:event_cpCajaTextoKeyTyped

    private void poblacionCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_poblacionCajaTextoKeyTyped
        // Limpia el color
        poblacionCajaTexto.setBackground(Color.WHITE);
        // Máximo 25 caracteres para la población
        if (poblacionCajaTexto.getText().length() > 25) {
            evt.consume();
        }
    }//GEN-LAST:event_poblacionCajaTextoKeyTyped

    private void provinciaCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_provinciaCajaTextoKeyTyped
        // Limpia el color
        provinciaCajaTexto.setBackground(Color.WHITE);
        // Máximo 25 caracteres para la provincia
        if (provinciaCajaTexto.getText().length() > 25) {
            evt.consume();
        }
    }//GEN-LAST:event_provinciaCajaTextoKeyTyped

    private void paisCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paisCajaTextoKeyTyped
        // Limpia el color
        paisCajaTexto.setBackground(Color.WHITE);
        // Máximo 25 caracteres para el país
        if (paisCajaTexto.getText().length() > 25) {
            evt.consume();
        }
    }//GEN-LAST:event_paisCajaTextoKeyTyped

    private void emailCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_emailCajaTextoKeyTyped
        // Limpia el color
        emailCajaTexto.setBackground(Color.WHITE);
        // Máximo 50 caracteres para el email
        if (emailCajaTexto.getText().length() > 50) {
            evt.consume();
        }
    }//GEN-LAST:event_emailCajaTextoKeyTyped

    private void telefonoCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_telefonoCajaTextoKeyTyped
        // Limpia el color
        telefonoCajaTexto.setBackground(Color.WHITE);
        // Máximo 8 caracteres para el teléfono
        if (telefonoCajaTexto.getText().length() > 8) {
            evt.consume();
        }
    }//GEN-LAST:event_telefonoCajaTextoKeyTyped

    private void nCuentaCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nCuentaCajaTextoKeyTyped
        // Limpia el color
        nCuentaCajaTexto.setBackground(Color.WHITE);
        // Máximo 8 caracteres para el teléfono
        if (nCuentaCajaTexto.getText().length() > 50) {
            evt.consume();
        }
    }//GEN-LAST:event_nCuentaCajaTextoKeyTyped

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
            java.util.logging.Logger.getLogger(nuevosClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(nuevosClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(nuevosClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(nuevosClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                nuevosClientes dialog = new nuevosClientes(new javax.swing.JFrame(), false);
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
    private javax.swing.JTextField apellido1CajaTexto;
    private javax.swing.JTextField apellido2CajaTexto;
    private javax.swing.JButton botonGuardar;
    private javax.swing.JTextField cpCajaTexto;
    private javax.swing.JTextField direccionCajaTexto;
    private javax.swing.JTextField emailCajaTexto;
    private javax.swing.JTextField idCajaTexto;
    private javax.swing.JTextField idFormaPagoCajaTexto;
    private javax.swing.JComboBox<String> jComboBoxFP;
    private javax.swing.JLabel jLabelApellido1;
    private javax.swing.JLabel jLabelApellido2;
    private javax.swing.JLabel jLabelAyuda;
    private javax.swing.JLabel jLabelCodigoPostal;
    private javax.swing.JLabel jLabelDireccion;
    private javax.swing.JLabel jLabelEmail;
    private javax.swing.JLabel jLabelFormaPago;
    private javax.swing.JLabel jLabelNcliente;
    private javax.swing.JLabel jLabelNcuenta;
    private javax.swing.JLabel jLabelNie;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JLabel jLabelNumero;
    private javax.swing.JLabel jLabelPais;
    private javax.swing.JLabel jLabelPoblacion;
    private javax.swing.JLabel jLabelProvincia;
    private javax.swing.JLabel jLabelSalir;
    private javax.swing.JLabel jLabelTelefono;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadioButtonFisica;
    private javax.swing.JRadioButton jRadioButtonJuridica;
    private javax.swing.JTextField nCuentaCajaTexto;
    private javax.swing.JTextField nieCajaTexto;
    private javax.swing.JTextField nombreCajaTexto;
    private javax.swing.JTextField numeroCajaTexto;
    private javax.swing.JTextField paisCajaTexto;
    private javax.swing.JTextField poblacionCajaTexto;
    private javax.swing.JTextField provinciaCajaTexto;
    private javax.swing.JTextField telefonoCajaTexto;
    // End of variables declaration//GEN-END:variables
}
