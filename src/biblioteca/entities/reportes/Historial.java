package biblioteca.entities.reportes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Historial {

    private int id;
    private LocalDate fecha;
    private String tipoOperacion;
    private int idUsuario;
    private int idLibro;
    private int idPrestamo;
    private String detalles;

    // Estructura estática para simular una base de datos en memoria
    private static List<Historial> registros = new ArrayList<>();

    public Historial(int id, LocalDate fecha, String tipoOperacion, int idUsuario, int idLibro, int idPrestamo, String detalles) {
        this.id = id;
        this.fecha = fecha;
        this.tipoOperacion = tipoOperacion;
        this.idUsuario = idUsuario;
        this.idLibro = idLibro;
        this.idPrestamo = idPrestamo;
        this.detalles = detalles;
    }

    public void registrar() {
        registros.add(this);
        System.out.println("Historial registrado: [" + tipoOperacion + "] para usuario ID " + idUsuario);
    }

    public static List<Historial> obtenerPorUsuario(int idUsuario) {
        List<Historial> resultado = new ArrayList<>();
        for (Historial h : registros) {
            if (h.idUsuario == idUsuario) {
                resultado.add(h);
            }
        }
        return resultado;
    }

    public static List<Historial> obtenerPorLibro(int idLibro) {
        List<Historial> resultado = new ArrayList<>();
        for (Historial h : registros) {
            if (h.idLibro == idLibro) {
                resultado.add(h);
            }
        }
        return resultado;
    }

    public static List<Historial> obtenerPorFecha(LocalDate fecha) {
        List<Historial> resultado = new ArrayList<>();
        for (Historial h : registros) {
            if (h.fecha.equals(fecha)) {
                resultado.add(h);
            }
        }
        return resultado;
    }

    public int getId() { return id; }
    public LocalDate getFecha() { return fecha; }
    public String getTipoOperacion() { return tipoOperacion; }
    public int getIdUsuario() { return idUsuario; }
    public int getIdLibro() { return idLibro; }
    public int getIdPrestamo() { return idPrestamo; }
    public String getDetalles() { return detalles; }

    @Override
    public String toString() {
        return "[" + fecha + "] " + tipoOperacion +
                " | Usuario ID: " + idUsuario +
                " | Libro ID: " + idLibro +
                " | Préstamo ID: " + idPrestamo +
                " | Detalles: " + detalles;
    }

}
