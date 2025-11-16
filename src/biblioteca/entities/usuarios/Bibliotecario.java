package biblioteca.entities.usuarios;

import java.time.LocalDate;

public class Bibliotecario extends Usuario{

    private String legajo;
    private String turno;

    public Bibliotecario(int id, String nombre, String apellido, String dni, String email, String telefono,
                         LocalDate fecha, TipoUsuario tipoUsuario, String usuario, String contrasenia,
                         String legajo, String turno) {

        super(id, nombre, apellido, dni, email, telefono, fecha, tipoUsuario, usuario, contrasenia);

        if (legajo == null || legajo.isBlank()) {
            throw new IllegalArgumentException("El legajo del bibliotecario no puede estar vacío.");
        }
        if (turno == null || turno.isBlank()) {
            throw new IllegalArgumentException("El turno del bibliotecario no puede estar vacío.");
        }

        this.legajo = legajo;
        this.turno = turno;
    }

    @Override
    public String getTipo() {
        return "Bibliotecario";
    }
    @Override
    public void setTipo(TipoUsuario tipoUsuario) {
        // No hace nada, el tipo siempre es BIBLIOTECARIO
    }


    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        if (legajo == null || legajo.isBlank()) {
            throw new IllegalArgumentException("El legajo no puede ser vacío.");
        }
        this.legajo = legajo;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        if (turno == null || turno.isBlank()) {
            throw new IllegalArgumentException("El turno no puede ser vacío.");
        }
        this.turno = turno;
    }

    @Override
    public String toString() {
        return super.toString() +
                " | Legajo: " + legajo +
                " | Turno: " + turno;
    }
}
