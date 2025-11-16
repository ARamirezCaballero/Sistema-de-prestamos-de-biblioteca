package biblioteca.data.dao;

import biblioteca.data.db.ConexionBD;
import biblioteca.data.interfaces.DAO;
import biblioteca.entities.notificaciones.Notificacion;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificacionesDAO implements DAO<Notificacion> {

    @Override
    public void insertar(Notificacion notificacion) throws DAOException {
        String sql = "INSERT INTO Notificacion (fecha_envio, tipo, mensaje, leida, id_prestamo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setTimestamp(1, Timestamp.valueOf(notificacion.getFechaHora()));
            // Inferimos tipo según contenido del mensaje
            String tipo = (notificacion.getMensaje().toLowerCase().contains("vencido"))
                    ? "Alerta de atraso"
                    : "Recordatorio";
            ps.setString(2, tipo);
            ps.setString(3, notificacion.getMensaje());
            ps.setBoolean(4, false);

            // Asociamos préstamo
            if (notificacion.getPrestamo() != null) {
                ps.setInt(5, notificacion.getPrestamo().getId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new DAOException("No se pudo insertar la notificación (0 filas afectadas).");
            }

        } catch (SQLException e) {
            throw new DAOException("Error al insertar notificación", e);
        }
    }

    @Override
    public Notificacion buscarPorId(int id) throws DAOException {
        String sql = "SELECT * FROM Notificacion WHERE id_notificacion = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearNotificacion(rs);
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Error al buscar notificación por ID", e);
        }
    }

    @Override
    public List<Notificacion> listarTodos() throws DAOException {
        String sql = "SELECT * FROM Notificacion ORDER BY fecha_envio DESC";
        List<Notificacion> lista = new ArrayList<>();

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearNotificacion(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Error al listar notificaciones", e);
        }
        return lista;
    }

    @Override
    public void actualizar(Notificacion notificacion) throws DAOException {
        String sql = "UPDATE Notificacion SET leida = ?, mensaje = ? WHERE id_notificacion = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, notificacionFueLeida(notificacion));
            ps.setString(2, notificacion.getMensaje());
            ps.setInt(3, notificacion.getIdNotificacion());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error al actualizar notificación", e);
        }
    }

    @Override
    public void eliminar(int id) throws DAOException {
        String sql = "DELETE FROM Notificacion WHERE id_notificacion = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error al eliminar notificación", e);
        }
    }

    // === Métodos adicionales específicos ===

    public List<Notificacion> listarNoLeidas() throws DAOException {
        String sql = "SELECT * FROM Notificacion WHERE leida = FALSE ORDER BY fecha_envio DESC";
        List<Notificacion> lista = new ArrayList<>();

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearNotificacion(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Error al listar notificaciones no leídas", e);
        }
        return lista;
    }

    public List<Notificacion> listarPorPrestamoId(int idPrestamo) throws DAOException {
        String sql = "SELECT * FROM Notificacion WHERE id_prestamo = ?";
        List<Notificacion> lista = new ArrayList<>();

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPrestamo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearNotificacion(rs));
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Error al listar notificaciones por préstamo", e);
        }
        return lista;
    }

    // === Métodos auxiliares ===

    private Notificacion mapearNotificacion(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_notificacion");
        LocalDateTime fecha = rs.getTimestamp("fecha_envio").toLocalDateTime();
        String tipo = rs.getString("tipo");
        String mensaje = rs.getString("mensaje");
        boolean leida = rs.getBoolean("leida");
        int idPrestamo = rs.getInt("id_prestamo");

        // Reconstrucción básica del Prestamo y su socio
        // Nota: El destinatario se resuelve luego por la capa superior usando idPrestamo
        // Usamos un placeholder temporal (null) ya que Usuario y Socio son abstractos
        Notificacion n = new Notificacion(id, mensaje, fecha, null, null);
        n.setMensaje(mensaje);

        return n;
    }

    private boolean notificacionFueLeida(Notificacion n) {
        return n.getMensaje().toLowerCase().contains("leída") || n.getMensaje().toLowerCase().contains("confirmada");
    }
}