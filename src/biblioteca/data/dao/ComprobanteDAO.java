package biblioteca.data.dao;

import biblioteca.data.db.ConexionBD;
import biblioteca.data.interfaces.DAO;
import biblioteca.entities.prestamos.Prestamo;
import biblioteca.entities.reportes.Comprobante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComprobanteDAO implements DAO<Comprobante> {

    private final PrestamoDAO prestamoDAO;

    public ComprobanteDAO(PrestamoDAO prestamoDAO) {
        this.prestamoDAO = prestamoDAO;
    }

    @Override
    public void insertar(Comprobante comprobante) throws DAOException {
        String sql = """
            INSERT INTO Comprobante (fecha_emision, tipo, contenido, id_prestamo)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setTimestamp(1, Timestamp.valueOf(comprobante.getFechaEmision().atStartOfDay()));
            ps.setString(2, comprobante.getTipo());
            ps.setString(3, comprobante.getContenido());
            ps.setInt(4, comprobante.getPrestamo().getId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    comprobante.setId(idGenerado);
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Error al insertar comprobante", e);
        }
    }

    @Override
    public Comprobante buscarPorId(int id) throws DAOException {
        String sql = """
            SELECT id_comprobante, fecha_emision, tipo, contenido, id_prestamo
            FROM Comprobante
            WHERE id_comprobante = ?
        """;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearComprobante(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Error al buscar comprobante por ID", e);
        }
    }

    @Override
    public List<Comprobante> listarTodos() throws DAOException {
        String sql = "SELECT id_comprobante, fecha_emision, tipo, contenido, id_prestamo FROM Comprobante";
        List<Comprobante> comprobantes = new ArrayList<>();

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                comprobantes.add(mapearComprobante(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Error al listar comprobantes", e);
        }

        return comprobantes;
    }

    @Override
    public void actualizar(Comprobante comprobante) throws DAOException {
        String sql = """
            UPDATE Comprobante
            SET tipo = ?, contenido = ?, fecha_emision = ?
            WHERE id_comprobante = ?
        """;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, comprobante.getTipo());
            ps.setString(2, comprobante.getContenido());
            ps.setTimestamp(3, Timestamp.valueOf(comprobante.getFechaEmision().atStartOfDay()));
            ps.setInt(4, comprobante.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error al actualizar comprobante", e);
        }
    }

    @Override
    public void eliminar(int id) throws DAOException {
        String sql = "DELETE FROM Comprobante WHERE id_comprobante = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error al eliminar comprobante", e);
        }
    }

    // === Método adicional útil para flujo del sistema ===
    public Comprobante buscarPorPrestamoId(int idPrestamo) throws DAOException {
        String sql = """
            SELECT id_comprobante, fecha_emision, tipo, contenido, id_prestamo
            FROM Comprobante
            WHERE id_prestamo = ?
        """;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPrestamo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearComprobante(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Error al buscar comprobante por id_prestamo", e);
        }
    }

    // === Método interno para mapear resultset a entidad ===
    private Comprobante mapearComprobante(ResultSet rs) throws SQLException, DAOException {
        int idComprobante = rs.getInt("id_comprobante");
        String tipo = rs.getString("tipo");
        int idPrestamo = rs.getInt("id_prestamo");

        Prestamo prestamo = prestamoDAO.buscarPorId(idPrestamo);
        Comprobante comprobante = new Comprobante(idComprobante, tipo, prestamo);

        String contenido = rs.getString("contenido");
        if (contenido != null && !contenido.isBlank()) {
            comprobante.setContenido(contenido);
        }

        return comprobante;
    }
}

