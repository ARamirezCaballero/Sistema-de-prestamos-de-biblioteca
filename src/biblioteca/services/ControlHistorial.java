package biblioteca.services;

import biblioteca.data.BaseDatosSimulada;
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
    private final List<Historial> registros;

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
     * @param devolucion Devolución vinculada (si aplica)
     * @param detalles Detalles adicionales sobre la operación
     */
    public void registrarOperacion(String tipoOperacion, Socio socio, Ejemplar ejemplar,
                                   Prestamo prestamo, Devolucion devolucion, String detalles) {

        if (socio == null || ejemplar == null) {
            System.out.println("Error: Datos insuficientes para registrar la operación en el historial.");
            return;
        }

        // Busca historial del socio o crea uno nuevo
        Historial historialSocio = registros.stream()
                .filter(h -> h.getSocio().getDni().equals(socio.getDni()))
                .findFirst()
                .orElseGet(() -> {
                    Historial nuevo = new Historial(registros.size() + 1, socio);
                    registros.add(nuevo);
                    return nuevo;
                });

        // Registra la operación
        if ("PRESTAMO".equalsIgnoreCase(tipoOperacion)) {
            historialSocio.agregarPrestamo(prestamo);
        } else if ("DEVOLUCION".equalsIgnoreCase(tipoOperacion) && devolucion != null) {
            historialSocio.agregarDevolucion(devolucion);
        }

        System.out.println("Historial actualizado: [" + tipoOperacion + "] " + detalles);
    }

    public void registrarPrestamo(Prestamo prestamo) {
        if (prestamo == null || prestamo.getSocio() == null || prestamo.getEjemplar() == null) {
            System.out.println("Error: datos insuficientes para registrar préstamo en el historial.");
            return;
        }

        // Busca historial del socio o crea uno nuevo
        Historial historialSocio = registros.stream()
                .filter(h -> h.getSocio().getDni().equals(prestamo.getSocio().getDni()))
                .findFirst()
                .orElseGet(() -> {
                    Historial nuevo = new Historial(registros.size() + 1, prestamo.getSocio());
                    registros.add(nuevo);
                    return nuevo;
                });

        historialSocio.agregarPrestamo(prestamo);
        System.out.println("Historial actualizado: [PRÉSTAMO] Socio: " + prestamo.getSocio().getNombreCompleto()
                + " | Ejemplar: " + prestamo.getEjemplar().getCodigo());
    }

    /**
     * Actualiza el historial de un socio con un préstamo o devolución.
     */
    public void actualizarHistorialUsuario(Socio socio, String tipoOperacion, Prestamo prestamo, Devolucion devolucion) {
        String detalles = tipoOperacion.equalsIgnoreCase("PRESTAMO") ?
                "Préstamo registrado para el socio." :
                "Devolución registrada para el socio.";
        registrarOperacion(tipoOperacion, socio, prestamo.getEjemplar(), prestamo, devolucion, detalles);
    }

    /**
     * Actualiza el historial de un libro con una operación.
     */
    public void actualizarHistorialLibro(Ejemplar ejemplar, String tipoOperacion, Prestamo prestamo, Devolucion devolucion) {
        String detalles = "Operación registrada para el libro " + ejemplar.getLibro().getTitulo();
        registrarOperacion(tipoOperacion, prestamo.getSocio(), ejemplar, prestamo, devolucion, detalles);
    }

    public void registrarDevolucion(Socio socio, Devolucion devolucion) {
        if (socio == null || devolucion == null) return;

        // Buscar historial existente del socio
        Historial historial = registros.stream()
                .filter(h -> h.getSocio().getId() == socio.getId())
                .findFirst()
                .orElse(null);

        // Si no tiene historial, se crea uno nuevo
        if (historial == null) {
            int nuevoId = registros.size() + 1;
            historial = new Historial(nuevoId, socio);
            registros.add(historial);
        }

        // Se agrega la devolución al historial del socio
        historial.agregarDevolucion(devolucion);
    }

    /**
     * Consulta el historial de un socio específico por su DNI.
     * @param dniSocio DNI del socio
     * @return Lista de historiales asociados al socio
     */
    public List<Prestamo> consultarHistorialUsuario(String dniSocio) {
        Historial historial = registros.stream()
                .filter(h -> h.getSocio().getDni().equals(dniSocio))
                .findFirst()
                .orElse(null);

        if (historial == null || historial.obtenerPrestamos().isEmpty()) {
            System.out.println("No se encontraron registros de historial para el socio con DNI " + dniSocio);
            return new ArrayList<>();
        }

        return historial.obtenerPrestamos();
    }


    /**
     * Consulta el historial de un libro específico por ID.
     */
    public List<Prestamo> consultarHistorialLibro(String isbn) {
        List<Prestamo> resultado = new ArrayList<>();

        for (Prestamo p : BaseDatosSimulada.getPrestamos()) {
            if (p.getEjemplar() != null &&
                    p.getEjemplar().getLibro().getIsbn().equals(isbn)) {
                resultado.add(p);
            }
        }

        if (resultado.isEmpty()) {
            System.out.println("No se encontraron registros de historial para el libro con ISBN " + isbn);
        }

        return resultado;
    }



    /** Muestra por consola todos los historiales registrados en el sistema. */
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

    /** Métodos auxiliares para verificación */
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


