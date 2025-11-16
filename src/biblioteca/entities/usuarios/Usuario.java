package biblioteca.entities.usuarios;

import java.time.LocalDate;
import java.util.Objects;

public abstract class Usuario {
    private final int id;
    private final String dni;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private LocalDate fechaAlta;
    private final TipoUsuario tipoUsuario;
    private String usuario;
    private String contrasenia;

    public Usuario(int id, String nombre, String apellido, String dni, String email, String telefono,
                   LocalDate fechaAlta, TipoUsuario tipoUsuario, String usuario, String contrasenia) {

        if (id <= 0) throw new IllegalArgumentException("El ID del usuario debe ser positivo.");
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre no puede estar vacío.");
        if (apellido == null || apellido.isBlank()) throw new IllegalArgumentException("El apellido no puede estar vacío.");
        if (dni == null || dni.isBlank()) throw new IllegalArgumentException("El DNI no puede estar vacío.");
        if (usuario == null || usuario.isBlank()) throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
        if (contrasenia == null || contrasenia.isBlank()) throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        if (tipoUsuario == null) throw new IllegalArgumentException("El tipo de usuario no puede ser nulo.");

        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
        this.fechaAlta = (fechaAlta != null) ? fechaAlta : LocalDate.now();
        this.tipoUsuario = tipoUsuario;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
    }

    public Usuario(int id, String dni, TipoUsuario tipoUsuario) {
        this.id = id;
        this.dni = dni;
        this.tipoUsuario = tipoUsuario;
    }

    public boolean validarCredenciales(String usuarioIngresado, String contraseniaIngresada) {
        if (usuarioIngresado == null || contraseniaIngresada == null) return false;
        return Objects.equals(this.usuario, usuarioIngresado) &&
                Objects.equals(this.contrasenia, contraseniaIngresada);
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    // --- Getters ---
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getDni() { return dni; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public TipoUsuario getTipoUsuario() { return tipoUsuario; }
    public String getUsuario() { return usuario; }
    public String getContrasenia() { return contrasenia; }

    public void setId(int id) {
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        if (apellido == null || apellido.isBlank())
            throw new IllegalArgumentException("El apellido no puede estar vacío.");
        this.apellido = apellido;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setUsuario(String usuario) {
        if (usuario == null || usuario.isBlank())
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
        this.usuario = usuario;
    }

    public void setContrasenia(String contrasenia) {
        if (contrasenia == null || contrasenia.isBlank())
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        this.contrasenia = contrasenia;
    }

    // --- Métodos abstractos ---
    public abstract String getTipo();

    @Override
    public String toString() {
        return tipoUsuario + ": " + getNombreCompleto() +
                " | DNI: " + dni +
                " | Email: " + email +
                " | Teléfono: " + telefono +
                " | Alta: " + fechaAlta;
    }

    public abstract void setTipo(TipoUsuario tipoUsuario);
    protected void setFechaAltaInterno(LocalDate fechaAlta) {
        if (fechaAlta != null) this.fechaAlta = fechaAlta;
    }
}

