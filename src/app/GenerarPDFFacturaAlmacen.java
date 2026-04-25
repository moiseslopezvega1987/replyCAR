package app;

/*Clases importadas para la utilización en la clase.
 */
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.JOptionPane;
import java.util.List;
import org.apache.commons.compress.utils.IOUtils;

/**
 *
 * Esta clase se encarga de generar la factura de almacén en PDF para su
 * exportación
 *
 * @author Moisés López Vega
 */
public class GenerarPDFFacturaAlmacen {
    
    /**
     * Constructor de la clase GenerarPDFFacturaAlmacen. Inicializa la conexión con la base
     * de datos.
     */
    public GenerarPDFFacturaAlmacen() {
    }

    /**
     * Método para generar la factura de almacén en PDF para su exportación
     * @param fac factura de almacén
     * @param cli cliente
     * @param movimientos listado de movimientos
     */
    public void generarFactura(facturasAlmacen fac, cliente cli, List<String[]> movimientos) {
        NumberFormat formato = NumberFormat.getCurrencyInstance(Locale.of("es", "ES"));

        try {
            String rutaArchivo = "C:/facturas/" + fac.getNumero_factura() + ".pdf";

            // Creamos la carpeta sino existe
            File archivo = new File(rutaArchivo);
            File carpeta = archivo.getParentFile();
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, new FileOutputStream(archivo));
            document.open();

            // ================= LOGO =================
            try {
                InputStream is = getClass().getClassLoader().getResourceAsStream("resources/fondos/logo.png");
                if (is == null) {
                    System.out.println("No se encontró el logo dentro del JAR.");
                } else {
                    Image logo = Image.getInstance(IOUtils.toByteArray(is));
                    logo.scaleToFit(180, 90);
                    logo.setAlignment(Image.ALIGN_LEFT);
                    document.add(logo);
                    is.close();
                }
            } catch (Exception e) {
                System.out.println("No se pudo cargar el logo: " + e.getMessage());
            }

            // ================= TÍTULO PRINCIPAL =================
            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph("FACTURA VENTA - ALMACÉN", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);

            // ================= EMPRESA + CLIENTE =================
            PdfPTable tablaCabecera = new PdfPTable(2);
            tablaCabecera.setWidthPercentage(100);
            tablaCabecera.setSpacingAfter(20);
            tablaCabecera.setWidths(new float[]{1, 1});

            Font fontCabecera = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
            Font fontNormal = new Font(Font.FontFamily.HELVETICA, 10);

            // Empresa
            PdfPTable tablaEmpresa = new PdfPTable(1);
            tablaEmpresa.setWidthPercentage(100);
            tablaEmpresa.addCell(crearCelda("ReplyCar, S.L.", fontCabecera));
            tablaEmpresa.addCell(crearCelda("El mejor servicio en tu ciudad", fontNormal));
            tablaEmpresa.addCell(crearCelda("Calle Rigodón, 44", fontNormal));
            tablaEmpresa.addCell(crearCelda("29004 Málaga", fontNormal));
            tablaEmpresa.addCell(crearCelda("CIF: B23456654", fontNormal));
            tablaEmpresa.addCell(crearCelda("Tel: 698567765", fontNormal));
            tablaEmpresa.addCell(crearCelda("Email: info@replycar.fr", fontNormal));

            // Cliente
            PdfPTable tablaCliente = new PdfPTable(1);
            tablaCliente.setWidthPercentage(100);
            tablaCliente.addCell(crearCelda("DATOS DEL CLIENTE:", fontCabecera));
            tablaCliente.addCell(crearCelda(cli.getNombre() + " " + cli.getApellido1() + " " + cli.getApellido2(), fontNormal));
            tablaCliente.addCell(crearCelda(cli.getDireccion() + " nº " + cli.getNumero(), fontNormal));
            tablaCliente.addCell(crearCelda(cli.getCp() + " " + cli.getProvincia(), fontNormal));
            tablaCliente.addCell(crearCelda("NIE: " + cli.getNie(), fontNormal));

            // Añadir mini-tablas
            PdfPCell celdaEmpresa = new PdfPCell(tablaEmpresa);
            celdaEmpresa.setBorder(Rectangle.NO_BORDER);
            PdfPCell celdaCliente = new PdfPCell(tablaCliente);
            celdaCliente.setBorder(Rectangle.NO_BORDER);

            tablaCabecera.addCell(celdaEmpresa);
            tablaCabecera.addCell(celdaCliente);
            document.add(tablaCabecera);

            // ================= DATOS DE LA FACTURA =================
            Paragraph facturaTitulo = new Paragraph("Datos de la Factura", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
            facturaTitulo.setSpacingBefore(10);
            facturaTitulo.setSpacingAfter(10);
            facturaTitulo.setAlignment(Element.ALIGN_LEFT);
            document.add(facturaTitulo);

            PdfPTable tablaFactura = new PdfPTable(2);
            tablaFactura.setWidthPercentage(100);
            tablaFactura.setWidths(new float[]{1, 2});

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            tablaFactura.addCell("Nº Factura:");
            tablaFactura.addCell(fac.getNumero_factura());

            tablaFactura.addCell("Fecha Factura:");
            tablaFactura.addCell(fac.getFecha_factura().format(formatter));

            tablaFactura.addCell("Nº Cliente:");
            tablaFactura.addCell(String.valueOf(fac.getnCliente()));

            tablaFactura.addCell("Forma de Pago:");
            tablaFactura.addCell(cli.getFormaPagoTxt());

            tablaFactura.addCell("Nº Cuenta Cliente:");
            tablaFactura.addCell(cli.getnCuenta());

            document.add(tablaFactura);

            // ================= DATOS VEHÍCULO =================
            Paragraph vehTitulo = new Paragraph("Detalle de las Referencias", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
            vehTitulo.setSpacingBefore(10);
            vehTitulo.setSpacingAfter(10);
            document.add(vehTitulo);

            PdfPTable tablaInterv = new PdfPTable(6);
            tablaInterv.setWidthPercentage(100);
            tablaInterv.setWidths(new float[]{1, 3, 1, 1, 1, 1});

            Font fontCab = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Font fontTxt = new Font(Font.FontFamily.HELVETICA, 10);

            // Cabecera
            tablaInterv.addCell(new Phrase("Referencia", fontCab));
            tablaInterv.addCell(new Phrase("Descripción", fontCab));
            tablaInterv.addCell(new Phrase("Unidades", fontCab));
            tablaInterv.addCell(new Phrase("P.V.P.", fontCab));
            tablaInterv.addCell(new Phrase("Descuento", fontCab));
            tablaInterv.addCell(new Phrase("Precio", fontCab));

            for (String[] mov : movimientos) {

                tablaInterv.addCell(new Phrase(mov[0], fontTxt));
                tablaInterv.addCell(new Phrase(mov[1], fontTxt));
                tablaInterv.addCell(new Phrase(mov[2], fontTxt));
                tablaInterv.addCell(new Phrase(formato.format(Double.parseDouble(mov[3])), fontTxt));
                tablaInterv.addCell(new Phrase(mov[4] + " %", fontTxt));
                tablaInterv.addCell(new Phrase(formato.format(Double.parseDouble(mov[5])), fontTxt));
            }
            document.add(tablaInterv);

            // ================= IMPORTES DE LA FACTURA =================
            Paragraph importesTitulo = new Paragraph("Importes de la Factura", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
            importesTitulo.setSpacingBefore(5);
            importesTitulo.setSpacingAfter(5);
            document.add(importesTitulo);

            PdfPTable tablaImportes = new PdfPTable(2);
            tablaImportes.setWidthPercentage(100);
            tablaImportes.setWidths(new float[]{1, 2});

            formato.setMaximumFractionDigits(2);
            formato.setMinimumFractionDigits(2);

            double baseImponible = fac.getbaseImponible();
            double iva = fac.getCalculoIVA();
            double importeTotal = baseImponible + iva;

            tablaImportes.addCell("Base Imponible:");
            tablaImportes.addCell(formato.format(baseImponible));

            tablaImportes.addCell("IVA (21 %):");
            tablaImportes.addCell(formato.format(iva));

            tablaImportes.addCell("Importe Total:");
            tablaImportes.addCell(formato.format(importeTotal));

            document.add(tablaImportes);

            // ================= MENSAJE CUENTA BANCARIA =================
            Paragraph mensajeCuenta = new Paragraph(
                    "\nNota: ingreso en este nº de cuenta: ES90 0909 0909 99 4567899876",
                    new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD)
            );
            mensajeCuenta.setSpacingBefore(10);
            mensajeCuenta.setAlignment(Element.ALIGN_LEFT);
            document.add(mensajeCuenta);

            // ================= PIE =================
            Paragraph pie = new Paragraph(
                    "\nGracias por su confianza.\nReplyCar, S.L.",
                    new Font(Font.FontFamily.HELVETICA, 11)
            );
            pie.setAlignment(Element.ALIGN_CENTER);
            document.add(pie);

            document.close();

            JOptionPane.showMessageDialog(
                    null,
                    "Factura generada correctamente:\n" + rutaArchivo,
                    "Factura creada",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= MÉTODO AUXILIAR PARA CELDAS SIN BORDE =================

    /**
     * Método para crear celdas en el cuerpo de la factura
     * @param texto texto a incluir en la celda
     * @param fuente fuente del texto a inclir en la celda
     * @return una celda con texto y su fuente
     */
    public PdfPCell crearCelda(String texto, Font fuente) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setBorder(Rectangle.NO_BORDER);
        celda.setPadding(3);
        return celda;
    }
}
