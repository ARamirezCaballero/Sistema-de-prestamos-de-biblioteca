package biblioteca.entities.usuarios;

public class Bibliotecario extends Usuario{

    private String turno;

    public Bibliotecario(int id, String nombre, String apellido, String email, String turno) {
        super(id, nombre, apellido, email);
        this.turno = turno;
    }

    @Override
    public String getTipo() {
        return "Bibliotecario";
    }

    public String getTurno() {
        return turno;
    }
}
