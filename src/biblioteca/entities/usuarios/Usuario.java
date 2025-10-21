package biblioteca.entities.usuarios;

import java.util.Date;

public abstract class Usuario {
    private int id;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String telefono;
    private Date fecha;
    private TipoUsuario tipoUsuario;
    private String usuario;
    private String contrasenia;


    public Usuario(int id, String nombre, String apellido, String dni, String email, String telefono, Date fecha, TipoUsuario tipoUsuario, String usuario, String contrasenia) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
        this.fecha = fecha;
        this.tipoUsuario = tipoUsuario;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
    }

    public boolean validarCredenciales(String usuarioIngresado, String contraseniaIngresada) {
        return this.usuario.equals(usuarioIngresado) && this.contrasenia.equals(contraseniaIngresada);
    }
    public String obtenerDatos() {
        return "ID: " + id +
                " | Nombre: " + nombre + " " + apellido +
                " | DNI: " + dni +
                " | Email: " + email +
                " | Teléfono: " + telefono +
                " | Tipo: " + tipoUsuario +
                " | Fecha de alta: " + fecha;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getNombreCompleto(){
        return nombre + " " + apellido;
    }

    public String getDni() {
        return dni;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public Date getFecha() {
        return fecha;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    @Override
    public String toString() {
        return tipoUsuario + ": " + nombre + " " + apellido + " (" + email + ") ";
    }

    public abstract String getTipo();
}
