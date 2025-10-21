package biblioteca.entities.inventario;
import java.util.ArrayList;
import java.util.List;

public class Libro {
    private int id;
    private String titulo;
    private String autor;
    private String isbn;
    private String categoria;
    private String editorial;
    private int anioPublicacion;
    private List<Ejemplar> ejemplares;

    public Libro(int id, String titulo, String autor, String isbn, String categoria, String editorial, int anioPublicacion) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.categoria = categoria;
        this.editorial = editorial;
        this.anioPublicacion = anioPublicacion;
        this.ejemplares = new ArrayList<>();
    }

    public Libro obtenerDatos() {
        return this;
    }

    public void actualizarDatos(String nuevoTitulo, String nuevoAutor, String nuevaEditorial,
                                String nuevaCategoria, int nuevoAnio) {
        this.titulo = nuevoTitulo;
        this.autor = nuevoAutor;
        this.editorial = nuevaEditorial;
        this.categoria = nuevaCategoria;
        this.anioPublicacion = nuevoAnio;
    }

    public List<Ejemplar> obtenerEjemplares() {
        return ejemplares;
    }

    public int contarEjemplaresDisponibles() {
        int disponibles = 0;
        for (Ejemplar e : ejemplares) {
            if (e.verificarDisponibilidad()) {
                disponibles++;
            }
        }
        return disponibles;
    }

    public void agregarEjemplar(Ejemplar ejemplar) {
        this.ejemplares.add(ejemplar);
    }

    @Override
    public String toString() {
        return "Libro: " + titulo + " (" + anioPublicacion + ") - " + autor +
                "\nISBN: " + isbn + " | Categoría: " + categoria + " | Editorial: " + editorial +
                "\nEjemplares: " + ejemplares.size() +
                " | Disponibles: " + contarEjemplaresDisponibles();
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getIsbn() { return isbn; }
    public String getCategoria() { return categoria; }
    public String getEditorial() { return editorial; }
    public int getAnioPublicacion() { return anioPublicacion; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setAutor(String autor) { this.autor = autor; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setEditorial(String editorial) { this.editorial = editorial; }
    public void setAnioPublicacion(int anioPublicacion) { this.anioPublicacion = anioPublicacion; }

}
