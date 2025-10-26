package biblioteca.services;

import biblioteca.data.BaseDatosSimulada;
import biblioteca.entities.prestamos.PoliticaPrestamo;
import biblioteca.entities.usuarios.Socio;

import java.time.LocalDate;

/**
 * Servicio que encapsula las reglas de negocio relacionadas con las políticas de préstamo.
 * Provee utilidades para calcular fechas de devolución y verificar si un socio puede tomar
 * prestados más ítems según la política vigente.
 */
public class ControlPoliticas {

    private PoliticaPrestamo politicaActual;
    private static final int DIAS_ESTANDAR_PRESTAMO = 15;
    private static final int LIMITE_ESTANDAR_PRESTAMOS = 3;
    private static final double MULTA_POR_DIA = 50.0;

    public ControlPoliticas() {
        this.politicaActual = new PoliticaPrestamo(
                1,
                "General",
                DIAS_ESTANDAR_PRESTAMO,
                LIMITE_ESTANDAR_PRESTAMOS,
                MULTA_POR_DIA
        );
    }

    /** Retorna la política de préstamo vigente en el sistema */
    public PoliticaPrestamo obtenerPoliticaPrestamo() {
        return politicaActual;
    }

    /** Devuelve la cantidad de días de préstamo definida por la política actual */
    public int obtenerDiasPrestamo() {
        return politicaActual.obtenerDiasPrestamo();
    }

    /** Calcula la fecha estimada de devolución en base a la política vigente */
    public LocalDate calcularFechaDevolucion(LocalDate fechaPrestamo) {
        if (fechaPrestamo == null) {
            System.out.println("Advertencia: fecha de préstamo nula. Se usará la fecha actual.");
            fechaPrestamo = LocalDate.now();
        }
        return fechaPrestamo.plusDays(obtenerDiasPrestamo());
    }

    /**
     * Verifica si el socio cumple las condiciones para realizar un nuevo préstamo:
     *  - Debe estar activo
     *  - No debe tener sanciones
     *  - No debe tener atrasos
     *  - No debe superar el límite simultáneo de préstamos activos
     *
     * @param socio Socio a verificar
     * @return true si puede tomar otro préstamo; false en caso contrario
     */
    public boolean verificarLimitePrestamos(Socio socio) {
        if (socio == null) {
            System.out.println("Error: socio no válido.");
            return false;
        }

        if (!"ACTIVO".equalsIgnoreCase(socio.getEstado())) {
            System.out.println("El socio " + socio.getNombreCompleto() + " no está activo para realizar préstamos.");
            return false;
        }

        if (socio.isTieneSanciones()) {
            System.out.println("El socio " + socio.getNombreCompleto() + " posee sanciones vigentes.");
            return false;
        }

        if (socio.isTieneAtrasos()) {
            System.out.println("El socio " + socio.getNombreCompleto() + " tiene préstamos vencidos o atrasos.");
            return false;
        }

        int prestamosActivos = socio.obtenerPrestamosActivos().size();
        boolean dentroDelLimite = politicaActual.verificarLimitePrestamos(prestamosActivos);

        if (!dentroDelLimite) {
            System.out.println("El socio " + socio.getNombreCompleto() +
                    " alcanzó el límite máximo de préstamos simultáneos (" +
                    politicaActual.getMaxPrestamosSimultaneos() + ").");
        }

        return dentroDelLimite;
    }

    // Paso 14–16
    public LocalDate calcularFechaVencimiento(Socio socio) {
        int diasPrestamo = BaseDatosSimulada.obtenerPoliticaDiasPrestamo(socio.getDni());
        return LocalDate.now().plusDays(diasPrestamo);
    }

    /** Permite reemplazar o actualizar la política vigente (por ejemplo, cambio institucional) */
    public void establecerNuevaPolitica(PoliticaPrestamo nuevaPolitica) {
        if (nuevaPolitica == null) {
            System.out.println("Error: la política no puede ser nula.");
            return;
        }
        this.politicaActual = nuevaPolitica;
        System.out.println("Política de préstamo actualizada a: " + nuevaPolitica.getCategoria());
    }
}


