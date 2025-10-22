package biblioteca.entities.prestamos;

import biblioteca.entities.inventario.Ejemplar;
import biblioteca.entities.usuarios.Socio;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Prestamo {

    private int id;
    private LocalDate fechaPrestamo;
    private LocalDate fechaVencimiento;
    private String estado; // "Activo", "Devuelto", "Vencido"
    private int diasPrestamo;
    private Socio socio;
    private Ejemplar ejemplar;

    public Prestamo(int id, LocalDate fechaPrestamo, LocalDate fechaVencimiento,
                    String estado, int diasPrestamo, Socio socio, Ejemplar ejemplar) {
        if (fechaPrestamo == null || fechaVencimiento == null) {
            throw new IllegalArgumentException("Las fechas de préstamo y vencimiento no pueden ser nulas.");
        }
        if (fechaVencimiento.isBefore(fechaPrestamo)) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede ser anterior a la fecha de préstamo.");
        }
        if (socio == null) {
            throw new IllegalArgumentException("El socio no puede ser nulo.");
        }
        if (ejemplar == null) {
            throw new IllegalArgumentException("El ejemplar no puede ser nulo.");
        }

        this.id = id;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaVencimiento = fechaVencimiento;
        this.socio = socio;
        this.ejemplar = ejemplar;
        this.diasPrestamo = (int) ChronoUnit.DAYS.between(fechaPrestamo, fechaVencimiento);
        this.estado = "Activo";

        // Marcar el ejemplar como prestado
        this.ejemplar.marcarComoPrestado();
        // Asociar este préstamo al socio
        this.socio.agregarPrestamo(this);
    }

    public int calcularDiasVencimiento() {
        return diasPrestamo;
    }

    public boolean estaVencido() {
        if (fechaVencimiento == null) return false;
        return LocalDate.now().isAfter(fechaVencimiento) && !"Devuelto".equalsIgnoreCase(estado);
    }

    public Socio getSocio() {
        return socio;
    }

    public Ejemplar getEjemplar() {
        return ejemplar;
    }

    public void marcarComoDevuelto() {
        this.estado = "Devuelto";
        if (ejemplar != null) {
            ejemplar.marcarComoDisponible();
        }
    }

    public void actualizarEstado() {
        if ("Devuelto".equalsIgnoreCase(estado)) return;
        if (estaVencido()) {
            estado = "Vencido";
        } else {
            estado = "Activo";
        }
    }

    // Getters y setters
    public int getId() { return id; }
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public String getEstado() { return estado; }
    public int getDiasPrestamo() { return diasPrestamo; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Préstamo #" + id +
                " | Socio: " + (socio != null ? socio.getNombreCompleto() : "N/A") +
                " | Ejemplar: " + (ejemplar != null ? ejemplar.getCodigo() : "N/A") +
                " | Estado: " + estado +
                " | Fecha Préstamo: " + fechaPrestamo +
                " | Vence: " + fechaVencimiento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prestamo)) return false;
        Prestamo prestamo = (Prestamo) o;
        return id == prestamo.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

