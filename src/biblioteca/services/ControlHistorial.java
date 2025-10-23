package biblioteca.services;

import biblioteca.entities.prestamos.Devolucion;
import biblioteca.entities.reportes.Historial;
import biblioteca.entities.prestamos.Prestamo;
import biblioteca.entities.inventario.Ejemplar;
import biblioteca.entities.usuarios.Socio;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para gestionar el historial de operaciones de socios y libros.
 * Registra préstamos y devoluciones, y permite consultar historiales por usuario o libro.
 */
public class ControlHistorial {

    /** Lista que almacena todos los historiales del sistema */
    private List<Historial> registros;

    /** Constructor que inicializa la lista de historiales */
    public ControlHistorial() {
        this.registros = new ArrayList<>();
    }

    /**
     * Registra una operación (préstamo o devolución) en el historial.
     * @param tipoOperacion Tipo de operación: "PRESTAMO" o "DEVOLUCION"
     * @param socio Socio que realiza la operación
     * @param ejemplar Ejemplar asociado a la operación
     * @param prestamo Préstamo vinculado a la operación
     * @param detalles Detalles adicionales sobre la operación
     */
    public void registrarOperacion(String tipoOperacion, Socio socio, Ejemplar ejemplar, Prestamo prestamo, Devolucion devolucion, String detalles) {
        if (socio == null || ejemplar == null) {
            System.out.println("Error: Datos insuficientes para registrar la operación en el historial.");
            return;
        }

        Historial historialSocio = registros.stream()
                .filter(h -> h.getSocio().equals(socio))
                .findFirst()
                .orElseGet(() -> {
                    Historial nuevo = new Historial(registros.size() + 1, socio);
                    registros.add(nuevo);
                    return nuevo;
                });

        if ("PRESTAMO".equalsIgnoreCase(tipoOperacion)) {
            historialSocio.agregarPrestamo(prestamo);
        } else if ("DEVOLUCION".equalsIgnoreCase(tipoOperacion) && devolucion != null) {
            historialSocio.agregarDevolucion(devolucion);
        }

        System.out.println("Historial actualizado: [" + tipoOperacion + "] " + detalles);
    }

    /**
     * Actualiza el historial de un socio con un préstamo o devolución.
     * @param socio Socio cuyo historial se actualizará
     * @param tipoOperacion Tipo de operación: "PRESTAMO" o "DEVOLUCION"
     * @param prestamo Préstamo asociado a la operación
     */
    public void actualizarHistorialUsuario(Socio socio, String tipoOperacion, Prestamo prestamo, Devolucion devolucion) {
        String detalles = tipoOperacion.equalsIgnoreCase("PRESTAMO") ?
                "Préstamo registrado para el socio." :
                "Devolución registrada para el socio.";
        registrarOperacion(tipoOperacion, socio, prestamo.getEjemplar(), prestamo, devolucion, detalles);
    }

    /**
     * Actualiza el historial de un libro con una operación.
     * @param ejemplar Ejemplar asociado al libro
     * @param tipoOperacion Tipo de operación
     * @param prestamo Préstamo asociado a la operación
     */
    public void actualizarHistorialLibro(Ejemplar ejemplar, String tipoOperacion, Prestamo prestamo, Devolucion devolucion) {
        String detalles = "Operación registrada para el libro " + ejemplar.getLibro().getTitulo();
        registrarOperacion(tipoOperacion, prestamo.getSocio(), ejemplar, prestamo, devolucion, detalles);
    }

    /**
     * Consulta el historial de un usuario específico.
     * @param idUsuario ID del socio
     * @return Lista de historiales asociados al usuario
     */
    public List<Historial> consultarHistorialUsuario(int idUsuario) {
        List<Historial> resultado = new ArrayList<>();
        for (Historial h : registros) {
            if (h.getSocio().getId() == idUsuario) {
                resultado.add(h);
            }
        }

        if (resultado.isEmpty()) {
            System.out.println("No se encontraron registros de historial para el usuario ID " + idUsuario);
        }
        return resultado;
    }

    /**
     * Consulta el historial de un libro específico.
     * @param idLibro ID del libro
     * @return Lista de historiales donde aparece el libro
     */
    public List<Historial> consultarHistorialLibro(int idLibro) {
        List<Historial> resultado = new ArrayList<>();
        for (Historial h : registros) {
            for (Prestamo p : h.obtenerPrestamos()) {
                if (p.getEjemplar() != null && p.getEjemplar().getLibro().getId() == idLibro) {
                    resultado.add(h);
                    break;
                }
            }
        }

        if (resultado.isEmpty()) {
            System.out.println("No se encontraron registros de historial para el libro ID " + idLibro);
        }
        return resultado;
    }

    /**
     * Muestra por consola todos los historiales registrados en el sistema.
     */
    public void mostrarHistorialCompleto() {
        if (registros.isEmpty()) {
            System.out.println("No hay operaciones registradas en el historial.");
            return;
        }

        System.out.println("=== HISTORIAL GENERAL DEL SISTEMA ===");
        for (Historial h : registros) {
            System.out.println(h.generarResumen());
        }
    }

    public boolean existeSocio(int idSocio) {
        return registros.stream().anyMatch(h -> h.getSocio().getId() == idSocio);
    }

    public boolean existeLibro(int idLibro) {
        return registros.stream().anyMatch(h ->
                h.obtenerPrestamos().stream()
                        .anyMatch(p -> p.getEjemplar() != null && p.getEjemplar().getLibro().getId() == idLibro)
        );
    }


    /** Getter de todos los historiales registrados */
    public List<Historial> getRegistros() {
        return registros;
    }
}

