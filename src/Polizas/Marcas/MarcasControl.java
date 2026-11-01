package Polizas.Marcas;

import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;

public final class MarcasControl {
    private final MarcasVista vista;
    private final MarcasModelo modelo;

    public MarcasControl(MarcasVista vista) {
        this.vista = vista;
        this.modelo = new MarcasModelo();
        conectarEventos();
        consultarMarcas(false);
    }

    private void conectarEventos() {
        vista.getBtnConsultar().addActionListener(evento -> consultarMarcas(true));
        vista.getBtnAgregar().addActionListener(evento -> agregarMarca());
        vista.getBtnActualizar().addActionListener(evento -> actualizarMarca());
        vista.getBtnEliminar().addActionListener(evento -> eliminarMarca());
        vista.getTabla().getSelectionModel().addListSelectionListener(this::seleccionarMarca);
        vista.getCmbMarcas().addActionListener(evento -> {
            Marca seleccionada = (Marca) vista.getCmbMarcas().getSelectedItem();
            if (seleccionada != null && !vista.getTabla().getSelectionModel().getValueIsAdjusting()) {
                vista.getTxtNombre().setText(seleccionada.getNombre());
            }
        });
    }

    private boolean consultarMarcas(boolean mostrarConfirmacion) {
        try {
            List<Marca> marcas = modelo.read();
            vista.actualizarVista(marcas);
            if (mostrarConfirmacion) {
                JOptionPane.showMessageDialog(vista,
                        "Consulta completada. Marcas encontradas: " + marcas.size(),
                        "Consulta de marcas", JOptionPane.INFORMATION_MESSAGE);
            }
            return true;
        } catch (SQLException ex) {
            mostrarError("No fue posible consultar las marcas.", ex);
            return false;
        }
    }

