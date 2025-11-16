package biblioteca.data.dao;

import biblioteca.entities.inventario.Libro;
import biblioteca.entities.inventario.Ejemplar;
import biblioteca.data.db.ConexionBD;

import java.sql.*;
import java.util.*;

public class LibroDAO implements biblioteca.data.interfaces.DAO<Libro> {

    private final AutorDAO autorDAO = new AutorDAO();
    private final EditorialDAO editorialDAO = new EditorialDAO();

    @Override
    public void insertar(Libro libro) throws DAOException {
        if (libro == null) throw new IllegalArgumentException("Libro no puede ser null");

        Connection conn = null;

        try {
            conn = ConexionBD.getConexion();
            conn.setAutoCommit(false);

            int idAutor = autorDAO.obtenerIdPorNombre(conn, libro.getAutor());
            if (idAutor == -1) idAutor = autorDAO.insertarAutor(conn, libro.getAutor());

            int idEditorial = editorialDAO.obtenerIdPorNombre(conn, libro.getEditorial());
            if (idEditorial == -1) idEditorial = editorialDAO.insertarEditorial(conn, libro.getEditorial());

            String sql = """
                INSERT INTO Libro (titulo, id_autor, isbn, categoria, id_editorial, anio_publicacion)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, libro.getTitulo());
            ps.setInt(2, idAutor);
            ps.setString(3, libro.getIsbn());
            ps.setString(4, libro.getCategoria());
            ps.setInt(5, idEditorial);
            ps.setInt(6, libro.getAnioPublicacion());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) libro.setId(rs.getInt(1));

            rs.close();
            ps.close();

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ignored) {}
            throw new DAOException("Error al insertar libro: " + e.getMessage(), e);
        }
    }

    @Override
    public Libro buscarPorId(int id) throws DAOException {
        String sql = """
            SELECT l.id_libro, l.titulo, l.id_autor, l.isbn, l.categoria, l.id_editorial, l.anio_publicacion,
                   e.id_ejemplar, e.codigo_ejemplar, e.estado, e.ubicacion
            FROM Libro l
            LEFT JOIN Ejemplar e ON l.id_libro = e.id_libro
            WHERE l.id_libro = ?
            """;

        try {
            Connection conn = ConexionBD.getConexion();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            Libro libro = null;
            while (rs.next()) {
                if (libro == null) libro = mapearLibro(rs);
                Ejemplar ej = mapearEjemplar(rs);
                if (ej != null) libro.agregarEjemplar(ej);
            }

            rs.close();
            ps.close();

            return libro;

        } catch (SQLException e) {
            throw new DAOException("Error al buscar libro por ID", e);
        }
    }

    @Override
    public List<Libro> listarTodos() throws DAOException {
        String sql = """
            SELECT l.id_libro, l.titulo, l.id_autor, l.isbn, l.categoria, l.id_editorial, l.anio_publicacion,
                   e.id_ejemplar, e.codigo_ejemplar, e.estado, e.ubicacion
            FROM Libro l
            LEFT JOIN Ejemplar e ON l.id_libro = e.id_libro
            ORDER BY l.id_libro
            """;

        try {
            Connection conn = ConexionBD.getConexion();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            Map<Integer, Libro> map = new LinkedHashMap<>();

            while (rs.next()) {
                int id = rs.getInt("id_libro");
                Libro libro = map.get(id);

                if (libro == null) {
                    libro = mapearLibro(rs);
                    map.put(id, libro);
                }

                Ejemplar ej = mapearEjemplar(rs);
                if (ej != null) libro.agregarEjemplar(ej);
            }

            rs.close();
            ps.close();

            return new ArrayList<>(map.values());

        } catch (SQLException e) {
            throw new DAOException("Error al listar todos los libros", e);
        }
    }

    @Override
    public void actualizar(Libro libro) throws DAOException {
        String sql = """
            UPDATE Libro SET titulo = ?, id_autor = ?, categoria = ?, id_editorial = ?, anio_publicacion = ?
            WHERE id_libro = ?
            """;

        try {
            Connection conn = ConexionBD.getConexion();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, libro.getTitulo());
            ps.setInt(2, Integer.parseInt(libro.getAutor()));
            ps.setString(3, libro.getCategoria());
            ps.setInt(4, Integer.parseInt(libro.getEditorial()));
            ps.setInt(5, libro.getAnioPublicacion());
            ps.setInt(6, libro.getId());

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new DAOException("Error al actualizar libro", e);
        }
    }

    @Override
    public void eliminar(int id) throws DAOException {
        String sql = "DELETE FROM Libro WHERE id_libro = ?";

        try {
            Connection conn = ConexionBD.getConexion();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new DAOException("Error al eliminar libro", e);
        }
    }

    public Libro obtenerPorTitulo(String titulo) throws DAOException {
        String sql = "SELECT * FROM Libro l LEFT JOIN Ejemplar e ON l.id_libro = e.id_libro WHERE l.titulo = ?";

        try {
            Connection conn = ConexionBD.getConexion();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, titulo);

            ResultSet rs = ps.executeQuery();
            Libro libro = null;

            while (rs.next()) {
                if (libro == null) libro = mapearLibro(rs);
                Ejemplar ej = mapearEjemplar(rs);
                if (ej != null) libro.agregarEjemplar(ej);
            }

            rs.close();
            ps.close();

            return libro;

        } catch (SQLException e) {
            throw new DAOException("Error al obtener libro por título", e);
        }
    }

    public Libro obtenerPorISBN(String isbn) throws DAOException {
        String sql = "SELECT * FROM Libro l LEFT JOIN Ejemplar e ON l.id_libro = e.id_libro WHERE l.isbn = ?";

        try {
            Connection conn = ConexionBD.getConexion();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, isbn);

            ResultSet rs = ps.executeQuery();
            Libro libro = null;

            while (rs.next()) {
                if (LibroDAO.this == null) {}
                if (libro == null) libro = mapearLibro(rs);
                Ejemplar ej = mapearEjemplar(rs);
                if (ej != null) libro.agregarEjemplar(ej);
            }

            rs.close();
            ps.close();

            return libro;

        } catch (SQLException e) {
            throw new DAOException("Error al obtener libro por ISBN", e);
        }
    }


    // -------------------------
    // MÉTODOS AUXILIARES
    // -------------------------

    private Libro mapearLibro(ResultSet rs) throws SQLException {
        return new Libro(
                rs.getInt("id_libro"),
                rs.getString("titulo"),
                rs.getString("id_autor"),
                rs.getString("isbn"),
                rs.getString("categoria"),
                rs.getString("id_editorial"),
                rs.getInt("anio_publicacion")
        );
    }

    private Ejemplar mapearEjemplar(ResultSet rs) throws SQLException {
        int idEj = rs.getInt("id_ejemplar");
        if (rs.wasNull()) return null;

        return new Ejemplar(
                idEj,
                rs.getString("codigo_ejemplar"),
                rs.getString("estado"),
                rs.getString("ubicacion"),
                null
        );
    }
}
