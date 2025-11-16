package biblioteca.services;

import biblioteca.data.dao.SocioDAO;
import biblioteca.data.dao.PoliticaPrestamoDAO;
import biblioteca.data.dao.DAOException;
import biblioteca.entities.prestamos.PoliticaPrestamo;
import biblioteca.entities.usuarios.Socio;

import java.time.LocalDate;

public class ControlPoliticas {

    private PoliticaPrestamo politicaActual;

    private final SocioDAO socioDAO;
    private final PoliticaPrestamoDAO politicaDAO;

    public ControlPoliticas(SocioDAO socioDAO, PoliticaPrestamoDAO politicaDAO) {
        this.socioDAO = socioDAO;
        this.politicaDAO = politicaDAO;
    }

    //   POLÍTICA DE PRÉSTAMO
    public PoliticaPrestamo obtenerPoliticaPrestamo() throws DAOException {
        if (politicaActual != null) return politicaActual;

        if (politicaDAO == null)
            throw new DAOException("PoliticaPrestamoDAO no inicializado.");

        PoliticaPrestamo p = politicaDAO.buscarPorCategoria("General");

        if (p == null)
            throw new DAOException("No existe política vigente en la base de datos (categoría 'General').");

        politicaActual = p;
        return p;
    }

    public int obtenerDiasPrestamo() throws DAOException {
        return obtenerPoliticaPrestamo().getDiasPrestamo();
    }

    public LocalDate calcularFechaDevolucion(LocalDate fechaPrestamo) throws DAOException {
        if (fechaPrestamo == null) fechaPrestamo = LocalDate.now();
        return fechaPrestamo.plusDays(obtenerDiasPrestamo());
    }

    //   VERIFICACIÓN DE LÍMITES
    public boolean verificarLimitePrestamos(Socio socio) throws DAOException {
        if (socio == null) return false;

        // actualizar socio con datos desde BD
        Socio socioBD = socioDAO.buscarPorDni(socio.getDni());
        if (socioBD != null) socio = socioBD;

        if (!"ACTIVO".equalsIgnoreCase(socio.getEstado())) return false;
        if (socio.isTieneSanciones()) return false;
        if (socio.isTieneAtrasos()) return false;

        int prestamosActivos = socio.obtenerPrestamosActivos().size();

        PoliticaPrestamo politica = obtenerPoliticaPrestamo();

        return politica.verificarLimitePrestamos(prestamosActivos);
    }

    //   CÁLCULO DE VENCIMIENTOS
    public LocalDate calcularFechaVencimiento(Socio socio) throws DAOException {
        return LocalDate.now().plusDays(obtenerPoliticaPrestamo().getDiasPrestamo());
    }

    //   ACTUALIZACIÓN MANUAL DE POLÍTICA
    public void establecerNuevaPolitica(PoliticaPrestamo nuevaPolitica) {
        this.politicaActual = nuevaPolitica;
    }
}