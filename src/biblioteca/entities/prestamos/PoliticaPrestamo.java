package biblioteca.entities.prestamos;

import java.time.LocalDate;

public class PoliticaPrestamo {

    private int idPolitica;
    private String categoria;
    private int diasPrestamo;
    private int maxPrestamosSimultaneos;
    private double multaPorDia;

    public PoliticaPrestamo(int idPolitica, String categoria, int diasPrestamo, int maxPrestamosSimultaneos, double multaPorDia) {
        this.idPolitica = idPolitica;
        this.categoria = categoria;
        this.diasPrestamo = diasPrestamo;
        this.maxPrestamosSimultaneos = maxPrestamosSimultaneos;
        this.multaPorDia = multaPorDia;
    }

    public int obtenerDiasPrestamo() {
        return diasPrestamo;
    }

    public LocalDate calcularFechaDevolucion(LocalDate fechaPrestamo) {
        return fechaPrestamo.plusDays(diasPrestamo);
    }

    public boolean verificarLimitePrestamos(int prestamosActivos) {
        return prestamosActivos < maxPrestamosSimultaneos;
    }

    public int getIdPolitica() { return idPolitica; }
    public String getCategoria() { return categoria; }
    public int getDiasPrestamo() { return diasPrestamo; }
    public int getMaxPrestamosSimultaneos() { return maxPrestamosSimultaneos; }
    public double getMultaPorDia() { return multaPorDia; }

    public void setId(int idPolitica) { this.idPolitica = idPolitica; }
    public void setDiasPrestamo(int diasPrestamo) { this.diasPrestamo = diasPrestamo; }
    public void setMaxPrestamosSimultaneos(int maxPrestamosSimultaneos) { this.maxPrestamosSimultaneos = maxPrestamosSimultaneos; }
    public void setMultaPorDia(double multaPorDia) { this.multaPorDia = multaPorDia; }

    @Override
    public String toString() {
        return "Política [" + categoria + "] - Días préstamo: " + diasPrestamo +
                " | Máx. simultáneos: " + maxPrestamosSimultaneos +
                " | Multa/día: $" + multaPorDia;
    }

}
