package Polizas.General;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public final class Bienvenida extends JFrame {
    private static final String TARJETA_INICIO = "inicio";
    private static final Color COLOR_CENTRO = new Color(248, 248, 248);
    private static final Color COLOR_LATERAL = new Color(238, 238, 238);
    private static final Color COLOR_VERDE = new Color(24, 119, 57);

    private final CardLayout tarjetasLayout = new CardLayout();
    private final JPanel tarjetas = new JPanel(tarjetasLayout);
    private final JMenuItem inicio = new JMenuItem("Inicio");
    private final JMenuItem mensaje = new JMenuItem("Mensaje");
    private final JMenuItem marcas = new JMenuItem("Marcas");

    public Bienvenida() {
        super("Sistema de Gestión de Pólizas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 600));
        setSize(1100, 720);
        setLocationRelativeTo(null);

        setJMenuBar(crearMenu());
        tarjetas.setBackground(COLOR_CENTRO);
        tarjetas.add(crearInicio(), TARJETA_INICIO);
        setContentPane(crearContenido());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private JPanel crearContenido() {
        JPanel principal = new JPanel(new BorderLayout());
        principal.add(crearEncabezado(), BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout());
        cuerpo.add(crearPanelLateral("Panel izquierdo", 180, false), BorderLayout.WEST);
        cuerpo.add(tarjetas, BorderLayout.CENTER);
        cuerpo.add(crearPanelLateral("Panel derecho", 125, true), BorderLayout.EAST);
        principal.add(cuerpo, BorderLayout.CENTER);
        return principal;
    }

    private JPanel crearEncabezado() {
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(COLOR_VERDE);
        encabezado.setPreferredSize(new Dimension(0, 38));

        JLabel titulo = new JLabel("Sistema de Gestión...", SwingConstants.CENTER);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 14f));
        encabezado.add(titulo, BorderLayout.CENTER);
        return encabezado;
    }

    private JPanel crearPanelLateral(String texto, int ancho, boolean bordeIzquierdo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_LATERAL);
        panel.setPreferredSize(new Dimension(ancho, 0));
        if (bordeIzquierdo) {
            panel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(220, 220, 220)));
        } else {
            panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));
        }

        JLabel etiqueta = new JLabel(texto, SwingConstants.CENTER);
        etiqueta.setFont(etiqueta.getFont().deriveFont(Font.BOLD, 11f));
        etiqueta.setBorder(BorderFactory.createEmptyBorder(8, 4, 0, 4));
        panel.add(etiqueta, BorderLayout.NORTH);
        return panel;
    }

    private JMenuBar crearMenu() {
        JMenuBar barra = new JMenuBar();
        JMenu archivo = new JMenu("Archivo");
        archivo.add(inicio);
        archivo.add(mensaje);

        JMenu baseDatos = new JMenu("Base de Datos");
        baseDatos.add(marcas);

        barra.add(archivo);
        barra.add(baseDatos);
        return barra;
    }

    private JPanel crearInicio() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_CENTRO);
        JLabel texto = new JLabel("Welcome to Java...");
        texto.setFont(texto.getFont().deriveFont(Font.BOLD, 12f));
        panel.add(texto, BorderLayout.WEST);
        return panel;
    }

    public JMenuItem getInicioItem() {
        return inicio;
    }

    public JMenuItem getMensajeItem() {
        return mensaje;
    }

    public JMenuItem getMarcasItem() {
        return marcas;
    }

    public void agregarTarjeta(String nombre, JPanel panel) {
        tarjetas.add(panel, nombre);
    }

    public void mostrarTarjeta(String nombre) {
        tarjetasLayout.show(tarjetas, nombre);
    }

    public void mostrarInicio() {
        mostrarTarjeta(TARJETA_INICIO);
    }
}
