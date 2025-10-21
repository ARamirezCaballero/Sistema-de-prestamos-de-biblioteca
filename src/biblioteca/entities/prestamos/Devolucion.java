package biblioteca.entities.prestamos;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Devolucion {

    private int id;
    private LocalDate fechaDevolucion;
    private String estadoEjemplar;
    private String observaciones;
    private Prestamo prestamo;

    public Devolucion(int id, LocalDate fechaDevolucion, String estadoEjemplar,
                      String observaciones, Prestamo prestamo) {
        this.id = id;
        this.fechaDevolucion = fechaDevolucion;
        this.estadoEjemplar = estadoEjemplar;
        this.observaciones = observaciones;
        this.prestamo = prestamo;
    }

    public double calcularMulta(PoliticaPrestamo politica) {
        double multa = 0.0;
        long diasAtraso = ChronoUnit.DAYS.between(prestamo.getFechaVencimiento(), fechaDevolucion);

        if (diasAtraso > 0) {
            multa = diasAtraso * politica.getMultaPorDia();
        }
        return multa;
    }

    public void registrarObservaciones(String nuevasObservaciones) {
        this.observaciones = nuevasObservaciones;
    }

    public Prestamo obtenerPrestamo() {
        return prestamo;
    }

    public boolean verificarEstadoEjemplar() {
        return estadoEjemplar.equalsIgnoreCase("Disponible");
    }

    public int getId() { return id; }
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public String getEstadoEjemplar() { return estadoEjemplar; }
    public String getObservaciones() { return observaciones; }

    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }
    public void setEstadoEjemplar(String estadoEjemplar) { this.estadoEjemplar = estadoEjemplar; }
    public void setPrestamo(Prestamo prestamo) { this.prestamo = prestamo; }

    @Override
    public String toString() {
        return "Devolución #" + id +
                " | Fecha: " + fechaDevolucion +
                " | Estado ejemplar: " + estadoEjemplar +
                " | Observaciones: " + observaciones;
    }

}
