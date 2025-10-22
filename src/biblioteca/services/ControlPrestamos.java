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
 * Se encarga de registrar nuevos préstamos, verificar condiciones de los socios y ejemplares,
 * y mantener la consistencia del inventario.
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
     * y la disponibilidad del ejemplar. Si se cumplen las reglas, se crea el préstamo
     * y se actualiza el estado del ejemplar.
     *
     * @param socio    Socio que realiza el préstamo.
     * @param ejemplar Ejemplar solicitado.
     */
    public void registrarPrestamo(Socio socio, Ejemplar ejemplar) {
        try {
            if (socio == null || ejemplar == null) {
                System.out.println("Error: datos de préstamo incompletos.");
                return;
            }

            if (!validarSocio(socio)) {
                System.out.println("El socio " + socio.getNombreCompleto() + " no está habilitado para realizar préstamos.");
                return;
            }

            if (!verificarDisponibilidad(ejemplar)) {
                System.out.println("El ejemplar " + ejemplar.getCodigo() + " no está disponible.");
                return;
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
                    ejemplar
            );

            prestamos.add(nuevoPrestamo);
            socio.agregarPrestamo(nuevoPrestamo);
            ejemplar.marcarComoPrestado();

            System.out.println("Préstamo registrado correctamente.");
            System.out.println("Socio: " + socio.getNombreCompleto());
            System.out.println("Ejemplar: " + ejemplar.getCodigo());
            System.out.println("Fecha de devolución: " + fechaVencimiento);

        } catch (Exception e) {
            System.out.println("Error al registrar el préstamo: " + e.getMessage());
        }
    }

    /**
     * Verifica si el ejemplar está disponible para ser prestado.
     */
    public boolean verificarDisponibilidad(Ejemplar ejemplar) {
        return ejemplar != null && ejemplar.verificarDisponibilidad();
    }

    /**
     * Calcula la fecha de vencimiento del préstamo según la política vigente.
     */
    public LocalDate calcularFechaVencimiento(LocalDate fechaPrestamo) {
        if (fechaPrestamo == null) {
            System.out.println("Advertencia: fecha de préstamo nula, se usará la fecha actual.");
            fechaPrestamo = LocalDate.now();
        }

        if (politicaActual == null) {
            System.out.println("Advertencia: no hay política configurada, se asignan 7 días por defecto.");
            return fechaPrestamo.plusDays(7);
        }

        return politicaActual.calcularFechaDevolucion(fechaPrestamo);
    }

    /**
     * Verifica si el socio cumple con las condiciones para poder realizar un préstamo.
     * Incluye verificaciones de estado, sanciones, atrasos y límite simultáneo.
     */
    public boolean validarSocio(Socio socio) {
        if (socio == null) return false;

        if (!socio.verificarHabilitacion()) {
            System.out.println("El socio no está habilitado.");
            return false;
        }

        int prestamosActivos = socio.obtenerPrestamosActivos().size();
        return politicaActual.verificarLimitePrestamos(prestamosActivos);
    }

    /**
     * Retorna la lista de préstamos activos en el sistema.
     */
    public List<Prestamo> obtenerPrestamosActivos() {
        List<Prestamo> activos = new ArrayList<>();
        for (Prestamo p : prestamos) {
            if (p != null && "Activo".equalsIgnoreCase(p.getEstado())) {
                activos.add(p);
            }
        }
        return activos;
    }

    /**
     * Muestra un listado general de todos los préstamos registrados en el sistema.
     */
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

    /** Getter de la lista completa de préstamos */
    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    /** Permite actualizar la política de préstamo vigente */
    public void setPoliticaActual(PoliticaPrestamo politicaActual) {
        this.politicaActual = politicaActual;
    }

    /** Retorna la política de préstamo actual */
    public PoliticaPrestamo getPoliticaActual() {
        return politicaActual;
    }
}

