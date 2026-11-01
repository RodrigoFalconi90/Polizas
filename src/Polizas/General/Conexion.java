package Polizas.General;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL_POR_DEFECTO =
            "jdbc:mysql://localhost:3306/polizas?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";

    protected Connection conectar() throws SQLException {
        String url = configuracion("polizas.db.url", "POLIZAS_DB_URL", URL_POR_DEFECTO);
        String usuario = configuracion("polizas.db.user", "POLIZAS_DB_USER", "root");
        String password = configuracion("polizas.db.password", "POLIZAS_DB_PASSWORD", "");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new SQLException(
                    "No se encontró MySQL Connector/J. Agrega lib/mysql-connector-j.jar al proyecto.", ex);
        }

        return DriverManager.getConnection(url, usuario, password);
    }

    private String configuracion(String propiedad, String variable, String valorPredeterminado) {
        String valor = System.getProperty(propiedad);
        if (valor == null || valor.isBlank()) {
            valor = System.getenv(variable);
        }
        return valor == null || valor.isBlank() ? valorPredeterminado : valor;
    }
}