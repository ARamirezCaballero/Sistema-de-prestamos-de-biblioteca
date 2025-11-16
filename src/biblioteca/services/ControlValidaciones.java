package biblioteca.services;

import biblioteca.data.dao.SocioDAO;
import biblioteca.data.dao.DAOException;
import biblioteca.entities.inventario.Ejemplar;
import biblioteca.entities.inventario.Libro;
import biblioteca.entities.usuarios.Socio;
import biblioteca.entities.usuarios.Usuario;

public class ControlValidaciones {

    private final SocioDAO socioDAO;

    // Inyectar SocioDAO por constructor
    public ControlValidaciones(SocioDAO socioDAO) {
        if (socioDAO == null) throw new IllegalArgumentException("SocioDAO no puede ser nulo.");
        this.socioDAO = socioDAO;
    }

    // Validación de ejemplares
    public boolean validarDisponibilidadEjemplar(Ejemplar ejemplar) {
        return ejemplar != null && ejemplar.verificarDisponibilidad();
    }

    // Validación de estado de socios usando SocioDAO inyectado
    public boolean validarEstadoSocio(Socio socio) throws DAOException {
        if (socio == null || socio.getDni() == null) {
            return false;
        }

        Socio socioBD = socioDAO.buscarPorDni(socio.getDni());
        if (socioBD == null) {
            return false;
        }
        return !socioBD.getEstado().equalsIgnoreCase("Inhabilitado");
    }

    public boolean verificarSanciones(Socio socio) {
        if (socio == null) throw new IllegalArgumentException("Socio no válido.");
        return socio.isTieneSanciones();
    }

    public boolean verificarAtrasos(Socio socio) {
        if (socio == null) throw new IllegalArgumentException("Socio no válido.");
        return socio.isTieneAtrasos();
    }

    // Validación de libros
    public boolean validarDatosLibro(Libro libro) {
        if (libro == null) {
            return false;
        }

        if (libro.getTitulo() == null || libro.getTitulo().isBlank()) {
            return false;
        }
        if (libro.getAutor() == null || libro.getAutor().isBlank()) {
            return false;
        }
        if (libro.getIsbn() == null || libro.getIsbn().isBlank()) {
            return false;
        }

        return true;
    }

    // Validación de usuarios
    public boolean validarDatosUsuario(Usuario usuario) {
        if (usuario == null) {
            return false;
        }

        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            return false;
        }
        if (usuario.getApellido() == null || usuario.getApellido().isBlank()) {
            return false;
        }
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            return false;
        }

        return true;
    }
}