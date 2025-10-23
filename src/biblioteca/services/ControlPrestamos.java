package biblioteca.services;

import biblioteca.entities.inventario.Ejemplar;
import biblioteca.entities.prestamos.Prestamo;
import biblioteca.entities.prestamos.PoliticaPrestamo;
import biblioteca.entities.usuarios.Socio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de servicio que gestiona el ciclo de vida de los préstamos en la biblioteca.
 */
public class ControlPrestamos {

    private final List<Prestamo> prestamos;
    private PoliticaPrestamo politicaActual;

    public ControlPrestamos(PoliticaPrestamo politicaInicial) {
        this.prestamos = new ArrayList<>();
        this.politicaActual = politicaInicial;
    }

    /**
     * Registra un nuevo préstamo, validando previamente las condiciones del socio
     * y la disponibilidad del ejemplar. Lanza excepciones si alguna regla falla.
     *
     * @param socio    Socio que realiza el préstamo.
     * @param ejemplar Ejemplar solicitado.
     * @throws IllegalArgumentException si los datos son inválidos o no se cumplen reglas de negocio.
     */
    public void registrarPrestamo(Socio socio, Ejemplar ejemplar) {
        if (socio == null || ejemplar == null) {
            throw new IllegalArgumentException("Datos de préstamo incompletos.");
        }

        if (!validarSocio(socio)) {
            throw new IllegalArgumentException(
                    "El socio " + socio.getNombreCompleto() + " no está habilitado para realizar préstamos."
            );
        }

        if (!verificarDisponibilidad(ejemplar)) {
            throw new IllegalArgumentException(
                    "El ejemplar " + ejemplar.getCodigo() + " no está disponible."
            );
        }

        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate fechaVencimiento = calcularFechaVencimiento(fechaPrestamo);

        Prestamo nuevoPrestamo = new Prestamo(
                prestamos.size() + 1,
                fechaPrestamo,
                fechaVencimiento,
                "Activo",
                politicaActual.obtenerDiasPrestamo(),
                socio,
                ejemplar,
                politicaActual
        );

        // Actualizar estados
        prestamos.add(nuevoPrestamo);
        socio.agregarPrestamo(nuevoPrestamo);
        ejemplar.marcarComoPrestado();
    }

    public boolean verificarDisponibilidad(Ejemplar ejemplar) {
        return ejemplar != null && ejemplar.verificarDisponibilidad();
    }

    public LocalDate calcularFechaVencimiento(LocalDate fechaPrestamo) {
        if (fechaPrestamo == null) fechaPrestamo = LocalDate.now();
        if (politicaActual == null) return fechaPrestamo.plusDays(7);
        return politicaActual.calcularFechaDevolucion(fechaPrestamo);
    }

    public boolean validarSocio(Socio socio) {
        if (socio == null) return false;
        if (!socio.verificarHabilitacion()) return false;

        int prestamosActivos = socio.obtenerPrestamosActivos().size();
        return politicaActual.verificarLimitePrestamos(prestamosActivos);
    }

    public List<Prestamo> obtenerPrestamosActivos() {
        List<Prestamo> activos = new ArrayList<>();
        for (Prestamo p : prestamos) {
            if (p != null && "Activo".equalsIgnoreCase(p.getEstado())) {
                activos.add(p);
            }
        }
        return activos;
    }

    public void mostrarPrestamos() {
        if (prestamos.isEmpty()) {
            System.out.println("No hay préstamos registrados.");
            return;
        }
        System.out.println("=== LISTADO DE PRÉSTAMOS REGISTRADOS ===");
        for (Prestamo p : prestamos) {
            System.out.println(p);
        }
    }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setPoliticaActual(PoliticaPrestamo politicaActual) {
        this.politicaActual = politicaActual;
    }

    public PoliticaPrestamo getPoliticaActual() {
        return politicaActual;
    }
}


