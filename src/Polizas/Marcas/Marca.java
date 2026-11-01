package Polizas.Marcas;

public final class Marca {
    private int id;
    private String nombre;

    public Marca(int id, String nombre) {
        setId(id);
        setNombre(nombre);
    }

    public static String normalizarNombre(String nombre) {
        if (nombre == null) {
            throw new IllegalArgumentException("El nombre de la marca es obligatorio.");
        }

        String normalizado = nombre.strip().replaceAll("(?U)\\s+", " ");
        if (normalizado.isEmpty()) {
            throw new IllegalArgumentException("Escribe el nombre de la marca.");
        }
        if (normalizado.codePointCount(0, normalizado.length()) > 100) {
            throw new IllegalArgumentException("El nombre admite hasta 100 caracteres.");
        }
        return normalizado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("El ID no puede ser negativo.");
        }
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = normalizarNombre(nombre);
    }

    @Override
    public String toString() {
        return id + " - " + nombre;
    }
}