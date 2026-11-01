package Polizas.General;

import Polizas.Marcas.MarcasControl;
import Polizas.Marcas.MarcasVista;

public final class Controlador {
    private final Bienvenida vista;
    private MarcasVista marcasVista;
    private boolean mensajeCargado;

    public Controlador(Bienvenida vista) {
        this.vista = vista;
        conectarEventos();
    }

    private void conectarEventos() {
        vista.getInicioItem().addActionListener(evento -> vista.mostrarInicio());
        vista.getMensajeItem().addActionListener(evento -> mostrarMensaje());
        vista.getMarcasItem().addActionListener(evento -> mostrarMarcas());
    }

    private void mostrarMensaje() {
        if (!mensajeCargado) {
            vista.agregarTarjeta("mensaje", new Mensaje());
            mensajeCargado = true;
        }
        vista.mostrarTarjeta("mensaje");
    }

    private void mostrarMarcas() {
        if (marcasVista == null) {
            marcasVista = new MarcasVista();
            new MarcasControl(marcasVista);
            vista.agregarTarjeta("marcas", marcasVista);
        }
        vista.mostrarTarjeta("marcas");
    }
}