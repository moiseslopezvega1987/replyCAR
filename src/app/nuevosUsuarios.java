package app;

/*Clases importadas para la utilización en la clase.
 */
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.AbstractDocument;

/**
 * Clase para añadir nuevos usuarios a nuestra APP
 *
 * @author Moisés López Vega
 */
public class nuevosUsuarios extends javax.swing.JDialog {

    // DAO para manejar usuarios
    private UsuariosDAO usuariosDAO;
    // Objeto Usuario que representa el usuario actual en el formulario
    private usuarios usuarios;

    /**
     * Constructor del diálogo de nuevos usuarios
     *
     * @param parent el componente padre.
     * @param modal indica si el cuadro de diálogo es modal.
     */
    public nuevosUsuarios(java.awt.Frame parent, boolean modal) {
        // Llamamos al constructor de la superclase JDialog
        super(parent, modal);
        // Inicializa componentes del formulario
        initComponents();
        // Inicializamos el método que incluye los roles de los usuarios
        roles();
        // Inicializamos DAOs
        usuariosDAO = new UsuariosDAO();
        // Creamos un nuevo usuario vacío
        usuarios = new usuarios();
        // Obtener y establecer el siguiente ID para el usuario
        usuariosDAO.obtenerSiguienteID(nusuarioCajaTexto);
        // Bloqueamos el nº de usuario para que no sea modificable
        nusuarioCajaTexto.setEnabled(false);

        // Acción del botón Guardar
        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    guardarUsuarioActionPerformed(evt);
                } catch (SQLException ex) {
                    Logger.getLogger(nuevosClientes.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Método para que si el objeto seleccionado es distinto a 0 se ponga en blanco.
        jComboBoxRol.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (jComboBoxRol.getSelectedIndex() != 0) {
                    jComboBoxRol.setBackground(Color.WHITE);
                }
            }
        });

        // Agregar MouseListener a jLabel1 para cerrar la ventana y cambiar el cursor
        maestros.salirFormulario(jLabelSalir, this::compruebaSalir);

        // Agregar MouseListener a jLabelAyuda para abrir la ayuda de la app
        maestros.consultarAyuda(jLabelAyuda, "nuevos_usuarios");
    }

    // Método que incluye los roles en un jComboBox
    private void roles() {
        jComboBoxRol.addItem("");
        jComboBoxRol.addItem("Administrador");
        jComboBoxRol.addItem("Usuario Avanzado");
        jComboBoxRol.addItem("Usuario");
        jComboBoxRol.addItem("Invitado");
    }

    // Método para guardar un usuario nuevo en la base de datos
    private void guardarUsuarioActionPerformed(ActionEvent evt) throws SQLException {
        boolean esValido = true;
        try {
            // Comprueba Nombre
            if (!nombreCajaTexto.getText().isEmpty()) {
                usuarios.setNombre(nombreCajaTexto.getText());
            } else {
                nombreCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Comprueba apellidos
            if (!apellidosCajaTexto.getText().isEmpty()) {
                usuarios.setApellidos(apellidosCajaTexto.getText());
            } else {
                apellidosCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Comprueba usuario
            if (!usuarioCajaTexto.getText().isEmpty()) {
                usuarios.setUser(usuarioCajaTexto.getText());
            } else {
                usuarioCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Comprueba clave
            if (!claveCajaTexto.getText().isEmpty()) {
                usuarios.setPassword(claveCajaTexto.getText());
            } else {
                claveCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Comprueba rol
            if (jComboBoxRol.getSelectedIndex() != 0) {
                String variable = (String) jComboBoxRol.getSelectedItem();
                usuarios.setRol(variable);
            } else {
                jComboBoxRol.setBackground(Color.RED);
                esValido = false;
            }

            // Guardar el usuario en la base de datos si todas las validaciones pasan
            if (esValido) {
                usuariosDAO.guardarUsuario(usuarios);
                JOptionPane.showMessageDialog(null, "Usuario insertado correctamente.", "Todo ha ido bien", JOptionPane.INFORMATION_MESSAGE);
                resetearFormulario();
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, corrija los campos en rojo.", "Error de validación de datos", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            // Mostrar un mensaje de error si hay un problema con la entrada del usuario
            System.err.println("Error al actualizar el usuario: " + e.getMessage());
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
        if (!nombreCajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!apellidosCajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!claveCajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        if (!usuarioCajaTexto.getText().isEmpty()) {
            hayDatos = true;
        }
        if (jComboBoxRol.getSelectedIndex() != 0) {
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
                nuevosUsuarios.this.dispose();
            } else {
                // Si no confirma, simplemente cerramos el frame auxiliar
                frame.dispose();
            }
        } else {
            // Si no hay datos, cerramos directamente el formulario
            nuevosUsuarios.this.dispose();
        }
    }

    // Método para resetear el formulario
    private void resetearFormulario() {
        this.nombreCajaTexto.setText("");
        this.usuariosDAO.obtenerSiguienteID(nusuarioCajaTexto);
        this.apellidosCajaTexto.setText("");
        this.usuarioCajaTexto.setText("");
        this.claveCajaTexto.setText("");
        this.jComboBoxRol.setSelectedIndex(0);
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
        jLabelNusuario = new javax.swing.JLabel();
        jLabelNombre = new javax.swing.JLabel();
        jLabelApellidos = new javax.swing.JLabel();
        nusuarioCajaTexto = new javax.swing.JTextField();
        nombreCajaTexto = new javax.swing.JTextField();
        apellidosCajaTexto = new javax.swing.JTextField();
        jLabelUsuario = new javax.swing.JLabel();
        jLabelClave = new javax.swing.JLabel();
        jLabelRol = new javax.swing.JLabel();
        claveCajaTexto = new javax.swing.JTextField();
        usuarioCajaTexto = new javax.swing.JTextField();
        jComboBoxRol = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        botonGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ReplyCar - Nuevo Usuario");

        jLabelSalir.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelSalir.setForeground(new java.awt.Color(0, 0, 204));
        jLabelSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/salir.png"))); // NOI18N
        jLabelSalir.setText("Salir");
        jLabelSalir.setToolTipText("Salir del formulario");

        jLabelAyuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/ayuda.png"))); // NOI18N
        jLabelAyuda.setToolTipText("Consultar ayuda para este formulario");

        jLabelTitulo.setFont(new java.awt.Font("Tempus Sans ITC", 1, 36)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 204));
        jLabelTitulo.setText("Nuevo Usuario");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 254, Short.MAX_VALUE)
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

        jLabelNusuario.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelNusuario.setForeground(new java.awt.Color(0, 0, 204));
        jLabelNusuario.setText("(Nº usuario):");

        jLabelNombre.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelNombre.setForeground(new java.awt.Color(0, 0, 204));
        jLabelNombre.setText("(Nombre):");

        jLabelApellidos.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelApellidos.setForeground(new java.awt.Color(0, 0, 204));
        jLabelApellidos.setText("(Apellidos):");

        nusuarioCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N

        nombreCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        nombreCajaTexto.setToolTipText("Ingresa nombre del usuario");
        ((AbstractDocument) this.nombreCajaTexto.getDocument()).setDocumentFilter(new maestros.UppercaseDocumentFilter());
        nombreCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nombreCajaTextoKeyTyped(evt);
            }
        });

        apellidosCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        apellidosCajaTexto.setToolTipText("Ingresa apellidos del usuario");
        ((AbstractDocument) this.apellidosCajaTexto.getDocument()).setDocumentFilter(new maestros.UppercaseDocumentFilter());
        apellidosCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                apellidosCajaTextoKeyTyped(evt);
            }
        });

        jLabelUsuario.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelUsuario.setForeground(new java.awt.Color(0, 0, 204));
        jLabelUsuario.setText("(Usuario):");

        jLabelClave.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelClave.setForeground(new java.awt.Color(0, 0, 204));
        jLabelClave.setText("(Clave):");

        jLabelRol.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabelRol.setForeground(new java.awt.Color(0, 0, 204));
        jLabelRol.setText("(Rol):");

        claveCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        claveCajaTexto.setToolTipText("Ingresa clave del usuario");
        claveCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                claveCajaTextoKeyTyped(evt);
            }
        });

        usuarioCajaTexto.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        usuarioCajaTexto.setToolTipText("Ingresa nombre del usuario");
        ((AbstractDocument) this.usuarioCajaTexto.getDocument()).setDocumentFilter(new maestros.UppercaseDocumentFilter());
        usuarioCajaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                usuarioCajaTextoKeyTyped(evt);
            }
        });

        jComboBoxRol.setToolTipText("Selecciona rol del usuario");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelRol)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxRol, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelUsuario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(usuarioCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelClave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(claveCajaTexto))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelApellidos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(apellidosCajaTexto))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelNombre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombreCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelNusuario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nusuarioCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNusuario)
                    .addComponent(nusuarioCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNombre)
                    .addComponent(nombreCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelApellidos)
                    .addComponent(apellidosCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelRol)
                    .addComponent(jComboBoxRol, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelUsuario)
                    .addComponent(usuarioCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelClave)
                    .addComponent(claveCajaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        botonGuardar.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        botonGuardar.setForeground(new java.awt.Color(0, 0, 204));
        botonGuardar.setText("Guardar Nuevo Usuario");
        botonGuardar.setToolTipText("Pincha para guardar el nuevo usuario");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(532, Short.MAX_VALUE)
                .addComponent(botonGuardar)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nombreCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombreCajaTextoKeyTyped
        // Restablece el color de fondo a blanco al escribir
        nombreCajaTexto.setBackground(Color.WHITE);
        // Si el nombre supera los 15 caracteres, no permite seguir escribiendo
        if (nombreCajaTexto.getText().length() > 15) {
            evt.consume();
        }
    }//GEN-LAST:event_nombreCajaTextoKeyTyped

    private void apellidosCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_apellidosCajaTextoKeyTyped
        // Restablece el color de fondo a blanco al escribir
        apellidosCajaTexto.setBackground(Color.WHITE);
        // Si el apellido supera los 20 caracteres, no permite seguir escribiendo
        if (apellidosCajaTexto.getText().length() > 20) {
            evt.consume();
        }
    }//GEN-LAST:event_apellidosCajaTextoKeyTyped

    private void claveCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_claveCajaTextoKeyTyped
        // Restablece el color de fondo a blanco al escribir
        claveCajaTexto.setBackground(Color.WHITE);
        // Si la clave supera los 10 caracteres, no permite seguir escribiendo
        if (claveCajaTexto.getText().length() > 10) {
            evt.consume();
        }
    }//GEN-LAST:event_claveCajaTextoKeyTyped

    private void usuarioCajaTextoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_usuarioCajaTextoKeyTyped
        // Restablece el color de fondo a blanco al escribir
        usuarioCajaTexto.setBackground(Color.WHITE);
        // Si el usuario supera los 15 caracteres, no permite seguir escribiendo
        if (usuarioCajaTexto.getText().length() > 15) {
            evt.consume();
        }
    }//GEN-LAST:event_usuarioCajaTextoKeyTyped

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
            java.util.logging.Logger.getLogger(nuevosUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(nuevosUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(nuevosUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(nuevosUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                nuevosUsuarios dialog = new nuevosUsuarios(new javax.swing.JFrame(), false);
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
    private javax.swing.JTextField apellidosCajaTexto;
    private javax.swing.JButton botonGuardar;
    private javax.swing.JTextField claveCajaTexto;
    private javax.swing.JComboBox<String> jComboBoxRol;
    private javax.swing.JLabel jLabelApellidos;
    private javax.swing.JLabel jLabelAyuda;
    private javax.swing.JLabel jLabelClave;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JLabel jLabelNusuario;
    private javax.swing.JLabel jLabelRol;
    private javax.swing.JLabel jLabelSalir;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelUsuario;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField nombreCajaTexto;
    private javax.swing.JTextField nusuarioCajaTexto;
    private javax.swing.JTextField usuarioCajaTexto;
    // End of variables declaration//GEN-END:variables
}
