package biblioteca.services;

import biblioteca.data.dao.DAOException;
import biblioteca.data.dao.DevolucionDAO;
import biblioteca.data.dao.PrestamoDAO;
import biblioteca.data.dao.EjemplarDAO;
import biblioteca.entities.inventario.Ejemplar;
import biblioteca.entities.prestamos.Devolucion;
import biblioteca.entities.prestamos.Prestamo;
import biblioteca.entities.usuarios.Socio;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ControlDevoluciones {

    private final ControlHistorial controlHistorial;
    private final PrestamoDAO prestamoDAO;
    private final DevolucionDAO devolucionDAO;
    private final EjemplarDAO ejemplarDAO;

    private static final double MULTA_POR_DIA = 50.0;

    public ControlDevoluciones(ControlHistorial controlHistorial,
                               PrestamoDAO prestamoDAO,
                               DevolucionDAO devolucionDAO,
                               EjemplarDAO ejemplarDAO) {
        this.controlHistorial = controlHistorial;
        this.prestamoDAO = prestamoDAO;
        this.devolucionDAO = devolucionDAO;
        this.ejemplarDAO = ejemplarDAO;
    }

    // Para UI
    public List<Prestamo> getPrestamos() throws DAOException {
        return prestamoDAO.listarTodos();
    }

    // Reglas básicas: préstamo debe existir y estar en estado válido para devolver
    public boolean validarPrestamo(int idPrestamo) throws DAOException {
        Prestamo p = prestamoDAO.buscarPorId(idPrestamo);
        if (p == null) return false;

        p.actualizarEstado(); // recalcula "Activo" / "Vencido"

        return p.getEstado().equalsIgnoreCase("Activo")
                || p.getEstado().equalsIgnoreCase("Vencido");
    }

    /**
     * Busca y valida un préstamo para devolución.
     * Encapsula la lógica de búsqueda y validación para cumplir con MVC.
     *
     * @param idPrestamo ID del préstamo a buscar y validar
     * @return Prestamo válido para devolución
     * @throws DAOException Si el préstamo no existe o no es válido para devolución
     */
    public Prestamo buscarYValidarPrestamoParaDevolucion(int idPrestamo) throws DAOException {
        Prestamo prestamo = prestamoDAO.buscarPorId(idPrestamo);

        if (prestamo == null) {
            throw new DAOException("Préstamo no encontrado con ID: " + idPrestamo);
        }

        if (!validarPrestamo(idPrestamo)) {
            throw new DAOException("El préstamo no puede devolverse (ya devuelto o inválido). ID: " + idPrestamo);
        }

        return prestamo;
    }

    // Cálculo independiente
    public double calcularMulta(Prestamo prestamo) {
        LocalDate hoy = LocalDate.now();
        if (hoy.isAfter(prestamo.getFechaVencimiento())) {
            long diasAtraso = ChronoUnit.DAYS.between(prestamo.getFechaVencimiento(), hoy);
            return diasAtraso * MULTA_POR_DIA;
        }
        return 0.0;
    }

    // Lógica de actualización del ejemplar
    private void liberarEjemplar(Prestamo prestamo, String nuevoEstado) {
        Ejemplar ej = prestamo.getEjemplar();
        if (ej != null) {
            ej.setEstado(nuevoEstado != null && !nuevoEstado.isBlank() ? nuevoEstado : "Disponible");
        }
    }

    /**
     * Caso de uso completo: registrar la devolución, actualizar estados y persistir.
     */
    public Devolucion registrarDevolucion(int idPrestamo, String estadoEjemplar, String observaciones) throws Exception {

        Prestamo prestamo = prestamoDAO.buscarPorId(idPrestamo);
        if (prestamo == null)
            throw new Exception("Préstamo no encontrado.");

        if (!validarPrestamo(idPrestamo))
            throw new Exception("El préstamo no puede devolverse (ya devuelto o inválido).");

        double multa = calcularMulta(prestamo);

        estadoEjemplar = (estadoEjemplar == null || estadoEjemplar.isBlank())
                ? "Disponible"
                : estadoEjemplar;

        // Crear devolución (sin efectos colaterales)
        Devolucion devolucion = new Devolucion(
                0,
                LocalDate.now(),
                estadoEjemplar,
                observaciones != null ? observaciones : "",
                prestamo,
                multa
        );

        // --- Mutaciones de dominio ---
        prestamo.marcarComoDevuelto();
        liberarEjemplar(prestamo, estadoEjemplar);

        // --- Persistencia ---
        devolucionDAO.insertar(devolucion);
        prestamoDAO.actualizar(prestamo);
        ejemplarDAO.actualizar(prestamo.getEjemplar());

        // --- Historial del socio ---
        Socio socio = prestamo.getSocio();
        if (controlHistorial != null && socio != null) {
            controlHistorial.registrarDevolucion(socio, devolucion);
        }

        return devolucion;
    }
}