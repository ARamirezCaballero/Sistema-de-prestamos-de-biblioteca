package biblioteca.services;

import biblioteca.data.BaseDatosSimulada;
import biblioteca.entities.inventario.Ejemplar;
import biblioteca.entities.inventario.Libro;
import biblioteca.entities.prestamos.Prestamo;
import biblioteca.entities.prestamos.PoliticaPrestamo;
import biblioteca.entities.usuarios.Socio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de servicio que gestiona el ciclo de vida de los préstamos en la biblioteca.
 */
public class ControlPrestamos {

    private final List<Prestamo> prestamos;
    private PoliticaPrestamo politicaActual;
    private ControlPoliticas controlPoliticas;
    private ControlValidaciones controlValidaciones;
    private ControlHistorial controlHistorial;

    public ControlPrestamos(PoliticaPrestamo politicaInicial, ControlPoliticas controlPoliticas, ControlValidaciones controlValidaciones, ControlHistorial controlHistorial) {
        this.controlHistorial = controlHistorial;
        this.prestamos = new ArrayList<>();
        this.politicaActual = politicaInicial;
        this.controlPoliticas = controlPoliticas;
        this.controlValidaciones = controlValidaciones;
    }


    /**
     * Registra un nuevo préstamo, validando previamente las condiciones del socio
     * y la disponibilidad del ejemplar. Lanza excepciones si alguna regla falla.
     */
    public Prestamo registrarPrestamo(String dniSocio, String codigoEjemplar) {
        // 1. Buscar y validar socio y ejemplar
        Socio socio = BaseDatosSimulada.buscarSocioPorDni(dniSocio);
        if (socio == null)
            throw new IllegalArgumentException("No existe un socio con DNI " + dniSocio);

        Ejemplar ejemplar = BaseDatosSimulada.buscarEjemplarPorCodigo(codigoEjemplar);
        if (ejemplar == null)
            throw new IllegalArgumentException("No existe un ejemplar con código " + codigoEjemplar);

        if (!controlValidaciones.validarEstadoSocio(socio))
            throw new IllegalArgumentException("El socio no está habilitado para préstamos.");
        if (!controlValidaciones.validarDisponibilidadEjemplar(ejemplar))
            throw new IllegalArgumentException("El ejemplar no está disponible para préstamo.");

        Libro libro = ejemplar.getLibro();
        if (libro.contarEjemplaresDisponibles() <= 0)
            throw new IllegalStateException("No hay ejemplares disponibles para " + libro.getTitulo());

        // 2. Calcular política y fechas
        int diasPrestamo = BaseDatosSimulada.obtenerPoliticaDiasPrestamo(dniSocio);
        LocalDate fechaPrestamo = LocalDate.now();
        PoliticaPrestamo politica;
        if (controlPoliticas != null && controlPoliticas.obtenerPoliticaPrestamo() != null) {
            PoliticaPrestamo base = controlPoliticas.obtenerPoliticaPrestamo();
            politica = new PoliticaPrestamo(
                    base.getId(),
                    base.getCategoria(),
                    diasPrestamo,
                    base.getMaxPrestamosSimultaneos(),
                    base.getMultaPorDia()
            );
        } else {
            politica = new PoliticaPrestamo(
                    1,
                    "Estándar",
                    diasPrestamo,
                    BaseDatosSimulada.obtenerMaximoPrestamosSimultaneos(socio.getDni()),
                    BaseDatosSimulada.obtenerMultaPorDia(socio.getDni())
            );
        }

        // 3. Crear el préstamo
        Prestamo prestamo = Prestamo.crearPrestamo(
                generarNuevoIdPrestamo(),
                socio,
                ejemplar,
                politica,
                fechaPrestamo
        );

        // 4. Guardar en BD simulada y actualizar estado del ejemplar
        BaseDatosSimulada.guardarPrestamo(prestamo);
        BaseDatosSimulada.actualizarEstadoEjemplar(codigoEjemplar, "Prestado");

        // 5. Registrar el préstamo en el historial centralizado
        if (controlHistorial != null) {
            controlHistorial.registrarPrestamo(prestamo);
        }

        // 6. Agregar a la lista local de ControlPrestamos
        prestamos.add(prestamo);

        return prestamo;
    }


    // Genera un ID incremental para el préstamo
    private int generarNuevoIdPrestamo() {
        return BaseDatosSimulada.getPrestamos().size() + 1;
    }

    public boolean verificarDisponibilidad(Ejemplar ejemplar) {
        return ejemplar != null && ejemplar.verificarDisponibilidad();
    }

    public LocalDate calcularFechaVencimiento(LocalDate fechaPrestamo) {
        if (fechaPrestamo == null) fechaPrestamo = LocalDate.now();
        if (politicaActual == null) return fechaPrestamo.plusDays(7);
        return politicaActual.calcularFechaDevolucion(fechaPrestamo);
    }

    public boolean validarSocio(Socio socio) {
        if (socio == null) return false;
        if (!socio.verificarHabilitacion()) return false;

        int prestamosActivos = socio.obtenerPrestamosActivos().size();
        return politicaActual.verificarLimitePrestamos(prestamosActivos);
    }

    public List<Prestamo> obtenerPrestamosActivos() {
        List<Prestamo> activos = new ArrayList<>();
        for (Prestamo p : prestamos) {
            if (p != null && ("Activo".equalsIgnoreCase(p.getEstado()) || "Vencido".equalsIgnoreCase(p.getEstado()))) {
                activos.add(p);
            }
        }
        return activos;
    }

    public void mostrarPrestamos() {
        if (prestamos.isEmpty()) {
            System.out.println("No hay préstamos registrados.");
            return;
        }
        System.out.println("=== LISTADO DE PRÉSTAMOS REGISTRADOS ===");
        for (Prestamo p : prestamos) {
            System.out.println(p);
        }
    }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setPoliticaActual(PoliticaPrestamo politicaActual) {
        this.politicaActual = politicaActual;
    }

    public PoliticaPrestamo getPoliticaActual() {
        return politicaActual;
    }
}


