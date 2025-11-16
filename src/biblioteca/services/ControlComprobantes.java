package biblioteca.services;

import biblioteca.data.dao.ComprobanteDAO;
import biblioteca.data.dao.DAOException;
import biblioteca.entities.prestamos.Prestamo;
import biblioteca.entities.reportes.Comprobante;

public class ControlComprobantes {

    private final ComprobanteDAO comprobanteDAO;

    public ControlComprobantes(ComprobanteDAO comprobanteDAO) {
        this.comprobanteDAO = comprobanteDAO;
    }

    public Comprobante generarComprobante(String tipo, Prestamo prestamo) {
        if (prestamo == null) throw new IllegalArgumentException("Préstamo no válido.");
        Comprobante c = new Comprobante(0, tipo, prestamo);
        c.generar();

        try {
            comprobanteDAO.insertar(c);
        } catch (DAOException e) {
            throw new RuntimeException("Error al guardar el comprobante.", e);
        }

        return c;
    }

    /**
     * Genera el contenido formateado del comprobante para impresión.
     * Retorna String para que la vista lo presente.
     */
    public String obtenerContenidoComprobante(Comprobante comprobante) {
        if (comprobante == null) throw new IllegalArgumentException("Comprobante nulo.");
        String contenido = comprobante.getContenido();
        return (contenido != null && !contenido.isBlank()) ? contenido : comprobante.toString();
    }

    public void imprimirComprobante(Comprobante comprobante) {
        // Método mantenido por compatibilidad, pero la impresión debe ser delegada a la vista
        obtenerContenidoComprobante(comprobante);
    }

    public void emitirComprobante(Comprobante comprobante) {
        if (comprobante == null) throw new IllegalArgumentException("Comprobante nulo.");
        // Obtenemos el contenido formateado
        String contenido = obtenerContenidoComprobante(comprobante);
        // Lo mostramos por consola (o lo podés enviar a la UI)
        System.out.println(contenido);
    }

    /**
     * Obtiene el contenido del comprobante formateado para envío por email.
     * Retorna String para que la vista lo procese.
     */
    public String obtenerContenidoEmail(Comprobante comprobante) {
        if (comprobante == null) throw new IllegalArgumentException("Comprobante nulo.");
        return comprobante.prepararEmail();
    }

    public Comprobante buscarComprobantePorId(int id) {
        try {
            return comprobanteDAO.buscarPorId(id);
        } catch (DAOException e) {
            throw new RuntimeException("Error al buscar comprobante por ID.", e);
        }
    }

    public Comprobante buscarComprobantePorPrestamo(int idPrestamo) {
        try {
            return comprobanteDAO.buscarPorPrestamoId(idPrestamo);
        } catch (DAOException e) {
            throw new RuntimeException("Error al buscar comprobante por prestamo.", e);
        }
    }

    public boolean eliminarComprobante(int id) {
        try {
            comprobanteDAO.eliminar(id);
            return true;
        } catch (DAOException e) {
            return false;
        }
    }

    public java.util.List<Comprobante> getComprobantes() {
        try {
            return comprobanteDAO.listarTodos();
        } catch (DAOException e) {
            throw new RuntimeException("Error al listar comprobantes.", e);
        }
    }
}