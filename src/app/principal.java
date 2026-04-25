package app;

/*Clases importadas para la utilización en la clase.
 */
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * Clase principal donde se aloja el login, el acceso al menú, etc...
 *
 * @author Moisés López Vega
 */
public class principal extends javax.swing.JFrame {

    // Declaramos el objeto MaestroDAO para manejar operaciones de base de datos relacionadas con los métodos reutilizables
    private MaestroDAO maestroDAO;

    /**
     * Creamos el formulario para pantalla principal, Constructor de la clase
     * principal
     *
     */
    public principal() {
        // Inicializa los componentes visuales de la interfaz gráfica
        initComponents();
        // Inicializamos el objeto maestroDAO para operaciones con los métodos reutilizables en BD
        maestroDAO = new MaestroDAO();
        // Como principal requisito, el menú queda desactivado hasta que se logee el usuario
        deshabilitarMenu();

        // Agregar MouseListener a jLabelAyuda para abrir la ayuda de la app
        maestros.consultarAyuda(jLabelAyuda, "aplicacion");

        // Método que permite la acción para entrar a la app, verificando el logeeo de la app
        jButtonEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Usuario y contraseña de la app / usuario
                    String usuario = jTextFieldUsuario.getText();
                    String contrasena = new String(jPasswordField.getPassword());
                    // Validar el login en la base de datos
                    String rol = maestroDAO.validarLogin(usuario, contrasena);
                    // Verificar el login
                    if (rol != null) {
                        // Login exitoso, habilitar menú dependiendo del rol
                        JOptionPane.showMessageDialog(null, "Autentificación efectuada con exito.", "Datos correctos", JOptionPane.INFORMATION_MESSAGE);
                        // Se desactiva el login, una vez logeado el usuario
                        desactivarLogin();
                        // Habilita el menú según el login
                        habilitarMenu(rol);
                        jLabel2.setText("Se ha conectado con rol de: " + rol);
                    } else {
                        // Login fallido, mostrar mensaje de error
                        JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos.", "Datos incorrectos", JOptionPane.ERROR_MESSAGE);
                    }
                    // Capturamos error a la hora de logearse
                } catch (SQLException ex) {
                    Logger.getLogger(principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // El item20 tiene la acción de deslogearse del sistema
        jMenuItem20.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                desloguearse();
            }
        });
    }

    // Método para salir de la app y deslogearse
    private void desloguearse() {
        // Mostrar un mensaje de confirmación
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas cerrar sesión?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
        // Valida la desahilitación del menú
        if (respuesta == JOptionPane.YES_OPTION) {
            // Deshabilitar el menú
            deshabilitarMenu();
            jLabel2.setText("");
            // Se vuelve a activar el login
            activarLogin();
            // Volver al formulario de login
            JOptionPane.showMessageDialog(this, "Sesión cerrada correctamente. Por favor, inicia sesión nuevamente.", "Sesión cerrada", JOptionPane.INFORMATION_MESSAGE);
            // Enfoca el cursor en el campo de usuario
            jTextFieldUsuario.requestFocus();
        }
    }

    // Método para desactivar Login
    private void desactivarLogin() {
        jTextFieldUsuario.setEnabled(false);
        jPasswordField.setEnabled(false);
        jButtonEntrar.setEnabled(false);
    }

    // Método para activar Login
    private void activarLogin() {
        jTextFieldUsuario.setEnabled(true);
        jPasswordField.setEnabled(true);
        jButtonEntrar.setEnabled(true);
        jTextFieldUsuario.setText("");
        jPasswordField.setText("");
    }

    // Método para activar módulo de Comercial
    private void activarModuloComercial() {
        for (int i = 0; i < jMenu7.getMenuComponentCount(); i++) {
            Component comp = jMenu7.getMenuComponent(i);
            if (comp instanceof JMenu) {
                JMenu menu = (JMenu) comp;
                for (int j = 0; j < menu.getItemCount(); j++) {
                    JMenuItem item = menu.getItem(j);
                    if (item != null) {
                        item.setEnabled(true);
                    }
                }
            } else if (comp instanceof JMenuItem) {
                comp.setEnabled(true);
            }
        }
    }

    // Método para activar módulo de Clientes
    private void activarModuloClientes() {
        for (int i = 0; i < jMenu2.getMenuComponentCount(); i++) {
            Component comp = jMenu2.getMenuComponent(i);
            if (comp instanceof JMenu) {
                JMenu menu = (JMenu) comp;
                for (int j = 0; j < menu.getItemCount(); j++) {
                    JMenuItem item = menu.getItem(j);
                    if (item != null) {
                        item.setEnabled(true);
                    }
                }
            } else if (comp instanceof JMenuItem) {
                comp.setEnabled(true);
            }
        }
    }

    // Método para activar módulo de Proveedores
    private void activarModuloProveedores() {
        for (int i = 0; i < jMenu3.getMenuComponentCount(); i++) {
            Component comp = jMenu3.getMenuComponent(i);
            if (comp instanceof JMenu) {
                JMenu menu = (JMenu) comp;
                for (int j = 0; j < menu.getItemCount(); j++) {
                    JMenuItem item = menu.getItem(j);
                    if (item != null) {
                        item.setEnabled(true);
                    }
                }
            } else if (comp instanceof JMenuItem) {
                comp.setEnabled(true);
            }
        }
    }

    // Método para activar módulo de Almacén
    private void activarModuloAlmacen() {
        for (int i = 0; i < jMenu4.getMenuComponentCount(); i++) {
            Component comp = jMenu4.getMenuComponent(i);
            if (comp instanceof JMenu) {
                JMenu menu = (JMenu) comp;
                for (int j = 0; j < menu.getItemCount(); j++) {
                    JMenuItem item = menu.getItem(j);
                    if (item != null) {
                        item.setEnabled(true);
                    }
                }
            } else if (comp instanceof JMenuItem) {
                comp.setEnabled(true);
            }
        }
    }

    // Método para activar módulo de Taller
    private void activarModuloTaller() {
        for (int i = 0; i < jMenu1.getMenuComponentCount(); i++) {
            Component comp = jMenu1.getMenuComponent(i);
            if (comp instanceof JMenu) {
                JMenu menu = (JMenu) comp;
                for (int j = 0; j < menu.getItemCount(); j++) {
                    JMenuItem item = menu.getItem(j);
                    if (item != null) {
                        item.setEnabled(true);
                    }
                }
            } else if (comp instanceof JMenuItem) {
                comp.setEnabled(true);
            }
        }
    }

    // Método para activar módulo de Facturas
    private void activarModuloFacturas() {
        for (int i = 0; i < jMenu5.getMenuComponentCount(); i++) {
            Component comp = jMenu5.getMenuComponent(i);
            if (comp instanceof JMenu) {
                JMenu menu = (JMenu) comp;
                for (int j = 0; j < menu.getItemCount(); j++) {
                    JMenuItem item = menu.getItem(j);
                    if (item != null) {
                        item.setEnabled(true);
                    }
                }
            } else if (comp instanceof JMenuItem) {
                comp.setEnabled(true);
            }
        }
    }

    // Método para activar la visibilidad de las facturas de comercial
    private void activarFacturasComercial() {
        jMenuItem18.setEnabled(true);
    }

    // Método para activar la visibilidad de las facturas de postventa
    private void activarFacturasPostventa() {
        jMenuItem15.setEnabled(true);
        jMenuItem17.setEnabled(true);
    }

    // Método para activar módulo de Menú
    private void activarModuloMenu() {
        for (int i = 0; i < jMenu8.getMenuComponentCount(); i++) {
            Component comp = jMenu8.getMenuComponent(i);
            if (comp instanceof JMenu) {
                JMenu menu = (JMenu) comp;
                for (int j = 0; j < menu.getItemCount(); j++) {
                    JMenuItem item = menu.getItem(j);
                    if (item != null) {
                        item.setEnabled(true);
                    }
                }
            } else if (comp instanceof JMenuItem) {
                comp.setEnabled(true);
            }
        }
    }

    // Método para deshabilitar los elementos del menú
    private void deshabilitarMenu() {
        jLabelAyuda.setEnabled(false);
        for (int i = 0; i < jMenuBar1.getMenuCount(); i++) {
            JMenu menu = jMenuBar1.getMenu(i);
            for (int j = 0; j < menu.getItemCount(); j++) {
                JMenuItem item = menu.getItem(j);
                if (item != null) {
                    item.setEnabled(false);
                }
            }
        }
    }

    // Método para habilitar el menú dependiendo del rol
    private void habilitarMenu(String rol) {
        jLabelAyuda.setEnabled(true);
        if (rol.equals("Administrador")) {
            for (int i = 0; i < jMenuBar1.getMenuCount(); i++) {
                JMenu menu = jMenuBar1.getMenu(i);
                for (int j = 0; j < menu.getItemCount(); j++) {
                    JMenuItem item = menu.getItem(j);
                    if (item != null) {
                        item.setEnabled(true);
                    }
                }
            }
        } else if (rol.equals("Usuario Avanzado")) {
            activarModuloComercial();
            activarModuloClientes();
            activarModuloProveedores();
            activarModuloAlmacen();
            activarModuloTaller();
            activarModuloFacturas();
            activarModuloMenu();
        } else if (rol.equals("Usuario Comercial")) {
            activarModuloComercial();
            activarModuloClientes();
            activarModuloProveedores();
            activarModuloMenu();
            activarFacturasComercial();
        } else if (rol.equals("Usuario Postventa")) {
            activarModuloClientes();
            activarModuloProveedores();
            activarModuloAlmacen();
            activarModuloTaller();
            activarModuloMenu();
            activarFacturasPostventa();
        } else {
            deshabilitarMenu();
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
        jLabel1 = new javax.swing.JLabel();
        jTextFieldUsuario = new javax.swing.JTextField();
        jLabelUsuario = new javax.swing.JLabel();
        jLabelPass = new javax.swing.JLabel();
        jButtonEntrar = new javax.swing.JButton();
        jPasswordField = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        jLabelAyuda = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenu9 = new javax.swing.JMenu();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenuItem27 = new javax.swing.JMenuItem();
        jMenu10 = new javax.swing.JMenu();
        jMenuItem28 = new javax.swing.JMenuItem();
        jMenuItem29 = new javax.swing.JMenuItem();
        jMenuItem30 = new javax.swing.JMenuItem();
        jMenu11 = new javax.swing.JMenu();
        jMenuItem31 = new javax.swing.JMenuItem();
        jMenuItem32 = new javax.swing.JMenuItem();
        jMenuItem33 = new javax.swing.JMenuItem();
        jMenu12 = new javax.swing.JMenu();
        jMenuItem34 = new javax.swing.JMenuItem();
        jMenuItem35 = new javax.swing.JMenuItem();
        jMenuItem36 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ReplyCar v.1.0 - by Moisés López Vega");
        setResizable(false);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/fondos/principal.jpg"))); // NOI18N
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jTextFieldUsuario.setToolTipText("Ingresa el usuario");

        jLabelUsuario.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabelUsuario.setForeground(new java.awt.Color(255, 255, 255));
        jLabelUsuario.setText("Usuario:");

        jLabelPass.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabelPass.setForeground(new java.awt.Color(255, 255, 255));
        jLabelPass.setText("Contraseña:");

        jButtonEntrar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonEntrar.setText("Entrar");
        jButtonEntrar.setToolTipText("Pulse para entrar y logearse con sus datos");

        jPasswordField.setToolTipText("Ingresa la clave de acceso");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));

        jLabelAyuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/ayudaRojo.png"))); // NOI18N
        jLabelAyuda.setToolTipText("Consultar ayuda para este formulario");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(235, 235, 235)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabelUsuario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButtonEntrar)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabelPass)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelAyuda)
                .addGap(15, 15, 15))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1285, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelAyuda)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelUsuario)
                    .addComponent(jTextFieldUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelPass)
                    .addComponent(jPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jButtonEntrar)
                .addContainerGap(683, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 856, Short.MAX_VALUE))
        );

        jMenu7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/comercial.png"))); // NOI18N
        jMenu7.setText("Comercial");
        jMenu7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jMenuItem9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/anadir.png"))); // NOI18N
        jMenuItem9.setText("Comprar Veh.");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem9);

        jMenuItem10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/facturar.png"))); // NOI18N
        jMenuItem10.setText("Vender Veh.");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem10);

        jMenuItem11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/consulta.png"))); // NOI18N
        jMenuItem11.setText("Listado Stock");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem11);

        jMenuItem13.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/consulta.png"))); // NOI18N
        jMenuItem13.setText("Listado Facturados");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem13);

        jMenuItem12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/excel.png"))); // NOI18N
        jMenuItem12.setText("Actualizar Tarifa");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem12);

        jMenuBar1.add(jMenu7);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/clientes.png"))); // NOI18N
        jMenu2.setText("Clientes");
        jMenu2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jMenuItem1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/anadir.png"))); // NOI18N
        jMenuItem1.setText("Nuevo");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuItem2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/consulta.png"))); // NOI18N
        jMenuItem2.setText("Consulta");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/modificar.png"))); // NOI18N
        jMenuItem3.setText("Modificar");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/proveedores.png"))); // NOI18N
        jMenu3.setText("Proveedores");
        jMenu3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jMenuItem4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/anadir.png"))); // NOI18N
        jMenuItem4.setText("Nuevo");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuItem5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/consulta.png"))); // NOI18N
        jMenuItem5.setText("Consulta");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuItem6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/modificar.png"))); // NOI18N
        jMenuItem6.setText("Modificar");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuBar1.add(jMenu3);

        jMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/almacen.png"))); // NOI18N
        jMenu4.setText("Almacen");
        jMenu4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jMenuItem7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/anadir.png"))); // NOI18N
        jMenuItem7.setText("Comprar");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem7);

        jMenuItem8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/comercio.png"))); // NOI18N
        jMenuItem8.setText("Vender");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem8);

        jMenuBar1.add(jMenu4);

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/taller.png"))); // NOI18N
        jMenu1.setText("Taller");
        jMenu1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jMenuItem14.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/comercio.png"))); // NOI18N
        jMenuItem14.setText("Fac. Intervención");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem14);

        jMenuBar1.add(jMenu1);

        jMenu5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/facturas.png"))); // NOI18N
        jMenu5.setText("Facturas");
        jMenu5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jMenuItem15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/factura.png"))); // NOI18N
        jMenuItem15.setText("Taller");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem15);

        jMenuItem17.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/factura.png"))); // NOI18N
        jMenuItem17.setText("Almacen");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem17);

        jMenuItem18.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/factura.png"))); // NOI18N
        jMenuItem18.setText("Vehículos");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem18);

        jMenuBar1.add(jMenu5);

        jMenu8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/maestro.png"))); // NOI18N
        jMenu8.setText("Menú");
        jMenu8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jMenuItem19.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/acercade.png"))); // NOI18N
        jMenuItem19.setText("Acerca de");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem19);

        jMenuItem20.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/enchufe.png"))); // NOI18N
        jMenuItem20.setText("Cerrar sesión");
        jMenu8.add(jMenuItem20);

        jMenuBar1.add(jMenu8);

        jMenu6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/superUser.png"))); // NOI18N
        jMenu6.setText("Super US");
        jMenu6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jMenu9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/usuario.png"))); // NOI18N
        jMenu9.setText("Usuarios");
        jMenu9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jMenuItem25.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/anadir.png"))); // NOI18N
        jMenuItem25.setText("Nuevo");
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem25);

        jMenuItem26.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/consulta.png"))); // NOI18N
        jMenuItem26.setText("Consulta / Borrar");
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem26);

        jMenuItem27.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/modificar.png"))); // NOI18N
        jMenuItem27.setText("Modificar");
        jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem27);

        jMenu6.add(jMenu9);

        jMenu10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/usuario.png"))); // NOI18N
        jMenu10.setText("Familias");
        jMenu10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jMenuItem28.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/anadir.png"))); // NOI18N
        jMenuItem28.setText("Nuevo");
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem28);

        jMenuItem29.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/consulta.png"))); // NOI18N
        jMenuItem29.setText("Consulta / Borrar");
        jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem29ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem29);

        jMenuItem30.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/modificar.png"))); // NOI18N
        jMenuItem30.setText("Modificar");
        jMenuItem30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem30ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem30);

        jMenu6.add(jMenu10);

        jMenu11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/usuario.png"))); // NOI18N
        jMenu11.setText("Forma de Pago");
        jMenu11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jMenuItem31.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/anadir.png"))); // NOI18N
        jMenuItem31.setText("Nuevo");
        jMenuItem31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem31ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem31);

        jMenuItem32.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/consulta.png"))); // NOI18N
        jMenuItem32.setText("Consulta / Borrar");
        jMenuItem32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem32ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem32);

        jMenuItem33.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/modificar.png"))); // NOI18N
        jMenuItem33.setText("Modificar");
        jMenuItem33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem33);

        jMenu6.add(jMenu11);

        jMenu12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/usuario.png"))); // NOI18N
        jMenu12.setText("Tarifa Taller");
        jMenu12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jMenuItem34.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/anadir.png"))); // NOI18N
        jMenuItem34.setText("Nuevo");
        jMenuItem34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem34ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem34);

        jMenuItem35.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/consulta.png"))); // NOI18N
        jMenuItem35.setText("Consulta / Borrar");
        jMenuItem35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem35ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem35);

        jMenuItem36.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/modificar.png"))); // NOI18N
        jMenuItem36.setText("Modificar");
        jMenuItem36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem36ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem36);

        jMenu6.add(jMenu12);

        jMenuItem21.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenuItem21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/iconos/acercade.png"))); // NOI18N
        jMenuItem21.setText("Config. B.D.");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem21);

        jMenuBar1.add(jMenu6);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // Nuevos Clientes:
        // Botón, acción para abrir la pantalla de "nuevos clientes o introducir clientes".
        nuevosClientes nClientes = new nuevosClientes(this, false);
        // No se puede redimensionar
        nClientes.setResizable(false);
        // Centrar sobre la ventana principal
        nClientes.setLocationRelativeTo(this);
        // La hacemos visible
        nClientes.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // Listado Clientes:
        // Botón, acción para abrir la pantalla de "listado clientes o consultas de clientes"
        listadoClientes lClientes = new listadoClientes(this, false);
        // No se puede redimensionar
        lClientes.setResizable(false);
        // Centrar sobre la ventana principal
        lClientes.setLocationRelativeTo(this);
        // La hacemos visible
        lClientes.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // Modificación Clientes:
        // Botón, acción para abrir la pantalla de "modificación de clientes"
        modificarClientes mClientes = new modificarClientes(this, false);
        // No se puede redimensionar
        mClientes.setResizable(false);
        // Centrar sobre la ventana principal
        mClientes.setLocationRelativeTo(this);
        // La hacemos visible
        mClientes.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // Nuevos Proveedores:
        // Botón, acción para abrir la pantalla de "nuevos proveedores o introducir proveedores"
        nuevosProveedores nProveedores = new nuevosProveedores(this, false);
        // No se puede redimensionar
        nProveedores.setResizable(false);
        // Centrar sobre la ventana principal
        nProveedores.setLocationRelativeTo(this);
        // La hacemos visible
        nProveedores.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // Listado Proveedores:
        // Botón, acción para abrir la pantalla de "listado proveedores o consultas de proveedores"
        listadoProveedores lProveedores = new listadoProveedores(this, false);
        // No se puede redimensionar
        lProveedores.setResizable(false);
        // Centrar sobre la ventana principal
        lProveedores.setLocationRelativeTo(this);
        // La hacemos visible
        lProveedores.setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // Modificación Proveedores:
        // Botón, acción para abrir la pantalla de "modificación de proveedores"
        modificarProveedores mProveedores = new modificarProveedores(this, false);
        // No se puede redimensionar
        mProveedores.setResizable(false);
        // Centrar sobre la ventana principal
        mProveedores.setLocationRelativeTo(this);
        // La hacemos visible
        mProveedores.setVisible(true);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // Nuevo artículo en almacen:
        // Botón, acción para abrir la pantalla de "comprar nueva artículo"
        nuevosArticulos nArticulos = null;
        try {
            nArticulos = new nuevosArticulos(this, false);
        } catch (SQLException ex) {
            Logger.getLogger(principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        // No se puede redimensionar
        nArticulos.setResizable(false);
        // Centrar sobre la ventana principal
        nArticulos.setLocationRelativeTo(this);
        // La hacemos visible
        nArticulos.setVisible(true);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // Venta artículo de almacen:
        // Botón, acción para abrir la pantalla de "venta artículo"
        ventaArticulos vArticulos = new ventaArticulos(this, false);
        // No se puede redimensionar
        vArticulos.setResizable(false);
        // Centrar sobre la ventana principal
        vArticulos.setLocationRelativeTo(this);
        // La hacemos visible
        vArticulos.setVisible(true);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        // Nuevo vehículo:
        // Botón, acción para abrir la pantalla de "comprar nueva vehículo"
        nuevosVehiculos vVehiculos = null;
        try {
            vVehiculos = new nuevosVehiculos(this, false);
        } catch (SQLException ex) {
            Logger.getLogger(principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        // No se puede redimensionar
        vVehiculos.setResizable(false);
        // Centrar sobre la ventana principal
        vVehiculos.setLocationRelativeTo(this);
        // La hacemos visible
        vVehiculos.setVisible(true);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // Facturar vehículo:
        // Botón, acción para abrir la pantalla de "facturar nuevo vehículo"
        ventaVehiculos vVehiculos = null;
        try {
            vVehiculos = new ventaVehiculos(this, false);
        } catch (SQLException ex) {
            Logger.getLogger(principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        // No se puede redimensionar
        vVehiculos.setResizable(false);
        // Centrar sobre la ventana principal
        vVehiculos.setLocationRelativeTo(this);
        // La hacemos visible
        vVehiculos.setVisible(true);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        // Mostrar listado de vehículos en Stock:
        // Botón, acción para abrir la pantalla de "listado stock"
        listadoStock vStock = new listadoStock(this, false);
        // No se puede redimensionar
        vStock.setResizable(false);
        // Centrar sobre la ventana principal
        vStock.setLocationRelativeTo(this);
        // La hacemos visible
        vStock.setVisible(true);
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        try {
            // Ejecutamos la actualización de tarifas de vehículos desde maestroDAO.
            maestroDAO.actualizarTarifaVehiculos();
        } catch (IOException ex) {
            // Si ocurre una excepción IOException, muestra un mensaje indicando que no se encontró el fichero.
            JOptionPane.showMessageDialog(null, "Fichero no encontrado, comprueba ruta.", "Error en la ruta", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            // Si ocurre una excepción SQLException, muestra un mensaje indicando que hubo un problema en la conexión a la base de datos.
            JOptionPane.showMessageDialog(null, "Hay algún tipo de error en la conexión.", "Error con la base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        // Mostrar listado de vehículos facturados:
        // Botón, acción para abrir la pantalla de "listado vehículos facturados"
        listadoFacturados vFacturados = new listadoFacturados(this, false);
        // No se puede redimensionar
        vFacturados.setResizable(false);
        // Centrar sobre la ventana principal
        vFacturados.setLocationRelativeTo(this);
        // La hacemos visible
        vFacturados.setVisible(true);
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        try {
            // Facturar intervención:
            // Botón, acción para abrir la pantalla de "facturar intervención"
            ventaTaller vTaller = new ventaTaller(this, false);
            // No se puede redimensionar
            vTaller.setResizable(false);
            // Centrar sobre la ventana principal
            vTaller.setLocationRelativeTo(this);
            // La hacemos visible
            vTaller.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        // Mostrar listado de facturas de taller:
        // Botón, acción para abrir la pantalla de "facturas taller"
        listadoFacturasTaller fTaller = new listadoFacturasTaller(this, false);
        // No se puede redimensionar
        fTaller.setResizable(false);
        // Centrar sobre la ventana principal
        fTaller.setLocationRelativeTo(this);
        // La hacemos visible
        fTaller.setVisible(true);
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        // Mostrar listado de facturas de almacén:
        // Botón, acción para abrir la pantalla de "facturas almacén"
        listadoFacturasAlmacen fAlmacen = new listadoFacturasAlmacen(this, false);
        // No se puede redimensionar
        fAlmacen.setResizable(false);
        // Centrar sobre la ventana principal
        fAlmacen.setLocationRelativeTo(this);
        // La hacemos visible
        fAlmacen.setVisible(true);
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        // Mostrar listado de facturas de vehículos:
        // Botón, acción para abrir la pantalla de "facturas vehículos"
        listadoFacturasVehiculos fVehiculos = new listadoFacturasVehiculos(this, false);
        // No se puede redimensionar
        fVehiculos.setResizable(false);
        // Centrar sobre la ventana principal
        fVehiculos.setLocationRelativeTo(this);
        // La hacemos visible
        fVehiculos.setVisible(true);
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        // Botón, acción para abrir la pantalla de "acerca de"
        acercade vacercade = new acercade(this, false);
        // No se puede redimensionar
        vacercade.setResizable(false);
        // Centrar sobre la ventana principal
        vacercade.setLocationRelativeTo(this);
        // La hacemos visible
        vacercade.setVisible(true);
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
        // Mostrar formulario para ingresar nuevos usuarios.
        nuevosUsuarios nUsuarios = new nuevosUsuarios(this, false);
        // No se puede redimensionar
        nUsuarios.setResizable(false);
        // Centrar sobre la ventana principal
        nUsuarios.setLocationRelativeTo(this);
        // La hacemos visible
        nUsuarios.setVisible(true);
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
        // Mostrar formulario para consultar y borrar usuarios.
        listadoUsuarios cUsuarios = new listadoUsuarios(this, false);
        // No se puede redimensionar
        cUsuarios.setResizable(false);
        // Centrar sobre la ventana principal
        cUsuarios.setLocationRelativeTo(this);
        // La hacemos visible
        cUsuarios.setVisible(true);
    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed
        // Mostrar formulario para modificar usuarios.
        modificarUsuarios mUsuarios = new modificarUsuarios(this, false);
        // No se puede redimensionar
        mUsuarios.setResizable(false);
        // Centrar sobre la ventana principal
        mUsuarios.setLocationRelativeTo(this);
        // La hacemos visible
        mUsuarios.setVisible(true);
    }//GEN-LAST:event_jMenuItem27ActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
        // Mostrar formulario para ingresar nuevas familias.
        nuevasFamilias nFamilias = new nuevasFamilias(this, false);
        // No se puede redimensionar
        nFamilias.setResizable(false);
        // Centrar sobre la ventana principal
        nFamilias.setLocationRelativeTo(this);
        // La hacemos visible
        nFamilias.setVisible(true);
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed
        // Mostrar formulario para consultar y borrar familias.
        listadoFamilia cFamilias = new listadoFamilia(this, false);
        // No se puede redimensionar
        cFamilias.setResizable(false);
        // Centrar sobre la ventana principal
        cFamilias.setLocationRelativeTo(this);
        // La hacemos visible
        cFamilias.setVisible(true);
    }//GEN-LAST:event_jMenuItem29ActionPerformed

    private void jMenuItem30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem30ActionPerformed
        // Mostrar formulario para modificar familias.
        modificarFamilia mFamilias = new modificarFamilia(this, false);
        // No se puede redimensionar
        mFamilias.setResizable(false);
        // Centrar sobre la ventana principal
        mFamilias.setLocationRelativeTo(this);
        // La hacemos visible
        mFamilias.setVisible(true);
    }//GEN-LAST:event_jMenuItem30ActionPerformed

    private void jMenuItem31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem31ActionPerformed
        // Mostrar formulario para ingresar nuevas formas de pago.
        nuevasFormasPago nFormaPago = new nuevasFormasPago(this, false);
        // No se puede redimensionar
        nFormaPago.setResizable(false);
        // Centrar sobre la ventana principal
        nFormaPago.setLocationRelativeTo(this);
        // La hacemos visible
        nFormaPago.setVisible(true);
    }//GEN-LAST:event_jMenuItem31ActionPerformed

    private void jMenuItem32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem32ActionPerformed
        // Mostrar formulario para consultar y borrar formas de pago.
        listadoFormasPago cFormaPago = new listadoFormasPago(this, false);
        // No se puede redimensionar
        cFormaPago.setResizable(false);
        // Centrar sobre la ventana principal
        cFormaPago.setLocationRelativeTo(this);
        // La hacemos visible
        cFormaPago.setVisible(true);
    }//GEN-LAST:event_jMenuItem32ActionPerformed

    private void jMenuItem33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem33ActionPerformed
        // Mostrar formulario para modificar formas de pago.
        modificarFormasPago mFormaPago = new modificarFormasPago(this, false);
        // No se puede redimensionar
        mFormaPago.setResizable(false);
        // Centrar sobre la ventana principal
        mFormaPago.setLocationRelativeTo(this);
        // La hacemos visible
        mFormaPago.setVisible(true);
    }//GEN-LAST:event_jMenuItem33ActionPerformed

    private void jMenuItem34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem34ActionPerformed
        // Nuevas Tarifas de Taller:
        // Botón, acción para abrir la pantalla de "nuevas tarifas de taller".
        nuevasTarifasTaller nTarifasTaller = new nuevasTarifasTaller(this, false);
        // No se puede redimensionar
        nTarifasTaller.setResizable(false);
        // Centrar sobre la ventana principal
        nTarifasTaller.setLocationRelativeTo(this);
        // La hacemos visible
        nTarifasTaller.setVisible(true);
    }//GEN-LAST:event_jMenuItem34ActionPerformed

    private void jMenuItem35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem35ActionPerformed
        // Consulta Tarifas de Taller:
        // Botón, acción para abrir la pantalla de "consulta de las tarifas de taller".
        listadoTarifasTaller lTarifasTaller = new listadoTarifasTaller(this, false);
        // No se puede redimensionar
        lTarifasTaller.setResizable(false);
        // Centrar sobre la ventana principal
        lTarifasTaller.setLocationRelativeTo(this);
        // La hacemos visible
        lTarifasTaller.setVisible(true);
    }//GEN-LAST:event_jMenuItem35ActionPerformed

    private void jMenuItem36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem36ActionPerformed
        // Modificar Tarifas de Taller:
        // Botón, acción para abrir la pantalla de "modificación de las tarifas de taller".
        modificarTarifasTaller mTarifasTaller = new modificarTarifasTaller(this, false);
        // No se puede redimensionar
        mTarifasTaller.setResizable(false);
        // Centrar sobre la ventana principal
        mTarifasTaller.setLocationRelativeTo(this);
        // La hacemos visible
        mTarifasTaller.setVisible(true);
    }//GEN-LAST:event_jMenuItem36ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        // Modificar configuración de la base de datos (conexión):
        // Botón, acción para abrir la pantalla de "modificación config. base de datos.
        configBD mConfigBD = new configBD(this, false);
        // No se puede redimensionar
        mConfigBD.setResizable(false);
        // Centrar sobre la ventana principal
        mConfigBD.setLocationRelativeTo(this);
        // La hacemos visible
        mConfigBD.setVisible(true);
    }//GEN-LAST:event_jMenuItem21ActionPerformed

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
            java.util.logging.Logger.getLogger(principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new principal().setVisible(true);

            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonEntrar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelAyuda;
    private javax.swing.JLabel jLabelPass;
    private javax.swing.JLabel jLabelUsuario;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu12;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem29;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem30;
    private javax.swing.JMenuItem jMenuItem31;
    private javax.swing.JMenuItem jMenuItem32;
    private javax.swing.JMenuItem jMenuItem33;
    private javax.swing.JMenuItem jMenuItem34;
    private javax.swing.JMenuItem jMenuItem35;
    private javax.swing.JMenuItem jMenuItem36;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField;
    private javax.swing.JTextField jTextFieldUsuario;
    // End of variables declaration//GEN-END:variables
}
