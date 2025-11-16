package biblioteca.services;

import biblioteca.data.dao.DAOException;
import biblioteca.data.dao.HistorialDAO;
import biblioteca.data.dao.PrestamoDAO;
import biblioteca.entities.prestamos.Devolucion;
import biblioteca.entities.reportes.Historial;
import biblioteca.entities.prestamos.Prestamo;
import biblioteca.entities.inventario.Ejemplar;
import biblioteca.entities.usuarios.Socio;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para gestionar el historial de operaciones,
 * delegando toda la persistencia en HistorialDAO.
 */
public class ControlHistorial {

    private final HistorialDAO historialDAO;
    private final PrestamoDAO prestamoDAO;

    public ControlHistorial(HistorialDAO historialDAO, PrestamoDAO prestamoDAO) {
        this.historialDAO = historialDAO;
        this.prestamoDAO = prestamoDAO;
    }

    /**
     * Registra una operación en la base de datos.
     */
    public void registrarOperacion(String tipoOperacion,
                                   Socio socio,
                                   Ejemplar ejemplar,
                                   Prestamo prestamo,
                                   Devolucion devolucion,
                                   String detalles) throws DAOException {
        if (socio == null) return;

        Integer idLibro = (ejemplar != null && ejemplar.getLibro() != null)
                ? ejemplar.getLibro().getId() : null;

        Integer idPrestamo = (prestamo != null) ? prestamo.getId() : null;

        historialDAO.registrarOperacion(
                socio.getId(),
                tipoOperacion,
                detalles,
                idLibro,
                idPrestamo
        );
    }

    /**
     * Registrar un préstamo en el historial.
     */
    public void registrarPrestamo(Prestamo prestamo) throws DAOException {
        if (prestamo == null || prestamo.getSocio() == null) return;

        String detalles = "Préstamo registrado para el socio.";

        registrarOperacion(
                "PRESTAMO",
                prestamo.getSocio(),
                prestamo.getEjemplar(),
                prestamo,
                null,
                detalles
        );
    }

    /**
     * Registrar devolución.
     */
    public void registrarDevolucion(Socio socio, Devolucion devolucion) throws DAOException {
        if (socio == null || devolucion == null) return;

        Prestamo prestamo = devolucion.getPrestamo();
        Ejemplar ejemplar = (prestamo != null) ? prestamo.getEjemplar() : null;

        String detalles = "Devolución registrada para el socio.";

        registrarOperacion(
                "DEVOLUCION",
                socio,
                ejemplar,
                prestamo,
                devolucion,
                detalles
        );
    }

    /**
     * Consultar historial de un usuario por DNI.
     */
    public List<Prestamo> consultarHistorialUsuario(String dni) throws DAOException {
        Historial h = historialDAO.buscarPorDni(dni);
        if (h == null) return new ArrayList<>();

        return prestamoDAO.listarPorSocio(h.getSocio().getId());
    }

    /**
     * Consultar historial de un libro por su ISBN.
     */
    public List<Prestamo> consultarHistorialLibro(String isbn) throws DAOException {
        // PrestamoDAO ya debe permitir filtrar por ISBN
        return prestamoDAO.listarPorISBN(isbn);
    }

    public boolean existeSocio(int idSocio) throws DAOException {
        return !historialDAO.listarPorSocioId(idSocio).isEmpty();
    }

    public boolean existeLibro(int idLibro) throws DAOException {
        return !historialDAO.listarPorLibroId(idLibro).isEmpty();
    }

    public List<Historial> obtenerHistorialCompleto() throws DAOException {
        return historialDAO.listarTodos();
    }

    /**
     * Obtiene el historial completo del sistema con información estructurada.
     * Retorna los datos en lugar de imprimir.
     */
    public List<Historial> obtenerHistorialCompletoConDetalles() throws DAOException {
        return historialDAO.listarTodos();
    }

}
