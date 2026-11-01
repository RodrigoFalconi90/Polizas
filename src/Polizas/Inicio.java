package Polizas;

import Polizas.General.Bienvenida;
import Polizas.General.Controlador;
import javax.swing.SwingUtilities;

public final class Inicio {
    private Inicio() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Bienvenida vista = new Bienvenida();
            new Controlador(vista);
            vista.setVisible(true);
        });
    }
}   