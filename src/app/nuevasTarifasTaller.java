package app;

/*Clases importadas para la utilización en la clase.
 */
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.AbstractDocument;

/**
 * Clase para añadir nuevas tarifas de taller a nuestra APP
 *
 * @author Moisés López Vega
 */
public class nuevasTarifasTaller extends javax.swing.JDialog {

    // DAO para manejar tarifas de taller
    private TarifaTallerDAO tarifaTallerDAO;
    // Objeto Tarifa Taller que representa la tarifa de taller actual en el formulario
    private averias tarifaTaller;
    // Dar formato moneda a una cantidad
    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.of("es", "ES"));

    /**
     * Constructor del diálogo de nuevas tarifas de taller
     *
     * @param parent el componente padre.
     * @param modal indica si el cuadro de diálogo es modal.
     */
    public nuevasTarifasTaller(java.awt.Frame parent, boolean modal) {
        // Llamamos al constructor de la superclase JDialog
        super(parent, modal);
        // Inicializa componentes del formulario
        initComponents();
        // Inicializamos DAOs
        tarifaTallerDAO = new TarifaTallerDAO();
        // Creamos una nueva tarifa de taller vacía
        tarifaTaller = new averias();
        //Obtener y establecer el siguiente ID para la tarifa de taller
        tarifaTallerDAO.obtenerSiguienteID(ntarifaCajaTexto);
        // Bloqueamos el nº de tarifa de taller para que no sea modificable
        ntarifaCajaTexto.setEnabled(false);

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
                    Logger.getLogger(nuevosClientes.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Agregar MouseListener a jLabel1 para cerrar la ventana y cambiar el cursor
        maestros.salirFormulario(jLabelSalir, this::compruebaSalir);

        // Agregar MouseListener a jLabelAyuda para abrir la ayuda de la app
        maestros.consultarAyuda(jLabelAyuda, "nuevas_tarifas");
    }

    // Método para guardar una tarifa de taller nueva en la base de datos
    private void guardarTarifaTallerActionPerformed(ActionEvent evt) throws SQLException {
        boolean esValido = true;
        try {
            // Comprueba Tipo averia
            if (!TipoAveriaCajaTexto.getText().isEmpty()) {
                tarifaTaller.setTipo(TipoAveriaCajaTexto.getText());
            } else {
                TipoAveriaCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Comprueba texto
            if (!textoCajaTexto.getText().isEmpty()) {
                tarifaTaller.setDescripcion(textoCajaTexto.getText());
            } else {
                textoCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Comprueba precio
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
                tarifaTallerDAO.guardarTarifaTaller(tarifaTaller);
                JOptionPane.showMessageDialog(null, "Tarifa de Taller insertada correctamente.", "Todo ha ido bien", JOptionPane.INFORMATION_MESSAGE);
                resetearFormulario();
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, corrija los campos en rojo.", "Error de validación de datos", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            // Mostrar un mensaje de error si hay un problema con la entrada de la tarifa de taller
            precioCajaTexto.setBackground(Color.RED);
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
                nuevasTarifasTaller.this.dispose();
            } else {
                // Si no confirma, simplemente cerramos el frame auxiliar
                frame.dispose();
            }
        } else {
            // Si no hay datos, cerramos directamente el formulario
            nuevasTarifasTaller.this.dispose();
        }
    }

    // Método para resetear el formulario
    private void resetearFormulario() {
        this.TipoAveriaCajaTexto.setText("");
        this.tarifaTallerDAO.obtenerSiguienteID(ntarifaCajaTexto);
        this.textoCajaTexto.setText("");
        this.precioCajaTexto.setText("");
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
        jLabelNtarifa = new javax.swing.JLabel();
        jLabelTipoAveria = new javax.swing.JLabel();
        jLabelTexto = new javax.swing.JLabel();
        ntarifaCajaTexto = new javax.swing.JTextField();
        TipoAveriaCajaTexto = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        textoCajaTexto = new javax.swing.JTextArea();
        precioCajaTexto = new javax.swing.JTextField();
        jLabelPrecio = new javax.swing.JLabel();
        botonGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ReplyCar - Nueva Tarifa de Taller");

        jLabelSalir.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelSalir.setForeground(new java.awt.Color(0, 0, 204));
        jLabelSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/salir.png"))); // NOI18N
        jLabelSalir.setText("Salir");
        jLabelSalir.setToolTipText("Salir del formulario");

        jLabelAyuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/ayuda.png"))); // NOI18N
        jLabelAyuda.setToolTipText("Consultar ayuda para este formulario");

        jLabelTitulo.setFont(new java.awt.Font("Tempus Sans ITC", 1, 36)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 204));
        jLabelTitulo.setText("Nueva Tarifa Taller");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
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
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jLabelNtarifa.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelNtarifa.setForeground(new java.awt.Color(0, 0, 204));
        jLabelNtarifa.setText("(Nº Tarifa):");

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
                        .addComponent(jLabelTexto)
                        .addGap(48, 48, 48)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 502, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelTipoAveria)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TipoAveriaCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelNtarifa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ntarifaCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNtarifa)
                    .addComponent(ntarifaCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTipoAveria)
                    .addComponent(TipoAveriaCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTexto)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

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

        botonGuardar.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        botonGuardar.setForeground(new java.awt.Color(0, 0, 204));
        botonGuardar.setText("Guardar Nueva Tarifa");
        botonGuardar.setToolTipText("Pincha para guardar la nueva tarifa");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelPrecio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(precioCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(205, 205, 205)
                .addComponent(botonGuardar)
                .addGap(15, 15, 15))
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelPrecio)
                    .addComponent(precioCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TipoAveriaCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TipoAveriaCajaTextoKeyTyped
        // Restablece el color de fondo a blanco al escribir
        TipoAveriaCajaTexto.setBackground(Color.WHITE);
        // Si el tipo supera los 30 caracteres, no permite seguir escribiendo
        if (TipoAveriaCajaTexto.getText().length() > 30) {
            evt.consume();
        }
    }//GEN-LAST:event_TipoAveriaCajaTextoKeyTyped

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
            java.util.logging.Logger.getLogger(nuevasTarifasTaller.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(nuevasTarifasTaller.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(nuevasTarifasTaller.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(nuevasTarifasTaller.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                nuevasTarifasTaller dialog = new nuevasTarifasTaller(new javax.swing.JFrame(), false);
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
    private javax.swing.JButton botonGuardar;
    private javax.swing.JLabel jLabelAyuda;
    private javax.swing.JLabel jLabelNtarifa;
    private javax.swing.JLabel jLabelPrecio;
    private javax.swing.JLabel jLabelSalir;
    private javax.swing.JLabel jLabelTexto;
    private javax.swing.JLabel jLabelTipoAveria;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField ntarifaCajaTexto;
    private javax.swing.JTextField precioCajaTexto;
    private javax.swing.JTextArea textoCajaTexto;
    // End of variables declaration//GEN-END:variables
}
