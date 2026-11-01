package Polizas.Marcas;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public final class MarcasTM extends AbstractTableModel {
    private String[] encabezados = {"id", "nombre"};
    private List<Marca> marcas = new ArrayList<>();

    @Override
    public String getColumnName(int columna) {
        return encabezados[columna];
    }

    public void setColumnNames(String[] nuevosNombres) {
        if (nuevosNombres == null || nuevosNombres.length != encabezados.length) {
            throw new IllegalArgumentException("La tabla requiere exactamente dos encabezados.");
        }
        for (String nombre : nuevosNombres) {
            if (nombre == null || nombre.isBlank()) {
                throw new IllegalArgumentException("Los encabezados no pueden estar vacíos.");
            }
        }
        encabezados = nuevosNombres.clone();
        fireTableStructureChanged();
    }

    public void vaciarLista() {
        marcas.clear();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return marcas.size();
    }

    @Override
    public int getColumnCount() {
        return encabezados.length;
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        Marca marca = marcas.get(fila);
        return switch (columna) {
            case 0 -> marca.getId();
            case 1 -> marca.getNombre();
            default -> null;
        };
    }

    public void addMarca(Marca marca) {
        if (marca == null) {
            throw new IllegalArgumentException("La marca no puede ser nula.");
        }
        int fila = marcas.size();
        marcas.add(marca);
        fireTableRowsInserted(fila, fila);
    }

    public void updateMarca(int fila, Marca nueva) {
        validarFila(fila);
        if (nueva == null) {
            throw new IllegalArgumentException("La marca no puede ser nula.");
        }
        marcas.set(fila, nueva);
        fireTableRowsUpdated(fila, fila);
    }

    public Marca getMarca(int fila) {
        validarFila(fila);
        return marcas.get(fila);
    }

    public Marca getMarcaAt(int fila) {
        return getMarca(fila);
    }

    public void removeMarca(int fila) {
        validarFila(fila);
        marcas.remove(fila);
        fireTableRowsDeleted(fila, fila);
    }

    public void llenar(List<Marca> nuevasMarcas) {
        setMarcas(nuevasMarcas);
    }

    public void setMarcas(List<Marca> nuevasMarcas) {
        marcas = nuevasMarcas == null ? new ArrayList<>() : new ArrayList<>(nuevasMarcas);
        fireTableDataChanged();
    }

    private void validarFila(int fila) {
        if (fila < 0 || fila >= marcas.size()) {
            throw new IndexOutOfBoundsException("La fila seleccionada no existe.");
        }
    }
}