package biblioteca.services;

import biblioteca.data.dao.*;
import biblioteca.entities.inventario.Ejemplar;
import biblioteca.entities.prestamos.Prestamo;
import biblioteca.entities.prestamos.PoliticaPrestamo;
import biblioteca.entities.usuarios.Bibliotecario;
import biblioteca.entities.usuarios.Socio;

import java.time.LocalDate;

public class ControlPrestamos {

    private final PrestamoDAO prestamoDAO;
    private final EjemplarDAO ejemplarDAO;
    private final SocioDAO socioDAO;
    private final ControlPoliticas controlPoliticas;
    private final ControlValidaciones controlValidaciones;
    private final ControlHistorial controlHistorial;

    public ControlPrestamos(
            PrestamoDAO prestamoDAO,
            EjemplarDAO ejemplarDAO,
            SocioDAO socioDAO,
            ControlPoliticas controlPoliticas,
            ControlValidaciones controlValidaciones,
            ControlHistorial controlHistorial
    ) {
        this.prestamoDAO = prestamoDAO;
        this.ejemplarDAO = ejemplarDAO;
        this.socioDAO = socioDAO;
        this.controlPoliticas = controlPoliticas;
        this.controlValidaciones = controlValidaciones;
        this.controlHistorial = controlHistorial;
    }

    // --- Búsquedas con validación de reglas de negocio ---

    public Socio buscarSocio(String dni) throws DAOException {
        Socio socio = socioDAO.buscarPorDni(dni);
        if (socio == null) throw new IllegalArgumentException("No existe un socio con DNI " + dni);
        if (!controlValidaciones.validarEstadoSocio(socio))
            throw new IllegalArgumentException("El socio no está habilitado para préstamos.");
        return socio;
    }

    public Ejemplar buscarEjemplar(String codigo) throws DAOException {
        Ejemplar ejemplar = ejemplarDAO.buscarPorCodigo(codigo);
        if (ejemplar == null) throw new IllegalArgumentException("No existe un ejemplar con código " + codigo);
        if (!controlValidaciones.validarDisponibilidadEjemplar(ejemplar))
            throw new IllegalArgumentException("El ejemplar no está disponible.");
        return ejemplar;
    }

    // --- Lógica de negocio para insertar/actualizar ejemplares en el flujo de préstamo ---

    private void actualizarEstadoEjemplar(Ejemplar ejemplar, String nuevoEstado) throws DAOException {
        ejemplar.setEstado(nuevoEstado);
        ejemplarDAO.actualizar(ejemplar);
    }

    // --- Registrar préstamo centralizando reglas ---

    public Prestamo registrarPrestamo(Socio socio, Ejemplar ejemplar, Bibliotecario bibliotecario) {
        try {
            // 1. Política de préstamo
            PoliticaPrestamo politica = controlPoliticas.obtenerPoliticaPrestamo();
            int dias = politica.getDiasPrestamo();

            // 2. Crear entidad de préstamo
            LocalDate fechaPrestamo = LocalDate.now();
            Prestamo prestamo = Prestamo.crearPrestamo(
                    0, fechaPrestamo, socio, ejemplar, bibliotecario, politica
            );
            prestamo.setDiasPrestamo(dias);

            // 3. Persistir préstamo
            prestamoDAO.insertar(prestamo);

            // 4. Cambiar estado del ejemplar a PRESTADO
            actualizarEstadoEjemplar(ejemplar, "PRESTADO");

            // 5. Registrar en historial si corresponde
            if (controlHistorial != null)
                controlHistorial.registrarPrestamo(prestamo);

            return prestamo;

        } catch (DAOException e) {
            throw new IllegalStateException("Error en BD al registrar el préstamo", e);
        }
    }
}
