package biblioteca.entities.inventario;

public class Ejemplar {

    private int id;
    private String codigo;
    private String estado;
    private String ubicacion;
    private Libro libro;

    public Ejemplar(int id, String codigo, String estado, String ubicacion, Libro libro) {
        this.id = id;
        this.codigo = codigo;
        this.estado = estado;
        this.ubicacion = ubicacion;
        this.libro = libro;
    }

    public void cambiarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public boolean verificarDisponibilidad() {
        return estado.equalsIgnoreCase("Disponible");
    }

    public Libro obtenerLibro() {
        return libro;
    }

    public void marcarComoPrestado() {
        this.estado = "Prestado";
    }

    public void marcarComoDisponible() {
        this.estado = "Disponible";
    }

    public int getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getEstado() { return estado; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public void setLibro(Libro libro) { this.libro = libro; }

    @Override
    public String toString() {
        return "Ejemplar [" + codigo + "] - Estado: " + estado +
                " | Ubicación: " + ubicacion +
                " | Libro: " + (libro != null ? libro.getTitulo() : "Sin asignar");
    }

}
