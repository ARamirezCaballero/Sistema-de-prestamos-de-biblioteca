package biblioteca.entities.prestamos;

import biblioteca.entities.inventario.Ejemplar;
import biblioteca.entities.usuarios.Socio;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Prestamo {

    private int id;
    private LocalDate fechaPrestamo;
    private LocalDate fechaVencimiento;
    private String estado;
    private int diasPrestamo;
    private Socio socio;
    private Ejemplar ejemplar;

    public Prestamo(int id, LocalDate fechaPrestamo, LocalDate fechaVencimiento,  String estado, int diasPrestamo, Socio socio, Ejemplar ejemplar) {
        this.id = id;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.diasPrestamo = diasPrestamo;
        this.socio = socio;
        this.ejemplar = ejemplar;
    }

    public int calcularDiasVencimiento() {
        return (int) ChronoUnit.DAYS.between(fechaPrestamo, fechaVencimiento);
    }

    public boolean estaVencido() {
        LocalDate hoy = LocalDate.now();
        return hoy.isAfter(fechaVencimiento);
    }

    public Socio obtenerSocio() {
        return socio;
    }

    public Ejemplar obtenerEjemplar() {
        return ejemplar;
    }

    public void marcarComoDevuelto() {
        this.estado = "Devuelto";
        if (ejemplar != null) {
            ejemplar.marcarComoDisponible();
        }
    }

    public void actualizarEstado() {
        if (this.estado.equalsIgnoreCase("Devuelto")) {
            return;
        }
        if (estaVencido()) {
            this.estado = "Vencido";
        } else {
            this.estado = "Activo";
        }
    }

    public int getId() { return id; }
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public String getEstado() { return estado; }
    public int getDiasPrestamo() { return diasPrestamo; }

    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Préstamo #" + id +
                " | Socio: " + (socio != null ? socio.getNombre() : "N/A") +
                " | Ejemplar: " + (ejemplar != null ? ejemplar.getCodigo() : "N/A") +
                " | Estado: " + estado +
                " | Vence: " + fechaVencimiento;
    }

}
