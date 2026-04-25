package app;

/*Clases importadas para la utilización en la clase.
 */
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;
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
public class modificarFormasPago extends javax.swing.JDialog {

    // DAO para manejar formas de pago
    private FormasPagoDAO formaPagoDAO;
    // Objeto formas de pago que representa la forma de pago actual en el formulario
    private formasPago formasPago;
    // Lista de todos los formasdepagoList
    private List<formasPago> formaPagoList;
    // Índice de la forma de pago actual
    private int formaPagoIndex = 0;

    /**
     * Constructor del diálogo de modificación de formas de pago
     *
     * @param parent el componente padre.
     * @param modal indica si el cuadro de diálogo es modal.
     */
    public modificarFormasPago(java.awt.Frame parent, boolean modal) {
        // Llamamos al constructor de la superclase JDialog
        super(parent, modal);
        // Inicializa componentes del formulario
        initComponents();
        // Inicializamos DAOs
        formaPagoDAO = new FormasPagoDAO();
        // Creamos una nueva forma de pago vacía
        formasPago = new formasPago();
        // Obtener todos los formasdepagoList de la base de datos
        formaPagoList = formaPagoDAO.obtenerTodasLasFormasPago();
        // Bloqueamos el nº de forma de pago para que no sea modificable
        nformapagoCajaTexto.setEnabled(false);
        // Cargar la primera forma de pago
        cargarFormaPago(formaPagoList.get(formaPagoIndex));

        // Acción del botón Guardar
        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    guardarFormaPagoActionPerformed(evt);
                } catch (SQLException ex) {
                    Logger.getLogger(modificarFormasPago.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Agregar MouseListener a jLabelDerecha para ir al siguiente cliente
        jLabelDerecha.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (formaPagoIndex < formaPagoList.size() - 1) {
                    formaPagoIndex++;
                    cargarFormaPago(formaPagoList.get(formaPagoIndex));
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

        // Agregar MouseListener a jLabelIzquierda para ir al cliente anterior
        jLabelIzquierda.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (formaPagoIndex > 0) {
                    formaPagoIndex--;
                    cargarFormaPago(formaPagoList.get(formaPagoIndex));
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
        maestros.consultarAyuda(jLabelAyuda, "modificar_formas");
    }

    // Método de carga la forma de pago completa
    private void cargarFormaPago(formasPago formasPago) {
        this.formasPago = formasPago;
        nformapagoCajaTexto.setText(String.valueOf(formasPago.getId()));
        TipoCajaTexto.setText(formasPago.getTipoPago());
    }

    // Método para guardar una forma de pago modificada en la base de datos
    private void guardarFormaPagoActionPerformed(ActionEvent evt) throws SQLException {
        boolean esValido = true;
        try {
            // Comprueba Tipo
            if (!TipoCajaTexto.getText().isEmpty()) {
                formasPago.setTipoPago(TipoCajaTexto.getText());
            } else {
                TipoCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Guardar la forma de pago en la base de datos si todas las validaciones pasan
            if (esValido) {
                formaPagoDAO.actualizarFormaPago(formasPago);
                JOptionPane.showMessageDialog(null, "Forma de Pago actualizada correctamente.", "Todo ha ido bien", JOptionPane.INFORMATION_MESSAGE);
                formaPagoList = formaPagoDAO.obtenerTodasLasFormasPago();
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, corrija los campos en rojo.", "Error de validación de datos", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            // Mostrar un mensaje de error si hay un problema con la entrada de la forma de pago
            System.err.println("Error al actualizar la Forma de Pago: " + e.getMessage());
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
        if (!TipoCajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        // Si hay datos escritos, pregunta al usuario si realmente quiere salir
        if (hayDatos) {
            JFrame frame = new JFrame("Ey, no tan rápido");
            String mensaje = "¿Realmente quiere salir?, Hay datos introducidos.";
            String titulo = "Confirmar Salida";
            // Muestra un cuadro de confirmación
            int opcion = JOptionPane.showConfirmDialog(frame, mensaje, titulo, JOptionPane.YES_NO_OPTION);
            // Si el usuario confirma, se cierra el formulario
            if (opcion == JOptionPane.YES_OPTION) {
                modificarFormasPago.this.dispose();
            } else {
                // Si no confirma, simplemente cerramos el frame auxiliar
                frame.dispose();
            }
        } else {
            // Si no hay datos, cerramos directamente el formulario
            modificarFormasPago.this.dispose();
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
        jLabelNformapago = new javax.swing.JLabel();
        jLabelTipo = new javax.swing.JLabel();
        nformapagoCajaTexto = new javax.swing.JTextField();
        TipoCajaTexto = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        botonGuardar = new javax.swing.JButton();
        botonBuscar = new javax.swing.JButton();
        jTextFieldBuscar = new javax.swing.JTextField();
        jLabelNformapago2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ReplyCar - Modificar Forma de Pago");

        jLabelSalir.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelSalir.setForeground(new java.awt.Color(0, 0, 204));
        jLabelSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/salir.png"))); // NOI18N
        jLabelSalir.setText("Salir");
        jLabelSalir.setToolTipText("Salir del formulario");

        jLabelAyuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/ayuda.png"))); // NOI18N
        jLabelAyuda.setToolTipText("Consultar ayuda para este formulario");

        jLabelTitulo.setFont(new java.awt.Font("Tempus Sans ITC", 1, 36)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 204));
        jLabelTitulo.setText("Modificar Forma de Pago");

        jLabelIzquierda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/izquierda.png"))); // NOI18N
        jLabelIzquierda.setToolTipText("Anterior Forma de Pago");

        jLabelDerecha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/derecha.png"))); // NOI18N
        jLabelDerecha.setToolTipText("Forma de Pago Siguiente");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jLabelIzquierda, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelDerecha, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                    .addComponent(jLabelIzquierda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabelAyuda))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabelTitulo)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabelDerecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jLabelNformapago.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelNformapago.setForeground(new java.awt.Color(0, 0, 204));
        jLabelNformapago.setText("(Nº ID):");

        jLabelTipo.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelTipo.setForeground(new java.awt.Color(0, 0, 204));
        jLabelTipo.setText("(Tipo):");

        nformapagoCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N

        TipoCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        TipoCajaTexto.setToolTipText("Ingresa el nombre de la forma de pago");
        ((AbstractDocument) this.TipoCajaTexto.getDocument()).setDocumentFilter(new maestros.UppercaseDocumentFilter());
        TipoCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TipoCajaTextoKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTipo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TipoCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelNformapago)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nformapagoCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNformapago)
                    .addComponent(nformapagoCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTipo)
                    .addComponent(TipoCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        botonGuardar.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        botonGuardar.setForeground(new java.awt.Color(0, 0, 204));
        botonGuardar.setText("Guardar F.P. Modificada");
        botonGuardar.setToolTipText("Pincha para guardar la forma de pago modificada");

        botonBuscar.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        botonBuscar.setForeground(new java.awt.Color(0, 0, 204));
        botonBuscar.setText("Buscar");
        botonBuscar.setToolTipText("Pincha para buscar la Forma de Pago");
        botonBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBuscarActionPerformed(evt);
            }
        });

        jTextFieldBuscar.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jTextFieldBuscar.setForeground(new java.awt.Color(0, 0, 204));
        jTextFieldBuscar.setToolTipText("Ingresa la ID de la forma de pago");

        jLabelNformapago2.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabelNformapago2.setForeground(new java.awt.Color(0, 0, 204));
        jLabelNformapago2.setText("(Nº ID):");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabelNformapago2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 216, Short.MAX_VALUE)
                .addComponent(botonGuardar))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNformapago2)
                    .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

    private void TipoCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TipoCajaTextoKeyTyped
        // Restablece el color de fondo a blanco al escribir
        TipoCajaTexto.setBackground(Color.WHITE);
        // Si el tipo supera los 15 caracteres, no permite seguir escribiendo
        if (TipoCajaTexto.getText().length() > 15) {
            evt.consume();
        }
    }//GEN-LAST:event_TipoCajaTextoKeyTyped

    // Método para guardar la forma de pago modificada
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
            if (usuarioIndex <= 0 || usuarioIndex > formaPagoList.size()) {
                JOptionPane.showMessageDialog(null, "Forma de Pago inexistente.", "Error en la búsqueda", JOptionPane.ERROR_MESSAGE);
                jTextFieldBuscar.setBackground(Color.RED);
                esValido = false;
            } else {
                // Si el índice es válido, cargar el usuario
                cargarFormaPago(formaPagoList.get(usuarioIndex - 1)); // Ajuste de índice para reflejar la posición en la lista
            }
        }

    }//GEN-LAST:event_botonBuscarActionPerformed

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
            java.util.logging.Logger.getLogger(modificarFormasPago.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(modificarFormasPago.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(modificarFormasPago.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(modificarFormasPago.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                modificarFormasPago dialog = new modificarFormasPago(new javax.swing.JFrame(), false);
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
    private javax.swing.JTextField TipoCajaTexto;
    private javax.swing.JButton botonBuscar;
    private javax.swing.JButton botonGuardar;
    private javax.swing.JLabel jLabelAyuda;
    private javax.swing.JLabel jLabelDerecha;
    private javax.swing.JLabel jLabelIzquierda;
    private javax.swing.JLabel jLabelNformapago;
    private javax.swing.JLabel jLabelNformapago2;
    private javax.swing.JLabel jLabelSalir;
    private javax.swing.JLabel jLabelTipo;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jTextFieldBuscar;
    private javax.swing.JTextField nformapagoCajaTexto;
    // End of variables declaration//GEN-END:variables
}
