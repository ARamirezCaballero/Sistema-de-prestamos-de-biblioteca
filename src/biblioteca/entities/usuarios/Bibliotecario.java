package biblioteca.entities.usuarios;

public class Bibliotecario extends Usuario{

    private String legajo;
    private String turno;

    public Bibliotecario(int id, String nombre, String apellido, String dni, String email, String telefono, java.util.Date fecha, TipoUsuario tipoUsuario, String usuario, String contrasenia, String legajo, String turno) {

        super(id, nombre, apellido, dni, email, telefono, fecha, tipoUsuario, usuario, contrasenia);
        this.legajo = legajo;
        this.turno = turno;
    }

    public String obtenerLegajo() {
        return legajo;
    }

    public String obtenerTurno() {
        return turno;
    }

    @Override
    public String getTipo() {
        return "Bibliotecario";
    }

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    @Override
    public String toString() {
        return super.toString() +
                " | Legajo: " + legajo +
                " | Turno: " + turno;
    }
}
