package app;

/*Clases importadas para la utilización en la clase.
 */
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
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
 * Clase para modificar los usuarios de nuestra APP
 *
 * @author Moisés López Vega
 */
public class modificarUsuarios extends javax.swing.JDialog {
    
    // DAO para manejar la persistencia de Usuarios
    private UsuariosDAO usuariosDAO; 
    // Objeto Usuario que representa el usuario actual en el formulario
    private usuarios usuarios;
    // Lista de todos los usuariosList
    private List<usuarios> usuariosList; 
    // Índice del cliente actual
    private int usuarioIndex = 0;  

    /**
    * Clase para modificar los usuarios de nuestra APP
    *
    * @author Moisés López Vega
     * @param parent ventana padre desde la que se abre el diálogo
     * @param modal indica si el diálogo es modal (bloquea la interacción con otras ventanas)
    */
    public modificarUsuarios(java.awt.Frame parent, boolean modal) {
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
        // Obtener todos los usuariosList de la base de datos
        usuariosList = usuariosDAO.obtenerTodosLosUsuarios();  
        // Bloqueamos el nº de usuario para que no sea modificable
        nusuarioCajaTexto.setEnabled(false);
        // Cargar el primer usuario
        cargarUsuario(usuariosList.get(usuarioIndex));

        // Acción del botón Guardar
        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    guardarUsuarioActionPerformed(evt);
                } catch (SQLException ex) {
                    Logger.getLogger(modificarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Agregar MouseListener a jLabelDerecha para ir al siguiente usuario
        jLabelDerecha.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (usuarioIndex < usuariosList.size() - 1) {
                    usuarioIndex++;
                    cargarUsuario(usuariosList.get(usuarioIndex));
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
        
        // Método para que si el objeto seleccionado es distinto a 0 se ponga en blanco.
        jComboBoxRol.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (jComboBoxRol.getSelectedIndex() != 0) { // si no es el valor por defecto
                    jComboBoxRol.setBackground(Color.WHITE);
                }
            }
        });

        // Agregar MouseListener a jLabelIzquierda para ir al usuario anterior
        jLabelIzquierda.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (usuarioIndex > 0) {
                    usuarioIndex--;
                    cargarUsuario(usuariosList.get(usuarioIndex));
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
        maestros.consultarAyuda(jLabelAyuda, "modificar_usuarios");
    }
    
    // Establecer los campos del formulario con los datos del usuario
    private void cargarUsuario(usuarios usuarios) {
        nusuarioCajaTexto.setText(String.valueOf(usuarios.getId()));
        nombreCajaTexto.setText(usuarios.getNombre());
        apellidosCajaTexto.setText(usuarios.getApellidos());
        jComboBoxRol.setSelectedItem(usuarios.getRol());
        usuarioCajaTexto.setText(usuarios.getUser());
        claveCajaTexto.setText(usuarios.getPassword());
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
            // Comprueba clave
            if (!claveCajaTexto.getText().isEmpty()) {
                usuarios.setUser(claveCajaTexto.getText());
            } else {
                claveCajaTexto.setBackground(Color.RED);
                esValido = false;
            }
            // Comprueba usuario
            if (!usuarioCajaTexto.getText().isEmpty()) {
                usuarios.setPassword(usuarioCajaTexto.getText());
            } else {
                usuarioCajaTexto.setBackground(Color.RED);
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
                usuariosDAO.actualizarUsuario(usuarios);
                JOptionPane.showMessageDialog(null, "Usuario actualizado correctamente.", "Todo ha ido bien", JOptionPane.INFORMATION_MESSAGE);
                // Obtener todos los usuariosList de la base de datos
                usuariosList = usuariosDAO.obtenerTodosLosUsuarios();  
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
                modificarUsuarios.this.dispose();
            } else {
                // Si no confirma, simplemente cerramos el frame auxiliar
                frame.dispose();
            }
        } else {
            // Si no hay datos, cerramos directamente el formulario
            modificarUsuarios.this.dispose();
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
        botonBuscar = new javax.swing.JButton();
        jTextFieldBuscar = new javax.swing.JTextField();
        jLabelNusuario2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ReplyCar - Modificar Usuario");

        jLabelSalir.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelSalir.setForeground(new java.awt.Color(0, 0, 204));
        jLabelSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/salir.png"))); // NOI18N
        jLabelSalir.setText("Salir");
        jLabelSalir.setToolTipText("Salir del formulario");

        jLabelAyuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/ayuda.png"))); // NOI18N
        jLabelAyuda.setToolTipText("Consultar ayuda para este formulario");

        jLabelTitulo.setFont(new java.awt.Font("Tempus Sans ITC", 1, 36)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 204));
        jLabelTitulo.setText("Modificar Usuario");

