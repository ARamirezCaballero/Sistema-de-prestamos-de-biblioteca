package biblioteca.services;

import biblioteca.data.BaseDatosSimulada;
import biblioteca.entities.inventario.Ejemplar;
import biblioteca.entities.inventario.Libro;
import biblioteca.entities.usuarios.Socio;
import biblioteca.entities.usuarios.Usuario;

public class ControlValidaciones {

    // Validación de ejemplares
    public boolean validarDisponibilidadEjemplar(Ejemplar ejemplar) {
        return ejemplar.verificarDisponibilidad(); // Paso 5
    }

    // Validación de estado de socios
    public boolean validarEstadoSocio(Socio socio) {
        return BaseDatosSimulada.verificarEstadoSocio(socio.getDni());
    }

    public boolean verificarSanciones(Socio socio) {
        if (socio == null) throw new IllegalArgumentException("Socio no válido.");
        if (socio.isTieneSanciones()) {
            System.out.println("El socio " + socio.getNombreCompleto() + " tiene sanciones activas.");
            return true;
        }
        return false;
    }

    public boolean verificarAtrasos(Socio socio) {
        if (socio == null) throw new IllegalArgumentException("Socio no válido.");
        if (socio.isTieneAtrasos()) {
            System.out.println("El socio " + socio.getNombreCompleto() + " tiene préstamos vencidos.");
            return true;
        }
        return false;
    }

    // Validación de libros
    public boolean validarDatosLibro(Libro libro) {
        if (libro == null) {
            System.out.println("Error: libro nulo.");
            return false;
        }

        boolean valido = true;
        if (libro.getTitulo() == null || libro.getTitulo().isBlank()) {
            System.out.println("Error: título no puede estar vacío.");
            valido = false;
        }
        if (libro.getAutor() == null || libro.getAutor().isBlank()) {
            System.out.println("Error: autor no puede estar vacío.");
            valido = false;
        }
        if (libro.getIsbn() == null || libro.getIsbn().isBlank()) {
            System.out.println("Error: ISBN no puede estar vacío.");
            valido = false;
        }

        return valido;
    }

    // Validación de usuarios
    public boolean validarDatosUsuario(Usuario usuario) {
        if (usuario == null) {
            System.out.println("Error: usuario nulo.");
            return false;
        }

        boolean valido = true;
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            System.out.println("Error: nombre no puede estar vacío.");
            valido = false;
        }
        if (usuario.getApellido() == null || usuario.getApellido().isBlank()) {
            System.out.println("Error: apellido no puede estar vacío.");
            valido = false;
        }
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            System.out.println("Error: correo electrónico no puede estar vacío.");
            valido = false;
        }

        return valido;
    }
}


