package biblioteca.entities.inventario;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Ejemplar {

    private int idEjemplar;
    private String codigo;
    private String estado;
    private String ubicacion;
    private Libro libro;

    private static final List<String> ESTADOS_VALIDOS = Arrays.asList("Disponible", "Prestado", "Dañado", "Extraviado");

    // Constructor completo
    public Ejemplar(int idEjemplar, String codigo, String estado, String ubicacion, Libro libro) {
        this.idEjemplar = idEjemplar;
        this.codigo = codigo;
        setEstado(estado); // usa setter con validación
        this.ubicacion = ubicacion;
        this.libro = libro;
    }

    // Constructor simplificado, útil para pruebas y formularios
    public Ejemplar(int idEjemplar, String codigo, boolean disponible) {
        this.idEjemplar = idEjemplar;
        this.codigo = codigo;
        this.estado = disponible ? "Disponible" : "Prestado";
        this.ubicacion = "Sin asignar";
        this.libro = null;
    }

    // Métodos de comportamiento
    public void cambiarEstado(String nuevoEstado) {
        setEstado(nuevoEstado);
    }

    public boolean verificarDisponibilidad() {
        return "Disponible".equalsIgnoreCase(estado);
    }

    public void marcarComoPrestado() {
        setEstado("Prestado");
    }

    public void marcarComoDisponible() {
        setEstado("Disponible");
    }

    public void setEstado(String estado) {
        if (!ESTADOS_VALIDOS.contains(estado)) {
            throw new IllegalArgumentException("Estado de ejemplar no válido: " + estado);
        }
        this.estado = estado;
    }

    // Getters y setters
    public int getIdEjemplar() { return idEjemplar; }
    public String getCodigo() { return codigo; }
    public String getEstado() { return estado; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public Libro getLibro() { return libro; }
    public void setLibro(Libro libro) { this.libro = libro; }

    @Override
    public String toString() {
        return "Ejemplar{" +
                "codigo='" + codigo + '\'' +
                ", estado='" + estado + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", libro=" + (libro != null ? libro.getTitulo() : "Sin asignar") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ejemplar)) return false;
        Ejemplar ejemplar = (Ejemplar) o;
        return Objects.equals(codigo, ejemplar.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}