    private void agregarMarca() {
        try {
            String nombre = Marca.normalizarNombre(vista.getTxtNombre().getText());
            int filas = modelo.create(new Marca(0, nombre));
            if (filas == 0) {
                JOptionPane.showMessageDialog(vista, "No se registró la marca.",
                        "Registro no completado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean consultaCorrecta = consultarMarcas(false);
            vista.getTxtNombre().setText("");
            vista.getTxtNombre().requestFocusInWindow();
            if (consultaCorrecta) {
                JOptionPane.showMessageDialog(vista, "La marca se registró correctamente.",
                        "Registro completado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(vista,
                        "La marca sí se guardó, pero no se pudo actualizar la tabla. Presiona Consultar.",
                        "Registro guardado", JOptionPane.WARNING_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(vista, ex.getMessage(),
                    "Dato no válido", JOptionPane.WARNING_MESSAGE);
            vista.getTxtNombre().requestFocusInWindow();
        } catch (SQLException ex) {
            String detalle = ex.getMessage() == null ? "" : ex.getMessage();
            if (ex.getErrorCode() == 1062
                    || (ex.getErrorCode() == 1644 && detalle.contains("Ya existe"))) {
                mostrarDuplicado();
            } else if (ex.getErrorCode() == 1644) {
                JOptionPane.showMessageDialog(vista, "Escribe el nombre de la marca.",
                        "Dato requerido", JOptionPane.WARNING_MESSAGE);
                vista.getTxtNombre().requestFocusInWindow();
            } else {
                mostrarError("No fue posible registrar la marca.", ex);
            }
        }
    }

    private void actualizarMarca() {
        Marca marca = marcaSeleccionada();
        if (marca == null) {
            JOptionPane.showMessageDialog(vista,
                    "Selecciona una marca en la tabla o en la lista antes de actualizarla.",
                    "Marca no seleccionada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String nombre = Marca.normalizarNombre(vista.getTxtNombre().getText());
            if (nombre.equals(marca.getNombre())) {
                JOptionPane.showMessageDialog(vista,
                        "El nombre no cambió. Selecciona una marca y escribe el nuevo nombre.",
                        "Sin cambios", JOptionPane.INFORMATION_MESSAGE);
                vista.getTxtNombre().requestFocusInWindow();
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(vista,
                    "¿Actualizar la marca \"" + marca.getNombre() + "\" a \"" + nombre
                            + "\" (ID " + marca.getId() + ")?",
                    "Confirmar actualización", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            int filas = modelo.update(new Marca(marca.getId(), nombre));
            if (filas == 0) {
                JOptionPane.showMessageDialog(vista,
                        "La marca ya no existe o no se modificó. Presiona Consultar para revisar la tabla.",
                        "Registro no actualizado", JOptionPane.WARNING_MESSAGE);
                consultarMarcas(false);
                return;
            }

            boolean consultaCorrecta = consultarMarcas(false);
            vista.getTxtNombre().setText("");
            vista.getTxtNombre().requestFocusInWindow();
            if (consultaCorrecta) {
                JOptionPane.showMessageDialog(vista, "La marca se actualizó correctamente.",
                        "Actualización completada", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(vista,
                        "La marca sí se actualizó, pero no se pudo refrescar la tabla. Presiona Consultar.",
                        "Actualización realizada", JOptionPane.WARNING_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            if (ex.getMessage() != null && ex.getMessage().contains("Ya existe")) {
                mostrarDuplicado();
            } else {
                JOptionPane.showMessageDialog(vista, ex.getMessage(),
                        "Dato no válido", JOptionPane.WARNING_MESSAGE);
                vista.getTxtNombre().requestFocusInWindow();
            }
        } catch (SQLException ex) {
            String detalle = ex.getMessage() == null ? "" : ex.getMessage();
            if (ex.getErrorCode() == 1062
                    || (ex.getErrorCode() == 1644 && detalle.contains("existe"))) {
                mostrarDuplicado();
            } else if (ex.getErrorCode() == 1644) {
                JOptionPane.showMessageDialog(vista, detalle,
                        "Dato no válido", JOptionPane.WARNING_MESSAGE);
                vista.getTxtNombre().requestFocusInWindow();
            } else {
                mostrarError("No fue posible actualizar la marca.", ex);
            }
        }
    }

    private Marca marcaSeleccionada() {
        Marca seleccionada = (Marca) vista.getCmbMarcas().getSelectedItem();
        if (seleccionada != null) {
            return seleccionada;
        }

        int filaVista = vista.getTabla().getSelectedRow();
        if (filaVista < 0) {
            return null;
        }
        int filaModelo = vista.getTabla().convertRowIndexToModel(filaVista);
        return vista.getModeloTabla().getMarca(filaModelo);
    }

    private void eliminarMarca() {
        Marca marca = marcaSeleccionada();

        if (marca == null) {
            JOptionPane.showMessageDialog(vista,
                    "Selecciona una marca en la tabla o en la lista antes de eliminarla.",
                    "Marca no seleccionada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(vista,
                "¿Eliminar la marca \"" + marca.getNombre() + "\" (ID " + marca.getId() + ")?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int filas = modelo.delete(marca.getId());
            if (filas == 0) {
                JOptionPane.showMessageDialog(vista,
                        "La marca ya no existe en la base de datos. Presiona Consultar para actualizar la lista.",
                        "Registro no encontrado", JOptionPane.WARNING_MESSAGE);
                consultarMarcas(false);
                return;
            }

            boolean consultaCorrecta = consultarMarcas(false);
            vista.getTxtNombre().setText("");
            vista.getTxtNombre().requestFocusInWindow();
            if (consultaCorrecta) {
                JOptionPane.showMessageDialog(vista, "La marca se eliminó correctamente.",
                        "Eliminación completada", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(vista,
                        "La marca sí se eliminó, pero no se pudo actualizar la tabla. Presiona Consultar.",
                        "Eliminación realizada", JOptionPane.WARNING_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(vista, ex.getMessage(),
                    "Selección no válida", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            mostrarError("No fue posible eliminar la marca.", ex);
        }
    }
    private void mostrarDuplicado() {
        JOptionPane.showMessageDialog(vista, "Ya existe una marca con ese nombre.",
                "Marca duplicada", JOptionPane.WARNING_MESSAGE);
        vista.getTxtNombre().requestFocusInWindow();
    }

    private void seleccionarMarca(ListSelectionEvent evento) {
        if (evento.getValueIsAdjusting()) {
            return;
        }
        int filaVista = vista.getTabla().getSelectedRow();
        if (filaVista >= 0) {
            int filaModelo = vista.getTabla().convertRowIndexToModel(filaVista);
            Marca marca = vista.getModeloTabla().getMarcaAt(filaModelo);
            vista.getTxtNombre().setText(marca.getNombre());
            vista.getCmbMarcas().setSelectedItem(marca);
        }
    }

    private void mostrarError(String mensaje, SQLException ex) {
        JOptionPane.showMessageDialog(vista,
                mensaje + "\nDetalle: " + ex.getMessage()
                        + "\nVerifica que MySQL esté activo, que exista la base polizas y que Connector/J esté agregado.",
                "Error de base de datos", JOptionPane.ERROR_MESSAGE);
    }
}
