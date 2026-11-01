package Polizas.Marcas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public final class MarcasVista extends JPanel {
    private final JButton btnConsultar = new JButton("Consultar");
    private final JButton btnAgregar = new JButton("Agregar");
    private final JButton btnActualizar = new JButton("Actualizar");
    private final JButton btnEliminar = new JButton("Eliminar");
    private final JTextField txtNombre = new CampoConAyuda("Marca", 18);
    private final JComboBox<Marca> cmbMarcas = new JComboBox<>();
    private final MarcasTM modeloTabla = new MarcasTM();
    private final JTable tabla = new JTable(modeloTabla);

    public MarcasVista() {
        super(new BorderLayout(8, 8));
        setBackground(new Color(248, 248, 248));
        setBorder(BorderFactory.createEmptyBorder(7, 14, 10, 14));

        add(crearParteSuperior(), BorderLayout.NORTH);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFillsViewportHeight(true);
        tabla.setAutoCreateRowSorter(true);

        JScrollPane desplazamiento = new JScrollPane(tabla);
        desplazamiento.setPreferredSize(new Dimension(575, 380));
        JPanel tablaCentrada = new JPanel(new java.awt.GridBagLayout());
        tablaCentrada.setBackground(getBackground());
        java.awt.GridBagConstraints posicionTabla = new java.awt.GridBagConstraints();
        posicionTabla.anchor = java.awt.GridBagConstraints.NORTH;
        posicionTabla.insets = new java.awt.Insets(8, 0, 0, 0);
        tablaCentrada.add(desplazamiento, posicionTabla);
        add(tablaCentrada, BorderLayout.CENTER);
    }

    private JPanel crearParteSuperior() {
        JPanel superior = new JPanel();
        superior.setOpaque(false);
        superior.setLayout(new javax.swing.BoxLayout(superior, javax.swing.BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Welcome to Marcas Admin.");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 12f));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        superior.add(titulo);
        superior.add(javax.swing.Box.createVerticalStrut(8));

        JPanel filaDatos = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filaDatos.setOpaque(false);
        filaDatos.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
        filaDatos.add(new JLabel("Ingresa tu nombre:"));
        txtNombre.setPreferredSize(new Dimension(190, 25));
        filaDatos.add(txtNombre);
        filaDatos.add(javax.swing.Box.createHorizontalStrut(30));
        cmbMarcas.setPreferredSize(new Dimension(180, 25));
        filaDatos.add(cmbMarcas);
        filaDatos.setAlignmentX(Component.CENTER_ALIGNMENT);
        superior.add(filaDatos);
        superior.add(javax.swing.Box.createVerticalStrut(10));

        JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filaBotones.setOpaque(false);
        filaBotones.setBorder(BorderFactory.createEmptyBorder(0, 98, 0, 0));
        btnConsultar.setPreferredSize(new Dimension(100, 25));
        btnAgregar.setPreferredSize(new Dimension(88, 25));
        btnActualizar.setPreferredSize(new Dimension(100, 25));
        btnEliminar.setPreferredSize(new Dimension(82, 25));
        filaBotones.add(btnConsultar);
        filaBotones.add(btnAgregar);
        filaBotones.add(btnActualizar);
        filaBotones.add(btnEliminar);
        filaBotones.setAlignmentX(Component.CENTER_ALIGNMENT);
        superior.add(filaBotones);
        return superior;
    }

    public JButton getBtnConsultar() {
        return btnConsultar;
    }

    public JButton getBtnAgregar() {
        return btnAgregar;
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public JComboBox<Marca> getCmbMarcas() {
        return cmbMarcas;
    }

    public JTable getTabla() {
        return tabla;
    }

    public MarcasTM getModeloTabla() {
        return modeloTabla;
    }

    public void actualizarVista(List<Marca> marcas) {
        modeloTabla.setMarcas(marcas);
        tabla.clearSelection();
        cmbMarcas.removeAllItems();
        cmbMarcas.addItem(null);
        for (Marca marca : marcas) {
            cmbMarcas.addItem(marca);
        }
        cmbMarcas.setSelectedIndex(0);
    }

    private static final class CampoConAyuda extends JTextField {
        private final String ayuda;

        private CampoConAyuda(String ayuda, int columnas) {
            super(columnas);
            this.ayuda = ayuda;
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            if (!getText().isEmpty() || isFocusOwner()) {
                return;
            }
            Graphics2D g = (Graphics2D) graphics.create();
            try {
                Insets insets = getInsets();
                g.setColor(new Color(135, 135, 135));
                g.setFont(getFont());
                int y = (getHeight() - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent();
                g.drawString(ayuda, insets.left + 3, y);
            } finally {
                g.dispose();
            }
        }
    }
}
