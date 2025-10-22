package biblioteca.services;

import biblioteca.entities.usuarios.*;
import java.util.ArrayList;
import java.util.List;

public class ControlUsuarios {

    private List<Usuario> usuarios;

    public ControlUsuarios() {
        this.usuarios = new ArrayList<>();
    }

    // ========================= REGISTROS =========================

    public void registrarSocio(Socio socio) {
        if (socio == null) {
            System.out.println("Error: el socio no puede ser nulo.");
            return;
        }

        if (buscarPorDni(socio.getDni()) != null) {
            System.out.println("Error: ya existe un socio con el DNI " + socio.getDni());
            return;
        }

        usuarios.add(socio);
        System.out.println("Socio registrado exitosamente: " + socio.getNombreCompleto());
    }

    public void registrarBibliotecario(Bibliotecario bibliotecario) {
        if (bibliotecario == null) {
            System.out.println("Error: el bibliotecario no puede ser nulo.");
            return;
        }

        if (buscarPorDni(bibliotecario.getDni()) != null) {
            System.out.println("Error: ya existe un usuario con el DNI " + bibliotecario.getDni());
            return;
        }

        usuarios.add(bibliotecario);
        System.out.println("Bibliotecario registrado: " + bibliotecario.getNombreCompleto());
    }

    // ========================= VALIDACIONES =========================

    public boolean validarCredenciales(String usuario, String contrasenia) {
        if (usuario == null || contrasenia == null) {
            System.out.println("Error: usuario o contraseña vacíos.");
            return false;
        }

        for (Usuario u : usuarios) {
            if (u.validarCredenciales(usuario, contrasenia)) {
                System.out.println("Acceso permitido para: " + u.getNombreCompleto());
                return true;
            }
        }
        System.out.println("Credenciales incorrectas.");
        return false;
    }

    // ========================= OPERACIONES =========================

    public void actualizarDatos(int idUsuario, String nuevoEmail, String nuevoTelefono) {
        Usuario u = buscarUsuario(idUsuario);
        if (u != null) {
            System.out.println("Actualizando datos de: " + u.getNombreCompleto());
            u.setEmail(nuevoEmail);
            u.setTelefono(nuevoTelefono);
            System.out.println("Datos actualizados correctamente.");
        } else {
            System.out.println("No se encontró usuario con ID " + idUsuario);
        }
    }

    // ========================= BÚSQUEDAS =========================

    public Usuario buscarUsuario(int id) {
        for (Usuario u : usuarios) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }

    public Usuario buscarPorDni(String dni) {
        for (Usuario u : usuarios) {
            if (u.getDni().equalsIgnoreCase(dni)) {
                return u;
            }
        }
        return null;
    }

    // ========================= CONSULTAS =========================

    public List<Usuario> obtenerTodos() {
        return new ArrayList<>(usuarios);
    }

    public void mostrarUsuarios() {
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }

        System.out.println("=== LISTADO DE USUARIOS ===");
        for (Usuario u : usuarios) {
            System.out.println(u);
        }
    }
}

