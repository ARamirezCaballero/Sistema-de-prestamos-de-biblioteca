package biblioteca.entities.inventario;

public class Libro {
    private int id;
    private String titulo;
    private String autor;
    private String isbn;
    private String categoria;
    private String editorial;
    private int anioPublicacion;

    public Libro(int id, String titulo, String autor, String isbn, String categoria, String editorial ,int anioPublicacion) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.categoria = categoria;
        this.editorial = editorial;
        this.anioPublicacion = anioPublicacion;
    }


}
