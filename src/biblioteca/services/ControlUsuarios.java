package biblioteca.services;

import biblioteca.data.dao.BibliotecarioDAO;
import biblioteca.data.dao.DAOException;
import biblioteca.data.dao.SocioDAO;
import biblioteca.entities.usuarios.*;

import java.time.LocalDate;
import java.util.List;

public class ControlUsuarios {

    private final BibliotecarioDAO bibliotecarioDAO;
    private final SocioDAO socioDAO;

    // Inyectar DAOs por constructor. No crear conexiones ni DAOs aquí.
    public ControlUsuarios(BibliotecarioDAO bibliotecarioDAO, SocioDAO socioDAO) {
        if (bibliotecarioDAO == null) throw new IllegalArgumentException("BibliotecarioDAO no puede ser nulo.");
        if (socioDAO == null) throw new IllegalArgumentException("SocioDAO no puede ser nulo.");
        this.bibliotecarioDAO = bibliotecarioDAO;
        this.socioDAO = socioDAO;
    }

    // === Registrar socio ===
    public String registrarSocio(Socio socio) throws DAOException {
        if (socio == null) {
            throw new IllegalArgumentException("El socio no puede ser nulo.");
        }

        if (buscarPorDni(socio.getDni()) != null) {
            throw new IllegalArgumentException("Ya existe un usuario con el DNI " + socio.getDni());
        }

        // --- Completar campos técnicos ---
        // Fecha de alta
        LocalDate fechaAlta = LocalDate.now();
        socio.setFechaAlta(fechaAlta);

        // Número de socio
        String numeroSocio = "SOC-" + System.currentTimeMillis() % 100000;
        socio.setNumeroSocio(numeroSocio);

        // Fecha de vencimiento del carnet (1 año)
        socio.setFechaVencimientoCarnet(fechaAlta.plusYears(1));

        // Estado activo
        socio.setEstado("Activo");

        // Inicializar sanciones y atrasos
        socio.setTieneSanciones(false);
        socio.setTieneAtrasos(false);

        // Tipo de usuario (por si viene vacío)
        if (socio.getTipo() == null) {
            socio.setTipo(TipoUsuario.SOCIO);
        }

        // Persistir en la BD
        socioDAO.insertar(socio);

        return "Socio registrado exitosamente: " + socio.getNombreCompleto() +
                ". Número de socio asignado: " + numeroSocio;
    }

    // === Registrar bibliotecario ===
    public String registrarBibliotecario(Bibliotecario b) throws DAOException {
        if (b == null) {
            throw new IllegalArgumentException("El bibliotecario no puede ser nulo.");
        }

        if (buscarPorDni(b.getDni()) != null) {
            throw new IllegalArgumentException("Ya existe un usuario con el DNI " + b.getDni());
        }

        bibliotecarioDAO.insertar(b);
        return "Bibliotecario registrado exitosamente: " + b.getNombreCompleto();
    }

    public Bibliotecario buscarBibliotecarioPorId(int id) throws DAOException {
        return bibliotecarioDAO.buscarPorId(id);
    }

    // === Buscar usuario por DNI (DAO real para ambos roles) ===
    public Usuario buscarPorDni(String dni) throws DAOException {
        // Buscar bibliotecario en BD
        Bibliotecario bibliotecario = bibliotecarioDAO.buscarPorDni(dni);
        if (bibliotecario != null) return bibliotecario;

        // Buscar socio en BD
        Socio socio = socioDAO.buscarPorDni(dni);
        if (socio != null) return socio;

        return null;
    }

    // === Login (usando DAOs reales) ===
    public Usuario login(String nombreUsuario, String contrasenia) throws DAOException {
        // Bibliotecario desde BD real
        Bibliotecario biblio = bibliotecarioDAO.buscarPorUsername(nombreUsuario);
        if (biblio != null && biblio.getContrasenia().equals(contrasenia)) {
            return biblio;
        }

        // Socio desde BD real
        Socio socio = socioDAO.buscarPorUsername(nombreUsuario);
        if (socio != null && socio.getContrasenia().equals(contrasenia)) {
            return socio;
        }

        return null;
    }

    // === Actualizar datos (solo Socio por ahora) ===
    public String actualizarDatos(int idUsuario, String nuevoEmail, String nuevoTelefono) throws DAOException {
        Socio socio = socioDAO.buscarPorId(idUsuario);
        if (socio == null) {
            throw new IllegalArgumentException("No se encontró socio con ID " + idUsuario);
        }

        socio.setEmail(nuevoEmail);
        socio.setTelefono(nuevoTelefono);
        socioDAO.actualizar(socio);

        return "Datos actualizados correctamente para socio " + socio.getNombreCompleto();
    }

    // === Listar todos los usuarios (ahora desde la BD real) ===
    public List<Usuario> listarTodosLosUsuarios() throws DAOException {
        List<Usuario> usuarios = new java.util.ArrayList<>();

        // Bibliotecarios desde BD real
        List<Bibliotecario> bibliotecarios = bibliotecarioDAO.listarTodos();
        usuarios.addAll(bibliotecarios);

        // Socios desde BD real
        List<Socio> socios = socioDAO.listarTodos();
        usuarios.addAll(socios);

        return usuarios;
    }
}

