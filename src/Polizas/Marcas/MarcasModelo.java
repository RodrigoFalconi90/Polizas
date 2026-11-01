package Polizas.Marcas;

import Polizas.General.Conexion;
import Polizas.General.DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class MarcasModelo extends Conexion implements DAO<Marca> {
    @Override
    public List<Marca> read() throws SQLException {
        String sql = "SELECT idMarca, nombreMarca FROM marca ORDER BY idMarca";
        List<Marca> marcas = new ArrayList<>();

        try (Connection conexion = conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                marcas.add(new Marca(
                        resultado.getInt("idMarca"),
                        resultado.getString("nombreMarca")));
            }
        }
        return marcas;
    }

    @Override
    public int create(Marca marca) throws SQLException {
        if (marca == null) {
            throw new IllegalArgumentException("La marca es obligatoria.");
        }
        String nombre = Marca.normalizarNombre(marca.getNombre());
        String sql = "INSERT INTO marca (nombreMarca) VALUES (?)";

        try (Connection conexion = conectar()) {
            if (nombreExiste(conexion, nombre, 0)) {
                throw new IllegalArgumentException("Ya existe una marca con ese nombre.");
            }
            try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                sentencia.setString(1, nombre);
                return sentencia.executeUpdate();
            }
        }
    }

    @Override
    public int update(Marca marca) throws SQLException {
        if (marca == null) {
            throw new IllegalArgumentException("La marca es obligatoria.");
        }
        if (marca.getId() <= 0) {
            throw new IllegalArgumentException("Selecciona una marca válida para actualizar.");
        }
        String nombre = Marca.normalizarNombre(marca.getNombre());
        String sql = "UPDATE marca SET nombreMarca = ? WHERE idMarca = ?";

        try (Connection conexion = conectar()) {
            if (nombreExiste(conexion, nombre, marca.getId())) {
                throw new IllegalArgumentException("Ya existe otra marca con ese nombre.");
            }
            try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                sentencia.setString(1, nombre);
                sentencia.setInt(2, marca.getId());
                return sentencia.executeUpdate();
            }
        }
    }

    @Override
    public int delete(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("Selecciona una marca válida para eliminar.");
        }
        String sql = "DELETE FROM marca WHERE idMarca = ?";
        try (Connection conexion = conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            return sentencia.executeUpdate();
        }
    }

    private boolean nombreExiste(Connection conexion, String nombre, int excluirId) throws SQLException {
        String sql = "SELECT 1 FROM marca WHERE TRIM(nombreMarca) = ? AND idMarca <> ? LIMIT 1";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, nombre);
            sentencia.setInt(2, excluirId);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next();
            }
        }
    }
}