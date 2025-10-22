package biblioteca.services;

import biblioteca.entities.prestamos.Prestamo;
import biblioteca.entities.reportes.Comprobante;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Servicio que gestiona la creación, emisión, impresión y envío de comprobantes.
 * Se encarga de almacenar los comprobantes generados y buscar por ID.
 */
public class ControlComprobantes {

    private List<Comprobante> comprobantes;
    private int contadorIds;

    public ControlComprobantes() {
        this.comprobantes = new ArrayList<>();
        this.contadorIds = 1; // contador interno para IDs únicos
    }

    /**
     * Genera un comprobante asociado a un préstamo.
     *
     * @param tipo     Tipo de comprobante (ej. "Préstamo", "Devolución")
     * @param prestamo Préstamo asociado
     * @return Comprobante generado
     * @throws IllegalArgumentException si el préstamo es null
     */
    public Comprobante generarComprobante(String tipo, Prestamo prestamo) {
        if (prestamo == null) {
            throw new IllegalArgumentException("No se puede generar un comprobante sin un préstamo válido.");
        }

        Comprobante comprobante = new Comprobante(contadorIds++, tipo, prestamo);
        comprobante.generar(); // genera contenido
        comprobantes.add(comprobante);

        return comprobante;
    }

    /**
     * Emite un comprobante. Actualmente simula la impresión en consola.
     *
     * @param comprobante Comprobante a emitir
     * @throws IllegalArgumentException si el comprobante es null
     */
    public void emitirComprobante(Comprobante comprobante) {
        if (comprobante == null) {
            throw new IllegalArgumentException("No se puede emitir un comprobante nulo.");
        }

        System.out.println("=== EMITIENDO COMPROBANTE ===");
        comprobante.imprimir();
    }

    /**
     * Imprime un comprobante.
     *
     * @param comprobante Comprobante a imprimir
     * @throws IllegalArgumentException si el comprobante es null
     */
    public void imprimirComprobante(Comprobante comprobante) {
        if (comprobante == null) {
            throw new IllegalArgumentException("No se puede imprimir un comprobante nulo.");
        }

        System.out.println("=== IMPRESIÓN DE COMPROBANTE ===");
        comprobante.imprimir();
    }

    /**
     * Simula el envío de un comprobante por email.
     *
     * @param comprobante Comprobante a enviar
     * @throws IllegalArgumentException si el comprobante es null
     */
    public void enviarComprobanteEmail(Comprobante comprobante) {
        if (comprobante == null) {
            throw new IllegalArgumentException("No se puede enviar un comprobante nulo.");
        }

        System.out.println("=== ENVÍO DE COMPROBANTE POR EMAIL ===");
        System.out.println(comprobante.prepararEmail());
    }

    /**
     * Busca un comprobante por su ID.
     *
     * @param id ID del comprobante
     * @return Comprobante encontrado o null si no existe
     */
    public Comprobante buscarComprobantePorId(int id) {
        Optional<Comprobante> resultado = comprobantes.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
        return resultado.orElse(null);
    }

    /**
     * Devuelve todos los comprobantes generados.
     *
     * @return Lista de comprobantes
     */
    public List<Comprobante> getComprobantes() {
        return new ArrayList<>(comprobantes); // devuelve copia para no exponer la lista interna
    }

    /**
     * Elimina un comprobante por ID.
     *
     * @param id ID del comprobante a eliminar
     * @return true si se eliminó correctamente, false si no se encontró
     */
    public boolean eliminarComprobante(int id) {
        return comprobantes.removeIf(c -> c.getId() == id);
    }
}

