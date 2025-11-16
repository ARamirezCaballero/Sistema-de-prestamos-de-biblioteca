package biblioteca.data.dao;

import java.sql.*;

public class EditorialDAO {

    public int obtenerIdPorNombre(Connection conn, String nombre) throws DAOException {
        String sql = "SELECT id_editorial FROM Editorial WHERE nombre = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id_editorial");
            }

        } catch (SQLException e) {
            throw new DAOException("Error buscando editorial", e);
        }

        return -1; // no existe
    }

    public int insertarEditorial(Connection conn, String nombre) throws DAOException {
        String sql = "INSERT INTO Editorial (nombre) VALUES (?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nombre);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new DAOException("Error insertando editorial", e);
        }

        throw new DAOException("No se pudo obtener ID de editorial insertado", null);
    }
}


