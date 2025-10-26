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
    private String estado;
    private int diasPrestamo;
    private Socio socio;
    private Ejemplar ejemplar;
    private PoliticaPrestamo politica;

    // ======== Constructor ========
    public Prestamo(int id, LocalDate fechaPrestamo, LocalDate fechaVencimiento,
                    Socio socio, Ejemplar ejemplar, PoliticaPrestamo politica) {

        if (fechaPrestamo == null || fechaVencimiento == null)
            throw new IllegalArgumentException("Fechas no pueden ser nulas.");
        if (fechaVencimiento.isBefore(fechaPrestamo))
            throw new IllegalArgumentException("Vencimiento no puede ser anterior al préstamo.");
        if (socio == null || ejemplar == null)
            throw new IllegalArgumentException("Socio o ejemplar nulos.");

        this.id = id;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaVencimiento = fechaVencimiento;
        this.socio = socio;
        this.ejemplar = ejemplar;
        this.diasPrestamo = (int) ChronoUnit.DAYS.between(fechaPrestamo, fechaVencimiento);
        this.estado = "Activo";
        this.politica = politica;
    }

    public Prestamo(Socio socio, Ejemplar ejemplar, LocalDate now) {
        this.socio = socio;
        this.ejemplar = ejemplar;
        this.fechaPrestamo = now;
    }

    // ======== Métodos según flujo ========

    // Paso 18
    public static Prestamo crearPrestamo(int id, Socio socio, Ejemplar ejemplar,
                                         PoliticaPrestamo politica, LocalDate fechaPrestamo) {
        LocalDate vencimiento = fechaPrestamo.plusDays(politica.getDiasPrestamo());
        Prestamo nuevo = new Prestamo(id, fechaPrestamo, vencimiento, socio, ejemplar, politica);
        ejemplar.marcarComoPrestado();
        return nuevo;
    }

    // Paso 19 (simula guardado)
    public void guardar() {
        System.out.println("[BD] Préstamo guardado: " + this);
    }

    // Paso 20
    public boolean confirmarCreacion() {
        System.out.println("[Sistema] Préstamo #" + id + " creado correctamente.");
        return true;
    }

    // Paso 24
    public void confirmarEstadoActualizado() {
        System.out.println("[Sistema] Estado del ejemplar actualizado correctamente a 'Prestado'.");
    }

    // ======== Otros ========
    public boolean estaVencido() {
        return LocalDate.now().isAfter(fechaVencimiento) && !"Devuelto".equalsIgnoreCase(estado);
    }

    public void marcarComoDevuelto() {
        this.estado = "Devuelto";
        ejemplar.marcarComoDisponible();
    }

    public void actualizarEstado() {
        if ("Devuelto".equalsIgnoreCase(estado)) {
            return; // No cambia si ya fue devuelto
        }

        if (estaVencido()) {
            estado = "Vencido";
        } else {
            estado = "Activo";
        }
    }


    // ======== Getters ========
    public int getId() { return id; }
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public Socio getSocio() { return socio; }
    public Ejemplar getEjemplar() { return ejemplar; }
    public String getEstado() { return estado; }

    @Override
    public String toString() {
        return "Prestamo{" +
                "id=" + id +
                ", socio=" + socio.getNombreCompleto() +
                ", ejemplar=" + ejemplar.getCodigo() +
                ", desde=" + fechaPrestamo +
                ", hasta=" + fechaVencimiento +
                ", estado='" + estado + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Prestamo)) return false;
        Prestamo p = (Prestamo) obj;
        return Objects.equals(ejemplar.getCodigo(), p.ejemplar.getCodigo()) &&
                Objects.equals(socio.getDni(), p.socio.getDni());
    }

    @Override
    public int hashCode() {
        return Objects.hash(ejemplar.getCodigo(), socio.getDni());
    }
}


