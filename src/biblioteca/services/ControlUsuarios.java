package biblioteca.services;

import biblioteca.data.BaseDatosSimulada;
import biblioteca.entities.usuarios.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ControlUsuarios {

    private List<Usuario> usuarios;

    public ControlUsuarios() {
        this.usuarios = new ArrayList<>();

        // === Bibliotecario por defecto ===
        Bibliotecario admin = new Bibliotecario(
                1,
                "Ana",
                "Pérez",
                "12345678",
                "ana.perez@biblioteca.com",
                "3875000000",
                LocalDate.of(1990, 5, 12),
                TipoUsuario.BIBLIOTECARIO,
                "admin",
                "admin123",
                "B001",
                "Mañana"
        );

        usuarios.add(admin);
    }

    // ========================= REGISTROS =========================

    public void registrarSocio(Socio socio) {
        if (socio == null) {
            System.out.println("Error: el socio no puede ser nulo.");
            return;
        }

        if (buscarPorDni(socio.getDni()) != null || BaseDatosSimulada.buscarSocioPorDni(socio.getDni()) != null) {
            System.out.println("Error: ya existe un socio con el DNI " + socio.getDni());
            return;
        }

        usuarios.add(socio);
        BaseDatosSimulada.agregarSocio(socio);
        System.out.println("Socio registrado exitosamente: " + socio.getNombreCompleto());
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

    public Socio loginSocio(String nombreUsuario, String contrasenia) {
        return BaseDatosSimulada.getSocios().stream()
                .filter(s -> s.getUsuario().equalsIgnoreCase(nombreUsuario)
                        && s.getContrasenia().equals(contrasenia))
                .findFirst()
                .orElse(null);
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

