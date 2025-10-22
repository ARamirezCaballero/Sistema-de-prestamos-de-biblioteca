package biblioteca.services;

import biblioteca.entities.inventario.Ejemplar;
import biblioteca.entities.inventario.Libro;
import biblioteca.entities.usuarios.Socio;
import biblioteca.entities.usuarios.Usuario;

public class ControlValidaciones {

    // ========================= EJEMPLARES =========================

    public boolean validarDisponibilidadEjemplar(Ejemplar ejemplar) {
        if (ejemplar == null) {
            System.out.println("Error: el ejemplar no existe.");
            return false;
        }

        if (!ejemplar.verificarDisponibilidad()) {
            System.out.println("El ejemplar " + ejemplar.getCodigo() + " no está disponible para préstamo.");
            return false;
        }

        return true;
    }

    // ========================= SOCIOS =========================

    public boolean validarEstadoSocio(Socio socio) {
        if (socio == null) {
            System.out.println("Error: el socio no existe.");
            return false;
        }

        if (!socio.verificarHabilitacion()) {
            System.out.println("El socio " + socio.getNombreCompleto() + " no está habilitado para operaciones.");
            return false;
        }

        return true;
    }

    public boolean verificarSanciones(Socio socio) {
        if (socio == null) {
            System.out.println("Error: socio no válido.");
            return false;
        }

        if (socio.isTieneSanciones()) {
            System.out.println("El socio " + socio.getNombreCompleto() + " tiene sanciones activas.");
            return true;
        }

        return false;
    }

    public boolean verificarAtrasos(Socio socio) {
        if (socio == null) {
            System.out.println("Error: socio no válido.");
            return false;
        }

        if (socio.isTieneAtrasos()) {
            System.out.println("El socio " + socio.getNombreCompleto() + " tiene préstamos vencidos.");
            return true;
        }

        return false;
    }

    // ========================= LIBROS =========================

    public boolean validarDatosLibro(Libro libro) {
        if (libro == null) {
            System.out.println("Error: el objeto libro es nulo.");
            return false;
        }

        boolean valido = true;

        if (libro.getTitulo() == null || libro.getTitulo().isBlank()) {
            System.out.println("Error: el título del libro no puede estar vacío.");
            valido = false;
        }
        if (libro.getAutor() == null || libro.getAutor().isBlank()) {
            System.out.println("Error: el autor del libro no puede estar vacío.");
            valido = false;
        }
        if (libro.getIsbn() == null || libro.getIsbn().isBlank()) {
            System.out.println("Error: el ISBN no puede estar vacío.");
            valido = false;
        }

        return valido;
    }

    // ========================= USUARIOS =========================

    public boolean validarDatosUsuario(Usuario usuario) {
        if (usuario == null) {
            System.out.println("Error: el objeto usuario es nulo.");
            return false;
        }

        boolean valido = true;

        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            System.out.println("Error: el nombre del usuario no puede estar vacío.");
            valido = false;
        }
        if (usuario.getApellido() == null || usuario.getApellido().isBlank()) {
            System.out.println("Error: el apellido del usuario no puede estar vacío.");
            valido = false;
        }
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            System.out.println("Error: el correo electrónico no puede estar vacío.");
            valido = false;
        }

        return valido;
    }
}