        jLabelIzquierda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/izquierda.png"))); // NOI18N
        jLabelIzquierda.setToolTipText("Anterior Usuario");

        jLabelDerecha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/derecha.png"))); // NOI18N
        jLabelDerecha.setToolTipText("Usuario Siguiente");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabelIzquierda, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelDerecha, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
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
        botonGuardar.setText("Guardar Usuario Modificado");
        botonGuardar.setToolTipText("Pincha para guardar el usuario modificado");

        botonBuscar.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        botonBuscar.setForeground(new java.awt.Color(0, 0, 204));
        botonBuscar.setText("Buscar");
        botonBuscar.setToolTipText("Pincha para buscar el usuario");
        botonBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBuscarActionPerformed(evt);
            }
        });

        jTextFieldBuscar.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jTextFieldBuscar.setForeground(new java.awt.Color(0, 0, 204));
        jTextFieldBuscar.setToolTipText("Ingresa nombre del usuario");
        jTextFieldBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldBuscarKeyTyped(evt);
            }
        });

        jLabelNusuario2.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabelNusuario2.setForeground(new java.awt.Color(0, 0, 204));
        jLabelNusuario2.setText("(Nº usuario):");

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

    // Método para guardar un usuario nuevo en la base de datos
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
            if (usuarioIndex <= 0 || usuarioIndex > usuariosList.size()) { // <= 0 descarta el usuario 0
                JOptionPane.showMessageDialog(null, "Número de usuario no inexistente.", "Error en la búsqueda", JOptionPane.ERROR_MESSAGE);
                jTextFieldBuscar.setBackground(Color.RED);
                esValido = false;
            } else {
                // Si el índice es válido, cargar el usuario
                cargarUsuario(usuariosList.get(usuarioIndex - 1)); // Ajuste de índice para reflejar la posición en la lista
            }
        }

    }//GEN-LAST:event_botonBuscarActionPerformed

    private void jTextFieldBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldBuscarKeyTyped
        // Restablece el color de fondo a blanco al escribir
        jTextFieldBuscar.setBackground(Color.WHITE);
        // Si el texto supera los 4 caracteres, no permite seguir escribiendo
        if (jTextFieldBuscar.getText().length() > 4) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldBuscarKeyTyped

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
            java.util.logging.Logger.getLogger(modificarUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(modificarUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(modificarUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(modificarUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                modificarUsuarios dialog = new modificarUsuarios(new javax.swing.JFrame(), false);
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
    private javax.swing.JButton botonBuscar;
    private javax.swing.JButton botonGuardar;
    private javax.swing.JTextField claveCajaTexto;
    private javax.swing.JComboBox<String> jComboBoxRol;
    private javax.swing.JLabel jLabelApellidos;
    private javax.swing.JLabel jLabelAyuda;
    private javax.swing.JLabel jLabelClave;
    private javax.swing.JLabel jLabelDerecha;
    private javax.swing.JLabel jLabelIzquierda;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JLabel jLabelNusuario;
    private javax.swing.JLabel jLabelNusuario2;
    private javax.swing.JLabel jLabelRol;
    private javax.swing.JLabel jLabelSalir;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelUsuario;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jTextFieldBuscar;
    private javax.swing.JTextField nombreCajaTexto;
    private javax.swing.JTextField nusuarioCajaTexto;
    private javax.swing.JTextField usuarioCajaTexto;
    // End of variables declaration//GEN-END:variables
}
