package biblioteca.data.dao;

import biblioteca.data.db.ConexionBD;
import biblioteca.data.interfaces.DAO;
import biblioteca.entities.inventario.Ejemplar;
import biblioteca.entities.inventario.Libro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EjemplarDAO implements DAO<Ejemplar> {

    @Override
    public void insertar(Ejemplar ejemplar) throws DAOException {
        if (ejemplar == null || ejemplar.getLibro() == null) {
            throw new DAOException("El ejemplar o su libro asociado no pueden ser nulos.");
        }

        String sql = "INSERT INTO Ejemplar (codigo_ejemplar, estado, ubicacion, id_libro) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, ejemplar.getCodigo());
            ps.setString(2, ejemplar.getEstado());
            ps.setString(3, ejemplar.getUbicacion());
            ps.setInt(4, ejemplar.getLibro().getId());

            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new DAOException("No se pudo insertar el ejemplar en la base de datos.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    ejemplar.setIdEjemplar(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Error al insertar ejemplar: " + e.getMessage(), e);
        }
    }

    @Override
    public Ejemplar buscarPorId(int id) throws DAOException {
        String sql = """
                SELECT e.id_ejemplar, e.codigo_ejemplar, e.estado, e.ubicacion,
                       l.id_libro, l.titulo, l.isbn, l.categoria, l.editorial, l.anio_publicacion, l.autor
                FROM Ejemplar e
                JOIN Libro l ON e.id_libro = l.id_libro
                WHERE e.id_ejemplar = ?
                """;
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearEjemplar(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al buscar ejemplar por ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Ejemplar> listarTodos() throws DAOException {
        String sql = """
                SELECT e.id_ejemplar, e.codigo_ejemplar, e.estado, e.ubicacion,
                       l.id_libro, l.titulo, l.isbn, l.categoria, l.editorial, l.anio_publicacion, l.autor
                FROM Ejemplar e
                JOIN Libro l ON e.id_libro = l.id_libro
                ORDER BY e.id_ejemplar
                """;
        List<Ejemplar> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearEjemplar(rs));
            }
            return lista;

        } catch (SQLException e) {
            throw new DAOException("Error al listar ejemplares: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizar(Ejemplar ejemplar) throws DAOException {
        if (ejemplar == null || ejemplar.getLibro() == null) {
            throw new DAOException("El ejemplar o su libro asociado no pueden ser nulos.");
        }

        String sql = "UPDATE Ejemplar SET codigo_ejemplar = ?, estado = ?, ubicacion = ?, id_libro = ? WHERE id_ejemplar = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ejemplar.getCodigo());
            ps.setString(2, ejemplar.getEstado());
            ps.setString(3, ejemplar.getUbicacion());
            ps.setInt(4, ejemplar.getLibro().getId());
            ps.setInt(5, ejemplar.getIdEjemplar());

            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new DAOException("No se pudo actualizar el ejemplar con ID " + ejemplar.getIdEjemplar());
            }

        } catch (SQLException e) {
            throw new DAOException("Error al actualizar ejemplar: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(int id) throws DAOException {
        String sql = "DELETE FROM Ejemplar WHERE id_ejemplar = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new DAOException("No se pudo eliminar el ejemplar con ID " + id);
            }

        } catch (SQLException e) {
            throw new DAOException("Error al eliminar ejemplar: " + e.getMessage(), e);
        }
    }

    // Métodos específicos
    public Ejemplar buscarPorCodigo(String codigo) throws DAOException {
        String sql = """
            SELECT 
                e.id_ejemplar, e.codigo_ejemplar, e.estado, e.ubicacion,
                l.id_libro, l.titulo, l.isbn, l.categoria,
                l.id_autor, l.id_editorial, l.anio_publicacion
            FROM Ejemplar e
            JOIN Libro l ON e.id_libro = l.id_libro
            WHERE e.codigo_ejemplar = ?
        """;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearEjemplar(rs);
                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Error al buscar ejemplar por código: " + e.getMessage(), e);
        }
    }

    public List<Ejemplar> listarPorLibro(Libro libro) throws DAOException {
        if (libro == null) throw new DAOException("El libro no puede ser nulo.");
        String sql = """
                SELECT e.id_ejemplar, e.codigo_ejemplar, e.estado, e.ubicacion,
                       l.id_libro, l.titulo, l.isbn, l.categoria, l.editorial, l.anio_publicacion, l.autor
                FROM Ejemplar e
                JOIN Libro l ON e.id_libro = l.id_libro
                WHERE l.id_libro = ?
                ORDER BY e.id_ejemplar
                """;
        List<Ejemplar> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, libro.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearEjemplar(rs));
                }
            }
            return lista;

        } catch (SQLException e) {
            throw new DAOException("Error al listar ejemplares por libro: " + e.getMessage(), e);
        }
    }

    // Mapeo ResultSet → Ejemplar con objeto Libro incluido
    private Ejemplar mapearEjemplar(ResultSet rs) throws SQLException {

        Libro libro = new Libro(
                rs.getInt("id_libro"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getString("isbn"),
                rs.getString("categoria"),
                rs.getString("editorial"),
                rs.getInt("anio_publicacion")
        );

        return new Ejemplar(
                rs.getInt("id_ejemplar"),
                rs.getString("codigo_ejemplar"), // este es el nombre real de la BD
                rs.getString("estado"),
                rs.getString("ubicacion"),
                libro
        );
    }
}

