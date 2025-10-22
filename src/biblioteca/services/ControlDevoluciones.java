package biblioteca.services;

import biblioteca.entities.inventario.Ejemplar;
import biblioteca.entities.prestamos.Devolucion;
import biblioteca.entities.prestamos.Prestamo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para gestionar las devoluciones de préstamos en la biblioteca.
 * Controla validaciones, cálculo de multas y actualización de estados de préstamos y ejemplares.
 */
public class ControlDevoluciones {

    /** Lista simulando la tabla de préstamos */
    private List<Prestamo> prestamos;

    /** Lista simulando la tabla de devoluciones */
    private List<Devolucion> devoluciones;

    /** Monto de multa por día de atraso */
    private static final double MULTA_POR_DIA = 50.0;

    /** Constructor que inicializa las listas internas */
    public ControlDevoluciones() {
        this.prestamos = new ArrayList<>();
        this.devoluciones = new ArrayList<>();
    }

    /**
     * Valida que un préstamo esté activo o vencido para poder realizar la devolución.
     * @param idPrestamo ID del préstamo
     * @return true si el préstamo está activo o vencido, false en caso contrario
     */
    public boolean validarPrestamo(int idPrestamo) {
        for (Prestamo p : prestamos) {
            if (p.getId() == idPrestamo) {
                p.actualizarEstado();
                return p.getEstado().equalsIgnoreCase("Activo") || p.getEstado().equalsIgnoreCase("Vencido");
            }
        }
        return false;
    }

    /**
     * Calcula la multa correspondiente a un préstamo según los días de atraso.
     * @param prestamo Préstamo a evaluar
     * @return Monto de multa
     */
    public double calcularMulta(Prestamo prestamo) {
        LocalDate hoy = LocalDate.now();
        if (hoy.isAfter(prestamo.getFechaVencimiento())) {
            long diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(prestamo.getFechaVencimiento(), hoy);
            return diasAtraso * MULTA_POR_DIA;
        }
        return 0.0;
    }

    /**
     * Libera un ejemplar asociado a un préstamo, marcándolo como disponible.
     * @param prestamo Préstamo cuyo ejemplar será liberado
     */
    public void liberarEjemplar(Prestamo prestamo) {
        Ejemplar ejemplar = prestamo.getEjemplar();
        if (ejemplar != null) {
            ejemplar.marcarComoDisponible();
        }
    }

    /**
     * Registra una devolución, calculando la multa y actualizando los estados correspondientes.
     * @param idPrestamo ID del préstamo a devolver
     * @param estadoEjemplar Estado final del ejemplar
     * @param observaciones Observaciones adicionales
     * @return Objeto Devolucion generado
     * @throws Exception si el préstamo no existe o no es válido para devolución
     */
    public Devolucion registrarDevolucion(int idPrestamo, String estadoEjemplar, String observaciones) throws Exception {
        Prestamo prestamo = null;
        for (Prestamo p : prestamos) {
            if (p.getId() == idPrestamo) {
                prestamo = p;
                break;
            }
        }

        if (prestamo == null) {
            throw new Exception("Préstamo no encontrado.");
        }

        if (!validarPrestamo(idPrestamo)) {
            throw new Exception("El préstamo no está activo o ya fue devuelto.");
        }

        double multa = calcularMulta(prestamo);

        // Se agrega la multa a las observaciones
        Devolucion devolucion = new Devolucion(
                devoluciones.size() + 1,
                LocalDate.now(),
                estadoEjemplar,
                observaciones + " | Multa: $" + multa,
                prestamo
        );

        devoluciones.add(devolucion);

        // Finaliza el préstamo y libera el ejemplar
        finalizarPrestamo(prestamo);

        return devolucion;
    }

    /**
     * Marca un préstamo como devuelto y libera el ejemplar asociado.
     * Método privado, solo llamado internamente.
     * @param prestamo Préstamo a finalizar
     */
    private void finalizarPrestamo(Prestamo prestamo) {
        prestamo.marcarComoDevuelto();
        liberarEjemplar(prestamo);
    }

    /**
     * Agrega un préstamo al registro interno.
     * @param prestamo Préstamo a agregar
     */
    public void agregarPrestamo(Prestamo prestamo) {
        if (prestamo != null) {
            prestamos.add(prestamo);
        }
    }

    // Getters
    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public List<Devolucion> getDevoluciones() {
        return devoluciones;
    }
}

