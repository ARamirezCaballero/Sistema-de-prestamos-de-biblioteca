package biblioteca.services;

import biblioteca.data.BaseDatosSimulada;
import biblioteca.entities.inventario.Ejemplar;
import biblioteca.entities.prestamos.Devolucion;
import biblioteca.entities.prestamos.Prestamo;
import biblioteca.entities.usuarios.Socio;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio que gestiona devoluciones.
 * Ahora recibe ControlHistorial para sincronizar devoluciones en el historial.
 */
public class ControlDevoluciones {

    private final ControlHistorial controlHistorial;
    private static final double MULTA_POR_DIA = 50.0;

    /**
     * Constructor inyectando dependencia a ControlHistorial.
     * Si querés crear una instancia sin historial (tests), podés pasar null.
     */
    public ControlDevoluciones(ControlHistorial controlHistorial) {
        this.controlHistorial = controlHistorial;
    }

    /** Valida si el préstamo existe y puede devolverse */
    public boolean validarPrestamo(int idPrestamo) {
        for (Prestamo p : BaseDatosSimulada.getPrestamos()) {
            if (p.getId() == idPrestamo) {
                p.actualizarEstado();
                return p.getEstado().equalsIgnoreCase("Activo")
                        || p.getEstado().equalsIgnoreCase("Vencido");
            }
        }
        return false;
    }

    /** Calcula multa si la devolución se realiza después del vencimiento */
    public double calcularMulta(Prestamo prestamo) {
        LocalDate hoy = LocalDate.now();
        if (hoy.isAfter(prestamo.getFechaVencimiento())) {
            long diasAtraso = java.time.temporal.ChronoUnit.DAYS
                    .between(prestamo.getFechaVencimiento(), hoy);
            return diasAtraso * MULTA_POR_DIA;
        }
        return 0.0;
    }

    /** Libera el ejemplar asociado, marcándolo como disponible */
    public void liberarEjemplar(Prestamo prestamo) {
        Ejemplar ejemplar = prestamo.getEjemplar();
        if (ejemplar != null) ejemplar.marcarComoDisponible();
    }

    /** Registra la devolución en la base de datos simulada y en el historial */
    public Devolucion registrarDevolucion(int idPrestamo, String estadoEjemplar, String observaciones) throws Exception {
        Prestamo prestamo = BaseDatosSimulada.getPrestamos().stream()
                .filter(p -> p.getId() == idPrestamo)
                .findFirst()
                .orElseThrow(() -> new Exception("Préstamo no encontrado."));

        if (!validarPrestamo(idPrestamo))
            throw new Exception("El préstamo no está activo o ya fue devuelto.");

        double multa = calcularMulta(prestamo);

        Devolucion devolucion = new Devolucion(
                BaseDatosSimulada.getDevoluciones().size() + 1,
                LocalDate.now(),
                estadoEjemplar,
                observaciones,
                prestamo,
                multa
        );

        // Actualiza estado del préstamo y del ejemplar (Devolucion ctor ya hace parte de esto,
        // pero lo dejamos por claridad si fuera necesario)
        prestamo.marcarComoDevuelto();
        liberarEjemplar(prestamo);

        // Guarda en la BD simulada
        BaseDatosSimulada.agregarDevolucion(devolucion);

        // Registra la devolución en el historial (si tenemos el controlHistorial disponible)
        Socio socio = prestamo.getSocio();
        if (controlHistorial != null && socio != null) {
            controlHistorial.registrarDevolucion(socio, devolucion);
        }

        return devolucion;
    }

    /** Accesos a la base simulada */
    public List<Prestamo> getPrestamos() { return BaseDatosSimulada.getPrestamos(); }
    public List<Devolucion> getDevoluciones() { return BaseDatosSimulada.getDevoluciones(); }
}



