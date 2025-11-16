package biblioteca.data.dao;

import biblioteca.data.db.ConexionBD;
import biblioteca.data.interfaces.DAO;
import biblioteca.entities.reportes.Historial;
import biblioteca.entities.usuarios.Socio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistorialDAO implements DAO<Historial> {

    private final SocioDAO socioDAO;

    // --- Constructor con inyección del SocioDAO ---
    public HistorialDAO(SocioDAO socioDAO) {
        this.socioDAO = socioDAO;
    }

    @Override
    public void insertar(Historial historial) throws DAOException {
        String sql = "INSERT INTO Historial (fecha, tipo_operacion, detalles, id_usuario, id_libro, id_prestamo) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setTimestamp(1, Timestamp.valueOf(historial.getFecha()));
            ps.setString(2, historial.getTipoOperacion());
            ps.setString(3, historial.getDetalles());
            ps.setInt(4, historial.getSocio().getId());

            if (historial.getPrestamo() != null) {
                if (historial.getPrestamo().getEjemplar() != null &&
                        historial.getPrestamo().getEjemplar().getLibro() != null) {
                    ps.setInt(5, historial.getPrestamo().getEjemplar().getLibro().getId());
                } else {
                    ps.setNull(5, Types.INTEGER);
                }
                ps.setInt(6, historial.getPrestamo().getId());
            } else {
                ps.setNull(5, Types.INTEGER);
                ps.setNull(6, Types.INTEGER);
            }

            int filas = ps.executeUpdate();
            if (filas == 0) throw new DAOException("No se pudo insertar historial (0 filas afectadas)");

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) historial.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            throw new DAOException("Error al insertar historial", e);
        }
    }

    @Override
    public Historial buscarPorId(int id) throws DAOException {
        String sql = "SELECT * FROM Historial WHERE id_historial = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearHistorial(rs);
                else return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Error al buscar historial por ID", e);
        }
    }

    @Override
    public List<Historial> listarTodos() throws DAOException {
        String sql = "SELECT * FROM Historial ORDER BY fecha DESC";
        List<Historial> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearHistorial(rs));
            }
            return lista;

        } catch (SQLException e) {
            throw new DAOException("Error al listar historiales", e);
        }
    }

    @Override
    public void actualizar(Historial historial) throws DAOException {
        String sql = "UPDATE Historial SET fecha = ?, tipo_operacion = ?, detalles = ?, id_usuario = ?, id_libro = ?, id_prestamo = ? " +
                "WHERE id_historial = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(historial.getFecha()));
            ps.setString(2, historial.getTipoOperacion());
            ps.setString(3, historial.getDetalles());
            ps.setInt(4, historial.getSocio().getId());

            if (historial.getPrestamo() != null) {
                if (historial.getPrestamo().getEjemplar() != null &&
                        historial.getPrestamo().getEjemplar().getLibro() != null) {
                    ps.setInt(5, historial.getPrestamo().getEjemplar().getLibro().getId());
                } else {
                    ps.setNull(5, Types.INTEGER);
                }
                ps.setInt(6, historial.getPrestamo().getId());
            } else {
                ps.setNull(5, Types.INTEGER);
                ps.setNull(6, Types.INTEGER);
            }

            ps.setInt(7, historial.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error al actualizar historial", e);
        }
    }

    @Override
    public void eliminar(int id) throws DAOException {
        String sql = "DELETE FROM Historial WHERE id_historial = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al eliminar historial", e);
        }
    }

    public Historial buscarPorDni(String dni) throws DAOException {
        String sql = """
            SELECT h.* FROM Historial h
            INNER JOIN Usuario u ON h.id_usuario = u.id_usuario
            WHERE u.dni = ?
        """;
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearHistorial(rs);
                else return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Error al buscar historial por DNI", e);
        }
    }

    public void registrarOperacion(int idSocio, String tipo, String detalles, Integer idLibro, Integer idPrestamo) throws DAOException {
        String sql = "INSERT INTO Historial (fecha, tipo_operacion, detalles, id_usuario, id_libro, id_prestamo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setString(2, tipo);
            ps.setString(3, detalles);
            ps.setInt(4, idSocio);
            if (idLibro != null) ps.setInt(5, idLibro); else ps.setNull(5, Types.INTEGER);
            if (idPrestamo != null) ps.setInt(6, idPrestamo); else ps.setNull(6, Types.INTEGER);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al registrar operación en historial", e);
        }
    }

    public List<Historial> listarPorTipo(String tipoOperacion) throws DAOException {
        String sql = "SELECT * FROM Historial WHERE tipo_operacion = ? ORDER BY fecha DESC";
        List<Historial> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tipoOperacion);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearHistorial(rs));
            }
            return lista;

        } catch (SQLException e) {
            throw new DAOException("Error al listar historial por tipo de operación", e);
        }
    }

    // --- helper corregido (usa el socioDAO inyectado) ---
    private Historial mapearHistorial(ResultSet rs) throws SQLException {
        int idHistorial = rs.getInt("id_historial");
        int idUsuario = rs.getInt("id_usuario");
        Socio socio;

        try {
            socio = socioDAO.buscarPorId(idUsuario);
        } catch (DAOException e) {
            throw new SQLException("No se pudo reconstruir Socio para Historial (id " + idUsuario + ")", e);
        }

        if (socio == null) {
            throw new SQLException("Socio asociado no encontrado (id " + idUsuario + ")");
        }

        return new Historial(idHistorial, socio);
    }

    public List<Historial> listarPorSocioId(int idSocio) throws DAOException {
        String sql = "SELECT * FROM Historial WHERE id_usuario = ? ORDER BY fecha DESC";
        List<Historial> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idSocio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearHistorial(rs));
            }
            return lista;

        } catch (SQLException e) {
            throw new DAOException("Error al listar historial por socio", e);
        }
    }

    public List<Historial> listarPorLibroId(int idLibro) throws DAOException {
        String sql = "SELECT * FROM Historial WHERE id_libro = ? ORDER BY fecha DESC";
        List<Historial> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idLibro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearHistorial(rs));
            }
            return lista;

        } catch (SQLException e) {
            throw new DAOException("Error al listar historial por libro", e);
        }
    }
}