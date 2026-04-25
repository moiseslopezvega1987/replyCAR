package app;

/*Clases importadas para la utilización en la clase.
 */
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.AbstractDocument;

/**
 * Clase para la venta de reparaciones de taller a nuestra APP
 *
 * @author Moisés López Vega
 */
public class ventaTaller extends javax.swing.JDialog {

    // Declaramos el objeto VehiculoDAO para manejar operaciones de base de datos relacionadas con vehículos.
    private VehiculoDAO vehiculoDAO;
    // Declaramos el objeto ClienteDAO para manejar operaciones de base de datos relacionadas con clientes.
    private ClienteDAO clienteDAO;
    // Objeto Facturas de taller que representa la factura de taller actual en el formulario
    private facturasTaller facturasTaller;
    // Declaramos el objeto FacturasDAO para manejar operaciones de base de datos relacionadas con las facturas.
    private FacturasDAO facturasDAO;
    // Declaramos el objeto AveriasDAO para manejar operaciones de base de datos relacionadas con las averías.
    private AveriasDAO averiasDAO;
    // Declaramos una variable donde se almacenan las intervenciones realizas en el taller
    List<intervenciones> intervenciones = new ArrayList<>();
    double total = 0;
    //Configuramos el formateador de moneda para euros.
    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.of("es", "ES"));
    //Configuramos NumberFormat para reconocer los formatos de número.
    NumberFormat formatearNumero = NumberFormat.getInstance(Locale.getDefault());

    /**
     * Creamos el formulario para ventaTaller Constructor de la clase
     * ventaTaller
     *
     * @param parent el componente padre.
     * @param modal indica si el cuadro de diálogo es modal.
     * @throws SQLException excepción que puede lanzarse durante la creación del
     * objeto.
     */
    public ventaTaller(java.awt.Frame parent, boolean modal) throws SQLException {
        //Llamamos al constructor de la superclase JDialog
        super(parent, modal);
        // Inicializa componentes del formulario
        initComponents();
        // Inicializamos DAOs
        clienteDAO = new ClienteDAO();
        vehiculoDAO = new VehiculoDAO();
        facturasDAO = new FacturasDAO();
        averiasDAO = new AveriasDAO();
        // Creamos una factura de taller vacía
        facturasTaller = new facturasTaller();
        // Oculta la caja de texto para el número del cliente
        nClienteCajaTexto.setVisible(false);
        // Llama al método que carga la lista de clientes en un comboBox
        cargarColumnaClientes();
        // Llama al método que carga la lista de averías en un comboBox
        cargarAverias();
        // Llama al método que establece ciertos campos (cajas de texto) que no pueden ser editadas
        noEditables();
        // Llama al método que establece ciertos campos (cajas de texto) que no pueden ser utilizables
        noUtilizables();
        // Establecer cajas de texto no visibles
        id_tarifa1CajaTexto.setVisible(false);
        id_tarifa2CajaTexto.setVisible(false);
        id_tarifa3CajaTexto.setVisible(false);
        id_tarifa4CajaTexto.setVisible(false);

        // Acción que ocurre al seleccionar un diagnóstico en el comboBox
        jComboBoxDiagnostico1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    // Llama al método que maneja la selección del diagnóstico
                    diagnostico1SeleccionadoActionPerformed(evt);
                    // Llama al método para calcular los importes de los precios
                    calcularImportes();
                    // Resetear averías
                    resetAverias();
                    // Manejo de excepciones de SQL en caso de error de BD
                } catch (SQLException ex) {
                    Logger.getLogger(nuevosArticulos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Acción que ocurre al seleccionar un diagnóstico en el comboBox
        jComboBoxDiagnostico2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    // Llama al método que maneja la selección del diagnóstico
                    diagnostico2SeleccionadoActionPerformed(evt);
                    // Llama al método para calcular los importes de los precios
                    calcularImportes();
                    // Resetear averías
                    resetAverias();
                    // Manejo de excepciones de SQL en caso de error de BD
                } catch (SQLException ex) {
                    Logger.getLogger(nuevosArticulos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Acción que ocurre al seleccionar un diagnóstico en el comboBox
        jComboBoxDiagnostico3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    // Llama al método que maneja la selección del diagnóstico
                    diagnostico3SeleccionadoActionPerformed(evt);
                    // Llama al método para calcular los importes de los precios
                    calcularImportes();
                    // Resetear averías
                    resetAverias();
                    // Manejo de excepciones de SQL en caso de error de BD
                } catch (SQLException ex) {
                    Logger.getLogger(nuevosArticulos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Acción que ocurre al seleccionar un diagnóstico en el comboBox
        jComboBoxDiagnostico4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    // Llama al método que maneja la selección del diagnóstico
                    diagnostico4SeleccionadoActionPerformed(evt);
                    // Llama al método para calcular los importes de los precios
                    calcularImportes();
                    // Resetear averías
                    resetAverias();
                    // Manejo de excepciones de SQL en caso de error de BD
                } catch (SQLException ex) {
                    Logger.getLogger(nuevosArticulos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Establecer los CheckBox no utilizables en su inicialización
        jCheckBox1.addItemListener(e -> noUtilizables());
        jCheckBox2.addItemListener(e -> noUtilizables());
        jCheckBox3.addItemListener(e -> noUtilizables());
        jCheckBox4.addItemListener(e -> noUtilizables());
        // Establecer los CheckBox al click que calcule los importes
        jCheckBox1.addActionListener(evt -> calcularImportes());
        jCheckBox2.addActionListener(evt -> calcularImportes());
        jCheckBox3.addActionListener(evt -> calcularImportes());
        jCheckBox4.addActionListener(evt -> calcularImportes());

        // Acción para realizar la busqueda de la matrícula
        jButtonBuscar.addActionListener(evt -> {
            try {
                buscarMatriculaVehiculoActionPerformed(evt);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al buscar la matrícula: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Acción que ocurre al seleccionar un cliente en el comboBox
        jComboBoxCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    //Llama al método que maneja la selección del cliente
                    clienteSeleccionadoActionPerformed(evt);
                    // Manejo de excepciones de SQL en caso de error de BD
                } catch (SQLException ex) {
                    Logger.getLogger(nuevosArticulos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Acción que ocurre al presionar el botón de guardar la intervención.
        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    // Llama al método que guarda la intervención en la base de datos.
                    facturarIntervencionActionPerformed(evt);
                    // Manejo de excepciones de SQL en caso de error de BD
                } catch (SQLException | ParseException ex) {
                    Logger.getLogger(ventaTaller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Agregar MouseListener a jLabel1 para cerrar la ventana y cambiar el cursor
        maestros.salirFormulario(jLabelSalir, this::compruebaSalir);

        // Agregar MouseListener a jLabelAyuda para abrir la ayuda de la app
        maestros.consultarAyuda(jLabelAyuda, "facturar_intervencion");

        // Seleccionamos el primer diagnóstico en jComboBox después de cargar todos los datos
        if (jComboBoxDiagnostico1.getItemCount() > 0) {
            jComboBoxDiagnostico1.setSelectedIndex(0);
            try {
                diagnostico1SeleccionadoActionPerformed(new ActionEvent(jComboBoxDiagnostico1, ActionEvent.ACTION_PERFORMED, null));
            } catch (SQLException ex) {
                Logger.getLogger(ventaTaller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Seleccionamos el primer cliente en jComboBox después de cargar los clientes
        if (jComboBoxCliente.getItemCount() > 0) {
            jComboBoxCliente.setSelectedIndex(0);
            try {
                clienteSeleccionadoActionPerformed(new ActionEvent(jComboBoxCliente, ActionEvent.ACTION_PERFORMED, null));
            } catch (SQLException ex) {
                Logger.getLogger(ventaTaller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // Método para resetear averías
    private void resetAverias() {
        if (jComboBoxDiagnostico1.getSelectedItem() == "") {
            resumen1CajaTexto.setText("");
            String precio1 = formatoMoneda.format(0.00);
            precio1CajaTexto.setText(precio1);
        } else if (jComboBoxDiagnostico2.getSelectedItem() == "") {
            resumen2CajaTexto.setText("");
            String precio2 = formatoMoneda.format(0.00);
            precio2CajaTexto.setText(precio2);
        } else if (jComboBoxDiagnostico3.getSelectedItem() == "") {
            resumen3CajaTexto.setText("");
            String precio3 = formatoMoneda.format(0.00);
            precio3CajaTexto.setText(precio3);
        } else if (jComboBoxDiagnostico4.getSelectedItem() == "") {
            resumen4CajaTexto.setText("");
            String precio4 = formatoMoneda.format(0.00);
            precio4CajaTexto.setText(precio4);
        }
    }

    // Método para cargar las averías de taller en jComboBox
    private void cargarAverias() {
        String columna = "tipo_averia";
        jComboBoxDiagnostico1.addItem("");
        jComboBoxDiagnostico2.addItem("");
        jComboBoxDiagnostico3.addItem("");
        jComboBoxDiagnostico4.addItem("");
        //Obtenemos la lista de bastidores de la base de datos
        List<String> var = averiasDAO.obtenerColumna(columna);
        for (String averia : var) {
            //Agregamos cada bastidor al jComboBox
            jComboBoxDiagnostico1.addItem(averia);
            jComboBoxDiagnostico2.addItem(averia);
            jComboBoxDiagnostico3.addItem(averia);
            jComboBoxDiagnostico4.addItem(averia);
        }
    }

    // Método para hacer campos que no sean editables.
    private void noEditables() {
        BastidorCajaTexto.setEditable(false);
        ModeloCajaTexto.setEditable(false);
        resumen1CajaTexto.setEditable(false);
        precio1CajaTexto.setEditable(false);
        String precio1 = formatoMoneda.format(0.00);
        precio1CajaTexto.setText(precio1);
        resumen2CajaTexto.setEditable(false);
        precio2CajaTexto.setEditable(false);
        String precio2 = formatoMoneda.format(0.00);
        precio2CajaTexto.setText(precio2);
        resumen3CajaTexto.setEditable(false);
        precio3CajaTexto.setEditable(false);
        String precio3 = formatoMoneda.format(0.00);
        precio3CajaTexto.setText(precio3);
        resumen4CajaTexto.setEditable(false);
        precio4CajaTexto.setEditable(false);
        String precio4 = formatoMoneda.format(0.00);
        precio4CajaTexto.setText(precio4);
        baseImponibleCajaTexto.setEditable(false);
        String base_imponible = formatoMoneda.format(0.00);
        baseImponibleCajaTexto.setText(base_imponible);
        ivaCajaTexto.setEditable(false);
        totalFacturaCajaTexto.setEditable(false);
    }

    // Método para hacer a algunos componentes no editables
    private void noUtilizables() {
        if (jCheckBox1.isSelected()) {
            jComboBoxDiagnostico1.setEnabled(true);
            resumen1CajaTexto.setEnabled(true);
            precio1CajaTexto.setEnabled(true);
        } else {
            jComboBoxDiagnostico1.setEnabled(false);
            resumen1CajaTexto.setEnabled(false);
            precio1CajaTexto.setEnabled(false);
        }
        if (jCheckBox2.isSelected()) {
            jComboBoxDiagnostico2.setEnabled(true);
            resumen2CajaTexto.setEnabled(true);
            precio2CajaTexto.setEnabled(true);
        } else {
            jComboBoxDiagnostico2.setEnabled(false);
            resumen2CajaTexto.setEnabled(false);
            precio2CajaTexto.setEnabled(false);
        }
        if (jCheckBox3.isSelected()) {
            jComboBoxDiagnostico3.setEnabled(true);
            resumen3CajaTexto.setEnabled(true);
            precio3CajaTexto.setEnabled(true);
        } else {
            jComboBoxDiagnostico3.setEnabled(false);
            resumen3CajaTexto.setEnabled(false);
            precio3CajaTexto.setEnabled(false);
        }
        if (jCheckBox4.isSelected()) {
            jComboBoxDiagnostico4.setEnabled(true);
            resumen4CajaTexto.setEnabled(true);
            precio4CajaTexto.setEnabled(true);
        } else {
            jComboBoxDiagnostico4.setEnabled(false);
            resumen4CajaTexto.setEnabled(false);
            precio4CajaTexto.setEnabled(false);
        }
    }

    // Método para calcular los importes del IVA y total de la factura
    private void calcularImportes() {
        double importeIva;
        double importeTotalFactura;
        double bi = 0;
        // Captura error:
        try {
            // Usa NumberFormat para interpretar el formato de la base imponible con coma decimal y punto de mil
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            if (jCheckBox1.isSelected()) {
                Number number1 = format.parse(precio1CajaTexto.getText());
                bi += number1.doubleValue();
            }
            if (jCheckBox2.isSelected()) {
                Number number2 = format.parse(precio2CajaTexto.getText());
                bi += number2.doubleValue();
            }
            if (jCheckBox3.isSelected()) {
                Number number3 = format.parse(precio3CajaTexto.getText());
                bi += number3.doubleValue();
            }
            if (jCheckBox4.isSelected()) {
                Number number4 = format.parse(precio4CajaTexto.getText());
                bi += number4.doubleValue();
            }
            // Calculamos importes:
            importeIva = (bi * 1.21) - bi;
            importeTotalFactura = bi * 1.21;

            String base_imponible = formatoMoneda.format(bi);
            baseImponibleCajaTexto.setText(base_imponible);
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

    // Método para cargar nombres de clientes en jComboBoxCliente desde la base de datos
    private void cargarColumnaClientes() {
        List<String> clientes = clienteDAO.obtenerColumnas("nombre", "apellido_1", "apellido_2");
        for (String cliente : clientes) {
            jComboBoxCliente.addItem(cliente);
        }
    }

    // Acción al seleccionar un Cliente en jComboBoxCliente.
    private void clienteSeleccionadoActionPerformed(ActionEvent evt) throws SQLException {
        String clienteSeleccionado = (String) jComboBoxCliente.getSelectedItem();
        if (clienteSeleccionado != null) {
            cliente cli = clienteDAO.obtenerClienteSegunNombre(clienteSeleccionado);
            if (cli != null) {
                nClienteCajaTexto.setText(String.valueOf(cli.getId()));
            }
        }
    }

    // Acción al seleccionar el diagnostico 1, nos rellena todas sus caracteristicas
    private void diagnostico1SeleccionadoActionPerformed(ActionEvent evt) throws SQLException {
        String diagnosticoSeleccionado = (String) jComboBoxDiagnostico1.getSelectedItem();
        jComboBoxDiagnostico1.setBackground(Color.WHITE);
        if (diagnosticoSeleccionado != null) {
            averias ave = averiasDAO.obtenerTarifaTallerPorTipoAveria(diagnosticoSeleccionado);
            if (ave != null) {
                resumen1CajaTexto.setText(ave.getTipo());
                precio1CajaTexto.setText(String.format("%.2f", ave.getPrecio()));
                id_tarifa1CajaTexto.setText(String.valueOf(ave.getId()));
            }
        }
    }

    // Acción al seleccionar el diagnostico 2, nos rellena todas sus caracteristicas
    private void diagnostico2SeleccionadoActionPerformed(ActionEvent evt) throws SQLException {
        String diagnosticoSeleccionado = (String) jComboBoxDiagnostico2.getSelectedItem();
        jComboBoxDiagnostico2.setBackground(Color.WHITE);
        if (diagnosticoSeleccionado != null) {
            averias ave = averiasDAO.obtenerTarifaTallerPorTipoAveria(diagnosticoSeleccionado);
            if (ave != null) {
                resumen2CajaTexto.setText(ave.getTipo());
                precio2CajaTexto.setText(String.format("%.2f", ave.getPrecio()));
                id_tarifa2CajaTexto.setText(String.valueOf(ave.getId()));
            }
        }
    }

    // Acción al seleccionar el diagnostico 3, nos rellena todas sus caracteristicas
    private void diagnostico3SeleccionadoActionPerformed(ActionEvent evt) throws SQLException {
        String diagnosticoSeleccionado = (String) jComboBoxDiagnostico3.getSelectedItem();
        jComboBoxDiagnostico3.setBackground(Color.WHITE);
        if (diagnosticoSeleccionado != null) {
            averias ave = averiasDAO.obtenerTarifaTallerPorTipoAveria(diagnosticoSeleccionado);
            if (ave != null) {
                resumen3CajaTexto.setText(ave.getTipo());
                precio3CajaTexto.setText(String.format("%.2f", ave.getPrecio()));
                id_tarifa3CajaTexto.setText(String.valueOf(ave.getId()));
            }
        }
    }

    // Acción al seleccionar el diagnostico 4, nos rellena todas sus caracteristicas
    private void diagnostico4SeleccionadoActionPerformed(ActionEvent evt) throws SQLException {
        String diagnosticoSeleccionado = (String) jComboBoxDiagnostico4.getSelectedItem();
        jComboBoxDiagnostico4.setBackground(Color.WHITE);
        if (diagnosticoSeleccionado != null) {
            averias ave = averiasDAO.obtenerTarifaTallerPorTipoAveria(diagnosticoSeleccionado);
            if (ave != null) {
                resumen4CajaTexto.setText(ave.getTipo());
                precio4CajaTexto.setText(String.format("%.2f", ave.getPrecio()));
                id_tarifa4CajaTexto.setText(String.valueOf(ave.getId()));
            }
        }
    }

    // Método para buscar vehículo por matrícula
    private void buscarMatriculaVehiculoActionPerformed(ActionEvent evt) throws SQLException {
        String matriculaSeleccionada = (String) MatriculaCajaTexto.getText();
        if (matriculaSeleccionada != null) {
            vehiculo veh = vehiculoDAO.obtenerVehiculoPorMatricula(matriculaSeleccionada);
            if (veh != null) {
                BastidorCajaTexto.setText(veh.getBastidor());
                ModeloCajaTexto.setText(veh.getModelo());
                BastidorCajaTexto.setBackground(Color.WHITE);
                ModeloCajaTexto.setBackground(Color.WHITE);
            } else {
                JOptionPane.showMessageDialog(null, "Matrícula inexistente en la base de datos", "Información", JOptionPane.INFORMATION_MESSAGE);
                BastidorCajaTexto.setText("");
                ModeloCajaTexto.setText("");
                MatriculaCajaTexto.setBackground(Color.RED);
                BastidorCajaTexto.setBackground(Color.RED);
                ModeloCajaTexto.setBackground(Color.RED);
            }
        }
    }

    // Acción para guardar los datos de un vehículo pulsando el botón guardar vehículo
    private void facturarIntervencionActionPerformed(ActionEvent evt) throws SQLException, ParseException {
        try {
            if (!validarCampos()) {
                JOptionPane.showMessageDialog(this, "Revise los campos marcados en rojo");
                return;
            }
            // Crear Factura
            String numeroFactura = facturasDAO.generarNumeroFacturaTaller();
            facturasTaller.setNumero_factura(numeroFactura);
            facturasTaller.setFecha_factura(LocalDate.now());
            facturasTaller.setId_cliente(Integer.parseInt(nClienteCajaTexto.getText()));
            facturasTaller.setBaseImponible(parsearDouble(baseImponibleCajaTexto.getText()));
            facturasTaller.setIva(parsearDouble(ivaCajaTexto.getText()));
            facturasTaller.setImporteFactura(parsearDouble(totalFacturaCajaTexto.getText()));
            // Crear intervenciones
            List<intervenciones> lista = asignarDiagnosticos(numeroFactura);
            if (lista.isEmpty()) {
                return;
            }
            // Guardar en la base de datos
            facturasDAO.guardarFacturaTaller(facturasTaller);

            for (intervenciones i : lista) {
                facturasDAO.guardarIntervencion(i);
            }

            JOptionPane.showMessageDialog(this, "Factura guardada correctamente");
            resetearFormulario();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Método para validar campos
    private boolean validarCampos() {
        boolean esValido = true;
        // Validar matrícula
        if (MatriculaCajaTexto.getText().trim().isEmpty()) {
            MatriculaCajaTexto.setBackground(Color.RED);
            esValido = false;
        } else {
            MatriculaCajaTexto.setBackground(Color.WHITE);
        }
        // Validar bastidor
        if (BastidorCajaTexto.getText().trim().isEmpty()) {
            BastidorCajaTexto.setBackground(Color.RED);
            esValido = false;
        } else {
            BastidorCajaTexto.setBackground(Color.WHITE);
        }
        // Validar modelo
        if (ModeloCajaTexto.getText().trim().isEmpty()) {
            ModeloCajaTexto.setBackground(Color.RED);
            esValido = false;
        } else {
            ModeloCajaTexto.setBackground(Color.WHITE);
        }
        return esValido;
    }

    // Método para validar tipo Double
    private boolean validarDouble(String texto) {
        try {
            Double.parseDouble(texto);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Método para parsear Double
    private double parsearDouble(String texto) throws ParseException {
        if (texto.trim().isEmpty()) {
            return 0.0;
        }
        return formatearNumero.parse(texto).doubleValue();
    }

    // Método para asignar diagnósticos
    private List<intervenciones> asignarDiagnosticos(String numeroFactura) throws ParseException {
        List<intervenciones> lista = new ArrayList<>();
        boolean validos = true;

        String bastidor = BastidorCajaTexto.getText().trim();
        String modelo = ModeloCajaTexto.getText().trim();
        String matricula = MatriculaCajaTexto.getText().trim();
        int idCliente = Integer.parseInt(nClienteCajaTexto.getText().trim());

        if (jCheckBox1.isSelected()) {
            if (validarCombo(jComboBoxDiagnostico1, "Diagnóstico 1")) {
                lista.add(new intervenciones(
                        numeroFactura, bastidor, modelo, matricula,
                        jComboBoxDiagnostico1.getSelectedItem().toString(),
                        resumen1CajaTexto.getText().trim(),
                        parsearDouble(precio1CajaTexto.getText().trim()),
                        idCliente,
                        Integer.parseInt(id_tarifa1CajaTexto.getText().trim())
                ));
            } else {
                validos = false;
            }
        }

        if (jCheckBox2.isSelected()) {
            if (validarCombo(jComboBoxDiagnostico2, "Diagnóstico 2")) {
                lista.add(new intervenciones(
                        numeroFactura, bastidor, modelo, matricula,
                        jComboBoxDiagnostico2.getSelectedItem().toString(),
                        resumen2CajaTexto.getText().trim(),
                        parsearDouble(precio2CajaTexto.getText().trim()),
                        idCliente,
                        Integer.parseInt(id_tarifa2CajaTexto.getText().trim())
                ));
            } else {
                validos = false;
            }
        }

        if (jCheckBox3.isSelected()) {
            if (validarCombo(jComboBoxDiagnostico3, "Diagnóstico 3")) {
                lista.add(new intervenciones(
                        numeroFactura, bastidor, modelo, matricula,
                        jComboBoxDiagnostico3.getSelectedItem().toString(),
                        resumen3CajaTexto.getText().trim(),
                        parsearDouble(precio3CajaTexto.getText().trim()),
                        idCliente,
                        Integer.parseInt(id_tarifa3CajaTexto.getText().trim())
                ));
            } else {
                validos = false;
            }
        }

        if (jCheckBox4.isSelected()) {
            if (validarCombo(jComboBoxDiagnostico4, "Diagnóstico 4")) {
                lista.add(new intervenciones(
                        numeroFactura, bastidor, modelo, matricula,
                        jComboBoxDiagnostico4.getSelectedItem().toString(),
                        resumen4CajaTexto.getText().trim(),
                        parsearDouble(precio4CajaTexto.getText().trim()),
                        idCliente,
                        Integer.parseInt(id_tarifa4CajaTexto.getText().trim())
                ));
            } else {
                validos = false;
            }
        }

        if (lista.isEmpty() || !validos) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar al menos un diagnóstico válido", "Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>(); // retorna lista vacía en vez de lanzar excepción
        }

        return lista;
    }

    // Método para validar jComboBox
    private boolean validarCombo(JComboBox<?> combo, String nombre) {
        if (combo.getSelectedIndex() == 0) {
            combo.setBackground(Color.RED);
            JOptionPane.showMessageDialog(this, nombre + " no seleccionado", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            combo.setBackground(Color.WHITE);
            return true;
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

    // Método para limpiar los campos del formulario.
    private void resetearFormulario() {
        //Restablece los campos de texto a vacío para eliminar cualquier dato ingresado
        this.baseImponibleCajaTexto.setText("");
        this.MatriculaCajaTexto.setText("");
        this.BastidorCajaTexto.setText("");
        this.ModeloCajaTexto.setText("");
        this.jCheckBox1.setSelected(false);
        this.jCheckBox2.setSelected(false);
        this.jCheckBox3.setSelected(false);
        this.jCheckBox4.setSelected(false);
        this.resumen1CajaTexto.setText("");
        this.resumen2CajaTexto.setText("");
        this.resumen3CajaTexto.setText("");
        this.resumen4CajaTexto.setText("");
        this.id_tarifa1CajaTexto.setText("0");
        this.id_tarifa2CajaTexto.setText("0");
        this.id_tarifa3CajaTexto.setText("0");
        this.id_tarifa4CajaTexto.setText("0");
        String precio1 = formatoMoneda.format(0.00);
        this.precio1CajaTexto.setText(precio1);
        String precio2 = formatoMoneda.format(0.00);
        this.precio2CajaTexto.setText(precio2);
        String precio3 = formatoMoneda.format(0.00);
        this.precio3CajaTexto.setText(precio3);
        String precio4 = formatoMoneda.format(0.00);
        this.precio4CajaTexto.setText(precio4);
        this.jComboBoxDiagnostico1.setSelectedIndex(0);
        this.jComboBoxDiagnostico2.setSelectedIndex(0);
        this.jComboBoxDiagnostico3.setSelectedIndex(0);
        this.jComboBoxDiagnostico4.setSelectedIndex(0);
        this.jComboBoxCliente.setSelectedIndex(0);
    }

    // Método para cerrar el formulario cuando el vehículo ha sido facturado
    private void cerrarFormulario() {
        ventaTaller.this.dispose();
    }

    // Método para verificar si hay datos en el formulario antes de cerrar
    private void compruebaSalir() {
        // Variable para rastrear si hay datos ingresados
        Boolean hayDatos = false;
        // Verifica si el campo de precio de venta contiene texto
        if (!MatriculaCajaTexto.getText().isEmpty()) {
            // Marca hayDatos como true si el campo no está vacío
            hayDatos = true;
        }
        if (jCheckBox1.isSelected() || jCheckBox2.isSelected() || jCheckBox3.isSelected() || jCheckBox4.isSelected()) {
            // Marca hayDatos como true si el campo no está vacío
            hayDatos = true;
        }
        // Si hay datos, muestra un diálogo de confirmación
        if (hayDatos) {
            JFrame frame = new JFrame("Ey, no tan rápido");
            String mensaje = "¿Realmente quiere salir?, Hay datos introducidos.";
            String titulo = "Confirmar Salida";
            int opcion = JOptionPane.showConfirmDialog(frame, mensaje, titulo, JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                // Cierra el formulario si el usuario confirma
                ventaTaller.this.dispose();
            } else {
                // Cierra solo el diálogo si el usuario cancela
                frame.dispose();
            }
        } else {
            // Si no hay datos, cierra directamente el formulario sin confirmación
            ventaTaller.this.dispose();
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
        BastidorCajaTexto = new javax.swing.JTextField();
        jLabelIVA = new javax.swing.JLabel();
        jLabelBaseImponible = new javax.swing.JLabel();
        jLabelTotalFactura = new javax.swing.JLabel();
        ivaCajaTexto = new javax.swing.JTextField();
        totalFacturaCajaTexto = new javax.swing.JTextField();
        baseImponibleCajaTexto = new javax.swing.JTextField();
        jComboBoxCliente = new javax.swing.JComboBox<>();
        jLabelColor = new javax.swing.JLabel();
        resumen1CajaTexto = new javax.swing.JTextField();
        jLabelExtras = new javax.swing.JLabel();
        precio1CajaTexto = new javax.swing.JTextField();
        nClienteCajaTexto = new javax.swing.JTextField();
        jLabelMatricula = new javax.swing.JLabel();
        MatriculaCajaTexto = new javax.swing.JTextField();
        jButtonBuscar = new javax.swing.JButton();
        ModeloCajaTexto = new javax.swing.JTextField();
        jComboBoxDiagnostico1 = new javax.swing.JComboBox<>();
        jLabelDiagnostico1 = new javax.swing.JLabel();
        jLabelDiagnostico2 = new javax.swing.JLabel();
        jComboBoxDiagnostico2 = new javax.swing.JComboBox<>();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        resumen2CajaTexto = new javax.swing.JTextField();
        precio2CajaTexto = new javax.swing.JTextField();
        jCheckBox3 = new javax.swing.JCheckBox();
        jLabelDiagnostico3 = new javax.swing.JLabel();
        jComboBoxDiagnostico3 = new javax.swing.JComboBox<>();
        resumen3CajaTexto = new javax.swing.JTextField();
        precio3CajaTexto = new javax.swing.JTextField();
        jCheckBox4 = new javax.swing.JCheckBox();
        jLabelDiagnostico4 = new javax.swing.JLabel();
        jComboBoxDiagnostico4 = new javax.swing.JComboBox<>();
        resumen4CajaTexto = new javax.swing.JTextField();
        precio4CajaTexto = new javax.swing.JTextField();
        id_tarifa1CajaTexto = new javax.swing.JTextField();
        id_tarifa3CajaTexto = new javax.swing.JTextField();
        id_tarifa2CajaTexto = new javax.swing.JTextField();
        id_tarifa4CajaTexto = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        botonGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ReplyCar - Venta de Taller");

        jLabelSalir.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelSalir.setForeground(new java.awt.Color(0, 0, 204));
        jLabelSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/salir.png"))); // NOI18N
        jLabelSalir.setText("Salir");
        jLabelSalir.setToolTipText("Salir del formulario");

        jLabelAyuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/ayuda.png"))); // NOI18N
        jLabelAyuda.setToolTipText("Consultar ayuda para este formulario");

        jLabelTitulo.setFont(new java.awt.Font("Tempus Sans ITC", 1, 36)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 204));
        jLabelTitulo.setText("Venta de Taller (Fact. Intervención)");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 577, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 175, Short.MAX_VALUE)
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

        BastidorCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        BastidorCajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        BastidorCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BastidorCajaTextoKeyTyped(evt);
            }
        });

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

        totalFacturaCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        totalFacturaCajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        baseImponibleCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        baseImponibleCajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jComboBoxCliente.setToolTipText("Seleccione el cliente");

        jLabelColor.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelColor.setForeground(new java.awt.Color(0, 0, 204));
        jLabelColor.setText("(Resumen):");

        resumen1CajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        resumen1CajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabelExtras.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelExtras.setForeground(new java.awt.Color(0, 0, 204));
        jLabelExtras.setText("(Precio):");

        precio1CajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        precio1CajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        nClienteCajaTexto.setEditable(false);
        nClienteCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        nClienteCajaTexto.setToolTipText("");
        nClienteCajaTexto.setEnabled(false);

        jLabelMatricula.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelMatricula.setForeground(new java.awt.Color(0, 0, 204));
        jLabelMatricula.setText("(Matrícula):");

        MatriculaCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        MatriculaCajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        MatriculaCajaTexto.setToolTipText("Ingrese la matrícula");
        ((AbstractDocument) this.MatriculaCajaTexto.getDocument()).setDocumentFilter(new maestros.UppercaseDocumentFilter());
        MatriculaCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                MatriculaCajaTextoKeyTyped(evt);
            }
        });

        jButtonBuscar.setText("Buscar");
        jButtonBuscar.setToolTipText("Busca la matrícula ingresada");

        ModeloCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        ModeloCajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ModeloCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ModeloCajaTextoKeyTyped(evt);
            }
        });

        jComboBoxDiagnostico1.setToolTipText("Seleccione el diagnóstico");

        jLabelDiagnostico1.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelDiagnostico1.setForeground(new java.awt.Color(0, 0, 204));
        jLabelDiagnostico1.setText("(Diagnóstico 1):");

        jLabelDiagnostico2.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelDiagnostico2.setForeground(new java.awt.Color(0, 0, 204));
        jLabelDiagnostico2.setText("(Diagnóstico 2):");

        jComboBoxDiagnostico2.setToolTipText("Seleccione el diagnóstico");

        jCheckBox1.setToolTipText("Active el diagnóstico");

        jCheckBox2.setToolTipText("Active el diagnóstico");

        resumen2CajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        resumen2CajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        precio2CajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        precio2CajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jCheckBox3.setToolTipText("Active el diagnóstico");

        jLabelDiagnostico3.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelDiagnostico3.setForeground(new java.awt.Color(0, 0, 204));
        jLabelDiagnostico3.setText("(Diagnóstico 3):");

        jComboBoxDiagnostico3.setToolTipText("Seleccione el diagnóstico");

        resumen3CajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        resumen3CajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        precio3CajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        precio3CajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jCheckBox4.setToolTipText("Active el diagnóstico");

        jLabelDiagnostico4.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelDiagnostico4.setForeground(new java.awt.Color(0, 0, 204));
        jLabelDiagnostico4.setText("(Diagnóstico 4):");

        jComboBoxDiagnostico4.setToolTipText("Seleccione el diagnóstico");

        resumen4CajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        resumen4CajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        precio4CajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        precio4CajaTexto.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        id_tarifa1CajaTexto.setText("0");
        id_tarifa1CajaTexto.setEnabled(false);

        id_tarifa3CajaTexto.setText("0");
        id_tarifa3CajaTexto.setEnabled(false);

        id_tarifa2CajaTexto.setText("0");
        id_tarifa2CajaTexto.setEnabled(false);

        id_tarifa4CajaTexto.setText("0");
        id_tarifa4CajaTexto.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(nClienteCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(447, 447, 447)
                                .addComponent(jLabelColor, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(90, 90, 90)
                                .addComponent(jLabelExtras))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(72, 72, 72)
                                .addComponent(id_tarifa1CajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(id_tarifa2CajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(id_tarifa3CajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(id_tarifa4CajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox3)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jCheckBox1)
                                        .addComponent(jCheckBox2))
                                    .addComponent(jCheckBox4))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabelDiagnostico3)
                                                .addGap(1, 1, 1))
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                    .addGap(3, 3, 3)
                                                    .addComponent(jLabelDiagnostico1))
                                                .addComponent(jLabelDiagnostico2, javax.swing.GroupLayout.Alignment.TRAILING))
                                            .addComponent(jLabelDiagnostico4))
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(jComboBoxDiagnostico4, 0, 176, Short.MAX_VALUE))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(17, 17, 17)
                                                .addComponent(jComboBoxDiagnostico3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jComboBoxDiagnostico2, 0, 176, Short.MAX_VALUE)
                                                    .addComponent(jComboBoxDiagnostico1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                        .addGap(34, 34, 34)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(resumen4CajaTexto, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                                            .addComponent(resumen3CajaTexto)
                                            .addComponent(resumen2CajaTexto)
                                            .addComponent(resumen1CajaTexto))
                                        .addGap(34, 34, 34)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(precio1CajaTexto, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                                            .addComponent(precio2CajaTexto)
                                            .addComponent(precio3CajaTexto)
                                            .addComponent(precio4CajaTexto)))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabelCliente)
                                                .addGap(14, 14, 14)
                                                .addComponent(jComboBoxCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabelMatricula)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(MatriculaCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButtonBuscar))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabelBastidor)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(BastidorCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabelModelo)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(ModeloCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
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
                                .addComponent(totalFacturaCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelCliente)
                            .addComponent(jComboBoxCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelMatricula)
                            .addComponent(MatriculaCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonBuscar))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelBastidor)
                            .addComponent(jLabelModelo)
                            .addComponent(BastidorCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ModeloCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(51, 51, 51)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelExtras, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelColor)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nClienteCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(id_tarifa1CajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(id_tarifa3CajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(id_tarifa2CajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(id_tarifa4CajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jCheckBox1)
                        .addComponent(precio1CajaTexto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelDiagnostico1)
                            .addComponent(jComboBoxDiagnostico1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(resumen1CajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelDiagnostico2)
                                    .addComponent(jComboBoxDiagnostico2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(resumen2CajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(precio2CajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabelDiagnostico3)
                                            .addComponent(jComboBoxDiagnostico3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(resumen3CajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(precio3CajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jComboBoxDiagnostico4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(resumen4CajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(precio4CajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jCheckBox4)
                                                .addComponent(jLabelDiagnostico4))))
                                    .addComponent(jCheckBox3)))
                            .addComponent(jCheckBox2))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(baseImponibleCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelBaseImponible)
                    .addComponent(jLabelIVA)
                    .addComponent(ivaCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTotalFactura)
                    .addComponent(totalFacturaCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        botonGuardar.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        botonGuardar.setForeground(new java.awt.Color(0, 0, 204));
        botonGuardar.setText("Facturar Intervención");
        botonGuardar.setToolTipText("Pincha para facturar intervención");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(616, Short.MAX_VALUE)
                .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void BastidorCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BastidorCajaTextoKeyTyped
        BastidorCajaTexto.setBackground(Color.WHITE); // Cambia el fondo a blanco
        // Verifica si la longitud del texto en bastidorCajaTexto es mayor a 16 caracteres
        if (BastidorCajaTexto.getText().length() > 16) {
            evt.consume();
        }
    }//GEN-LAST:event_BastidorCajaTextoKeyTyped

    private void ModeloCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ModeloCajaTextoKeyTyped
        ModeloCajaTexto.setBackground(Color.WHITE); // Cambia el fondo a blanco
        // Verifica si la longitud del texto en bastidorCajaTexto es mayor a 16 caracteres
        if (ModeloCajaTexto.getText().length() > 40) {
            evt.consume();
        }
    }//GEN-LAST:event_ModeloCajaTextoKeyTyped

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
            java.util.logging.Logger.getLogger(ventaTaller.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ventaTaller.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ventaTaller.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ventaTaller.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                ventaTaller dialog = null;
                try {
                    dialog = new ventaTaller(new javax.swing.JFrame(), false);
                } catch (SQLException ex) {
                    Logger.getLogger(ventaTaller.class.getName()).log(Level.SEVERE, null, ex);
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
    private javax.swing.JTextField BastidorCajaTexto;
    private javax.swing.JTextField MatriculaCajaTexto;
    private javax.swing.JTextField ModeloCajaTexto;
    private javax.swing.JTextField baseImponibleCajaTexto;
    private javax.swing.JButton botonGuardar;
    private javax.swing.JTextField id_tarifa1CajaTexto;
    private javax.swing.JTextField id_tarifa2CajaTexto;
    private javax.swing.JTextField id_tarifa3CajaTexto;
    private javax.swing.JTextField id_tarifa4CajaTexto;
    private javax.swing.JTextField ivaCajaTexto;
    private javax.swing.JButton jButtonBuscar;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JComboBox<String> jComboBoxCliente;
    private javax.swing.JComboBox<String> jComboBoxDiagnostico1;
    private javax.swing.JComboBox<String> jComboBoxDiagnostico2;
    private javax.swing.JComboBox<String> jComboBoxDiagnostico3;
    private javax.swing.JComboBox<String> jComboBoxDiagnostico4;
    private javax.swing.JLabel jLabelAyuda;
    private javax.swing.JLabel jLabelBaseImponible;
    private javax.swing.JLabel jLabelBastidor;
    private javax.swing.JLabel jLabelCliente;
    private javax.swing.JLabel jLabelColor;
    private javax.swing.JLabel jLabelDiagnostico1;
    private javax.swing.JLabel jLabelDiagnostico2;
    private javax.swing.JLabel jLabelDiagnostico3;
    private javax.swing.JLabel jLabelDiagnostico4;
    private javax.swing.JLabel jLabelExtras;
    private javax.swing.JLabel jLabelIVA;
    private javax.swing.JLabel jLabelMatricula;
    private javax.swing.JLabel jLabelModelo;
    private javax.swing.JLabel jLabelSalir;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelTotalFactura;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField nClienteCajaTexto;
    private javax.swing.JTextField precio1CajaTexto;
    private javax.swing.JTextField precio2CajaTexto;
    private javax.swing.JTextField precio3CajaTexto;
    private javax.swing.JTextField precio4CajaTexto;
    private javax.swing.JTextField resumen1CajaTexto;
    private javax.swing.JTextField resumen2CajaTexto;
    private javax.swing.JTextField resumen3CajaTexto;
    private javax.swing.JTextField resumen4CajaTexto;
    private javax.swing.JTextField totalFacturaCajaTexto;
    // End of variables declaration//GEN-END:variables
}
