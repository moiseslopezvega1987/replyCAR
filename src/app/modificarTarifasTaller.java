package app;

/*Clases importadas para la utilización en la clase.
 */
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.AbstractDocument;

/**
 * Clase para modificar formas de pago a nuestra APP
 *
 * @author Moisés López Vega
 */
public class modificarTarifasTaller extends javax.swing.JDialog {

    // DAO para manejar tarifas de taller
    private TarifaTallerDAO tarifaTallerDAO; //Acceso a operaciones relacionadas con tarifas de taller
    // Objeto Tarifa Taller que representa la tarifa de taller actual en el formulario
    private averias tarifaTaller;
    // Lista de todos las tarifas de taller
    private List<averias> tarifasList;
    // Índice de la tarifa de taller actual
    private int idIndex = 0;
    // Dar formato moneda a una cantidad
    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.of("es", "ES"));

    /**
     * Constructor del diálogo de modificación tarifas de taller
     *
     * @param parent el componente padre.
     * @param modal indica si el cuadro de diálogo es modal.
     */
    public modificarTarifasTaller(java.awt.Frame parent, boolean modal) {
        // Llamamos al constructor de la superclase JDialog
        super(parent, modal);
        // Inicializa componentes del formulario
        initComponents();
        // Inicializamos DAOs
        tarifaTallerDAO = new TarifaTallerDAO();
        // Creamos una nueva tarifas de taller vacía
        tarifaTaller = new averias();
        // Obtener todas las tarifaTallerList de la base de datos
        tarifasList = tarifaTallerDAO.obtenerTodasLasTarifasTaller();
        // Bloqueamos el nº de ID para que no sea modificable
        ntarifaCajaTexto.setEnabled(false);
        // Cargar el primer ID
        cargarTarifaTaller(tarifasList.get(idIndex));

        // Realizar el foco para que se actualice el precio de la tarifa
        precioCajaTexto.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    String texto = precioCajaTexto.getText()
                            .replace(".", "")
                            .replace("€", "")
                            .replace(",", ".")
                            .trim();
                    double valor = Double.parseDouble(texto);
                    precioCajaTexto.setText(formatoMoneda.format(valor));
                } catch (NumberFormatException ex) {
                    precioCajaTexto.setBackground(Color.RED);
                }
            }
        });

        // Acción del botón Guardar
        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    guardarTarifaTallerActionPerformed(evt);
                } catch (SQLException ex) {
                    Logger.getLogger(modificarTarifasTaller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Agregar MouseListener a jLabelDerecha para ir a la siguiente tarifa de taller
        jLabelDerecha.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (idIndex < tarifasList.size() - 1) {
                    idIndex++;
                    cargarTarifaTaller(tarifasList.get(idIndex));
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                jLabelDerecha.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                jLabelDerecha.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });

        // Agregar MouseListener a jLabelIzquierda para ir a la tarifa de taller anterior
        jLabelIzquierda.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (idIndex > 0) {
                    idIndex--;
                    cargarTarifaTaller(tarifasList.get(idIndex));
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                jLabelIzquierda.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                jLabelIzquierda.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });

        // Agregar MouseListener a jLabel1 para cerrar la ventana y cambiar el cursor
        maestros.salirFormulario(jLabelSalir, this::compruebaSalir);

        // Agregar MouseListener a jLabelAyuda para abrir la ayuda de la app
        maestros.consultarAyuda(jLabelAyuda, "modificar_tarifas");
    }

    // Método de carga la tarifa de taller completa
    private void cargarTarifaTaller(averias tarifaTaller) {
        this.tarifaTaller = tarifaTaller;
        ntarifaCajaTexto.setText(String.valueOf(tarifaTaller.getId()));
        TipoAveriaCajaTexto.setText(tarifaTaller.getTipo());
        textoCajaTexto.setText(tarifaTaller.getDescripcion());
        precioCajaTexto.setText(formatoMoneda.format(tarifaTaller.getPrecio()));
    }

    // Método para guardar una tarifa de taller modificada en la base de datos
    private void guardarTarifaTallerActionPerformed(ActionEvent evt) throws SQLException {
        tarifaTaller.setId(Integer.parseInt(ntarifaCajaTexto.getText()));
        boolean esValido = true;
        try {
            // Comprueba Tipo de avería
            if (!TipoAveriaCajaTexto.getText().isEmpty()) {
                tarifaTaller.setTipo(TipoAveriaCajaTexto.getText());
            } else {
                TipoAveriaCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Comprueba Texto
            if (!textoCajaTexto.getText().isEmpty()) {
                tarifaTaller.setDescripcion(textoCajaTexto.getText());
            } else {
                textoCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Comprueba Precio de tarifa
            if (!precioCajaTexto.getText().isEmpty()) {
                String texto = precioCajaTexto.getText();
                texto = texto.replaceAll("[^0-9,]", "");
                texto = texto.replace(",", ".");
                tarifaTaller.setPrecio(Double.parseDouble(texto));
            } else {
                precioCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Guardar la tarifa de taller en la base de datos si todas las validaciones pasan
            if (esValido) {
                tarifaTallerDAO.actualizarTarifaTaller(tarifaTaller);
                JOptionPane.showMessageDialog(null, "Tarifa de Taller actualizada correctamente.", "Todo ha ido bien", JOptionPane.INFORMATION_MESSAGE);
                tarifasList = tarifaTallerDAO.obtenerTodasLasTarifasTaller();
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, corrija los campos en rojo.", "Error de validación de datos", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            // Mostrar un mensaje de error si hay un problema con la entrada del usuario
            System.err.println("Error al actualizar la tarifa de taller: " + e.getMessage());
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

    // Método para comprobar si los datos están correctos al salir del formulario
    private void compruebaSalir() {
        Boolean hayDatos = false;
        if (!TipoAveriaCajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!textoCajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!precioCajaTexto.getText().isEmpty()) {
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
                modificarTarifasTaller.this.dispose();
            } else {
                // Si no confirma, simplemente cerramos el frame auxiliar
                frame.dispose();
            }
        } else {
            // Si no hay datos, cerramos directamente el formulario
            modificarTarifasTaller.this.dispose();
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
        jLabelIzquierda = new javax.swing.JLabel();
        jLabelDerecha = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabelntarifa = new javax.swing.JLabel();
        jLabelTipoAveria = new javax.swing.JLabel();
        jLabelTexto = new javax.swing.JLabel();
        ntarifaCajaTexto = new javax.swing.JTextField();
        TipoAveriaCajaTexto = new javax.swing.JTextField();
        precioCajaTexto = new javax.swing.JTextField();
        jLabelPrecio = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textoCajaTexto = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        botonGuardar = new javax.swing.JButton();
        botonBuscar = new javax.swing.JButton();
        jTextFieldBuscar = new javax.swing.JTextField();
        jLabelNusuario2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ReplyCar - Modificar Tarifa de Taller");

        jLabelSalir.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelSalir.setForeground(new java.awt.Color(0, 0, 204));
        jLabelSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/salir.png"))); // NOI18N
        jLabelSalir.setText("Salir");
        jLabelSalir.setToolTipText("Salir del formulario");

        jLabelAyuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/ayuda.png"))); // NOI18N
        jLabelAyuda.setToolTipText("Consultar ayuda para este formulario");

        jLabelTitulo.setFont(new java.awt.Font("Tempus Sans ITC", 1, 36)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 204));
        jLabelTitulo.setText("Modificar Tarifa de Taller");

        jLabelIzquierda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/izquierda.png"))); // NOI18N
        jLabelIzquierda.setToolTipText("Anterior Tarifa de Taller");

        jLabelDerecha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/derecha.png"))); // NOI18N
        jLabelDerecha.setToolTipText("Siguiente Tarifa de Taller");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 388, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabelIzquierda, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelDerecha, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabelDerecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelIzquierda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelTitulo)
                            .addComponent(jLabelAyuda))
                        .addContainerGap(10, Short.MAX_VALUE))))
        );

        jLabelntarifa.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelntarifa.setForeground(new java.awt.Color(0, 0, 204));
        jLabelntarifa.setText("(Nº Tarifa):");

        jLabelTipoAveria.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelTipoAveria.setForeground(new java.awt.Color(0, 0, 204));
        jLabelTipoAveria.setText("(Tipo avería):");

        jLabelTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelTexto.setForeground(new java.awt.Color(0, 0, 204));
        jLabelTexto.setText("(Texto):");

        ntarifaCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N

        TipoAveriaCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        TipoAveriaCajaTexto.setToolTipText("Ingresa el tipo de avería");
        ((AbstractDocument) this.TipoAveriaCajaTexto.getDocument()).setDocumentFilter(new maestros.UppercaseDocumentFilter());
        TipoAveriaCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TipoAveriaCajaTextoKeyTyped(evt);
            }
        });

        precioCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        precioCajaTexto.setToolTipText("Ingresa el precio de la tarifa");
        precioCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                precioCajaTextoKeyTyped(evt);
            }
        });

        jLabelPrecio.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelPrecio.setForeground(new java.awt.Color(0, 0, 204));
        jLabelPrecio.setText("(Precio Kit):");

        textoCajaTexto.setColumns(20);
        textoCajaTexto.setRows(5);
        textoCajaTexto.setToolTipText("Ingresa el texto de la avería");
        ((AbstractDocument) this.textoCajaTexto.getDocument()).setDocumentFilter(new maestros.UppercaseDocumentFilter());
        jScrollPane1.setViewportView(textoCajaTexto);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelTipoAveria)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelTexto)
                        .addGap(48, 48, 48)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(TipoAveriaCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelntarifa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ntarifaCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addContainerGap(18, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabelPrecio)
                .addGap(18, 18, 18)
                .addComponent(precioCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelntarifa)
                    .addComponent(ntarifaCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTipoAveria)
                    .addComponent(TipoAveriaCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelTexto)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(precioCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPrecio)))
        );

        botonGuardar.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        botonGuardar.setForeground(new java.awt.Color(0, 0, 204));
        botonGuardar.setText("Guardar Tarifa Modificada");
        botonGuardar.setToolTipText("Pincha para guardar la nueva tarifa modificada");

        botonBuscar.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        botonBuscar.setForeground(new java.awt.Color(0, 0, 204));
        botonBuscar.setText("Buscar");
        botonBuscar.setToolTipText("Pincha para buscar la tarifa de taller");
        botonBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBuscarActionPerformed(evt);
            }
        });

        jTextFieldBuscar.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jTextFieldBuscar.setForeground(new java.awt.Color(0, 0, 204));
        jTextFieldBuscar.setToolTipText("Ingresa el ID del tipo de avería");

        jLabelNusuario2.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabelNusuario2.setForeground(new java.awt.Color(0, 0, 204));
        jLabelNusuario2.setText("(Nº Tarifa):");
        jLabelNusuario2.setToolTipText("");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabelNusuario2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 210, Short.MAX_VALUE)
                .addComponent(botonGuardar)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(botonBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelNusuario2))
                    .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TipoAveriaCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TipoAveriaCajaTextoKeyTyped
        // Restablece el color de fondo a blanco al escribir
        TipoAveriaCajaTexto.setBackground(Color.WHITE);
        // Si el tipo supera los 15 caracteres, no permite seguir escribiendo
        if (TipoAveriaCajaTexto.getText().length() > 15) {
            evt.consume();
        }
    }//GEN-LAST:event_TipoAveriaCajaTextoKeyTyped

    // Método para guardar la tarifa de taller modificada
    private void botonBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBuscarActionPerformed
        boolean esValido = true;
        String texto = jTextFieldBuscar.getText().trim();
        // Validar que el texto no esté vacío y que sea numérico
        if (texto.isEmpty() || !isNumeric(texto)) {
            jTextFieldBuscar.setBackground(Color.RED);
            JOptionPane.showMessageDialog(null, "Por favor, corrija los campos en rojo.", "Error de validación de datos", JOptionPane.ERROR_MESSAGE);
            esValido = false;
        } else {
            // Convertir el texto en número
            int usuarioIndex = Integer.parseInt(texto);
            // Validar que el índice esté dentro del rango permitido
            if (usuarioIndex <= 0 || usuarioIndex > tarifasList.size()) {
                JOptionPane.showMessageDialog(null, "ID de tarifa de taller no inexistente.", "Error en la búsqueda", JOptionPane.ERROR_MESSAGE);
                jTextFieldBuscar.setBackground(Color.RED);
                esValido = false;
            } else {
                // Si el índice es válido, cargar la tarifa de taller
                cargarTarifaTaller(tarifasList.get(usuarioIndex - 1)); // Ajuste de índice para reflejar la posición en la lista
            }
        }
    }//GEN-LAST:event_botonBuscarActionPerformed

    private void precioCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_precioCajaTextoKeyTyped
        // Restablece el color de fondo a blanco al escribir
        precioCajaTexto.setBackground(Color.WHITE);
        // Si el precio supera los 10 caracteres, no permite seguir escribiendo
        if (precioCajaTexto.getText().length() > 10) {
            evt.consume();
        }
    }//GEN-LAST:event_precioCajaTextoKeyTyped

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
            java.util.logging.Logger.getLogger(modificarTarifasTaller.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(modificarTarifasTaller.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(modificarTarifasTaller.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(modificarTarifasTaller.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                modificarTarifasTaller dialog = new modificarTarifasTaller(new javax.swing.JFrame(), false);
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
    private javax.swing.JTextField TipoAveriaCajaTexto;
    private javax.swing.JButton botonBuscar;
    private javax.swing.JButton botonGuardar;
    private javax.swing.JLabel jLabelAyuda;
    private javax.swing.JLabel jLabelDerecha;
    private javax.swing.JLabel jLabelIzquierda;
    private javax.swing.JLabel jLabelNusuario2;
    private javax.swing.JLabel jLabelPrecio;
    private javax.swing.JLabel jLabelSalir;
    private javax.swing.JLabel jLabelTexto;
    private javax.swing.JLabel jLabelTipoAveria;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelntarifa;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldBuscar;
    private javax.swing.JTextField ntarifaCajaTexto;
    private javax.swing.JTextField precioCajaTexto;
    private javax.swing.JTextArea textoCajaTexto;
    // End of variables declaration//GEN-END:variables
}
