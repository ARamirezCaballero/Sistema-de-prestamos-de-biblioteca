package biblioteca.entities.reportes;

import biblioteca.data.BaseDatosSimulada;
import biblioteca.entities.prestamos.Prestamo;
import biblioteca.entities.prestamos.Devolucion;
import biblioteca.entities.usuarios.Socio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Historial {

    private int id;
    private Socio socio;
    private List<Prestamo> prestamos;
    private List<Devolucion> devoluciones;

    public Historial(int id, Socio socio) {
        if (socio == null) {
            throw new IllegalArgumentException("El socio no puede ser nulo.");
        }
        this.id = id;
        this.socio = socio;
        this.prestamos = new ArrayList<>();
        this.devoluciones = new ArrayList<>();
    }

    public void agregarPrestamo(Prestamo prestamo) {
        if (prestamo != null && !prestamos.contains(prestamo)) {
            prestamos.add(prestamo);
        }
    }

    public void agregarDevolucion(Devolucion devolucion) {
        if (devolucion != null) {
            devoluciones.add(devolucion);
        }
    }

    public List<Prestamo> obtenerPrestamos() {
        return new ArrayList<>(prestamos); // retorna copia para evitar manipulación externa
    }

    public String generarResumen() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== HISTORIAL DEL SOCIO =====\n");
        sb.append("Socio: ").append(socio.getNombreCompleto())
                .append(" | Email: ").append(socio.getEmail()).append("\n");

        List<Devolucion> devolucionesGlobales = BaseDatosSimulada.getDevoluciones().stream()
                .filter(d -> d.getPrestamo().getSocio().getId() == socio.getId())
                .toList();

        sb.append("Préstamos registrados: ").append(prestamos.size()).append("\n");
        sb.append("Devoluciones registradas: ").append(devolucionesGlobales.size()).append("\n\n");

        sb.append("DETALLE DE PRÉSTAMOS:\n");
        for (Prestamo p : prestamos) {
            sb.append("Préstamo #").append(p.getId())
                    .append(" | Ejemplar: ").append(p.getEjemplar() != null ? p.getEjemplar().getCodigo() : "N/A")
                    .append(" | Estado: ").append(p.getEstado())
                    .append(" | Fecha vencimiento: ").append(p.getFechaVencimiento()).append("\n");
        }

        sb.append("\nDETALLE DE DEVOLUCIONES:\n");
        for (Devolucion d : devolucionesGlobales) {
            sb.append("Devolución #").append(d.getId())
                    .append(" | Fecha: ").append(d.getFechaDevolucion())
                    .append(" | Estado ejemplar: ").append(d.getEstadoEjemplar())
                    .append(" | Observaciones: ").append(d.getObservaciones()).append("\n");
        }

        return sb.toString();
    }


    public int getId() { return id; }
    public Socio getSocio() { return socio; }
    public List<Devolucion> getDevoluciones() {
        return devoluciones;
    }

    @Override
    public String toString() {
        return "Historial #" + id + " - Socio: " + socio.getNombreCompleto()
                + " | Préstamos: " + prestamos.size()
                + " | Devoluciones: " + devoluciones.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Historial)) return false;
        Historial historial = (Historial) o;
        return id == historial.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

