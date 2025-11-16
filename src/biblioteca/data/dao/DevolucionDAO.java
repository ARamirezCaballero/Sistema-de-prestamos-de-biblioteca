package biblioteca.data.dao;

import biblioteca.data.db.ConexionBD;
import biblioteca.data.interfaces.DAO;
import biblioteca.entities.prestamos.Devolucion;
import biblioteca.entities.prestamos.Prestamo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO que gestiona únicamente la persistencia de devoluciones.
 * No actualiza otras entidades; las actualizaciones deben manejarse desde el servicio.
 */
public class DevolucionDAO implements DAO<Devolucion> {

    private final PrestamoDAO prestamoDAO;

    public DevolucionDAO(PrestamoDAO prestamoDAO) {
        this.prestamoDAO = prestamoDAO;
    }

    // Insertar una devolución (solo la devolución)
    @Override
    public void insertar(Devolucion d) throws DAOException {
        String sqlInsert = "INSERT INTO Devolucion " +
                "(fecha_devolucion, estado_ejemplar, observaciones, multa, id_prestamo) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, Date.valueOf(d.getFechaDevolucion()));
            ps.setString(2, d.getEstadoEjemplar());
            ps.setString(3, d.getObservaciones());
            ps.setDouble(4, d.getMulta());
            ps.setInt(5, d.getPrestamo().getId());
            ps.executeUpdate();

            // Obtener ID generado
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    d.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Error al insertar devolución", e);
        }
    }

    // Buscar devolución por ID
    @Override
    public Devolucion buscarPorId(int id) throws DAOException {
        String sql = "SELECT * FROM Devolucion WHERE id_devolucion = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int idPrestamo = rs.getInt("id_prestamo");
                    Prestamo prestamo = prestamoDAO.buscarPorId(idPrestamo);

                    return new Devolucion(
                            rs.getInt("id_devolucion"),
                            rs.getDate("fecha_devolucion").toLocalDate(),
                            rs.getString("estado_ejemplar"),
                            rs.getString("observaciones"),
                            prestamo,
                            rs.getDouble("multa")
                    );
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Error al buscar devolución con ID " + id, e);
        }

        return null;
    }

    // Listar todas las devoluciones
    @Override
    public List<Devolucion> listarTodos() throws DAOException {
        List<Devolucion> lista = new ArrayList<>();
        String sql = "SELECT * FROM Devolucion";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Prestamo prestamo = prestamoDAO.buscarPorId(rs.getInt("id_prestamo"));

                lista.add(new Devolucion(
                        rs.getInt("id_devolucion"),
                        rs.getDate("fecha_devolucion").toLocalDate(),
                        rs.getString("estado_ejemplar"),
                        rs.getString("observaciones"),
                        prestamo,
                        rs.getDouble("multa")
                ));
            }

        } catch (SQLException e) {
            throw new DAOException("Error al listar devoluciones.", e);
        }

        return lista;
    }

    // Actualizar devolución (solo los campos de devolución)
    @Override
    public void actualizar(Devolucion d) throws DAOException {
        String sql = "UPDATE Devolucion SET estado_ejemplar = ?, observaciones = ?, multa = ? WHERE id_devolucion = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, d.getEstadoEjemplar());
            ps.setString(2, d.getObservaciones());
            ps.setDouble(3, d.getMulta());
            ps.setInt(4, d.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error al actualizar devolución con ID " + d.getId(), e);
        }
    }

    // Eliminar devolución
    @Override
    public void eliminar(int id) throws DAOException {
        String sql = "DELETE FROM Devolucion WHERE id_devolucion = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error al eliminar devolución con ID " + id, e);
        }
    }
}


