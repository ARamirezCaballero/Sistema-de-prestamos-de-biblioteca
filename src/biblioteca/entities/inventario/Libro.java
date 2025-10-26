package biblioteca.entities.inventario;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Libro {
    private final int id;
    private String titulo;
    private String autor;
    private final String isbn;
    private String categoria;
    private String editorial;
    private int anioPublicacion;
    private final List<Ejemplar> ejemplares;

    public Libro(int id, String titulo, String autor, String isbn, String categoria,
                 String editorial, int anioPublicacion) {

        if (id <= 0) throw new IllegalArgumentException("El ID del libro debe ser positivo.");
        if (titulo == null || titulo.isBlank()) throw new IllegalArgumentException("El título no puede estar vacío.");
        if (autor == null || autor.isBlank()) throw new IllegalArgumentException("El autor no puede estar vacío.");
        if (isbn == null || isbn.isBlank()) throw new IllegalArgumentException("El ISBN no puede estar vacío.");
        if (anioPublicacion <= 0) throw new IllegalArgumentException("El año de publicación debe ser válido.");

        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.categoria = categoria;
        this.editorial = editorial;
        this.anioPublicacion = anioPublicacion;
        this.ejemplares = new ArrayList<>();
    }

    // metodos funcionales

    public void actualizarDatos(String nuevoTitulo, String nuevoAutor, String nuevaEditorial,
                                String nuevaCategoria, int nuevoAnio) {
        if (nuevoTitulo != null && !nuevoTitulo.isBlank()) this.titulo = nuevoTitulo;
        if (nuevoAutor != null && !nuevoAutor.isBlank()) this.autor = nuevoAutor;
        if (nuevaEditorial != null && !nuevaEditorial.isBlank()) this.editorial = nuevaEditorial;
        if (nuevaCategoria != null && !nuevaCategoria.isBlank()) this.categoria = nuevaCategoria;
        if (nuevoAnio > 0) this.anioPublicacion = nuevoAnio;
    }

    public void agregarEjemplar(Ejemplar ejemplar) {
        if (ejemplar == null) throw new IllegalArgumentException("El ejemplar no puede ser nulo.");
        if (ejemplares.contains(ejemplar))
            throw new IllegalArgumentException("El ejemplar ya está registrado en este libro.");
        ejemplares.add(ejemplar);
    }

    public List<Ejemplar> obtenerEjemplares() {
        return new ArrayList<>(ejemplares); // copia defensiva
    }

    public int contarEjemplaresDisponibles() {
        return (int) ejemplares.stream()
                .filter(e -> e != null && "Disponible".equalsIgnoreCase(e.getEstado()))
                .count();
    }

    public int contarEjemplaresPrestados() {
        return (int) ejemplares.stream()
                .filter(e -> e != null && "Prestado".equalsIgnoreCase(e.getEstado()))
                .count();
    }

    public int contarTotalEjemplares() {
        return ejemplares.size();
    }

    // getters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getIsbn() { return isbn; }
    public String getCategoria() { return categoria; }
    public String getEditorial() { return editorial; }
    public int getAnioPublicacion() { return anioPublicacion; }

    // setters
    public void setTitulo(String titulo) {
        if (titulo == null || titulo.isBlank())
            throw new IllegalArgumentException("El título no puede estar vacío.");
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        if (autor == null || autor.isBlank())
            throw new IllegalArgumentException("El autor no puede estar vacío.");
        this.autor = autor;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public void setAnioPublicacion(int anioPublicacion) {
        if (anioPublicacion <= 0)
            throw new IllegalArgumentException("El año de publicación debe ser válido.");
        this.anioPublicacion = anioPublicacion;
    }

    @Override
    public String toString() {
        return "Libro: " + titulo + " (" + anioPublicacion + ") - " + autor +
                " | ISBN: " + isbn +
                " | Categoría: " + categoria +
                " | Editorial: " + editorial +
                " | Ejemplares: " + ejemplares.size() +
                " | Disponibles: " + contarEjemplaresDisponibles();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Libro)) return false;
        Libro libro = (Libro) o;
        return Objects.equals(isbn, libro.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
}

