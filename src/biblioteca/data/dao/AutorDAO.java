package biblioteca.data.dao;

import biblioteca.data.db.ConexionBD;
import java.sql.*;

public class AutorDAO {

    public int obtenerIdPorNombre(Connection conn, String nombre) throws DAOException {
        String sql = "SELECT id_autor FROM Autor WHERE nombre_completo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id_autor");
            }
        } catch (SQLException e) {
            throw new DAOException("Error buscando autor", e);
        }
        return -1;
    }

    public int insertarAutor(Connection conn, String nombre) throws DAOException {
        String sql = "INSERT INTO Autor (nombre_completo) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nombre);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new DAOException("Error insertando autor", e);
        }
        throw new DAOException("No se pudo obtener ID de autor insertado", null);
    }
}

