package biblioteca.services;

import biblioteca.data.dao.DAOException;
import biblioteca.data.dao.LibroDAO;
import biblioteca.data.dao.EjemplarDAO;
import biblioteca.entities.inventario.Ejemplar;
import biblioteca.entities.inventario.Libro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControlLibros {

    private final LibroDAO libroDAO;
    private final EjemplarDAO ejemplarDAO;

    public ControlLibros(LibroDAO libroDAO, EjemplarDAO ejemplarDAO) {
        this.libroDAO = libroDAO;
        this.ejemplarDAO = ejemplarDAO;
    }

    // REGISTRO DE LIBROS
    public void registrarLibro(Libro libro) throws DAOException {
        if (libro == null) throw new IllegalArgumentException("Libro no puede ser null");
        libroDAO.insertar(libro); // id se asigna automáticamente
    }

    // CREACIÓN Y REGISTRO DE EJEMPLARES
    public void crearEjemplares(int idLibro,
                                int cantidad,
                                String estado,
                                String ubicacion) throws DAOException {

        Libro libro = libroDAO.buscarPorId(idLibro);
        if (libro == null) {
            throw new DAOException("No existe un libro con ID " + idLibro);
        }

        for (int i = 0; i < cantidad; i++) {
            String codigo = "LIB" + idLibro + "-EJ" + String.format("%03d", i + 1); // LIB1-EJ001
            Ejemplar ej = new Ejemplar(0, codigo, estado, ubicacion, libro);
            ejemplarDAO.insertar(ej);
            libro.agregarEjemplar(ej);
        }
    }

    // BÚSQUEDAS
    public Libro buscarLibro(int id) throws DAOException {
        return libroDAO.buscarPorId(id);
    }

    public Libro buscarLibroPorISBN(String isbn) throws DAOException {
        return libroDAO.obtenerPorISBN(isbn);
    }

    // INVENTARIO
    public void actualizarInventario(int idLibro,
                                     String nuevoTitulo,
                                     String nuevoAutor,
                                     String nuevaEditorial,
                                     String nuevaCategoria,
                                     int nuevoAnio) throws DAOException {

        Libro libro = libroDAO.buscarPorId(idLibro);
        if (libro == null) throw new DAOException("No existe un libro con ID " + idLibro);

        libro.actualizarDatos(nuevoTitulo, nuevoAutor, nuevaEditorial, nuevaCategoria, nuevoAnio);
        libroDAO.actualizar(libro);
    }

    // LISTADOS
    public List<Libro> listarLibros() throws DAOException {
        return libroDAO.listarTodos();
    }

    public List<Map<String, Object>> listarLibrosConDetalleEjemplares() throws DAOException {
        List<Libro> libros = libroDAO.listarTodos();
        List<Map<String, Object>> resultado = new ArrayList<>();

        for (Libro libro : libros) {
            int disponibles = 0;
            int prestados = 0;

            List<Ejemplar> ejemplares = ejemplarDAO.listarPorLibro(libro);
            for (Ejemplar e : ejemplares) {
                if ("Disponible".equalsIgnoreCase(e.getEstado())) disponibles++;
                else if ("Prestado".equalsIgnoreCase(e.getEstado())) prestados++;
            }

            Map<String, Object> detalle = new HashMap<>();
            detalle.put("libro", libro);
            detalle.put("totalEjemplares", ejemplares.size());
            detalle.put("disponibles", disponibles);
            detalle.put("prestados", prestados);

            resultado.add(detalle);
        }

        return resultado;
    }
}
