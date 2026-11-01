package Polizas.General;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public final class Mensaje extends JPanel {
    public Mensaje() {
        super(new BorderLayout());
        JLabel texto = new JLabel("Sistema de Gestión de Pólizas", SwingConstants.CENTER);
        texto.setFont(texto.getFont().deriveFont(Font.BOLD, 22f));
        add(texto, BorderLayout.CENTER);
    }
}