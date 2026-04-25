package app;

/*Clases importadas para la utilización en la clase.
 */
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Clase para añadir nuevos usuarios a nuestra APP
 *
 * @author Moisés López Vega
 */
public final class configBD extends javax.swing.JDialog {

    /**
     * Constructor del diálogo de configuración de B.D.
     *
     * @param parent el componente padre.
     * @param modal indica si el cuadro de diálogo es modal.
     */
    public configBD(java.awt.Frame parent, boolean modal) {
        // Llamamos al constructor de la superclase JDialog
        super(parent, modal);
        // Inicializa componentes del formulario
        initComponents();

        cargarConfiguracion();

        // Acción del botón Guardar
        botonGuardar.addActionListener((ActionEvent evt) -> {
            try {
                guardarBDActionPerformed();
            } catch (SQLException ex) {
                Logger.getLogger(nuevosClientes.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Agregar MouseListener a jLabel1 para cerrar la ventana y cambiar el cursor
        maestros.salirFormulario(jLabelSalir, this::compruebaSalir);

        // Agregar MouseListener a jLabelAyuda para abrir la ayuda de la app
        maestros.consultarAyuda(jLabelAyuda, "configuracion_bd");
    }

    // Método para guardar un usuario nuevo en la base de datos
    private void guardarBDActionPerformed() throws SQLException {

        Properties prop = new Properties();

        try {

            prop.setProperty("db.url", txtURL.getText());
            prop.setProperty("db.user", txtUsuario.getText());
            prop.setProperty("db.password", txtPassword.getText());
            prop.setProperty("db.driver", txtDriver.getText());
            String ruta = "configBD.properties";
            FileOutputStream fos = new FileOutputStream(ruta);
            prop.store(fos, "Configuración BD");
            fos.close();
            JOptionPane.showMessageDialog(this, "Configuración guardada correctamente", "Datos correctos", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error guardando configuración", "Datos INcorrectos", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método para cargar la configuración de la conexión de la base de datos
     */
    public void cargarConfiguracion() {
        Properties prop = new Properties();
        InputStream is = null;
        try {
            File archivoExterno = new File("configBD.properties");
            if (archivoExterno.exists()) {
                is = new FileInputStream(archivoExterno);
            } else {
                is = getClass().getClassLoader().getResourceAsStream("resources/configBD.properties");
            }
            if (is == null) {
                JOptionPane.showMessageDialog(this, "No se encontró el archivo de configuración de la base de datos", "Error configuración", JOptionPane.ERROR_MESSAGE);
                return;
            }
            prop.load(is);
            txtURL.setText(prop.getProperty("db.url"));
            txtUsuario.setText(prop.getProperty("db.user"));
            txtPassword.setText(prop.getProperty("db.password"));
            txtDriver.setText(prop.getProperty("db.driver"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error cargando configuración de B.D.");
        }
    }

    // Método para comprobar si los datos están correctos al salir del formulario
    private void compruebaSalir() {
        Boolean hayDatos = false;
        if (!txtURL.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!txtDriver.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!txtPassword.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!txtUsuario.getText().isEmpty()) {
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
                configBD.this.dispose();
            } else {
                // Si no confirma, simplemente cerramos el frame auxiliar
                frame.dispose();
            }
        } else {
            // Si no hay datos, cerramos directamente el formulario
            configBD.this.dispose();
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
        jLabelURL = new javax.swing.JLabel();
        jLabelDriver = new javax.swing.JLabel();
        txtURL = new javax.swing.JTextField();
        txtDriver = new javax.swing.JTextField();
        jLabelUsuario = new javax.swing.JLabel();
        jLabelClave = new javax.swing.JLabel();
        txtPassword = new javax.swing.JTextField();
        txtUsuario = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        botonGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ReplyCar - Configuración de la B.D.");

        jLabelSalir.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelSalir.setForeground(new java.awt.Color(0, 0, 204));
        jLabelSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/salir.png"))); // NOI18N
        jLabelSalir.setText("Salir");
        jLabelSalir.setToolTipText("Salir del formulario");

        jLabelAyuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/ayuda.png"))); // NOI18N
        jLabelAyuda.setToolTipText("Consultar ayuda para este formulario");

        jLabelTitulo.setFont(new java.awt.Font("Tempus Sans ITC", 1, 36)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 204));
        jLabelTitulo.setText("Configuración B.D.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addComponent(jLabelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelAyuda)
                .addGap(18, 18, 18)
                .addComponent(jLabelSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        jLabelURL.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelURL.setForeground(new java.awt.Color(0, 0, 204));
        jLabelURL.setText("(URL):");

        jLabelDriver.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelDriver.setForeground(new java.awt.Color(0, 0, 204));
        jLabelDriver.setText("(Driver):");

        txtURL.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        txtURL.setToolTipText("Ingresa URL");

        txtDriver.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        txtDriver.setToolTipText("Ingresa Driver");

        jLabelUsuario.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelUsuario.setForeground(new java.awt.Color(0, 0, 204));
        jLabelUsuario.setText("(Usuario):");

        jLabelClave.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelClave.setForeground(new java.awt.Color(0, 0, 204));
        jLabelClave.setText("(Clave):");

        txtPassword.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        txtPassword.setToolTipText("Ingresa clave del usuario");

        txtUsuario.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        txtUsuario.setToolTipText("Ingresa nombre del usuario");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabelURL)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtURL, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabelDriver)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtDriver))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabelUsuario)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabelClave)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelURL)
                    .addComponent(txtURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDriver)
                    .addComponent(txtDriver, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelUsuario)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelClave)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        botonGuardar.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        botonGuardar.setForeground(new java.awt.Color(0, 0, 204));
        botonGuardar.setText("Guardar Nueva Configuración");
        botonGuardar.setToolTipText("Pincha para guardar la configuración");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botonGuardar)
                .addGap(261, 261, 261))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(configBD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(configBD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(configBD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(configBD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                configBD dialog = new configBD(new javax.swing.JFrame(), false);
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
    private javax.swing.JLabel jLabelAyuda;
    private javax.swing.JLabel jLabelClave;
    private javax.swing.JLabel jLabelDriver;
    private javax.swing.JLabel jLabelSalir;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelURL;
    private javax.swing.JLabel jLabelUsuario;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField txtDriver;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtURL;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
