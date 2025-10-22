package biblioteca.entities.reportes;

import biblioteca.entities.prestamos.Prestamo;
import biblioteca.entities.usuarios.Socio;
import biblioteca.entities.inventario.Ejemplar;
import java.time.LocalDate;
import java.util.Objects;

public class Comprobante {

    private int id;
    private LocalDate fechaEmision;
    private String tipo;
    private String contenido;
    private Prestamo prestamo;

    public Comprobante(int id, String tipo, Prestamo prestamo) {
        if (tipo == null || tipo.isEmpty()) {
            throw new IllegalArgumentException("El tipo de comprobante no puede ser nulo o vacío.");
        }

        this.id = id;
        this.tipo = tipo;
        this.prestamo = prestamo;
        this.fechaEmision = LocalDate.now();
        this.contenido = "";
    }

    public void generar() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== COMPROBANTE DE ").append(tipo.toUpperCase()).append(" =====\n");
        sb.append("ID Comprobante: ").append(id).append("\n");
        sb.append("Fecha de emisión: ").append(fechaEmision).append("\n\n");

        if (prestamo != null) {
            sb.append("Préstamo N°: ").append(prestamo.getId()).append("\n");

            Socio socio = prestamo.getSocio();
            if (socio != null) {
                sb.append("Socio: ").append(socio.getNombreCompleto())
                        .append(" | Email: ").append(socio.getEmail()).append("\n");
            } else {
                sb.append("Socio: No disponible\n");
            }

            Ejemplar ejemplar = prestamo.getEjemplar();
            if (ejemplar != null) {
                sb.append("Ejemplar: ").append(ejemplar.getCodigo()).append("\n");
            } else {
                sb.append("Ejemplar: No asignado\n");
            }

            sb.append("Estado actual: ").append(prestamo.getEstado()).append("\n");
            sb.append("Fecha vencimiento: ").append(prestamo.getFechaVencimiento()).append("\n");
        } else {
            sb.append("No hay información del préstamo asociada a este comprobante.\n");
        }

        sb.append("\nGracias por utilizar el sistema de la Biblioteca.\n");
        this.contenido = sb.toString();
    }

    /**
     * Imprime el contenido del comprobante en consola.
     * Si no fue generado, imprime un mensaje de advertencia.
     */
    public void imprimir() {
        if (contenido == null || contenido.isEmpty()) {
            System.out.println("El comprobante aún no fue generado.");
        } else {
            System.out.println(contenido);
        }
    }

    public String obtenerContenido() {
        if (contenido == null || contenido.isEmpty()) {
            return "El comprobante aún no fue generado.";
        }
        return contenido;
    }

    public String prepararEmail() {
        if (prestamo == null || prestamo.getSocio() == null) {
            return "No se pudo enviar el comprobante: falta información del socio.";
        }
        String email = prestamo.getSocio().getEmail();
        return "Enviando comprobante al correo: " + email + "\n" +
                "Asunto: Comprobante de " + tipo + "\n" +
                "Contenido:\n" + contenido;
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public int getId() { return id; }
    public LocalDate getFechaEmision() { return fechaEmision; }
    public String getTipo() { return tipo; }
    public String getContenido() { return contenido; }
    public int getIdPrestamo() { return prestamo != null ? prestamo.getId() : -1; }

    @Override
    public String toString() {
        return "Comprobante #" + id + " (" + tipo + ") - Emitido el " + fechaEmision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comprobante)) return false;
        Comprobante that = (Comprobante) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}



