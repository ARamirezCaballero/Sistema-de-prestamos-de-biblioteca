package biblioteca.entities.inventario;

import biblioteca.entities.prestamos.Prestamo;

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
        this.estado = estado;
        this.ubicacion = ubicacion;
        this.libro = libro;
    }

    // Constructor simplificado
    public Ejemplar(int idEjemplar, String codigo, boolean disponible) {
        this.idEjemplar = idEjemplar;
        this.codigo = codigo;
        this.estado = disponible ? "Disponible" : "Prestado";
        this.ubicacion = "Sin asignar";
        this.libro = null;
    }

    // Constructor alternativo usado en BaseDeDatosSimulada
    public Ejemplar(String codigo, Libro libro, String estado) {
        this.idEjemplar = (int) (Math.random() * 1000);
        this.codigo = codigo;
        this.libro = libro;
        setEstado(estado);
        this.ubicacion = "Depósito";
    }

    // Paso 5 del flujo CU03
    public boolean verificarDisponibilidad() {
        return "Disponible".equalsIgnoreCase(consultarEstado());
    }

    // Paso 6 del flujo CU03
    public String consultarEstado() {
        return estado;
    }

    // Paso 22 del flujo CU03
    public void actualizarEstado(String nuevoEstado) {
        if (!ESTADOS_VALIDOS.contains(nuevoEstado))
            throw new IllegalArgumentException("Estado no válido: " + nuevoEstado);
        this.estado = nuevoEstado;
    }

    // Paso 23 del flujo CU03
    public void cambiarEstadoBD(String nuevoEstado) {
        // Simulación de actualización en la “BD”
        actualizarEstado(nuevoEstado);
        System.out.println("[BD] Estado del ejemplar " + codigo + " actualizado a: " + nuevoEstado);
    }

    public void marcarComoPrestado() {
        cambiarEstadoBD("Prestado");
    }

    public void marcarComoDisponible() {
        cambiarEstadoBD("Disponible");
    }

    public static boolean esEstadoValido(String estado) {
        return ESTADOS_VALIDOS.contains(estado);
    }

    // ======== Getters y Setters ========
    public int getIdEjemplar() { return idEjemplar; }
    public void setIdEjemplar(int idEjemplar) {
        if (idEjemplar <= 0) throw new IllegalArgumentException("El ID del ejemplar debe ser positivo.");
        this.idEjemplar = idEjemplar;
    }
    public String getCodigo() { return codigo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) {
        if (!ESTADOS_VALIDOS.contains(estado))
            throw new IllegalArgumentException("Estado no válido: " + estado);
        this.estado = estado;
    }
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