package app;

/**
 * Clases importadas para la utilización en la clase.
 */
import java.awt.Cursor;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.help.DefaultHelpBroker;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Clase que representa los maestros de la aplicación.
 *
 * @author Moisés López Vega
 */
public class maestros {

    /**
     * Constructor de la clase maestros. Inicializa la conexión con la base de
     * datos.
     */
    public maestros() {
    }

    /**
     * Filtro de documentos que convierte automáticamente todo el texto
     * ingresado en mayúsculas Esta clase se puede usar en un JTextField o
     * JTextArea para forzar que el usuario solo ingrese texto en mayúsculas
     */
    public static class UppercaseDocumentFilter extends DocumentFilter {

        /**
         * Constructor por defecto Esta clase se usa para filtrar texto y
         * convertirlo a mayúsculas
         */
        public UppercaseDocumentFilter() {
            super();
        }

        /**
         * Inserta texto en el documento convirtiéndolo a mayúsculas
         *
         * @param fb el objeto FilterBypass que permite modificar el contenido
         * del documento
         * @param offset la posición donde se insertará el texto
         * @param text el texto a insertar (se convertirá a mayúsculas)
         * @param attr atributos del texto, como estilo o fuente
         * @throws BadLocationException si la posición de inserción es inválida
         */
        @Override
        public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
                throws BadLocationException {
            if (text != null) {
                super.insertString(fb, offset, text.toUpperCase(), attr);
            }
        }

        /**
         * Reemplaza un rango de texto en el documento convirtiéndolo a
         * mayúsculas
         *
         * @param fb el objeto FilterBypass que permite modificar el contenido
         * del documento
         * @param offset la posición inicial donde se reemplazará el texto
         * @param length la cantidad de caracteres que se reemplazarán
         * @param text el texto de reemplazo (se convertirá a mayúsculas)
         * @param attrs atributos del texto, como estilo o fuente
         * @throws BadLocationException si la posición o longitud del reemplazo
         * son inválidas
         */
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text != null) {
                super.replace(fb, offset, length, text.toUpperCase(), attrs);
            }
        }
    }

    /**
     * Filtro de documentos que convierte automáticamente todo el texto
     * ingresado en minúsculas Esta clase se puede usar en un JTextField o
     * JTextArea para forzar que el usuario solo ingrese texto en minúsculas
     */
    public static class LowercaseDocumentFilter extends DocumentFilter {

        /**
         * Constructor por defecto Esta clase se usa para filtrar texto y
         * convertirlo a minúsculas
         */
        public LowercaseDocumentFilter() {
            super();
        }

        /**
         * Inserta texto en el documento convirtiéndolo a minúsculas
         *
         * @param fb el objeto FilterBypass que permite modificar el contenido
         * del documento
         * @param offset la posición donde se insertará el texto
         * @param text el texto a insertar (se convertirá a minúsculas)
         * @param attr atributos del texto, como estilo o fuente
         * @throws BadLocationException si la posición de inserción es inválida
         */
        @Override
        public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
                throws BadLocationException {
            if (text != null) {
                super.insertString(fb, offset, text.toLowerCase(), attr);
            }
        }

        /**
         * Reemplaza un rango de texto en el documento convirtiéndolo a
         * minúsculas
         *
         * @param fb el objeto FilterBypass que permite modificar el contenido
         * del documento
         * @param offset la posición inicial donde se reemplazará el texto
         * @param length la cantidad de caracteres que se reemplazarán
         * @param text el texto de reemplazo (se convertirá a minúsculas)
         * @param attrs atributos del texto, como estilo o fuente
         * @throws BadLocationException si la posición o longitud del reemplazo
         * son inválidas
         */
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text != null) {
                super.replace(fb, offset, length, text.toLowerCase(), attrs);
            }
        }
    }

    /**
     * Método para consultar la ayuda en cada formulario de la app
     *
     * @param labelAyuda jLabel que se pasa por parámetros
     * @param idAyuda id de la ayuda que se pasa por parámetros
     */
    public static void consultarAyuda(JLabel labelAyuda, String idAyuda) {
        labelAyuda.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!labelAyuda.isEnabled()) {
                    return;
                }
                try {
                    File fichero = new File("./help/help_set.hs");
                    HelpSet helpset = new HelpSet(null, fichero.toURI().toURL());
                    HelpBroker hb = helpset.createHelpBroker();
                    hb.setCurrentID(idAyuda);
                    hb.setDisplayed(true);
                    Window ventanaAyuda = ((DefaultHelpBroker) hb).getWindowPresentation().getHelpWindow();
                    Window ventanaPadre = SwingUtilities.getWindowAncestor(labelAyuda);
                    if (ventanaAyuda != null) {
                        ventanaAyuda.setLocationRelativeTo(ventanaPadre);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (labelAyuda.isEnabled()) {
                    labelAyuda.setCursor(
                            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labelAyuda.setCursor(
                        Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    /**
     * Método para salir de cada formulario de la app
     *
     * @param labelSalir jLabel que se pasa por parámetros
     * @param accionSalir acción que se pasa por parámetros
     */
    public static void salirFormulario(JLabel labelSalir, Runnable accionSalir) {
        labelSalir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                accionSalir.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                labelSalir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labelSalir.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }
}
