package biblioteca.services;

import biblioteca.entities.inventario.Libro;
import biblioteca.entities.inventario.Ejemplar;

import java.util.ArrayList;
import java.util.List;

public class ControlLibros {

    private List<Libro> libros;

    public ControlLibros() {
        this.libros = new ArrayList<>();
    }

    /**
     * Registra un nuevo libro en el sistema.
     * Verifica que no exista otro con el mismo título y autor.
     */
    public void registrarLibro(Libro libro) {
        if (libro == null) {
            System.out.println("Error: El libro no puede ser nulo.");
            return;
        }

        for (Libro existente : libros) {
            if (existente.getTitulo().equalsIgnoreCase(libro.getTitulo()) &&
                    existente.getAutor().equalsIgnoreCase(libro.getAutor())) {
                System.out.println("Error: Ya existe un libro con el mismo título y autor.");
                return;
            }
        }

        libros.add(libro);
        System.out.println("Libro registrado correctamente: " + libro.getTitulo());
    }

    /**
     * Crea y asocia una cantidad determinada de ejemplares a un libro existente.
     */
    public void crearEjemplares(Libro libro, int cantidad) {
        if (libro == null) {
            System.out.println("Error: Libro no válido para crear ejemplares.");
            return;
        }
        if (cantidad <= 0) {
            System.out.println("Error: La cantidad de ejemplares debe ser mayor a 0.");
            return;
        }

        int inicio = libro.obtenerEjemplares().size() + 1;
        for (int i = 0; i < cantidad; i++) {
            Ejemplar ejemplar = new Ejemplar(
                    inicio + i,
                    "EJ-" + libro.getId() + "-" + (inicio + i),
                    "Disponible",
                    "Estante " + ((inicio + i) % 5 + 1),
                    libro
            );
            libro.agregarEjemplar(ejemplar);
        }

        System.out.println("Se crearon " + cantidad + " ejemplares para el libro: " + libro.getTitulo());
    }

    /**
     * Actualiza los datos principales del libro identificado por su ID.
     */
    public void actualizarInventario(int idLibro, String nuevoTitulo, String nuevoAutor,
                                     String nuevaEditorial, String nuevaCategoria, int nuevoAnio) {
        Libro libro = buscarLibro(idLibro);
        if (libro != null) {
            libro.actualizarDatos(nuevoTitulo, nuevoAutor, nuevaEditorial, nuevaCategoria, nuevoAnio);
            System.out.println("Datos del libro actualizados correctamente: " + libro.getTitulo());
        } else {
            System.out.println("Error: No se encontró un libro con ID " + idLibro);
        }
    }

    /**
     * Busca un libro por su identificador único.
     */
    public Libro buscarLibro(int id) {
        for (Libro l : libros) {
            if (l.getId() == id) {
                return l;
            }
        }
        System.out.println("No se encontró el libro con ID " + id);
        return null;
    }

    /**
     * Busca un libro por su título (insensible a mayúsculas/minúsculas).
     */
    public Libro buscarLibroPorTitulo(String titulo) {
        for (Libro l : libros) {
            if (l.getTitulo().equalsIgnoreCase(titulo)) {
                return l;
            }
        }
        System.out.println("No se encontró el libro con título: " + titulo);
        return null;
    }

    /**
     * Verifica si el libro tiene al menos un ejemplar disponible.
     */
    public boolean verificarDisponibilidad(Libro libro) {
        if (libro == null) return false;
        return libro.contarEjemplaresDisponibles() > 0;
    }

    /**
     * Muestra por consola el inventario completo de libros.
     */
    public void mostrarLibros() {
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en el sistema.");
            return;
        }

        System.out.println("=== INVENTARIO DE LIBROS ===");
        for (Libro l : libros) {
            System.out.println(l);
        }
    }

    public List<Libro> getLibros() {
        return new ArrayList<>(libros); // devuelve copia para evitar modificaciones externas
    }
}

