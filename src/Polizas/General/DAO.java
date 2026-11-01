package Polizas.General;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {
    int create(T objeto) throws SQLException;

    List<T> read() throws SQLException;

    int update(T objeto) throws SQLException;

    int delete(int id) throws SQLException;
}