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
    private double multa;
    private Prestamo prestamo;

    public Devolucion(int id, LocalDate fechaDevolucion, String estadoEjemplar,
                      String observaciones, Prestamo prestamo, double multa) {

        if (fechaDevolucion == null) throw new IllegalArgumentException("La fecha de devolución no puede ser nula.");
        if (prestamo == null) throw new IllegalArgumentException("El préstamo asociado no puede ser nulo.");

        this.id = id;
        this.fechaDevolucion = fechaDevolucion;
        this.estadoEjemplar = estadoEjemplar != null ? estadoEjemplar : "Disponible";
        this.observaciones = observaciones != null ? observaciones : "";
        this.prestamo = prestamo;
        this.multa = multa;

        // Actualiza estado del préstamo y del ejemplar
        prestamo.marcarComoDevuelto();
        if (prestamo.getEjemplar() != null) {
            prestamo.getEjemplar().setEstado(this.estadoEjemplar);
        }
    }

    // Genera un string listo para mostrar en la UI o VisorComprobante
    public String formatearParaUI() {
        StringBuilder sb = new StringBuilder();
        sb.append("Devolución #").append(id).append("\n");
        sb.append("Fecha: ").append(fechaDevolucion).append("\n");
        sb.append("Estado del ejemplar: ").append(estadoEjemplar).append("\n");
        sb.append("Observaciones: ").append(observaciones.isBlank() ? "N/A" : observaciones).append("\n");
        sb.append("Préstamo ID: ").append(prestamo != null ? prestamo.getId() : "N/A").append("\n");
        sb.append("Multa: $").append(multa);
        return sb.toString();
    }

    // Getters y Setters
    public int getId() { return id; }
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public String getEstadoEjemplar() { return estadoEjemplar; }
    public String getObservaciones() { return observaciones; }
    public double getMulta() { return multa; }
    public Prestamo getPrestamo() { return prestamo; }

    public void setEstadoEjemplar(String estadoEjemplar) {
        this.estadoEjemplar = estadoEjemplar;
        if (prestamo != null && prestamo.getEjemplar() != null) {
            prestamo.getEjemplar().setEstado(estadoEjemplar);
        }
    }

    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Devolucion)) return false;
        Devolucion that = (Devolucion) o;
        return id == that.id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}


