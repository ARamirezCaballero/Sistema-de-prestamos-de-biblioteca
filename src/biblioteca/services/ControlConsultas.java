package biblioteca.services;

import biblioteca.entities.prestamos.Prestamo;
import biblioteca.entities.reportes.Historial;

import java.util.ArrayList;
import java.util.List;


/**
 * Servicio para realizar consultas sobre préstamos y generar reportes históricos
 * de usuarios y libros en la biblioteca.
 */
public class ControlConsultas {

    /** Lista simulando la base de datos de préstamos */
    private List<Prestamo> prestamos;

    /** Lista simulando la base de datos de historiales */
    private List<Historial> historiales;

    /** Constructor que inicializa las listas internas */
    public ControlConsultas() {
        this.prestamos = new ArrayList<>();
        this.historiales = new ArrayList<>();
    }

    /**
     * Consulta los historiales asociados a un usuario específico.
     * @param idUsuario ID del usuario
     * @return Lista de historiales correspondientes al usuario
     */
    public List<Historial> consultarHistorialUsuario(int idUsuario) {
        List<Historial> resultado = new ArrayList<>();
        for (Historial h : historiales) {
            if (h.getId() == idUsuario || (h.getSocio() != null && h.getSocio().getId() == idUsuario)) {
                resultado.add(h);
            }
        }
        return resultado;
    }

    /**
     * Consulta los historiales asociados a un libro específico.
     * @param idLibro ID del libro
     * @return Lista de historiales que incluyen el libro
     */
    public List<Historial> consultarHistorialLibro(int idLibro) {
        List<Historial> resultado = new ArrayList<>();
        for (Historial h : historiales) {
            for (Prestamo p : h.obtenerPrestamos()) {
                if (p.getEjemplar() != null && p.getEjemplar().getLibro() != null
                        && p.getEjemplar().getLibro().getId() == idLibro) {
                    resultado.add(h);
                    break; // Evita agregar el mismo historial múltiples veces
                }
            }
        }
        return resultado;
    }

    /**
     * Retorna todos los préstamos activos o vencidos,
     * actualizando previamente su estado según la fecha.
     * @return Lista de préstamos activos o vencidos
     */
    public List<Prestamo> buscarPrestamos() {
        List<Prestamo> resultado = new ArrayList<>();
        for (Prestamo p : prestamos) {
            p.actualizarEstado(); // Asegura estado correcto según fecha
            if (p.getEstado().equalsIgnoreCase("Activo") || p.getEstado().equalsIgnoreCase("Vencido")) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    /**
     * Genera un reporte textual de todos los préstamos activos y vencidos.
     * @return String con el reporte
     */
    public String generarReporte() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("=== REPORTE DE PRÉSTAMOS ACTIVOS/VENCIDOS ===\n");
        List<Prestamo> listaPrestamos = buscarPrestamos();
        for (Prestamo p : listaPrestamos) {
            reporte.append(p.toString()).append("\n");
        }
        reporte.append("Total de préstamos: ").append(listaPrestamos.size()).append("\n");
        return reporte.toString();
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

    /**
     * Agrega un historial al registro interno.
     * @param historial Historial a agregar
     */
    public void agregarHistorial(Historial historial) {
        if (historial != null) {
            historiales.add(historial);
        }
    }

    // Getters
    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public List<Historial> getHistoriales() {
        return historiales;
    }
}
