package biblioteca.entities.prestamos;

import biblioteca.entities.inventario.Ejemplar;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Devolucion {

    private int id;
    private LocalDate fechaDevolucion;
    private String estadoEjemplar;
    private String observaciones;
    private Prestamo prestamo;

    public Devolucion(int id, LocalDate fechaDevolucion, String estadoEjemplar,
                      String observaciones, Prestamo prestamo) {

        if (fechaDevolucion == null) {
            throw new IllegalArgumentException("La fecha de devolución no puede ser nula.");
        }
        if (prestamo == null) {
            throw new IllegalArgumentException("El préstamo asociado no puede ser nulo.");
        }

        this.id = id;
        this.fechaDevolucion = fechaDevolucion;
        this.estadoEjemplar = estadoEjemplar != null ? estadoEjemplar : "Disponible";
        this.observaciones = observaciones;
        this.prestamo = prestamo;

        // Actualiza estado del préstamo y ejemplar
        prestamo.marcarComoDevuelto();
        if (prestamo.getEjemplar() != null) {
            prestamo.getEjemplar().setEstado(estadoEjemplar);
        }
    }

    public double calcularMulta(PoliticaPrestamo politica) {
        if (politica == null || prestamo == null) return 0.0;

        long diasAtraso = ChronoUnit.DAYS.between(prestamo.getFechaVencimiento(), fechaDevolucion);
        return diasAtraso > 0 ? diasAtraso * politica.getMultaPorDia() : 0.0;
    }

    public void registrarObservaciones(String nuevasObservaciones) {
        this.observaciones = nuevasObservaciones;
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public boolean verificarEstadoEjemplar() {
        Ejemplar ejemplar = prestamo != null ? prestamo.getEjemplar() : null;
        return ejemplar != null && "Disponible".equalsIgnoreCase(ejemplar.getEstado());
    }

    // Getters y setters
    public int getId() { return id; }
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public String getEstadoEjemplar() { return estadoEjemplar; }
    public String getObservaciones() { return observaciones; }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public void setEstadoEjemplar(String estadoEjemplar) {
        this.estadoEjemplar = estadoEjemplar;
        if (prestamo != null && prestamo.getEjemplar() != null) {
            prestamo.getEjemplar().setEstado(estadoEjemplar);
        }
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }

    @Override
    public String toString() {
        return "Devolución #" + id +
                " | Fecha: " + fechaDevolucion +
                " | Estado ejemplar: " + estadoEjemplar +
                " | Observaciones: " + (observaciones != null ? observaciones : "N/A") +
                " | Préstamo ID: " + (prestamo != null ? prestamo.getId() : "N/A");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Devolucion)) return false;
        Devolucion that = (Devolucion) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

